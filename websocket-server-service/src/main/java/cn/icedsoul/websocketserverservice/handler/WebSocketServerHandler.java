package cn.icedsoul.websocketserverservice.handler;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.messageservice.domain.Message;
import cn.icedsoul.websocketserverservice.domain.AuthUser;
import cn.icedsoul.websocketserverservice.jedis.JedisPoolUtil;
import cn.icedsoul.websocketserverservice.util.CONSTANT;
import cn.icedsoul.websocketserverservice.util.JwtUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by 14437 on 2018/2/10.
 */
@Log

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handShaker;

    private AuthUser user = new AuthUser();

    /**
     * 不确定是每个用户都分配一个jedis连接比较好还是全部使用一个比较好，暂时采用每个用户新获取一个jedis对象，
     * 以后如果这里出现瓶颈那么再做优化
     */
    //Jedis连接
    private Jedis jedis;

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
        //校验token
        String token = req.getUri();
        token = token.replace("/", "");
        user = JwtUtils.parseJWT(token);
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
        onOpen(ctx);
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

    private void onOpen(ChannelHandlerContext ctx) {
        //获取Jedis连接
        this.jedis = JedisPoolUtil.getJedis();
        log.info("[Jedis:] : 获取Jedis连接");
        //如果有办法在redis初始化时设置key和value，那么这里的判断可以省略*
        //增加在线人数
        if(jedis.exists(CONSTANT.ON_LINE_COUNT)){
            jedis.incr(CONSTANT.ON_LINE_COUNT);
        }
        else {
            jedis.set(CONSTANT.ON_LINE_COUNT, "1");
        }
        //改变用户在线状态(存储channel对象（也不知道能不能存）)
        //更为优雅地存储在线用户（哈希表）
        if(jedis.exists(CONSTANT.ON_LINE_LIST)){
            jedis.hset(CONSTANT.ON_LINE_LIST,String.valueOf(user.getUserId()), JSONObject.toJSONString(ctx.channel()));
        }
        else {
            Map<String, String> onLineList = new HashMap<>();
            onLineList.put(String.valueOf(user.getUserId()), JSONObject.toJSONString(ctx.channel()));
            jedis.hmset(CONSTANT.ON_LINE_LIST, onLineList);
        }
        log.info("[存储用户channel对象：] " + JSONObject.toJSONString(ctx.channel()));
//        NettyConfig.onLineList.put(user.getUserId(), ctx.channel());
        //检查离线消息应该需要再添加一张离线消息表来做（离线消息表配合缓存？），目前先取消这个功能。
//        checkOffLineMessage(ctx, user);
    }

//    private void checkOffLineMessage(ChannelHandlerContext ctx, AuthUser user) {
//        List<Message> messages = messageRepository.findAllByToIdAndIsTransport(user.getUserId(), 0);
//        for (Message message : messages) {
//            sendMessage(ctx.channel(), getMessage(message));
//            message.setIsTransport(1);
//            messageRepository.save(message);
//        }
//    }

    private void onClose() {
        jedis.decr(CONSTANT.ON_LINE_COUNT);
        jedis.hdel(CONSTANT.ON_LINE_LIST, String.valueOf(user.getUserId()));
        this.jedis.close();
        log.info("[Jedis:] : 返回Jedis连接");

//        NettyConfig.onLineList.remove(user.getUserId());
    }

    /**
     * 解析并且处理消息
     * @param jsonMessage
     */
    private void onMessage(String jsonMessage) {
        Message message = new Message();
        JSONObject jsonObjectMessage = JSON.parseObject(jsonMessage);
        message.setFromId(jsonObjectMessage.getInteger("from"));
        message.setContent(jsonObjectMessage.getString("content"));
        message.setType(jsonObjectMessage.getInteger("type"));
        message.setTime(Common.getCurrentTime());
        JSONArray users = jsonObjectMessage.getJSONArray("to");
        //type为0证明自己也需要，那么给自己也发一份（逻辑应该优化）
        if (jsonObjectMessage.getInteger("type") == 0) {
            Channel self = getChannel(message.getFromId());
            sendMessage(self, jsonMessage);
        }
        if (jsonObjectMessage.getIntValue("type") == 1) {
            message.setToId(users.getInteger(0));
            message.setType(2);
            message.setIsTransport(1);
            //为了测试，消息暂时存到redis中吧
//            messageRepository.save(message);
            jedis.lpush(CONSTANT.MESSAGE, JSON.toJSONString(message));
            message.setType(1);
        }
        for (int i = 0; i < users.size(); i++) {
            if (!(jsonObjectMessage.getIntValue("type") == 1 && i == 0)) {
                Integer toUser = users.getInteger(i);
                message.setToId(toUser);
                Channel channel = getChannel(toUser);
                if (!Common.isNull(channel)) {
                    sendMessage(channel, jsonMessage);
                    message.setIsTransport(1);
                    //messageRepository.save(message);
                    jedis.lpush(CONSTANT.MESSAGE, JSON.toJSONString(message));
                } else {
                    message.setIsTransport(0);
                    if (message.getType() != 3 && message.getType() != 4) {
                        jedis.lpush(CONSTANT.MESSAGE, JSON.toJSONString(message));
//                        messageRepository.save(message);
                    }

                }
            }
        }
    }

    private void sendMessage(Channel channel, String message) {
        channel.write(new TextWebSocketFrame(message));
        channel.flush();
    }

    public String getMessage(Message message) {
        //使用JSONObject方法构建Json数据
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("from", String.valueOf(message.getFromId()));
        jsonObjectMessage.put("to", new String[]{String.valueOf(message.getToId())});
        jsonObjectMessage.put("content", String.valueOf(message.getContent()));
        jsonObjectMessage.put("type", String.valueOf(message.getType()));
        jsonObjectMessage.put("time", message.getTime().toString());
        return jsonObjectMessage.toString();
    }

    private Channel getChannel(Integer userId){
        return (Channel) JSON.parse(jedis.hget(CONSTANT.ON_LINE_LIST, String.valueOf(userId)));
    }
}
