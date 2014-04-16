package cn.yidukeji.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-15
 * Time: 下午1:19
 * To change this template use File | Settings | File Templates.
 */
public class Rooms {
    private Integer id;
    private String hotelId;
    private Double minPrice;
    private Double maxPrice;
    private String data;
    private Integer indexed;
    private List<Room> roomList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    @JsonIgnore
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JsonIgnore
    public Integer getIndexed() {
        return indexed;
    }

    public void setIndexed(Integer indexed) {
        this.indexed = indexed;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
