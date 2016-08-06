package com.momathink.sys.dict.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.dict.model.Dict;
import com.momathink.sys.dict.service.DictService;
import com.momathink.sys.table.model.MetaObject;

/**字典管理 <br>
<pre>
id	int	110	-10字典ID				-1	0
version	bigint	20-1000	数据版本号			0
dictname	varchar	30-100	字典名称	utf8	utf8_general_ci	0
val	varchar	255-100	字典项值	utf8	utf8_general_ci	0
numbers	varchar	50-100	字典项编码	utf8	utf8_general_ci	0
parentid	int	11-100	父级字典			0
isparent	varchar	10-100	'false'是否父类	utf8	utf8_general_ci	0
levels	int	4-100	所在层级			0
state	int	2-100	1是否正常			0
<pre>
 */
@Controller(controllerKey="/system/dict")
public class DictController extends BaseController {
	
	private static final DictService dictService = new DictService();
	/** 页面 的 路径  */
	private static final String filePath = "/WEB-INF/view/sys/dict/";
	
	/** 字典列表 */
	public void list(){
		List<Dict> dictList = Dict.dao.queryTableNodeRoot();
		setAttr("dictlist",dictList);
		renderJsp(filePath + "listTree.jsp");
	}
	
	/**字典树*/
	public void treeTable(){
		String treeTableData = dictService.treeTable(getPara("pId"));
		renderText(treeTableData);
	}
	
	/**添加字典**/
	public void addDict(){
		setAttr("operatorType","add");
		renderJsp(filePath + "layer_adddict.jsp");
	}
	
	/** 选择父级字典 */
	public void choiceParentDict(){
		setAttr("dictId","dictId");
		setAttr("dictName","dictName");
		String checkedId = getPara("checkedIds");
		String checkedIds = "";
		String checkedName = "";
		if(StrKit.notBlank(checkedId)){
			Dict dict = Dict.dao.cacheGet(checkedId);
			if(dict!=null){
				checkedIds = dict.getPrimaryKeyValue().toString();
				checkedName = dict.getStr("dictname");
			}
		}
		setAttr("checkedIds",checkedIds);
		setAttr("checkedName",checkedName);
		renderJsp(filePath + "layer_radio.jsp");
		
	}
	
	/**字典树取值*/
	public void treeData(){
		List<JSONObject> treeDataJsonList = dictService.childTreeData( getPara("id") );
		renderJson(treeDataJsonList);
	}
	
	/**查重 */
	public void checkExit(){
		renderJson(MetaObject.dao.queryCheckExcludeId("pt_dict", "numbers", getPara("numbers"), getPara("dictid")));
	}
	
	/**保存字典*/
	public void save(){
		renderJson(dictService.saveDict(getModel(Dict.class)));
	}
	
	/**编辑字典*/
	public void editDict(){
		Dict dict = Dict.dao.queryDictDetail(getPara());
		setAttr("dict",dict);
		setAttr("operatorType","update");
		renderJsp(filePath + "layer_adddict.jsp");
	}
	
	/**修改字典*/
	public void update(){
		renderJson(dictService.updateDict(getModel(Dict.class),getPara("oldParentId")));
	}
	
	/**快速添加 子级字典*/
	public void fastAddDict(){
		Dict dict = Dict.dao.queryDictDetail(getPara());
		dict.put("parentname", dict.get("dictname"));
		dict.set("dictname", dict.get("dictname")+"的子级");
		dict.set("val", dict.get("val")+"的子级");
		dict.set("numbers", dict.get("numbers")+"的子级");
		dict.set("parentid", dict.get("id"));
		dict.set("id", null);
		setAttr("dict",dict);
		setAttr("operatorType","add");
		renderJsp(filePath + "layer_adddict.jsp");
	}
	
}

