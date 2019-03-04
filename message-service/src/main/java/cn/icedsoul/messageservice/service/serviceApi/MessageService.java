package cn.icedsoul.messageservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.messageservice.domain.Message;

import java.util.List;

public interface MessageService {
    Response addMessage(Integer fromId, Integer toId, String content, Integer type, String time);
    Response getMessageRecordBetweenUsers(Integer userIdA, Integer userIdB);
    Response addMessages(List<Message> messages);
//    Response updateMessage(Integer id, Integer isTransport);
}
