package cn.yidukeji.controller;

import cn.yidukeji.bean.*;
import cn.yidukeji.core.DefaultPaginator;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.service.HotelOrderService;
import cn.yidukeji.service.UserService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.DateUtils;
import cn.yidukeji.utils.RestResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "version=1.0")
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
                throw new ApiException("价格等级在0-7范围内选择 [priceClass]", 400);
            }
        }
        if(StringUtils.isNotBlank(startDate) && DateUtils.parseDate(startDate, "yyyy-MM-dd") == null){
            throw new ApiException("开始时间格式不正确，正确的格式为yyyy-MM-dd [startDate]", 400);
        }
        if(StringUtils.isNotBlank(endDate) && DateUtils.parseDate(endDate, "yyyy-MM-dd") == null){
            throw new ApiException("结束时间格式不正确，正确的格式为yyyy-MM-dd [endDate]", 400);
        }
        Paginator paginator = hotelOrderService.search(cityName, startDate, endDate, priceClass, keyword, p);
        return RestResult.SUCCESS().put("hotelList", paginator.getResults()).put("page", paginator);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, params = "version=1.0")
    @ResponseBody
    public RestResult hotelInfo(Integer id){
        Rooms rooms = hotelOrderService.getRooms(id);
        Hotel hotel = null;
        if(rooms != null){
            hotel = hotelOrderService.getHotel(rooms.getHotelId());
        }
        return RestResult.SUCCESS().put("rooms", rooms).put("hotel", hotel);
    }

    /**
     * 下单
     * @param goodsId rooms 表ID
     * @param account 用户 手机号或email
     * @param clients 住客 格式： name:mobile:depart:job,name:mobile:depart:job,name:mobile:depart:job  depart和job可不填 name:mobile,name:mobile
     * @param rooms 房间数量
     * @param startDate 入住时间
     * @param endDate 退房时间
     * @param ticket 是否需要发票 1需要 0不需要
     * @return
     */
    @RequestMapping(value = "/order/place", method = RequestMethod.POST, params = "version=1.0")
    @ResponseBody
    public RestResult placeOrder(@RequestParam(required = true)Integer goodsId, @RequestParam(required = true)String account,
                                 @RequestParam(required = true)String clients, @RequestParam(required = true)Integer rooms,
                                 @RequestParam(required = true)String startDate,@RequestParam(required = true) String endDate,
                                 @RequestParam(defaultValue = "0")Integer ticket, @RequestParam(required = true)String roomIdentity) throws ApiException {
        if(goodsId <= 0){
            throw new ApiException("goodsId必须大于0", 400);
        }
        if(rooms <= 0){
            throw new ApiException("房间数量必须大于0 [rooms]", 400);
        }
        if(ticket != 1 && ticket != 0){
            throw new ApiException("ticket只能是1或0，1表示需要发票，0表示不需要发票", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        User user = userService.getUser(account, accessUser.getCompanyId());
        if(user == null){
            throw new ApiException("公司没有该用户帐号", 400);
        }
        Date d1 = DateUtils.parseDate(startDate, "yyyy-MM-dd");
        Date d2 = DateUtils.parseDate(endDate, "yyyy-MM-dd");
        if(d1 == null || d2 == null){
            throw new ApiException("日期格式不正确", 400);
        }
        if(d1.getTime() <= System.currentTimeMillis()){
            throw new ApiException("入住时间不能小于等于今天", 400);
        }
        if(d2.getTime() <= d1.getTime()){
            throw new ApiException("离店时间必须大于入住时间", 400);
        }
        long day = (d2.getTime() - d1.getTime())/(3600*24*1000);
        List<Map<String, String>> clientList = clientsAnalyze(clients);
        Ordered ordered = hotelOrderService.placeOrder(goodsId, user, clientList, rooms, startDate, endDate, day, ticket, roomIdentity);

        return RestResult.SUCCESS().put("order", ordered);
    }

    @RequestMapping(value = "/order/get", method = RequestMethod.GET, params = "version=1.0")
    @ResponseBody
    public RestResult getOrder(Integer id){
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        Ordered ordered = hotelOrderService.getOrderById(id, accessUser.getCompanyId());
        return RestResult.SUCCESS().put("order", ordered);
    }

    /**
     * 订单查询
     * @param pageNum 第几页
     * @param pageSize 一页几条
     * @param startDate 大于startDate的下单时间
     * @param endDate 小于endDate的下单时间
     * @param status 状态 可以添多个，逗号分开 1,2,3
     * @param keyword 订单 标题 搜索
     * @return
     */
    @RequestMapping(value = "/order/list", method = RequestMethod.GET, params = "version=1.0")
    @ResponseBody
    public RestResult orderList(Integer pageNum, Integer pageSize, String startDate, String endDate, String status, String keyword) throws ApiException {
        Paginator p = new DefaultPaginator();
        if(pageNum != null){
            p.setPageNum(pageNum);
        }
        if(pageSize != null){
            p.setPageSize(pageSize);
        }
        Integer d1 = null;
        if(StringUtils.isNotBlank(startDate)){
            Date date = DateUtils.parseDate(startDate, "yyyy-MM-dd");
            if(date == null){
                throw new ApiException("日期格式不正确", 400);
            }
            d1 = (int)(date.getTime()/1000);
        }
        Integer d2 = null;
        if(StringUtils.isNotBlank(endDate)){
            Date date = DateUtils.parseDate(endDate, "yyyy-MM-dd");
            if(date == null){
                throw new ApiException("日期格式不正确", 400);
            }
            d2 = (int)(date.getTime()/1000);
        }
        if(StringUtils.isNotBlank(status)){
            String [] args = status.split(",");
            for(String s : args){
                if(!NumberUtils.isNumber(s)){
                    throw new ApiException("status参数填写不正确", 400);
                }
            }
            if(args.length > 10){
                throw new ApiException("status参数填写不正确", 400);
            }
        }
        Paginator paginator = hotelOrderService.getOrderList(d1, d2, status, keyword, p);
        return RestResult.SUCCESS().put("orderList", paginator.getResults()).put("page", paginator);
    }

    @RequestMapping(value = "/order/cancel", method = RequestMethod.POST, params = "version=1.0")
    @ResponseBody
    public RestResult cancelOrder(@RequestParam(required = true)Integer id){
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        Ordered order = new Ordered();
        order.setId(id);
        order.setCompanyId(accessUser.getCompanyId());
        order.setStatus(5);
        int c = hotelOrderService.updateOrder(order);
        return RestResult.SUCCESS().put("result", c);
    }

    private List<Map<String, String>> clientsAnalyze(String clients) throws ApiException {
        String[] cs = clients.split(",");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for(String c : cs){
            Map<String, String> map = new LinkedHashMap<String, String>();
            String [] args = c.split(":");
            if(args.length<2){
                throw new ApiException("clients参数格式不正确", 400);
            }
            for(int i=0; i<args.length; i++){
                if(i == 0){
                    map.put("name", args[i]);
                }else if(i == 1){
                    map.put("mobile", args[i]);
                }else if(i == 2){
                    map.put("department", args[i]);
                }else if(i == 3){
                    map.put("job", args[i]);
                }
            }
            list.add(map);
        }
        return list;
    }

}
