package cn.yidukeji.service;

import cn.yidukeji.bean.Hotel;
import cn.yidukeji.bean.Rooms;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;

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

}
