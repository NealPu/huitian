package com.momathink.finance.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.finance.model.CoursePrice;

public class CoursePriceService extends BaseService {
	
	public static final CoursePrice dao = new CoursePrice();

	public void list(SplitPage splitPage) {
		String select = "SELECT c.*,s.SUBJECT_NAME ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM course c LEFT JOIN `subject` s ON c.SUBJECT_ID=s.Id WHERE c.SUBJECT_ID != 0 ");
		if (null == queryParam) {
			return;
		}

		String coursename = queryParam.get("coursename");
		String subjectid = queryParam.get("subjectid");
		if (null != coursename && !coursename.equals("")) {
			formSqlSb.append(" AND c.course_name like ? ");
			paramValue.add("%" + coursename + "%");
		}
		if (null != subjectid && !subjectid.equals("")) {
			formSqlSb.append(" AND c.subject_id =? ");
			paramValue.add(Integer.parseInt(subjectid));
		}
		formSqlSb.append(" ORDER BY c.id,c.subject_id");
	}
	
	@Before(Tx.class)
	public void save(CoursePrice coursePrice) {
		try {
			coursePrice.set("createtime", ToolDateTime.getDate());
			coursePrice.save();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("添加数据异常");
		}
	}
	
	@Before(Tx.class)
	public void update(CoursePrice coursePrice) {
		try {
			coursePrice.set("updatetime", new Date());
			coursePrice.update();
		} catch (Exception e) {
			throw new RuntimeException("更新数据异常");
		}
	}
	
	public boolean delete(Integer id) {
		CoursePrice.dao.deleteById(id);
		return true;
	}
}
