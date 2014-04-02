package cn.yidukeji.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 下午2:47
 * To change this template use File | Settings | File Templates.
 */
public class AccessUserHolder {

    public Map<String, String> map = new HashMap<String, String>();

    public static String getSecretKey(String accessKeyId){
        return "2014";
    }

}
