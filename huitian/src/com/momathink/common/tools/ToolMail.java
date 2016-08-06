package com.momathink.common.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.jfinal.kit.StrKit;
import com.momathink.common.plugin.MessagePropertiesPlugin;
import com.momathink.crm.mediator.model.Organization;
import com.sun.mail.smtp.SMTPAddressFailedException;

/**
 * javaMail发送邮件工具类 2015年8月10日prq
 * 
 * @author prq
 * 
 */
public class ToolMail {
	private static final Logger logger = Logger.getLogger(ToolMail.class);

	/**
	 * 发送邮件
	 * 
	 * @param mailServerHost
	 *            邮件服务器地址
	 * @param mailServerPort
	 *            邮件服务器端口
	 * @param validate
	 *            是否要求身份验证
	 * @param fromAddress
	 *            发送邮件地址
	 * @param toAddress
	 *            接收邮件地址//以,号分割多个接收地址
	 * @param ccAddress
	 *            抄送地址//以,号分割多个抄送地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param isHTML
	 *            是否是html格式邮件
	 * @param isSSL
	 *            邮件服务器是否需要安全连接(SSL)
	 * @return true:发送成功;false:发送失败
	 */
	@SuppressWarnings("static-access")
	public static boolean sendMail(boolean validate, String toAddress, String ccAddress, String subject, String content, boolean isHTML, boolean isSSL) {
		Organization org = Organization.dao.findById(1);
		String mailServerPort = org.getStr("email_serverport") != null ? org.getStr("email_serverport") : MessagePropertiesPlugin.getEmailParamMapValue("server_port");
		String mailServerHost = org.getStr("email_serverhost") != null ? org.getStr("email_serverhost") : MessagePropertiesPlugin.getEmailParamMapValue("server_host");
		String fromAddress = org.getStr("email_fromaddress") != null ? org.getStr("email_fromaddress") : MessagePropertiesPlugin.getEmailParamMapValue("from_address");
		String userPass = org.getStr("email_senderpassword") != null ? org.getStr("email_senderpassword") : MessagePropertiesPlugin.getEmailParamMapValue("sender_password");
		Properties p = new Properties();
		p.put("mail.smtp.host", mailServerHost);
		p.put("mail.smtp.port", mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		if (isSSL) {
			p.put("mail.smtp.starttls.enable", "true");
			p.put("mail.smtp.socketFactory.fallback", "false");
			p.put("mail.smtp.socketFactory.port", mailServerPort);
		}
		Authenticator auth = null;
		if (validate) {
			auth = new myAuthenticator(fromAddress, userPass);
		}

		try {
			//Session session = Session.getDefaultInstance(p, auth);
			Session session = Session.getInstance(p, auth);
			Message message = new MimeMessage(session);
			Address from = new InternetAddress(fromAddress);
			message.setFrom(from);
			// 发送
			if (StrKit.notBlank(toAddress)) {
				InternetAddress[] to = new InternetAddress().parse(toAddress);
				message.setRecipients(Message.RecipientType.TO, to);
			}
			// 抄送
			if (StrKit.notBlank(ccAddress)) {
				InternetAddress[] cc = new InternetAddress().parse(ccAddress);
				message.setRecipients(Message.RecipientType.CC, cc);
			}
			message.setSubject(subject);
			message.setSentDate(new Date());
			if (isHTML) {
				Multipart m = new MimeMultipart();
				BodyPart bp = new MimeBodyPart();
				bp.setContent(content, "text/html; charset=utf-8");
				m.addBodyPart(bp);
				message.setContent(m);
			} else
				message.setText(content);
			Transport.send(message);
			return true;
		} catch (SMTPAddressFailedException e) {
			logger.info("接受邮箱地址错误吧.");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据url生成静态页面
	 *
	 * @param url
	 *            动态文件路经 如：http://www.163.com/x.jsp
	 * @return d
	 */
	public static StringBuffer getHtmlTextByURL(String url) {
		// 从utl中读取html存为str
		StringBuffer sb = new StringBuffer();
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			InputStream is = uc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (br.ready()) {
				sb.append(br.readLine() + "/n");
			}
			is.close();
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			return sb;
		}
	}

}

class myAuthenticator extends Authenticator {
	String userName;
	String userPass;

	public myAuthenticator() {
	}

	public myAuthenticator(String userName, String userPass) {
		this.userName = userName;
		this.userPass = userPass;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, userPass);
	}
}
