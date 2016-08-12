package com.momathink.crm.netschoolstudent.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.crm.netschoolstudent.model.NetStudentAccount;
import com.momathink.crm.netschoolstudent.service.NetSchoolStudentService;

@Controller( controllerKey="/netstudentaccount" )
public class NetSchoolStudentController extends BaseController {

	private static final NetSchoolStudentService netStudentService = new NetSchoolStudentService();
	
	/**
	 * 网校学生账号
	 */
	public void netStudentList() {
		Integer userAccountId = getSysuserId();
		netStudentService.netStudentSplitList( splitPage , userAccountId );
		renderJsp( "netstuaccounts.jsp" );
	}
	
	/**
	 * 创建
	 */
	public void addNetStudent() {
		renderJsp( "newnetstudent.jsp" );
	}
	
	public void saveNetStudent() {
		NetStudentAccount studentAccount = getModel( NetStudentAccount.class );
		JSONObject resultJson = netStudentService.saveNetStudentAccount( studentAccount , getSysuserId() );
		renderJson( resultJson );
	}
	
	/**
	 * 校验账号名称重复
	 */
	public void checkAccountNameExist() {
		String accountName = getPara( "accountName" );
		boolean existFlag = NetStudentAccount.dao.queryAccountNameExist( accountName );
		renderJson( existFlag );
	}
	
	/**
	 * 保存
	 */
	public void saveBatchNetStudent() {
		String[] accountOrders = getParaValues( "order" );
		Integer operateUserId = getSysuserId();
		for( String order : accountOrders ) {
			String accountName = getPara( "accountStudent_" + order ) ;
			if( StrKit.notBlank( accountName ) ) {
				NetStudentAccount studentAccount = new NetStudentAccount();
				studentAccount.set( "netstudentname" ,accountName );
				netStudentService.saveNetStudentAccount( studentAccount , operateUserId );
			}
		}
		renderJson( "flag" , true );
	}
	
	/**
	 * 手动发布账号到网校
	 */
	public void pushNetStudentAccount() {
		JSONObject pushJson = netStudentService.getPushStudentJson();
		renderJson( pushJson );
	}
	
	/**
	 * 网校返回结果处理
	 */
	public void netStudentPushResult() {
		String sourceList = getPara( "list" ) ;
		JSONArray jsonArr = JSONArray.parseArray( sourceList );
		long arrSize = jsonArr.size() ;
		for( int i = 0 ; i < arrSize ; i ++ ) {
			JSONObject accountJson = jsonArr.getJSONObject( i );
			if( "1".equals( accountJson.getString( "code" ) )  ) {
				NetStudentAccount.dao.updateNetAccountId( accountJson.getString( "jwStudentId" ) , accountJson.getString( "netSchoolUserId" ) );
			}
		}
		renderJson( "msg" , "Success!" );
	}
	
	
}

