package com.momathink.crm.opportunity.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

/**
 * 销售机会反馈
 * @author David
 *
 */
@Table(tableName = "crm_feedback")
public class Feedback extends BaseModel<Feedback> {

	private static final long serialVersionUID = 6823268247907819820L;
	public static final Feedback dao = new Feedback();

	public List<Feedback> queryFeedbacks(String opportunityId){
		if(StringUtils.isEmpty(opportunityId)){
			return null;
		}else{
			String sql = "SELECT * FROM crm_feedback cf WHERE cf.opportunityid=";
			return find(sql,Integer.parseInt(opportunityId));
		}
	}

	public List<Feedback> findByOpportunityId(String opportunityId) {
		String sql="SELECT u.REAL_NAME sysusername,cm.id mediatorid,cm.realname mediatorname,cf.createtime,cf.content "
				+ "FROM crm_feedback cf "
				+ "LEFT JOIN account u ON cf.userid=u.Id "
				+ "LEFT JOIN crm_mediator cm ON cf.mediatorid=cm.id "
				+ "WHERE cf.opportunityid=? and cf.callstatus is null ";
		return StringUtils.isEmpty(opportunityId)?null:Feedback.dao.find(sql,Integer.parseInt(opportunityId));
	}

	public Long getFeedbackTimes(Integer opportunityId) {
		if(opportunityId==null){
			return 0l;
		}else{
			Long count = Db.queryLong("SELECT COUNT(1) FROM crm_feedback WHERE opportunityid=?",opportunityId);
			return count;
		}
	}

	public List<Feedback> findByLimit(Integer sysuserId,Integer count) {
		String sql = "select *,user.REAL_NAME FEEDNAME from crm_feedback cf left join account user on cf.userid = user.ID "
				+ " left join crm_opportunity co on co.id = cf.opportunityid "
				+ " where 1=1  ";
		SysUser sysuser = SysUser.dao.findById(sysuserId);
		if(!Role.isAdmins(sysuser.getStr("roleids"))){
			sql += " and co.scuserid = "+sysuserId;
		}
		sql += "  order by cf.id desc limit ? ";
		return dao.find(sql,count);
	}

	public long queryBackTimes(Integer oppid, Integer sysuserId) {
		String sql = "select count(1) counts from crm_feedback where opportunityid = ?  ";
		return dao.findFirst(sql, oppid)==null?0:dao.findFirst(sql, oppid ).getLong("counts");
	}

	public List<Feedback> queryBackRecord(String oppid, Integer sysuserId) {
		String sql = "select DATE_FORMAT(cf.createtime,'%Y-%m-%d') createtime,cf.content,cf.callstatus,cf.callresult, acc.REAL_NAME "
				+ " from crm_feedback cf left join account acc on acc.Id=cf.userid "
				+ " where cf.opportunityid = ?  order by cf.createtime asc ";
		return dao.find(sql, oppid);
	}
	
}
