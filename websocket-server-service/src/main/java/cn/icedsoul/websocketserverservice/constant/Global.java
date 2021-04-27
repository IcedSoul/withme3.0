package cn.icedsoul.websocketserverservice.constant;

import cn.icedsoul.websocketserverservice.domain.dto.ScriptNode;
import cn.icedsoul.websocketserverservice.util.ScriptReaderUtil;
import io.netty.channel.Channel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static cn.icedsoul.websocketserverservice.constant.CONSTANT.CAT_TEST_SCRIPT;


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

    /**
     * 负责存储每一个用户的状态
     */
    public static Map<Integer, ScriptNode> onlineUserStatus = new HashMap<>();

    public static HashMap<String, ScriptNode> gameMap = new HashMap<>();

    /**
     * 机器人ID
     */
    public static Integer robotId = null;

    public static Integer onLineNumber = 0;


    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
}
