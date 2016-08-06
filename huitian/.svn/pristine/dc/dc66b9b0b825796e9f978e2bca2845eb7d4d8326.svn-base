package com.momathink.teaching.student.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.Constants;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolString;
import com.momathink.common.tools.ToolUtils;
import com.momathink.crm.opportunity.model.Feedback;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.crm.opportunity.service.OpportunityService;
import com.momathink.finance.model.CourseOrder;
import com.momathink.finance.service.CourseOrderService;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.account.model.UserCourse;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.sms.model.SendSMS;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.classtype.model.BanciCourse;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.course.service.CourseplanService;
import com.momathink.teaching.student.model.HeadPicture;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.student.model.StudentKcgw;
import com.momathink.teaching.student.service.StudentService;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.teacher.model.Teachergrade;


@Controller(controllerKey="/student")
public class StudentController extends BaseController {
	private static final Logger logger = Logger.getLogger(StudentController.class);
	private StudentService studentService = new StudentService();
	private OpportunityService opportunityService = new OpportunityService();
	private CourseplanService courseplanService = new CourseplanService();
	private CourseOrderService courseOrderService = new CourseOrderService();
	/**
	 * 学生列表*
	 */
	public void index(){
		Map<String,String> queryParam = splitPage.getQueryParam();
		String oppid = getPara("_query.oppid");
		if(oppid!=null){
			queryParam.put("oppid", oppid);
		}
		studentService.list(splitPage);
		setAttr("kcgwList",SysUser.dao.getKechengguwen(null));
		setAttr("dudaoList",SysUser.dao.getDudao());
		setAttr("scList",SysUser.dao.getSysUser());
		setAttr("classorders",ClassOrder.dao.getAllClassOrder());
		setAttr("showPages", splitPage.getPage());
		setAttr("roles",Role.dao.getAllRole());
		render("/student/student_list.jsp");
	}
	/**
	 * 学生暂停账号*
	 */
	public void stopStudentIndex(){
		String oppid = getPara("_query.oppid");
		if(oppid!=null){
			Map<String,String> queryParam = splitPage.getQueryParam();
			queryParam.put("oppid", oppid);
		}
		studentService.queryMyStudents(splitPage,getSysuserId());
		setAttr("kcgwList",SysUser.dao.getKechengguwen(null));
		setAttr("dudaoList",SysUser.dao.getDudao());
		setAttr("scList",SysUser.dao.getSysUser());
		setAttr("classorders",ClassOrder.dao.getAllClassOrder());
		setAttr("showPages", splitPage.getPage());
		setAttr("roles",Role.dao.getAllRole());
		render("/student/student_list.jsp");
	}
	public void toExcel(){
		String oppid = getPara("_query.oppid");
		String filename = getPara("_query.filename");
		String today = ToolDateTime.format(new Date(), "yyyy-MM-dd");
//		if(StringUtils.isEmpty(filename)){
			filename = "学生列表_"+today;
//		}
		if(oppid!=null){
			Map<String,String> queryParam = splitPage.getQueryParam();
			queryParam.put("oppid", oppid);
		}
		List<Record> list = studentService.queryMyStudentsToexcel(splitPage,getSysuserId());
		studentService.export(getResponse(), getRequest(), list,filename);		
		renderNull();
	}
	
	
	/**
	 * 检查是否存在*
	 */
	public void checkExist() {
		renderJson("result", Student.dao.queryCount(getPara("checkField"), getPara("checkValue"), getPara("id")));
	}
	public void add() {
		try {
			int studentId = 0;
			setAttr("studentId", studentId);
			setAttr("subjects", Subject.dao.getSubject());
			List<Opportunity> opportunityList = Opportunity.dao.findIsConver();
			List<Campus> clist = Campus.dao.getCampus();
			setAttr("campusList",clist);
			setAttr("olist",opportunityList);
			setAttr("roles",Role.dao.getAllRole());
			renderJsp("/student/student_form.jsp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void edit() {
		try {
			int studentId = Integer.parseInt(getPara());
			List<StudentKcgw> sks  = StudentKcgw.dao.getAllKcgwidsByStudentid(studentId);
			String str = "|";
			if(!sks.isEmpty()){
				for(StudentKcgw sk:sks){
					str+=sk.getInt("kcgw_id")+"|";
				}
			}
			setAttr("kcgws",str);
 			setAttr("studentId", studentId);
			Student student = Student.dao.findById(getParaToInt());
			String campusid =student.getInt("campusid").toString();
			student.set("sex", student.getInt("sex")==1?true:false);
			setAttr("student", student);
			if(student.get("headpictureid")!=null){
				setAttr("url",HeadPicture.dao.findById(student.getInt("headpictureid")).getStr("url"));
			}else{
				setAttr("url","");
			}
			setAttr("subjects", Subject.dao.getSubject());
			setAttr("supervisors", SysUser.dao.getTutorsByCampusid(campusid));
			setAttr("jwuserid", SysUser.dao.getJiaowuByCampusid(campusid ));
			setAttr("scuserid", SysUser.dao.getShichangByCampusid(campusid ));
			setAttr("kcuserid", SysUser.dao.getCampusKcgws(campusid ));
			List<Campus> clist = Campus.dao.getCampus();
			setAttr("campusList",clist);
			List<Opportunity> opportunityList = Opportunity.dao.findIsConver();
			setAttr("olist",opportunityList);
			setAttr("roles",Role.dao.getAllRole());
			renderJsp("/student/student_form.jsp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 保存学生*
	 */
	public void save() {
		JSONObject json = new JSONObject();
		try {
			Student student = getModel(Student.class);
			student.set("user_pwd", ToolMD5.getMD5("111111"));
			student.set("create_time", new Date());
			student.set("createuserid",getSysuserId());
			Opportunity o = Opportunity.dao.findById(student.getInt("opportunityid"));
			if(o!=null){
				student.set("mediatorid", o.getInt("mediatorid"));
				o.set("isconver", 1).update();
			}
			if(student.get("headpictureid")!=null){
				HeadPicture hp = HeadPicture.dao.findById(student.getInt("headpictureid"));
				hp.set("state", 1).update();
			}
			Integer id = studentService.save(student);
			List<HeadPicture> list =  HeadPicture.dao.getScrapPicture(getSysuserId());
			if(!list.isEmpty()){
				for(HeadPicture h:list){
					File f = new File(h.getStr("name")); 
					f.delete();
					h.delete();
				}
			}
			String kcgwids = getPara("kcgwids");
			if(kcgwids.length()!=0){
				kcgwids = kcgwids.replaceFirst("\\|", "");
				String[] str = kcgwids.replace("|", ",").split(",");
				for(int i=0;i<str.length;i++){
					StudentKcgw sk = new StudentKcgw();
					sk.set("student_id", id).set("kcgw_id", str[i]).save();
				}
			}
			json.put("code", 1);
			json.put("msg", "添加成功");
		} catch (Exception ex) {
			logger.error(ex.toString());
			json.put("code", 0);
			json.put("msg", "添加信息异常，请联系管理员");
		}
		renderJson(json);
	}
	/**
	 * 更新学生信息*
	 */
	public void update() {
		JSONObject json = new JSONObject();
		try {
			Student student = getModel(Student.class);
			String stateToOne = getPara("difference");
			if(StrKit.notBlank(stateToOne)){
				student.set("state", 0);
			}
			String id = getPara("student.id");
			String kcgwids = getPara("kcgwids");
			Opportunity o = Opportunity.dao.findById(student.getInt("opportunityid"));
			if(o!=null){
				student.set("mediatorid", o.getInt("mediatorid"));
			}
			if(student.get("headpictureid")!=null){
				Student s = Student.dao.findById(id);
				if(s.get("headpictureid")!=null){
					HeadPicture hp = HeadPicture.dao.findById(s.getInt("headpictureid"));
					hp.set("state", 0).update();
				}
				HeadPicture hp = HeadPicture.dao.findById(student.getInt("headpictureid"));
				hp.set("state", 1).update();
			}
			studentService.update(student);
			List<HeadPicture> list =  HeadPicture.dao.getScrapPicture(getSysuserId());
			if(!list.isEmpty()){
				for(HeadPicture h:list){
					File f = new File(h.getStr("name")); 
					f.delete();
					h.delete();
				}
			}
			List<StudentKcgw> acc= StudentKcgw.dao.getAllKcgwidsByStudentid(Integer.parseInt(id));
			if(!acc.isEmpty()){
				for(StudentKcgw a:acc){
					a.delete();
				}
			}
			if(!kcgwids.isEmpty()){
				kcgwids = kcgwids.replaceFirst("\\|", "");
				String[] kcgwid = kcgwids.replace("|", ",").split(",");
					for(int i=0;i<kcgwid.length;i++){
							StudentKcgw ac = new StudentKcgw();
							ac.set("student_id", id).set("kcgw_id",kcgwid[i]).save();
					}
			}
			json.put("code", 1);
			json.put("msg", "添加成功");
		} catch (Exception ex) {
			logger.error(ex.toString());
			json.put("code", 0);
			json.put("msg", "数据更新异常，请联系管理员");
		}
		renderJson(json);
	}
	
	/**
	 * 	/student/queryStudentCoursePlanedInfo
	 * 那课程排课   获取学员课程排课信息
	 */
	public void queryStudentCoursePlanedInfo(){
		logger.info("按课程排课查询学生课程基本信息");
		try {
			String studentId=getPara("studentId");
			Student student = Student.dao.findById(getParaToInt("studentId"));
			if (student != null) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				// 查询总节数和已经排课的节数
				double zks = 0;
				float usedLesson = 0;
				StringBuffer str = new StringBuffer();
				if(student.getInt("state")==2){
					logger.info("小班课程信息.");
					List<Object> courseList = new ArrayList<Object>();
					ClassOrder ban = ClassOrder.dao.findByXuniId(student.getPrimaryKeyValue());
					zks = ban.getInt("lessonNum"); 
					usedLesson = CoursePlan.coursePlan.getClassYpkcClasshour(ban.getPrimaryKeyValue());
					str.append("已购：").append(ToolString.subZeroAndDot(zks+"")).append("课时 已排：").append(ToolString.subZeroAndDot(usedLesson+"")).append("课时;") ;
					List<BanciCourse> bclist = BanciCourse.dao.getBanciCourse(ban.getPrimaryKeyValue());
					for (BanciCourse bc : bclist) {
						float ypks = CoursePlan.coursePlan.getClassYpkcCount(student.getPrimaryKeyValue(), bc.getInt("course_id"));
						Map<Object, Object> map1 = new HashMap<Object, Object>();
						str.append(bc.getStr("COURSE_NAME")).append("：").append(ToolString.subZeroAndDot(ypks+"")).append(" ");
						map1.put("course_id", bc.get("course_id"));
						map1.put("course_name", bc.get("COURSE_NAME")+"(已排"+ypks+"课时)");
						if (zks <= usedLesson) {
							map1.put("status", 0);
						} else {
							map1.put("status", 1);
						}
						courseList.add(map1);
					}
					map.put("courseList", courseList);
				}else{
					logger.info("一对一学员课程信息.");
					zks = CourseOrder.dao.getCanUseVIPzks(student.getPrimaryKeyValue());
					usedLesson = CoursePlan.coursePlan.getUseClasshour(studentId,null);//全部已用课时
					str.append("已购：").append(ToolString.subZeroAndDot(zks+"")).append("课时 已排：").append(ToolString.subZeroAndDot(usedLesson+"")).append("课时;") ;
					List<Course> courseList = UserCourse.dao.getStudentUserCourse(studentId);
					for (Course course : courseList) {
						double ypk = CoursePlan.coursePlan.getYpVIPkcCount(Integer.parseInt(studentId),course.getPrimaryKeyValue());
						str.append(course.getStr("COURSE_NAME")).append("：").append(ToolString.subZeroAndDot(ypk+"")).append(" ");
						if(zks<=usedLesson){
							course.put("status", 0);
						}else{
							course.put("status", 1);
						}
						course.put("course_name", course.getStr("COURSE_NAME")+"(已排"+ypk+"课时)");
					}
					map.put("courseList", courseList);
				}
				String stuName = student.getStr("REAL_NAME");// 学生姓名
				map.put("courseUseNum", usedLesson);
				map.put("stuName", stuName);
				map.put("stuMsg", str.toString());
				map.put("studentId", student.getPrimaryKeyValue());
				map.put("sumCourse", ToolString.subZeroAndDot(zks+""));
				map.put("useCourse", usedLesson);
				renderJson("account", map);
			} else {
				renderJson("account", "noResult");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	}
	
	public void queryCourseInfo(){
		try {
			String studentId=getPara("studentId");
			Student student = Student.dao.findById(getParaToInt("studentId"));
			if (student != null) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				// 查询总节数和已经排课的节数
				double zks = 0;
				float usedLesson = 0;
				StringBuffer str = new StringBuffer();
				if(student.getInt("state")==2){
					List<Object> courseList = new ArrayList<Object>();
					ClassOrder ban = ClassOrder.dao.findByXuniId(student.getPrimaryKeyValue());
					zks = ban.getInt("lessonNum"); 
					usedLesson = CoursePlan.coursePlan.getClassYpkcClasshour(ban.getPrimaryKeyValue());
					str.append("已购订单：").append(ToolString.subZeroAndDot(zks+"")).append("课时 已排：").append(ToolString.subZeroAndDot(usedLesson+"")).append("课时、") ;
					List<BanciCourse> bclist = BanciCourse.dao.getBanciCourse(ban.getPrimaryKeyValue());
					for (BanciCourse bc : bclist) {
						float ypks = CoursePlan.coursePlan.getClassYpkcCount(student.getPrimaryKeyValue(), bc.getInt("course_id"));
						Map<Object, Object> map1 = new HashMap<Object, Object>();
						str.append(bc.getStr("COURSE_NAME")).append("：").append(ToolString.subZeroAndDot(ypks+"")).append(" ");
						map1.put("course_id", bc.get("course_id"));
						map1.put("course_name", bc.get("COURSE_NAME")+"(已排"+ypks+"课时)");
						if (zks <= usedLesson) {
							map1.put("status", 0);
						} else {
							map1.put("status", 1);
						}
						courseList.add(map1);
					}
					map.put("courseList", courseList);
				}else{
					zks = CourseOrder.dao.getCanUseVIPzks(student.getPrimaryKeyValue());
					usedLesson = CoursePlan.coursePlan.getUseClasshour(studentId,null);//全部已用课时
					List<CourseOrder> courseOrderList = CourseOrder.dao.findByvipId(studentId);
					str.append("已购：");
					for(CourseOrder order : courseOrderList){
						str.append(order.getStr("subjectName")).append(":").append(order.getDouble("classhour")).append("课时、");
					}
					str.append("<br/>已购课时合计：").append(ToolString.subZeroAndDot(zks+"")).append("课时 已排：").append(ToolString.subZeroAndDot(usedLesson+"")).append("课时、") ;
					List<Course> courseList = UserCourse.dao.getStudentUserCourse(studentId);
					for (Course course : courseList) {
						double ypk = CoursePlan.coursePlan.getYpVIPkcCount(Integer.parseInt(studentId),course.getPrimaryKeyValue());
						str.append(course.getStr("COURSE_NAME")).append("：").append(ToolString.subZeroAndDot(ypk+"")).append(" ");
						if(zks<=usedLesson){
							course.put("status", 0);
						}else{
							course.put("status", 1);
						}
						course.put("course_name", course.getStr("COURSE_NAME")+"(已排"+ypk+"课时)");
					}
					map.put("courseList", courseList);
				}
				String stuName = student.getStr("REAL_NAME");// 学生姓名
				map.put("courseUseNum", usedLesson);
				map.put("stuName", stuName);
				map.put("stuMsg", str.toString());
				map.put("studentId", student.getPrimaryKeyValue());
				map.put("sumCourse", ToolString.subZeroAndDot(zks+""));
				map.put("useCourse", usedLesson);
				renderJson("account", map);
			} else {
				renderJson("account", "noResult");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	
	}
	
	public void freeze(){
		try{
			int state=getParaToInt("state");
			int studentId=getParaToInt("studentId");
			Student user = Student.dao.findById(studentId);
			user.set("state", state);
			studentService.update(user);
			renderJson("result", "true");
		}catch(Exception e){
			logger.error("AccountController.freezeAccount",e);
		}
	}
	
	public void changePassword() {
		JSONObject json = new JSONObject();
		try {
			Integer id = getParaToInt("id");
			String pwd = getPara("password");
			Student student = Student.dao.findById(id);
			student.set("USER_PWD", ToolMD5.getMD5(pwd));
			studentService.update(student);
			json.put("result", true);
		} catch (Exception ex) {
			logger.error("error",ex);
			json.put("result", false);
		}
		renderJson(json);
	}
	
	public void showStudentDetail(){
		Map<String, String> queryParam  = splitPage.getQueryParam();
		
		String studentid = new String();
		if(StrKit.isBlank(getPara())){
			studentid = queryParam.get("stuid");
		}else{
			studentid = getPara();
			queryParam.put("stuid", studentid);
		}
		setqueryParamMap(queryParam);
		Student student = Student.dao.showStudentDetail(studentid);
		setAttr("kcgwname",StudentKcgw.dao.getKcgwNames(studentid));
		setAttr("result",student);
		setAttr("roles",Role.dao.getAllRole());
		Integer restday = student.getInt("rest_day");
		String day ="";
		if(restday==1){
			day="星期一";
		}else  if(restday==2){
			day="星期二";
		}else  if(restday==3){
			day="星期三";
		}else  if(restday==4){
			day="星期四";
		}else  if(restday==5){
			day="星期五";
		}else  if(restday==6){
			day="星期六";
		}else  if(restday==7){
			day="星期日";
		}
		setAttr("day",day);
		
		Integer opportunityIdint = student.getInt("opportunityid");
		if(opportunityIdint != null){
			String opportunityId = opportunityIdint.toString();
			Opportunity o = opportunityService.findDetailById(opportunityId);
			setAttr("o", o);
			List<Feedback> fblist = Feedback.dao.queryBackRecord(opportunityIdint.toString(), getSysuserId());
			setAttr("fblist",fblist);
		}
		
		setAttr("stuId",studentid);
			
		setAttr("student",student);
		
		courseplanService.list(splitPage);
		setAttr("list", splitPage.getPage());
		List<Subject> list = Subject.dao.getSubject();
		setAttr("subject", list);
		
		Record r = courseOrderService.queryOrderListStudentMessage(studentid);
		setAttr("record",r);
		
		
		renderJsp("/student/student_message.jsp");
	}
	
	public void getStuNameById(){
		try{
			String stuid = getPara("stuId");
			String stuName = Student.dao.getStudentNameById(stuid);
			renderJson("stuName",stuName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void getStudentIdByName(){
		try{
			String stuName = getPara("stuName");
			Student student = Student.dao.getStudentByName(stuName);
			renderJson("student",student);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void getStuCourseName(){
		try{
			String stuId = getPara("stuId");
			Student student = Student.dao.findById(stuId);
			List<Course> list =studentService.getStuCourseName(student.getStr("CLASS_TYPE"));
			for(Course t : list){
				System.out.println(t);
			}
			renderJson("course",list);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * 我的学生
	 */
	public void myStudents() {
		try {
			studentService.queryMyStudents(splitPage,getSysuserId());
			setAttr("showPages", splitPage.getPage());
			render("/student/student_list.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
	
	/**
	 * 购课记录
	 */
	public void showCourseOrdersDetail(){
		setAttr("buylist", studentService.showCourseOrdersDetail(splitPage, getSysuserId()));
		setAttr("roles",Role.dao.getAllRole());
		render("/student/student_ordersdetail.jsp");
	}
	
	public void showCourseUsedCount(){
		try{
			String studentid = getPara();
			Student student = studentService.showCourseUsedCount(studentid);
			setAttr("student",student);
			render("/student/stucoursecount.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 校区的所有督导，教务，市场，课程顾问
	 */
	public void getCampusTutors(){
		try{
			JSONObject json=new JSONObject();
			String campusid = getPara("campusid");
			List<SysUser> tutors = SysUser.dao.getTutorsByCampusid(campusid);
			List<SysUser> kcgw = SysUser.dao.getCampusKcgws(campusid);
			List<SysUser>jiaowu = SysUser.dao.getJiaowuByCampusid(campusid);
			List<SysUser> shichang = SysUser.dao.getShichangByCampusid(campusid);
			json.put("tutors",tutors);
			json.put("kcgw",kcgw);
			json.put("jiaowu",jiaowu);
			json.put("shichang",shichang);
			renderJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 学生统计
	 */
	public void studentCensus(){
		renderJsp("/account/student_census.jsp");
	}
	/**
	 * 获取转介绍人。数据是所有的人<模糊查询>
	 */
	public void getNameByLike(){
		String recommendusername = getPara("recommendusername");
		try {
			if(recommendusername==""){
				renderJson("accounts", 1);
			}else{
				String sql = "select * from account where   real_name like \"%" + recommendusername + "%\"";
				List<Account> list = Account.dao.find(sql);
				renderJson("accounts", list);
			}
		
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
	
	
	/**
	 *  /student/getStudentCourse
	 *  课程
	 */
	public void getStudentCourse(){
		logger.info("更改学生排课课程");
		String stuid = getPara();
		Student student = Student.dao.findById(stuid);
		setAttr("student",student);
		List<Course> courselist = Course.dao.findStudentOrdersList(stuid);//order courses
		String courseplanedids = CoursePlan.coursePlan.getCourseIdsUsed(stuid);// planed courseids 
		if(StrKit.notBlank(courseplanedids)){
			Iterator<Course> iter = courselist.iterator();//
			while(iter.hasNext()){
				Course cp = iter.next();
				String courseid = ","+cp.getInt("courseid")+",";
				if(courseplanedids.indexOf(courseid)!=-1)
					iter.remove();
			}
		}
		setAttr("courselist",courselist);
		String usercourseids = UserCourse.dao.getStudentCourseids(stuid);
		setAttr("usercourseids",usercourseids);
		List<Subject> subjectList = CourseOrder.dao.findSubjectByStudentId(student.getPrimaryKeyValue());
		String studentCourseIds[] = usercourseids == null ? new String[0]:usercourseids.split("\\|");
		String studentUseCourseIds[] = courseplanedids == null ? new String[0]:courseplanedids.split(",");
		for(Subject subject : subjectList){
			List<Course> courseList = Course.dao.findBySubjectId(subject.getPrimaryKeyValue());
			for(Course course : courseList){
				for(String studentCourseId : studentCourseIds){
					course.put("choose",false);
					if(StringUtils.isEmpty(studentCourseId))
						continue;
					if(course.getPrimaryKeyValue().equals(Integer.parseInt(studentCourseId))){
						course.put("choose",true);
						break;
					}else{
						continue;
					}
				}
				for(String studentUserCourseId:studentUseCourseIds){
					course.put("isUse",false);
					if(StringUtils.isEmpty(studentUserCourseId))
						continue;
					if(course.getPrimaryKeyValue().equals(Integer.parseInt(studentUserCourseId))){
						course.put("choose",true);
						course.put("isUse",true);
						break;
					}else{
						continue;
					}
				}
			}
			subject.put("courseList", courseList);
		}
		setAttr("subjectlist",subjectList);
		String coursenameused = CoursePlan.coursePlan.getCourseUsedNames(stuid);
		setAttr("coursenameused",coursenameused);
		
		renderJsp("/student/layer_studentcourse.jsp");
	}
	
	/**
	 *  保存学生课程
	 *  /student/setStudentCourse
	 */
	public void setStudentCourse(){
		try{
			logger.info("保存/更新user_course");
			String studentId = getPara("studentid");
			String courseIds[] = getParaValues("courseId");
			if(StringUtils.isEmpty(studentId)){
				renderJson("code","0");
			}else{
				if(courseIds != null && courseIds.length >0){
					UserCourse.dao.deleteByStudentId(Integer.parseInt(studentId));
					for(String cid:courseIds){
						UserCourse uc = new UserCourse();
						uc.set("account_id", studentId);
						uc.set("course_id", cid);
						uc.save();
					}
				}
				renderJson("code","1");
			}
		}catch(Exception ex){
			renderJson("code","0");
			logger.info("保存/更新user_course出错了");
			ex.printStackTrace();
		}
	}
	/**
	 * 学生生日列表
	 */
	@SuppressWarnings("unchecked")
	public void birthday(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy");
		String time=format.format(date); 
		studentService.getAllStudentBirthDay(splitPage);
		setAttr("showPages", splitPage.getPage());
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> olist = page.getList();
		for (Record r : olist) {
			int nianling = (int)(double)r.getDouble("nianling");
			r.set("nianling", nianling);
			try {
				if((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time+"-"+r.get("shengri").toString()+" 23:59:00")).getTime()>=new Date().getTime()){
					r.set("code", 1);
				}else{
					r.set("code", 0);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		renderJsp("/birthday/birthday_list.jsp");
	}
	/**
	 * 给学生发送生日祝福短信前获取短信模板
	 */
	public void getBirthdaySmsTemplate(){
		String[] studentid = getPara().toString().split(",");
		setAttr("student",Student.dao.findById(studentid[0]));
		renderJsp("/birthday/birthday_form.jsp");
	}
	/**
	 * 确认发送生日祝福信息，并保存发送记录
	 */
	public void sendMessage(){
		JSONObject json = new JSONObject();
			String id = getPara("id");
			String tel = getPara("tel");
			String message = getPara("message");
			Map<String,String> map = SendSMS.sendCoursePlanSms(message,tel,Constants.RECEIVE_SMS_STUDENT);
			Student student = Student.dao.findById(id);
			for(String str: map.keySet()){
				if(str.equals("errordescription")){
					if(map.get(str).equals("成功")){
						json.put("code", 1);
						student.set("isbirthday",1).set("sendbirthdaytime",new SimpleDateFormat("yyyy-MM-dd").format(new Date())).update();
					}else{
						json.put("code", 0);
					}
				}
			}
		renderJson(json);
	}
	
	/** 
	 *  /student/assessCourse
	 *  学生进入微信评价页面
	 */
	public void assessCourse(){
		String ids = ToolUtils.returnOldData(getPara("ids"));
		String stuid = ids.split("_")[0];
		String cpid = ids.split("_")[1];
		Teachergrade tg = Teachergrade.teachergrade.findByCoursePlanIdAndStudentid(cpid,stuid);
		setAttr("tg",tg);
		renderJsp("/student/assessCourse.jsp");
	}
	
	/**
	 *   /student/toSaveStudentGrade
	 *   保存学生课程评价
	 */
	public void toSaveStudentGrade(){
		Teachergrade tg = getModel(Teachergrade.class);
		String tgid = getPara("tgid");
		tg.set("role", 2);
		if(StrKit.isBlank(tgid))
			tg.save();
		else
			tg.update();
		
		renderJsp("/student/studentAssessSuccess.jsp");
	}
	/**
	 * 上传头像*
	 */
	public void unloadPicture(){
		try{
			UploadFile upload = getFile("file");
			String name = upload.getSaveDirectory();
			String url = upload.getFileName();
			String newurl = name.substring(0,name.length()-7)+"images\\headPortrait\\";
			File file = new File(name+url);
			file.renameTo(new File(newurl+url));
			HeadPicture hp = new HeadPicture();
			hp.set("url", url).set("name", newurl+url).set("sysuserid",getSysuserId()).save();
			setAttr("url",url);
			setAttr("code",0);
			setAttr("id",hp.getPrimaryKeyValue());
			renderJsp("/student/head.jsp");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取学生的今日课程信息
	 */
	public void getStudentCourseMessage(){
		List<CoursePlan> cplist = new ArrayList<CoursePlan>();
		try{
			Integer id = getSysuserId();
			List<ClassOrder> colist = ClassOrder.dao.finfByStudentid(id);
			String orderids = "";
			if(!colist.isEmpty()){
				StringBuffer sf = new StringBuffer();
				for(ClassOrder co : colist){
					sf.append(co.getInt("id")).append(",");
				}
				orderids = sf.substring(0,sf.length()-1);
			}
			 cplist = CoursePlan.coursePlan.findByOrderIdsAndStudentid(orderids,id);
		}catch(Exception e){
			e.printStackTrace();
		}
		renderJson(cplist);
	}
	
	/**
	 * 准备绑定射频卡
	 */
	public void cardReading(){
		Student student = Student.dao.findById(getPara());
		setAttr("student", student);
		renderJsp("/student/clock/layer_student_binding.jsp");
	}
	
}
