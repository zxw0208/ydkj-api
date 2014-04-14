package cn.yidukeji.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-14
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesUtils {

    private static Properties properties = new Properties();

    static{
        ClassPathResource cp = new ClassPathResource("ydkj.properties");
        try {
            properties.load(cp.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

}
