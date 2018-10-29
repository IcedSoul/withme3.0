package cn.icedsoul.withmeservice.repository;

import cn.icedsoul.withmeservice.domain.PriKey.UserRelationPriKey;
import cn.icedsoul.withmeservice.domain.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserRelationRepository extends JpaRepository<UserRelation, UserRelationPriKey> {
}
