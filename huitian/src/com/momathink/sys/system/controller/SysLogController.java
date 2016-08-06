package com.momathink.sys.system.controller;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.service.SysLogService;
/**
 * 2015年7月13日prq
 * @author prq
 *
 */
@Controller(controllerKey="/syslog")
public class SysLogController extends BaseController {
	SysLogService syslogService = new SysLogService();
	
	public void index(){
		syslogService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		setAttr("roles",Role.dao.getAllRoleNost());
		render("/sysuser/syslog_list.jsp");
	}
	/**
	 * 清空所有数据
	 */
	public void deleteAllMessage(){
		Db.update("truncate table pt_syslog ");
		renderJson("1");
	}

}
