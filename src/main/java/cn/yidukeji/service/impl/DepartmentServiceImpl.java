package cn.yidukeji.service.impl;

import cn.yidukeji.bean.Department;
import cn.yidukeji.persistence.DepartmentMapper;
import cn.yidukeji.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void addDepartment(Department department) {
        departmentMapper.insertDepartment(department);
    }

    @Override
    public Department getDepartmentById(Integer id, Integer companyId) {
        return departmentMapper.getDepartmentById(id, companyId);
    }

    @Override @Transactional
    public void updateDepartment(Department department) {
        departmentMapper.updateDepartment(department);
    }
}
