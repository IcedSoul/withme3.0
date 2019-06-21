package cn.icedsoul.websocketserverservice.constant;

/**
 * @author icedsoul
 */
public class CONSTANT {
    /**
     * Netty相关
     */

    public static Integer NETTY_PORT = 8282;
    public static String WEBSOCKET_PATH = "/websocket";

    /**
     * jwt相关
     */
    public static Integer EXPIRE_TIME = 60;
    public static String SECRET_KEY = "authJWT";

    /**
     * Jedis相关
     */
    public static Integer MAX_TOTAL = 100;
    public static Integer MAX_WAIT = 1000;
    public static Integer MAX_IDLE = 10;

    public static String REDIS_ADDRESS = "redis";
    public static Integer REDIS_PORT = 6379;

    /**
     * Websocket相关
     */

    public static String ONLINE_COUNT = "onLineCount";
    public static String ONLINE_LIST = "onLineList";

    public static String MESSAGE = "message";

    /**
     * 存储Message相关
     */
    public static String MESSAGE_BASE = "http://message-service:8081";
    public static String ADD_MESSAGE = "/v1/message";

    /**
     * 存储Group Message 相关
     */
    public static String GROUP_MESSAGE_BASE = "http://group-message-service:8081";
    //    public static String MESSAGE_BASE = "http://10.141.211.176:21005";
    public static String ADD_GROUP_MESSAGE = "/v1/groupMessage";

    /**
     * 存储离线消息 相关
     */
    public static String OFFLINE_MESSAGE_BASE = "http://offline-message-service:8081";
    public static String ADD_OFFLINE_MESSAGE = "/v1/offlineMessage";
}
