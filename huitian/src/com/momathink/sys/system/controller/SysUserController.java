package com.momathink.sys.system.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.service.SysUserService;
import com.momathink.teaching.campus.model.Campus;

@Controller(controllerKey = "/sysuser")
public class SysUserController extends BaseController {
	private static final Logger logger = Logger.getLogger(SysUserController.class);
	private SysUserService sysuserService = new SysUserService();

	public void index() {
		sysuserService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		setAttr("roles",Role.dao.getAllRoleNost());
		render("/sysuser/sysuser_list.jsp");
	}

	/**
	 * 检查是否存在
	 */
	public void checkExist() {
		String field = getPara("checkField");
		String value = getPara("checkValue");
		String id = getPara("id");
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			Long count = SysUser.dao.queryCount(field, value, id);
			renderJson("result", count);
		} else {
			renderJson("result", null);
		}
	}
	/**
	 * 添加系统用户*
	 */
	public void add() {
		try {
			List<Campus> clist = Campus.dao.getCampus();
			setAttr("campusList", clist);
			setAttr("roles",Role.dao.getAllRoleNost());
			renderJsp("/sysuser/sysuser_form.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void edit() {
		try {
			SysUser sysuser = SysUser.dao.findById(getParaToInt());
			setAttr("sysuser", sysuser);
			List<Campus> clist = Campus.dao.getCampus();
			List<AccountCampus> accountcampus = AccountCampus.dao.getCampusidsByAccountid(getParaToInt());
			String campusids = "|";
			if(!accountcampus.isEmpty()){
				for(AccountCampus c :accountcampus){
					campusids+=c.getInt("campus_id")+"|";
				}
			}
			//回填按校区区分是否显示全部信息
			for(Campus campus :clist){
				Integer campusid = campus.getInt("id");
				campus.put("showall", 0);
				for(AccountCampus c :accountcampus){
					Integer aCampusid = c.getInt("campus_id");
					if(campusid==aCampusid){
						campus.put("showall", 1);
					}
				}
			}
			
			setAttr("campusids",campusids);
			setAttr("campusList", clist);
			setAttr("roles",Role.dao.getAllRoleNost());
			renderJsp("/sysuser/sysuser_form.jsp");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存用户
	 */
	public void save() {
		try {
			SysUser sysuser = getModel(SysUser.class);
			sysuser.set("user_pwd", ToolMD5.getMD5("111111"));
			sysuser.set("create_time", new Date());
			sysuser.set("createuserid", getSysuserId());
			int id = sysuserService.save(sysuser);
			
			String str = getPara("campusids");
			if(!str.isEmpty()){
				String[] campusid = str.replace("|", ",").split(",");
				for(int i=0;i<campusid.length;i++){
					AccountCampus ac = new AccountCampus();
					ac.set("account_id", id).set("campus_id",campusid[i]).save();
				}
			}
			redirect("/sysuser/index");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 更新用户
	 */
	public void update() {
		try {
			SysUser sysuser = getModel(SysUser.class);
			sysuserService.update(sysuser);
			String str = getPara("campusids");
			String id = getPara("sysUser.id");
			List<AccountCampus> acc= AccountCampus.dao.getAllCampusidByAccountid(id);
			if(!acc.isEmpty()){
				for(AccountCampus a:acc){
					a.delete();
				}
			}
			if(!str.isEmpty()){
				String[] campusid = str.replace("|", ",").split(",");
					for(int i=0;i<campusid.length;i++){
							AccountCampus ac = new AccountCampus();
							ac.set("account_id", id).set("campus_id",campusid[i]).save();
					}
			}
			redirect("/sysuser/index");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void freeze() {
		try {
			int state = getParaToInt("state");
			int id = getParaToInt("sysuserId");
			SysUser sysuser = SysUser.dao.findById(id);
			sysuser.set("state", state);
			sysuserService.update(sysuser);
			renderJson("result", "true");
		} catch (Exception e) {
			logger.error("AccountController.freezeAccount", e);
		}
	}

	public void changePassword() {
		JSONObject json = new JSONObject();
		try {
			Integer id = getParaToInt("id");
			String pwd = getPara("password");
			SysUser user = SysUser.dao.findById(id);
			user.set("USER_PWD", ToolMD5.getMD5(pwd));
			sysuserService.update(user);
			json.put("result", true);
		} catch (Exception ex) {
			logger.error("error", ex);
			json.put("result", false);
		}
		renderJson(json);
	}

	/*
	 * 跟去校区ID获取课程顾问
	 */
	public void getKcge() {
		JSONObject json = new JSONObject();
		String id = getPara("id");
		String oppkcuserid = getPara("oppkcuserid");
		String oppscuserid = getPara("oppscuserid");
		int sysid = getSysuserId();
		if (id != null) {
			List<SysUser> account = SysUser.dao. getKechengguwenCampus(id);
			List<SysUser> shichang = SysUser.dao.getShichangByCampusid(id);
			json.put("accountList", account);
			json.put("shichangList", shichang);
		} else {
			List<SysUser> account = SysUser.dao.getAllKcgw();
			json.put("accountList", account);
		}
		setAttr("roles",Role.dao.getAllRole());
		json.put("oppkcuserid", oppkcuserid);
		json.put("oppscuserid", oppscuserid);
		json.put("sysid", SysUser.dao.findById(sysid));
		renderJson(json);
	}
	/**
	 * 保存用户基本信息
	 */
	public void basePersonMessage(){
		try{
			String id= getPara("id");
			String name= getPara("name");
			String tel= getPara("tel");
			String email= getPara("email");
			String sex= getPara("sex");
			SysUser user = SysUser.dao.findById(id);
			if(user!=null){
				user.set("real_name",name).set("tel",tel).set("email",email).set("sex",sex).update();
			}
			renderJson(1);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(0);
		}
	}
	
	public void updateNetSchoolId() {
		try {
			String jwUserId = getPara( "jwSysUserId" );
			String netSchoolId = getPara( "netAccountId" );
			SysUser user = SysUser.dao.findById( jwUserId );
			if( user != null ) {
				user.set( "netschoolid" , netSchoolId );
				user.update();
			}
		} catch (Exception e) {
			logger.error( "updateNetSchoolId" , e );
		}
		renderJson( "flag" , true );
	}
}
