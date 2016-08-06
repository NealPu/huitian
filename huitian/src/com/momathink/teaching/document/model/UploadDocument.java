package com.momathink.teaching.document.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
/**
 *  2015年8月19日prq
 * @author prq
 *
 */

@Table(tableName="pt_upload")
public class UploadDocument extends BaseModel<UploadDocument> {

	private static final long serialVersionUID = 1L;
	public static final UploadDocument dao = new UploadDocument();
	

}
