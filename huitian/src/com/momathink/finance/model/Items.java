package com.momathink.finance.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/**
 * 2015年10月26日
 * @author prq
 * 物品管理
 */

@SuppressWarnings("serial")
@Table(tableName="cw_items")
public class Items extends BaseModel<Items> {

	public static final Items dao = new Items();

	/**
	 * 根据ids获取基本信息
	 * @param ids
	 * @return
	 */
	public Items getItemsById(String id) {
		String sql = "select item.id itemid,item.itemsname,item.num,sum(detail.handlenum) sumnum "
				+"from cw_items item "
				+"left join cw_itemsoutindetail detail on detail.itemid = item.id "
				+"where item.id = ? ";
		return Items.dao.findFirst(sql, id);
	}
	
	/**
	 * 物品名称
	 * @return
	 */
//	public List<Items> getItemsnameList() {
//		String sql = getSql("caiwu.items.itemsnamelist");
//		return dao.find(sql);
//	}
	
	/**、
	 * 获取物品图片
	 */
//	public List<MyFile> getAllWuPinPhoto(String autids){
//		return MyFile.dao.find(getSql("caiwu.items.itemPhotolist"),autids);
//	}
}
