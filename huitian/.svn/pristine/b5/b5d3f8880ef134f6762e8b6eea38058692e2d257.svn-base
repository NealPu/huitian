package com.momathink.sys.sms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.base.Util;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.sys.sms.model.SendSMS;
import com.momathink.sys.sms.service.EmailService;

/**
 * @ClassName  CustomerApplyAction
 * @package com.fairyhawk.action.customer
 * @description
 * @author  
 * @Create Date: 2013-3-5 下午04:40:50
 * 
 */
@Controller(controllerKey="/sendcms")
public class SendController extends BaseController {

	private static final Log logger = LogFactory.getLog(SendController.class);
	private EmailService emailService;

	/**
	 * @return the emailService
	 */
	public EmailService getEmailService() {
		return emailService;
	}

	/**
	 * @param emailService the emailService to set
	 */
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void sendCourseInfo() {
		try {
			String sql = "select teacher.id,student.real_name as student_name,student.tel as student_tel,student.parents_tel parenttel,student.PARENTS_TEL_ACCEPT parentTelAccept,teacher.real_name teacher_name ,teacher.tel teacher_tel,classroom.name classname,time_rank.rank_name,time_rank.rank_type,course.course_name,campus.CAMPUS_NAME,courseplan.remark from courseplan"
					+ " left join account student on courseplan.student_id=student.id"
					+ " left join account teacher on courseplan.teacher_id=teacher.id "
					+ " left join classroom on courseplan.room_id=classroom.id"
					+ " left join time_rank on courseplan.TIMERANK_ID=time_rank.id"
					+ " left join course on courseplan.course_id=course.id"
					+ " LEFT JOIN campus ON courseplan.CAMPUS_ID=campus.Id"
					+ " where student.state =0 and DATE_FORMAT(course_time,'%Y-%m-%d')=DATE_FORMAT(date_add(CURDATE(), interval 1 day),'%Y-%m-%d') AND courseplan.STATE <> 1 order by  teacher.id,TIMERANK_ID";
			List<Record> list = Db.find(sql);
			String smsInfo = "";
			String smsTel = "";
			int index = 0;
			String tomorrowDate = Util.getTomorrowDate();
			Map<String, String> maps = new HashMap<String, String>();
			for (Record record : list) {// 组合数据
				index++;
				String remark = "";
				if(record.getStr("remark").length() == 2 && "暂无".equals(record.getStr("remark"))){
					remark = "";
				}else{
					remark = "备注：" + record.getStr("remark") + ",";
				}
				smsInfo = "【上课通知】" + record.getStr("student_name") + "同学你好," + tomorrowDate + ""
						+ record.getStr("rank_name") + "," + record.getStr("CAMPUS_NAME") + ""
						+ record.getStr("classname") + "," + record.getStr("teacher_name") + "老师"
						+ record.getStr("course_name") + "课程," + remark + "请提前温习:)--" + Util.getPropVal("company");
				smsTel = record.getStr("STUDENT_TEL");
				if (smsTel != null && !smsTel.equals("") && smsTel.length() == 11)// 判断学生手机号为空
				{
					smsTel = smsTel + "_s_" + index;// 学生
					maps.put(smsTel, smsInfo);
				}
				// 家长
				smsTel = record.getStr("parenttel");
				if (smsTel != null && !smsTel.equals("") && record.getInt("parentTelAccept") == 1
						&& smsTel.length() == 11)// 判断家长手机号为空或者是否接受
				{
					smsTel = smsTel + "_p_" + index;// 家长
					maps.put(smsTel, smsInfo);
				}
				/*
				 * //老师 smsTel=record.getStr("teacher_tel");
				 * if(smsTel!=null&&!smsTel.equals("")&&smsTel.length()==11)//判断老师手机号为空或者是否接受 { smsTel=smsTel+"_t";//老师
				 * smsInfo
				 * =record.getStr("rank_type")+"时段："+record.getStr("student_name")+record.getStr("course_name")+" ";
				 * if(maps.get(smsTel)!=null){ smsInfo=maps.get(smsTel)+smsInfo; } maps.put(smsTel, smsInfo); }
				 */
			}
			for (Map.Entry<String, String> entry : maps.entrySet()) {
				smsTel = entry.getKey();
				smsInfo = entry.getValue();
				if (smsTel.contains("_t")) {
					smsInfo = "【" + tomorrowDate + "课表】" + smsInfo + "by" + Util.getPropVal("company");
				}
				smsTel = smsTel.substring(0, 11);
				SendSMS.sendSms(smsInfo, smsTel);// 发送短信
				logger.info(smsInfo + "-->" + smsTel);
				Thread.sleep(200);
			}
			Record plan1 = Db.findFirst(
					"SELECT COUNT(*) plan1 FROM courseplan " +
					"WHERE DATE_FORMAT(course_time,'%Y-%m-%d')=DATE_FORMAT(date_add(CURDATE(), interval 1 day),'%Y-%m-%d') AND class_id = 0");
			Record plan2 = Db.findFirst("SELECT COUNT(*) plan2 FROM (SELECT * FROM courseplan " +
					"WHERE DATE_FORMAT(course_time,'%Y-%m-%d')=DATE_FORMAT(date_add(CURDATE(), interval 1 day),'%Y-%m-%d') AND class_id <> 0 GROUP BY COURSE_TIME,TIMERANK_ID,TEACHER_ID) a ");
			Organization o = Organization.dao.findById(1);
			List<String> nametels = new ArrayList<String>();
			if(o.get("sms_names")!=null&&o.get("sms_names")!=""){
				String[] smsname=o.getStr("sms_names").split(",");
				String[] smstel=o.getStr("sms_tels").split(",");
				for(int i=0;i<smsname.length;i++){
					nametels.add(smsname[i]+','+smstel[i]);
				}
			}
			if (nametels.size() > 0) {
				for (String rec : nametels) {
					String[] recInfo = rec.split(",");
					if (recInfo.length > 1) {
						SendSMS.sendSms(
								recInfo[0] + "您好！" + tomorrowDate + "的课表已发送完毕，一对一共计：" + plan1.get("plan1") + " 节，小班共计："
										+ plan2.get("plan2") + "节，总计" + list.size() + "人。" + "--"
										+ Util.getPropVal("company"), recInfo[1]);// 发送确认短信给刘国强
						logger.info(recInfo[0] + "您好！" + tomorrowDate + "的课表已发送完毕，一对一共计：" + plan1.get("plan1")
								+ " 节，小班共计：" + plan2.get("plan2") + "节，总计" + list.size() + "人。" + "--"
								+ Util.getPropVal("company") + "--" + recInfo[1]);
					}
				}
			}
			// this.send(tomorrowDate+"的课表已发送完毕，总计"+list.size()+"人。", "13260008070");//发送确认短信给刘国强
			// this.send(tomorrowDate+"的课表已发送完毕，总计"+list.size()+"人。", "18610056596");//发送确认短信给刘国强
			// this.send(tomorrowDate+"的课表已发送完毕，总计"+list.size()+"人。", "13488751040");//发送确认短信给david
		} catch (Exception e) {
			logger.error(e);
			Organization o = Organization.dao.findById(1);
			List<String> nametels = new ArrayList<String>();
			if(o.get("sms_names")!=null&&o.get("sms_names")!=""){
				String[] smsname=o.getStr("sms_names").split(",");
				String[] smstel=o.getStr("sms_tels").split(",");
				for(int i=0;i<smsname.length;i++){
					nametels.add(smsname[i]+','+smstel[i]);
				}
			}
			if (nametels.size() > 0) {
				for (String rec : nametels) {
					String[] recInfo = rec.split(",");
					if (recInfo.length > 1) {
						SendSMS.sendSms("课表发送出现问题!!!" + "--" + Util.getPropVal("company"), recInfo[1]);// 发送确认短信给刘国强
						logger.info("课表发送出现问题!!!" + "--" + Util.getPropVal("company") + "--" + recInfo[1]);
					}
				}
			}
			// this.send("课表发送出现问题!!!", "15652961182");// 发送确认短信给刘国强
		}
	}

}
