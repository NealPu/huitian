package com.momathink.weixin.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.weixin.model.Wpnumber;
import com.momathink.weixin.service.UserService;
@Controller(controllerKey="/weixin/user")
public class UserController extends BaseController{

	private UserService userservice=new UserService();
	
	public void index(){
		List<Wpnumber> wpnumbers = Wpnumber.dao.getAllWpnumbers();
		userservice.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		setAttr("wpnumberList", wpnumbers);
		renderJsp("/weixin/user/user_list.jsp");
	}
	/**
	 * 用户绑定
	 */
	public void band(){
		Integer id=getParaToInt(0);
		String sql="select wu.nickname,IF(ISNULL(cm.realname),0,1) isbound,from_unixtime(wu.subscribeTime) guanzhuTime,IF (wu.sex = 2, '女', '未知') xingbie from wx_user wu left join crm_mediator cm on wu.openId=cm.openid where wu.id=?";
		String sql2="select realname,openid from crm_mediator where openid is NULL";
		List<Record> crmlist=Db.find(sql2);
		List<Record> list=Db.find(sql, id);
		setAttr("userlist", list);
		setAttr("crmlist", crmlist);
		setAttr("id", id);
		renderJsp("/weixin/user/user_band.jsp");
	}
	/**
	 * 绑定成功
	 */
	public void bandok(){
		JSONObject json=new JSONObject();
		String realname=getPara("realname");
		Integer id=getParaToInt("id");
		String sql="select openId from wx_user where id=?";
		String sql2="select id from crm_mediator where realname=?";
		String openId=Db.findFirst(sql, id).get("openId");
		Integer cid=Db.findFirst(sql2,realname).getInt("id");
		String code="0";
		String msg="绑定失败！";
		try{
			Mediator mediator=getModel(Mediator.class);
			mediator.set("id", cid);
			mediator.set("openid", openId);
			if(mediator.update()){
				code="1";
				msg="添加成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	/**
	 * 用户解绑
	 */
	public void noband(){
		Integer id=getParaToInt("id");
		JSONObject json=new JSONObject();
		boolean result=false;
		try{
			Mediator mediator=getModel(Mediator.class);
			mediator.set("id", id);
			mediator.set("openid", null);
			if(mediator.update()){
				result=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		json.put("result",result);
		renderJson(json);
	}
}
