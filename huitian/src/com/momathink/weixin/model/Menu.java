package com.momathink.weixin.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "wx_menu")
public class Menu extends BaseModel<Menu> {

	private static final long serialVersionUID = 7513950057125407026L;

	public static final Menu dao = new Menu();

	/**
	 * 根据id查询是否有二级菜单
	 * 
	 * @param ids
	 * @return
	 */
	public List<Menu> getAllMenu(){
		return dao.find("select * from wx_menu wx where wx.key is not null");
	}
	
	public List<Menu> findChildMenuById(String id) {
		return StringUtils.isEmpty(id) ? null : find("select * from wx_menu where parentid = ?", Integer.parseInt(id));
	}

	public List<Menu> findSubMenu() {
		return dao.find("select * from wx_menu where menutype=1 order by sortorder");
	}

	public List<Menu> findFirstMenu(String wpnumberid) {
		return dao.find("select * from wx_menu where menutype=0 and wpnumberid=? order by sortorder",Integer.parseInt(wpnumberid));
	}

}
