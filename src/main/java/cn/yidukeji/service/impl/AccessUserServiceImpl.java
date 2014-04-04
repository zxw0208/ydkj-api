package cn.yidukeji.service.impl;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.persistence.AccessUserMapper;
import cn.yidukeji.service.AccessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-4
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AccessUserServiceImpl implements AccessUserService {

    @Autowired
    private AccessUserMapper accessUserMapper;

    @Override
    public List<AccessUser> getAccessUserList() {
        return accessUserMapper.getAccessUserList();
    }
}
