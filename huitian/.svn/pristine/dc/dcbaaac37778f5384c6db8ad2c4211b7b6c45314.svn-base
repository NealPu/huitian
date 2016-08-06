package com.momathink.sys.table.controller;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.table.model.MetaObject;
import com.momathink.sys.table.service.FieIdService;

/**
 * 系统表字段
 */
@Controller(controllerKey = "/system/tablefieid")
public class TableFieldController extends BaseController {
	private static FieIdService service = new FieIdService();
	private static final String viewPath = "/WEB-INF/view/sys/table/tablefieid/";
	
	/**
	 * 列表
	 */
	public void list() {
		service.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		renderJsp(viewPath+"list.jsp");
	}

	/**
	 * 添加
	 */
	public void add() {
		/*setAttr("tableField", (new TableField().set("tableName",getPara("tableName")).set("orders", "1" ) ) );*/
		renderJsp(viewPath+"layer_add.jsp");
	}

	/**
	 * 保存
	 */
	public void save() {
		operation(0);
	}

	/**
	 * 编辑
	 */
	public void edit(){
		/*TableField tableField = TableField.dao.findById(getPara("id"));
		setAttr("tableField", tableField);*/
		renderJsp(viewPath+"layer_edit.jsp");
	}

	/**
	 * 修改
	 */
	public void update(){
		operation(1);
	}

	/**
	 * 删除
	 */
	public void delete(){
		operation(2);
	}
	
	/**
	 * 查重
	 */
	public void checkExit(){
		renderJson(MetaObject.dao.queryTablesTheColumnsTheCount(MetaObject.db_connection_name_main, getPara("tableName"), getPara("columnName")));
	}
	
	/**
	 * @param type 0=保存,1=修改,2=删除
	 */
	private void operation(int type) {
		Integer returnValue = 0; // 0=失败,1成功,-1异常
		try {
			/*switch (type) {
			case 0:
				if (getModel(TableField.class).save())
					returnValue = 1;
				break;
			case 1:
				if (getModel(TableField.class).update())
					returnValue = 1;
				break;
			case 2:
				if (TableField.dao.findById(getPara("id")).delete())
					returnValue = 1;
				break;
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = -1;
		}
		renderJson(returnValue);
	}
	

}
