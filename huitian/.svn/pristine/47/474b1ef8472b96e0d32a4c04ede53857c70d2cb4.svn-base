package com.momathink.teaching.grade.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.teaching.grade.model.GradeDetail;
import com.momathink.teaching.grade.model.GradeRecord;

public class GradeRecordService extends BaseService {
	private static final Logger logger = Logger.getLogger(GradeRecordService.class);

	public void list(SplitPage splitPage) {
		logger.debug("成绩管理：分页处理");
		String select = " select * ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from graderecord where 1=1 ");
		if (null == queryParam) {
			return;
		}else{
			String studentId = queryParam.get("studentId");
			String subjectId = queryParam.get("subjectId");
			String studentName = queryParam.get("studentName");
			if (!ToolString.isNull(studentId)) {
				formSqlSb.append(" and studentid = ? ");
				paramValue.add(Integer.parseInt(studentId));
			}
			if (!ToolString.isNull(studentName)) {
				formSqlSb.append(" and studentname like ? ");
				paramValue.add("%"+studentName+"%");
			}
			if (!ToolString.isNull(subjectId)) {
				formSqlSb.append(" and subjectid = ? ");
				paramValue.add(Integer.parseInt(subjectId));
			}
		}
	}

	public void saveGradeInfo(GradeRecord record, List<GradeDetail> detailList) {
		if (record != null) {
			Date date = ToolDateTime.getDate();
			record.set("createtime", date);
			if (record.getDate("examdate") == null)
				record.set("examdate", ToolDateTime.format(date, ToolDateTime.pattern_ymd));
			record.save();
			if (detailList != null) {
				Integer recordId = record.getPrimaryKeyValue();
				for (GradeDetail detail : detailList) {
					detail.set("recordid", recordId);
					detail.set("createtime", date);
					detail.save();
				}
			}
		}
		logger.info("保存成绩成功");
	}

	public void deleteByRecordId(Integer paraToInt) {
		GradeDetail.dao.deleteByRecordId(paraToInt);
		GradeRecord.dao.deleteById(paraToInt);
	}
}
