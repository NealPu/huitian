package com.momathink.finance.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.subject.model.Subject;
@Table(tableName = "crm_courseorder")
public class CourseOrder extends BaseModel<CourseOrder> {

	private static final long serialVersionUID = -8229493298471656800L;
	public static final CourseOrder dao = new CourseOrder();
	
	/**
	 * 根据ID获取订单 
	 * @author David
	 * @param id
	 * @return
	 * @since JDK 1.7
	 */
	public CourseOrder findById(Integer id) {
		String sql = "SELECT o.*,s.REAL_NAME studentname,s.PARENTS_EMAIL,s.EMAIL,"
				+ " c.REAL_NAME operatorname,d.real_name delusername, "
				+ "(SELECT COUNT(1) FROM crm_payment p WHERE p.orderid=o.id) paycount,"
				+ "(SELECT SUM(classhour) FROM crm_payment p WHERE p.orderid=o.id) paidclasshour,"
				+ "(SELECT IFNULL(SUM(IFNULL(amount,0)),0) FROM crm_payment p WHERE p.ispay=1 and p.orderid=o.id) paidamount,bc.classNum "
				+ "FROM crm_courseorder o "
				+ "LEFT JOIN account s ON o.studentid=s.Id "
				+ "LEFT JOIN account c ON o.operatorid=c.Id "
				+ "LEFT JOIN account d ON o.deluserid=d.Id "
				+ "LEFT JOIN class_order bc ON o.classorderid=bc.id WHERE o.id=?";
		CourseOrder courseOrder = dao.findFirst(sql,id);
		courseOrder.put("subjectname", Subject.dao.getSubjectNameByIds(courseOrder.getStr("subjectids")));
		courseOrder.put("coursePriceList",CoursePrice.dao.findByOrderId(id.toString()));
		return courseOrder;
	}

	/**
	 * 获取支付情况
	 * @param studentId
	 * @return
	 */
	public Record getOrderInfo(String studentId) {
		if(StringUtils.isEmpty(studentId)){
			return null;
		}else{
			String sql="SELECT a.studentid,IFNULL(SUM(a.classhour),0) zks,IFNULL(SUM(a.totalsum),0) zje,IFNULL(SUM(a.realsum),0) ssje,IFNULL(FLOOR(SUM(a.ks)),0) ygks\n" +
					"FROM \n" +
					"(SELECT co.classhour,co.totalsum,co.realsum,co.studentid,co.classorderid,co.teachtype,IF(co.avgprice=0,co.classhour,cp.totalfee/co.avgprice) ks \n" +
					"FROM crm_courseorder co \n" +
					"LEFT JOIN\n" +
					"(SELECT p.orderid,SUM(amount) totalfee FROM crm_payment p WHERE 1=1 GROUP BY p.orderid) cp ON co.id=cp.orderid WHERE co.delflag=0 and co.teachtype=1) a\n" +
					"WHERE a.studentid = "+studentId;
			Record record = Db.findFirst(sql);
			return record;
		}
	}

	/**
	 * 
	 * 查询学生可用课时数
	 * @author David
	 * @param studentId
	 * @param courseId
	 * @return
	 * @since JDK 1.7
	 */
	public float getUsableHour(String studentId,String courseId) {
		// 查询总节数和已经排课的节数
		Record info = CourseOrder.dao.getOrderInfo(studentId);
		Double _ygks = info.getDouble("ygks");
		int ygks = _ygks==null?0:_ygks.intValue();
		float ypks = CoursePlan.coursePlan.getUseClasshour(studentId,courseId);
		return ygks-ypks;
	}

	/**
	 * 根据学生ID和班次ID获取学生可用课时
	 * @param stuId
	 * @param classOrder_id
	 * @return
	 */
	public float getClassStuUsableHour(Integer stuId,Integer classOrder_id) {
		ClassOrder co = ClassOrder.dao.findById(classOrder_id);//lessonNum总课时
		Integer _ygks = co.getInt("lessonNum");
		int ygks = _ygks==null?0:_ygks.intValue();
		float ypks = CoursePlan.coursePlan.getClassYpkcClasshour(classOrder_id);
		return ygks-ypks;
	}

	/**
	 * 根据学生ID和班次ID获取班次订单信息
	 * @param stuId
	 * @param classOrder_id
	 * @return
	 */
	public  Record getClassStuOrderInfo(Integer stuId, Integer classOrder_id) {
		String sql="SELECT a.studentid,a.classorderid,SUM(a.classhour) zks,SUM(a.totalsum) zje,SUM(a.realsum) ssje,FLOOR(SUM(a.ks)) ygks\n" +
				"FROM \n" +
				"(SELECT co.classhour,co.totalsum,co.realsum,co.studentid,co.classorderid,co.teachtype,IF(co.avgprice=0,co.classhour,cp.totalfee/co.avgprice) ks \n" +
				"FROM crm_courseorder co \n" +
				"LEFT JOIN\n" +
				"(SELECT p.orderid,SUM(amount) totalfee FROM crm_payment p WHERE p.ispay=1 GROUP BY p.orderid) cp ON co.id=cp.orderid where co.teachtype=2) a\n" +
				"WHERE a.studentid = "+stuId+" and a.classorderid= "+classOrder_id;
		Record record = Db.findFirst(sql);
		return record;
	}

	/**
	 * 根据学生ID和班次ID获取全部班次订单
	 * @param studentId
	 * @param classId
	 * @return
	 */
	public List<CourseOrder> findCOByStuClassId(String studentId, Integer classId) {
		String sql = "select * from crm_courseorder where studentid = ? and classorderid = ? ";
		List<CourseOrder> list = CourseOrder.dao.find(sql, studentId,classId);
		return list;
	}

	/**
	 * 获取未查看的订单信息
	 * @param userId
	 * @return
	 */
	public Long getUnreadOrderCounts(Integer userId) {
		String sql = "select COUNT(*) as count from crm_courseorder co "
				+ " left join account stu on stu.ID=co.studentid "
				+ " left join crm_opportunity opp on opp.id=stu.opportunityid "
				+ " where co.isread=0 and opp.scuserid = ? ";
		return Db.queryLong(sql, userId);
	}
	
	/**
	 * 获取学生所有订单总课时，包含已取消订单
	 * @param stuid
	 * @return
	 */
	public CourseOrder getStudentSumHours(String stuid){
		String sql = "select sum(co.classhour) allhour,stu.real_name from crm_courseorder co left join account stu on stu.Id=co.studentid where co.studentid = ? ";
		CourseOrder co = CourseOrder.dao.findFirst(sql, stuid);
		return co;
	}
	
	/**
	 * 获取学生欠费和已支付的订单信息，包含了已取消订单
	 * @param stuid
	 * @return
	 */
	public CourseOrder getStudentHours(String stuid){
		String sql = "select sum(co.classhour) allhour,stu.real_name from crm_courseorder co left join account stu on stu.Id=co.studentid where co.status <> 0 and co.studentid = ? ";
		CourseOrder co = CourseOrder.dao.findFirst(sql, stuid);
		return co;
	}
	
	/**
	 * //图表订单
	 * @param date1
	 * @param date2
	 * @param integer
	 * @return
	 */
	public Long getUserDayOrders(String date1,String date2, Integer integer){
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" select COUNT(1) from crm_courseorder co left join account a on a.ID=co.studentid ");
		Long t1 = null;
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join student_kcgw ak ON a.Id = ak.student_id ");
		}
		sql.append(" where 1=1 and co.createtime >= ? and co.createtime <= ?");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			t1 = Db.queryLong(sql.toString(),date1,date2,integer);
		}
		
		else{
			t1 = Db.queryLong(sql.toString(),date1,date2);
		}
		
		return  t1;
		
	}

	/** 图表付款
	 * 
	 * @param date1
	 * @param date2
	 * @param integer
	 * @return
	 */
	public Long getDayUserPay( String date1, String date2, Integer integer) {
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" select COUNT(1) as count from crm_courseorder co left join account stu on stu.ID=co.studentid ");
		Long total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join student_kcgw ak ON stu.Id = ak.student_id ");
		}
		sql.append(" where 1=1 and co.paiedtime >= ? and co.paiedtime < ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryLong(sql.toString(),date1,date2,integer);
		}
		
		else{
			total = Db.queryLong(sql.toString(),date1,date2);
		}
		
		return  total==null?0:total;
	}
	
	/**
	 * 获取已支付订单总金额
	 * @param sysuserId
	 * @param firstday
	 * @param today
	 * @return
	 */
	public String getSumPaied(Integer sysuserId, String firstday, String today) {
		String sql = " select sum(totalsum) as totalsum from crm_courseorder co  "
				+ " left join crm_opportunity opp on opp.id=stu.opportunityid "
				+ " where co.status=1  and co.paiedtime >= ? and co.paiedtime <= ? ";
		SysUser sysuser = SysUser.dao.findById(sysuserId);
		if(!Role.isAdmins(sysuser.getStr("roleids"))){
			sql += " and opp.scuserid = "+sysuserId;
		}
		CourseOrder list = dao.findFirst(sql, firstday, today);
		if(list==null){
			return "0";
		}else{
			if(list.get("totalsum")==null){
				return "0";
			}else{
				return list.get("totalsum").toString();
			}
		}
	}

	/**
	 * 根据日期时段获取课程顾问所属学生已付总金额
	 * @param beginDate
	 * @param endDate
	 * @param kcgwId
	 * @return
	 */
	public double getSumPaied(String beginDate, String endDate, Integer kcgwId) {
		Account user  =Account.dao.findById(kcgwId); 
		StringBuffer sql = new  StringBuffer(" SELECT sum(totalsum) FROM crm_courseorder co ");
		BigDecimal total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" LEFT JOIN account stu ON stu.ID = co.studentid LEFT JOIN student_kcgw ak ON stu.Id = ak.student_id ");
		}
		sql.append(" WHERE co.delflag=0 AND co.status=1 AND co.paiedtime >= ? AND co.paiedtime <= ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryBigDecimal(sql.toString(),beginDate,endDate,kcgwId);
		}
		
		else{
			total = Db.queryBigDecimal(sql.toString(),beginDate,endDate);
		}
		
		return  total==null?0:total.doubleValue();
	}
	
	/**
	 * //付款总数(x/单)
	 * @param date
	 * @param integer
	 * @return
	 */
	public Long getCOUNTPaied(String date, Integer integer) {
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" SELECT COUNT(1) FROM crm_courseorder co ");
		Long total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" LEFT JOIN account stu ON stu.ID = co.studentid LEFT JOIN student_kcgw ak ON stu.Id = ak.student_id ");
		}
		sql.append(" WHERE co.checkstatus=1 AND co.delflag=0 AND co.status=1 AND co.paiedtime >= ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ? ");
			total = Db.queryLong(sql.toString(),date,integer);
		}
		
		else{
			total = Db.queryLong(sql.toString(),date);
		}
		
		return  total;
		
	}
	
	/**
	 * 获取购买过班次的已支付订单
	 * @param classOrderId
	 * @return
	 */
	public List<CourseOrder> findPaidOrderByClassId(Integer classOrderId) {
		if(classOrderId == null){
			return null;
		}else{
			return dao.find("select * from crm_courseorder where `status`=1 and classorderid=?",classOrderId);
		}
	}
	
	/**
	 * 根据班次ID获取订单
	 * @param classOrderid
	 * @return
	 */
	public List<CourseOrder> findOrderByClassId(Integer classOrderid){
		if(classOrderid == null){
			return null;
		}else{
			return dao.find("select DISTINCT * from crm_courseorder where classorderid=?",classOrderid);
		}
	}

	/**
	 * 获取用户已购买的订单
	 * @param studentId
	 * @return
	 */
	public List<CourseOrder> findByStudentId(String studentId) {
		return StringUtils.isEmpty(studentId)?null:dao.find("SELECT * FROM crm_courseorder WHERE delflag=0 and studentid=?",Integer.parseInt(studentId));
	}

	/**
	 * 1对1 购买 课程记录
	 * @param studentid
	 * @return
	 */
	public List<CourseOrder> findByvipId(String studentid) {
		String sql = "select * from crm_courseorder where studentid = ? and teachtype=1 and `status` !=0 and delflag=0";
		List<CourseOrder> list = dao.find(sql, studentid);
		for(CourseOrder order : list){
			order.put("subjectName",Subject.dao.getSubjectNameByIds(order.getStr("SUBJECTIDS").replace("\\|", ",")));
		}
		return list;
	}

	/**
	 * 获取没有消耗完的订单
	 * @param studentId
	 * @return
	 */
	public List<CourseOrder> getCanuseOrder(String studentId,String subjectId) {
		String sql = "select * from crm_courseorder where delflag=0 and remainclasshour!=0 and studentid = ? and subjectid=?";
		List<CourseOrder> list = dao.find(sql, Integer.parseInt(studentId),Integer.parseInt(subjectId));
		return list;
	}
	/**
	 * 获取剩余课时数
	 * @param studentId
	 * @param subjectId
	 * @return
	 */
	public double getRemainHour(String studentId,String subjectId) {
		String sql = "select SUM(remainclasshour) remainhour from crm_courseorder where delflag=0 and remainclasshour!=0 and studentid = ? and subjectid = ?";
		return Db.queryDouble(sql,Integer.parseInt(studentId),Integer.parseInt(subjectId))==null?0:Db.queryDouble(sql,Integer.parseInt(studentId),Integer.parseInt(subjectId));
	}

	/**
	 * 获取学生购买的一对一课程
	 * @param studentId
	 * @return
	 */
	public List<Course> getCourseByStudentId(Integer studentId){
		List<CourseOrder> orderList = CourseOrder.dao.find("SELECT * FROM crm_courseorder WHERE delflag=0 and teachtype=1 and studentid=?",studentId);
		StringBuffer subjectIds = new StringBuffer();
		for(CourseOrder o : orderList){
			subjectIds.append(o.getStr("subjectids").replace("|", ",")).append(",");
		}
		subjectIds = subjectIds.deleteCharAt(subjectIds.length() - 1);
		if(subjectIds.toString().indexOf(",")==0){
			return Course.dao.find("select * from course where subject_id in("+subjectIds.toString().replaceFirst(",", "")+") and STATE=0 order by subject_id");
		}else{
			return Course.dao.find("select * from course where subject_id in("+subjectIds.toString()+") and STATE=0 order by subject_id");
		}
	}
	/**
	 * 获取学生预购的一对一总课时
	 * @param studentId
	 * @return
	 */
	public double getVIPzks(Integer studentId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=1 and studentid = ?";
		return Db.queryDouble(sql,studentId)==null?0:Db.queryDouble(sql,studentId);
	}
	
	/**
	 * 获取学生已交费和欠费的一对一总课时
	 * @param studentId
	 * @return
	 */
	public double getCanUseVIPzks(Integer studentId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=1 and `status`!=0 and studentid=? ";
		return Db.queryDouble(sql,studentId)==null?0:Db.queryDouble(sql,studentId);
	}
	/**
	 * 获取学生已交费和欠费的一对一总课时
	 * @param studentId
	 * @param subjectId
	 * @return
	 */
	public double getCanUseVIPzks(Integer studentId,Integer subjectId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=1 and `status`!=0 and studentid=? AND FIND_IN_SET(?,REPLACE(subjectids,'|',','))";
		return Db.queryDouble(sql,studentId,subjectId)==null?0:Db.queryDouble(sql,studentId,subjectId);
	}
	
	public List<CourseOrder> findOrderByStudentId(String studentId) {
		return StringUtils.isEmpty(studentId)?null:dao.find("SELECT * FROM crm_courseorder WHERE delflag=0 and teachtype=1 and studentid=?",Integer.parseInt(studentId));
	}

	public List<CourseOrder> findStudentClassOrder(Integer stuid) {
		String sql = "select studentid,classorderid from crm_courseorder where studentid=? and teachtype=2 ";
		List<CourseOrder> list = dao.find(sql, stuid);
		return list;
	}

	/**
	 * 获取学生已交费的一对一总课时
	 * @param studentId
	 * @return
	 */
	public double getPaidVIPzks(Integer studentId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=1 and `status`=1 and studentid=? ";
		double zks = Db.queryDouble(sql,studentId)==null?0:Db.queryDouble(sql,studentId);
		return zks;
	}
	/**
	 * 获取学生已交费的一对一总课时
	 * @param studentId
	 * @param subjectId
	 * @return
	 */
	public double getPaidVIPzks(Integer studentId,Integer subjectId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=1 and `status`=1 and studentid=? and FIND_IN_SET(?,REPLACE(subjectids,'|',','))";
		double zks = Db.queryDouble(sql,studentId,subjectId)==null?0:Db.queryDouble(sql,studentId,subjectId);
		return zks;
	}
	
	/**
	 * 获取学生欠费的一对一总课时
	 * @param studentId
	 * @return
	 */
	public double getArrearVIPzks(Integer studentId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=1 and `status`=2 and studentid=? ";
		double zks = Db.queryDouble(sql,studentId)==null?0:Db.queryDouble(sql,studentId);
		return zks;
	}
	
	/**
	 * 获取学生已交费的小班总课时
	 * @param studentId
	 * @return
	 */
	public double getPaidBanzks(Integer studentId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=2 and `status`=1 and studentid=? ";
		double zks = Db.queryDouble(sql,studentId)==null?0:Db.queryDouble(sql,studentId);
		return zks;
	}
	
	/**
	 * 获取学生欠费的小班总课时
	 * @param studentId
	 * @return
	 */
	public double getArrearBanzks(Integer studentId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and teachtype=2 and `status`=2 and studentid=? ";
		double zks = Db.queryDouble(sql,studentId)==null?0:Db.queryDouble(sql,studentId);
		return zks;
	}

	/**
	 * 获取学生购买一对一的科目
	 * @param studentId
	 * @return
	 */
	public List<Subject> findSubjectByStudentId(Integer studentId) {
		List<CourseOrder> orderList = CourseOrder.dao.find("SELECT id,subjectids FROM crm_courseorder WHERE delflag=0 and teachtype=1 and studentid=?",studentId);
		List<Integer> subjectIdList = new ArrayList<Integer>();
		List<Subject> list = new ArrayList<Subject>();
		for(CourseOrder o : orderList){
			String subjectIds[] = o.getStr("subjectids").split("\\|");
			for(String subjectId : subjectIds){
				subjectIdList.add(Integer.parseInt(subjectId));
			}
		}
		if(subjectIdList.size()>0){
			String subjectIds = subjectIdList.toString().replace("[", "").replace("]", "");
			list = Subject.dao.findByIds(subjectIds);
		}
		return list;
	}
	
	public String findStudentNamesByClassIds(String classorderid){
		String sql ="select distinct stu.id,stu.real_name from crm_courseorder left join account stu on stu.id=crm_courseorder.studentid where crm_courseorder.classorderid = ? ";
		List<CourseOrder> list = CourseOrder.dao.find(sql, classorderid);
		StringBuffer sb = new StringBuffer("");
		if(list!=null){
			for(CourseOrder co:list){
				sb.append(" ").append(co.getStr("real_name"));
			}
		}else{
			sb.append("无");
		}
		return sb.toString();
	}
	

	public double getWeekOrder(String date1, String date2) {
//		Long weekOrder = Db.queryLong("select sum(classhour) from crm_courseorder where createtime <='"+date+"'");
//		return weekOrder;
		String sql = "select sum(classhour) from crm_courseorder where createtime >='"+date1+"' and createtime<='"+date2+"'";
		double weekOrder = Db.queryDouble(sql)==null?0:Db.queryDouble(sql);
		return weekOrder;
	}

	public double getKeShi(String date1, String date2) {
//		BigDecimal total = Db.queryBigDecimal("select sum(classhour) from crm_courseorder where createtime <='"+date+"'");
//		return total==null?0:total.doubleValue();
		String sql = "select sum(classhour) from crm_courseorder where createtime >='"+date1+"'and createtime <='"+date2+"'";
		double zks = Db.queryDouble(sql)==null?0:Db.queryDouble(sql);
		return zks;
	}
	
	public Long getAmountDingdan() {
		Long total = Db.queryLong("SELECT COUNT(*) FROM crm_courseorder co LEFT JOIN account stu ON stu.Id=co.studentid WHERE co.status=1");
		return total;
	}

	public double getWeek(String date1, String date2) {
		Long week = Db.queryLong("SELECT COUNT(*) FROM crm_courseorder co LEFT JOIN account stu ON stu.Id=co.studentid WHERE co.status=1 and stu.CREATE_TIME >='"+date1+"'");
		return week;
	}

	/**
	 * 订单总数(x/单)
	 * @param date1
	 * @param date2
	 * @param integer
	 * @return
	 */
	public double getMonth(String date1, String date2, Integer integer) {
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" SELECT COUNT(1) FROM crm_courseorder co LEFT JOIN account stu ON stu.Id=co.studentid ");
		Long total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join student_kcgw ak ON stu.Id = ak.student_id ");
		}
		sql.append(" WHERE co.delflag=0 AND co.status=1 and stu.CREATE_TIME >= ? and stu.CREATE_TIME <= ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryLong(sql.toString(),date1,date2,integer);
		}
		
		else{
			total = Db.queryLong(sql.toString(),date1,date2);
		}
		
		return  total==null?0:total.doubleValue();
	}

	public double getThmonth(String date1) {
		String sql = "SELECT COUNT(*) FROM crm_courseorder co LEFT JOIN account stu ON stu.Id=co.studentid WHERE co.status=1 and stu.CREATE_TIME >='"+date1+"'";
		Long m = Db.queryLong(sql);
		return m;
	}

	public double getYear(String firstDay, String now) {
		String sql = "SELECT COUNT(*) FROM crm_courseorder co LEFT JOIN account stu ON stu.Id=co.studentid WHERE co.status=1 and stu.CREATE_TIME >='"+firstDay+"' and stu.CREATE_TIME<='"+now+"'";
		Long m = Db.queryLong(sql);
		return m;
	}
	
	public long getQianfeiCount(String stuid){
		String sql = "select count(1) counts from crm_courseorder co where co.status=2 and co.studentid = ? ";
		CourseOrder co = CourseOrder.dao.findFirst(sql, stuid);
		if(co==null){
			return 0;
		}else{
			return co.getLong("counts");
		}
		
	}
	
	public List<CourseOrder> queryStudentAllCourseOrders(String stuid){
		if(StringUtils.isEmpty(stuid)){
			return null;
		}else{
			String sql = "select stu.real_name,co.Id ,co.createtime,co.totalsum,co.realsum,co.classhour,co.avgprice,co.teachtype,0 usedhours,0 planedhours,co.status, \n" +
					"co.classorderid,corder.classNum,co.studentid,co.deposit,co.subjectids,co.version,IFNULL(plan.planHour,0) planedHours,IFNULL(used.usedHour,0) usedHours\n" +
					"from crm_courseorder co \n" +
					"left join account stu on stu.id=co.studentid  \n" +
					"left join class_order corder on co.classorderid = corder.id \n" +
					"left join \n" +
					"(SELECT ab.courseorderid,SUM(ab.classhour) usedHour from account_book ab \n" +
					"LEFT JOIN courseplan cp ON ab.courseplanid=cp.Id\n" +
					"where ab.accountid=? AND ab.operatetype=4 AND ab.`status`=0 AND cp.COURSE_TIME<=? GROUP BY ab.courseorderid) used\n" +
					"ON co.id=used.courseorderid\n" +
					"left join \n" +
					"(SELECT ab.courseorderid,SUM(ab.classhour) planHour from account_book ab \n" +
					"LEFT JOIN courseplan cp ON ab.courseplanid=cp.Id\n" +
					"where ab.accountid=? AND ab.operatetype=4 AND ab.`status`=0 GROUP BY ab.courseorderid) plan\n" +
					"ON co.id=plan.courseorderid\n" +
					"where stu.id = ? order by co.createtime asc";
			return dao.find(sql, stuid,ToolDateTime.getCurDate()+" 23:59:59",stuid,stuid);
		}
	}
	public List<CourseOrder> getAllFeeMessage(String studentid){
		return dao.find("select * from crm_courseorder where studentid = ? ", studentid);
	}
	/***
	 * 查询学生购买过所有课程的订单
	 * @param studentId
	 * @param subjectid
	 * @return
	 */
	public List<CourseOrder> getOrderByStudentidAndSubjectid(String studentId, String subjectid) {
		String sql = "select * from crm_courseorder where studentid  = "+studentId+" and delflag=0 and `status`!=0 and classhour>0 and  LOCATE( CONCAT('|', "+subjectid+", '|') , CONCAT('|', subjectids,'|')) > 0 order by createtime ";
		List<CourseOrder> orderList = dao.find(sql);
		if(orderList == null || orderList.size()==0){
			sql = "select * from crm_courseorder where studentid  = "+studentId+" and delflag=0 and `status`!=0 and classhour>0 order by createtime ";
			orderList = dao.find(sql);
			if(orderList == null || orderList.size()==0){
				sql = "select * from crm_courseorder where studentid  = "+studentId+" and delflag=0 and `status`!=0 and classhour=0 order by createtime ";
				orderList = dao.find(sql);
				double ypks = CoursePlan.coursePlan.getYpksForVIP(Integer.parseInt(studentId));
				for(CourseOrder o : orderList){
					o.set("classhour", ypks);
					break;
				}
			}
		}
		return orderList;
	}
	/**
	 * 获取已购课学生
	 * @param campusSql
	 * @return
	 */
	public String getArrearStudentIds() {
		String sql="select studentid from crm_courseorder where  delflag=0 and `status`=2 GROUP BY studentid ";
		List<CourseOrder> colist = CourseOrder.dao.find(sql);
		StringBuffer sf = new StringBuffer();
		if(colist.size()>0){
			for(CourseOrder co :colist){
				sf.append(co.getInt("studentid").toString()).append(",");
			}
		}
		if(sf.toString().equals("")){
			return "";
		}else{
			return sf.substring(0,sf.length()-1).toString();
		}
	}
	
	/**
	 * 获取欠费用户的订单
	 * @param studentId
	 * @return
	 */
	public List<CourseOrder> findArrearByStudentId(String studentId) {
		return StringUtils.isEmpty(studentId)?null:dao.find("SELECT * FROM crm_courseorder WHERE delflag=0 and `status`=2 and studentid=?",Integer.parseInt(studentId));
	}

	/**
	 * 获取学生购买的班课订单
	 * @param studentId
	 * @param classOrderId
	 * @return
	 */
	public List<CourseOrder> getCourseOrders(Integer studentId, Integer classOrderId) {
		String orderSql = "select * from crm_courseorder co where co.studentid=? and co.classorderid=?";
		List<CourseOrder> courseOrderList = dao.find(orderSql, studentId,classOrderId);
		for(CourseOrder courseOrder : courseOrderList){
			courseOrder.put("coursePriceList",CoursePrice.dao.findByOrderId(courseOrder.getPrimaryKeyValue().toString()));
		}
		return courseOrderList;
	}

	/**
	 * 获取学生购买的某个班的课时
	 * @param studentId
	 * @param classOrderId
	 * @return
	 */
	public double getBanHour(Integer studentId, Integer classOrderId) {
		String sql = "select SUM(classhour) zks from crm_courseorder where delflag=0 and `status`!=0 and studentid = ? and classorderid = ?";
		return Db.queryDouble(sql,studentId,classOrderId)==null?0:Db.queryDouble(sql,studentId,classOrderId);
	}

	/**
	 * 获取1对1订单还有多少剩余可用课时
	 * @param courseOrderId
	 * @return
	 */
	public double getCanUseVIPzksByCourseOrderId(Integer courseOrderId) {
		CourseOrder courseOrder = dao.findById(courseOrderId);
		String sql = "select IFNULL(SUM(classhour),0) ks from account_book where courseorderid=? and operatetype=4";
		double usedHour = Db.queryDouble(sql,courseOrderId) == null?0:Db.queryDouble(sql,courseOrderId);
		double classHour = courseOrder.getDouble("classhour");
		return ToolArith.sub(classHour, usedHour);
	}

	/**
	 *  根据订单ID获取订单消耗信息和排课信息
	 * @param courseOrderId
	 * @return
	 */
	public CourseOrder getCourseOrderInfoById(Integer courseOrderId) {
		String sql = "select stu.real_name,co.Id ,co.createtime,co.totalsum,co.realsum,co.classhour,co.avgprice,co.teachtype,0 usedhours,0 planedhours,co.status, \n" +
				"co.classorderid,corder.classNum,co.studentid,co.deposit,co.subjectids,co.version,IFNULL(plan.planHour,0) planedHours,IFNULL(used.usedHour,0) usedHours\n" +
				"from crm_courseorder co \n" +
				"left join account stu on stu.id=co.studentid  \n" +
				"left join class_order corder on co.classorderid = corder.id \n" +
				"left join \n" +
				"(SELECT ab.courseorderid,SUM(ab.classhour) usedHour from account_book ab \n" +
				"LEFT JOIN courseplan cp ON ab.courseplanid=cp.Id\n" +
				"where ab.courseorderid=? AND ab.operatetype=4 AND ab.`status`=0 AND cp.COURSE_TIME<=? GROUP BY ab.courseorderid) used\n" +
				"ON co.id=used.courseorderid\n" +
				"left join \n" +
				"(SELECT ab.courseorderid,SUM(ab.classhour) planHour from account_book ab \n" +
				"LEFT JOIN courseplan cp ON ab.courseplanid=cp.Id\n" +
				"where ab.courseorderid=? AND ab.operatetype=4 AND ab.`status`=0 GROUP BY ab.courseorderid) plan\n" +
				"ON co.id=plan.courseorderid\n" +
				"where co.id=? order by co.createtime asc";
		return dao.findFirst(sql, courseOrderId,ToolDateTime.getCurDate()+" 23:59:59",courseOrderId,courseOrderId);
	}
}
