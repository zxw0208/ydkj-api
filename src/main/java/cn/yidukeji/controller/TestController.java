package cn.yidukeji.controller;

import cn.yidukeji.bean.Test;
import cn.yidukeji.interceptor.AccessLevel;
import cn.yidukeji.utils.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-1
 * Time: 下午3:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    @ResponseBody @AccessLevel(2)
    public RestResult test(){
        Test t = new Test();
        t.setT1("111111");
        t.setT2("222222");
        System.out.println("ssssss");
        return RestResult.SUCCESS().put("test", t);
    }

}
