package com.momathink.finance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.finance.model.Payment;

public class PaymentService extends BaseService {
	
	public static final Payment dao = new Payment();

	public void list(SplitPage splitPage) {
		String select = "select cp.id,s.REAL_NAME studentname,cp.createtime,cp.amount,cp.ispay,cp.paydate,cp.paytype,u.REAL_NAME operatename,co.ordernum,co.delflag,co.delreason ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from crm_payment cp \n" 
				+ "left join crm_courseorder co on cp.orderid=co.id\n" 
				+ "left join account u on cp.operatorid=u.id "
				+ "left join account s on cp.studentid=s.id "
				+ "where co.delflag=0\n");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "studentId":
				formSqlSb.append(" and cp.studentid = ? ");
				paramValue.add(Integer.parseInt(value));
				break;
			case "studentname":
				formSqlSb.append(" and s.real_name like ? ");
				paramValue.add("%"+value+"%");
				break;
			case "startDate":// 添加日期
				formSqlSb.append(" and cp.createtime>= ? ");
				paramValue.add( value + " 00:00:00");
				break;
			case "endDate":// 添加日期
				formSqlSb.append(" and cp.createtime<= ? ");
				paramValue.add( value + " 59:59:59");
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" order by cp.createtime desc ");
	}
	
	@Before(Tx.class)
	public void save(Payment payment) {
		try {
			payment.set("createtime", ToolDateTime.getDate());
			payment.save();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("添加数据异常");
		}
	}
	
	@Before(Tx.class)
	public void update(Payment payment) {
		try {
			payment.set("updatetime", new Date());
			payment.update();
		} catch (Exception e) {
			throw new RuntimeException("更新数据异常");
		}
	}

	public double getPaidAmount(Integer courseOrderId) {
		BigDecimal sum = Db.queryBigDecimal("SELECT SUM(amount) FROM crm_payment WHERE orderid=?",courseOrderId);
		return sum==null?0:sum.doubleValue();
	}

	public Map<String, String> queryPrice(String studentId, String classType) {
		Map<String, String> result = new HashMap<String, String>();
		int teachType = 1;
		long price = 0;
		String ispay = "0";
		if (!"0".equals(classType)) {
			teachType = 2;
		}
		List<Payment> plist = Payment.dao.findbyStuIdAndTeachType(studentId, teachType);
		for (Payment payment : plist) {
			long remain = payment.getLong("remain") == null ? 0 : payment.getLong("remain");
			if (remain == 0) {
				continue;
			} else {
				price = payment.getLong("price") == null ? 0 : payment.getLong("price");
				ispay = payment.getStr("ispay");
				payment.set("remain", remain - 1);
				payment.update();
				result.put("price", price + "");
				result.put("ispay", ispay);
				result.put("paymentid", payment.getPrimaryKeyValue().toString());
				break;
			}
		}
		return result;
	}
}
