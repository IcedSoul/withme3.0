package cn.icedsoul.groupservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class Groups {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '群组主键'")
    private int id;
    @Column(nullable = false, columnDefinition = "varchar(10) COMMENT '群组编号'")
    private String groupId;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '群组名称'")
    private String groupName;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '群组创建者Id'")
    private int groupCreatorId;
    @Column(nullable = false, columnDefinition = "datetime COMMENT '创建时间'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp groupCreateTime;
    @Column(nullable = true, columnDefinition = "varchar(1000) COMMENT '群组描述'")
    private String groupIntroduction;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '成员数量'")
    private int groupUserCount;
    @Column(nullable = true, columnDefinition = "varchar(5000) COMMENT '群组成员'")
    private String groupMembers;

}
