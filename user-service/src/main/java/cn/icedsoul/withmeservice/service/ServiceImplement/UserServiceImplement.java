package cn.icedsoul.withmeservice.service.ServiceImplement;

import cn.icedsoul.withmeservice.service.ServiceAPI.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.icedsoul.withmeservice.domain.AuthUser;
import cn.icedsoul.withmeservice.domain.Groups;
import cn.icedsoul.withmeservice.domain.User;
import cn.icedsoul.withmeservice.domain.UserDetail;
import cn.icedsoul.withmeservice.repository.GroupRepository;
import cn.icedsoul.withmeservice.repository.UserDetailRepository;
import cn.icedsoul.withmeservice.repository.UserRepository;
import cn.icedsoul.withmeservice.utils.CONSTANT;
import cn.icedsoul.withmeservice.utils.Common;
import cn.icedsoul.withmeservice.utils.JwtUtils;
import cn.icedsoul.withmeservice.utils.Response;
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

    @Autowired
    GroupRepository groupRepository;


    @Override
    public Response login(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            UserDetail userDetail = userDetailRepository.findByUserDetailName(jsonObject.getString("userName"));
            if(userDetail != null ){
                if(Common.isEquals(userDetail.getUserDetailPassword(),jsonObject.getString("userPassword"))){
                    Timestamp expireTime = new Timestamp(System.currentTimeMillis()+ CONSTANT.EXPIRE_TIME*1000*60);
                    AuthUser authUser = new AuthUser(userDetail.getUserDetailId(),
                            userDetail.getUserDetailName(),
                            userDetail.getUserDetailNickName(),
                            expireTime);
                    String token = JwtUtils.createJWT(JSON.toJSONString(authUser));
                    return new Response(1,"登陆成功",token);

                }
                else
                    return new Response(-1,"用户名或者密码错误",null);
            }
            else{
                return new Response(-1,"用户不存在",null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"登录异常",null);
        }
    }

    @Override
    public Response logout(String jsonObj) {
        return null;
    }

    @Override
    @Transactional
    public Response register(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            if(userRepository.findByUserName(jsonObject.getString("userName")) == null){
                UserDetail userDetail = new UserDetail();
                userDetail.setUserDetailName(jsonObject.getString("userName"));
                userDetail.setUserDetailNickName(jsonObject.getString("userNickName"));
                userDetail.setUserDetailPassword(jsonObject.getString("userPassword"));
                userDetail.setUserRegisterTime(Common.getCurrentTime());
                userDetailRepository.save(userDetail);
                userDetail = userDetailRepository.findByUserDetailName(jsonObject.getString("userName"));
                User user = new User();
                user.setUserId(userDetail.getUserDetailId());
                user.setUserName(userDetail.getUserDetailName());
                user.setUserIsOnline(0);
                user.setUserNickName(userDetail.getUserDetailNickName());
                user.setUserGroups("");
                user.setUserRelations("");
                user.setUserRole(0);
                userRepository.save(user);
                return new Response(1,"注册成功",null);
            }
            else
                return new Response(-1,"用户名已存在",null);

        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"注册异常",null);
        }
    }

    @Override
    public Response getRelations(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            List<User> userList = new ArrayList<>();
            User user = userRepository.findByUserId(jsonObject.getInteger("userId"));
            if(!Common.isNull(user)){
                if(!Common.isEmpty(user.getUserRelations())) {
                    String relations[] = user.getUserRelations().split(",");
                    for (int i = 0; i < relations.length; i++) {
                        user = userRepository.findByUserId(Integer.valueOf(relations[i]));
                        userList.add(user);
                    }
                }
            }
            return new Response(1,"获取好友关系成功", JSONArray.toJSONString(userList));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取好友关系异常",null);
        }
    }

    @Override
    public Response getUserGroups(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            List<Groups> groupList = new ArrayList<>();
            User user = userRepository.findByUserId(jsonObject.getInteger("userId"));
            Groups group = null;
            if(!Common.isNull(user)){
                if(!Common.isEmpty(user.getUserGroups())) {
                    String groups[] = user.getUserGroups().split(",");
                    for (int i = 0; i < groups.length; i++) {
                        group = groupRepository.getOne(Integer.valueOf(groups[i]));
                        groupList.add(group);
                    }
                }
            }
            return new Response(1,"获取用户群组成功",JSONArray.toJSONString(groupList));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取用户群组异常",null);
        }
    }

    @Override
    public Response getCurrentUser(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            AuthUser authUser = JwtUtils.parseJWT(jsonObject.getString("token"));
            return new Response(1,"获取当前用户成功",JSON.toJSONString(authUser));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取当前用户异常",null);
        }
    }

    @Override
    public Response findUserByName(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            AuthUser authUser = new AuthUser(userRepository.findByUserName(jsonObject.getString("userName")));
            return new Response(1,"获取用户信息成功",JSON.toJSONString(authUser));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取用户信息异常",null);
        }
    }

    @Override
    public Response findUserById(String jsonObj) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            AuthUser authUser = new AuthUser(userRepository.findByUserId(jsonObject.getInteger("userId")));
            return new Response(1,"获取用户信息成功",JSON.toJSONString(authUser));
        }catch (Exception e){
            e.printStackTrace();
            return new Response(-1,"获取用户信息异常",null);
        }
    }
}
