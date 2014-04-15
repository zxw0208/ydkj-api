package cn.yidukeji.persistence;

import cn.yidukeji.bean.Hotel;
import cn.yidukeji.bean.Rooms;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-15
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public interface HotelMapper {

    public Rooms getRooms(Integer id);

    public Hotel getCtrip(Integer id);

    public Hotel getElong(Integer id);

    public Hotel getU17(Integer id);

}
