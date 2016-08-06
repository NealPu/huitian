package com.momathink.common.tools;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.jfinal.kit.StrKit;

/**
 * 公共工具类
 * @author 董华健  2012-9-7 下午2:20:06
 */
public class ToolUtils {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ToolUtils.class);
	
	/**
	 * double精度调整
	 * @param doubleValue 需要调整的值123.454
	 * @param format 目标样式".##"
	 * @return
	 */
	public static String decimalFormatToString(double doubleValue, String format){
		DecimalFormat myFormatter = new DecimalFormat(format);  
		String formatValue = myFormatter.format(doubleValue);
		return formatValue;
	}
	
	/**
	 * 获取UUID by jdk
	 * @author 董华健    2012-9-7 下午2:22:18
	 * @return
	 */
	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 电话号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-?[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	public static String getUuidByJdk(boolean is32bit){
		String uuid = UUID.randomUUID().toString();
		if(is32bit){
			return uuid.toString().replace("-", ""); 
		}
		return uuid;
	}
	public static boolean CheckKeyWord(String sWord){
	      String StrKeyWord = "select|insert|delete|from|count/(|drop table|update|truncate|asc/(|mid/(|char/(|xp_cmdshell|exec master|netlocalgroup administrators|:|net user";
	      for(String str:StrKeyWord.split("\\|")){
	    	  if(sWord.trim().indexOf(str)!=-1)
	    		  return true;
	      }
	      return false;
	    }
	
	public static String changeDataRule(String data){
		if(StrKit.isBlank(data))
			return null;
		else{
			String newData = "";
			for(int i=data.length();i>0;i--){
				if(newData.length()>0){
					newData = newData.substring(newData.length()-1, newData.length())+data.substring(i-1, i)+newData.substring(0, newData.length()-1);
				}else{
					newData = data.substring(i-1, i);
				}
			}
			return newData;
		}
	}
	
	public static String returnOldData(String data){
		if(StrKit.isBlank(data))
			return null;
		else{
			String newData = "";
			while(data.length()>0){
				while(data.length()==1){
					newData += data;
					return newData;
				}
				while(data.length()>1){
					newData += data.substring(1, 2);
					data = data.substring(2, data.length())+data.substring(0, 1);
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args){
		System.out.println(isPhone("0432-67151281"));
		System.out.println(changeDataRule("635_19260"));
		System.out.println(returnOldData("9151454667"));
	}
	
}
