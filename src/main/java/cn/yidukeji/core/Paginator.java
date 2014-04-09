package cn.yidukeji.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @author xinwei.zhang
 * @version 2010-7-9 下午01:36:59
 */
public interface Paginator {

	/**
	 * 默认开始页
	 */
	int DEFAULT_PAGE_NUMBER = 1;
	
	/**
	 * 默认每页记录数
	 */
	int DEFAULT_PAGE_SIZE = 50;
	
	/**
	 * 一共多少页数
	 * @return 
	 */
    long getTotalPages();
	
	/**
	 * 当前第几页
	 * @return 
	 */
    long getPageNum();
	
	/**
	 * 设置当前转到第几页
	 * @param pageNum
	 */
	void setPageNum(long pageNum);
	
	/**
	 * 设置一页显示多少行
	 * @param pageSize
	 */
	void setPageSize(long pageSize);
	/**
	 * 一页显示多少行
	 * @return
	 */
    long getPageSize();
	/**
	 * 总条数
	 * @return 
	 */
	long getTotalCount();
	
	/**
	 * 设置总条数
	 * @param totalCount
	 */
	void setTotalCount(long totalCount);
	
	/**
	 * 是否有上一页
	 * @return
	 */
    @JsonIgnore
	boolean isHasPre();
	
	/**
	 * 得到上一页的页数
	 * @return
	 */
    @JsonIgnore
    long getPrePage();
	
	/**
	 * 是否有下一页
	 * @return
	 */
    @JsonIgnore
	boolean isHasNext();
	
	/**
	 * 得到下一页的页数
	 * @return
	 */
    @JsonIgnore
    long getNextPage();
	
	/**
	 * 返回分页后的数据
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    @JsonIgnore
	List getResults();
	
	/**
	 * 设置分页数据
	 * @param results
	 */
	@SuppressWarnings("rawtypes")
	void setResults(List results);
	
	/**
	 * 从哪一行开始
	 * @return
	 */
    @JsonIgnore
    long getFirstResult();
	
	/**
	 * 共显示多少条
	 * @return
	 */
    @JsonIgnore
    long getMaxResults();
	
}
