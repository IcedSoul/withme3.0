package cn.icedsoul.withmeservice.netty.Config;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储全局配置
 * Created by 14437 on 2018/2/8.
 */
public class NettyConfig {
    /**
     * 负责存储每一个客户端连接进来的Channel对象
     */
    public static Map<Integer,Channel> onLineList = new HashMap<>();
}
