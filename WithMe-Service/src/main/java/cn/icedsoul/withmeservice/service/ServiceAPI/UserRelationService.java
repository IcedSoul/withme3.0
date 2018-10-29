package cn.icedsoul.withmeservice.service.ServiceAPI;

import cn.icedsoul.withmeservice.utils.Response;

public interface UserRelationService {
    Response buildRelation(String jsonObj);
    Response removeRelation(String jsonObj);
    Response getRelations(String jsonObj);
}
