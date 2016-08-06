package com.momathink.sys.account.model;

import java.util.ArrayList;
import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.teaching.course.model.CoursePlan;

@Table(tableName="banci_course")
public class BanciCourse extends BaseModel<BanciCourse> {
	private static final long serialVersionUID = 1L;
	public static final BanciCourse dao = new BanciCourse();
	
	/**
	 * 获取班次的课程信息
	 * @author David
	 * @param classTypeId
	 * @param classId
	 */
	public List<BanciCourse> getCourseList(Integer classTypeId, Integer classId) {
		String typeSql = "select bc.*,c.COURSE_NAME from banci_course bc left join course c on bc.course_id=c.id where bc.type_id=? and bc.banci_id=0";
		List<BanciCourse> typeCourseList = dao.find(typeSql, classTypeId);
		String banciSql = "select bc.*,c.COURSE_NAME from banci_course bc left join course c on bc.course_id=c.id where bc.banci_id=?";
		List<BanciCourse> banciCourseList = dao.find(banciSql, classId);
		List<BanciCourse> courseList = new ArrayList<BanciCourse>();
		for(BanciCourse tc : typeCourseList){
			for(BanciCourse bc : banciCourseList){//获取班次课程数和已排课程数
				if(tc.getInt("course_id").equals(bc.getInt("course_id"))){
					tc.set("Id", bc.getInt("Id"));
					tc.set("lesson_num", bc.getInt("LESSON_NUM"));
					tc.put("coursePlanCount", CoursePlan.coursePlan.getClassYpkcClasshour(classId,bc.getInt("course_id")));
					break;
				}else{
					tc.set("lesson_num", 0);
					tc.put("coursePlanCount", 0);
				}
				boolean flag = false;
				for(BanciCourse typeCourse : typeCourseList){
					if(bc.getInt("course_id").equals(typeCourse.getInt("course_id"))){
						flag = true;
						break;
					}
				}
				if(!flag){
					bc.put("coursePlanCount", CoursePlan.coursePlan.getClassYpkcClasshour(classId,bc.getInt("course_id")));
					courseList.add(bc);
				}
			}
			courseList.add(tc);
		}
		return courseList;
	}

	/**
	 * 根据班次ID获取班次的课程
	 * @author David
	 * @param classOrderId
	 */
	public List<BanciCourse> findByClassOrderId(Integer classOrderId) {
		String sql = "select bc.*,c.COURSE_NAME from banci_course bc LEFT JOIN course c ON bc.course_id=c.Id WHERE bc.banci_id=? and lesson_num!=0";
		List<BanciCourse> banciCourseList = dao.find(sql, classOrderId);
		for(BanciCourse bc : banciCourseList){//获取班次课程数和已排课程数
			bc.put("coursePlanCount", CoursePlan.coursePlan.getClassYpkcClasshour(classOrderId,bc.getInt("course_id")));
		}
		return banciCourseList;
	}

}
