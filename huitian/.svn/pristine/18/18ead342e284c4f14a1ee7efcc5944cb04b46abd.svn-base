package com.momathink.crm.brokerage.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "crm_brokerage_detail")
public class BrokerageDetail extends BaseModel<BrokerageDetail> {

	private static final long serialVersionUID = -5457693903821517516L;
	public static final BrokerageDetail dao = new BrokerageDetail();

	public List<BrokerageDetail> findByBrokerageIds(String brokerageId) {
		return StringUtils.isEmpty(brokerageId) ? null : find("select * from crm_brokerage_detail where brokerageid = ?", brokerageId);
	}

	public void deleteByBrokerageId(Integer brokerageId) {
		if(brokerageId!=null){
			Db.update("DELETE FROM crm_brokerage_detail WHERE brokerageid=?",brokerageId);
		}
	}
}
