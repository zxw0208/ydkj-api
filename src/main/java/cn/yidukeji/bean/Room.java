package cn.yidukeji.bean;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-16
 * Time: 上午11:46
 * To change this template use File | Settings | File Templates.
 */
public class Room {
    private String identity;
    private String roomType;
    private String bedType;
    private Double settle;
    private Double price;
    private String breakfast;
    private String note;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public Double getSettle() {
        return settle;
    }

    public void setSettle(Double settle) {
        this.settle = settle;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
