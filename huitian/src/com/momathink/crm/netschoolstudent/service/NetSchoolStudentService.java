package com.momathink.crm.netschoolstudent.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMD5;
import com.momathink.crm.netschoolstudent.model.NetStudentAccount;

public class NetSchoolStudentService extends BaseService {

	private static final Logger log = Logger.getLogger( NetSchoolStudentService.class );
	
	public void netStudentSplitList( SplitPage splitPage , Integer userAccountId ) {
		
		Map< String , String > paramMap = splitPage.getQueryParam();
		List< Object > paramValue = new LinkedList< Object >();
		
		StringBuilder fromSql = new StringBuilder();
		String selectSql = " select *  ";
		fromSql.append( " from netstudentaccount where accountstate = 0 and createuserid = ? " );
		paramValue.add( userAccountId );
		
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) { 
			String entryKey = entry.getKey();
			if( "studentaccount".equals( entryKey ) ) {
				fromSql.append( " and netstudentname = ? " );
				paramValue.add( entry.getValue() );
			}
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSql.toString() , paramValue.toArray() );	
		splitPage.setPage( page );
	}

	public JSONObject saveNetStudentAccount( NetStudentAccount studentAccount , Integer sysUserId ) {
		JSONObject resultJson = new JSONObject();
		try {
			String accountName = studentAccount.getStr( "netstudentname" );
			if( StrKit.isBlank( accountName ) ) {
				resultJson.put( "flag" , false );
				resultJson.put( "msg" , "没有填写账号名称。" );
				return resultJson;
			} else {
				
				boolean existFlag = NetStudentAccount.dao.queryAccountNameExist( accountName );
				if( existFlag ) {
					resultJson.put( "flag" , false );
					resultJson.put( "msg" , "账号名称不可用。" );
					return resultJson;
				} else {
					studentAccount.set( "grantstate" , 0 );
					studentAccount.set( "createdate" , ToolDateTime.getDate() );
					studentAccount.set( "createuserid" , sysUserId );
					studentAccount.set( "accountstate" , 0 );
					studentAccount.save();
				}
			}
			
			resultJson.put( "flag" , true );
		} catch (Exception e) {
			log.error( "saveNetSchoolStudentAccountError" , e );
			resultJson.put( "flag" , false );
			resultJson.put( "msg" , "保存异常。" );
		}
		return resultJson;
	}

	public JSONObject getPushStudentJson() {
		JSONObject pushJson = new JSONObject();
		List< JSONObject > sourceList = new LinkedList< JSONObject >();
		List< NetStudentAccount > unpushedStudent = NetStudentAccount.dao.unpushedStudentInfo();
		for( NetStudentAccount account : unpushedStudent ) {
			JSONObject accountInfo = new JSONObject();
			accountInfo.put( "jwStudentId" , account.getInt( "jwStudentId" ) ) ;
			accountInfo.put( "studentName" , account.getStr( "studentName" ) ) ;
			sourceList.add( accountInfo );
		}
		pushJson.put( "list" , sourceList );
		
		long timestamp = System.currentTimeMillis();
		pushJson.put( "timestamp" , timestamp );
		pushJson.put( "signature" , ToolMD5.getMD5( ToolMD5.getMD5( timestamp + "huitian" ) ) );
		
		return pushJson;
	}

}

