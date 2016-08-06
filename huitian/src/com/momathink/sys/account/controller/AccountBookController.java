package com.momathink.sys.account.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.account.model.AccountBook;
import com.momathink.sys.account.service.AccountBookService;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;

/**
 * 用户账本记录
 * @author David
 *
 */
@Controller(controllerKey = "/accountbook")
public class AccountBookController extends BaseController {

	private static Logger log = Logger.getLogger(AccountBookController.class);

	private AccountBookService accountBookService = new AccountBookService();

	/**
	 * 流水账目列表
	 */
	public void index() {
		log.debug("账本记录：分页");
		accountBookService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/accountbook_list.jsp");
	}

	/**
	 * 查看顾问详情
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void view() {
		Student student = Student.dao.findById(getPara());
		List<AccountBook> list = AccountBook.dao.findByAccountId(getPara());
		setAttr("accountBookList", list);
		setAttr("student", student);
		setAttr("operatorType", "view");
		render("/account/accountbook_list.jsp");
	}
	
	
	public void showCoursePlanDelete(){
		JSONObject json = new JSONObject();
		try{
			String id = getPara("courseplanid");
			CoursePlan cp = CoursePlan.coursePlan.getCoursePlanBack(id);
			json.put("planback", cp);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	public void showCoursePlan(){
		JSONObject json = new JSONObject();
		try{
			String id = getPara("courseplanid");
			CoursePlan cp = CoursePlan.coursePlan.getCoursePlan(id);
			json.put("planback", cp);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}

}
