package com.momathink.crm.proxyproject.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolOperatorSession;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.crm.proxyproject.model.ProxyProject;
import com.momathink.sys.system.model.SysUser;

public class ProxyProjectService extends BaseService {

	/**
	 * 所有合作项目列表
	 */
	public void cooperationProjectList( SplitPage splitPage , Integer sysUserId ) {
		
		List< Object > paramValues = new LinkedList< Object >();
		StringBuilder fromSqlSb = new StringBuilder();
		
		String selectSql = " select project.*   ";
		fromSqlSb.append( " from cooperationproject project " );
		fromSqlSb.append( " where project.state = 0  " );
		
		Map< String , String > paramMap = splitPage.getQueryParam();
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey();
			if( "projectname".equals( entryKey ) ) {
				fromSqlSb.append( " and project.projectname like ? " );
				paramValues.add( "%" + entry.getValue() + "%" );
			}
		}
		
		if( StrKit.notBlank( splitPage.getOrderColunm() ) && StrKit.notBlank( splitPage.getOrderMode() ) ) {
			fromSqlSb.append( " " ).append( splitPage.getOrderColunm() ).append( " " ).append( splitPage.getOrderMode() );
		} else {
			fromSqlSb.append( " order by project.projectname desc " );
		}
		
		Page< Record > page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), selectSql, fromSqlSb.toString(), paramValues.toArray() );
		splitPage.setPage( page );
	}
	
	/**
	 * 未合作项目
	 */
	public void notCooperationProjectList(SplitPage splitPage, Integer sysUserId) {
		List< Object > paramValues = new LinkedList< Object >();
		Map< String , String > paramMap = splitPage.getQueryParam();
		
		Proxy proxy = Proxy.dao.queryProxyByAccount( sysUserId );
		Integer proxyId = proxy.getInt( "id" );
		String cooperatedProjectIds = ProxyProject.dao.queryProxyCooperatedIds( proxyId );
		
		Integer createProxyUserId = proxy.getInt( "createuserid" );
		SysUser createAccount = SysUser.dao.findById( createProxyUserId );
		
		boolean isFirstAgents = ToolOperatorSession.judgeRole( "firstagents" , createAccount.getStr( "roleids" ) );
		
		StringBuilder fromSbSql = new StringBuilder( "" );
		String selectSql = " select project.*  ";
		fromSbSql.append( " from cooperationproject project where project.state = 0 " );
		if( isFirstAgents ) {
			Proxy parentAgents = Proxy.dao.queryProxyByAccount( createProxyUserId );
			String parentProjectIds = ProxyProject.dao.queryProxyCooperatedIds( parentAgents.getInt( "id" ) );
			fromSbSql.append( " and project.id in ( " ).append( parentProjectIds ).append( " ) " );
			
			fromSbSql.append( " and project.id not in ( " ).append( cooperatedProjectIds ).append( " ) " );
		} else {
			if( StrKit.notBlank( cooperatedProjectIds ) ) {
				fromSbSql.append( " and project.id not in ( " ).append( cooperatedProjectIds ).append( " ) " );
			}
		}
		
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey() ;
			if( "projectname".equals( entryKey ) ) {
				fromSbSql.append( " and project.projectname like ? " );
				paramValues.add( "%" + entry.getValue() + "%" );
			}
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSbSql.toString(), paramValues.toArray() );
		splitPage.setPage( page );
		
	}
	
	/**
	 * 未开放的合作项目（针对二级代理，一级代理没有合作的项目为未开放的项目）
	 */
	public void notOpenedProject( SplitPage splitPage , Integer sysUserId ) {
		List< Object > paramValues = new LinkedList< Object >();
		Map< String , String > paramMap = splitPage.getQueryParam();
		
		Proxy proxy = Proxy.dao.queryProxyByAccount( sysUserId );
		Integer createProxyUserId = proxy.getInt( "createuserid" );
		Proxy firstAgents = Proxy.dao.queryProxyByAccount( createProxyUserId );
		Integer agentId =  firstAgents.getInt( "id" );
		String firstAgentProjectIds = ProxyProject.dao.queryProxyCooperatedIds( agentId );
		
		StringBuilder fromSbSql = new StringBuilder( "" );
		String selectSql = " select project.*  ";
		fromSbSql.append( " from cooperationproject project  " );
		fromSbSql.append( " where project.state = 0 " );
		if( StrKit.notBlank( firstAgentProjectIds ) ) {
			fromSbSql.append( " and project.id not in ( " ).append( firstAgentProjectIds ).append( " ) " );
		}
		
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey();
			if( "projectname".equals( entryKey ) ) {
				fromSbSql.append( " and project.projectname like ? " );
				paramValues.add( "%" + entry.getValue() + "%" );
			}
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSbSql.toString() , paramValues.toArray() );
		splitPage.setPage( page );
		
	}
	
	/**
	 * 代理已合作项目
	 */
	public void cooperatedProjectList( SplitPage splitPage , Integer sysUserId ) {
		
		Proxy proxy = Proxy.dao.queryProxyByAccount( sysUserId );
		
		List< Object > paramValues = new LinkedList< Object >();
		StringBuilder fromSqlSb = new StringBuilder();
		
		String selectSql = " select project.projectname , pro.id , pro.projectid , pro.state ,  pro.createdate , pro.enddate , pro.startdate  ";
		fromSqlSb.append( " from proxyproject pro left join cooperationproject project on project.id = pro.projectid " );
		fromSqlSb.append( " where proxyid = ? " );
		paramValues.add( proxy.getInt( "id" ) );
		
		Map< String , String > paramMap = splitPage.getQueryParam();
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey();
			if( "projectname".equals( entryKey ) ) {
				fromSqlSb.append( " and project.projectname like ? " );
				paramValues.add( "%" + entry.getValue() + "%" );
			}
		}
		
		if( StrKit.notBlank( splitPage.getOrderColunm() ) && StrKit.notBlank( splitPage.getOrderMode() ) ) {
			fromSqlSb.append( " " ).append( splitPage.getOrderColunm() ).append( "  " ).append( splitPage.getOrderMode() );
		} else {
			fromSqlSb.append( " order by pro.state asc , pro.createdate desc " );
		}
		
		Page< Record > page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), selectSql, fromSqlSb.toString() , paramValues.toArray() );
		splitPage.setPage( page );
		
	}
	
	

}

