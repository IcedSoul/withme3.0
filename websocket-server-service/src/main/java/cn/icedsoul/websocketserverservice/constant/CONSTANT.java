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
    public static String ROBOT_NAME = "admin-robot";
    public static String USER_ID = "userId";
    public static String SPLIT_COMMA = ".";

    /**
     * 存储Message相关
     */
    public static String MESSAGE_BASE = "http://message-service:8081";
    public static String ADD_MESSAGE = "/v1/message";

    /**
     * 存储Group Message 相关
     */
    public static String GROUP_MESSAGE_BASE = "http://group-message-service:8081";
    public static String ADD_GROUP_MESSAGE = "/v1/groupMessage";

    /**
     * 存储离线消息 相关
     */
    public static String OFFLINE_MESSAGE_BASE = "http://offline-message-service:8081";
    public static String ADD_OFFLINE_MESSAGE = "/v1/offlineMessage";

    /**
     * 获取用户信息 相关
     */
    public static String USER_SERVICE_BASE = "http://user-service:8081";
    public static String GET_USER_BY_NAME = "/v1/user/userName/";

    public static String ROLE_CHOICE = "choice";
    public static String ROLE_CAT = "cat";
    public static String ROLE_CEO = "ceo";
    public static String ROLE_NEW_GRAD = "new_grad";
    public static String ROLE_AUNT = "aunt";

    public static final String CHOICE_SCRIPT = "/home/choice.txt";
    public static final String NEW_GRAD_TEST_SCRIPT = "/home/newgrad.txt";
    public static final String CEO_TEST_SCRIPT = "/home/ceo.txt";
    public static final String CAT_TEST_SCRIPT = "/home/cat.txt";
    public static final String AUNT_TEST_SCRIPT = "/home/aunt.txt";

    public static final String GAME_START = ROLE_CHOICE + "1";
    public static final String CAT_START = ROLE_CAT + "1";
    public static final String CEO_START = ROLE_CEO + "1";
    public static final String NEW_GRAD_START = ROLE_NEW_GRAD + "1";
    public static final String AUNT_START = ROLE_AUNT + "1";

    public static final String MESSAGE_CONTENT_SPLIT = "\\$";

    public static final String CHOICE_PARSE_EXCEPTION_NOTICE = "请输入格式和范围正确的数字选项，如1、2、3或4等";
    public static final String[] ROLE_CHOICE_ARRAY = new String[]{"", CEO_START, NEW_GRAD_START, AUNT_START, CAT_START};
}
