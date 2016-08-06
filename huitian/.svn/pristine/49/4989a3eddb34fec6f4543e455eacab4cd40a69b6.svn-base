package com.momathink.finance.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName="sort_pay")
public class Sort extends BaseModel<Sort> {
	private static final long serialVersionUID = 1L;
	public static final Sort dao = new Sort();
	/**
	 * 获取发送催费邮件序号
	 * @return
	 */
	public Sort findBySort() {
		String sql = "select * from sort_pay where today = (select current_date)";
		Sort co = Sort.dao.findFirst(sql);
		return co;
	}
}
