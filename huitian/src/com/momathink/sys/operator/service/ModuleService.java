package com.momathink.sys.operator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.momathink.common.base.BaseService;
import com.momathink.sys.operator.model.Module;

public class ModuleService extends BaseService {
	
	public static List<Module> getFeatures(String systemsid) {
		List<Module> list = Module.getFeatures(systemsid); 
		if(!list.isEmpty()){
			StringBuffer parentmoduleidsSb = new StringBuffer();
			for(Module m:list){
				parentmoduleidsSb.append(m.getInt("id")+",");
			}
			String parentmoduleids = parentmoduleidsSb.toString();
			Map<String, List<Module>> moduleMap = queryBYParentmoduleidsMap(parentmoduleids.substring(0, parentmoduleids.length()-1));
			for(Module m:list){
				 List<Module> listm= moduleMap.get(m.get("id").toString());
				 //List<Module> listm2= Module.dao.queryBYParentmoduleids(m.get("id").toString());
				 if(listm != null){
					 for(Module mod:listm){
						 if(mod.get("url")!=null){
							 String str = mod.getStr("url");
							 if(str.indexOf("index")>0){
								 str=str.substring(0,str.indexOf("index"));
							 }
							 if(str.indexOf('?')>0){
								 mod.put("urls",str.substring(0, str.indexOf('?')).replaceAll("/",""));
							 }else{
								 mod.put("urls",str.replaceAll("/",""));
							 } 
						 }
					 }
				 }else{
					 listm = new ArrayList<Module>();
				 }
				 m.put("smail", listm);
			}
		}
		return list;
	}
	
	/** 获取多个模块下的小功能 **/
	public static Map<String, List<Module>> queryBYParentmoduleidsMap(String parentmoduleids){
		List<Module> modulesDb = Module.dao.queryBYParentmoduleids(parentmoduleids);
		Map<String, List<Module>> moduleMap = new HashMap<String, List<Module>>();
		String parentmoduleidsFor = "";
		List<Module> modules = null;
		for (Module module : modulesDb) {
			String parentmoduleidsDb = module.getStr("parentmoduleids");
			if (!parentmoduleidsFor.equals(parentmoduleidsDb)) {
				modules = new ArrayList<Module>();
				moduleMap.put(parentmoduleidsDb, modules);
				parentmoduleidsFor = parentmoduleidsDb!=null?parentmoduleidsDb:"";
			}
			modules.add(module);
		}
		return moduleMap;
	}
}
