package com.momathink.finance.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.finance.model.Items;
import com.momathink.finance.model.ItemsOutIn;
import com.momathink.finance.service.ItemsService;
import com.momathink.sys.system.model.SysUser;

/**
 * 2015年10月26日
 * @author prq
 *物品管理
 */

@Controller(controllerKey="/itemsManager")
public class ItemsManageController extends com.momathink.common.base.BaseController {
	
	/**
	 * 物品列表   分页
	 */
	public void list(){
		ItemsService.service.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/items_list.jsp");
	}
	
	/**
	 * 添加物品
	 */
	public void add(){
		List<SysUser> sysUserList = SysUser.dao.getSysUser(); 
		setAttr("stafflist",sysUserList);
		setAttr("operatorType", "add");
		render("/finance/layer_item_form.jsp");
	}
	
	/**
	 * 保存物品
	 */
	public void save(){
		JSONObject json = new JSONObject();
		try{
			Items items = getModel(Items.class);
			ItemsService.service.save(items,getAttr("cUserIds").toString());
			json.put("code", 1);
			json.put("msg", "添加成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "添加数据异常，请联系管理员");
		}
		renderJson(json);
		
	}
	
//	public void valiName(){
//		String name = getPara("name");
//		String ids = getPara("ids");
//		int count = ItemsService.service.valiName(name,ids);
//		renderJson(count+"");
//	}
	
	/**
	 * 编辑物品信息
	 */
	public void edit(){
		Items items = Items.dao.findById(getPara());
		setAttr("items",items);
		List<SysUser> sysUserList = SysUser.dao.getSysUser(); 
		setAttr("stafflist",sysUserList);
		setAttr("operatorType", "update");
		render("/finance/layer_item_form.jsp");
	}
	
	public void update(){
		JSONObject json = new JSONObject();
		try{
			Items items = getModel(Items.class);
			ItemsService.service.update(items,getAttr("cUserIds").toString());
			json.put("code", 1);
			json.put("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "修改数据异常，请联系管理员");
		}
		renderJson(json);
	}
	
	/**
	 * instorage入库
	 */
	public void toItemsIn(){
		Items item  = Items.dao.getItemsById(getPara());
		setAttr("items",item);
		setAttr("operatorType", "add");
		render("/finance/layer_itemin_form.jsp");
	}
	
	public void saveItemsIn(){
		ItemsOutIn outIn = getModel(ItemsOutIn.class);
		Items item = Items.dao.findById(outIn.getStr("itemids"));
		String userIds = getAttr("cUserIds").toString();
		boolean flag = ItemsService.service.itemsIn(item,outIn,userIds);
		renderJson(flag);
	}
	
	/**
	 * 编辑入库
	 */
	public void editItemsIn(){
		ItemsOutIn item = ItemsOutIn.dao.getItemsOutInDetailIn(getPara());
		setAttr("items",item);
		setAttr("operatorType", "update");
		render("/finance/layer_itemin_form.jsp");
	}
	
	/**
	 * 更新入库
	 */
	public void updateItemsOutIn(){
		ItemsOutIn detail = getModel(ItemsOutIn.class);
		String userIds = getAttr("cUserIds").toString();
		boolean flag = ItemsService.service.updateItemsOutIn(detail,userIds);
		renderJson(flag);
	}
	
	/**
	 * outstorage出库
	 */
	public void toItemsOut(){
		Items item  = Items.dao.getItemsById(getPara());
		setAttr("items",item);
		setAttr("operatorType", "add");
		render("/finance/layer_itemout_form.jsp");
	}
	
	
	
	public void saveItemsOut(){
		Items item = getModel(Items.class);
		ItemsOutIn out = getModel(ItemsOutIn.class);
		boolean flag = ItemsService.service.saveItemsOut(item,out,getAttr("cUserIds").toString());
		renderJson(flag);
	}
	
	/**
	 * 编辑出库
	 */
	public void editItemsOut(){
		ItemsOutIn out = ItemsOutIn.dao.getItemsOutInDetailOut(getPara());
		setAttr("items",out);
		setAttr("operatorType", "update");
		render("/finance/layer_itemout_form.jsp");
	}
	
	/**
	 * 更新出库
	 */
	public void updateItemsOut(){
		ItemsOutIn out = getModel(ItemsOutIn.class);
		boolean flag  = ItemsService.service.updateItemsOut(out,getAttr("cUserIds").toString());
		renderJson(flag);
	}
	
	/**
	 * 出入库明细 分页 
	 */
	public void outInDetail(){
		ItemsService.service.outInList(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/itemsOutIn_list.jsp");
	}
	
	/**
	 * 删除出入库
	 */
	public void deleteOutInItems(){
		String ids = getPara("ids");
		JSONObject json = new JSONObject();
		boolean flag = ItemsService.service.deleteOutInItems(ids);
		if(flag){
			json.put("code", "0");
		    json.put("msg", "删除成功！");
		}else{
			json.put("code", "1");
			json.put("msg", "修改数据异常，请联系管理员！");
		}
		renderJson(json);
	}
	
//	public void applyClassesOptions(){
//		JSONObject json = new JSONObject();
//		String number = getPara("number");
//		StringBuffer data = ItemsService.service.applyClassesOptions(number,null);
//		json.put("data",data.toString());
//		renderJson(json);
//	}
//	
//	/**
//	 * 班次预算
//	 */
//	public void applyClassBudgetOptions(){
//		JSONObject json = new JSONObject();
//		StringBuffer data = ItemsService.service.applyClassesBudgetOptions(getPara("classids"),null,getPara("itemsids"));
//		json.put("data",data.toString());
//		renderJson(json);
//	}
//	
//	public void changeBudget(){
//		JSONObject json = new JSONObject();
//		StringBuffer data = BudgetService.service.budgetItemInputs(getPara("budgetids"),getPara("itemsids"));
//		json.put("data", data.toString());
//		renderJson(json);
//	}
//	
//	/**
//	 * 头像列表
//	 */
//	public void filelist(){
//		String autid = getPara();
//		setAttr("filelist", MyFileService.service.filelist(autid, "Authentication"));
//		setAttr("autid", autid);
//		setAttr("addfileuri", "/finance/itemsManager/addfile/" + autid);
//		setAttr("filelisturi", "/finance/itemsManager/filelist/" + autid);
//		render("/jiaoxue/myfile/filelist.html");
//	}
//	
//	/**
//	 * 上传保存头像
//	 */
//	public void addfile(){
//		String msg = "文件类型不符合上传要求,请重新选择文件";
//		try {
//			Integer max = (Integer)PropertiesPlugin.getParamMapValue(DictKeys.config_maxPostSize_key);
//			String uir = MyFileService.service.getPhotoFileURLDate();
//			
//			UploadFile file = getFile("fileField", PathKit.getWebRootPath() + uir, max);
//			uir = uir.replaceAll("\\\\", "/");
//			MyFile af = getModel(MyFile.class);
//			setAttr("msg",MyFileService.service.savefile(file, af, uir, "Authentication") == true ?"上传成功":"抱歉上传失败请重试!");
//		} catch (Exception e) {
//			setAttr("msg",msg);
//		}finally{
//			render("/jiaoxue/myfile/import_file_alert.html");
//		}
//	}
//	/**
//	 * 查看图片，以及物品信息
//	 */
//	public void selectPhoto(){
//		String autids = getPara(0);
//		setAttr("PhotoList", Items.dao.getAllWuPinPhoto(autids));
//		Items items = Items.dao.findById(getPara());
//		setAttr("stafflist", Staff.dao.getAllStaff());
//		setAttr("items",items);
//		render("/finance/layer_showPhoto.html");
//	}
	
	

}
