package com.momathink.sys.sms.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.constants.Constants;
@Table(tableName="crm_smstemplate")
public class SmsTemplate extends BaseModel<SmsTemplate> {
	private static final long serialVersionUID = 9173917692482770076L;
	public static final SmsTemplate dao = new SmsTemplate();
	
	/**
	 * 查询所有的模板
	 * @return
	 */
	public List<SmsTemplate> getAllMessage(String name,String type) {
		StringBuffer sf = new StringBuffer("select * from crm_smstemplate where 1=1  ");
		if(name!=null&&!name.equals("")){
			sf.append(" and sms_name = ").append(name);
		}
		if(type!=null&&!type.equals("0")){
			sf.append(" and sms_type = ").append(type);
		}
		return dao.find(sf.toString());
	}
	
	/**
	 * 学生丶家长和老师 下发课表的短信模板
	 * @param name
	 * @param time
	 * @param timerank
	 * @param campus
	 * @param room
	 * @param teacher
	 * @param course
	 * @param code   用于区别谁调用了这个方法
	 * @return
	 */
	public static String useTemplateToSendDownForAppointor(String name,String time,String timerank,String campus,String room,String teacher,String course,int code){
		StringBuffer sf = new StringBuffer("select * from crm_smstemplate where sms_state = 1 ");
		if(code==1){
			sf.append(" and sms_type=1 and sms_name = 0");
		}else if(code==2){
			sf.append(" and sms_type=2 and sms_name = 0");
		}else if(code==3){
			sf.append(" and sms_type=3 and sms_name = 0");
		}
		SmsTemplate sms = SmsTemplate.dao.findFirst(sf.toString());
		String str="";
			//判断对象是否为空
		if(sms==null){
			str="";
			//判断是否为有值
		}else if(sms.get("sms_ch_style")==null){
			str="";
			//判断是否包含
		}else if(sms.get("sms_ch_style").toString().contains("{student_name}") && sms.get("sms_ch_style").toString().contains("{course_date}")
				&& sms.get("sms_ch_style").toString().contains("{rank_name}") && sms.get("sms_ch_style").toString().contains("{campus_name}")
				&& sms.get("sms_ch_style").toString().contains("{room_name}")&& sms.get("sms_ch_style").toString().contains("{teacher_name}")
				&& sms.get("sms_ch_style").toString().contains("{course_name}")){
			 str = sms.get("sms_ch_style").toString()
						.replace("{student_name}",name).replace("{course_date}",time )
						.replace("{rank_name}", timerank).replace("{campus_name}",campus )
						.replace("{room_name}",room ).replace("{teacher_name}",teacher).replace("{course_name}",course);
		}else{
			str="";
		}
		return str;
	}
	
	/**
	 * 学生丶家长和老师的取消排课下发短信模板
	 * @param name
	 * @param time
	 * @param timerank
	 * @param campus
	 * @param room
	 * @param teacher 学生和家长调用的时候应该传入空
	 * @param course
	 * @param code
	 * @return
	 */
	public String useTemplateToSendDownCancelCourseForAppointor(String name,String time,String timerank,String campus,String room,String teacher,String course,int code){
		StringBuffer sf = new StringBuffer("select * from crm_smstemplate where sms_state = 1  ");
		if(code==1){
			sf.append(" and sms_type=1 and sms_name = 1");
		}else if(code==2){
			sf.append(" and sms_type=2 and sms_name = 1");
		}else if(code==3){
			sf.append(" and sms_type=3 and sms_name = 1");
		}
		SmsTemplate sms = SmsTemplate.dao.findFirst(sf.toString());
		String str="";
		//判断该对象是否存在《不存在则返回空》
		if(sms==null){
			str="";
			//判断获取的值是否为null
		}else if(sms.get("sms_ch_style")==null){
			str="";
		//判断获取的字符串是否包含指定字符串《不包含则返回空》	
		}else if(sms.get("sms_ch_style").toString().contains("{student_name}") && sms.get("sms_ch_style").toString().contains("{course_date}")
				&& sms.get("sms_ch_style").toString().contains("{rank_name}") && sms.get("sms_ch_style").toString().contains("{campus_name}")
				&& sms.get("sms_ch_style").toString().contains("{room_name}")
				&& sms.get("sms_ch_style").toString().contains("{course_name}")){
			//学生和家长调用的时候teacher传入的是空   区别家长丶学生跟老师下发课表的不同之处
			if(teacher!=""){
				 str = sms.get("sms_ch_style").toString()
							.replace("{student_name}",name).replace("{course_date}",time )
							.replace("{rank_name}", timerank).replace("{campus_name}",campus )
							.replace("{room_name}",room ).replace("{teacher_name}",teacher).replace("{course_name}",course);
			}else{
				//确定是老师的时候判断是否包含指定的字符串
				if(sms.get("sms_ch_style").toString().contains("{teacher_name}")){
					str = sms.get("sms_ch_style").toString()
							.replace("{student_name}",name).replace("{course_date}",time )
							.replace("{rank_name}", timerank).replace("{campus_name}",campus )
							.replace("{room_name}",room ).replace("{course_name}",course);
				}else{
					str="";
				}
			}
		}else{
			str="";
		}
		return str;
	}
	
	/**
	 * 该方法用与给外教发送上课或取消排课的信息
	 * @param name
	 * @param time
	 * @param timerank
	 * @param campus
	 * @param room
	 * @param teacher
	 * @param course
	 * @param code 用于判断是外教时   发送的是上课信息   还是取消排课信息
	 * @return
	 */
	public String isForeignTeacherUseTemplate(String name,String time,String timerank,String campus,String room,String teacher,String course,int code){
		StringBuffer sf = new StringBuffer("select * from crm_smstemplate where sms_state = 1  ");
		if(code==0){
			sf.append(" and sms_type=3 and sms_name = 0 ");
		}else{
			sf.append(" and sms_type=3 and sms_name = 1 ");
		}
		SmsTemplate sms = SmsTemplate.dao.findFirst(sf.toString());
		String str="";
		if(sms==null){
			str="";
			//判断获取的值是否为null
		}else if(sms.get("sms_en_style")==null){
			str="";
		//判断获取的字符串是否包含指定字符串《不包含则返回空》	
		}else if(sms.get("sms_en_style").toString().contains("{student_name}") && sms.get("sms_ch_style").toString().contains("{course_date}")
				&& sms.get("sms_en_style").toString().contains("{rank_name}") && sms.get("sms_ch_style").toString().contains("{campus_name}")
				&& sms.get("sms_en_style").toString().contains("{room_name}")&&sms.get("sms_en_style").toString().contains("{teacher_name}")
				&& sms.get("sms_en_style").toString().contains("{course_name}")){
				//确定是老师的时候判断是否包含指定的字符串
					str = sms.get("sms_en_style").toString()
							.replace("{student_name}",name).replace("{course_date}",time )
							.replace("{rank_name}", timerank).replace("{campus_name}",campus )
							.replace("{room_name}",room ).replace("{course_name}",course);
		}else{
			str="";
		}
		return str;
	}
	
	/**
	 * 查询正在使用的模板
	 * @return
	 */
	public SmsTemplate getUseTemplate(int name,int type){
		StringBuffer  sf = new StringBuffer("select * from crm_smstemplate where sms_state = 1  ");
		if(name==0 && type==1){
			sf.append(" and sms_name = 0 and sms_type = 1 ");
		}else if(name==1 && type==1){
			sf.append(" and sms_name = 1 and sms_type = 1 ");
		}else if(name==0 && type==2){
			sf.append(" and sms_name = 0 and sms_type = 2 ");
		}else if(name==1 && type==2){
			sf.append(" and sms_name = 1 and sms_type = 2 ");
		}else if(name==0 && type==3){
			sf.append(" and sms_name = 0 and sms_type = 3 ");
		}else if(name==1 && type==3){
			sf.append(" and sms_name = 1 and sms_type = 3 ");
		}
		return dao.findFirst(sf.toString());
	}

	/**
	 * 根据短信模板编码获取短信模板 
	 * @author David
	 * @param numbers 短信模板编码
	 * @param language 语言
	 * @return 默认返回中文短信模板
	 */
	public String getNoticeMessageByNumbers(String numbers,String language) {
		SmsTemplate sms = dao.findFirst("select * from crm_smstemplate where numbers = ?",numbers);
		if(Constants.LANGUAGE_EN.equals(language)){
			return sms.getStr("sms_en_style");
		}else{
			return sms.getStr("sms_en_style");
		}
	}
}
