package com.momathink.crm.proxy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.crm.proxy.model.ProxyContract;
import com.momathink.crm.proxy.model.ProxyContractPayment;
import com.momathink.crm.proxy.service.ProxyContractService;

@Controller( controllerKey="/proxy/contract" )
public class ProxyContractController extends BaseController {
	
	private static final Logger log = Logger.getLogger( ProxyContractController.class );
	
	private static final ProxyContractService contractService = new ProxyContractService();
	
	//取出乙方的合同。
	public void proxyContract() {
		try {
			String proxyId = getPara();
			Proxy proxyDetail = Proxy.dao.findById( proxyId );
			setAttr( "proxy" , proxyDetail );
			
			List< ProxyContract > contractList = contractService.queryProxyContractBaseInfo( proxyId );
			setAttr( "contractList" , contractList );
			
			renderJsp( "contract.jsp" );
		} catch (Exception e) {
			log.error( "ProxyContractController" , e );
			renderError( 500 );
		}
	}
	
	//保存新建的合同
	@Before( Tx.class )
	public void saveProxyContract() {
		JSONObject saveProxyJson = new JSONObject();
		try {
			Integer createUser = getSysuserId();
			ProxyContract contract = getModel( ProxyContract.class );
			
			Map< String , String > paymentVals = new HashMap< String , String >();
			String[] orders = getParaValues( "order" );
			if( orders.length > 0 ) {
				for( String order : orders ) {
					paymentVals.put( order , getPara( "payment_" + order ) ) ;
				}
			}
			
			contractService.saveProxyContract( createUser , contract , paymentVals );
			
			saveProxyJson.put( "flag" , true );
		} catch ( Exception e ) {
			log.error( "saveProxyContract" , e );
			Res res = I18n.use( ( String )PropertiesPlugin.getParamMapValue( DictKeys.basename ) , getCookie( "_locale" ) );
			saveProxyJson.put( "flag" , false );
			saveProxyJson.put( "msg" , res.get( "operationError" ) );
		}
		renderJson( saveProxyJson );
	}
	
	public void editProxyContractDetail() {
		String contractId = getPara( "contractId" );
		ProxyContract contract = contractService.queryContractWithPaymentRules( contractId );
		renderJson( "contract" , contract );
	}
	
	//进入回款页面
	public void contractPayment() {
		String contractId = getPara();
		
		//合同详情
		ProxyContract contractDetail = ProxyContract.dao.queryContractDetail( contractId );
		setAttr( "contractDetail" , contractDetail );
		
		//该合同回款记录
		List< ProxyContractPayment > paiedList = ProxyContractPayment.dao.queryContractPaiedLists( contractId );
		setAttr( "paiedList" , paiedList );
		
		//待回款
		ProxyContractPayment pendingPay = ProxyContractPayment.dao.queryContractPendingPayment( Integer.parseInt( contractId ) );
		setAttr( "pendingPay" , pendingPay );
		
		renderJsp( "payment.jsp" );
	}
	
	//保存回款
	public void saveContractPayment() {
		JSONObject saveJson = new JSONObject();
		try {
			ProxyContractPayment payment = getModel( ProxyContractPayment.class );
			contractService.saveContractPaymentBack( payment );
			saveJson.put( "flag" , true );
		} catch (Exception e) {
			log.error( "saveContractPayment" , e );
			Res res = I18n.use( ( String )PropertiesPlugin.getParamMapValue( DictKeys.basename ) , getCookie( "_locale" ) );
			saveJson.put( "flag" , true );
			saveJson.put( "msg" , res.get( "operationError" ) );
		}
		renderJson( saveJson );
	}
	
	public void changeContractState() {
		JSONObject updateResult = new JSONObject();
		try {
			String state = getPara( "state" );
			String contractId = getPara( "contractId" );
			ProxyContract.dao.changeContractState( contractId , state );
			updateResult.put( "flag" , true );
		} catch (Exception e) {
			log.error( "saveContractPayment" , e );
			Res res = I18n.use( ( String )PropertiesPlugin.getParamMapValue( DictKeys.basename ) , getCookie( "_locale" ) );
			updateResult.put( "flag" , true );
			updateResult.put( "msg" , res.get( "operationError" ) );
		}
		renderJson( updateResult );
		
	}
	
	
	

}

