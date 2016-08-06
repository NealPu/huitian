package com.momathink.crm.mediator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

@Table(tableName = "crm_mediator")
public class Mediator extends BaseModel<Mediator> {

	private static final long serialVersionUID = 3266082781995545961L;
	public static final Mediator dao = new Mediator();

	public Mediator findByEmail(String email) {
		if (!StringUtils.isEmpty(email)) {
			String sql = "select * from crm_mediator c where c.email=?";
			return findFirst(sql, email);
		}
		return null;
	}

	public Mediator findByOpenId(String openid) {
		if (!StringUtils.isEmpty(openid)) {
			String sql = "select * from crm_mediator c where c.openid=?";
			return findFirst(sql, openid);
		}
		return null;
	}

	public List<Mediator> findByParentid(String parentId) {
		return (StringUtils.isEmpty(parentId) ? null
				: find("select c.*,p.realname parentname from crm_mediator c left join crm_mediator p on c.parentid=p.id where c.parentid = ? order by createtime desc ",
						parentId));
	}

	public Mediator queryMediatorByInviteCode(String inviteCode) {
		return (StringUtils.isEmpty(inviteCode) ? null : findFirst("select * from crm_mediator c where c.invitecode = ?", inviteCode));
	}

	public Long findAllCount(Map<String, String> queryParam) {
		StringBuffer sql = new StringBuffer("select count(*) as counts from crm_mediator where 1=1 ");
		List<Object> paramValue = new ArrayList<Object>();
		if (null != queryParam) {
			String createtime = queryParam.get("createtime");// 用户真实姓名
			if (!StringUtils.isEmpty(createtime)) {
				sql.append(" and createtime = ?");
				paramValue.add(createtime);
			}
		}
		Record record = Db.findFirst(sql.toString(), paramValue.toArray());
		Long counts = record.getNumber("counts").longValue();
		return counts;
	}

	public Mediator findById(String id) {
		if (!StringUtils.isEmpty(id)) {
			String sql = "select c.*,p.realname parentname,cp.companyname,acc.REAL_NAME sysname  from crm_mediator c "
					+ " left join crm_mediator p on c.parentid = p.id " + " LEFT JOIN crm_company cp ON c.companyid=cp.id "
					+ "left join account as acc on acc.id=c.sysuserid where c.id=?";
			return findFirst(sql, id);
		}
		return null;
	}

	public List<Mediator> findMediatorIdAndName(String userid) {
		StringBuffer sb = new StringBuffer("select id,sysuserid,realname from crm_mediator where status=1");
		if(!StringUtils.isEmpty(userid)){
			sb.append(" and sysuserid = ").append(userid);
		}
		return find(sb.toString());
	}

	/**
	 * 获取推荐人
	 * 
	 * @param mediatorId
	 * @return
	 */
	public List<Mediator> getParentMediator(String mediatorId,String userid) {
		SysUser user = SysUser.dao.findById(userid);
		StringBuffer sb = new StringBuffer("select id,realname from crm_mediator where status=1 ");
		sb.append(StringUtils.isEmpty(mediatorId) ? "" : " and id!= "+mediatorId);
		sb.append(Role.isShichang(user.getStr("roleids")) ? " and sysuserid =  "+userid : "");
		return find(sb.toString());
	}

	/**
	 * 检查是否存在相应字段的数据
	 * 
	 * @param field
	 * @param value
	 * @param mediatorId
	 * @return
	 */
	public Long queryMediatorCount(String field, String value, String mediatorId) {
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			StringBuffer sql = new StringBuffer("select count(1) from crm_mediator where ");
			sql.append(field).append("='").append(value).append("'");
			if (!ToolString.isNull(mediatorId)) {
				sql.append(" and id != ").append(mediatorId);
			}
			return Db.queryLong(sql.toString());
		} else {
			return null;
		}
	}

	public Mediator findByInviteCode(String inviteCode) {
		return (StringUtils.isEmpty(inviteCode) ? null : findFirst("select * from crm_mediator m where m.invitecode = ?", inviteCode));
	}

	public List<Mediator> findByParentid(Integer mediatorId) {
		return mediatorId == null ? null
				: find("select c.*,p.realname parentname from crm_mediator c left join crm_mediator p on c.parentid=p.id where c.parentid = ? order by c.createtime desc ",
						mediatorId);
	}

	public Map<String, Mediator> findAllMediator() {
		List<Mediator> list = find("select * from crm_mediator");
		Map<String, Mediator> map = new HashMap<String, Mediator>();
		for (Mediator mediator : list) {
			map.put(mediator.getPrimaryKeyValue().toString(), mediator);
		}
		return map;
	}

	public Mediator getMediatorById(Integer id) {
		String sql = "select * from crm_mediator where id = ? ";
		Mediator mediator = dao.findFirst(sql, id);
		return mediator;
	}
	
	public List<Mediator> findAll(){
		return dao.find("select * from crm_mediator");
	}

	/**
	 * 统计渠道推荐的学生一对一消耗情况
	 * @param mediatorId
	 * @return
	 */
	public List<Mediator> statVip(String mediatorId) {
		String sql = "SELECT s.mediatorid,m.realname,m.ratio,DATE_FORMAT(c.COURSE_TIME,'%Y-%m') statmonth,SUM(t.class_hour) vipks,SUM(b.realamount) vipsxh,SUM(b.realamount*m.ratio) vipyj,SUM(b.rewardamount) vipxxh\n" +
				"FROM account_book b \n" +
				"LEFT JOIN account s ON b.accountid=s.Id\n" +
				"LEFT JOIN courseplan c ON b.courseplanid=c.Id\n" +
				"LEFT JOIN crm_mediator m ON s.mediatorid=m.id\n" +
				"LEFT JOIN time_rank t ON c.TIMERANK_ID=t.Id\n" +
				"LEFT JOIN course k ON b.courseid=k.Id\n" +
				"WHERE b.isclearing=0 AND b.operatetype=4 AND b.`status`=0 AND c.PLAN_TYPE=0 AND c.STATE=0 AND m.id IS NOT NULL\n";
		if(!StringUtils.isEmpty(mediatorId)){
			sql+=" AND m.id="+mediatorId;
		}
		sql+= " GROUP BY s.mediatorid,DATE_FORMAT(c.COURSE_TIME,'%Y-%m')";
		return dao.find(sql);
	}

	/**
	 * 统计渠道班课消耗情况
	 * @param mediatorId
	 * @return
	 */
	public List<Mediator> statBan(String mediatorId) {
		String sql = "SELECT s.mediatorid,m.realname,m.ratio,DATE_FORMAT(o.endTime,'%Y-%m') statmonth,"
				+ " SUM(o.lessonNum) banks,SUM(b.realamount) bansxh,SUM(b.realamount*m.ratio) banyj,SUM(b.rewardamount) banxxh " +
				"FROM account_book b \n" +
				"LEFT JOIN account s ON b.accountid=s.Id\n" +
				"LEFT JOIN class_order o ON b.classorderid=o.id\n" +
				"LEFT JOIN crm_mediator m ON s.mediatorid=m.id\n" +
				"WHERE b.isclearing=0 AND b.operatetype=4 AND b.`status`=0 AND o.endTime IS NOT NULL AND s.mediatorid IS NOT NULL\n";
		if(!StringUtils.isEmpty(mediatorId)){
			sql+=" AND m.id="+mediatorId;
		}
		sql+=" GROUP BY s.mediatorid,DATE_FORMAT(o.endTime,'%Y-%m')";
		return dao.find(sql);
	}

	public Mediator findByPhoneNumber(String pnumber) {
		return dao.findFirst("select * from crm_mediator where phonenumber = ? ", pnumber);
	}
	/**
	 * 根据渠道名称查找*
	 * @param mediatorname
	 * @return
	 */
	public Mediator findByName(String mediatorname) {
		String sql = "select * from crm_mediator where realname = '"+mediatorname+"'";
		return dao.findFirst(sql);
	}
	/**
	 * 根据名称模糊查询*
	 * @param name
	 * @return
	 */
	public List<Mediator> findByLikeName(String name) {
		String sql = "SELECT * FROM crm_mediator WHERE realname LIKE '%"+name+"%'";
		return dao.find(sql);
	}
	/**
	 * 根据名称模糊查询*
	 * @param name
	 * @return
	 */
	public List<Mediator> findByLikeName(String name,String scuserid) {
		String sql = "SELECT * FROM crm_mediator WHERE realname LIKE '%"+name+"%' and sysuserid = "+scuserid+"";
		return dao.find(sql);
	}
}
