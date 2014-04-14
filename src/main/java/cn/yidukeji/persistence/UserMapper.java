package cn.yidukeji.persistence;

import cn.yidukeji.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-9
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public interface UserMapper {

    public int findUserListCount(Integer companyId);

    public List<User> findUserList(@Param("companyId")Integer companyId, @Param("first")Long first, @Param("max")Long max);

    public int insertUser(User user);

    public int updateUser(User user);

    public User getUserById(@Param("id")Integer id, @Param("companyId")Integer companyId);

    public int isUnique(@Param("mobile")String mobile, @Param("email")String email, @Param("account")String account);

}
