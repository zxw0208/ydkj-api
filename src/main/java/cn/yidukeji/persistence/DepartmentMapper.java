package cn.yidukeji.persistence;

import cn.yidukeji.bean.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-4
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public interface DepartmentMapper {

    public int findDepartmentListCount(Integer companyId);

    public List<Department> findDepartmentList(@Param("companyId")Integer companyId, @Param("first")Long first, @Param("max")Long max);

    public int insertDepartment(Department department);

    public int updateDepartment(Department department);

    public Department getDepartmentById(@Param("id")Integer id, @Param("companyId")Integer companyId);

}
