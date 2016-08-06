package com.momathink.common.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.StrKit;
import com.momathink.sys.operator.model.Module;
import com.momathink.sys.operator.model.Operator;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

public class ToolOperatorSession {
	
	private static Map<String, String> roleMap = new HashMap<String, String>();
	
	public static Map<Object,Object> operatorSessionSet(String userId){
		Map<Object,Object> map = new HashMap<Object,Object>();
		try{
			StringBuffer moduleinids = new StringBuffer();
			SysUser user = SysUser.dao.findById(userId);
			if(!Role.isAdmins(user.getStr("roleids"))){
				String roleids = user.getStr("roleids");
				StringBuffer rolesin = new StringBuffer("select id,names,operatorids from pt_role where id in (").append(roleids.substring(0, roleids.length()-1)).append(" ) order by names asc");
				List<Role> rolelist = Role.dao.find(rolesin.toString());
				StringBuffer roleBuffer = new StringBuffer();
				for(Role role:rolelist){
					if(!StringUtils.isEmpty(role.getStr("operatorids"))){
						roleBuffer.append(role.getStr("operatorids").replace("operator_", ""));
					}
				}
				String operatorids = toSql(roleBuffer.toString());
				StringBuffer operatorin = new StringBuffer("select id,names,url,privilege,moduleids,formaturl from pt_operator where id in (").append(operatorids).append(" ) order by names asc ");
				StringBuffer operatornotin = new StringBuffer("select id,names,privilege,url,moduleids,formaturl from pt_operator where id not in (").append(operatorids).append(" ) order by names asc ");
				List<Operator> operatorinList = Operator.dao.find(operatorin.toString());
				List<Operator> operatornotList = Operator.dao.find(operatornotin.toString());
				for(Operator opin:operatorinList){
					map.put(opin.getStr("formaturl"), true);
					moduleinids.append(",\"").append(opin.getStr("moduleids")).append("\"");
				}
				for(Operator opnotin:operatornotList){
					if(opnotin.getStr("privilege").equals("0")){
						map.put(opnotin.getStr("formaturl"), true);
					}else{
						map.put(opnotin.getStr("formaturl"), false);
					}
				}
			}else{
				//
				List<Operator> unoperatorlist = Operator.dao.queryAll();
				for(Operator unoperator:unoperatorlist){
					if(Role.isAdmins(user.getStr("roleids"))){
						map.put(unoperator.getStr("formaturl"), true);
						moduleinids.append(",\"").append(unoperator.getStr("moduleids")).append("\"");
					}else{
						if(unoperator.getStr("privilege").equals("0")){
							map.put(unoperator.getStr("formaturl"), true);
							moduleinids.append(",\"").append(unoperator.getStr("moduleids")).append("\"");
						}else{
							map.put(unoperator.getStr("formaturl"), false);
						}
					}
				}
			}
			String muinids = moduleinids.toString().replaceFirst(",", "");
			StringBuffer strmoduleids = new StringBuffer();
			List<Module> moduleinlist =  Module.dao.find("select * from pt_module where 1=1 and id in ("+muinids+")");
			for(Module module: moduleinlist){
				strmoduleids.append(",\"").append(module.getStr("parentmoduleids")).append("\"");
				map.put(module.getStr("numbers"), true);
			}
			List<Module> parentmodule = Module.dao.find("select * from pt_module where 1=1 and isparent = 'true' and id in ( "+strmoduleids.toString().replaceFirst(",", "")+" )");
			List<Module> parentnotmodule = Module.dao.find("select * from pt_module where 1=1 and isparent = 'true' and id not in ("+strmoduleids.toString().replaceFirst(",", "")+")");
			for(Module parent:parentmodule){
				map.put(parent.getStr("numbers"), true);
			}
			for(Module parentnot : parentnotmodule){
				map.put(parentnot.getStr("numbers"), false);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//取出所在分组、所在角色、角色具有权限  为可
		
		return map;	
	}

	
	protected static String toSql(String ids) {
		if (null == ids || ids.isEmpty()) {
			return "";
		}

		String[] idsArr = ids.split(",");
		StringBuilder sqlSb = new StringBuilder();
		int length = idsArr.length;
		for (int i = 0, size = length - 1; i < size; i++) {
			sqlSb.append(" '").append(idsArr[i]).append("', ");
		}
		if (length != 0) {
			sqlSb.append(" '").append(idsArr[length - 1]).append("' ");
		}

		return sqlSb.toString();
	}
	
	/**
	 * 判断角色
	 * 
	 * @param numbers
	 *            角色编码
	 * @param roleids
	 *            角色表的id, 允许逗号 “,” 拼接的字符串
	 * @return 是：返回true 否：返回false
	 */
	public static boolean judgeRole(String numbers, String roleids) {
		String roleId = roleMap.get(numbers);
		if (StrKit.isBlank(roleids) || StrKit.isBlank(numbers) || StrKit.isBlank(roleId) ) {
			return false;
		}
		String[] roleid = roleids.split(",");
		for (int i = 0; i < roleid.length; i++) {
			if (roleId.equals(roleid[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 刷新加载数据库中的角色到系统内存map
	 */
	public static void refreshRoleMap() {
		List<Role> roleList = Role.dao.getAllRole();
		if (null != roleList) {
			for (Role role : roleList) {
				refreshRoleMap(role);
			}
		}else
			roleMap.clear();
	}
	
	/**
	 * 刷新加载 某 角色到系统内存map
	 */
	public static void refreshRoleMap(Role role) {
		roleMap.put(role.getStr("numbers"), role.get("id").toString());
	}
	
}
