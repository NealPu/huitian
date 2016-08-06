package com.momathink.sys.table.service;

import java.util.Map;
import java.util.Set;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.table.model.MetaObject;
/**
 * 系统表结构管理
 */
public class TableService extends BaseService {

	public void metaList(SplitPage splitPage) {
		String select = "SELECT * ";
		StringBuffer fromSql = new StringBuffer(" FROM information_schema.`TABLES` ");
		fromSql.append(" WHERE table_schema = '" + MetaObject.db_connection_name_main + "' ");
		
		Map<String, String> paramMap = splitPage.getQueryParam();
		Set<String> keyList = paramMap.keySet();
		for (String key : keyList) {
			String value = paramMap.get(key);
			switch (key) {
			case "TABLE_NAME":
				fromSql.append(" AND TABLE_NAME LIKE '%").append(value).append("%' ");
				break;
			case "TABLE_COMMENT":
				fromSql.append(" AND TABLE_COMMENT LIKE '%").append(value).append("%' ");
				break;
			}
		}
		fromSql.append(" ORDER BY TABLE_NAME ASC ");
		
		Page<Record> page = Db.paginate(splitPage.getPageNumber(),
				splitPage.getPageSize(), select, fromSql.toString());
		splitPage.setPage(page);
	}
	
}
