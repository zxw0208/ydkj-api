package cn.yidukeji.interceptor;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.exception.ApiException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
        String temp = "&signature=" + signature;
        String queryString = request.getQueryString();
        String signatureStr = queryString.replace(temp, "");
        String s = HMACUtils.sha265(accessUser.getSecretKey(), signatureStr);
        if(!signature.equals(s)){
            throw new ApiException("验签失败", 403);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

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
}
