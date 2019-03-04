package cn.icedsoul.offlinemessageservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.offlinemessageservice.domain.OfflineGroupMessage;
import cn.icedsoul.offlinemessageservice.repository.OfflineGroupMessageRepository;
import cn.icedsoul.offlinemessageservice.service.serviceApi.OfflineGroupMessageService;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class OfflineGroupMessageServiceImplement implements OfflineGroupMessageService{

    @Autowired
    OfflineGroupMessageRepository offlineGroupMessageRepository;


    @Override
    public Response addOfflineGroupMessage(Integer fromId, Integer groupId, Integer toId, String content, Integer type, String time) {
        try {
            OfflineGroupMessage offlineGroupMessage = new OfflineGroupMessage();
            offlineGroupMessage.setFromId(fromId);
            offlineGroupMessage.setGroupId(groupId);
            offlineGroupMessage.setToId(toId);
            offlineGroupMessage.setContent(content);
            offlineGroupMessage.setType(type);
            offlineGroupMessage.setTime(Common.getTimeFromString(time));
            offlineGroupMessageRepository.save(offlineGroupMessage);
            return new Response(1, "Insert offline group message success", null);
        } catch (Exception e){
            e.printStackTrace();
            return new Response(-1, "Insert offline group message error", null);
        }
    }


    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }
}
