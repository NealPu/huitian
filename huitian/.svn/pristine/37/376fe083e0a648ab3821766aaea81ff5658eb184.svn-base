package com.momathink.teaching.course.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;
@Table(tableName="course")
public class Course extends BaseModel<Course> {
	private static final long serialVersionUID = -7303659999774959862L;
	public static final Course dao = new Course();
	public List<Course> findBySubjectId(Integer subjectId) {
		return dao.find("SELECT c.*,s.subject_name FROM course c LEFT JOIN `subject` s ON c.subject_id=s.id WHERE c.state = 0 and c.subject_id=?", subjectId);
	}
	
	public List<Course> getVIPStuCourse(String[] arrCourse){
		List<Course> lists = new ArrayList<Course>();
		for(int i=0;i<arrCourse.length;i++){
			String sql = "select * from course where state = 0 and id = ?";
			Course course = dao.findFirst(sql,arrCourse[i]);
			lists.add(course);
		}
		for(int j=0;j<lists.size();j++){
			if(lists.get(j)==null){
				lists.remove(j);
			}
		}
		return lists;
	}
	public Long queryCourseCount(String field, String value, String courseId, String subjectId) {
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			StringBuffer sql = new StringBuffer("select count(1) from course where subject_id=? and state=0 and ");
			sql.append(field).append("='").append(value).append("'");
			if (!ToolString.isNull(courseId)) {
				sql.append(" and id != ").append(courseId);
			}
			return Db.queryLong(sql.toString(),subjectId);
		} else {
			return null;
		}
	}

	public List<Course> getStuCourses(String stuId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCourseNameById(String courseId) {
		if(StringUtils.isEmpty(courseId)){
			return null;
		}else{
			Course course = dao.findById(Integer.parseInt(courseId));
			return course.getStr("course_name");
		}
	}

	public List<Course> getCourses() {
		return dao.find("select * from course where state=0");
	}

	public Course getCourseByName(String coursename) {
		String sql = "select * from course where course_name = ? ";
		Course course = Course.dao.findFirst(sql, coursename);
		return course;
	}
	
	public List<Course> getCourseBySubjectId(String subId){
		String sql = "select * from course where SUBJECT_ID = ? and SUBJECT_ID != 0 ";
		List<Course> list = dao.find(sql, subId);
		return list;
	}
	
	public List<Course> getCourseBySubjectIds(String subids){
		String sql = "select Id courseid,COURSE_NAME coursename from course where SUBJECT_ID in ( "+subids+ " ) and state = 0 and SUBJECT_ID !=0 " ;
		return dao.find(sql);
	}
	
	/**
	 * 学生订单中科目下所有课程
	 * @param stuid
	 * @return
	 */
	public List<Course> findStudentOrdersList(String stuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT distinct course.course_name,course.Id courseid FROM course WHERE FIND_IN_SET(course.SUBJECT_ID ,(SELECT REPLACE (GROUP_CONCAT(DISTINCT subjectids),'|',',') subids FROM crm_courseorder WHERE teachtype=1 and studentid=").append(stuid).append(")) AND course.STATE = 0");
		return dao.find(sb.toString());
	}

	public List<Course> getTeacherCourse(String ids) {
		String str = " select * from course where state=0 and  id in ("+ids+")";
		return dao.find(str);
	}

	/**
	 * 根据SubjectIds获取课程
	 * @author David
	 * @param subjcetIds：使用,分割如1,2,3,4
	 * @return
	 * @since JDK 1.7
	 */
	public  List<Course> findBySubjectIds(String subjectIds) {
		String str = " select * from course where state=0 and subject_id in ("+subjectIds+")";
		return dao.find(str);
	}
}
