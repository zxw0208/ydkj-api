package cn.yidukeji.persistence;

import cn.yidukeji.bean.Category;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-17
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryMapper {

    public Category getCategory(String unikey);

}
