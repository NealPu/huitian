package com.momathink.crm.opportunity.service;

import java.text.ParseException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.opportunity.model.Opportunity;

/**
 * 销售机会业务类
 * 
 * @author David
 *
 */
public class OpportunityCensusService extends BaseService {

	/**
	 * 分页
	 * 
	 * @param splitPage
	 * @throws ParseException 
	 */

	public List<Opportunity> getOpportunityCensusByCampus(String yuefen1,String yuefen2,String campusids){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.id, DATE_FORMAT(o.createtime,'%Y-%m') yuefen,c.CAMPUS_NAME,COUNT(*) zxl,(SELECT COUNT(*) FROM crm_opportunity p WHERE isconver = 1 AND p.campusid = o.campusid AND DATE_FORMAT(p.convertime,'%Y-%m')=yuefen ) cdl FROM crm_opportunity o LEFT JOIN campus c ON c.Id = o.campusid WHERE 1=1 ");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')<='"+yuefen1+"'");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')>='"+yuefen2+"'");
		if(!ToolString.isNull(campusids)){
			sql.append(" AND c.Id IN("+campusids+") ");
		}
		sql.append(" GROUP BY o.campusid,yuefen ");
		List<Opportunity> list = Opportunity.dao.find(sql.toString());
		return list;
	}
	
	public List<Opportunity> getOpportunityCensusByCustomerRating(String yuefen1,String yuefen2,String campusids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT o.customer_rating,DATE_FORMAT(o.createtime,'%Y-%m') yuefen,(case when (customer_rating= 0) then '未知客户' when (customer_rating= 1) then '潜在客户' when (customer_rating= 2) then '目标客户' when (customer_rating= 3) then '发展中客户' when (customer_rating= 4) then '交易客户' when (customer_rating= 5) then '后续介绍客户' when (customer_rating= 6) then '非客户' else  0 end) as 'kehudengji',COUNT(*) zxl,(SELECT COUNT(*) FROM crm_opportunity p WHERE isconver = 1 AND p.customer_rating = o.customer_rating AND p.campusid = o.campusid AND DATE_FORMAT(p.convertime,'%Y-%m')=yuefen) cdl FROM crm_opportunity o LEFT JOIN campus c ON c.Id = o.campusid WHERE 1=1 ");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')<='"+yuefen1+"'");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')>='"+yuefen2+"'");
		if(!ToolString.isNull(campusids)){
			sql.append(" AND c.Id IN("+campusids+") ");
		}
		sql.append(" GROUP BY o.customer_rating,yuefen ");
		List<Opportunity> list = Opportunity.dao.find(sql.toString());
		return list;
	}
	
	public List<Opportunity> getOpportunityCensusBySource(String yuefen1,String yuefen2,String campusids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT o.source,DATE_FORMAT(o.createtime,'%Y-%m') yuefen,s.`name` sname,COUNT(*) zxl,(SELECT COUNT(*) FROM crm_opportunity p WHERE isconver = 1 AND p.source = o.source AND p.campusid = o.campusid AND DATE_FORMAT(p.convertime,'%Y-%m')=yuefen) cdl FROM crm_opportunity o LEFT JOIN campus c ON c.Id = o.campusid LEFT JOIN crm_source s ON s.id = o.source WHERE 1=1 ");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')<='"+yuefen1+"'");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')>='"+yuefen2+"'");
		if(!ToolString.isNull(campusids)){
			sql.append(" AND c.Id IN("+campusids+") ");
		}
		sql.append(" GROUP BY o.source,yuefen ");
		List<Opportunity> list = Opportunity.dao.find(sql.toString());
		return list;
	}
	
	public List<Opportunity> getOpportunityCensusBymediator(String yuefen1,String yuefen2,String campusids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DATE_FORMAT(o.createtime,'%Y-%m') yuefen,o.mediatorid,m.realname,COUNT(*) zxl,(SELECT COUNT(*) FROM crm_opportunity p WHERE isconver = 1 AND p.mediatorid = o.mediatorid AND DATE_FORMAT(p.convertime,'%Y-%m')=yuefen) cdl FROM crm_opportunity o LEFT JOIN campus c ON c.Id = o.campusid LEFT JOIN crm_mediator m ON m.id = o.mediatorid WHERE 1=1 ");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')<='"+yuefen1+"'");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')>='"+yuefen2+"'");
		if(!ToolString.isNull(campusids)){
			sql.append(" AND c.Id IN("+campusids+") ");
		}
		sql.append(" GROUP BY o.mediatorid,yuefen  ");
		List<Opportunity> list = Opportunity.dao.find(sql.toString());
		return list;
	}
	
	public List<Opportunity> getOpportunityCensusBykcuser(String yuefen1,String yuefen2,String campusids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DATE_FORMAT(o.createtime,'%Y-%m') yuefen,o.kcuserid,a.REAL_NAME,COUNT(*) zxl,(SELECT COUNT(*) FROM crm_opportunity p WHERE isconver = 1 AND p.kcuserid = o.kcuserid AND DATE_FORMAT(p.convertime,'%Y-%m')=yuefen) cdl FROM crm_opportunity o LEFT JOIN campus c ON c.Id = o.campusid LEFT JOIN account a ON a.id = o.kcuserid WHERE 1=1 ");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')<='"+yuefen1+"'");
		sql.append(" AND DATE_FORMAT(o.createtime,'%Y-%m')>='"+yuefen2+"'");
		if(!ToolString.isNull(campusids)){
			sql.append(" AND c.Id IN("+campusids+") ");
		}
		sql.append(" GROUP BY o.kcuserid,yuefen  ");
		List<Opportunity> list = Opportunity.dao.find(sql.toString());
		return list;
	}
	
	public List<Record> getmingxi(String code, String yuefen, String message) {
		StringBuffer sql = new StringBuffer();
		sql.append("select DATE_FORMAT(co.createtime,'%Y-%m') yuefen,co.*,st.oppid as oppid,"
				+ " su.real_name scusername,cu.real_name kcusername,cm.realname mediatorname,"
				+ " crmsc.id crmscid,crmsc.name crmscname,cp.campus_name campus_name from crm_opportunity co "
				+ " left join (select DISTINCT a.opportunityid as oppid from account a "
				+ " LEFT JOIN  crm_opportunity co  ON a.opportunityid = co.id  "
				+ " where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', a.roleids) ) > 0 and a.opportunityid is not NULL) st "
				+ " on st.oppid=co.id left join account su on co.scuserid=su.id "
				+ " left join account cu on co.kcuserid=cu.id left join campus cp on co.campusid=cp.id "
				+ " left join crm_mediator cm on co.mediatorid=cm.id "
				+ " left join crm_source crmsc on crmsc.id=co.source  WHERE 1=1 ");
		sql.append(" AND DATE_FORMAT(co.createtime,'%Y-%m')='"+yuefen+"'");
		if(code.equals("0")){
			sql.append(" AND co.campusid="+message);
		}else if(code.equals("1")){
			sql.append(" AND co.source="+message);
		}else if(code.equals("2")){
			sql.append(" AND co.mediatorid="+message);
		}else if(code.equals("3")){
			sql.append(" AND co.kcuserid="+message);
		}else if(code.equals("4")){
			sql.append(" AND co.customer_rating="+message);
		}
		
		sql.append(" order by id desc  ");
		List<Record> list = Db.find(sql.toString());
		return list;
	}

	
}
