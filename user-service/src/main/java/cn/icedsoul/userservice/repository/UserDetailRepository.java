package cn.icedsoul.userservice.repository;

import cn.icedsoul.userservice.domain.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {
    UserDetail findByUserDetailName(String userDetailName);
}
