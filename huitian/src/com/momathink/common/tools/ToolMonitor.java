package com.momathink.common.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.momathink.common.tools.securitys.Des3Encryption;
import com.momathink.crm.mediator.model.Organization;

/**
 * 售后服务 
 * @author dufuzhong
 */
public class ToolMonitor extends Thread {
	private static Logger log = Logger.getLogger(ToolMonitor.class);
	private static Date expirationDate = null;
	
	//---------------------------------------服务器状态报告 ----------------------------------------------------------
	/**
	 * 字段描述：客户信息ids 
	 * 字段类型：varchar(32)  长度：32
	 */
	private final String column_customerIds = "monitor.customerIds";
	
	/**
	 * 字段描述：监控类型1=启动,2=关闭 
	 * 字段类型：int(2)  长度：null
	 */
	private final String column_monitorTyp = "monitor.monitorTyp";
	
	/**
	 * 字段描述：服务器ip 
	 * 字段类型：varchar(50)  长度：50
	 */
	private final String column_serverIP = "monitor.serverIP";
	
	/**
	 * 字段描述：MAC地址 
	 * 字段类型：varchar(50)  长度：50
	 */
	private final String column_addressMAC = "monitor.addressMAC";
	
	/**
	 * 字段描述：项目路径 
	 * 字段类型：varchar(255)  长度：255
	 */
	private final String column_projectPath = "monitor.projectPath";
	
	/**
	 * 字段描述：操作日期 
	 * 字段类型：datetime  长度：null
	 */
	private final String column_operationDate = "monitor.operationDate";

	/**
	 * 字段描述：机构信息 
	 * 字段类型：text  长度：65535
	 */
	private final String column_organization = "organization";
	
	
	//---------------------------------------登陆统计 ----------------------------------------------------------
	
	/**
	 * 字段描述：客户信息ids 
	 * 字段类型：varchar(32)  长度：32
	 */
	private static final String visits_column_customerIds = "visits.customerIds";
	
	/**
	 * 字段描述：监控类型1=访问 
	 * 字段类型：int(2)  长度：null
	 */
	private static final String visits_column_visitsTyp = "visits.visitsTyp";
	
	/**
	 * 字段描述：网址 
	 * 字段类型：varchar(50)  长度：50
	 */
	private static final String visits_column_website = "visits.website";
	
	/**
	 * 字段描述：操作日期 
	 * 字段类型：datetime  长度：null
	 */
	private static final String visits_column_operationDate = "visits.operationDate";
	
	//----------------------------------------配置信息---------------------------------------------------------
	/**
	 * 授权码
	 */
	private static String authorization_code = "9c9af6c6f19d4ff898f37a89b46d7ba8";//默认开发版的,不用管它
	
	/**
	 * 服务器 网址
	 */
	private static String moma_url = "";
	
	/** 系统保留  服务器状态 接口路径  */
	public static final String parameter_InterfaceAddress = "/crm/monitor/report";
	
	/** 系统保留  服务器登录统计  接口路径  */
	public static final String parameter_visitsLogin = "/crm/visits/login";
	
	
	private String type = "1";//默认值
	/** * 系统启动时报告 MoMA创想 项目运作情况 */
	public static final String report_start = "1";
	/*** 定时报告 MoMA创想 项目运作情况 */
	public static final String report_timing = "2";
	/** * 登录时报告 MoMA创想 登录情况*/
	public static final String report_login = "3";

	public ToolMonitor(String type) {
		this.type = type;
	}
	
	public void run() {
		if(report_start.equals(type)){
			refresh();
			momaPost();
		}else if(report_timing.equals(type)){
			momaPost();
		}else if(report_login.equals(type)){
			loginPost();
		}
	}

	//----------------------------------------自定义方法---------------------------------------------------------
	/**
	 * 报告 MoMA创想 项目运作情况
	 */
	private void momaPost(){
		Map<String, String> monitor = new HashMap<String, String>();
		try {
			monitor.put(column_customerIds, authorization_code);
			monitor.put(column_monitorTyp, report_start);
			monitor.put(column_serverIP, getRealIp());
			monitor.put(column_addressMAC, getMACAddr());
			monitor.put(column_projectPath, PathKit.getWebRootPath());
			monitor.put(column_operationDate, getDate());
			//机构信息
			monitor.put(column_organization, Organization.dao.getOrganizationMessage().toJson());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StrKit.notBlank(moma_url))
			doPost(moma_url + parameter_InterfaceAddress, monitor);
	}
	
	/**
	 * 报告 MoMA创想 登陆一次
	 */
	private void loginPost(){
		Map<String, String> monitor = new HashMap<String, String>();
		try {
			monitor.put(visits_column_customerIds, authorization_code);
			monitor.put(visits_column_visitsTyp, "1");
			monitor.put(visits_column_website, PathKit.getWebRootPath());
			monitor.put(visits_column_operationDate, getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StrKit.notBlank(moma_url))
			doPost(moma_url + parameter_visitsLogin, monitor);
	}
	
	/**
	 * MAC地址 
	 * @return 如: "00 00 00 00 00 00"
	 */
	private String getMACAddr(){
		String mac = "";
		try {
			NetworkInterface netInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] macAddr = netInterface.getHardwareAddress();
			String macByte = "";
			// 将得来的地址转化为十六进制数
			for (byte b : macAddr) {
				macByte = Integer.toHexString((int) (b & 0xff));
				if (macByte.length() == 1) {
					macByte = "0" + macByte;
		 		}
				mac = mac+ " " + macByte;
			}
		} catch (Exception e) {
		}
		return mac.substring(1);
	}
	
	/**
	 * 服务器ip
	 * @return 如 : "111.111.111.111"
	 */
	private String getRealIp() {
		String netip = null;// 外网IP
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			boolean finded = false;// 是否找到外网IP
			while (netInterfaces.hasMoreElements() && !finded) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() 
							&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
						netip = ip.getHostAddress();
						finded = true;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		return netip;
	}
	
	/**
	 * 获取时间格式化后的
	 */
	private String getDate() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}
	
	 /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url
     *            请求的URL地址
     * @param params
     *            请求的查询参数,可以为null
     * @return 返回请求响应的HTML
     */
	private String doPost(String url, Map<String, String> params) {
        StringBuffer result = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        // 设置Http Post数据
        if (params != null) {
            NameValuePair[] data = new NameValuePair[params.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                data[i++] = new NameValuePair(entry.getKey(), entry.getValue());
            }
            method.setRequestBody(data);
        }
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
                String str = null;
                while ((str = reader.readLine()) != null) {
                    result.append(str);
                }
            }
        } catch (IOException e) {
        } finally {
            method.releaseConnection();
        }
        return result.toString();
    }

	/**
	 * 判断是否过期
	 * @param date 该日期,(注意:等于该天也算过期)
	 * @return 过期=true   未过期=false
	 */
	public static boolean whetherExpired(Date date){
		log.info("判断是否过期: 授权截止日期："+(expirationDate)+"      当前日期："+(date));
		boolean result = false;
		if(null == date || null == expirationDate || date.equals(expirationDate)){//|| ToolDateTime.format(expirationDate, ToolDateTime.pattern_ymd).equals(ToolDateTime.format(date,ToolDateTime.pattern_ymd))
			result = true;
		}else{
			//b.before(a)如果b的时间在a之前（不包括等于）返回true
			result = expirationDate.before(date);
		}
		log.info("判断是否过期: 结果："+(result)+"(过期=true 未过期=false)");
		return result;
	}
	
	/**
	 * 加载解析激活码信息
	 */
	public static boolean refresh() {
		try {
			Organization organization = Organization.dao.getOrganizationMessage();
			String licensekey = organization.getStr("LICENSEKEY");
			String expirationDateKeys = organization.getStr("EXPIRATIONDATEKEYS");
			log.info("授权码:"+(authorization_code)+"      系统激活码："+(expirationDateKeys));
			if(StrKit.isBlank(expirationDateKeys) && StrKit.isBlank(authorization_code))
				return false;
			authorization_code = licensekey.trim();
			expirationDateKeys = decryptFromHex(expirationDateKeys.trim());//解密
			if(null != expirationDateKeys){
				String[] keys = expirationDateKeys.split(",");//分开信息 0是到期日期,1是请求的服务器地址
				//到期日期
				expirationDate = ToolDateTime.getDate(keys[0], ToolDateTime.pattern_ymd);
				organization.set("expirationDate", expirationDate).update();
				//服务器地址
				moma_url = keys[1];
				log.info("系统授权解析成功");
				return true;
			}
			expirationDate = null;
			return false;
		} catch (Exception e) {
			log.info("系统授权解析验证异常");
			e.printStackTrace();
			expirationDate = null;
			return false;
		}
	}
	
	/**
     * 解密 
     * @param key
     * @param value
     * @return
     */
    private static String decryptFromHex(String value) throws Exception {
        return Des3Encryption.decryptFromHex(authorization_code.substring(0, 24), value);
    }
	
}
