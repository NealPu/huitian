package com.momathink.sys.sms.service;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.sys.sms.model.SmsSettings;

public class SmsSettingsService extends BaseService {
	/**
	 * 更新
	 * @param org
	 */
	@Before(Tx.class)
	public void update(SmsSettings sms) {
		try {
			sms.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存
	 * @param sms
	 */
	public void save(SmsSettings sms) {
		try {
			sms.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
