package com.momathink.sys.sms.model;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @ClassName  URLConnIO
 * @package com.fairyhawk.service.sms
 * @description
 * @author  liuqinggang
 * @Create Date: 2013-3-20 下午6:08:07
 *
 */
public class URLConnIO {
	public URLConnIO() {

	}

	public static InputStream getSoapInputStream(String requestAddress,
			String servicesHost) {
		InputStream is = null;
		try {
			URL U = new URL(requestAddress);
			URLConnection conn = U.openConnection();
			conn.setRequestProperty("Host", servicesHost);
			conn.connect();
			is = U.openStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
}