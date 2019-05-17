package cn.icedsoul.websocketserverservice.handler;

import cn.icedsoul.websocketserverservice.constant.CONSTANT;
import cn.icedsoul.websocketserverservice.service.api.MessageService;
import cn.icedsoul.websocketserverservice.service.impl.MessageServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 2019.05.13
 * 今天证明了Channel序列化之后存至Redis的方法是不可取的，序列化之后无法使用其发送消息。
 * 看了网上的一些方法，目前认为比较有效的解决方法
 * 1. 使用MQ，发送消息时使用MQ向Redis集群发送消息，Redis收到消息时如果有ID进行消息转发，否则忽略。可以不共享Channel。
 * 2. 使用sessionId关联channel，共享sessionId，通过sessionId获取channel.
 *
 *
 * 最终决定采用分布式session来解决这个问题，MQ每个消息都发给所有节点这个方法代价有些高了，到时候动态扩展MQ会成为瓶颈。目前暂时先不使用MQ吧，
 * 先搭起来分布式Session，然后再尝试与Channel绑定，完成消息的发送。
 * @author icedsoul
 * Created by icedsoul on 2018/2/10.
 */
@Log
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handShaker;

    private MessageService messageService;

    public WebSocketServerHandler(){
        this.messageService = new MessageServiceImpl();
    }

    /**
     * 处理接收到的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            log.info("========= Http请求接入=========");
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            log.info("========= WebSocket请求接入=========");
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理Http请求
     *
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //Http解码失败，返回Http异常
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
        messageService.onOpen(ctx, req);
    }

    /**
     * 处理WebSocket消息
     *
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        //判断是否是关闭连接的指令
        if (frame instanceof CloseWebSocketFrame) {
            messageService.onClose();
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        //判断是否为ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //判断是否为文本消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("======= 仅支持文本消息，不支持二进制消息 ========");
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }

        String jsonMessage = ((TextWebSocketFrame) frame).text();
        messageService.onMessage(ctx, jsonMessage);
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaders.Names.HOST) + CONSTANT.WEBSOCKET_PATH;
        return "ws://" + location;
    }

    /**
     * 给客户端回复消息
     *
     * @param ctx
     * @param req
     * @param res
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {

        //返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
