package cn.yidukeji.interceptor;

import cn.yidukeji.exception.ApiException;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.HMACUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 上午11:36
 * To change this template use File | Settings | File Templates.
 */
public class AuthInterceptor implements HandlerInterceptor, InitializingBean {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String accessKeyId = request.getParameter("accessKeyId");
        if(StringUtils.isBlank(accessKeyId)){
            throw new ApiException("accessKeyId is null", 401);
        }
        String secretKey = AccessUserHolder.getSecretKey(accessKeyId);
        if(StringUtils.isBlank(accessKeyId)){
            throw new ApiException("accessKeyId不存在", 403);
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
        String s = HMACUtils.sha265(secretKey, signatureStr);
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

    }
}
