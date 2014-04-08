package cn.yidukeji.bean;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-4
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class Department {
    private Integer id;
    private Integer companyId;
    private Integer parentId;
    @NotNull(message = "部门名称不能为空")
    private String name;
    @NotNull(message = "部门描述不能为空")
    private String description;
    private Integer status;
    private Integer ctime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
