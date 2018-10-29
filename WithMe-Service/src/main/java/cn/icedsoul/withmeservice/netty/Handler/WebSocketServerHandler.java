package cn.icedsoul.withmeservice.netty.Handler;

import cn.icedsoul.withmeservice.repository.MessageRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.icedsoul.withmeservice.domain.AuthUser;
import cn.icedsoul.withmeservice.domain.Message;
import cn.icedsoul.withmeservice.domain.User;
import cn.icedsoul.withmeservice.netty.Config.NettyConfig;
import cn.icedsoul.withmeservice.repository.UserRepository;
import cn.icedsoul.withmeservice.utils.CONSTANT;
import cn.icedsoul.withmeservice.utils.Common;
import cn.icedsoul.withmeservice.utils.JwtUtils;
import cn.icedsoul.withmeservice.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;

import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by 14437 on 2018/2/10.
 */
@Log

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {


    private MessageRepository messageRepository = SpringUtils.getBean(MessageRepository.class);
    private UserRepository userRepository = SpringUtils.getBean(UserRepository.class);
    private WebSocketServerHandshaker handShaker;
    private static Integer onLineCount = 0;

    private AuthUser user = new AuthUser();
    /**
     * 处理接收到的消息
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
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //Http解码失败，返回Http异常
        if(!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        //校验token
        String token = req.getUri();
        token = token.replace("/","");
        user = JwtUtils.parseJWT(token);
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
        onOpen(ctx,user);
    }

    /**
     * 处理WebSocket消息
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        //判断是否是关闭连接的指令
        if (frame instanceof CloseWebSocketFrame) {
            onClose();
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
        onMessage(jsonMessage);
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location =  req.headers().get(HttpHeaders.Names.HOST) + CONSTANT.WEBSOCKET_PATH;
        return "ws://" + location;
    }

    /**
     * 给客户端回复消息
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

    private void onOpen(ChannelHandlerContext ctx,AuthUser user){
        User users = userRepository.findByUserId(user.getUserId());
        users.setUserIsOnline(1);
        userRepository.save(users);
        onLineCount++;
        NettyConfig.onLineList.put(user.getUserId(),ctx.channel());
        checkOffLineMessage(ctx,user);
    }

    private void checkOffLineMessage(ChannelHandlerContext ctx,AuthUser user) {
        List<Message> messages = messageRepository.findAllByToIdAndIsTransport(user.getUserId(),0);
        for(Message message:messages){
            sendMessage(ctx.channel(),getMessage(message));
            message.setIsTransport(1);
            messageRepository.save(message);
        }
    }

    private void onClose() {
        onLineCount--;
        User users = userRepository.findByUserId(user.getUserId());
        users.setUserIsOnline(0);
        userRepository.save(users);
        NettyConfig.onLineList.remove(user.getUserId());
    }

    private void onMessage(String jsonMessage) {
        Message message = new Message();
        JSONObject jsonObjectMessage = JSON.parseObject(jsonMessage);
        message.setFromId(jsonObjectMessage.getInteger("from"));
        message.setContent(jsonObjectMessage.getString("content"));
        message.setType(jsonObjectMessage.getInteger("type"));
        message.setTime(Common.getCurrentTime());
        JSONArray users =  jsonObjectMessage.getJSONArray("to");
        if(jsonObjectMessage.getInteger("type") == 0){
            sendMessage(NettyConfig.onLineList.get(message.getFromId()),jsonMessage);
        }
        if(jsonObjectMessage.getIntValue("type") == 1){
            message.setToId(users.getInteger(0));
            message.setType(2);
            message.setIsTransport(1);
            messageRepository.save(message);
            message.setType(1);
        }
        for(int i = 0;i < users.size();i++){
            if(!(jsonObjectMessage.getIntValue("type") == 1 && i==0)){
                Integer toUser = users.getInteger(i);
                message.setToId(toUser);
                Channel channel = NettyConfig.onLineList.get(toUser);
                if(!Common.isNull(channel)){
                    sendMessage(channel,jsonMessage);
                    message.setIsTransport(1);
                    messageRepository.save(message);
                }
                else{
                    message.setIsTransport(0);
                    if(message.getType()!=3 && message.getType()!=4)
                        messageRepository.save(message);
                }
            }
        }
    }

    private void sendMessage(Channel channel, String message){
        channel.write(new TextWebSocketFrame(message));
        channel.flush();
    }

    public String getMessage(Message message){
        //使用JSONObject方法构建Json数据
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("from", String.valueOf(message.getFromId()));
        jsonObjectMessage.put("to", new String[] {String.valueOf(message.getToId())});
        jsonObjectMessage.put("content", String.valueOf(message.getContent()));
        jsonObjectMessage.put("type", String.valueOf(message.getType()));
        jsonObjectMessage.put("time", message.getTime().toString());
        return jsonObjectMessage.toString();
    }
}
