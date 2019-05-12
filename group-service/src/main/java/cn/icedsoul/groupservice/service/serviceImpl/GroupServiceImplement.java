package cn.icedsoul.groupservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupservice.constant.CONSTANT;
import cn.icedsoul.groupservice.domain.Groups;
import cn.icedsoul.groupservice.domain.UserGroupRelation;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;


@Service
public class GroupServiceImplement implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserGroupRelationRepository userGroupRelationRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Response getGroupUsers(Integer id) {
        try {
            Groups groups = groupRepository.getOne(id);
            JSONObject jsonObject1 = new JSONObject();
            if (!Common.isNull(groups) && !Common.isEmpty(groups.getGroupMembers())) {
                Response response = restTemplate.getForEntity(CONSTANT.USER_SERVICE_GET_USERS_BY_USERIDS, Response.class, groups.getGroupMembers()).getBody();
                if(response.getStatus() == 1){
                    jsonObject1.put("users", response.getContent());
                }
                else{
                    jsonObject1.put("users", "[]");
                }
            }
            else
                jsonObject1.put("users", "[]");
            List<UserGroupRelation> userGroupRelations = userGroupRelationRepository.findByGroupId(id);
            jsonObject1.put("userGroups", JSONArray.toJSONString(userGroupRelations, SerializerFeature.UseSingleQuotes));

            return new Response(1, "获取群组成员成功", jsonObject1.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取群组成员异常", null);
        }
    }

    @Override
    public Response createGroup(String groupName, String groupIntroduction, Integer groupCreatorId) {
        try {
            Groups group = new Groups();
            String groupId = String.valueOf((int) (Math.random() * 100000));
            while (groupRepository.findByGroupId(groupId) != null) {
                groupId = String.valueOf((int) (Math.random() * 100000));
            }
//            Response response = restTemplate.getForEntity(CONSTANT.USER_SERVICE_GET_USER_BY_USERID, Response.class, groupCreatorId).getBody();
//            JSONObject user = JSONObject.parseObject((String)response.getContent());
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
            userGroupRelation.setGroupUserNickName("");
            userGroupRelation.setGroupLevel(10);
            userGroupRelationRepository.save(userGroupRelation);
            groups.setGroupMembers(groups.getId() + "," + String.valueOf(groupCreatorId));
            groups.setGroupUserCount(groups.getGroupUserCount() + 1);
            groupRepository.save(groups);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("groupId", groupId);

            MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("userId", groupCreatorId);
            requestEntity.add("groupId", groups.getId());
            ResponseEntity<Response> responseEntity =
                    restTemplate.postForEntity(CONSTANT.USER_SERVICE_UPDATE_USER_GROUPS, requestEntity, Response.class);
            if(responseEntity.getBody().getStatus() != 1){
                return new Response(-1, "远程服务异常", null);
            }

            return new Response(1, "创建群组成功", jsonObject1.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "创建群组异常", null);
        }
    }

    @Override
    public Response findGroupById(Integer id) {
        try {
            Groups groups = groupRepository.getOne(id);
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
