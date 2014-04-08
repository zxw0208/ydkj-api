package cn.yidukeji.service;

import cn.yidukeji.bean.Department;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-8
 * Time: 上午10:34
 * To change this template use File | Settings | File Templates.
 */
public interface DepartmentService {

    public void addDepartment(Department department);

    public Department getDepartmentById(Integer id, Integer companyId);

    public void updateDepartment(Department department);

}
