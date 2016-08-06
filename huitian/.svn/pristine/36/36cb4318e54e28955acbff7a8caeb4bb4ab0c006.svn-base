package com.momathink.teaching.course.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolArith;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.account.model.AccountBanci;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.campus.model.Classroom;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.course.model.SmartPlan;
import com.momathink.teaching.course.service.CourseService;
import com.momathink.teaching.course.service.CourseplanService;
import com.momathink.teaching.course.service.SmartPlanService;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.teacher.model.Teacher;

@Controller(controllerKey="/smartplan")
public class SmartPlanController extends BaseController {

	private SmartPlanService smartplanService = new SmartPlanService();
	private CourseplanService coursePlanService = new CourseplanService();
	private CourseService courseService = new CourseService();
	
	public void index(){
		try{
			smartplanService.queryAllPlan(splitPage);
			setAttr("showPages",splitPage.getPage());
			setAttr("campuslist",Campus.dao.getCampus());
			renderJsp("/course/smartpaike.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void setNewRule(){
		try{
			String stuId = getPara("stuid");
			setAttr("stuid", stuId);
			String campusid = getPara("campusid");
			setAttr("campusid", campusid);
			
			List<Campus> campus = Campus.dao.getCampus();
			setAttr("campus",campus);
			List<TimeRank> timelist = TimeRank.dao.getTimeRank();
			setAttr("trlist",timelist);
			if(StringUtils.isEmpty(stuId)){
				setAttr("stu","hant");
				setAttr("stucourse", null);
				setAttr("studentName",null);
			}else{
//				List<Course> courselist = smartplanService.getStudentCourses(stuId);
				Student student = Student.dao.findById(stuId);
				String type = "1";
				String classid = null;
				if(student.getInt("STATE").toString().equals("2")){
					type="2";
					ClassOrder classOrder = ClassOrder.dao.findByXuniId(student.getPrimaryKeyValue());
					classid = classOrder.getPrimaryKeyValue().toString();
				}
				List<Record> courselist = courseService.getStudentOrClassCourse(stuId,classid,type,0+"");
				setAttr("stu","has");
				setAttr("stucourse", courselist);
				setAttr("studentName",Student.dao.findById(stuId).getStr("REAL_NAME"));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJsp("/course/layer_smartrule.jsp");
	}
	
	public void getAccountByNameLike(){
		try {
			String userName = getPara("studentName");
			List<Account> list = smartplanService.getAccountByNameLike(userName);
			renderJson("accounts", list);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void getRuleStudentCourses(){
		String stuid = getPara("stuid");
		List<Course> courselist = smartplanService.getStudentCourses(stuid);
		renderJson(courselist);
	}
	
	public void getCourseTeachers(){
		try{
			String stuid = getPara("stuid");
			String courseid = getPara("courseid");
			List<Teacher> list = smartplanService.getCourseTeachers(stuid,courseid);
			renderJson(list);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void saveRulePlan(){
		JSONObject json = new JSONObject();
		boolean flag = false;
		try{
			String[] weekdays = getParaValues("weekdays");
			SmartPlan smartplan = getModel(SmartPlan.class);
			SysUser user = SysUser.dao.findById(getSysuserId());
			if(user!=null){
				smartplan.set("createuserid", user.getInt("Id"));
			}
			StringBuffer sb = new StringBuffer("");
			if(weekdays.length>0){
				for(String wday:weekdays){
					sb.append("|").append(wday);
				}
			}
			smartplan.set("weekday", sb.toString().replaceFirst("\\|", ""));
			smartplan.set("addtime", new Date());
			flag = smartplan.save();
		}catch(Exception ex){
			ex.printStackTrace();
			flag = false;
		}
		if(flag){
			json.put("code", "1");
			json.put("msg", "保存成功.");
		}else{
			json.put("code", "0");
			json.put("msg", "保存失败.");
		}
		renderJson(json);
	}
	
	public void setRowRuleCourse(){
		try{
			String ruleid = getPara();
			setAttr("ruleid",ruleid);
			SmartPlan smartrule = smartplanService.getSmartPlan(ruleid);
			setAttr("smartrule",smartrule);
			setAttr("planday",new Date());
			setAttr("createuser",SysUser.dao.findById(getSysuserId()).getStr("REAL_NAME"));
			setAttr("room",Classroom.dao.getAllRooms());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJsp("/course/layer_setrulecourse.jsp");
	}
	
	public void sureEnoughHours(){
		String stime = getPara("stime");
		String alldays = getPara("alldays");
		String allhours = getPara("allhours");
		String weekday = getPara("weekday");
		String stuid = getPara("stuid");
		String rankid = getPara("rankid");
		String teacherid = getPara("teacherid");
		String campusid = getPara("campusid");
		String courseid = getPara("courseid");
		JSONObject json = new JSONObject();
		String msg = "msg";
		String code= "0";
		try{
			Student student = Student.dao.findById(stuid);
			if(student.getInt("state").toString().equals("2")){
				ClassOrder classorder = ClassOrder.dao.findByXuniId(student.getPrimaryKeyValue());
				float coursesum = classorder.getInt("lessonNum").floatValue();
				float thishours = Float.parseFloat(allhours);
				float ypks =  CoursePlan.coursePlan.getClassYpkcClasshour(classorder.getPrimaryKeyValue());
				double syks = ToolArith.sub(coursesum, ToolArith.add(thishours, ypks));
				if(syks>=0){
					Map<String,Object> map = smartplanService.getRoomAndDays(stime,alldays,weekday,stuid,teacherid,rankid,campusid,courseid);
					json.put("map", map);
					msg = "";
					code= "1";
				}else{
					msg = "课时不足";
					code= "0";
				}
			}else{
				double zks = CourseOrder.dao.getCanUseVIPzks(student.getPrimaryKeyValue());
				double ypks = CoursePlan.coursePlan.getUseClasshour(stuid,null);//全部已用课时
				float thishours = Float.parseFloat(allhours);
				double syks = ToolArith.sub(zks, ToolArith.add(thishours, ypks));
				if(syks>=0){
					Map<String,Object> map = smartplanService.getRoomAndDays(stime,alldays,weekday,stuid,teacherid,rankid,campusid,courseid);
					json.put("map", map);
					msg="";
					code="1";
				}else{
					msg = "课时不足";
					code= "0";
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
		
	}
	
	
	public synchronized void saveSmartCoursePlan(){
		JSONObject json = new JSONObject();
		CoursePlan plan = getModel(CoursePlan.class);
		String studentId = plan.getInt("STUDENT_ID").toString();
		Student student = Student.dao.findById(studentId);
		String teacherId = plan.getInt("TEACHER_ID").toString();
		String roomId = plan.getInt("ROOM_ID").toString();
		String timeId = plan.getInt("TIMERANK_ID").toString();
		String campusId = plan.getInt("CAMPUS_ID").toString();
		String courseId = plan.getInt("COURSE_ID").toString();
		Course course = Course.dao.findById(courseId);
		String subjectid = course.getInt("SUBJECT_ID").toString();
		String planType ="0";
		String plantype = "0";
		String type = "1";
		Integer classOrderId = null;
		if((student.getInt("STATE").toString()).equals("2")){
			type = "2";
			ClassOrder classorder = ClassOrder.dao.findByXuniId(Integer.parseInt(studentId));
			classOrderId = classorder.getInt("id");
		}
		String netCourse = getPara("netCourse");
		String remark = null;
		String rankId = plan.getInt("TIMERANK_ID").toString();
		SmartPlan planrule = getModel(SmartPlan.class);
		String coursedays = planrule.getStr("coursedays").trim();
		try{
			if(coursedays.length()>0){
				String[] days = coursedays.split(",");
				for(int i=0;i<days.length;i++){
					String courseTime = days[i];
					if(type.equals("2")){
						List<AccountBanci> list = AccountBanci.dao.findABbyClassId(classOrderId);
						if(list.size()>0){
							for(AccountBanci ab:list){
								CoursePlan saveCoursePlan = new CoursePlan();
								CoursePlan newCoursePlan = new CoursePlan();
								json = coursePlanService.doAddCoursePlans(type,timeId,ab.getInt("account_id").toString(),courseTime,saveCoursePlan,teacherId,roomId,campusId,subjectid,
										plantype,netCourse,remark,courseId,classOrderId,planType,rankId,newCoursePlan,getSysuserId());
							}
						}
					}else{
						CoursePlan saveCoursePlan = new CoursePlan();
						CoursePlan newCoursePlan = new CoursePlan();
						json = coursePlanService.doAddCoursePlans(type,timeId,studentId,courseTime,saveCoursePlan,teacherId,roomId,campusId,subjectid,
								plantype,netCourse,remark,courseId,classOrderId,planType,rankId,newCoursePlan,getSysuserId());
					}
				}
				Teacher  t = Teacher.dao.findById(teacherId);
				if(t!=null){
					t.set("kcuserid", 0).update();//使用kcuserid字段表示教师课表变动
				}
				SmartPlan rule = SmartPlan.dao.findById(planrule.getInt("ID"));
				rule.set("coursedays", coursedays.replace(",", "|"));
				rule.set("updatetime", new Date());
				rule.set("version",rule.getInt("version")+1);
				rule.set("usedtimes",rule.getInt("usedtimes")+1);
				rule.update();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);

	}
	
	public synchronized void delPlanRule(){
		JSONObject json = new JSONObject();
		String msg="成功删除";
		String code="1";
		String id = getPara("id");
		SmartPlan rule = SmartPlan.dao.findById(id);
		if(rule==null){
			msg="该规则不存在。";
			code="0";
		}else{
			if(StringUtils.isEmpty(rule.getStr("coursedays"))){
				rule.deleteById(id);
				msg="删除成功";
				code="1";
			}else{
				msg="该规则已使用，不能删除.";
				code="0";
			}
		}
		json.put("msg", msg);
		json.put("code", code);
		renderJson(json);
		
	}
	
	
	public void showStudentLastCourse(){
		JSONObject json = new JSONObject();
		String id = getPara("id");
		SmartPlan rule = SmartPlan.dao.findById(id);
		Integer stuid = rule.getInt("studentid");
		Integer courseid = rule.getInt("courseid");
		json = smartplanService.showStudentLastCourse(stuid.toString(),courseid.toString());
		renderJson(json);
	}
	
}
