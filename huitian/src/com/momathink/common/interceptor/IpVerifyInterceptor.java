package com.momathink.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class IpVerifyInterceptor implements Interceptor{
	private static final Logger LOGGER = Logger.getLogger(IpVerifyInterceptor.class);
	@Override
	public void intercept(Invocation ai) {
		HttpServletRequest request = ai.getController().getRequest();
		String ip = request.getRemoteHost();
		String sql = "select name from ipaddress where status = 1 and name like '%" + ip + "%' limit 1";
		Record records = Db.findFirst(sql);
		if(records!=null){
			ai.invoke();
		}else{
			LOGGER.info("Illegal server requests from:" + ip);
			JSONObject json = new JSONObject();
			json.put("code", "1");
			json.put("msg", "错误的IP地址请求！");
			ai.getController().renderJson(json);
		}
	
	}

}
