package cn.icedsoul.offlinemessageservice.repository;

import cn.icedsoul.offlinemessageservice.domain.OfflineGroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfflineGroupMessageRepository extends JpaRepository<OfflineGroupMessage, Integer>{
    List<OfflineGroupMessage> findAllByToId(Integer userId);
    void deleteAllByToId(Integer userId);
}
