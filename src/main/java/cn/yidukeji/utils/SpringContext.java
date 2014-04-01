package cn.yidukeji.utils;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 13-6-18
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class SpringContext {

    private static Logger logger = Logger.getLogger(SpringContext.class);

    private static ApplicationContext ac;

    public static void setApplicationContext(ApplicationContext ac){
        SpringContext.ac = ac;
    }

    public static ApplicationContext getApplicationContext(){
        return ac;
    }

    public static Object getBean(String name){
        if(ac == null){
            logger.warn("applicationContext is null");
            return null;
        }
        return ac.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        if(ac == null){
            logger.warn("applicationContext is null");
            return null;
        }
        return ac.getBean(clazz);
    }

}
