package com.momathink.sys.operator.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.operator.model.Operator;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.operator.model.Systems;
import com.momathink.sys.system.model.SysUser;
/**获取 菜单
 * */
@Controller(controllerKey="/operator/getHeadMessage")
public class HeadMessageController extends BaseController {
	
	public void index(){
		SysUser user = SysUser.dao.findById(getSysuserId());
		List<Systems> sys =Systems.dao.queryAll();
		try{
			List<Role> listr = Role.dao.getRoleNamesByIds(user.getStr("roleids").substring(0, user.getStr("roleids").length()-1));
			Map<String,String> map  = new HashMap<String,String>();
			for(Role r :listr){
				String[] opes = r.getStr("operatorids").split(",");
				for(String s : opes){
					map.put(s,s);
				}
			}
			List<String> roleids = new ArrayList<String>();
			for(String key:map.keySet()){
				String ke= key.substring(key.indexOf("_")+1,key.length());
				roleids.add(ke);
			}
			if(!sys.isEmpty()){
				for(Systems s:sys){
					boolean flag = false;
					List<Operator> listo = Operator.dao.queryBySystemsid(s.getInt("id"));
					for(String roleid :roleids){
						for(Operator op:listo){
							if(roleid.equals(op.getInt("id").toString())){
								flag=true;
								break;
							}
						}
					}
					s.put("flag",flag);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		renderJson(sys);
	}
}
