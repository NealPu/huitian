package com.momathink.finance.controller;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.finance.service.TeachercheckeService;

/**
 * 薪资管理
 * 
 * @author David
 */
@Controller(controllerKey = "/teacherchecke")
public class TeachercheckeController extends BaseController {
	private TeachercheckeService teachercheckeService = new TeachercheckeService();

	/**
	 * 教师考勤
	 * 
	 * @author David
	 */
	public void index() {
		// String yuefen = getPara("_query.date");
		// List<Salary> salarylist =
		// Salary.dao.find("SELECT * FROM salary WHERE stat_time = ?",yuefen);
		teachercheckeService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		// setAttr("salarylist", salarylist);
		render("/finance/teacher_check.jsp");
	}

}
