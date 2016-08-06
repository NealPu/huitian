package com.momathink.finance.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.finance.model.Items;
import com.momathink.finance.model.ItemsOutIn;
import com.momathink.sys.account.model.Account;
import com.momathink.weixin.model.User;

/**
 * 2015年10月26日
 * @author prq
 *
 */

public class ItemsService extends BaseService {
	
	private static Logger log = Logger.getLogger(ItemsService.class);
	public static final ItemsService service = new ItemsService();

	/**
	 * 物品管理   分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage) {
		log.debug("教材物品管理：分页处理");
		String select = " select st.real_name teachername,detail.state,fi.filename filename,fi.fileuri fileuri,item.id,item.itemsname,item.itemstype,item.place,item.price,item.taxrates,item.kuanshi,item.num,item.unit,IFNULL(sum(detail.handlenum),0) sumnum,"
				+ "item.sysuser,item.remark,DATE_FORMAT(IFNULL(item.updatedate,item.createdate),'%Y-%m-%d') updatedate,user.real_name  ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from cw_items item left join cw_itemsoutindetail detail on detail.itemid = item.id ");
		formSqlSb.append(" left join account user on user.id = item.sysuser ");
		formSqlSb.append(" LEFT JOIN account st ON st.id=item.staffids ");
		formSqlSb.append(" left join jx_file fi on item.id = fi.autid WHERE 1=1");
		if (null == queryParam) {
			return;
		}

		String itemsname = queryParam.get("itemsname");
		String startDate = queryParam.get("startDate");
		String endDate = queryParam.get("endDate");
		
		if (null != itemsname && !itemsname.equals("")) {
			formSqlSb.append(" and item.itemsname like ?");
			paramValue.add("%" + itemsname + "%");
		}
		if (null != startDate && !startDate.equals("")) {
			formSqlSb.append(" and item.createdate >= ? ");
			paramValue.add(startDate + " 00:00:00");
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" and item.createdate <= ? ");
			paramValue.add(endDate + " 23:59:59");
		}
		formSqlSb.append(" group by item.id");
	}
	
	/**
	 * 保存
	 * @param items
	 * @return
	 */
	public boolean save(Items items,String userids) {
		try{
			items.set("createdate", ToolDateTime.getDate());
			items.set("version", 0);
			items.set("sysuser", userids);
			items.save();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新物品
	 * @param items
	 * @param string
	 * @return
	 */
	public boolean update(Items items, String userids) {
		try{
			items.set("updatedate", ToolDateTime.getDate());
			items.set("version", items.getLong("version")+1);
			items.set("sysuser", userids);
			items.update();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false; 
		}
	}

//	/**
//	 * 
//	 * @param name
//	 * @param ids
//	 * @return
//	 */
//	public int valiName(String name, String ids) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("column", "ids");
//		param.put("table", "cw_items");
//		param.put("condition", "itemsname");
//		String sql = getSql("jiaoxue.baseJiaoxue.selectColumn", param);
//		List<Items> list = Items.dao.find(sql,name);
//		int size = list.size();
//		if(size == 0){
//			return 0;
//		}
//		if(size == 1){
//			Items item = list.get(0);
//			if(item != null &&  item.getStr("ids").equals(ids)){
//				return 0;
//			}
//		}
//		return 1;
//	}

	/**
	 * 保存入库
	 * @param para
	 * @param outIn
	 * @param cUserIds
	 * @return
	 */
	public boolean itemsIn(Items items, ItemsOutIn outIn, String cUserIds) {
		boolean flag = false;
		try{
			Account user = Account.dao.findById(cUserIds);
			outIn.set("handletime", ToolDateTime.getDate());
			outIn.set("state", 0);
			outIn.set("sysuserids", user.getInt("id"));
			flag = outIn.save();
		/*	if(flag){
				try{
					Items item = Items.dao.findById(items.getPKValue());
					item.set("num", item.getLong("num")+outIn.getLong("handlenum"));
					item.set("version", item.getLong("version")+1);
					flag = item.update();
				}catch(Exception ex){
					outIn.delete();
				}
				if(!flag)
					outIn.delete();
			}*/
			
			return flag;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

//	/**
//	 * 开班类型获取开班编号options
//	 * @param number
//	 * @return
//	 */
//	public StringBuffer applyClassesOptions(String number,String classids) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("<option value=''>请选择</option>");
//		String sql = "";
//		if("typeEI".equals(number)){
//			sql = getSql("jiaoxue.kaiban.getEIApplied");
//		}
//		if("typeIT".equals(number)){
//			sql = getSql("jiaoxue.kaiban.getITApplied");
//		}
//		List<ApplyClass> list = ApplyClass.dao.find(sql);
//		if(list!=null&&list.size()>0){
//			for(ApplyClass apc : list){
//				sb.append("<option value='").append(apc.getStr("ids")).append("'");
//				if(StrKit.notBlank(classids)&&apc.getStr("ids").equals(classids)){
//					sb.append("selected='selected'");
//				}
//				sb.append( " >");
//				sb.append(apc.getStr("classnum")).append("(").append(apc.getStr("schoolname")).append(")");
//				sb.append("</option>");
//			}
//		}
//		return sb;
//	}

	/**
	 * 记录出库
	 * @param item
	 * @param out
	 * @param cUserIds
	 * @return
	 */
	public boolean saveItemsOut(Items items, ItemsOutIn out, String cUserIds ) {
		boolean flag = false;
		try{
			User user = User.dao.findById(cUserIds);
			
			out.set("handletime", ToolDateTime.getDate());
			out.set("state", 0);
			out.set("sysuserids", user.getInt("id"));
			if(null!=out.getLong("fnum")){
				out.set("handlenum", -out.getLong("fnum"));
			}
			flag = out.save();
			return flag;
		}catch(Exception ex){
			ex.printStackTrace();
			return false; 
		}
		
	}

	/**
	 * 出入库明细 分页
	 * @param splitPage
	 */
	public void outInList(SplitPage splitPage) {
		String selectSql = "select detail.id,date_format(detail.handletime,'%Y-%m-%d') handletime,abs(detail.handlenum) handlenum,detail.remark,"
				+ "item.itemsname,user.real_name,detail.handletype,(case detail.handletype when 'instorage' then '入库' else '出库' end) handletypeval ";
		
		StringBuilder formSqlSb = new StringBuilder();
		List<Object> paramValue = new LinkedList<Object>();
		Map<String, String> queryParam = splitPage.getQueryParam();

		formSqlSb.append(" from cw_itemsoutindetail detail left join cw_items item on item.id= detail.itemid ");
//		formSqlSb.append(" left join jx_applyclass jac on jac.ids = detail.classids ");
		formSqlSb.append(" left join pt_dict storage on storage.numbers = detail.handletype ");
		formSqlSb.append(" left join account user on user.id = detail.sysuserids WHERE 1=1 ");
		if (null == queryParam) {
			return;
		}

		String itemsname = queryParam.get("itemsname");
		String startDate = queryParam.get("startDate");
		String endDate = queryParam.get("endDate");
		
		if (null != itemsname && !itemsname.equals("")) {
			formSqlSb.append(" and item.itemsname like ?");
			paramValue.add("%" + itemsname + "%");
		}
		if (null != startDate && !startDate.equals("")) {
			formSqlSb.append(" and detail.handletime >= ? ");
			paramValue.add(startDate + " 00:00:00");
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" and detail.handletime <= ? ");
			paramValue.add(endDate + " 23:59:59");
		}

		// 行级：过滤
		rowFilter(formSqlSb);

		// 排序
		String orderColunm = splitPage.getOrderColunm();
		String orderMode = splitPage.getOrderMode();
		if (null != orderColunm && !orderColunm.isEmpty() && null != orderMode && !orderMode.isEmpty()) {
			formSqlSb.append(" order by ").append(orderColunm).append(" ").append(orderMode);
		}

		String formSql = formSqlSb.toString();

		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), selectSql, formSql, paramValue.toArray());
		splitPage.setPage(page);
		
	}

	/**
	 * 更新
	 * @param detail
	 * @param cUserIds
	 * @return
	 */
	public boolean updateItemsOutIn(ItemsOutIn detail, String cUserIds) {
		try{
			detail.set("sysuserids", cUserIds);
			detail.set("handletime", ToolDateTime.getDate());
			detail.set("state", 0);
			return detail.update();
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	

	/**
	 * 更新出库
	 * @param out
	 * @param bi
	 * @param cUserIds
	 * @return
	 */
	public boolean updateItemsOut(ItemsOutIn out,  String cUserIds) {
		try {
			if(null!=out.getLong("fnum")){
				out.set("handlenum", -out.getLong("fnum"));
			}
			out.set("sysuserids", cUserIds);
			out.set("handletime", ToolDateTime.getDate());
			return out.update();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除出入库操作
	 * @param ids
	 * @return
	 */
	public boolean deleteOutInItems(String ids) {
		try{
			ItemsOutIn outin = ItemsOutIn.dao.findById(ids);
			if(outin==null){
				return true;
			}else{
				return outin.delete();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

//	/**
//	 * 开班预算模板
//	 * @param classids
//	 * @param bbudgetids
//	 * @return
//	 */
//	public StringBuffer applyClassesBudgetOptions(String classids, String budgetids,String itemids) {
//		StringBuffer sb = new StringBuffer();
//		String sql = getSql("caiwu.budget.getApplyClassBudgets");
//		List<Budget> list = Budget.dao.find(sql,classids,itemids);
//		
//		if(list!=null&&list.size()>0){
//			for(Budget b  : list){
//				if("0".equals(b.getInt("budgettype").toString())){
//					sb.append("<option value='").append(b.getStr("ids")).append("'");
//					if(StrKit.notBlank(budgetids)&&b.getStr("ids").equals(budgetids)){
//						sb.append("selected='selected'");
//					}
//					sb.append( " >").append(b.getStr("budgetname")).append("</option>");
//				}
//			}
//		}
//		return sb;
//		
//	}

	

}
