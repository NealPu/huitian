package com.momathink.sys.operator.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/***
 * 顶部导航
 */
@Table(tableName="pt_systems")
public class Systems extends BaseModel<Systems> {
	private static final long serialVersionUID = 2553505654790525730L;
	public static final Systems dao = new Systems();
	
	/** 获取顶部导航 标题  */
	public List<Systems> queryAll() {
		return dao.find("SELECT * FROM pt_systems ORDER BY orderids");
	}

}
