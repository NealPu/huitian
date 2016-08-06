package com.momathink.sys.sms.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;


@Table(tableName = "crm_smsmessage")
public class SmsMessage extends BaseModel<SmsMessage>{
	private static final long serialVersionUID = 9173917692482770076L;
	public static final SmsMessage dao = new SmsMessage();
	/**
	 * 该方法只使用于发送生日祝福
	 * 判断是否给学生发送过信息 并且是发送失败 的信息
	 * @param tel
	 * @return
	 */
	public SmsMessage getMessageByTel(String tel){
		return dao.findFirst("select * from crm_smsmessage where send_state = 0 and  recipient_tel =?"
				+ " and DATE_FORMAT(send_time,'%Y') = DATE_FORMAT((select current_date),'%Y') ",tel);
	}
}
