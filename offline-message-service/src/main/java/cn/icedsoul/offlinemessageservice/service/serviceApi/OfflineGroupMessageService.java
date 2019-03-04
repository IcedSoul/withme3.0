package cn.icedsoul.offlinemessageservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

public interface OfflineGroupMessageService {
    Response addOfflineGroupMessage(Integer fromId, Integer groupId, Integer toId, String content, Integer type,String time);
}
