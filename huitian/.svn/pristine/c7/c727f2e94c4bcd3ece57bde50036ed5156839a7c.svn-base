/**
 * Project Name:moma_crm
 * File Name:ThreadParamIinit.java
 * Package Name:cn.yun.common.thread
 * Date:2016年3月1日下午7:30:07
 * Copyright (c) 2016, www.yunjiaowu.cn All Rights Reserved.
 *
*/

package com.momathink.common.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.momathink.sys.dict.model.Dict;

/**
 * ClassName:ThreadParamIinit <br/>
 * 
 * 
 * Date:     2016年3月1日 下午7:30:07 <br/>
 * @author   Richie
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class ThreadParamInit extends Thread {
	private static Logger log = Logger.getLogger(ThreadParamInit.class);
	
	public static String cacheStart_user = "user_";
	public static String cacheStart_user_systemsList = "user_systemsList";
	public static String cacheStart_role = "role_";
	public static String cacheStart_operator = "operator_";
	public static String cacheStart_dict = "dict_";
	public static String cacheStart_dict_child =  "dict_child_";
	public static String cacheStart_i18n = "i18n_";
	public static String cacheStart_system_parentnullmodule ="system_parentnullmodule_" ;
	public static String cacheStart_moduleChild = "moduleChild_" ;
	public static String cacheStart_module = "module_" ;
	public static String cacheStart_moduleOperatorAll = "moduleOperatorAll_" ;
	public static String cacheStart_moduleOperatorPrivilege = "moduleOperatorPrivilege_" ;
	
	public static String cacheStart_dept = "dept_";
	public static String cacheStart_dept_child = "dept_child_";
	
	public static String cacheStart_station = "station_";
	public static String cacheStart_station_child = "station_child_";
	public static String cacheStart_pageData = "pageData_";
	
	
	@Override
	public void run(){
		cacheAll();
	}

	public static synchronized void cacheAll() {
		log.info("加载缓存开始:(字典项缓存初始化)--------------------------------------------------------------");
		cacheDict();
		log.info("加载缓存结束:(字典项缓存初始化)--------------------------------------------------------------");
		
		log.info("加载缓存完毕--------------------------------------------------------------");
	}
	
	/**
	 * cacheDict: 字典项缓存初始化 . <br/>
	 * @author Richie
	 * @since JDK 1.7
	 */
	public static void cacheDict(){
		try {
			List<Dict> dictList = Dict.dao.queryAllDictList();
			for(Dict dict:dictList){
				Dict.dao.cacheAdd(dict.getPrimaryKeyValue());
				dict = null;
			}
			dictList = null;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("加载缓存异常:(字典项缓存初始化异常)--------------------------------------------------------------");
		}
	}
	
	
}

