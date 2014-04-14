package cn.yidukeji.exception;

import cn.yidukeji.utils.RestResult;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public class ApiExceptionResolver extends SimpleMappingExceptionResolver {
    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ApiException exception = null;

        if (e instanceof ApiException) {
            exception = (ApiException) e;
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(
                        response.getOutputStream(), JsonEncoding.UTF8);
                response.setStatus(exception.getCode());
                mapper.writeValue(jsonGenerator, exception.getError());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else if(e instanceof org.springframework.validation.BindException){
            org.springframework.validation.BindException be = (org.springframework.validation.BindException) e;
            ApiException ae = new ApiException("[" + be.getFieldError().getField() + "]参数类型不正确", 400);
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(
                        response.getOutputStream(), JsonEncoding.UTF8);
                response.setStatus(ae.getCode());
                mapper.writeValue(jsonGenerator, ae.getError());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            e.printStackTrace();
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(
                        response.getOutputStream(), JsonEncoding.UTF8);
                response.setStatus(500);
                mapper.writeValue(jsonGenerator, RestResult.ERROR_500().put("error", "内部错误"));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return super.doResolveException(request, response, o, e);
    }
}
