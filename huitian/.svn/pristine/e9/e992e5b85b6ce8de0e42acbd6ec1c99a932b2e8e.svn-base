package com.momathink.crm.brokerage.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "crm_brokerage")
public class Brokerage extends BaseModel<Brokerage> {

	private static final long serialVersionUID = 7799487931078955591L;
	public static final Brokerage dao = new Brokerage();

	public List<Brokerage> findByMediatorId(String mediatorId, String brokerageType) {
		String sql = "select * from crm_brokerage where 1=1 ";
		if (!StringUtils.isEmpty(mediatorId)) {
			sql += " and mediatorid = '" + mediatorId + "'";
		}
		if (!StringUtils.isEmpty(brokerageType)) {
			sql += " and brokeragetype = '" + brokerageType + "'";
		}
		return Brokerage.dao.find(sql + " order by createdate");
	}

	public boolean findByClearingMonth(String statMonth) {
		Long count = Db.queryLong("select count(1) from crm_brokerage where clearingmonth=?",statMonth);
		return count == null||count==0?false:true;
	}
}
