package cn.icedsoul.userservice.service.serviceApi;


import cn.icedsoul.commonservice.util.Response;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserService {
    Response login(String userName, String userPassword);

    Response logout(String token);

    Response register(String userName, String userNickName, String userPassword);

    Response getRelations(Integer userId);

    Response getUserGroups(Integer userId);

    Response getCurrentUser(String token);

    Response findUserByName(String name);

    Response findUserById(String id);

    Response updateUserRelation(Integer userIdA, Integer userIdB);

    Response getUsersByUserIds(String userIds);
}
