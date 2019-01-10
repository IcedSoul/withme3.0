package cn.icedsoul.userrelationservice.service.serviceImpl;

import cn.icedsoul.commonservice.util.Response;
import cn.icedsoul.userrelationservice.constant.CONSTANT;
import cn.icedsoul.userrelationservice.domain.UserRelation;
import cn.icedsoul.userrelationservice.domain.prikey.UserRelationPriKey;
import cn.icedsoul.userrelationservice.repository.UserRelationRepository;
import cn.icedsoul.userrelationservice.service.serviceApi.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserRelationServiceImplement implements UserRelationService {

    @Autowired
    UserRelationRepository userRelationRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 根据两个用户的ID来添加好友关系，同时更新两者的好友缓存。
     * @param userIdA
     * @param userIdB
     * @return
     */
    @Override
    @Transactional
    public Response buildRelation(Integer userIdA, Integer userIdB) {
        try {
            UserRelation userRelation = new UserRelation();
            userRelation.setUserIdA(userIdA);
            userRelation.setUserIdB(userIdB);
            userRelation.setRelationStatus(1);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            userRelation.setRelationStart(timestamp);
            userRelationRepository.save(userRelation);
            // 此处使用远程网络调用，暂且采用同步的方式，如果性能出现瓶颈可以考虑用异步
            MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("userIdA", userIdA);
            requestEntity.add("userIdB", userIdB);
            ResponseEntity<Response> responseEntity =
                    restTemplate.postForEntity(CONSTANT.USER_SERVICE, requestEntity, Response.class);
            Response response = responseEntity.getBody();
            if(response.getStatus() == 1)
                return new Response(1, "添加好友成功", null);
            else {
                userRelationRepository.deleteById(new UserRelationPriKey(userIdA, userIdB));
                return new Response(-1, "添加好友失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "添加好友失败", null);
        }

    }

    @Override
    public Response removeRelation(String jsonObj) {
        return null;
    }


}
