package com.momathink.teaching.grade.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "gradedetail")
public class GradeDetail extends BaseModel<GradeDetail> {

	private static final long serialVersionUID = 7996762001003047294L;
	public static final GradeDetail dao = new GradeDetail();
	
	public void deleteByRecordId(Integer recordId) {
		Db.update("DELETE FROM gradedetail WHERE gradedetail.recordid=?",recordId );
	}

	public List<GradeDetail> findbyRecordId(String recordId) {
		return StringUtils.isEmpty(recordId)?null:dao.find("SELECT * FROM gradedetail WHERE recordid = ?",Integer.parseInt(recordId));
	}

}
