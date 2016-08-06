package com.momathink.crm.proxy.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table( tableName="proxycontract" )
public class ProxyContract extends BaseModel<ProxyContract> {

	private static final long serialVersionUID = -1888877526244195547L;
	
	public static final ProxyContract dao = new ProxyContract();

	public List<ProxyContract> queryContractByProxyId( String proxyId ) {
		String querySql = " select * from proxycontract where proxyid = ? ";
		return dao.find( querySql , proxyId );
	}

	public ProxyContract queryContractDetail( String contractId ) {
		String querySql = " select contract.id , contract.contractname , contract.contractcode , contract.amount , contract.effectivedate , "
				+ " contract.proxyid , contract.state , pro.type , pro.personname , pro.companyname "
				+ " from proxycontract contract left join proxy pro on pro.id = contract.proxyid where contract.id = ? ";
		return dao.findFirst( querySql , contractId );
	}

	
	public void changeContractState( String contractId , String state ) {
		String updateState = " update proxycontract set state = ? where id = ? ";
		Db.update( updateState , state , contractId );
	}

	public void updateContractEndPayment( Integer contractId ) {
		String updatePaystate = " update proxycontract set paystate = 1 where id = ? ";
		Db.update( updatePaystate , contractId );
	}

}

