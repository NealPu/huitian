package com.momathink.teaching.teacher.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;


@Table(tableName = "teachergrade_update")
public class GradeUpdate extends BaseModel<GradeUpdate> {
	private static final long serialVersionUID = 1L;
	public static final GradeUpdate dao = new GradeUpdate();
	public GradeUpdate getPointGradeByPointIdUpdate(String pointid) {
		String sql = "select * from teachergrade_update where pointid = ? ";
		return dao.findFirst(sql, pointid);
	}
	public GradeUpdate getPointGradeByPointId(String pointid) {
		String sql = "select * from teachergrade_update where pointid = ? ";
		return dao.findFirst(sql, pointid);
	}
	
}
