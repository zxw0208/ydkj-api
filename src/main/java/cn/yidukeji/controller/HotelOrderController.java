package cn.yidukeji.controller;

import cn.yidukeji.core.DefaultPaginator;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.service.HotelOrderService;
import cn.yidukeji.utils.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-14
 * Time: 下午4:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/hotel")
public class HotelOrderController {

    @Autowired
    private HotelOrderService hotelOrderService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public RestResult search(String cityName, String startDate, String endDate, Integer priceClass, String keyword, Integer pageNum, Integer pageSize){
        Paginator p = new DefaultPaginator();
        if(pageNum != null){
            p.setPageNum(pageNum);
        }
        if(pageSize != null){
            p.setPageSize(pageSize);
        }
        Paginator paginator = hotelOrderService.search(cityName, startDate, endDate, priceClass, keyword, p);
        return RestResult.SUCCESS().put("userList", paginator.getResults()).put("page", paginator);
    }

}
