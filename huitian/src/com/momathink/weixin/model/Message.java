package com.momathink.weixin.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "wx_message")
public class Message extends BaseModel<Message> {
	private static final long serialVersionUID = 8833098949476333948L;
	public static final Message dao = new Message();

}
