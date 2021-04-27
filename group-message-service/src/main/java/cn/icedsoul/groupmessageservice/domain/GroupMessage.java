package cn.icedsoul.groupmessageservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class GroupMessage implements Comparable<GroupMessage>{
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息主键'")
    private int id;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息发送者ID'")
    private int fromId;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息接受群ID'")
    private int groupId;
    @Column(nullable = false, columnDefinition = "varchar(5000) COMMENT '消息接收者ID（可能不使用，若尽量减少前台交互则不使用,冗余字段）'")
    private String toId;
    @Column(nullable = false, columnDefinition = "varchar(5000) COMMENT '发送消息内容'")
    private String content;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息类型'")
    private int type;
    @Column(nullable = false, columnDefinition = "datetime COMMENT '发送消息时间'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;

    @Override
    public int compareTo(GroupMessage o) {
        return this.time.compareTo(o.getTime());
    }
}
