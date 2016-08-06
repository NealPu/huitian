package com.momathink.teaching.teacher.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolDirFile;
import com.momathink.common.tools.ToolMail;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.teacher.model.GradeUpdate;
import com.momathink.teaching.teacher.model.SetPoint;
import com.momathink.teaching.teacher.model.Teacher;
import com.momathink.teaching.teacher.model.Teachergrade;
import com.momathink.teaching.teacher.service.ReportService;
/**
 * 2015年7月30日
 * @author prq
 *
 */
@Controller(controllerKey="/report")
public class ReportManagement extends BaseController {
	private ReportService reportService = new ReportService();
	private static final Logger logger = Logger.getLogger(ReportManagement.class);

	/**
	 * 设置节点(左侧菜单)
	 */
	public void setPoint(){
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("sysuserid", getSysuserId().toString());
		reportService.setPointPages(splitPage);
		setAttr("showPages",splitPage.getPage().getList());
		renderJsp("/teacher/report/studentpoint.jsp");
	}
	
	/**
	 * 跳转至设置节点页面
	 */
	public void toSetPoint(){
		String stuid = getPara();
		Student student = Student.dao.findById(stuid);
		List<Teacher> teacherList = Teacher.dao.getTeachers();
		setAttr("student",student);
		setAttr("teacherList",teacherList);
		renderJsp("/teacher/report/layer_setpoint.jsp");
		
	}
	
	/**
	 * 保存设置节点
	 */
	public void submitPoints(){
		JSONObject json = new JSONObject();
		try{
			String stuid = getPara("studentid");
			String tchids[] = getParaValues("teacherids");
			String days = getPara("setPoint.days");
			String remark = getPara("setPoint.remark");
			String[] dates = getParaValues("datelistval");
			
			json = reportService.saveNewPoint(stuid, tchids, days, dates, remark);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	/**
	 * 查看学生节点(需要分页？)
	 */
	public void getStudentReportPoints(){
		String stuid = getPara();
		List<SetPoint> stulist = SetPoint.dao.getPointsById(stuid,null);
		setAttr("student",Student.dao.findById(stuid));
		setAttr("studentlist",stulist);
		renderJsp("/teacher/report/layer_showstudentpoints.jsp");
	}
	
	/**
	 * 	/report/deleteSetPoint
	 * 删除预约报告
	 */
	public void deleteSetPoint(){
		JSONObject json = new JSONObject();
		String code="0";
		try{
			String pointid = getPara("pointid");
			SetPoint point = SetPoint.dao.findById(pointid);
			point.delete();
			code="1";
		}catch(Exception ex){
			ex.printStackTrace();
			code="0";
		}
		json.put("code", code);
		renderJson(json);
	}
	
	/**
	 * 我的报告(主要针对老师)
	 */
	public void teacherReports(){
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("teacherid", getSysuserId().toString());
		reportService.getTeacherPoints(splitPage);
		setAttr("showPages",splitPage.getPage().getList());
		renderJsp("/teacher/report/teacherpoints.jsp");
	}
	
	/**
	 * 填写报告
	 */
	public void toFillReport(){
		String pointid = getPara();
		List<Course> courseList = Course.dao.getCourses();//做筛选
		setAttr("courseList",courseList);
		SetPoint point = SetPoint.dao.getFillReportBaseMsg(pointid);
		Teachergrade tg = Teachergrade.teachergrade.getPointGradeByPointId(pointid);
		setAttr("pointgrade",tg);
		setAttr("baseMsg",point);
		if(tg==null){
			setAttr("code",0);
		}
		renderJsp("/teacher/report/tofillreport.jsp");
	}
	
	/**
	 * submitReportDetail
	 * 保存填写报告
	 */
	public void submitReportDetail(){
		try{
			GradeUpdate reportgrade = getModel(GradeUpdate.class);
			reportgrade.set("ROLE", 1);
			Date nowdate = new Date();
			reportgrade.set("createtime", nowdate);
			reportgrade.save();
			Db.update("insert into teachergrade select * from teachergrade_update where id =? ",reportgrade.getPrimaryKeyValue());
			SetPoint point = SetPoint.dao.findById(reportgrade.getInt("pointid"));
			point.set("state", 1);
			point.set("submissiontime", nowdate);
			point.update();
			String nextmessage = getPara("nextmessage");
			if(nextmessage.equals("1")){
				String nextpoint  = getPara("nextpoint");
				SetPoint sp = new SetPoint(); 
				sp.set("studentid",point.getInt("studentid")).set("teacherid", point.getInt("teacherid"))
					.set("appointment", nextpoint)
					.set("days", point.getInt("days")).set("state",0).save();
				String teacherfeedback  = getPara("teacherfeedback");
				String question  = getPara("question");
				String method  = getPara("method");
				String homework  = getPara("homework");
				GradeUpdate tg =new GradeUpdate();
				tg.set("pointid", sp.getPrimaryKeyValue())
				.set("teacherfeedback", teacherfeedback).set("question", question)
				.set("method", method).set("homework",homework).set("courseid",reportgrade.getInt("courseid")).set("createtime",nowdate).save();
				Db.update("insert into teachergrade select * from teachergrade_update where id = ?",tg.getPrimaryKeyValue());
			}
			redirect("/report/teacherReports");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 *	/report/toFixReport
	 * 修改报告
	 */
	public void toFixReport(){
		try{
			String pointid = getPara("pointid");
			String type = getPara("type");
			setAttr("type",type);
			List<Course> courseList = Course.dao.getCourses();//做筛选
			setAttr("courseList",courseList);
			SetPoint point = SetPoint.dao.getFillReportBaseMsg(pointid);
			setAttr("baseMsg",point);
			GradeUpdate pointgrade = GradeUpdate.dao.getPointGradeByPointId(pointid);
			Teachergrade tg = Teachergrade.teachergrade.getPointGradeByPointId(pointid);
			setAttr("another",tg);
			setAttr("pointgrade",pointgrade);
			renderJsp("/teacher/report/tofillreport.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	public void toPrintReport(){
		try{
			logger.info("周报打印");
			String tgId = getPara("pointid");
			String url = getCxt();
			String type=getPara("type");
			StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/weekreportprint.html");
			JSONObject json = reportService.getWeekMailContent(report.toString(), url, tgId,false);
			String content = "";
			if(type.equals("2")){
				content = json.getString("content").replace("display:none;", "");
			}
			renderHtml(content);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void toPrintReportUpdate(){
		try{
			logger.info("翻译周报打印");
			String tgId = getPara("pointid");
			String url = getCxt();
			String type=getPara("type");
			StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/weekreportprint.html");
			JSONObject json = reportService.getWeekMailContentUpdate(report.toString(), url, tgId,false);
			String content = "";
			if(type.equals("2")){
				content = json.getString("content").replace("display:none;", "");
			}
			renderHtml(content);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 *  /report/updateReportDetail
	 *  更新翻译报告修改
	 */
	public void updateReportDetail(){
		logger.info("更新周报");
		try{
			GradeUpdate reportgrade = getModel(GradeUpdate.class);
			String type = getPara("type");
			if(type.equals("2")){
				reportgrade.update();
			}else{
				String teacherfeedback  = reportgrade.get("teacherfeedback");
				String question  = reportgrade.get("question");
				String method  = reportgrade.get("method");
				String homework  = reportgrade.get("homework");
				Teachergrade t = Teachergrade.teachergrade.findById(reportgrade.getInt("id"));
				t.set("teacherfeedback", teacherfeedback).set("question", question)
				.set("method", method).set("homework",homework).update();
			}
			SetPoint sp = SetPoint.dao.findById(GradeUpdate.dao.findById(reportgrade.getInt("id")).getInt("pointid"));
			Date nowdate = new Date();
			sp.set("state", 1).set("submissiontime", nowdate).update();
			if(type.equals("2")){
				redirect("/report/reportListUpdate");
			}else{
				redirect("/report/teacherReports");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	/**
	 * 日报打印
	 */
	public void toPrintDayReport(){
		JSONObject json = new JSONObject();
		logger.info("日报打印");
		String tgId = getPara("pointid");
		String url = getCxt();
		StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/daypaperprint.html");
		json = reportService.getDayReportContent(report.toString(),url,tgId);
		String content = "";
		content = json.getString("content");
		renderHtml(content);
	}
	/**
	 * 翻译日报打印
	 */
	public void toPrintDayReportUpdate(){
		JSONObject json = new JSONObject();
		logger.info("日报打印");
		String tgId = getPara("pointid");
		String url = getCxt();
		StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/daypaperprint.html");
		json = reportService.getDayReportContentUpdate(report.toString(),url,tgId);
		String content = "";
		content = json.getString("content");
		renderHtml(content);
	}
	
	public void toFixDayReport(){
		logger.info("报告列表-----》修改日报");
		String tgid = getPara();
		Record record = reportService.getReportDetail(getPara());
		setAttr("record",record);
		GradeUpdate tg = GradeUpdate.dao.findById(tgid);
		setAttr("tg",tg);
		CoursePlan plan = CoursePlan.coursePlan.getCoursePlan(tg.getInt("COURSEPLAN_ID").toString());//course_name,course_time
		setAttr("plan",plan);
		
		Integer stuid = tg.getInt("studentid");
		CourseOrder co = CourseOrder.dao.getStudentSumHours(stuid.toString());//allhour,real_name
		setAttr("corder",co);
		
		Map<String,Date> nowmap = new HashMap<String,Date>();
		nowmap.put("start", null);
		nowmap.put("end", tg.getDate("COURSE_TIME"));
		Double usedplanhours = CoursePlan.coursePlan.getStudentUsedCoursePlanHours(stuid,nowmap);//used
		setAttr("usedHours",usedplanhours);
		setAttr("leftHours",co.getDouble("allhour")-usedplanhours);
		renderJsp("/teacher/report/layer_tofixdayreport.jsp");
		
	}
	
	
	/**
	 *   /report/submitDayreportFixed
	 *   提交修改
	 */
	public void submitDayreportFixed(){
		logger.info("更新日报");
		JSONObject json = new JSONObject();
		String code = "1";
		String msg = "更新成功";
		try{
			GradeUpdate reportgrade = getModel(GradeUpdate.class);
			reportgrade.update();
		}catch(Exception ex){
			ex.printStackTrace();
			code = "0";
			msg = "更新失败";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	/**
	 *  /report/reportList
	 *  报告列表
	 */
	public void reportList(){
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("sysuserid", getSysuserId().toString());
		String stuname = getPara("studentname");
		if(StrKit.notBlank(stuname)){
			Student student = Student.dao.getStudentByName(stuname);
			if(student!=null){
				queryParam.put("studentid", student.getInt("Id").toString());
			}else{
				queryParam.put("studentid", "0");
			}
		}
		setAttr("studentname",stuname);
		reportService.getReportList(splitPage);
		setAttr("showPages",splitPage.getPage().getList());
		setAttr("teacher",Teacher.dao.getTeachers());
		renderJsp("/teacher/report/reportlist.jsp");
	}
	public void reportListUpdate(){
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("sysuserid", getSysuserId().toString());
		String stuname = getPara("studentname");
		if(StrKit.notBlank(stuname)){
			Student student = Student.dao.getStudentByName(stuname);
			if(student!=null){
				queryParam.put("studentid", student.getInt("Id").toString());
			}else{
				queryParam.put("studentid", "0");
			}
		}
		setAttr("studentname",stuname);
		reportService.getReportListUpdate(splitPage);
		setAttr("showPages",splitPage.getPage().getList());
		setAttr("teacher",Teacher.dao.getTeachers());
		renderJsp("/teacher/report/reportupdatelist.jsp");
	}
	
	
	/**
	 *  /report/getPointTeachergradeId
	 *  跳转取teachergrade的id
	 */
	public void getPointTeachergradeId(){
		JSONObject json = new JSONObject();
		Teachergrade grade = Teachergrade.teachergrade.getPointGradeByPointId(getPara("pointid"));
		if(grade==null){
			json.put("code", "0");
		}else{
			json.put("code", "1");
			json.put("tg", grade);
		}
		renderJson(json);
	}
	
	/** /report/replaceImage
	 * 替换报告模板所用图片
	 */
	public void replaceImage(){
		renderJsp("/teacher/report/uploadImage.jsp");
	}
	
	/** /report/saveReplaceImage
	 * 保存 替换报告模板所用图片
	 */
	public void saveReplaceImage(){
		UploadFile file = getFile("fileField");
		String type = getPara("type");
		String newPath = null;
		if ("head".equals(type)) {
			newPath = "/images/sendmail/logo.png";
		} else if ("tail".equals(type)) {
			newPath = "/images/sendmail/bottom.png";
		}
		String msg = ToolDirFile.replaceFile(file, "png", newPath);
		setAttr("msg", msg);
		if ("head".equals(type)) {
			renderJsp("/teacher/report/import_replace_head.jsp");
		} else if ("tail".equals(type)) {
			renderJsp("/teacher/report/import_replace_tail.jsp");
		}
	}
	
	/**
	 * 预览周报
	 */
	public void previewWeekReport(){
		logger.info("周报预览");
		String tgId = getPara();
		String url = getCxt();
		StringBuffer report = new StringBuffer();
		boolean en = false;
		report = ToolMail.getHtmlTextByURL(url+"/mail/weekreport.html");
		JSONObject json = reportService.getWeekMailContent(report.toString(), url, tgId ,en);
		String content = "";
		if(json.get("code").equals("1"))
			content = json.getString("content");
		renderHtml(content);
		
	}
	/**
	 * 预览翻译周报
	 */
	public void previewWeekReportUpdate(){
		logger.info("翻译周报预览");
		String tgId = getPara();
		String url = getCxt();
		StringBuffer report = new StringBuffer();
		boolean en = false;
		report = ToolMail.getHtmlTextByURL(url+"/mail/weekreport.html");
		JSONObject json = reportService.getWeekMailContentUpdate(report.toString(), url, tgId ,en);
		String content = "";
		if(json.get("code").equals("1"))
			content = json.getString("content");
		renderHtml(content);
	}

	/**
	 * 预览日报
	 */
	public void previewDayReport(){
		JSONObject json = new JSONObject();
		logger.info("日报预览");
		String tgId = getPara();
		String url = getCxt();
		StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/daypaper.html");
		json = reportService.getDayReportContent(report.toString(),url,tgId);
		String content = "";
		if(json.getString("code").equals("1")){
			content = json.getString("content");
		}
		renderHtml(content);
		
	}
	
	/**
	 * 预览翻译日报
	 */
	public void previewDayReportUpdate(){
		JSONObject json = new JSONObject();
		logger.info("日报预览");
		String tgId = getPara();
		String url = getCxt();
		StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/daypaper.html");
		json = reportService.getDayReportContentUpdate(report.toString(),url,tgId);
		String content = "";
		if(json.getString("code").equals("1")){
			content = json.getString("content");
		}
		renderHtml(content);
		
	}
	
	public void sentDayReport(){
		JSONObject json = new JSONObject();
		String tgId = getPara("tgid");
		GradeUpdate tg = GradeUpdate.dao.findById(tgId);
		String type = getPara("type");
		String toMails = getPara("toMails").replaceAll("；", ";");
		if(StrKit.notBlank(toMails)){
			StringBuffer sb = new StringBuffer();
			String[] alltos = toMails.split(";");
			if(alltos.length>0){
				for(String to:alltos){
					sb.append(",").append(to.trim());
				}
				toMails = sb.toString().trim().substring(1);
			}
		}
		String ccMails = getPara("tgccmails");
		if(StrKit.notBlank(ccMails)){
			ccMails = ccMails.replaceFirst("\\|", "");
			ccMails = ccMails.replaceAll("\\|", ",");
		}
		String mailSubject = getPara("mailSubject");
		String url = getCxt();
		if(type.equals("1")){
			logger.info("type类型为1；发送周报.");
			StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/weekreport.html");
			json = reportService.sendWeekMail(toMails,ccMails,mailSubject,report.toString(),url,tgId);
		}
		if(type.equals("2")){
			logger.info("type类型为2；发送日报.");
			StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/daypaper.html");
			json = reportService.sendDayMail(toMails,ccMails,mailSubject,report.toString(),url,tgId);
		}
		if(json.get("code").equals("1")){
			logger.info("发送报告成功，更新发送时间及发送状态0未发、1已发送.");
			tg.set("sendtime", new Date());
			tg.set("issend", 1);
			tg.update();
		}
		renderJson(json);
	}
	public void sendMailMessage(){
		logger.info("跳转至发送弹窗");
		String tgid = getPara("tgid");
		setAttr("tgid",tgid);
		String type = getPara("type");
		setAttr("type",type);
		Record student = null;
		String title="";
		if(type.equals("1")){
			student = Db.findFirst(" select stu.* from teachergrade_update tg "
					+ "	left join jw_setpoint sp on tg.pointid = sp.id "
					+ " left join  account stu on stu.Id = sp.studentid  where tg.id = ? ", tgid);
			title=student.getStr("real_name")+"的课程周报_"+ToolDateTime.format(ToolDateTime.getDate(), "yyyy-MM-dd");
		}
		if(type.equals("2")){
			student = Db.findFirst(" select stu.* from teachergrade_update tg "
					+ " left join courseplan sp on tg.courseplan_id = sp.id "
					+ " left join  account stu on stu.Id = tg.studentid  where tg.id = ? ", tgid);
			title=student.getStr("real_name")+"的课程日报_"+ToolDateTime.format(ToolDateTime.getDate(), "yyyy-MM-dd");
		}
		setAttr("student",student);
		setAttr("title",title);
		List<SysUser> userList = SysUser.dao.find("select * from account where state=0 and "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', roleids) ) = 0 "
				+ " and email like '%@%'");
		setAttr("userList",userList);
		renderJsp("/teacher/report/layer_sendmessage.jsp");
	}
	
	
}
