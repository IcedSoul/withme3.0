package cn.icedsoul.groupservice.service.serviceApi;


import cn.icedsoul.commonservice.util.Response;

public interface GroupService {
    Response getGroupUsers(Integer id);

    Response createGroup(String groupName, String groupIntroduction, Integer groupCreatorId);

    Response findGroupById(Integer id);
    // Response findGroupByGroupId(String jsonObj);

}
