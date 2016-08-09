package com.momathink.crm.proxy.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.common.tools.ExcelExportUtil;
import com.momathink.common.tools.ExcelExportUtil.Pair;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolOperatorSession;
import com.momathink.crm.proxy.model.Proxy;
import com.momathink.crm.proxyproject.model.ProxyProject;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

public class ProxyService extends BaseService {

	private static final Logger log = Logger.getLogger( ProxyService.class );
	
	public List< Record > proxyList( SplitPage splitPage , Integer sysuserId ) {
		
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
		List< Record > list = page.getList();
		/*for( Record record : list ) {
			
			
		}*/
		splitPage.setPage(page);
		return list;
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

	public void saveProxySysAccount( String proxyId , SysUser proxyAccount , Integer sysuserId ) {
		SysUser account = SysUser.dao.findById( sysuserId );
		boolean agents = ToolOperatorSession.judgeRole( "firstagents" , account.getStr( "roleids" ) );
		String roleCode = "firstagents";
		if( agents ) {
			roleCode = "secondagents";
		}
		Role accountRole = Role.dao.getRoleByNumbers( roleCode );
		
		proxyAccount.set( "roleids" ,  accountRole.getInt( "id" ) + "," );
		proxyAccount.set( "user_pwd" , ToolMD5.getMD5("111111") );
		proxyAccount.set( "create_time" , ToolDateTime.getDate() );
		proxyAccount.set( "createuserid" , sysuserId );
		proxyAccount.set("state", "0");
		boolean flag = proxyAccount.save();
		if( flag ) {
			Proxy.dao.updateProxyAccount( proxyAccount.getPrimaryKeyValue() , proxyAccount.getStr( "email" ) , proxyId );
		}
	}

	public void exportProxyRecord( HttpServletResponse response , HttpServletRequest request , List< Record > recordList ) {
		
		for( Record record : recordList ) {
			int typeVal = record.getInt( "type" ).intValue();
			record.set( "typeName" , typeVal == 0 ? "个人" : "机构" );
			record.set( "tel" , typeVal == 0 ? record.getStr( "tel" ) : record.getStr( "companytel" ) );
			record.set( "stateName" , record.getInt( "state" ).intValue() == 0 ? "正常" : "取消" );
			record.set( "createdate" , ToolDateTime.format( record.getDate( "createdate" )  , ToolDateTime.pattern_ymd ) );
		}
		
		List< Pair > titles = new LinkedList< Pair >();
		titles.add( new Pair( "typeName" , "类型" ) );
		titles.add( new Pair( "personname" , "姓名" ) );
		titles.add( new Pair( "companyname" , "机构名称" ) );
		titles.add( new Pair( "tel" , "联系电话" ) );
		titles.add( new Pair( "location" , "所在地" ) );
		titles.add( new Pair( "address" , "通讯地址" ) );
		titles.add( new Pair( "createdate" , "添加日期" ) );
		titles.add( new Pair( "createname" , "创建人" ) );
		titles.add( new Pair( "commissioner" , "服务专员" ) );
		titles.add( new Pair( "stateName" , "状态" ) );
		
		ExcelExportUtil.exportByRecord( response , request , "代理列表", titles , recordList );
		
	}
	
	/**
	 * 
	 */
	public JSONObject importProxyRecord( UploadFile file , Integer sysUserId , String i18nState ) {
		JSONObject resultJson = new JSONObject();
		
		Res resInfo = I18n.use( (String) PropertiesPlugin.getParamMapValue( DictKeys.basename ) ,  i18nState );
		String msg = "" ;
		boolean flag = false;
		try {
			
			String fileName = file.getFileName();
			String path = file.getSaveDirectory() + fileName ;
			if( fileName.toLowerCase().endsWith( ".xls" ) ) {
				
				Map< String , Object > flagList = dealDataByPath( path );
				flag = (boolean) flagList.get("flag");
				if ( !flag ) {
					msg = resInfo.get( "UseCorrectTemplate" );
				} else {
					@SuppressWarnings("unchecked")
					List< Map < String , String>> list = ( List < Map < String , String > > ) flagList.get("list"); // 分析EXCEL数据
					Map<String, Object> saveMsg =  forAddXLSDB( list , sysUserId );
					StringBuilder sb = new StringBuilder();
					if( null != saveMsg.get( "save" ) && (Integer)saveMsg.get( "save" ) != 0  ) {
						sb.append( "您成功导入了 " ).append( saveMsg.get("save") ).append( " 条代理信息  " );
					}
					if( !( (boolean)saveMsg.get( "saveflag" ) ) ) {
						sb.append( "本次导入存在的问题如下：<br>" );
						sb.append( flagList.get("errormsg") ).append("<br>	");
						sb.append( saveMsg.get("saveMsg") );
					}
					msg = sb.toString();
				}
			}
			
		} catch ( Exception e ) {
			log.error( "importProxyRecord" , e );
			msg = resInfo.get( "dataHandleError" );
			flag = false;
		} finally {
			File fe = new File( file.getSaveDirectory() + file.getFileName() );
			fe.delete();
		}
		resultJson.put( "flag" , flag );
		resultJson.put( "msg" , msg );
		return resultJson;
		
	}

	private Map< String , Object > dealDataByPath( String path ) {
		
		Map< String , Object > flagMap = new HashMap< String , Object >();
		List< Map < String , String > > list = new ArrayList< Map < String , String > > ();
		// 工作簿
		HSSFWorkbook hwb = null;
		flagMap.put( "flag" , false );
		
		try {
			hwb = new HSSFWorkbook( new FileInputStream( new File( path ) ) );

			HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
			
			HSSFRow titleRow = sheet.getRow( 0 );
			HSSFCell titleRowCell = titleRow.getCell( 0 );
			titleRowCell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// "判断是否为提供模版"
			boolean flag = isProvidedModel( titleRow );
			if ( !flag ) {
				flagMap.put( "flag" , false );
				return flagMap;
			}
			
			int rowNums = sheet.getLastRowNum();
			// "获取文件内容"
			circle: for (int i = 1; i < rowNums + 1; i++ ) {// 第二行开始取值，第一行为标题行

				HSSFRow row = sheet.getRow(i); // 获取到第i列的行数据(表格行)
				if( null == row ) {
					break circle ;
				}

				Map< String , String > map = new HashMap < String , String >();
				mapmsg:for ( int j = 0 , tabSize = Proxy.TabName.length ; j < tabSize ; j++ ) {

					HSSFCell cell = row.getCell( j ); // 获取到第j行的数据(单元格)
					
					//列标题
					titleRowCell = titleRow.getCell( j );
					titleRowCell.setCellType( HSSFCell.CELL_TYPE_STRING );
					
					if( cell == null && j == 0 ) {
						break circle; // 第一列没有内容（NullPointException），结束整个excel的读取
					}
					if( cell == null && j != 0 ) {
						continue mapmsg;
					}

					if ( cell != null ) {
						cell.setCellType( HSSFCell.CELL_TYPE_STRING );
						String obj = cell.getStringCellValue() ;
						map.put( Proxy.TabDBName[j] , obj.trim() );
					}
				}
				list.add(map);
			}
			flagMap.put( "list" , list );
			flagMap.put( "flag" , true );
		} catch ( FileNotFoundException e ) {
			log.error( "importProxyDealData" , e );
			e.printStackTrace();
		} catch ( IOException e ) {
			log.error( "importProxyDealData" , e );
			e.printStackTrace();
		}
		return flagMap;
	}
	
	private boolean isProvidedModel( HSSFRow titleRow ) {
		List< String > strList = new ArrayList< String >();
		for ( String tabName : Proxy.TabName ) {
			strList.add( tabName );
		}
		
		for ( int i = 0 , tabNameLength = Proxy.TabName.length ; i < tabNameLength ; i++ ) {
			HSSFCell cellName = titleRow.getCell(i);
			cellName.setCellType(HSSFCell.CELL_TYPE_STRING);
			String realName = cellName.getStringCellValue();
			if ( !( StrKit.notBlank( realName ) && strList.contains( realName.trim() ) ) ) {
				return false;
			}
		}
		return true;
	}
	
	@Before( Tx.class )
	private Map< String , Object > forAddXLSDB( final List< Map< String , String > > list , final Integer sysUserId  ) {

		final Map< String, Object > saveMsg = new HashMap< String , Object >();
		saveMsg.put( "saveflag" , true );
		
		final StringBuilder sb = new StringBuilder("");
		
		SysUser account = SysUser.dao.findById( sysUserId );
		boolean agents = ToolOperatorSession.judgeRole( "firstagents" , account.getStr( "roleids" ) );
		int proxyAgentState = 0;
		if( agents ) {
			proxyAgentState = 1;
		}
		
		int save = 0;
		for ( Map< String , String> map : list ) { 
			Proxy proxy = new Proxy();
			for ( Map.Entry< String , String > entry : map.entrySet() ) { 
				if( Proxy.TabDBName[0].equals( entry.getKey() ) ) { //类型
					proxy.set( "type" ,  "机构".equals( entry.getValue().trim() ) ? 1 : 0 );
				} else {
					proxy.set(entry.getKey(), entry.getValue() != null ? entry.getValue().trim() : entry.getValue() );
				}
			}
			
			try {
				boolean saveFlag = saveImportProxy( proxy , sysUserId , proxyAgentState );
				if ( saveFlag ) {
					save++;
					saveMsg.put( "save" , save );
				} else {
					sb.append( "学生姓名为：" ).append( proxy.getStr( "name" ) );
					sb.append( " 、 学号为：" ).append( proxy.getStr( "num" ) );
					sb.append( "<br>" );
					saveMsg.put( "saveflag" , false );
				}
			}catch( Exception ex ) {
				log.error( "importStudentSave" , ex );
				saveMsg.put( "saveflag" , false );
			}
		}
		
		saveMsg.put( "saveMsg" , sb );
		return saveMsg;
	}
	
	private boolean saveImportProxy( Proxy proxy , Integer sysUserId , int proxyAgentState ) {
		boolean flag = false;
		String personIDcard = proxy.getStr( "IDcard" );
		if( StrKit.notBlank( personIDcard ) ) { //填写了身份证号的导入，每填写的不导入
			Proxy oldData = Proxy.dao.queryCurrentProxyByIDNumber ( personIDcard );
			if( null != oldData && null != oldData.getInt( "id" ) ) { //导入的身份证已存在，更新数据
				proxy.set( "id" , oldData.getInt( "id" ) );
				flag = proxy.update();
			} else {
				proxy.set( "agents" , proxyAgentState );
				proxy.set( "state" , 0 );
				proxy.set( "createuserid" , sysUserId );
				proxy.set( "createdate" , ToolDateTime.getDate() );
				flag = proxy.save();
			}
		}
		return flag;
	}

	/**
	 * 
	 */
	public void cooperationProjectLists( SplitPage splitPage , String proxyId ) {
		StringBuilder fromSbSql = new StringBuilder();
		List< Object > paramValue = new LinkedList< Object >();
		Map< String , String > paramMap = splitPage.getQueryParam();
		
		String selectSql = " select proxy.id , proxy.createdate , proxy.state , proxy.startdate , proxy.enddate , project.projectname  ";
		fromSbSql.append( " from proxyproject proxy left join cooperationproject project on project.id = proxy.projectid " );
		fromSbSql.append( " where proxy.proxyid = ? " );
		paramValue.add( proxyId );
		
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey();
			if( "projectName".equals( entryKey ) ) {
				fromSbSql.append( " and project.projectname like ? " );
				paramValue.add( "%" + entry.getValue() + "%"  );
			}
		}
		
		if( StrKit.isBlank( splitPage.getOrderColunm() ) || StrKit.isBlank( splitPage.getOrderMode() ) ) {
			fromSbSql.append( " order by project.id desc " ); 	
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSbSql.toString() , paramValue.toArray() );
		splitPage.setPage( page );
		
	}

	public JSONObject endingCooperation( String cooperationId , String i18nState ) {
		JSONObject resultJson = new JSONObject();
		try {
			ProxyProject.dao.endingProjectCooperation( cooperationId , 1 );
			resultJson.put( "flag" , true );
		} catch (Exception e) {
			log.error( "endingCooperation" , e );
			Res res = I18n.use( (String) PropertiesPlugin.getParamMapValue(DictKeys.basename) , i18nState );
			resultJson.put( "flag" , false );
			resultJson.put( "msg" , res.get( "operationError" ) );
		}
		return resultJson;
	}

	public void uncooperationProject( SplitPage splitPage , String proxyId ) {
		StringBuilder fromSbSql = new StringBuilder();
		List< Object > paramValue = new LinkedList< Object >();
		Map< String , String > paramMap = splitPage.getQueryParam();
		
		Proxy proxy = Proxy.dao.findById( proxyId );
		String cooperatedId = ProxyProject.dao.queryProxyCooperatedIds( Integer.parseInt( proxyId ) );
		
		Integer parentProxyAccountId = proxy.getInt( "createuserid" );
		SysUser account = SysUser.dao.findById( parentProxyAccountId );
		boolean agents = ToolOperatorSession.judgeRole( "firstagents" , account.getStr( "roleids" ) );
		
		String selectSql = " select project.projectname , project.id  ";
		if( agents ) {
			selectSql += " , pro.proxyid , pro.id proId , pro.startdate , pro.enddate ";
		}
		
		fromSbSql.append( " from cooperationproject project " );
		if( agents ) {//二级代理的未合作项目从一级代理的合作项目中关联出来
			fromSbSql.append( " left join proxyproject pro on pro.projectid = project.id " );
		}
		fromSbSql.append( " where project.state = 0 " );
		
		if( StrKit.notBlank( cooperatedId ) ) {
			fromSbSql.append( " and project.id not in ( " ).append( cooperatedId ).append( " ) " );
		}
		
		if( agents ) {
			Proxy parentProxy = Proxy.dao.queryProxyByAccount( parentProxyAccountId );
			fromSbSql.append( " and pro.proxyid = ? and pro.state = 0  " );
			paramValue.add( parentProxy.getInt( "id" )  );
		}
		
		for( Map.Entry< String , String > entry : paramMap.entrySet() ) {
			String entryKey = entry.getKey();
			if( "projectName".equals( entryKey ) ) {
				fromSbSql.append( " and project.projectname like ? " );
				paramValue.add( "%" + entry.getValue() + "%"  );
			}
		}
		
		Page< Record > page = Db.paginate( splitPage.getPageNumber() , splitPage.getPageSize() , selectSql , fromSbSql.toString() , paramValue.toArray() );
		splitPage.setPage( page );
		
	}

	public JSONObject sureProjectCooperation( ProxyProject project , String i18nState ) {
		JSONObject resultJson = new JSONObject();
		try {
			
			Date startDate = project.getDate( "startdate" );
			if( null == startDate ) {
				project.set( "startdate" , ToolDateTime.parse( "1900-01-01" , ToolDateTime.pattern_ymd ) );
			}
			Date endDate = project.getDate( "enddate" );
			if( null == endDate ) {
				project.set( "enddate" , ToolDateTime.parse( "9999-12-31" , ToolDateTime.pattern_ymd ) );
			}
			
			project.set( "createdate" , ToolDateTime.getDate() );
			project.set( "state" , 0 );
			project.save();
			
			resultJson.put( "flag" , true );
		} catch (Exception e) {
			log.error( "sureProjectCooperation" , e );
			Res res = I18n.use( (String) PropertiesPlugin.getParamMapValue(DictKeys.basename) , i18nState );
			resultJson.put( "flag" , false );
			resultJson.put( "msg" , res.get( "operationError" ) );
		}
		return resultJson;
	}



}

