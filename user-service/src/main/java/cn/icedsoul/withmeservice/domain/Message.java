package cn.icedsoul.withmeservice.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class Message {
	@Id
	@GeneratedValue
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息主键'")
	private int id;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息发送者ID'")
	private int fromId;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息接收者ID'")
	private int toId;
	@Column(nullable = false, columnDefinition = "varchar(5000) COMMENT '发送消息内容'")
	private String content;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '消息类型'")
	private int type;
	@Column(nullable = false, columnDefinition = "datetime COMMENT '发送消息时间'")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Timestamp time;
	@Column(nullable = false, columnDefinition = "Int(11) COMMENT '是否成功转发（留待用，若采用离线消息表则不采用）'")
	private int isTransport;
}
