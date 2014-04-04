package cn.yidukeji.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-4
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLevel {
    int value() default 1;
}
