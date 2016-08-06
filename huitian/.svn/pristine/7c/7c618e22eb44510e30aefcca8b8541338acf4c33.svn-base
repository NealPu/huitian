package com.momathink.common.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.sys.sms.model.SmsSettings;

/**
 * 短信/邮件消息下发配置
 * 
 * @author David
 *
 */
public class MessagePropertiesPlugin implements IPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	/**
	 * 保存系统配置参数值
	 */
	private static Map<String, String> smsParamMap = new HashMap<String, String>();
	private static Map<String, String> emailParamMap = new HashMap<String, String>();
//	private static Map<String, Map<String,String>> campusParamMap = new HashMap<String, Map<String,String>>();
//	private static Map<String, String> subjectParamMap = new HashMap<String,String>();
//	private static Map<String, String> courseParamMap = new HashMap<String,String>();
//	private static Map<String, String> timeRankParamMap = new HashMap<String,String>();
//	private static Map<String, String> classroomParamMap = new HashMap<String,String>();
	private static Map<String, String> smsMessageMap = new HashMap<String,String>();
	private static Map<String, String> emailMessageMap = new HashMap<String,String>();
	private Properties properties;

	public MessagePropertiesPlugin(Properties properties) {
		this.properties = properties;
	}

	/**
	 * 获取短信配置信息
	 * @param key
	 * @return
	 */
	public static String getSmsParamMapValue(String key) {
		return smsParamMap.get(key);
	}
	/**
	 * 获取邮件配置信息
	 * @param key
	 * @return
	 */
	public static String getEmailParamMapValue(String key) {
		return emailParamMap.get(key);
	}
	/**
	 * 获取校区配置信息
	 * @param key
	 * @return
	 */
//	public static Map<String,String> getCampusParamMapValue(String key) {
//		return campusParamMap.get(key);
//	}
	/**
	 * 获取发送邮件相关内容配置
	 * @param key
	 * @return
	 */
	public static String getEmailMessageMapValue(String key) {
		return emailMessageMap.get(key);
	}
	/**
	 * 获取发送短信相关内容配置
	 * @param key
	 * @return
	 */
	public static String getSmsMessageMapValue(String key) {
		return smsMessageMap.get(key);
	}
	/**
	 * 获取科目信息
	 * @param key
	 * @return
	 */
//	public static String getSubjectParamMapValue(String key) {
//		return subjectParamMap.get(key);
//	}
//	
//	public static String getTimeRankParamMapValue(String key) {
//		return timeRankParamMap.get(key);
//	}
//
//	public static String getCourseParamMapValue(String key) {
//		return courseParamMap.get(key);
//	}
//	public static String getClassroomParamMapValue(String key) {
//		return classroomParamMap.get(key);
//	}
	@Override
	public boolean start() {
		Organization org = Organization.dao.findById(1);
		SmsSettings sms = SmsSettings.dao.getUsedServiceProvider();
		smsParamMap.put("username", sms==null?"":sms.getStr("sms_user"));
		smsParamMap.put("password",sms==null?"":sms.getStr("sms_possword"));
		smsParamMap.put("servicesHost", sms==null?"":sms.getStr("sms_servicesHost"));
		smsParamMap.put("servicesRequestAddRess", sms==null?"":sms.getStr("sms_servicesRequestAddRess"));
		smsParamMap.put("company", org==null?"":org.getStr("name"));
		smsParamMap.put("smsStatus",org==null?"":org.getInt("sms_control")==0?"on":"off");
		
		emailParamMap.put("emailStatus",org==null?"off":org.getStr("email_state"));
		emailParamMap.put("server_host",org==null?"":org.getStr("email_serverhost"));
		emailParamMap.put("server_port",org==null?"":org.getStr("email_serverport"));
		emailParamMap.put("sender_email",org==null?"":org.getStr("email_senderemail"));
		emailParamMap.put("sender_password",org==null?"":org.getStr("email_senderpassword"));
		emailParamMap.put("from_address",org==null?"":org.getStr("email_fromaddress"));
		emailParamMap.put("email_title",org==null?"":org.getStr("email_title"));
		emailParamMap.put("company",org==null?"":org.getStr("name"));
		
		smsMessageMap.put(MesContantsFinal.cs_sms_zhuce, properties.getProperty(MesContantsFinal.cs_sms_zhuce));
		smsMessageMap.put(MesContantsFinal.cs_sms_tuijian, properties.getProperty(MesContantsFinal.cs_sms_tuijian));
		smsMessageMap.put(MesContantsFinal.cs_sms_tuijian_nokcgw, properties.getProperty(MesContantsFinal.cs_sms_tuijian_nokcgw));
		smsMessageMap.put(MesContantsFinal.cs_sms_buchong, properties.getProperty(MesContantsFinal.cs_sms_buchong));
		smsMessageMap.put(MesContantsFinal.cs_sms_fankui, properties.getProperty(MesContantsFinal.cs_sms_fankui));
		smsMessageMap.put(MesContantsFinal.cs_sms_fankui_again, properties.getProperty(MesContantsFinal.cs_sms_fankui_again));
		smsMessageMap.put(MesContantsFinal.cs_sms_chengdan, properties.getProperty(MesContantsFinal.cs_sms_chengdan));
		smsMessageMap.put(MesContantsFinal.cs_sms_jiaofei, properties.getProperty(MesContantsFinal.cs_sms_jiaofei));
		smsMessageMap.put(MesContantsFinal.cs_sms_yupaike, properties.getProperty(MesContantsFinal.cs_sms_yupaike));
		smsMessageMap.put(MesContantsFinal.cs_sms_xufei, properties.getProperty(MesContantsFinal.cs_sms_xufei));
		smsMessageMap.put(MesContantsFinal.cs_sms_xiaji, properties.getProperty(MesContantsFinal.cs_sms_xiaji));
		smsMessageMap.put(MesContantsFinal.cs_sms_jiesuan, properties.getProperty(MesContantsFinal.cs_sms_jiesuan));
		
		emailMessageMap.put(MesContantsFinal.cs_email_zhuce, properties.getProperty(MesContantsFinal.cs_email_zhuce));
		emailMessageMap.put(MesContantsFinal.cs_email_tuijian, properties.getProperty(MesContantsFinal.cs_email_tuijian));
		emailMessageMap.put(MesContantsFinal.cs_email_buchong, properties.getProperty(MesContantsFinal.cs_email_buchong));
		emailMessageMap.put(MesContantsFinal.cs_email_fankui, properties.getProperty(MesContantsFinal.cs_email_fankui));
		emailMessageMap.put(MesContantsFinal.cs_email_fankui_again, properties.getProperty(MesContantsFinal.cs_email_fankui_again));
		emailMessageMap.put(MesContantsFinal.cs_email_chengdan, properties.getProperty(MesContantsFinal.cs_email_chengdan));
		emailMessageMap.put(MesContantsFinal.cs_email_jiaofei, properties.getProperty(MesContantsFinal.cs_email_jiaofei));
		emailMessageMap.put(MesContantsFinal.cs_email_yupaike, properties.getProperty(MesContantsFinal.cs_email_yupaike));
		emailMessageMap.put(MesContantsFinal.cs_email_xufei, properties.getProperty(MesContantsFinal.cs_email_xufei));
		emailMessageMap.put(MesContantsFinal.cs_email_xiaji, properties.getProperty(MesContantsFinal.cs_email_xiaji));
		emailMessageMap.put(MesContantsFinal.cs_email_jiesuan, properties.getProperty(MesContantsFinal.cs_email_jiesuan));
		
		smsMessageMap.put(MesContantsFinal.kc_sms_buchong, properties.getProperty(MesContantsFinal.kc_sms_buchong));
		smsMessageMap.put(MesContantsFinal.kc_sms_nottuijian, properties.getProperty(MesContantsFinal.kc_sms_nottuijian));
		smsMessageMap.put(MesContantsFinal.kc_sms_tuijian, properties.getProperty(MesContantsFinal.kc_sms_tuijian));
		smsMessageMap.put(MesContantsFinal.kc_sms_tuijian_xuesheng, properties.getProperty(MesContantsFinal.kc_sms_tuijian_xuesheng));
		
		smsMessageMap.put(MesContantsFinal.apply_sms, properties.getProperty(MesContantsFinal.apply_sms));
		smsMessageMap.put(MesContantsFinal.apply_sms_again, properties.getProperty(MesContantsFinal.apply_sms_again));
		smsMessageMap.put(MesContantsFinal.apply_sms_pass, properties.getProperty(MesContantsFinal.apply_sms_pass));
		smsMessageMap.put(MesContantsFinal.apply_sms_refuse, properties.getProperty(MesContantsFinal.apply_sms_refuse));
		
		emailMessageMap.put(MesContantsFinal.kc_email_buchong, properties.getProperty(MesContantsFinal.kc_email_buchong));
		emailMessageMap.put(MesContantsFinal.kc_email_tuijian, properties.getProperty(MesContantsFinal.kc_email_tuijian));
		
		emailMessageMap.put(MesContantsFinal.apply_email, properties.getProperty(MesContantsFinal.apply_email));
		emailMessageMap.put(MesContantsFinal.apply_email_again, properties.getProperty(MesContantsFinal.apply_email_again));
		emailMessageMap.put(MesContantsFinal.apply_email_pass, properties.getProperty(MesContantsFinal.apply_email_pass));
		emailMessageMap.put(MesContantsFinal.apply_email_refuse, properties.getProperty(MesContantsFinal.apply_email_refuse));

		smsMessageMap.put(MesContantsFinal.ls_sms_today_qxpk, properties.getProperty(MesContantsFinal.ls_sms_today_qxpk));
		smsMessageMap.put(MesContantsFinal.ls_sms_today_tjpk, properties.getProperty(MesContantsFinal.ls_sms_today_tjpk));
		
		smsMessageMap.put(MesContantsFinal.xs_sms_today_qxpk, properties.getProperty(MesContantsFinal.xs_sms_today_qxpk));
		smsMessageMap.put(MesContantsFinal.xs_sms_today_tjpk, properties.getProperty(MesContantsFinal.xs_sms_today_tjpk));
		
		smsMessageMap.put(MesContantsFinal.jz_sms_today_qxpk, properties.getProperty(MesContantsFinal.jz_sms_today_qxpk));
		smsMessageMap.put(MesContantsFinal.jz_sms_today_tjpk, properties.getProperty(MesContantsFinal.jz_sms_today_tjpk));
		
		smsMessageMap.put(MesContantsFinal.ls_email_tjpk, properties.getProperty(MesContantsFinal.ls_email_tjpk));
		smsMessageMap.put(MesContantsFinal.dd_email_tjpk, properties.getProperty(MesContantsFinal.dd_email_tjpk));

		return true;
	}

	@Override
	public boolean stop() {
		smsParamMap.clear();
		return true;
	}

}
