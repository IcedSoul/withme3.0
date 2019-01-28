package cn.icedsoul.messageservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

public interface MessageService {
    Response addMessage(Integer fromId, Integer toId, String content, Integer type,
                        String time, Integer isTransport);
    Response getMessageRecordBetweenUsers(Integer userIdA, Integer userIdB);
    Response getMessageRecordBetweenUserAndGroup(String jsonObj);
    Response updateMessage(Integer id, Integer isTransport);
}
