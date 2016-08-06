package com.momathink.sys.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.Constants;
import com.momathink.sys.sms.model.SendSMS;
import com.momathink.sys.sms.model.SmsMessage;
import com.momathink.sys.sms.service.SmsMessageService;
@Controller(controllerKey="/smsmessage")
public class SmsMessageController extends BaseController {
	SmsMessageService smsMessageService = new SmsMessageService();
	/**
	 * 短信管理发送短信记录 分页
	 */
	public void index(){
		smsMessageService.smsMessagelist(splitPage);
		setAttr("showPages", splitPage.getPage());
		renderJsp("/operator/sms_message.jsp");
	}
	
	public void aginSend(){
		JSONObject json = new JSONObject();
		String id = getPara("id");
		SmsMessage sms = SmsMessage.dao.findById(id);
		SendSMS.sendCoursePlanSms(sms.get("send_msg").toString(), sms.get("recipient_tel").toString(),Constants.RECEIVE_SMS_STUDENT);
		renderJson(json);
	}
}
