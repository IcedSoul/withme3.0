package cn.icedsoul.withmeservice.service.ServiceAPI;


import cn.icedsoul.withmeservice.utils.Response;

public interface GroupService {
    Response getGroupUsers(String jsonObj);
    Response createGroup(String jsonObj);
    Response findGroupById(String jsonObj);
   // Response findGroupByGroupId(String jsonObj);

}
