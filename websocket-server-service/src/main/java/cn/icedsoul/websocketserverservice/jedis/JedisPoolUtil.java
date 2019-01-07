package cn.icedsoul.websocketserverservice.jedis;

import cn.icedsoul.websocketserverservice.util.CONSTANT;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 建立Jedis连接池，管理jedis连接
 */
public class JedisPoolUtil {
    private static JedisPool pool;

    /**
     * 创建连接池并且配置相关的参数
     */
    private static void createJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数
        config.setMaxTotal(CONSTANT.MAX_TOTAL);
        //设置最长等待时间（单位ms）
        config.setMaxWaitMillis(CONSTANT.MAX_WAIT);
        //设置空间链接
        config.setMinIdle(CONSTANT.MAX_IDLE);
        //创建连接池
        pool = new JedisPool(config, CONSTANT.REDIS_ADDRESS, CONSTANT.REDIS_PORT);
    }

    /**
     * 多线程场景同步初始化
     */
    private static synchronized void poolInit(){
        if(pool == null)
            createJedisPool();
    }

    /**
     * 获取Jedis
     * @return
     */
    public static Jedis getJedis(){
        if(pool == null)
            poolInit();
        return pool.getResource();
    }

}
