package cn.yidukeji.service.impl;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.bean.Department;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.persistence.DepartmentMapper;
import cn.yidukeji.service.DepartmentService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-8
 * Time: 上午10:34
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override @Transactional
    public int addDepartment(Department department) throws ApiException {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        department.setCompanyId(accessUser.getCompanyId());
        department.setStatus(0);
        department.setCtime((int)(System.currentTimeMillis()/1000));
        if(department.getParentId() == null){
            department.setParentId(0);
        }
        if(department.getParentId() > 0){
            Department d = getDepartmentById(department.getParentId(), accessUser.getCompanyId());
            if(d == null){
               throw new ApiException("上级部门不存在", 400);
            }
        }
        return departmentMapper.insertDepartment(department);
    }

    @Override
    public Department getDepartmentById(Integer id, Integer companyId) {
        return departmentMapper.getDepartmentById(id, companyId);
    }

    @Override @Transactional
    public int updateDepartment(Department department) throws ApiException {
        if(department.getId() == null){
            throw new ApiException("id不能为空", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        if(department.getParentId() != null && department.getParentId() > 0){
            if(department.getParentId().equals(department.getId())){
                throw new ApiException("上级部门不能是当前部门", 400);
            }
            Department d = getDepartmentById(department.getParentId(), accessUser.getCompanyId());
            if(d == null){
                throw new ApiException("上级部门不存在", 400);
            }
        }
        department.setCompanyId(accessUser.getCompanyId());
        department.setStatus(null);
        return departmentMapper.updateDepartment(department);
    }

    @Override
    public int delDepartment(Integer id) throws ApiException {
        if(id == null){
            throw new ApiException("id不能为空", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        Department d = new Department();
        d.setId(id);
        d.setStatus(1);
        d.setCompanyId(accessUser.getCompanyId());
        return departmentMapper.updateDepartment(d);
    }

    @Override
    public Paginator departmentList(Paginator paginator) {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        List<Department> list = departmentMapper.findDepartmentList(accessUser.getCompanyId(), paginator.getFirstResult(), paginator.getMaxResults());
        paginator.setResults(list);
        int count = departmentMapper.findDepartmentListCount(accessUser.getCompanyId());
        paginator.setTotalCount(count);
        return paginator;
    }
}
