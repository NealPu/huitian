package com.momathink.teaching.course.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
/**
 *  2015年8月19日prq
 * @author prq
 *
 */

@Table(tableName="courseplan_back")
public class CourseBack extends BaseModel<CourseBack> {

	private static final long serialVersionUID = 1L;
	public static final CourseBack dao = new CourseBack();
	/**
	 * 查询已经删除排课的信息*
	 * @param id
	 * @return
	 */
	public CourseBack findByIdToMessage(String id) {
		String sql ="SELECT cb.Id,cb.student_id,cb.teacher_id,cb.room_id, cb.CAMPUS_ID, DATE_FORMAT(cb.COURSE_TIME,'%Y-%m-%d') AS COURSE_TIME,tr.RANK_NAME "
				+ "FROM courseplan_back cb LEFT JOIN time_rank tr ON cb.TIMERANK_ID = tr.Id WHERE cb.Id = ? ";
		return dao.findFirst(sql,id);
	}
	

}
