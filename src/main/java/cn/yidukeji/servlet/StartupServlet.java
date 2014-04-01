package cn.yidukeji.servlet;

import cn.yidukeji.utils.SpringContext;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 昕炜
 * Date: 13-4-17
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class StartupServlet extends DispatcherServlet {

    Logger logger = Logger.getLogger(this.getClass());

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext ac = super.initWebApplicationContext();
        SpringContext.setApplicationContext(ac);
        return ac;
    }

}
