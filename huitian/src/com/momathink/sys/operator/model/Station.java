package com.momathink.sys.operator.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="pt_station")
public class Station extends BaseModel<Station> {

	private static final long serialVersionUID = 1L;
	
	public static final Station dao = new Station();

}
