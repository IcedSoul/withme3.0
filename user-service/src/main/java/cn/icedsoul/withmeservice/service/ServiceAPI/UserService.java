package cn.icedsoul.withmeservice.service.ServiceAPI;

import cn.icedsoul.withmeservice.utils.Response;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserService {
    Response login(String jsonObj);
    Response logout(String jsonObj);
    Response register(String jsonObj);
    Response getRelations(String jsonObj);
    Response getUserGroups(String jsonObj);
    Response getCurrentUser(String jsonObj);
    Response findUserByName(String jsonObj);
    Response findUserById(String jsonObj);
}
