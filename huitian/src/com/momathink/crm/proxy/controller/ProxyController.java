package com.momathink.crm.proxy.controller;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.crm.proxy.service.ProxyService;
import com.momathink.sys.system.model.SysUser;

@Controller( controllerKey="/proxy" )
public class ProxyController extends BaseController {

	private static final Logger log = Logger.getLogger( ProxyController.class );
	private static final ProxyService proxyService = new ProxyService();
	
	
	//说明：系统人员可以添加一级代理，一级代理可以添加代理（二级），二级代理没有代理管理
	public void list() {
		try {
			Integer sysuserId = getSysuserId();
			String queryType = getPara( "_query.searchType" );
			List< Record > recordList = proxyService.proxyList( splitPage , sysuserId );
			if( StrKit.notBlank( queryType ) && "1".equals( queryType )  ) {
				proxyService.exportProxyRecord( getResponse() , getRequest() , recordList );
				renderNull();
			} else {
				renderJsp( "list.jsp" );
			}
		} catch (Exception e) {
			log.error( "proxy.list" , e );
			renderError( 500 );
		}
	}
	
	public void newProxy() {
		renderJsp( "newproxy.jsp" );
	}
	
	public void editProxy() {
		String proxyId = getPara();
		Proxy proxy = Proxy.dao.findById( proxyId );
		setAttr( "proxy" , proxy );
		renderJsp( "newproxy.jsp" );
	}
	
	public void viewProxy() {
		Proxy proxy = proxyService.viewProxyDetailMsg( getPara() );
		setAttr( "proxy" , proxy );
		renderJsp( "viewproxy.jsp" );
	}
	
	public void saveProxy() {
		try {
			Proxy proxy = getModel( Proxy.class );
			Integer sysUserId = getSysuserId();
			String locale = getCookie( "_locale" );
			JSONObject proxySave = proxyService.saveProxy( sysUserId , proxy , locale );
			renderJson( proxySave );
		} catch (Exception e) {
			log.error( "error" , e );
		}
	}
	
	public void openProxyAccount() {
		String proxyId = getPara();
		setAttr( "proxyId" , proxyId );
		Proxy proxyInfo = Proxy.dao.findById( proxyId );
		setAttr( "proxyInfo" , proxyInfo );
		renderJsp( "openproxyaccount.jsp" );
	}
	
	public void saveProxySysAccount() {
		JSONObject resultJson = new JSONObject();
		try {
			String proxyId = getPara( "proxyId" );
			SysUser proxyAccount = getModel(  SysUser.class );
			proxyService.saveProxySysAccount( proxyId , proxyAccount , getSysuserId() );
			
			resultJson.put( "flag" , true );
		} catch (Exception e) {
			log.error( "saveProxySysAccount" , e );
			Res res = I18n.use( ( String )PropertiesPlugin.getParamMapValue( DictKeys.basename ) , getCookie( "_locale" ) );
			resultJson.put( "flag" , false );
			resultJson.put( "msg" , res.get( "operationError" ) );
		}
		renderJson( resultJson );
	}
	
	public void viewProxyAccount() {
		String proxyId = getPara();
		Proxy proxyAccountInfo = Proxy.dao.proxyWithAccountState( proxyId );
		setAttr( "proxyAccount" , proxyAccountInfo );
		renderJsp( "viewproxyaccount.jsp" );
	}
	
	@Before( Tx.class )
	public void importProxy() {
		JSONObject resultJson = new JSONObject();
		UploadFile file = getFile( "fileField" , Proxy.importProxyTemplateFile );
		String i18nState = getCookie( "_locale" );
		if( null == file ) {
			resultJson.put( "flag" , false );
			Res resInfo = I18n.use( (String) PropertiesPlugin.getParamMapValue( DictKeys.basename ) ,  i18nState );
			resultJson.put( "msg" , resInfo.get( "SelectYourFiles" ) );
		} else {
			resultJson = proxyService.importProxyRecord( file , getSysuserId() , i18nState  ) ;
		}
		renderJson( resultJson );
	}
	
	public void downloadTemplate() {
		File template = new File( PathKit.getWebRootPath() + Proxy.proxyImportTemplatePath );
		renderFile( template );
	}
	
}

