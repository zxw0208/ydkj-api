package cn.yidukeji.service;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-5-4
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public interface LoggerService {

    public void addAccessLog(long time, String method, String clazz, String type, String params, Integer id);

}
