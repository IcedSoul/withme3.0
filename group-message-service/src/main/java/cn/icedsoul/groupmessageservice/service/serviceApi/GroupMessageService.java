package cn.icedsoul.groupmessageservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

public interface GroupMessageService {
    Response addGroupMessage(Integer fromId, Integer groupId, String toId, String content, Integer type,
                        String time, Integer isTransport);
    Response getMessageRecordBetweenUserAndGroup(Integer userId, Integer groupId, Integer limit);
}
