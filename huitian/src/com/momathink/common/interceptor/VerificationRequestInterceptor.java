package com.momathink.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.momathink.common.tools.ToolMD5;


public class VerificationRequestInterceptor implements Interceptor {
	
	private static final Logger log = Logger.getLogger( VerificationRequestInterceptor.class );
	@Override
	public void intercept( Invocation ai ) {
		
		HttpServletRequest request = ai.getController().getRequest();
		String pullSignature = request.getParameter("signature");
		pullSignature = StrKit.isBlank( pullSignature ) ? "" : pullSignature;
		
		String pullTimestamp = request.getParameter("timestamp");
		String spliceSignature = ToolMD5.getMD5( ToolMD5.getMD5( pullTimestamp + "huitian" ) );
		
		pullTimestamp = StrKit.isBlank( pullTimestamp ) ? "0" : pullTimestamp ;
		long timestamp = Long.parseLong( pullTimestamp );
		
		JSONObject json = new JSONObject();
		long differenceTime = ( System.currentTimeMillis() - timestamp )/( 1000 * 60 );
		if( timestamp != 0 ){
			if( differenceTime < 5 ) {
				if( pullSignature.equals( spliceSignature ) ) {
					ai.invoke();
				} else {
					json.put( "code" , 0 );
					json.put( "message" , "签名不符" );
					ai.getController().renderJson( json );
				}
			} else {
				json.put( "code" , 0 );
				json.put( "message" , "请求超时" );
				ai.getController().renderJson( json );
			}
		} else {
			json.put( "code" , 0 );
			json.put( "message" , "请求时间戳格式不正确" );
			ai.getController().renderJson( json );
		}
	}


}

