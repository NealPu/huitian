package com.momathink.sys.address.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName = "ipaddress")
public class IpAddressRecord extends BaseModel<IpAddressRecord>{

	private static final long serialVersionUID = -7123450286284981885L;
	public static final IpAddressRecord dao = new IpAddressRecord();
	public List<IpAddressRecord> findById(String Id) {
		return StringUtils.isEmpty(Id)?null:dao.find("SELECT * FROM ipaddress WHERE Id=?", Integer.parseInt(Id));
	}
}
