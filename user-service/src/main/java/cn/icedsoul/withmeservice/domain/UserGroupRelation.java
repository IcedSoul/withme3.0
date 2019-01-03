package cn.icedsoul.withmeservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import cn.icedsoul.withmeservice.domain.PriKey.UserGroupRelationPriKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Timestamp;

@Entity
@Data
@IdClass(UserGroupRelationPriKey.class)
public class UserGroupRelation {
	@Id
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '用户外键'")
	private int userId;
	@Id
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '群组外键'")
	private int groupId;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '群等级'")
	private int groupLevel;
	@Column(nullable = false, columnDefinition = "varchar(50) COMMENT '群昵称'")
	private String groupUserNickName;
	@Column(nullable = false, columnDefinition = "datetime COMMENT '进入群组时间'")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Timestamp enterGroupTime;
}
