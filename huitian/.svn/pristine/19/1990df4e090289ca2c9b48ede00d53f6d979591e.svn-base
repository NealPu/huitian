package com.momathink.crm.mediator.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.constants.Constants;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.plugin.MessagePropertiesPlugin;
import com.momathink.common.tools.ToolArith;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.sms.model.SendSMS;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.student.service.StudentService;
import com.momathink.weixin.model.User;
import com.momathink.weixin.tools.ToolUser;

/**
 * 顾问管理 ClassName: MediatorService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年8月10日 下午6:38:34 <br/>
 * 
 * @author David
 * @version
 * @since JDK 1.7
 */
public class MediatorService extends BaseService {

	private static Logger log = Logger.getLogger(MediatorService.class);
	private StudentService studentService = new StudentService();

	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage) {
		log.debug("留学顾问管理：分页处理");
		String select = " select cm.*,su.real_name sysusername,pm.realname parentname,cp.companyname ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from crm_mediator cm left join account su on cm.sysuserid=su.id ");
		formSqlSb.append(" left join crm_mediator pm on cm.parentid=pm.id ");
		formSqlSb.append(" LEFT JOIN crm_company cp ON cm.companyid=cp.id WHERE 1=1");
		if (null == queryParam) {
			return;
		}

		String realname = queryParam.get("realname");
		String phonenumber = queryParam.get("phonenumber");// 电话
		String startDate = queryParam.get("startDate");
		String endDate = queryParam.get("endDate");
		String parentname = queryParam.get("parentname");
//		String sysUserId = queryParam.get("sysuserid");
		String userid = queryParam.get("userid");

		if(!StringUtils.isEmpty(userid)){
			SysUser sysuser = SysUser.dao.findById(userid);
			if(!Role.isAdmins(sysuser.getStr("roleids"))){
				String fzxqids = Campus.dao.IsCampusFzr(Integer.parseInt(userid));
				if(fzxqids != null){
					formSqlSb.append("\n  AND su.campusid in(").append(fzxqids).append(") ");
				}else{
					String campusids = Campus.dao.IsCampusScFzr(Integer.parseInt(userid));
					if(campusids != null){
						formSqlSb.append("\n  AND su.campusid in(").append(campusids).append(") ");
					}else{
						formSqlSb.append("\n  and cm.sysuserid= ? ");
						paramValue.add(userid);
					}
				}
			}
		}
//		if (null != sysUserId && !sysUserId.equals("")) {
//			formSqlSb.append(" and cm.sysuserid = ?");
//			paramValue.add(Integer.parseInt(sysUserId));
//		}
		if (null != realname && !realname.equals("")) {
			formSqlSb.append(" and cm.realname like ?");
			paramValue.add("%" + realname + "%");
		}
		if (null != parentname && !parentname.equals("")) {
			formSqlSb.append(" and parentname like ?");
			paramValue.add(parentname);
		}
		if (null != phonenumber && !phonenumber.equals("")) {
			formSqlSb.append(" and cm.phonenumber like ?");
			paramValue.add("%" + phonenumber + "%");
		}
		if (null != startDate && !startDate.equals("")) {
			formSqlSb.append(" and cm.createtime >= ? ");
			paramValue.add(startDate + " 00:00:00");
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" and cm.createtime <= ? ");
			paramValue.add(endDate + " 23:59:59");
		}
		formSqlSb.append(" order by id desc");
	}

	@Before(Tx.class)
	public void save(Mediator mediator) {
		try {
			// 保存顾问
			mediator.set("status", "1");
			mediator.set("createtime", new Date());
			mediator.save();
		} catch (Exception e) {
			throw new RuntimeException("保存用户异常");
		}
	}

	@Before(Tx.class)
	public void update(Mediator mediator) {
		try {
			mediator.set("updatetime", new Date());
			mediator.update();
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常");
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> findAllSpreader(Mediator mediator, Integer level, Map<String, Object> resultAll) {

		Map<String, Object> spread = new LinkedHashMap<String, Object>();
		Map<String, Object> spreadTemp = new LinkedHashMap<String, Object>();
		List<Mediator> list = Mediator.dao.findByParentid(mediator.getPrimaryKeyValue());// 取出LV1用户
		if (list != null && list.size() != 0) {
			float szksTotalSum = 0;// 上周课程总数
			double szxhTotalSum = 0;// 上周消耗总数
			float ljksTotalSum = 0;// 累计课程总数
			double ljxhTotalSum = 0;// 累计消耗总数
			int ztjs = 0;// 总推荐数
			int zcds = 0;// 总成单数
			double zyj = 0;// 总佣金
			spreadTemp = (Map<String, Object>) resultAll.get("LV" + level);
			if (spreadTemp != null) {
				List<Mediator> listTemp = (List<Mediator>) spreadTemp.get("spreaders");
				szksTotalSum = (float) spreadTemp.get("szksTotalSum");// 上周课程
				szxhTotalSum = (double) spreadTemp.get("szxhTotalSum");// 上周消耗
				ljksTotalSum = (float) spreadTemp.get("ljksTotalSum");// 累计课程
				ljxhTotalSum = (double) spreadTemp.get("ljxhTotalSum");// 累计消耗
				ztjs = (int) spreadTemp.get("ztjs");// 总推荐数
				zcds = (int) spreadTemp.get("zcds");// 总成单数
				zyj = (double) spreadTemp.get("zyj");// 总佣金
				listTemp.addAll(list);
				spread.put("total", listTemp.size());
				spread.put("spreaders", listTemp);

			} else {
				spread.put("total", list.size());
				spread.put("spreaders", list);
			}
			for (Mediator c : list) {// 循环LV1用户
				List<Student> slist = studentService.queryStudentByMediator(c);
				List<Opportunity> clist = Opportunity.dao.findByMediatorId(c.getPrimaryKeyValue());
				List<Mediator> cslist = Mediator.dao.findByParentid(c.getPrimaryKeyValue());// 取出LV1下的LV1用户
				float szksTotal = 0;
				double szxhTotal = 0;
				float ljksTotal = 0;
				double ljxhTotal = 0;
				int tjs = clist == null ? 0 : clist.size();
				int cds = slist == null ? 0 : slist.size();
				for (Student s : slist) {
					double szxh = s.getDouble("szxh");
					double ljxh = s.getDouble("ljxh");
					szksTotal += s.getFloat("szks");;
					szxhTotal += szxh;
					log.info("学生姓名："+s.getStr("real_name")+s.get("ljks"));
					ljksTotal += s.getFloat("ljks");
					ljxhTotal += ljxh;
				}
				szksTotalSum += szksTotal;
				szxhTotalSum += szxhTotal;
				ljksTotalSum += ljksTotal;
				ljxhTotalSum += ljxhTotal;
				c.put("szksTotal", szksTotal);
				c.put("szxhTotal", szxhTotal);
				c.put("ljksTotal", ljksTotal);
				c.put("ljxhTotal", ljxhTotal);
				c.put("tuijian", tjs);
				c.put("chengdan", cds);
				c.put("tuiguang", cslist == null ? 0 : cslist.size());
				c.put("yongjin", getCommission(ljxhTotal, level));
				ztjs += tjs;
				zcds += cds;
				this.findAllSpreader(c, level + 1, resultAll);
			}
			spread.put("szksTotalSum", szksTotalSum);// 上周课程
			spread.put("szxhTotalSum", szxhTotalSum);// 上周消耗
			spread.put("ljksTotalSum", ljksTotalSum);// 累计课程
			spread.put("ljxhTotalSum", ljxhTotalSum);// 累计消耗
			spread.put("ztjs", ztjs);// 总推荐数
			spread.put("zcds", zcds);// 总成单数
			spread.put("zyj", getCommission(ljxhTotalSum, level) + zyj);// 总佣金
			resultAll.put("LV" + level, spread);
		}
		return resultAll;
	}

	private double getCommission(double ljxhTotal, Integer level) {
		double bl=0;
		Organization org = Organization.dao.findById(1);
		switch (level) {
		case 1:
			bl=Double.parseDouble(org.get("basic_promolv1").toString());
			break;
		case 2:
			bl=Double.parseDouble(org.get("basic_promolv2").toString());
			break;
		case 3:
			bl=Double.parseDouble(org.get("basic_promolv3").toString());
			break;
		case 4:
			bl=Double.parseDouble(org.get("basic_promolv4").toString());
			break;
		case 5:
			bl=Double.parseDouble(org.get("basic_promolv5").toString());
			break;
		}
		return ToolArith.mul(ljxhTotal, bl);
	}

	/**
	 * 推广渠道用户通过微信端注册
	 * @param mediator渠道
	 * @param inviteCode邀请码
	 * @param appId
	 * @param appSecret
	 * @param wpnumberId所属公众号ID
	 * @return
	 */
	public JSONObject register(Mediator mediator, String inviteCode,String appId,String appSecret, String wpnumberId) {
		JSONObject json = new JSONObject();
		String code = Constants.OPERATE_SUCCESS_CODE;
		String msg = Constants.OPERATE_SUCCESS;
		if(mediator == null){
			code = Constants.OPERATE_FAILE_CODE;
			msg = Constants.ENTITY_IS_NULL;
		}else{
			String openId = mediator.getStr("openid");
			User wxuser = User.dao.findByOpenId(openId,wpnumberId);
			if(wxuser == null){
				ToolUser.saveUser(openId,appId,appSecret,wpnumberId);
			}
			long emailCount = Mediator.dao.queryMediatorCount("email", mediator.getStr("email"), null);
			long phoneCount = Mediator.dao.queryMediatorCount("phonenumber", mediator.getStr("phonenumber"),null);
			long qqCount = Mediator.dao.queryMediatorCount("qq", mediator.getStr("qq"),null);
			Mediator parentMediator = Mediator.dao.findByInviteCode(inviteCode);
			if(phoneCount>=1){
				code = Constants.OPERATE_FAILE_CODE;
				msg = Constants.MOBILE_IS_EXIST;
			}else{
				if(emailCount>=1){
					code = Constants.OPERATE_FAILE_CODE;
					msg = Constants.QQ_IS_EXIST;
				}else{
					if(qqCount>=1){
						code = Constants.OPERATE_FAILE_CODE;
						msg = Constants.QQ_IS_EXIST;
					}else{
						if(parentMediator == null){
							code = Constants.OPERATE_FAILE_CODE;
							msg = Constants.COUNSELOR_PARENT_NOT_EXIST;
						}else{
							mediator.set("parentid", parentMediator.getPrimaryKeyValue());
							mediator.set("sysuserid", parentMediator.getInt("sysuserid"));//所属市场专员ID
							save(mediator);
							JSONObject data = new JSONObject();
							String newCounselorName = mediator.getStr("realname");
							String newCounselorMobile = mediator.getStr("phonenumber");
							data.put("realname", newCounselorName);
							data.put("mobile", newCounselorMobile);
							data.put("email", mediator.getStr("email"));
							data.put("regdate", mediator.getDate("regdate"));
							data.put("ids", mediator.getPrimaryKeyValue());
							data.put("qq", mediator.getStr("qq"));
							data.put("cardholder", mediator.getStr("cardholder"));
							data.put("bankname", mediator.getStr("bankname"));
							data.put("bankcard", mediator.getStr("bankcard"));
							json.put("data", data);
							//发短信给留学顾问
							if(!StringUtils.isEmpty(newCounselorMobile)){
								String zhuceSmsContent = MessagePropertiesPlugin.getSmsMessageMapValue(MesContantsFinal.cs_sms_zhuce);
								zhuceSmsContent = zhuceSmsContent.replace("{counselor_name}", newCounselorName);
								SendSMS.sendSms(zhuceSmsContent, newCounselorMobile);
							}
							//发短信给上级推荐留学顾问
							if(!StringUtils.isEmpty(parentMediator.getStr("phonenumber"))){
								String parentSmsContent = MessagePropertiesPlugin.getSmsMessageMapValue(MesContantsFinal.cs_sms_xiaji);
								parentSmsContent = parentSmsContent.replace("{counselor_name}", parentMediator.getStr("realname"));
								parentSmsContent = parentSmsContent.replace("{new_counselor_name}", newCounselorName);
								SendSMS.sendSms(parentSmsContent, parentMediator.getStr("mobile"));
							}
						}
					}
				}
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}

	public JSONObject searchById(String id) {
		JSONObject json = new JSONObject();
		String code = Constants.OPERATE_SUCCESS_CODE;
		String msg = Constants.OPERATE_SUCCESS;
		if(StringUtils.isEmpty(id)){
			code = Constants.OPERATE_FAILE_CODE;
			msg = Constants.ENTITY_IS_NULL;
		}else{
			Mediator mediator = Mediator.dao.findById(Integer.parseInt(id));
			if(mediator == null){
				code = Constants.OPERATE_FAILE_CODE;
				msg = Constants.COUNSELOR_PARENT_NOT_EXIST;
			}else{
				JSONObject data = new JSONObject();
				data.put("realname", mediator.getStr("realname"));
				data.put("mobile", mediator.getStr("mobile"));
				data.put("email", mediator.getStr("email"));
				data.put("regdate", mediator.getDate("regdate"));
				data.put("ids", mediator.getPrimaryKeyValue());
				data.put("qq", mediator.getStr("qq"));
				data.put("cardholder", mediator.getStr("cardholder"));
				data.put("bankname", mediator.getStr("bankname"));
				data.put("bankcard", mediator.getStr("bankcard"));
				json.put("data", data);
			}

		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}
}
