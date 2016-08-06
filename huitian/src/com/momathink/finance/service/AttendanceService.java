package com.momathink.finance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
/**
 * 考勤管理
 * @author Administrator
 *
 */
public class AttendanceService extends BaseService {
	/**
	 * 学生考勤分页信息《已经排课的学生》
	 * @param splitPage
	 */
	public void attendanceStudents(SplitPage splitPage) {
		List<Object> paramValue = new ArrayList<Object>();
		StringBuffer select =new StringBuffer("SELECT SUM(IF(tg.singn=1,1,0)) normal,SUM(IF(tg.singn=2,1,0)) late,SUM(IF(tg.singn=3,1,0)) leaveing,SUM(IF(tg.singn=4,1,0)) truancy,s.real_name,s.id,s.tel ");
		StringBuffer formSqlSb= new StringBuffer(" FROM "
				+ " account s "
				+ " LEFT JOIN teachergrade tg ON tg.studentid = s.Id "
				+ " LEFT JOIN courseplan cp ON tg.courseplan_id = cp.id "
				+ "  WHERE cp.STATE = 0 AND s.STATE = 0 ");
		Map<String,String> queryParam = splitPage.getQueryParam();
		if (null == queryParam) {
			return;
		}
		String studentname = queryParam.get("studentname");
		String begintime = queryParam.get("begindate");
		String endtime = queryParam.get("endtime");
		if (!StringUtils.isEmpty(studentname)) {
			formSqlSb.append(" AND s.real_name like ? ");
			paramValue.add("%"+studentname+"%");
		}
		if (!StringUtils.isEmpty(begintime)) {
			formSqlSb.append(" AND DATE_FORMAT(cp.create_time,'%Y-%m-%d') >= ? ");
			paramValue.add(begintime);
		}
		if (!StringUtils.isEmpty(endtime)) {
			formSqlSb.append(" AND DATE_FORMAT(cp.create_time,'%Y-%m-%d') <= ");
			paramValue.add(endtime);
		}
		formSqlSb.append(" group by s.id desc ");
		
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSqlSb.toString(), paramValue.toArray());
		splitPage.setPage(page);
	}

}
