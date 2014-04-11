package cn.yidukeji.service;

import cn.yidukeji.bean.User;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-9
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {

    public int addUser(User user) throws ApiException;

    public int updateUser(User user) throws ApiException;

    public User getUser(Integer userId, Integer companyId);

    public int delUser(Integer id) throws ApiException;

    public Paginator userList(Paginator paginator);

}
