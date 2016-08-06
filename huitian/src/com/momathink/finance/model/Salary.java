package com.momathink.finance.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName = "salary")
public class Salary extends BaseModel<Salary> {

	private static final long serialVersionUID = 3709647110842972787L;
	public static final Salary dao = new Salary();
	
	
}
