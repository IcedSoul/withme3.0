package cn.icedsoul.userservice.service.serviceApi;


import cn.icedsoul.commonservice.util.Response;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserService {
    Response login(String jsonObj);

    Response logout(String jsonObj);

    Response register(String jsonObj);

    Response getRelations(Integer userId);

    Response getUserGroups(Integer userId);

    Response getCurrentUser(String jsonObj);

    Response findUserByName(String jsonObj);

    Response findUserById(String jsonObj);
}
