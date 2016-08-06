package com.momathink.teaching.teacher.model;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "coursecost")
public class Coursecost extends BaseModel<Coursecost> {

	private static final long serialVersionUID = 4846535641711782202L;
	public static final Coursecost dao = new Coursecost();
	/**
	 * 检查是否存在相应字段的数据
	 * @param field
	 * @param value
	 * @param mediatorId
	 * @return
	 */
	public Long queryCount(String date,String tid) {
			return Db.queryLong("select count(1) from coursecost where teacherid = ? and startdate = ? ",tid,date);
	}
}
