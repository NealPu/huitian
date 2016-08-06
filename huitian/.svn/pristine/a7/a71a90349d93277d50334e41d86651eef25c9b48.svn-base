package com.momathink.sys.sms.model;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.momathink.common.plugin.MessagePropertiesPlugin;
import com.momathink.crm.mediator.model.Organization;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class SimpleMailSender {

	private static final Logger logger = Logger.getLogger(SimpleMailSender.class);

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {

		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToEmail());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToEmail());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void send(String msg, String email) {
		try {
			// 这个类主要是设置邮件
			Organization	org = Organization.dao.findById(1);
			String emailStatus = org.getStr("email_state");
			if ("on".equals(emailStatus)) {
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost(org.getStr("email_serverhost")==null?MessagePropertiesPlugin.getEmailParamMapValue("server_host"):org.getStr("email_serverhost"));
				mailInfo.setMailServerPort(org.getStr("email_serverport")==null?MessagePropertiesPlugin.getEmailParamMapValue("server_port"):org.getStr("email_serverport"));
				mailInfo.setValidate(true);
				mailInfo.setUserName(org.getStr("email_senderemail")==null?MessagePropertiesPlugin.getEmailParamMapValue("sender_email"):org.getStr("email_senderemail"));
				mailInfo.setPassword(org.getStr("email_senderpassword")==null?MessagePropertiesPlugin.getEmailParamMapValue("sender_password"):org.getStr("email_senderpassword"));// 您的邮箱密码
				mailInfo.setFromAddress(org.getStr("email_fromaddress")==null? MessagePropertiesPlugin.getEmailParamMapValue("from_address"):org.getStr("email_fromaddress"));
				mailInfo.setToEmail(email);
				mailInfo.setSubject(org.getStr("email_title")==null?MessagePropertiesPlugin.getEmailParamMapValue("email_title"):org.getStr("email_title")); // 设置邮箱标题
				mailInfo.setContent(msg + org.getStr("name")); // 设置邮箱内容
				// 这个类主要来发送邮件
				SimpleMailSender.sendTextMail(mailInfo);// 发送文体格式
				logger.info("接受邮箱：" + email + "发送内容：" + msg);
			}else{
				logger.info("邮件发送已关闭：接受邮箱：" + email + "发送内容：" + msg);
			}
		} catch (Exception e) {
			logger.error(e);
			logger.info("邮件发送失败");
		}
	}
	public static void main(String[] args) {
		SimpleMailSender.send("邮箱接口调试", "david@luode.org");
	}
}
