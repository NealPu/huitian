package com.momathink.weixin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.common.tools.ToolContext;
import com.momathink.common.tools.ToolRandoms;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.mediator.service.MediatorService;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.crm.opportunity.service.OpportunityService;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.course.service.CourseplanService;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.student.service.StudentService;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.subject.service.SubjectService;
import com.momathink.weixin.model.Menu;
import com.momathink.weixin.model.User;
import com.momathink.weixin.model.Wpnumber;
import com.momathink.weixin.service.MessageService;
import com.momathink.weixin.service.UserService;
import com.momathink.weixin.tools.ToolOAuth2;
import com.momathink.weixin.tools.ToolSignature;
import com.momathink.weixin.vo.oauth.RecevieOauth2Token;
import com.momathink.weixin.vo.oauth.RecevieSNSUserInfo;

/**
 * 接收微信消息
 * @author David
 */
@Controller(controllerKey = "/weixin/message")
public class MessageController extends BaseController {

	private static Logger log = Logger.getLogger(MessageController.class);
	private MessageService messageService = new MessageService();
	private SubjectService subjectService = new SubjectService();
	private OpportunityService opportunityService = new OpportunityService();
	private StudentService studentService = new StudentService();
	private MediatorService mediatorService = new MediatorService();
	private CourseplanService coursePlanService = new CourseplanService();
	private UserService userService = new UserService();
	/**
	 * 渠道顾问助手微信
	 */
	public void index(){
		String echostr  = getPara("echostr");	//随机字符串
		String timestamp = getPara("timestamp");//时间戳
		String signature = getPara("signature");//微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
		String nonce = getPara("nonce");
		Wpnumber wpnumber = Wpnumber.dao.findByWxh((String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh));//CRM微信端，更加微信号查询公众号
		boolean flag = ToolSignature.checkSignature(signature, timestamp, nonce, wpnumber.getStr("token"));
		if(echostr != null && !echostr.isEmpty()){ // 验证URL有效性
			log.info("开发者验证");
			if(flag){
				renderText(echostr);
			}
		}else{
			if(flag){
				String recverMsg = ToolContext.requestStream(getRequest());
				log.info("接收微信发送过来的消息" + recverMsg);
				String responseMsg = messageService.messageProcess(null,wpnumber.getPrimaryKeyValue().toString(), recverMsg,wpnumber.getStr("appid"),wpnumber.getStr("appsecret"));
				log.info("返回消息" + responseMsg);
				renderText(responseMsg);
			}
		}
	}
	/**
	 * 学生查课表微信公众号
	 */
	public void xsdIndex(){
		String echostr  = getPara("echostr");	//随机字符串
		String timestamp = getPara("timestamp");//时间戳
		String signature = getPara("signature");//微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
		String nonce = getPara("nonce");
		log.info((String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_xsgzh));
		Wpnumber wpnumber = Wpnumber.dao.findByWxh((String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_xsgzh));//学生微信端，更加微信号查询公众号
		log.info(wpnumber);
		boolean flag = ToolSignature.checkSignature(signature, timestamp, nonce, wpnumber.getStr("token"));
		if(echostr != null && !echostr.isEmpty()){ // 验证URL有效性
			log.info("开发者验证");
			if(flag){
				renderText(echostr);
			}
		}else{
			if(flag){
				String recverMsg = ToolContext.requestStream(getRequest());
				log.info("接收微信发送过来的消息" + recverMsg);
				String responseMsg = messageService.messageProcess(getCxt(),wpnumber.getPrimaryKeyValue().toString(), recverMsg,wpnumber.getStr("appid"),wpnumber.getStr("appsecret"));
				log.info("返回消息" + responseMsg);
				renderText(responseMsg);
			}
		}
	}
	/**
	 * CRM微信客户端服务号授权后的回调请求处理
	 */
	public void oauth2(){
		try {
			Organization org = Organization.dao.findById(1);
			setAttr("gzh",(String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh));
			setAttr("gzhname",(String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzhname));
			setAttr("company",org.get("name").toString());
			setAttr("kfemail",org.get("email")==null?"":org.get("email").toString());
			setAttr("website",org.get("web")==null?"":org.get("web").toString());
			Wpnumber wpnumber = Wpnumber.dao.findByWxh((String)PropertiesPlugin.getParamMapValue(DictKeys.weixin_gzh));//CRM微信端，更加微信号查询公众号
			String code = getPara("code");// 用户同意授权后，能获取到/code
			String openId = "";
//		String code = "oXKgDjx_3YfysqJwpU4E8-gu95Qw";// 用户/同意授权后，能获取到code
			String state = getPara("state");// 用户同意授权后，能获取到code
		if(code!=null && code.length()>0){
			RecevieOauth2Token weixinOauth2Token = ToolOAuth2.getOauth2AccessToken(wpnumber.getStr("appid"),wpnumber.getStr("appsecret"), code);
			String accessToken = weixinOauth2Token.getAccess_token();
			openId = weixinOauth2Token.getOpenId();
			RecevieSNSUserInfo snsUserInfo = ToolOAuth2.getSNSUserInfo(accessToken, openId);
			log.info("页面授权微信用户信息：OpenId："+snsUserInfo.getOpenId()+"用户code："+code);
			User wxuser = userService.queryByOpenId(openId,wpnumber.getStr("appid"),wpnumber.getStr("appsecret"),wpnumber.getPrimaryKeyValue().toString());
			log.info("微信用户："+wxuser.getStr("nickname")+"请求访问标识："+state);
		}else{
			openId = getPara("openId");
		}
			setAttr("openid", openId);
			Mediator counselor = Mediator.dao.findByOpenId(openId);
//		Counselor counselor = Counselor.dao.findByOpenId("oXKgDjx_3YfysqJwpU4E8-gu95Qw");
			if(counselor!=null){
				if(state.contains("register")){//我的信息注册绑定
					setAttr("showinfo","searchReslutId");
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_register.jsp");
				}else if(state.contains("tuijian")){//我要推荐
					List<Subject> subjectList = subjectService.findAvailableSubject();
					List<Campus> campusList = Campus.dao.getCanUseCampusInfo(); 
					setAttr("subjectList",subjectList);
					setAttr("campusList",campusList);
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_recommend.jsp");
				}else if(state.contains("gdqk")){//跟单情况
					List<Opportunity> contacterList = opportunityService.queryContacterByCounselor(counselor);
					setAttr("contacterList",contacterList);
					setAttr("counselor",counselor);
					render("/weixin/crm_front/gendan.jsp");
				}else if(state.contains("cdqk")){//成单情况
					List<Student> studentList = studentService.queryStudentByMediator(counselor);
					setAttr("studentList",studentList);
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_chengdan.jsp");
				}else if(state.contains("tgyj")){//推广佣金
//				List<Brokerage> brokerageList = brokerageService.queryBrokerage(counselor,"2");
					setAttr("brokerageList",null);
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_yjjs.jsp");
				}else if(state.contains("tjyj")){//推荐佣金
//				List<Brokerage> brokerageList = brokerageService.queryBrokerage(counselor,"1");
					setAttr("brokerageList",null);
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_yjjs.jsp");
				}else if(state.contains("tuiguang")){//我要推广
					String inviteCode=counselor.getStr("invitecode");
					if(StringUtils.isEmpty(inviteCode)){
						inviteCode = ToolRandoms.getAuthCode(4);
						Mediator c = Mediator.dao.findByInviteCode(inviteCode);
						while(c != null){
							inviteCode = ToolRandoms.getAuthCode(4);
							c = Mediator.dao.findByInviteCode(inviteCode);
						}
						counselor.set("invitecode", inviteCode);
						counselor.update();
					}
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_tuiguang.jsp");
				}else if(state.contains("tgqk")){//推广情况
					Map<String ,Object> result = new HashMap<String,Object>();
					mediatorService.findAllSpreader(counselor, 1,result);
					counselor.put("spread", result);
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_tgqk.jsp");
				}else if(state.contains("yjfk")){//意见反馈
					setAttr("counselor",counselor);
					render("/weixin/crm_front/counselor_yjfk.jsp");
				}else if(state.contains("kcxh")){//课程消耗
					List<CoursePlan> coursePlanList = coursePlanService.queryCourseByMediatorId(counselor.getPrimaryKeyValue());
					setAttr("counselor",counselor);
					setAttr("coursePlanList",coursePlanList);
					render("/weixin/crm_front/counselor_kcxh.jsp");
				}else if(state.contains("tjgz")){//推荐规则菜单
					setAttr("counselor",counselor);
					render("/weixin/crm_front/recommend/index.jsp");
				}else if(state.contains("tggz")){//推广规则菜单
					setAttr("counselor",counselor);
					render("/weixin/crm_front/tuiguangguize/index.jsp");
				}
			}else{
				render("/weixin/crm_front/register.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void list(){
		List<Wpnumber> wpnumbers = Wpnumber.dao.getAllWpnumbers();
		List<Menu> menu = Menu.dao.getAllMenu();
		messageService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		setAttr("wpnumberList", wpnumbers);
		setAttr("menuList", menu);
		renderJsp("/weixin/message/message_list.jsp");
	}
	
	public void getEventkeyByWpnumberid() {
		JSONObject json = new JSONObject();
		String wpnumber = getPara("wpnumber");
		String sql = "select wm.`key`,wm.menuname from wx_menu wm where wm.wpnumberid= "+wpnumber+" and wm.`key` is not NULL and wm.type = 'click'";
		List<Menu> menu = Menu.dao.find(sql);
		json.put("menuname", menu);
		renderJson(json);
	}
	
}
