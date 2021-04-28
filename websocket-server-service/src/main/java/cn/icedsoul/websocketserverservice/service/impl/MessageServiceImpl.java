package cn.icedsoul.websocketserverservice.service.impl;

import cn.icedsoul.commonservice.dto.AuthUser;
import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.JwtUtils;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.websocketserverservice.common.base.Tuple;
import cn.icedsoul.websocketserverservice.domain.dto.Message;
import cn.icedsoul.websocketserverservice.domain.dto.ScriptNode;
import cn.icedsoul.websocketserverservice.service.api.MessageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.icedsoul.commonservice.util.Common.isNull;
import static cn.icedsoul.websocketserverservice.constant.CONSTANT.*;
import static cn.icedsoul.websocketserverservice.constant.Global.*;

/**
 * @author IcedSoul
 * @date 19-5-17 上午9:47
 */
@Slf4j
public class MessageServiceImpl implements MessageService {

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
        onLineList.put(user.getUserId(), ctx.channel());
        onLineNumber++;

        initUser(user.getUserId());
        robotHandleMessage(user.getUserId(), null);
    }


    @Override
    public void onClose() {
        onLineList.remove(user.getUserId());
        onLineNumber--;
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String jsonMessage) {
        log.info("send message, {}", jsonMessage);
        Message message = new Message();
        JSONObject jsonObjectMessage = JSON.parseObject(jsonMessage);
        message.setFromId(jsonObjectMessage.getInteger("from"));
        message.setContent(jsonObjectMessage.getString("content"));
        message.setType(jsonObjectMessage.getInteger("type"));
        message.setTime(Common.getCurrentTime());
        JSONArray users = jsonObjectMessage.getJSONArray("to");
        Channel self = getChannel(message.getFromId());
        if (message.getType() == 0) {
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
                    message.setFromId(users.getInteger(0));
                    message.setContent("1");
                    message.setType(7);
                    saveMessage(message, OFFLINE_MESSAGE_BASE, ADD_OFFLINE_MESSAGE);
                }
            }

        }
        else {
            robotHandleMessage(message.getFromId(), message);
        }


    }

    /**
     * 机器人处理会话
     * @param userId 用户ID
     * @param message 消息
     */
    private void robotHandleMessage(Integer userId, Message message) {
        try {
            ScriptNode scriptNode = onlineUserStatus.getOrDefault(userId, null);
            if(Objects.isNull(scriptNode)){
                initUser(userId);
                throw new Exception("用户状态异常");
            }
            log.info("robot start handle message. currentNode: {}", scriptNode.toString());
            switch (scriptNode.getType().getKey()) {
                case ScriptNode.NARRATIVE:
                    log.info("旁白:");
                    //发送旁白消息
                    sendMessageToUserFromRobot(userId, scriptNode.getContents().get(0).getFirst());
                    //旁白消息发送完自动更新节点状态
                    onlineUserStatus.put(userId, gameMap.get(scriptNode.getContents().get(0).getSecond()));
                    robotHandleMessage(userId, null);
                    break;
                case ScriptNode.START:
                    log.info("开始:");
                    //开始状态修改为choice文件才有，为选择其它文件的形态
                    //message为null说明是从其它状态跳转过来
                    if(Objects.isNull(message)){
                        sendMessagesToUserFromRobotByContents(userId, scriptNode.getContents());
                        sendMessageToUserFromRobot(userId, CHOICE_PARSE_EXCEPTION_NOTICE);
                    }
                    else {
                        try {
                            int choice = Integer.parseInt(message.getContent());
                            if(choice <= 0 || choice > scriptNode.getContents().size()){
                                throw new Exception("选项超出正常范围");
                            }
                            //切换到对应角色的状态
                            onlineUserStatus.put(userId, gameMap.get(ROLE_CHOICE_ARRAY[choice]));
                        } catch (Exception e){
                            log.info("选择解析错误");
                            sendMessageToUserFromRobot(userId, CHOICE_PARSE_EXCEPTION_NOTICE);
                        }
                        robotHandleMessage(userId, null);
                    }
                    break;
                case ScriptNode.CHOICE:
                    log.info("选择:");
                    if(Objects.isNull(message)){
                        log.info("提供选项:");
                        sendMessagesToUserFromRobotByContents(userId, scriptNode.getContents());
                        sendMessageToUserFromRobot(userId, CHOICE_PARSE_EXCEPTION_NOTICE);
                    }
                    else {
                        try {
                            int choice = Integer.parseInt(message.getContent());
                            if(choice < 0 || choice >= scriptNode.getContents().size()){
                                throw new Exception("选项超出正常范围");
                            }
                            onlineUserStatus.put(userId, gameMap.get(scriptNode.getContents().get(choice).getSecond()));
                        } catch (Exception e){
                            log.info("选择解析错误");
                            sendMessageToUserFromRobot(userId, CHOICE_PARSE_EXCEPTION_NOTICE);
                        }
                        robotHandleMessage(userId, null);
                    }
                    break;
                case ScriptNode.END:
                    log.info("结束:");
                    sendMessageToUserFromRobot(userId, scriptNode.getContents().get(0).getFirst());
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e){
            log.info("机器人处理消息出现异常", e);
        }


    }


    private void sendMessage(Channel channel, String message) {
        log.info("send message {}", message);
        channel.write(new TextWebSocketFrame(message));
        channel.flush();
    }

    private Channel getChannel(Integer userId){
        return onLineList.get(userId);
    }

    private void saveMessage(Message message, String baseUrl, String path){
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromId", message.getFromId());
        requestParams.add("toId", message.getToId());
        requestParams.add("content", message.getContent());
        requestParams.add("type", message.getType());
        requestParams.add("time", sdf.format(message.getTime()));
        sendSyncHttpPostRequest(baseUrl, path, requestParams);
    }

    private void saveGroupMessage(Message message, String toId){
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("fromId", message.getFromId());
        requestParams.add("groupId", message.getToId());
        requestParams.add("toId", toId);
        requestParams.add("content", message.getContent());
        requestParams.add("type", message.getType());
        requestParams.add("time", sdf.format(message.getTime()));
        sendSyncHttpPostRequest(GROUP_MESSAGE_BASE, ADD_GROUP_MESSAGE, requestParams);
    }

    /**
     * 发送POST请求
     * @param baseUrl 基本URL
     * @param path 方法路径
     * @param requestParams 请求参数
     * @return 返回参数
     */
    private Response sendSyncHttpPostRequest(String baseUrl, String path, MultiValueMap<?, ?> requestParams){
        Mono<Response> response = WebClient.create(baseUrl).post()
                .uri(path)
                .syncBody(requestParams)
                .retrieve()
                .bodyToMono(Response.class);
        return response.block();
    }

    /**
     * 发送GET请求
     * @param baseUrl 基本URL
     * @param path 方法路径
     * @return 返回参数
     */
    private Response sendSyncHttpGetRequest(String baseUrl, String path){
        Mono<Response> response = WebClient.create(baseUrl).get()
                .uri(path)
                .retrieve()
                .bodyToMono(Response.class);
        return response.block();
    }

    /**
     * 对新登陆的用户进行初始化操作，设置初登陆的用户状态，初始化机器人ID
     * @param userId 用户ID
     */
    private void initUser(Integer userId) {
        onlineUserStatus.put(userId, gameMap.get(GAME_START));
        log.info("userId, {}, {}", userId, gameMap.get(GAME_START).toString());
        if(Objects.isNull(robotId)) {
            Response response = sendSyncHttpGetRequest(USER_SERVICE_BASE, GET_USER_BY_NAME + ROBOT_NAME);
            if (!Objects.isNull(response) && !Objects.isNull(response.getContent())) {
                JSONObject jsonObjectMessage = JSON.parseObject((String)response.getContent());
                robotId = jsonObjectMessage.getInteger(USER_ID);
            }
        }
    }

    private void sendMessagesToUserFromRobotByContents(Integer userId, List<Tuple<String, String>> contents){
        for (int i = 0; i < contents.size(); i++) {
            sendMessageToUserFromRobot(userId, ((i + 1) + SPLIT_COMMA + contents.get(i).getFirst()));
        }
    }

    /**
     * 机器人向指定用户发送消息
     * @param userId 用户ID
     * @param messageContent 消息内容
     */
    private void sendMessageToUserFromRobot(Integer userId, String messageContent){
        String[] messageContents = messageContent.split(MESSAGE_CONTENT_SPLIT);
        for (String singleMessageContent : messageContents) {
            Message message = new Message();
            message.setFromId(robotId);
            message.setToId(userId);
            message.setContent(singleMessageContent);
            message.setType(0);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            message.setTime(now);
            //保存消息
            saveMessage(message, MESSAGE_BASE, ADD_MESSAGE);
            sendMessage(onLineList.get(userId), getMessage(message));
        }
    }

    /**
     * 构建Json数据
     * @param message Message对象
     * @return Json字符串
     */
    public String getMessage(Message message) {
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("from", String.valueOf(message.getFromId()));
        jsonObjectMessage.put("to", new String[]{String.valueOf(message.getToId())});
        jsonObjectMessage.put("content", String.valueOf(message.getContent()));
        jsonObjectMessage.put("type", String.valueOf(message.getType()));
        jsonObjectMessage.put("time", message.getTime().toString());
        return jsonObjectMessage.toString();
    }
}
