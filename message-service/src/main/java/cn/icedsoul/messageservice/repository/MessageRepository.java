package cn.icedsoul.messageservice.repository;


import cn.icedsoul.messageservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByFromIdAndToId(Integer fromId, Integer toId);

    List<Message> findAllByToIdAndType(Integer toId, Integer type);

    List<Message> findAllByToId(Integer toId);
}
