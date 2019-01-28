package cn.icedsoul.groupservice.service.serviceImpl;

import cn.icedsoul.userservice.domain.Groups;
import cn.icedsoul.userservice.domain.User;
import cn.icedsoul.userservice.domain.UserGroupRelation;
import cn.icedsoul.userservice.repository.GroupRepository;
import cn.icedsoul.userservice.repository.UserGroupRelationRepository;
import cn.icedsoul.userservice.repository.UserRepository;
import cn.icedsoul.userservice.service.serviceApi.UserGroupRelationService;
import cn.icedsoul.userservice.constant.Common;
import cn.icedsoul.userservice.constant.Response;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserGroupRelationServiceImplement implements UserGroupRelationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRelationRepository userGroupRelationRepository;

    @Autowired
    GroupRepository groupRepository;

    @Override
    @Transactional
    public Response addGroupUsers(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            Integer id = jsonObject.getInteger("id");
            Integer userId = jsonObject.getInteger("userId");
            UserGroupRelation userGroupRelation = new UserGroupRelation();
            userGroupRelation.setGroupId(id);
            userGroupRelation.setGroupLevel(0);
            userGroupRelation.setUserId(userId);
            userGroupRelation.setEnterGroupTime(Common.getCurrentTime());
            userGroupRelation.setGroupUserNickName(userRepository.findByUserId(userId).getUserNickName());
            userGroupRelationRepository.save(userGroupRelation);
            Groups group = groupRepository.getOne(id);
            if (Common.isEmpty(group.getGroupMembers()))
                group.setGroupMembers(String.valueOf(userGroupRelation.getUserId()));
            else
                group.setGroupMembers(group.getGroupMembers() + "," + String.valueOf(userGroupRelation.getUserId()));
            group.setGroupUserCount(group.getGroupUserCount() + 1);
            groupRepository.save(group);
            User user = userRepository.findByUserId(userId);
            if (Common.isEmpty(user.getUserGroups()))
                user.setUserGroups(String.valueOf(id));
            else
                user.setUserGroups(user.getUserGroups() + "," + String.valueOf(id));
            userRepository.save(user);
            return new Response(1, "获取群组成员成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取群组成员异常", null);
        }
    }
}
