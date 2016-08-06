package com.momathink.sys.account.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.account.model.AccountBook;

public class AccountBookService extends BaseService {
	
	public static final AccountBook dao = new AccountBook();
	
	public void list(SplitPage splitPage) {
		String select = " select ab.*, a.REAL_NAME as stuName, "
				+ " operate.REAL_NAME as operateName,cco.teachtype teachtype,"
				+ " classo.classNum xiaobanName, c.COURSE_NAME courseName,cp.orderid cpid ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from account_book ab ");
		formSqlSb.append(" left join account a on ab.accountid=a.id "
				+ " left join crm_payment cp on ab.paymentid = cp.id "
				+ " left join account operate on operate.id=ab.createuserid "
				+ " left join course c on c.Id = ab.courseid "
				+ " left join crm_courseorder cco on cco.id = ab.orderid "
				+ " left join class_order classo on classo.id= ab.classorderid  where 1=1 ");
		if (null == queryParam) {
			return;
		}

		String accountid = queryParam.get("accountid");
		if (!StringUtils.isEmpty(accountid)) {
			formSqlSb.append(" and ab.accountid = ? ");
			paramValue.add(Integer.parseInt(accountid));
		}
		formSqlSb.append(" order by id desc");
	}

	@Before(Tx.class)
	public void save(AccountBook accountBook) {
		try {
			// 保存顾问
			accountBook.set("createtime", new Date());
			accountBook.save();
		} catch (Exception e) {
			throw new RuntimeException("保存用户异常");
		}
	}
}
