package com.momathink.weixin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.Constants;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.service.MediatorService;
import com.momathink.crm.opportunity.model.Feedback;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.crm.opportunity.service.OpportunityService;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.weixin.model.Wpnumber;

/**
 * 顾问管理 ClassName: CounselorController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年8月11日 上午10:43:49 <br/>
 *
 * @author David
 * @version
 * @since JDK 1.7
 */
@Controller(controllerKey = "/weixin/mediator")
public class MediatorFrontController extends BaseController {

	private static Logger log = Logger.getLogger(MediatorFrontController.class);

	private MediatorService mediatorService = new MediatorService();
	private OpportunityService opportunityService = new OpportunityService();

	/**
	 * 顾问注册
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void register() {
		Mediator mediator = new Mediator();
		mediator.set("openid", getPara("openid"));
		mediator.set("realname", getPara("realname"));
		mediator.set("phonenumber", getPara("mobile"));
		mediator.set("email", getPara("qq") + "@qq.com");
		mediator.set("qq", getPara("qq"));
		String inviteCode = getPara("invitecode");// 邀请码
		Wpnumber wpnumber = Wpnumber.dao.findByWxh((String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh));//CRM微信端，更加微信号查询公众号
		JSONObject json = mediatorService.register(mediator, inviteCode,wpnumber.getStr("appid"),wpnumber.getStr("appsecret"),wpnumber.getPrimaryKeyValue().toString());
		renderJson(json);
	}

	/**
	 * 根据ID查询顾问
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void searchByIds() {
		JSONObject json = mediatorService.searchById(getPara("ids"));
		renderJson(json);
	}

	/**
	 * 修改性别
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void changeSex() {
		Mediator mediator = Mediator.dao.findById(getPara("ids"));
		JSONObject json = new JSONObject();
		if (mediator != null) {
			mediator.set("sex", getPara("sex"));
			try {
				mediatorService.update(mediator);
				json.put("code", Constants.OPERATE_SUCCESS_CODE);
			} catch (Exception e) {
				json.put("code", Constants.OPERATE_FAILE_CODE);
				json.put("msg", Constants.OPERATE_FAILE);
			}
		} else {
			json.put("code", Constants.OPERATE_FAILE_CODE);
			json.put("msg", Constants.OPERATE_FAILE);
		}
		renderJson(json);
	}

	/**
	 * 修改变更银行账号信息
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void changeBank() {
		Mediator mediator = Mediator.dao.findById(getPara("id"));
		JSONObject json = new JSONObject();
		if (mediator != null) {
			mediator.set("bankcard", getPara("bankcard"));
			mediator.set("cardholder", getPara("cardholder"));
			mediator.set("bankname", getPara("bankname"));
			try {
				mediatorService.update(mediator);
				json.put("code", Constants.OPERATE_SUCCESS_CODE);
				json.put("msg", Constants.OPERATE_SUCCESS);
			} catch (Exception e) {
				json.put("code", Constants.OPERATE_FAILE_CODE);
				json.put("msg", Constants.OPERATE_FAILE);
			}
		} else {
			json.put("code", Constants.OPERATE_FAILE_CODE);
			json.put("msg", Constants.OPERATE_FAILE);
		}
		renderJson(json);
	}

	/**
	 * 补充信息
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void additionalInfo() {
		Opportunity opportunity = Opportunity.dao.findById(getParaToInt("contacterIds"));
		JSONObject json = new JSONObject();
		if (opportunity != null) {
			Feedback feedback = new Feedback();
			Mediator mediator = Mediator.dao.findById(opportunity.getInt("mediatorid"));
			feedback.set("mediatorid", mediator.getPrimaryKeyValue());
			feedback.set("content", getPara("content"));
			try {
				opportunityService.SaveFollowInfo(opportunity,feedback);
				json.put("code", Constants.OPERATE_SUCCESS_CODE);
				json.put("url", "/weixin/mediator/queryGendanDetail?mediatorid=" + mediator.getPrimaryKeyValue());
				SysUser kcuser = SysUser.dao.findById(opportunity.getInt("kcuserid"));
				// 发短信给课程顾问
				if (!StringUtils.isEmpty(kcuser.getStr("tel"))) {
					MessageService.sendMessageToAdvisor(MesContantsFinal.kc_sms_buchong, MesContantsFinal.kc_email_buchong, opportunity.getPrimaryKeyValue().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", Constants.OPERATE_FAILE_CODE);
				json.put("msg", Constants.OPERATE_FAILE);
			}
		} else {
			json.put("code", Constants.OPERATE_FAILE_CODE);
			json.put("msg", Constants.OPERATE_FAILE);
		}
		renderJson(json);
	}

	public void referrals() {
		log.debug("推荐学生");
		Map<String, String> param = new HashMap<String, String>();
		param.put("openid", getPara("openid"));
		param.put("contactername", getPara("contactername"));
		param.put("phonenumber", getPara("phonenumber"));
		param.put("subjectid", getPara("subjectid"));
		param.put("relation", getPara("relation"));
		param.put("needcalled", getPara("needcalled"));
		param.put("campus", getPara("campus"));
		param.put("note", getPara("note"));
		JSONObject json = opportunityService.save(param);
		renderJson(json);
	}

	public void brokerageDetail() {
		JSONObject json = new JSONObject();
//		List<BrokerageDetail> list = BrokerageDetail.dao.findByBrokerageIds(brokerageIds);
//		for (BrokerageDetail b : list) {
//			String brokerageType = b.getStr("brokeragetype");
//			if ("1".equals(brokerageType)) {
//				continue;
//			} else {
//				String studentName = b.getStr("studentname");
//				b.set("studentname", studentName.substring(0, 1) + "同学");
//			}
//		}
		json.put("code", Constants.OPERATE_SUCCESS_CODE);
		json.put("data", list);
		renderJson(json);
	}

	public void queryGendanDetail() {
		Mediator mediator = Mediator.dao.findById(getPara("mediatorid"));
		List<Opportunity> contacterList = opportunityService.queryContacterByCounselor(mediator);
		setAttr("contacterList", contacterList);
		setAttr("mediator", mediator);
		render("/weixin/crm_front/counselor_gendan.jsp");
	}

	public void showFollows() {
		Opportunity opportunity = Opportunity.dao.findById(Integer.parseInt(getPara("id")));
		List<Feedback> list = Feedback.dao.findByOpportunityId(opportunity.getPrimaryKeyValue().toString());
		JSONObject json = new JSONObject();
		if (list == null) {
			json.put("code", "1");
		} else {
			json.put("code", "0");
			json.put("title",ToolDateTime.format(opportunity.getTimestamp("createtime"), ToolDateTime.pattern_ymd) + opportunity.getStr("contacter") + "咨询" + Subject.dao.getSubjectNameByIds(opportunity.getStr("subjectids")));
			json.put("id", opportunity.getInt("id"));
			json.put("name", opportunity.getStr("contacter"));
			json.put("subjectname",  Subject.dao.getSubjectNameByIds(opportunity.getStr("subjectids")));
			json.put("data", list);
		}
		renderJson(json);
	}
	
	/**
	 * 学生推荐学生获取积分
	 */
	public void studentRecommend() {
		Map<String, String> param = new HashMap<String, String>();
		JSONObject json = new JSONObject();
		json.put("code", Constants.OPERATE_FAILE_CODE);
		String myname=getPara("myname");
		String myphone=getPara("myphone");
		String stuname=getPara("stuname");
		String stuphone=getPara("stuphone");
		String subjectid=getPara("subjectid");
		String remark=getPara("remark");
		if(StringUtils.isEmpty(myphone)){
			json.put("msg", Constants.COUNSELOR_MOBILE_IS_NULL);
		}else{
			if(StringUtils.isEmpty(stuphone)){
				json.put("msg", Constants.COUNSELOR_MOBILE_IS_NULL);
			}else{
				if(StringUtils.isEmpty(subjectid)){
					json.put("msg", Constants.SUBJECT_IS_NULL);
				}else{
					if(StringUtils.isEmpty(stuname)||StringUtils.isEmpty(myname)){
						json.put("msg", Constants.COUNSELOR_NAME_IS_NULL);	
					}else{
						param.put("recommendphone", myphone);
						param.put("recommendusername", myname);
						param.put("openid", "0");
						param.put("contactername", stuname);
						param.put("phonenumber", stuphone);
						param.put("subjectid", subjectid);
						param.put("relation", "1");
						param.put("needcalled", "1");
						param.put("note", remark);
						json = opportunityService.save(param);
					}
				}
			}
		}
		renderJson(json);
	}
	
	/**
	 * 推荐积分
	 */
	public void zanjifen(){
		List<Subject> list = Subject.dao.getSubject();
		setAttr("subjectList", list);
		renderJsp("/weixin/counselor_tjtx.jsp");
	}
}
