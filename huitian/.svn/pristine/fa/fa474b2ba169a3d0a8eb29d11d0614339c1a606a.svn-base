package com.momathink.common.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.momathink.common.constants.DictKeys;

/**
 * 读取Properties配置文件数据放入缓存
 * @author David
 */
public class PropertiesPlugin implements IPlugin {

    protected final Logger log = Logger.getLogger(getClass());

	/**
	 * 保存系统配置参数值
	 */
	private static Map<String, Object> paramMap = new HashMap<String, Object>();
	
    private Properties properties;

	public PropertiesPlugin(Properties properties){
		this.properties = properties;
	}

	/**
	 * 获取系统配置参数值
	 * @param key
	 * @return
	 */
	public static Object getParamMapValue(String key){
		return paramMap.get(key);
	}
	
	@Override
	public boolean start() {
		paramMap.put(DictKeys.db_type_key, properties.getProperty(DictKeys.db_type_key).trim());
		
		String jdbcUrl = null;
		String database = null;
		
		// 判断数据库类型
		String db_type = (String) getParamMapValue(DictKeys.db_type_key);
		if(db_type.equals(DictKeys.db_type_postgresql)){ // pg 数据库连接信息
			// 读取当前配置数据库连接信息
			paramMap.put(DictKeys.db_connection_driverClass, properties.getProperty("postgresql.driverClass").trim());
			paramMap.put(DictKeys.db_connection_jdbcUrl, properties.getProperty("postgresql.jdbcUrl").trim());
			paramMap.put(DictKeys.db_connection_userName, properties.getProperty("postgresql.userName").trim());
			paramMap.put(DictKeys.db_connection_passWord, properties.getProperty("postgresql.passWord").trim());
			
			// 解析数据库连接URL，获取数据库名称
			jdbcUrl = (String) getParamMapValue(DictKeys.db_connection_jdbcUrl);
			database = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
			database = database.substring(database.indexOf("/") + 1);
			
		}else if(db_type.equals(DictKeys.db_type_mysql)){ // mysql 数据库连接信息
			// 读取当前配置数据库连接信息
			paramMap.put(DictKeys.db_connection_driverClass, "com.mysql.jdbc.Driver");
			paramMap.put(DictKeys.db_connection_jdbcUrl, properties.getProperty("mysql.jdbcUrl").trim());
			paramMap.put(DictKeys.db_connection_userName, properties.getProperty("mysql.userName").trim());
			paramMap.put(DictKeys.db_connection_passWord, properties.getProperty("mysql.passWord").trim());

			// 解析数据库连接URL，获取数据库名称
			jdbcUrl = (String) getParamMapValue(DictKeys.db_connection_jdbcUrl);
			database = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
			database = database.substring(database.indexOf("/") + 1, database.indexOf("?"));
		}
		
		// 解析数据库连接URL，获取数据库地址IP
		String ip = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
		ip = ip.substring(0, ip.indexOf(":"));

		// 解析数据库连接URL，获取数据库地址端口
		String port = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
		port = port.substring(port.indexOf(":") + 1, port.indexOf("/"));
		
		// 把数据库连接信息写入常用map
		paramMap.put(DictKeys.db_connection_ip, ip);
		paramMap.put(DictKeys.db_connection_port, port);
		paramMap.put(DictKeys.db_connection_dbName, database);
		
		// 数据库连接池信息
		paramMap.put(DictKeys.db_initialSize, Integer.parseInt(properties.getProperty(DictKeys.db_initialSize).trim()));
		paramMap.put(DictKeys.db_minIdle, Integer.parseInt(properties.getProperty(DictKeys.db_minIdle).trim()));
		paramMap.put(DictKeys.db_maxActive, Integer.parseInt(properties.getProperty(DictKeys.db_maxActive).trim()));
		
		// 把常用配置信息写入map
		paramMap.put(DictKeys.config_securityKey_key, properties.getProperty(DictKeys.config_securityKey_key).trim());
		paramMap.put(DictKeys.db_connection_name_main, properties.getProperty(DictKeys.db_connection_name_main).trim());
		paramMap.put(DictKeys.config_passErrorCount_key, Integer.parseInt(properties.getProperty(DictKeys.config_passErrorCount_key)));
		paramMap.put(DictKeys.config_passErrorHour_key, Integer.parseInt(properties.getProperty(DictKeys.config_passErrorHour_key)));
		paramMap.put(DictKeys.config_maxPostSize_key, Integer.valueOf(properties.getProperty(DictKeys.config_maxPostSize_key)));
		paramMap.put(DictKeys.config_devMode, properties.getProperty(DictKeys.config_devMode));
		
		// 微信配置写入Map
	//	paramMap.put(DictKeys.weixin_token, properties.getProperty(DictKeys.weixin_token).trim());
	//	paramMap.put(DictKeys.weixin_appId, properties.getProperty(DictKeys.weixin_appId).trim());
		//paramMap.put(DictKeys.weixin_appSecret, properties.getProperty(DictKeys.weixin_appSecret).trim());
		paramMap.put(DictKeys.weixin_gzh, properties.getProperty(DictKeys.weixin_gzh).trim());
		paramMap.put(DictKeys.weixin_gzhname, properties.getProperty(DictKeys.weixin_gzhname).trim());
		paramMap.put(DictKeys.weixin_xsgzh, properties.getProperty(DictKeys.weixin_xsgzh).trim());
		
		// 公司信息配置
	/*	paramMap.put(DictKeys.company_name, properties.getProperty(DictKeys.company_name).trim());
		paramMap.put(DictKeys.company_kfemail, properties.getProperty(DictKeys.company_kfemail).trim());
		paramMap.put(DictKeys.company_website, properties.getProperty(DictKeys.company_website).trim());*/
		paramMap.put(DictKeys.company_defaultCanUseKeshi, properties.getProperty(DictKeys.company_defaultCanUseKeshi).trim());
		paramMap.put(DictKeys.company_zuidaweichengdan, properties.getProperty(DictKeys.company_zuidaweichengdan).trim());
		//推广返佣比例
		paramMap.put(DictKeys.promo_lv1, properties.getProperty(DictKeys.promo_lv1).trim());
		paramMap.put(DictKeys.promo_lv2, properties.getProperty(DictKeys.promo_lv2).trim());
		paramMap.put(DictKeys.promo_lv3, properties.getProperty(DictKeys.promo_lv3).trim());
		paramMap.put(DictKeys.promo_lv4, properties.getProperty(DictKeys.promo_lv4).trim());
		paramMap.put(DictKeys.promo_lv5, properties.getProperty(DictKeys.promo_lv5).trim());
		paramMap.put(DictKeys.promo_default, properties.getProperty(DictKeys.promo_default).trim());
		
		//i18n
		paramMap.put(DictKeys.basename, properties.getProperty(DictKeys.basename).trim());
		
		return true;
	}

	@Override
	public boolean stop() {
		paramMap.clear();
		return true;
	}

}
