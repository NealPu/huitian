package com.momathink.finance.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/**
 * 2015年10月26日
 * @author prq
 * 出入库
 */

@SuppressWarnings("serial")
@Table(tableName="cw_itemsoutindetail")
public class ItemsOutIn extends BaseModel<ItemsOutIn> {
	
	public static final ItemsOutIn dao = new ItemsOutIn();

	/**
	 * 出库编辑
	 * @param ids
	 * @return
	 */
	public ItemsOutIn getItemsOutInDetailOut(String ids) {
		String sql = "SELECT  detail.id,ABS(detail.handlenum) handlenum,detail.itemid,detail.fnum,detail.ftotalcost,detail.ftaxprice,detail.remark,item.itemsname,detail.id outinid "
				+ "from cw_itemsoutindetail detail LEFT JOIN cw_items item on item.id = detail.itemid "
				+ "where detail.id = ?";
		return dao.findFirst(sql,ids);
	}
	
	/**
	 * 入库编辑
	 * @param ids
	 * @return
	 */
	public ItemsOutIn getItemsOutInDetailIn(String id) {
		String sql = "SELECT  detail.id,ABS(detail.handlenum) handlenum,detail.itemid,detail.remark,item.itemsname,detail.id outinid "
				+ "from cw_itemsoutindetail detail LEFT JOIN cw_items item on item.id = detail.itemid "
				+ "where detail.id = ?";
		return dao.findFirst(sql,id);
	}
	
	

}
