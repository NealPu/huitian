package com.momathink.weixin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolXml;
import com.momathink.weixin.tools.ToolMessage;

public class MessageService extends BaseService {

	private static Logger log = Logger.getLogger(MessageService.class);

	/**
	 * 消息处理
	 * @param wpnumberId 公众账号标识
	 * @param recverMsg 接收的消息xml
	 * @param appSecret 
	 * @param appId 
	 * @return
	 */
	@Before(Tx.class)
	public String messageProcess(String cxt,String wpnumberId, String recverMsg, String appId, String appSecret) {
		// 消息类型
		String msgType = ToolXml.getStairText(recverMsg, "msgType");
		
		// 1.事件推送
		if (msgType.equals(ToolMessage.recevie_event)) {
			log.info("事件推送类型消息处理");
			String event = ToolXml.getStairText(recverMsg, "Event");
			return event(event, recverMsg,appId,appSecret,wpnumberId,cxt);
			
		} else {// 2.普通消息
			log.info("普通消息处理");
			return message(msgType, recverMsg,wpnumberId);
		}
	}
	
	/**
	 * 事件类型的消息
	 * @param event
	 * @param recverMsg
	 * @param appSecret 
	 * @param appId 
	 * @param accountId 
	 * @return
	 */
	public String event(String event, String recverMsg, String appId, String appSecret, String wpnumberId,String cxt){
		String responseMsg = null;
		
		if(event.equals(ToolMessage.recevie_event_subscribe)){//订阅
			String eventKey = ToolXml.getStairText(recverMsg, "Event");
			if(null == eventKey){//订阅
				responseMsg = ToolMessage.recevie_event_subscribe(recverMsg,appId,appSecret,wpnumberId);
				
			}else{// 扫描二维码事件1：如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
				responseMsg = ToolMessage.recevie_event_subscribe_scan(recverMsg,appId,appSecret,wpnumberId);
				
			}
			
		}else if(event.equals(ToolMessage.recevie_event_unsubscribe)){//取消订阅
			responseMsg = ToolMessage.recevie_event_unsubscribe(recverMsg,wpnumberId);
			
		}else if(event.equals(ToolMessage.recevie_event_scan)){//扫描二维码事件2：如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者。
			responseMsg = ToolMessage.recevie_event_scan(recverMsg);
			
		}else if(event.equals(ToolMessage.recevie_event_location)){//上报地理位置事件
			responseMsg = ToolMessage.recevie_event_location(recverMsg);
			
		}else if(event.equals(ToolMessage.recevie_event_click)){//点击菜单拉取消息时的事件推送
			responseMsg = ToolMessage.recevie_event_click(recverMsg,wpnumberId,cxt);
			
		}else if(event.equals(ToolMessage.recevie_event_view)){//点击菜单跳转链接时的事件推送
			responseMsg = ToolMessage.recevie_event_view(recverMsg);
		}
		
		return responseMsg;
	}
	
	/**
	 * 普通消息
	 * @param msgType
	 * @param recverMsg
	 * @return
	 */
	public String message(String msgType, String recverMsg,String wpnumberId){
		String responseMsg = null;
		
		if (msgType.equals(ToolMessage.recevie_msg_text)) {// 文本消息
			responseMsg = ToolMessage.recevie_msg_text(recverMsg,wpnumberId);

		} else if (msgType.equals(ToolMessage.recevie_msg_image)) {// 图片消息
			responseMsg = ToolMessage.recevie_msg_image(recverMsg);

		} else if (msgType.equals(ToolMessage.recevie_msg_voice)) {// 语音消息
			responseMsg = ToolMessage.recevie_msg_voice(recverMsg);
			
		} else if (msgType.equals(ToolMessage.recevie_msg_video)) {// 视频消息
			responseMsg = ToolMessage.recevie_msg_video(recverMsg);
			
		} else if (msgType.equals(ToolMessage.recevie_msg_location)) {// 地理位置消息
			responseMsg = ToolMessage.recevie_msg_location(recverMsg);
			
		} else if (msgType.equals(ToolMessage.recevie_msg_link)) {// 链接消息
			responseMsg = ToolMessage.recevie_msg_link(recverMsg);
		}
		
		return responseMsg;
	}

	public void list(SplitPage splitPage) {
		String select = " select we.menuname,wu.nickname,wn.accountname,wm.wpnumberid,wm.createdate,wm.Event,wm.EventKey,wm.FromUserName";
		splitPageBase(splitPage, select);
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from wx_message wm left join wx_wpnumber wn ON wm.ToUserName=wn.accountnumber" + 
				" left join wx_user wu on wm.FromUserName=wu.openId" + " left join wx_menu we on wm.EventKey=we.key" + " where wm.inouts=0");

		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "openid":
				formSqlSb.append(" and wm.FromUserName = ? ");
				paramValue.add(value);
				break;

			default:
				break;
			}
		}
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束日期
		String wpnumberid = queryParam.get("wpnumberid");
		String eventkey = queryParam.get("eventkey");
		
		if (!StringUtils.isEmpty(wpnumberid)) {
			formSqlSb.append(" AND wu.wpnumberid = ? ");
			paramValue.add(Integer.parseInt(wpnumberid));
		}
		if (!StringUtils.isEmpty(eventkey)) {
			formSqlSb.append(" AND we.key = ? ");
			paramValue.add(eventkey);
		}
		if (null != startDate && !startDate.equals("")) {
			formSqlSb.append(" AND wm.createdate >= ? ");
			paramValue.add(startDate + " 00:00:00");
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" AND wm.createdate <= ? ");
			paramValue.add(endDate + " 23:59:59");
		}
	}
}
