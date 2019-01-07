package cn.icedsoul.userrelationservice.repository;

import cn.icedsoul.userservice.domain.PriKey.UserRelationPriKey;
import cn.icedsoul.userservice.domain.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserRelationRepository extends JpaRepository<UserRelation, UserRelationPriKey> {
}
