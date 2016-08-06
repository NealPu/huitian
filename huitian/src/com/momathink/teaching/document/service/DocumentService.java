package com.momathink.teaching.document.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;

/**
 * 2015年8月19日prq
 * @author prq
 *
 */

public class DocumentService extends BaseService {

	private static final Logger logger = Logger.getLogger(DocumentService.class);

	public void documentList(SplitPage splitPage) {
		
		List<Object> paramValue = new ArrayList<Object>();
		StringBuffer select =new StringBuffer("SELECT pul.*,stu.real_name stuname,uld.real_name uldname ");
		StringBuffer formSqlSb= new StringBuffer(" FROM pt_upload pul left join account stu on stu.id=pul.studentid left join account uld on uld.id=pul.uploaduser where 1=1 ");
		Map<String,String> queryParam = splitPage.getQueryParam();
		String studentname = queryParam.get("studentname");
		String begintime = queryParam.get("begindate");
		String endtime = queryParam.get("enddate");
		String documentname = queryParam.get("documentname");
		if (!StringUtils.isEmpty(studentname)) {
			logger.info("按学生姓名查询");
			formSqlSb.append(" AND stu.real_name like ").append("'%"+studentname+"%'");
		}
		if (!StringUtils.isEmpty(begintime)) {
			logger.info("上传时间开始");
			formSqlSb.append(" AND DATE_FORMAT(pul.uploadtime,'%Y-%m-%d') >= ? ");
			paramValue.add(begintime);
		}
		if (!StringUtils.isEmpty(endtime)) {
			logger.info("上传时间截止");
			formSqlSb.append(" AND DATE_FORMAT(pul.uploadtime,'%Y-%m-%d') <= ? ");
			paramValue.add(endtime);
		}
		if (!StringUtils.isEmpty(documentname)) {
			logger.info("按文档名称查询");
			formSqlSb.append(" AND pul.documentname like ").append("'%"+documentname+"%'");
		}
		formSqlSb.append(" order by pul.id desc ");
		
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSqlSb.toString(), paramValue.toArray());
		splitPage.setPage(page);
	}
	
	
}
