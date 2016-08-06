package com.momathink.teaching.classtype.service;

import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.account.model.BanciCourse;
import com.momathink.teaching.classtype.model.ClassType;

public class ClassTypeService extends BaseService {
	
	public static final ClassType dao = new ClassType();
	
	/**
	 * 班型管理分页
	 * @param splitPage
	 */
	@SuppressWarnings("unchecked")
	public void list(SplitPage splitPage) {
		String sql = "SELECT distinct ct.status,ct.id,ct.`name` typeName , ct.subjectids subjectid ";
		splitPageBase(splitPage, sql);
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> olist = page.getList();
		for (Record r : olist) {
			Integer typeid = r.getInt("id");
			String coursename = "";
			List<BanciCourse> bclist = BanciCourse.dao.find("select c.COURSE_NAME courseName from banci_course bc left join course c on c.id=bc.course_id where banci_id =0 and type_id = ? ", typeid);
			for(BanciCourse bc : bclist){
				coursename +="、"+bc.getStr("courseName");
			}
			coursename = coursename.replaceFirst("、", "");
			r.set("coursename", coursename);
			String subname = "";
			List<BanciCourse> sublist = BanciCourse.dao.find("select distinct sub.SUBJECT_NAME subName from banci_course bc left join subject sub on sub.Id=bc.subject_id where bc.banci_id=0 and type_id = ? ", typeid)  ;
			for(BanciCourse bs : sublist){
				subname += "、"+bs.getStr("subName");
			}
			subname = subname.replaceFirst("、", "");
			r.set("subjectname", subname);
		}
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from class_type ct where 1=1 ");
		if (null == queryParam) {
			return;
		}
		String subid = queryParam.get("subjectid");
		String typeName = queryParam.get("typeName");
		if(!StringUtils.isEmpty(subid)){
			formSqlSb.append(" and ct.subjectids like ? ");
			paramValue.add("%\\|"+subid+"%");
		}
		if(!StringUtils.isEmpty(typeName)){
			formSqlSb.append(" and ct.`name` like ? ");
			paramValue.add("%"+typeName+"%");
		}
		formSqlSb.append(" order by ct.Id desc ");
	}
	public Record getClassOrderbyid(String orderid) {
			return dao.getClassOrderbyid(orderid);
	}
	
	public static void update(ClassType classType) {
		try {
			classType.update();
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常");
		}
	}
	
	

}
