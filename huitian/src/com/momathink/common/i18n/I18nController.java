package com.momathink.common.i18n;


import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
/**
 * 
 * 2015年7月28日
 * @author prq
 *
 */
@Controller(controllerKey="/i18n")
public class I18nController extends BaseController {
	
	public void setI18nCookies(){
		String status = getPara("_locale");
		if(status.equals("zh_CN")){
			setSessionAttr("en",1);
		}else{
			setSessionAttr("en",2);
		}
		renderJson("code",1);
		
	}
	
}
