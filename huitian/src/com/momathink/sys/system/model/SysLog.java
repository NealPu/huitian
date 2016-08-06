package com.momathink.sys.system.model;


import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/**
 * 日志model
 * 2015年7月13日
 * @author prq
 *
 */
@Table(tableName="pt_syslog")
public class SysLog extends BaseModel<SysLog> {
	private static final long serialVersionUID = 2051998642258015518L;

	public static final SysLog dao = new SysLog();
	
}
