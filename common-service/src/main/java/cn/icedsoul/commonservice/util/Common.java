package cn.icedsoul.commonservice.util;

import java.sql.Timestamp;

public class Common {
    public static boolean isEquals(String a, String b) {
        if (a == null || b == null || a.equals("") || b.equals(""))
            return false;
        if (a == b || a.equals(b))
            return true;
        return false;
    }

    public static boolean isEmpty(String x) {
        if (x == null || x.equals("") || x == "")
            return true;
        return false;
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getTimeFromString(String time){
        return Timestamp.valueOf(time);
    }
    public static boolean isNull(Object object) {
        if (object == null)
            return true;
        return false;
    }
}
