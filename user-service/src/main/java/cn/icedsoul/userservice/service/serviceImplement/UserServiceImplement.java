package cn.icedsoul.userservice.service.serviceImplement;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.userservice.service.serviceApi.UserService;
import cn.icedsoul.userservice.utils.URL;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.icedsoul.userservice.domain.AuthUser;

import cn.icedsoul.userservice.domain.User;
import cn.icedsoul.userservice.domain.UserDetail;

import cn.icedsoul.userservice.repository.UserDetailRepository;
import cn.icedsoul.userservice.repository.UserRepository;
import cn.icedsoul.userservice.utils.CONSTANT;

import cn.icedsoul.userservice.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    RestTemplate restTemplate;


    @Override
    public Response login(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            UserDetail userDetail = userDetailRepository.findByUserDetailName(jsonObject.getString("userName"));
            if (userDetail != null) {
                if (Common.isEquals(userDetail.getUserDetailPassword(), jsonObject.getString("userPassword"))) {
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
    public Response logout(String jsonObj) {
        return null;
    }

    @Override
    @Transactional
    public Response register(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            if (userRepository.findByUserName(jsonObject.getString("userName")) == null) {
                UserDetail userDetail = new UserDetail();
                userDetail.setUserDetailName(jsonObject.getString("userName"));
                userDetail.setUserDetailNickName(jsonObject.getString("userNickName"));
                userDetail.setUserDetailPassword(jsonObject.getString("userPassword"));
                userDetail.setUserRegisterTime(Common.getCurrentTime());
                userDetailRepository.save(userDetail);
                UserDetail userDetail1 = userDetailRepository.findByUserDetailName(jsonObject.getString("userName"));
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
    public Response getCurrentUser(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            AuthUser authUser = JwtUtils.parseJWT(jsonObject.getString("token"));
            return new Response(1, "获取当前用户成功", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取当前用户异常", null);
        }
    }

    @Override
    public Response findUserByName(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            AuthUser authUser = new AuthUser(userRepository.findByUserName(jsonObject.getString("userName")));
            return new Response(1, "获取用户信息成功", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户信息异常", null);
        }
    }

    @Override
    public Response findUserById(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            AuthUser authUser = new AuthUser(userRepository.findByUserId(jsonObject.getInteger("userId")));
            return new Response(1, "获取用户信息成功", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户信息异常", null);
        }
    }
}
