package com.momathink.finance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.finance.model.CourseOrder;
import com.momathink.finance.model.Refund;
import com.momathink.finance.service.CourseOrderService;
import com.momathink.finance.service.FinanceService;
import com.momathink.sys.account.model.AccountBook;
import com.momathink.sys.account.service.AccountService;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.subject.model.Subject;

/**
 * 财务管理
 * 
 * @author David
 */
@Controller(controllerKey = "/finance")
public class FinanceController extends BaseController {
	private static final Logger logger = Logger.getLogger(FinanceController.class);
	private FinanceService financeService = new FinanceService();
	private AccountService accountService = new AccountService();
	private CourseOrderService courseOrderService = new CourseOrderService();
	public void index(){
		Integer sysuserId = getSysuserId();
		String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
		if (campusids != null) {
		String campusSql = " and s.campusid IN("+campusids+")";
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("campusSql",campusSql);
		}

		financeService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/finance_manage.jsp");
	}
	
	public void initdata(){
		try {
			if("init".equals(getPara("key"))){
				Integer studentId = null;
				StringBuffer sqlo = new StringBuffer("select * from crm_courseorder where delflag=0");
				StringBuffer sqlc = new StringBuffer("select * from courseplan where plan_type=0 and state=0 and class_id=0 and STUDENT_ID is NOT null AND SUBJECT_ID is NOT null ");
				StringBuffer banSql = new StringBuffer( "insert into account_book(accountid,operatetype,createuserid,classorderid,courseid,subjectid,courseorderid,courseplanid,courseprice,carriedOver,realamount,classhour)\n" +
				"select a.studentid,4,1,a.class_id,a.COURSE_ID,a.SUBJECT_ID,o.id,a.COURSEPLAN_ID,o.avgprice,0,ROUND(o.avgprice*t.class_hour,2),t.class_hour from \n" +
				"(select t.studentid,p.class_id,t.COURSEPLAN_ID,p.COURSE_ID,p.SUBJECT_ID,p.TIMERANK_ID from teachergrade t \n" +
				"left join account s on t.studentid=s.Id\n" +
				"left join courseplan p on t.COURSEPLAN_ID=p.Id\n" +
				"where s.STATE=0 AND p.plan_type=0 and p.class_id!=0) a\n" +
				"left join crm_courseorder o on a.class_id=o.classorderid and a.studentid=o.studentid\n" +
				"left join time_rank t on a.TIMERANK_ID=t.Id\n" +
				"WHERE o.`status`!=0");
				if(!StringUtils.isEmpty(getPara("studentid"))){
					studentId=getParaToInt("studentid");
					sqlo.append(" and studentid="+getPara("studentid"));
					sqlc.append(" and student_id="+getPara("studentid"));
					banSql.append(" and a.studentid="+getPara("studentid"));
				}
//				List<CourseOrder> orderList = CourseOrder.dao.find(sqlo.toString());
//				for(CourseOrder o : orderList){
//					if(o.getBigDecimal("realsum").compareTo(BigDecimal.ZERO)==1){
//						Account account = Account.dao.findById(o.getInt("studentid"));
//						double realbalance = account.getBigDecimal("realbalance")==null?0:account.getBigDecimal("realbalance").doubleValue();
//						double rewardbalance = account.getBigDecimal("rewardbalance")==null?0:account.getBigDecimal("rewardbalance").doubleValue();
//						AccountBook book = new AccountBook();
//						book.set("accountid", o.get("studentid"));
//						book.set("createtime", o.get("createtime"));
//						book.set("operatetype", 1);
//						book.set("createuserid", 1);
//						book.set("realamount", o.get("realsum"));
//						book.set("rewardamount",0);
//						book.set("realbalance", ToolArith.add(realbalance,o.getBigDecimal("realsum").doubleValue()));
//						book.set("rewardbalance", rewardbalance);
//						book.set("orderid", o.getPrimaryKeyValue());
//						book.set("classorderid", o.getInt("classorderid"));
//						book.set("status", 0);
//						book.save();
//						account.set("realbalance", ToolArith.add(realbalance,o.getBigDecimal("realsum").doubleValue()));
//						account.set("rewardbalance", rewardbalance);
//						account.set("version", account.getInt("version")==null?1:account.getInt("version")+1);
//						account.update();
//					}
//					if(o.getBigDecimal("rebate").compareTo(BigDecimal.ZERO)==1){
//						Account account = Account.dao.findById(o.getInt("studentid"));
//						double realbalance = account.getBigDecimal("realbalance")==null?0:account.getBigDecimal("realbalance").doubleValue();
//						double rewardbalance = account.getBigDecimal("rewardbalance")==null?0:account.getBigDecimal("rewardbalance").doubleValue();
//						AccountBook book = new AccountBook();
//						book.set("accountid", o.get("studentid"));
//						book.set("createtime", o.get("createtime"));
//						book.set("operatetype", 2);
//						book.set("createuserid", 1);
//						book.set("realamount", 0);
//						book.set("rewardamount", o.get("rebate"));
//						book.set("realbalance", realbalance);
//						book.set("rewardbalance", ToolArith.add(rewardbalance,o.getBigDecimal("rebate").doubleValue()));
//						book.set("orderid", o.getPrimaryKeyValue());
//						book.set("classorderid", o.getInt("classorderid"));
//						book.set("status", 0);
//						book.save();
//						account.set("realbalance", realbalance);
//						account.set("rewardbalance", ToolArith.add(rewardbalance,o.getBigDecimal("rebate").doubleValue()));
//						account.set("version", account.getInt("version")==null?1:account.getInt("version")+1);
//						account.update();
//					}
//					if(o.getInt("classorderid")!=null&&o.getInt("classorderid")!=0){
//						Account _account = Account.dao.findById(o.getInt("studentid"));
//						double _realbalance = _account.getBigDecimal("realbalance")==null?0:_account.getBigDecimal("realbalance").doubleValue();
//						double _rewardbalance = _account.getBigDecimal("rewardbalance")==null?0:_account.getBigDecimal("rewardbalance").doubleValue();
//						AccountBook _book = new AccountBook();
//						_book.set("accountid", o.get("studentid"));
//						_book.set("createtime", o.get("createtime"));
//						_book.set("operatetype", 4);
//						_book.set("createuserid", 1);
//						_book.set("realamount", o.get("realsum"));
//						_book.set("rewardamount", o.get("rebate"));
//						_book.set("realbalance", ToolArith.sub(_realbalance,o.getBigDecimal("realsum").doubleValue()));
//						_book.set("rewardbalance", ToolArith.sub(_rewardbalance,o.getBigDecimal("rebate").doubleValue()));
//						_book.set("orderid", o.getPrimaryKeyValue());
//						_book.set("classorderid", o.getInt("classorderid"));
//						_book.set("status", 0);
//						_book.save();
//						_account.set("realbalance", ToolArith.sub(_realbalance,o.getBigDecimal("realsum").doubleValue()));
//						_account.set("rewardbalance", ToolArith.sub(_rewardbalance,o.getBigDecimal("rebate").doubleValue()));
//						_account.set("version", _account.getInt("version")==null?1:_account.getInt("version")+1);
//						_account.update();
//					}
//				}
//				
				List<CoursePlan> clist = CoursePlan.coursePlan.find(sqlc.append(" order by student_id,course_time").toString());
				AccountBook.dao.deleteByAccountId(studentId);
				for(CoursePlan p : clist){
					boolean result = accountService.consumeCourse(p.getPrimaryKeyValue(), p.getInt("student_id"), getSysuserId(),0);
					if(result){
						logger.info("完成：学生ID:"+p.getInt("student_id")+"课程ID"+p.getPrimaryKeyValue());
					}else{
						logger.info("失败：学生ID:"+p.getInt("student_id")+"课程ID"+p.getPrimaryKeyValue());
					}
				}
				Db.update(banSql.toString());
				renderJson("msg","数据初始化成功");
			}else{
				renderJson("msg","非法请求");
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("msg","初始化数据异常");
		}
	}
	
	public void checkCanRefund(){
		JSONObject json = new JSONObject();
		try{
			String id = getPara("id");
			long counts = CourseOrder.dao.getQianfeiCount(id);
			if(counts>0){
				//有欠费
				json.put("code", 0);
				json.put("msg", "还有订单欠费，不可申请退费");
			}else{
				json.put("code", 1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	public void applyRefund(){
		try{
			String id = getPara("id");
			if(StringUtils.isEmpty(id)){
				String name = getPara("name");
				Student student = Student.dao.getStudentByName(name);
				id = student.getPrimaryKeyValue().toString();
				setAttr("name",student.getStr("real_name"));
			}else{
				Student student = Student.dao.findById(id);
				setAttr("name",student.getStr("real_name"));
			}
				List<CourseOrder> list = courseOrderService.getStudentCourseOrderlists(id);
				setAttr("stulist",list);
				renderJsp("/finance/apply_refund.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void toDoRefund(){
		try{
			Integer courseOrderId = getParaToInt();
			CourseOrder courseOrder =  CourseOrder.dao.getCourseOrderInfoById(courseOrderId);
//			String subjectids =  ordermap.getStr("subjectids");
//			Integer studentid =  ordermap.getInt("studentid");
//			Integer teachtype =  ordermap.getInt("teachtype");
//			Integer classOrderId =  ordermap.getInt("classOrderId");
//			List<CoursePlan> subjectList = financeService.getStudentOrderSubjectPlaned(subjectids,studentid.toString(),teachtype.toString(),classOrderId);
//			setAttr("subjectList",subjectList);
			double remainClassHour = ToolArith.sub(courseOrder.getDouble("classhour"), courseOrder.getDouble("usedhours"));
			setAttr("remainClassHour",remainClassHour);
			setAttr("remainBalance",ToolArith.mul(courseOrder.getDouble("avgprice"),remainClassHour));
			setAttr("orders",courseOrder);
			renderJsp("/finance/layer_torefund.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 退费
	 */
	public void submitRefundRecord(){
		JSONObject json = new JSONObject();
		try{
			Refund refund = getModel(Refund.class);
			refund.set("sysuser", getSysuserId());
			json = financeService.saveRefund(refund,getSysuserId());
			removeSessionAttr("refund");
		}catch(Exception ex){
			json.put("code", 0);
			json.put("msg", "操作失败!");
			ex.printStackTrace();
		}
		renderJson(json);
		
	}
	
	public void queryRefundPage(){
		Integer sysuserId = getSysuserId();
		String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
		if (campusids != null) {
		String campusSql = " and a.campusid IN("+campusids+")";
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("campusSql",campusSql);
		}
		financeService.queryRefundPageMessage(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/refund_list.jsp");
	}
	
	/**
	 * 消耗统计
	 * @author David
	 */
	public void consumptionStatistic(){
		Map<String, String> queryParam = splitPage.getQueryParam();
		if (null == queryParam || queryParam.size()==0) {
			queryParam = new HashMap<String,String>();
			queryParam.put("endDate", ToolDateTime.getCurDate());
			queryParam.put("beginDate", ToolDateTime.getMonthFirstDay(ToolDateTime.getDate()));
			splitPage.setQueryParam(queryParam);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("_query.endDate", ToolDateTime.getCurDate());
			paramMap.put("_query.beginDate", ToolDateTime.getMonthFirstDay(ToolDateTime.getDate()));
			setAttr("paramMap", paramMap);
		}
		Record record = financeService.consumptionList(splitPage);
		setAttr("subjectList",Subject.dao.getSubject());
		if(!ToolString.isNull(getPara("_query.subjectId")))
			setAttr("courseList",Course.dao.getCourseBySubjectId(getPara("_query.subjectId")));
		setAttr("campusList",Campus.dao.getCampus());
		setAttr("totalHour",record.getBigDecimal("totalHour"));
		setAttr("totalAmount",record.getBigDecimal("totalAmount"));
		setAttr("showPages", splitPage.getPage());		
		render("/finance/consumptionStatistic.jsp");
	}
	
}
