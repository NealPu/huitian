package com.momathink.teaching.teacher.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.operator.model.Role;
import com.momathink.teaching.teacher.model.Receiver;
import com.momathink.teaching.teacher.model.Teacher;

public class TeacherService extends BaseService {

	private static Logger log = Logger.getLogger(TeacherService.class);

	
	
	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		log.debug("教师管理：分页处理");
		String select = " select s.* ,d.cname";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from account s LEFT JOIN "
				+ "(SELECT GROUP_CONCAT(c.CAMPUS_NAME) cname ,ac.account_id "
				+ "FROM account_campus ac LEFT JOIN account s ON ac.account_id = s.Id "
				+ "LEFT JOIN campus  c ON c.Id = ac.campus_id "
				+ "GROUP BY s.Id) d ON s.id = d.account_id where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', s.roleids) ) > 0 ");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		String state = queryParam.get("state");
		if(StringUtils.isEmpty(state))
			queryParam.put("state", "0");
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "state":
				formSqlSb.append(" and s.state = ?");
				paramValue.add(Integer.parseInt(value));
				break;
			case "teachername":// 学生姓名
				formSqlSb.append(" and s.real_name like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "email":// 邮箱
				formSqlSb.append(" and s.email like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "mobile":// 手机
				formSqlSb.append(" and s.mobile like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "telephone":// 电话
				formSqlSb.append(" and s.telephone like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "qq":// QQ
				formSqlSb.append(" and s.qq like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "createdate":// 添加日期
				formSqlSb.append(" and s.createtime=? ");
				paramValue.add("'" + value + "'");
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" ORDER BY s.id DESC");
	}
	
	@Before(Tx.class)
	public Integer save(Teacher account) {
		Integer id;
		try {
			// 保存顾问
			account.set("state", "0");
			account.set("create_time", new Date());
			Role group = Role.dao.getRoleByNumbers("teachers");
			account.set("roleids", group.getInt("id")+",");
			account.save();
		 id = account.getPrimaryKeyValue();
		} catch (Exception e) {
			throw new RuntimeException("保存用户异常");
		}
		return id;
	}

	@Before(Tx.class)
	public void update(Teacher account) {
		try {
			account.set("update_time", new Date());
			account.update();
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常");
		}
	}
	
	/**
	 * 已发消息分页*
	 * @param splitPage
	 */
	public void queryAllAnnouncement(SplitPage splitPage,Integer id) {
		List<Object> paramValue = new ArrayList<Object>();
			StringBuffer select = new StringBuffer("SELECT a.content,a.title,a.sendtime,a.Id,s.REAL_NAME fsr ");
			StringBuffer formSql = new StringBuffer(" FROM announcement a "
					+ " LEFT JOIN account  s  on a.teacherid = s.Id where a.teacherid = "+id);
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "title":
					formSql.append(" and a.title  like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "data1":
					formSql.append(" and DATE_FORMAT(a.sendtime,'%Y-%m-%d') = ? ");
					paramValue.add(value);
					break;
				default:
					break;
				}
			}
			formSql.append(" order BY a.Id");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			List<Record>  list =page.getList();
			for(Record r:list){
				List<Receiver> rlist = Receiver.dao.getUserState(r.getInt("id"));
				r.set("firstmessage",rlist.get(0));
				r.set("row",rlist.size());
				rlist.remove(0);
				r.set("rlist",rlist);
			}
			splitPage.setPage(page);
	}
	/**
	 * 已收消息分页*
	 */				
	public void queryAllReceiver(SplitPage splitPage,Integer id) {
		List<Object> paramValue = new ArrayList<Object>();
			StringBuffer select = new StringBuffer("SELECT a.*,s.REAL_NAME,ar.state,ar.id arid ");
			StringBuffer formSql = new StringBuffer(" FROM announcementreceiver ar "
					+ " LEFT JOIN announcement a  on ar.senderid = a.id "
					+ " LEFT JOIN account  s on a.teacherid = s.Id where ar.recipientid = "+id);
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "title":
					formSql.append(" and a.title  like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "states":
					formSql.append(" and ar.state = ?");
					paramValue.add(value);
					break;
				case "data1":
					formSql.append(" and DATE_FORMAT(a.sendtime,'%Y-%m-%d') = ? ");
					paramValue.add(value);
					break;
				default:
					break;
				}
			}
			formSql.append(" ORDER BY  ar.id DESC");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			splitPage.setPage(page);
	}
}
