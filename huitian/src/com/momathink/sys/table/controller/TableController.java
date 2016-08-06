package com.momathink.sys.table.controller;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.table.model.MetaObject;
import com.momathink.sys.table.service.TableService;

/**
 * 系统表结构管理
 */
@Controller(controllerKey = "/system/table")
public class TableController extends BaseController {
	//private static final Logger logger = Logger.getLogger(TableController.class);
	private static TableService service = new TableService();
	private static final String viewPath = "/WEB-INF/view/sys/table/introduction/";
	
	/**
	 * 列表
	 */
	public void list() {
		//service.list(splitPage);
		service.metaList(splitPage);
		setAttr("showPages", splitPage.getPage());
		//renderJsp(viewPath+"list.jsp");
		renderJsp(viewPath+"metaList.jsp");
	}

	/**
	 * 添加
	 */
	public void add() {
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
		/*TableIntroduction tableField = TableIntroduction.dao.findById(getPara());
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
		renderJson(MetaObject.dao.queryTablesTheCount(MetaObject.db_connection_name_main, getPara("tableName")));
	}
	
	/**
	 * @param type 0=保存,1=修改,2=删除
	 */
	private void operation(int type) {
		Integer returnValue = 0; // 0=失败,1成功,-1异常
		try {
			/*switch (type) {
			case 0:
				if (getModel(TableIntroduction.class).save())
					returnValue = 1;
				break;
			case 1:
				if (getModel(TableIntroduction.class).update())
					returnValue = 1;
				break;
			case 2:
				if (TableIntroduction.dao.findById(getPara("id")).delete())
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
