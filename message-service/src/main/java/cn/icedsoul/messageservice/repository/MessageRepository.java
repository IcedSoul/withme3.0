package cn.icedsoul.messageservice.repository;


import cn.icedsoul.messageservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByFromIdAndToIdAndIsTransport(Integer fromId, Integer toId, Integer isTransport);

    List<Message> findAllByToIdAndTypeAndIsTransport(Integer toId, Integer type, Integer isTransport);

    List<Message> findAllByToIdAndIsTransport(Integer toId, Integer isTransport);
}
