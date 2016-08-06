package com.momathink.finance.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/**
 * 2015年7月17日
 * @author prq
 *
 */
@Table(tableName="crm_refunds")
public class Refund extends BaseModel<Refund> {

	private static final long serialVersionUID = 1L;
	public static final Refund dao = new Refund();
	

}
