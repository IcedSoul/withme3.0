package cn.icedsoul.websocketserverservice.constant;

import io.netty.channel.Channel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * 存储全局配置
 *
 * @author icedsoul
 * @date 2018/2/8
 */
public class Global {
    /**
     * 负责存储每一个客户端连接进来的Channel对象
     */
    public static Map<Integer, Channel> onLineList = new HashMap<>();

    public static Integer onLineNumber = 0;

//    public static Jedis jedis = JedisPoolUtil.getJedis();

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
}
