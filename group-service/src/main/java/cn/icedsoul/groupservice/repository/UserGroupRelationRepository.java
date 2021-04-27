package cn.icedsoul.groupservice.repository;


import cn.icedsoul.groupservice.domain.prikey.UserGroupRelationPriKey;
import cn.icedsoul.groupservice.domain.UserGroupRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserGroupRelationRepository extends JpaRepository<UserGroupRelation, UserGroupRelationPriKey> {
    List<UserGroupRelation> findByGroupId(Integer groupId);
}
