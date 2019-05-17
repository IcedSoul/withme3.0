package cn.icedsoul.messageservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.messageservice.domain.Message;
import cn.icedsoul.messageservice.repository.MessageRepository;
import cn.icedsoul.messageservice.service.serviceApi.MessageService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageServiceImplement implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Response addMessage(Integer fromId, Integer toId, String content, Integer type, String time) {
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setType(type);
            message.setTime(Common.getTimeFromString(time));
            messageRepository.save(message);
            return new Response(1, "Insert message success", null);
        } catch (Exception e){
            e.printStackTrace();
            return new Response(-1, "Insert message error", null);
        }
    }

    @Override
    public Response getMessageRecordBetweenUsers(Integer userIdA, Integer userIdB) {
        try {
            List<Message> messages = messageRepository.findAllByFromIdAndToId(userIdA, userIdB);
            messages.addAll(messageRepository.findAllByFromIdAndToId(userIdB, userIdA));
            return new Response(1, "Get message record between users success", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Get message record between users error", null);
        }
    }

    @Override
    public Response addMessages(List<Message> messages) {
        try {
            for(Message message : messages){
                messageRepository.save(message);
            }
            return new Response(1, "add messages success", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "add message fail", null);
        }
    }

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }
//    /**
//     * 这个也暂时保留吧，不一定用的上，因为把下最好不把未转发的消息存到消息表，最好加离线消息表，方便查询
//     * @param id
//     * @param isTransport
//     * @return
//     */
//    @Override
//    public Response updateMessage(Integer id, Integer isTransport) {
//        try {
//            Message message = messageRepository.getOne(id);
//            message.setIsTransport(isTransport);
//            messageRepository.save(message);
//            return new Response(1, "Update message success",null);
//        } catch (Exception e){
//            e.printStackTrace();
//            return new Response(1, "Update message error",null);
//        }
//    }



//
//    /**
//     * 暂时保留
//     * @param jsonObj
//     * @return
//     */
//
//    @Override
//    public Response getMessageRecordBetweenUserAndGroup(String jsonObj) {
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
//            Integer id = jsonObject.getInteger("id");
//            Integer userId = jsonObject.getInteger("userId");
//            List<Message> messages = messageRepository.findAllByToIdAndTypeAndIsTransport(id, 2, 1);
//            for (Message message : messages) {
//                message.setType(1);
//                message.setToId(userId);
//            }
//            return new Response(1, "获取群组聊天记录成功", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Response(-1, "获取群组聊天记录异常", null);
//        }
//    }

}
