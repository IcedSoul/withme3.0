package cn.icedsoul.userrelationservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.userservice.domain.AuthUser;
import cn.icedsoul.userservice.domain.User;
import cn.icedsoul.userservice.domain.UserRelation;
import cn.icedsoul.userservice.repository.UserRelationRepository;
import cn.icedsoul.userservice.repository.UserRepository;
import cn.icedsoul.userservice.service.serviceApi.UserRelationService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserRelationServiceImplement implements UserRelationService {

    @Autowired
    UserRelationRepository userRelationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public Response buildRelation(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            UserRelation userRelation = new UserRelation();
            userRelation.setUserIdA(jsonObject.getInteger("userIdA"));
            userRelation.setUserIdB(jsonObject.getInteger("userIdB"));
            userRelation.setRelationStatus(1);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            userRelation.setRelationStart(timestamp);
            userRelationRepository.save(userRelation);
            User userA = userRepository.findByUserId(jsonObject.getInteger("userIdA"));
            User userB = userRepository.findByUserId(jsonObject.getInteger("userIdB"));
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

            return new Response(1, "添加好友成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "添加好友失败", null);
        }

    }

    @Override
    public Response removeRelation(String jsonObj) {
        return null;
    }

    @Override
    public Response getRelations(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            String users[] = userRepository.findByUserId(jsonObject.getInteger("userId")).getUserRelations().split(",");
            List<AuthUser> userList = new ArrayList<>();
            for (int i = 0; i < users.length; i++) {
                userList.add(new AuthUser(userRepository.findByUserId(Integer.valueOf(users[i]))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "查找好友失败", null);
        }
        return null;
    }
}
