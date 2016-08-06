package com.momathink.finance.model;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.sys.account.model.Account;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.subject.model.Subject;

@Table(tableName = "crm_payment")
public class Payment extends BaseModel<Payment> {

	private static final long serialVersionUID = 3001059907319493514L;
	public static final Payment dao = new Payment();

	/**
	 * 根据课程订单获取交费记录
	 * @param orderId
	 * @return
	 */
	public List<Payment> findByOrderId(String orderId) {
		String sql = "SELECT cp.*,o.REAL_NAME operatorname,c.REAL_NAME confirmusername " + "FROM crm_payment cp "
				+ "LEFT JOIN account o ON cp.operatorid=o.Id " + "LEFT JOIN account c ON cp.confirmuserid=c.Id WHERE cp.orderid=?";
		return StringUtils.isEmpty(orderId) ? null : dao.find(sql, Integer.parseInt(orderId));
	}
	
	/**
	 * 获取用户已购买课时
	 * @param studentId
	 * @return
	 */
	public int getPaidClasshourByStudentId(String studentId){
		if(StringUtils.isEmpty(studentId)){
			return 0;
		}
		BigDecimal sumhour = Db.queryBigDecimal("SELECT SUM(classhour) FROM crm_payment WHERE ispay=1 AND studentid=?",Integer.parseInt(studentId));
		return sumhour==null?0:sumhour.intValue();
	}
	

	public List<Payment> getPaymentsByCourseOrderId(Integer orderid) {
		String sql = "select * from crm_payment where orderid = ? ";
		List<Payment> pays = Payment.dao.find(sql, orderid);
		return pays;
	}

	public List<Payment> findbyStudentId(Integer studentId, String valid) {

		if(studentId==null){
			return null;
		}else{
			StringBuffer sql = new StringBuffer("select * from crm_payment p where p.studentid = ? ");
			if(!StringUtils.isEmpty(valid)){
				sql.append(" and p.ispay = '"+valid+"'");
			}
			List<Payment> list = Payment.dao.find(sql.append(" order by createtime desc").toString(), studentId);
			return list;
		}
	
	}

	public List<Payment> findbyStuIdAndTeachType(String studentId, int teachType) {
		if(StringUtils.isEmpty(studentId)){
			return null;
		}else{
			String sql = "select * from crm_payment p where p.studentid = ? and teachtype =? order by p.createtime";
			return Payment.dao.find(sql, Integer.parseInt(studentId),teachType);
		}
	}

	/**
	 * 跟进订单ID获取已支付金额
	 * @param orderId
	 * @return
	 */
	public double getTotalFeeByOrderId(Integer orderId) {
		if(orderId == null){
			return 0;
		}else{
			BigDecimal total = Db.queryBigDecimal("SELECT SUM(amount) FROM crm_payment WHERE orderid=?",orderId);
			return total==null?0:total.doubleValue();
		}
	}
	
	/**
	 * 总收入
	 * @return
	 */
	public double getAmount() {
		
		BigDecimal total = Db.queryBigDecimal("SELECT SUM(cp.amount) FROM crm_payment cp LEFT JOIN account stu ON cp.id=studentid where cp.ispay=1");
			return total==null?0:total.doubleValue();
		
	}
	
	/**
	 * 周收入
	 * @param date2 
	 * @param date 
	 * @param sysuserId
	 * @param date
	 * @param string 
	 * @param string 
	 * @return
	 */
	
	public double getWeek(String date1, String date2){
//		BigDecimal total = Db.queryBigDecimal("select sum(amount) from crm_payment where paydate >='"+date+"'");
//		return  total==null?0:total.doubleValue();
		System.out.println("++++++++"+date1);
		System.out.println(date2);
		BigDecimal total = Db.queryBigDecimal("select sum(amount) from crm_payment where paydate >='"+date1 +"' and paydate <='"+ date2+"'");
		return total==null?0:total.doubleValue();
	}
	
	/**
	 * 月收入
	 * @param end 
	 * @param start 
	 * @param sysuserId
	 * @param date
	 * @return
	 */
	
	
	/**
	 * 季度收入
	 * @param string 
	 * @param date 
	 * @param sysuserId
	 * @param date
	 * @param string 
	 * @return
	 */
	
	public double getThmonth(String date1) {
//		return total==null?0:total.doubleValue();
//		String sql = "select sum(classhour) from crm_courseorder where createtime >='"+date1+"'and createtime <='"+date2+"'";
//		double zks = Db.queryDouble(sql)==null?0:Db.queryDouble(sql);
//		return zks;
//		String sql = "select sum(amount) from crm_payment where paydate >='"+date1 +"'";
//		BigDecimal total = Db.queryBigDecimal("select count(amount) from crm_payment where paydate >='"+date1 +"' and paydate <='"+ date2+"'");
//		BigDecimal m = Db.queryBigDecimal(sql);
//		return m;
		System.out.println(date1+"+++"+date1);
		BigDecimal total = Db.queryBigDecimal("select sum(amount) from crm_payment where paydate >='"+date1 +"'");
		
		return  total==null?0:total.doubleValue();
	}
	/**
	 * 年收入
	 * @param date 
	 * @param sysuserId
	 * @param date
	 * @param now 
	 * @return
	 */
	
	public double getYear(String date, String now){
		
	BigDecimal total = Db.queryBigDecimal("select sum(amount) from crm_payment where paydate >='"+date+"' and paydate<='"+now+"'");
		
		return total==null?0:total.doubleValue();
	}
	
	public String getIncome(Integer sysuserId,String date){
		try{
			//----------------------
			String sql = "select sum(amount) as paysum from crm_payment pay "
					+ " left join account stu on pay.studentid=stu.ID "
					+ " left join crm_opportunity opp on opp.id=stu.opportunityid "
					+ " where pay.paydate>= ?  ";
			
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			if(!Role.isAdmins(sysuser.getStr("Roleids"))){
				sql += " and opp.scuserid = "+sysuserId;
			}
			String sum;
			Payment list = dao.findFirst(sql, date);
			if(list==null){
				sum = "0";
			}else{
				if(list.get("paysum")==null){
					sum = "0";
				}else{
					sum = list.get("paysum").toString();
				}
				
			}
			return sum;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public List<Payment> getArrearsList() {
		String sql = " SELECT cpay.id,cpay.amount,cco.subjectids,cco.teachtype,clo.classNum,stu.REAL_NAME from crm_payment cpay LEFT JOIN account  stu on stu.Id = cpay.studentid "
				+ " left join crm_courseorder cco on cco.id = cpay.orderid "
				+ " left join class_order clo on clo.id = cco.classorderid  "
				+ " WHERE cpay.ispay=0 ";
		List<Payment> list = dao.find(sql);
		for(Payment payment : list){
			payment.put("subject_name", Subject.dao.getSubjectNameByIds(payment.getStr("subjectids")));
		}
		return list;
	}

	public boolean hasNotPayCount(Integer orderid) {
		long count = Db.queryLong("select count(1) from crm_payment where ispay=0 and orderid=?",orderid);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 查询交费记录次数
	 * @param orderid
	 * @return
	 */
	public long getPayCount(Integer orderid) {
		long count = Db.queryLong("select count(1) from crm_payment where orderid=?",orderid);
		return count;
	}
	
	/** //收入
	 * @param date
	 * @param date2 
	 * @return
	 */
	public double getMonth(Integer uid, String date1, String date2) {
		Account user = Account.dao.findById(uid);
		StringBuffer sql = new  StringBuffer(" select sum(amount) from crm_payment cp ");
		BigDecimal total = null;
		
		if( Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join account a ON cp.studentid = a.id left join student_kcgw ak ON a.Id = ak.student_id ");
		}
		sql.append(" where cp.ispay=1 and cp.paydate >= ? and cp.paydate<= ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryBigDecimal(sql.toString(),date1,date2,uid);
		}
		
		else{
			total = Db.queryBigDecimal(sql.toString(),date1,date2);
		}
		
		return  total==null?0:total.doubleValue();
	}

	public Long getPayAmountOrder() {
		Long totalPay = Db.queryLong("SELECT COUNT(*) FROM account stu  LEFT JOIN crm_payment cp ON cp.studentid = stu.Id WHERE cp.ispay = 1");
	
		return totalPay;
	}

	public double getPayMoney() {
		BigDecimal paymoney = Db.queryBigDecimal("SELECT SUM(amount) FROM crm_payment cp LEFT JOIN account stu ON cp.studentid = stu.Id WHERE cp.ispay = 1");
		
		return paymoney==null?0:paymoney.doubleValue();
	}

	public Long PayweekOrder(String date) {
		Long weeklPay = Db.queryLong("SELECT COUNT(*) FROM account stu  LEFT JOIN crm_payment cp ON cp.studentid = stu.Id WHERE cp.ispay = 1 and CREATE_TIME >='"+date+"'");
		
		return weeklPay;
	}
	
	/**
	 * //付款总数(x/单)
	 * @param date
	 * @param integer
	 * @return
	 */
	public Long PaymonthOrder(String date, Integer integer) {
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" SELECT COUNT(1) FROM account stu  LEFT JOIN crm_payment cp ON cp.studentid = stu.Id ");
		Long total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join student_kcgw ak ON stu.Id = ak.student_id ");
		}
		sql.append(" WHERE cp.ispay = 1 and CREATE_TIME >= ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryLong(sql.toString(),date,integer);
		}else{
			total = Db.queryLong(sql.toString(),date);
		}
		
		return  total;
		
	}

	public Long PaytmonthOrder(String date) {
		Long tmonthlPay = Db.queryLong("SELECT COUNT(*) FROM account stu  LEFT JOIN crm_payment cp ON cp.studentid = stu.Id WHERE cp.ispay = 1 and CREATE_TIME >='"+date+"'");
		
		return tmonthlPay;
	}

	public Long PaytyearOrder(String date) {
		Long yearPay = Db.queryLong("SELECT COUNT(*) FROM account stu  LEFT JOIN crm_payment cp ON cp.studentid = stu.Id WHERE cp.ispay = 1 and CREATE_TIME >='"+date+"'");
		
		return yearPay;	
	}

	public double getPayweektotalMoney(String date) {
		BigDecimal payweekmoney = Db.queryBigDecimal("SELECT SUM(amount) FROM crm_payment cp LEFT JOIN account stu ON cp.studentid = stu.Id WHERE cp.ispay = 1 and CREATE_TIME>='"+date+"'");
		
		return payweekmoney==null?0:payweekmoney.doubleValue();
	}
	/**
	 * //付款总数(x/RMB)
	 * @param date
	 * @param integer
	 * @return
	 */
	public double getPaymonthtotalMoney(String date, Integer integer) {
		Account user = Account.dao.findById(integer);
		StringBuffer sql = new  StringBuffer(" SELECT SUM(amount) FROM crm_payment cp LEFT JOIN account stu ON cp.studentid = stu.Id ");
		BigDecimal total = null;
		
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" left join student_kcgw ak ON stu.Id = ak.student_id ");
		}
		sql.append(" WHERE cp.ispay = 1 and CREATE_TIME >= ? ");
		if(Role.isKcgw(user.getStr("roleids"))){//课程顾问
			sql.append(" and ak.kcgw_id = ?");
			total = Db.queryBigDecimal(sql.toString(),date,integer);
		}else{
			total = Db.queryBigDecimal(sql.toString(),date);
		}
		
		return  total==null?0:total.doubleValue();
	}

	public double getPaytmonthtotalMoney(String date) {
		BigDecimal payweekmoney = Db.queryBigDecimal("SELECT SUM(amount) FROM crm_payment cp LEFT JOIN account stu ON cp.studentid = stu.Id WHERE cp.ispay = 1 and CREATE_TIME>='"+date+"'");
		
		return payweekmoney==null?0:payweekmoney.doubleValue();
	}

	public double getPaytyeartotalMoney(String date) {
		BigDecimal payweekmoney = Db.queryBigDecimal("SELECT SUM(amount) FROM crm_payment cp LEFT JOIN account stu ON cp.studentid = stu.Id WHERE cp.ispay = 1 and CREATE_TIME>='"+date+"'");
		
		return payweekmoney==null?0:payweekmoney.doubleValue();
	}

	public List<Payment> getArrearsList(String campusSql) {
		String sql = " SELECT cpay.id,cpay.amount,cco.subjectids,cco.teachtype,clo.classNum,stu.REAL_NAME from crm_payment cpay LEFT JOIN account  stu on stu.Id = cpay.studentid "
				+ " left join crm_courseorder cco on cco.id = cpay.orderid "
				+ " left join class_order clo on clo.id = cco.classorderid  "
				+ " left join account a on a.id = cpay.studentid  "
				+ " WHERE cpay.ispay=0 ";
		
		List<Payment> list = dao.find(sql+campusSql);
		for(Payment payment : list){
			payment.put("subject_name", Subject.dao.getSubjectNameByIds(payment.getStr("subjectids")));
		}
		return list;
	}

	public Payment findByOrderId(Integer orderId) {
		String sql = "SELECT cp.* FROM crm_payment cp WHERE cp.orderid=?";
		return dao.findFirst(sql, orderId);
	}

}
