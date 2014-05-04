package cn.yidukeji.core;

import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-5-4
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
public class JedisServiceImpl implements JedisService {

    private String host;
    private int port = 6379;
    private int db;

    private Jedis jedis;

    public JedisServiceImpl(){
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    @Override
    public Jedis getJedis() {
        if(jedis == null){
            jedis = new Jedis(host, port);
            jedis.select(db);
        }
        return jedis;
    }
}
