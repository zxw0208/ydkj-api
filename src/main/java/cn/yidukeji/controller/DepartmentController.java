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
        department.setId(null);
        BeanValidation.validate(department);
        int c = departmentService.addDepartment(department);
        return RestResult.SUCCESS().put("department", department).put("result", c);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RestResult update(Department department) throws ApiException {
        int c = departmentService.updateDepartment(department);
        return RestResult.SUCCESS().put("result", c);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public RestResult get(Integer id){
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        if(id == null || id == 0){
            return RestResult.ERROR_400().put("error", "ID不能为空");
        }
        Department department = departmentService.getDepartmentById(id, accessUser.getCompanyId());
        return RestResult.SUCCESS().put("department", department);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RestResult delete(Integer id) throws ApiException {
        int c = departmentService.delDepartment(id);
        return RestResult.SUCCESS().put("result", c);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public RestResult list(Integer pageNum, Integer pageSize){
        Paginator p = new DefaultPaginator();
        if(pageNum != null){
            p.setPageNum(pageNum);
        }
        if(pageSize != null){
            p.setPageSize(pageSize);
        }
        Paginator paginator = departmentService.departmentList(p);
        return RestResult.SUCCESS().put("departmentList", paginator.getResults()).put("page", paginator);
    }
}
