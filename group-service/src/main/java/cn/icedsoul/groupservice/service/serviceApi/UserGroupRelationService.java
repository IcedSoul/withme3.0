package cn.icedsoul.groupservice.service.serviceApi;

import cn.icedsoul.commonservice.util.Response;

public interface UserGroupRelationService {
    Response addGroupUsers(Integer id, Integer userId);
}
