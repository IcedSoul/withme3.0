package cn.icedsoul.groupmessageservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupmessageservice.domain.GroupMessage;
import cn.icedsoul.groupmessageservice.repository.GroupMessageRepository;
import cn.icedsoul.groupmessageservice.service.serviceApi.GroupMessageService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
public class GroupMessageServiceImplement implements GroupMessageService {

    @Autowired
    GroupMessageRepository groupMessageRepository;

    @Override
    public Response addGroupMessage(Integer fromId, Integer groupId, String toId, String content, Integer type, String time) {
        try {
            GroupMessage groupMessage = new GroupMessage();
            groupMessage.setFromId(fromId);
            groupMessage.setGroupId(groupId);
            groupMessage.setToId(toId);
            groupMessage.setContent(content);
            groupMessage.setType(type);
            groupMessage.setTime(Common.getTimeFromString(time));
            groupMessageRepository.save(groupMessage);
            return new Response(1, "Insert group message success", null);
        } catch (Exception e){
            e.printStackTrace();
            return new Response(-1, "Insert group message error", null);
        }
    }


    /**
     *
     * @param userId
     * @param id
     * @param page
     * @param number
     * @return
     */
    @Override
    public Response getMessageRecordBetweenUserAndGroup(Integer userId, Integer id, Integer page, Integer number) {
        try {
            //userCheck
            Pageable sortedByTime = PageRequest.of(page, number, Sort.by("time").descending());
            List<GroupMessage> groupMessages = groupMessageRepository.findAllById(id, sortedByTime);
            Collections.sort(groupMessages);
            //下次尝试直接返回对象看是否能够转为json对象
            return new Response(1, "获取群组聊天记录成功", JSONArray.toJSONString(groupMessages, SerializerFeature.UseSingleQuotes));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取群组聊天记录异常", null);
        }
    }
    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }



}
