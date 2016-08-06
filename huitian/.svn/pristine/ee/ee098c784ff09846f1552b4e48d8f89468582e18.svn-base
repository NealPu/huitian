package com.momathink.finance.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;

public class TeachercheckeService extends BaseService {
	
	public void list(SplitPage splitPage) {
		String select = "select p.teacher_id,c.real_name,"
				+ "SUM( IF ( p.iscancel = 0, t.class_hour, p.teacherhour )) zks,"
				+ "IFNULL(a.sign,0) zc,IFNULL(b.sign,0) cd,"
				+ "IFNULL(e.sign,0) wq,IFNULL(d.sign,0) bq,DATE_FORMAT(p.COURSE_TIME,'%Y-%m') yuefen,"
				+ "IF(s.stat_time,1,0) AS yijs";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = queryParam.get("date");
		String date1 = "";
		String date2 = "";
		try{
			if (null != date && !date.equals("")) {
				Date dateD1 = sf.parse(date+"-1 00:00:00");
				Date date11 = ToolDateTime.getFirstDateOfMonth(dateD1);
				Date date12 = ToolDateTime.getLastDateOfMonth(dateD1);
				date1 = ToolDateTime.format(date11, "yyyy-MM-dd")+" 00:00:00";
				date2 = ToolDateTime.format(date12, "yyyy-MM-dd")+" 23:59:59";
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.MONTH, -1);
				Date datem =calendar.getTime();
				Date date11 = ToolDateTime.getFirstDateOfMonth(datem);
				Date date12 = ToolDateTime.getLastDateOfMonth(datem);
				date1 = ToolDateTime.format(date11, "yyyy-MM-dd")+" 00:00:00";
				date2 = ToolDateTime.format(date12, "yyyy-MM-dd")+" 23:59:59";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		formSqlSb.append("FROM courseplan p "
				+ " LEFT JOIN salary s ON p.teacher_id = s.teacher_id "
				+ " AND DATE_FORMAT(p.COURSE_TIME, '%Y-%m') = s.stat_time "
				+ " LEFT JOIN time_rank t ON p.TIMERANK_ID = t.Id "
				+ " LEFT JOIN account c ON c.Id = p.TEACHER_ID "
				+ " LEFT JOIN ( SELECT p.TEACHER_ID,SUM( IF ( p.iscancel = 0, t.class_hour, p.teacherhour )) AS sign "
				+ "	FROM	courseplan p"
				+ "  LEFT JOIN time_rank t ON p.TIMERANK_ID = t.Id "
				+ " LEFT JOIN account c ON c.Id = p.TEACHER_ID"
				+ "  WHERE LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', c.roleids) ) > 0 AND p.PLAN_TYPE = 0	AND p.SIGNIN = 1 "
				+ " AND p.COURSE_TIME <= ? AND p.COURSE_TIME >= ? "
				+ " GROUP BY p.TEACHER_ID, p.SIGNIN ) a ON a.TEACHER_ID = p.TEACHER_ID "
				+ " LEFT JOIN ( SELECT p.TEACHER_ID,SUM( IF ( p.iscancel = 0, t.class_hour, p.teacherhour )) AS sign "
				+ " FROM	courseplan p "
				+ " LEFT JOIN time_rank t ON p.TIMERANK_ID = t.Id"
				+ "  LEFT JOIN account c ON c.Id = p.TEACHER_ID "
				+ " WHERE LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', c.roleids) ) > 0 AND p.PLAN_TYPE = 0	AND p.SIGNIN = 0 "
				+ " AND p.COURSE_TIME <= ? AND p.COURSE_TIME >= ? "
				+ " GROUP BY p.TEACHER_ID, p.SIGNIN ) e ON e.TEACHER_ID = p.TEACHER_ID"
				+ "  LEFT JOIN (SELECT	p.TEACHER_ID,	SUM( IF ( p.iscancel = 0, t.class_hour, p.teacherhour )) AS sign	"
				+ " FROM	courseplan p	"
				+ " LEFT JOIN time_rank t ON p.TIMERANK_ID = t.Id	"
				+ " LEFT JOIN account c ON c.Id = p.TEACHER_ID	"
				+ " WHERE LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', c.roleids) ) > 0 AND p.PLAN_TYPE = 0 AND p.SIGNIN = 2"
				+ "  AND p.COURSE_TIME <= ? AND p.COURSE_TIME >= ? "
				+ " GROUP BY p.TEACHER_ID, p.SIGNIN) b ON b.TEACHER_ID = p.TEACHER_ID "
				+ " LEFT JOIN (	SELECT p.TEACHER_ID, SUM( IF ( p.iscancel = 0, t.class_hour, p.teacherhour )) AS sign "
				+ " FROM courseplan p"
				+ "  LEFT JOIN time_rank t ON p.TIMERANK_ID = t.Id "
				+ " LEFT JOIN account c ON c.Id = p.TEACHER_ID"
				+ "  WHERE LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', c.roleids) ) > 0 AND p.PLAN_TYPE = 0	AND p.SIGNIN = 3 "
				+ " AND p.COURSE_TIME <= ? AND p.COURSE_TIME >= ? "
				+ " GROUP BY p.TEACHER_ID, p.SIGNIN) d ON d.TEACHER_ID = p.TEACHER_ID "
				+ " WHERE LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', c.roleids) ) > 0 AND p.PLAN_TYPE = 0 "
				+ " AND p.COURSE_TIME <= ? AND p.COURSE_TIME >= ? ");
		paramValue.add(date2);
		paramValue.add(date1);
		paramValue.add(date2);
		paramValue.add(date1);
		paramValue.add(date2);
		paramValue.add(date1);
		paramValue.add(date2);
		paramValue.add(date1);
		paramValue.add(date2);
		paramValue.add(date1);
		
		String teachername = queryParam.get("teachername");
		if (null != teachername && !teachername.equals("")) {
			formSqlSb.append(" AND c.real_name like ? ");
			paramValue.add("%" + teachername + "%");
		}
		
		formSqlSb.append(" GROUP BY	p.TEACHER_ID");
	}
	
}
