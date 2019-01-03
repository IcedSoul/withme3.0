package cn.icedsoul.withmeservice.service.ServiceImplement;


import cn.icedsoul.withmeservice.domain.Message;
import cn.icedsoul.withmeservice.repository.MessageRepository;
import cn.icedsoul.withmeservice.utils.Response;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import cn.icedsoul.withmeservice.service.ServiceAPI.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageServiceImplement implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Response getMessageRecordBetweenUsers(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            Integer userIdA = jsonObject.getInteger("userIdA");
            Integer userIdB = jsonObject.getInteger("userIdB");
            List<Message> messages = messageRepository.findAllByFromIdAndToIdAndIsTransport(userIdA,userIdB,1);
            messages.addAll(messageRepository.findAllByFromIdAndToIdAndIsTransport(userIdB,userIdA,1));
            return new Response(1,"获取好友聊天记录成功", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取好友聊天记录异常",null);
        }
    }

    @Override
    public Response getMessageRecordBetweenUserAndGroup(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            Integer id = jsonObject.getInteger("id");
            Integer userId = jsonObject.getInteger("userId");
            List<Message> messages = messageRepository.findAllByToIdAndTypeAndIsTransport(id,2,1);
            for(Message message:messages){
                message.setType(1);
                message.setToId(userId);
            }
            return new Response(1,"获取群组聊天记录成功",JSONArray.toJSONString(messages,SerializerFeature.UseSingleQuotes));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取群组聊天记录异常",null);
        }
    }

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;
    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }
}
