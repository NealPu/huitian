package com.momathink.api;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.interceptor.IpVerifyInterceptor;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.account.model.Account;

@Before(IpVerifyInterceptor.class)
@Controller(controllerKey="/api")
public class UserInfoController extends BaseController {
	public void queryAccountInfoById(){
		String code="0";
		String msg = "";
		String accountId = getPara("accountId");
		JSONObject json = new JSONObject();
		if(!ToolString.isNull(accountId)){
			Account account = Account.dao.findFirst("SELECT a.Id,a.REAL_NAME,a.SUBJECT_ID,a.CLASS_TYPE FROM account a WHERE a.STATE=0 AND a.Id="+accountId);
			if(account == null){
				code="1";
				msg="帐号不存在！";
			}else{
				json.put("user", account);
			}
		}else{
			code="1";
			msg="帐号ID不能为空";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);		
	}
	

}
