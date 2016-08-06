package com.momathink.teaching.teacher.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;

public class TeacherGroupService extends BaseService {
	private static Logger log = Logger.getLogger(TeacherGroupService.class);

	/**
	 * 教研组分页
	 */
	public void list(SplitPage splitPage){
		log.debug("教师分组：分页处理");
		String select = "SELECT g.*,t.REAL_NAME";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from teachergroup g left join account t on g.leaderid=t.Id where 1=1");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "groupname":
				formSqlSb.append(" and g.groupname like  ? ");
				paramValue.add("%" + value + "%");
				break;
			case "leadername":
				formSqlSb.append(" and t.REAL_NAME like  ? ");
				paramValue.add("%" + value + "%");
				break;
			default:
				break;
			}
		}
		formSqlSb.append("");
	}
	
}
