package com.momathink.crm.proxyproject.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolOperatorSession;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.sys.system.model.SysUser;

@Table( tableName="cooperationproject" )
public class CooperationProject extends BaseModel< CooperationProject > {

	private static final long serialVersionUID = -5204782195173416404L;
	
	public static final CooperationProject dao = new CooperationProject();

	public List< CooperationProject > AllProjectNotInProxy( String proxyId ) {
		List< CooperationProject > allProject = null;
		Proxy proxy = Proxy.dao.findById( proxyId );
		Integer createUserId = proxy.getInt( "createuserid" );
		SysUser createAccount = SysUser.dao.findById( createUserId );
		boolean isFirstAgents = ToolOperatorSession.judgeRole( "firstagents" , createAccount.getStr( "roleids" ) );
		if( isFirstAgents ) {
			Proxy parentAgent = Proxy.dao.queryProxyByAccount( createUserId );
			allProject = proxyCooperationProjectLists( parentAgent.getInt( "id" ).toString() );
		} else {
			allProject = queryAllProject();
		}
		
		List< CooperationProject > proxyProject = proxyCooperationProjectLists( proxyId );
		allProject.removeAll( proxyProject );
		
		return allProject;
	}
	
	public List< CooperationProject > queryAllProject() {
		String sql = " select id , projectname , state , price from cooperationproject where state = 0 ";
		return dao.find( sql );
	}
	
	public List< CooperationProject > proxyCooperationProjectLists( String proxyId ) {
		String proxySql = " select project.id , project.projectname , project.state , project.price "
				+ " from cooperationproject project right join proxyproject proxy on proxy.projectid = project.id "
				+ " where proxy.id = ? and project.state = 0 ";
		return dao.find( proxySql , proxyId );
	}
	
	
	

}

