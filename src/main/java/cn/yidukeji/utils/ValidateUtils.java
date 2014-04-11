package cn.yidukeji.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-10
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class ValidateUtils {

    public static boolean emailValidate(String email){
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    public static boolean mobileValidate(String mobile){
        if(StringUtils.isBlank(mobile)){
            return false;
        }
        if(mobile.length() != 11){
            return false;
        }
        if(!NumberUtils.isNumber(mobile)){
            return false;
        }
        return true;
    }
}
