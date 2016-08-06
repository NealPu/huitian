package com.momathink.homepage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.momathink.common.base.BaseService;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.finance.model.CourseOrder;
import com.momathink.finance.model.Payment;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;

public class MainService extends BaseService {

	private static Logger log = Logger.getLogger(MainService.class);

	public Long getUnreadOrder(Integer userId) {
		try{
			String sql = "select COUNT(*) as count from crm_courseorder co "
					+ " left join account stu on stu.ID=co.studentid "
					+ " left join crm_opportunity opp on opp.id=stu.oppotunityid"
					+ " where co.isread=0 and opp.scuserid = ? ";
			Long counts = CourseOrder.dao.find(sql, userId).get(0).get("count");
			return counts;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public String getMonthIncome(Integer sysuserId) {
		try{
			//根据当天取得这个月的首
			Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
			return Payment.dao.getIncome(sysuserId,ToolDateTime.format(map.get("start"), "yyyy-MM-dd"));
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
		}
		return null;
	}

	public String getYearIncome(Integer sysuserId) {
		try{
			String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
			return Payment.dao.getIncome(sysuserId,firstDay);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}

	public Long getUserSales(Integer sysId) {
		try{
			String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
			return Opportunity.dao.getUserSales(sysId,firstDay);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public Long getMonthUserStudents(Integer sysId) {
		try{
			Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
			return Student.dao.getMonthStudents(sysId,map.get("start"),map.get("end"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public Long getCoursePlanDay(Integer sysId) {
		String dayStr = ToolDateTime.format(new Date(), "yyyy-MM-dd");
		return CoursePlan.coursePlan.getCoursePlanMonth(sysId,dayStr,dayStr);
	}

	public Long getCoursePlanMonth(Integer sysId) {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		String firstDay = ToolDateTime.format(map.get("start"), "yyyy-MM-dd");
		String dayStr = ToolDateTime.format(new Date(), "yyyy-MM-dd");
		return CoursePlan.coursePlan.getCoursePlanMonth(sysId,firstDay,dayStr);
	}
	//..................
//	public String getSevenIncome() {
//		Map<String, Date> map = ToolDateTime.getWeekDate(new Date());
//		String firstDay = ToolDateTime.format(map.get("start"), "yyyy-MM-dd");
//		String dayStr = ToolDateTime.format(new Date(), "yyyy-MM-dd");
//		System.out.println("firstDay"+firstDay+"dayStr"+dayStr);
//		return  Payment.dao.getIncome();
//		
//	}

	public double getAmount() {
		
		return Payment.dao.getAmount();
	}

	public double getWeek(Integer integer) throws ParseException {
		 String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		 return CourseOrder.dao.getSumPaied(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}
//----------------------------------------------
	public double getMonth(Integer integer) {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return CourseOrder.dao.getSumPaied(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"), ToolDateTime.format(map.get("end"), "yyyy-MM-dd"), integer);
	}
	public double getMonths(Integer integer) {
		Date map = ToolDateTime.getFirstDateOfMonth(new Date());
		return  Payment.dao.getMonth(integer, ToolDateTime.format(map, "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"));
	}

	public double getThmonth(Integer integer) {
		Date d1 = ToolDateTime.getlastMonthFirstDay(new Date());
		return CourseOrder.dao.getSumPaied(ToolDateTime.format(d1, "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double Year(Integer integer) {
		String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
		String now = ToolDateTime.format(new Date(), "yyyy-MM-dd");
		return CourseOrder.dao.getSumPaied(firstDay, now, integer);
	}

	public Long getAmountOpportunity() {
		
		
		return Opportunity.dao.getAmountOpportunity();
	}

	public Long getWeekOpportunity(Integer integer) throws ParseException {
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return Opportunity.dao.getOneSale(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map) , "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public Long getWeekStudent(Integer integer) throws ParseException {
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return Student.dao.getMonthStudent(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"), integer);
	}

	public Long getAmStudent() {
		
		return Student.dao.getAmountStudent();
	}

	public Long getAmountCous() {
		
		return CoursePlan.coursePlan.getAmountCous();
	}
	
	//-----
	public double getKeShi(Integer integer) throws ParseException {
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return CoursePlan.coursePlan.monthkeshi(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}
	
	//总订单
	public Long getAmountOrderCous() {
//		Map<String, Date> map = ToolDateTime.getWeekDate(new Date());
//		return CourseOrder.dao.getAmountOrderCous(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"));
		
		return CourseOrder.dao.getAmountDingdan();
	}
	//课时
	public double getWeekOrder() throws ParseException {
		//Map<String, Date> map = ToolDateTime.getWeekDate(new Date());
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return CourseOrder.dao.getWeekOrder(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"));
		//return CourseOrder.dao.getWeekOrder();
	}
	//付款总数（人）
	public Long getPayAmountOrder() {
		
		return Payment.dao.getPayAmountOrder();
	}
	
	public Long getOneSale(Integer integer) {

		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return Opportunity.dao.getOneSale(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"),ToolDateTime.format(map.get("end"), "yyyy-MM-dd"), integer);
	}
	public Long getOneSales(Integer integer) {

		 Date map = ToolDateTime.getFirstDateOfMonth(new Date());
		return Opportunity.dao.getOneSale(ToolDateTime.format(map, "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public Long getThMonthSale(Integer integer) {
		Date d1 = ToolDateTime.getlastMonthFirstDay(new Date());
		return Opportunity.dao.getOneSale(ToolDateTime.format(d1, "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public Long getYearSale(Integer integer) {
		String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
		String now = ToolDateTime.format(new Date(), "yyyy-MM-dd");
		return Opportunity.dao.getOneSale(firstDay,now,integer);
	}

//	public Long getWeekMonth() {
//		
//		return Student.dao.getMonth();
//	}

	public Long studentThMonth(Integer integer) {
		Date d1 = ToolDateTime.getlastMonthFirstDay(new Date());
		return Student.dao.getMonthStudent(ToolDateTime.format(d1, "yyyy-MM-dd"), integer);
	}

	public Long studentYear(Integer integer) {
		String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
		return Student.dao.getMonthStudent(firstDay, integer);
	}

	public double monthkeshi(Integer integer) {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return CoursePlan.coursePlan.monthkeshi(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"),ToolDateTime.format(map.get("end"), "yyyy-MM-dd"), integer);
		//return CoursePlan.coursePlan.monthkeshi();
	}
	public double monthkeshis(Integer integer) {
		 Date map = ToolDateTime.getFirstDateOfMonth(new Date());
		return CoursePlan.coursePlan.monthkeshi(ToolDateTime.format(map, "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double thmonthkeshi(Integer integer) {
		Date d1 = ToolDateTime.getlastMonthFirstDay(new Date());
		return CoursePlan.coursePlan.monthkeshi(ToolDateTime.format(d1, "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double yearkeshi(Integer integer) {
		String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
		String now = ToolDateTime.format(new Date(), "yyyy-MM-dd");
		return CoursePlan.coursePlan.monthkeshi(firstDay,now, integer);
	}
	
	//学生
	public Long getMonthStudent(Integer integer) {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return Student.dao.getMonthStudent(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"), integer);
	}
	public Long getMonthStudents(Integer integer) {
		 Date map = ToolDateTime.getFirstDateOfMonth(new Date());
		return Student.dao.getMonthStudent(ToolDateTime.format(map, "yyyy-MM-dd"), integer);
	}
	
	//销售机会
	public Long getMonthOpportunity() {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return  Opportunity.dao.getMonth(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"));
	}
	//周订单
	public double getWeekDingdan(Integer integer) throws ParseException {
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return CourseOrder.dao.getMonth(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"),ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double getMonthDingdan(Integer integer) {
		
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return  CourseOrder.dao.getMonth(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"),ToolDateTime.format(map.get("end"), "yyyy-MM-dd"), integer);
	}

	public double getthMonthDingdan(Integer integer) {
		Date d1 = ToolDateTime.getlastMonthFirstDay(new Date());
		return CourseOrder.dao.getMonth(ToolDateTime.format(d1, "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double getyearDingdan(Integer integer) {
		String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
		String now = ToolDateTime.format(new Date(), "yyyy-MM-dd");
		return CourseOrder.dao.getMonth(firstDay,now, integer);
	}

	public double getPayMoney() {
		
	 return Payment.dao.getPayMoney();
	 
	}

	public Long getPayweekOrder(Integer integer) throws ParseException {
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return CourseOrder.dao.getCOUNTPaied(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"), integer);
	}

	public Long getPaymonthOrder(Integer integer) {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return  CourseOrder.dao.getCOUNTPaied(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"), integer);
	}

	public Long getPaytmonthOrder(Integer integer) {
		Date map = ToolDateTime.getlastMonthFirstDay(new Date());
		return  CourseOrder.dao.getCOUNTPaied(ToolDateTime.format(map, "yyyy-MM-dd"), integer);
	}

	public Long getPaytyearOrder(Integer integer) {
		String firstDay = ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd");
		return CourseOrder.dao.getCOUNTPaied(firstDay, integer);
	}

	public double getPayweektotalMoney(Integer integer) throws ParseException {
		String  map = ToolDateTime.getSpecifiedDayBefore(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),7);
		return CourseOrder.dao.getSumPaied(ToolDateTime.format(new SimpleDateFormat("yyyy-MM-dd").parse(map), "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double getPaymonthtotalMoney(Integer integer) {
		Map<String, Date> map = ToolDateTime.getMonthDate(new Date());
		return CourseOrder.dao.getSumPaied(ToolDateTime.format(map.get("start"), "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double getPaytmonthtotalMoney(Integer integer) {
		Date map = ToolDateTime.getlastMonthFirstDay(new Date());
		return CourseOrder.dao.getSumPaied(ToolDateTime.format(map, "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}

	public double getPayyeartotalMoney(Integer integer) {
		return CourseOrder.dao.getSumPaied(ToolDateTime.format(ToolDateTime.getCurrentYearStartTime(), "yyyy-MM-dd"), ToolDateTime.format(new Date(), "yyyy-MM-dd"), integer);
	}
	

	
}
