package com.momathink.crm.proxy.model;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table( tableName="proxy" )
public class Proxy extends BaseModel<Proxy> {

	private static final long serialVersionUID = -4599136050085925619L;
	
	public static final Proxy dao = new Proxy();

	public void updateProxyAmounts( Integer proxyId, Double contractAmount ) {
		String updateSql = " update proxy set predeposit = predeposit + ? where id = ? ";
		Db.update( updateSql , contractAmount , proxyId );
	}
	
	public void updateProxyAmountsByContractId( Integer contractId , Double paymentAmount ) {
		String updateSql = " update proxy left join proxycontract contract on contract.proxyid = proxy.id "
				+ "  set proxy.predeposit = proxy.predeposit + ? where contract.id = ?  ";
		Db.update( updateSql , paymentAmount , contractId );
	}

	public Proxy queryProxyWithCreateName( String proxyId ) {
		String querySql = " select pro.* , sysuser.real_name createName from proxy pro left join account sysuser "
				+ " on pro.createuserid = sysuser.id where pro.id = ? ";
		return dao.findFirst( querySql , proxyId );
	}

	public void updateProxyAccount(Integer sysAccountId , String loginMail , String proxyId ) {
		String updateSql = " update proxy set sysaccountid = ? , loginaccount = ? where id = ? ";
		Db.update( updateSql , sysAccountId , loginMail , proxyId );
	}

	public Proxy proxyWithAccountState( String proxyId ) {
		String querySql = " select proxy.id , proxy.personname , proxy.companyname ,proxy.type , proxy.sysaccountid , proxy.loginaccount , "
				+ " account.state accountState from proxy left join account on proxy.sysaccountid = account.id where proxy.id = ? ";
		return dao.findFirst( querySql , proxyId );
	}
	
	

}

