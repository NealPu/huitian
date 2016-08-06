package com.momathink.crm.proxy.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolOperatorSession;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.sys.system.model.SysUser;

public class ProxyService extends BaseService {

	private static final Logger log = Logger.getLogger( ProxyService.class );
	
	public void proxyList( SplitPage splitPage , Integer sysuserId ) {
		
		List< Object > paramValue = new LinkedList<Object>();
		StringBuilder fromSql = new StringBuilder();
		String selectSql = " select pro.* , sysuser.real_name createname ";
		fromSql.append( " from proxy pro left join account sysuser on sysuser.id = pro.createuserid " );
		SysUser account = SysUser.dao.findById( sysuserId );
		boolean agents = ToolOperatorSession.judgeRole( "firstagents" , account.getStr( "roleids" ) );
		if( agents ) {
			//一级代理查看自己的代理
			fromSql.append( " where pro.agents = 1 and pro.createuserid = ? " );
			paramValue.add( sysuserId );
		} else {
			//系统人员查看所有一级代理
			fromSql.append( " where pro.agents = 0 " );
		}
		
		Map< String , String > paramMap = splitPage.getQueryParam();
		for ( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String key = entry.getKey();
			if( "personname".equals( key ) ) {
				String valueStr = "%" + entry.getValue() + "%" ;
				fromSql.append( " and ( pro.personname like ? or pro.companyname like ? ) " );
				paramValue.add( valueStr ) ; 
				paramValue.add( valueStr ) ; 
			}
			if( "location".equals( key ) ) {
				fromSql.append( " and pro.location like ? " );
				paramValue.add( "%" + entry.getValue() + "%" );
			}
			if( "startdate".equals( key ) ) {
				fromSql.append( " and date_format( pro.createdate , '%Y-%m-%d' ) >= ? " );
				paramValue.add( entry.getValue() );
			}
			if( "enddate".equals( key ) ) {
				fromSql.append( " and date_format( pro.createdate , '%Y-%m-%d' ) <= ? " );
				paramValue.add( entry.getValue() );
			}
			if( "createname".equals( key ) ) {
				fromSql.append( " and sysuser.real_name like ? " );
				paramValue.add( "%" + entry.getValue() + "%" );
			}
			if( "commissioner".equals( key ) ) {
				fromSql.append( " and pro.commissioner like ? " );
				paramValue.add( "%" + entry.getValue() + "%" );
			}
			if( "type".equals( key ) ) {
				fromSql.append( " and pro.type = ? " );
				paramValue.add( entry.getValue() );
			}
		} 
		
		if( StrKit.notBlank( splitPage.getOrderColunm() ) && StrKit.notBlank( splitPage.getOrderMode() ) ) {
			fromSql.append( " order by " ).append( splitPage.getOrderColunm() ) .append( "  " ).append( splitPage.getOrderMode() );
		} else {
			fromSql.append( " order by pro.createdate desc " );
		}
		
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSql.toString() , paramValue.toArray() );
		/*List< Record > list = page.getList();
		for( Record record : list ) {
			
			
		}*/
		splitPage.setPage(page);
		
	}

	/* 保存代理 */
	public JSONObject saveProxy( Integer sysUserId , Proxy proxy , String locale ) {
		JSONObject resultJson = new JSONObject();
		boolean flag = false;
		String msg = "";
		try {
			Integer proxyId = proxy.getInt( "id" );
			if( null != proxyId ) {
				proxy.update();
			} else {
				SysUser account = SysUser.dao.findById( sysUserId );
				boolean agents = ToolOperatorSession.judgeRole( "firstagents" , account.getStr( "roleids" ) );
				if( agents ) {
					//当前用户为一级代理，新建的代理标识为二级代理
					proxy.set( "agents" , 1 );
				} else {
					proxy.set( "agents" , 0 );
				}
				
				proxy.set( "createuserid" , sysUserId );
				proxy.set( "createdate" , ToolDateTime.getDate() );
				proxy.set( "state" , 0 );
				proxy.save();
			}
			flag = true;
		} catch ( Exception e ) {
			Res res = I18n.use( ( String )PropertiesPlugin.getParamMapValue( DictKeys.basename ) , locale );
			log.error( "saveProxy" , e );
			flag = false;
			msg = res.get( "operationError" );
		}
		resultJson.put( "flag" , flag );
		resultJson.put( "msg" , msg );
		return resultJson;
	}
	
	public Proxy viewProxyDetailMsg( String proxyId ) {
		Proxy proxy = Proxy.dao.queryProxyWithCreateName( proxyId );
		//可用余额   合作项目 开通记录中 实时查询消耗的费用，  用预存额减去 
		double proxyPredeposit = proxy.getDouble( "predeposit" );
		proxy.put( "usableBalance" , proxyPredeposit );
		
		return proxy;
	}

}

