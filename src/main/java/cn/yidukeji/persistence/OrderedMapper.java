package cn.yidukeji.persistence;

import cn.yidukeji.bean.Ordered;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-17
 * Time: 上午9:46
 * To change this template use File | Settings | File Templates.
 */
public interface OrderedMapper {

    public List<Ordered> findOrderList(@Param("startDate")Integer startDate, @Param("endDate")Integer endDate,
                                       @Param("status")String status, @Param("keyword")String keyword,
                                       @Param("companyId")Integer companyId, @Param("first")Long first, @Param("max")Long max);

    public int findOrderListCount(@Param("startDate")Integer startDate, @Param("endDate")Integer endDate,
                                  @Param("status")String status, @Param("keyword")String keyword,@Param("companyId")Integer companyId);

    public int insertOrder(Ordered ordered);

    public int updateOrder(Ordered ordered);

    public Ordered getOrderById(@Param("id")Integer id, @Param("companyId")Integer companyId);

}
