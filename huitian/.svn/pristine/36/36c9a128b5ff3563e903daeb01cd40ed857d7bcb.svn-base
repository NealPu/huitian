package com.momathink.weixin.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.weixin.model.User;
import com.momathink.weixin.tools.ToolUser;

public class UserService extends BaseService {

	private static Logger log = Logger.getLogger(UserService.class);

	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage) {
		log.debug("微信用户管理：分页处理");
		String select = "SELECT cm.id cid,wu.id,wu.openId,wu.nickname,wu.city,wu.country,wu.province,IF (wu.sex = 1,'男',IF (wu.sex = 2, '女', '未知')) xingbie,from_unixtime(wu.subscribeTime) guanzhuTime,IF(ISNULL(cm.realname),0,1) isbound,IF(wu.subscribe=1,'已关注','未关注') guanzhu,cm.realname,a.real_name studentname, wu.wpnumberid,n.accountname";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		if (null == queryParam) {
			return;
		}
		formSqlSb.append(" from wx_user wu left join crm_mediator cm ON wu.openId=cm.openid " + "left join account a on wu.banduserid=a.id "
				+ "left join wx_wpnumber n on wu.wpnumberid=n.id " + "WHERE 1=1 and wu.openId is not NULL");
		String nickname = queryParam.get("nickname");// 昵称
		String band = queryParam.get("band");// 绑定状态
		String guanzhu = queryParam.get("guanzhu");// 关注
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束日期
		String wpnumberid = queryParam.get("wpnumberid");

		if (null != band && !band.equals("")) {
			if ("1".equals(band))
				formSqlSb.append(" AND realname is not null ");
			else
				formSqlSb.append(" AND realname is null ");

		}
		if (null != guanzhu && !guanzhu.equals("")) {
			if ("1".equals(guanzhu)) {
				formSqlSb.append(" AND subscribe = ? ");
				paramValue.add(guanzhu);
			} else {
				formSqlSb.append(" AND subscribe = ? ");
				paramValue.add(guanzhu);
			}
		}
		if (null != nickname && !nickname.equals("")) {
			formSqlSb.append(" AND nickname like ? ");
			paramValue.add("%" + nickname + "%");
		}
		if (!StringUtils.isEmpty(wpnumberid)) {
			formSqlSb.append(" AND wu.wpnumberid = ? ");
			paramValue.add(Integer.parseInt(wpnumberid));
		}
		if (null != startDate && !startDate.equals("")) {
			formSqlSb.append(" AND from_unixtime(wu.subscribeTime) >= ? ");
			paramValue.add(startDate + " 00:00:00");
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" AND from_unixtime(wu.subscribeTime) <= ? ");
			paramValue.add(endDate + " 23:59:59");
		}
		formSqlSb.append(" order by id desc");
	}

	/**
	 * 跟进OpenId查询微信用户
	 * 
	 * @param openId
	 * @param appId
	 * @param appSecret
	 * @param wpnumberId
	 * @return
	 */
	public User queryByOpenId(String openId, String appId, String appSecret, String wpnumberId) {
		User user = User.dao.findByOpenId(openId, wpnumberId);
		if (user == null) {
			ToolUser.saveUser(openId, appId, appSecret, wpnumberId);
		}
		user = User.dao.findByOpenId(openId, wpnumberId);
		return user;
	}

}
