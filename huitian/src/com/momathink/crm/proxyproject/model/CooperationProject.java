package com.momathink.crm.proxyproject.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table( tableName="cooperationproject" )
public class CooperationProject extends BaseModel< CooperationProject > {

	private static final long serialVersionUID = -5204782195173416404L;
	
	public static final CooperationProject dao = new CooperationProject();

	public List< CooperationProject > AllProjectNotInProxy( String proxyId ) {
		List< CooperationProject > allProject = queryAllProject();
		String proxySql = " select project.id , project.projectname , project.state , project.price "
				+ " from cooperationproject project right join proxyproject proxy on proxy.projectid = project.id "
				+ " where proxy.id = ? and project.state = 0 ";
		List< CooperationProject > proxyProject = dao.find( proxySql , proxyId );
		allProject.removeAll( proxyProject );
		
		return allProject;
	}
	
	public List< CooperationProject > queryAllProject() {
		String sql = " select id , projectname , state , price from cooperationproject where state = 0 ";
		return dao.find( sql );
	}
	
	
	

}

