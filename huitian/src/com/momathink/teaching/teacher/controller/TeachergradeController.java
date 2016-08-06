package com.momathink.teaching.teacher.controller;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.account.model.AccountBook;
import com.momathink.sys.account.service.AccountService;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.teacher.model.GradeUpdate;
import com.momathink.teaching.teacher.model.Teachergrade;

@Controller(controllerKey = "/teachergrade")
public class TeachergradeController extends BaseController {
	private static final Logger logger = Logger.getLogger(TeachergradeController.class);
	private AccountService accountService = new AccountService();
	/**
	 * 小班关联学生要上的课程，以备老师评论打分
	 */
	public void add() {
		JSONObject json = new JSONObject();
		json.put("code", 0);//成功
		try {
			// 获取页面数据
			String studentId = getPara("studentId");
			String coursePlanId = getPara("coursePlanId");
			String operateType = getPara("operateType");
			CoursePlan coursePlan = CoursePlan.coursePlan.findById(Integer.parseInt(coursePlanId));
			Integer classOrderId = coursePlan.getInt("class_id");
			Teachergrade teachergrade = Teachergrade.teachergrade.findByCoursePlanIdAndStudentid(coursePlanId, studentId);
			if ("add".equals(operateType)) {
				double banHour = CourseOrder.dao.getBanHour(Integer.parseInt(studentId), classOrderId);// 学生购买的小班课时
				double classHour = TimeRank.dao.getHourById(coursePlan.getInt("TIMERANK_ID"));// 本次要上的课程安排的课时
				double hasHour = Teachergrade.teachergrade.getHasHour(Integer.parseInt(studentId), classOrderId);// 已经安排了的课时总数
				double sumHour = ToolArith.add(classHour, hasHour);// 要给学生安排的总的课时数
				if (ToolArith.compareTo(banHour, sumHour) >= 0) {
					// 判断是否点名或是否已经评价
					if (teachergrade == null) {
						teachergrade = new Teachergrade();
						teachergrade.set("courseplan_id", coursePlanId).set("studentid", studentId).set("role", 1).set("createtime", new Date());
						teachergrade.save();
						Db.update("insert into teachergrade_update select * from teachergrade  where id= ? ", teachergrade.getPrimaryKeyValue());
					}
					accountService.consumeCourse(coursePlan.getPrimaryKeyValue(), Integer.parseInt(studentId), getSysuserId(),0);
				} else {
					json.put("code", 1);
					json.put("msg", "课时数不能大于已购" + banHour + "课时");
				}
			} else {
				if (teachergrade != null) {
					teachergrade.delete();
					Db.deleteById("teachergrade_update", teachergrade.getPrimaryKeyValue());
				}
				AccountBook.dao.deleteByAccountIdAndCoursePlanId(Integer.parseInt(studentId),Integer.parseInt(coursePlanId));
			}
		} catch (Exception e) {
			json.put("code", 1);
			json.put("msg", "操作失败请联系管理员");
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 保存老师为学生打的分数
	 */
	public void saveTeachergrade() {
		JSONObject json = new JSONObject();
		try {
			// 获取页面数据
			String studentid = getPara("studentid");
			String courseplanid = getPara("courseplanid");
			String zhuyili = getPara("zhuyili");
			String lijieli = getPara("lijieli");
			String xuxitaidu = getPara("xuxitaidu");
			String shangcizuoye = getPara("shangcizuoye");

			String bencikecheng = getPara("bencikecheng");
			String bencizuoye = getPara("bencizuoye");
			String question = getPara("question");
			String method = getPara("method");
			String tutormsg = getPara("tutormsg");

			// 获取登陆用户身份用于判断
			Integer sysuserid = getSysuserId();
			SysUser user = SysUser.dao.findById(sysuserid);
			String role = "";

			// 获取课程信息，判断是否在规定时间内
			CoursePlan cp = CoursePlan.coursePlan.getCoursePlanCurrentSaved(courseplanid);

			// 判断是否点名或是否已经评价
			Teachergrade records = Teachergrade.teachergrade.findByCoursePlanIdAndStudentid(courseplanid, studentid);

			if (Role.isStudent(user.getStr("roleids"))) {
				role = "2";
			}
			if (Role.isTeacher(user.getStr("roleids"))) {
				role = "1";
			}
			String cptime = ToolDateTime.getStringTimestamp(cp.getTimestamp("COURSE_TIME"));
			// 查看机构配置规定评价时间
			Organization o = Organization.dao.findById(1);
			int hours = o.getInt("basic_maxdaily");

			String time_rank = cp.getStr("RANK_NAME");// 上课的时间段
			String lastTime = cptime + " " + time_rank.split("-")[0] + ":00";// 取出结束时间并合并成time格式
			long diffTime = ToolDateTime.compareDateStr(lastTime, ToolDateTime.dateToDateString(new Date())) / 1000 / 60;// 计算相差的分钟数
			if (diffTime >= 0) {
				if (role.equals("1") || role.equals("2")) {
					if (role.equals("1")) { // 老师评论
						Db.update("update courseplan set TEACHER_PINGLUN='y' where ID=?", cp.getInt("id"));
						if (records == null) {
							Teachergrade t = new Teachergrade();
							t.set("courseplan_id", courseplanid).set("studentid", studentid).set("ATTENTION", zhuyili).set("UNDERSTAND", lijieli)
									.set("STUDYMANNER", xuxitaidu).set("STUDYTASK", shangcizuoye);
							t.set("COURSE_DETAILS", bencikecheng).set("HOMEWORK", bencizuoye).set("question", question).set("method", method)
									.set("tutormsg", tutormsg);
							t.set("role", role);
							t.set("createtime", new Date());
							if (diffTime < hours * 60 * 60) {
								t.set("isoneday", 1);
							} else {
								t.set("isoneday", 2);
							}
							t.save();
							Db.update("insert into teachergrade_update select * from teachergrade  where id= ? ", t.getPrimaryKeyValue());
						} else {
							boolean flag = false;
							if (records.getStr("UNDERSTAND").equals("")) {
								flag = true;
							}
							records.set("ATTENTION", zhuyili).set("UNDERSTAND", lijieli).set("STUDYMANNER", xuxitaidu).set("STUDYTASK", shangcizuoye);
							records.set("COURSE_DETAILS", bencikecheng).set("HOMEWORK", bencizuoye).set("question", question).set("method", method)
									.set("tutormsg", tutormsg).update();
							if (flag) {
								GradeUpdate gp = GradeUpdate.dao.findById(records.getPrimaryKeyValue());
								gp.set("ATTENTION", zhuyili).set("UNDERSTAND", lijieli).set("STUDYMANNER", xuxitaidu).set("STUDYTASK", shangcizuoye);
								gp.set("COURSE_DETAILS", bencikecheng).set("HOMEWORK", bencizuoye).set("question", question).set("method", method)
										.set("tutormsg", tutormsg).update();
							}
						}
					} else if (role.equals("2")) {// 学生评论 暂未
						Db.update("update courseplan set STUDENT_PINGLUN='y' where ID=?", cp.getInt("id"));
						records.set("LJX", zhuyili).set("ZSD", lijieli).set("ZRX", xuxitaidu).set("QHL", shangcizuoye).set("SBZ", tutormsg).update();
					}
					if (diffTime < (hours * 60 * 60)) {
						json.put("message", "保存成功。");
					} else {
						json.put("message", "保存成功,但是提交日期已经超出" + hours + "小时");
					}
				} else {
					json.put("message", "对不起,您不是该课程的评价教师或学生。如需评价，请通知课程教师或学生进行评价");
				}
			} else {
				json.put("message", "课程未完成！");
			}
			renderJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询老师为学生打的分数 回显
	 */
	public void getTeachergrade() {
		try {
			String courseplan_id = getPara("courseplan_id");
			String sql = "select * from teachergrade where COURSEPLAN_ID=" + Integer.parseInt(courseplan_id);
			java.util.List<Record> records = Db.find(sql);
			if (records.size() > 0) {
				renderJson(records);
			} else {
				renderJson("aaa");
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/*
	 * public void syncPinglun(){ String sql =
	 * "SELECT cp.class_id,cp.TIMERANK_ID,cp.COURSE_TIME,cp.ROOM_ID,cp.TEACHER_ID,cp.TEACHER_PINGLUN,cp.STUDENT_PINGLUN FROM courseplan cp WHERE cp.class_id<>0 AND cp.STATE=0 AND cp.TEACHER_PINGLUN='y'"
	 * ; List<Record> list = Db.find(sql); for(Record cp : list){ Record xb =
	 * Db.findFirst(
	 * "SELECT * FROM courseplan WHERE STATE=1 AND COURSE_TIME=? AND TIMERANK_ID=? AND TEACHER_ID=? "
	 * , cp.get("COURSE_TIME"), cp.getInt("TIMERANK_ID"),
	 * cp.getInt("TEACHER_ID")); } }
	 */
}
