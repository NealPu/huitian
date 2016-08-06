package com.momathink.sys.address.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "ipaddress")
public class IpAddressDetail extends BaseModel<IpAddressDetail>{

	
	private static final long serialVersionUID = 3540300166071308985L;
	public static final IpAddressDetail dao = new IpAddressDetail();
	
	public void deleteByRecordId(Integer recordId) {
		Db.update("DELETE FROM ipaddress WHERE ipaddress.Id=?",recordId );
	}

	public List<IpAddressDetail> findbyRecordId(String recordId) {
		return StringUtils.isEmpty(recordId)?null:dao.find("SELECT * FROM ipaddress WHERE Id = ?",Integer.parseInt(recordId));
	}

}
