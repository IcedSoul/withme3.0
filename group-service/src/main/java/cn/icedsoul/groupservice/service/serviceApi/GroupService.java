package cn.icedsoul.groupservice.service.serviceApi;


import cn.icedsoul.commonservice.util.Response;

public interface GroupService {
    Response getGroupUsers(String jsonObj);

    Response createGroup(String jsonObj);

    Response findGroupById(String jsonObj);
    // Response findGroupByGroupId(String jsonObj);

}
