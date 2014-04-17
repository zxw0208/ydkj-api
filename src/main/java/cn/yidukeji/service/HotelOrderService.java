package cn.yidukeji.service;

import cn.yidukeji.bean.Hotel;
import cn.yidukeji.bean.Ordered;
import cn.yidukeji.bean.Rooms;
import cn.yidukeji.bean.User;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-14
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public interface HotelOrderService {

    public Paginator search(String cityName, String startDate, String endDate, Integer priceClass, String keyword, Paginator paginator) throws ApiException;

    public Rooms getRooms(Integer id);

    public Hotel getHotel(String id);

    public Ordered placeOrder(Integer goodsId, User user, List<Map<String, String>> clientList, Integer rooms, String startDate, String endDate, Long day, Integer ticket, String roomIdentity) throws ApiException;

    public Ordered getOrderById(Integer id, Integer companyId);

    public Paginator getOrderList(Integer startDate, Integer endDate, String status, String keyword, Paginator paginator);

    public int updateOrder(Ordered order);

}
