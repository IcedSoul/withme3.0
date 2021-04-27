package cn.icedsoul.commonservice.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AuthUser {
    Integer userId;
    String userName;
    String userNickName;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    Timestamp expireTime;

    public AuthUser() {
    }

    public AuthUser(Integer userId, String userName, String userNickName, Timestamp expireTime) {
        this.userId = userId;
        this.userName = userName;
        this.userNickName = userNickName;
        this.expireTime = expireTime;
    }

//    public AuthUser(User user){
//        this.userId = user.getUserId();
//        this.userName = user.getUserName();
//        this.userNickName = user.getUserNickName();
//        this.expireTime = Common.getCurrentTime();
//    }
}
