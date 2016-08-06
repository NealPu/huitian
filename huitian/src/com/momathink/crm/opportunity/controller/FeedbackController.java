package com.momathink.crm.opportunity.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.crm.opportunity.model.Feedback;

/**
 * 销售机会
 * @author David
 *
 */
@Controller(controllerKey = "/feedback")
public class FeedbackController extends BaseController {
	/**
	 * 近期反馈
	 */
	public void queryFeedbackJson(){
		try{
			Integer count = getParaToInt("count");
			List<Feedback> list= Feedback.dao.findByLimit(getSysuserId(),count);
			JSONObject json = new JSONObject();
			if(list!=null){
				String code="1";//0失败1成功
				json.put("code", code);
			}else{
				String code="0";//0失败1成功
				String msg = "无最近的销售机会记录！";
				json.put("msg", msg);
				json.put("code", code);
			}
			json.put("list", list);
			renderJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
