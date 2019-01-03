package cn.icedsoul.withmeservice.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IcedSoul on 2018/2/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int status;
    private String message;
    private Object content;
}
