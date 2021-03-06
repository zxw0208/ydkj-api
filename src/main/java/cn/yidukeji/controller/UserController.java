package cn.yidukeji.controller;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.bean.User;
import cn.yidukeji.core.DefaultPaginator;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.service.UserService;
import cn.yidukeji.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-10
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, params = "version=1.0")
    @ResponseBody
    public RestResult add(User user) throws ApiException {
        user.setId(null);
        user.setAccount(null);
        BeanValidation.validate(user);
        if(!ValidateUtils.emailValidate(user.getEmail())){
            throw new ApiException("email格式不正确", 400);
        }
        if(!ValidateUtils.mobileValidate(user.getMobile())){
            throw new ApiException("手机格式不正确", 400);
        }
        try {
            if(StringUtils.isNotBlank(IdCardUtils.IDCardValidate(user.getIdentification()))){
                throw new ApiException("身份证格式不正确", 400);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApiException("身份证格式不正确", 400);
        }
        int c = userService.addUser(user);
        return RestResult.SUCCESS().put("user", user).put("result", c);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, params = "version=1.0")
    @ResponseBody
    public RestResult update(User user) throws ApiException {
        user.setAccount(null);
        if(StringUtils.isNotBlank(user.getEmail()) && !ValidateUtils.emailValidate(user.getEmail())){
            throw new ApiException("email格式不正确", 400);
        }
        if(StringUtils.isNotBlank(user.getMobile()) && !ValidateUtils.mobileValidate(user.getMobile())){
            throw new ApiException("手机格式不正确", 400);
        }
        try {
            if(StringUtils.isNotBlank(user.getIdentification()) && StringUtils.isNotBlank(IdCardUtils.IDCardValidate(user.getIdentification()))){
                throw new ApiException("身份证格式不正确", 400);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApiException("身份证格式不正确", 400);
        }
        int c = userService.updateUser(user);
        return RestResult.SUCCESS().put("result", c);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET, params = "version=1.0")
    @ResponseBody
    public RestResult get(Integer id) throws ApiException {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        if(id == null || id == 0){
            throw new ApiException("ID不能为空", 400);
        }
        User user = userService.getUser(id, accessUser.getCompanyId());
        return RestResult.SUCCESS().put("user", user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, params = "version=1.0")
    @ResponseBody
    public RestResult delete(Integer id) throws ApiException {
        int c = userService.delUser(id);
        return RestResult.SUCCESS().put("result", c);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = "version=1.0")
    @ResponseBody
    public RestResult list(Integer pageNum, Integer pageSize){
        Paginator p = new DefaultPaginator();
        if(pageNum != null){
            p.setPageNum(pageNum);
        }
        if(pageSize != null){
            p.setPageSize(pageSize);
        }
        Paginator paginator = userService.userList(p);
        return RestResult.SUCCESS().put("userList", paginator.getResults()).put("page", paginator);
    }

}
