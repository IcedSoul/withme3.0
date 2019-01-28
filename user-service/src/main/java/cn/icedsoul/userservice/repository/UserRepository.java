package cn.icedsoul.userservice.repository;

import cn.icedsoul.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(Integer userId);

    User findByUserName(String userName);
}
