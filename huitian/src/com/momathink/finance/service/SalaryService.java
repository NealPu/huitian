package com.momathink.finance.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.finance.model.Salary;

public class SalaryService extends BaseService {
	
	public static final Salary dao = new Salary();
	public void list(SplitPage splitPage) {
		String select = "SELECT b.REAL_NAME AS creatname,a.REAL_NAME,s.*";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM salary s LEFT JOIN account a ON a.Id = s.teacher_id LEFT JOIN account b ON b.Id = s.creat_id WHERE 1=1");
		String teachername = queryParam.get("teachername");
		if (null != teachername && !teachername.equals("")) {
			formSqlSb.append(" AND a.real_name like ? ");
			paramValue.add("%" + teachername + "%");
		}
		String date = queryParam.get("date");
		String date1 = "";
		String date2 = "";
		if (null != date && !date.equals("")) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			date1 = date+"-01 00:00:00";
			try {
				date2= sdf.format(ToolDateTime.getLastDateOfMonth(sdf.parse(date+"-01")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			formSqlSb.append(" AND s.stat_time <= ? AND s.stat_time >= ?");
			paramValue.add(date2+"23:59:59");
			paramValue.add(date1);
		}
	}
	
	/*
	 *按教师查询课程
	 */
	public List<Record> getsalarylist(String tid,String yuefen) throws ParseException{
		Date dateD1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(yuefen+"-1 00:00:00");
		Date date11 = ToolDateTime.getFirstDateOfMonth(dateD1);
		Date date12 = ToolDateTime.getLastDateOfMonth(dateD1);
		String date1 = ToolDateTime.format(date11, "yyyy-MM-dd")+" 00:00:00";
		String date2 = ToolDateTime.format(date12, "yyyy-MM-dd")+" 23:59:59";
		List<Record> list = Db.find("SELECT DATE_FORMAT(p.COURSE_TIME, '%Y-%m-%d') yuefen,"
				+ "	t.RANK_NAME,IF ( p.class_id = 0, '一对一', '小班') AS sklx,s.SUBJECT_NAME,"
				+ " a.REAL_NAME,(case when (p.SIGNIN= '0') then '未签到' when (p.SIGNIN= '1') then '正常' when (p.SIGNIN= '2') "
				+ " then '迟到' when (p.SIGNIN= '3') then '补签' else  0 end) as kaoqin,t.class_hour ,p.iscancel ,p.teacherhour "
				+ "	FROM courseplan p LEFT JOIN time_rank t ON p.TIMERANK_ID = t.Id "
				+ " LEFT JOIN `subject` s ON p.SUBJECT_ID = s.Id "
				+ " LEFT JOIN account a ON p.STUDENT_ID = a.Id "
				+ " WHERE p.teacher_id = ? AND p.COURSE_TIME <= ? AND p.COURSE_TIME >= ? order by p.COURSE_TIME ",tid,date2,date1 );
		return list;
	}
	
}
