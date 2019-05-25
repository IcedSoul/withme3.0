package cn.icedsoul.websocketserverservice.service.impl;

import cn.icedsoul.commonservice.dto.AuthUser;
import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.JwtUtils;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.websocketserverservice.domain.dto.Message;
import cn.icedsoul.websocketserverservice.service.api.MessageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.java.Log;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cn.icedsoul.commonservice.util.Common.isNull;
import static cn.icedsoul.websocketserverservice.constant.CONSTANT.*;
import static cn.icedsoul.websocketserverservice.constant.Global.*;

/**
 * @author IcedSoul
 * @date 19-5-17 上午9:47
 */
@Log
public class MessageServiceImpl implements MessageService {

    /**
     * 不确定是每个用户都分配一个jedis连接比较好还是全部使用一个比较好，暂时采用每个用户新获取一个jedis对象，
     * 以后如果这里出现瓶颈那么再做优化
     *
     * Jedis连接
     */
    AuthUser user = new AuthUser();

    @Override
    public void onOpen(ChannelHandlerContext ctx, FullHttpRequest req) {
        //校验token
        String token = req.getUri().replace("/", "");
        user  = JwtUtils.parseJWT(token);
        if(user == null){
            log.info("用户token校验失败，请重新登陆！");
            return;
        }

        //获取Jedis连接
//        jedis = JedisPoolUtil.getJedis();
//        log.info("[Jedis:] : 获取Jedis连接");
//        //如果有办法在redis初始化时设置key和value，那么这里的判断可以省略*
//        //增加在线人数
//        if(jedis.exists(CONSTANT.ONLINE_COUNT)){
//            jedis.incr(CONSTANT.ONLINE_COUNT);
//        }
//        else {
//            jedis.set(CONSTANT.ONLINE_COUNT, "1");
//        }
//        //改变用户在线状态(存储channel对象（也不知道能不能存）)
//        //更为优雅地存储在线用户（哈希表）
////        this.self = ctx.channel();
//        if(jedis.exists(CONSTANT.ONLINE_LIST)){
//            jedis.hset(CONSTANT.ONLINE_LIST,String.valueOf(user.getUserId()), JSON.toJSONString(ctx.channel()));
//        }
//        else {
//            Map<String, String> onLineList = new HashMap<>();
//            onLineList.put(String.valueOf(user.getUserId()), JSON.toJSONString(ctx.channel()));
//            jedis.hmset(CONSTANT.ONLINE_LIST, onLineList);
//        }
//        log.info("[存储用户channel对象：] " + JSON.toJSONString(ctx.channel()));
        onLineList.put(user.getUserId(), ctx.channel());
        onLineNumber++;
    }

    @Override
    public void onClose() {
        onLineList.remove(user.getUserId());
        onLineNumber--;
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String jsonMessage) {
        log.info("I receive message " + jsonMessage);
        Message message = new Message();
        JSONObject jsonObjectMessage = JSON.parseObject(jsonMessage);
        message.setFromId(jsonObjectMessage.getInteger("from"));
        message.setContent(jsonObjectMessage.getString("content"));
        message.setType(jsonObjectMessage.getInteger("type"));
        message.setTime(Common.getCurrentTime());
        JSONArray users = jsonObjectMessage.getJSONArray("to");
        Channel self = getChannel(message.getFromId());
        if (message.getType() == 0) {
            log.info("I send message to myself " + message.getFromId());
            sendMessage(self, jsonMessage);
        }
        if (message.getType() == 1) {
            message.setToId(users.getInteger(0));
            saveGroupMessage(message, JSONArray.toJSONString(users));
            for (int i = 1; i < users.size(); i++) {
                message.setToId(users.getInteger(i));
                Channel channel = getChannel(message.getToId());
                if(!isNull(channel)){
                    sendMessage(channel, jsonMessage);
                }
                else {
                    //TODO offline group message handle
                }

                log.info("I send message to " + message.getToId());
            }

        }
        else {
            if(users.size() > 0) {
                message.setToId(users.getInteger(0));
                Channel channel = getChannel(message.getToId());
                if(isNull(channel)) {
                    saveMessage(message);
                    sendMessage(channel, jsonMessage);
                }
                else {
                    //TODO offline message handle
                }
                log.info("I send message to " + message.getToId());
            }
        }


    }

    private void sendMessage(Channel channel, String message) {
        channel.write(new TextWebSocketFrame(message));
        channel.flush();
    }

    private Channel getChannel(Integer userId){
        return onLineList.get(userId);
    }

//    public String getMessage(Message message) {
//        //使用JSONObject方法构建Json数据
//        JSONObject jsonObjectMessage = new JSONObject();
//        jsonObjectMessage.put("from", String.valueOf(message.getFromId()));
//        jsonObjectMessage.put("to", new String[]{String.valueOf(message.getToId())});
//        jsonObjectMessage.put("content", String.valueOf(message.getContent()));
//        jsonObjectMessage.put("type", String.valueOf(message.getType()));
//        jsonObjectMessage.put("time", message.getTime().toString());
//        return jsonObjectMessage.toString();
//    }

    private void saveMessage(Message message){
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromId", message.getFromId());
        requestParams.add("toId", message.getToId());
        requestParams.add("content", message.getContent());
        requestParams.add("type", message.getType());
        requestParams.add("time", sdf.format(message.getTime()));

        seneSyncHttpRequest(MESSAGE_BASE, ADD_MESSAGE, requestParams);

//        Mono<Response> response = WebClient.create(MESSAGE_BASE).post()
//                .uri(ADD_MESSAGE)
////                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .syncBody(requestParams)
//                .retrieve()
//                .bodyToMono(Response.class);
//        Response res = response.block();
//        assert res != null;
//        log.info("I save Message" + res.toString());
    }

    private void saveGroupMessage(Message message, String toId){
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromId", message.getFromId());
        requestParams.add("groupId", message.getToId());
        requestParams.add("toId", toId);
        requestParams.add("content", message.getContent());
        requestParams.add("type", message.getType());
        requestParams.add("time", sdf.format(message.getTime()));

        seneSyncHttpRequest(GROUP_MESSAGE_BASE, ADD_GROUP_MESSAGE, requestParams);

    }

    private void seneSyncHttpRequest(String baseURL, String path, MultiValueMap requestParams){
        Mono<Response> response = WebClient.create(baseURL).post()
                .uri(path)
                .syncBody(requestParams)
                .retrieve()
                .bodyToMono(Response.class);
        Response res = response.block();
        assert res != null;
        log.info("I save Message" + res.toString());
    }

}
