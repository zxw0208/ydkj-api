package cn.yidukeji.service;

import cn.yidukeji.bean.Department;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-8
 * Time: 上午10:34
 * To change this template use File | Settings | File Templates.
 */
public interface DepartmentService {

    public int addDepartment(Department department) throws ApiException;

    public Department getDepartmentById(Integer id, Integer companyId);

    public int updateDepartment(Department department) throws ApiException;

    public int delDepartment(Integer id) throws ApiException;

    public Paginator departmentList(Paginator paginator);

}
