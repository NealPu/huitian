package com.momathink.weixin.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="wx_wpnumber")
public class Wpnumber extends BaseModel<Wpnumber> {
	
	private static final long serialVersionUID = 3803797846387486628L;
	public static final Wpnumber dao = new Wpnumber();

	public List<Wpnumber> getAllWpnumbers(){
		return dao.find("select * from wx_wpnumber");
	}

	public Wpnumber findByWxh(String accountNumber) {
		return Wpnumber.dao.findFirst("select * from wx_wpnumber where accountnumber='"+accountNumber+"'");
	}
}
