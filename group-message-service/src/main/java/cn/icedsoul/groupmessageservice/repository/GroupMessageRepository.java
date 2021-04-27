package cn.icedsoul.groupmessageservice.repository;


import cn.icedsoul.groupmessageservice.domain.GroupMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

    List<GroupMessage> findAllByGroupId(Integer id, Pageable pageable);

    List<GroupMessage> findAllByFromIdAndToId(Integer fromId, Integer toId);

    List<GroupMessage> findAllByToIdAndType(Integer toId, Integer type);

    List<GroupMessage> findAllByToId(Integer toId);
}
