package cn.yidukeji.service;

import cn.yidukeji.core.Paginator;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-14
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public interface HotelOrderService {

    public Paginator search(String cityName, String startDate, String endDate, Integer priceClass, String keyword, Paginator paginator);

}
