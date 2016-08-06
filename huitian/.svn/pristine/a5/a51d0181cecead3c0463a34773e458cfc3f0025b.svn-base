package com.momathink.sys.operator.controller;

import java.util.List;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.operator.model.Module;
import com.momathink.sys.operator.service.ModuleService;
/**
 * 根据单击的模块标题获取其应有的模块下的功能*
 */
@Controller(controllerKey="/operator/getModulCompetence")
public class ModulCompetenceController extends BaseController {
	public void index(){
		try{
			String systemsid = getPara("systemsid");
			List<Module> list = ModuleService.getFeatures(systemsid); 
			setAttr("modules",list);
		}catch(Exception e){
			e.printStackTrace();
		}
		renderJsp("/WEB-INF/view/main.jsp");
	}
}
