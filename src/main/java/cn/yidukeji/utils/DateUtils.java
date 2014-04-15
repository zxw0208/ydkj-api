package cn.yidukeji.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-15
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {

    public static Date parseDate(String str, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
