package com.momathink.sys.sms.service;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.log.Logger;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.plugin.MessagePropertiesPlugin;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.brokerage.model.Brokerage;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.finance.model.Payment;
import com.momathink.sys.sms.model.SendSMS;
import com.momathink.sys.sms.model.SimpleMailSender;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.campus.model.Classroom;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.teacher.model.Teacher;

public class MessageService {
	
	public static final Subject subjectDao = new Subject();
	public static final Course courseDao = new Course();
	public static final TimeRank timeDao = new TimeRank();
	public static final Student studentDao = new Student();
	public static final Teacher teacherDao = new Teacher();
    public static final Logger log=Logger.getLogger(MessageService.class);
	/**
	 * 给学生发短信
	 * 
	 * @param sendType
	 * @param studentId
	 * @param subjectId
	 * @param courseId
	 * @param timeId
	 * @param coursePlanId
	 * @param planDate
	 * @param sms
	 * @param email
	 * @return
	 */
	public static void sendMessageToStudent(String sendSmsType, String coursePlanId) {
		if(!StringUtils.isEmpty(coursePlanId)){
			CoursePlan plan = CoursePlan.coursePlan.findById(Integer.parseInt(coursePlanId));
			if(plan!=null){
				Student student = studentDao.findById(plan.getInt("student_id"));
				Teacher teacher = teacherDao.findById(plan.getInt("teacher_id"));
				String sms = MessagePropertiesPlugin.getSmsMessageMapValue(sendSmsType);
				if (!StringUtils.isEmpty(sms)) {
					String studentName = student.getStr("real_name");
					String timerankName = TimeRank.dao.getTimeRankNameById(plan.getInt("TIMERANK_ID").toString());
					String campusName = Campus.dao.getCampusNameById(plan.getInt("CAMPUS_ID").toString());
					String roomName = Classroom.dao.getRoomNameById(plan.getInt("ROOM_ID").toString());
					String teacherName = teacher.getStr("real_name");
					String crouseName = Course.dao.getCourseNameById(plan.getInt("COURSE_ID").toString());
					switch (sendSmsType) {
					case MesContantsFinal.xs_sms_today_tjpk:
						sms = sms.replace("{student_name}", studentName);
						sms = sms.replace("{course_date}", ToolDateTime.format(plan.getTimestamp("COURSE_TIME"), ToolDateTime.pattern_ymd_ch));
						sms = sms.replace("{rank_name}", timerankName);
						sms = sms.replace("{campus_name}", campusName);
						sms = sms.replace("{room_name}", roomName);
						sms = sms.replace("{teacher_name}", teacherName);
						sms = sms.replace("{course_name}", crouseName);
						SendSMS.sendSms(sms, student.getStr("tel"));
						if ("1".equals(student.getInt("PARENTS_TEL_ACCEPT") + "")&&!(student.get("tel")==null?"":student.getStr("tel")).equals(student.get("PARENTS_TEL")==null?"":student.getStr("PARENTS_TEL"))) {// 家长接收短信通知
							if (!StringUtils.isEmpty(student.get("PARENTS_TEL")==null?"":student.getStr("PARENTS_TEL")) && student.getStr("PARENTS_TEL").length() == 11) {
								sms = MessagePropertiesPlugin.getSmsMessageMapValue(MesContantsFinal.jz_sms_today_tjpk);
								sms = sms.replace("{student_name}", studentName);
								sms = sms.replace("{course_date}", ToolDateTime.format(plan.getTimestamp("COURSE_TIME"), ToolDateTime.pattern_ymd_ch));
								sms = sms.replace("{rank_name}", timerankName);
								sms = sms.replace("{campus_name}", campusName);
								sms = sms.replace("{room_name}", roomName);
								sms = sms.replace("{teacher_name}", teacherName);
								sms = sms.replace("{course_name}", crouseName);
								SendSMS.sendSms(sms, student.getStr("PARENTS_TEL"));
							}
						}
						break;
					case MesContantsFinal.xs_sms_today_qxpk:
						sms = sms.replace("{student_name}", studentName);
						sms = sms.replace("{course_date}", plan.getTimestamp("COURSE_TIME").toString());
						sms = sms.replace("{rank_name}", timerankName);
						sms = sms.replace("{campus_name}", campusName);
						sms = sms.replace("{room_name}", roomName);
						sms = sms.replace("{teacher_name}", teacherName);
						sms = sms.replace("{course_name}", crouseName);
						SendSMS.sendSms(sms, student.getStr("tel"));
						if ("1".equals(student.getInt("PARENTS_TEL_ACCEPT") + "")&&!student.getStr("tel").equals(student.getStr("PARENTS_TEL"))) {// 家长接收短信通知
							if (!StringUtils.isEmpty(student.getStr("PARENTS_TEL")) && student.getStr("PARENTS_TEL").length() == 11) {
								sms = MessagePropertiesPlugin.getSmsMessageMapValue(MesContantsFinal.jz_sms_today_qxpk);
								sms = sms.replace("{student_name}", studentName);
								sms = sms.replace("{course_date}", plan.getTimestamp("COURSE_TIME").toString());
								sms = sms.replace("{rank_name}", timerankName);
								sms = sms.replace("{campus_name}", campusName);
								sms = sms.replace("{room_name}", roomName);
								sms = sms.replace("{teacher_name}", teacherName);
								sms = sms.replace("{course_name}", crouseName);
								SendSMS.sendSms(sms, student.getStr("PARENTS_TEL"));
							}
						}
						break;
					default:
						break;
					}
				}			
			}
		}
	}
	/**
	 * 发送邮件给老师
	 * @param coursePlanId
	 * @param sendEmailType
	 */
	public static void sendMessageToTeacher(String coursePlanId,String sendEmailType){
		if(!StringUtils.isEmpty(coursePlanId)){
			CoursePlan plan = CoursePlan.coursePlan.findById(Integer.parseInt(coursePlanId));
			if(plan!=null){
				Student student = studentDao.findById(plan.getInt("student_id"));
				Teacher teacher = teacherDao.findById(plan.getInt("teacher_id"));
				String sms = MessagePropertiesPlugin.getSmsMessageMapValue(sendEmailType);
				if (!StringUtils.isEmpty(sms)) {
					String studentName = student.getStr("real_name");
					String timerankName = TimeRank.dao.getTimeRankNameById(plan.getInt("TIMERANK_ID").toString());
					String campusName = Campus.dao.getCampusNameById(plan.getInt("CAMPUS_ID").toString());
					String roomName = Classroom.dao.getRoomNameById(plan.getInt("ROOM_ID").toString());
					String teacherName = teacher.getStr("real_name");
					String crouseName = Course.dao.getCourseNameById(plan.getInt("COURSE_ID").toString());
					switch (sendEmailType) {
					case MesContantsFinal.ls_email_tjpk:
						sms = sms.replace("{student_name}", studentName);
						sms = sms.replace("{course_date}", ToolDateTime.format(plan.getTimestamp("COURSE_TIME"), ToolDateTime.pattern_ymd));
						sms = sms.replace("{rank_name}", timerankName);
						sms = sms.replace("{campus_name}", campusName);
						sms = sms.replace("{room_name}", roomName);
						sms = sms.replace("{teacher_name}", teacherName);
						sms = sms.replace("{course_name}", crouseName);
						SimpleMailSender.send(sms, teacher.getStr("email"));;
						break;
					case MesContantsFinal.dd_email_tjpk:
						SysUser dudao = SysUser.dao.findById(student.getInt("SUPERVISOR_ID"));
						if(dudao!=null){
						sms=sms.replace("{supervisor_name}",dudao.getStr("REAL_NAME"));
						sms = sms.replace("{student_name}", studentName);
						sms = sms.replace("{course_date}", ToolDateTime.format(plan.getTimestamp("COURSE_TIME"), ToolDateTime.pattern_ymd_ch));
						sms = sms.replace("{rank_name}", timerankName);
						sms = sms.replace("{campus_name}", campusName);
						sms = sms.replace("{room_name}", roomName);
						sms = sms.replace("{teacher_name}", teacherName);
						sms = sms.replace("{course_name}", crouseName);
						SimpleMailSender.send(sms, dudao.getStr("email"));;
						}
						break;
					default:
						break;
					}
				}			
			}
		}
	}
	/**

	/**
	 * 发送短信、邮件给中介顾问
	 * 
	 * @param sendSmsType
	 * @param sendEmailType
	 * @param mediatorId渠道ID
	 * @param opportunityId销售机会ID
	 * @param feedbackContent反馈内容
	 */
	public static void sendMessageToMediator(String sendSmsType, String sendEmailType, String opportunityId, String paymentId, String brokerageId,String feedbackContent) {
		String sms = MessagePropertiesPlugin.getSmsMessageMapValue(sendSmsType);
		if (!StringUtils.isEmpty(sms)) {
			Opportunity opportunity = Opportunity.dao.findById(Integer.parseInt(opportunityId));
			Mediator mediator = Mediator.dao.findById(opportunity.getInt("mediatorid"));
			String counselorName = "";
			String counselorMobile = "";
			String counselorEmail = "";
			if(mediator == null){
				counselorName = opportunity.getStr("recommendusername");
				counselorMobile = opportunity.getStr("recommendphone");
			}else{
				counselorName = mediator.getStr("realname");
				counselorMobile = mediator.getStr("phonenumber");
				counselorEmail = mediator.getStr("email");
			}
			SysUser sysuser = SysUser.dao.findById(opportunity.getInt("kcuserid"));
			String kcgwName = null;
			String kcgwMobile = null;
			if(sysuser==null){
				sendSmsType =  MesContantsFinal.cs_sms_tuijian_nokcgw;
				sendEmailType = MesContantsFinal.cs_email_tuijian_nokcgw;
			}else{
				 kcgwName = sysuser.getStr("real_name");
				 kcgwMobile = sysuser.getStr("tel");
			}
			String contacterName = opportunity.getStr("contacter");
			String subjectName = Subject.dao.getSubjectNameByIds(opportunity.getStr("subjectids"));
			if (!StringUtils.isEmpty(sendSmsType) && !StringUtils.isEmpty(counselorMobile)) {
				switch (sendSmsType) {
				case MesContantsFinal.cs_sms_tuijian:
					sms = sms.replace("{kcgw_name}", kcgwName);
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					sms = sms.replace("{kcgw_mobile}", kcgwMobile);
					break;
				case MesContantsFinal.cs_sms_tuijian_nokcgw:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_sms_zhuce:
					sms = sms.replace("{counselor_name}", counselorName);
					break;
				case MesContantsFinal.cs_sms_buchong:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_sms_fankui:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_sms_fankui_again:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_sms_chengdan:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_sms_jiesuan:
					Brokerage b = Brokerage.dao.findById(Integer.parseInt(brokerageId));
					sms = sms.replace("{counselor_name}", b.getStr("counselorname"));
					sms = sms.replace("{begindate}", b.getDate("begindate").toString());
					sms = sms.replace("{enddate}", b.getDate("enddate").toString());
					break;
				case MesContantsFinal.cs_sms_jiaofei:
					Payment payment = Payment.dao.findById(Integer.parseInt(paymentId));
					boolean ispay = payment.getBoolean("ispay");
					if (ispay) {
						sms = sms.replace("{counselor_name}", counselorName);
						sms = sms.replace("{contacter_name}", contacterName);
						sms = sms.replace("{amount}", payment.getBigDecimal("amount").toString());
					}
					break;
				default:
					break;
				}
				SendSMS.sendSms(sms, counselorMobile);
			}
			if (!StringUtils.isEmpty(sendEmailType) && !StringUtils.isEmpty(counselorEmail)) {
				switch (sendEmailType) {
				case MesContantsFinal.cs_email_tuijian:
					sms = sms.replace("{kcgw_name}", kcgwName);
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					sms = sms.replace("{kcgw_mobile}", kcgwMobile);
					break;
				case MesContantsFinal.cs_email_tuijian_nokcgw:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_email_zhuce:
					sms = sms.replace("{counselor_name}", counselorName);
					break;
				case MesContantsFinal.cs_email_buchong:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_email_fankui:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					sms +="反馈内容："+feedbackContent;
					break;
				case MesContantsFinal.cs_email_fankui_again:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_email_chengdan:
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.cs_email_jiesuan:
					Brokerage b = Brokerage.dao.findById(Integer.parseInt(brokerageId));
					sms = sms.replace("{counselor_name}", b.getStr("counselorname"));
					sms = sms.replace("{begindate}", b.getDate("begindate").toString());
					sms = sms.replace("{enddate}", b.getDate("enddate").toString());
					break;
				case MesContantsFinal.cs_email_jiaofei:
					Payment payment = Payment.dao.findById(Integer.parseInt(paymentId));
					boolean ispay = payment.getBoolean("ispay");
					if (ispay) {
						sms = sms.replace("{counselor_name}", counselorName);
						sms = sms.replace("{contacter_name}", contacterName);
						sms = sms.replace("{amount}", payment.getBigDecimal("amount").toString());
					}
					break;
				default:
					break;
				}
				SimpleMailSender.send(sms, counselorEmail);
			}
		}
	}

	/**
	 * 发短信、邮件给课程顾问
	 * 
	 * @param sendSmsType
	 * @param sendEmailType
	 * @param advisorId
	 * @param mediatorId
	 * @param opportunityId
	 */
	public static void sendMessageToAdvisor(String sendSmsType, String sendEmailType, String opportunityId) {
		String sms = MessagePropertiesPlugin.getSmsMessageMapValue(sendSmsType);
		if (!StringUtils.isEmpty(sms)) {
			Opportunity opportunity = Opportunity.dao.findById(Integer.parseInt(opportunityId));
			Mediator mediator = Mediator.dao.findById(opportunity.getInt("mediatorid"));
			String counselorName ="";
			if(mediator == null){
				if((opportunity.getInt("source").toString()).equals("6")){
					sms = MessagePropertiesPlugin.getSmsMessageMapValue(MesContantsFinal.kc_sms_tuijian_xuesheng);
					counselorName = opportunity.getStr("recommendusername");
				}else{
					sms = MessagePropertiesPlugin.getSmsMessageMapValue(MesContantsFinal.kc_sms_nottuijian);
				}
			}else{
				counselorName = mediator.getStr("realname");
			}
			SysUser sysuser = SysUser.dao.findById(opportunity.getInt("kcuserid"));
			if(sysuser==null){
				Campus campus = Campus.dao.findById(opportunity.getInt("campusid"));
				Integer kcuserid = campus.getInt("kcuserid");
				if(kcuserid==null){
					return ;
				}else{
					sysuser = SysUser.dao.findById(campus.getInt("kcuserid"));
				}
			}
			String kcgwName = sysuser.getStr("real_name");
			String kcgwMobile = sysuser.getStr("tel");
			String kcEmail = sysuser.getStr("email");
			String contacterName = opportunity.getStr("contacter");
			String subjectName = Subject.dao.getSubjectNameByIds(opportunity.getStr("subjectids"));
			if (!StringUtils.isEmpty(sendSmsType) && !StringUtils.isEmpty(kcgwMobile)) {
				switch (sendSmsType) {
				case MesContantsFinal.kc_sms_tuijian:
					sms = sms.replace("{kcgw_name}", kcgwName);
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.kc_sms_buchong:
					sms = sms.replace("{kcgw_name}", kcgwName);
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				default:
					break;
				}
				SendSMS.sendSms(sms, kcgwMobile);
			}
			if (!StringUtils.isEmpty(sendEmailType) && !StringUtils.isEmpty(kcEmail)) {
				switch (sendEmailType) {
				case MesContantsFinal.kc_email_tuijian:
					sms = sms.replace("{kcgw_name}", kcgwName);
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				case MesContantsFinal.kc_email_buchong:
					sms = sms.replace("{kcgw_name}", kcgwName);
					sms = sms.replace("{counselor_name}", counselorName);
					sms = sms.replace("{contacter_name}", contacterName);
					sms = sms.replace("{subject_name}", subjectName);
					break;
				default:
					break;
				}
				SimpleMailSender.send(sms, kcEmail);
			}
		}
	}

	/**
	 * 发送短信、邮件给财务
	 * 
	 * @param sendSmsType
	 * @param sendEmailType
	 * @param studentId
	 * @param verifierUserId
	 * @param applyUserId
	 * @param orderId
	 */
	public static void sendMessageToFinance(String sendSmsType, String sendEmailType, String studentId, String verifierUserId, String applyUserId) {
		Student student = studentDao.findById(Integer.parseInt(studentId));
		String studentName = student.getStr("real_name");
		SysUser verifierUser = SysUser.dao.findById(Integer.parseInt(verifierUserId));
		SysUser applyUser = SysUser.dao.findById(Integer.parseInt(applyUserId));
		String verifierName = verifierUser.getStr("real_name");
		String verifierTel = verifierUser.getStr("tel");
		String verifierEmail = verifierUser.getStr("email");
		String applyUserName = applyUser.getStr("real_name");
		String applyUserTel = applyUser.getStr("tel");
		String applyUserEmail = applyUser.getStr("email");
		String sms = MessagePropertiesPlugin.getSmsMessageMapValue(sendSmsType);

		if (!StringUtils.isEmpty(sendSmsType)&&!StringUtils.isEmpty(verifierTel)) {
			switch (sendSmsType) {
			case MesContantsFinal.apply_sms:
				sms = sms.replace("{verifier_name}", verifierName);
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SendSMS.sendSms(sms, verifierTel);
				break;
			case MesContantsFinal.apply_sms_again:
				sms = sms.replace("{verifier_name}", verifierName);
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SendSMS.sendSms(sms, verifierTel);
				break;
			default:
				break;
			}
		}
		if (!StringUtils.isEmpty(sendSmsType)&&!StringUtils.isEmpty(applyUserTel)) {
			switch (sendSmsType) {
			case MesContantsFinal.apply_sms_pass:
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SendSMS.sendSms(sms, applyUserTel);
				break;
			case MesContantsFinal.apply_sms_refuse:
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SendSMS.sendSms(sms, applyUserTel);
				break;
			default:
				break;
			}
		}
	
		if (!StringUtils.isEmpty(sendEmailType)&&!StringUtils.isEmpty(verifierEmail)) {
			switch (sendEmailType) {
			case MesContantsFinal.apply_email:
				sms = sms.replace("{verifier_name}", verifierName);
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SimpleMailSender.send(sms, verifierEmail);
				break;
			case MesContantsFinal.apply_email_again:
				sms = sms.replace("{verifier_name}", verifierName);
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SimpleMailSender.send(sms, verifierEmail);
				break;
			default:
				break;
			}
		}
		if (!StringUtils.isEmpty(sendEmailType)&&!StringUtils.isEmpty(applyUserEmail)) {
			switch (sendEmailType) {
			case MesContantsFinal.apply_email_pass:
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SimpleMailSender.send(sms, applyUserEmail);
				break;
			case MesContantsFinal.apply_email_refuse:
				sms = sms.replace("{apply_user_name}", applyUserName);
				sms = sms.replace("{student_name}", studentName);
				SimpleMailSender.send(sms, applyUserEmail);
				break;
			default:
				break;
			}
		}
	}
}
