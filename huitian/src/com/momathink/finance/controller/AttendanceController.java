package com.momathink.finance.controller;

import java.util.List;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.finance.service.AttendanceService;
import com.momathink.teaching.course.model.CoursePlan;

/**
 * 考勤管理
 * @author Administrator
 *
 */
@Controller(controllerKey = "/attendance")
public class AttendanceController extends BaseController {
	private AttendanceService attendanceService = new AttendanceService();
	
	/**
	 * 学生考勤分页
	 */
	public void studentIndex(){
		attendanceService.attendanceStudents(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/studentAttendance_list.jsp");
	}
	/**
	 * 查看该学生的考勤信息
	 */
	public void checkStudentAttendance(){
		Integer id = getParaToInt();
		List<CoursePlan> courseplan = CoursePlan.coursePlan.queryStudentCourseInfo(id);
		setAttr("courseplan",courseplan);
		renderJsp("/finance/studentAttendanceMessage.jsp");
	}
	/**
	 * 教师考勤分页
	 */
}
