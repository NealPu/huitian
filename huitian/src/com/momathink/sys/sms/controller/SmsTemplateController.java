package com.momathink.sys.sms.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.sms.model.SmsTemplate;
@Controller(controllerKey="/smstemplate")
public class SmsTemplateController  extends BaseController {
	/**
	 * 列表显示
	 */
	public void index(){
		String name = getPara("sms_name");
		setAttr("name",name);
		String type = getPara("sms_type");
		setAttr("type",type);
		List<SmsTemplate> list = SmsTemplate.dao.getAllMessage(name,type);
		setAttr("list",list);
		renderJsp("/operator/sms_templateMessage.jsp");
	}
	/**
	 * 添加新的模板
	 */
	public void add(){
		setAttr("type","add");
		renderJsp("/operator/layer_smsTemplate_form.jsp");
	}
	/**
	 * 修改模板
	 */
	public void update(){
		String id = getPara();
		SmsTemplate sms = SmsTemplate.dao.findById(id);
		setAttr("sms",sms);
		setAttr("type","update");
		renderJsp("/operator/layer_smsTemplate_form.jsp");
	}
	/**
	 * 保存新添加或修改的模板
	 */
	public void save(){
		JSONObject json = new JSONObject();
		SmsTemplate sms = getModel(SmsTemplate.class);
		String id = getPara("smsTemplate.id");
		try{
			if(id.equals("")){
				sms.save();
			}else{
				sms.update();
			}
			json.put("code",1);
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}
	/**
	 * 改变模板的使用状态
	 */
	public void updateState(){
		JSONObject json = new JSONObject();
		try{
			String[] id = getPara("id").toString().split(",");
			if(id[1].equals("1")){
				SmsTemplate sms = SmsTemplate.dao.findById(id[0]);
				sms.set("sms_state", 1).update();
			}else if(id[1].equals("2")){
				SmsTemplate sms = SmsTemplate.dao.findById(id[0]);
				sms.set("sms_state", 0).update();
			}
			json.put("code", 1);
		} catch (Exception e){
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}
	/**
	 * 删除模板
	 */
	public void deleteTemplate(){
		JSONObject json = new JSONObject();
		try{
			String id = getPara("id");
			SmsTemplate sms = SmsTemplate.dao.findById(id);
			if(sms.getInt("sms_state")==1){
				json.put("code", 2);
			}else{
				sms.delete();
				json.put("code", 1);
			}
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}
}
