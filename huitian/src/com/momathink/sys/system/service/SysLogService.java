package com.momathink.sys.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
/**
 * 2015年7月13日prq
 * @author prq
 *
 */

public class SysLogService extends BaseService {

	public void list(SplitPage splitPage) {
		String select = " select sys.*,op.names ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from pt_syslog sys left join pt_operator op on op.url = sys.uri "
				+ " where sys.uri!='/main/getMessage' and sys.uri is not null ");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "startdate":// 添加日期
				formSqlSb.append(" and date_format(sys.startdate,'%Y-%m-%d')= '").append(value).append("' ");
				break;
			case "sysusername":
				formSqlSb.append(" and sys.username= '").append(value).append("'");
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" ORDER BY sys.id DESC");
	}

}
