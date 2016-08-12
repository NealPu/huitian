package com.momathink.crm.proxy.controller;

import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.crm.proxy.service.ProxyFinanceService;

@Controller( controllerKey="/proxy/finance" )
public class ProxyFinanceController extends BaseController {
	
	private static final ProxyFinanceService financeService = new ProxyFinanceService();

	/**
	 * 回款记录
	 */
	public void repayment() {
		financeService.repaymentList( splitPage , getSysuserId() );
		renderJsp( "repayment.jsp" );
	}
	
	/**
	 * 最近待回款项目
	 */
	public void nearlestToPay() {
		financeService.nearlestToPayList( splitPage ) ;
		renderJsp( "nearlest.jsp" );
	}
	
	
}

