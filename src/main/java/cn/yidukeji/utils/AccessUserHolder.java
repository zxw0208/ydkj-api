package cn.yidukeji.utils;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.service.AccessUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 下午2:47
 * To change this template use File | Settings | File Templates.
 */
public class AccessUserHolder {

    public static Map<String, AccessUser> map = new HashMap<String, AccessUser>();

    public static AccessUser getAccessUser(String accessKeyId){
        return map.get(accessKeyId);
    }

    public static void resetAccessUser() {
        map.clear();
        AccessUserService accessUserService = SpringContext.getApplicationContext().getBean(AccessUserService.class);
        List<AccessUser> list = accessUserService.getAccessUserList();
        for(AccessUser user : list){
            map.put(user.getAccessKeyId(), user);
        }
    }

    private static ThreadLocal<AccessUser> tl = new ThreadLocal<AccessUser>();

    public static void setAccessUser(AccessUser User){
        tl.set(User);
    }

    public static AccessUser getAccessUser(){
        return tl.get();
    }

    public static void removeAccessUser(){
        tl.remove();
    }

    public static void clear(){
        tl.remove();
    }

}
