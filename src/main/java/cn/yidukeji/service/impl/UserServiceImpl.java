package cn.yidukeji.service.impl;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.bean.Department;
import cn.yidukeji.bean.User;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.persistence.UserMapper;
import cn.yidukeji.service.DepartmentService;
import cn.yidukeji.service.UserService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.IdCardUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-9
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public int addUser(User user) throws ApiException {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        user.setCompanyId(accessUser.getCompanyId());
        user.setCtime((int)(System.currentTimeMillis()/1000));
        user.setStatus(0);
        user.setAccount(generateAccount());
        user.setSex(IdCardUtils.sex(user.getIdentification()));
        if(StringUtils.isNotBlank(user.getPasswd())){
            user.setPasswd(DigestUtils.md5Hex(user.getPasswd()));
        }else{
            user.setPasswd(DigestUtils.md5Hex("nopassword"));
        }
        if(user.getRole() == null){
            user.setRole(11);
        }else if(user.getRole() != 11 && user.getRole() != 10 ){
            user.setRole(11);
        }
        if(userMapper.isUnique(user.getMobile(), null, null) > 0){
            throw new ApiException("手机号已存在", 400);
        }else if(userMapper.isUnique(null, user.getEmail(), null) > 0){
            throw new ApiException("电子邮件已存在", 400);
        }else if(userMapper.isUnique(null, null, user.getAccount()) > 0){
            throw new ApiException("账户号已存在", 400);
        }
        Department d = departmentService.getDepartmentById(user.getDepartmentId(), accessUser.getCompanyId());
        if(d == null){
            throw new ApiException("部门不存在", 400);
        }
        return userMapper.insertUser(user);
    }

    @Override
    public int updateUser(User user) throws ApiException {
        if(user.getId() == null){
            throw new ApiException("id不能为空", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        User u = getUser(user.getId(), accessUser.getCompanyId());
        if(user.getMobile() != null && !u.getMobile().equals(user.getMobile()) && userMapper.isUnique(user.getMobile(), null, null) > 0){
            throw new ApiException("手机号已存在", 400);
        }else if(user.getEmail() != null && !u.getEmail().equals(user.getEmail()) && userMapper.isUnique(null, user.getEmail(), null) > 0){
            throw new ApiException("电子邮件已存在", 400);
        }
        if(user.getDepartmentId() != null){
            Department d = departmentService.getDepartmentById(user.getDepartmentId(), accessUser.getCompanyId());
            if(d == null){
                throw new ApiException("部门不存在", 400);
            }
        }
        if(StringUtils.isNotBlank(user.getPasswd())){
            user.setPasswd(DigestUtils.md5Hex(user.getPasswd()));
        }
        user.setCompanyId(accessUser.getCompanyId());
        user.setStatus(null);
        return userMapper.updateUser(user);
    }

    @Override
    public User getUser(Integer userId, Integer companyId) {
        return userMapper.getUserById(userId, companyId);
    }

    @Override
    public int delUser(Integer id) throws ApiException {
        if(id == null){
            throw new ApiException("id不能为空", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        User user = new User();
        user.setCompanyId(accessUser.getCompanyId());
        user.setId(id);
        user.setStatus(3);
        return userMapper.updateUser(user);
    }

    @Override
    public Paginator userList(Paginator paginator) {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        List<User> list = userMapper.findUserList(accessUser.getCompanyId(), paginator.getFirstResult(), paginator.getMaxResults());
        paginator.setResults(list);
        int count = userMapper.findUserListCount(accessUser.getCompanyId());
        paginator.setTotalCount(count);
        return paginator;
    }

    private  String generateAccount(){
        return String.valueOf(System.currentTimeMillis());
    }
}
