package com.momathink.homepage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolMonitor;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.finance.model.CourseOrder;
import com.momathink.homepage.service.MainService;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.teacher.model.Announcement;
import com.momathink.teaching.teacher.model.SetPoint;

@Controller(controllerKey="/main")
public class IndexPageController extends BaseController {

	private static final Logger log = Logger.getLogger( IndexPageController.class );
	private MainService mainService = new MainService();
	private  JSONObject json = new JSONObject();
	public void index(){
		try{
			new ToolMonitor("3").start();
			setAttr("copyrighYear", ToolDateTime.getYear(new Date()));
			renderJsp("/WEB-INF/view/main.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 *周
	 */
	public void getWeek(){
		try {
//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//收入
			double week = mainService.getWeek(getSysuserId());
			json.put("income", week);
			//销售机会
			Long mountOpp = mainService.getWeekOpportunity(getSysuserId());
			json.put("saleyear", mountOpp);
			//学生人数
			Long studentWeek = mainService.getWeekStudent(getSysuserId());
			json.put("stunum", studentWeek);
			//课时
			double keshi = mainService.getKeShi(getSysuserId());
			json.put("coursenum", keshi);
//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//图表
			String today = ToolDateTime.format(new Date(), "yyyy-MM-dd");
			List<Map<String, Object>> olist = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
			for(int i=7;i>=0;i--){
				String firstDay = ToolDateTime.getSpecifiedDayBefore(today,i);
				String lastDay = ToolDateTime.getSpecifiedDayBefore(today, i-1);
				//订单
				Long dayOrders = CourseOrder.dao.getUserDayOrders( firstDay, lastDay, getSysuserId());
				Map<String,Object> omap = new HashMap<String,Object>();
				omap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				omap.put("value", dayOrders);
				//付款
				Long dayPay = CourseOrder.dao.getDayUserPay(firstDay,lastDay, getSysuserId());
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				pmap.put("value", dayPay);
				olist.add(omap);
				plist.add(pmap);
			}
			json.put("data1", olist);
			json.put("data2", plist);
			//订单总数(x/单)
			double weekDingdan = mainService.getWeekDingdan(getSysuserId());
			json.put("userOrders", weekDingdan);
			//付款总数(x/单)
			Long weekPay = mainService.getPayweekOrder(getSysuserId());
			json.put("totalOrders", weekPay);
			//付款总数(x/RMB)
			double weektotalPay = mainService.getPayweektotalMoney(getSysuserId());
			json.put("totalNum", weektotalPay);
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 月
	 */
	public void getMonth(){
		
		try {
//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//收入
			double income = mainService.getMonth(getSysuserId());
			json.put("income", income);
			//销售机会
			Long saleyear = mainService.getOneSales(getSysuserId());
			json.put("saleyear", saleyear);
			//学生人数
			Long stunum= mainService.getMonthStudent(getSysuserId());
			json.put("stunum", stunum);
			//课时
			double coursenum = mainService.monthkeshi(getSysuserId());
			json.put("coursenum", coursenum);
//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//图表
			String today = ToolDateTime.format(new Date(), "yyyy-MM-dd");
			List<Map<String, Object>> olist = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
			for(int i=30;i>=0;i--){
				String firstDay = ToolDateTime.getSpecifiedDayBefore(today,i);
				String lastDay = ToolDateTime.getSpecifiedDayBefore(today, i-1);
				//订单
				Long dayOrders = CourseOrder.dao.getUserDayOrders( firstDay, lastDay, getSysuserId());
				Map<String,Object> omap = new HashMap<String,Object>();
				omap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				omap.put("value", dayOrders);
				//付款
				Long dayPay = CourseOrder.dao.getDayUserPay(firstDay,lastDay, getSysuserId());
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				pmap.put("value", dayPay);
				olist.add(omap);
				plist.add(pmap);
			}
			//图表数据
			json.put("data1", olist);
			json.put("data2", plist);
			//订单总数(x/单)
			double userOrders = mainService.getMonthDingdan(getSysuserId());
			json.put("userOrders", userOrders);
			//付款总数(x/单)
			Long totalOrders = mainService.getPaymonthOrder(getSysuserId());
			json.put("totalOrders", totalOrders);
			//付款总数(x/RMB)
			double totalNum = mainService.getPaymonthtotalMoney(getSysuserId());
			json.put("totalNum", totalNum);
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 季度
	 */
	public void getThmonth(){
		
		try {
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 收入
			double income = mainService.getThmonth(getSysuserId());
			json.put("income", income);
			// 销售机会
			Long saleyear = mainService.getThMonthSale(getSysuserId());
			json.put("saleyear", saleyear);
			// 学生人数
			Long stunum = mainService.studentThMonth(getSysuserId());
			json.put("stunum", stunum);
			// 课时
			double coursenum = mainService.thmonthkeshi(getSysuserId());
			json.put("coursenum", coursenum);
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////
			//图表
			String today = ToolDateTime.format(new Date(), "yyyy-MM-dd");
			List<Map<String, Object>> olist = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
			for(int i=90;i>=0;i--){
				String firstDay = ToolDateTime.getSpecifiedDayBefore(today,i);
				String lastDay = ToolDateTime.getSpecifiedDayBefore(today, i-1);
				Long dayOrders = CourseOrder.dao.getUserDayOrders( firstDay, lastDay, getSysuserId());
				Map<String,Object> omap = new HashMap<String,Object>();
				omap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				omap.put("value", dayOrders);
				Long dayPay = CourseOrder.dao.getDayUserPay(firstDay,lastDay, getSysuserId());
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				pmap.put("value", dayPay);
				olist.add(omap);
				plist.add(pmap);
			}
			// 图表数据
			json.put("data1", olist);
			json.put("data2", plist);
			// 订单总数(x/单)
			double userOrders = mainService.getthMonthDingdan(getSysuserId());
			json.put("userOrders", userOrders);
			// 付款总数(x/单)
			Long totalOrders = mainService.getPaytmonthOrder(getSysuserId());
			json.put("totalOrders", totalOrders);
			// 付款总数(x/RMB)
			double totalNum = mainService.getPaytmonthtotalMoney(getSysuserId());
			json.put("totalNum", totalNum);
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 年
	 */
	public void getYear(){
		try {
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 收入
			double income = mainService.Year(getSysuserId());
			json.put("income", income);
			// 销售机会
			Long saleyear = mainService.getYearSale(getSysuserId());
			json.put("saleyear", saleyear);
			// 学生人数
			Long stunum = mainService.studentYear(getSysuserId());
			json.put("stunum", stunum);
			// 课时
			double coursenum = mainService.yearkeshi(getSysuserId());
			json.put("coursenum", coursenum);
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////
			//图表
			String today = ToolDateTime.format(new Date(), "yyyy-MM-dd");
			List<Map<String, Object>> olist = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
			for(int i=365;i>=0;i--){
				String firstDay = ToolDateTime.getSpecifiedDayBefore(today,i);
				String lastDay = ToolDateTime.getSpecifiedDayBefore(today, i-1);
				Long dayOrders = CourseOrder.dao.getUserDayOrders( firstDay, lastDay, getSysuserId());
				Map<String,Object> omap = new HashMap<String,Object>();
				omap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				omap.put("value", dayOrders);
				Long dayPay = CourseOrder.dao.getDayUserPay(firstDay,lastDay, getSysuserId());
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				pmap.put("value", dayPay);
				olist.add(omap);
				plist.add(pmap);
			}
			// 图表数据
			json.put("data1", olist);
			json.put("data2", plist);
			// 订单总数(x/单)
			double userOrders = mainService.getyearDingdan(getSysuserId());
			json.put("userOrders", userOrders);
			// 付款总数(x/单)
			Long totalOrders = mainService.getPaytyearOrder(getSysuserId());
			json.put("totalOrders", totalOrders);
			// 付款总数(x/RMB)
			double totalNum = mainService.getPayyeartotalMoney(getSysuserId());
			json.put("totalNum", totalNum);
			renderJson(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 总收入
	 */
	
	public void getAmout(){
	
		try {
			double amount = mainService.getAmount();
			json.put("amount", amount);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}
	
	/**
	 * 总学生人数
	 */
	
	public void getAmountStudent(){
		
		JSONObject json = new JSONObject();
		
		try {
			Long amSt = mainService.getAmStudent();
			json.put("amSt", amSt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}
	/**
	 * 总课时
	 */
	public void getAmountCourseplan(){
		try {
			Long amountCour = mainService.getAmountCous();
			json.put("amountCour", amountCour);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}
	/**
	 * 总订单数
	 */
	public void getAmountOrder(){
		
		try {
			Long amountOrder = mainService.getAmountOrderCous();
			json.put("amountOrder", amountOrder);
			System.out.println(amountOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(json);
	}

	/**
	 * 付款总数
	 */
//	public void getAmountPay(){
//		
//		try {
//			Long AmountPay = mainService.getPayAmountOrder();
//			json.put("AmountPay", AmountPay);
//			System.out.println(AmountPay);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		renderJson(json);
//	}
	
	//---------------------------------------------------
	/**
	 * 月收入
	 */
	public void getRevenues(){
		JSONObject json = new JSONObject();
		Record sysUser = getSessionAttr("account_session");
		Integer sysuserId = sysUser.getInt("ID");
		try{
			//当月payment
			String monthIncome = mainService.getMonthIncome(sysuserId);
			
			json.put("monthIncome",monthIncome);
			//整年
			String yearIncome = mainService.getYearIncome(sysuserId);
			json.put("yearIncome", yearIncome);
			System.out.println("year"+json);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		renderJson(json);
	}
	
	/**
	 * 
	 * 总销售机会
	 */
	
	public void getAmountOpportunity(){
		
		JSONObject json = new JSONObject();
		
		try{
			Long mountOpp = mainService.getAmountOpportunity();
			json.put("mountOpp", mountOpp);
			System.out.println(mountOpp);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	/**
	 * 销售机会总数（全年）
	 */
	public void getSaleOpportunity(){
		JSONObject json = new JSONObject();
		try{
			Long userSaleOpportunities = mainService.getUserSales(getSysuserId());
			json.put("userSales", userSaleOpportunities);
			
			Long totalSaleOppo = Opportunity.dao.getUserSales(getSysuserId(),"");
			json.put("totalSales", totalSaleOppo);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	/**
	 * 学生人数（月）
	 */
	public void getStudentNum(){
		JSONObject json = new JSONObject();
		//Record sysUser = getSessionAttr("account_session");
		//Long sysId = sysUser.getLong("ID");
		try{
			//Long monthStudents = mainService.getMonthUserStudents(sysId);
			Long monthStudents = mainService.getMonthUserStudents(getSysuserId());
			json.put("monthSysStu", monthStudents);
			Long monthStu = mainService.getMonthUserStudents(null);
			json.put("monthStus", monthStu);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	/**
	 * 课时（天）
	 */
	public void getDayCourseNum(){
		JSONObject json = new JSONObject();
		//Record sysUser = getSessionAttr("account_session");
		//Long sysId = sysUser.getLong("ID");
		try{
//			Long stuCourseplans = mainService.getCoursePlanDay(sysId);
//			Long stuMonthPlans = mainService.getCoursePlanMonth(sysId);
			Long stuCourseplans = mainService.getCoursePlanDay(getSysuserId());
			Long stuMonthPlans = mainService.getCoursePlanMonth(getSysuserId());
			json.put("dayPlans", stuCourseplans);
			json.put("monthPlans", stuMonthPlans);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
		
	}
	
	
	/**
	 * 订单数和付款数(图表)
	 */
	public void getOrderAndPayList(){
		try{
			String today = ToolDateTime.format(new Date(), "yyyy-MM-dd");
			List<Map<String, Object>> olist = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
			Long maxOrder = 0L;
			Long maxPay = 0L;
			Long amountOrder = mainService.getAmountOrderCous();
			json.put("amountOrder", amountOrder);
			Long AmountPay = mainService.getPayAmountOrder();//付款总数（人）
			json.put("AmountPay", AmountPay);
			double Pay = mainService.getPayMoney();//总金额
			json.put("Pay", Pay);
			for(int i=31;i>=0;i--){
				String firstDay = ToolDateTime.getSpecifiedDayBefore(today, i);
				String lastDay = ToolDateTime.getSpecifiedDayBefore(today, i-1);
				Long dayOrders = CourseOrder.dao.getUserDayOrders( firstDay, lastDay, getSysuserId());
				Map<String,Object> omap = new HashMap<String,Object>();
				omap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				omap.put("value", dayOrders);
				maxOrder = maxOrder>dayOrders?maxOrder:dayOrders;
				Long dayPay = CourseOrder.dao.getDayUserPay(firstDay,lastDay, getSysuserId());
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("time", ToolDateTime.parse(firstDay,"yyyy-MM-dd").getTime());
				pmap.put("value", dayPay);
				olist.add(omap);
				plist.add(pmap);
				maxPay = maxPay>dayPay?maxPay:dayPay;
				
			}
			Long userOrders = CourseOrder.dao.getUserDayOrders( ToolDateTime.getSpecifiedDayBefore(today, 31), today, getSysuserId());
			Long paiedOrders = CourseOrder.dao.getDayUserPay(ToolDateTime.getSpecifiedDayBefore(today, 31), today, getSysuserId());
//			String totalNumRMB = CourseOrder.dao.getSumPaied(getSysuserId(),ToolDateTime.getSpecifiedDayBefore(today, 31), today);
			json.put("userOrders", userOrders);//订单总数(月/单)
			json.put("totalOrders", paiedOrders);//付款订单数量(月/单)
			json.put("data1", olist);
			json.put("data2", plist);
			json.put("maxdata1", maxOrder+2);
			json.put("maxdata2", maxPay+1);
//			json.put("totalNum", totalNumRMB);//付款金额(月/RMB)
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
		
	}
	
	
	/**
	 * 反馈消息
	 */
	public void getFeedbackMassage(){
		
	}
		
	public void getMessage(){
		JSONObject json = new JSONObject();
		try{
			Record sysUser = getSessionAttr("account_session");
			//未读消息
			Long ordercounts = CourseOrder.dao.getUnreadOrderCounts(sysUser.getInt("ID"));
			Long oppocounts = Opportunity.dao.getUnreadOppoCounts(sysUser.getInt("ID"));
			Long reportcounts = SetPoint.dao.getCountsForTeacherUnSubmit(sysUser.getInt("ID"));
			Long ann = Announcement.dao.getCountsUnreadMessage(sysUser.getInt("ID"));
			Long count = Student.dao.getCountBirthdayToDay().get("sum");	
			Map<Object,Object> s = getSessionAttr("operator_session");
			boolean f1=(boolean)s.get("qx_orders");
			boolean f2=(boolean)s.get("qx_opportunity");
			boolean f3=(boolean)s.get("qx_studentbirthday");
			boolean f4=(boolean)s.get("qx_reportteacherReports");
			boolean f5=(boolean)s.get("qx_teacherqueryAllReceiver");
			Long total = (f1?ordercounts:0)+(f2?oppocounts:0)+(f4?reportcounts:0)+(f3?count:0)+(f5?ann:0);
			json.put("total",total);
			json.put("noann",ann);
			json.put("orderCount",ordercounts);
			json.put("oppocounts",oppocounts);
			json.put("reportcounts", reportcounts);
			json.put("count", count);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJson(json);
	}
	
	
	public void queryUserNetAccountId() {
		JSONObject resultJson = new JSONObject();
		try {
			Integer sysUserId  = getSysuserId();
			SysUser currentUser = SysUser.dao.findById( sysUserId );
			Integer netSchoolId = currentUser.getInt( "netschoolid" );
			if( netSchoolId == null ) {
				resultJson.put( "flag" , false );
			} else {
				resultJson.put( "netUserId" , netSchoolId );
				
				resultJson.put( "flag" , true );
				long timestamp = System.currentTimeMillis();
				resultJson.put( "timestamp" , timestamp );
				resultJson.put( "signature" , ToolMD5.getMD5( ToolMD5.getMD5( timestamp + "huitian" ) ) );
			}
			
		} catch (Exception e) {
			log.error( "queryUserNetAccountId" , e );
			resultJson.put( "flag" , false );
		}
		renderJson( resultJson );
	}
	
	
	
	
}


