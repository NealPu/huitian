package com.momathink.sys.sms.model;


import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName = "crm_smssettings")
public class SmsSettings extends BaseModel<SmsSettings> {

	private static final long serialVersionUID = 9173917692482770076L;
	public static final SmsSettings dao = new SmsSettings();
	
	/**
	 * 查询所有短信商的配置信息和接口
	 * @return
	 */
	public List<SmsSettings> getAllMessage() {
		return dao.find("SELECT * FROM crm_smssettings ");
	}
	
	/**
	 * 根据状态找到正在使用的服务商
	 * @return
	 */
	public SmsSettings getUsedServiceProvider() {
		return dao.findFirst("select * from crm_smssettings where sms_state= 1 ");
	}
	
}
