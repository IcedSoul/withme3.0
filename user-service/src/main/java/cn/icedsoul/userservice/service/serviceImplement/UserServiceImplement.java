package cn.icedsoul.userservice.service.serviceImplement;

import cn.icedsoul.commonservice.dto.AuthUser;
import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.JwtUtils;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.userservice.service.serviceApi.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.icedsoul.userservice.domain.User;
import cn.icedsoul.userservice.domain.UserDetail;

import cn.icedsoul.userservice.repository.UserDetailRepository;
import cn.icedsoul.userservice.repository.UserRepository;
import cn.icedsoul.userservice.constant.CONSTANT;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
@Service
public class UserServiceImplement implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailRepository userDetailRepository;

//    @Autowired
//    RestTemplate restTemplate;


    @Override
    public Response login(String userName, String userPassword) {
        try {
            UserDetail userDetail = userDetailRepository.findByUserDetailName(userName);
            if (userDetail != null) {
                if (Common.isEquals(userDetail.getUserDetailPassword(), userPassword)) {
                    Timestamp expireTime = new Timestamp(System.currentTimeMillis() + CONSTANT.EXPIRE_TIME * 1000 * 60);
                    AuthUser authUser = new AuthUser(userDetail.getUserDetailId(),
                            userDetail.getUserDetailName(),
                            userDetail.getUserDetailNickName(),
                            expireTime);
                    String token = JwtUtils.createJWT(JSON.toJSONString(authUser));
                    return new Response(1, "登陆成功", token);

                } else
                    return new Response(-1, "用户名或者密码错误", null);
            } else {
                return new Response(-1, "用户不存在", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "登录异常", null);
        }
    }

    @Override
    public Response logout(String token) {
        return null;
    }

    @Override
    @Transactional
    public Response register(String userName, String userNickName, String userPassword) {
        try {
            if (userRepository.findByUserName(userName) == null) {
                UserDetail userDetail = new UserDetail();
                userDetail.setUserDetailName(userName);
                userDetail.setUserDetailNickName(userNickName);
                userDetail.setUserDetailPassword(userPassword);
                userDetail.setUserRegisterTime(Common.getCurrentTime());
                userDetailRepository.save(userDetail);
                UserDetail userDetail1 = userDetailRepository.findByUserDetailName(userName);
                User user = new User();
                user.setUserId(userDetail1.getUserDetailId());
                user.setUserName(userDetail1.getUserDetailName());
                user.setUserIsOnline(0);
                user.setUserNickName(userDetail1.getUserDetailNickName());
                user.setUserGroups("");
                user.setUserRelations("");
                user.setUserRole(0);
                userRepository.save(user);
                return new Response(1, "注册成功", null);
            } else
                return new Response(-1, "用户名已存在", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "注册异常", null);
        }
    }

    @Override
    public Response getRelations(Integer userId) {
        try {
            List<User> userList = new ArrayList<>();
            User user = userRepository.findByUserId(userId);
            if (!Common.isNull(user)) {
                if (!Common.isEmpty(user.getUserRelations())) {
                    String relations[] = user.getUserRelations().split(",");
                    for (String friendId : relations) {
                        User user1 = userRepository.findByUserId(Integer.valueOf(friendId));
                        userList.add(user1);
                    }
                }
            }
            return new Response(1, "获取好友成功", JSONArray.toJSONString(userList));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取好友异常", null);
        }
    }

    @Override
    public Response getUserGroups(Integer userId) {
        try {
            User user = userRepository.findByUserId(userId);
            String groups = "";
            if (!Common.isNull(user)) {
                if (!Common.isEmpty(user.getUserGroups())) {
//                    MultiValueMap<String,String> requestEntity = new LinkedMultiValueMap<>();
//                    requestEntity.add("userName", userName);
//                    requestEntity.add("userPassword", userPassword);
//                    ResponseEntity<Response> responseResponseEntity = restTemplate.postForEntity(URL.USER_GROUP_LIST_URL,)
                }
            }
            return new Response(1, "获取用户群组成功", groups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户群组异常", null);
        }
    }

    @Override
    public Response getCurrentUser(String token) {
        try {
            AuthUser authUser = JwtUtils.parseJWT(token);
            return new Response(1, "获取当前用户成功", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取当前用户异常", null);
        }
    }

    @Override
    public Response findUserByName(String name) {
        try {
            User user = userRepository.findByUserName(name);
            AuthUser authUser = new AuthUser();
            authUser.setUserId(user.getUserId());
            authUser.setUserName(user.getUserName());
            authUser.setUserNickName(user.getUserNickName());
            authUser.setExpireTime(Common.getCurrentTime());
            return new Response(1, "获取用户信息成功", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户信息异常", null);
        }
    }

    @Override
    public Response findUserById(String id) {
        try {
            User user = userRepository.findByUserId(Integer.valueOf(id));
            AuthUser authUser = new AuthUser();
            authUser.setUserId(user.getUserId());
            authUser.setUserName(user.getUserName());
            authUser.setUserNickName(user.getUserNickName());
            authUser.setExpireTime(Common.getCurrentTime());
            return new Response(1, "获取用户信息成功", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户信息异常", null);
        }
    }

    @Override
    @Transactional
    public Response updateUserRelation(Integer userIdA, Integer userIdB) {
        try {
            User userA = userRepository.findByUserId(userIdA);
            User userB = userRepository.findByUserId(userIdB);
            if (Common.isEmpty(userA.getUserRelations()))
                userA.setUserRelations(String.valueOf(userB.getUserId()));
            else
                userA.setUserRelations(userA.getUserRelations() + "," + String.valueOf(userB.getUserId()));
            userRepository.save(userA);
            if (Common.isEmpty(userB.getUserRelations()))
                userB.setUserRelations(String.valueOf(userA.getUserId()));
            else
                userB.setUserRelations(userB.getUserRelations() + "," + String.valueOf(userA.getUserId()));
            userRepository.save(userB);
            return new Response(1, "添加好友关系成功", null);
        } catch (Exception e){
            return new Response(-1, "更新好友关系败", null);
        }

    }

    @Override
    public Response getUsersByUserIds(String userIds) {
        try {
            String users[] = userIds.split(",");
            List<AuthUser> userList = new ArrayList<>();
            for (String userId : users) {
                User user = userRepository.findByUserId(Integer.valueOf(userId));
                AuthUser authUser = new AuthUser();
                authUser.setUserId(user.getUserId());
                authUser.setUserName(user.getUserName());
                authUser.setUserNickName(user.getUserNickName());
                authUser.setExpireTime(Common.getCurrentTime());
                userList.add(authUser);
            }
            return new Response(1, "获取用户成功", JSONArray.toJSONString(userList));
        } catch (Exception e){
            e.printStackTrace();
            return new Response(-1, "获取用户失败", null);
        }
    }
}
