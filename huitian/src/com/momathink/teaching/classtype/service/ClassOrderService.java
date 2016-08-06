package com.momathink.teaching.classtype.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.account.model.AccountBanci;
import com.momathink.teaching.course.model.CoursePlan;

public class ClassOrderService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public void list(SplitPage splitPage) {
		String sql = "SELECT DISTINCT  class_order.id,	class_order.classNum className, class_order.classtype_id,kc.real_name kcgwname,"
				+ " class_order.stuNum, class_order.teachTime, class_order.is_assesment,class_order.endTime, class_type.`name` ,class_order.chargeType, "
				+ "	class_type.lesson_count, class_order.lessonNum, class_order.totalfee, class_type.id AS type_id,class_order.accountid";
		splitPageBase(splitPage, sql);
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> list = page.getList();
		for (Record r : list) {
			StringBuffer sf = new StringBuffer();
			Map<String ,String> map = new HashMap<String,String>();
			List<CoursePlan> cplist = CoursePlan.coursePlan.getAllCourseByOrderId(r.getInt("id").toString());
			if(cplist.size()>0){
				for(CoursePlan cp:cplist){
					map.put(cp.getStr("REAL_NAME"), cp.getStr("REAL_NAME"));
				}
			}
			if(!map.isEmpty()){
				Set<String> keySet = map.keySet(); 
				for(String key:keySet){
					sf.append(map.get(key)).append("<br>");
				}
			}
			r.set("teachername",sf.toString());
			r.set("studentCount", AccountBanci.dao.getStudentCountByClassOrderId(r.getInt("id"),r.getInt("accountid")));
			r.set("coursePlanCount", CoursePlan.coursePlan.getClassYpkcClasshour(r.getInt("id")));
		}
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from class_order  LEFT JOIN class_type ON class_order.classtype_id = class_type.id  "
				+ "	left join account kc on class_order.pcid = kc.id WHERE  1=1  ");
		if (null == queryParam) {
			return;
		}
		
		String className = queryParam.get("className");
		String classTypeId = queryParam.get("classTypeId");
		String jieke = queryParam.get("jieke");
		String teachtimefirst = queryParam.get("teachtimefirst");
		String teachtimeend = queryParam.get("teachtimeend");
		String pcid = queryParam.get("pcid");
		boolean flag = true;
		if(!StringUtils.isEmpty(className)){
			flag= false;
			formSqlSb.append(" and class_order.classNum like ? ");
			paramValue.add("%"+className+"%");
		}
		if(!StringUtils.isEmpty(classTypeId)){
			flag= false;
			formSqlSb.append(" and class_order.classtype_id = ? ");
			paramValue.add(classTypeId);
		}
		if(!StringUtils.isEmpty(pcid)){
			flag= false;
			formSqlSb.append(" and class_order.pcid = ? ");
			paramValue.add(pcid);
		}
		if(!StringUtils.isEmpty(jieke)){
			flag= false;
			if(jieke.equals("1")){
				formSqlSb.append(" and DATE_FORMAT(IFNULL(class_order.endTime,'2099-1-1'),'%Y-%m-%d') < (select current_date)");
			}else{
				formSqlSb.append(" and DATE_FORMAT(IFNULL(class_order.endTime,'2099-1-1'),'%Y-%m-%d') >= (select current_date)");
			}
			
		}
		if(!StringUtils.isEmpty(teachtimefirst)){
			flag= false;
			formSqlSb.append("  and class_order.teachTime >=  ? ");
			paramValue.add(teachtimefirst);
		}
		if(!StringUtils.isEmpty(teachtimeend)){
			flag= false;
			formSqlSb.append(" and class_order.teachTime <=  ?  ");
			paramValue.add(teachtimeend);
		}
		if(flag){
			formSqlSb.append(" and DATE_FORMAT(IFNULL(class_order.endTime,'2099-1-1'),'%Y-%m-%d') >= (select current_date)");
		}
		formSqlSb.append("  order by class_order.id desc ");
		
	}

}
