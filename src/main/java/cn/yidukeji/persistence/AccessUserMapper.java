package cn.yidukeji.persistence;

import cn.yidukeji.bean.AccessUser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-4
 * Time: 上午10:50
 * To change this template use File | Settings | File Templates.
 */
public interface AccessUserMapper {

    public List<AccessUser> getAccessUserList();

}
