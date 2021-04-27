package cn.icedsoul.userrelationservice.domain;

import cn.icedsoul.userrelationservice.domain.prikey.UserRelationPriKey;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Timestamp;

/*
 * http://blog.csdn.net/robinpipi/article/details/7655388
 */

@Entity
@Data
@IdClass(UserRelationPriKey.class)
public class UserRelation {
    @Id
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '用户外键A'")
    private int userIdA;
    @Id
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '用户外键B'")
    private int userIdB;
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '关系状态'")
    private int relationStatus;
    @Column(nullable = false, columnDefinition = "datetime COMMENT '建立联系时间'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp relationStart;
}
