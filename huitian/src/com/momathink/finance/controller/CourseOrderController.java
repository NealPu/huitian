package com.momathink.finance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.Constants;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMail;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.finance.model.CourseOrder;
import com.momathink.finance.model.CoursePrice;
import com.momathink.finance.model.OrdersReject;
import com.momathink.finance.model.Payment;
import com.momathink.finance.model.Sort;
import com.momathink.finance.service.CourseOrderService;
import com.momathink.finance.service.CoursePriceService;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.account.model.BanciCourse;
import com.momathink.sys.account.model.UserCourse;
import com.momathink.sys.account.service.AccountService;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.subject.model.Subject;

/**
 * 订单管理
 * 
 * @author David
 */
@Controller(controllerKey = "/orders")
public class CourseOrderController extends BaseController {
	private static final Logger logger = Logger.getLogger(CourseOrderController.class);
	private CourseOrderService courseOrderService = new CourseOrderService();
	private CoursePriceService coursePriceService = new CoursePriceService();
	private AccountService accountService = new AccountService();	
	/**
	 * 交费管理*
	 */
	public void index(){
		Integer sysuserId = getSysuserId();
		String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
		if (campusids != null) {
		String campusSql = " and s.campusid IN("+campusids+")";
		Map<String,String> queryParam = splitPage.getQueryParam();
			queryParam.put("campusSql",campusSql);
		}

		Record r = courseOrderService.queryOrderList(splitPage);
		setAttr("showPages", splitPage.getPage());
		setAttr("record",r);
		render("/finance/order_list.jsp");
	}
	/**
	 * 导出列表
	 */
	public void toExcel(){
		Integer sysuserId = getSysuserId();
		String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
		Map<String,String> queryParam = splitPage.getQueryParam();
		splitPage.setPageNumber(1);
		splitPage.setPageSize(10000000);
		if (campusids != null) {
			String campusSql = " and s.campusid IN("+campusids+")";
			queryParam.put("campusSql",campusSql);
			if(null==queryParam.get("type")||queryParam.get("type").equals("")){
				queryParam.put("type","1");
			}
		}
		courseOrderService.export(getResponse(), getRequest(), splitPage,"交费信息");	
		renderNull();
	}
	/*
	 * 订单审核*
	 */
	public void orderreview(){
		Integer sysuserId = getSysuserId();
		String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
		if (campusids != null) {
		String campusSql = " and s.campusid IN("+campusids+")";
		Map<String,String> queryParam = splitPage.getQueryParam();
			queryParam.put("campusSql",campusSql);
		}
		courseOrderService.checkorder(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/ordercheck_list.jsp");
	}
	public void save(){
		CourseOrder courseOrder = getModel(CourseOrder.class);
		JSONObject json = new JSONObject();
		String code="0";
		String msg="确认成功！";
		Student student = Student.dao.findById(courseOrder.getInt("studentid"));
		if(student == null){
			msg="学生不存在！";
		}else{
			Integer teachType = courseOrder.getInt("teachtype");
			String subjectids = getPara("subjectids");
			if(subjectids==null&&"1".equals(teachType.toString())){
				msg="科目不能为空";
			}else{
				Double classhour = Double.parseDouble(getPara("courseOrder.classhour"));
				if(classhour == 0){
					msg="课时数不能为0";
				}else{
					Organization o = Organization.dao.findById(1);
					if(o.getInt("basic_audithourmaxnumber")>classhour){
						courseOrder.set("needcheck", 0).set("checkstatus",1);
						if(BigDecimal.ZERO.equals(courseOrder.getBigDecimal("realsum")))
							courseOrder.set("status", 1);
						else
							courseOrder.set("status", 0);
					}else{
						courseOrder.set("needcheck", 1);
						courseOrder.set("status", 0);
					}
					courseOrder.set("operatorid",getSysuserId());
					courseOrder.set("createuserid", getSysuserId());
					courseOrder.set("remainclasshour", courseOrder.getDouble("classhour"));
					long ordernum = System.currentTimeMillis();
					courseOrder.set("ordernum",ordernum);
					courseOrder.set("subjectids",subjectids);
					courseOrderService.save(courseOrder);
					Integer orderId = courseOrder.getPrimaryKeyValue();
					if("1".equals(teachType.toString())){//1对1
						String courseIds[] = getPara("courseids").split("\\|");
						if(Integer.compare(0, courseOrder.getInt("needcheck"))==0){//不需要审核
							for(String courseId :courseIds){
								UserCourse userCourse = UserCourse.dao.findByStudentIdAndCourseId(student.getPrimaryKeyValue(),Integer.parseInt(courseId));
								if(userCourse == null){
									UserCourse ab = new UserCourse();
									ab.set("account_id", courseOrder.getInt("studentid")).set("course_id", courseId).save();
								}
							}
						}
						for(int i=0 ;i<courseIds.length;i++){
							CoursePrice coursePrice = new CoursePrice();
							coursePrice.set("studentid", courseOrder.getInt("studentid"));
							coursePrice.set("unitprice", courseOrder.getDouble("avgprice"));
							coursePrice.set("realprice", courseOrder.getDouble("avgprice"));
							coursePrice.set("classhour", 0);
							coursePrice.set("remainclasshour", 0);
							coursePrice.set("courseid", Integer.parseInt(courseIds[i]));
							coursePrice.set("orderid", orderId);
							coursePrice.set("operatorid",courseOrder.getInt("operatorid"));
							coursePriceService.save(coursePrice);
						}
					} else if ("2".equals(teachType.toString())){//班课
						String courseIds[] = getParaValues("course_id");
						String courseHours[] = getParaValues("keshi");
						for(int i=0 ;i<courseIds.length;i++){
							String courseHour = courseHours[i];
							if(StringUtils.isEmpty(courseHour))
								continue;
							CoursePrice coursePrice = new CoursePrice();
							coursePrice.set("studentid", courseOrder.getInt("studentid"));
							coursePrice.set("unitprice", courseOrder.getDouble("avgprice"));
							coursePrice.set("realprice", courseOrder.getDouble("avgprice"));
							coursePrice.set("classhour", courseHour);
							coursePrice.set("remainclasshour", courseHour);
							coursePrice.set("courseid", courseIds[i]);
							coursePrice.set("orderid", orderId);
							coursePrice.set("operatorid",courseOrder.getInt("operatorid"));
							coursePriceService.save(coursePrice);
						}
					}
					SysUser sysuser = SysUser.dao.findById(getSysuserId());
					Campus campus = Campus.dao.findById(sysuser.getInt("campusid"));
					if(campus!=null&&courseOrder.getBigDecimal("realsum").compareTo(BigDecimal.ZERO)==1){
						MessageService.sendMessageToFinance(MesContantsFinal.apply_sms, MesContantsFinal.apply_email, courseOrder.getInt("studentid").toString(), campus.getInt("presidentid").toString(), getSysuserId().toString());
					}
					code="1";
					msg="订单提交成功";
				}
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	/*
	 *	/order/checkEnoughCoursehours
	 *  按课程排课课时校验(粗略校验)
	 */
	public void checkEnoughCoursehours(){
		try{
			JSONObject json = new JSONObject();
			String code="0";
			String studentId = getPara("studentId");
			String plantype = getPara("plantype");
			if(plantype.equals("1")){
				logger.info("模考，不需要判断时间是否够用");
				code="0";
			}else{
				Student student = Student.dao.findById(Integer.parseInt(studentId));
				if(student.getInt("state")==2){
					logger.info("虚拟用户，查询小班课时是否够用");
					ClassOrder classorder= ClassOrder.dao.findByXuniId(Integer.parseInt(studentId));
					int zks = classorder.getInt("lessonNum");
					float ypks =  CoursePlan.coursePlan.getClassYpkcClasshour(classorder.getPrimaryKeyValue());
					double syks = ToolArith.sub(zks, ypks);//剩余课时
					int chargeType = classorder.getInt("chargeType");
					if(chargeType == 1 && syks <= 0){// 按期
						code = "1";
						json.put("msg",student.getStr("real_name")+"的课时不足，请购买课时。");
					}
				}else{
					Course course = Course.dao.findById(getParaToInt("courseId"));
					Account account = Account.dao.findById(Integer.parseInt(studentId));
					Integer subjectId = course.getInt("subject_id");
					double yjfks = CourseOrder.dao.getPaidVIPzks(Integer.valueOf(studentId),subjectId);
					double zks = CourseOrder.dao.getCanUseVIPzks(student.getPrimaryKeyValue(),subjectId);
					double ypks = CoursePlan.coursePlan.getUsedVIPClasshour(Integer.parseInt(studentId), subjectId);
					double syks = ToolArith.sub(zks, ypks);//剩余课时
					Organization org = Organization.dao.findById(1);
					String keqianStr = org.get("basic_maxdefaultclass").toString();
					double keqian = 10000000;
					if(!StringUtils.isEmpty(keqianStr)){
						keqian = Double.parseDouble(keqianStr);
					}
					if (syks >= 0) {
						if (ypks > yjfks +keqian) {
							code = "1";
							json.put("msg", account.getStr("real_name") + "同学含有欠费订单，交款后继续排课。");
						}else{
							if(0 > syks) {
								code = "1";
								json.put("msg", account.getStr("real_name") + "剩余" + syks + "课时,请先进行其他相应操作再排课.");
							}
						}
					} else {
						code = "1";
						json.put("msg", account.getStr("real_name") + "课时不足，请确认是否已交费或已购买。");
					}
				}		
			}
			logger.info("code为0课时足够，为1课时不够;一对一可以排超指定课时，小班没有排超.");
			json.put("code", code);
			renderJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * 查询预排课时是否足够
	 */
	public void queryUsableHour(){
		try{
			JSONObject json = new JSONObject();
			String code="0";
			String studentId = getPara("studentId");
			String rankId = getPara("rankId");
			String rankName = getPara("rankName");
			String plantype = getPara("plantype");
			rankName = rankName.substring(0, 5);
			if(plantype.equals("1")){//模考
				code="0";
			}else{//正常排课，走验证课时是否够用
				Student student = Student.dao.findById(Integer.parseInt(studentId));
				if(student.getInt("state")==2){//虚拟用户，查询小班课时是否够用
					ClassOrder classorder= ClassOrder.dao.findByXuniId(Integer.parseInt(studentId));
					TimeRank timeRank = TimeRank.dao.findById(Integer.parseInt(rankId));
					int zks = classorder.getInt("lessonNum");
					int chargeType = classorder.getInt("chargeType");
					if(chargeType == 1){// 按期
						float ypks =  CoursePlan.coursePlan.getClassYpkcClasshour(classorder.getPrimaryKeyValue());
						double syks = ToolArith.sub(zks, ypks);//剩余课时
						if(syks>0){
							if (timeRank.getBigDecimal("class_hour").doubleValue()>syks) {
								code = "1";
								json.put("msg",student.getStr("real_name")+"剩余"+syks+"课时,该时段课时为"+timeRank.getBigDecimal("class_hour")+"课时");
							}
						}else{
							code = "1";
							json.put("msg",student.getStr("real_name")+"的课时不足，请购买课时。");
						}			
					}
				}else{
					Course course = Course.dao.findById(getParaToInt("courseId"));
					Integer subjectId = course.getInt("subject_id");
					Account account = Account.dao.findById(Integer.parseInt(studentId));
					TimeRank timeRank = TimeRank.dao.findById(Integer.parseInt(rankId));
					double yjfks = CourseOrder.dao.getPaidVIPzks(Integer.valueOf(studentId),subjectId);
					double zks = CourseOrder.dao.getCanUseVIPzks(student.getPrimaryKeyValue(),subjectId);
					double ypks = CoursePlan.coursePlan.getUsedVIPClasshour(Integer.parseInt(studentId), subjectId);
					double syks = ToolArith.sub(zks, ypks);//剩余课时
					Organization org = Organization.dao.findById(1);
					String keqianStr = org.get("basic_maxdefaultclass").toString();
					double keqian = 10000000;
					if(!StringUtils.isEmpty(keqianStr)){
						keqian = Double.parseDouble(keqianStr);
					}
					if (syks > 0) {
						if (ypks + timeRank.getBigDecimal("class_hour").doubleValue() > yjfks +keqian) {
							code = "1";
							json.put("msg", account.getStr("real_name") + "同学含有欠费订单，交款后继续排课。");
						}else{
							if(timeRank.getBigDecimal("class_hour").doubleValue() > syks) {
								code = "1";
								json.put("msg", account.getStr("real_name") + "剩余" + syks + "课时,该时段课时为" + timeRank.getBigDecimal("class_hour") + "课时");
							}
						}
					} else {
						code = "1";
						json.put("msg", account.getStr("real_name") + "课时不足，请确认是否已交费或已购买。");
					}
				}		
			}
			json.put("code", code);
			renderJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 购买课程
	 */
	public void goBuyCoursePage(){
		try{
		String studentId = getPara();
		if(StringUtils.isEmpty(studentId)){
			logger.info("学生ID为空");
		}else{
			Student student = Student.dao.findById(Integer.parseInt(studentId));
			if(student == null){
				logger.info("学生不存在");	
			}else{
				List<Subject> subjectList = Subject.dao.getSubject();
				Opportunity opportunity = Opportunity.dao.findById(student.getInt("opportunityid"));
				if(opportunity!=null){
					String subjectIds = opportunity.getStr("subjectids");
					if(!StringUtils.isEmpty(subjectIds)){
						String subIds[] = subjectIds.split("\\|");
						for(Subject subject : subjectList){
							String _sid = subject.getPrimaryKeyValue().toString();
							for(String sid : subIds){
								if(_sid.equals(sid)){
									subject.put("checked", "checked");
								}
							}
						}
					}
				}
				List<ClassOrder> classList = ClassOrder.dao.findCanBuy(studentId);
				setAttr("student", student);
				setAttr("banciList", classList);
				setAttr("subjectList", subjectList);
				setAttr("operateType","buy");
				renderJsp("/finance/course_buypage.jsp");
			}
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void edit(){
		try{
			String orderId = getPara();
			if(StringUtils.isEmpty(orderId)){
				logger.info("订单ID为空");
			}else{
				CourseOrder courseOrder = CourseOrder.dao.findById(Integer.parseInt(orderId));
				if(courseOrder == null){
					logger.info("订单不存在");	
				}else{
					Integer teachType = courseOrder.getInt("teachtype");
					if(teachType!=null&&teachType.equals(1)){
						List<Subject> subjectList = Subject.dao.getSubject();
						String subjectIds = courseOrder.getStr("subjectids");
						double canUseHour = CourseOrder.dao.getCanUseVIPzksByCourseOrderId(Integer.parseInt(orderId));
						long payCount = Payment.dao.getPayCount(Integer.parseInt(orderId));
						if(!StringUtils.isEmpty(subjectIds)){
							String subIds[] = subjectIds.split("\\|");
							for(Subject subject : subjectList){
								String _sid = subject.getPrimaryKeyValue().toString();
								for(String sid : subIds){
									boolean isuse = CoursePlan.coursePlan.checkSubjectIsUse(courseOrder.getInt("studentid"),Integer.parseInt(sid));
									if(_sid.equals(sid)){
										subject.put("checked", "checked");
									}
									if(isuse){
										subject.put("checked", "checked");
										subject.put("isuse", isuse);
									}
								}
							}
						}
						setAttr("payCount",payCount);
						setAttr("canUseHour",canUseHour);
						setAttr("subjectList", subjectList);
						setAttr("courseList",Course.dao.findBySubjectIds(subjectIds.replace("|", ",")));
						setAttr("studentCourseIds",CoursePrice.dao.getCourseids(courseOrder.getPrimaryKeyValue()));
					}else{
						int classOrderId = courseOrder.getInt("classorderid");
						List<ClassOrder> classList = new ArrayList<ClassOrder>();
						classList.add(ClassOrder.dao.findById(classOrderId));
						setAttr("banciList", classList);
						List<BanciCourse> banciCourseList = BanciCourse.dao.findByClassOrderId(classOrderId);
						List<CoursePrice> coursePriceList = CoursePrice.dao.findByOrderId(orderId);
						for(BanciCourse bc : banciCourseList){
							for(CoursePrice cp: coursePriceList ){
								if(bc.getInt("course_id").equals(cp.getInt("courseid"))){
									bc.put("oldClassHour", cp.getDouble("classhour"));
									break;
								}
							}
						}
						setAttr("banciCourseList",banciCourseList);
					}
					setAttr("student", Student.dao.findById(courseOrder.getInt("studentid")));
					setAttr("courseOrder",courseOrder);
					setAttr("operateType","modify");
					renderJsp("/finance/course_buypage.jsp");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 保存课程订单
	 */
	public void saveCourseOrder(){
		CourseOrder courseOrder = getModel(CourseOrder.class);
		JSONObject json = new JSONObject();
		String code="0";
		String msg="确认成功！";
		Student student = Student.dao.findById(courseOrder.getInt("studentid"));
		if(student == null){
			msg="学生不存在！";
		}else{
			String useYucun = getPara("useYucun");
			courseOrder.set("operatorid",getSysuserId());
			courseOrder.set("createuserid", getSysuserId());
			courseOrder.set("remainclasshour", courseOrder.getDouble("classhour"));
			if(courseOrder.getBigDecimal("rebate").compareTo(BigDecimal.ZERO)==0){
				courseOrder.set("needcheck", 0);
			}else{
				courseOrder.set("needcheck", 1);
			}
			
			if("1".equals(useYucun)){
				double yucunBalance = Double.parseDouble(getPara("yck"));
				double yucunkuan = Double.parseDouble(getPara("yck_hidden"));
				double useyucunAmount = ToolArith.sub(yucunkuan, yucunBalance);
				double newyucun = ToolArith.add(student.getBigDecimal("temprealbalance").doubleValue(), student.getBigDecimal("temprewardbalance").doubleValue());
				if(useyucunAmount>newyucun){
					json.put("code", code);
					json.put("msg", "预存款金额不足！");
					renderJson(json);
				}else{
					if(courseOrder.getBigDecimal("realsum").compareTo(BigDecimal.ZERO)==0&&courseOrder.getBigDecimal("rebate").compareTo(BigDecimal.ZERO)==0){
						courseOrder.set("status", 1);
					}
				}
			}
			
			courseOrderService.save(courseOrder);
			Integer orderId = courseOrder.getPrimaryKeyValue();
			List<Course> clist = Course.dao.findBySubjectId(courseOrder.getInt("subjectid"));
			for(Course c : clist){
				double ks = getPara("ks_"+c.getPrimaryKeyValue())==null?0:Double.parseDouble(getPara("ks_"+c.getPrimaryKeyValue()));
				if(ks==0){
					continue;
				}
				CoursePrice cp = new CoursePrice();
				cp.set("studentid", courseOrder.getInt("studentid"));
				cp.set("subjectid", courseOrder.getInt("subjectid"));
				cp.set("unitprice", c.getInt("unit_price"));
				cp.set("realprice", c.getInt("unit_price"));
				cp.set("classhour", ks);
				cp.set("remainclasshour", ks);
				cp.set("courseid", c.getPrimaryKeyValue());
				cp.set("orderid", orderId);
				cp.set("operatorid",courseOrder.getInt("operatorid"));
				coursePriceService.save(cp);
			}
			SysUser sysuser = SysUser.dao.findById(getSysuserId());
			Campus campus = Campus.dao.findById(sysuser.getInt("campusid"));
			if(campus!=null){
				MessageService.sendMessageToFinance(MesContantsFinal.apply_sms, MesContantsFinal.apply_email, courseOrder.getInt("studentid").toString(), campus.getInt("presidentid").toString(), getSysuserId().toString());
			}
			
			if("1".equals(useYucun)){
				double yucunBalance = Double.parseDouble(getPara("yck"));
				double yucunkuan = Double.parseDouble(getPara("yck_hidden"));
				double useyucunAmount = ToolArith.sub(yucunkuan, yucunBalance);
				Map<String, String> map = accountService.accountManage(Constants.ACCOUNT_ZHUANRU, courseOrder.getInt("studentid").toString(), null, orderId.toString(), getSysuserId().toString(), useyucunAmount, null, null);
				code=map.get("code");
				msg=map.get("msg");
			}else{
				code="1";
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	
	/*
	 * 未审核过的订单审核
	 */
	public void orderFirstReviews(){
		String orderid = getPara();
		CourseOrder co = CourseOrder.dao.findById(Integer.parseInt(orderid));
		String teachtype = co.getInt("teachtype").toString();
		if(teachtype.equals("1")){
			co.put("subjectname", Subject.dao.getSubjectNameByIds(co.getStr("subjectids")));
		}
		setAttr("orders", co);
		if(co.getInt("checkstatus")==2){
			List<OrdersReject> orj = OrdersReject.dao.getOrdersRejectsByOrderId(orderid);
			setAttr("reject", orj);
		}
		render("/finance/order_firstreviews.jsp");
	
	}
	
	public synchronized void ordersPassed(){
		try{
			String orderId = getPara("orderId");
			Record loginAccount = getSessionAttr("account_session");// 当前登陆用户
			String loginUserId = loginAccount.getInt("ID").toString();
			CourseOrder co = CourseOrder.dao.findById(orderId);
			double realsum = co.getBigDecimal("realsum")==null?0:co.getBigDecimal("realsum").doubleValue();
			co.set("checkstatus", 1);
			co.set("checktime", ToolDateTime.getDate());
			co.set("checkuserid", loginUserId);
			if(realsum == 0){
				co.set("status", 1);
				co.set("paiedtime", new Date());
				co.set("operatorid", getSysuserId());
				co.set("version", co.getInt("version") + 1);
			}
			courseOrderService.update(co);
			List<CoursePrice> coursePriceList = CoursePrice.dao.findByOrderId(orderId);
			for(CoursePrice coursePrice : coursePriceList){
				UserCourse ab = new UserCourse();
				ab.set("account_id", co.getInt("studentid")).set("course_id", coursePrice.getInt("courseid")).save();
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("msg", "操作已成功.");
			map.put("face", 9);
			MessageService.sendMessageToFinance(MesContantsFinal.apply_sms_pass, MesContantsFinal.apply_email_pass, co.getInt("studentid").toString(), getSysuserId().toString(),co.getInt("operatorid").toString());
			renderJson(map);
		}catch(Exception ex){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("msg", "操作出现异常，请联系管理员.");
			map.put("face", 8);
			renderJson(map);
			ex.printStackTrace();
		}
	}
	
	public synchronized void ordersReject(){
		try{
			String orderId = getPara("orderId");
			Record loginAccount = getSessionAttr("account_session");// 当前登陆用户
			String loginUserId = loginAccount.getInt("ID").toString();
			CourseOrder co = CourseOrder.dao.findById(orderId);
			co.set("checkstatus", 2);
			co.set("checktime", ToolDateTime.getDate());
			co.set("checkuserid", loginUserId);
			co.update();
			/*OrdersReject ore = new OrdersReject();*/
			OrdersReject ore = getModel(OrdersReject.class);
			ore.set("rejecttime", ToolDateTime.getDate());
			ore.set("operatorid", loginUserId);
			ore.set("operatorname", loginAccount.getStr("REAL_NAME").toString());
			ore.set("orderid", orderId);
			ore.set("reason", getPara("remark"));
			ore.save();
			MessageService.sendMessageToFinance(MesContantsFinal.apply_sms_refuse, MesContantsFinal.apply_email_refuse, co.getInt("studentid").toString(), getSysuserId().toString(), co.getInt("operatorid").toString());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("msg", "操作已成功.");
			map.put("face", 9);
			renderJson(map);
		}catch(Exception ex){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("msg", "操作出现异常，请联系管理员.");
			map.put("face", 8);
			renderJson(map);
			ex.printStackTrace();
		}
	}
	
	public void showOrderReviews(){
		try{
			String orderid = getPara();
			CourseOrder courseOrder = CourseOrder.dao.findById(Integer.parseInt(orderid));
			List<CoursePrice> cplist = CoursePrice.dao.findByOrderId(orderid);
			String teachtype = courseOrder.getInt("teachtype").toString();
			boolean sameprice = true;
			if(teachtype.equals("1")){
				List<Course> courses = Course.dao.findBySubjectId(courseOrder.getInt("subjectid"));
				if(courses.size()>0){
					int price=courses.get(0).getInt("UNIT_PRICE");
					courseOrder.put("price", price);
					for(Course course:courses){
						int _price = course.getInt("UNIT_PRICE")==null?0:course.getInt("UNIT_PRICE");
						if(price !=_price){
							sameprice = false;
							break;
						}
					}
				}
				setAttr("cplist", cplist);
			}
			courseOrder.put("sameprice", sameprice);
			courseOrder.put("coursePirceList", CoursePrice.dao.findByOrderId(orderid));
			setAttr("orders", courseOrder);
			if(courseOrder.getInt("checkstatus")==1){
				List<OrdersReject> orj = OrdersReject.dao.getOrdersRejectsByOrderId(orderid);
				if(orj==null){
					setAttr("msg",0);
				}else{
					setAttr("reject", orj);
					setAttr("msg",1);
				}
			}else{
				List<OrdersReject> orj = OrdersReject.dao.getOrdersRejectsByOrderId(orderid);
				setAttr("reject", orj);
			}
			render("/finance/payment_list.jsp");
	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public synchronized void delOrder(){
		JSONObject json = new JSONObject();
		try{
			String orderId = getPara("orderId");
			String reason = getPara("reason").replace(" ", "");
			CourseOrder co = CourseOrder.dao.findById(orderId);
			if(StringUtils.isEmpty(reason)){
				json.put("msg", "请填写取消订单原因！");
				json.put("code", 0);	
			}else{
				co.set("delflag", 1);
				co.set("deltime", ToolDateTime.getDate());
				co.set("deluserid", getSysuserId());
				co.set("delreason", reason);
				co.update();
				json.put("msg", "订单取消成功！");
				json.put("code", 1);
			}
		
		}catch(Exception ex){
			json.put("msg", "操作出现异常，请联系管理员.");
			json.put("code", 0);
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	/**
	 * 调课
	 */
	public void tiaoke(){
		try{
			String orderId = getPara();
			if(StringUtils.isEmpty(orderId)){
				logger.info("订单ID为空");
			}else{
				CourseOrder courseOrder = CourseOrder.dao.findById(Integer.parseInt(orderId));
				if(courseOrder == null){
					logger.info("订单不存在");	
				}else{
					Integer teachType = courseOrder.getInt("teachtype");
					if(teachType!=null&&teachType.equals(1)){
						List<Subject> subjectList = Subject.dao.getSubject();
						String subjectIds = courseOrder.getStr("subjectids");
						if(!StringUtils.isEmpty(subjectIds)){
							if(subjectIds.substring(0,1).equals("|")){
								subjectIds = subjectIds.substring(1);
							}
							String subIds[] = subjectIds.split("\\|");
							for(Subject subject : subjectList){
								String _sid = subject.getPrimaryKeyValue().toString();
								for(String sid : subIds){
									boolean isuse = CoursePlan.coursePlan.checkSubjectIsUse(courseOrder.getInt("studentid"),Integer.parseInt(sid));
									if(_sid.equals(sid)){
										subject.put("checked", "checked");
									}
									if(isuse){
										subject.put("checked", "checked");
										subject.put("isuse", isuse);
									}
								}
							}
						}
						setAttr("subjectList", subjectList);
					}else{
						List<ClassOrder> classList = new ArrayList<ClassOrder>();
						classList.add(ClassOrder.dao.findById(courseOrder.getInt("classorderid")));
						setAttr("banciList", classList);
					}
					setAttr("student", Student.dao.findById(courseOrder.getInt("studentid")));
					setAttr("courseOrder",courseOrder);
					setAttr("operateType","tiaoke");
					renderJsp("/finance/course_buypage.jsp");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 修改订单
	 */
	public void modify(){
		try{
			String orderId = getPara();
			if(StringUtils.isEmpty(orderId)){
				logger.info("订单ID为空");
				setAttr("msg", "订单不存在");
			}else{
				CourseOrder courseOrder = CourseOrder.dao.findById(Integer.parseInt(orderId));
				if(courseOrder == null){
					logger.info("订单不存在");
					setAttr("msg", "订单不存在");
				}else{
					Subject subject = courseOrder.getInt("studentid")==null?null:Subject.dao.findById(courseOrder.getInt("subjectid"));
					if(subject == null){
						logger.info("科目不存在");
						setAttr("msg", "科目不存在");
					}else{
						Student student = Student.dao.findById(courseOrder.getInt("studentid"));
						if(student == null){
							logger.info("学生不存在");
							setAttr("msg", "学生不存在");
						}else{
							List<CoursePrice> coursePriceList = CoursePrice.dao.findByOrderId(orderId);
							setAttr("student", student);
							setAttr("subject", subject);
							setAttr("courseOrder", courseOrder);
							setAttr("type","modify");
							setAttr("coursePriceList", coursePriceList);
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			setAttr("msg", "系统异常");
		}
		renderJsp("/finance/layer_modifyorder.jsp");
	}
	
	public void update(){
		CourseOrder courseOrder = getModel(CourseOrder.class);
		Integer orderId = courseOrder.getPrimaryKeyValue();
		int studentId = courseOrder.getInt("studentid");
		JSONObject json = new JSONObject();
		String code="0";
		String msg="确认成功！";
		Student student = Student.dao.findById(studentId);
		if(student == null){
			msg="学生不存在！";
		}else{
			Integer teachType = courseOrder.getInt("teachtype");
			String subjectids = getPara("subjectids");
			String courseids = getPara("courseids");
			if(subjectids==null&&"1".equals(teachType.toString())){
				msg="科目不能为空";
			}else{
				Double classhour = Double.parseDouble(getPara("courseOrder.classhour"));
				Organization o = Organization.dao.findById(1);
				long paidCount = Payment.dao.getPayCount(orderId);
				if(paidCount==0){
					if(o.getInt("basic_audithourmaxnumber")>classhour){
						courseOrder.set("needcheck", 0).set("checkstatus",1);
						if(BigDecimal.ZERO.equals(courseOrder.getBigDecimal("realsum")))
							courseOrder.set("status", 1);
						else
							courseOrder.set("status", 0);
					}else{
						courseOrder.set("needcheck", 1);
						courseOrder.set("status", 0);
					}
				}
				courseOrder.set("remainclasshour",courseOrder.getDouble("classhour"));
				double avgprice = ToolArith.div(courseOrder.getBigDecimal("realsum").doubleValue(), courseOrder.getDouble("classhour"), 2);
				courseOrder.set("avgprice", avgprice);
				courseOrder.set("subjectids",subjectids);
				courseOrderService.update(courseOrder);
				CoursePrice.dao.deleteByOrderId(orderId);
				if("1".equals(teachType.toString())){//1对1
					String[] courseIds = courseids.split("\\|");
					if(paidCount>0||Integer.compare(0, courseOrder.getInt("needcheck"))==0){//不需要审核
						for(String courseId :courseIds){
							UserCourse userCourse = UserCourse.dao.findByStudentIdAndCourseId(student.getPrimaryKeyValue(),Integer.parseInt(courseId));
							if(userCourse == null){
								UserCourse ab = new UserCourse();
								ab.set("account_id", courseOrder.getInt("studentid")).set("course_id", courseId).save();
							}
						}
					}
					for(int i=0 ;i<courseIds.length;i++){
						CoursePrice coursePrice = new CoursePrice();
						coursePrice.set("studentid", courseOrder.getInt("studentid"));
						coursePrice.set("unitprice", courseOrder.getDouble("avgprice"));
						coursePrice.set("realprice", courseOrder.getDouble("avgprice"));
						coursePrice.set("classhour", 0);
						coursePrice.set("remainclasshour", 0);
						coursePrice.set("courseid", Integer.parseInt(courseIds[i]));
						coursePrice.set("orderid", orderId);
						coursePrice.set("operatorid",getSysuserId());
						coursePriceService.save(coursePrice);
					}
				}else{
					courseOrder.set("subjectids",0);
					String courseIds[] = getParaValues("course_id");
					String courseHours[] = getParaValues("keshi");
					if(courseIds != null){
						for(int i=0 ;i<courseIds.length;i++){
							String courseHour = courseHours[i];
							if(StringUtils.isEmpty(courseHour))
								continue;
							CoursePrice coursePrice = new CoursePrice();
							coursePrice.set("studentid", courseOrder.getInt("studentid"));
							coursePrice.set("unitprice", courseOrder.getDouble("avgprice"));
							coursePrice.set("realprice", courseOrder.getDouble("avgprice"));
							coursePrice.set("classhour", courseHour);
							coursePrice.set("remainclasshour", courseHour);
							coursePrice.set("courseid", courseIds[i]);
							coursePrice.set("orderid", orderId);
							coursePrice.set("operatorid",getSysuserId());
							coursePriceService.save(coursePrice);
						}
					}
				}
				code = "1";
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	/**
	 * 发送催费邮件
	 */
	 public void sendCourseOrderMesage(){
		 try{
			 	String studentid = getPara();
			 	Student s = Student.dao.findById(studentid);
			 	List<CourseOrder> colist  = CourseOrder.dao.findArrearByStudentId(studentid);
			 	Double totelqfe = 0.0;
				for(CourseOrder co:colist ){
					CourseOrder c = CourseOrder.dao.findById(co.getInt("id"));
					co.put("paymessage",c);
					totelqfe +=c.getBigDecimal("realsum").doubleValue()-(c.getBigDecimal("paidamount")==null?0.0:c.getBigDecimal("paidamount").doubleValue());
				}
				List<SysUser> ulist = SysUser.dao.getAllSysUsers();
				setAttr("ulist",ulist);
				setAttr("totelqfe",totelqfe);
				setAttr("student",s);
				setAttr("date",new Date());
				setAttr("list",colist);
				setAttr("organization",Organization.dao.findById(1));
			 renderJsp("/finance/layer_sendPaymentMessage.jsp");
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 /**
	  * 预览邮件详情
	  */
	 public void  preview(){
		 try{
			 String studentid = getPara("studentid");
			 String paydate = getPara("paydate");
			 	Student s = Student.dao.findById(studentid);
			 	List<CourseOrder> colist  = CourseOrder.dao.findArrearByStudentId(studentid);
			 	Sort sort = Sort.dao.findBySort();
			 	Double totelqfe = 0.0;
				for(CourseOrder co:colist ){
					CourseOrder c = CourseOrder.dao.findById(co.getInt("id"));
					co.put("paymessage",c);
					totelqfe +=c.getNumber("realsum").doubleValue()-(c.getBigDecimal("paidamount")==null?0.0:c.getBigDecimal("paidamount").doubleValue());
				}
				setAttr("totelqfe",totelqfe);
				setAttr("student",s);
				if(sort!=null){
					setAttr("sort",sort.getInt("sort")+1);
				}else{
					setAttr("sort",0);
				}
				setAttr("organization",Organization.dao.findById(1));
				setAttr("date",new Date());
				setAttr("paydate",paydate);
				setAttr("list",colist);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 renderJsp("/finance/previewinvoice.jsp");;
	 }
	 /**
	  * 确认接收人后发送
	  */
	 public void sendPaymentMessage(){
		 JSONObject  json = new JSONObject();
		 try{
			 String studentid = getPara("studentid");
			 String paydate = getPara("paydate");
			 String toemails = getPara("toemails");//接收人
			 String ccemails = getPara("ccemails");//抄送人
			 if(toemails!=null&&!toemails.equals("")){
				 toemails=toemails.substring(1,toemails.length());
			 }
			 if(ccemails!=null&&!ccemails.equals("")){
				 ccemails=ccemails.substring(1,ccemails.length());
			 }
			 String url = getCxt();
			 StringBuffer report = ToolMail.getHtmlTextByURL(url+"/mail/invoice.html");
			 json = courseOrderService.sendOrderPayMessage(toemails,ccemails,paydate,report.toString(),url,studentid);
			if(json.get("code").equals("1")){
				logger.info("发送报告成功，更新发送时间及发送状态0未发、1已发送.");
			}
			Sort sort = Sort.dao.findBySort();
			if(sort==null){
				Sort sor = new Sort();
				sor.set("today", new Date()).set("sort", 1).save();
			}else{
				sort.set("sort", sort.getInt("sort")+1).update();
			}
			renderJson(json);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
}
