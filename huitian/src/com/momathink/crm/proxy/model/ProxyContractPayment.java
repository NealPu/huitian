package com.momathink.crm.proxy.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table( tableName="proxycontractpayment" )
public class ProxyContractPayment extends BaseModel<ProxyContractPayment> {

	private static final long serialVersionUID = 1894233396168556535L;
	
	public static final ProxyContractPayment dao = new ProxyContractPayment();

	/* 最近一次回款 */
	public ProxyContractPayment queryContractLastPay( Integer contractId ) {
		String querySql = " select * from proxycontractpayment where contractid = ? and state = 2 order by payorder desc limit 1 ";
		ProxyContractPayment pay = ProxyContractPayment.dao.findFirst( querySql , contractId );
		return pay;
	}
	
	/* 待回款 */
	public ProxyContractPayment queryContractPendingPayment( Integer contractId ) {
		String querySql = " select * from proxycontractpayment where contractid = ? and state = 1 ";
		return ProxyContractPayment.dao.findFirst( querySql , contractId );
	}

	/* 插入合同回款记录  */
	public void saveContractPayRules(Integer contractId, int state, String order, String amount ) {
		String insertSql = " insert into proxycontractpayment ( contractid , state , amount , payorder ) values ( ? , ? , ? , ? ) ";
		Db.update(insertSql , contractId , state , amount , order );
	}
	
	/**
	 * 插入合同回款记录 
	 *  返回当前payment的ID
	 *   */
	public Integer updateContractPayRules(Integer contractId,  String order, String amount ) {
		boolean paymentExist = payorderRuleExist( contractId , order );
		if( paymentExist ) {
			String updateSql = " update proxycontractpayment set amount = ? where payorder = ? and contractid = ? ";
			Db.update( updateSql , amount , order , contractId );
		} else {
			String insertSql = "insert into proxycontractpayment( contractid , state , amount , payorder ) select ? , ? , ? , ?  from "
					+ " proxycontractpayment where not exists( select contractid , payorder from proxycontractpayment where contractid = ? and payorder = ? ) ";
			Db.update( insertSql , contractId , 0 , amount , order , contractId , order );
		}
		ProxyContractPayment currentPayment = queryPaymentByContractIdAndOrder( contractId , order );
		return currentPayment.getInt( "id" );
	}
	
	public ProxyContractPayment queryPaymentByContractIdAndOrder( Integer contractId , String payOrder ) {
		String selectSql = " select id , contractid , payorder from proxycontractpayment where contractid = ? and payorder = ? ";
		return ProxyContractPayment.dao.findFirst( selectSql , contractId , payOrder );
	}
	
	public void removeOrderNotInOrderids( Integer contractId , String orderIds ) {
		String updateSql = " delete from proxycontractpayment where contractid = ? and id not in ( " + orderIds + " ) and payorder != 1 ";
		Db.update( updateSql , contractId );
	}
	
	public void updateNearlyPendingState( Integer contractId ) {
		String pendingSetSql = "update proxycontractpayment set state = 1 where contractid = ? and  payorder = "
				+ "	 ( select temp.nextorder from ( "
				+ " select ( max( payorder ) + 1 ) as nextorder from proxycontractpayment where contractid = ? and state = 2 ) temp ) ";
		int count = Db.update( pendingSetSql , contractId , contractId );
		System.out.println( count );
	}
	

	/* 回款记录 */
	public List<ProxyContractPayment> queryContractPaiedLists( String contractId ) {
		String selectSql = " select amount , backdate from proxycontractpayment where contractid = ? and state = 2 ";
		return dao.find( selectSql , contractId );
	}
	
	public List< ProxyContractPayment > queryContractPaymentRules( String contractId ) {
		String ruleSql = " select id , amount , payorder , state , contractid from proxycontractpayment where contractid = ? order by payorder asc ";
		return dao.find( ruleSql , contractId );
	}

	public void updateNextOrderPaymentState( Integer contractId , Integer currentOrder ) {
		String updateSql = " update proxycontractpayment set state = 1 where contractid = ? and state = 0 and payorder = ? ";
		Db.update( updateSql , contractId , currentOrder + 1 );
	}

	public boolean queryPaymentPendingExist( Integer contractId ) {
		String pendingCountSql = " select count( 1 ) pendingCounts from proxycontractpayment where contractid = ? and state = 0 ";
		ProxyContractPayment paymentCount = ProxyContractPayment.dao.findFirst( pendingCountSql , contractId );
		if( null != paymentCount && paymentCount.getLong( "pendingCounts" ) != null ) {
			return paymentCount.getLong( "pendingCounts" ) > 0;
		}
		return false;
	}

	public boolean payorderRuleExist( Integer contractId , String order ) {
		String orderCountSql = " select count( 1 ) ruleCounts from proxycontractpayment where contractid = ? and payorder = ? ";
		ProxyContractPayment paymentCount = ProxyContractPayment.dao.findFirst( orderCountSql , contractId , order );
		if( null != paymentCount && paymentCount.getLong( "ruleCounts" ) != null ) {
			return paymentCount.getLong( "ruleCounts" ) > 0;
		}
		return false;
	}



}

