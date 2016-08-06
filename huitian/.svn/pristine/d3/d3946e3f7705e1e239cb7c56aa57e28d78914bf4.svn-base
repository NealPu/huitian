package com.momathink.crm.mediator.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.mediator.model.Company;

/**
 * @author David
 *
 */
public class CompanyService extends BaseService {
	private static Logger log = Logger.getLogger(CompanyService.class);

	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage) {
		log.debug("机构列表：分页处理");
		String select = " select c.*,a.real_name createusername ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from crm_company c ");
		formSqlSb.append(" left join account a on c.createuserid=a.id WHERE 1=1 ");
		if (null == queryParam) {
			return;
		}
		
		String userid = queryParam.get("userid");
		String companyname = queryParam.get("companyname");
		String phonenumber = queryParam.get("phonenumber");
		String contacts = queryParam.get("contacts");
		if(!StringUtils.isEmpty(userid)){
			formSqlSb.append(" and c.createuserid = ? ");
			paramValue.add(Integer.parseInt(userid));
		}
		if (null != companyname && !companyname.equals("")) {
			formSqlSb.append(" and c.companyname like ?");
			paramValue.add("%" + companyname + "%");
		}
		if (null != contacts && !contacts.equals("")) {
			formSqlSb.append(" and c.contacts like ?");
			paramValue.add("%" + contacts + "%");
		}
		if (null != phonenumber && !phonenumber.equals("")) {
			formSqlSb.append(" and c.phonenumber like ?");
			paramValue.add(phonenumber);
		}
		formSqlSb.append(" order by id desc");
	}

	@Before(Tx.class)
	public void save(Company company) {
		try {
			company.set("createtime", ToolDateTime.getDate());
			company.save();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("添加公司异常");
		}
	}
	
	@Before(Tx.class)
	public void update(Company company) {
		try {
			company.set("updatetime", new Date());
			company.update();
		} catch (Exception e) {
			throw new RuntimeException("更新机构异常");
		}
	}
}
