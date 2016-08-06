/**
 ************************************************************
 *  Copyright (c) 2011-2014 罗德国际教育 
 ************************************************************
 * @Package com.luode.quartz
 * @Title：JobReciteWord.java
 * @author: David
 * @Date:2013-9-22 上午10:56:30
 */
package com.momathink.common.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.Util;
import com.momathink.common.constants.Constants;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMonitor;
import com.momathink.common.tools.ToolUtils;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.sys.sms.model.SendSMS;
import com.momathink.sys.sms.model.SmsTemplate;
import com.momathink.sys.sms.service.SmsMessageService;

/**
 * 类描述：
 * 
 * @author David
 * @time 2013-9-22 上午10:56:30
 * 
 */
public class SendCourseInfoJobPerday implements Job {
	private static Logger logger = Logger.getLogger(SendCourseInfoJobPerday.class);
	SmsMessageService smsMessageService = new SmsMessageService();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.debug("每日上课通知定时任务开始：" + ToolDateTime.getDate());
		try {
			Organization organization = Organization.dao.findById(1);
			String sql = "SELECT cp.Id,cp.PLAN_TYPE,cp.class_id, xs.real_name AS student_name, xs.tel AS student_tel,"
					+ "xs.receive_sms_student, xs.fathertel,xs.receive_sms_father,xs.mothertel,xs.receive_sms_mother, "
					+ "ls.real_name teacher_name,ls.tel teacher_tel, ls.receive_sms_teacher,ls.isforeignteacher, "
					+ "xq.CAMPUS_NAME,js.`NAME` classname, sd.rank_name, kc.course_name, cp.remark\n" +
					"FROM courseplan cp\n" +
					"LEFT JOIN account xs ON cp.student_id = xs.id\n" +
					"LEFT JOIN account ls ON cp.teacher_id = ls.id\n" +
					"LEFT JOIN classroom js ON cp.room_id = js.id\n" +
					"LEFT JOIN time_rank sd ON cp.TIMERANK_ID = sd.id\n" +
					"LEFT JOIN course kc ON cp.course_id = kc.id\n" +
					"LEFT JOIN campus xq ON cp.CAMPUS_ID = xq.Id\n" +
					"WHERE DATE_FORMAT(cp.COURSE_TIME, '%Y-%m-%d')=date_add(CURDATE(), INTERVAL 1 DAY)";
			List<Record> list = Db.find(sql);
			String tomorrowDate = Util.getTomorrowDate();
			int vipCourseCount = 0;
			int banCourseCount = 0;
			for (Record record : list) {// 组合数据
				int planType = record.getInt("PLAN_TYPE");
				if(planType == 2)//老师休息
					continue;
				int classId = record.getInt("class_id");
				String studentName = record.getStr("student_name");
				String studentTel = record.getStr("student_tel");
				String teacherTel = record.getStr("teacher_tel");
				String fatherTel = record.getStr("fathertel");
				List<Record> xbStudentlist = null;
				if(classId != 0){
					String xbsql = "SELECT xs.REAL_NAME,xs.TEL,xs.receive_sms_student, xs.fathertel,xs.receive_sms_father,xs.mothertel,xs.receive_sms_mother"
							+ " FROM account_banci ab LEFT JOIN account xs ON ab.account_id=xs.Id WHERE ab.banci_id=?";
					xbStudentlist = Db.find(xbsql,classId);
				}
				if(planType == 0){//课程
					String studentCourseNoticeMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.STUDENT_COURSE_NOTICE,Constants.LANGUAGE_CH);
					String parentsCourseNoticeMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.PARENTS_COURSE_NOTICE,Constants.LANGUAGE_CH);
					String studentMessage = studentCourseNoticeMessage;
					studentMessage=studentMessage.replace("{course_date}",tomorrowDate ).replace("{rank_name}", record.getStr("rank_name"))
							.replace("{campus_name}",record.getStr("CAMPUS_NAME") ).replace("{room_name}",record.getStr("classname") )
							.replace("{teacher_name}",record.getStr("teacher_name")).replace("{course_name}",record.getStr("course_name"));
					String parentsMessage = parentsCourseNoticeMessage;
					parentsMessage=parentsMessage.replace("{course_date}",tomorrowDate ).replace("{rank_name}", record.getStr("rank_name"))
							.replace("{campus_name}",record.getStr("CAMPUS_NAME") ).replace("{room_name}",record.getStr("classname") )
							.replace("{teacher_name}",record.getStr("teacher_name")).replace("{course_name}",record.getStr("course_name"));
					if(classId == 0){//1vs1
						studentMessage = studentMessage.replace("{student_name}",studentName);
						parentsMessage = parentsMessage.replace("{student_name}",studentName);
						if(record.getInt("receive_sms_student")==1&&ToolUtils.isMobile(studentTel)){
							SendSMS.sendCoursePlanSms(studentMessage, studentTel,Constants.RECEIVE_SMS_STUDENT);
						}
						if(record.getInt("receive_sms_father")==1&&ToolUtils.isMobile(fatherTel)){
							SendSMS.sendCoursePlanSms(parentsMessage, fatherTel,Constants.RECEIVE_SMS_PARENTS);
						}
						if(record.getInt("receive_sms_teacher")==1&&ToolUtils.isMobile(teacherTel)){
							String teacherCourseNoticeChMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.TEACHER_COURSE_NOTICE,Constants.LANGUAGE_CH);
							String teacherCourseNoticeEnMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.TEACHER_COURSE_NOTICE,Constants.LANGUAGE_EN);
							String teacherMessage = record.getInt("isforeignteacher")==1?teacherCourseNoticeEnMessage:teacherCourseNoticeChMessage;
							teacherMessage=teacherMessage.replace("{course_date}",tomorrowDate ).replace("{rank_name}", record.getStr("rank_name"))
									.replace("{campus_name}",record.getStr("CAMPUS_NAME") ).replace("{room_name}",record.getStr("classname") )
									.replace("{teacher_name}",record.getStr("teacher_name")).replace("{course_name}",record.getStr("course_name"));
							teacherMessage = teacherMessage.replace("{student_name}",studentName);
							SendSMS.sendCoursePlanSms(teacherMessage, teacherTel,Constants.RECEIVE_SMS_TEACHER);
						}
						if(record.getInt("receive_sms_mother")==1&&ToolUtils.isMobile(record.getStr("mothertel"))){
							SendSMS.sendCoursePlanSms(parentsMessage, record.getStr("mothertel"),Constants.RECEIVE_SMS_PARENTS);
						}
						vipCourseCount++;
					}else{//小班
						String xbStudentMessage = studentMessage;
						String xbParentsMessage = parentsMessage;
						if(record.getInt("receive_sms_teacher")==1&&ToolUtils.isMobile(teacherTel)){
							String teacherCourseNoticeChMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.TEACHER_BAN_COURSE_NOTICE,Constants.LANGUAGE_CH);
							String teacherCourseNoticeEnMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.TEACHER_BAN_COURSE_NOTICE,Constants.LANGUAGE_EN);
							String teacherMessage = record.getInt("isforeignteacher")==1?teacherCourseNoticeEnMessage:teacherCourseNoticeChMessage;
							teacherMessage=teacherMessage.replace("{class_name}",studentName).replace("{course_date}",tomorrowDate ).replace("{rank_name}", record.getStr("rank_name"))
									.replace("{campus_name}",record.getStr("CAMPUS_NAME") ).replace("{room_name}",record.getStr("classname") )
									.replace("{teacher_name}",record.getStr("teacher_name")).replace("{course_name}",record.getStr("course_name"));
							SendSMS.sendCoursePlanSms(teacherMessage, teacherTel,Constants.RECEIVE_SMS_TEACHER);
						}
						if(xbStudentlist != null){
							for(Record student : xbStudentlist){
								if(student.getInt("receive_sms_student")==1&&ToolUtils.isMobile(student.getStr("TEL"))){
									SendSMS.sendCoursePlanSms(xbStudentMessage.replace("{student_name}", student.getStr("REAL_NAME")), studentTel,Constants.RECEIVE_SMS_STUDENT);
								}
								if(student.getInt("receive_sms_father")==1&&ToolUtils.isMobile(student.getStr("fathertel"))){
									SendSMS.sendCoursePlanSms(xbParentsMessage.replace("{student_name}", student.getStr("REAL_NAME")), student.getStr("fathertel"),Constants.RECEIVE_SMS_PARENTS);
								}	
								if(student.getInt("receive_sms_mother")==1&&ToolUtils.isMobile(student.getStr("mothertel"))){
									SendSMS.sendCoursePlanSms(xbParentsMessage.replace("{student_name}", student.getStr("REAL_NAME")), student.getStr("mothertel"),Constants.RECEIVE_SMS_PARENTS);
								}	
							}
						}
						banCourseCount++;
					}
				}else if(planType == 1){//模考
					String studentTestNoticeMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.STUDENT_TEST_NOTICE,Constants.LANGUAGE_CH);
					String parentsTestNoticeMessage = SmsTemplate.dao.getNoticeMessageByNumbers(Constants.PARENTS_TEST_NOTICE,Constants.LANGUAGE_CH);
					String studentMessage = studentTestNoticeMessage;
					studentMessage=studentMessage.replace("{course_date}",tomorrowDate ).replace("{rank_name}", record.getStr("rank_name"))
							.replace("{campus_name}",record.getStr("CAMPUS_NAME") ).replace("{room_name}",record.getStr("classname") )
							.replace("{course_name}",record.getStr("course_name"));
					String parentsMessage = parentsTestNoticeMessage;
					parentsMessage=parentsMessage.replace("{course_date}",tomorrowDate ).replace("{rank_name}", record.getStr("rank_name"))
							.replace("{campus_name}",record.getStr("CAMPUS_NAME") ).replace("{room_name}",record.getStr("classname") )
							.replace("{course_name}",record.getStr("course_name"));
					if(classId == 0){//1vs1
						studentMessage = studentMessage.replace("{student_name}",studentName);
						parentsMessage = parentsMessage.replace("{student_name}",studentName);
						if(record.getInt("receive_sms_student")==1&&ToolUtils.isMobile(studentTel)){
							SendSMS.sendCoursePlanSms(studentMessage, studentTel,Constants.RECEIVE_SMS_STUDENT);
						}
						if(record.getInt("receive_sms_father")==1&&ToolUtils.isMobile(fatherTel)){
							SendSMS.sendCoursePlanSms(parentsMessage, fatherTel,Constants.RECEIVE_SMS_PARENTS);
						}
						if(record.getInt("receive_sms_mother")==1&&ToolUtils.isMobile(record.getStr("mothertel"))){
							SendSMS.sendCoursePlanSms(parentsMessage, record.getStr("mothertel"),Constants.RECEIVE_SMS_PARENTS);
						}
					}else{//小班
						String xbStudentMessage = studentMessage;
						String xbParentsMessage = parentsMessage;
						if(xbStudentlist != null){
							for(Record student : xbStudentlist){
								if(student.getInt("receive_sms_student")==1&&ToolUtils.isMobile(student.getStr("TEL"))){
									SendSMS.sendCoursePlanSms(xbStudentMessage.replace("{student_name}", student.getStr("REAL_NAME")), studentTel,Constants.RECEIVE_SMS_STUDENT);
								}
								if(student.getInt("receive_sms_father")==1&&ToolUtils.isMobile(student.getStr("fathertel"))){
									SendSMS.sendCoursePlanSms(xbParentsMessage.replace("{student_name}", student.getStr("REAL_NAME")), student.getStr("fathertel"),Constants.RECEIVE_SMS_PARENTS);
								}	
								if(student.getInt("receive_sms_mother")==1&&ToolUtils.isMobile(student.getStr("mothertel"))){
									SendSMS.sendCoursePlanSms(xbParentsMessage.replace("{student_name}", student.getStr("REAL_NAME")), student.getStr("mothertel"),Constants.RECEIVE_SMS_PARENTS);
								}	
							}
						}
					}
				}
				Db.update("Update courseplan set sendsms=1 where id="+record.getInt("Id"));
			}
			
			List<String> nametels = new ArrayList<String>();
			if(organization.get("sms_names")!=null&&organization.get("sms_names")!=""){
				String[] smsname=organization.getStr("sms_names").split(",");
				String[] smstel=organization.getStr("sms_tels").split(",");
				for(int i=0;i<smsname.length;i++){
					nametels.add(smsname[i]+','+smstel[i]);
				}
			}
			if (nametels.size() > 0) {
				for (String rec : nametels) {
					String[] recInfo = rec.split(",");
					if (recInfo.length > 1) {
						int courseCount = vipCourseCount+banCourseCount;
						SendSMS.sendCoursePlanSms(recInfo[0] + "您好！" + tomorrowDate + "的课表已发送完毕，一对一共计：" + vipCourseCount + " 节，小班共计：" + banCourseCount + "节，总计" + courseCount + "节。", recInfo[1],Constants.RECEIVE_SMS_STAFF);
					}
				}
			}
			//报告服务器运行状态
			new ToolMonitor(ToolMonitor.report_timing).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("每日上课通知定时任务结束：" + ToolDateTime.getDate());
	}
}
