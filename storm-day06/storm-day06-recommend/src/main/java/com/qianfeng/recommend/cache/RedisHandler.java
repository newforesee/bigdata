package com.qianfeng.recommend.cache;


import com.qianfeng.recommend.utils.MyShardedJedisPool;

/**
 * Describe: 请补充类描述
 * Author:   chenfenggao
 * Domain:   www.1000phone.com
 * Data:     2015/12/2.
 */
public class RedisHandler {
    public static String getValueByHashField(String key, String field) {
        return MyShardedJedisPool.getResource().hget(key, field);
    }

    public static String getString(String key) {
        return MyShardedJedisPool.getResource().get(key);
    }
}
