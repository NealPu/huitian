/**
 * Project Name:moma_crm
 * File Name:DictService.java
 * Package Name:cn.yun.system.dict.service
 * Date:2016年2月26日上午11:03:15
 * Copyright (c) 2016, www.yunjiaowu.cn All Rights Reserved.
 *
*/

package com.momathink.sys.dict.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.momathink.common.base.BaseService;
import com.momathink.sys.dict.model.Dict;

/**
 * ClassName:DictService <br/>
 * 
 * 
 * Date:     2016年2月26日 上午11:03:15 <br/>
 * @author   Richie
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class DictService extends BaseService {

	public List<JSONObject> childTreeData( String parentId ) {
		List<JSONObject> treeJsonDataList = new ArrayList<JSONObject>();
		
		List<Dict> dictDatasList = null;
		if ( null != parentId ) {
			dictDatasList = Dict.dao.queryChildList( parentId );
		} else {
			dictDatasList = Dict.dao.queryTableNodeRoot();
		}

		for ( Dict dict : dictDatasList ) {
			JSONObject treeDataJson = new JSONObject();
			treeDataJson.put( "id" , dict.getPrimaryKeyValue() );
			treeDataJson.put( "name" , dict.getStr( "dictName" ) );
			treeDataJson.put( "isParent" , dict.getStr( "isparent" ) );
			treeJsonDataList.add( treeDataJson );
		}

		return treeJsonDataList;
	
	}

	public String treeTable(String pId) {
		StringBuffer treeStr = new StringBuffer();
		List<Dict> dictList = Dict.dao.cacheGetChild(pId);
		if(dictList!=null&&dictList.size()>0){
			for(Dict dict : dictList){
				treeStr.append("<tr id='").append(dict.getPrimaryKeyValue()).append("' pId='").append(dict.getInt("parentid")).append("' hasChild='true' >");
				treeStr.append("<td class='textleft'><span controller='true' >").append(dict.getInt("levels")).append(" 级 </span></td>");
				treeStr.append("<td>").append(dict.getStr("dictname")).append("</td>");
				treeStr.append("<td>").append(dict.getStr("numbers")).append("</td>");
				treeStr.append("<td>").append(dict.getStr("val")).append("</td>");
				treeStr.append("<td>").append(dict.getInt("state").toString().equals("1")?"启用":"停用").append("</td>");
				treeStr.append("<td><a class='btn btn-xs btn-primary' onclick='fastAddDict(").append(dict.getInt("id")).append(")' >添加子级</a>");
				treeStr.append("<a class='btn btn-xs btn-primary' onclick='editDict(").append(dict.getInt("id")).append(")' >编辑</a></td>");
				treeStr.append("</tr>");
			}
		}
		return treeStr.toString();
	}

	public boolean saveDict(Dict dict) {
		boolean flag = true;
		try {
			Integer pId = dict.getInt("parentid");
			Integer levels = 1;
			if(pId!=null){
				Dict parentDict = Dict.dao.findById(pId);
				if(parentDict!=null){
					levels = 1 + parentDict.getInt("levels");
				}
			}
			dict.set("version", 0);
			dict.set("state", 1);
			dict.set("levels", levels);
			dict.set("isparent", "false");
			dict.save();
			
			if(dict.getInt("parentid")!=null){
				Dict pdict = Dict.dao.findById(dict.getInt("parentid"));
				pdict.set("isparent", "true");
				pdict.update();
				Dict.dao.cacheAdd(pdict);
			}
			
			Dict.dao.cacheAdd(dict);
			
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public boolean updateDict(Dict dict, String oldParentId) {
		boolean flag = true;
		try {
			Integer pId = dict.getInt("parentid");
			Integer levels = 0;
			if(pId!=null){
				Dict parentDict = Dict.dao.findById(pId);
				if(parentDict!=null){
					levels = 1 + parentDict.getInt("levels");
				}
			}
			dict.set("levels", levels);
			dict.update();
			Dict.dao.cacheAdd(dict);
			
			updateChildLevels(dict.getPrimaryKeyValue());
			
			if(dict.getInt("parentid")!=null){
				Dict pdict = Dict.dao.findById(dict.getInt("parentid"));
				pdict.set("isparent", "true");
				pdict.update();
				Dict.dao.cacheAdd(pdict);
			}
			
			if(StrKit.notBlank(oldParentId)){
				List<Dict> oldList = Dict.dao.queryChildList(oldParentId);
				if(!(oldList!=null && oldList.size()>0)){
					Dict.dao.updateParentFalse(oldParentId);
				}
				Dict.dao.cacheAdd(Integer.parseInt(oldParentId));
			}
			
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}

	private void updateChildLevels(Integer primaryKeyValue) {
		Dict dict = Dict.dao.findById(primaryKeyValue);
		List<Dict> child = Dict.dao.queryChildList(primaryKeyValue.toString());
		if(child!=null&&child.size()>0){
			for(Dict kid:child){
				kid.set("levels", dict.getInt("levels")+1);
				kid.update();
				Dict.dao.cacheAdd(kid);
				updateChildLevels(kid.getPrimaryKeyValue());
			}
		}
		
	}



}

