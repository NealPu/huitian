package com.momathink.crm.mediator.service;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.tools.ToolMonitor;
import com.momathink.crm.mediator.model.Organization;

public class OrganizationService extends BaseService {
	
	@Before(Tx.class)
	public void update(Organization org) {
		try {
			org.update();
			ToolMonitor.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
