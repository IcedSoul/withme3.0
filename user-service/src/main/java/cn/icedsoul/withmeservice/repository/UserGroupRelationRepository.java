package cn.icedsoul.withmeservice.repository;


import cn.icedsoul.withmeservice.domain.PriKey.UserGroupRelationPriKey;
import cn.icedsoul.withmeservice.domain.UserGroupRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserGroupRelationRepository extends JpaRepository<UserGroupRelation, UserGroupRelationPriKey> {
    List<UserGroupRelation> findByGroupId(Integer groupId);
}
