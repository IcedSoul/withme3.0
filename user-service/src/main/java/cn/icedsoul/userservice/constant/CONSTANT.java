package cn.icedsoul.userservice.constant;

public class CONSTANT {
    //JWT相关
    public static int EXPIRE_TIME = 60;
//    public static String SECRET_KEY = "authJWT";

    public static String GROUP_FIND_BY_IDS = "http://group-service:8081/v1/groups/{ids}";

    public static String USER_RELATION_BUILD = "http://user-relation-service:8081/v1/userRelations/{ids}";

    public static String ROBOT_NAME = "admin-robot";

    public static String ROBOT_NICK_NAME = "阿里妹";
}
