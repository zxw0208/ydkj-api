package cn.yidukeji.controller;

import cn.yidukeji.service.CityService;
import cn.yidukeji.utils.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-15
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = "version=1.0")
    @ResponseBody
    public RestResult list(){
        List<String> list = cityService.getCityList();
        return RestResult.SUCCESS().put("cityList", list);
    }
}
