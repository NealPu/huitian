package com.momathink.crm.opportunity.model;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;


@Table(tableName="crm_source")
public class CrmSource extends BaseModel<CrmSource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final CrmSource dao = new CrmSource();

	public Object getAllcrmSource() {
		return dao.find("select * from crm_source ");
	}

	public long countNumByName(String name, String id) {
		StringBuffer sb = new StringBuffer("select count(1) counts from crm_source where name = '").append(name).append("'");
		if(!StringUtils.isEmpty(id)){
			sb.append(" and id != ").append(id);
		}
		CrmSource cs = dao.findFirst(sb.toString());
		return cs==null?0:cs.getLong("counts");
	}

}
