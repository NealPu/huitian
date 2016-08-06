package com.momathink.weixin.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.weixin.model.Wpnumber;
import com.momathink.weixin.service.WpnumberService;

/**
 * WeChat public number微信公众号管理
 * @author David
 *
 */
@Controller(controllerKey = "/weixin/wpnumber")
public class WpnumberController extends BaseController {

	
	private WpnumberService wpnumberService = new WpnumberService();

	public void index(){
		setAttr("wpnumberlist", Wpnumber.dao.getAllWpnumbers());
		render("/weixin/wpnumber/wpnumber_list.jsp");
	}
	
	public void add(){
		setAttr("operatorType","add");
		render("/weixin/wpnumber/layer_wpnumber_form.jsp");
	}
	
	public void save() {

		JSONObject json = new JSONObject();
		String code="0";
		String msg="保存成功！";
		try {
			Wpnumber wpnumber=getModel(Wpnumber.class);
			wpnumberService.save(wpnumber);
			code="1";
		} catch (Exception e) {
			e.printStackTrace();
			code="0";
			msg="数据更新异常，请联系系统管理员！";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	
	}
	
	public void edit() {
		Wpnumber wpnumber = Wpnumber.dao.findById(getPara());
		setAttr("wpnumber", wpnumber);
		setAttr("operatorType","update");
		render("/weixin/wpnumber/layer_wpnumber_form.jsp");
	}
	

	/**
	 * 更新 
	 * @author David
	 * @since JDK 1.7
	 */
	public void update() {
		JSONObject json = new JSONObject();
		String code="0";
		String msg="更新成功！";
		try {
			Wpnumber wpnumber=getModel(Wpnumber.class);
			wpnumberService.update(wpnumber);
			code="1";
		} catch (Exception e) {
			e.printStackTrace();
			code="0";
			msg="数据更新异常，请联系系统管理员！";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	/**
	 * 变更公众号状态
	 */
	public void changeStatus() {
		JSONObject json = new JSONObject();
		String id = getPara("id");
		if (!StringUtils.isEmpty(id)) {
			Wpnumber wpnumber = Wpnumber.dao.findById(Integer.parseInt(id));
			if(wpnumber == null){
				json.put("code", "1");
				json.put("msg", "公众号不存在");
			}else{
				wpnumber.set("status", getPara("status"));
				wpnumber.update();
				json.put("code", "1");
				json.put("msg", "修改成功");
			}
		} else {
			json.put("code", "0");
			json.put("msg", "修改失败");
		}
		renderJson(json);
	}
	

}
