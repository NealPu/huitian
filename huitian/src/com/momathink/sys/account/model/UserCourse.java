package com.momathink.sys.account.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;
import com.momathink.teaching.course.model.Course;
@Table(tableName="user_course")
public class UserCourse extends BaseModel<UserCourse> {
	private static final long serialVersionUID = 1L;
	public static final UserCourse dao = new UserCourse();
	public List<UserCourse> findByStudentId(String accountId) {
		return ToolString.isNull(accountId)?null:UserCourse.dao.find("SELECT uc.subject_id,uc.course_id,c.COURSE_NAME FROM user_course uc LEFT JOIN course c ON uc.course_id=c.Id WHERE uc.account_id=? ORDER BY uc.subject_id",accountId);
	}
	public List<UserCourse> findHasSubjecByStudentId(String accountId) {
		return ToolString.isNull(accountId)?null:UserCourse.dao.find("SELECT uc.subject_id FROM user_course uc WHERE uc.account_id=? GROUP BY uc.subject_id",accountId);
	}
	public List<UserCourse> findByAccountId(String accountId) {
		return ToolString.isNull(accountId)?null:UserCourse.dao.find("SELECT * FROM user_course uc WHERE uc.account_id=? ",Integer.parseInt(accountId));
	}
	/**
	 * 获取学生对应ids
	 * @param stuid
	 * @return
	 */
	public String getStudentCourseids(String stuid) {
		String sql="SELECT GROUP_CONCAT( DISTINCT course_id SEPARATOR '|') courseids FROM user_course WHERE account_id = ? ";
		UserCourse ucourse = dao.findFirst(sql, stuid);
		return ucourse==null?"":ucourse.getStr("courseids");
	}
	
	/**
	 * 获取学生已选择的课程
	 * @param stuid
	 * @return
	 */
	public List<Course> getStudentUserCourse(String stuid){
		String sql = "SELECT c.* FROM user_course uc LEFT JOIN course c ON uc.course_id=c.Id WHERE uc.account_id=?";
		return Course.dao.find(sql, Integer.parseInt(stuid));
	}
	
	/**
	 * 根据学生ID和科目ID获取课程
	 * @param studentId学生ID
	 * @param subjectIds科目ID,格式1,2,3
	 * @author David
	 */
	public Object findByStudentIdAndSubjectIds(Integer studentId, String subjectIds) {
		String sql = "select uc.id,uc.account_id,c.COURSE_NAME,c.SUBJECT_ID from user_course uc left join course c on uc.course_id = c.id WHERE uc.account_id=? and c.subject_id in(?)";
		return dao.find(sql, studentId,subjectIds);
	}
	
	/**
	 * 根据学生ID科目ID获取课程ID
	 * @author David
	 * @param studentId学生ID
	 * @param subjectIds科目ID：1,2,3
	 * @return
	 */
	public String getStudentCourseids(String studentId, String subjectIds) {
		String sql="select GROUP_CONCAT( DISTINCT course_id SEPARATOR '|') courseids from user_course uc left join course c on uc.course_id = c.id WHERE uc.account_id=? and c.subject_id in(?)";
		UserCourse ucourse = dao.findFirst(sql, studentId,subjectIds);
		return ucourse==null?"":ucourse.getStr("courseids");
	}
	
	/**
	 * 根据学生ID删除学生的课程
	 * @author David
	 * @param studentId
	 */
	public void deleteByStudentId(Integer studentId) {
		Db.update("DELETE FROM user_course WHERE account_id=?", studentId);
	}
	
	/**
	 * 根据学生ID和课程ID获取学生的课程
	 * @param studentId
	 * @param coruseId
	 * @return
	 */
	public UserCourse findByStudentIdAndCourseId(Integer studentId, Integer courseId) {
		return UserCourse.dao.findFirst("SELECT * FROM user_course uc WHERE uc.account_id=? and course_id=?",studentId,courseId);
	}
	
}
