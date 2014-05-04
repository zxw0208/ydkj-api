package cn.yidukeji.interceptor;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.service.LoggerService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.HMACUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 上午11:36
 * To change this template use File | Settings | File Templates.
 */
public class AuthInterceptor implements HandlerInterceptor, InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<Method, Integer> levelMap = new HashMap<Method, Integer>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HandlerMethod hm = (HandlerMethod) o;

        String accessKeyId = request.getParameter("accessKeyId");
        if(StringUtils.isBlank(accessKeyId)){
            throw new ApiException("accessKeyId is null", 401);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser(accessKeyId);
        if(accessUser == null){
            throw new ApiException("accessKeyId不存在", 403);
        }
        String ip = getIpAddr(request);
        if(!"0".equals(accessUser.getIp()) && !ip.equals(accessUser.getIp())){
            throw new ApiException("ip异常不允许访问", 403);
        }
        Integer level = levelMap.get(hm.getMethod());
        if(accessUser.getLevel() < level){
            throw new ApiException("访问权限不足", 403);
        }
        String signature = request.getParameter("signature");
        if(StringUtils.isBlank(signature)){
            throw new ApiException("signature is null", 403);
        }
        String expires = request.getParameter("expires");
        if(StringUtils.isBlank(signature)){
            throw new ApiException("expires is null", 401);
        }else{
            if(!NumberUtils.isNumber(expires)){
                throw new ApiException("expires参数错误", 400);
            }
            long e = Long.parseLong(expires);
            long c = System.currentTimeMillis()/1000;
            if(c-1800 > e || c+1800 < e){
                throw new ApiException("请求过期", 401);
            }
        }
        //String temp = "&signature=" + signature;
        //String queryString = request.getQueryString();
        Enumeration<String> e = request.getParameterNames();
        Set<String> set = new TreeSet<String>();
        while (e.hasMoreElements()){
            String str = e.nextElement();
            if(!"signature".equals(str)){
                set.add(str);
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String str : set){
            if(sb.length() > 0){
                sb.append("&");
            }
            sb.append(str).append("=").append(request.getParameter(str));
        }
        //String signatureStr = queryString.replace(temp, "");
        String s = HMACUtils.sha265(accessUser.getSecretKey(), sb.toString());
        if(!signature.equals(s)){
            throw new ApiException("验签失败", 403);
        }
        AccessUserHolder.setAccessUser(accessUser);
        LoggerService loggerService = applicationContext.getBean(LoggerService.class);
        loggerService.addAccessLog(System.currentTimeMillis()/1000, hm.getMethod().getName(), hm.getBeanType().getName(), request.getMethod(), sb.toString(), accessUser.getId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        AccessUserHolder.removeAccessUser();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> map = applicationContext
                .getBeansWithAnnotation(Controller.class);
        for (Object obj : map.values()) {
            final AccessLevel alc = AnnotationUtils.findAnnotation(
                    obj.getClass(), AccessLevel.class);
            ReflectionUtils.doWithMethods(obj.getClass(),
                    new ReflectionUtils.MethodCallback() {
                        public void doWith(Method method) {
                            AccessLevel al = AnnotationUtils
                                    .findAnnotation(method,
                                            AccessLevel.class);
                            if (alc != null || al != null) {
                                if(al != null){
                                    levelMap.put(method, al.value());
                                }else if(alc != null){
                                    levelMap.put(method, alc.value());
                                }
                            }else{
                                levelMap.put(method, 1);
                            }
                        }
                    }, ReflectionUtils.USER_DECLARED_METHODS);
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
