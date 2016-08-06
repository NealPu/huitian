package com.momathink.sys.address.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="ipaddress")
public class IpAddress extends BaseModel<IpAddress> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3303278592079027185L;
	public static final IpAddress dao = new IpAddress();
}
