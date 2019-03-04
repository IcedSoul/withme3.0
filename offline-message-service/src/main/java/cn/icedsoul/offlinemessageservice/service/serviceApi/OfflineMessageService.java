package cn.icedsoul.offlinemessageservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

import java.sql.Timestamp;

public interface OfflineMessageService {
    Response addOfflineMessage(Integer fromId, Integer toId, String content, Integer type, String time);
    Response getOfflineMessageTo(Integer userId);

//    Response getOfflineMessageFrom(Integer userId);
}
