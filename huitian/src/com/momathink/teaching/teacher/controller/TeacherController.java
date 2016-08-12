package com.momathink.teaching.teacher.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.base.Util;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.campus.model.Classroom;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.reservation.model.Reservation;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.teacher.model.Announcement;
import com.momathink.teaching.teacher.model.Receiver;
import com.momathink.teaching.teacher.model.Teacher;
import com.momathink.teaching.teacher.model.TeacherRest;
import com.momathink.teaching.teacher.model.Teachergroup;
import com.momathink.teaching.teacher.service.TeacherGroupService;
import com.momathink.teaching.teacher.service.TeacherService;

@Controller(controllerKey = "/teacher")
public class TeacherController extends BaseController {
	private static final Logger logger = Logger.getLogger(TeacherController.class);
	private TeacherService teacherService = new TeacherService();
	private TeacherGroupService groupService = new TeacherGroupService();

	/**
	 * 教师列表*
	 */
	public void index() {
		teacherService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/teacher/teacher_list.jsp");
	}

	/**
	 * 教师暂停列表
	 */
	public void stopTeacherIndex() {
		teacherService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/teacher/teacher_list.jsp");
	}

	// 教研分组 list方法
	public void tgroup() {
		groupService.list(splitPage);
		setAttr("teacherList", splitPage.getPage());
		@SuppressWarnings("unchecked")
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> olist = page.getList();
		for (Record r : olist) {
			Teachergroup tg = Teachergroup.dao.findById(r.getInt("id"));
			String str = tg.get("teacherids");
			String[] grouparrays = str.split("\\|");
			String name = "";
			for (int i = 0; i < grouparrays.length; i++) {
				Teacher t = Teacher.dao.findById(Integer.parseInt(grouparrays[i]));
				name = name + (t.get("real_name") + "、");
			}
			name = name.substring(0, name.length() - 1);
			r.set("name", name);
		}
		render("/teacher/teachergroup.jsp");
	}

	// 教研编辑信息
	public void editgroup() {
		// 查找符合条件的teachergroup对象
		String id = getPara();
		String sql = "SELECT t.updatetime,t.teacherids,t.groupname,t.id,t.leaderid,a.real_name from teachergroup t left join account a on a.Id = t.leaderid where t.id= ? ";
		Teachergroup teacheredit = Teachergroup.dao.findFirst(sql, id);
		List<Teacher> teacher = Teacher.dao
				.find("SELECT REAL_NAME,Id from account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', roleids) )> 0");
		setAttr("teacher", teacher);// 保存
		setAttr("teacheredit", teacheredit);
		render("/teacher/teachergroup_form.jsp");
	}

	// 修改教研分组信息
	public void updategroup() {
		Teachergroup teachergroup = getModel(Teachergroup.class);
		String teacherids = teachergroup.getStr("teacherids").replaceFirst("\\|", "");
		teachergroup.set("teacherids", teacherids);
		teachergroup.set("updatetime", new Date());
		teachergroup.update();
		redirect("/teacher/tgroup");
	}

	// 教研保存
	public void savegroup() {
		System.out.println("添加 ");
		Teachergroup teachergroup = getModel(Teachergroup.class);
		String teacherids = teachergroup.getStr("teacherids").replaceFirst("\\|", "");
		teachergroup.set("teacherids", teacherids);
		teachergroup.set("createtime", new Date());
		teachergroup.save();
		redirect("/teacher/tgroup");
	}

	/*
	 * 验证名字的唯一性
	 */
	public void checkGroupName() {
		JSONObject json = new JSONObject();
		String name = getPara("name");
		Teachergroup teacherGroup = Teachergroup.dao.findFirst("select * from teachergroup where groupname = ? ", name);
		if (teacherGroup != null) {
			json.put("code", 0);
			json.put("msg", "组名称已存在");
		} else {
			json.put("code", 1);
			json.put("msg", "");
		}
		renderJson(json);
	}

	// 教研添加
	public void findOfadd() {
		setAttr("teacher", Teacher.dao.getTeachers());
		render("/teacher/teachergroup_form.jsp");
	}

	// 教研组删除
	public void deletegroup() {
		Teachergroup.dao.deleteById(getParaToInt());
		redirect("/teacher/tgroup");

	}

	// 查看分组按钮
	public void showgroup() {
		// JSONObject json = new JSONObject();
		String groupid = getPara();
		// 获得分组对象
		Teachergroup teachgroup = Teachergroup.dao.findById(Integer.parseInt(groupid));
		// 获得组成员id
		String str = teachgroup.getStr("teacherids");
		// String[] grouparr = str.split("\\|");
		String ids = str.replace("|", ",");
		// 获得负责人id
		Teacher tobj = Teacher.dao.findById(teachgroup.getInt("leaderid"));
		String leadername = tobj.getStr("real_name");
		// String membername = "";
		List<Teacher> teacher = Teacher.dao.find("select * from account where Id in(" + ids + ")");
		setAttr("teacher", teacher);
		/*
		 * for(int i = 0;i < grouparr.length;i++){ Teacher t =
		 * Teacher.dao.findById(Integer.parseInt(grouparr[i])); //membername +=
		 * (t.getStr("real_name")); json.put(grouparr[i],t); }
		 */
		// membername=membername.substring(0, membername.length()-1);
		// teachgroup.put("membername",membername);
		teachgroup.put("leadername", leadername);
		setAttr("teachgroup", teachgroup);
		renderJsp("/teacher/layer_teachergroup.jsp");
	}

	/**
	 * 检查是否存在
	 */
	public void checkExist() {
		String field = getPara("checkField");
		String value = getPara("checkValue");
		String id = getPara("id");
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			Long count = Teacher.dao.queryCount(field, value, id);
			renderJson("result", count);
		} else {
			renderJson("result", null);
		}
	}

	public void add() {
		try {
			setAttr("subjects", Subject.dao.getSubject());
			setAttr("supervisors", SysUser.dao.getDudao());
			List<Opportunity> opportunityList = Opportunity.dao.findIsConver();
			List<Campus> campus = Campus.dao.getCampus();
			setAttr("campus", campus);
			setAttr("olist", opportunityList);
			renderJsp("/teacher/layer_teacher_form.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void edit() {
		try {
			Teacher teacher = Teacher.dao.findById(getParaToInt());
			setAttr("teacher", teacher);
			setAttr("subjects", Subject.dao.getSubject());
			setAttr("supervisors", SysUser.dao.getDudao());
			setAttr("campus", Campus.dao.getCampus());
			List<AccountCampus> accampus = AccountCampus.dao.getCampusidsByAccountid(getParaToInt());
			String campusids = "|";
			if (!accampus.isEmpty()) {
				for (AccountCampus ac : accampus) {
					campusids += ac.getInt("campus_id") + "|";
				}
			}
			setAttr("campusids", campusids);
			List<Opportunity> opportunityList = Opportunity.dao.findIsConver();
			setAttr("olist", opportunityList);
			renderJsp("/teacher/layer_teacher_form.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存用户
	 */
	public void save() {
		JSONObject json = new JSONObject();
		try {
			Teacher teacher = getModel(Teacher.class);

			teacher.set("user_pwd", ToolMD5.getMD5("111111"));
			teacher.set("create_time", new Date());
			teacher.set("createuserid", getSysuserId());
			Integer teacherid = teacherService.save(teacher);
			String campusids = getPara("campusids");
			if (campusids.length() != 0) {
				String[] str = campusids.replace("|", ",").split(",");
				for (int i = 0; i < str.length; i++) {
					AccountCampus acc = new AccountCampus();
					acc.set("account_id", teacherid).set("campus_id", str[i]).save();
				}
			}
			//保存成功，推送信息到网校系统
			json.put("code", 1);
			
			json.put("userName", teacher.getStr( "real_name" ) );
			json.put("userEmail", teacher.getStr( "email" ) );
			json.put("jwSysUserId", teacherid );
			
			long timestamp = System.currentTimeMillis();
			json.put( "timestamp" , timestamp );
			json.put( "signature" , ToolMD5.getMD5( ToolMD5.getMD5( timestamp + "huitian" ) ) );
			
		} catch (Exception ex) {
			logger.error( "saveTeacherAccount" , ex );
			json.put("code", 0);
			json.put("msg", "保存异常，请刷新页面重试");
		}
		renderJson(json);
	}
	
	public void manualBatchSynchroTeacherAccount() {
		JSONObject pushSource = new JSONObject();
		List< JSONObject > sourceList = new LinkedList< JSONObject >();
		List< Teacher > unsynchroTeacherList = Teacher.dao.queryUnsynchroTeacherAccountList();
		for( int i = 0 , size = unsynchroTeacherList.size() ; i < size ; i++ ) {
			Teacher teacherAccount = unsynchroTeacherList.get( i );
			JSONObject teacherSource = new JSONObject();
			teacherSource.put( "jwSysUserId" , teacherAccount.getInt( "id" ) );
			teacherSource.put( "userName" , teacherAccount.getStr( "REAL_NAME" ) );
			teacherSource.put( "userEmail" , teacherAccount.getStr( "EMAIL" ) );
			sourceList.add( teacherSource );
		}
		pushSource.put( "list" , sourceList );
		
		long timestamp = System.currentTimeMillis();
		pushSource.put( "timestamp" , timestamp );
		pushSource.put( "signature" , ToolMD5.getMD5( ToolMD5.getMD5( timestamp + "huitian" ) ) );
		
		renderJson( pushSource );
	}
	
	public void manualBatchPushResult() {
		String sourceList = getPara( "list" ) ;
		JSONArray jsonArr = JSONArray.parseArray( sourceList );
		long arrSize = jsonArr.size() ;
		for( int i = 0 ; i < arrSize ; i ++ ) {
			JSONObject accountJson = jsonArr.getJSONObject( i );
			if( "1".equals( accountJson.getString( "code" ) )  ) {
				Teacher.dao.updateTeacherNetAccountId( accountJson.getString( "jwSysUserId" ) , accountJson.getString( "netAccountId" ) );
			}
		}
		renderJson( "msg" , "Success!" );
	}

	/**
	 * 更新用户
	 */
	public void update() {
		JSONObject json = new JSONObject();

		try {
			String str = getPara("campusids");
			String id = getPara("teacher.id");
			Teacher teacher = getModel(Teacher.class);
			teacherService.update(teacher);
			List<AccountCampus> acc = AccountCampus.dao.getAllCampusidByAccountid(id);
			if (!acc.isEmpty()) {
				for (AccountCampus a : acc) {
					a.delete();
				}
			}
			if (!str.isEmpty()) {
				String[] campusid = str.replace("|", ",").split(",");
				for (int i = 0; i < campusid.length; i++) {
					AccountCampus ac = new AccountCampus();
					ac.set("account_id", id).set("campus_id", campusid[i]).save();
				}
			}
			json.put("code", 1);
			json.put("msg", "数据更新成功");
		} catch (Exception ex) {
			logger.error(ex.toString());
			json.put("code", 0);
			json.put("msg", "数据信息异常，请联系管理员");
		}
		renderJson(json);
	}

	/**
	 * 管理教师课程
	 */
	public void toManageCoursePage() {
		String teacherId = getPara();
		Teacher teacher = Teacher.dao.findById(Integer.parseInt(teacherId));
		List<Subject> subjectList = Subject.dao.getSubject();
		if (!Role.isStudent(teacher.getStr("roleids"))) {
			for (Subject s : subjectList) {
				s.put("isChoose", "0");// 未选择
				Integer sid = s.getInt("id");
				String canChooseCourseSql = "SELECT * FROM course c WHERE c.SUBJECT_ID =? ORDER BY c.SUBJECT_ID,c.ID";
				List<Course> courseList = Course.dao.find(canChooseCourseSql, sid);
				if (!ToolString.isNull(teacher.getStr("subject_id"))) {
					String sids = teacher.getStr("subject_id");
					String[] subjectids = sids.startsWith("\\|") ? sids.substring(1).split("\\|") : sids.split("\\|");
					for (String suid : subjectids) {
						if (sid.toString().equals(suid)) {
							s.put("isChoose", "1");// 选择
							break;
						}
					}
				}
				if (!ToolString.isNull(teacher.getStr("class_type"))) {
					String cids = teacher.getStr("class_type");
					String[] courseids = cids.startsWith("\\|") ? cids.substring(1).split("\\|") : cids.split("\\|");
					for (Course c : courseList) {
						c.put("isChoose", "0");
						String _cid = c.getInt("id").toString();
						for (String cid : courseids) {
							if (_cid.equals(cid)) {
								c.put("isChoose", "1");
								break;
							}
						}
					}
				}
				s.put("courseList", courseList);
			}
		}
		setAttr("subjectids", teacher.getStr("subject_id"));
		setAttr("courseids", teacher.getStr("class_type"));
		setAttr("teacher", teacher);
		setAttr("subjectList", subjectList);
		renderJsp("/teacher/layer_teacher_course.jsp");
	}

	/**
	 * 保存用户
	 */
	public void saveTeacherCourse() {
		JSONObject json = new JSONObject();
		try {
			String teacherId = getPara("tid");
			Integer version = getParaToInt("version");
			Teacher teacher = Teacher.dao.findById(Integer.parseInt(teacherId));
			/*
			 * String[] subject_id = getParaValues("subject_id");//科目id String[]
			 * classTypes = getParaValues("course_id");
			 */
			String[] subject_id = getPara("subjectids").replace("|", ",").split(",");// 科目id
			String[] classTypes = getPara("courseids").replace("|", ",").split(",");
			if (subject_id != null && subject_id.length > 0)
				teacher.set("subject_id", Util.printArray(subject_id));
			if (classTypes != null && classTypes.length > 0)
				teacher.set("class_type", Util.printArray(classTypes));
			teacher.set("version", version);
			teacherService.update(teacher);
			json.put("code", 1);
			json.put("msg", "数据更新成功");
		} catch (Exception ex) {
			logger.error(ex.toString());
			json.put("code", 0);
			json.put("msg", "数据信息异常，请联系管理员");
		}
		renderJson(json);
	}

	public void freeze() {
		try {
			int state = getParaToInt("state");
			int id = getParaToInt("teacherId");
			Teacher user = Teacher.dao.findById(id);
			user.set("state", state);
			teacherService.update(user);
			renderJson("result", "true");
		} catch (Exception e) {
			logger.error("AccountController.freezeAccount", e);
		}
	}

	public void changePassword() {
		JSONObject json = new JSONObject();
		try {
			Integer id = getParaToInt("id");
			String pwd = getPara("password");
			Teacher teacher = Teacher.dao.findById(id);
			teacher.set("USER_PWD", ToolMD5.getMD5(pwd));
			teacherService.update(teacher);
			json.put("result", true);
		} catch (Exception ex) {
			logger.error("error", ex);
			json.put("result", false);
		}
		renderJson(json);
	}

	public void getTeacherIdByName() {
		try {
			String tname = getPara("tname");
			Teacher teacher = Teacher.dao.getTeacherByName(tname);
			renderJson("teacher", teacher);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getAllTeachers() {
		try {
			List<Teacher> teacherlist = Teacher.dao.getTeachers();
			renderJson("teacherlist", teacherlist);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void getTeacherNameById() {
		try {
			String tid = getPara("tid");
			String teacherName = Teacher.dao.getTeacherNameById(tid);
			renderJson("teacherName", teacherName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 给老师安排休息日
	 */
	public void theacherRestDay() {
		try {
			String tid = getPara();
			List<TeacherRest> restList = TeacherRest.dao.getRestDay(tid);
			setAttr("teacherid", tid);
			if (restList.size() == 0) {
				setAttr("code", "0");
			} else {
				setAttr("code", "1");
				TeacherRest teacher = restList.get(restList.size() - 1);
				if (teacher.getDate("starttime") == null) {
					setAttr("teacherrest", teacher);
					restList.remove(restList.size() - 1);
				}
				setAttr("restlist", restList);
			}
			setAttr("tname", Teacher.dao.findById(tid).getStr("REAL_NAME"));

			List<Map<String, String>> hourlist = new ArrayList<Map<String, String>>();
			for (int i = 5; i < 24; i++) {
				Map<String, String> hourmap = new HashMap<String, String>();
				String key = null;
				if (i <= 9)
					key = "0" + i;
				else
					key = i + "";
				hourmap.put("key", key);
				hourmap.put("value", key);
				hourlist.add(hourmap);
			}
			setAttr("hour", hourlist);

			renderJsp("/teacher/layer_setTeacherRestday.jsp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void submitTeacherRestMsg() {
		JSONObject json = new JSONObject();
		try {
			String mon = getPara("monMsg").equals("全天") ? "1" : getPara("monMsg").replaceFirst("\\|", "");
			String tues = getPara("tuesMsg").equals("全天") ? "1" : getPara("tuesMsg").replaceFirst("\\|", "");
			String wed = getPara("wedMsg").equals("全天") ? "1" : getPara("wedMsg").replaceFirst("\\|", "");
			String thur = getPara("thurMsg").equals("全天") ? "1" : getPara("thurMsg").replaceFirst("\\|", "");
			String fri = getPara("friMsg").equals("全天") ? "1" : getPara("friMsg").replaceFirst("\\|", "");
			String sat = getPara("satMsg").equals("全天") ? "1" : getPara("satMsg").replaceFirst("\\|", "");
			String sun = getPara("sunMsg").equals("全天") ? "1" : getPara("sunMsg").replaceFirst("\\|", "");

			String tid = getPara("tid");
			String restid = getPara("restId");
			TeacherRest rest;
			if (StringUtils.isEmpty(restid)) {
				rest = new TeacherRest();
				rest.set("Mon", mon).set("teacherid", tid).set("Tues", tues).set("Wed", wed).set("Thur", thur).set("Fri", fri).set("Sat", sat).set("Sun", sun)
						.set("creattime", new Date()).save();
			} else {
				rest = TeacherRest.dao.findById(restid);
				rest.set("Mon", mon).set("Tues", tues).set("Wed", wed).set("Thur", thur).set("Fri", fri).set("Sat", sat).set("Sun", sun)
						.set("updatetime", new Date()).update();
			}
			json.put("code", "1");
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put("code", "0");
			json.put("msg", "提交失败");
		}
		renderJson(json);

	}

	public synchronized void submitTeacherRestArrange() {
		JSONObject json = new JSONObject();
		String cptime = "";
		String haverest = "";
		String code = "1";
		String msg = "";
		try {
			String restId = getPara("restId");
			String teacherId = getPara("tid");
			String stime = getPara("stime");
			String etime = getPara("endtime");
			Date starttime = ToolDateTime.parse(stime, "yyyy-MM-dd");
			Date endtime = ToolDateTime.parse(etime, "yyyy-MM-dd");
			Campus campus = Campus.dao.getRoundCampusId(teacherId);// 排休时随便安排一个校区
			TeacherRest trest = TeacherRest.dao.findById(restId);// 排休规则
			List<TimeRank> trlist = TimeRank.dao.getTimeRank();// 所有时段
			List<String> printDay = ToolDateTime.printDay(starttime, endtime);
			Map<String, Long> planCountsMap = CoursePlan.coursePlan.getPlanCountsForRest(teacherId, stime, etime);
			boolean flag = false;
			circle: for (String day : printDay) {// 循环每一天要排休的
				String weekday = ToolDateTime.getDateInWeek(ToolDateTime.parse(day, "yyyy-MM-dd"), 0);// 周几
				String dd = weekday.equals("日") ? "Sun" : weekday.equals("一") ? "Mon" : weekday.equals("二") ? "Tues" : weekday.equals("三") ? "Wed" : weekday
						.equals("四") ? "Thur" : weekday.equals("五") ? "Fri" : "Sat";
				String resttime = trest.getStr(dd);
				if (!StringUtils.isEmpty(resttime)) {// 某一天的休息规则，如2016-06-22是周三，则取Wed字段中的排休规则时间段，全天为1
					String[] timelist = resttime.replace("|", ",").split(",");// 一天内有多个时段休息，如08:00-09:00|13:00-14:00
					if (timelist[0].equals("1")) {// 全天
						timelist = new String[] { "00:00--23:59" };
					}
					for (int i = 0; i < timelist.length; i++) {// 循环排休时段
						String tlist = timelist[i];
						Long tcp = 0L;// 判断该休息时段内有没有排课
						String[] tt = tlist.replace(":", "").split("--");// [0800,0900]
						if (timelist[0].equals("1")) {
							long ccs = CoursePlan.coursePlan.queryTeacherCourseplansCounts(teacherId, day);// 取出老师当天的全部的排课和排休次数
							tcp += ccs;
						} else {
							String key = "";
							trc: for (TimeRank tr : trlist) {// 循环所有时段
								String[] ttr = tr.getStr("RANK_NAME").replace(":", "").split("-");
								if ((Integer.parseInt(ttr[0]) <= Integer.parseInt(tt[0]) && Integer.parseInt(ttr[1]) >= Integer.parseInt(tt[1]))
										|| (Integer.parseInt(ttr[0]) >= Integer.parseInt(tt[0]) && Integer.parseInt(ttr[0]) < Integer.parseInt(tt[1]))
										|| (Integer.parseInt(ttr[1]) > Integer.parseInt(tt[0]) && Integer.parseInt(ttr[1]) <= Integer.parseInt(tt[1]))) {// 存在交叉时段
									key = day + tr.getInt("id");// 取出某一天某个时段为key的排课情况
									long cpcounts = planCountsMap.get(key) == null ? 0 : planCountsMap.get(key);
									tcp += cpcounts;
									if (cpcounts > 0) {
										break trc;
									}
								}
							}
						}
						if (tcp > 0) {
							code = "0";
							// msg=day.substring(5, 7)+"月"+day.substring(8,
							// 10)+"日,"+tlist+"有课程冲突，未添加该日休息，其余都已添加。";
							cptime += day.substring(5, 7) + "月" + day.substring(8, 10) + "日" + tlist + ",";
							flag = false;
							continue circle;
						} else {
							List<CoursePlan> trp = CoursePlan.coursePlan.queryTeacherRestPlan(tlist.split("--")[0], tlist.split("--")[1], day, teacherId);// 取出老师的休息
							if (trp.size() == 0) {
								List<TimeRank> tranklist = TimeRank.dao.getTimeRanksDesc(tlist.split("--")[0]);
								List<Classroom> crlist = Classroom.dao.getClassRoombyCampusid(campus.getInt("campus_id").toString());// 取出校区的教室
								Integer timerankid = null;
								Integer classroomid = null;
								crl: for (TimeRank tr : tranklist) {// 查询某个时段下的某个教室有没有排课
									for (Classroom croom : crlist) {
										long crtr = CoursePlan.coursePlan.queryCoursePlanCRTRCount(tr.getInt("id"), croom.getInt("id"), day);
										if (crtr == 0) {// 说明这个日期下的时段这个教室可用
											timerankid = tr.getInt("id");
											classroomid = croom.getInt("id");
											break crl;
										}
									}
								}
								if (timerankid != null && classroomid != null) {// 添加休息找一间空教室排休息
									TimeRank trank = TimeRank.dao.findById(timerankid);
									String fh = trank.getStr("RANK_NAME").split("-")[0].split(":")[0];
									String fm = trank.getStr("RANK_NAME").split("-")[0].split(":")[1];
									String hms = day.trim().substring(0, 10) + " " + (fh.trim().length() == 1 ? ("0" + fh.trim()) : fh.trim()) + ":"
											+ (fm.trim().length() == 1 ? ("0" + fm.trim()) : fm.trim()) + ":00";
									String thms = ToolDateTime.dateToDateString(new Date(), ToolDateTime.DATATIMEF_STR);
									long between = ToolDateTime.compareDateStr(thms, hms);
									Integer recharge = between < 0 ? 1 : 0;
									CoursePlan plan = new CoursePlan();
									plan.set("TIMERANK_ID", timerankid);
									plan.set("ROOM_ID", classroomid);
									plan.set("CAMPUS_ID", campus.getInt("campus_id"));
									plan.set("CREATE_TIME", new Date());
									plan.set("COURSE_TIME", day);
									plan.set("startrest", tlist.split("--")[0]);
									plan.set("endrest", tlist.split("--")[1]);
									plan.set("PLAN_TYPE", 2);
									plan.set("TEACHER_ID", teacherId);
									plan.set("rechargecourse", recharge);
									plan.set("adduserid", getSysuserId());
									plan.set("REMARK", "暂无");
									plan.set("STUDENT_ID", teacherId);
									plan.save();
									flag = true;
									code = "1";
								} else {// 没想到如何处理先空着

								}
							} else {
								code = "0";
								haverest += day.substring(5, 7) + "月" + day.substring(8, 10) + "日" + tlist + ",";// +"该休息时间已添加";
								flag = false;
								continue circle;
							}
						}

					}
				}
				if (flag) {
					trest.set("starttime", stime);
					trest.set("endtime", etime);
					trest.update();
					/*
					 * }else{ code="1"; msg="有休息时段且非重复的日期休息已添加.";
					 */
				}
			}
			if (cptime.length() > 0) {
				msg += cptime + "有课程冲突，未添加该日休息。";
			}
			if (haverest.length() > 0) {
				msg += haverest + "该休息时间已添加";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			code = "0";
			msg = "添加失败!";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);

	}

	public void teacherTongji() {
		setAttr("teacherList", Teacher.dao.getTeachers());
		renderJsp("/account/tongjilog.jsp");
	}

	/*
	 * 查看教师的教学能力
	 */
	public void checkTeacherStyle() {
		String id = getPara();
		Teacher teacher = Teacher.dao.findById(id);
		StringBuffer sdf = new StringBuffer("");
		if (teacher.get("subject_id") != null) {
			List<Subject> subject = Subject.dao.findByIds(teacher.get("subject_id").toString().replace("|", ","));
			for (Subject c : subject) {
				sdf.append(c.get("SUBJECT_NAME")).append("丶");
			}
		} else {
			sdf.append("暂无教学科目,");
		}
		setAttr("subject", sdf.toString().substring(0, sdf.toString().length() - 1));
		StringBuffer sf = new StringBuffer("");
		if (teacher.get("class_type") != null) {
			List<Course> course = Course.dao.getTeacherCourse(teacher.get("class_type").toString().replace("|", ","));
			for (Course c : course) {
				sf.append(c.get("COURSE_NAME")).append("丶");
			}
		} else {
			sf.append("暂无教学课程,");
		}
		setAttr("course", sf.toString().substring(0, sf.toString().length() - 1));
		setAttr("result", teacher);
		setAttr("style", teacher.get("style") != null ? teacher.get("style") : "暂无介绍");
		setAttr("ability", teacher.get("ability") != null ? teacher.get("ability") : "暂无介绍");
		renderJsp("/teacher/layer_checkTeacherStyle.jsp");
	}

	/**
	 * 教师发送公告*
	 */
	public void sendMessage() {
		try {
			Teacher t = Teacher.dao.findById(getSysuserId());
			setAttr("teacher", t);
			List<SysUser> userlist = SysUser.dao.findAllSysUser();
			String ids = "";
			for (SysUser user : userlist) {
				boolean flag = false;
				// 获取每个用户的角色权限
				String roleids = user.getStr("roleids");
				List<Role> rolelist = Role.dao.getRoleNamesByIds(roleids.substring(0, roleids.length() - 1));
				Map<String, String> map = new HashMap<String, String>();
				for (Role r : rolelist) {
					String[] opes = r.getStr("operatorids").split(",");
					for (String s : opes) {
						map.put(s, s);
					}
				}
				List<String> roles = new ArrayList<String>();
				for (String key : map.keySet()) {
					String ke = key.substring(key.indexOf("_") + 1, key.length());
					roles.add(ke);
				}
				for (String s : roles) {
					if (s.equals("493")) {// 判断该用户是否有发送公告的权限，暂时写死
						flag = true;
						break;
					}
				}
				if (flag) {
					ids += user.getInt("id") + "|";
				}
				user.put("flag", flag);
			}
			setAttr("ids", ids);
			setAttr("userlist", userlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJsp("/teacher/layer_sendannouncement.jsp");
	}

	/**
	 * 保存发布公告信息
	 */
	public void saveSendMessage() {
		try {
			Announcement a = getModel(Announcement.class);
			a.set("sendtime", new Date());
			a.save();
			String[] userids = getPara("userids").split("\\|");
			for (String id : userids) {
				Receiver r = new Receiver();
				r.set("recipientid", id).set("senderid", a.getPrimaryKeyValue()).save();
			}
			renderJson(0);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(1);
		}
	}

	/**
	 * 改变发送消息状态
	 */
	public void updateState() {
		try {
			Receiver r = Receiver.dao.findById(getPara("arid"));
			r.set("state", 1).update();
			renderJson(0);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(1);
		}
	}

	/**
	 * 改变所有的状态
	 */
	public void updateStates() {
		try {
			List<Receiver> r = Receiver.dao.findByUserid(getSysuserId());
			if (!r.isEmpty()) {
				for (Receiver re : r) {
					re.set("state", 1).update();
				}
			}
			renderJson(0);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(1);
		}
	}

	/**
	 * 删除已发送消息
	 */
	public void deleteTeacherSendMessage() {
		try {
			String id = getPara("id");
			Announcement a = Announcement.dao.findById(id);
			List<Receiver> listr = Receiver.dao.fingSendMessage(id);
			for (Receiver r : listr) {
				r.delete();
			}
			a.delete();
			renderJson(0);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(1);
		}
	}

	/**
	 * 获取预约的信息
	 */
	public void getReservationMessage() {
		try {
			Integer teacherid = getSysuserId();
			List<Reservation> listr = Reservation.dao.getMessageTeacherId(teacherid);
			renderJson(listr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取教师接收消息*
	 */
	public void getTeacherReceiveMessage() {
		JSONObject json = new JSONObject();
		try {
			List<Receiver> lista = Receiver.dao.findSendMessage(getSysuserId().toString());
			int newmessage = 0;
			if (!lista.isEmpty()) {
				for (Receiver r : lista) {
					if (r.getInt("state") == 0) {
						newmessage += 1;
					}
				}
			}
			json.put("lista", lista);
			json.put("newmessage", newmessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 获取课程信息
	 */
	public void getTeacherCourseMessage() {
		JSONObject json = new JSONObject();
		try {
			Teacher t = Teacher.dao.findById(getSysuserId());
			List<CoursePlan> tlist = CoursePlan.coursePlan.getDayCourse(getSysuserId().toString());
			json.put("tlist", tlist);
			json.put("t", t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 获取教师发送的消息
	 */
	public void getTeacherSendMessage() {
		try {
			List<Announcement> lista = Announcement.dao.findSendMessage(getSysuserId());
			if (!lista.isEmpty()) {
				for (Announcement a : lista) {
					List<Receiver> listr = Receiver.dao.getUserState(a.getInt("id"));
					a.put("users", listr);
				}
			}
			renderJson(lista);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取教师登陆后的基本信息*
	 */
	public void getTeacherCourHour() {
		try {
			JSONObject json = new JSONObject();
			String teacherId = getSysuserId().toString();
			String code = getPara("code");
			Date nowData = new Date();// 获取当前时间
			Map<String, Date> Mmap = ToolDateTime.getMonthDate(nowData);// 获取每个月的开始结束时间戳
			Map<String, Date> Wmap = ToolDateTime.getWeekDate(nowData);// 获取每个周的开始结束时间戳
			Date Mstart = null;
			Date Mend = null;
			if (code.equals("2")) {
				Mstart = Mmap.get("start");
				Mend = Mmap.get("end");
			} else if (code.equals("1")) {
				Mstart = Wmap.get("start");
				Mend = Wmap.get("end");
			} else if (code.equals("3")) {
				Mstart = ToolDateTime.getFirstDateOfSeason(nowData);
				Mend = ToolDateTime.getLastDateOfSeason(nowData);
			} else if (code.equals("4")) {
				Mstart = ToolDateTime.getCurrentYearFristTime();
				Mend = ToolDateTime.getCurrentYearEndTime();
			} else {
				Mstart = Mmap.get("start");
				Mend = Mmap.get("end");
			}
			CoursePlan Msign0 = CoursePlan.coursePlan.getCourseSign(0, teacherId, ToolDateTime.getSqlTimestamp(Mstart), ToolDateTime.getSqlTimestamp(Mend));
			CoursePlan Msign1 = CoursePlan.coursePlan.getCourseSign(1, teacherId, ToolDateTime.getSqlTimestamp(Mstart), ToolDateTime.getSqlTimestamp(Mend));
			CoursePlan MonthClassHour = CoursePlan.coursePlan.getCoursePlanHour(teacherId, ToolDateTime.getSqlTimestamp(Mstart),
					ToolDateTime.getSqlTimestamp(Mend));
			CoursePlan todayClassHour = CoursePlan.coursePlan.getDayCoursePlanHour(teacherId);
			json.put("sign0", Msign0);
			json.put("sign1", Msign1);
			json.put("month", MonthClassHour);
			json.put("today", todayClassHour);
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	}

	/**
	 * 已发消息分页*
	 */
	public void queryAllAnnouncement() {
		teacherService.queryAllAnnouncement(splitPage, getSysuserId());
		setAttr("showPages", splitPage.getPage());
		render("/teacher/announcement_list.jsp");
	}

	/**
	 * 发送详情*
	 */
	public void viewAnnouncement() {
		try {
			String id = getPara();
			Announcement a = Announcement.dao.findByIdForDetail(Integer.parseInt(id));
			setAttr("ann", a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJsp("/teacher/layer_viewAnnouncementMessage.jsp");
	}

	/**
	 * 已收消息分页*
	 */
	public void queryAllReceiver() {
		teacherService.queryAllReceiver(splitPage, getSysuserId());
		setAttr("showPages", splitPage.getPage());
		render("/teacher/receiver_list.jsp");
	}

	/**
	 * 消息详情*
	 */
	public void viewReceiver() {
		try {
			String id = getPara();
			Receiver r = Receiver.dao.findById(id);
			r.set("state", 1).update();
			List<Receiver> rlist = Receiver.dao.getUserState(r.getInt("senderid"));
			Announcement an = Announcement.dao.findByIdForDetail(r.getInt("senderid"));
			setAttr("rlist", rlist);
			setAttr("ann", an);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJsp("/teacher/layer_viewReceiverMessage.jsp");
	}

	/**
	 * 消息回复*
	 */
	public void replyReceiver() {
		try {
			String id = getPara();
			Receiver r = Receiver.dao.findById(id);
			setAttr("r", r);
			renderJsp("/teacher/layer_replyReceiver.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存回复*
	 */
	public void replyMessage() {
		try {
			String id = getPara("id");
			String reply = getPara("reply");
			Receiver r = Receiver.dao.findById(id);
			r.set("state", 1);
			r.set("reply", reply).update();
			renderJson(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
