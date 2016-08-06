package com.momathink.common.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.momathink.common.base.BaseController;
import com.momathink.common.handler.GlobalHandler;
import com.momathink.common.tools.ToolContext;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.sys.operator.model.Operator;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysLog;
import com.momathink.sys.system.model.SysUser;

/**
 * 权限认证拦截器
 * @author rongqiang.pu
 */
public class AuthenticationInterceptor implements Interceptor {
	
	private static Logger log = Logger.getLogger(AuthenticationInterceptor.class);
	
	@Override
	public void intercept(Invocation ai) {
		BaseController contro = (BaseController) ai.getController();
		HttpServletRequest request = contro.getRequest();
		
		log.info("获取reqSysLog!");
		SysLog reqSysLog = contro.getAttr(GlobalHandler.reqSysLogKey);
		contro.setReqSysLog(reqSysLog);

		String uri = ai.getActionKey(); // 默认就是ActionKey
		if(ai.getMethodName().equals("toUrl")){
			uri = ToolContext.getParam(request, "toUrl"); // 否则就是toUrl的值
		}
		log.info(uri);
		reqSysLog.set("uri", uri);

		SysUser user = null;
		if(contro.getSysuserId()!=null){
			user = SysUser.dao.findById(contro.getSysuserId());// 当前登录用户
		}
		if(null != user){
			contro.setAttr("cUser", user);
			reqSysLog.set("userids", user.getPrimaryKeyValue());
			reqSysLog.set("username", user.getStr("real_name"));
			contro.setAttr("cUserIds", user.getPrimaryKeyValue());
		}
			Object operatorObj = Operator.dao.queryByUrl(uri);
			if(null != operatorObj){
				log.info("URI存在!");
				Operator operator = (Operator) operatorObj;
				reqSysLog.set("operatorids", operator.getPrimaryKeyValue());
				if(operator.get("privilege").equals("1")){// 是否需要权限验证
					log.info("需要权限验证!");
					reqSysLog.set("need", "1");
					if (user == null) {
						log.info("权限认证过滤器检测:未登录!");
						reqSysLog.set("status", "0");//失败
						reqSysLog.set("description", "未登录");
						reqSysLog.set("cause", "2");//2 未登录
						toInfoJsp(contro, 1);
						return;
					}else{
						if(Role.isAdmins(user.getStr("roleids"))){
							log.info("管理员不需要验证");
							reqSysLog.set("status", "1");//成功
							Date actionStartDate = ToolDateTime.getDate();//action开始时间
							reqSysLog.set("actionstartdate", ToolDateTime.getSqlTimestamp(actionStartDate));
							reqSysLog.set("actionstarttime", actionStartDate.getTime());
						}else{
							if(!ToolContext.hasPrivilegeOperator(operator, user)){// 权限验证
								log.info("权限验证失败，没有权限!");
								
								reqSysLog.set("status", "0");//失败
								reqSysLog.set("description", "没有权限!");
								reqSysLog.set("cause", "0");//没有权限
								
								toInfoJsp(contro, 2);
								return;
							}
						}
					}
				}else{
					reqSysLog.set("need", "0");
					log.info("不需要权限验证.");
				}
				log.info("权限认证成功!!!继续处理请求...");
				/*log.info("是否需要表单重复提交验证!");
				if(operator.getStr("formtoken").equals("1")){
					String tokenRequest = ToolContext.getParam(request, "formToken");
					String tokenCookie = ToolWeb.getCookieValueByName(request, "token");
					if(null == tokenRequest || tokenRequest.equals("")){
						log.info("tokenRequest为空，无需表单验证!");
					}else if(null == tokenCookie || tokenCookie.equals("") || !tokenCookie.equals(tokenRequest)){
						log.info("tokenCookie为空，或者两个值不相等，把tokenRequest放入cookie!");
						ToolWeb.addCookie(response,  "token", tokenRequest, 0);
					}else if(tokenCookie.equals(tokenRequest)){
						log.info("表单重复提交!");
						toInfoJsp(contro, 4);
						return;
						
					}else{
						log.error("表单重复提交验证异常!!!");
					}
				}*/
				log.info("权限认真成功更新日志对象属性!");
				reqSysLog.set("status", "1");//成功
				Date actionStartDate = ToolDateTime.getDate();//action开始时间
				reqSysLog.set("actionstartdate", ToolDateTime.getSqlTimestamp(actionStartDate));
				reqSysLog.set("actionstarttime", actionStartDate.getTime());
				try {
					ai.invoke();
					if(user==null){
						user = SysUser.dao.findById(contro.getSysuserId());
						if(user!=null){
							reqSysLog.set("userids", user.getPrimaryKeyValue());
							reqSysLog.set("username", user.getStr("real_name"));
							reqSysLog.set("need", "1");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("业务逻辑代码遇到异常时保存日志!");
					toInfoJsp(contro, 5);
					
				} finally {
					
				}
			}else{
				log.info("访问失败时保存日志!");
				reqSysLog.set("status", "0");//失败
				reqSysLog.set("description", "URL不存在");
				reqSysLog.set("cause", "1");//URL不存在
				log.info("URI不存在!");
				toInfoJsp(contro, 6);
				return;
			}
	}
	
	/**
	 * 提示信息展示页
	 * @param contro
	 * @param type
	 */
	private void toInfoJsp(BaseController contro, int type) {
		if(type == 1){// 未登录处理
			contro.redirect("/account/login");
			return ;
		}

		String referer = contro.getRequest().getHeader("X-Requested-With"); 
		String toPage = "/common/msgAjax.html";
		if(null == referer || referer.isEmpty()){
			toPage = "/common/msg.html";
		}
		
		String msg = null;
		if(type == 2){// 权限验证失败处理
			msg = "权限验证失败!";
			toPage = "/common/401.html";
		}else if(type == 3){// IP验证失败
			msg = "IP验证失败!";
			toPage = "/common/401.html";
		}else if(type == 4){// 表单验证失败
			msg = "请不要重复提交表单数据!";
			toPage = "/common/401.html";
		}else if(type == 5){// 业务代码异常
			msg = "业务代码异常!";
			toPage = "/common/500.html";
		}else if(type == 6){
			msg = "请求路径不存在!";
			toPage = "/common/404.html";
		}else{
			toPage = "/common/404.html";
		}
		
		contro.setAttr("referer", referer);
		contro.setAttr("msg", msg);
		contro.render(toPage);
	}
	
}
