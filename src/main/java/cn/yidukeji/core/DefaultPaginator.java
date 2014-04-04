package cn.yidukeji.core;

import java.util.List;

/**
 * @author xinwei.zhang
 * @version 2010-7-9 下午01:39:03
 */
public class DefaultPaginator implements Paginator {

	private long pageNumber = DEFAULT_PAGE_NUMBER;
	
	private long pageSize = DEFAULT_PAGE_SIZE;
	
	private long totalCount;
	
	@SuppressWarnings("rawtypes")
	private List results;

	public DefaultPaginator(){}
	
	public DefaultPaginator(int pageSize){
		this.pageSize = pageSize;
	}
	
	@Override
	public long getNextPage() {
		if(isHasNext())
			return pageNumber + 1;
		return pageNumber;
	}

	@Override
	public long getPageNumber() {
		return this.pageNumber;
	}

	@Override
	public long getPrePage() {
		if(isHasPre())
			return pageNumber - 1;
		return pageNumber;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getResults() {
		return results;
	}

	@Override
	public long getTotalCount() {
		return totalCount;
	}

	@Override
	public long getTotalPages() {
		if (totalCount < 0)
			return -1;

		int count = (int) (totalCount / pageSize);
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isHasNext() {
		if(pageNumber<getTotalPages())
			return true;
		return false;
	}

	@Override
	public boolean isHasPre() {
		if(pageNumber>1)
			return true;
		return false;
	}

	@Override
	public void setPageNumber(long pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public long getPageSize() {
		return pageSize;
	}

	@SuppressWarnings("rawtypes")
	public void setResults(List results) {
		this.results = results;
	}

	@Override
	public long getFirstResult() {
        long pageIndex = getPageNumber() - 1;
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		return pageIndex * getMaxResults();
	}

	@Override
	public long getMaxResults() {
		return pageSize;
	}

}
