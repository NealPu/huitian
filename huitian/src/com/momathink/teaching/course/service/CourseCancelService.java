package com.momathink.teaching.course.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;

public class CourseCancelService extends BaseService {
	private static Logger log = Logger.getLogger(CourseCancelService.class);
	
	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		log.debug("已删除课程");
		String select = "SELECT\n" +
				"courseplan_back.Id,\n" +
				"DATE_FORMAT(courseplan_back.COURSE_TIME,'%Y-%m-%d') AS COURSE_TIME,\n" +
				"DATE_FORMAT(courseplan_back.UPDATE_TIME,'%Y-%m-%d %H:%i:%s') AS UPDATE_TIME,\n" +
				"courseplan_back.REMARK,\n" +
				"courseplan_back.PLAN_TYPE,\n" +
				"courseplan_back.SIGNIN,\n" +
				"del.REAL_NAME AS deluserid,\n" +
				"teacher.REAL_NAME AS teacher_name,\n" +
				"student.REAL_NAME AS student_name,\n" +
				"course.COURSE_NAME,\n" +
				"classroom.`NAME`,\n" +
				"time_rank.RANK_NAME,\n" +
				"IFNULL(campus.CAMPUS_NAME,'') AS CAMPUS_NAME,\n" +
				"IFNULL(class_order.classNum,'无') AS classNum,\n" +
				"courseplan_back.del_msg ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("FROM\n" +
				"courseplan_back\n" +
				"LEFT JOIN account AS student ON courseplan_back.STUDENT_ID = student.Id\n" +
				"LEFT JOIN account AS teacher ON courseplan_back.TEACHER_ID = teacher.Id\n" +
				"LEFT JOIN account AS del ON courseplan_back.deluserid = del.Id\n" +
				"LEFT JOIN course ON courseplan_back.COURSE_ID = course.Id\n" +
				"LEFT JOIN classroom ON courseplan_back.ROOM_ID = classroom.Id\n" +
				"LEFT JOIN time_rank ON courseplan_back.TIMERANK_ID = time_rank.Id\n" +
				"LEFT JOIN campus ON courseplan_back.CAMPUS_ID = campus.Id\n" +
				"LEFT JOIN class_order ON courseplan_back.class_id = class_order.id WHERE 1=1");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			if(!StringUtils.isEmpty(value)){
				switch (paramKey) {
				case "studentname":
					formSqlSb.append(" and student.REAL_NAME = '").append(value).append("' ");
					break;
				case "userid":
					if(!StringUtils.isEmpty(value)){
						formSqlSb.append(" and at.Id = ? ");
						paramValue.add(value);
					}
					break;
				case "startCourseDate":
					formSqlSb.append(" AND DATE_FORMAT(courseplan_back.COURSE_TIME,'%Y-%m-%d') >= ? ");
					paramValue.add(value);
					break;
				case "endCourseDate":
					formSqlSb.append(" AND DATE_FORMAT(courseplan_back.COURSE_TIME,'%Y-%m-%d') <= ? ");
					paramValue.add(value);
					break;
				case "startDelDate":
					formSqlSb.append(" AND courseplan_back.UPDATE_TIME >= ? ");
					paramValue.add(value+" 00:00:00");
					break;
				case "endDelDate":
					formSqlSb.append(" AND courseplan_back.UPDATE_TIME <= ? ");
					paramValue.add(value+" 59:59:59");
					break;
				case "campusid":
					formSqlSb.append(" AND courseplan_back.CAMPUS_ID = ? ");
					paramValue.add(Integer.parseInt(value));
					break;
				default:
					break;
				}
			}
		}
		formSqlSb.append(" ORDER BY courseplan_back.UPDATE_TIME DESC");
	}

}
