package com.momathink.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import com.momathink.common.constants.DictKeys;

/**
 * 分页封装
 * @author David
 */
public class SplitPage implements Serializable {

	private static final long serialVersionUID = -7914983945613661637L;
	
	// 分页查询参数
	protected Map<String, String> queryParam;// 查询条件
	protected String orderColunm;// 排序条件
	protected String orderMode;// 排序方式
	protected int pageNumber = DictKeys.default_pageNumber;// 第几页
	protected int pageSize = DictKeys.default_pageSize;// 每页显示几多
	
	// jfinal数据库分页相关
	protected Page<?> page;
	
	// lucene分页相关
	protected List<?> list;				// list result of this page
	protected int totalPage;			// total page
	protected int totalRow;				// total row

	// 辅助分页属性
	protected int currentPageCount;// 当前页记录数量
	protected boolean isFirst;// 是否第一页
	protected boolean isLast;// 是否最后一页
	
	/**
	 * 分页计算
	 */
	public void compute() {
		getTotalPage();

		this.currentPageCount = this.list.size();// 当前页记录数

		if (pageNumber == 1) {
			this.isFirst = true;
		} else {
			this.isFirst = false;
		}

		if (pageNumber == totalPage) {
			this.isLast = true;
		} else {
			this.isLast = false;
		}
	}

	public int getTotalPage() {
		if ((this.totalRow % this.pageSize) == 0) {
			this.totalPage = this.totalRow / this.pageSize;// 计算多少页
		} else {
			this.totalPage = this.totalRow / this.pageSize + 1;// 计算多少页
		}
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public Page<?> getPage() {
		return page;
	}
	public void setPage(Page<?> page) {
		this.page = page;
	}
	public Map<String, String> getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(Map<String, String> queryParam) {
		this.queryParam = queryParam;
	}
	public String getOrderColunm() {
		return orderColunm;
	}
	public void setOrderColunm(String orderColunm) {
		this.orderColunm = orderColunm;
	}
	public String getOrderMode() {
		return orderMode;
	}
	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}
	public int getPageNumber() {
		if(pageNumber <= 0){
			pageNumber = DictKeys.default_pageNumber;
		}
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		if(pageSize <= 0){
			pageSize = DictKeys.default_pageSize;
		}
		if(pageSize > 200){
			pageSize = DictKeys.default_pageSize;
		}
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
