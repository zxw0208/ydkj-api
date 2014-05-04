package cn.yidukeji.core;

import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-5-4
 * Time: 下午1:24
 * To change this template use File | Settings | File Templates.
 */
public interface JedisService {
    public Jedis getJedis();
}
