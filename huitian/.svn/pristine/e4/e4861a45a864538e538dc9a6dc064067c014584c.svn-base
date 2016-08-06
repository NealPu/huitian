package com.momathink.common.base;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class Util {
	private static Properties props = new Properties();
	static InputStream ips = Util.class.getClassLoader().getResourceAsStream("smsconfig.properties");

	/**
	 * 数组转字符串
	 * 
	 * @param arrs
	 * @return
	 */
	public static String printArray(String[] arrs) {
		StringBuffer str = new StringBuffer("");
		if(arrs.length>0){
			for (String arr : arrs) {
				str.append(arr+"|");
			}
			str.deleteCharAt(str.length()-1);
		}
		return str.toString();
	}

	public static String getTomorrowDate() {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getPropVal(String key) {
		try {
			props.load(ips);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("加载配置文件错误！");
		}
		String value = props.getProperty(key);
		return value;
	}

	public static void setPropVal(String key, String value) {
		try {
			props.load(ips);
			props.setProperty(key, value);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("加载配置文件错误！");
		}
	}

	public static void main(String[] args) {
		System.out.println(getTomorrowDate());
	}
}
