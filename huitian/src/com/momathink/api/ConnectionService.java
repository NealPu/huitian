package com.momathink.api;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.crm.proxyproject.model.CooperationProject;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

public class ConnectionService   {

	private static final Logger log = Logger.getLogger( ConnectionService.class );
	
	public JSONObject addCooperationProject( String netCooperationId , String cooperationName ) {
		JSONObject resultJson = new JSONObject();
		try {
			CooperationProject project = CooperationProject.dao.queryByNetSchoolId( netCooperationId );
			if( null != project ) {
				project.set( "projectname" , cooperationName );
				project.update();
				resultJson.put( "code" , "1" );
			} else {
				Integer projectId = CooperationProject.dao.addNetCooperationProject( netCooperationId , cooperationName );
				if( null == projectId ) {
					resultJson.put( "code" , "0" );
					resultJson.put( "message" , "保存失败" );
				} else {
					resultJson.put( "code" , "1" );
					resultJson.put( "message" , "保存成功" );
				}
			}
		} catch (Exception e) {
			log.error( "addCooperation" , e );
			resultJson.put( "code" , "0" );
			resultJson.put( "message" , "添加合作项目异常。" );
		}
		return resultJson ;
		
	}
	
	/**
	 * 手动同步合作项目
	 */
	public JSONObject manualSynchroCooperate( String netCooperateId , String cooperateName )  {
		JSONObject resultJson = new JSONObject();
		try {
			CooperationProject project = CooperationProject.dao.queryByNetSchoolId( netCooperateId );
			if( null == project ) {
				Integer jwProjectId = CooperationProject.dao.addNetCooperationProject( netCooperateId , cooperateName );
				if( null == jwProjectId ) {
					resultJson.put( "code" , "0" );
				} else {
					resultJson.put( "code" , "1" );
				}
			} else {
				project.set( "projectname" , cooperateName );
				project.update();
				resultJson.put( "code" , "1" );
			}
		} catch ( Exception e ) {
			log.error( "manualSynchroCooperate" , e );
			resultJson.put( "code" , "0" );
		}
		resultJson.put( "cooperateId" , netCooperateId );
		return resultJson;
	}


	/**
	 * 添加单个系统用户
	 */
	public JSONObject addNetUserAccount( String userName , String userEmail , String netSchoolId , String userType ) {
		JSONObject resultJson = new JSONObject();
		try {
			SysUser userAccount = SysUser.dao.queryByNetSchoolId(netSchoolId);
			if( null != userAccount ) {
				userAccount.set( "real_name" , userName );
				userAccount.set( "email" , userEmail );
				userAccount.update();
				
				resultJson.put( "jwSysUserId" , userAccount.getInt( "id" ) );
				resultJson.put( "code" , "1" );
				resultJson.put( "message" , "保存成功" );
			} else {
				Integer jwSysUserId = SysUser.dao.insertNetUserAccount( userEmail , userName , netSchoolId );
				if( null == jwSysUserId ) {
					resultJson.put( "code" , "0" );
					resultJson.put( "message" , "保存失败" );
				} else {
					resultJson.put( "code" , "1" );
					resultJson.put( "message" , "保存成功" );
					resultJson.put( "jwSysUserId" , jwSysUserId );
					
					if( "1".equals( userType ) ) {
						SysUser teacherAccount  = SysUser.dao.findById( jwSysUserId );
						Role teacherRole = Role.dao.getRoleByNumbers( "teachers" );
						teacherAccount.set( "roleids" , teacherRole.getInt( "id" ) + "," );
						teacherAccount.update();
					}
				}
			}
		} catch ( Exception e ) {
			log.error( "addNetUserAccount" , e );
			resultJson.put( "code" ,"0" );
			resultJson.put( "msg" ,"添加用户异常" );
		}
		resultJson.put( "netAccountId" , netSchoolId );
		return null;
	}


	/**
	 * 手动同步系统用户
	 */
	public JSONObject manualSynchroSysUserAccount( String netSchoolId , String userEmail, String accountName, String userType ) {
		JSONObject resultJson = new JSONObject();
		try {
			SysUser account = SysUser.dao.queryByNetSchoolId( netSchoolId );
			if( null == account ) {
				resultJson = addNetUserAccount( accountName , userEmail , netSchoolId , userType );
			} else {
				account.set( "real_name" , accountName );
				account.set( "email" , userEmail );
				account.update();
				
				resultJson.put( "jwSysUserId" , account.getInt( "id" ) );
				resultJson.put( "code" , "1" );
				resultJson.put( "message" , "保存成功" );
			}
		} catch ( Exception e ) {
			log.error( "manualSynchroCooperate" , e );
			resultJson.put( "code" , "0" );
			resultJson.put( "message" , "保存异常" );
		}
		resultJson.put( "netAccountId" , netSchoolId );
		return resultJson;
	}

	
	
	
}

