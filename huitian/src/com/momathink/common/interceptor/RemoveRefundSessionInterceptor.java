package com.momathink.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolContext;

/**
 * 去除退费的时候产生的Session
 * 2015年7月18日prq
 * @author prq
 *
 */

public class RemoveRefundSessionInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation ai) {
		BaseController contro = (BaseController) ai.getController();
		HttpServletRequest request = contro.getRequest();
		
		String uri = ai.getActionKey(); // 默认就是ActionKey
		if(ai.getMethodName().equals("toUrl")){
			uri = ToolContext.getParam(request, "toUrl"); // 否则就是toUrl的值
		}
		
		if(uri.equals("/main/getMessage")||uri.equals("/operator/getHeadMessage")||uri.equals("/finance/toDoRefund")||uri.equals("/finance/submitRefundRecord")){
			
		}else{
			contro.removeSessionAttr("refund");
		}
		
		try {
			ai.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
	}
	
}
