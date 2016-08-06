/**
 * Project Name:moma_crm
 * File Name:Dict.java
 * Package Name:cn.yun.system.dict.model
 * Date:2016年2月26日上午11:03:00
 * Copyright (c) 2016, www.yunjiaowu.cn All Rights Reserved.
 *
*/

package com.momathink.sys.dict.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.thread.ThreadParamInit;

/**
 * ClassName:Dict <br/>
 * 
 * 
 * Date:     2016年2月26日 上午11:03:00 <br/>
 * @author   Richie
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Table(tableName="pt_dict")
public class Dict extends BaseModel<Dict> {

	/**
	 * serialVersionUID:
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1871485970870402127L;
	
	public static final Dict dao = new Dict();

	/**通过Key 获取相应的值**/
	public String queryValGetDictname(String key){
		Dict dict = cacheGet(key);
		return dict!=null?dict.getStr("val"):"";
	}
	
	/**
	 * queryTableNodeRoot:(根字典目录). <br/>
	 * @author Richie
	 * @return
	 * @since JDK 1.7
	 */
	public List<Dict> queryTableNodeRoot() {
		String sql = "select * from pt_dict where parentid is null order by id asc ";
		return dao.find(sql);
	}

	/**
	 * queryChildList:(根据父级id获取子级所有 字典集合). <br/>
	 * @author Richie
	 * @param pId
	 * @return
	 * @since JDK 1.7
	 */
	public List<Dict> queryChildList(String pId) {
		String sql = "select * from pt_dict where parentid = ? order by id asc ";
		return dao.find(sql,pId);
	}

	/**
	 * checkExit:(字典编号存在). <br/>
	 * @author Richie
	 * @param id
	 * @param numbers
	 * @return
	 * @since JDK 1.7
	 */
	/*public Long checkExit1(String id, String numbers) {
		String sql = "select count(1) numCount from pt_dict where numbers = '"+ numbers+"'" ;
		if(StrKit.notBlank(id)){
			sql += " and id != " + id;
		}
		Dict dict = dao.findFirst(sql);
		if(dict!=null){
			return dict.getLong("numCount");
		}
		return Long.valueOf(0);
	}*/
	/***
	 * 根据编码查询
	 * @param numbers
	 * @return
	 */
	public Dict queryByNumbers(String numbers){
		return dao.findFirst("SELECT * FROM pt_dict WHERE numbers = ?", numbers);
	}

	/**
	 * queryAllDictList:(所有的字典项). <br/>
	 * @author Richie
	 * @return
	 * @since JDK 1.7
	 */
	public List<Dict> queryAllDictList() {
		String sql = "select * from pt_dict where state = 1 ";
		List<Dict> list = Dict.dao.find(sql);
		return list;
	}
	
	public Dict queryDictDetail(String id){
		String sql = "select dict.*,pd.dictname parentname from pt_dict dict left join pt_dict pd on pd.id = dict.parentid where dict.id = ? ";
		return dao.findFirst(sql, id);
	}

	/**
	 * cacheAdd:(). <br/>
	 * @author Richie
	 * @param dict
	 * @since JDK 1.7
	 */
	public void cacheAdd(Integer id){
		Dict dict = Dict.dao.findById(id);
		cacheAdd(dict);
	}
	public void cacheAdd(Dict dict) {
		
		//CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict + dict.getPrimaryKeyValue(), dict);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict + dict.getStr("numbers"), dict);
		
		List<Dict> childList = queryChildList(dict.getPrimaryKeyValue().toString());
		//CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + dict.getPrimaryKeyValue(), childList);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + dict.getStr("numbers"), childList);
		
	}
	
	public void cacheRemove(Integer id){
		Dict dict = Dict.dao.findById(id);
		//CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict + dict.getPrimaryKeyValue());
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict + dict.getStr("numbers"));
		//CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + dict.getPrimaryKeyValue());
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + dict.getStr("numbers"));
		
		Integer parentid = dict.getInt("parentid");
		if(null!=parentid){
			Dict parent = Dict.dao.findById(parentid);
			if(null!=parent){
				List<Dict> parentChildren = queryChildList(parentid.toString());
				//CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + parentid, parentChildren );
				CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + parent.getStr("numbers"), parentChildren );
			}
		}
	}
	
	public Dict cacheGet(String key){
		Dict dict = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict + key);
		if(null == dict){
			dict = queryByNumbers(key);
			if(null != dict)
				cacheAdd(dict);
		}
		return dict;
	}
	
	public List<Dict> cacheGetChild(String key){
		List<Dict> list = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + key);
		if(null == list){
			Dict dict = cacheGet(key);
			if(null!=dict)
				list = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_dict_child + key);
		}
		return list;
	}

	public void updateParentFalse(String oldParentId) {
		String sql = "update pt_dict set isparent = 'false' where id = ? ";
		Db.update(sql, oldParentId);
	}
	
	

}

