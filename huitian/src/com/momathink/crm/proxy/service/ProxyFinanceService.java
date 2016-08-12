package com.momathink.crm.proxy.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;

public class ProxyFinanceService extends BaseService {

	
	//代理回款记录
	public void repaymentList( SplitPage splitPage , Integer sysuserId ) {
		
		List< Object > paramValue = new LinkedList< Object >();
		Map< String , String > queryParam = splitPage.getQueryParam();
		StringBuilder fromSql = new StringBuilder();
		
		String selectSql = " select payment.amount , payment.backdate , contract.contractname , contract.contractcode , proxy.personname , "
				+ " proxy.type , proxy.companyname ";
		fromSql.append( " from proxycontractpayment payment  " );
		fromSql.append( " left join proxycontract contract on contract.id = payment.contractid " );
		fromSql.append( " left join proxy on proxy.id = contract.proxyid " );
		fromSql.append( " where payment.state = 2  " );
		
		for( Map.Entry< String , String > entry : queryParam.entrySet() ) {
			String entryKey = entry.getKey();
			if( "proxyname".equals( entryKey ) ) {
				String entryValue = entry.getValue();
				fromSql.append( " and ( proxy.personname like ? or proxy.companyname like ? ) " );
				paramValue.add( "%" + entryValue + "%" ) ;
				paramValue.add( "%" + entryValue + "%" );
			}
			if( "contractname".equals( entryKey ) ) {
				fromSql.append( " and contract.contractname like ? " );
				paramValue.add( "%" + entry.getValue() + "%" );
			}
			if( "contractcode".equals( entryKey ) ) {
				fromSql.append( " and contract.contractcode = ? " );
				paramValue.add( entry.getValue() );
			}
		}
		
		if( StrKit.isBlank( splitPage.getOrderColunm() ) || StrKit.isBlank( splitPage.getOrderMode() ) ) {
			fromSql.append( " order by payment.backdate desc " );
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSql.toString() , paramValue.toArray() );
		splitPage.setPage( page );
		
	}

	public void nearlestToPayList( SplitPage splitPage ) {

		List< Object > paramValue = new LinkedList< Object >();
		Map< String , String > queryParam = splitPage.getQueryParam();
		StringBuilder fromSql = new StringBuilder();
		
		String selectSql = " select payment.amount , contract.contractname , contract.contractcode , proxy.personname , proxy.type , proxy.companyname ";
		fromSql.append( " from proxycontractpayment payment  " );
		fromSql.append( " left join proxycontract contract on contract.id = payment.contractid " );
		fromSql.append( " left join proxy on proxy.id = contract.proxyid " );
		fromSql.append( " where payment.state = 1 and proxy.state = 0 and contract.state = 0 " );
		
		for( Map.Entry< String , String > entry : queryParam.entrySet() ) {
			String entryKey = entry.getKey();
			if( "proxyname".equals( entryKey ) ) {
				String entryValue = entry.getValue();
				fromSql.append( " and ( proxy.personname like ? or proxy.companyname like ? ) " );
				paramValue.add( "%" + entryValue + "%" ) ;
				paramValue.add( "%" + entryValue + "%" );
			}
			if( "contractname".equals( entryKey ) ) {
				fromSql.append( " and contract.contractname like ? " );
				paramValue.add( "%" + entry.getValue() + "%" );
			}
			if( "contractcode".equals( entryKey ) ) {
				fromSql.append( " and contract.contractcode = ? " );
				paramValue.add( entry.getValue() );
			}
		}
		
		if( StrKit.isBlank( splitPage.getOrderColunm() ) || StrKit.isBlank( splitPage.getOrderMode() ) ) {
			fromSql.append( " order by payment.id asc " );
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSql.toString() , paramValue.toArray() );
		splitPage.setPage( page );
		
	}

	
	
	
}

