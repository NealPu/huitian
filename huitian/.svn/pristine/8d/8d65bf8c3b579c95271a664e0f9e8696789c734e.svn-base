package com.momathink.sys.account.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "account")
public class Account extends BaseModel<Account> {
	private static final long serialVersionUID = -3456750340176316673L;
	public static final Account dao = new Account();

	public Account findByName(String accountName) {
		Account account = this.findFirst("SELECT * FROM account WHERE account.REAL_NAME=?", accountName);
		return account;
	}

	public Account getAccountById(String id) {
		String sql = "select * from account where account.ID = ? ";
		Account account = dao.findFirst(sql, id);
		return account;
	}

	/**
	 * 查询所有已经有的班次供选择
	 * 
	 * @return
	 */
	public static List<Account> getUsedClassId(Integer subject_id) {
		String sql = "SELECT\n" + "class_order.classNum,\n" + "class_order.teachTime,\n" + "class_type.`name`\n" + "FROM\n" + "account_banci\n"
				+ "LEFT JOIN class_order ON account_banci.banci_id = class_order.id\n"
				+ "LEFT JOIN banci_course ON class_order.classtype_id = banci_course.type_id\n"
				+ "LEFT JOIN class_type ON class_order.classtype_id = class_type.id\n" + "WHERE\n" + "banci_course.subject_id = ?\n" + "GROUP BY\n"
				+ "account_banci.banci_id ";
		List<Account> list = dao.find(sql, subject_id);
		return list;
	}

	public String getStuByName(String name) {
		// TODO Auto-generated method stub
		String sql = "select CLASS_TYPE from account where REAL_NAME = ? and COURSE_SUM > 0 ";
		Account acc = dao.findFirst(sql);
		String strCourse = acc.getStr("CLASS_TYPE");
		return strCourse;
	}

	/**
	 * 获取所有的教师*
	 * @return
	 */
	public List<Account> getTeachers() {
		String sql = "select * from account where LOCATE((SELECT CONCAT(',', id, ',') ids FROM 	pt_role WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0 and state=0 and class_type!= '' ";
		List<Account> list = dao.find(sql);
		return list;
	}


	public Long studentThMonth(String date) {
		Long student = Db.queryLong("select count(*) from account where CREATE_TIME >='"+date +"'");
		return student;
	}
}
