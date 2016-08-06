package com.momathink.crm.proxy.service;

import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.crm.proxy.model.ProxyContract;
import com.momathink.crm.proxy.model.ProxyContractPayment;

public class ProxyContractService extends BaseService {

	//某代理的合同基本信息
	public List<ProxyContract> queryProxyContractBaseInfo( String proxyId ) {
		List< ProxyContract > contractList = ProxyContract.dao.queryContractByProxyId( proxyId );
		for( ProxyContract contract : contractList ) {
			Integer contractId = contract.getInt( "id" );
			ProxyContractPayment lastPay = ProxyContractPayment.dao.queryContractLastPay( contractId );
			contract.put( "lastPay" , lastPay );
			
			ProxyContractPayment pendingPay = ProxyContractPayment.dao.queryContractPendingPayment( contractId );
			contract.put( "pendingPay" , pendingPay );
			
		}
		return contractList;
	}

	@Before( Tx.class )
	public void saveProxyContract( Integer createUser , ProxyContract contract , Map< String , String > paymentVals ) {
		Integer contractId = contract.getInt( "id" );
		boolean flag = false;
		if( contractId != null ) {
			flag = contract.update();
			if( flag ) {
				StringBuilder payOrderIdSb = new StringBuilder( "" );
				for ( Map.Entry< String , String > entry : paymentVals.entrySet() ) {  
					String order = entry.getKey();
					Integer payOrderId = ProxyContractPayment.dao.updateContractPayRules( contractId  , order , entry.getValue() );
					payOrderIdSb.append( "," ).append( payOrderId );
				}
				ProxyContractPayment.dao.removeOrderNotInOrderids( contractId , payOrderIdSb.toString().substring( 1 ) );
				ProxyContractPayment.dao.updateNearlyPendingState( contractId );
			}
		} else {
			contract.set( "createuserid" , createUser );
			contract.set( "createdate" , ToolDateTime.getDate() );
			contract.set( "state" , 0 );
			contract.set( "paystate" , 0 );
			flag = contract.save();
			if( flag ) {
				contractId = contract.getInt( "id" );
				for ( Map.Entry< String , String > entry : paymentVals.entrySet() ) {  
					String order = entry.getKey();
					int state = "1".equals( order ) ? 1 : 0;
					ProxyContractPayment.dao.saveContractPayRules( contractId , state , order , entry.getValue() );
				} 
			}
		}
		
	}

	//保存合同回款
	public void saveContractPaymentBack( ProxyContractPayment payment ) {
		payment.set( "state" , 2 );
		boolean flag = payment.update();
		if( flag ) {
			payment = ProxyContractPayment.dao.findById( payment.getInt( "id" ) );
			Integer contractId = payment.getInt( "contractid" );
			
			//更新代理的预存款金额
			Double paymentAmount = payment.getDouble( "amount" );
			Proxy.dao.updateProxyAmountsByContractId( contractId , paymentAmount );
			
			//更新下一个为待回款
			Integer currentOrder = payment.getInt( "payorder" );
			ProxyContractPayment.dao.updateNextOrderPaymentState( contractId , currentOrder );
			
			//没有待回款，更新合同状态为回款完成
			boolean pendingExist = ProxyContractPayment.dao.queryPaymentPendingExist( contractId );
			if( !pendingExist ) {
				ProxyContract.dao.updateContractEndPayment( contractId );
			}
			
		}
	}

	//某合同：（具有回款规则）
	public ProxyContract queryContractWithPaymentRules( String contractId ) {
		ProxyContract contract = ProxyContract.dao.findById( contractId );
		List< ProxyContractPayment > paymentLists = ProxyContractPayment.dao.queryContractPaymentRules( contractId );
		contract.put( "rules" , paymentLists );
		return contract;
	}
	
	
}

