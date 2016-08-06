package com.momathink.teaching.course.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Classroom;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.teacher.model.Teachergrade;

public class CourseplanService extends BaseService {
	public static final CoursePlan dao = new CoursePlan();
	
	public void list(SplitPage splitPage) {
		String select = "SELECT s.REAL_NAME STUNAME,t.real_name teachername,cp.isovertime, DATE_FORMAT(cp.course_time,'%Y-%m-%d') COURSETIME,tr.RANK_NAME ranktime, tr.class_hour,c.COURSE_NAME  courseName,cp.class_id,campus.CAMPUS_NAME,cp.TEACHER_PINGLUN,cp.iscancel,cp.SIGNIN  signin ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String stuid = queryParam.get("stuid");
		String tid = queryParam.get("tid");
		String courseid = queryParam.get("courseid");
		String signin = queryParam.get("signin");
		String plun = queryParam.get("TEACHER_PINGLUN");
		String iscancel = queryParam.get("ISCANCEL");
		String startTime = ToolString.isNull(queryParam.get("startTime"))?ToolDateTime.getMonthFirstDayYMD(new Date()):queryParam.get("startTime");
		String endTime = ToolString.isNull(queryParam.get("endTime")) ? ToolDateTime.getMonthLastDayYMD(ToolDateTime.getDate(startTime + " 00:00:00")):queryParam.get("endTime");
		formSqlSb.append ( "FROM courseplan cp\n" +
				"LEFT JOIN class_order co ON cp.class_id = co.id\n" +
				"LEFT JOIN account s ON cp.STUDENT_ID = s.Id\n" +
				"left join course c on cp.course_id = c.id  \n" +
				"Left join account t on cp.teacher_id = t.id\n" +
				"LEFT JOIN time_rank tr ON cp.TIMERANK_ID = tr.Id\n" +
				"LEFT JOIN campus ON cp.CAMPUS_ID = campus.Id "+ 
				"WHERE cp.PLAN_TYPE=0 ");
		if(null != stuid && !stuid.equals("") ){
			formSqlSb.append("  and s.ID = ").append(stuid);
			queryParam.put("STUNAME", getStuName(Integer.parseInt(stuid)));
		}
		if (null != tid && !tid.equals("")) {
			formSqlSb.append(" and t.ID = ").append(tid);
		}
		if (null != startTime && !startTime.equals("")) {
			formSqlSb.append(" and cp.course_time >= '").append(startTime).append("'");
		}
		if (null != iscancel && !iscancel.equals("")) {
			formSqlSb.append(" and cp.iscancel= ").append(iscancel);
		}
		if (null != endTime && !endTime.equals("")) {
			formSqlSb.append(" and cp.course_time <= '").append(endTime).append("'");
		}
		if(null != courseid && !courseid.equals("") ){
			formSqlSb.append(" and cp.COURSE_ID = ").append(courseid);
		}
		if(null != signin && !signin.equals("") ){
			formSqlSb.append(" and cp.SIGNIN = ").append(signin);
		}
		if(null != plun && !plun.equals("") ){
			formSqlSb.append(" and cp.TEACHER_PINGLUN= '").append(plun).append("'");
		}

	}

	/**
	 * 课程结束才可以补签
	 * @param id
	 * @return
	 */
	public Map<String, Object> confirmCoursePlan(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		CoursePlan courseplan = CoursePlan.coursePlan.findById(id);
		TimeRank tr = TimeRank.dao.findById(courseplan.getInt("TIMERANK_ID"));
		StringBuffer sb = new StringBuffer(ToolDateTime.format(courseplan.getDate("COURSE_TIME"),"yyyy-MM-dd"));
		sb.append(" ").append(tr.getStr("RANK_NAME").split("-")[1]).append(":00");
		String nowdate = ToolDateTime.format(new Date(),ToolDateTime.pattern_ymd_hms);
		boolean flag =  nowdate.compareTo(sb.toString())>0?true:false;
		if(flag){
			map.put("code", "1");
			map.put("msg", "");
		}else{
			map.put("code", "0");
			map.put("msg", "课程未结束,不能补签.");
		}
		return map;
	}

	/**
	 * 课程结束之后只能让他补签
	 * @param id
	 * @return
	 */
	public Map<String, Object> confirmCoursePlanReadySign(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		CoursePlan courseplan = CoursePlan.coursePlan.findById(id);
		TimeRank tr = TimeRank.dao.findById(courseplan.getInt("TIMERANK_ID"));
		StringBuffer sb = new StringBuffer(ToolDateTime.format(courseplan.getDate("COURSE_TIME"),"yyyy-MM-dd")).append(" ").append(tr.getStr("RANK_NAME").split("-")[1]).append(":00");
		String nowdate = ToolDateTime.format(new Date(),ToolDateTime.pattern_ymd_hms);
		long end = ToolDateTime.compareDateStr(sb.toString(),nowdate);
		boolean flag =  end>1800000?false:true;
		String startcourse = ToolDateTime.format(courseplan.getDate("COURSE_TIME"),"yyyy-MM-dd")+" "+tr.getStr("RANK_NAME").split("-")[0]+":00";
		long start = ToolDateTime.compareDateStr(nowdate,startcourse);
		boolean bflag = start>1800000?false:true;
		if(flag){
			if(bflag){
				map.put("code", "1");
				map.put("msg", "");
			}else{
				map.put("code", "0");
				map.put("msg", "提前30分钟才可以签到.");
			}
		}else{
			map.put("code", "0");
			map.put("msg", "课程结束超过30分钟，请进行补签.");
		}
		return map;
	}
	public void queryUserMessage(SplitPage splitPage){
		List<Object> paramValue = new ArrayList<Object>();
		StringBuffer select = new StringBuffer("select cp.Id as courseplan_id,cp.COURSE_TIME as courseplan_time,cp.class_id,cp.SIGNIN,cp.signedtime,cp.signcause,   \n" +
				"c.COURSE_NAME ,ast.REAL_NAME as student_name, at.REAL_NAME as teacher_name,\n" +
				"ca.CAMPUS_NAME, cr.NAME as room_name,tr.RANK_NAME, sj.SUBJECT_NAME,\n" +
				"co.classNum ,ct.name as type_name,ap.REAL_NAME as signinperson,tg.singn,tg.singnremark,tg.demohour");
		StringBuffer formSqlSb = new StringBuffer("");
		formSqlSb.append(" from courseplan  cp "
				+ " left join course c on cp.COURSE_ID = c.Id "
				+ " left join account ast on cp.STUDENT_ID = ast.Id "
				+ " left join account at on cp.TEACHER_ID=at.Id "
				+ " left join campus ca on cp.CAMPUS_ID = ca.Id"
				+ " left join classroom cr on cp.ROOM_ID= cr.Id "
				+ " left join time_rank tr on cp.TIMERANK_ID = tr.Id"
				+ " left join subject sj on cp.SUBJECT_ID = sj.Id"
				+ " left join class_order co on cp.class_id = co.id"
				+ " left join class_type ct on co.classtype_id = ct.id"
				+ " left join account ap on cp.signinperson_id = ap.Id"
				+ " left join teachergrade tg ON cp.Id=tg.COURSEPLAN_ID"
				+ " where cp.STATE = 0 and cp.iscancel = 0 AND cp.PLAN_TYPE = 0 ");
		Map<String,String> queryParam = splitPage.getQueryParam();
		if (null == queryParam) {
			return;
		}
		String studentname = queryParam.get("studentname");
		String teachername = queryParam.get("teachername");
		String stuid = queryParam.get("stuid");
		String tid = queryParam.get("tid");
		String courseid = queryParam.get("courseid");
		String signin = queryParam.get("signin");
		String plun = queryParam.get("TEACHER_PINGLUN");
		String iscancel = queryParam.get("ISCANCEL");
		String startTime = queryParam.get("startTime");
		String teacherId = queryParam.get("teacherId");
		String endTime = ToolString.isNull(queryParam.get("endTime")) ? ToolDateTime.getCurDateTime():queryParam.get("endTime");
		if(null != stuid && !stuid.equals("") ){
			formSqlSb.append("and cp.STATE = 0 and stu.ID = ? ");
			paramValue.add(Integer.parseInt(stuid));
			queryParam.put("STUNAME", getStuName(Integer.parseInt(stuid)));
		}
		if(null != teacherId && !teacherId.equals("") ){
			formSqlSb.append("and cp.STATE = 0 and cp.teacher_id = ? ");
			paramValue.add(Integer.parseInt(teacherId));
			queryParam.put("teacherId", teacherId);
		}
		if(null != studentname && !studentname.equals("") ){
			formSqlSb.append("and  ast.real_name like ? ");
			paramValue.add("%" + studentname + "%");
			queryParam.put("studentname",studentname);
		}
		if(null != teachername && !teachername.equals("") ){
			formSqlSb.append("and  at.real_name like ? ");
			paramValue.add("%" + teachername + "%");
			queryParam.put("teachername", teachername);
		}
		if (null != tid && !tid.equals("")) {
			formSqlSb.append(" and LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', t.roleids) ) > 0  and t.ID = ? ");
			paramValue.add(Integer.parseInt(tid));
			formSqlSb.append(" and cp.id not in (select id from courseplan as cplan where cplan.state=0 and cplan.class_id <> 0 ) ");

		}
		if (null != startTime && !startTime.equals("")) {
			formSqlSb.append(" and cp.course_time >= ? ");
			paramValue.add(startTime);
		}
		if (null != iscancel && !iscancel.equals("")) {
			formSqlSb.append(" and cp.iscancel= ? ");
			paramValue.add(iscancel);
		}
		if (null != endTime && !endTime.equals("")) {
			formSqlSb.append(" and cp.course_time <= ? ");
			paramValue.add(endTime);
		}
		if(null != courseid && !courseid.equals("") ){
			formSqlSb.append(" and cp.COURSE_ID = ? ");
			paramValue.add(courseid);
		}
		if(null != signin && !signin.equals("") ){
			formSqlSb.append(" and cp.SIGNIN = ? ");
			paramValue.add(signin);
		}
		if(null != plun && !plun.equals("") ){
			formSqlSb.append(" and cp.TEACHER_PINGLUN = ? ");
			paramValue.add(plun);
		}
		
		formSqlSb.append(" ORDER BY cp.COURSE_TIME DESC,tr.RANK_NAME,room_name ");
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSqlSb.toString(), paramValue.toArray());
		splitPage.setPage(page);
	}
	private String getStuName(Integer stuId) {
		Student stu = Student.dao.findById(stuId);
		return stu.getStr("REAL_NAME");
	}

	/*
	 * private String getTeacherName(Integer tId) { Teacher teacher =
	 * Teacher.dao.findById(tId); return teacher.getStr("REAL_NAME"); }
	 */

	public Map<String, Float> queryCourseplanInfo(Integer studentId, String start, String end) {
		Map<String, Float> result = new HashMap<String, Float>();
		float y = 0;
		float x = 0;
		if (studentId != null) {
			List<CoursePlan> cList = dao.findByStudentId(studentId, start, end);
			for (CoursePlan c : cList) {
				Integer classId = c.getInt("class_id");
				float classhour = c.getBigDecimal("class_hour").floatValue();
				if (classId == null || classId == 0) {// 1对1
					y += classhour;
				} else {// 小班
					x += classhour;
				}
			}
		}
		result.put("vip", y);
		result.put("xb", x);
		return result;
	}

	public List<CoursePlan> queryCourseByMediatorId(Integer mediator) {

		return null;
	}

	public JSONObject deleteCoursePlan(Integer planId,Integer operateId,String delreason,String enforce) {
		JSONObject json = new JSONObject(); 
		String code="0";
		String msg = "删除成功";
		SysUser user = SysUser.dao.findById(operateId);
		if(planId != null){
			CoursePlan coursePlan = CoursePlan.coursePlan.findById(planId);
			if (coursePlan != null) {
				Student student = Student.dao.findById(coursePlan.getInt("student_id"));
				String today = ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd)+" 00:00:00";
				String courseTime = ToolDateTime.format(coursePlan.getTimestamp("course_time"),ToolDateTime.pattern_ymd)+" 00:00:00";
				long betw = ToolDateTime.compareDateStr(today, courseTime);
				if(coursePlan.getInt("PLAN_TYPE")==2){
					if(betw>=0 || Role.isAdmins(user.getStr("roleids"))){
						String sql3 = "delete from  courseplan where ID=? ";
						Db.update(sql3, coursePlan.getPrimaryKeyValue());
						code="1";
					}else{
						code="0";
						msg = "非管理员不可删除今日(包括)之前休息安排";
					}
				}else{
					if (coursePlan.getInt("SIGNIN") != 0 && !"yes".equals(enforce)) {
						code="2";
						msg="已签到禁止删除该排课记录";
					} else {
						Integer banid = coursePlan.getInt("class_id");
						if(betw>=0|| Role.isAdmins(user.getStr("roleids"))){
							if("0".equals(banid+"")){//1对1退费
								//更新排课记录的update_time
								String sql2 = "update courseplan set del_msg = ?,update_time=now() where ID=? ";
								Db.update(sql2, delreason,coursePlan.getPrimaryKeyValue());
								coursePlan.set("deluserid",operateId);
								coursePlan.update();
								Teachergrade.teachergrade.deleteByCoursePlanId(planId);
								//将要删除的排课记录添加到courseplan_back表中
								String sql1 = "insert into courseplan_back SELECT * from  courseplan where ID=? ";
								Db.update(sql1, coursePlan.getPrimaryKeyValue());
								//删除courseplan表中的数据
								String sql3 = "delete from  courseplan where ID=? ";
								Db.update(sql3, coursePlan.getPrimaryKeyValue());
								// 发送短信
								if (ToolDateTime.isToday(courseTime)) {
									MessageService.sendMessageToStudent(MesContantsFinal.xs_sms_today_qxpk,  coursePlan.getPrimaryKeyValue().toString());
								}
								code="1";
							}else{//班课取消
								Integer state = student.getInt("state");
								Integer classOrderId = coursePlan.getInt("class_id");
								if("2".equals(state+"")){//删除的为虚拟用户，则删除整个班课下面的学生排课
									Integer timeId = coursePlan.getInt("timerank_id");
									String sql2 = "update courseplan set deluserid = ? ,del_msg = ? ,update_time=now() where class_id=? and course_time=? and timerank_id=? ";
									Db.update(sql2,operateId,delreason,banid,coursePlan.getTimestamp("course_time"),timeId);
									//将要删除的排课记录添加到courseplan_back表中
									String sql1 = "insert into courseplan_back SELECT * from  courseplan where class_id=? and course_time=? and timerank_id=? ";
									Db.update(sql1, banid,coursePlan.getTimestamp("course_time"),timeId);
									//删除courseplan表中的数据
									String sql3 = "delete from  courseplan where class_id=? and course_time=? and timerank_id=? ";
									Db.update(sql3, banid,coursePlan.getTimestamp("course_time"),timeId);
									Teachergrade.teachergrade.deleteByCoursePlanId(planId);
									ClassOrder classOrder = ClassOrder.dao.findById(classOrderId);
									classOrder.set("endTime", null).update();
								}else{//删除小班中的某一个学生的排课
									//更新排课记录的update_time
									String sql2 = "update courseplan set del_msg = ?, update_time=now() where ID=? ";
									Db.update(sql2, delreason,coursePlan.getPrimaryKeyValue());
									//将要删除的排课记录添加到courseplan_back表中
									coursePlan.set("deluserid",operateId);
									coursePlan.update();
									String sql1 = "insert into courseplan_back SELECT * from  courseplan where ID=? ";
									Db.update(sql1, coursePlan.getPrimaryKeyValue());
									//删除courseplan表中的数据
									String sql3 = "delete from  courseplan where ID=? ";
									Db.update(sql3, coursePlan.getPrimaryKeyValue());
								}
								code="1";
							}
						}else{
							msg="非管理员不可删除今日之前的排课";
						}
					}
				}
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}
	
	public JSONObject deleteCoursePlans(String planid){
		JSONObject json = new JSONObject();
		String sql1 = "insert into courseplan_back SELECT * from  courseplan where id= ? ";
		Db.update(sql1, planid);
		json.put("code", 1);
		return json;
	}
	
	public JSONObject doAddCoursePlans(String type,String timeId,String studentId,String courseTime,CoursePlan saveCoursePlan,String teacherId,String roomId,String campusId,
			String subjectid,String plantype,String netCourse,String remark,String courseId,Integer classOrderId,String planType,
			String rankId,CoursePlan newCoursePlan,Integer sysuerid){
		JSONObject json = new JSONObject();
		String code = "0";
		String msg = "保存成功！";
		try {
//			String type = getPara("type");
			Date nowdate = ToolDateTime.getDate();
			TimeRank timeRank = TimeRank.dao.findById(Integer.parseInt(timeId));
			double classhour = timeRank.getBigDecimal("class_hour").doubleValue();
			if (type.equals("1")) { 
//				CoursePlan cp = CoursePlan.coursePlan.getStuCoursePlan(studentId, timeId, getPara("dayTime"));
				CoursePlan cp = CoursePlan.coursePlan.getStuCoursePlan(studentId, timeId, courseTime);
				if (cp == null) {
					Account account = Account.dao.findById(Integer.parseInt(studentId));
					json.put("havecourse", "0");
					double zks = CourseOrder.dao.getVIPzks(account.getPrimaryKeyValue());
					double ypks = CoursePlan.coursePlan.getUseClasshour(studentId, null);// 全部已用课时
					double syks = ToolArith.sub(zks, ypks);// 剩余课时
//					CoursePlan saveCoursePlan = getModel(CoursePlan.class);
//					String courseTime = getPara("dayTime");
					DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
					saveCoursePlan.set("course_time", dd.parse(courseTime));
					saveCoursePlan.set("create_time", nowdate);
					saveCoursePlan.set("update_time", nowdate);
					saveCoursePlan.set("class_id", 0);
					saveCoursePlan.set("student_id", studentId);
					saveCoursePlan.set("teacher_id", teacherId.equals("") ? null : teacherId);
					saveCoursePlan.set("room_id", roomId);
					saveCoursePlan.set("timerank_id", timeId);
					saveCoursePlan.set("campus_id", campusId);
					saveCoursePlan.set("SUBJECT_ID", subjectid);
					saveCoursePlan.set("plan_type", plantype);
//					saveCoursePlan.set("plan_type", getPara("plantype"));
//					saveCoursePlan.set("netCourse", getPara("netCourse"));
					saveCoursePlan.set("netCourse", netCourse);
					saveCoursePlan.set("adduserid", sysuerid);
					if (remark == null || remark.equals("")) {
						saveCoursePlan.set("remark", "暂无");
					} else {
						saveCoursePlan.set("remark", ToolString.replaceBlank(remark));
					}
					if (plantype.equals("1")) {
						saveCoursePlan.set("course_id", "-" + courseId);
						code = "1";
						saveCoursePlan.save();
					} else {
						saveCoursePlan.set("course_id", courseId);
						if (syks > 0 && plantype.equals("0")) {
							if (timeRank.getBigDecimal("class_hour").doubleValue() > syks) {
								msg = account.getStr("real_name") + "剩余" + syks + "课时,该时段课时为" + timeRank.getBigDecimal("class_hour") + "课时";
							} else {
								saveCoursePlan.save();
								//查看该学生是否第一次排课
								@SuppressWarnings("static-access")
								Integer num=CoursePlan.coursePlan.getUseCourse(Integer.parseInt(studentId),Integer.parseInt(teacherId));
								if(num==1){
									//向老师发送学生排课信息邮件
									MessageService.sendMessageToTeacher(String.valueOf(saveCoursePlan.getPrimaryKeyValue()),MesContantsFinal.dd_email_tjpk);
									MessageService.sendMessageToTeacher(String.valueOf(saveCoursePlan.getPrimaryKeyValue()),MesContantsFinal.ls_email_tjpk);
								}
								
								// 发送短信
								if (ToolDateTime.isToday(courseTime)) {
									MessageService.sendMessageToStudent(MesContantsFinal.xs_sms_today_tjpk, saveCoursePlan.getPrimaryKeyValue().toString());
								}
								code = "1";
								msg = "success";
							}
						} else {
							code = "0";
							msg = account.getStr("real_name") + "课时不足，请购买课时。";
						}
					}
				} else {
					json.put("havecourse", "1");
				}
			} else {
				DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
//				Integer classOrderId = getParaToInt("banci_id");
				ClassOrder ban = ClassOrder.dao.findById(classOrderId);
				if (ban == null) {
					code = "0";
					msg = "班课不存在";
				} else {
					double lessonNum = ban.getInt("lessonNum");
					double ypks = CoursePlan.coursePlan.getClassYpkcClasshour(classOrderId);
					if (classhour > ToolArith.sub(lessonNum, ypks) && planType == "0") {
						code = "0";
						msg = "该班课课时不足";
					} else {
						List<CoursePlan> cp = CoursePlan.coursePlan.getClassCoursePlan(classOrderId, timeId, courseTime);
						if (cp.size() == 0) {
							json.put("havecourse", "0");
						//	String sql = "SELECT DISTINCT studentid from crm_courseorder WHERE classorderid=? ";// 根据班次id取出所有具有此班次的学生id
							//List<Record> banUser = Db.find(sql, classOrderId);
							Integer xnAccountId = ban.getInt("accountid");// 虚拟班课账户ID

//							CoursePlan saveCoursePlan = getModel(CoursePlan.class);
							saveCoursePlan.set("course_time", dd.parse(courseTime));
							saveCoursePlan.set("create_time", nowdate);
							saveCoursePlan.set("update_time", nowdate);
							saveCoursePlan.set("class_id", classOrderId);
							saveCoursePlan.set("student_id", xnAccountId);
							saveCoursePlan.set("SUBJECT_ID", subjectid);
							saveCoursePlan.set("room_id",roomId);
							saveCoursePlan.set("timerank_id", rankId);
							saveCoursePlan.set("campus_id", campusId);
							saveCoursePlan.set("plan_type", plantype);
							saveCoursePlan.set("netCourse", netCourse);
							saveCoursePlan.set("adduserid", sysuerid);
							if (plantype.equals("1")) {// 模考
								saveCoursePlan.set("course_id", "-" + courseId);
							} else {
								saveCoursePlan.set("teacher_id", teacherId);
								saveCoursePlan.set("course_id", courseId);
							}
							if (remark == null || remark.equals("")) {
								saveCoursePlan.set("remark", "暂无");
							} else {
								saveCoursePlan.set("remark", ToolString.replaceBlank(remark));
							}
							saveCoursePlan.set("STATE", 0);
							saveCoursePlan.save();
							/*for (Record record : banUser) {
								Integer accountId = record.getInt("studentid");
								List<CourseOrder> orders = CourseOrder.dao.findPaidOrderByClassId(classOrderId);
								// 非虚拟用户需要判断是否已付费
								for (CourseOrder o : orders) {
									Integer _studentId = o.getInt("studentid");// 付费的学生账户ID
									if (accountId.equals(_studentId)) {
//										CoursePlan newCoursePlan = getModel(CoursePlan.class);
										newCoursePlan.set("course_time", dd.parse(courseTime));
										newCoursePlan.set("create_time", nowdate);
										newCoursePlan.set("update_time", nowdate);
										newCoursePlan.set("class_id", classOrderId);
										newCoursePlan.set("student_id", accountId);
										newCoursePlan.set("SUBJECT_ID", subjectid);
										newCoursePlan.set("room_id", roomId);
										newCoursePlan.set("timerank_id", rankId);
										newCoursePlan.set("campus_id", campusId);
										newCoursePlan.set("plan_type", plantype);
										newCoursePlan.set("netCourse", netCourse);
										if (plantype.equals("1")) {// 模考
											newCoursePlan.set("course_id", "-" + courseId);
										} else {
											newCoursePlan.set("teacher_id", teacherId);
											newCoursePlan.set("course_id", courseId);
										}
										if (StringUtils.isEmpty(remark)) {
											newCoursePlan.set("remark", "暂无");
										}else{
											newCoursePlan.set("remark", ToolString.replaceBlank(remark));
										}
										newCoursePlan.save();
										if (ToolDateTime.isToday(courseTime)) {
											MessageService.sendMessageToStudent(MesContantsFinal.xs_sms_today_tjpk, newCoursePlan.getPrimaryKeyValue()
													.toString());
										}
									} else {
										continue;
									}
								}

							}*/
							ClassOrder.dao.updateTeachTime(classOrderId);// 更新开班时间和结束时间
							code = "1";
							msg = "success";

						} else {// 在该日期下的该时间段内，该学生或小班已有排课
							json.put("havacourse", "1");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = "0";
			msg = "数据保存异常，请联系系统管理员！";
		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}

	/**
	 * 交叉时间内所有课程
	 * @param rankid
	 * @param coursetime
	 * @param stuid
	 * @param tchid
	 * @return
	 */
	public List<CoursePlan> getPlanListByRankIdArround(String rankid, String coursetime, String stuid, String tchid) {
		String sql = " SELECT distinct cp.id,cp.course_time,cp.STUDENT_ID,cp.TEACHER_ID,cp.COURSE_ID,cp.ROOM_ID,cp.TIMERANK_ID,cp.PLAN_TYPE,cp.CAMPUS_ID,cp.startrest,cp.endrest, "
				+ " stu.REAL_NAME stuname, tch.REAL_NAME tchname, tr.RANK_NAME,course.COURSE_NAME,campus.CAMPUS_NAME,room.name roomname,sub.subject_name from courseplan cp "
				+ " LEFT JOIN  account stu ON stu.Id = cp.STUDENT_ID LEFT JOIN account tch ON tch.Id=cp.TEACHER_ID "
				+ " LEFT JOIN course on course.Id=cp.COURSE_ID LEFT JOIN time_rank tr ON tr.Id=cp.TIMERANK_ID "
				+ " LEFT JOIN campus ON campus.Id=cp.CAMPUS_ID left join subject sub on sub.id=cp.subject_id "
				+ " left join classroom room on room.id=cp.room_id  WHERE (cp.STUDENT_ID= ? OR cp.TEACHER_ID = ? )"
				+ " AND cp.COURSE_TIME = ?  ";
		List<CoursePlan> allplans = CoursePlan.coursePlan.find(sql, stuid, tchid, coursetime);
		List<CoursePlan> planlist = new ArrayList<CoursePlan>(allplans.size());
		TimeRank rank = TimeRank.dao.findById(rankid);
		Integer rankstart = Integer.parseInt(rank.getStr("rank_name").split("-")[0].replace(":", ""));
		Integer rankend = Integer.parseInt(rank.getStr("rank_name").split("-")[1].replace(":", ""));
		if(allplans!=null&&allplans.size()>0){
			for(CoursePlan plan:allplans){
				String plantype = plan.getInt("plan_type").toString();
				Integer startplan = null;
				Integer endplan = null;
				if(plantype.equals("2")){
					startplan = Integer.parseInt(plan.getStr("startrest").replace(":", ""));
					endplan = Integer.parseInt(plan.getStr("endrest").replace(":", ""));
				}else{
					startplan = Integer.parseInt(plan.getStr("rank_name").split("-")[0].replace(":", ""));
					endplan = Integer.parseInt(plan.getStr("rank_name").split("-")[1].replace(":", ""));
				}
				if((rankstart<=startplan&&rankend>startplan)||(rankstart>=startplan&&rankstart<endplan)){
					planlist.add(plan);
				}
			}
		}
		return planlist;
	}

	/**
	 * 该校区当天教室占用情况
	 * @param campusid
	 * @param coursetime
	 * @return
	 */
	public Map<String, Object> getCampusDayRoomMsgMap(String campusid, String coursetime,String rankId) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<TimeRank> tr = TimeRank.dao.getTimeRank();
		String[] timeNames = TimeRank.dao.findById(Integer.parseInt(rankId)).getStr("RANK_NAME").split("-");
		int beginTime = Integer.parseInt(timeNames[0].replace(":", ""));
		int endTime = Integer.parseInt(timeNames[1].replace(":", ""));
		List<Integer> rankIds = new ArrayList<Integer>();
		rankIds.add(Integer.parseInt(rankId));
		for (TimeRank rec : tr) {
			String rankName = rec.getStr("RANK_NAME");
			String rankTimes[] = rankName.split("-");
			int beginRankTime = Integer.parseInt(rankTimes[0].replace(":", ""));
			int endRankTime = Integer.parseInt(rankTimes[1].replace(":", ""));
			if ((beginTime >= beginRankTime && beginTime < endRankTime) || (endTime > beginRankTime && endTime <= endRankTime)
					|| (beginTime <= beginRankTime && endTime > endRankTime) || (beginTime >= beginRankTime && endTime <= endRankTime)) {
				rankIds.add(rec.getInt("Id"));
			}
		}
		String rids = rankIds.toString().replace("[", "(").replace("]", ")");
		List<Classroom> roomlists = Classroom.dao.getClassRoombyCampusid(campusid);
		String dayroomids = CoursePlan.coursePlan.getCampusDayRoomids(campusid,coursetime,rids);
		dayroomids = ","+dayroomids+",";
		for(Classroom room:roomlists){
			if(StrKit.isBlank(dayroomids))
				room.put("code","0");
			else
			{
				String roompkids = ","+room.getPrimaryKeyValue()+",";
				if(dayroomids.indexOf(roompkids)!=-1)
					room.put("code", "1");
				else
					room.put("code", "0");
			}
		}
		map.put("roomlists", roomlists);
		List<CoursePlan> roomplans = CoursePlan.coursePlan.getCampusDayRoomPlans(campusid, coursetime);
		map.put("roomplans", roomplans);
		return map;
	}
	/**
	 * 多选删除课程
	 * @param cpid
	 */
	public boolean isDeleteMultipleChoice(String cpids,Integer userid) {
		boolean flag =false;
		try{
			String[] ids = cpids.split(",");
			for(String id :ids){
				CoursePlan cp = CoursePlan.coursePlan.findById(id);
				cp.set("deluserid", userid).set("update_time", new Date()).update();
				String sql = "insert into courseplan_back SELECT * from  courseplan where id= ? ";
				Db.update(sql, id);
				cp.delete();
			}
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 按日历格式展示课程安排情况查询
	 * @author David
	 * @param queryParams 页面查询条件封装到Map对象中
	 * @return 返回Calendar插件需要的json格式
	 */
	public String queryCoursePlanForCalendar(Map<String, String> queryParams) {
		List<CoursePlan> coursePlanList = CoursePlan.coursePlan.getAllCoursePlan(queryParams);
		if(coursePlanList == null){
			return "";
		}else{
			Map<Integer,String> studentNameInClassMap = Teachergrade.teachergrade.getStudentNames(queryParams);
			Iterator<CoursePlan> coursePlanIterator = coursePlanList.iterator();
			StringBuffer result = new StringBuffer("[");
			while(coursePlanIterator.hasNext()){
				CoursePlan coursePlan = coursePlanIterator.next();
				int classId = coursePlan.getInt("class_id");
				String students = "";
				if(classId!=0){
					students = studentNameInClassMap.get(coursePlan.getInt("id"));
					if(ToolString.isNull(students))
						students="没有学生或没有设置学生班课课表";
				}
				StringBuffer kcsj = new StringBuffer(coursePlan.getStr("kcrq")).append(" ").append(coursePlan.getStr("RANK_NAME").substring(0, coursePlan.getStr("RANK_NAME").indexOf("-"))).append(":00");
				String nowDateTime = ToolDateTime.getCurDateTime();
				long ss = ToolDateTime.compareDateStr(kcsj.toString(), nowDateTime);
				if(queryParams.get("language").equals("en_US")){
					result.append(",{title:'").append( coursePlan.getStr("title").replace("授课:一对一", "ONE-ON-ONE TUTORING").replace("课程", "SUBJECT")
							.replace("补排", "Re-enter").replace("已取消课", "Canceled").replace("场地", "LOCATION").replace("日期", "DATE").replace("时段", "TIME").replace("老师", "TUTOR").replace("学生", "STUDENT"));
				}else{
					result.append(",{title:'").append( coursePlan.getStr("title"));
				}
				result.append("',start:new Date(").append(coursePlan.getStr("start")).append(")");
				result.append(",classId:'").append(classId).append("'");
				result.append(",student:'").append(students).append("'");
				result.append(",msg:'").append(coursePlan.getStr("remark")).append("'");
				result.append(",courseplanId:'").append(coursePlan.getInt("id")).append("'");
				result.append(",name:'").append(coursePlan.getStr("classroomname")).append("'");
				result.append(",TIMERANK_ID:'").append(coursePlan.getInt("TIMERANK_ID")).append("'");
				result.append(",datetime:'").append(coursePlan.getStr("START")).append("'");
				result.append(",campus_name:'").append(coursePlan.getStr("campus_name")).append("'");
				result.append(",campustype:'").append(coursePlan.getInt("campustype")).append("'");
				result.append(",netCourse:'").append(coursePlan.getInt("netCourse")).append("'");
				result.append(",plantype:'").append(coursePlan.getInt("planType")).append("'");
				if ("true".equals(queryParams.get("isStudent"))) {
					if (ss > 0) {
						result.append(",signin:'1'");
						result.append(",teacherPinglun:'y'}");
					} else {
						result.append(",signin:'0'");
						result.append(",teacherPinglun:'n'}");
					}
				} else {
					result.append(",signin:'").append(coursePlan.getInt("signin")).append("'");
					result.append(",teacherPinglun:'").append(coursePlan.getStr("teacherPinglun")).append("'}");
				}
			}
			result.append("]");
			return result.toString().replaceFirst(",", "");
		}
	}

	/**
	 * 查询学生课表
	 * @param splitPage
	 */
	public void queryStudentPlan(SplitPage splitPage) {
		
		Map<String, String> queryParam = splitPage.getQueryParam();
		String stuid = queryParam.get("stuid");
		String studentName = queryParam.get("studentName");
		String tid = queryParam.get("teacherId");
		String subjectId = queryParam.get("subjectId");
		String courseid = queryParam.get("courseid");
		String startTime = ToolString.isNull(queryParam.get("startTime"))?ToolDateTime.getMonthFirstDayYMD(new Date()):queryParam.get("startTime");
		String endTime = ToolString.isNull(queryParam.get("endTime")) ? ToolDateTime.getMonthLastDayYMD(ToolDateTime.getDate(startTime + " 00:00:00")):queryParam.get("endTime");
		StringBuffer banWhereSql = new StringBuffer("");
		StringBuffer whereSql = new StringBuffer("");
		if(null != stuid && !stuid.equals("") ){
			whereSql.append("  and s.ID = ").append(stuid);
			queryParam.put("STUNAME", getStuName(Integer.parseInt(stuid)));
		}
		if (null != tid && !tid.equals(""))
			whereSql.append(" and cp.TEACHER_ID = ").append(tid);
		if (null != startTime && !startTime.equals("")) {
			whereSql.append(" and cp.course_time >= '").append(startTime).append("'");
			banWhereSql.append(" and p.course_time >= '").append(startTime).append("'");
			queryParam.put("startTime", startTime);
		}
		if (null != endTime && !endTime.equals("")) {
			whereSql.append(" and cp.course_time <= '").append(endTime).append("'");
			banWhereSql.append(" and p.course_time <= '").append(endTime).append("'");
			queryParam.put("endTime", endTime);
		}
		if(null != courseid && !courseid.equals("") )
			whereSql.append(" and cp.COURSE_ID = ").append(courseid);
		if(null != subjectId && !subjectId.equals("") )
			whereSql.append(" and cp.subject_id = ").append(courseid);
		if(!ToolString.isNull(studentName))
			whereSql.append(" and s.REAL_NAME='").append(studentName).append("'");
		String selectSql = "SELECT *";
		String fromSql = "FROM (\n" +
				"(SELECT s.REAL_NAME SNAME, cp.COURSE_TIME,cp.ranktime,cp.COURSE_NAME,cp.teachername,cp.class_hour,cp.CAMPUS_NAME, cp.class_id,s.id sid,cp.TEACHER_PINGLUN\n" +
				"FROM teachergrade tg \n" +
				"LEFT JOIN account s ON tg.studentid=s.Id\n" +
				"LEFT JOIN (SELECT p.Id,p.TEACHER_PINGLUN,p.class_id,p.CAMPUS_ID,c.CAMPUS_NAME,p.COURSE_TIME,t.class_hour,t.RANK_NAME ranktime,tc.REAL_NAME teachername,k.COURSE_NAME,p.TEACHER_ID,p.SUBJECT_ID,p.COURSE_ID\n" +
				"FROM courseplan p LEFT JOIN time_rank t ON p.TIMERANK_ID=t.Id LEFT JOIN campus c ON p.CAMPUS_ID=c.Id LEFT JOIN account tc ON p.TEACHER_ID=tc.Id\n" +
				"LEFT JOIN course k ON p.COURSE_ID=k.Id WHERE p.PLAN_TYPE=0 " + banWhereSql.toString()+
				") cp ON tg.COURSEPLAN_ID=cp.Id\n" +
				"WHERE cp.class_id!=0 AND s.STATE!=2 " +whereSql.toString()+
				"\nUNION ALL\n" +
				"(SELECT s.REAL_NAME SNAME, cp.COURSE_TIME,tr.RANK_NAME ranktime,k.COURSE_NAME,tc.real_name teachername,tr.class_hour,c.CAMPUS_NAME, cp.class_id,s.id sid,cp.TEACHER_PINGLUN\n" +
				"FROM courseplan cp  \n" +
				"LEFT JOIN account s ON cp.STUDENT_ID = s.Id\n" +
				"LEFT JOIN account tc ON cp.TEACHER_ID=tc.Id\n" +
				"LEFT JOIN time_rank tr ON cp.TIMERANK_ID = tr.Id  \n" +
				"LEFT JOIN campus c ON cp.CAMPUS_ID = c.Id\n" +
				"LEFT JOIN course k ON cp.COURSE_ID=k.Id\n" +
				"WHERE cp.plan_type=0 and cp.STATE = 0 AND cp.class_id = 0 "+whereSql.toString()+" ))) a ";
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), selectSql,fromSql+"ORDER BY a.course_time, a.ranktime");
		splitPage.setPage(page);
	}
	
}
