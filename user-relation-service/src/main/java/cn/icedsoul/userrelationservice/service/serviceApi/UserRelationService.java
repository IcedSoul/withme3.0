package cn.icedsoul.userrelationservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

public interface UserRelationService {
    Response buildRelation(Integer userIdA, Integer userIdB);

    Response removeRelation(String jsonObj);

}
