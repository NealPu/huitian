package com.momathink.crm.proxyproject.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
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

	public String queryProxyCooperatedIds( Integer proxyId ) {
		String querySql = " select group_concat( projectid ) cooperatedIds  from proxyproject where proxyid = ? and state = 0  ";
		ProxyProject project = dao.findFirst( querySql , proxyId );
		if( null != project ) {
			return project.getStr( "cooperatedIds" );
		}
		return "" ;
	}

	public void endingProjectCooperation(String cooperationId , int state ) {
		String updateSql = " update proxyproject set state = ? where id = ?  ";  
		Db.update( updateSql , state , cooperationId );
	}

	public ProxyProject queryCooperationByProIds(String proxyId, String projectId) {
		
		return null;
	}
	
	
	
}

