package com.momathink.weixin.model;

import org.apache.commons.lang3.StringUtils;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="wx_user")
public class User extends BaseModel<User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6572339697770585474L;
	public static final User dao = new User();

	public User findByOpenId(String openId, String wpnumberId) {
		if(StringUtils.isEmpty(openId))
			return null;
		User user = dao.findFirst("select * from wx_user where openid=? and wpnumberid=?",openId,wpnumberId);
		return user;
	}

	public User findByBandUserId(Integer studentId,String openId) {
		if(studentId==null)
			return null;
		User user = dao.findFirst("select * from wx_user where openid=? and banduserid=?",openId,studentId);
		return user;
	}
	
}
