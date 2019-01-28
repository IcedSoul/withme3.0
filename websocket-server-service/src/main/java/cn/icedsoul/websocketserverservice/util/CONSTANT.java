package cn.icedsoul.websocketserverservice.util;

public class CONSTANT {
    //Netty相关
    public static Integer NETTY_PORT = 8282;
    public static String WEBSOCKET_PATH = "/websocket";

    public static Integer EXPIRE_TIME = 60;
    public static String SECRET_KEY = "authJWT";

    //Jedis相关
    public static Integer MAX_TOTAL = 100;
    public static Integer MAX_WAIT = 1000;
    public static Integer MAX_IDLE = 10;

//    public static String REDIS_ADDRESS = "10.141.211.176";
    public static String REDIS_ADDRESS = "redis";
    public static Integer REDIS_PORT = 6379;

    //Websocket相关
    public static String ON_LINE_COUNT = "onLineCount";
    public static String ON_LINE_LIST = "onLineList";

    public static String MESSAGE = "message";
}
