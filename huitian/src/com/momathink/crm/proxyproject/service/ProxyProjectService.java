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

public class ProxyProjectService extends BaseService {

	public void cooperationProjectList( SplitPage splitPage , Integer sysUserId ) {
		
		List< Object > paramValues = new LinkedList< Object >();
		StringBuilder fromSqlSb = new StringBuilder();
		
		String selectSql = " select project.*   ";
		fromSqlSb.append( " from cooperationproject project " );
		fromSqlSb.append( " where project.state = 0  " );
		
		Map< String , String > paramMap = splitPage.getQueryParam();
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey();
			if( "projectName".equals( entryKey ) ) {
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
	
	public void cooperatedProjectList( SplitPage splitPage , Integer sysUserId ) {
		
		List< Object > paramValues = new LinkedList< Object >();
		StringBuilder fromSqlSb = new StringBuilder();
		
		String selectSql = " select    ";
		fromSqlSb.append( " from  " );
		
		fromSqlSb.append( " where   " );
		
		Map< String , String > paramMap = splitPage.getQueryParam();
		
		
		
		if( StrKit.notBlank( splitPage.getOrderColunm() ) && StrKit.notBlank( splitPage.getOrderMode() ) ) {
			fromSqlSb.append( " " ).append( splitPage.getOrderColunm() ).append( "  " ).append( splitPage.getOrderMode() );
		} else {
			fromSqlSb.append( " order by  desc " );
		}
		
	}
	
	

}

