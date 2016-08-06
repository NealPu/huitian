package com.momathink.sys.sms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.sms.model.SmsMessage;

public class SmsMessageService extends BaseService{
	/**
	 * 下发短信的记录分页
	 */
	public void smsMessagelist(SplitPage splitPage) {
		 List<Object> paramValue = new ArrayList<Object>();
			StringBuffer select = new StringBuffer(" select * " );
			StringBuffer formSql = new StringBuffer("from crm_smsmessage where 1=1 ");
			
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "recipient_tel":
					formSql.append(" and recipient_tel like '%").append(value).append("%'");
					break;
				case "send_time":
					formSql.append(" and DATE_FORMAT(send_time,'%Y-%m-%d') = ").append(value);
					break;
				case "send_state":
				formSql.append(" and send_state = ").append(value);
				break;
				default:
					break;
				}
			}
			formSql.append(" order by send_time desc ");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			splitPage.setPage(page);
		}
	/**
	 * 保存发送短信的消息记录
	 */
	public void saveSendMessage(String tel,Integer type,String time,Integer state, String msg ){
		SmsMessage s = new SmsMessage();
		s.set("recipient_tel", tel).set("recipient_type", type).set("send_time", time).set("send_state",state).set("send_msg",msg).save();
	}
}
