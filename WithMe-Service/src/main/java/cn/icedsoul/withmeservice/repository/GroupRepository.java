package cn.icedsoul.withmeservice.repository;

import cn.icedsoul.withmeservice.domain.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface GroupRepository extends JpaRepository<Groups,Integer> {
    Groups findByGroupId(String groupId);
}
