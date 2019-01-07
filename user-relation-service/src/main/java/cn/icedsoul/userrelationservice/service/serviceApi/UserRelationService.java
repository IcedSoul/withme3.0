package cn.icedsoul.userrelationservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

public interface UserRelationService {
    Response buildRelation(String jsonObj);

    Response removeRelation(String jsonObj);

    Response getRelations(String jsonObj);
}
