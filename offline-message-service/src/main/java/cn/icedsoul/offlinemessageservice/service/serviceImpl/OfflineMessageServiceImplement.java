package cn.icedsoul.offlinemessageservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.offlinemessageservice.constant.CONSTANT;
import cn.icedsoul.offlinemessageservice.domain.OfflineGroupMessage;
import cn.icedsoul.offlinemessageservice.domain.OfflineMessage;
import cn.icedsoul.offlinemessageservice.repository.OfflineGroupMessageRepository;
import cn.icedsoul.offlinemessageservice.repository.OfflineMessageRepository;
import cn.icedsoul.offlinemessageservice.service.serviceApi.OfflineMessageService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OfflineMessageServiceImplement implements OfflineMessageService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OfflineGroupMessageRepository offlineGroupMessageRepository;

    @Autowired
    OfflineMessageRepository offlineMessageRepository;

    @Override
    public Response addOfflineMessage(Integer fromId, Integer toId, String content, Integer type, String time) {
        try {
            OfflineMessage offlineMessage = new OfflineMessage();
            offlineMessage.setFromId(fromId);
            offlineMessage.setToId(toId);
            offlineMessage.setContent(content);
            offlineMessage.setType(type);
            offlineMessage.setTime(Common.getTimeFromString(time));
            offlineMessageRepository.save(offlineMessage);
            return new Response(1, "Insert offline message success", null);
        } catch (Exception e){
            e.printStackTrace();
            return new Response(-1, "Insert offline message error", null);
        }
    }

    @Override
    @Transactional
    public Response getOfflineMessageTo(Integer userId) {
        try {
            List<OfflineMessage> messages = offlineMessageRepository.findAllByToId(userId);

            offlineMessageRepository.deleteAllByToId(userId);
//            Response response = restTemplate.postForEntity(CONSTANT.MESSAGE_SERVICE_ADD_MESSAGES, messages, Response.class).getBody();
//            if(response.getStatus() != 1){
//                throw new Exception();
//            }

            List<OfflineGroupMessage> groupMessages = offlineGroupMessageRepository.findAllByToId(userId);
            for(OfflineGroupMessage offlineGroupMessage : groupMessages){
                messages.add(new OfflineMessage(offlineGroupMessage));
            }
            offlineMessageRepository.deleteAllByToId(userId);
            return new Response(1, "Get offline messages success", JSONArray.toJSONString(messages, SerializerFeature.UseSingleQuotes));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Get offline messages error", null);
        }
    }

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

//    @Override
//    public Response getOfflineMessageFrom(Integer userId) {
//        return null;
//    }
}
