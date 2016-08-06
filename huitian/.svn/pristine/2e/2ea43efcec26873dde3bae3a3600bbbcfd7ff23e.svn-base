package com.momathink.sys.sms.model;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.momathink.common.plugin.MessagePropertiesPlugin;
import com.momathink.common.tools.ToolMD5;
import com.momathink.crm.mediator.model.Organization;

/**
 * 
 * @author David
 *
 */
public class SendSMS {
	private static final Logger logger = Logger.getLogger(SendSMS.class);
	private String encode = "GBK";

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	// 服务器地址(需要加密);
	private String servicesHost = "";
	// 服务器请求地址(需要加密);
	private String servicesRequestAddRess = "";
	// 登录的用户名(需要加密);
	private String username = "";
	// 登录的密码(需要加密);
	private String password = "";
	// 短信发送方式;
	private int smstype = 1;
	// 短信发送是否定时;
	private int timerflag = 0;
	// 短信发送定时时间;
	private String timervalue;
	// 短信发送定时的类型;
	private int timertype = 1;
	// 短信发送的编号(需要加密);
	private String timerid;
	// 发送的手机号码(需要加密);
	private String mobiles;
	// 发送的内容(需要加密);
	private String message;

	public String getServicesHost() {
		return servicesHost;
	}

	public void setServicesHost(String servicesHost) {
		this.servicesHost = servicesHost;// Base.base64Decode(servicesHost);
	}

	public String getServicesRequestAddRess() {
		return servicesRequestAddRess;
	}

	public void setServicesRequestAddRess(String servicesRequestAddRess) {
		this.servicesRequestAddRess = servicesRequestAddRess; // Base.base64Decode(servicesRequestAddRess);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username; // Base.base64Decode(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;// Base.base64Decode(password);
	}

	public int getSmstype() {
		return smstype;
	}

	public void setSmstype(int smstype) {
		this.smstype = smstype;
	}

	public int getTimerflag() {
		return timerflag;
	}

	public void setTimerflag(int timerflag) {
		this.timerflag = timerflag;
	}

	public String getTimervalue() {
		return timervalue;
	}

	public void setTimervalue(String timervalue) {
		this.timervalue = timervalue;
	}

	public int getTimertype() {
		return timertype;
	}

	public void setTimertype(int timertype) {
		this.timertype = timertype;
	}

	public String getTimerid() {
		return timerid;
	}

	public void setTimerid(String timerid) {
		this.timerid = timerid;// Base.base64Decode(timerid);
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;// Base.base64Decode(mobiles);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		try {
			this.message = URLEncoder.encode(message, this.getEncode());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// this.message = message; //Base.base64Decode(message);
	}

	// 发送短信;
	public Map<String, String> sendSMS() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc;
		Map<String, String> result = new LinkedHashMap<String, String>();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = URLConnIO.getSoapInputStream(this.getRequestAddRess().toString(), this.servicesHost);
			doc = db.parse(is);
			result.put("errorcode", doc.getElementsByTagName("errorcode").item(0).getFirstChild().getNodeValue());
			result.put("errordescription", doc.getElementsByTagName("errordescription").item(0).getFirstChild().getNodeValue());
			result.put("time", doc.getElementsByTagName("time").item(0).getFirstChild().getNodeValue());
			result.put("msgcount", doc.getElementsByTagName("msgcount").item(0).getFirstChild().getNodeValue());
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送短信失败!");
		}
		return result;
	}

	// 拼接参数;
	private StringBuffer getRequestAddRess() {
		StringBuffer requestAddRess = new StringBuffer();
		requestAddRess.append(this.servicesHost);
		requestAddRess.append(this.servicesRequestAddRess);
		requestAddRess.append("?func=sendsms&username=");
		requestAddRess.append(this.username);
		requestAddRess.append("&password=");
		requestAddRess.append(password);
		requestAddRess.append("&smstype=");
		requestAddRess.append(smstype);
		requestAddRess.append("&timerflag=");
		requestAddRess.append(this.timerflag);
		if (this.timerflag != 0) {
			requestAddRess.append("&timervalue=");
			requestAddRess.append(this.timervalue);
		}
		requestAddRess.append("&timertype=");
		requestAddRess.append(this.timertype);
		requestAddRess.append("&timerid=");
		requestAddRess.append(this.timerid);
		requestAddRess.append("&mobiles=");
		requestAddRess.append(this.mobiles);
		requestAddRess.append("&message=");
		requestAddRess.append(this.message);

		return requestAddRess;
	}

	public static void sendSms(String msg, String mobile) {
		Organization org = Organization.dao.findById(1);
		//获取正在使用的服务商的账户和密码
		SmsSettings sms = SmsSettings.dao.getUsedServiceProvider();
		try {
			String smsStatus = MessagePropertiesPlugin.getSmsParamMapValue("smsStatus");
			if ("on".equals(smsStatus)) {
				 SendSMS ss = new SendSMS();
				 ss.setUsername(sms.get("sms_user")==null?"":sms.get("sms_user").toString());
				 ss.setPassword(ToolMD5.getMD5(sms.get("sms_password")==null?"":sms.get("sms_password").toString()));
				 ss.setMessage(msg + "【"+org.getStr("name")+"】");
				 ss.setMobiles(mobile);
				 ss.setServicesHost(MessagePropertiesPlugin.getSmsParamMapValue("servicesHost"));
				 ss.setServicesRequestAddRess(MessagePropertiesPlugin.getSmsParamMapValue("servicesRequestAddRess"));
				 ss.setSmstype(0);
				 ss.setTimerid("0");
				 ss.setTimertype(0);
				 SmsThread smsThread = new SmsThread(ss);
				 Map<String ,String> maps = smsThread.call();
				logger.info("接受号码：" + mobile + "发送内容：" + msg+"发送结果："+maps.get("errorcode"));
			} else {
				logger.info("短信发送功能关闭->接受号码：" + mobile + "发送内容：" + msg);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 发送课表短信接口
	 * @param msg
	 * @param mobile
	 */
	public static Map<String ,String> sendCoursePlanSms(String msg, String mobile,int type) {
		Organization org = Organization.dao.findById(1);
		//获取正在使用的服务商的账户和密码
		SmsSettings sms = SmsSettings.dao.getUsedServiceProvider();
		Map<String ,String> sendResult = null;
		String smsStatus = org.getInt("sms_control")==0?"on":"off";
		if ("on".equals(smsStatus)) {
			SendSMS sendSMS = new SendSMS();
			sendSMS.setUsername(sms.getStr("sms_user"));
			sendSMS.setPassword(ToolMD5.getMD5(sms.getStr("sms_password")));
			sendSMS.setMessage(msg + "【" + org.getStr("name") + "】");
			sendSMS.setMobiles(mobile);
			sendSMS.setServicesHost(sms.getStr("sms_servicesHost"));
			sendSMS.setServicesRequestAddRess(sms.getStr("sms_servicesRequestAddRess"));
			sendSMS.setSmstype(0);
			sendSMS.setTimerid("0");
			sendSMS.setTimertype(0);
			SmsThread smsThread = new SmsThread(sendSMS);
			sendResult = smsThread.call();
			if (sendResult != null && sendResult.size() > 0) {
				String errorcode = sendResult.get("errorcode");
				String errordescription = sendResult.get("errordescription");
				String time = sendResult.get("time");
				String msgcount = sendResult.get("msgcount");
				SmsMessage smsMessage = new SmsMessage();
				smsMessage.set("recipient_tel", mobile).set("recipient_type", type).set("send_time", time).set("send_msg",msg).set("errorcode", errorcode).set("errordescription", errordescription);
				if(errorcode.equals("0")){
					smsMessage.set("send_state", 1).save();
				}else{
					smsMessage.set("send_state", 0).save();
				}
				SmsSettings smsSettings  = SmsSettings.dao.getUsedServiceProvider();
				smsSettings.set("sms_surplus",Integer.parseInt(msgcount)).update();
			}
			logger.info("接受号码：" + mobile + "发送内容：" + msg);
		} else {
			logger.info("短信发送功能关闭->接受号码：" + mobile + "发送内容：" + msg);
		}
		return sendResult;
	}
	public static void main(String[] args) {
		SendSMS.sendSms("短信接口调试", "13488751040");
	}
}
