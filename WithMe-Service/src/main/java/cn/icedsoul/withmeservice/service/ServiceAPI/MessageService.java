package cn.icedsoul.withmeservice.service.ServiceAPI;


import cn.icedsoul.withmeservice.utils.Response;

public interface MessageService {
    Response getMessageRecordBetweenUsers(String jsonObj);
    Response getMessageRecordBetweenUserAndGroup(String jsonObj);
}
