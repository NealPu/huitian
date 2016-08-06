package com.momathink.crm.opportunity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.constants.Constants;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.tools.ToolUtils;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.opportunity.model.Feedback;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.subject.model.Subject;

/**
 * 销售机会业务类
 * 
 * @author David
 *
 */
public class OpportunityService extends BaseService {

	private static Logger log = Logger.getLogger(OpportunityService.class);
	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	@SuppressWarnings("unchecked")
	public void list(SplitPage splitPage) {
		log.debug("销售机会管理：分页处理");
		String select = " select co.*,st.oppid as oppid,su.real_name scusername,cu.real_name kcusername,cm.realname mediatorname,crmsc.id crmscid,crmsc.name crmscname,cp.campus_name campus_name ";
		splitPageBase(splitPage, select);
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> olist = page.getList();
		for (Record r : olist) {
			String sids = r.getStr("subjectids");
			r.set("subjectnames", Subject.dao.getSubjectNameByIds(sids));
			r.set("feedbacktimes", Feedback.dao.getFeedbackTimes(r.getInt("id")));
		}
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from crm_opportunity co ");
		formSqlSb.append(" left join (select DISTINCT a.opportunityid as oppid"
				+ " from account a LEFT JOIN  crm_opportunity co  ON a.opportunityid = co.id  "
				+ " where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', a.roleids) ) > 0 and a.opportunityid is not NULL) st on st.oppid=co.id ");
		formSqlSb.append(" left join account su on co.scuserid=su.id ");
		formSqlSb.append(" left join account cu on co.kcuserid=cu.id ");
		formSqlSb.append(" left join campus cp on co.campusid=cp.id ");
		formSqlSb.append(" left join crm_mediator cm on co.mediatorid=cm.id ");
		formSqlSb.append(" left join crm_source crmsc on crmsc.id=co.source  WHERE 1=1 ");
		if (null == queryParam) {
			return;
		}
		String customer = queryParam.get("customer_rating");
		String isconver= queryParam.get("isconver");//成单状态
		String classtype = queryParam.get("classtype");//班课类型
		String source = queryParam.get("source");
		//String data1 = queryParam.get("date2");
		String contacter = queryParam.get("contacter");
		String phonenumber = queryParam.get("phonenumber");// 电话
		String startDate = queryParam.get("startDate");
		String endDate = queryParam.get("endDate");
		String parentname = queryParam.get("parentname");
		String scuserid = queryParam.get("scuserid");
		String kcuserid = queryParam.get("kcuserid");
		String mediatorid = queryParam.get("mediatorid");
		String subjectids=queryParam.get("subjectids");//科目
		String isread = queryParam.get("isread");
		String sysuserId = queryParam.get("userid");
		String relation = queryParam.get("relation");//关系
		String campid = queryParam.get("campusid");
		String pool = queryParam.get("querytype");
		SysUser sysuser = SysUser.dao.findById(Integer.parseInt(sysuserId));
		String campusIds = AccountCampus.dao.getCampusIdsByAccountId(sysuser.getPrimaryKeyValue());
		String roleids = sysuser.getStr("roleids");
		String whereSql = "";
		if(StringUtils.isEmpty(pool)){
			if(!Role.isAdmins(sysuser.getStr("roleids"))){//非管理员
				if(sysuser.getInt("showall")==1){
					whereSql += " AND co.campusid IN(" + campusIds+")";
				}else{
					if(Role.isKcgw(roleids)&&Role.isShichang(roleids)){//课程顾问
						whereSql += " and (co.kcuserid=" + sysuserId +" or co.scuserid="+sysuserId+") ";
					}else{
						if(Role.isKcgw(sysuser.getStr("roleids"))){
							whereSql += " AND co.kcuserid=" + sysuserId;
						}
						if(Role.isShichang(sysuser.getStr("roleids"))){
							whereSql += " AND co.scuserid=" + sysuserId;
						}
					}
				}
			}
		}
		formSqlSb.append(whereSql);
		if (!StringUtils.isEmpty(source)) {
			formSqlSb.append(" and co.source = ? ");
			paramValue.add(Integer.parseInt(source));
		}
		if (!StringUtils.isEmpty(isread)) {
			formSqlSb.append(" and co.isread = ?");
			paramValue.add(Integer.parseInt(isread));
		}
		if (null != scuserid && !scuserid.equals("")) {
			formSqlSb.append(" and co.scuserid = ?");
			paramValue.add(Integer.parseInt(scuserid));
		}
		if (null != kcuserid && !kcuserid.equals("")) {
			formSqlSb.append(" and co.kcuserid = ?");
			paramValue.add(Integer.parseInt(kcuserid));
		}
		if(!StringUtils.isEmpty(relation)){
			formSqlSb.append(" and co.relation = ? ");
			paramValue.add(Integer.parseInt(relation));
		}
		if (null != mediatorid && !mediatorid.equals("")) {
			formSqlSb.append(" and co.mediatorid = ?");
			paramValue.add(Integer.parseInt(mediatorid));
		}
		if (null != customer && !customer.equals("")) {
			formSqlSb.append(" and co.customer_rating = ?");
			paramValue.add(Integer.parseInt(customer));
		}
		if(!StringUtils.isEmpty(isconver)){
			formSqlSb.append(" and co.isconver = ? ");
			paramValue.add(Integer.parseInt(isconver));
		}else{
			String querytype = queryParam.get("querytype");
			if("pool".equals(querytype)){
				formSqlSb.append(" and (( co.isconver in (5) ) or (co.vallottime is not null and co.kcuserid is null))");
			}
		}
		if(!StringUtils.isEmpty(campid)){
			formSqlSb.append(" and co.campusid = ? ");
			paramValue.add(campid);
		}
		if(!StringUtils.isEmpty(classtype)){
			formSqlSb.append(" and co.classtype = ? ");
			paramValue.add(Integer.parseInt(classtype));
		}
		if(!StringUtils.isEmpty(subjectids)){
			formSqlSb.append(" and co.subjectids = ? ");
			paramValue.add(Integer.parseInt(subjectids));
		}
		if (null != contacter && !contacter.equals("")) {
			formSqlSb.append(" and co.contacter like ?");
			paramValue.add("%" + contacter + "%");
		}
		if (null != parentname && !parentname.equals("")) {
			formSqlSb.append(" and co.parentname like ?");
			paramValue.add(parentname);
		}
		if (null != phonenumber && !phonenumber.equals("")) {
			formSqlSb.append(" and co.phonenumber like ?");
			paramValue.add("%" + phonenumber + "%");
		}
		if (null != startDate && !startDate.equals("")) {
			formSqlSb.append(" and co.createtime >= ? ");
			paramValue.add(startDate+" 00:00:00");
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" and co.createtime <= ? ");
			paramValue.add(endDate + " 23:59:59");
		}
		
		formSqlSb.append(" order by id desc");
	}

	@Before(Tx.class)
	public void save(Opportunity opportunity) {
		try {
			// 保存顾问
			opportunity.set("status", "1");
			opportunity.set("createtime", new Date());
			opportunity.save();
		} catch (Exception e) {
			throw new RuntimeException("保存用户异常");
		}
	}

	@Before(Tx.class)
	public void update(Opportunity opportunity) {
		try {
			opportunity.set("updatetime", new Date());
			opportunity.update();
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常");
		}
	}

	public Opportunity findDetailById(String opportunityId) {
		Opportunity o = Opportunity.dao.findDetailById(opportunityId);
		if (o == null) {
			return null;
		} else {
			List<Feedback> fblist = Feedback.dao.findByOpportunityId(opportunityId);
			o.put("fblist", fblist);
			o.put("subjectnames", Subject.dao.getSubjectNameByIds(o.getStr("subjectids")));
			o.put("feedbacktimes", Feedback.dao.getFeedbackTimes(Integer.parseInt(opportunityId)));
			return o;
		}
	}

	public List<Opportunity> queryContacterByCounselor(Mediator mediator) {
		List<Opportunity> list = Opportunity.dao.findByMediatorId(mediator.getPrimaryKeyValue());
		for (Opportunity o : list) {
			List<Feedback> fblist = Feedback.dao.findByOpportunityId(o.getPrimaryKeyValue().toString());
			o.put("contacterFollowList", fblist);
			o.put("subject_name", Subject.dao.getSubjectNameByIds(o.getStr("subjectids")));
		}
		return list;
	}

	public void SaveFollowInfo(Opportunity opportunity, Feedback feedback) {
		if (opportunity != null) {
			Date date = new Date();
			Integer feedbacktimes = opportunity.getInt("feedbacktimes");
			if (feedbacktimes == null)
				feedbacktimes = 0;
			if (feedback != null) {
				opportunity.set("feedbacktimes", feedbacktimes + 1);
				feedback.set("createtime", date);
				feedback.set("mediatorid", opportunity.getInt("mediatorid"));
				feedback.set("opportunityid", opportunity.getPrimaryKeyValue());
				feedback.save();
			}
			opportunity.set("updatetime", date);
			opportunity.update();
		}
	}

	public JSONObject save(Map<String, String> param) {
		JSONObject json = new JSONObject();
		String code = Constants.OPERATE_FAILE_CODE;
		String msg = Constants.TUIJIAN_SUCCESS;
		Opportunity opportunity = new Opportunity();
		String openid = param.get("openid");
		String contacter = param.get("contactername");
		String phonenumber = param.get("phonenumber").replace("-", "");
		String subjectid = param.get("subjectid");
		String relation = param.get("relation");
		String note = param.get("note");
		String recommendphone=param.get("recommendphone");
		Mediator mediator = Mediator.dao.findByOpenId(openid);
		if (StringUtils.isEmpty(contacter)) {
			msg = Constants.NAME_CANNOT_EMPTY;
		} else {
			if (!StringUtils.isEmpty(note) && note.length() > 200) {
				msg = Constants.LENGTH_IS_ERROR;
			} else {
				long counts = Opportunity.dao.queryOpportunityCount("phonenumber", phonenumber, null);
				if (counts>=1) {
					msg = Constants.CONTACTER_IS_EXIST;
				} else {
					boolean isMobile = ToolUtils.isMobile(phonenumber);
					if (isMobile) {
						opportunity.set("phonenumber", phonenumber);
					} else {
						boolean isPhone = ToolUtils.isPhone(phonenumber);
						if (isPhone) {
							opportunity.set("phonenumber", phonenumber);
						} else {
							msg = Constants.PHONE_IS_ERROR;
							json.put("code", code);
							json.put("msg", msg);
							return json;
						}
					}
					if (mediator == null) {
						if(StringUtils.isEmpty(recommendphone)){
							msg = Constants.COUNSELOR_NOT_EXIST;
							json.put("code", code);
							json.put("msg", msg);
							return json;
						}else{
							Student student = Student.dao.findByPhone(recommendphone);
							if(student!=null){
								opportunity.set("recommenduserid", student.getPrimaryKeyValue().toString());
								opportunity.set("campusid", student.getInt("campusid"));
								opportunity.set("scuserid", student.getInt("scuserid"));// 所属市场ID
								param.put("campus", student.getInt("campusid").toString());
							}else{
								opportunity.set("campusid", 1);
								opportunity.set("scuserid", Campus.dao.findById(1).getInt("scuserid"));// 所属市场ID
								param.put("campus", "1");
							}
							opportunity.set("recommendphone", recommendphone);
							opportunity.set("recommendusername", param.get("recommendusername"));
						}
					} else {
						opportunity.set("mediatorid", mediator.getPrimaryKeyValue());
						opportunity.set("scuserid", mediator.getInt("sysuserid"));// 所属市场专员IDS
						opportunity.set("campusid", param.get("campus"));
					}
					opportunity.set("contacter", contacter);
					opportunity.set("subjectids", subjectid);
					opportunity.set("relation", relation);
					opportunity.set("note", note);
					opportunity.set("isconver", 0);
					opportunity.set("needcalled", param.get("needcalled"));
					String campusId = param.get("campus");
					Campus campus = Campus.dao.getCampusInfo(campusId);
					String kcgwMobile = campus.getStr("kcfzrtel");
					opportunity.set("createtime", new Date());
					opportunity.set("kcuserid", campus.getInt("kcuserid"));
					opportunity.set("source", "1");
					opportunity.save();
					code = Constants.OPERATE_SUCCESS_CODE;
					// 发短信给课程顾问
					if (!StringUtils.isEmpty(kcgwMobile)) {
						MessageService.sendMessageToAdvisor(MesContantsFinal.kc_sms_tuijian,MesContantsFinal.kc_email_tuijian, opportunity.getPrimaryKeyValue().toString());
					}
					// 发短信给留学顾问
					if (mediator!=null&&!StringUtils.isEmpty(mediator.getStr("phonenumber"))) {
						MessageService.sendMessageToMediator(MesContantsFinal.cs_sms_tuijian, MesContantsFinal.cs_email_tuijian,opportunity.getPrimaryKeyValue().toString(),null,null,null);
					}
				}
			}
		}
	
		
		
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}
	
	public JSONObject updateAllot(String oppid,String newkcgwid){
		JSONObject json = new JSONObject();
		boolean flag = false;
		try{
			Opportunity opp = Opportunity.dao.findById(oppid);
			if(opp!=null){
				SysUser newKcgw = SysUser.dao.findById(newkcgwid);
				if(newKcgw!=null){
					opp.set("kcuserid", newkcgwid);
					flag = opp.update();
				}
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(flag){
			json.put("code", "1");
			json.put("msg", "更改成功.");
		}else{
			json.put("code", "0");
			json.put("msg", "更改失败.");
		}
		
		return json;
		
	}
	
	public void allotList(SplitPage splitPage,String sysuserId){
		
		 List<Object> paramValue = new ArrayList<Object>();
		if(sysuserId!=null){
			StringBuffer select = new StringBuffer(" select co.*,campus.CAMPUS_NAME campusname,su.real_name scusername,cu.real_name kcusername,cm.realname mediatorname,crmsc.id crmscid,crmsc.name crmscname  ");
			StringBuffer formSql = new StringBuffer(" from crm_opportunity co left join campus on co.campusid = campus.id ");
			formSql.append("left join account su on co.scuserid=su.id ");
			formSql.append("left join account cu on co.kcuserid=cu.id left join crm_mediator cm on co.mediatorid=cm.id ");
			formSql.append("left join crm_source crmsc on crmsc.id=co.source WHERE 1=1  ");
			
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			boolean flag = false;
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "contacter":// 
					formSql.append(" and co.contacter like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "phonenumber":// 
					formSql.append(" and co.phonenumber = ? ");
					paramValue.add( value );
					break;
				case "customer_rating":// 
					formSql.append(" and co.customer_rating = ? ");
					paramValue.add( value );
					break;
				case "campusid":// 
					formSql.append(" and co.campusid = ? ");
					paramValue.add(Integer.parseInt(value));
					break;
				case "myvallot":// 
					formSql.append(" and (co.kcuserid = ? or co.scuserid = ? ) ");
					paramValue.add(value);
					paramValue.add(value);
					break;
				case "subjectids":// 
					formSql.append(" and FIND_IN_SET(?,REPLACE(co.subjectids,'|',',')) ");
					paramValue.add(value);
					break;
				case "classtype":// 
					formSql.append(" and co.classtype = ? ");
					paramValue.add(value);
					break;
				case "mediatorid":// 
					formSql.append(" and co.mediatorid = ? ");
					paramValue.add(value);
					break;
				case "sourceid":// 
					formSql.append(" and co.source = ? ");
					paramValue.add(value);
					break;
				case "isnoconver":// 
					flag = true;
					if(value.equals("0")){
						formSql.append(" and co.isconver <> 1 ");
					}else if(value.equals("1")){
						formSql.append(" and co.isconver = ? ");
						paramValue.add(value);
					}else if(value.equals("4")){
						formSql.append(" and co.isconver = ? ");
						paramValue.add(value);
					}
					break;
				case "isconver":// 
					if(!flag){
						formSql.append(" and co.isconver = ? ");
						paramValue.add(value);
					}
					break;
				case "valloted":// 
					if(value.equals("1")){
						formSql.append(" and co.kcuserid is not null ");
					}else{
						formSql.append(" and co.kcuserid is null ");
					}
					break;
				case "startday":// 		
					formSql.append(" and DATE_FORMAT(co.vallottime,'%Y-%m-%d') = ? ");
					paramValue.add(value);
					break;
				case "dayday":// 
					formSql.append("  and DATE_FORMAT(co.createtime,'%Y-%m-%d') = ? ");
					paramValue.add(value);
					break;
				case "startNextvisitDate":// 
					formSql.append(" and co.nextvisit >= ? ");
					paramValue.add(value);
					break;
				case "endNextvisitDate"://
					formSql.append(" and co.nextvisit <= ? ");
					paramValue.add(value);
					break;
				default:
					break;
				}
			}
			
			String myvallot = queryParam.get("myvallot");
			if(StringUtils.isEmpty(myvallot)){
				formSql.append(" and co.isconver != 1 ");
			}
			formSql.append(" ORDER BY co.id DESC");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			List<Record> olist = page.getList();
			for (Record r : olist) {
				String sids = r.getStr("subjectids");
				r.set("subjectnames", Subject.dao.getSubjectNameByIds(sids));
				r.set("feedbacktimes", Feedback.dao.getFeedbackTimes(r.getInt("id")));
			}
			splitPage.setPage(page);
		}
	
		
	}

	public boolean setKcgwToOpp(String kcgw, String opp) {
		boolean reboolean = true;
		Opportunity opportunity = Opportunity.dao.findById(opp);
		if(opportunity !=null){
			SysUser user = SysUser.dao.findById(kcgw);
			if(user!=null){
				opportunity.set("kcuserid", kcgw);
				opportunity.set("vallottime", new Date());
				opportunity.set("updatetime", new Date());
				opportunity.set("version" ,(opportunity.getInt("version")+1));
				opportunity.set("isconver" ,0);
				opportunity.set("nextvisit" ,null);
				boolean flag = opportunity.update();
				if(!flag){
					reboolean =  false;
				}
			}
		}
		return reboolean;
	}

	public boolean delOppVallot(String oppid) {
		boolean reflag = true;
		Opportunity opportunity = Opportunity.dao.findById(oppid);
		if(opportunity !=null){
			opportunity.set("kcuserid", null);
			opportunity.set("updatetime", new Date());
			opportunity.set("isconver", 5);
			opportunity.set("nextvisit" ,null);
			opportunity.set("version" ,(opportunity.getInt("version")+1));
			boolean flag = opportunity.update();
			if(!flag){
				return false;
			}else{
				reflag = flag;
			}
		}
		return reflag;
	}

	public boolean sureRebackTime(String oppid, String time) {
		boolean reflag = true;
		Opportunity opportunity = Opportunity.dao.findById(oppid);
		if(opportunity !=null){
			opportunity.set("nextvisit" ,time);
			boolean flag = opportunity.update();
			if(!flag){
				return false;
			}else{
				reflag = flag;
			}
		}
		return reflag;
		
	}

	public List<Record> getwuxiaojihui() {
		return Db.find("SELECT COUNT(*) shu,kcuserid FROM crm_opportunity WHERE isconver IN(0,2,3,6) GROUP BY kcuserid");
	}
	
	public List<Record> getwuxiaojihuiByid(int sysid) {
		return Db.find("SELECT COUNT(*) shu FROM crm_opportunity WHERE isconver IN(0,2,3,6) AND kcuserid = ? ",sysid);//GROUP BY kcuserid
	}
	
}
