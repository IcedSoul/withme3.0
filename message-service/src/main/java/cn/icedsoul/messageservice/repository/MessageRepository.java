package cn.icedsoul.messageservice.repository;


import cn.icedsoul.messageservice.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IcedSoul on 2018/2/20.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByFromIdAndToId(Integer fromId, Integer toId);

    /**
     * 查询两个用户之间的聊天记录
     *
     * @param fromId 用户A ID
     * @param toId 用户B ID
     * @param fromId2 用户B ID
     * @param toId2 用户A ID
     * @param pageable 分页对象，用户分页和排序
     * @return
     */
    List<Message> findAllByFromIdAndToIdOrFromIdAndToId(Integer fromId, Integer toId, Integer fromId2, Integer toId2, Pageable pageable);

    List<Message> findAllByToIdAndType(Integer toId, Integer type);

    List<Message> findAllByToId(Integer toId);
}
