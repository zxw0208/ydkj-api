package cn.yidukeji.controller;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.bean.Department;
import cn.yidukeji.core.DefaultPaginator;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.service.DepartmentService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.BeanValidation;
import cn.yidukeji.utils.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-8
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RestResult add(Department department) throws ApiException {
        BeanValidation.validate(department);
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        department.setCompanyId(accessUser.getCompanyId());
        department.setStatus(0);
        department.setCtime((int)(System.currentTimeMillis()/1000));
        if(department.getParentId() == null){
            department.setParentId(0);
        }
        if(department.getParentId() > 0){
            Department d = departmentService.getDepartmentById(department.getParentId(), accessUser.getCompanyId());
            if(d == null){
                return RestResult.ERROR_400().put("error", "上级部门不存在");
            }
        }
        departmentService.addDepartment(department);
        return RestResult.SUCCESS().put("department", department);
    }

    public RestResult update(Department department){
        if(department.getId() == null){
            return RestResult.ERROR_400().put("error", "id不能为空");
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        if(department.getParentId() != null && department.getParentId() > 0){
            Department d = departmentService.getDepartmentById(department.getParentId(), accessUser.getCompanyId());
            if(d == null){
                return RestResult.ERROR_400().put("error", "上级部门不存在");
            }
        }
        return RestResult.SUCCESS();
    }
}
