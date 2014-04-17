package cn.yidukeji.bean;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-17
 * Time: 下午2:34
 * To change this template use File | Settings | File Templates.
 */
public class Category {

    private Integer id;
    private Integer parentId;
    private String name;
    private String unikey;
    private String description;
    private Integer status;
    private Integer ctime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnikey() {
        return unikey;
    }

    public void setUnikey(String unikey) {
        this.unikey = unikey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCtime() {
        return ctime;
    }

    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }
}
