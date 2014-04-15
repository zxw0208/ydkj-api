package cn.yidukeji.controller;

import cn.yidukeji.bean.Hotel;
import cn.yidukeji.bean.Rooms;
import cn.yidukeji.core.DefaultPaginator;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.service.HotelOrderService;
import cn.yidukeji.utils.DateUtils;
import cn.yidukeji.utils.RestResult;
import org.apache.commons.lang3.StringUtils;
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
    public RestResult search(String cityName, String startDate, String endDate, Integer priceClass, String keyword, Integer pageNum, Integer pageSize) throws ApiException {
        Paginator p = new DefaultPaginator();
        if(pageNum != null){
            p.setPageNum(pageNum);
        }
        if(pageSize != null){
            p.setPageSize(pageSize);
        }
        if(priceClass != null){
            if(priceClass < 0 || priceClass > 7){
                return RestResult.ERROR_400().put("error", "价格等级在0-7范围内选择 [priceClass]");
            }
        }
        if(StringUtils.isNotBlank(startDate) && DateUtils.parseDate(startDate, "yyyy-MM-dd") == null){
            return RestResult.ERROR_400().put("error", "开始时间格式不正确，正确的格式为yyyy-MM-dd [startDate]");
        }
        if(StringUtils.isNotBlank(endDate) && DateUtils.parseDate(endDate, "yyyy-MM-dd") == null){
            return RestResult.ERROR_400().put("error", "结束时间格式不正确，正确的格式为yyyy-MM-dd [endDate]");
        }
        Paginator paginator = hotelOrderService.search(cityName, startDate, endDate, priceClass, keyword, p);
        return RestResult.SUCCESS().put("userList", paginator.getResults()).put("page", paginator);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public RestResult hotelInfo(Integer id){
        Rooms rooms = hotelOrderService.getRooms(id);
        Hotel hotel = null;
        if(rooms != null){
            hotel = hotelOrderService.getHotel(rooms.getHotelId());
        }
        return RestResult.SUCCESS().put("rooms", rooms).put("hotel", hotel);
    }

}
