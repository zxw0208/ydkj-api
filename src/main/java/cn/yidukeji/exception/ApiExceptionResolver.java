package cn.yidukeji.exception;

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

                mapper.writeValue(jsonGenerator, exception.getError());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            e.printStackTrace();
        }

        return super.doResolveException(request, response, o, e);
    }
}
