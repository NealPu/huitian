package com.momathink.sys.system.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

public class SysUserService extends BaseService {

	private static Logger log = Logger.getLogger(SysUserService.class);
	/**
	 * 系统用户分页
	 * @param splitPage
	 */
	@SuppressWarnings("unchecked")
	public void list(SplitPage splitPage) {
		log.debug("分页处理");
		String select = " select s.* ";
		splitPageBase(splitPage, select);
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> list = page.getList();
		for(Record r:list){
			String roleids = r.getStr("roleids");
			if(ToolString.isNull(roleids))
				continue;
			List<Role> roleList = Role.dao.getRoleNamesByIds(roleids.substring(0,roleids.length()-1));
			if(roleList.size()==0)
				continue;
			r.set("row",roleList.size());
			r.set("firstRole",roleList.get(0));
			roleList.remove(0);
			r.set("roleNames",roleList);
		}
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from account s where s.state=0 and user_type=0 ");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "sysusername":
				formSqlSb.append(" and s.real_name like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "phonenumber":
				formSqlSb.append(" and s.tel like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "email":
				formSqlSb.append(" and s.email like ? ");
				paramValue.add("%" + value + "%");
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" ORDER BY s.id DESC");
	}
	
	@Before(Tx.class)
	public int save(SysUser account) {
		int id;
		try {
			// 保存顾问
			account.set("state", "0");
			account.set("create_time", new Date());
			account.save();
			 id =account.getPrimaryKeyValue();
		} catch (Exception e) {
			throw new RuntimeException("保存用户异常");
		}
		return id;
	}

	@Before(Tx.class)
	public void update(SysUser account) {
		try {
			account.set("update_time", new Date());
			account.update();
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常");
		}
	}
}
