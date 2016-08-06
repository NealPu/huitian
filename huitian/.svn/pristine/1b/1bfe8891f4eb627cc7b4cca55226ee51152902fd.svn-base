package com.momathink.crm.opportunity.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.subject.model.Subject;

/**
 * 销售机
 * @author David
 *
 */
@Table(tableName = "crm_opportunity")
public class Opportunity extends BaseModel<Opportunity> {

	private static final long serialVersionUID = 6735652422367149354L;
	public static final Opportunity dao = new Opportunity();

	/**
	 * 检查是否存在相应字段的数据
	 * @param field
	 * @param value
	 * @param mediatorId
	 * @return
	 */
	public Long queryOpportunityCount(String field, String value, String id) {
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			StringBuffer sql = new StringBuffer("select count(1) from crm_opportunity where ");
			sql.append(field).append("='").append(value).append("'");
			if (!ToolString.isNull(id)) {
				sql.append(" and id != ").append(id);
			}
			return Db.queryLong(sql.toString());
		} else {
			return null;
		}
	}

	/**
	 * 根据销售机会ID查询详情
	 * @param opportunityId
	 * @return
	 */
	public Opportunity findDetailById(String opportunityId) {
		if(StringUtils.isEmpty(opportunityId)){
			return null;
		}else{
			String sql = " select co.*,su.real_name scusername,cu.real_name kcusername,cm.realname mediatorname,tu.real_name confirmusername,crmsc.id crmscid,crmsc.name crmscname  "
					+ " from crm_opportunity co "
					+ " left join account su on co.scuserid=su.id "
					+ " left join account cu on co.kcuserid=cu.id "
					+ " left join account tu on co.confirmuserid=tu.id "
					+ " left join crm_mediator cm on co.mediatorid=cm.id "
					+ " left join crm_source crmsc on crmsc.id = co.source  WHERE co.id=?";
			return dao.findFirst(sql, Integer.parseInt(opportunityId));
		}
	}

	/**
	 * 查询已成单销售机会
	 * @return
	 */
	public List<Opportunity> findIsConver() {
		return dao.find("SELECT id,contacter,phonenumber FROM crm_opportunity WHERE isconver=1 ORDER BY id DESC");
	}

	/**
	 * 根据留学顾问查询销售机会
	 * @param mediatorId
	 * @return
	 */
	public List<Opportunity> findByMediatorId(Integer mediatorId) {
		List<Opportunity> olist = dao.find("select o.* from crm_opportunity o WHERE o.mediatorid=? ORDER BY o.createtime DESC",mediatorId);
		return olist;
	}

	/**
	 * 
	 * @param opportunityid
	 * @param followresult
	 * @return
	 */
	public List<Opportunity> findIsConverById(String opportunityid, String isconver) {
		StringBuffer sql = new StringBuffer("select o.* from crm_opportunity o where 1=1");
		if(!StringUtils.isEmpty(opportunityid))
			sql.append(" and o.opportunityid =" ).append(opportunityid);
		if(!StringUtils.isEmpty(isconver))
			sql.append(" and o.isconver = ").append(isconver);
		List<Opportunity> list = dao.find(sql.toString());
		for(Opportunity o : list){
			o.put("subject_name", Subject.dao.getSubjectNameByIds(o.getStr("subjectids")));
		}
		return list;
	}

	public Long getUnreadOppoCounts(Integer id) {
		String sql = "select COUNT(*) as counts from crm_opportunity where isread = 0 and scuserid = ? ";
		Long num = dao.find(sql, id).get(0).get("counts");
		return num;
	}

	public Long getUserSales(Integer sysId, String date) {
		String sql = "select COUNT(*) as counts from crm_opportunity opp where 1=1  ";
		SysUser sysuser = SysUser.dao.findById(sysId);
		if(!Role.isAdmins(sysuser.getStr("roleids"))){
			sql += " and opp.scuserid = "+sysId;
		}
		if(!StringUtils.isEmpty(date)){
			sql += " and createtime>=  '" + date + "'";
		}
		return dao.find(sql).get(0).get("counts");
	}

	public List<Opportunity> findByLimit(Integer count) {
		StringBuffer sql = new StringBuffer(" select co.*,su.real_name scusername,cu.real_name kcusername,cm.realname mediatorname ");
		sql.append(" from crm_opportunity co ");
		sql.append(" left join account su on co.scuserid=su.id ");
		sql.append(" left join account cu on co.kcuserid=cu.id ");
		sql.append(" left join crm_mediator cm on co.mediatorid=cm.id ORDER BY co.id DESC limit ? ");
		List<Opportunity> list = dao.find(sql.toString(), count);
		for(Opportunity o: list){
			o.put("subjectnames", Subject.dao.getSubjectNameByIds(o.getStr("subjectids")));
			o.put("feedbacktimes", Feedback.dao.getFeedbackTimes(o.getInt("id")));
		}
		return list;
	}

	public Long getOppCountBySourceId(String source) {
		String sql = "select count(1) counts from crm_opportunity co where co.source = ? ";
		Opportunity opp = dao.findFirst(sql, source);
		return opp==null?0:opp.getLong("counts");
	}
	
	//总销售
	public Long getAmountOpportunity() {
	
		//String sql = "select count(*) from crm_opportunity ";
		Long opp = Db.queryLong("select count(*) from crm_opportunity ");
		return opp==null?0:opp.longValue();
	}

	public Long getWeekOpportunity(String date1, String date2) {
		
		Long opp = Db.queryLong("select count(*) from crm_opportunity where createtime >='"+date1+"' and createtime <='"+date2+"'");
		return opp==null?0:opp.longValue();
	}


	/**
	 * //销售机会
	 * @param date1
	 * @param date2
	 * @param integer
	 * @return
	 */
	public Long getOneSale(String date1, String date2, Integer integer) {
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" select count(1) from crm_opportunity cp ");
		Long total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join account a ON cp.id = a.opportunityid left join student_kcgw ak ON a.Id = ak.student_id ");
		}
		sql.append(" where cp.createtime >= ?");
		sql.append(" and cp.createtime<= ?");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryLong(sql.toString(),date1,date2,integer);
		}
		
		else{
			total = Db.queryLong(sql.toString(),date1,date2);
		}
		
		return  total;
	}

	public Long getThreeMonthSale(String date1) {
		//Long opp = Db.queryLong("SELECT count(*) FROM crm_opportunity cp WHERE  cp.createtime>DATE_SUB(CURDATE(), INTERVAL 1 MONTH) ");
				Long opp = Db.queryLong("select count(*) from crm_opportunity where createtime >='"+date1 +"'");
				return opp;
	}



	public Long getMonth(String date) {
		Long opp = Db.queryLong("select sum(*) from crm_opportunity where createtime <='"+date+"'");
		return opp;
	}

	public Long getYear(String date,String now) {
	
		long total = Db.queryLong("select count(*) from crm_opportunity where createtime >='"+date+"' and createtime<='"+now+"'");
		
		return total;
	}
	/**
	 * 查询今天需要回访的销售机会
	 */
	public List<Opportunity> findAllTodayReturnOppMessage(Record r) {
		StringBuffer sql = new StringBuffer("");
		 sql.append("select co.id,co.contacter,co.phonenumber,co.isconver ,kc.REAL_NAME ,c.CAMPUS_NAME ,co.subjectids"
					+ " from crm_opportunity co  "
					+ " LEFT JOIN  (select Id ,REAL_NAME from account where "
					+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'admin'), CONCAT(',', roleids) ) > 0 "
					+ " or LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0) kc ON co.kcuserid = kc.Id "
					+ " LEFT JOIN campus c on co.campusid = c.Id  "
					+ " where co.nextvisit = (select current_date) ");
		 if(Role.isKcgw(r.getStr("roleids"))){
			 sql.append(" and co.kcuserid = "+r.getInt("id"));
		 }
		return dao.find(sql.toString());
	}

}
