package com.momathink.weixin.service;

import java.util.ArrayList;
import java.util.List;

import com.momathink.common.base.BaseService;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolString;
import com.momathink.common.tools.ToolUtils;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.operator.model.Role;
import com.momathink.teaching.classtype.model.ClassType;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.teacher.model.Teachergrade;
import com.momathink.weixin.model.User;

public class XsdMessageService extends BaseService {
	private static final String SEPARATED_LINE_1 = "\n----------\n";// 分割线1
	private static final String SEPARATED_LINE_2 = "\n***************\n";// 分割线2

	public static String getMessageByEventKey(String cxt, String openId, Integer studentId, String eventKey) {
		StringBuffer content = new StringBuffer();
		User wxuser = User.dao.findByBandUserId(studentId, openId);
		if (wxuser == null) {
			content.append("您还未绑定，请输入(输入格式：手机号#密码)");
		} else {
			Student student = Student.dao.findById(studentId);
			if (student == null) { // 老师绑定
				content.append("未查到您的信息，请确认您已开通帐号！");
			} else {
				if ("account".equalsIgnoreCase(eventKey)) { // 账号绑定
					if (Role.isStudent(student.getStr("roleids"))) { // 学生绑定
						content.append(student.get("real_name")).append(" 同学您好！您的账号已绑定，可查看信息。");
					} else { // 家长绑定
						content.append(student.get("real_name")).append(" 同学家长您好！您的账号已绑定，可查看信息。");
					}
				} else if ("course".equalsIgnoreCase(eventKey)) { // 今日课
					StringBuffer sbf = new StringBuffer();
					sbf.append("SELECT\n" +
							"	*\n" +
							"FROM\n" +
							"	((\n" +
							"		SELECT\n" +
							"			cp.ID,\n" +
							"			cp.SIGNIN,\n" +
							"			cs.CAMPUS_NAME campus_name,\n" +
							"			r. NAME,\n" +
							"			s.real_name student_name,\n" +
							"			s.PARENTS_TEL,\n" +
							"			t.REAL_NAME teacher_name,\n" +
							"			tr.id trid,\n" +
							"			tr.rank_name,\n" +
							"			c.course_name,\n" +
							"			date_format(cp.course_time, '%Y-%m-%d') coursedate,\n" +
							"			cp.REMARK\n" +
							"		FROM\n" +
							"			courseplan cp\n" +
							"		LEFT JOIN class_order co ON cp.class_id = co.id\n" +
							"		LEFT JOIN account_banci ab ON co.id = ab.banci_id\n" +
							"		LEFT JOIN account s ON ab.account_id = s.Id\n" +
							"		LEFT JOIN account t ON cp.TEACHER_ID = t.id\n" +
							"		LEFT JOIN campus cs ON cp.campus_id = cs.id\n" +
							"		LEFT JOIN classroom r ON cp.room_id = r.id\n" +
							"		LEFT JOIN time_rank tr ON cp.timerank_id = tr.id\n" +
							"		LEFT JOIN course c ON cp.course_id = c.id\n" +
							"		WHERE\n" +
							"			s.id = " +studentId+
							"		AND cp.course_time = curdate()\n" +
							"	)\n" +
							"UNION ALL\n" +
							"	(\n" +
							"		SELECT\n" +
							"			cp.ID,\n" +
							"			cp.SIGNIN,\n" +
							"			cs.CAMPUS_NAME campus_name,\n" +
							"			r. NAME,\n" +
							"			s.real_name student_name,\n" +
							"			s.PARENTS_TEL,\n" +
							"			t.REAL_NAME teacher_name,\n" +
							"			tr.id trid,\n" +
							"			tr.rank_name,\n" +
							"			c.course_name,\n" +
							"			date_format(cp.course_time, '%Y-%m-%d') coursedate,\n" +
							"			cp.REMARK\n" +
							"		FROM\n" +
							"			courseplan cp\n" +
							"		LEFT JOIN campus cs ON cp.campus_id = cs.id\n" +
							"		LEFT JOIN classroom r ON cp.room_id = r.id\n" +
							"		LEFT JOIN account s ON cp.student_id = s.id\n" +
							"		LEFT JOIN account t ON cp.teacher_id = t.id\n" +
							"		LEFT JOIN time_rank tr ON cp.timerank_id = tr.id\n" +
							"		LEFT JOIN course c ON cp.course_id = c.id\n" +
							"		WHERE\n" +
							"			cp.student_id = "+studentId+
							"		AND cp.course_time = curdate()\n" +
							"	)) s\n" +
							"order by  s.trid");
					// 学员和家长
					List<CoursePlan> list = new ArrayList<CoursePlan>();
					list = CoursePlan.coursePlan.find(sbf.toString());
					content.append("今日课程信息如下：").append(SEPARATED_LINE_2);
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							CoursePlan cp = list.get(i);
							content.append("科目：").append(cp.get("course_name")).append("\n").append("日期：").append(cp.get("coursedate")).append("\n")
									.append("时间：").append(cp.get("rank_name")).append("\n").append("校区：").append(cp.get("campus_name")).append("\n")
									.append("教室：").append(cp.get("name")).append("\n").append("学生：").append(cp.get("student_name")).append("\n")
									.append("老师：").append(cp.get("teacher_name")).append("\n").append("备注：").append(cp.get("REMARK")).append("\n");
							String ids = ToolUtils.changeDataRule(studentId+"_"+cp.get("ID"));
							Teachergrade  tg = Teachergrade.teachergrade.findByCoursePlanIdAndStudentid(cp.getInt("id").toString(),studentId.toString());
							if(tg!=null){
								String assessUrl = cxt.substring(0, cxt.lastIndexOf(":"))+"/student/assessCourse?ids="+ids;
								content.append("<a href=\""+assessUrl+"\">【点击评价】</a>");
							}
							content.append(SEPARATED_LINE_1);
						}
					} else {
						content.append("无课程信息。");
						content.append(SEPARATED_LINE_1);
					}
					

				} else if ("nextCourse".equalsIgnoreCase(eventKey)) { // 下节课
					StringBuffer sbf = new StringBuffer();
					sbf.append("SELECT\n" +
							"	*\n" +
							"FROM\n" +
							"	(\n" +
							"		(\n" +
							"			SELECT\n" +
							"				cp.ID,\n" +
							"				cp.SIGNIN,\n" +
							"				cs.CAMPUS_NAME campus_name,\n" +
							"				r. NAME,\n" +
							"				tr.id trid,\n" +
							"				tr.rank_name,\n" +
							"				s.real_name student_name,\n" +
							"				s.PARENTS_TEL,\n" +
							"				t.REAL_NAME teacher_name,\n" +
							"				concat(\n" +
							"					date_format(cp.course_time, '%Y-%m-%d'),\n" +
							"					' ',\n" +
							"					substring_index(tr.rank_name, '-', 1),\n" +
							"					':',\n" +
							"					'00'\n" +
							"				) AS rank_time,\n" +
							"				c.course_name,\n" +
							"				date_format(cp.course_time, '%Y-%m-%d') coursedate,\n" +
							"				cp.REMARK\n" +
							"			FROM\n" +
							"				courseplan cp\n" +
							"			LEFT JOIN class_order co ON cp.class_id = co.id\n" +
							"			LEFT JOIN account_banci ab ON co.id = ab.banci_id\n" +
							"			LEFT JOIN account s ON ab.account_id = s.Id\n" +
							"			LEFT JOIN account t ON cp.TEACHER_ID = t.id\n" +
							"			LEFT JOIN campus cs ON cp.campus_id = cs.id\n" +
							"			LEFT JOIN classroom r ON cp.room_id = r.id\n" +
							"			LEFT JOIN time_rank tr ON cp.timerank_id = tr.id\n" +
							"			LEFT JOIN course c ON cp.course_id = c.id\n" +
							"			WHERE\n" +
							"				s.id =" +studentId+
							"			AND cp.course_time = curdate()\n" +
							"		)\n" +
							"		UNION ALL\n" +
							"			(\n" +
							"				SELECT\n" +
							"					cp.ID,\n" +
							"					cp.SIGNIN,\n" +
							"					cs.CAMPUS_NAME campus_name,\n" +
							"					r. NAME,\n" +
							"					tr.id trid,\n" +
							"					tr.rank_name,\n" +
							"					s.real_name student_name,\n" +
							"					s.PARENTS_TEL,\n" +
							"					t.REAL_NAME teacher_name,\n" +
							"					concat(\n" +
							"						date_format(cp.course_time, '%Y-%m-%d'),\n" +
							"						' ',\n" +
							"						substring_index(tr.rank_name, '-', 1),\n" +
							"						':',\n" +
							"						'00'\n" +
							"					) AS rank_time,\n" +
							"					c.course_name,\n" +
							"					date_format(cp.course_time, '%Y-%m-%d') coursedate,\n" +
							"					cp.REMARK\n" +
							"				FROM\n" +
							"					courseplan cp\n" +
							"				LEFT JOIN campus cs ON cp.campus_id = cs.id\n" +
							"				LEFT JOIN classroom r ON cp.room_id = r.id\n" +
							"				LEFT JOIN account s ON cp.student_id = s.id\n" +
							"				LEFT JOIN account t ON cp.teacher_id = t.id\n" +
							"				LEFT JOIN time_rank tr ON cp.timerank_id = tr.id\n" +
							"				LEFT JOIN course c ON cp.course_id = c.id\n" +
							"				WHERE\n" +
							"					cp.student_id = " +studentId+
							"				AND cp.course_time = curdate()\n" +
							"			)\n" +
							"	) s\n" +
							"WHERE\n" +
							"	s.rank_time > now()\n" +
							"ORDER BY\n" +
							"	s.rank_time\n" +
							"LIMIT 0,1");

					CoursePlan cp = CoursePlan.coursePlan.findFirst(sbf.toString());
					content.append("下节课程信息如下：").append(SEPARATED_LINE_2);
					if (cp != null) {
						content.append("科目：").append(cp.get("course_name")).append("\n").append("日期：").append(cp.get("coursedate")).append("\n")
								.append("时间：").append(cp.get("rank_name")).append("\n").append("校区：").append(cp.get("campus_name")).append("\n")
								.append("教室：").append(cp.get("name")).append("\n").append("学生：").append(cp.get("student_name")).append("\n")
								.append("老师：").append(cp.get("teacher_name")).append("\n").append("备注：").append(cp.get("REMARK")).append(SEPARATED_LINE_1);
					} else {
						content.append("无课程信息。");
					}
				} else if ("futureCourse".equalsIgnoreCase(eventKey)) { // 未来课
					StringBuffer sbf = new StringBuffer();
					sbf.append("SELECT\n" +
							"	*\n" +
							"FROM\n" +
							"	(\n" +
							"		(\n" +
							"			SELECT\n" +
							"					cp.ID,\n" +
							"			cp.SIGNIN,\n" +
							"			cs.CAMPUS_NAME campus_name,\n" +
							"			r. NAME,\n" +
							"			tr.rank_name,\n" +
							"			s.real_name student_name,\n" +
							"			s.PARENTS_TEL,\n" +
							"			t.REAL_NAME teacher_name,\n" +
							"			concat(\n" +
							"				date_format(cp.course_time, '%Y-%m-%d'),\n" +
							"				' ',\n" +
							"				substring_index(tr.rank_name, '-', 1),\n" +
							"				':',\n" +
							"				'00'\n" +
							"			) AS rank_time,\n" +
							"			c.course_name,\n" +
							"			date_format(cp.course_time, '%Y-%m-%d') coursedate,\n" +
							"			cp.REMARK\n" +
							"			FROM\n" +
							"				courseplan cp\n" +
							"			LEFT JOIN class_order co ON cp.class_id = co.id\n" +
							"			LEFT JOIN account_banci ab ON co.id = ab.banci_id\n" +
							"			LEFT JOIN account s ON ab.account_id = s.Id\n" +
							"			LEFT JOIN account t ON cp.TEACHER_ID = t.id\n" +
							"			LEFT JOIN campus cs ON cp.campus_id = cs.id\n" +
							"			LEFT JOIN classroom r ON cp.room_id = r.id\n" +
							"			LEFT JOIN time_rank tr ON cp.timerank_id = tr.id\n" +
							"			LEFT JOIN course c ON cp.course_id = c.id\n" +
							"			WHERE\n" +
							"				s.id = " +studentId+
							"			AND cp.course_time >= curdate()\n" +
							"		)\n" +
							"		UNION ALL\n" +
							"			(\n" +
							"				SELECT\n" +
							"						cp.ID,\n" +
							"			cp.SIGNIN,\n" +
							"			cs.CAMPUS_NAME campus_name,\n" +
							"			r. NAME,\n" +
							"			tr.rank_name,\n" +
							"			s.real_name student_name,\n" +
							"			s.PARENTS_TEL,\n" +
							"			t.REAL_NAME teacher_name,\n" +
							"			concat(\n" +
							"				date_format(cp.course_time, '%Y-%m-%d'),\n" +
							"				' ',\n" +
							"				substring_index(tr.rank_name, '-', 1),\n" +
							"				':',\n" +
							"				'00'\n" +
							"			) AS rank_time,\n" +
							"			c.course_name,\n" +
							"			date_format(cp.course_time, '%Y-%m-%d') coursedate,\n" +
							"			cp.REMARK\n" +
							"				FROM\n" +
							"					courseplan cp\n" +
							"				LEFT JOIN campus cs ON cp.campus_id = cs.id\n" +
							"				LEFT JOIN classroom r ON cp.room_id = r.id\n" +
							"				LEFT JOIN account s ON cp.student_id = s.id\n" +
							"				LEFT JOIN account t ON cp.teacher_id = t.id\n" +
							"				LEFT JOIN time_rank tr ON cp.timerank_id = tr.id\n" +
							"				LEFT JOIN course c ON cp.course_id = c.id\n" +
							"				WHERE\n" +
							"					cp.student_id = " +studentId+
							"				AND cp.course_time >= curdate()\n" +
							"			)\n" +
							"	) s\n" +
							"WHERE\n" +
							"	s.rank_time > now()\n" +
							"ORDER BY\n" +
							"	s.rank_time\n" +
							"LIMIT 0,5");
					// 学员和家长
					List<CoursePlan> list = new ArrayList<CoursePlan>();
					list = CoursePlan.coursePlan.find(sbf.toString());
					content.append("未来课程信息如下：").append(SEPARATED_LINE_2);
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							CoursePlan cp = list.get(i);
							content.append("科目：").append(cp.get("course_name")).append("\n").append("日期：").append(cp.get("coursedate")).append("\n")
									.append("时间：").append(cp.get("rank_name")).append("\n").append("校区：").append(cp.get("campus_name")).append("\n")
									.append("教室：").append(cp.get("name")).append("\n").append("学生：").append(cp.get("student_name")).append("\n")
									.append("老师：").append(cp.get("teacher_name")).append("\n").append("备注：").append(cp.get("REMARK"))
									.append(SEPARATED_LINE_1);
						}
					} else {
						content.append("无课程信息。").append(SEPARATED_LINE_1);
					}
				} else if ("mycourse".equalsIgnoreCase(eventKey)) { // 统计
					content.append(student.getStr("real_name")).append("同学！\n").append("您的课程信息如下：").append(SEPARATED_LINE_2);
					double vipPaidzks = CourseOrder.dao.getPaidVIPzks(studentId);// 已交费VIP课时
					double vipVrrearzks = CourseOrder.dao.getArrearVIPzks(studentId);// 欠费VIP课时
					double banPaidzks = CourseOrder.dao.getPaidBanzks(studentId);// 已交费班课
					double banVrrearzks = CourseOrder.dao.getArrearBanzks(studentId);// 欠费班课
					double vipzks = ToolArith.add(vipPaidzks, vipVrrearzks);
					double banzks = ToolArith.add(banPaidzks, banVrrearzks);
					if (vipzks != 0) {
						content.append("一对一\n总课时：").append(vipzks).append("课时");
						if (vipVrrearzks != 0) {
							content.append("\n未交费：").append(vipzks).append("课时");
						}
						List<CoursePlan> cplist = CoursePlan.coursePlan.getVipYpks(studentId);
						for (CoursePlan cp : cplist) {
							content.append("\n科目：").append(cp.getStr("km"));
							content.append("\n已排：").append(cp.get("ypks")).append("课时");
							content.append("\n已上：").append(CoursePlan.coursePlan.getVipYsks(studentId, cp.getInt("subjectId"))).append("课时");
						}
						content.append(SEPARATED_LINE_1);
					}

					if (banzks != 0) {
						content.append("班课：").append(banzks).append("课时");
						List<CoursePlan> bklist = CoursePlan.coursePlan.getBanYpks(studentId);
						for (CoursePlan cp : bklist) {
							content.append("\n班型：").append(ClassType.dao.findById(cp.getInt("banxingId")).getStr("name"));
							content.append("\n班次：").append(cp.getStr("bcbh"));
							content.append("\n已排：").append(cp.get("ypks")).append("课时");
							content.append("\n已上：").append(CoursePlan.coursePlan.getBanYsks(studentId, cp.getInt("banciId"))).append("课时");
						}
						content.append(SEPARATED_LINE_1);
					}

				} else if ("homework".equalsIgnoreCase(eventKey)) { // 今日作业
					StringBuffer pmsg = new StringBuffer();
					String sql = " select date_format(cp.course_time,'%Y-%m-%d') course_date,c.COURSE_NAME,grade.COURSEPLAN_ID,"
							+ " ifnull(grade.COURSE_DETAILS,'无') COURSE_DETAILS,ifnull(grade.HOMEWORK,'无') HOMEWORK,"
							+ " ifnull(grade.REMARK,'无') REMARK from teachergrade grade left join courseplan cp on cp.id=grade.courseplan_id "
							+ " left join COURSE C on C.ID=cp.COURSE_ID where cp.student_id=? AND grade.ROLE=1 "
							+ " and cp.course_time=curdate() order by grade.COURSEPLAN_ID ";
					List<Teachergrade> grade = Teachergrade.teachergrade.find(sql, studentId);
					content.append("今日作业情况如下：").append(SEPARATED_LINE_2);
					if (grade.size() > 0) {
						for (int i = 0; i < grade.size(); i++) {
							String courseDetails = "\n课程内容：\n" + grade.get(i).getStr("COURSE_DETAILS") + SEPARATED_LINE_1;
							String homework = "作业内容：\n" + grade.get(i).getStr("HOMEWORK") + SEPARATED_LINE_1;
							pmsg.append("\n授课日期：").append(grade.get(i).getStr("course_date")).append("\n课程名称：")
									.append(grade.get(i).getStr("COURSE_NAME")).append(courseDetails).append(homework);
						}
						if (ToolString.getByteSize(pmsg.toString()) >= 2047)
							pmsg = new StringBuffer("作业内容较多，请登陆教务系统查看。");
					} else {
						pmsg.append("今日无作业。");
					}

					content.append(pmsg).append(SEPARATED_LINE_1);

				} else if ("historyHomework".equalsIgnoreCase(eventKey)) { // 历史作业
					StringBuffer pmsg = new StringBuffer();
					String sql = " select date_format(cp.course_time,'%Y-%m-%d') course_date,c.COURSE_NAME,grade.COURSEPLAN_ID,"
							+ " ifnull(grade.COURSE_DETAILS,'无') COURSE_DETAILS,ifnull(grade.HOMEWORK,'无') HOMEWORK,"
							+ " ifnull(grade.REMARK,'无') REMARK from teachergrade grade left join courseplan cp on cp.id=grade.courseplan_id "
							+ " left join COURSE C on C.ID=cp.COURSE_ID where cp.student_id=? AND grade.ROLE=1 "
							+ " and cp.course_time<=curdate() order by course_date desc,grade.COURSEPLAN_ID desc limit 0,5 ";
					List<Teachergrade> grade = Teachergrade.teachergrade.find(sql, studentId);
					content.append("历史作业情况如下：").append(SEPARATED_LINE_2);
					if (grade.size() > 0) {
						for (int i = 0; i < grade.size(); i++) {
							pmsg.append("\n授课日期：").append(grade.get(i).getStr("course_date")).append("\n课程名称：")
									.append(grade.get(i).getStr("COURSE_NAME")).append("\n课程内容：\n").append(grade.get(i).getStr("COURSE_DETAILS"))
									.append(SEPARATED_LINE_1).append("作业内容：\n").append(grade.get(i).getStr("HOMEWORK")).append(SEPARATED_LINE_1);
						}
						if (ToolString.getByteSize(pmsg.toString()) >= 2047)
							pmsg = new StringBuffer("作业内容较多，请登陆教务系统查看。");
					} else {
						pmsg.append("无历史作业信息。");
					}
					content.append(pmsg).append(SEPARATED_LINE_1);
				} else {
					content.append("功能完善中，敬请关注。").append(SEPARATED_LINE_1);
				}
			}
		}

		return content.toString() ;
	}
}
