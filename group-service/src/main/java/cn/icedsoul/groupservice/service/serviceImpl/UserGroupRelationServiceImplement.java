package cn.icedsoul.groupservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Common;
import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.groupservice.constant.CONSTANT;
import cn.icedsoul.groupservice.domain.Groups;
import cn.icedsoul.groupservice.domain.UserGroupRelation;
import cn.icedsoul.groupservice.repository.GroupRepository;
import cn.icedsoul.groupservice.repository.UserGroupRelationRepository;
import cn.icedsoul.groupservice.service.serviceApi.UserGroupRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class UserGroupRelationServiceImplement implements UserGroupRelationService {

    @Autowired
    UserGroupRelationRepository userGroupRelationRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    @Transactional
    public Response addGroupUsers(Integer id, Integer userId) {
        try {
            UserGroupRelation userGroupRelation = new UserGroupRelation();
            userGroupRelation.setGroupId(id);
            userGroupRelation.setGroupLevel(0);
            userGroupRelation.setUserId(userId);
            userGroupRelation.setEnterGroupTime(Common.getCurrentTime());
            userGroupRelation.setGroupUserNickName("");
            userGroupRelationRepository.save(userGroupRelation);
            Groups group = groupRepository.getOne(id);
            if (Common.isEmpty(group.getGroupMembers())) {
                group.setGroupMembers(String.valueOf(userGroupRelation.getUserId()));
            }
            else {
                group.setGroupMembers(group.getGroupMembers() + "," + userGroupRelation.getUserId());
            }
            group.setGroupUserCount(group.getGroupUserCount() + 1);
            groupRepository.save(group);

            MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("userId", userId);
            requestEntity.add("groupId", id);
            ResponseEntity<Response> responseEntity =
                    restTemplate.postForEntity(CONSTANT.USER_SERVICE_UPDATE_USER_GROUPS , requestEntity, Response.class);
            if(responseEntity.getBody().getStatus() != 1){
                return new Response(-1, "远程服务异常", null);
            }

            return new Response(1, "添加群组成员成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "获取群组成员异常", null);
        }
    }
}
