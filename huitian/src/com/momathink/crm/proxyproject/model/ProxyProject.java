package com.momathink.crm.proxyproject.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table( tableName="proxyproject" )
public class ProxyProject extends BaseModel< ProxyProject > {

	private static final long serialVersionUID = -8780251294173823238L;

	public static final ProxyProject dao = new ProxyProject();

	public List< ProxyProject > currentProxyProjectList( String proxyId ) {
		String querySql = " select proxy.id , proxy.proxyid , proxy.projectid , proxy.state , proxy.startdate , proxy.enddate , project.cooperationname "
				+ " from proxyproject proxy left join cooperationproject project on project.id = proxy.projectid "
				+ " where proxyid = ? and project.state = 0 order by state asc ";
		return dao.find( querySql , proxyId );
	}
	
	
	
}

