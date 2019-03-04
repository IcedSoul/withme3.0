package cn.icedsoul.offlinemessageservice.repository;

import cn.icedsoul.offlinemessageservice.domain.OfflineMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfflineMessageRepository extends JpaRepository<OfflineMessage, Integer> {
    List<OfflineMessage> findAllByToId(Integer toId);
//    List<OfflineMessage> findAllByFromId(Integer fromId);
    void deleteAllByToId(Integer toId);
}
