package com.momathink.teaching.grade.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "graderecord")
public class GradeRecord extends BaseModel<GradeRecord> {

	private static final long serialVersionUID = 7996762001003047294L;
	public static final GradeRecord dao = new GradeRecord();
	public List<GradeRecord> findByStudentId(String studentId) {
		return StringUtils.isEmpty(studentId)?null:dao.find("SELECT * FROM graderecord WHERE studentid=?", Integer.parseInt(studentId));
	}

}
