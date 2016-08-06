package com.momathink.teaching.course.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "smartplanrecord")
public class SmartPlan extends BaseModel<SmartPlan> {

	private static final long serialVersionUID = 1L;
	
	public static final SmartPlan dao = new SmartPlan();

}
