package cn.icedsoul.userservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class UserDetail {
    @Id
    @GeneratedValue
    @Column(nullable = false, columnDefinition = "Int(11) COMMENT '用户详细主键'")
    private int userDetailId;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT '用户名'")
    private String userDetailName;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT '用户昵称'")
    private String userDetailNickName;
    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT '用户密码'")
    private String userDetailPassword;
    @Column(nullable = true, columnDefinition = "varchar(20) COMMENT '用户邮箱'")
    private String userMailNumber;
    @Column(nullable = true, columnDefinition = "varchar(15) COMMENT '用户手机号码'")
    private String userPhoneNumber;
    @Column(nullable = false, columnDefinition = "datetime COMMENT '注册时间'")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp userRegisterTime;

}
