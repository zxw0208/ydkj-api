package cn.yidukeji.service.impl;

import cn.yidukeji.core.JedisService;
import cn.yidukeji.service.LoggerService;
import cn.yidukeji.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-5-4
 * Time: 下午1:48
 * 日志用redis中转，最终存mysql，可控制存库时间，提升效率
 */
@Service
public class LoggerServiceImpl implements LoggerService {

    @Autowired
    private JedisService jedisService;

    private static final String logKey = "access.api.log.{0}";
    private static final String separator = "(@)";

    @Override
    public void addAccessLog(long time, String method, String clazz, String type, String params, Integer id) {
        Jedis jedis = jedisService.getJedis();
        StringBuilder value = new StringBuilder();
        value.append(time).append(separator).append(clazz).append(separator).append(method).append(separator)
                .append(type).append(separator).append(id).append(separator).append(params);
        jedis.rpush(MessageFormat.format(logKey, DateUtils.formatDate(new Date(), "yyyyMMdd")), value.toString());
    }
}
