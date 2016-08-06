package com.momathink.teaching.student.model;
import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "headpicture")
public class HeadPicture extends BaseModel<HeadPicture> {

	private static final long serialVersionUID = 8091569877737327778L;
	public static final HeadPicture dao = new HeadPicture();
	/**
	 * 
	 * @param sysuserId
	 * @return
	 */
	public List<HeadPicture> getScrapPicture(Integer sysuserId) {
		String sql = "select * from headpicture where state =0 and sysuserid = ?";
		return dao.find(sql,sysuserId);
	}
	
}
