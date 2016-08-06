package com.momathink.weixin.tools;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolUtils;
import com.momathink.common.tools.ToolXml;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.teaching.student.model.Student;
import com.momathink.weixin.model.Location;
import com.momathink.weixin.model.Message;
import com.momathink.weixin.model.User;
import com.momathink.weixin.model.Wpnumber;
import com.momathink.weixin.service.XsdMessageService;
import com.momathink.weixin.vo.map.RecevieUserLocation;
import com.momathink.weixin.vo.message.RecevieEventLocation;
import com.momathink.weixin.vo.message.RecevieEventMenu;
import com.momathink.weixin.vo.message.RecevieEventQRCode;
import com.momathink.weixin.vo.message.RecevieEventSubscribe;
import com.momathink.weixin.vo.message.RecevieMsgImage;
import com.momathink.weixin.vo.message.RecevieMsgLink;
import com.momathink.weixin.vo.message.RecevieMsgLocation;
import com.momathink.weixin.vo.message.RecevieMsgText;
import com.momathink.weixin.vo.message.RecevieMsgVideo;
import com.momathink.weixin.vo.message.RecevieMsgVoice;
import com.momathink.weixin.vo.message.ResponseMsgText;

/**
 * 接收消息处理
 * @author 董华健
 */
public class ToolMessage {

	/**
	 * 事件类型
	 */
	public static final String recevie_event = "event";
	public static final String recevie_event_subscribe = "subscribe";
	public static final String recevie_event_unsubscribe = "unsubscribe";
	public static final String recevie_event_scan = "SCAN";
	public static final String recevie_event_location = "LOCATION";
	public static final String recevie_event_click = "CLICK";
	public static final String recevie_event_view = "VIEW";
	
	/**
	 * 消息类型
	 */
	public static final String recevie_msg_text = "text";
	public static final String recevie_msg_image = "image";
	public static final String recevie_msg_voice = "voice";
	public static final String recevie_msg_video = "video";
	public static final String recevie_msg_location = "location";
	public static final String recevie_msg_link = "link";
	
	/**
	 * wx_message表字段值
	 */
	public static final String message_inouts_in = "0";//接受的消息
	public static final String message_inouts_out = "1";//发出的消息
	public static final String message_datatype_xml = "0";//数据类型XML
	public static final String message_datatype_json = "1";//数据类型JSON
	
	/**
	 * 订阅
	 * @param recverMsg
	 * @param appSecret 
	 * @param appId 
	 * @param wpnumberId 
	 * @return
	 */
	public static String recevie_event_subscribe(String recverMsg, String appId, String appSecret, String wpnumberId){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventSubscribe.class);
		RecevieEventSubscribe subscribe = (RecevieEventSubscribe) ToolXml.xmlToBean(recverMsg, map);
		
		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", subscribe.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", subscribe.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",subscribe.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", subscribe.getMsgType());// 消息类型，event
		messageIn.set("Event", subscribe.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageIn.save();
		//保存关注用户
		String openId = subscribe.getFromUserName();
		ToolUser.saveUser(openId,appId,appSecret,wpnumberId);
		//关注提示语
		String contentBuffer = null;
		String crmwxh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh);
		String xswxh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_xsgzh);
		Wpnumber wpnumber = Wpnumber.dao.findById(Integer.parseInt(wpnumberId));
		if(crmwxh.equals(wpnumber.getStr("accountnumber"))){
			contentBuffer = getUsage();
		}else if(xswxh.equals(wpnumber.getStr("accountnumber"))){// 学生绑定微信
			contentBuffer = getXsdUsage();
		}else{
			contentBuffer = "去火星了吗？";
		}
		
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(subscribe.getFromUserName());
		text.setFromUserName(subscribe.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(contentBuffer);
		String responseXml = ToolXml.beanToXml(text);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageOut.save();
		
		return responseXml;
	}
	
	/**
	 * 订阅：扫描二维码事件1：如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
	 * @param recverMsg
	 * @param appSecret 
	 * @param appId 
	 * @param wpnumberId 
	 * @return
	 */
	public static String recevie_event_subscribe_scan(String recverMsg, String appId, String appSecret, String wpnumberId){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventQRCode.class);
		RecevieEventQRCode qrCode = (RecevieEventQRCode) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", qrCode.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", qrCode.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",qrCode.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", qrCode.getMsgType());// 消息类型，event
		messageIn.set("Event", qrCode.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("EventKey", qrCode.getEventKey());//事件KEY值，qrscene_为前缀，后面为二维码的参数值
		messageIn.set("Ticket", qrCode.getTicket());//二维码的ticket，可用来换取二维码图片
		messageIn.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageIn.save();
		String openId = qrCode.getFromUserName();
		ToolUser.saveUser(openId,appId,appSecret,wpnumberId);
		//关注提示语
		String contentBuffer = null;
		String crmwxh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh);
		String xswxh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_xsgzh);
		Wpnumber wpnumber = Wpnumber.dao.findById(Integer.parseInt(wpnumberId));
		if(crmwxh.equals(wpnumber.getStr("accountnumber"))){
			contentBuffer = getUsage();
		}else if(xswxh.equals(wpnumber.getStr("accountnumber"))){// 学生绑定微信
			contentBuffer = getXsdUsage();
		}else{
			contentBuffer = "去火星了吗？";
		}
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(qrCode.getFromUserName());
		text.setFromUserName(qrCode.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(contentBuffer);
		String responseXml = ToolXml.beanToXml(text);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageOut.save();
		
		return responseXml;
	}
	
	/**
	 * 取消订阅
	 * @param recverMsg
	 * @return
	 */
	public static String recevie_event_unsubscribe(String recverMsg,String wpnumberId){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventSubscribe.class);
		RecevieEventSubscribe subscribe = (RecevieEventSubscribe) ToolXml.xmlToBean(recverMsg, map);
		
		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", subscribe.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", subscribe.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",subscribe.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", subscribe.getMsgType());// 消息类型，event
		messageIn.set("Event", subscribe.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageIn.save();
		String openId = subscribe.getFromUserName();
		ToolUser.unsubscribe(openId,wpnumberId);
		//关注提示语
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("谢谢！欢迎下次光临！:）").append("\n\n");
		
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(subscribe.getFromUserName());
		text.setFromUserName(subscribe.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(text);

		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 扫描二维码事件2：如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者。
	 * @param qrCode
	 * @return
	 */
	public static String recevie_event_scan(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventQRCode.class);
		RecevieEventQRCode qrCode = (RecevieEventQRCode) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", qrCode.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", qrCode.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",qrCode.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", qrCode.getMsgType());// 消息类型，event
		messageIn.set("Event", qrCode.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("EventKey", qrCode.getEventKey());//事件KEY值，qrscene_为前缀，后面为二维码的参数值
		messageIn.set("Ticket", qrCode.getTicket());//二维码的ticket，可用来换取二维码图片
		messageIn.save();
		
		//关注提示语
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("您已经关注了哦！").append("\n\n");
		contentBuffer.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，回复“附近”开始体验吧！");
		
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(qrCode.getFromUserName());
		text.setFromUserName(qrCode.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(text);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}
	
	/**
	 * 上报地理位置事件
	 * @param location
	 * @return
	 */
	public static String recevie_event_location(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventLocation.class);
		RecevieEventLocation location = (RecevieEventLocation) ToolXml.xmlToBean(recverMsg, map);
		
		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", location.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", location.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",location.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", location.getMsgType());// 消息类型，event
		messageIn.set("Event", location.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("Latitude", location.getLatitude());//地理位置纬度
		messageIn.set("Longitude", location.getLongitude());//地理位置经度
		messageIn.set("Precisions", location.getPrecision());//地理位置精度
		messageIn.save();
		
		//关注提示语
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("您的位置已经收录！").append("\n\n");
		contentBuffer.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，回复“附近”开始体验吧！");
		
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(location.getFromUserName());
		text.setFromUserName(location.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(text);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 点击菜单拉取消息时的事件推送
	 * @param menu
	 * @return
	 */
	public static String recevie_event_click(String recverMsg,String wpnumberId,String cxt){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventMenu.class);
		RecevieEventMenu menu = (RecevieEventMenu) ToolXml.xmlToBean(recverMsg, map);
		
		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", menu.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", menu.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",menu.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", menu.getMsgType());// 消息类型，event
		messageIn.set("Event", menu.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("EventKey", menu.getEventKey());//事件KEY值，与自定义菜单接口中KEY值对应
		messageIn.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageIn.save();
		Wpnumber wpnumber = Wpnumber.dao.findById(Integer.parseInt(wpnumberId));
		String xsgzh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_xsgzh);
		String content = null;
		User wxuser = User.dao.findByOpenId(menu.getFromUserName(), wpnumberId);
		if(wxuser == null){
			ToolUser.saveUser(menu.getFromUserName(),wpnumber.getStr("appid"),wpnumber.getStr("appsecret"),wpnumberId);
		}else{
			if(xsgzh.equals(wpnumber.getStr("accountnumber"))){//学生端公众号
				if(wxuser.getInt("banduserid") == null){
					content = "您还未绑定，请发送:#手机号#格式进行绑定！";
				}else{
					content = XsdMessageService.getMessageByEventKey(cxt, menu.getFromUserName(),wxuser.getInt("banduserid"),menu.getEventKey());
				}
			}
		}
		
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(menu.getFromUserName());
		text.setFromUserName(menu.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(content);
		String responseXml = ToolXml.beanToXml(text);
		responseXml = responseXml.replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.set("wpnumberid", Integer.parseInt(wpnumberId));
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 点击菜单跳转链接时的事件推送
	 * @param menu
	 * @return
	 */
	public static String recevie_event_view(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieEventMenu.class);
		RecevieEventMenu menu = (RecevieEventMenu) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", menu.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", menu.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",menu.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", menu.getMsgType());// 消息类型，event
		messageIn.set("Event", menu.getEvent());// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		messageIn.set("EventKey", menu.getEventKey());//事件KEY值，与自定义菜单接口中KEY值对应
		messageIn.save();
		
		//关注提示语
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("您的位置已经收录！").append("\n\n");
		contentBuffer.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，回复“附近”开始体验吧！");
		
		//返回xml
		ResponseMsgText text = new ResponseMsgText();
		text.setToUserName(menu.getFromUserName());
		text.setFromUserName(menu.getToUserName());
		text.setCreateTime(ToolDateTime.getDateByTime());
		text.setMsgType("text");
		text.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(text);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", text.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", text.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",text.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", text.getMsgType());// 消息类型
		messageOut.set("content", text.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 文本消息
	 * @param text
	 * @return
	 */
	public static String recevie_msg_text(String recverMsg,String wpnumberId){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieMsgText.class);
		RecevieMsgText recevieText = (RecevieMsgText) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", recevieText.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", recevieText.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",recevieText.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", recevieText.getMsgType());// 消息类型，event
		messageIn.set("content", recevieText.getContent());// 文本消息内容
		messageIn.set("wpnumberId", Integer.parseInt(wpnumberId));// 所属公众号
		messageIn.save();
		
		String responseXml = "";
		String responseContent = "感谢您的反馈，我们会尽快处理您的意见。";
		String content = recevieText.getContent();
		String crmwxh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh);
		String xswxh = (String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_xsgzh);
		Wpnumber wpnumber = Wpnumber.dao.findById(Integer.parseInt(wpnumberId));
		if(content.startsWith("#")&&content.endsWith("#")&&content.length()==13){//微信端通过手机绑定帐号
			String phoneNumber = content.substring(1, 12);
			User user = User.dao.findByOpenId(recevieText.getFromUserName(),wpnumberId);
			if(crmwxh.equals(wpnumber.getStr("accountnumber"))){
				Mediator media = Mediator.dao.findByOpenId(recevieText.getFromUserName());//openid有绑定
				if(media==null){
					if(ToolUtils.isMobile(phoneNumber.trim())){
						Mediator mediator = Mediator.dao.findByPhoneNumber(phoneNumber.trim());
						if(mediator==null){
							responseContent = "系统中没有手机号对应的伙伴.";
						}else{
							String openided = mediator.getStr("openid");
							if(StringUtils.isEmpty(openided)){
								mediator.set("openid", user.getStr("openId"));
								boolean flag = mediator.update();
								if(flag){
									responseContent = "绑定成功.";
								}else{
									responseContent = "绑定失败.";
								}
							}else{
								responseContent = "该手机号已被绑定.";
							}
						}
					}else{
						responseContent = "手机号不正确，请重试.";
					}
				}else{
					responseContent = "已经绑定；不需重复绑定";
				}
			}else if(xswxh.equals(wpnumber.getStr("accountnumber"))){// 学生绑定微信
				if(user.getInt("banduserid")==null){//未绑定
					if(ToolUtils.isMobile(phoneNumber.trim())){
						Student student = Student.dao.findByPhone(phoneNumber);
						if(student == null){
							student = Student.dao.findByParentsPhone(phoneNumber);
						}
						if(student==null){
							responseContent = "系统中没有手机号对应的伙伴.";
						}else{
							user.set("banduserid", student.getPrimaryKeyValue());
							boolean flag = user.update();
							if(flag){
								responseContent = "绑定成功.";
							}else{
								responseContent = "绑定失败.";
							}
						
						}
					}else{
						responseContent = "手机号不正确，请重试.";
					}
				}else{
					Student student = Student.dao.findByPhone(phoneNumber);
					if(student == null){
						student = Student.dao.findByParentsPhone(phoneNumber);
					}
					if(student==null){
						responseContent = "系统中没有手机号对应的伙伴.";
					}else{
						user.set("banduserid", student.getPrimaryKeyValue());
						boolean flag = user.update();
						if(flag){
							responseContent = "绑定更新成功.";
						}else{
							responseContent = "绑定失败.";
						}
					}
				}
			}
			ResponseMsgText responseText = new ResponseMsgText();
			responseText.setToUserName(recevieText.getFromUserName());
			responseText.setFromUserName(recevieText.getToUserName());
			responseText.setCreateTime(ToolDateTime.getDateByTime());
			responseText.setMsgType("text");
			responseText.setContent(responseContent);
			responseXml = ToolXml.beanToXml(responseText);
		}
			
		if(null == responseXml || responseXml.isEmpty()){
			//返回xml
			ResponseMsgText responseText = new ResponseMsgText();
			responseText.setToUserName(recevieText.getFromUserName());
			responseText.setFromUserName(recevieText.getToUserName());
			responseText.setCreateTime(ToolDateTime.getDateByTime());
			responseText.setMsgType("text");
			responseText.setContent(responseContent);
			responseXml = ToolXml.beanToXml(responseText);
			
			//返回数据入库
			Message messageOut = new Message();
			messageOut.set("inouts", message_inouts_out);
			messageOut.set("datatype", message_datatype_xml);
			messageOut.set("datacontent", responseXml);//返回数据
			messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
			messageOut.set("ToUserName", responseText.getToUserName());	// 开发者微信号
			messageOut.set("FromUserName", responseText.getFromUserName());// 发送方帐号（一个OpenID）
			messageOut.set("CreateTime",responseText.getCreateTime());// 消息创建时间 （整型）
			messageOut.set("MsgType", responseText.getMsgType());// 消息类型
			messageOut.set("content", responseText.getContent());// 消息内容
			messageOut.set("wpnumberId", Integer.parseInt(wpnumberId));// 所属公众号
			messageOut.save();
		}
		
		return responseXml;
	}

	/**
	 * 图片消息
	 * @param image
	 * @return
	 */
	public static String recevie_msg_image(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieMsgImage.class);
		RecevieMsgImage image = (RecevieMsgImage) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", image.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", image.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",image.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", image.getMsgType());// 消息类型，event
		messageIn.set("PicUrl", image.getPicUrl());//图片链接
		messageIn.set("MediaId", image.getMediaId());//图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
		messageIn.save();

		//关注提示语
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("图片已经收到！").append("\n\n");
		contentBuffer.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，回复“附近”开始体验吧！");
		
		//返回xml
		ResponseMsgText responseText = new ResponseMsgText();
		responseText.setToUserName(image.getFromUserName());
		responseText.setFromUserName(image.getToUserName());
		responseText.setCreateTime(ToolDateTime.getDateByTime());
		responseText.setMsgType("text");
		responseText.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(responseText);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", responseText.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", responseText.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",responseText.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", responseText.getMsgType());// 消息类型
		messageOut.set("content", responseText.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 语音消息
	 * @param voice
	 * @return
	 */
	public static String recevie_msg_voice(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieMsgVoice.class);
		RecevieMsgVoice voice = (RecevieMsgVoice) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", voice.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", voice.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",voice.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", voice.getMsgType());// 消息类型，event
		
		messageIn.set("Format", voice.getFormat());//语音格式，如amr，speex等
		messageIn.set("MediaId", voice.getMediaId());//语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
		//开通语音识别功能，用户每次发送语音给公众号时，微信会在推送的语音消息XML数据包中，增加一个Recongnition字段
		messageIn.set("Recognition", voice.getRecognition());//语音识别结果，UTF8编码
		messageIn.save();

		// 返回xml
		StringBuilder contentBuffer = new StringBuilder();
		String recognition = voice.getRecognition();// 语音识别结果
		if(null != recognition){//接收语音识别结果
			
		}else{
			
		}
		
		//返回xml
		ResponseMsgText responseText = new ResponseMsgText();
		responseText.setToUserName(voice.getFromUserName());
		responseText.setFromUserName(voice.getToUserName());
		responseText.setCreateTime(ToolDateTime.getDateByTime());
		responseText.setMsgType("text");
		responseText.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(responseText);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", responseText.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", responseText.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",responseText.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", responseText.getMsgType());// 消息类型
		messageOut.set("content", responseText.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 视频消息
	 * @param video
	 * @return
	 */
	public static String recevie_msg_video(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieMsgVideo.class);
		RecevieMsgVideo video = (RecevieMsgVideo) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", video.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", video.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",video.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", video.getMsgType());// 消息类型，event
		
		messageIn.set("MediaId", video.getMediaId());//视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
		messageIn.set("ThumbMediaId", video.getThumbMediaId());//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
		messageIn.save();

		//关注提示语
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("图片已经收到！").append("\n\n");
		contentBuffer.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，回复“附近”开始体验吧！");
		
		//返回xml
		ResponseMsgText responseText = new ResponseMsgText();
		responseText.setToUserName(video.getFromUserName());
		responseText.setFromUserName(video.getToUserName());
		responseText.setCreateTime(ToolDateTime.getDateByTime());
		responseText.setMsgType("text");
		responseText.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(responseText);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", responseText.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", responseText.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",responseText.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", responseText.getMsgType());// 消息类型
		messageOut.set("content", responseText.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 地理位置消息
	 * @param location
	 * @return
	 */
	public static String recevie_msg_location(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieMsgLocation.class);
		RecevieMsgLocation location = (RecevieMsgLocation) ToolXml.xmlToBean(recverMsg, map);
		String lng = location.getLocation_Y();// 用户发送的经纬度
		String lat = location.getLocation_X();// 用户发送的经纬度
		
		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", location.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", location.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",location.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", location.getMsgType());// 消息类型，event
		
		messageIn.set("Location_X", location.getLocation_X());//地理位置维度
		messageIn.set("Location_Y", location.getLocation_Y());//地理位置经度
		messageIn.set("Scale", location.getScale());//地图缩放大小
		messageIn.set("Label", location.getLabel());//地理位置信息 
		messageIn.save();
		
		// 坐标转换后的经纬度
		String bd09Lng = null;
		String bd09Lat = null;
		
		// 调用接口转换坐标
		RecevieUserLocation userLocation = ToolBaiduMap.convertCoord(lng, lat);
		if (null != userLocation) {
			bd09Lng = userLocation.getBd09Lng();
			bd09Lat = userLocation.getBd09Lat();
		}
		
		// 保存用户地理位置
		Location uLocation = new Location();
		uLocation.set("open_id", location.getFromUserName());
		uLocation.set("lng", lng);
		uLocation.set("lat", lat);
		uLocation.set("bd09_lng", bd09Lng);
		uLocation.set("bd09_lat", bd09Lat);
		long starttime = ToolDateTime.getDateByTime();
		uLocation.set("createdate", ToolDateTime.getSqlTimestamp(starttime));//创建时间
		uLocation.save();
		
		//回显信息
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("[愉快]").append("成功接收您的位置！").append("\n\n");
		contentBuffer.append("您可以输入搜索关键词获取周边信息了，例如：").append("\n");
		contentBuffer.append("        附近ATM").append("\n");
		contentBuffer.append("        附近KTV").append("\n");
		contentBuffer.append("        附近厕所").append("\n");
		contentBuffer.append("必须以“附近”两个字开头！");
		
		//返回xml
		ResponseMsgText responseText = new ResponseMsgText();
		responseText.setToUserName(location.getFromUserName());
		responseText.setFromUserName(location.getToUserName());
		responseText.setCreateTime(ToolDateTime.getDateByTime());
		responseText.setMsgType("text");
		responseText.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(responseText);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", responseText.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", responseText.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",responseText.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", responseText.getMsgType());// 消息类型
		messageOut.set("content", responseText.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}

	/**
	 * 链接消息
	 * @param link
	 * @return
	 */
	public static String recevie_msg_link(String recverMsg){
		//请求数据封装
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("xml", RecevieMsgLink.class);
		RecevieMsgLink link = (RecevieMsgLink) ToolXml.xmlToBean(recverMsg, map);

		//请求数据入库
		Message messageIn = new Message();
		messageIn.set("inouts", message_inouts_in);
		messageIn.set("datatype", message_datatype_xml);
		messageIn.set("datacontent", recverMsg);//请求数据
		messageIn.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		
		messageIn.set("ToUserName", link.getToUserName());	// 开发者微信号
		messageIn.set("FromUserName", link.getFromUserName());// 发送方帐号（一个OpenID）
		messageIn.set("CreateTime",link.getCreateTime());// 消息创建时间 （整型）
		messageIn.set("MsgType", link.getMsgType());// 消息类型，event
		
		messageIn.set("Title", link.getTitle());//消息标题
		messageIn.set("Description", link.getDescription());//消息描述
		messageIn.set("Url", link.getUrl());//消息链接	 
		messageIn.save();

		//回显信息
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("您点击的啥链接呢？");
		
		//返回xml
		ResponseMsgText responseText = new ResponseMsgText();
		responseText.setToUserName(link.getFromUserName());
		responseText.setFromUserName(link.getToUserName());
		responseText.setCreateTime(ToolDateTime.getDateByTime());
		responseText.setMsgType("text");
		responseText.setContent(contentBuffer.toString());
		String responseXml = ToolXml.beanToXml(responseText);
		
		//返回数据入库
		Message messageOut = new Message();
		messageOut.set("inouts", message_inouts_out);
		messageOut.set("datatype", message_datatype_xml);
		messageOut.set("datacontent", responseXml);//返回数据
		messageOut.set("createdate", ToolDateTime.getSqlTimestamp(null));//数据创建时间
		messageOut.set("ToUserName", responseText.getToUserName());	// 开发者微信号
		messageOut.set("FromUserName", responseText.getFromUserName());// 发送方帐号（一个OpenID）
		messageOut.set("CreateTime",responseText.getCreateTime());// 消息创建时间 （整型）
		messageOut.set("MsgType", responseText.getMsgType());// 消息类型
		messageOut.set("content", responseText.getContent());// 消息内容
		messageOut.save();
		
		return responseXml;
	}
	
	/**
	 * 使用说明
	 * @return
	 */
	private static String getUsage() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Hi伙伴，感谢关注我们！").append("\n");
		buffer.append("使用说明：").append("\n");
		buffer.append("1）如何注册：点击“账户”—“我的信息”—输入“邀请码”注册。").append("\n");
		buffer.append("2）如何推广：点击“推广赚”—“我要推广”—系统生成专属邀请码，发送好友关注我们的公众号并输入您的邀请码。").append("\n\n");
		buffer.append("赚佣金：").append("\n");
		buffer.append("1）推荐赚：推荐学生佣金比例25%，随时查看跟进、到访和成单续费情况。").append("\n");
		buffer.append("2）推广赚：推广此微信号给好友赚佣金，推广佣金支持3级，分别3%、1%、0.5%。");
		return buffer.toString();
	}
	/**
	 * 使用说明
	 * @return
	 */
	private static String getXsdUsage() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Hi，感谢您关注我们！").append("\n");
		buffer.append("使用说明：").append("\n");
		buffer.append("1）如何绑定：\n发送消息#手机号#绑定你的帐号\n如没有开通帐号请联系您的课程顾问老师。").append("\n");
		buffer.append("2）点击“查课表”、“查作业”您可以查询相应的课程信息和作业信息。").append("\n");
		return buffer.toString();
	}
}
