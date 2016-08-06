package com.momathink.weixin.service;

import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.weixin.model.Wpnumber;

public class WpnumberService extends BaseService {
	public static final Wpnumber dao = new Wpnumber();

	/**
	 * 保存
	 * 
	 * @param
	 * @return
	 */
	@Before(Tx.class)
	public void save(Wpnumber wpnumber) {
		wpnumber.save();
	}

	/**
	 * 更新
	 * 
	 * @param
	 */
	public void update(Wpnumber wpnumber) {
		wpnumber.update();
	}

	/**
	 * 删除
	 * 
	 * @param
	 */
	public void delete(String id) {
		Wpnumber.dao.deleteById(id);
	}

	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage) {
		String select = " select * ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from wx_wpnumber where 1=1 ");
		if (null == queryParam) {
			return;
		}
		String accountname = queryParam.get("accountname");
		String accounttype = queryParam.get("accounttype");
		if (null != accountname && !accountname.equals("")) {
			formSqlSb.append(" and accountname like ? ");
			paramValue.add("%" + accountname.trim() + "%");
		}
		if (null != accounttype && !accounttype.equals("")) {
			formSqlSb.append(" and menutype = ? ");
			paramValue.add(Integer.parseInt(accounttype));
		}
	}
}
