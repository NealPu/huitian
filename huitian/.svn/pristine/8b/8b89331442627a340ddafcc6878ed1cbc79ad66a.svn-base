package com.momathink.common.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.system.model.SysLog;

/**
 * 公共Controller
 * @author David
 */
public abstract class BaseController extends Controller {

	private static Logger log = Logger.getLogger(BaseController.class);
	
	/**
	 * 全局变量
	 */
	protected String id;			// 主键
	protected SplitPage splitPage;	// 分页封装
	protected List<?> list;			// 公共list
	protected SysLog reqSysLog;		//访问日志
	/**
	 * 请求/WEB-INF/下的视图文件
	 */
	public void toUrl() {
		String toUrl = getPara("toUrl");
		render(toUrl);
	}


	/**
	 * 获取项目请求根路径
	 * @return
	 */
	protected String getCxt() {
		return getAttr("cxt");
	}
	
	/**
	 * 重写getPara，进行二次decode解码
	 */
	@Override
	public String getPara(String name) {
		String value = getRequest().getParameter(name);
		if(null != value && !value.isEmpty()){
			try {
				value = URLDecoder.decode(value, ToolString.encoding);
			} catch (UnsupportedEncodingException e) {
				log.error("decode异常："+value);
			}
		}
		return value;
	}

	
	/**
	 * 获取查询参数
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	public Map<String, String> getQueryParam(){
		Map<String, String> queryParam = new HashMap<String, String>();
		Enumeration<String> paramNames = getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			String value = getPara(name);
			if (name.startsWith("_query") && !value.isEmpty()) {// 查询参数分拣
				String key = name.substring(7);
				if(null != value && !value.trim().equals("")){
					queryParam.put(key, value.trim());
				}
			}
		}
		
		return queryParam;
	}

	/**
	 * 调用此方法 可以页面查询条件回填可以直接写成value="${name}"  不需要写成value="${paramMap['_query.name']}"
	 * @param queryParam
	 */
	public void setqueryParamMap(Map<String, String> queryParam){
		Map<String,Object> params = new HashMap<String,Object>();
		Enumeration<String> paramNames = getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			String value = getPara(name);
			if (!name.startsWith("_query") && !value.isEmpty()) {
				if (null != value && !value.trim().equals("")) {
					params.put(name, value.trim());
				}
			}
		}
		for (Map.Entry<String, String> entry : queryParam.entrySet()){
			params.put(entry.getKey(), entry.getValue());
		}
		
		setAttrs(params);
	}
	
	/**
	 * 设置默认排序
	 * @param colunm
	 * @param mode
	 */
	public void defaultOrder(String colunm, String mode){
		if(null == splitPage.getOrderColunm() || splitPage.getOrderColunm().isEmpty()){
			splitPage.setOrderColunm(colunm);
			splitPage.setOrderMode(mode);
		}
	}
	
	/**
	 * 排序条件
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	public String getOrderColunm(){
		String orderColunm = getPara("orderColunm");
		return orderColunm;
	}

	/**
	 * 排序方式
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	public String getOrderMode(){
		String orderMode = getPara("orderMode");
		return orderMode;
	}
	

	/************************************		get 	set 	方法		************************************************/
	
	public SysLog getReqSysLog() {
		return reqSysLog;
	}

	public void setReqSysLog(SysLog reqSysLog) {
		this.reqSysLog = reqSysLog;
	}

	public void setSplitPage(SplitPage splitPage) {
		this.splitPage = splitPage;
	}

	public Integer getSysuserId(){
		Record sysuser = getSessionAttr("account_session");
		if(sysuser==null){
			return null;
		}else{
			return sysuser.getInt("id");
		}
	}
	
	public boolean getIsEn(){
		String locale = getPara("_locale");
	       if (StrKit.notBlank(locale)) {	// change locale, write cookie
	        setCookie("_locale", locale, Const.DEFAULT_I18N_MAX_AGE_OF_COOKIE);
	       } else {	 // get locale from cookie and use the default locale if it is null
	        locale = getCookie("_locale");
	        if (StrKit.isBlank(locale))
	         locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
	       }
	       if(locale.equals("en_US"))
	       return true;
	       else
	       return false;
	}
}
