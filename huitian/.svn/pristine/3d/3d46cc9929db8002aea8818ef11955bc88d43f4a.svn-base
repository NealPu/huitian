package com.momathink.sys.operator.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/**菜单图标路径
 * **/
@Table(tableName = "pt_icon")
public class Icon extends BaseModel<Icon>{

	private static final long serialVersionUID = -7123450286284981885L;
	public static final Icon dao = new Icon();
	
	/**
	 * 找到该用户上传图片  未使用的图片*
	 * @param sysuserId
	 * @return
	 */
	public List<Icon> queryBySysuserid(Integer sysuserId) {
		String sql="SELECT * FROM pt_icon WHERE state = 0 AND sysuserid = ?";
		return dao.find(sql, sysuserId);
	}
	
}
