package com.momathink.teaching.teacher.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="teacherrest")
public class TeacherRest extends BaseModel<TeacherRest> {
	public static final TeacherRest dao = new TeacherRest();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 获取某个老师一周的休息时间
	 * @param tid
	 * @return
	 */
	public List<TeacherRest> getRestDay(String tid) {
		String sql = "select tr.*,t.REAL_NAME tname from teacherrest tr left join account t on t.id = tr.teacherid where tr.teacherid = ? order by tr.creattime asc ";
		List<TeacherRest> teacher = dao.find(sql, tid);
		return teacher;
	}



}
