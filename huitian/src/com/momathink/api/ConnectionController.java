package com.momathink.api;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.interceptor.VerificationRequestInterceptor;
import com.momathink.common.tools.ToolOperatorSession;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

@Before( VerificationRequestInterceptor.class )
@Controller( controllerKey="/api" )
public class ConnectionController extends BaseController {

	private static final Logger log = Logger.getLogger( ConnectionController.class );
	
	private static final ConnectionService connectionService = new ConnectionService();
	
	/**
	 * 合作项目
	 */
	public void addCooperation() {
		JSONObject resultJson = new JSONObject();
		try {
			String netCooperateId = getPara( "cooperateId" );
			String cooperateName = getPara( "cooperateName" );
			resultJson = connectionService.addCooperationProject( netCooperateId , cooperateName );
		} catch (Exception e) {
			log.error( "addCooperation" , e );
			resultJson.put( "code" , "0" );
			resultJson.put( "message" , "添加合作项目异常。" );
		}
		renderJson( resultJson );
		
	}
	
	/**
	 * 合作项目批量同步
	 */
	public void manualSynchroCooperate() {
		List< JSONObject > resultList = new LinkedList< JSONObject >();
		
		String sourceList = getPara( "list" ) ;
		JSONArray jsonArr = JSONArray.parseArray( sourceList );
		long arrSize = jsonArr.size() ;
		for( int i = 0 ; i < arrSize ; i ++ ) {
			JSONObject projectJson = jsonArr.getJSONObject( i );
			projectJson = connectionService.manualSynchroCooperate( (String) projectJson.get( "cooperateId" ) , 
						(String) projectJson.get( "cooperateName" ) );
			resultList.add( projectJson );
		}
		renderJson( "list" , resultList );
		
	}
	
	/**
	 * 系统用户
	 */
	public void addSysUser() {
		
		String userName = getPara( "userName" );
		String userEmail = getPara( "userEmail" );
		String netSchoolId = getPara( "netAccountId" );
		String userType = getPara( "userType" );
		
		JSONObject resultJson = connectionService.addNetUserAccount( userName , userEmail , netSchoolId, userType );
		
		renderJson( resultJson );
	}
	
	/**
	 * 手动同步系统用户信息
	 */
	public void manualSynchroSysUser() {
		List< JSONObject > resultList = new LinkedList< JSONObject >();
		
		String sourceList = getPara( "list" ) ;
		JSONArray jsonArr = JSONArray.parseArray( sourceList );
		long arrSize = jsonArr.size() ;
		for( int i = 0 ; i < arrSize ; i ++ ) {
			JSONObject accountJson = jsonArr.getJSONObject( i );
			accountJson = connectionService.manualSynchroSysUserAccount( accountJson.getString( "netAccountId" ) , 
					accountJson.getString( "userName" ) , accountJson.getString( "userEmail" ) , accountJson.getString( "userType" ) );
			resultList.add( accountJson );
		}
		renderJson( "list" , resultList );
		
	}
	
	/**
	 * 网校登陆教务系统
	 */
	public void login() {
		JSONObject resultJson = new JSONObject();
		String accountId = getPara( "loginUserId" );
		SysUser accountUser = SysUser.dao.findById( accountId );
		if( accountUser == null ) {
			resultJson.put( "code" , "0" );
			resultJson.put( "message" , "教务系统中不存在该用户" );
		} else {
			resultJson.put( "code" , "1" );
			resultJson.put( "message" , "Successed!" );
			
			setSessionAttr("account_session", accountUser );
			setSessionAttr("operator_session",ToolOperatorSession.operatorSessionSet( accountId ) );
			
			if( Role.isTeacher( accountUser.getStr( "roleids" ) ) || Role.isStudent( accountUser.getStr( "roleids" ) ) 
					|| Role.isDudao( accountUser.getStr( "roleids" ) ) || Role.isJiaowu( accountUser.getStr( "roleids" ) ) ) {
				resultJson.put( "returnUrl" , "/course/courseSortListForMonth" );
			} else {
				resultJson.put( "returnUrl" , "/main/index" );
			}
		}
		
		renderJson( resultJson );
	}
	
	
	
}

