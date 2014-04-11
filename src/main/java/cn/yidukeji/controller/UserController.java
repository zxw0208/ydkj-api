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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RestResult add(User user) throws ApiException {
        user.setId(null);
        user.setAccount(null);
        if(!ValidateUtils.emailValidate(user.getEmail())){
            return RestResult.ERROR_400().put("error", "email格式不正确");
        }
        if(!ValidateUtils.mobileValidate(user.getMobile())){
            return RestResult.ERROR_400().put("error", "手机格式不正确");
        }
        try {
            if(StringUtils.isNotBlank(IdCardUtils.IDCardValidate(user.getIdentification()))){
                return RestResult.ERROR_400().put("error", "身份证格式不正确");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return RestResult.ERROR_400().put("error", "身份证格式不正确");
        }
        BeanValidation.validate(user);
        int c = userService.addUser(user);
        return RestResult.SUCCESS().put("user", user).put("result", c);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RestResult update(User user) throws ApiException {
        user.setAccount(null);
        if(StringUtils.isNotBlank(user.getEmail()) && !ValidateUtils.emailValidate(user.getEmail())){
            return RestResult.ERROR_400().put("error", "email格式不正确");
        }
        if(StringUtils.isNotBlank(user.getMobile()) && !ValidateUtils.mobileValidate(user.getMobile())){
            return RestResult.ERROR_400().put("error", "手机格式不正确");
        }
        try {
            if(StringUtils.isNotBlank(user.getIdentification()) && StringUtils.isNotBlank(IdCardUtils.IDCardValidate(user.getIdentification()))){
                return RestResult.ERROR_400().put("error", "身份证格式不正确");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return RestResult.ERROR_400().put("error", "身份证格式不正确");
        }
        int c = userService.updateUser(user);
        return RestResult.SUCCESS().put("result", c);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public RestResult get(Integer id){
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        if(id == null || id == 0){
            return RestResult.ERROR_400().put("error", "ID不能为空");
        }
        User user = userService.getUser(id, accessUser.getCompanyId());
        return RestResult.SUCCESS().put("user", user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RestResult delete(Integer id) throws ApiException {
        int c = userService.delUser(id);
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
        Paginator paginator = userService.userList(p);
        return RestResult.SUCCESS().put("userList", paginator.getResults()).put("page", paginator);
    }

}
