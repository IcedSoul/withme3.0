package cn.icedsoul.websocketserverservice.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private int type;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
}
