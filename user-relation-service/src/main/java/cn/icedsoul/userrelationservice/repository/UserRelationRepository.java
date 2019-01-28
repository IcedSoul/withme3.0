package cn.icedsoul.userrelationservice.repository;

import cn.icedsoul.userrelationservice.domain.UserRelation;
import cn.icedsoul.userrelationservice.domain.prikey.UserRelationPriKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserRelationRepository extends JpaRepository<UserRelation, UserRelationPriKey> {
}
