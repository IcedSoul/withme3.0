package cn.icedsoul.withmeservice.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
	@Id
	@GenericGenerator(name = "generator", strategy = "assigned")
	@GeneratedValue(generator = "generator")
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '用户主键'")
	private int userId;
	@Column(nullable = false, columnDefinition = "varchar(40) COMMENT '用户名'")
	private String userName;
	@Column(nullable = false, columnDefinition = "varchar(40) COMMENT '用户昵称'")
	private String userNickName;
	@Column(nullable = true, columnDefinition = "varchar(50) COMMENT '用户头像路径'")
	private String userImgPath;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '是否在线标识（留待使用）'")
	private int userIsOnline;
	@Column(nullable = true, columnDefinition = "varchar(5000) COMMENT '联系人列表'")
	private String userRelations;
	@Column(nullable = true, columnDefinition = "varchar(5000) COMMENT '群组列表'")
	private String userGroups;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '用户角色（留待使用）'")
	private int userRole;
}  