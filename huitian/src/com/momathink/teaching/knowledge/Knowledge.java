package com.momathink.teaching.knowledge;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "knowledge")
public class Knowledge extends BaseModel<Knowledge> {
	private static final long serialVersionUID = -7408938877866830090L;
	public static final Knowledge knowledge = new Knowledge();
}
