package com.momathink.teaching.course.controller;

import java.util.List;
import java.util.Map;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.service.CourseplanService;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.teacher.model.Teacher;

@Controller(controllerKey="/courseplan")
public class CoursePlanController extends BaseController {

	private CourseplanService courseplanService = new CourseplanService();
	
	
	/**
	 * 查看老师的上课信息
	 * */
	public void getTeacherMessage() {
		try{
			Map<String,String> queryParam = splitPage.getQueryParam();
			queryParam.put("ISCANCEL", "0");
			courseplanService.list(splitPage);
			setAttr("list", splitPage.getPage());
			List<Subject> list = Subject.dao.getSubject();
			setAttr("subject", list);
			renderJsp("/account/month_census.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查看学生的上课信息
	 * */
	public void getStudentMessage() {
		try{
			setAttr("teacherList", Teacher.dao.getTeachers());
			courseplanService.queryStudentPlan(splitPage);
			setAttr("list", splitPage.getPage());
			List<Subject> list = Subject.dao.getSubject();
			setAttr("subject", list);
			renderJsp("/account/month_census_student.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void getCoursesBySubjectId(){
		try{
			String subjectId = getPara("subId");
			List<Course> list = Course.dao.getCourseBySubjectId(subjectId);
			renderJson("course", list);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
