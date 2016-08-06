package com.momathink.finance.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="crm_orderreject")
public class OrdersReject extends BaseModel<OrdersReject> {

	private static final long serialVersionUID = 1L;
	public static final OrdersReject dao = new OrdersReject();
	public List<OrdersReject> getOrdersRejectsByOrderId(String orderid) {
		String sql = "select * from crm_orderreject where orderid= ? ";
		List<OrdersReject> list = dao.find(sql, orderid);
		return list;
	}
	

}
