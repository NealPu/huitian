package com.momathink.crm.mediator.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.mediator.model.Company;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.service.MediatorService;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

/**
 * 顾问管理 ClassName: CounselorController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年8月11日 上午10:43:49 <br/>
 *
 * @author David
 * @version
 * @since JDK 1.7
 */
@Controller(controllerKey = "/mediator")
public class MediatorController extends BaseController {

	private static Logger log = Logger.getLogger(MediatorController.class);

	private MediatorService mediatorService = new MediatorService();
	public void index() {
		log.debug("营销顾问管理：分页");
		List<SysUser> sysUserList = SysUser.dao.getScuserid();//获取市场
		Map<String,String> queryParam = splitPage.getQueryParam();
		if(!StringUtils.isEmpty(getSysuserId().toString()))
			queryParam.put("userid", getSysuserId().toString());
		mediatorService.list(splitPage);
		setAttr("sysUserList", sysUserList);
		setAttr("showPages", splitPage.getPage());
		setAttr("roles",Role.dao.getAllRole());
		render("/mediator/mediator_list.jsp");
	}

	/**
	 * 保存顾问*
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void save() {
		JSONObject json = new JSONObject();
		try{
			Mediator mediator = getModel(Mediator.class);
			String companyname = getPara("companynames");
			Integer companyId = null;
			if(!companyname.equals("")){
				Company  c = Company.dao.findByNames(companyname.trim());
				if(c!=null){
					companyId=c.getInt("id");
				}else{
					Company company = new Company();
					company.set("companyname", companyname).save();
					companyId = company.getPrimaryKeyValue();
				}
				
			}
			mediator.set("companyid",companyId);
			mediatorService.save(mediator);
			json.put("code", 1);
			json.put("msg", "添加成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "添加数据异常，请联系管理员");
		}
		renderJson(json);
	}
	
	/**
	 * 查看渠道详情*
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public synchronized void view() {
		Mediator mediator = Mediator.dao.findById(getPara());
		List<SysUser> sysUserList = SysUser.dao.getScuserid();
		setAttr("mediator", mediator);
		setAttr("sysUserList", sysUserList);
		String bili = mediator.get("ratio")==null?" ":(mediator.get("ratio")+"%");
		setAttr("bili", bili);
		renderJsp("/mediator/layer_mediatorMessage.jsp");
	}

	/**
	 * 添加渠道*
	 */
	public void add() {
		List<Mediator> list;
		List<SysUser> sysUserList = SysUser.dao.getScuserid(); 
		SysUser user = SysUser.dao.findById(getSysuserId().toString());
		if(Role.isShichang(user.getStr("roleids"))){
			list = Mediator.dao.findMediatorIdAndName(getSysuserId().toString());
		}else{
			list = Mediator.dao.findMediatorIdAndName(null);
		}
		setAttr("mediatorList", list);//推荐人
		setAttr("sysUserList", sysUserList);//市场
		setAttr("operatorType", "add");
		render("/mediator/layer_mediator_form.jsp");
	}

	/**
	 * 准备更新渠道*
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void edit() {
		Mediator mediator = Mediator.dao.findById(getPara());
		if(mediator.get("companyid")!=null&&mediator.get("companyid")!=""){
			Company c = Company.dao.findById(mediator.getInt("companyid"));
			if(c!=null){
				setAttr("companynames",c.getStr("companyname"));
			}else{
				setAttr("companynames","");
			}
		}
		List<Mediator> list = Mediator.dao.getParentMediator(getPara("id"),getSysuserId().toString());
		List<SysUser> sysUserList =SysUser.dao.getScuserid();
		double ratio = mediator.getBigDecimal("ratio")==null?0:mediator.getBigDecimal("ratio").doubleValue();
		mediator.set("ratio", ToolArith.mul(ratio, 100));
		setAttr("sysUserList", sysUserList);
		setAttr("mediatorList", list);
		setAttr("mediator", mediator);
		setAttr("operatorType", "update");
		render("/mediator/layer_mediator_form.jsp");
	}

	/**
	 * 更新
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void update() {
		JSONObject json = new JSONObject();
		try{
			Mediator mediator = getModel(Mediator.class);
			String companyname = getPara("companynames");
			Integer companyId = null;
			if(!companyname.equals("")){
				Company  c = Company.dao.findByNames(companyname.trim());
				if(c!=null){
					companyId=c.getInt("id");
				}else{
					Company company = new Company();
					company.set("companyname", companyname).save();
					companyId = company.getPrimaryKeyValue();
				}
				
			}
			mediator.set("companyid",companyId);
			mediatorService.update(mediator);
			json.put("code", 1);
			json.put("msg", "数据更新成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "数据更新异常");
		}
		renderJson(json);
	}

	/**
	 * 检查是否存在
	 */
	public void checkExist() {
		String field = getPara("checkField");
		String value = getPara("checkValue");
		String mediatorId = getPara("mediatorId");
		if(!ToolString.isNull(field)&&!ToolString.isNull(value)){
			Long count = Mediator.dao.queryMediatorCount(field,value,mediatorId);
			renderJson("result",count);
		}else{
			renderJson("result",null);
		}
	}
	
	/**
	 * 设置返佣比例
	 */
	public synchronized void transferToCommission(){
		try{
			String mediatorId = getPara();
			Mediator mediator = Mediator.dao.getMediatorById(Integer.parseInt(mediatorId));
			double ratio = mediator.getBigDecimal("ratio")==null?0:mediator.getBigDecimal("ratio").doubleValue();
			mediator.set("ratio", ToolArith.mul(ratio, 100));
			setAttr("mediator", mediator);
			render("/mediator/setCommission.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 更改返佣比例
	 */
	public synchronized void setCommission(){
		JSONObject json = new JSONObject();
		String msg="设置成功";
		String code="1";
		Mediator mediator = getModel(Mediator.class);
		try{
			mediator.set("ratio", ToolArith.div(mediator.getBigDecimal("ratio").doubleValue(), 100, 5));
			mediatorService.update(mediator);
			code="1";
			msg="设置成功.";
		}catch(Exception ex){
			ex.printStackTrace();
			code="0";
			msg="设置出错，请联系管理员.";
		}
		json.put("mediator", mediator);
		json.put("msg", msg);
		json.put("code", code);
		renderJson(json);
	}
	/*
	 * 模糊查询渠道回填选择*
	 */
	public void getLikeMediatorName(){
		JSONObject json = new JSONObject();
		try{
			String name=getPara("name");
			String scuserid=getPara("scuserid");
			List<Mediator>  namelist = new ArrayList<Mediator>();
			if(!ToolString.isNull(scuserid)){
				namelist = Mediator.dao.findByLikeName(name,scuserid);
			}else{
				namelist = Mediator.dao.findByLikeName(name);
			}
			json.put("code", 1);
			json.put("namelist", namelist);
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
		}
		renderJson(json);
	}
}
