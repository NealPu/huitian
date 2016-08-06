package com.momathink.sys.sms.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.sms.model.SmsSettings;
import com.momathink.sys.sms.service.SmsSettingsService;
@Controller(controllerKey="/smssettings")
public class SmsSettingsController extends BaseController {
	private SmsSettingsService smsSettingsService = new SmsSettingsService();
	
	/**
	 *	服务商配置信息列表显示
	 */
	public void index(){
		List<SmsSettings>  list = SmsSettings.dao.getAllMessage();
		setAttr("list",list);
		renderJsp("/operator/sms_settingsMessage.jsp");
	}
	
	/**
	 * 添加服务商和配置信息《弹出窗口形式》
	 */
	public void add(){
		setAttr("type","add");
		renderJsp("/operator/layer_smssetttings_form.jsp");
	}
	
	/**
	 * 修改服务商及其配置信息
	 */
	public void update(){
		String id= getPara();
		SmsSettings sms = SmsSettings.dao.findById(id);
		setAttr("sms",sms);
		setAttr("type","update");
		renderJsp("/operator/layer_smssetttings_form.jsp");
	}
	
	/**
	 * 保存添加服务商及其配置信息
	 */
	public void save(){
		JSONObject json = new JSONObject();
		String id = getPara("smsSettings.id");
		SmsSettings sms = getModel(SmsSettings.class);
		try{
			if(id.equals("")){
				smsSettingsService.save(sms);
			}else{
				smsSettingsService.update(sms);
			}
			json.put("code",1);
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}
	
	/**
	 * 修改当前服务商的使用状态
	 */
	public void updateState(){
		JSONObject json = new JSONObject();
		try{
			SmsSettings sms = SmsSettings.dao.findById(getPara("id"));
			if(sms.getInt("sms_state")==0){
				//修改状态时 ，当未使用改变到使用状态时 ，先将正在使用的那个服务商暂停使用
				SmsSettings s = SmsSettings.dao.getUsedServiceProvider();
				if(s!=null){
					s.set("sms_state", 0);
					smsSettingsService.update(s);
				}
				sms.set("sms_state", 1);
			}else{
				sms.set("sms_state", 0);
			}
			smsSettingsService.update(sms);
			json.put("code", 1);
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}
}
