package com.momathink.weixin.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.weixin.model.Menu;
import com.momathink.weixin.model.Wpnumber;
import com.momathink.weixin.service.MenuService;
import com.momathink.weixin.tools.ToolMenu;
import com.momathink.weixin.tools.ToolWeiXin;
import com.momathink.weixin.vo.message.RecevieToken;

/**
 * 微信自定义菜单管理
 * ClassName: menuController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年8月9日 下午5:10:21 <br/>
 *
 * @author David
 * @version 
 * @since JDK 1.7
 */
@Controller(controllerKey = "/weixin/menu")
public class MenuController extends BaseController {

	private static Logger log = Logger.getLogger(MenuController.class);
	
	private MenuService menuService = new MenuService();
	private String menuIds;
	public void index(){
		String wpnumberid = getPara("wpnumberid");
		List<Wpnumber> wpnumberList = Wpnumber.dao.getAllWpnumbers();
		if(!StringUtils.isEmpty(wpnumberid)){
			List<Menu> menuList = menuService.getAllMenuList(wpnumberid);
			setAttr("menuList", menuList);
			setAttr("wpnumberid", wpnumberid);
		}
		setAttr("wpnumberList", wpnumberList);
		render("/weixin/menu/menu_list.jsp");
	}
	
	/**
	 * 用户树ztree节点数据
	 */

	public void treeData() {
		String json = menuService.parentNodeData(menuIds);
		renderJson(json);
	}
	
	public void add(){
		List<Menu> menuList =  new ArrayList<Menu>();
		List<Wpnumber> wpnumberList = new ArrayList<Wpnumber>();
		String pId = getPara("firstMenuId");
		String wpnumberId = getPara("wpnumberId");
		if(StringUtils.isEmpty(pId)){
			setAttr("menu",new Menu().set("menutype", 0).set("wpnumberid", wpnumberId));
			menuList = Menu.dao.findFirstMenu(wpnumberId);
		}else{
			Menu menu = Menu.dao.findById(Integer.parseInt(pId));
			if(menu!=null){
				menuList.add(menu);
				wpnumberList.add(Wpnumber.dao.findById(menu.getInt("wpnumberid")));
			}
			setAttr("menu",new Menu().set("parentid", pId).set("menutype", 1).set("wpnumberid", menu.getInt("wpnumberid")));
		}
		setAttr("wpnumberList", wpnumberList);
		setAttr("operatorType","add");
		setAttr("menuList",menuList);
		render("menu_form.jsp");
	}
	
	public void save() {
		JSONObject json = new JSONObject();
		String code="0";
		String msg="添加成功，请重新查询一下！";
		try{
			Menu menu = getModel(Menu.class);
			menuService.save(menu);
			code="1";
		} catch (Exception e) {
			e.printStackTrace();
			code="0";
			msg="数据更新异常，请联系系统管理员！";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	public void view() {
		Menu menu = Menu.dao.findById(getPara());
		setAttr("menuList",Menu.dao.findFirstMenu(menu.getInt("wpnumberid").toString()));
		setAttr("menu", menu);
		setAttr("operatorType","view");
		render("/weixin/menu/menu_form.jsp");
	}
	public void edit() {
		Menu menu = Menu.dao.findById(getPara());
		setAttr("menuList",Menu.dao.findFirstMenu(menu.getInt("wpnumberid").toString()));
		setAttr("menu", menu);
		setAttr("operatorType","update");
		render("/weixin/menu/menu_form.jsp");
	}
	

	/**
	 * 更新 
	 * @author David
	 * @since JDK 1.7
	 */
	public void update() {
		JSONObject json = new JSONObject();
		String code="0";
		String msg="更新成功！";
		try {
			Menu menu=getModel(Menu.class);
			String oauth=menu.getStr("isoauth");
			if("1".equals(oauth)){
				menu.set("url", null);
				menu.set("key", null);
				menu.set("type",null);
			}else{
				menu.set("redirect_uri", null);
				menu.set("state", null);
			}
			menuService.update(menu);
			code="1";
		} catch (Exception e) {
			e.printStackTrace();
			code="0";
			msg="数据更新异常，请联系系统管理员！";
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	public void delete() {
		JSONObject json = new JSONObject();
		//取出ids
		String id = getPara("menuid");
		if (!StringUtils.isEmpty(id)) {
			List<Menu> childMenu = Menu.dao.findChildMenuById(id);
			if(childMenu == null || childMenu.size()==0){
				menuService.delete(id);
				json.put("code", "0");
				json.put("msg", "删除成功");
			}else{
				json.put("code", "1");
				json.put("msg", "请先删除子菜单");
			}
		} else {
			json.put("code", "1");
			json.put("msg", "菜单不存在");
		}
		renderJson(json);
	}
	public void changeMenuStatus() {
		JSONObject json = new JSONObject();
		String id = getPara("menuId");
		if (!StringUtils.isEmpty(id)) {
			Menu menu = Menu.dao.findById(Integer.parseInt(id));
			if(menu == null){
				json.put("code", "1");
				json.put("msg", "菜单不存在");
			}else{
				menu.set("status", getPara("status"));
				menu.update();
				json.put("code", "1");
				json.put("msg", "修改成功");
			}
		} else {
		}
		renderJson(json);
	}
	
	/**
	 * 与微信服务菜单同步
	 * @author David
	 * @since JDK 1.7
	 */
	public void synchronizeMenu(){
		String wpnumberid = getPara("wpnumberid");
		Wpnumber wpnumber = Wpnumber.dao.findById(Integer.parseInt(wpnumberid));
		JSONObject json = new JSONObject();
		String jsonmenu = menuService.queryAllMenuJSON(wpnumberid);
		RecevieToken at = ToolWeiXin.getAccessToken(wpnumber.getStr("appid"),wpnumber.getStr("appsecret"));
		boolean isok = ToolMenu.createMenu(jsonmenu, at.getAccess_token());
		if(isok){
			json.put("code", "1");
			json.put("msg", "同步成功");
			log.info("微信创建自定义菜单成功");
		} else{
			json.put("code", "0");
			json.put("msg", "同步失败");
		}
		renderJson(json);
	}
}
