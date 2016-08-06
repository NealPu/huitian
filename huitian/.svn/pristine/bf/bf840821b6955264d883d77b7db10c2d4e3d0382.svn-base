package com.momathink.crm.mediator.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolMonitor;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.mediator.model.Company;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.mediator.service.CompanyService;
@Controller(controllerKey = "/company")
public class CompanyController extends BaseController {
	private static Logger log = Logger.getLogger(CompanyController.class);

	private CompanyService companyService = new CompanyService();

	/**
	 * 列表分页
	 */
	public void index() {
		log.debug("机构列表：分页");
//		Map<String,String> queryParam = splitPage.getQueryParam();
//		if(!StringUtils.isEmpty(getSysuserId().toString())){
//			SysUser user = SysUser.dao.findById(getSysuserId());
//			if(Role.isShichang(user.getStr("roleids"))){
//				queryParam.put("userid", user.getInt("Id").toString());
//			}else{
//				queryParam.put("userid", null);
//			}
//		}
		companyService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/mediator/company_list.jsp");
	}
	
	/**
	 * 跳转添加
	 */
	public void add(){
		setAttr("operatorType", "add");
		render("/mediator/layer_company_form.jsp");
	}
	
	/**
	 * 保存数据
	 */
	public void save(){
		JSONObject json = new JSONObject();
		try{
			Company company = getModel(Company.class);
			company.set("createuserid", getSysuserId());
			companyService.save(company);
			json.put("code", 1);
			json.put("msg", "添加成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "数据保存异常，请联系管理员");
		}
		renderJson(json);
	}
	/**
	 * 准备更新
	 */
	public void edit() {
		Company company = Company.dao.findById(getPara());
		setAttr("company", company);
		setAttr("operatorType", "update");
		render("/mediator/layer_company_form.jsp");
	}
	/**
	 * 删除机构
	 * */
	public void delete(){
		//1、判断是否有顾问关联了company 如果管理了返回false
		String companyId = getPara("companyId");
		JSONObject json = new JSONObject();
		Long count = Company.dao.getUseCompanyMediators(Integer.parseInt(companyId));
		if(count==0){
		//2、如果没有关联进行删除，返回true
		//删除公司机构
			Company.dao.deleteById(Integer.parseInt(companyId));
			json.put("code", "0");
		    json.put("msg", "删除成功！");
		}else{
			json.put("code", "1");
			json.put("msg", "已有关联顾问无法删除！");
		}
		renderJson(json);
	}

	/**
	 * 更新数据
	 */
	public void update() {
		JSONObject json = new JSONObject();
		try{
			Company company = getModel(Company.class);
			companyService.update(company);
			json.put("code", 1);
			json.put("msg", "更新成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "数据更新异常，请联系管理员");			
		}
		renderJson(json);
	}
	/**
	 * 检查是否存在
	 */
	public void checkExist() {
		String field = getPara("checkField");
		String value = getPara("checkValue");
		String companyId = getPara("companyId");
		if(!ToolString.isNull(field)&&!ToolString.isNull(value)){
			Long count = Company.dao.queryCompanyCount(field,value,companyId,getSysuserId().toString());
			renderJson("result",count);
		}else{
			renderJson("result",null);
		}
	}
	
	/**
	 * 根据名称获取对象模糊查询--查询所属机构
	 */
	public void getCompanyByNameLike() {
		try {
			String companyname = getPara("companyname");
			List<Company> list = Company.dao.findByName(companyname);
			renderJson("companys", list);
		} catch (Exception ex) {
			log.error(ex.toString());
		}
	}
	
	/**
	 * 系统过期需激活界面
	 */
	public void expiration(){
		setAttr("licenseKey", Organization.dao.getOrganizationMessage().get("licenseKey"));
		render("/common/expiration.jsp");
	}
	
	/**
	 * 保存过期日期钥匙
	 */
	public void saveExpiration(){
		String licenseKey = getPara("licenseKey");
		String expirationDateKeys = getPara("expirationDateKeys");
		if(ToolMonitor.whetherExpired(new Date())){
			Organization.dao.getOrganizationMessage().set("licenseKey", licenseKey).set("expirationDateKeys", expirationDateKeys).update();
			ToolMonitor.refresh();
			log.info("授权码修改成功");
			redirect("/account/login");
		}else{
			log.info("授权未过期，请到系统机构修改");
			redirect("/organization/index");
		}
	}
}
