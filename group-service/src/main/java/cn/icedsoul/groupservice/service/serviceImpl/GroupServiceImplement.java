package cn.icedsoul.groupservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupservice.repository.GroupRepository;
import cn.icedsoul.groupservice.repository.UserGroupRelationRepository;
import cn.icedsoul.groupservice.service.serviceApi.GroupService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImplement implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRelationRepository userGroupRelationRepository;

    @Override
    public Response getGroupUsers(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            List<AuthUser> userList = new ArrayList<>();
            Groups groups = groupRepository.getOne(jsonObject.getInteger("id"));
            AuthUser authUser = null;
            if (!Common.isNull(groups)) {
                if (!Common.isEmpty(groups.getGroupMembers())) {
                    String users[] = groups.getGroupMembers().split(",");
                    for (int i = 1; i < users.length; i++) {
                        authUser = new AuthUser(userRepository.findByUserId(Integer.valueOf(users[i])));
                        userList.add(authUser);
                    }
                }
            }
            List<UserGroupRelation> userGroupRelations = userGroupRelationRepository.findByGroupId(jsonObject.getInteger("id"));
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("userGroups", JSONArray.toJSONString(userGroupRelations, SerializerFeature.UseSingleQuotes));
            jsonObject1.put("users", JSONArray.toJSONString(userList));
            return new Response(1, "获取群组成员成功", jsonObject1.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取群组成员异常", null);
        }
    }

    @Override
    public Response createGroup(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            String groupName = jsonObject.getString("groupName");
            String groupIntroduction = jsonObject.getString("groupIntroduction");
            Integer groupCreatorId = jsonObject.getInteger("groupCreatorId");

            Groups group = new Groups();
            String groupId = String.valueOf((int) (Math.random() * 100000));
            while (groupRepository.findByGroupId(groupId) != null) {
                groupId = String.valueOf((int) (Math.random() * 100000));
            }
            group.setGroupId(groupId);
            group.setGroupCreatorId(groupCreatorId);
            group.setGroupIntroduction(groupIntroduction);
            group.setGroupName(groupName);
            group.setGroupCreateTime(Common.getCurrentTime());
            group.setGroupUserCount(0);
            group.setGroupMembers("");
            groupRepository.save(group);
            UserGroupRelation userGroupRelation = new UserGroupRelation();
            //这里两个id不是一回事，一个是逻辑id，一个是业务id，要区分开
            Groups groups = groupRepository.findByGroupId(groupId);
            userGroupRelation.setGroupId(groups.getId());
            userGroupRelation.setUserId(groupCreatorId);
            userGroupRelation.setEnterGroupTime(Common.getCurrentTime());
            userGroupRelation.setGroupUserNickName(userRepository.findByUserId(groupCreatorId).getUserNickName());
            userGroupRelation.setGroupLevel(10);
            userGroupRelationRepository.save(userGroupRelation);
            groups.setGroupMembers(groups.getId() + "," + String.valueOf(groupCreatorId));
            groups.setGroupUserCount(groups.getGroupUserCount() + 1);
            groupRepository.save(groups);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("groupId", groupId);
            User user = userRepository.findByUserId(groupCreatorId);
            if (Common.isEmpty(user.getUserGroups())) {
                user.setUserGroups(String.valueOf(group.getId()));
            } else
                user.setUserGroups(user.getUserGroups() + "," + String.valueOf(group.getId()));
            userRepository.save(user);
            return new Response(1, "获取用户群组成功", jsonObject1.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户群组异常", null);
        }
    }

    @Override
    public Response findGroupById(String jsonObj) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            Groups groups = groupRepository.getOne(jsonObject.getInteger("id"));
            return new Response(1, "获取用户群组成功", JSON.toJSONString(groups));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取用户群组异常", null);
        }
    }


    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }
}
