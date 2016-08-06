package com.momathink.teaching.course.controller;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Const;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.Constants;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.account.model.AccountBanci;
import com.momathink.sys.account.service.AccountService;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.campus.model.Classroom;
import com.momathink.teaching.classtype.model.BanciCourse;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.course.service.CourseService;
import com.momathink.teaching.course.service.CourseplanService;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.student.service.StudentService;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.teacher.model.Teacher;
import com.momathink.teaching.teacher.model.Teachergrade;
import com.momathink.teaching.teacher.model.Teachergroup;

@Controller(controllerKey = "/course")
public class CourseController extends BaseController {
	private static final Logger logger = Logger.getLogger(CourseController.class);

	private CourseService courseService = new CourseService();
	private StudentService studentService = new StudentService();
	private AccountService accountService = new AccountService();
	private CourseplanService courseplanService = new CourseplanService();

	/**
	 * 列表形式显示课程所有信息
	 */
	public void showAllCourseMessage() {
		Map<String, String> queryParam = splitPage.getQueryParam();
		if (!StringUtils.isEmpty(getSysuserId().toString())) {
			SysUser user = SysUser.dao.findById(getSysuserId());
			if (Role.isTeacher(user.getStr("roleids"))) {
				queryParam.put("teacherId", user.getInt("Id").toString());
			} else {
				queryParam.put("teacherId", null);
				queryParam.put("campusid", AccountCampus.dao.getCampusIdsByAccountId(getSysuserId()));
			}
		}
		courseplanService.queryUserMessage(splitPage);
		setAttr("showPages", splitPage.getPage());
		renderJsp("/course/courseplan_list.jsp");
	}

	/**
	 * 课程列表
	 */
	public void index() {
		courseService.list(splitPage);
		List<Subject> subject = Subject.dao.getSubject();
		setAttr("subject", subject);
		setAttr("showPages", splitPage.getPage());
		renderJsp("/course/course_list.jsp");
	}

	/**
	 * 添加课程
	 */
	public void add() {
		String subjcetid = getPara();
		List<Subject> subjectList = new ArrayList<Subject>();
		if (StringUtils.isEmpty(subjcetid)) {
			subjectList = Subject.dao.getSubject();
		} else {
			Subject subject = Subject.dao.findById(Integer.parseInt(subjcetid));
			subjectList.add(subject);
		}
		setAttr("subject", subjectList);
		setAttr("operatorType", "add");
		renderJsp("/course/layer_course_form.jsp");
	}

	/**
	 * 保存课程
	 */
	public void save() {
		Course course = getModel(Course.class);
		courseService.save(course);
		renderJson("1");
	}

	/**
	 * 编辑课程
	 */
	public void edit() {
		String courseid = getPara();// 课程id
		Course course = Course.dao.findById(Integer.parseInt(courseid));
		List<Subject> subject = Subject.dao.getSubject();
		setAttr("subject", subject);
		setAttr("course", course);
		setAttr("operatorType", "update");
		renderJsp("/course/layer_course_form.jsp");
	}

	/**
	 * 更新课程
	 */
	public void update() {
		Course course = getModel(Course.class);
		courseService.update(course);
		renderJson("1");
	}

	/**
	 * 跟进科目ID返回课程JSON格式
	 */
	public void getCoursesBySubjectIdForJSON() {
		JSONObject json = new JSONObject();
		String code = "1";
		try {
			List<Course> courses = Course.dao.findBySubjectId(getParaToInt("SUBJECT_ID"));
			boolean sameprice = true;
			if (courses.size() > 0) {
				int price = courses.get(0).getInt("UNIT_PRICE");
				for (Course course : courses) {
					int _price = course.getInt("UNIT_PRICE") == null ? 0 : course.getInt("UNIT_PRICE");
					if (price != _price) {
						sameprice = false;
						break;
					}
				}
			}
			json.put("courses", courses);
			json.put("sameprice", sameprice);
		} catch (Exception ex) {
			logger.error(ex.toString());
			code = "0";
			json.put("msg", "系统好像出问题了:（请联系管理员！");
		}
		json.put("code", code);
		renderJson(json);
	}

	/**
	 * 根据姓名或班次编号查询用户选择的科目
	 */
	public void getSubjectForUser() {
		String teach_type = getPara("teach_type");// 1为一对一，2为小班
		String studentId = getPara("studentId");
		if ("1".equals(teach_type)) {
			renderJson("subject", Subject.dao.getSubject());
		} else {
			if (!ToolString.isNull(studentId)) {
				Record record = Db.findFirst("SELECT id FROM class_order WHERE accountid=? ", Integer.parseInt(studentId));
				if (record != null) {
					Integer class_id = record.getInt("id");
					String sql = "SELECT\n" + "	banci_course.Id as bancicourseid,\n" + "	banci_course.subject_id as ID,\n" + "	banci_course.course_id,\n"
							+ "	banci_course.lesson_num,\n" + "	banci_course.type_id,\n" + "	banci_course.banci_id,\n" + "	`subject`.SUBJECT_NAME\n" + "FROM\n"
							+ "	banci_course\n" + "LEFT JOIN `subject` ON banci_course.subject_id = `subject`.Id\n" + "WHERE\n" + "	banci_id = ?\n"
							+ "GROUP BY\n" + "	subject_id";
					List<Record> record2 = Db.find(sql, class_id);
					renderJson("subject", record2);
				} else {
					renderJson("subject", null);
				}
			} else {
				renderJson("subject", null);
			}
		}
	}

	/**
	 * 根据班次id取出后班次中的所有课程和课时
	 */
	public void findClassCourse() {
		String banci_id = getPara("banci_id");
		String sql = "SELECT\n" + "class_order.classNum,\n" + "banci_course.course_id,\n" + "banci_course.subject_id,\n" + "banci_course.lesson_num,\n"
				+ "class_order.id\n" + "FROM\n" + "class_order\n" + "INNER JOIN banci_course ON class_order.classtype_id = banci_course.type_id\n" + "WHERE\n"
				+ "class_order.id = ? ";
		List<Record> record = Db.find(sql, banci_id);
		renderJson("list", record);
	}

	/**
	 * 根据科目id查找可选择班次
	 */
	public void finClassOrder() {
		String stu_id = getPara("stu_id"); // 用户的account id
		Integer banciId = 0;
		if (!ToolString.isNull(stu_id) && !"0".equals(stu_id)) {
			Record record3 = Db.findFirst("SELECT * FROM account_banci where account_id=? ", stu_id);
			if (record3 != null) {
				banciId = record3.getInt("banci_id");
			}
		}
		String subject_id = getPara("sub_id");
		String[] s_id = subject_id.split(",");
		String sql = "SELECT\n" + "class_order.classNum,\n" + "class_type.`name`,\n" + "class_type.teach_type,\n" + "class_order.stuNum,\n"
				+ "banci_course.lesson_num,\n" + " class_order.id FROM\n" + "class_order\n"
				+ "LEFT JOIN class_type ON class_order.classtype_id = class_type.id\n" + "LEFT JOIN banci_course ON class_type.id = banci_course.type_id\n"
				+ "WHERE (class_order.endTime >= CURDATE() OR class_order.endTime IS NULL OR class_order.id=" + banciId + ") AND \n"
				+ "(banci_course.subject_id = " + s_id[0];
		for (int i = 1; i < s_id.length; i++) {
			sql += " or banci_course.subject_id = " + s_id[i];
		}
		sql += " ) group by class_order.classNum ORDER BY class_order.classNum ";
		List<Record> record = Db.find(sql);
		List<Object> list = new ArrayList<Object>();
		if (record.size() > 0) {
			if (!ToolString.isNull(stu_id) && !"0".equals(stu_id)) {
				List<Record> studentcp = Db.find("SELECT * FROM courseplan WHERE student_id=? AND class_id=0 ", Integer.parseInt(stu_id));// 查询该用户的一对一排课
				if (studentcp.size() > 0) {
					for (Record stucp : studentcp) {
						String course_time = ToolDateTime.getStringTimestamp(stucp.getTimestamp("COURSE_TIME"));
						Integer timerank_id = stucp.getInt("TIMERANK_ID");
						//
						TimeRank timeRank = TimeRank.dao.findById(timerank_id);
						String timeNames[] = timeRank.getStr("RANK_NAME").split("-");
						int beginTime = Integer.parseInt(timeNames[0].replace(":", ""));
						int endTime = Integer.parseInt(timeNames[1].replace(":", ""));
						circle: for (int i = 0; i < record.size(); i++) {// 遍历所有可选班次的排课
							int class_id = record.get(i).getInt("id");
							List<Record> record2 = Db
									.find("SELECT DATE_FORMAT(COURSE_TIME,'%Y-%m-%d') AS COURSE_TIME,TIMERANK_ID,class_id,STUDENT_ID FROM courseplan WHERE class_id=? ",
											class_id);
							if (record2.size() > 0) { // 该班次已有排课
								for (Record rec2 : record2) {
									if (course_time.equals(rec2.get("COURSE_TIME"))) {
										//
										Integer rec2_timerank_id = rec2.getInt("TIMERANK_ID");
										TimeRank rectimeRank = TimeRank.dao.findById(rec2_timerank_id);
										String rectimeNames[] = rectimeRank.getStr("RANK_NAME").split("-");
										int recbeginTime = Integer.parseInt(rectimeNames[0].replace(":", ""));
										int recendTime = Integer.parseInt(rectimeNames[1].replace(":", ""));
										if ((beginTime <= recendTime && endTime >= recendTime) || (endTime >= recbeginTime && beginTime <= recbeginTime)) {
											record.remove(i);
											break circle;
										}

									}
									/*
									 * if
									 * (course_time.equals(rec2.get("COURSE_TIME"
									 * )) && timerank_id ==
									 * rec2.getInt("TIMERANK_ID")) {
									 * record.remove(i); break circle; }
									 */
								}
							}
						}
					}
					for (Record rec2 : record) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer class_id = rec2.getInt("id");
						String class_name = rec2.getStr("name");
						String class_num = rec2.getStr("classNum");
						map.put("ID", class_id);
						map.put("NAME", class_name);
						map.put("CLASSNUM", class_num);
						list.add(map);
					}
					renderJson("record", list);
				} else {
					// 一对一排课为空则返回
					renderJson("record", record);
				}
			} else {
				renderJson("record", record);
			}
		} else {
			renderJson("record", null);
		}
	}

	public void addCoursePlan() {
		try {
			String banjiType = getPara("banjiType");
			setAttr("campus", Db.find("select * from campus"));
			if (getPara("studentId") != null) {
				if ("1".equals(banjiType)) {
					setAttr("banjiType", 1);
					String sql = "SELECT MAX(DATE_FORMAT(course_time,'%Y-%m-%d')) AS last_course from courseplan WHERE STUDENT_ID=? ";
					setAttr("studentName", Db.findFirst("select * from account where id=? ", getPara("studentId")).get("REAL_NAME"));
					setAttr("studentId", getPara("studentId"));
					setAttr("lastCourseTime", Db.findFirst(sql, getPara("studentId")).get("last_course"));
				}
			}
			if (getPara("banciId") != null) {
				if ("2".equals(banjiType)) {
					setAttr("banjiType", 2);
					String sql = "SELECT MAX(DATE_FORMAT(course_time,'%Y-%m-%d')) AS last_course from courseplan WHERE class_id=? ";
					setAttr("studentName", Db.findFirst("SELECT * FROM class_order WHERE Id=?  ", getPara("banciId")).get("classNum"));
					setAttr("banci_id", getPara("banciId"));
					setAttr("lastCourseTime", Db.findFirst(sql, getPara("banciId")).get("last_course"));
				}
			}
			renderJsp("/course/addCoursePlan.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * /course/addCoursePlanByCourse 按课程排课
	 */
	public void addCoursePlanByCourse() {
		logger.info("按课程排课开始");
		try {
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("teacherList", Teacher.dao.getTeachers());
			setAttr("date1", date1);
			setAttr("date2", date2);
			setAttr("campus", Db.find("select * from campus where state !=1 "));
			renderJsp("/course/addplanforcourse.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}

	}

	/**
	 * 正常排课
	 */
	public void addCourseWeekPlan() {
		try {
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			setAttr("campus", Db.find("select * from campus where state !=1 "));
			if (getPara("studentId") != null) {
				Student student = Student.dao.findById(getParaToInt("studentId"));
				if (student.getInt("state") == 2) {
					setAttr("banjiType", 2);
				} else {
					setAttr("banjiType", 1);
				}
				setAttr("studentName", student.getStr("real_name"));
				setAttr("studentId", student.getPrimaryKeyValue());
			}
			setAttr("teacherList", Teacher.dao.getTeachers());
			renderJsp("/course/addCourseWeekPlan.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * /course/addDayCoursePlan 添加排课--选择教室弹窗
	 */
	public void addDayCoursePlan() {
		renderJsp("/course/addDayCoursePlan.jsp");
	}

	/**
	 * 根据班型id查找班型的信息回显到页面
	 */
	public void editClassType() {
		String type_id = getPara("type_id");// 班型id
		Integer type = getParaToInt("type");// 方式，2为修改班型
		String sql = "SELECT\n" + "	class_type.id,\n" + "	class_type.`name`,\n" + "	class_type.teach_type,\n" + "	class_type.lesson_count,\n"
				+ "	banci_course.subject_id,\n" + "	banci_course.course_id\n" + "FROM\n" + "	class_type\n"
				+ "LEFT JOIN banci_course ON class_type.id = banci_course.type_id\n" + "WHERE\n" + "	class_type.id = ?\n" + "AND banci_course.banci_id = 0";
		List<Record> record = Db.find(sql, type_id);
		if (!ToolString.isNull(type_id)) {
			setAttr("type", type);
			setAttr("name", record.get(0).get("name"));
			setAttr("id", record.get(0).get("id"));
			setAttr("lesson_count", record.get(0).get("lesson_count"));
			setAttr("subject_id", record.get(0).get("subject_id"));
			List<Integer> list = new ArrayList<Integer>();
			for (Record rec : record) {
				list.add(rec.getInt("course_id"));
			}
			setAttr("record", list);
		}
		List<Subject> subject = Subject.dao.getSubject();
		setAttr("subject", subject);
		renderJsp("/course/addClassType.jsp");
	}

	public void editClassTypeForJson() {
		String type_id = getPara("id");// 班型id
		String sql = "SELECT\n" + "	class_type.id,\n" + "	class_type.`name`,\n" + "	class_type.teach_type,\n" + "	class_type.lesson_count,\n"
				+ "	banci_course.subject_id,\n" + "	banci_course.course_id\n" + "FROM\n" + "	class_type\n"
				+ "LEFT JOIN banci_course ON class_type.id = banci_course.type_id\n" + "WHERE\n" + "	class_type.id = ?\n" + "AND banci_course.banci_id = 0";
		List<Record> record = Db.find(sql, type_id);
		renderJson("courseJson", record);
	}

	public void editClassTypeForJson2() {
		String type_id = getPara("type_id");// 班型id
		String sql = "SELECT\n" + "	class_type.id,\n" + "	class_type.`name`,\n" + "	class_type.teach_type,\n" + "	class_type.lesson_count,\n"
				+ "	banci_course.subject_id,\n" + "	banci_course.course_id\n" + "FROM\n" + "	class_type\n"
				+ "LEFT JOIN banci_course ON class_type.id = banci_course.type_id\n" + "WHERE\n" + "	class_type.id = ?\n"
				+ "AND banci_course.banci_id = 0 GROUP BY banci_course.subject_id ";
		List<Record> record = Db.find(sql, type_id);
		renderJson("record", record);
	}

	/**
	 * 课程管理
	 */
	public void findCourseManager() {
		try {
			String courseName = getPara("courseName");
			String kemu = getPara("kemu");
			List<Subject> subject = Subject.dao.getSubject();
			setAttr("subject", subject);
			// 分页sql语句
			String page = getPara("page") != null ? getPara("page") : "1";
			// 要查询第几页
			int pagecount = (Integer.parseInt(page) - 1) * 20;
			// 课程分页记录
			String sql = "SELECT cou.* FROM course cou WHERE 1=1 ";
			// 一共有多少条记录
			String sqlcount = "select count(*) as counts from course cou where 1=1 ";
			if (!ToolString.isNull(courseName)) {
				courseName = courseName.trim();
				sql += " and (cou.course_Name like '%" + courseName + "%')";
				sqlcount += " and (cou.course_Name like '%" + courseName + "%')";
			}
			if (!ToolString.isNull(kemu) && !"0".equals(kemu)) {
				sql += " AND cou.subject_id = " + "'" + kemu + "'";
				sqlcount += " AND cou.subject_id = " + "'" + kemu + "'";
			}
			sql += " order by CREATE_TIME desc LIMIT " + pagecount + ",20;";

			List<Record> counts = Db.find(sqlcount);
			String count = counts.get(0).get("counts").toString();
			// 一共有多少页
			String pages = "";
			if ((Integer.parseInt(count) / 20) * 20 != Integer.parseInt(count)) {
				pages = (Integer.parseInt(count) / 20 + 1) + "";
			} else {
				pages = (Integer.parseInt(count) / 20) + "";
			}
			setAttr("pages", pages); // 总页数
			setAttr("count", count); // 总记录数
			setAttr("page", page); // 查询的第几页
			setAttr("coursePage", Course.dao.find(sql));
			setAttr("courseName", courseName);
			setAttr("kemu", kemu);
			renderJsp("/course/findCourseManager.jsp");
		} catch (Exception e) {
			logger.error("CourseController.findCourseManager", e);
			renderJsp("/course/findCourseManager.jsp");
		}
	}

	/**
	 * 验证课程名是否已存在
	 */
	public void checkCourseName() {
		try {
			String courseName = getPara("courseName");
			Course course = Course.dao.findFirst("select * from course where state=0 and Course_Name=?", courseName);
			boolean flag = false;
			if (course != null) {
				flag = true;
			}
			renderJson(flag);
		} catch (Exception e) {
			logger.error("CourseController.checkCourseName", e);
			renderJson(false);
		}
	}

	/**
	 * 检查是否存在
	 */
	public void checkExist() {
		String field = getPara("checkField");
		String value = getPara("checkValue");
		String courseId = getPara("courseid");
		String subjectId = getPara("subjectId");
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			Long count = Course.dao.queryCourseCount(field, value, courseId, subjectId);
			renderJson("result", count);
		} else {
			renderJson("result", null);
		}
	}

	/**
	 * 添加班型跳转页面
	 */
	public void addCourseManager() {
		try {
			List<Subject> subject = Subject.dao.getSubject();
			setAttr("subject", subject);
			renderJsp("/course/addCourseManager.jsp");
		} catch (Exception e) {
			logger.error("CourseController.addCourseManager", e);
			renderJsp("/course/addCourseManager.jsp");
		}
	}

	/**
	 * 添加或更新课程信息
	 */
	public void doAddCourseManager() {
		String id = getPara("id"); // 课程id
		String courseName = getPara("courseName"); // 课程名称
		String courseSubject = getPara("courseSubject"); // 科目ID
		String courseRemark = getPara("courseRemark"); // 备注
		String courseIntro = getPara("courseIntro"); // 简介

		try {
			if (!ToolString.isNull(id))// id不为空更新，为空添加
			{
				Course.dao.findById(id).set("course_Name", courseName).set("Subject_id", courseSubject).set("intro", courseIntro).set("remark", courseRemark)
						.update();
			} else {
				new Course().set("course_Name", courseName).set("Subject_id", courseSubject).set("intro", courseIntro).set("remark", courseRemark).save();
			}
			forwardAction("/course/findCourseManager");
		} catch (Exception e) {
			logger.error("CourseCpntroller.doAddCourseManager", e);
			forwardAction("/course/findCourseManager");
		}
	}

	/**
	 * 根据班型id查找课程的信息回显到页面
	 */
	public void editCourseManager() {
		try {
			String course_id = getPara("course_id");// 课程id
			Record record = Db.findById("course", course_id);
			if (!ToolString.isNull(course_id)) {
				setAttr("id", course_id);
				setAttr("courseName", record.get("COURSE_NAME"));
				setAttr("remark", record.get("REMARK"));
				setAttr("intro", record.get("INTRO"));
				setAttr("subjectId", record.get("subject_id"));
			}
			List<Subject> subject = Subject.dao.getSubject();
			setAttr("subject", subject);
			renderJsp("/course/addCourseManager.jsp");
		} catch (Exception e) {
			logger.error("CourseController.editCourseManager", e);
			renderJsp("/course/addCourseManager.jsp");
		}
	}

	/**
	 * 删除课程
	 */
	public void delCourse() {
		try {
			if (getParaToInt("courseId") != null) {
				int id = getParaToInt("courseId");
				int state = getParaToInt("state");
				// 查询该课程是否有未开始或未结束的排课，有则不让删除。sql语义：查找大于当前日期的该课程的排课和等与当前日期该课程该时段未开始或未结束的排课
				String sql = "select * from courseplan  c"
						+ " LEFT JOIN course co on co.ID=c.COURSE_ID"
						+ " LEFT JOIN time_rank t ON c.TIMERANK_ID=t.Id"
						+ " where  (c.COURSE_TIME>NOW() OR (DATE_FORMAT(c.COURSE_TIME,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') and SUBSTRING(t.RANK_NAME,-5)>DATE_FORMAT(NOW(),'%H:%i')))"
						+ " and c.state=0 and co.id=?" + " ORDER BY COURSE_TIME ";
				if (Db.find(sql, id).size() > 0 && state == 1)// 如果有排课未开始或未结束
				{
					renderJson("result", "该课程有未开始或未结束的排课，不能停用");
				} else {
					Db.update("update course set state=? where id=?", state, id);// 改变状态，删除成功，该时段只会在按月浏览的历史排课中显示
					renderJson("result", "true");
				}
			}
		} catch (Exception e) {
			logger.error("CourseController.delCourse", e);
		}
	}

	/**
	 * 删除班型
	 */
	public void delClassType() {
		String type_id = getPara("type_id");
		String sql = "delete from class_type where id = ? ";
		try {
			Db.update(sql, type_id);
			forwardAction("/course/getClassType");
		} catch (Exception e) {
			renderText("删除失败！");
		}
	}

	/**
	 * 级联查询根据科目查询班型
	 */
	public void findClassTypeBySubject() {
		Integer sub_id = getParaToInt("sub_id");
		String sql = "SELECT\n" + "	class_type.`name`,\n" + "	banci_course.subject_id,\n" + "	class_type.id\n" + "FROM\n" + "	class_type\n"
				+ "LEFT JOIN banci_course ON class_type.id = banci_course.type_id\n" + "WHERE\n" + "	banci_course.subject_id = ?\n" + "GROUP BY\n"
				+ "	class_type.`name`\n" + "ORDER BY\n" + "	class_type.id ASC";
		List<Record> list = Db.find(sql, sub_id);
		renderJson("classType", list);
	}

	/**
	 * 查询已经有的班次进行排课
	 */
	public void getClassOrder() {
		Integer course_id = getParaToInt("course_id");
		List<AccountBanci> classOrder = AccountBanci.findCourseOrder(course_id);
		renderJson("classOrder", classOrder);
	}

	/**
	 * 获取该班次的选择人数等信息
	 */
	public void getClassOrderById() {
		Integer class_id = getParaToInt("classOrder_id");
		ClassOrder getOrder = ClassOrder.getClassOrderById(class_id);
		renderJson("getOrder", getOrder);
	}

	public void getTimeRankByStudentId() {
		try {
			String studentId = getPara("studentId");
			String courseTime = getPara("courseTime");
			List<TimeRank> timeList = TimeRank.dao.getTimeRank();
			List<Record> timeUseList = TimeRank.dao.queryUsrTimeRank(studentId, courseTime);
			float surplusClasshour = studentService.getSurplusClasshour(studentId);
			for (TimeRank timeRank : timeList) {
				String timeNames[] = timeRank.getStr("RANK_NAME").split("-");
				int beginTime = Integer.parseInt(timeNames[0].replace(":", ""));
				int endTime = Integer.parseInt(timeNames[1].replace(":", ""));
				for (Record rec : timeUseList) {
					String rankName = rec.getStr("rankName");
					String rankTimes[] = rankName.split("-");
					int beginRankTime = Integer.parseInt(rankTimes[0].replace(":", ""));
					int endRankTime = Integer.parseInt(rankTimes[1].replace(":", ""));
					if ((beginTime >= beginRankTime && beginTime < endRankTime) || (endTime > beginRankTime && endTime <= endRankTime)) {
						timeRank.put("planId", rec.getInt("planId"));
						timeRank.put("planType", rec.getInt("planType"));
						break;
					}
				}
				float ks = timeRank.getBigDecimal("class_hour").floatValue();
				if (ks > surplusClasshour) {
					timeRank.put("planId", "9999999");
					timeRank.put("planType", "0");
				}
			}
			renderJson("timeRanks", timeList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void getTimeRankByClassId() {
		try {
			String classId = getPara("classId");
			String courseTime = getPara("courseTime");
			List<TimeRank> timeList = TimeRank.dao.getTimeRank();
			List<Record> timeUseList = TimeRank.dao.queryUsrTimeRankByClassId(classId, courseTime);
			for (TimeRank timeRank : timeList) {
				String timeNames[] = timeRank.getStr("RANK_NAME").split("-");
				int beginTime = Integer.parseInt(timeNames[0].replace(":", ""));
				int endTime = Integer.parseInt(timeNames[1].replace(":", ""));
				for (Record rec : timeUseList) {
					String rankName = rec.getStr("rankName");
					String rankTimes[] = rankName.split("-");
					int beginRankTime = Integer.parseInt(rankTimes[0].replace(":", ""));
					int endRankTime = Integer.parseInt(rankTimes[1].replace(":", ""));
					if ((beginTime >= beginRankTime && beginTime < endRankTime) || (endTime > beginRankTime && endTime <= endRankTime)) {
						timeRank.put("planId", rec.getInt("planId"));
						timeRank.put("planType", rec.getInt("planType"));
						break;
					}
				}
			}
			renderJson("timeRanks", timeList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void getClassRoomByDateAndDateRank() {
		try {
			int campusId = getParaToInt("campus_id");
			String courseTime = getPara("courseTime");
			String timeRank = getPara("timeRank");
			Integer timerank = Integer.parseInt(timeRank);

			// 根据courseTime取出当天的所有排课记录；
			List<Record> record = courseService.getCoursePlanDay(courseTime, campusId);

			// 不能用的时段；
			TimeRank tRank = TimeRank.dao.findById(timerank);
			String timeNames[] = tRank.getStr("RANK_NAME").split("-");
			int beginTime = Integer.parseInt(timeNames[0].replace(":", ""));
			int endTime = Integer.parseInt(timeNames[1].replace(":", ""));
			List<TimeRank> lists = TimeRank.dao.find("select * from time_rank ");
			List<Integer> trIds = new ArrayList<Integer>();// 不能选的时段id
			for (int i = 0; i < lists.size(); i++) {
				String timeNa[] = lists.get(i).getStr("RANK_NAME").split("-");
				int bt = Integer.parseInt(timeNa[0].replace(":", ""));
				int et = Integer.parseInt(timeNa[1].replace(":", ""));
				if ((beginTime >= bt && beginTime <= et) || (endTime >= bt && endTime <= et) || (beginTime <= bt && endTime >= et)) {
					trIds.add(lists.get(i).getInt("ID"));
				}
			}
			List<Classroom> cr = Classroom.dao.getClassRoomByCamp(campusId);// 该校区所有教室
			List<Integer> rmIds = new ArrayList<Integer>();// 不能安排的教室；
			List<Record> list = new ArrayList<Record>();// 和该时段会有冲突的已排课程(这里面有roomId)
			for (int i = 0; i < record.size(); i++) {
				circle: for (int j = 0; j < trIds.size(); j++) {
					if (record.get(i).getInt("timeRankId") == trIds.get(j)) {
						list.add(record.get(i));
						rmIds.add(record.get(i).getInt("roomId"));
						break circle;
					}

				}
			}

			for (int i = 0; i < cr.size(); i++) {
				if (rmIds.size() > 0) {
					for (int j = 0; j < rmIds.size(); j++) {
						if (cr.get(i).getInt("ID") == rmIds.get(j)) {
							cr.get(i).put("USED", 1);
						} else {
							cr.get(i).put("USED", 0);
						}
					}
				} else {
					cr.get(i).put("USED", 0);
				}
			}

			renderJson("classRooms", cr);

		} catch (Exception ex) {
			logger.error(ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * 根据姓名查询已排课程的最大日期
	 */
	public void getMaxTime1() {
		String real_name = getPara("real_name");
		// Integer course_id = getParaToInt("course_id");
		Integer stu_id = Db.findFirst("select * from account where real_name=? ", real_name).getInt("id");
		Record record = Db.findFirst("SELECT DATE_FORMAT(MAX(COURSE_TIME),'%Y-%m-%d') as maxTime FROM courseplan WHERE STUDENT_ID=? ", stu_id);
		renderJson("maxTime", record.get("maxTime"));
	}

	/**
	 * 根据班次和课程选择已排课程的最大日期
	 */
	public void getMaxTime2() {
		String class_num = getPara("class_num"); // 编号
		// Integer course_id = getParaToInt("course_id");
		Integer class_id = Db.findFirst("SELECT * FROM class_order WHERE classNum=? ", class_num).getInt("id");
		Record record = Db.findFirst("SELECT DATE_FORMAT(MAX(COURSE_TIME),'%Y-%m-%d') AS maxTime FROM courseplan WHERE class_id=? ", class_id);
		renderJson("maxTime", record.get("maxTime"));
	}

	@SuppressWarnings("static-access")
	public synchronized void addCoursePlans() {
		JSONObject json = new JSONObject();
		String code = "0";
		String msg = "保存成功！";
		String studentId = getPara("stuId");
		String teacherId = getPara("teacherId");
		String roomId = getPara("roomId");
		String timeId = getPara("rankId");
		String campusId = getPara("campusId");
		String subjectid = getPara("subjectid");
		String planType = getPara("plantype");
		String isovertime = getPara("isovertime");
		String coursetime = getPara("dayTime");
		try {
			String type = getPara("type");
			Date nowdate = ToolDateTime.getDate();
			TimeRank timeRank = TimeRank.dao.findById(Integer.parseInt(timeId));
			String fh = timeRank.getStr("RANK_NAME").split("-")[0].split(":")[0];
			String fm = timeRank.getStr("RANK_NAME").split("-")[0].split(":")[1];
			String hms = coursetime.trim().substring(0, 10) + " " + (fh.trim().length() == 1 ? ("0" + fh.trim()) : fh.trim()) + ":"
					+ (fm.trim().length() == 1 ? ("0" + fm.trim()) : fm.trim()) + ":00";
			String thms = ToolDateTime.dateToDateString(new Date(), ToolDateTime.DATATIMEF_STR);
			long between = ToolDateTime.compareDateStr(thms, hms);
			Integer recharge = between < 0 ? 1 : 0;// 1是补排课程,0是正常排课
			double classhour = timeRank.getBigDecimal("class_hour").doubleValue();
			if (type.equals("1")) { // 一对一排课
				CoursePlan cp = CoursePlan.coursePlan.getStuCoursePlan(studentId, timeId, getPara("dayTime"));
				if (cp == null) {
					Account account = Account.dao.findById(Integer.parseInt(studentId));
					json.put("havecourse", "0");
					double zks = CourseOrder.dao.getVIPzks(account.getPrimaryKeyValue());
					double ypks = CoursePlan.coursePlan.getUseClasshour(studentId, null);// 全部已用课时
					double syks = ToolArith.sub(zks, ypks);// 剩余课时
					CoursePlan saveCoursePlan = getModel(CoursePlan.class);
					String courseTime = getPara("dayTime");
					DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
					saveCoursePlan.set("course_time", dd.parse(courseTime));
					saveCoursePlan.set("create_time", nowdate);
					saveCoursePlan.set("update_time", nowdate);
					saveCoursePlan.set("class_id", 0);
					saveCoursePlan.set("isovertime", isovertime);
					saveCoursePlan.set("student_id", studentId);
					saveCoursePlan.set("room_id", roomId);
					saveCoursePlan.set("timerank_id", timeId);
					saveCoursePlan.set("campus_id", campusId);
					saveCoursePlan.set("SUBJECT_ID", subjectid);
					saveCoursePlan.set("adduserid", getSysuserId());
					saveCoursePlan.set("rechargecourse", recharge);
					saveCoursePlan.set("plan_type", getPara("plantype"));
					saveCoursePlan.set("netCourse", getPara("netCourse"));
					List<CourseOrder> colist = CourseOrder.dao.getOrderByStudentidAndSubjectid(studentId, subjectid);
					TimeRank tr = TimeRank.dao.findById(timeId);
					for (CourseOrder co : colist) {
						double d = CoursePlan.coursePlan.getKeShiByCourseOrderId(co.getInt("id"));
						if (d + tr.getBigDecimal("class_hour").doubleValue() <= co.getDouble("classhour")) {
							saveCoursePlan.set("courseorderid", co.getInt("id"));
							break;
						}
					}
					if (getPara("remark") == null || getPara("remark").equals("")) {
						saveCoursePlan.set("remark", "暂无");
					} else {
						saveCoursePlan.set("remark", ToolString.replaceBlank(getPara("remark")));
					}
					if (getPara("plantype").equals("1")) {
						saveCoursePlan.set("course_id", "-" + getPara("courseId"));
						code = "1";
						saveCoursePlan.save();
						Teacher t = Teacher.dao.findById(teacherId);
						if (t != null) {
							t.set("kcuserid", 0).update();// 使用kcuserid字段表示教师课表变动
						}
					} else {
						saveCoursePlan.set("teacher_id", teacherId.equals("") ? null : teacherId);
						saveCoursePlan.set("course_id", getPara("courseId"));
						if (syks > 0 && getPara("plantype").equals("0")) {
							if (timeRank.getBigDecimal("class_hour").doubleValue() > syks) {
								msg = account.getStr("real_name") + "剩余" + syks + "课时,该时段课时为" + timeRank.getBigDecimal("class_hour") + "课时";
							} else {
								saveCoursePlan.save();
								accountService.consumeCourse(saveCoursePlan.getPrimaryKeyValue(), Integer.parseInt(studentId), getSysuserId(),0);
								Teacher t = Teacher.dao.findById(teacherId);
								if (t != null) {
									t.set("kcuserid", 0).update();// 使用kcuserid字段表示教师课表变动
								}
								if (recharge == 0) {
									// 查看该学生是否第一次排课
									Integer num = CoursePlan.coursePlan.getUseCourse(getParaToInt("stuId"), getParaToInt("teacherId"));
									if (num == 1) {
										// 向老师发送学生排课信息邮件
										MessageService
												.sendMessageToTeacher(String.valueOf(saveCoursePlan.getPrimaryKeyValue()), MesContantsFinal.dd_email_tjpk);
										MessageService
												.sendMessageToTeacher(String.valueOf(saveCoursePlan.getPrimaryKeyValue()), MesContantsFinal.ls_email_tjpk);
									}

									// 发送短信
									if (ToolDateTime.isToday(courseTime)) {
										MessageService.sendMessageToStudent(MesContantsFinal.xs_sms_today_tjpk, saveCoursePlan.getPrimaryKeyValue().toString());
									}
								}
								code = "1";
								msg = "success";
								json.put("plan", CoursePlan.coursePlan.getCoursePlanCurrentSaved(saveCoursePlan.getPrimaryKeyValue().toString()));

							}
						} else {
							code = "0";
							msg = account.getStr("real_name") + "课时不足，请购买课时。";
						}
					}
				} else {
					json.put("havecourse", "1");
				}
			} else {// 小班排课
				String courseTime = getPara("dayTime");
				DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
				Integer classOrderId = getParaToInt("banci_id");
				ClassOrder ban = ClassOrder.dao.findById(classOrderId);
				if (ban == null) {
					code = "0";
					msg = "班课不存在";
				} else {
					double lessonNum = ban.getInt("lessonNum");
					int chargeType = ban.getInt("chargeType");
					double ypks = CoursePlan.coursePlan.getClassYpkcClasshour(classOrderId);
					if (classhour > ToolArith.sub(lessonNum, ypks) && planType == "0" && chargeType == 1) {
						code = "0";
						msg = "该班课课时不足";
					} else {
						List<CoursePlan> cp = CoursePlan.coursePlan.getClassCoursePlan(classOrderId, timeId, courseTime);
						if (cp.size() == 0) {
							json.put("havecourse", "0");
							Integer xnAccountId = ban.getInt("accountid");// 虚拟班课账户ID
							CoursePlan saveCoursePlan = getModel(CoursePlan.class);
							saveCoursePlan.set("course_time", dd.parse(courseTime));
							saveCoursePlan.set("create_time", nowdate);
							saveCoursePlan.set("update_time", nowdate);
							saveCoursePlan.set("isovertime", isovertime);
							saveCoursePlan.set("class_id", classOrderId);
							saveCoursePlan.set("student_id", xnAccountId);
							saveCoursePlan.set("SUBJECT_ID", subjectid);
							saveCoursePlan.set("room_id", getPara("roomId"));
							saveCoursePlan.set("timerank_id", getPara("rankId"));
							saveCoursePlan.set("campus_id", getPara("campusId"));
							saveCoursePlan.set("plan_type", getPara("plantype"));
							saveCoursePlan.set("netCourse", getPara("netCourse"));
							saveCoursePlan.set("rechargecourse", recharge);
							saveCoursePlan.set("adduserid", getSysuserId());
							if (getPara("plantype").equals("1")) {// 模考
								saveCoursePlan.set("course_id", "-" + getPara("courseId"));
							} else {
								saveCoursePlan.set("teacher_id", getPara("teacherId"));
								saveCoursePlan.set("course_id", getPara("courseId"));
							}
							if (getPara("remark") == null || getPara("remark").equals("")) {
								saveCoursePlan.set("remark", "暂无");
							} else {
								saveCoursePlan.set("remark", ToolString.replaceBlank(getPara("remark")));
							}
							saveCoursePlan.save();
							ClassOrder.dao.updateTeachTime(classOrderId);// 更新开班时间和结束时间
							code = "1";
							msg = "success";
							json.put("plan", CoursePlan.coursePlan.getCoursePlanCurrentSaved(saveCoursePlan.getPrimaryKeyValue().toString()));
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
		renderJson(json);
	}

	public synchronized void doAddCoursePlan() {
		try {
			Integer banjiType = getParaToInt("banjiType");
			if (banjiType == 1) { // 一对一排课
				CoursePlan saveCoursePlan = getModel(CoursePlan.class);
				String courseTime = getPara("courseTime");
				DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
				saveCoursePlan.set("course_time", dd.parse(courseTime));
				saveCoursePlan.set("create_time", new Date());
				saveCoursePlan.set("update_time", new Date());
				saveCoursePlan.set("class_id", 0);
				if (saveCoursePlan.get("remark") == null || saveCoursePlan.get("remark").equals("")) {
					saveCoursePlan.set("remark", "暂无");
				} else {
					saveCoursePlan.set("remark", ToolString.replaceBlank(saveCoursePlan.getStr("remark")));
				}
				if (saveCoursePlan.getInt("plan_type") == 1) {
					saveCoursePlan.set("course_id", "-" + saveCoursePlan.get("subject_id"));
				}
				saveCoursePlan.save();
				Integer useCourse = CoursePlan.getUseCourse(saveCoursePlan.getInt("student_id")); // 查询已排课节数
				Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ", useCourse, saveCoursePlan.getInt("student_id")); // 更新account表中的已排课记录
				String goon = getPara("goon");
				if ("1".equals(goon)) {
					redirect("/course/addCoursePlan?studentId=" + saveCoursePlan.getInt("STUDENT_ID").toString() + "&banjiType=1");
				} else {
					Record user = getSessionAttr("account_session");
					redirect("/course/coursePlanList?loginId=" + user.getInt("Id") + "&returnType=1");
				}
			} else { // 小班排课
				String courseTime = getPara("courseTime");
				DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
				Integer classOrder_id = getParaToInt("banci_id");
				String sql = "select * from account_banci where state=0 and banci_id=? ";// 根据班次id取出所有具有此班次的学生id
				List<Record> getName = Db.find(sql, classOrder_id);
				for (Record list : getName) {
					CoursePlan saveCoursePlan = getModel(CoursePlan.class);
					saveCoursePlan.set("course_time", dd.parse(courseTime));
					saveCoursePlan.set("create_time", new Date());
					saveCoursePlan.set("update_time", new Date());
					saveCoursePlan.set("STUDENT_ID", list.getInt("account_id"));
					saveCoursePlan.set("class_id", classOrder_id);
					if (saveCoursePlan.get("remark") == null || saveCoursePlan.get("remark").equals("")) {
						saveCoursePlan.set("remark", "暂无");
					} else {
						saveCoursePlan.set("remark", ToolString.replaceBlank(saveCoursePlan.getStr("remark")));
					}
					if (saveCoursePlan.getInt("plan_type") == 1) {
						saveCoursePlan.set("course_id", "-" + saveCoursePlan.get("subject_id"));
					}
					String stuState = Db.findFirst("select * from account where id=? ", list.getInt("account_id")).getInt("STATE").toString();
					if (stuState.equals("2")) {
						saveCoursePlan.set("STATE", 1);
					}
					saveCoursePlan.save();
					Integer stu_id = list.getInt("account_id");
					Integer useCourse = CoursePlan.getUseCourse(stu_id); // 查询已排课节数
					Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ", useCourse, stu_id); // 更新account表中的已排课记录
				}
				ClassOrder.dao.updateTeachTime(classOrder_id);// 更新开班时间和结束时间
				String goon = getPara("goon");
				if ("2".equals(goon)) {
					redirect("/course/addCoursePlan?banciId=" + classOrder_id + "&banjiType=2");
				} else {
					Record user = getSessionAttr("account_session");
					redirect("/course/coursePlanList?loginId=" + user.getInt("Id") + "&returnType=1");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	}

	public void coursePlanList() {
		try {
			String studentId = "";
			String teacherId = "";
			SysUser user = SysUser.dao.findById(getSysuserId());
			if (Role.isTeacher(user.getStr("roleids"))) {
				teacherId = getPara("loginId");
			}
			if (Role.isStudent(user.getStr("roleids"))) {
				studentId = getPara("loginId");
			}
			String banci = getPara("banci"); // 班次编号
			setAttr("banci", banci);
			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String classtype = getPara("classtype");
			setAttr("classtype", classtype);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			if ("6".equals(getPara("returnType")))// 如果今日账号
			{
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			String sql = "select * from campus";
			setAttr("campus", Db.find(sql));
			String campusSql = "";
			if (campusId == null || campusId == "" || Integer.parseInt(campusId) == 0) {
				campusSql = " 1 or 2 ";
			} else {
				campusSql = campusId;
			}
			sql = "select * from classroom where campus_id= " + campusSql;
			List<Record> classRooms = Db.find(sql);
			setAttr("classRooms", classRooms);
			sql = "select * from time_rank";
			setAttr("timeRanks", Db.find(sql));
			sql = "select  date_format(course_time,'%Y年%m月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1  ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));
			List<Object> plans = new ArrayList<Object>();
			for (Object obj : classRooms) {
				Record record = (Record) obj;
				sql = "select campus.campus_name as campusname ,courseplan.id as planId ,courseplan.room_id as roomId,courseplan.plan_type as planType,classroom.name as roomName,course.id as courseId,course.course_name as courseName,student.id as studentId,student.real_name as studentName,"
						+ "teacher.id as teacherId,teacher.real_name as teacherName,time_rank.id as rankId,time_rank.rank_name as rankName,date_format(courseplan.course_time,'%Y-%m-%d') as course_time,courseplan.state as state,courseplan.remark as remark, class_order.classNum,class_type.`name` as type_name  "
						+ "from courseplan left join course on courseplan.course_id=course.id  "
						+ " left join account as student on  courseplan.student_id=student.id "
						+ " left join account as teacher on courseplan.teacher_id=teacher.id "
						+ " left join campus on courseplan.campus_id=campus.id "
						+ " left join time_rank on courseplan.TIMERANK_ID=time_rank.id"
						+ " left join classroom on courseplan.room_id=classroom.id "
						+ " LEFT JOIN class_order ON courseplan.class_id = class_order.id "
						+ " LEFT JOIN class_type ON class_order.classtype_id = class_type.id" + " where student.state <> 1";
				sql += " and ( courseplan.campus_id = " + campusSql + " )";
				if (Integer.parseInt(getPara("returnType")) != 3)
					sql += " and courseplan.room_id=" + record.get("ID");
				if (studentName != null && !studentName.equals(""))// 根据学员名称
					sql += " and (student.real_name like'%" + studentName + "%' or student.REAL_NAME like'%" + studentName + "%')";
				if (teacherName != null && !teacherName.equals(""))// 老师名称
					sql += " and (teacher.real_name like'%" + teacherName + "%' or teacher.REAL_NAME like'%" + teacherName + "%')";
				if (classtype.equals("0"))
					sql += " and class_id = 0 ";
				if (classtype.equals("1"))
					sql += " and class_id <> 0 ";
				if (banci != null && !banci.equals(""))// 班次
					sql += " and (class_order.classNum like'%" + banci + "%' or class_order.classNum like'%" + banci + "%')";
				if (date1 != null && !date1.equals(""))// 开始时间段
					sql += " and courseplan.course_time>='" + date1 + "'";
				else
					sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
				if (date2 != null && !date2.equals(""))// 结束时间段
					sql += " and courseplan.course_time<='" + date2 + "'";
				if (teacherId != null && !teacherId.equals("")) {
					sql += " and courseplan.teacher_id=" + Integer.parseInt(teacherId);
				}
				if (studentId != null && !studentId.equals("")) {
					sql += " and courseplan.student_id=" + Integer.parseInt(studentId);
				}
				sql += " GROUP BY COURSE_TIME,ROOM_ID,RANK_NAME,TEACHER_ID order by course_time,rankId ";
				List<Record> list = Db.find(sql);
				String sql2 = "SELECT 	account.Id,	account.REAL_NAME, courseplan.STATE, courseplan.class_id  FROM\n" + "	courseplan\n"
						+ "LEFT JOIN account ON account.Id = courseplan.STUDENT_ID\n" + "WHERE\n" + "	account.state <> 1 and COURSE_TIME = ?\n"
						+ "AND ROOM_ID = ?\n" + "AND TIMERANK_ID = ?\n" + "AND TEACHER_ID = ? ";
				for (Record rec : list) {
					String course_time = rec.getStr("COURSE_TIME");
					Integer room_id = rec.getInt("ROOMID");
					Integer rank_id = rec.getInt("rankId");
					Integer teacher_id = rec.getInt("TEACHERID");
					List<Record> nameList = Db.find(sql2, course_time, room_id, rank_id, teacher_id);
					String student = "";
					if (nameList.size() > 1) { // 大于1则存在真实用户，需替换虚拟用户并合并用户
						for (Record rec2 : nameList) { // 取得班次用户的姓名
							if (rec2.getInt("STATE") != 1) {
								student += "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + rec2.getStr("REAL_NAME");
							}
						}
						rec.set("STUDENTNAME", student);
						if (rec.getStr("teacherName").indexOf("测试") != -1) {
							rec.set("teacherName", "【模考】");
						}
					} else {
						if (nameList.size() > 0) {
							if (nameList.get(0).getInt("STATE") != 1) {
								student = nameList.get(0).getStr("REAL_NAME");
								rec.set("STUDENTNAME", student);
							} else {
								rec.set("STUDENTNAME", "无");
							}
							if (rec.getStr("teacherName").indexOf("测试") != -1) {
								rec.set("teacherName", "【模考】");
							}
						}
					}
					String str = rec.getStr("COURSE_TIME");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date3 = null;
					date3 = sdf.parse(str);
					String weekday = getDateToWeek(date3);
					rec.set("weekday", weekday);
				}
				plans.add(list);
				if (Integer.parseInt(getPara("returnType")) == 3) {
					break;

				}
			}
			setAttr("plans", plans);
			if (getPara("returnType") == null)
				renderJsp("/course/coursePlanList.jsp");
			else {
				if (Integer.parseInt(getPara("returnType")) == 1)
					renderJsp("/course/coursePlanList.jsp");
				else if (Integer.parseInt(getPara("returnType")) == 2)
					renderJsp("/course/coursePlanListDis.jsp");
				else if (Integer.parseInt(getPara("returnType")) == 3)
					renderJsp("/course/export_list.jsp");
				else if (Integer.parseInt(getPara("returnType")) == 6)
					renderJsp("/course/courseSort_Today.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public static String getDateToWeek(Date date) {
		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(calendar.DAY_OF_WEEK) - calendar.SUNDAY;
		if (dayIndex < 0) {
			dayIndex = 0;
		}
		return weekDays[dayIndex];
	}

	/**
	 * /course/getCourseCalender 按课程排课日历
	 */
	public synchronized void getCourseCalender() {
		JSONObject json = new JSONObject();
		try {
			logger.info("获取按课程排课日历表格");
			String stuId = getPara("stuId");
			String startDate = getPara("startDay");
			String endDate = getPara("endDay");
			String type = getPara("type");// 1：一对一,2:小班
			String banciId = getPara("banciId");
			String courseType = getPara("coursetype");// 1:模考 0:课程

			// 课程
			List<Record> courselist = courseService.getStudentOrClassCourse(stuId, banciId, type, courseType);
			json.put("courselist", courselist);

			// 前后几天
			List<String> dayLists = ToolDateTime.printDay(ToolDateTime.parse(startDate, "yyyy-MM-dd"), ToolDateTime.parse(endDate, "yyyy-MM-dd"));
			json.put("dayLists", dayLists);

			// 周几
			Map<String, String> dayweekmap = new HashMap<String, String>(dayLists.size());
			for (String dayweek : dayLists) {
				String week = ToolDateTime.getDateInWeek(ToolDateTime.parse(dayweek, "yyyy-MM-dd"), 0);
				dayweekmap.put(dayweek, week);
			}
			json.put("dayweek", dayweekmap);

			// 课程已排课
			Map<String, Map<String, Object>> courseMap = new HashMap<String, Map<String, Object>>();
			if (courseType.equals("0")) {
				for (Record course : courselist) {
					Map<String, Object> daycoursemap = new HashMap<String, Object>();
					daycoursemap.put(course.getInt("courseid").toString(), course.getStr("course_name"));
					for (String day : dayLists) {
						List<CoursePlan> planlist = CoursePlan.coursePlan.getStudentDayCoursePlans(stuId, course.getInt("courseid").toString(), day);
						daycoursemap.put(day, planlist);
					}
					courseMap.put(course.getInt("courseid").toString(), daycoursemap);
				}
			}

			if (courseType.equals("1")) {
				// 模考
			}

			json.put("courseMap", courseMap);
			json.put("result", "1");
		} catch (Exception ex) {
			logger.info("获取日历失败.");
			json.put("result", "0");
			ex.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * /course/toArrangeCoursePlan 按课程排课弹窗
	 */
	public void toArrangeCoursePlan() {
		String courseid = getPara("courseid");
		String coursetime = getPara("daytime");
		String stuid = getPara("stuid");
		CoursePlan lastcp = CoursePlan.coursePlan.getLastCoursePlan(stuid, courseid);
		setAttr("lastcp", lastcp);
		List<Teacher> teacherlists = Teacher.dao.getTeachersByCourseidsState(courseid);
		Course course = Course.dao.findById(courseid);
		setAttr("course", course);
		setAttr("courseid", courseid);
		setAttr("coursetime", coursetime);
		setAttr("teacherlists", teacherlists);
		renderJsp("/course/layer_arrangecourse.jsp");
	}

	/**
	 * /course/getTimeRankByTeacherId 根据老师选出可用时段
	 */
	public void getTimeRankByTeacherId() {
		String coursetime = getPara("coursetime");
		String tchid = getPara("tchid");
		String stuid = getPara("stuid");
		List<TimeRank> ranklist = TimeRank.dao.getAddPlanTimeRank();
		List<CoursePlan> planlist = CoursePlan.coursePlan.getTeacherPlanesdDay(stuid, tchid, coursetime);
		if (planlist != null && planlist.size() > 0) {
			for (CoursePlan plan : planlist) {
				String plantype = plan.getInt("plan_type").toString();
				Integer starttime = null;
				Integer endtime = null;
				if (plantype.equals("2")) {// 老师休息
					starttime = Integer.parseInt(plan.getStr("startrest").replace(":", ""));
					endtime = Integer.parseInt(plan.getStr("endrest").replace(":", ""));
				} else {// 课程和模考
					starttime = Integer.parseInt(plan.getStr("rank_name").split("-")[0].replace(":", ""));
					endtime = Integer.parseInt(plan.getStr("rank_name").split("-")[1].replace(":", ""));
				}
				for (TimeRank time : ranklist) {
					Integer rankstart = Integer.parseInt(time.getStr("rank_name").split("-")[0].replace(":", ""));
					Integer rankend = Integer.parseInt(time.getStr("rank_name").split("-")[1].replace(":", ""));
					if ((rankstart <= starttime && rankend > starttime) || (starttime <= rankstart && endtime > rankstart)) {
						// 0正常；1占用；2有课
						if (!time.get("code").toString().equals("2"))
							time.put("code", "1");
					}
					if (rankstart.equals(starttime) && rankend.equals(endtime)) {
						time.put("code", "2");
					}
				}
			}
		}
		renderJson(ranklist);
	}

	/**
	 * /course/getClassRoomForRanktime 根据所选时段获取教室
	 */
	public synchronized void getClassRoomForRanktime() {
		JSONObject json = new JSONObject();
		String stuid = getPara("stuid");
		String tchid = getPara("tchid");
		String rankid = getPara("rankid");
		String campusid = getPara("campusid");
		String coursetime = getPara("coursetime");
		String plantype = getPara("plantype");
		String courseId = getPara("courseId");
		try {
			logger.info("取出交叉时段及本身已有的排课");
			List<CoursePlan> planlist = courseplanService.getPlanListByRankIdArround(rankid, coursetime, stuid, tchid);
			json.put("planlist", planlist);
			logger.info("开始查看课时是否足够");
			boolean flag = studentService.checkHaveEnoughHours(stuid, rankid, plantype,courseId);
			if (flag) {
				logger.info("课时足够");
				json.put("enough", "1");
			} else {
				logger.info("课时不够用了");
				json.put("enough", "0");
				json.put("normal", "1");
				renderJson(json);
			}
			logger.info("获取教室");
			Map<String, Object> roomMap = courseplanService.getCampusDayRoomMsgMap(campusid, coursetime, rankid);
			json.put("room", roomMap);
			json.put("normal", "1");
		} catch (Exception ex) {
			json.put("normal", "0");
			logger.info("/course/getClassRoomForRanktime----->获取课程有误.");
			ex.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * /course/changeRoomGetRoomPlans 重新选择教室 取出教室所在排课
	 * 
	 */
	public void changeRoomGetRoomPlans() {
		String roomid = getPara("roomid");
		String coursetime = getPara("coursetime");
		String rankid = getPara("rankid");
		List<CoursePlan> allplans = CoursePlan.coursePlan.getCoursetimeRoomPlans(roomid, coursetime);
		List<CoursePlan> planlist = new ArrayList<CoursePlan>(allplans.size());
		TimeRank rank = TimeRank.dao.findById(rankid);
		if (rank != null) {
			Integer rankstart = Integer.parseInt(rank.getStr("rank_name").split("-")[0].replace(":", ""));
			Integer rankend = Integer.parseInt(rank.getStr("rank_name").split("-")[1].replace(":", ""));
			if (allplans != null && allplans.size() > 0) {
				for (CoursePlan plan : allplans) {
					Integer startplan = null;
					Integer endplan = null;
					startplan = Integer.parseInt(plan.getStr("rank_name").split("-")[0].replace(":", ""));
					endplan = Integer.parseInt(plan.getStr("rank_name").split("-")[1].replace(":", ""));
					if ((rankstart <= startplan && rankend > startplan) || (rankstart >= startplan && rankstart < endplan)) {
						planlist.add(plan);
					}

				}
			}
		}
		renderJson(planlist);
	}

	/**
	 * /course/getCalender 添加排课
	 */
	public void getCalender() {
		try {
			String stuId = getPara("stuId");
			String startDate = getPara("startDay");
			String endDate = getPara("endDay");
			String tId = getPara("tId");
			String type = getPara("type");
			String banciId = getPara("banciId");
			String courseType = getPara("coursetype");

			Map<Object, Object> map = new HashMap<Object, Object>();
			// 时段
			List<TimeRank> tr = TimeRank.dao.getTimeRank();
			map.put("timerank", tr);
			// 不可添加
			List<Map<String, Object>> cantDay = new ArrayList<Map<String, Object>>();
			// 日期集合
			List<Map<String, String>> dayList = new ArrayList<Map<String, String>>();
			List<String> dayLists = ToolDateTime.printDay(ToolDateTime.parse(startDate, "yyyy-MM-dd"), ToolDateTime.parse(endDate, "yyyy-MM-dd"));
			for (int i = 0; i < dayLists.size(); i++) {
				Map<String, String> dayMap = new HashMap<String, String>();
				dayMap.put("dateTime", dayLists.get(i));
				dayMap.put("dayWeek", ToolDateTime.getDateInWeek(ToolDateTime.parse(dayLists.get(i), "yyyy-MM-dd"), 0));
				dayList.add(dayMap);

				if (!StringUtils.isEmpty(tId)) {
					List<Map<String, Object>> cantmaplist = courseService.getTeacherWeekDayRestRankId(tId, ToolDateTime.parse(dayLists.get(i), "yyyy-MM-dd"),
							ToolDateTime.getDateInWeek(ToolDateTime.parse(dayLists.get(i), "yyyy-MM-dd"), 0));
					if (cantmaplist != null) {
						for (Map<String, Object> mt : cantmaplist) {
							cantDay.add(mt);
						}
					}
				}

				List<CoursePlan> cpListDay = courseService.getCoursePlansByDay(dayLists.get(i), stuId, tId);
				for (CoursePlan cplistday : cpListDay) {
					String timeNames[] = cplistday.getStr("RANK_NAME").split("-");
					int beginTime = Integer.parseInt(timeNames[0].replace(":", ""));
					int endTime = Integer.parseInt(timeNames[1].replace(":", ""));
					for (TimeRank rec : tr) {
						String rankName = rec.getStr("RANK_NAME");
						String rankTimes[] = rankName.split("-");
						int beginRankTime = Integer.parseInt(rankTimes[0].replace(":", ""));
						int endRankTime = Integer.parseInt(rankTimes[1].replace(":", ""));
						if ((beginTime >= beginRankTime && beginTime < endRankTime) || (endTime > beginRankTime && endTime <= endRankTime)
								|| (beginTime <= beginRankTime && endTime > endRankTime) || (beginTime >= beginRankTime && endTime <= endRankTime)) {
							Map<String, Object> cantDayMap = new HashMap<String, Object>();
							cantDayMap.put("timeId", rec.getInt("ID"));
							cantDayMap.put("rankname", rankName);
							cantDayMap.put("day", cplistday.getStr("COURSE_TIME"));
							cantDay.add(cantDayMap);
						}
					}

				}
			}

			map.put("cantDay", cantDay);
			map.put("dayLists", dayList);
			map.put("startDate", startDate);
			map.put("endDate", endDate);

			// 根据始末时间取出日期内该学生和老师的排课
			if (type.equals("1")) {
				List<CoursePlan> stcplist = courseService.getCoursePlansBetweenDates(stuId, tId, startDate, endDate, courseType);
				map.put("cplist", stcplist);
			} else if (type.equals("2")) {
				List<CoursePlan> stcplist = courseService.getClassCoursePlansBetweenDates(banciId, tId, startDate, endDate);
				map.put("cplist", stcplist);
			}

			renderJson("result", map);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}

	/**
	 * 该方法判断添加排课时 时间和教室对否满足条件
	 */
	public void getClassRoom() {
		JSONObject json = new JSONObject();
		try {
			String dayTime = getPara("day");
			String rankId = getPara("rankId");
			String rName = getPara("rankName");
			String campusId = getPara("campusId");
			String studentId = getPara("studentId");
			String courseId = getPara("courseId");
			List<TimeRank> tr = TimeRank.dao.getTimeRank();
			String[] timeNames = rName.split("-");
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
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Classroom> cr = Classroom.dao.getClassRoomByCamp(Integer.parseInt(campusId));
			List<CoursePlan> cplist = CoursePlan.coursePlan.getCoursePlansByTimerankIds(ToolString.listToString(rankIds,","),dayTime);
			String sql = "select ROOM_ID from courseplan where STUDENT_ID=" + studentId + " order by create_time DESC";
			List<CoursePlan> listCourse = CoursePlan.coursePlan.find(sql);
			for (int i = 0; i < cplist.size(); i++) {
				cricle: for (int j = 0; j < cr.size(); j++) {
					if (cr.get(j).getInt("ID").equals(cplist.get(i).getInt("ROOM_ID"))) {
						Map<String, Object> cantMap = new HashMap<String, Object>();
						cantMap.put("room", cr.get(j));
						cantMap.put("can", "cant");
						cantMap.put("last", "cantlast");
						list.add(cantMap);
						cr.remove(j);
						break cricle;
					}
				}
			}
			for (int i = 0; i < cr.size(); i++) {
				Map<String, Object> canMap = new HashMap<String, Object>();
				canMap.put("room", cr.get(i));
				canMap.put("can", "can");
				if (listCourse.size() > 0) {
					if (listCourse.get(0).getInt("ROOM_ID").toString().equals(cr.get(i).getInt("Id").toString())) {
						canMap.put("last", "canlast");
					} else {
						canMap.put("last", "cantlast");
					}
				} else {
					canMap.put("last", "cantlast");
				}
				list.add(canMap);
			}

			Double sumhours = CoursePlan.coursePlan.getHoursForStudentCoursePlaned(studentId, courseId);
			String courseMsg = Course.dao.findById(courseId).getStr("COURSE_NAME") + " (已排" + sumhours + "课时)";
			json.put("courseMsg", courseMsg);
			json.put("result", list);
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}

	}

	public void queryCoursePlansManagement() {
		try {
			Integer sysuserId = getSysuserId();
			String studentId = "";
			String teacherId = "";
			String banci = getPara("banci");
			setAttr("banci", banci);
			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String classtype = getPara("classtype");
			setAttr("classtype", classtype);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 6);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (!StringUtils.isEmpty(studentName)) {
				Account student = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + studentName + "'");
				if (student != null)
					studentId = student.getInt("ID") + "";
			}
			Account teacher = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + teacherName + "'");
			if (teacher != null)
				teacherId = teacher.getInt("ID") + "";
			List<CoursePlan> getCourseDate = CoursePlan.getCourseDate(date1, date2);

			StringBuffer coursePlanSql = new StringBuffer(
					"select * from ( SELECT cp.Id, IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,");
			coursePlanSql
					.append("cp.class_id,r.Id as RANK_ID,r.RANK_NAME,IF(cp.COURSE_ID<0,(SELECT s.SUBJECT_NAME FROM `subject` s WHERE s.Id = ABS(cp.COURSE_ID)),c.COURSE_NAME) COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ,");
			coursePlanSql
					.append(" cp.STATE,cp.CAMPUS_ID,cp.ROOM_ID,cp.rechargecourse,IFNULL( CONCAT( IF(LENGTH(cp.startrest)<=4,CONCAT('0',cp.startrest),cp.startrest), '-', cp.endrest ), r.RANK_NAME ) trrankname, cp.COURSE_TIME,");
			coursePlanSql
					.append(" ( SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id) ) as CAMPUS_NAME,");
			coursePlanSql.append(" cp.REMARK,cp.PLAN_TYPE,cp.startrest,cp.iscancel,cp.endrest,class_order.classNum,class_type.`name` AS type_name,cc.kcgwids");
			coursePlanSql.append(" FROM courseplan cp ");
			coursePlanSql.append(" LEFT JOIN course c ON cp.COURSE_ID=c.Id");
			coursePlanSql.append(" LEFT JOIN account as t ON cp.TEACHER_ID=t.Id");
			coursePlanSql.append(" LEFT JOIN account as s ON cp.STUDENT_ID=s.Id");
			coursePlanSql.append(" LEFT JOIN (SELECT GROUP_CONCAT(k.REAL_NAME) real_name, GROUP_CONCAT(ak.kcgw_id) kcgwids, ak.student_id id ");
			coursePlanSql
					.append(" FROM student_kcgw ak LEFT JOIN account a ON ak.student_id = a.Id LEFT JOIN ( SELECT * FROM account 	WHERE "
							+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM 	pt_role WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0 ) k ON k.Id = ak.kcgw_id GROUP BY a.Id) cc on s.id = cc.id ");
			coursePlanSql.append(" LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id");
			coursePlanSql.append(" LEFT JOIN classroom m ON cp.ROOM_ID=m.Id ");
			coursePlanSql.append(" LEFT JOIN class_order ON cp.class_id = class_order.id ");
			coursePlanSql.append(" LEFT JOIN class_type ON class_order.classtype_id = class_type.id   WHERE   s.state <> 1  and cp.COURSE_TIME =? ");
			if (teacherId.length() > 0) {// 老师
				String teacherIds = Teachergroup.dao.getGroupMembersId(teacherId);
				coursePlanSql.append(" AND cp.TEACHER_ID IN (" + teacherIds + ")");
			}
			if (studentId.length() > 0) {
				coursePlanSql.append(" AND cp.STUDENT_ID=").append(studentId);
			} 
			if (classtype != null) {
				if (classtype.equals("0"))
					coursePlanSql.append(" and cp.class_id = 0 ");
				if (classtype.equals("1"))
					coursePlanSql.append("and cp.class_id <> 0 ");
			}
			
			if (banci != null && !banci.equals(""))// 班次
				coursePlanSql.append(" and class_order.classNum like'%").append(banci).append("%'");
			if (StringUtils.isEmpty(campusId)) {
				String campusids = Campus.dao.IsCampusKcFzr(sysuserId);
				if (campusids != null) {
					coursePlanSql.append(" and cp.CAMPUS_ID IN(").append(campusids).append(")");
				}
			} else {
				coursePlanSql.append(" AND cp.CAMPUS_ID=").append(campusId);
			}
			setAttr("campus", Db.find("select * from campus "));
			Map<String, Object> timeMap = new LinkedHashMap<String, Object>();
			for (CoursePlan cpdate : getCourseDate) {
				String courseDate = cpdate.getStr("COURSE_TIME");
				List<CoursePlan> cplist = CoursePlan.coursePlan.find(coursePlanSql + " ) a ORDER BY  a.trrankname asc  ", courseDate);
				timeMap.put(courseDate, cplist);

			}
			setAttr("timeMap", timeMap);
			setAttr("timeRanks", Db.find("select * from time_rank"));
			renderJsp("/course/coursePlanList.jsp");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void queryAllcoursePlans() {
		try {
			Integer sysuserId = getSysuserId();
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			Integer returnType = getParaToInt("returnType");
			String studentId = "";
			String teacherId = "";
			String banci = getPara("banci"); // 班次编号
			setAttr("banci", banci);
			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				/*
				 * if (returnType == 1) { calendar.add(Calendar.DATE, 6); }else{
				 */
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				/* } */
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (Role.isStudent(sysuser.getStr("roleids"))) {// 学生
				studentId = getSysuserId() + "";
			} else {
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + studentName + "'");
				if (account != null)
					studentId = account.getInt("ID") + "";
			}
			if (Role.isTeacher(sysuser.getStr("roleids"))) {// 老师名称
				teacherId = getSysuserId() + "";
			} else {
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + teacherName + "'");
				if (account != null)
					teacherId = account.getInt("ID") + "";
			}
			String campussql = "select * from campus ";
			String sql = "";
			/*
			 * String zdjsxqsql =
			 * "SELECT cr.CAMPUS_ID FROM classroom cr GROUP BY cr.CAMPUS_ID ORDER BY COUNT(*) DESC"
			 * ;// 最多教室SQL Record zdjsxq = Db.findFirst(zdjsxqsql); if (zdjsxq
			 * != null) { int xqid = zdjsxq.getInt("CAMPUS_ID"); sql =
			 * "select * from classroom where campus_id=?";// 取出教室最多的中关村校区
			 * List<Record> classRooms = Db.find(sql, xqid);
			 * setAttr("classRooms", classRooms);
			 */

			StringBuffer coursePlanSql = new StringBuffer(
					"select * from ( SELECT cp.Id,cp.netCourse,ca.campustype, IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,");
			coursePlanSql
					.append("cp.class_id,r.Id as RANK_ID,r.RANK_NAME,CONCAT(IF(cp.COURSE_ID<0,(SELECT s.SUBJECT_NAME FROM `subject` s WHERE s.Id = ABS(cp.COURSE_ID)),c.COURSE_NAME),IF ( cp.netCourse = 1,'网络课',''	)) COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ,");
			coursePlanSql
					.append(" cp.STATE,cp.CAMPUS_ID,cp.ROOM_ID,cp.rechargecourse,IFNULL( CONCAT( IF(LENGTH(cp.startrest)<=4,CONCAT('0',cp.startrest),cp.startrest), '-', cp.endrest ), r.RANK_NAME ) trrankname, cp.COURSE_TIME,");
			coursePlanSql
					.append(" ( SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id) ) as CAMPUS_NAME,");
			coursePlanSql.append(" cp.REMARK,cp.PLAN_TYPE,cp.iscancel,cp.startrest,cp.endrest,class_order.classNum,class_type.`name` AS type_name,cc.kcgwids");
			coursePlanSql.append(" FROM courseplan cp ");
			coursePlanSql.append(" LEFT JOIN course c ON cp.COURSE_ID=c.Id");
			coursePlanSql.append(" LEFT JOIN account as t ON cp.TEACHER_ID=t.Id");
			coursePlanSql.append(" LEFT JOIN account as s ON cp.STUDENT_ID=s.Id");
			coursePlanSql.append(" LEFT JOIN (SELECT GROUP_CONCAT(k.REAL_NAME) real_name, GROUP_CONCAT(ak.kcgw_id) kcgwids, ak.student_id id ");
			coursePlanSql
					.append(" FROM student_kcgw ak LEFT JOIN account a ON ak.student_id = a.Id LEFT JOIN ( SELECT * FROM account 	WHERE "
							+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM 	pt_role WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0 ) k ON k.Id = ak.kcgw_id GROUP BY a.Id) cc on s.id = cc.id ");
			coursePlanSql.append(" LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id");
			coursePlanSql.append(" LEFT JOIN classroom m ON cp.ROOM_ID=m.Id ");
			coursePlanSql.append(" LEFT JOIN class_order ON cp.class_id = class_order.id ");
			coursePlanSql.append(" LEFT JOIN class_type ON class_order.classtype_id = class_type.id LEFT JOIN campus ca ON ca.Id = cp.CAMPUS_ID"
					+ " WHERE s.state <> 1 and cp.COURSE_TIME =? ");
			if (Role.isAdmins(sysuser.getStr("roleids"))) {
				setAttr("campus", Campus.dao.getCampus());
				if (teacherId.length() > 0) {// 老师
					String teacherIds = Teachergroup.dao.getGroupMembersId(teacherId);
					coursePlanSql.append(" AND cp.TEACHER_ID IN (" + teacherIds + ")");
				} else if (studentId.length() > 0) {// 学生登录
					coursePlanSql.append(" AND cp.STUDENT_ID=").append(studentId);
				}

			} else {
				if (Role.isTeacher(sysuser.getStr("roleids")) || teacherId.length() > 0) {// 老师
					String teacherIds = Teachergroup.dao.getGroupMembersId(teacherId);
					coursePlanSql.append(" AND cp.TEACHER_ID IN (" + teacherIds + ")");
				} else if (Role.isStudent(sysuser.getStr("roleids")) || studentId.length() > 0) {// 学生登录
					coursePlanSql.append(" AND cp.STUDENT_ID=").append(studentId);
				}
				setAttr("campus", AccountCampus.dao.getCampusByAccountId(sysuserId));
			}
			if (banci != null && !banci.equals(""))// 班次
				coursePlanSql.append(" and class_order.classNum like'%").append(banci).append("%'");
			List<CoursePlan> getCourseDate = CoursePlan.getCourseDate(date1, date2);
			Map<String, Object> timeMap = new LinkedHashMap<String, Object>();
			if (StringUtils.isEmpty(campusId)) {
				String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
				if (campusids != null) {
					coursePlanSql.append(" and cp.CAMPUS_ID IN(").append(campusids).append(")");
					campussql += "WHERE id IN(" + campusids + ")";
				}
			} else {
				coursePlanSql.append(" AND cp.CAMPUS_ID=").append(campusId);
				campussql += "WHERE id =" + campusId;
			}
			String locale = getCookie("_locale");
			List<Record> campusRecord = Db.find(campussql);
			for (CoursePlan cpdate : getCourseDate) {
				String courseDate = cpdate.getStr("COURSE_TIME");
				List<CoursePlan> cplist = CoursePlan.coursePlan.find(coursePlanSql + " ) a ORDER BY a.COURSE_TIME asc, a.trrankname asc  ", courseDate);
				Map<String, Object> campusMap = new LinkedHashMap<String, Object>();
				for (Object campusObj : campusRecord) {// 循环校区
					// 循环校区
					Record campus = (Record) campusObj;
					String campusName = campus.get("CAMPUS_NAME");// 校区名称
					String roomsql = "select * from classroom where campus_id = ? ";
					List<Record> classRooms = Db.find(roomsql, campus.getInt("Id"));
					Map<String, Object> timeRankMap = new LinkedHashMap<String, Object>();
					List<TimeRank> timeRankList = TimeRank.dao.getAllTimeRank();
					Map<String, Object> coursePlanCampus = new HashMap<String, Object>();
					int countcampus = 0;
					int showcount = 0;
					for (TimeRank timeRank : timeRankList) {// 循环时段
						Integer timeRankId = timeRank.getInt("ID");
						String timeRankName = timeRank.getStr("RANK_NAME");
						Map<String, Object> coursePlanRoom = new HashMap<String, Object>();
						int count = 0;
						List<CoursePlan> coursePlanRoomList = new ArrayList<CoursePlan>();
						for (Object obj : classRooms) {// 循环教室
							Record record = (Record) obj;
							String classRoomName = record.get("NAME");
							boolean hasPlan = false;
							for (CoursePlan cp : cplist) {// 循环课程安排
								String teach_type = cp.getStr("teach_type");
								if (locale != null) {
									if (locale.equals("en_US")) {// 标题头国际化
										teach_type = teach_type.replace("授课:一对一", "One-on-One");
									}
								}
								cp.put("teach_type", teach_type);
								Integer cid = cp.getInt("CAMPUS_ID");
								if (!campus.getInt("id").equals(cid)) {
									continue;
								}
								String roomName = cp.getStr("ROOM_NAME");
								Integer rId = cp.getInt("RANK_ID");
								String courseCampusName = cp.getStr("CAMPUS_NAME");
								String student = "";
								if (timeRankId.equals(rId) && campusName.equals(courseCampusName)) {
									if (classRoomName.equals(roomName)) {
										if (cp.getInt("class_id") != 0) {
											student += CoursePlan.getNames(roomName, rId, courseCampusName, courseDate);
											cp.put("S_NAME", student);
										}
										/*
										 * if(returnType == 1){
										 * coursePlanRoomList.add(cp); }else{
										 */
										if (cp.getInt("PLAN_TYPE") == 1) {
											cp.put("teach_type", "【模考】");
											// cp.put("T_NAME", "");
										}
										if (!StringUtils.isEmpty(teacherName)) {
											coursePlanRoomList.add(cp);
										} else {
											if (cp.getInt("PLAN_TYPE") != 2) {
												coursePlanRoomList.add(cp);
											}
										}
										/* } */
										hasPlan = true;
										break;
									} else {
										continue;
									}
								} else {
									continue;
								}
							}
							if (!hasPlan) {
								count++;
								coursePlanRoomList.add(null);
							}
						}
						coursePlanRoom.put("isnull", count);
						showcount += count;
						coursePlanRoom.put("valuelist", coursePlanRoomList);
						timeRankMap.put(timeRankName, coursePlanRoom);
						if (count == coursePlanRoomList.size()) {
							countcampus++;
						}
					}
					coursePlanCampus.put("showcount", showcount);
					coursePlanCampus.put("rooms", classRooms);
					coursePlanCampus.put("campus", countcampus);
					coursePlanCampus.put("campuslist", timeRankMap);
					campusMap.put(campusName, coursePlanCampus);
				}
				timeMap.put(courseDate, campusMap);

			}
			setAttr("timeMap", timeMap);
			setAttr("timeRanks", Db.find("select * from time_rank"));
			sql = "select  date_format(course_time,'%Y年%c月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1 and courseplan.state=0 ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));

			/*
			 * if (returnType == 1) { renderJsp("/course/coursePlanList.jsp"); }
			 * else
			 */if (returnType == 2) {
				renderJsp("/course/coursePlanListDis.jsp");
				// }else if(campusId == 1 || campusId == 2){
				// renderJsp("/course/courseSort_Today.jsp");
			} else {
				renderJsp("/course/all_courseplan_today.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	public void queryAllcoursePlansByroom() {
		try {
			Integer sysuserId = getSysuserId();
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			String studentId = "";
			String teacherId = "";
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (Role.isStudent(sysuser.getStr("roleids"))) {// 学生
				studentId = getSysuserId() + "";
			}
			if (Role.isTeacher(sysuser.getStr("roleids"))) {// 老师名称
				teacherId = getSysuserId() + "";
			}
			String campussql = "select * from campus ";
			String sql = "";
			boolean flag = true;

			StringBuffer coursePlanSql = new StringBuffer(
					"select * from ( SELECT cp.Id,cp.netCourse,ca.campustype, IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,");
			coursePlanSql
					.append("cp.class_id,r.Id as RANK_ID,r.RANK_NAME,CONCAT(IF(cp.COURSE_ID<0,(SELECT s.SUBJECT_NAME FROM `subject` s WHERE s.Id = ABS(cp.COURSE_ID)),c.COURSE_NAME),IF ( cp.netCourse = 1,'网络课',''	)) COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ,");
			coursePlanSql
					.append(" cp.STATE,cp.CAMPUS_ID,cp.ROOM_ID,cp.rechargecourse,IFNULL( CONCAT( IF(LENGTH(cp.startrest)<=4,CONCAT('0',cp.startrest),cp.startrest), '-', cp.endrest ), r.RANK_NAME ) trrankname, cp.COURSE_TIME,");
			coursePlanSql
					.append(" ( SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id) ) as CAMPUS_NAME,");
			coursePlanSql.append(" cp.REMARK,cp.PLAN_TYPE,cp.iscancel,cp.startrest,cp.endrest,class_order.classNum,class_type.`name` AS type_name,cc.kcgwids");
			coursePlanSql.append(" FROM courseplan cp ");
			coursePlanSql.append(" LEFT JOIN course c ON cp.COURSE_ID=c.Id");
			coursePlanSql.append(" LEFT JOIN account as t ON cp.TEACHER_ID=t.Id");
			coursePlanSql.append(" LEFT JOIN account as s ON cp.STUDENT_ID=s.Id");
			coursePlanSql.append(" LEFT JOIN (SELECT GROUP_CONCAT(k.REAL_NAME) real_name, GROUP_CONCAT(ak.kcgw_id) kcgwids, ak.student_id id ");
			coursePlanSql
					.append(" FROM student_kcgw ak LEFT JOIN account a ON ak.student_id = a.Id LEFT JOIN ( SELECT * FROM account 	WHERE "
							+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM 	pt_role WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0) k ON k.Id = ak.kcgw_id GROUP BY a.Id) cc on s.id = cc.id ");
			coursePlanSql.append(" LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id");
			coursePlanSql.append(" LEFT JOIN classroom m ON cp.ROOM_ID=m.Id ");
			coursePlanSql.append(" LEFT JOIN class_order ON cp.class_id = class_order.id ");
			coursePlanSql
					.append(" LEFT JOIN class_type ON class_order.classtype_id = class_type.id LEFT JOIN campus ca ON ca.Id = cp.CAMPUS_ID  WHERE (IF(cp.class_id<>0,s.STATE=2,s.STATE<>1)) and cp.COURSE_TIME =? ");
			if (Role.isTeacher(sysuser.getStr("roleids")) || teacherId.length() > 0) {// 老师
				String teacherIds = Teachergroup.dao.getGroupMembersId(teacherId);
				coursePlanSql.append(" AND cp.TEACHER_ID IN (" + teacherIds + ")");
			} else if (Role.isStudent(sysuser.getStr("roleids")) || studentId.length() > 0) {// 学生登录
				coursePlanSql.append(" AND cp.STUDENT_ID=").append(studentId);
			} else if (Role.isDudao(sysuser.getStr("roleids"))) {// 督导
				String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
				if (campusids != null) {
					coursePlanSql.append(" and cp.CAMPUS_ID IN(").append(campusids).append(")");
					campussql += "WHERE id IN(" + campusids + ")";
					flag = false;
				} else {
					campussql += "WHERE id IN(0)";
					flag = false;
				}
			}

			List<CoursePlan> getCourseDate = CoursePlan.getCourseDate(date1, date2);
			if (StringUtils.isEmpty(campusId) && flag) {
				String campusids = Campus.dao.IsCampusKcFzr(sysuserId);
				if (campusids != null) {
					coursePlanSql.append(" and cp.CAMPUS_ID IN(").append(campusids).append(")");
					campussql += "WHERE id IN(" + campusids + ")";
				}
			} else if (flag) {
				coursePlanSql.append(" AND cp.CAMPUS_ID=").append(campusId);
				campussql += "WHERE id =" + campusId;
			}
			List<Record> campusRecord = Db.find(campussql);
			String campusidsAttr = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
			setAttr("campus", Db.find("select * from campus WHERE id IN(" + campusidsAttr + ")"));
			String locale = getCookie("_locale");
			Map<String, Object> campusMap = new LinkedHashMap<String, Object>();
			for (Object campusObj : campusRecord) {// 循环校区
				// 循环校区
				Record campus = (Record) campusObj;
				String campusName = campus.get("CAMPUS_NAME");// 校区名称
				String roomsql = "select * from classroom where campus_id = ? ";

				List<Record> classRooms = Db.find(roomsql, campus.getInt("Id"));
				Map<String, Object> timeRankMap = new LinkedHashMap<String, Object>();
				Map<String, Object> coursePlanCampus = new HashMap<String, Object>();
				int countcampus = 0;
				for (CoursePlan cpdate : getCourseDate) {
					String courseDate = cpdate.getStr("COURSE_TIME");
					List<CoursePlan> cplist = CoursePlan.coursePlan.find(coursePlanSql
							+ "   and cp.PLAN_TYPE!=2 ) a ORDER BY a.COURSE_TIME asc, a.trrankname asc  ", courseDate);
					Map<String, Object> coursePlanRoom = new HashMap<String, Object>();
					int count = 0;
					List<Object> coursePlanRoomList = new ArrayList<Object>();
					for (Object obj : classRooms) {// 循环教室
						Record record = (Record) obj;
						String classRoomName = record.get("NAME");
						List<CoursePlan> coursePlanList = new ArrayList<CoursePlan>();
						for (CoursePlan cp : cplist) {// 循环课程安排
							String teach_type = cp.getStr("teach_type");
							if (locale != null) {
								if (locale.equals("en_US")) {// 标题头国际化
									teach_type = teach_type.replace("授课:一对一", "One-on-One");
								}
							}
							cp.put("teach_type", teach_type);
							Integer cid = cp.getInt("CAMPUS_ID");
							if (!campus.getInt("id").equals(cid)) {
								continue;
							}
							String roomName = cp.getStr("ROOM_NAME");
							Integer rId = cp.getInt("RANK_ID");
							String courseCampusName = cp.getStr("CAMPUS_NAME");
							String student = "";
							if (campusName.equals(courseCampusName)) {
								if (classRoomName.equals(roomName)) {
									if (cp.getInt("class_id") != 0) {
										student += CoursePlan.getNames(roomName, rId, courseCampusName, courseDate);
										cp.put("S_NAME", student);
									}
									if (cp.getInt("PLAN_TYPE") == 1) {
										cp.put("teach_type", "【模考】");
									}
									coursePlanList.add(cp);
								} else {
									continue;
								}
							} else {
								continue;
							}
						}
						if (coursePlanList.size() == 0) {
							count++;
							coursePlanRoomList.add(null);
						} else {
							coursePlanRoomList.add(coursePlanList);
						}
					}
					coursePlanRoom.put("isnull", count);
					coursePlanRoom.put("valuelist", coursePlanRoomList);
					timeRankMap.put(courseDate, coursePlanRoom);
					if (count == coursePlanRoomList.size()) {
						countcampus++;
					}
				}
				coursePlanCampus.put("rooms", classRooms);
				coursePlanCampus.put("campusnull", countcampus);
				coursePlanCampus.put("campuslist", timeRankMap);
				campusMap.put(campusName, coursePlanCampus);
			}
			setAttr("timeMap", campusMap);
			setAttr("timeRanks", Db.find("select * from time_rank"));
			sql = "select  date_format(course_time,'%Y年%c月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1 and courseplan.state=0 ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));

			renderJsp("/course/classroom_kebiao.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	/**
	 * 教师课表
	 */
	public void queryAllcoursePlansByteacher() {
		try {
			String teacherId = getPara("allteacherids");
			StringBuffer teacherIdB = new StringBuffer();
			Integer sysuserId = getSysuserId();
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			String returnType = getPara("returnType");
			if (StrKit.isBlank(returnType))
				returnType = "1";
			setAttr("returnType", returnType);
			String studentId = "";
			if (StrKit.isBlank(teacherId)) {
				teacherId = "";
			} else {
				teacherId = teacherId.replaceFirst("\\|", "");
				setAttr("teacheredit", teacherId);
				String[] str = teacherId.replace("|", ",").split(",");
				for (int i = 0; i < str.length; i++) {
					teacherIdB.append("," + str[i]);
				}
				teacherId = teacherIdB.toString().replaceFirst(",", "");
			}
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			String teacherDateSql;
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				logger.info("第一次进入自动选择今天起七天之内的时间。");
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime();
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 6);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime();
				date2 = formatter.format(date);
			}
			teacherDateSql = " AND DATE_FORMAT(cp.COURSE_TIME, '%Y-%m-%d') >='" + date1 + "' AND DATE_FORMAT(cp.COURSE_TIME, '%Y-%m-%d') <='" + date2 + "'";
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (Role.isStudent(sysuser.getStr("roleids"))) {// 学生
				studentId = getSysuserId() + "";
			}
			if (Role.isTeacher(sysuser.getStr("roleids"))) {// 老师名称
				teacherId = getSysuserId() + "";
				teacherId = Teachergroup.dao.getGroupMembersId(teacherId);
			}

			StringBuffer coursePlanSql = new StringBuffer(
					"select * from ( SELECT cp.Id,cp.TEACHER_ID, IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,");
			coursePlanSql
					.append("cp.class_id,r.Id as RANK_ID,r.RANK_NAME,IF(cp.COURSE_ID<0,(SELECT s.SUBJECT_NAME FROM `subject` s WHERE s.Id = ABS(cp.COURSE_ID)),c.COURSE_NAME) COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ,");
			coursePlanSql
					.append(" cp.STATE,cp.CAMPUS_ID,cp.ROOM_ID,cp.rechargecourse,IFNULL( CONCAT( IF(LENGTH(cp.startrest)<=4,CONCAT('0',cp.startrest),cp.startrest), '-', cp.endrest ), r.RANK_NAME ) trrankname,DATE_FORMAT(COURSE_TIME, '%Y-%m-%d') as COURSE_TIME,");
			coursePlanSql
					.append(" ( SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id) ) as CAMPUS_NAME,");
			coursePlanSql.append(" cp.REMARK,cp.PLAN_TYPE,cp.startrest,cp.iscancel,cp.endrest,class_order.classNum,class_type.`name` AS type_name,cc.kcgwids");
			coursePlanSql.append(" FROM courseplan cp ");
			coursePlanSql.append(" LEFT JOIN course c ON cp.COURSE_ID=c.Id");
			coursePlanSql.append(" LEFT JOIN account as t ON cp.TEACHER_ID=t.Id");
			coursePlanSql.append(" LEFT JOIN account as s ON cp.STUDENT_ID=s.Id");
			coursePlanSql.append(" LEFT JOIN (SELECT GROUP_CONCAT(k.REAL_NAME) real_name, GROUP_CONCAT(ak.kcgw_id) kcgwids, ak.student_id id ");
			coursePlanSql
					.append(" FROM student_kcgw ak LEFT JOIN account a ON ak.student_id = a.Id LEFT JOIN ( SELECT * FROM account 	WHERE "
							+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM 	pt_role WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0 ) k ON k.Id = ak.kcgw_id GROUP BY a.Id) cc on s.id = cc.id ");
			coursePlanSql.append(" LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id");
			coursePlanSql.append(" LEFT JOIN classroom m ON cp.ROOM_ID=m.Id ");
			coursePlanSql.append(" LEFT JOIN class_order ON cp.class_id = class_order.id ");
			coursePlanSql
					.append(" LEFT JOIN class_type ON class_order.classtype_id = class_type.id WHERE (IF(cp.class_id<>0,s.STATE=2,s.STATE<>1)) and cp.PLAN_TYPE!=1 ");
			StringBuffer teacherSql = new StringBuffer();
			teacherSql
					.append("SELECT DISTINCT cp.TEACHER_ID,t.REAL_NAME FROM courseplan cp LEFT JOIN account t ON cp.TEACHER_ID = t.Id LEFT JOIN account as s ON cp.STUDENT_ID=s.Id WHERE 1=1   and cp.PLAN_TYPE!=1 ");
			if (StrKit.notBlank(teacherId)) {
				teacherSql.append(" AND cp.TEACHER_ID IN (" + teacherId + ")");
			}
			if (StrKit.notBlank(studentId)) {
				coursePlanSql.append(" AND cp.STUDENT_ID=").append(studentId);
				teacherSql.append(" AND cp.STUDENT_ID=").append(studentId);
			}
			if (Role.isDudao(sysuser.getStr("roleids"))) {// 督导
				coursePlanSql.append(" AND s.SUPERVISOR_ID=").append(sysuser.getPrimaryKeyValue());
				teacherSql.append(" AND s.SUPERVISOR_ID=").append(sysuser.getPrimaryKeyValue());
			}

			List<CoursePlan> getCourseDate = CoursePlan.getCourseDate(date1, date2);
			if (StringUtils.isEmpty(campusId)) {
				String campusids = Campus.dao.IsCampusKcFzr(sysuserId);
				if (campusids != null) {
					coursePlanSql.append(" and cp.CAMPUS_ID IN(").append(campusids).append(")");
					teacherSql.append(" and cp.CAMPUS_ID IN(").append(campusids).append(")");
				}
			} else {
				coursePlanSql.append(" AND cp.CAMPUS_ID=").append(campusId);
				teacherSql.append(" AND cp.CAMPUS_ID=").append(campusId);
			}
			setAttr("campus", Db.find("select * from campus "));
			String locale = getCookie("_locale");
			teacherSql.append(teacherDateSql);
			List<Record> teacherList = Db.find(teacherSql.toString());
			setAttr("teacherList", Teacher.dao.getTeachers());
			List<CoursePlan> cplist = CoursePlan.coursePlan.find(coursePlanSql.append(teacherDateSql)
					.append(" ) a ORDER BY a.COURSE_TIME asc, a.trrankname asc  ").toString());
			Map<String, Object> teacherMap = new LinkedHashMap<String, Object>();
			for (Record teacher : teacherList) {
				String teacherStr = teacher.getStr("REAL_NAME");
				int teacherid_teacher = teacher.getInt("TEACHER_ID");
				List<Object> dateList = new ArrayList<Object>();
				Map<String, Object> coursePlans = new LinkedHashMap<String, Object>();
				int count = 0;
				for (CoursePlan cpdate : getCourseDate) {
					String courseDate = cpdate.getStr("COURSE_TIME");
					List<CoursePlan> coursePlanList = new ArrayList<CoursePlan>();
					for (CoursePlan cp : cplist) {
						String teach_type = cp.getStr("teach_type");
						if (locale != null) {
							if (locale.equals("en_US")) {// 标题头国际化
								teach_type = teach_type.replace("授课:一对一", "One-on-One");
							}
						}
						cp.put("teach_type", teach_type);
						if (cp.getStr("COURSE_TIME").equals(courseDate)) {
							int teacherid_cp = cp.getInt("TEACHER_ID");
							String roomName = cp.getStr("ROOM_NAME");
							Integer rId = cp.getInt("RANK_ID");
							String courseCampusName = cp.getStr("CAMPUS_NAME");
							String student = "";
							if (teacherid_cp == teacherid_teacher) {
								if (cp.getInt("class_id") != 0) {
									student += CoursePlan.getNames(roomName, rId, courseCampusName, courseDate);
									cp.put("S_NAME", student);
								}
								if (cp.getInt("PLAN_TYPE") == 1) {
									cp.put("teach_type", "【模考】");
								}
								coursePlanList.add(cp);
							}
						}
					}
					if (coursePlanList.size() == 0) {
						count++;
						dateList.add(null);
					} else {
						dateList.add(coursePlanList);
					}
				}
				coursePlans.put("datenull", count);
				coursePlans.put("datelist", dateList);
				teacherMap.put(teacherStr, coursePlans);
			}
			System.out.println(teacherMap);
			setAttr("getCourseDate", getCourseDate);
			setAttr("teacherMap", teacherMap);
			setAttr("timeRanks", Db.find("select * from time_rank"));
			String sql = "select  date_format(course_time,'%Y年%c月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1 and courseplan.state=0 ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));

			renderJsp("/course/teacher_kebiao.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	/**
	 * 更改课表状态
	 */
	public void updateCoursePlanState() {
		try {
			String state = getPara("state");
			String planId = getPara("planId");
			String sql = "update courseplan set state=" + Integer.parseInt(state) + " where id=" + Integer.parseInt(planId);
			Db.update(sql);
			renderJson("result", "ok");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 查询班课信息
	 */
	public void getClassInfo() {
		try {
			Integer classId = Integer.parseInt(getPara("classId"));
			ClassOrder calssOrder = ClassOrder.dao.findById(classId);
			if (calssOrder != null) {
				int chargeType = calssOrder.getInt("chargeType");// 0按课时1按期
				Student classStudent = Student.dao.findById(calssOrder.getInt("accountid"));// 班课的虚拟用户
				List<BanciCourse> bclist = BanciCourse.dao.getBanciCourse(classId);
				Integer ygks = calssOrder.getInt("lessonNum");// 总课时
				float ypks = CoursePlan.coursePlan.getClassYpkcClasshour(classId);
				StringBuffer msg = new StringBuffer();
				if (chargeType == 0) {
					msg.append("按课时收费");
				} else {
					msg.append("按期：").append(ToolString.subZeroAndDot(ygks + "")).append("课时");
				}
				msg.append(" 已排：" + ToolString.subZeroAndDot(ypks + "")).append("课时、");
				List<Object> courseList = new ArrayList<Object>();
				Map<Object, Object> classOrderMap = new HashMap<Object, Object>();
				for (BanciCourse bc : bclist) {
					Map<Object, Object> courseMap = new HashMap<Object, Object>();
					String ypk = ToolString.subZeroAndDot(CoursePlan.coursePlan.getClassYpkcCount(classStudent.getPrimaryKeyValue(), bc.getInt("course_id"))
							+ "");
					if (chargeType == 0) {
						courseMap.put("status", 1);
						msg.append(bc.getStr("COURSE_NAME")).append("(").append(bc.getInt("lesson_num")).append("元/课时)：").append(ypk).append(" ");
					} else {
						if (ygks <= ypks && chargeType == 1) {
							courseMap.put("status", 0);
						} else {
							courseMap.put("status", 1);
						}
						msg.append(bc.getStr("COURSE_NAME")).append("：").append(ypk).append(" ");
					}
					courseMap.put("course_id", bc.get("course_id"));
					courseMap.put("course_name", bc.get("COURSE_NAME") + "(已排" + ypk + "课时)");
					courseList.add(courseMap);
				}
				classOrderMap.put("courseList", courseList);
				classOrderMap.put("courseUseNum", ypks);
				classOrderMap.put("stuName", classStudent.getStr("REAL_NAME"));
				classOrderMap.put("stuMsg", msg.toString());
				classOrderMap.put("studentId", classStudent.getPrimaryKeyValue());
				classOrderMap.put("sumCourse", ygks);
				classOrderMap.put("useCourse", ypks);
				classOrderMap.put("chargeType", chargeType);
				renderJson("account", classOrderMap);
			} else {
				renderJson("account", "noResult");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}

	}

	public void delClassWeekCoursePlan() {
		try {

			Integer classId = Integer.parseInt(getPara("classId"));
			String courseTime = getPara("courseTime");
			String rankId = getPara("rankId");
			String delmsg = getPara("delmsg");

			String sql = "select * from courseplan where class_id=? AND COURSE_TIME= ? AND TIMERANK_ID= ? ";
			Record signCourse = Db.findFirst(sql, classId, courseTime, rankId);
			if (signCourse.getInt("SIGNIN") != 0) {
				renderJson("result", "error"); // 课程已签到，不能取消
			} else {
				String str = "update courseplan set del_msg = ? where class_id = ? AND COURSE_TIME= ? AND TIMERANK_ID=? ";
				Db.update(str, delmsg, classId, courseTime, rankId);
				String sql1 = "insert into courseplan_back SELECT * from  courseplan where class_id=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql1, getPara("classId"), getPara("courseTime"), getPara("rankId"));
				String sql2 = "update courseplan_back set update_time=now() where class_id=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql2, getPara("classId"), getPara("courseTime"), getPara("rankId"));
				String sql3 = "delete from  courseplan where class_id=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql3, getPara("classId"), getPara("courseTime"), getPara("rankId"));
				/*
				 * Integer useCourse = CoursePlan.getUseCourse(stuId); //
				 * 查询已排课节数
				 * Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ",
				 * useCourse, stuId);
				 */
				List<Record> list = Db.find("SELECT * FROM courseplan where class_id=? AND COURSE_TIME=? AND TIMERANK_ID=? ", classId, courseTime, rankId);
				for (Record rec : list) {
					Integer stu_id = rec.getInt("STUDENT_ID");
					Integer useCourse = CoursePlan.getUseCourse(stu_id); // 查询已排课节数
					Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ", useCourse, stu_id); // 更新account表中的已排课记录
				}
				renderJson("result", "1");
			}
		} catch (Exception ex) {
			renderJson("result", "failed");
			ex.printStackTrace();
			logger.error(ex);
		}

	}

	public void delTeacherCoursePlan() {
		try {

			Integer tId = Integer.parseInt(getPara("tId"));
			String courseTime = getPara("courseTime");
			String rankId = getPara("rankId");
			String delmsg = getPara("delmsg");

			String sql = "select * from courseplan where TEACHER_ID=? AND COURSE_TIME= ? AND TIMERANK_ID= ? ";
			int size = CoursePlan.coursePlan.find(sql, tId, courseTime, rankId).size();
			Record signCourse = Db.findFirst(sql, tId, courseTime, rankId);
			if (signCourse.getInt("SIGNIN") != 0) {
				renderJson("result", "error"); // 课程已签到，不能取消
			} else {
				String str = "update courseplan set del_msg = ? where TEACHER_ID = ? AND COURSE_TIME= ? AND TIMERANK_ID=? ";
				Db.update(str, delmsg, tId, courseTime, rankId);
				String sql1 = "insert into courseplan_back SELECT * from  courseplan where TEACHER_ID=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql1, getPara("tId"), getPara("courseTime"), getPara("rankId"));
				String sql2 = "update courseplan_back set update_time=now() where TEACHER_ID=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql2, getPara("tId"), getPara("courseTime"), getPara("rankId"));
				String sql3 = "delete from  courseplan where TEACHER_ID=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql3, getPara("tId"), getPara("courseTime"), getPara("rankId"));
				if (size == 1) {
					accountService.accountManage(Constants.ACCOUNT_CANCEL_COURSE, signCourse.getInt("STUDENT_ID").toString(), null, null, getSysuserId()
							.toString(), null, signCourse.getInt("id").toString(), null);
				}
				/*
				 * Integer useCourse = CoursePlan.getUseCourse(stuId); //
				 * 查询已排课节数
				 * Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ",
				 * useCourse, stuId);
				 */
				List<Record> list = Db.find("SELECT * FROM courseplan where TEACHER_ID=? AND COURSE_TIME=? AND TIMERANK_ID=? ", tId, courseTime, rankId);
				for (Record rec : list) {
					Integer stu_id = rec.getInt("STUDENT_ID");
					Integer useCourse = CoursePlan.getUseCourse(stu_id); // 查询已排课节数
					Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ", useCourse, stu_id); // 更新account表中的已排课记录
				}
				renderJson("result", "1");
			}
		} catch (Exception ex) {
			renderJson("result", "failed");
			ex.printStackTrace();
			logger.error(ex);
		}

	}

	public void checkCoursePlanTime() {
		JSONObject json = new JSONObject();
		String planId = getPara("planId");
		CoursePlan cp = CoursePlan.coursePlan.findById(planId);
		if (Integer.parseInt(cp.get("PLAN_TYPE").toString()) == 0) {
			String time = cp.get("course_time").toString().substring(0, 10);
			String tr = TimeRank.dao.findById(cp.get("timerank_id")).get("rank_name").toString().substring(0, 5);
			Date datetime = ToolDateTime.getDate(time + " " + tr + ":00");
			Date nowdate = ToolDateTime.getDate(ToolDateTime.dateToDateString(new Date()));
			long min = (datetime.getTime() - nowdate.getTime()) / (1000 * 60);
			Organization org = Organization.dao.findById(1);
			if (min > (Double.parseDouble(org.get("basic_cancelcourseplanmaxtime").toString()) * 60)) {
				json.put("nums", "1");
			} else {
				json.put("nums", "0");
			}
		} else {
			json.put("nums", "1");
		}
		renderJson(json);
	}

	public void delCoursePlan() {
		JSONObject json = new JSONObject();
		try {
			String planId = getPara("planId");
			String delreason = getPara("delmsg");
			String enforce = getPara("enforce");
			if (!ToolString.isNull(planId)) {
				json = courseplanService.deleteCoursePlan(Integer.parseInt(planId), getSysuserId(), delreason, enforce);
				if (json.get("code").equals("1")) {
					Record cp = Db.findFirst("select * from courseplan_back where id = ? ", planId);
					json.put("plan", cp);
				}
			} else {
				json.put("code", "0");
				json.put("msg", "课程记录不存在");
			}
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	}

	/**
	 * 多选删除课程
	 */
	public void deleteCoursePlans() {
		JSONObject json = new JSONObject();
		try {
			String ids = getPara("str");
			boolean flag = courseplanService.isDeleteMultipleChoice(ids, getSysuserId());
			if (flag) {
				json.put("code", 1);
			} else {
				json.put("code", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 强制删除排课
	 */
	public void enforceDelCoursePlan() {
		try {
			String planId = getPara("planId");
			String kengId = getPara("kengId");// 接收传递的课程信息
			String strMsg = getPara("strMsg");
			if (!ToolString.isNull(kengId)) {
				CoursePlan plan = CoursePlan.coursePlan.findById(Integer.parseInt(planId));
				String[] st = kengId.split("_");
				// Integer roomIndex = Integer.parseInt(st[1]) + 1;//教室序号
				String courseTime = st[2];// 课程时间2014-5-08
				String rankId = st[3];// 上课时间段id 2
				String sql = "select TEACHER_ID from courseplan where id=? ";
				Record record = Db.findFirst(sql, planId);
				Integer teacher_id = record.getInt("TEACHER_ID");// 教师id
				String sql1 = "INSERT INTO courseplan_back (\n" + "	Id,\n" + "	COURSE_ID,\n" + "	STUDENT_ID,\n" + "	TEACHER_ID,\n" + "	CREATE_TIME,\n"
						+ "	UPDATE_TIME,\n" + "	SORT_NUM,\n" + "	ROOM_ID,\n" + "	TIMERANK_ID,\n" + "	COURSE_TIME,\n" + "	SUBJECT_ID,\n" + "	STATE,\n"
						+ "	REMARK,\n" + "	PLAN_TYPE,\n" + "	KNOWLEDGE_NAMES,\n" + "	TEACHER_PINGLUN,\n" + "	STUDENT_PINGLUN,\n" + "	SIGNIN,\n"
						+ "	LATE_TIME,\n" + "	CAMPUS_ID,\n" + "	class_id,\n" + "	del_msg\n" + ") SELECT\n" + "	cp.Id,\n" + "	cp.COURSE_ID,\n"
						+ "	cp.STUDENT_ID,\n" + "	cp.TEACHER_ID,\n" + "	cp.CREATE_TIME,\n" + "	NOW(),\n" + "	cp.SORT_NUM,\n" + "	cp.ROOM_ID,\n"
						+ "	cp.TIMERANK_ID,\n" + "	cp.COURSE_TIME,\n" + "	cp.SUBJECT_ID,\n" + "	cp.STATE,\n" + "	cp.REMARK,\n" + "	cp.PLAN_TYPE,\n"
						+ "	cp.KNOWLEDGE_NAMES,\n" + "	cp.TEACHER_PINGLUN,\n" + "	cp.STUDENT_PINGLUN,\n" + "	cp.SIGNIN,\n" + "	cp.LATE_TIME,\n"
						+ "	cp.CAMPUS_ID,\n" + "	cp.class_id,\n" + "	?\n" + "FROM\n" + "	courseplan cp\n" + "WHERE\n" + "	cp.TEACHER_ID =?\n"
						+ "AND cp.COURSE_TIME =?\n" + "AND cp.TIMERANK_ID =?";
				Db.update(sql1, strMsg, teacher_id, courseTime, rankId);
				String sql3 = "delete from  courseplan where TEACHER_ID=? AND COURSE_TIME=? AND TIMERANK_ID=? ";
				Db.update(sql3, teacher_id, courseTime, rankId);
				List<Record> list = Db.find("SELECT * FROM courseplan where TEACHER_ID=? AND COURSE_TIME=? AND TIMERANK_ID=? ", teacher_id, courseTime, rankId);
				for (Record rec : list) {
					Integer stu_id = rec.getInt("STUDENT_ID");
					Integer useCourse = CoursePlan.getUseCourse(stu_id); // 查询已排课节数
					Db.update("UPDATE account SET COURSE_USENUM=? WHERE Id=? ", useCourse, stu_id); // 更新account表中的已排课记录
				}
				Integer banid = plan.getInt("class_id");
				if ("0".equals(banid + "")) {// 1对1退费
					accountService.accountManage(Constants.ACCOUNT_CANCEL_COURSE, plan.getInt("student_id").toString(), null, null, getSysuserId().toString(),
							null, planId, null);
				}
				renderJson("result", "ok");
			} else {
				renderJson("result", "notexist"); // 课程不存在
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 查询已取消排课列表
	 */
	public void queryDelCoursePlan() {
		Integer pageNumber = (getParaToInt(0) == null || getParaToInt(0) < 1) ? 1 : getParaToInt(0);
		Page<CoursePlan> page = CoursePlan.queryDelCoursePlan(pageNumber, 15);
		List<CoursePlan> list = page.getList();
		Integer getTotalPage = page.getTotalPage();
		Integer getTotalRow = page.getTotalRow();
		setAttr("list", list);
		setAttr("pageNumber", pageNumber);
		setAttr("totalPage", getTotalPage);
		setAttr("totalRow", getTotalRow);
		renderJsp("/course/findDelCoursePlan.jsp");
	}

	public void getTeacherByIdForTimeRank() {
		try {
			String teacherId = getPara("teacherId");
			String time = getPara("time") + " 00:00:00";
			String sql = "select time_rank.id rankId,time_rank.rank_name rankName,courseplan.id planId from time_rank "
					+ "left join courseplan on   courseplan.course_time='" + time + "' and courseplan.teacher_id=" + teacherId
					+ "  and time_rank.id=courseplan.timerank_id ";
			renderJson("ranks", Db.find(sql));
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 获取老师课程统计
	 */
	public void getCourseCount() {
		try {
			String teacherId = getPara("teacherId");
			setAttr("teacherId", teacherId);
			String teacherSql = ToolString.isNull(teacherId) ? "" : " and TEACHER_ID=" + teacherId;
			String studentName = ToolString.isNull(getPara("studentName")) ? "" : getPara("studentName");
			setAttr("studentName", studentName);
			String campusidSql = "";
			SysUser sysuser = SysUser.dao.findById(getSysuserId());
			if (ToolString.isNull(getPara("campus"))) {
				if (!Role.isAdmins(sysuser.getStr("roleids"))) {
					campusidSql = " and campus.Id in (" + AccountCampus.dao.getCampusIdsByAccountId(getSysuserId()) + ")";
				}
			} else {
				campusidSql = " and campus.Id in (" + getPara("campus") + ")";
			}
			if (!Role.isAdmins(sysuser.getStr("roleids"))) {
				setAttr("campus", Campus.dao.getCampusIdsIn(AccountCampus.dao.getCampusIdsByAccountId(getSysuserId())));
			} else {
				setAttr("campus", Campus.dao.find("select * from campus"));
			}
			setAttr("campusid", getPara("campus"));
			String startTime = ToolString.isNull(getPara("date1")) ? ToolDateTime.getMonthFirstDayYMD(new Date()) : getPara("date1");
			setAttr("date1", startTime);
			String endTime = ToolString.isNull(getPara("date2")) ? ToolDateTime.getCurDate() : getPara("date2");
			setAttr("date2", endTime);
			String sqlStr1 = "select date_format(course_time,'%Y-%m-%d') as course_time,iscancel,teacherhour, campus.CAMPUS_NAME, teahcer.real_name as teacherName,student.real_name as studentName,course.course_name,time_rank.RANK_NAME as courseRankTime, time_rank.class_hour ";
			String sqlCount = "select sum(class_hour) plancount ";
			String sql = " from courseplan "
					+ "inner join account teahcer on courseplan.TEACHER_ID=teahcer.id    inner join account student on courseplan.STUDENT_ID=student.id "
					+ "left join course on courseplan.course_id=course.id "
					+ "left join subject  on courseplan.SUBJECT_ID=subject.id LEFT JOIN time_rank ON courseplan.TIMERANK_ID=time_rank.Id "
					+ " left join campus on campus.Id = student.campusid "
					+ "where student.state = 0 and courseplan.class_id=0 and courseplan.state=0 and courseplan.PLAN_TYPE=0 AND COURSE_TIME<='" + endTime
					+ "' and COURSE_TIME>='" + startTime + "' " + teacherSql + campusidSql + " and student.real_name like '%" + studentName + "%'";
			String sql2 = " from courseplan "
					+ "inner join account teahcer on courseplan.TEACHER_ID=teahcer.id    inner join account student on courseplan.STUDENT_ID=student.id "
					+ "left join course on courseplan.course_id=course.id "
					+ "left join subject  on courseplan.SUBJECT_ID=subject.id LEFT JOIN time_rank ON courseplan.TIMERANK_ID=time_rank.Id "
					+ " left join campus on campus.Id = student.campusid "
					+ "where student.state = 2 and courseplan.class_id!=0 and  courseplan.PLAN_TYPE=0 AND COURSE_TIME<='" + endTime + "' and COURSE_TIME>='"
					+ startTime + "' " + teacherSql + campusidSql + " and student.real_name like '%" + studentName + "%'";
			String sql3 = "select id,real_name,STATE from  account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0 ORDER BY STATE,REAL_NAME";
			setAttr("teachers", Db.find(sql3));

			double count1 = 0L;
			double count2 = 0L;
			List<Record> plan1 = Db.find(sqlCount + sql);
			for (Record rec1 : plan1) {
				count1 += rec1.getBigDecimal("plancount") == null ? 0 : rec1.getBigDecimal("plancount").doubleValue();
			}
			List<Record> plan2 = Db.find(sqlCount + sql2);
			for (Record rec2 : plan2) {
				count2 += rec2.getBigDecimal("plancount") == null ? 0 : rec2.getBigDecimal("plancount").doubleValue();
			}
			setAttr("plan1", count1);
			setAttr("plan2", count2);
			String querysql = sqlStr1 + sql + " union " + sqlStr1 + sql2 + " ORDER BY COURSE_TIME,courseRankTime";
			setAttr("list", Db.find(querysql));
			renderJsp("/course/courseCount.jsp");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public void toCourseCount() {
		try {
			String sql = "select id,real_name from  account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM 	pt_role WHERE numbers = 'admins'), CONCAT(',', roleids) ) > 0";
			setAttr("teachers", Db.find(sql));
			renderJsp("/course/courseCount.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 查询课表热按月
	 */
	public void courseSortListForMonth() {
		try {
			List<Map<String, String>> hourlist = new ArrayList<Map<String, String>>();
			for (int i = 1; i < 24; i++) {
				Map<String, String> hourmap = new HashMap<String, String>();
				String key = i + "";
				hourmap.put("key", key);
				hourmap.put("value", key);
				hourlist.add(hourmap);
			}
			List<Map<String, String>> minlist = new ArrayList<Map<String, String>>();
			for (int i = 0; i < 6; i++) {
				Map<String, String> minmap = new HashMap<String, String>();
				String key = i + "0";
				minmap.put("key", key);
				minmap.put("value", key);
				minlist.add(minmap);
			}
			setAttr("hour", hourlist);
			setAttr("min", minlist);
			setAttr("starthour", getPara("starthour"));
			setAttr("startmin", getPara("startmin"));
			setAttr("endhour", getPara("endhour"));
			setAttr("endmin", getPara("endmin"));

			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String banciName = getPara("banciName");
			setAttr("banciName", banciName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			List<TimeRank> tr = TimeRank.dao.getTimeRank();
			setAttr("timelist", tr);
			setAttr("timerankid", getPara("timerankid"));
			List<Campus> campuslist = Campus.dao.getCampusByLoginUser(getSysuserId());
			setAttr("campuslist", campuslist);
			setAttr("campusid", getPara("campusid"));
			setAttr("date1", getPara("date1"));
			setAttr("date2", getPara("date2"));
			renderJsp("/course/courseSort_month.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 课程安排
	 */
	public void curriculumArrangement() {
		renderJsp("/course/courseplan_month.jsp");
	}

	public void courseSortListForMonthGetJsonOld() {
		try {
			Integer sysuserId = getSysuserId();
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			String teacherIds = Teachergroup.dao.getGroupMembersId(sysuserId.toString());
			Integer campusId = sysuser.getInt("campusid");
			String campus = getPara("campusid");
			String startDate = getPara("startTime");
			String endDate = getPara("endTime");
			String starthour = StringUtils.isEmpty(getPara("starthour")) ? "00" : getPara("starthour");
			String endhour = StringUtils.isEmpty(getPara("endhour")) ? "23" : getPara("endhour");
			String startmin = StringUtils.isEmpty(getPara("startmin")) ? "00" : getPara("startmin");
			String endmin = StringUtils.isEmpty(getPara("endmin")) ? "59" : getPara("endmin");
			StringBuffer timeids = TimeRank.dao.getTimeIdsByTime(starthour, startmin, endhour, endmin);
			String banciName = getPara("banciName");
			String studentName = getPara("studentName");
			String teacherName = getPara("teacherName");
			String flag = getPara("flag");

			StringBuffer campusSql = new StringBuffer("");
			StringBuffer whereSql = new StringBuffer("");
			String sbStr = timeids.toString();
			if (!ToolString.isNull(sbStr)) {
				timeids.insert(0, "(");
				timeids.append(")");
				whereSql.append(" and time_rank.id in ( ").append(sbStr).append(" ) ");
				campusSql.append(" and time_rank.id in ( ").append(sbStr).append(" ) ");
			}
			if (!ToolString.isNull(campus)) {
				whereSql.append(" and courseplan.CAMPUS_ID = ").append(campus);
				campusSql.append(" and courseplan.CAMPUS_ID != ").append(campus).append(" AND PLAN_TYPE = 2 ");
			}
			if (!ToolString.isNull(studentName)) {
				whereSql.append(" and stu.real_name ='").append(studentName.trim()).append("' ");
				campusSql.append(" and stu.real_name ='").append(studentName.trim()).append("' ");
			} else {
				whereSql.append(" and (courseplan.class_id=0 or (courseplan.class_id<>0 and stu.state=2 )) ");
			}
			if (!ToolString.isNull(teacherName)) {// 老师名称
				Teacher teacher = Teacher.dao.getTeacherByName(teacherName.trim());
				whereSql.append(" and courseplan.teacher_id =").append(teacher.getPrimaryKeyValue());
				campusSql.append(" and courseplan.teacher_id =").append(teacher.getPrimaryKeyValue());
			}
			if (!StringUtils.isEmpty(banciName)) {
				whereSql.append(" and class_order.classNum like'%").append(banciName).append("%' ");
				campusSql.append(" and class_order.classNum like'%").append(banciName).append("%' ");
			}
			if (!Role.isAdmins(sysuser.getStr("roleids"))) {
				String fzxqids = Campus.dao.IsCampusFzr(sysuserId);
				if (fzxqids != null) {
					whereSql.append(" and courseplan.CAMPUS_ID IN(").append(fzxqids).append(")");
				} else {
					boolean f = false;
					if (Role.isJxzj(sysuser.getStr("roleids"))) {
						f = true;
					}
					if (Role.isTeacher(sysuser.getStr("roleids")) && !f) {// 老师
						sysuser.set("kcuserid", 1).update();
						if (flag.equals("0")) {
							whereSql.append(" AND courseplan.teacher_id IN (").append(teacherIds).append(")");
							campusSql.append(" AND courseplan.teacher_id IN (").append(teacherIds).append(")");
						}
						if (flag.equals("1")) {
							whereSql.append(" and courseplan.teacher_id=").append(sysuserId);
							campusSql.append(" and courseplan.teacher_id=").append(sysuserId);
						}
					} else if (Role.isStudent(sysuser.getStr("roleids"))) {// 学生登录
						whereSql.append(" and courseplan.student_id=").append(sysuserId);
						campusSql.append(" and courseplan.student_id=").append(sysuserId);
					} else if (Role.isDudao(sysuser.getStr("roleids"))) {// 督导
						String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
						if (campusids != null) {
							whereSql.append(" and courseplan.CAMPUS_ID IN(").append(campusids).append(",").append(campusId).append(")");
						} else {
							whereSql.append(" and courseplan.CAMPUS_ID IN(0)");
						}
						campusSql.append(" AND stu.SUPERVISOR_ID=").append(sysuser.getPrimaryKeyValue());
					} else if (Role.isJiaowu(sysuser.getStr("roleids"))) {// 教务
						String campusids = Campus.dao.IsCampusJwFzr(sysuserId);
						if (campusids != null) {
							whereSql.append(" and courseplan.CAMPUS_ID IN(").append(campusids).append(",").append(campusId).append(")");
							campusSql.append(" and courseplan.CAMPUS_ID IN(").append(campusids).append(",").append(campusId).append(")");
						} else {
							if (campusId != null) {
								whereSql.append(" AND courseplan.CAMPUS_ID=").append(campusId);
								campusSql.append(" AND courseplan.CAMPUS_ID=").append(campusId);
							}
						}
					} else if (Role.isKcgw(sysuser.getStr("roleids"))) {// 课程顾问
						String campusids = Campus.dao.IsCampusKcFzr(sysuserId);
						if (campusids != null) {
							whereSql.append(" and courseplan.CAMPUS_ID IN(").append(campusids).append(")");
						} else {
							campusSql.append(" AND stu.kcuserid=").append(sysuser.getPrimaryKeyValue());
							campusSql.append(" AND stu.kcuserid=").append(sysuser.getPrimaryKeyValue());
							if (campusId != null) {
								whereSql.append(" AND courseplan.CAMPUS_ID=").append(campusId);
							}
						}
					} else if (Role.isShichang(sysuser.getStr("roleids"))) {// 市场
						String campusids = Campus.dao.IsCampusScFzr(sysuserId);
						if (campusids != null) {
							whereSql.append(" and courseplan.CAMPUS_ID IN(").append(campusids).append(")");
						} else {
							whereSql.append(" AND stu.scuserid=").append(sysuser.getPrimaryKeyValue());
						}
					}
				}
			}

			StringBuffer str = new StringBuffer("[");
			StringBuffer sbf = new StringBuffer();
			String header = " select * from (";
			String last = "  ) a ORDER BY a.course_time asc, a.trrankname asc  ";
			sbf.append("select  distinct concat(IF(courseplan.iscancel=1,'《已取消课》<br>',''),'授课:一对一',IF(courseplan.netCourse=1,'(网络课)',''),'<br>课程:' ,IFNULL(course.course_name,'无'),'(补排)','<br>场地:',IFNULL(campus.campus_name,''),classroom.name,'<br>日期:','courseplan_course_time','<br>时段:',time_rank.RANK_NAME,'<br>老师:',"
					+ " IFNULL(teacher.real_name,''),'<br>班型:','class_type_name',IF(courseplan.class_id=0,CONCAT('<br>学生:',stu.real_name),''),IF(courseplan.class_id!=0,CONCAT('<br>班次:',class_order.classNum),'')) as  title,IFNULL( CONCAT( IF(LENGTH(courseplan.startrest)<=4,CONCAT('0',courseplan.startrest),courseplan.startrest), '-', courseplan.endrest ), time_rank.RANK_NAME ) trrankname, courseplan.course_time,"
					+ " classroom.name classroomname,campus.campus_name,campus.campustype,time_rank.RANK_NAME,courseplan.ROOM_ID,courseplan.campus_id,courseplan.teacher_id, courseplan.student_id,courseplan.course_id, courseplan.class_id, courseplan.remark, date_format(courseplan.course_time,'%Y,(%c-1),%d') as start,courseplan.rechargecourse "
					+ ",courseplan.plan_type as planType,courseplan.id,courseplan.startrest,courseplan.endrest, courseplan.netCourse as netCourse, courseplan.signin as signin,courseplan.TEACHER_PINGLUN as teacherPinglun,courseplan.TIMERANK_ID,date_format(courseplan.course_time,'%Y-%m-%d') kcrq "
					+ " from  courseplan left join course on courseplan.course_id = course.id  "
					+ " left join campus on campus.id=courseplan.campus_id left join classroom on classroom.id=courseplan.room_id "
					+ " left join time_rank  on time_rank.id=courseplan.timerank_id  "
					+ " left join class_order on class_order.id = courseplan.class_id "
					+ " left join account stu on stu.id=courseplan.student_id  "
					+ " left join account teacher on teacher.id=courseplan.teacher_id  "
					+ " where courseplan.course_time>='" + startDate + "' and courseplan.course_time<= '" + endDate + "' ");
			String sql3 = sbf.toString() + campusSql;
			sbf.append(whereSql);
			List<CoursePlan> list = null;
			if (StringUtils.isEmpty(campus)) {
				list = CoursePlan.coursePlan.find(header + sbf.toString() + last);
			} else {
				list = CoursePlan.coursePlan.find(header + " ( " + (sbf.append(" )").append(" UNION ALL ").append("( ").append(sql3).append(" )").toString())
						+ last);
			}
			if (list != null && list.size() > 0) {
				for (int i = 0, h = list.size(); i < h; i++) {
					CoursePlan clist = list.get(i);
					String title = clist.getStr("title");
					title = title.replace("courseplan_course_time", clist.getStr("kcrq"));
					String student = null;
					StringBuffer kcsj = new StringBuffer(clist.getStr("kcrq")).append(" ")
							.append(clist.getStr("RANK_NAME").substring(0, clist.getStr("RANK_NAME").indexOf("-"))).append(":00");
					String nowDateTime = ToolDateTime.getCurDateTime();
					long ss = ToolDateTime.compareDateStr(kcsj.toString(), nowDateTime);
					if (!ToolString.isNull(campus)) {
						title = clist.getInt("rechargecourse") == 1 ? title : title.replace("(补排)", "");
					} else {
						title = clist.getBoolean("rechargecourse") ? title : title.replace("(补排)", "");
					}
					if (clist.getInt("planType").toString().equals("0")) {// 课程
						if (clist.getInt("class_id") == 0) {
							title = title.replace("<br>班型:class_type_name", "");
						} else {
							ClassOrder classorder = ClassOrder.dao.getClassOrderDetailMsg(clist.getInt("class_id").toString());
							title = title.replace("class_type_name", classorder.getStr("ctname")).replace("授课:一对一<br>", "");
							student = CourseOrder.dao.findStudentNamesByClassIds(classorder.getPrimaryKeyValue().toString());
						}
					}
					if (clist.getInt("planType").toString().equals("1")) {// 模考
						title = "【模考】<br>" + title.replace("<br>老师:", "").replace("授课:一对一<br>", "").replace("<br>班型:class_type_name", "");
						if (clist.getInt("class_id") != 0) {
							ClassOrder classorder = ClassOrder.dao.getClassOrderDetailMsg(clist.getInt("class_id").toString());
							student = CourseOrder.dao.findStudentNamesByClassIds(classorder.getPrimaryKeyValue().toString());
						}
					}
					if (clist.getInt("planType").toString().equals("2")) {// 休息
						String recharge = "";
						if (!ToolString.isNull(campus)) {
							title = clist.getInt("rechargecourse") == 1 ? title : title.replace("(补排)", "");
						} else {
							title = clist.getBoolean("rechargecourse") ? title : title.replace("(补排)", "");
						}
						Teacher teachercp = Teacher.dao.findById(clist.getInt("teacher_id"));
						if (clist.getStr("startrest").equals("00:00") && clist.getStr("endrest").equals("23:59")) {
							title = "老师:" + teachercp.getStr("REAL_NAME") + "<br>" + "类型:休息" + recharge + "<br>" + "时段:" + "全天";
						} else {
							title = "老师:" + teachercp.getStr("REAL_NAME") + "<br>" + "类型:休息" + recharge + "<br>" + "时段:" + clist.getStr("startrest") + "-"
									+ clist.getStr("endrest");
						}
					}

					String locale = getPara("_locale");
					if (StrKit.notBlank(locale)) { // change locale, write
													// cookie
						setCookie("_locale", locale, Const.DEFAULT_I18N_MAX_AGE_OF_COOKIE);
					} else { // get locale from cookie and use the default
								// locale if it is null
						locale = getCookie("_locale");
						if (StrKit.isBlank(locale))
							locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
					}
					if (locale.equals("en_US")) {
						title = title.replace("授课:一对一<br>", "ONE-ON-ONE TUTORING<br>").replace("课程", "SUBJECT").replace("补排", "Re-enter")
								.replace("已取消课", "Canceled").replace("场地", "LOCATION").replace("日期", "DATE").replace("时段", "TIME").replace("老师", "TUTOR")
								.replace("学生", "STUDENT");
					}

					str.append(",{title:'").append(title).append("',start:new Date(").append(clist.getStr("start")).append(")");
					str.append(",classId:'").append(clist.getInt("class_id")).append("'");
					str.append(",student:'").append(student).append("'");
					str.append(",msg:'").append(clist.getStr("remark")).append("'");
					str.append(",courseplanId:'").append(clist.getInt("id")).append("'");
					str.append(",name:'").append(clist.getStr("classroomname")).append("'");
					str.append(",TIMERANK_ID:'").append(clist.getInt("TIMERANK_ID")).append("'");
					str.append(",datetime:'").append(clist.getStr("START")).append("'");
					str.append(",campus_name:'").append(clist.getStr("campus_name")).append("'");
					str.append(",campustype:'").append(clist.getInt("campustype")).append("'");
					str.append(",netCourse:'").append(clist.getInt("netCourse")).append("'");
					str.append(",plantype:'").append(clist.getInt("planType")).append("'");
					if (Role.isStudent(sysuser.getStr("roleids"))) {
						if (ss > 0) {
							str.append(",signin:'1'");
							str.append(",teacherPinglun:'y'}");
						} else {
							str.append(",signin:'0'");
							str.append(",teacherPinglun:'n'}");
						}
					} else {
						str.append(",signin:'").append(clist.getInt("signin")).append("'");
						str.append(",teacherPinglun:'").append(clist.getStr("teacherPinglun")).append("'}");
					}

				}
			}
			str.append("]");
			renderText(str.toString().replaceFirst(",", ""));
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	}

	public void courseSortListForMonthGetJson() {
		try {
			Integer sysuserId = getSysuserId();
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			String roleids = sysuser.getStr("roleids")+"";
			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("teacherIds", Teachergroup.dao.getGroupMembersId(getSysuserId().toString()));
			if (StringUtils.isEmpty(getPara("campusid"))) {
				queryParams.put("campusId", AccountCampus.dao.getCampusIdsByAccountId(sysuserId));
			} else {
				queryParams.put("campusId", getPara("campusid"));
			}
			queryParams.put("startDate", getPara("startTime"));
			queryParams.put("endDate", getPara("endTime"));
//			queryParams.put("starthour", StringUtils.isEmpty(getPara("starthour")) ? "00" : getPara("starthour"));
//			queryParams.put("endhour", StringUtils.isEmpty(getPara("endhour")) ? "23" : getPara("endhour"));
//			queryParams.put("startmin", StringUtils.isEmpty(getPara("startmin")) ? "00" : getPara("startmin"));
//			queryParams.put("endmin", StringUtils.isEmpty(getPara("endmin")) ? "59" : getPara("endmin"));
			queryParams.put("banciName", getPara("banciName"));
			queryParams.put("studentName", getPara("studentName"));
			queryParams.put("teacherName", getPara("teacherName"));
			if (Role.isStudent(sysuser.getStr("roleids"))){
				queryParams.put("studentId", sysuserId.toString());
			}else{
				if(Role.isTeacher(roleids)&&"2,".equals(roleids)){
					queryParams.put("teacherId", sysuserId.toString());
				}
			}
				
			String locale = getPara("_locale");
			if (StrKit.notBlank(locale)) { // change locale, write cookie
				setCookie("_locale", locale, Const.DEFAULT_I18N_MAX_AGE_OF_COOKIE);
			} else { // get locale from cookie and use the default locale if it is null
				locale = getCookie("_locale");
				if (StrKit.isBlank(locale))
					locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
			}
			queryParams.put("language", locale);
			String queryResult = courseplanService.queryCoursePlanForCalendar(queryParams);
			renderText(queryResult);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
	}

	public void queryCoursePlansForToday() {
		try {
			String studentId = "";
			String teacherId = "";
			SysUser user = SysUser.dao.findById(getPara("loginId"));
			if (Role.isTeacher(user.getStr("roleids"))) {
				teacherId = getPara("loginId");
			}
			if (Role.isStudent(user.getStr("roleids"))) {
				studentId = getPara("loginId");
			}
			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (studentName != null && !studentName.equals("")) {// 根据学员名称
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + studentName + "'");
				if (account != null)
					studentId = account.getInt("ID") + "";
			}
			if (teacherName != null && !teacherName.equals("")) {// 老师名称
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + teacherName + "'");
				if (account != null)
					teacherId = account.getInt("ID") + "";
			}
			String sql = "select * from campus where id=2";
			List<Record> campusRecord = Db.find(sql);
			setAttr("campus", campusRecord);
			sql = "select * from classroom where campus_id=1";// 取出教室最多的中关村校区
			List<Record> classRooms = Db.find(sql);
			setAttr("classRooms", classRooms);
			sql = "select * from time_rank";
			List<TimeRank> timeRankList = TimeRank.dao.find(sql);
			String coursePlanSql = "SELECT IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,"
					+ "cp.class_id,r.Id as RANK_ID,r.RANK_NAME,c.COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ," + "cp.STATE,"
					+ "(" + "SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id)"
					+ ") as CAMPUS_NAME,cp.REMARK,cp.PLAN_TYPE" + " FROM courseplan cp " + " LEFT JOIN course c ON cp.COURSE_ID=c.Id"
					+ " LEFT JOIN account as t ON cp.TEACHER_ID=t.Id" + " LEFT JOIN account as s ON cp.STUDENT_ID=s.Id"
					+ " LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id" + " LEFT JOIN classroom m ON cp.ROOM_ID=m.Id "
					+ " LEFT JOIN class_order ON cp.class_id = class_order.id " + " LEFT JOIN class_type ON class_order.classtype_id = class_type.id "
					+ " WHERE s.state <> 1 and cp.COURSE_TIME='" + date1 + "'";
			if (studentId.length() > 0)
				coursePlanSql += " AND cp.STUDENT_ID=" + studentId;
			if (teacherId.length() > 0)
				coursePlanSql += " AND cp.TEACHER_ID=" + teacherId;
			List<CoursePlan> cplist = CoursePlan.coursePlan.find(coursePlanSql);
			Map<String, Object> timeRankMap = new LinkedHashMap<String, Object>();
			for (TimeRank timeRank : timeRankList) {// 循环时段
				Integer timeRankId = timeRank.getInt("ID");
				String timeRankName = timeRank.getStr("RANK_NAME");
				Map<String, Object> campusMap = new LinkedHashMap<String, Object>();
				for (Object campusObj : campusRecord) {// 循环校区
					Record campus = (Record) campusObj;
					String campusName = campus.get("CAMPUS_NAME");// 校区名称
					List<CoursePlan> coursePlanRoomList = new ArrayList<CoursePlan>();
					for (Object obj : classRooms) {// 循环教室
						Record record = (Record) obj;
						String classRoomName = record.get("NAME");
						boolean hasPlan = false;
						for (CoursePlan cp : cplist) {// 循环课程安排
							String roomName = cp.getStr("ROOM_NAME");
							Integer rId = cp.getInt("RANK_ID");
							String courseCampusName = cp.getStr("CAMPUS_NAME");
							String student = "";
							if (timeRankId.equals(rId) && campusName.equals(courseCampusName)) {
								if (classRoomName.equals(roomName)) {
									student += CoursePlan.getNames(roomName, rId, courseCampusName, date1);
									if (cp.getInt("PLAN_TYPE") == 1) {
										cp.put("teach_type", "【模考】");
										cp.put("T_NAME", "");
									}
									cp.put("S_NAME", student);
									if (cp.getInt("PLAN_TYPE") != 2) {
										coursePlanRoomList.add(cp);
									}
									hasPlan = true;
									break;
								} else {
									continue;
								}
							} else {
								continue;
							}
						}
						if (!hasPlan) {
							coursePlanRoomList.add(null);
						}
					}
					campusMap.put(campusName, coursePlanRoomList);
				}
				timeRankMap.put(timeRankName, campusMap);
			}
			setAttr("timeRankMap", timeRankMap);
			setAttr("timeRanks", Db.find(sql));
			sql = "select  date_format(course_time,'%Y年%c月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1 and courseplan.state=0  ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));
			renderJsp("/course/courseplan_today.jsp");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public void queryCoursePlansForZGC() {
		try {
			String studentId = "";
			String teacherId = "";
			SysUser user = SysUser.dao.findById(getPara("loginId"));
			if (Role.isTeacher(user.getStr("roleids"))) {
				teacherId = getPara("loginId");
			}
			if (Role.isStudent(user.getStr("roleids"))) {
				studentId = getPara("loginId");
			}
			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (studentName != null && !studentName.equals("")) {// 根据学员名称
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + studentName + "'");
				if (account != null)
					studentId = account.getInt("ID") + "";
			}
			if (teacherName != null && !teacherName.equals("")) {// 老师名称
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + teacherName + "'");
				if (account != null)
					teacherId = account.getInt("ID") + "";
			}
			String sql = "select * from campus where id=1";
			List<Record> campusRecord = Db.find(sql);
			setAttr("campus", campusRecord);
			sql = "select * from classroom where campus_id=1";// 取出教室最多的中关村校区
			List<Record> classRooms = Db.find(sql);
			setAttr("classRooms", classRooms);
			sql = "select * from time_rank";
			List<TimeRank> timeRankList = TimeRank.dao.find(sql);
			String coursePlanSql = "SELECT IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,"
					+ "cp.class_id,r.Id as RANK_ID,r.RANK_NAME,c.COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ," + "cp.STATE,"
					+ "(" + "SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id)"
					+ ") as CAMPUS_NAME,cp.REMARK,cp.PLAN_TYPE" + " FROM courseplan cp " + " LEFT JOIN course c ON cp.COURSE_ID=c.Id"
					+ " LEFT JOIN account as t ON cp.TEACHER_ID=t.Id" + " LEFT JOIN account as s ON cp.STUDENT_ID=s.Id"
					+ " LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id" + " LEFT JOIN classroom m ON cp.ROOM_ID=m.Id "
					+ " LEFT JOIN class_order ON cp.class_id = class_order.id " + " LEFT JOIN class_type ON class_order.classtype_id = class_type.id "
					+ " WHERE s.state <> 1 and cp.COURSE_TIME='" + date1 + "'";
			if (studentId.length() > 0)
				coursePlanSql += " AND cp.STUDENT_ID=" + studentId;
			if (teacherId.length() > 0)
				coursePlanSql += " AND cp.TEACHER_ID=" + teacherId;
			List<CoursePlan> cplist = CoursePlan.coursePlan.find(coursePlanSql);
			Map<String, Object> timeRankMap = new LinkedHashMap<String, Object>();
			for (TimeRank timeRank : timeRankList) {// 循环时段
				Integer timeRankId = timeRank.getInt("ID");
				String timeRankName = timeRank.getStr("RANK_NAME");
				Map<String, Object> campusMap = new LinkedHashMap<String, Object>();
				for (Object campusObj : campusRecord) {// 循环校区
					Record campus = (Record) campusObj;
					String campusName = campus.get("CAMPUS_NAME");// 校区名称
					List<CoursePlan> coursePlanRoomList = new ArrayList<CoursePlan>();
					for (Object obj : classRooms) {// 循环教室
						Record record = (Record) obj;
						String classRoomName = record.get("NAME");
						boolean hasPlan = false;
						for (CoursePlan cp : cplist) {// 循环课程安排
							String roomName = cp.getStr("ROOM_NAME");
							Integer rId = cp.getInt("RANK_ID");
							String courseCampusName = cp.getStr("CAMPUS_NAME");
							String student = "";
							if (timeRankId.equals(rId) && campusName.equals(courseCampusName)) {
								if (classRoomName.equals(roomName)) {
									student += CoursePlan.getNames(roomName, rId, courseCampusName, date1);
									if (cp.getInt("PLAN_TYPE") == 1) {
										cp.put("teach_type", "【模考】");
										cp.put("T_NAME", "");
									}
									cp.put("S_NAME", student);
									coursePlanRoomList.add(cp);
									hasPlan = true;
									break;
								} else {
									continue;
								}
							} else {
								continue;
							}
						}
						if (!hasPlan) {
							coursePlanRoomList.add(null);
						}
					}
					campusMap.put(campusName, coursePlanRoomList);
				}
				timeRankMap.put(timeRankName, campusMap);
			}
			setAttr("timeRankMap", timeRankMap);
			setAttr("timeRanks", Db.find(sql));
			sql = "select  date_format(course_time,'%Y年%c月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1 and courseplan.state=0  ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));
			renderJsp("/course/courseplan_today_zgc.jsp");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 开放查课接口
	 */
	public void allCourseplanToday() {
		try {
			String studentId = "";
			String teacherId = "";
			String banci = getPara("banci"); // 班次编号
			setAttr("banci", banci);
			String studentName = getPara("studentName");
			setAttr("studentName", studentName);
			String teacherName = getPara("teacherName");
			setAttr("teacherName", teacherName);
			Integer campusId = getParaToInt("campusId") == null ? 0 : getParaToInt("campusId");
			setAttr("campusId", campusId);
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			if (ToolString.isNull(date1) || ToolString.isNull(date2)) {
				Date date = new Date();// 取时间
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(date);
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date1 = formatter.format(date);
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
				date2 = formatter.format(date);
			}
			setAttr("date1", date1);
			setAttr("date2", date2);
			if (studentName != null && !studentName.equals("")) {// 根据学员名称
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + studentName + "'");
				if (account != null)
					studentId = account.getInt("ID") + "";
			}
			if (teacherName != null && !teacherName.equals("")) {// 老师名称
				Account account = Account.dao.findFirst("SELECT * FROM account WHERE REAL_NAME='" + teacherName + "'");
				if (account != null)
					teacherId = account.getInt("ID") + "";
			}
			String sql = "select * from campus ";
			if (campusId != null && campusId != 0) {
				sql += "where id=" + campusId;
			}
			List<Record> campusRecord = Db.find(sql);
			setAttr("campus", Db.find("select * from campus "));
			sql = "select * from classroom where campus_id=1";// 取出教室最多的中关村校区
			List<Record> classRooms = Db.find(sql);
			setAttr("classRooms", classRooms);
			sql = "select * from time_rank";
			List<TimeRank> timeRankList = TimeRank.dao.find(sql);
			String coursePlanSql = "SELECT cp.Id, IF (cp.class_id <> 0,CONCAT('班型:',class_type.`name`,'<br>班次:',class_order.classNum),'授课:一对一') AS teach_type,"
					+ "cp.class_id,r.Id as RANK_ID,r.RANK_NAME,c.COURSE_NAME,s.REAL_NAME as S_NAME,t.REAL_NAME as T_NAME,m.`NAME` as ROOM_NAME ," + "cp.STATE,"
					+ "(" + "SELECT p.CAMPUS_NAME FROM campus p WHERE p.Id=(SELECT classroom.CAMPUS_ID FROM classroom WHERE classroom.Id=m.Id)"
					+ ") as CAMPUS_NAME,cp.REMARK,cp.PLAN_TYPE,class_order.classNum,class_type.`name` AS type_name " + " FROM courseplan cp "
					+ " LEFT JOIN course c ON cp.COURSE_ID=c.Id" + " LEFT JOIN account as t ON cp.TEACHER_ID=t.Id"
					+ " LEFT JOIN account as s ON cp.STUDENT_ID=s.Id" + " LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id"
					+ " LEFT JOIN classroom m ON cp.ROOM_ID=m.Id " + " LEFT JOIN class_order ON cp.class_id = class_order.id "
					+ " LEFT JOIN class_type ON class_order.classtype_id = class_type.id " + " WHERE s.state <> 1 and cp.COURSE_TIME =? ";
			if (studentId.length() > 0) {
				coursePlanSql += " AND cp.STUDENT_ID=" + studentId;
			}
			if (teacherId.length() > 0) {
				coursePlanSql += " AND cp.TEACHER_ID=" + teacherId;
			}
			if (campusId != null && campusId != 0) {
				coursePlanSql += " and cp.CAMPUS_ID=" + campusId;
			}
			if (banci != null && !banci.equals(""))// 班次
				coursePlanSql += " and (class_order.classNum like'%" + banci + "%' or class_order.classNum like'%" + banci + "%')";
			List<Object> rankList = new ArrayList<Object>();
			List<CoursePlan> getCourseDate = CoursePlan.getCourseDate(date1, date2);
			Map<String, Object> timeMap = new LinkedHashMap<String, Object>();
			for (CoursePlan cpdate : getCourseDate) {
				String courseDate = cpdate.getStr("COURSE_TIME");
				List<CoursePlan> cplist = null;
				cplist = CoursePlan.coursePlan.find(coursePlanSql, courseDate);
				Map<String, Object> timeRankMap = new LinkedHashMap<String, Object>();
				for (TimeRank timeRank : timeRankList) {// 循环时段
					Integer timeRankId = timeRank.getInt("ID");
					String timeRankName = timeRank.getStr("RANK_NAME");
					Map<String, Object> campusMap = new LinkedHashMap<String, Object>();
					for (Object campusObj : campusRecord) {// 循环校区
						Record campus = (Record) campusObj;
						String campusName = campus.get("CAMPUS_NAME");// 校区名称
						List<CoursePlan> coursePlanRoomList = new ArrayList<CoursePlan>();
						for (Object obj : classRooms) {// 循环教室
							Record record = (Record) obj;
							String classRoomName = record.get("NAME");
							boolean hasPlan = false;
							for (CoursePlan cp : cplist) {// 循环课程安排
								String roomName = cp.getStr("ROOM_NAME");
								Integer rId = cp.getInt("RANK_ID");
								String courseCampusName = cp.getStr("CAMPUS_NAME");
								String student = "";
								if (timeRankId.equals(rId) && campusName.equals(courseCampusName)) {
									if (classRoomName.equals(roomName)) {
										student += CoursePlan.getNames(roomName, rId, courseCampusName, courseDate);
										if (cp.getInt("PLAN_TYPE") == 1) {
											cp.put("teach_type", "【模考】");
											cp.put("T_NAME", "");
										}
										cp.put("S_NAME", student);
										coursePlanRoomList.add(cp);
										hasPlan = true;
										break;
									} else {
										continue;
									}
								} else {
									continue;
								}
							}
							if (!hasPlan) {
								coursePlanRoomList.add(null);
							}
						}
						campusMap.put(campusName, coursePlanRoomList);
					}
					timeRankMap.put(timeRankName, campusMap);
				}
				timeMap.put(courseDate, timeRankMap);
			}
			setAttr("timeMap", timeMap);
			setAttr("rankList", rankList);
			setAttr("timeRanks", Db.find(sql));
			sql = "select  date_format(course_time,'%Y年%c月%d日') as courseTime,date_format(course_time,'%Y-%m-%d') as simCourseTime from courseplan where 1=1 and courseplan.state=0 ";
			if (date1 != null && !date1.equals(""))// 开始时间段
				sql += " and courseplan.course_time>='" + date1 + "'";
			else
				sql += " and course_time>=date_format(now(),'%Y-%m-%d')";
			if (date2 != null && !date2.equals(""))// 结束时间段
				sql += " and courseplan.course_time<='" + date2 + "'";
			sql += " group by course_time";
			setAttr("days", Db.find(sql));
			renderJsp("/course/allCourseplanToday.jsp");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 数据统计（按月）
	 */
	public void getCourseCountByMonth() {
		String queryType;
		try {
			queryType = getPara("queryType");
			String queryMonth = getPara("queryMonth");
			DateFormat dd = new SimpleDateFormat("yyyy-MM");
			if (ToolString.isNull(queryMonth))
				queryMonth = dd.format(new Date());
			if (ToolString.isNum(queryType))
				queryType = "teacher";
			setAttr("queryType", queryType);
			setAttr("queryMonth", queryMonth);
			setAttr("endDate", ToolDateTime.getMonthLastDayYMD(ToolDateTime.getDate(queryMonth + "-01 00:00:00")));
			String ydyjoinon = null;
			String ydygroup = null;

			List<Campus> campus = Campus.dao.find("select * from campus");
			setAttr("campus", campus);
			String campusid = "";
			if ("student".equals(queryType)) {
				campusid = getPara("campusid");
				ydyjoinon = "cp.STUDENT_ID=a.Id ";
				ydygroup = "cp.STUDENT_ID";
			} else if ("teacher".equals(queryType)) {
				ydyjoinon = "cp.TEACHER_ID=a.Id";
				ydygroup = "cp.TEACHER_ID";
			}
			setAttr("campusid", StringUtils.isEmpty(campusid) ? "-1" : campusid);
			String ydysql = "SELECT a.Id,a.REAL_NAME username,campus.CAMPUS_NAME,DATE_FORMAT(cp.COURSE_TIME,'%Y-%m') coursemonth,count(1) vip,"
					+ "  SUM(t.class_hour)  vipzks " + " FROM courseplan cp LEFT JOIN account a ON " + ydyjoinon
					+ " left join campus on campus.Id = a.campusid " + "LEFT JOIN time_rank t ON cp.TIMERANK_ID=t.Id\n"
					+ "WHERE cp.iscancel=0 and cp.PLAN_TYPE=0 AND DATE_FORMAT(cp.COURSE_TIME,'%Y-%m')='" + queryMonth + "' AND cp.STATE=0 AND cp.class_id=0\n";
			if (!StringUtils.isEmpty(campusid)) {
				ydysql += " and  a.campusid =" + campusid;
			}
			ydysql += " GROUP BY " + ydygroup;

			String xbjoinon = null;
			String xbgroup = null;
			if ("student".equals(queryType)) {
				campusid = getPara("campusid");
				xbjoinon = "  left join class_order co on cp.class_id = co.id " + "  left join account_banci ab on ab.banci_id = co.id  "
						+ "  left join account a on ab.account_id = a.id ";
				xbgroup = "a.id";
			} else if ("teacher".equals(queryType)) {
				xbjoinon = "LEFT JOIN account a ON cp.TEACHER_ID=a.Id";
				xbgroup = "cp.TEACHER_ID";
			}
			String xbsql = "SELECT a.Id,a.REAL_NAME username,campus.CAMPUS_NAME,DATE_FORMAT(cp.COURSE_TIME,'%Y-%m') coursemonth,count(1) xb,SUM(t.class_hour) xbzks "
					+ " FROM courseplan cp  "
					+ xbjoinon
					+ " left join campus on campus.Id = a.campusid "
					+ "LEFT JOIN time_rank t ON cp.TIMERANK_ID=t.Id\n"
					+ "WHERE cp.PLAN_TYPE=0 AND DATE_FORMAT(cp.COURSE_TIME,'%Y-%m')='" + queryMonth + "'  AND cp.class_id!=0 and a.STATE <> 2 ";
			if (!StringUtils.isEmpty(campusid)) {
				xbsql += " and  a.campusid =" + campusid;
			}
			xbsql += " GROUP BY " + xbgroup;
			List<Record> ydyList = Db.find(ydysql);
			List<Record> xbList = Db.find(xbsql);
			for (Record x : ydyList) {
				CoursePlan cp = CoursePlan.coursePlan.getDelHourByTeacherIDs(x.getInt("id").toString(), queryMonth);
				double s = x.get("vipzks") == null ? 0 : Float.parseFloat(x.get("vipzks").toString())
						+ Double.parseDouble(cp.get("delkeshi") == null ? "0" : cp.get("delkeshi").toString());
				int ss = x.get("vip") == null ? 0 : Integer.parseInt(x.get("vip").toString())
						+ Integer.parseInt(cp.get("nums") == null ? "0" : cp.get("nums").toString());
				x.set("vipzks", s);
				x.set("vip", ss);
			}
			for (Record r : xbList) {
				boolean nohas = true;
				int xbid = r.getInt("id");
				for (Record x : ydyList) {
					int ydyid = x.getInt("id");
					if (xbid == ydyid) {
						x.set("xb", r.getLong("xb"));
						x.set("xbzks", r.getBigDecimal("xbzks"));
						nohas = false;
						break;
					}
				}
				if (nohas) {
					ydyList.add(r);
				}
			}
			float[] sum = new float[4];
			for (Record x : ydyList) {
				sum[0] += x.get("vip") == null ? 0 : Integer.parseInt(x.get("vip").toString());
				sum[1] += x.get("vipzks") == null ? 0 : Float.parseFloat(x.get("vipzks").toString());
				if (x.getLong("xb") != null) {
					sum[2] += x.getLong("xb") == null ? 0 : x.getLong("xb");
				}
				if (x.get("xbzks") != null) {
					sum[3] += x.get("xbzks") == null ? 0 : Float.parseFloat(x.get("xbzks").toString());
				}
			}
			setAttr("sum", sum);
			setAttr("list", ydyList);
			renderJsp("/course/courseCountByMonth.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	/**
	 * 查询老师的所有学生 -- 考试系统用
	 */
	public void queryStudentForTeacher() {
		String teacherId = getPara("teacherId");
		String sql = "SELECT * FROM courseplan\n" + "LEFT JOIN account ON courseplan.STUDENT_ID = account.Id\n"
				+ "WHERE courseplan.TEACHER_ID = ? AND courseplan.STATE=0 AND account.STATE=0\n" + "GROUP BY STUDENT_ID ";
		List<Record> record = Db.find(sql, teacherId);
		String userIds = "";
		for (Record rec : record) {
			userIds += rec.get("STUDENT_ID") + ",";
		}
		if (userIds != "") {
			userIds = userIds.substring(0, userIds.length() - 1);
		}
		renderJson("userIds", userIds);
	}

	/**
	 * 查询课程记录
	 */
	public void queryTeacherCoursePlanDetail() {
		try {
			long startmin = ToolDateTime.getDateByTime();
			String page = getPara("page") != null ? getPara("page") : "1";// 查询第几页
			String teacherName = getPara("teacherName");
			String studentName = getPara("studentName");
			String starttime = getPara("starttime");
			String endtime = getPara("endtime");
			Integer sysuserId = getSysuserId();
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			String campusId = getPara("campusId");
			setAttr("campusId", campusId);
			String teacherId = "";
			String studentId = "";
			int pageSize = 20;
			if (!ToolString.isNull(studentName)) {
				Account student = Account.dao.findByName(studentName);
				if (student == null) {
					setAttr("errormsg", "学生" + studentName + "不存在，或姓名有误，请输入完整姓名");
					renderJsp("/account/teacher_record.jsp");
				}
				studentId = student.getInt("id") + "";
				setAttr("studentName", studentName);
			}

			if (!ToolString.isNull(teacherName)) {
				Account teacher = Account.dao.findByName(teacherName);
				if (teacher == null) {
					setAttr("errormsg", "老师" + teacherName + "不存在，或姓名有误，请输入完整姓名");
					renderJsp("/account/teacher_record.jsp");
				}
				teacherId = teacher.getInt("id") + "";
				setAttr("teacherName", teacherName);
			}

			if (Role.isTeacher(sysuser.getStr("roleids"))) {// 老师
				teacherId = sysuserId + "";
			} else if (Role.isStudent(sysuser.getStr("roleids"))) {
				studentId = sysuserId + "";
			}

			StringBuffer condition = new StringBuffer();
			if (!Role.isStudent(sysuser.getStr("roleids"))) {// 学生查询全部
				if (ToolString.isNull(studentName) && ToolString.isNull(teacherName) && ToolString.isNull(starttime) && ToolString.isNull(endtime)) {
					Date day = new Date();
					if (ToolString.isNull(starttime))
						starttime = ToolDateTime.getMonthFirstDayYMD(day);
					if (ToolString.isNull(endtime))
						endtime = ToolDateTime.getCurDate();
				}
				if (!ToolString.isNull(starttime))
					condition.append(" AND cp.COURSE_TIME>='" + starttime + "' ");
				if (!ToolString.isNull(endtime))
					condition.append(" AND cp.COURSE_TIME<='" + endtime + "' ");
			}
			if (!Role.isAdmins(sysuser.getStr("roleids"))) {
				String fzxqids = Campus.dao.IsCampusFzr(sysuserId);
				if (fzxqids != null) {
					condition.append(" and cp.CAMPUS_ID IN(" + fzxqids + ")");
				} else {
					if (Role.isTeacher(sysuser.getStr("roleids")) || teacherId.length() > 0) {// 老师
						condition.append(" AND cp.TEACHER_ID=" + teacherId);
					} else if (Role.isStudent(sysuser.getStr("roleids"))) {// 学生登录
						condition.append(" AND cp.STUDENT_ID=" + studentId);
					} else if (Role.isDudao(sysuser.getStr("roleids")) || Role.isKcgw(sysuser.getStr("roleids")) || Role.isJiaowu(sysuser.getStr("roleids"))) {
						String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
						if (campusids == null) {
							condition.append(" and cp.CAMPUS_ID IN(0)");
						} else {
							condition.append(" and cp.CAMPUS_ID IN(" + campusids + ")");
						}
					} else if (Role.isShichang(sysuser.getStr("roleids"))) {// 市场
						String campusids = Campus.dao.IsCampusScFzr(sysuserId);
						if (campusids == null) {
							condition.append(" AND s.scuserid=" + sysuser.getPrimaryKeyValue());
						}
					}
				}
			}
			if (!Role.isStudent(sysuser.getStr("roleids")) && !StringUtils.isEmpty(studentId)) {
				condition.append(" AND cp.STUDENT_ID=" + studentId);
			}
			if (!StringUtils.isEmpty(campusId)) {
				condition.append(" AND cp.CAMPUS_ID=" + campusId);
			}
			String sqlSelect = " select *  ";
			String sqlFrom = " FROM (( SELECT c.Id COURSE_ID,cp.Id cpid,c.COURSE_NAME,t.REAL_NAME TNAME, "
					+ "  s.REAL_NAME SNAME,DATE_FORMAT(cp.COURSE_TIME,'%Y-%m-%d') COURSE_TIME, " + " r.RANK_NAME,campus.CAMPUS_NAME ,co.classNum ,tg.*"
					+ " FROM courseplan cp  " + " left join  class_order  co on cp.class_id = co.id  "
					+ " LEFT JOIN  account_banci  ab  on ab.banci_id = co.id " + " left join account s  on ab.account_id = s.Id "
					+ " left join teachergrade tg on tg.courseplan_id = cp.Id " + " LEFT JOIN account t ON cp.TEACHER_ID=t.Id "
					+ " LEFT JOIN course c ON cp.COURSE_ID=c.Id " + " LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id "
					+ " LEFT JOIN campus ON cp.CAMPUS_ID = campus.Id WHERE cp.STATE=0  and s.STATE =0 " + condition + ") " + " union all "
					+ " (SELECT c.Id COURSE_ID,cp.Id cpid,c.COURSE_NAME,t.REAL_NAME TNAME, "
					+ " s.REAL_NAME SNAME,DATE_FORMAT(cp.COURSE_TIME,'%Y-%m-%d') COURSE_TIME, " + " r.RANK_NAME,campus.CAMPUS_NAME,0,tg.* "
					+ " FROM courseplan cp  " + " LEFT JOIN account t ON cp.TEACHER_ID=t.Id  " + " LEFT JOIN account s ON cp.STUDENT_ID=s.Id  "
					+ " left join teachergrade tg on tg.courseplan_id = cp.Id " + " LEFT JOIN course c ON cp.COURSE_ID=c.Id  "
					+ " LEFT JOIN time_rank r ON cp.TIMERANK_ID=r.Id  " + " LEFT JOIN campus ON cp.CAMPUS_ID = campus.Id WHERE cp.STATE=0 and cp.class_id = 0 "
					+ condition + "))a   ";
			String orderby = "  ORDER BY a.course_time DESC ";
			System.out.println(ToolDateTime.getDateByTime() - startmin);
			Page<CoursePlan> coursePlans = CoursePlan.coursePlan.paginate(Integer.parseInt(page), pageSize, sqlSelect, sqlFrom + orderby);
			if (coursePlans != null) {
				setAttr("coursePlans", coursePlans);
				setAttr("page", coursePlans.getPageNumber());
				setAttr("pageSize", coursePlans.getPageSize());
				setAttr("pages", coursePlans.getTotalPage());
				setAttr("count", coursePlans.getTotalRow());
				setAttr("teacherId", teacherId);
				setAttr("starttime", starttime);
				setAttr("endtime", endtime);
			} else {
				setAttr("errormsg", "无排课信息！");
			}
			setAttr("campus", Campus.dao.getCampus());
			System.out.println(ToolDateTime.getDateByTime() - startmin);
			renderJsp("/account/teacher_record.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	public void getStudentMessage() {
		try {
			String studentId = getPara("studentId");
			String startTime = ToolString.isNull(getPara("startTime")) ? ToolDateTime.getMonthFirstDayYMD(new Date()) : getPara("startTime");
			String endTime = ToolString.isNull(getPara("endTime")) ? ToolDateTime.getMonthLastDayYMD(ToolDateTime.getDate(startTime + " 00:00:00"))
					: getPara("endTime");
			String sql = "SELECT cp.STUDENT_ID AS studentId,\n" + "	s.real_name AS realname,\n" + "	t.real_name as teachername,\n"
					+ "	DATE_FORMAT(cp.course_time,'%Y-%m-%d') AS coursetime,\n" + "	cp.TEACHER_PINGLUN,\n" + "	c.COURSE_NAME AS courseName,\n"
					+ "	cp.signin,\n" + "	c.id AS courseId,\n" + "	tr.rank_name AS ranktime,\n" + "	cp.class_id,\n"
					+ "	(CASE WHEN ISNULL(tg.HOMEWORK) THEN 0 ELSE 1 END) HOMEWORK\n" + "FROM courseplan cp\n" + "LEFT JOIN account s ON cp.STUDENT_ID=s.Id\n"
					+ "LEFT JOIN course c ON cp.COURSE_ID=c.Id\n" + "LEFT JOIN time_rank tr ON cp.TIMERANK_ID=tr.Id\n"
					+ "LEFT JOIN account t ON cp.TEACHER_ID=t.Id\n" + "LEFT JOIN (SELECT * FROM teachergrade WHERE ROLE=2) tg ON cp.Id=tg.COURSEPLAN_ID\n"
					+ "WHERE cp.STATE = 0 AND cp.STUDENT_ID = " + studentId + "\n" + "AND cp.course_Time >= '" + startTime + "'\n" + "AND cp.course_Time <= '"
					+ endTime + "'\n" + "ORDER BY cp.COURSE_TIME DESC";
			List<Record> list = Db.find(sql);
			setAttr("list", list);
			setAttr("startTime", startTime);
			setAttr("endTime", endTime);
			renderJsp("/account/month_census_student.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看老师的上课信息
	 * */
	public void getTeacherMessage() {
		try {
			String teacherId = getPara("teacherId");
			String startTime = ToolString.isNull(getPara("startTime")) ? ToolDateTime.getMonthFirstDayYMD(new Date()) : getPara("startTime");
			String endTime = ToolString.isNull(getPara("endTime")) ? ToolDateTime.getMonthLastDayYMD(ToolDateTime.getDate(startTime + " 00:00:00"))
					: getPara("endTime");
			String sql = "SELECT DISTINCT\n"
					+ "	account.id AS teacherId,\n"
					+ "	account.real_name AS realname,\n"
					+ "	date_format(\n"
					+ "		courseplan.course_time,\n"
					+ "		'%Y-%m-%d'\n"
					+ "	) AS coursetime,\n"
					+ "	courseplan.TEACHER_PINGLUN,\n"
					+ "	course.COURSE_NAME AS courseName,\n"
					+ "	courseplan.signin,courseplan.isovertime,\n"
					+ "	courseplan.KNOWLEDGE_NAMES,\n"
					+ "	course.id AS courseId,\n"
					+ "	time_rank.rank_name AS ranktime,\n"
					+ "	time_rank.Id AS timeid,\n"
					+ "	courseplan.class_id,\n"
					+ "	(CASE WHEN ISNULL(tg.HOMEWORK) THEN 0 ELSE 1 END) HOMEWORK\n"
					+ "FROM\n"
					+ "	courseplan\n"
					+ "LEFT JOIN account ON courseplan.teacher_id = account.id\n"
					+ "LEFT JOIN course ON course.id = courseplan.COURSE_ID\n"
					+ "LEFT JOIN time_rank ON courseplan.TIMERANK_ID = time_rank.id\n"
					+ "LEFT JOIN (SELECT * FROM teachergrade WHERE ROLE=1) tg ON courseplan.Id=tg.COURSEPLAN_ID"
					+ " where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',',  account.roleids) ) > 0  and courseplan.state=0 and  account.id="
					+ teacherId;
			if (startTime != null && startTime != "") {
				sql += " and courseplan.course_Time >= '" + startTime + "'";
			}
			if (endTime != null && endTime != "") {
				sql += " and courseplan.course_Time <= '" + endTime + "'";
			}
			sql += " order by courseplan.COURSE_TIME desc ";
			List<Record> list = Db.find(sql);
			setAttr("list", list);
			setAttr("startTime", startTime);
			setAttr("endTime", endTime);
			renderJsp("/account/month_census.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void siGninCourese() {
		try {
			String courseplan_id = getPara("courseplan_id");
			CoursePlan coursePlan = CoursePlan.coursePlan.findById(courseplan_id);
			Boolean b = true;
			String mes = "";
			String cousrTime = ToolDateTime.dateToDateString(coursePlan.getDate("COURSE_TIME"), ToolDateTime.pattern_ymd);
			String TIMENAME = TimeRank.dao.getTimeRankNameById(coursePlan.getInt("timerank_id")+"");
			String hour = TIMENAME.substring(0, 2);
			String minute = TIMENAME.substring(3, 5);
			String latehour = TIMENAME.substring(6, 8);
			String lateminute = TIMENAME.substring(9, 11);
			int between = Integer.parseInt(latehour) * 60 + Integer.parseInt(lateminute) - Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
			HttpServletRequest request = this.getRequest();
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			Teacher teacher = Teacher.dao.findById(getSysuserId());
			Campus campus = Campus.dao.findById(coursePlan.get("CAMPUS_ID"));
			String work = teacher.get("tworktype");// 获取老师是兼职还是全职
			boolean fullsign = campus.get("fullsign");
			boolean partsign = campus.get("partsign");
			boolean limitip = campus.get("limitip");
			Integer netCourse = coursePlan.getInt("netCourse");
			String sql = "select name from ipaddress where status = 1 and name like '%" + ip + "%' limit 1";
			Record records = Db.findFirst(sql);

			if (!limitip || ((records != null || netCourse == 1) && limitip)) {
				sql = "select * from courseplan where id =" + courseplan_id;
				Record record = Db.findFirst(sql);
				if (work.equals("1") && !fullsign) {
					renderJson("message", "campusfullno");
				} else if (work.equals("0") && !partsign) {
					renderJson("message", "campuspartno");
				} else if (record.getInt("signin") == 0) {
					Date date = new Date();
					String datetime = new SimpleDateFormat("yyyy-MM-dd").format(date);// 格式化成字符串
					if (cousrTime.equals(datetime)) {
						int nowHour = date.getHours();
						int nowMinute = date.getMinutes();
						int different = (nowHour * 60 + nowMinute) - (Integer.parseInt(hour) * 60 + Integer.parseInt(minute));
						int latedifferent = (nowHour * 60 + nowMinute) - (Integer.parseInt(latehour) * 60 + Integer.parseInt(lateminute));
						Organization o = Organization.dao.findById(1);
						int max = 30;
						int min = 15;
						if (o.getInt("basic_maxregistration") != 0) {
							max = o.getInt("basic_maxregistration");
						}
						if (o.getInt("basic_minregistration") != 0) {
							min = o.getInt("basic_minregistration");
						}
						if (different < -min && different > -max) {
							sql = "update courseplan set signin =1 where id=" + courseplan_id;
							Db.update(sql);
							if (record != null) {
								List<Record> list11 = Db.find("SELECT * FROM courseplan WHERE COURSE_TIME=? AND TIMERANK_ID=? AND TEACHER_ID=? ",
										record.get("COURSE_TIME"), record.getInt("TIMERANK_ID"), record.getInt("TEACHER_ID"));
								if (list11.size() > 0) {
									for (Record rec : list11) {
										Db.update("update courseplan set signin =1 where id=? ", rec.getInt("id"));
									}
								}
							}
							renderJson("message", "ok");
							b = false;
						} else if (different < -max) {
							renderJson("message", "notearly");
							b = false;
						} else if (latedifferent < 0 && latedifferent > -between) {
							mes = "late";
							sql = "update courseplan set signin=2 , late_time=" + latedifferent + " where id =" + courseplan_id;
							Db.update(sql);
							if (record != null) {
								List<Record> list11 = Db.find("SELECT * FROM courseplan WHERE COURSE_TIME=? AND TIMERANK_ID=? AND TEACHER_ID=? ",
										record.get("COURSE_TIME"), record.getInt("TIMERANK_ID"), record.getInt("TEACHER_ID"));
								if (list11.size() > 0) {
									for (Record rec : list11) {
										Db.update("update courseplan set signin=2 , late_time=" + latedifferent + " where id=? ", rec.getInt("id"));
									}
								}
							}
							Map<String, Object> map = new HashMap<String, Object>();
							map.put(mes, different);
							renderJson("message", map);
							b = false;
						} else {
							if (b) {
								renderJson("message", "short");
								b = false;
							}
						}
					}
					if (b) {
						renderJson("message", "NotDay");
						b = false;
					}
				} else {
					if (b) {
						renderJson("message", "not");
					}
				}
			} else {
				renderJson("message", "no");
			}
		} catch (Exception ex) {
			logger.error("执行siGninCourese出现错误");
			ex.printStackTrace();
		}
	}

	/**
	 * 《点名》获取该班课下的学生姓名
	 */
	public void callNameMessage() {
		try {
			String courseplanid = getPara("coursePlanId");
			setAttr("refresh",getPara("refresh"));
			CoursePlan cp = CoursePlan.coursePlan.findById(courseplanid);
			setAttr("cp", cp);
			List<Student> slist = new ArrayList<Student>();
			StringBuffer sf = new StringBuffer();
			if (cp.getInt("class_id") != 0) {
				slist = Student.dao.findByOrderCourseMessage(courseplanid);
			} else {
				slist = Student.dao.findByOneCourseMessage(courseplanid);
			}
			for (Student s : slist) {
				Teachergrade sign = Teachergrade.teachergrade.findByCoursePlanIdAndStudentid(s.getInt("id").toString(), s.getInt("studentid").toString());
				if (sign != null) {
					s.put("sign", sign.getInt("singn"));
					s.put("remark", sign.getStr("singnremark"));
				}
				sf.append(s.getInt("studentid")).append(",");
			}
			setAttr("studentids", sf.toString());
			setAttr("stu", slist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJsp("/course/layer_callName.jsp");
	}

	/**
	 * 保存点名结果
	 */
	public void saveCallNameMessage() {
		JSONObject json = new JSONObject();
		try {
			String cpid = getPara("cpid");
			String[] remark = getPara("remark").replace("|", ",").split(",");
			String[] singn = getPara("singn").split(",");
			String[] studentids = getPara("studentids").split(",");
			for (int i = 0; i < studentids.length; i++) {
				Teachergrade tg = Teachergrade.teachergrade.findByCoursePlanIdAndStudentid(cpid, studentids[i]);
				if (tg == null) {
					Teachergrade t = new Teachergrade();
					t.set("courseplan_id", cpid).set("studentid", studentids[i]).set("singn", singn[i]).set("singnremark", remark[i])
							.set("demohour", new Date()).save();
					Db.update("insert into teachergrade_update select * from teachergrade where id =? ", t.getPrimaryKeyValue());
				} else {
					tg.set("courseplan_id", cpid).set("studentid", studentids[i]).set("singn", singn[i]).set("singnremark", remark[i]).update();
				}
				accountService.consumeCourse(Integer.parseInt(cpid), Integer.parseInt(studentids[i]), getSysuserId(),1);
			}
			json.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}

	/**
	 * 统计签到信息
	 */
	public void getSignMessage() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String courseplan_id = getPara("courseplan_id");
			if (!ToolString.isNull(courseplan_id)) {
				String sql = "select course_time,teacher_id from courseplan where id=" + courseplan_id;
				Record coursePlan = Db.findFirst(sql);
				int teacher_id = coursePlan.getInt("teacher_id");
				Date courseDate = (Date) coursePlan.get("course_time");
				String minDate = ToolDateTime.getMonthFirstDay(courseDate)+" 00:00:00";
				String maxDate = ToolDateTime.getMonthLastDay(courseDate)+" 23:59:59";
				String Nowdate = ToolDateTime.getCurDate();
				sql = "SELECT COUNT(*) AS courseNum FROM (SELECT * FROM courseplan WHERE TEACHER_ID=" + teacher_id + " and course_time >= '" + minDate + "'"
						+ " and course_time  <= ' " + maxDate + "' GROUP BY COURSE_TIME,CAMPUS_ID,TIMERANK_ID) a ";
				Record record1 = Db.findFirst(sql); // 本月排课
				sql = "SELECT COUNT(*) AS SIGNNUM FROM (SELECT * FROM courseplan WHERE TEACHER_ID=" + teacher_id + " and course_time >= '" + minDate + "'"
						+ " and course_time  <= ' " + maxDate + "' AND SIGNIN=1 GROUP BY COURSE_TIME,CAMPUS_ID,TIMERANK_ID) a ";
				Record record2 = Db.findFirst(sql); // 已签到
				if (Nowdate.compareTo(maxDate) > 0) {
					Nowdate = maxDate;
				}
				String sql2 = "SELECT COUNT(*) AS happen FROM (SELECT * FROM courseplan WHERE TEACHER_ID=" + teacher_id + " and course_time >= '" + minDate
						+ "'" + " and course_time  <= ' " + Nowdate + "' GROUP BY COURSE_TIME,CAMPUS_ID,TIMERANK_ID) a ";
				Record record6 = Db.findFirst(sql2); // 已发生课程
				Float percent = (float) 0;
				if (record6.getNumber("happen").floatValue() != 0) {
					percent = record2.getNumber("SIGNNUM").floatValue() / record6.getNumber("happen").floatValue();
				}
				NumberFormat numFormat = NumberFormat.getNumberInstance();
				numFormat.setMaximumFractionDigits(2);
				String per = numFormat.format(percent * 100) + "%";
				sql = "SELECT COUNT(*) AS LateNUM FROM (SELECT * FROM courseplan WHERE TEACHER_ID=" + teacher_id + " and course_time >= '" + minDate + "'"
						+ " and course_time  <= ' " + maxDate + "' AND SIGNIN=2 GROUP BY COURSE_TIME,CAMPUS_ID,TIMERANK_ID) a ";
				Record record5 = Db.findFirst(sql);
				int num = record5.getNumber("LateNUM").intValue();
				int rec3 = record6.getNumber("happen").intValue() - record2.getNumber("SIGNNUM").intValue();
				map.put("record1", record1);
				map.put("record2", record2);
				map.put("record3", rec3);
				map.put("record4", per);
				map.put("record5", num);
				map.put("record6", record6);
			} else {
				map.put("code", "0");
				map.put("errorMessage", "没有查到相关学生数据");
			}
			renderJson(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getTimeRanks() {
		List<TimeRank> tr = courseService.getTimeRank();
		renderJson("result", tr);
	}

	public void getCourseByStuId() {
		String stuId = getPara("stuId");
		Account list = Account.dao.getAccountById(stuId);
		if (list != null) {
			String sql = "SELECT\n" + "	c.account_id,\n" + "	c.course_id,\n" + "	c.COURSE_NAME,\n"
					+ "	convert(CONCAT(IFNULL(b.count_course,0),' '),char) AS classinfo,\n" + "	IFNULL(b.count_course, 0) yipai,\n" + "	c.lesson_num\n"
					+ "FROM\n" + "	(\n" + "		SELECT\n" + "			uc.account_id,\n" + "			uc.course_id,\n" + "			c.COURSE_NAME,\n" + "			uc.lesson_num\n"
					+ "		FROM\n" + "			user_course uc\n" + "		LEFT JOIN course c ON uc.course_id = c.Id\n" + "		WHERE\n" + "			uc.account_id = ?\n" + "	) c\n"
					+ "LEFT JOIN (\n" + "	SELECT\n" + "		a.COURSE_ID,\n" + "		course.COURSE_NAME,\n" + "		COUNT(*) AS count_course\n" + "	FROM\n" + "		(\n"
					+ "			SELECT\n" + "				cp.COURSE_ID,\n" + "				DATE_FORMAT(cp.COURSE_TIME, '%Y-%m-%d') courseDate,\n" + "				cp.TIMERANK_ID\n"
					+ "			FROM\n" + "				courseplan cp\n" + "			WHERE\n" + "				cp.STUDENT_ID = ? and cp.class_id = 0 and cp.PLAN_TYPE=0 \n" + "			GROUP BY\n"
					+ "				cp.COURSE_ID,\n" + "				DATE_FORMAT(cp.COURSE_TIME, '%Y-%m-%d'),\n" + "				cp.TIMERANK_ID\n" + "		) AS a\n"
					+ "	INNER JOIN course ON course.Id = a.COURSE_ID\n" + "	GROUP BY\n" + "		a.COURSE_ID\n" + ") b ON c.course_id = b.COURSE_ID";
			List<Record> record = Db.find(sql, stuId, stuId);

			Integer countLesson = 0;
			Integer usedLesson = 0;
			for (Record rec : record) {
				usedLesson += rec.getLong("yipai").intValue();
				countLesson += rec.getInt("lesson_num");
			}
			countLesson = list.getInt("COURSE_SUM");

			List<Object> courseList = new ArrayList<Object>();
			Map<Object, Object> map = new HashMap<Object, Object>();
			for (Record rec : record) {
				Map<Object, Object> map1 = new HashMap<Object, Object>();
				map1.put("course_id", rec.get("course_id"));
				map1.put("course_name", rec.get("COURSE_NAME"));
				if (countLesson != 0 && countLesson == usedLesson) {
					map1.put("status", 0);
				} else {
					map1.put("status", 1);
				}
				courseList.add(map1);
			}

			map.put("courseList", courseList);

			renderJson("result", map);

		}
	}

	public void getTeacherByCourseId() {
		JSONObject json = new JSONObject();
		String courseId = getPara("course_id");
		String studentId = getPara("studentid");
		Student student = Student.dao.findById(Integer.parseInt(studentId));
		List<Account> accounts = accountService.getTeacherByCourseId(courseId);
		Course course = Course.dao.findById(courseId);
		String sql = "select TEACHER_ID,CAMPUS_ID from courseplan where STUDENT_ID = " + studentId + " and COURSE_ID= " + courseId
				+ " order by create_time DESC";
		List<CoursePlan> courseplan = CoursePlan.coursePlan.find(sql);
		if (courseplan.size() != 0) {
			json.put("teacherid", courseplan.get(0).get("TEACHER_ID"));
			json.put("schoolid", courseplan.get(0).get("CAMPUS_ID"));
		} else {
			json.put("schoolid", student.getInt("campusid") == null ? 0 : student.getInt("campusid"));
		}
		json.put("teacher", accounts);
		json.put("subjectid", course.getInt("SUBJECT_ID"));
		renderJson(json);
	}

	/**
	 * 签到
	 */
	public void signMessage() {
		String courseplan_id = getPara();
		setAttr("courseplan_id", courseplan_id);
		renderJsp("/course/courseplan_layer_sign.jsp");
	}

	/**
	 * 保存签到信息
	 */
	public void tosaveSign() {
		JSONObject json = new JSONObject();
		String signinperson_id = getSysuserId().toString();
		try {
			String courseplan_id = getPara("courseplan_id");
			String sign = getPara("sign");
			CoursePlan course = CoursePlan.coursePlan.findById(courseplan_id);
			course.set("SIGNIN", sign).set("signinperson_id", signinperson_id).set("signedtime", new Date()).update();
			json.put("code", 1);
			json.put("msg", "签到成功");
		} catch (Exception e) {
			json.put("code", 0);
			json.put("msg", "签到异常");
			e.printStackTrace();
		} finally {
			renderJson(json);
		}

	}

	/**
	 * 补签
	 */
	public void signedCause() {
		String courseplan_id = getPara();
		setAttr("courseplan_id", courseplan_id);
		renderJsp("/course/courseplan_layer_signed.jsp");
	}

	/**
	 * 保存补签信息
	 */
	public void tosaveSignedCause() {
		JSONObject json = new JSONObject();
		String signinperson_id = getSysuserId().toString();
		try {
			String courseplan_id = getPara("courseplan_id");
			String signedcause = getPara("signedcause");
			CoursePlan coursePlan = CoursePlan.coursePlan.findById(courseplan_id);
			String rank_name = CoursePlan.coursePlan.cpIdToGetRankTime(courseplan_id);
			Date rank_time = new SimpleDateFormat("HH:mm").parse(rank_name.substring(5, 10));
			Date nowDate = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(new Date()));
			if (rank_time.getTime() > nowDate.getTime()) {
				coursePlan.set("SIGNIN", 2).set("signinperson_id", signinperson_id).set("signedtime", new Date()).update();
				json.put("code", 1);
				json.put("msg", "课程未结束不能补签");
			} else {
				coursePlan.set("signcause", signedcause).set("signinperson_id", signinperson_id).set("SIGNIN", 3).set("signedtime", new Date()).update();
				json.put("code", 1);
				json.put("msg", "已补签");
			}
		} catch (Exception e) {
			json.put("code", 0);
			json.put("msg", "补签失败");
			e.printStackTrace();
		} finally {
			renderJson(json);
		}
	}

	/**
	 * 时间验证； type:1为补签验证；0为签到验证
	 */
	public void checkSignCourseTime() {
		String id = getPara("id");
		String type = getPara("type");
		Map<String, Object> map = null;
		try {
			if (type.equals("1")) {
				map = courseplanService.confirmCoursePlan(id);
			}
			if (type.equals("0")) {
				map = courseplanService.confirmCoursePlanReadySign(id);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		renderJson(map);
	}

	/**
	 * 超出规定时间删除排课
	 */
	public void fullHourDelCoursePlan() {
		String[] planid = getPara().split(",");
		CoursePlan cp = CoursePlan.coursePlan.findById(planid[0]);
		setAttr("cp", cp);
		setAttr("num", planid[1]);//0是按课程排课页面进来的，1是删除课表页面进来的
		renderJsp("/course/layer_fullHourDelCoursePlan.jsp");
	}

	/**
	 * 老师选择课酬
	 */
	public void checkClassHours() {
		JSONObject json = new JSONObject();
		String planid = getPara("planid");
		try {
			CoursePlan cp = CoursePlan.coursePlan.findById(planid);
			TimeRank hour = TimeRank.dao.findById(cp.get("timerank_id"));
			int s = (int) (Double.parseDouble(hour.get("class_hour").toString()) / 0.5);
			List<Double> list = new ArrayList<Double>();
			double ss = 0.0;
			for (int i = 0; i <= s; i++) {
				list.add(ss);
				ss += 0.5;
			}
			json.put("hour", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 超出规定时间删除排课(选择取消)
	 */
	public void saveFullHourDelCoursePlan() {
		JSONObject json = new JSONObject();
		try {
			String planid = getPara("planid");
			String remark = getPara("remark");
			double hours = Double.parseDouble(getPara("hours"));
			String iscancel = getPara("delcode");
			CoursePlan cp = CoursePlan.coursePlan.findById(planid);
			cp.set("del_msg", remark).set("teacherhour", hours).set("iscancel", iscancel).set("deluserid", getSysuserId()).update();
			//accountService.consumeCourse(cp.getPrimaryKeyValue(), cp.getInt("student_id"), getSysuserId(),1);
			json.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}

	/**
	 * 超出规定时间删除排课(选择移除)
	 */
	public void saveFullHourDelCoursePlans() {
		JSONObject json = new JSONObject();
		try {
			String planId = getPara("planid");
			String delreason = getPara("remark");
			if (!ToolString.isNull(planId)) {
				json = courseplanService.deleteCoursePlan(Integer.parseInt(planId), getSysuserId(), delreason, "yes");
				if (json.get("code").equals("1")) {
					Record cp = Db.findFirst("select * from courseplan_back where id = ? ", planId);
					json.put("plan", cp);
				}
			} else {
				json.put("code", "0");
				json.put("msg", "课程记录不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}

	/**
	 * 简化模式《教师课表信息》 回调函数封装数据形式：Map<日期,教师s(教师：教师, 日期：课程s)>
	 */
	public void simplifiedModeCourse() {
		JSONObject json = new JSONObject();
		try {
			String ids = getPara("ids");
			if (ids != null && !ids.equals("")) {
				ids = ids.substring(1, ids.length()).replace("|", ",");
			}
			String campusid = getPara("campusId");
			String begin = getPara("begin");
			String end = getPara("end");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			List<String> sdate = ToolDateTime.printDay(sf.parse(begin), sf.parse(end));
			Map<String, List<Teacher>> map = new HashMap<String, List<Teacher>>();
			List<Teacher> tlist = Teacher.dao.findByIds(ids);
			for (String date : sdate) {
				String d = date;
				date += "(" + ToolDateTime.getDateInWeek(sf.parse(date), 1) + ")";
				for (Teacher t : tlist) {
					List<CoursePlan> cplist = CoursePlan.coursePlan.findByCampusidAndTeacherids(campusid, ids, d, t.getInt("id"));
					t.put(date, cplist);
				}
				map.put(date, tlist);
			}
			List<TimeRank> trlist = TimeRank.dao.getTimeRank();
			json.put("trlist", trlist);
			json.put("map", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 根据科目ids获取课程
	 */
	public void getCourseBySubjectIds() {
		try {
			String ids = getPara("ids");
			List<Course> clist = Course.dao.getCourseBySubjectIds(ids.replaceAll("\\|", ","));
			renderJson(clist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
