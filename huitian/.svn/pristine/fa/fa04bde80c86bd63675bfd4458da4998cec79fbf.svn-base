package com.momathink.sys.system.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.teaching.campus.model.Campus;

@Table(tableName = "account_campus")
public class AccountCampus extends BaseModel<AccountCampus> {
	private static final long serialVersionUID = 4762813779629969917L;
	public static final AccountCampus dao = new AccountCampus();

	public List<AccountCampus> getCampusidsByAccountid(Integer paraToInt) {

		return dao.find("select * from account_campus where account_id  = ? ", paraToInt);
	}

	public AccountCampus getAccountCampusMessage(String id, String string) {
		return dao.findFirst("select * from account_campus where account_id = ? and campus_id = ? ", id, string);
	}

	public List<AccountCampus> getAllCampusidByAccountid(String id) {
		return dao.find("select * from account_campus where account_id = ? ", id);
	}

	/**
	 * 根据用户id 获取所在所有校区的ids
	 * 
	 * @param id
	 * @return
	 */
	public String getCampusIdsByAccountId(Integer id) {
		if (StrKit.isBlank(id + "")) {
			return null;
		} else {
			String sql = "SELECT GROUP_CONCAT(DISTINCT account_campus.campus_id) campusids FROM account_campus LEFT JOIN account on account.Id=account_campus.account_id "
					+ " WHERE  account_campus.account_id = " + id;
			return dao.findFirst(sql).getStr("campusids");// 使用时候验证isNull
		}
	}

	/**
	 * 某几个校区的老师
	 * 
	 * @param campusids
	 *            eg. (1,2)
	 * @return
	 */
	public String getTeacherIdsfromCampusids(String campusids) {
		if (StrKit.isBlank(campusids)) {
			return null;
		} else {
			String sql = "SELECT GROUP_CONCAT(DISTINCT account_campus.account_id) tchids FROM account_campus LEFT JOIN account on account.Id=account_campus.account_id "
					+ " WHERE 1 = 1 AND account_campus.campus_id in ( " + campusids + " )";
			return dao.findFirst(sql).getStr("tchids");// 使用时候验证isNull
		}
	}

	public List<Campus> getCampusByAccountId(Integer sysuserId) {
		return Campus.dao.find("select * from campus c left join account_campus ac on c.id= ac.campus_id where ac.account_id = ? ", sysuserId);
	}
}
