package com.momathink.teaching.student.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ExcelExportUtil;
import com.momathink.common.tools.ExcelExportUtil.Pair;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.finance.model.CourseOrder;
import com.momathink.finance.model.Payment;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.AccountCampus;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.course.service.CourseplanService;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.student.model.StudentKcgw;
import com.momathink.teaching.subject.model.Subject;


public class StudentService extends BaseService {

	private static Logger log = Logger.getLogger(StudentService.class);
	private CourseplanService coursePlanService = new CourseplanService();
	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	@SuppressWarnings("unchecked")
	public void list(SplitPage splitPage) {
		log.debug("学生管理：分页处理");
		String select = "select s.*,c.campus_name campusname,sc.real_name scusername,jw.real_name jwusername ";
		splitPageBase(splitPage, select);
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> list = page.getList();
		for (Record r : list) {
			r.set("zksvip", CourseOrder.dao.getCanUseVIPzks(r.getInt("Id")));
			r.set("ypksvip", CoursePlan.coursePlan.getYpksForVIP(r.getInt("Id")));
			r.set("ysksvip", CoursePlan.coursePlan.getYsksForVIP(r.getInt("Id")));
			r.set("classList", ClassOrder.dao.getClassOrderByStudentId(r.getInt("Id")));
			r.set("kcgwList", StudentKcgw.dao.getKcgwByStudentId(r.getInt("Id")));
		}
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from account s "
				+ "left join campus c on s.campusid=c.id "
				+ "left join account sc on s.scuserid=sc.id "
				+ "left join account jw on s.jwuserid=jw.id "
				+ "where s.USER_TYPE=1 and s.STATE=0 ");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "state":
				formSqlSb.append(" and s.state = ?");
				paramValue.add(Integer.parseInt(value));
				break;
			case "studentname":// 学生姓名
				formSqlSb.append(" and s.real_name like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "email":// 邮箱
				formSqlSb.append(" and s.email like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "mobile":// 手机
				formSqlSb.append(" and s.mobile like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "telephone":// 电话
				formSqlSb.append(" and s.telephone like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "qq":// QQ
				formSqlSb.append(" and s.qq like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "beginDate"://查询开始日期
				formSqlSb.append(" and s.createtime>=? ");
				paramValue.add("'" + value + " 00:00:00'");
				break;
			case "endDate":// 结束日期
				formSqlSb.append(" and s.createtime<=? ");
				paramValue.add("'" + value + " 23:59:59'");
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" ORDER BY s.id DESC");
	}
	
	/**
	 * 保存学生*
	 * @param account
	 * @return
	 */
	@Before(Tx.class)
	public Integer save(Student account) {
		Integer id ;
		try {
			account.set("state", "0");
			account.set("create_time", new Date());
			account.set("roleids", "4,");
			account.set("user_type", 1);
			account.save();
			id=account.getPrimaryKeyValue();
		} catch (Exception e) {
			throw new RuntimeException("保存用户异常");
		}
		return id;
	}

	@Before(Tx.class)
	public void update(Student account) {
		try {
			account.set("update_time", new Date());
			account.set("roleids", "4,");
			account.set("user_type", 1);
			account.update();
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常");
		}
	}

	/**
	 * 获取学生的剩余总课时
	 * @param studentId
	 * @return
	 */
	public float getSurplusClasshour(String studentId) {
		float zks = Payment.dao.getPaidClasshourByStudentId(studentId);
		float ypks = CoursePlan.coursePlan.getUseClasshour(studentId,null);//全部已用课时
		return zks-ypks;
	}

	public List<Student> queryStudentByMediator(Mediator mediator) {
		List<Student> list = Student.dao.findByMediatorId(mediator.getPrimaryKeyValue());
		for (Student student : list) {
			List<Payment> paymentList = Payment.dao.findbyStudentId(student.getPrimaryKeyValue(),"1");
			Date date = new Date();
			Map<String, Date> beforeWeek = ToolDateTime.getBeforeWeekDate(date);
			Map<String, Date> week = ToolDateTime.getWeekDate(date);
			String start = ToolDateTime.format(week.get("start"), ToolDateTime.pattern_ymd);
//			String end = ToolDateTime.format(week.get("end"), ToolDateTime.pattern_ymd);
			String bstart = ToolDateTime.format(beforeWeek.get("start"), ToolDateTime.pattern_ymd);
			String bend = ToolDateTime.format(beforeWeek.get("end"), ToolDateTime.pattern_ymd);
			Map<String, Float> szPlan = coursePlanService.queryCourseplanInfo(student.getPrimaryKeyValue(), bstart, bend);//上周排课
			Map<String, Float> bzPlan = coursePlanService.queryCourseplanInfo(student.getPrimaryKeyValue(), start, ToolDateTime.format(date, ToolDateTime.pattern_ymd));//本周排课
			Map<String, Float> ljPlan = coursePlanService.queryCourseplanInfo(student.getPrimaryKeyValue(), null,ToolDateTime.format(date, ToolDateTime.pattern_ymd));//累计排课
			if (paymentList != null && paymentList.size() > 0) {
				student.put("paymentList", paymentList);
				student.put("recordNumber", paymentList.size());
				double moneyTotal = 0;
				double jieshuTotal = 0;
				for (Payment payment : paymentList) {
					moneyTotal = ToolArith.add(moneyTotal, payment.getBigDecimal("amount").doubleValue());
					jieshuTotal = ToolArith.add(jieshuTotal,payment.getDouble("classhour"));
				}
				student.put("moneyTotal", moneyTotal);
				student.put("jieshuTotal", jieshuTotal);
				student.put("szxh", (double) (szPlan.get("vip") + szPlan.get("xb")));
				student.put("szyj", (szPlan.get("vip") + szPlan.get("xb")) );
				student.put("bzxh", (double) (bzPlan.get("vip") + bzPlan.get("xb")));
				student.put("ljks", (ljPlan.get("vip") + ljPlan.get("xb")));
				student.put("ljxh", (double) (ljPlan.get("vip") + ljPlan.get("xb")));
				student.put("ljyj", (ljPlan.get("vip") + ljPlan.get("xb")));
			} else {
				student.put("recordNumber", 0);
				student.put("moneyTotal", 0);
				student.put("jieshuTotal", 0);
				student.put("szxh", 0.00);
				student.put("szyj", 0.0f);
				student.put("bzxh", 0.00);
				student.put("ljks", 0.0f);
				student.put("ljxh", 0.00);
				student.put("ljyj", 0.00);
			}
			student.put("szks", szPlan.get("vip") + szPlan.get("xb"));
			student.put("bzks", bzPlan.get("vip") + bzPlan.get("xb"));
		}
		return list;
	
	}

	public List<Course> getStuCourseName(String classType) {
		try{
			String[] couid = classType.replace("|", ",").split(",");
			List<Course> list = new ArrayList<Course>();
			for(int i=0;i<couid.length;i++){
				Course course = Course.dao.findById(couid[i]);
				if(course!=null){
					list.add(course);
				}
			}
			return list;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public void queryMyStudents(SplitPage splitPage,Integer sysuserId) {
		 List<Object> paramValue = new ArrayList<Object>();
		if(sysuserId!=null){
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			StringBuffer select = new StringBuffer("select s.Id,s.REAL_NAME,s.opportunityid,s.STATE,s.create_time,s.TEL, d.real_name dudao,"
					+ " sc.real_name scusername,cc.real_name kcusername,m.realname mediatorname, IFNULL(b.zksvip,0) zksvip,xiaoban.classNum,"
					+ "IFNULL(b.kyksvip,0) kyksvip,IFNULL(c.zksxb,0) zksxb,IFNULL(c.kyksxb,0) kyksxb   " );
			StringBuffer formSql = 
					new StringBuffer("from account s  "
					+ " left join "
					+ " (select cpt.studentid,cod.id,GROUP_CONCAT(cod.classNum) classNum from crm_courseorder cpt "
					+ " left join class_order cod on cpt.classorderid = cod.id where cpt.teachtype =2 and cpt.status <> 0 ) xiaoban  "
					+ " on s.id = xiaoban.studentid \n" 
					+ "left join account d on s.SUPERVISOR_ID=d.id  left join account sc on s.scuserid=sc.id "
					+ " left join (SELECT GROUP_CONCAT(k.REAL_NAME) real_name,GROUP_CONCAT(ak.kcgw_id) kcgwids, ak.student_id  id "
					+ " FROM "
					+ " student_kcgw ak "
					+ " LEFT JOIN account a ON ak.student_id = a.Id "
					+ " LEFT JOIN (select * from account where "
					+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0) k ON k.Id = ak.kcgw_id "
					+ " GROUP BY a.Id) cc on s.id=cc.id  left join crm_mediator m on s.mediatorid=m.id  \n" 
					+ "left join \n" 
					+ "(SELECT a.studentid,SUM(a.classhour) zksvip,SUM(a.totalsum) zjevip,SUM(a.realsum) zjfvip,FLOOR(SUM(a.ks)) kyksvip\n" 
					+ "FROM (SELECT co.classhour,co.totalsum,co.realsum,co.studentid,IF(co.avgprice=0,co.classhour,cp.totalfee/co.avgprice) ks \n" 
					+ "FROM crm_courseorder co \n" 
					+ "LEFT JOIN (SELECT p.orderid,SUM(amount) totalfee FROM crm_payment p GROUP BY p.orderid) cp ON co.id=cp.orderid "
					+ " WHERE co.status<>0 and co.delflag=0 AND co.teachtype=1) a GROUP BY a.studentid) b \n" 
					+ "on s.id=b.studentid \n" 
					+ "left join \n" 
					+ "(SELECT a.studentid,SUM(a.classhour) zksxb,SUM(a.totalsum) zjexb,SUM(a.realsum) zjfxb,FLOOR(SUM(a.ks)) kyksxb \n" 
					+ "FROM (SELECT co.classhour,co.totalsum,co.realsum,co.studentid,IF(co.avgprice=0,co.classhour,cp.totalfee/co.avgprice) ks \n" 
					+ "FROM crm_courseorder co \n" 
					+ "LEFT JOIN (SELECT p.orderid,SUM(amount) totalfee FROM crm_payment p GROUP BY p.orderid) cp ON co.id=cp.orderid "
					+ " WHERE co.status<>0 and co.delflag=0 AND co.teachtype=2) a GROUP BY a.studentid) c \n" 
					+ "on s.id=c.studentid ");
			 	if(Role.isAdmins(sysuser.getStr("roleids"))){//管理员
					formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0 ");
				}else{
					String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
					if(Role.isTeacher(sysuser.getStr("roleids"))){//老师
						formSql.append("\n LEFT JOIN ("
								+ "SELECT\n" +
								"	* from(\n" +
								"		(\n" +
								"			SELECT\n" +
								"				lspk.TEACHER_ID,\n" +
								"				lspk.STUDENT_ID\n" +
								"			FROM\n" +
								"				courseplan lspk\n" +
								"			WHERE\n" +
								"				lspk.class_id = 0\n" +
								"		)\n" +
								"		UNION ALL\n" +
								"			(\n" +
								"				SELECT\n" +
								"					lspk.TEACHER_ID,\n" +
								"					ab.account_ID student_id\n" +
								"				FROM\n" +
								"					courseplan lspk\n" +
								"				LEFT JOIN class_type ct ON lspk.class_id = ct.id\n" +
								"				LEFT JOIN account_banci ab ON ct.id = ab.banci_id\n" +
								"				WHERE\n" +
								"					lspk.class_id <> 0\n" +
								"		GROUP BY ab.account_id	)\n" +
								"	) s\n" +
								"GROUP BY\n" +
								"	s.TEACHER_ID,\n" +
								"	s.student_ID"
								+ ") t "
								+ " ON s.Id=t.STUDENT_ID where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  and s.state = 0 AND t.TEACHER_ID=?");
						paramValue.add(sysuserId);
					}else if(Role.isDudao(sysuser.getStr("roleids"))||Role.isKcgw(sysuser.getStr("roleids"))||Role.isJiaowu(sysuser.getStr("roleids"))){//教务
						if(campusids != null){
							formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(campusids);
							if(sysuser.getInt("campusid")!=null){
								formSql.append(",").append(sysuser.getInt("campusid"));
							}
							formSql.append(") ");
						}else{
							if(sysuser.getInt("campusid")!=null){
								formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(sysuser.getInt("campusid")).append(") ");
							}
						}
					}else if(Role.isShichang(sysuser.getStr("roleids"))){//市场
						if(campusids != null){
							formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(campusids).append(") ");
						}else{
							formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.scuserid=?");
							paramValue.add(sysuserId);
						}
					}
				}
			
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			String state = queryParam.get("state");
			if(StringUtils.isEmpty(state))
				queryParam.put("state", "0");
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "state":
					formSql.append(" and s.state = ?");
					paramValue.add(Integer.parseInt(value));
					break;
				case "studentname":
					formSql.append(" and s.real_name like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "begindate":
					formSql.append(" and s.create_time >= ?");
					paramValue.add(value);
					break;
				case "enddate":
					formSql.append(" and s.create_time <= ?");
					paramValue.add(value);
					break;
				case "scuserid":
					formSql.append(" and s.scuserid = ? ");
					paramValue.add(Integer.parseInt(value));
					break;
				case "oppid":
					formSql.append(" and s.opportunityid = ? ");
					paramValue.add(Integer.parseInt(value));
					break;	
				case "kcuserid":
					formSql.append(" and CONCAT(',',cc.kcgwids,',') like ? ");
					paramValue.add("%,"+value+",%");
					break;
				case "supervisor_id":
					formSql.append(" and s.supervisor_id = ? ");
					paramValue.add(Integer.parseInt(value));
					break;
				case "classorderid":
					formSql.append(" and xiaoban.id = ? ");
					paramValue.add(Integer.parseInt(value));
					break;
				default:
					break;
				}
			}
			formSql.append(" ORDER BY s.id DESC");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			List<Record> list = page.getList();
			for (Record r : list) {
				r.set("ypksvip", CoursePlan.coursePlan.findFirst("SELECT IFNULL(SUM(t.class_hour),0) ypksvip FROM courseplan cp LEFT JOIN time_rank t ON cp.TIMERANK_ID=t.Id  "
						+ " WHERE cp.STATE=0 AND cp.PLAN_TYPE=0 AND cp.class_id=0 AND cp.STUDENT_ID= " + r.getInt("Id")).get("ypksvip"));
				r.set("ypksxb", CoursePlan.coursePlan.findFirst("SELECT IFNULL(SUM(t.class_hour),0) ypksxb FROM courseplan cp LEFT JOIN time_rank t ON cp.TIMERANK_ID=t.Id "
					+ " WHERE cp.STATE=0 AND cp.PLAN_TYPE=0 AND cp.class_id!=0 AND cp.STUDENT_ID= " + r.getInt("Id")).get("ypksxb"));
				String kyksvip = r.getDouble("kyksvip")==null?"0":r.getDouble("kyksvip").toString();
				String ypksvip = r.getBigDecimal("ypksvip")==null?"0":r.getBigDecimal("ypksvip").toString();
				String kyksxb = r.getDouble("kyksxb")==null?"0":r.getDouble("kyksxb").toString();
				String ypksxb = r.getBigDecimal("ypksxb")==null?"0":r.getBigDecimal("ypksxb").toString();
				r.set("kyksvip", ToolString.subZeroAndDot(kyksvip));
				r.set("ypksvip", ToolString.subZeroAndDot(ypksvip));
				r.set("kyksxb", ToolString.subZeroAndDot(kyksxb));
				r.set("ypksxb", ToolString.subZeroAndDot(ypksxb));
			}
			splitPage.setPage(page);
		}
	}
	
	public List<Record> queryMyStudentsToexcel(SplitPage splitPage,Integer sysuserId) {
		 List<Object> paramValue = new ArrayList<Object>();
		if(sysuserId!=null){
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			StringBuffer select = new StringBuffer("select op.contacter,s.PARENTS_EMAIL,s.PARENTS_TEL,s.QQ,s.REAL_NAME,s.opportunityid,s.STATE,DATE_FORMAT(s.create_time,'%Y-%m-%d') AS create_time,s.TEL, d.real_name dudao,"
					+ " sc.real_name scusername,cc.real_name kcusername,m.realname mediatorname, IFNULL(b.zksvip,0) zksvip,"
					+ "IFNULL(b.kyksvip,0) kyksvip,IFNULL(c.zksxb,0) zksxb,IFNULL(c.kyksxb,0) kyksxb  \n" );
			StringBuffer formSql = 
					new StringBuffer("from account s  \n" 
					+ "left join account d on s.SUPERVISOR_ID=d.id  left join account sc on s.scuserid=sc.id "
					+ "left join crm_opportunity op on s.opportunityid=op.id"
					+ " left join (SELECT GROUP_CONCAT(k.REAL_NAME) real_name,GROUP_CONCAT(ak.kcgw_id) kcgwids, ak.student_id  id "
					+ " FROM "
					+ " student_kcgw ak "
					+ " LEFT JOIN account a ON ak.student_id = a.Id "
					+ " LEFT JOIN (select * from account where "
					+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0) k ON k.Id = ak.kcgw_id "
					+ " GROUP BY a.Id) cc on s.id=cc.id  left join crm_mediator m on s.mediatorid=m.id  \n" 
					+ "left join \n" 
					+ "(SELECT a.studentid,SUM(a.classhour) zksvip,SUM(a.totalsum) zjevip,SUM(a.realsum) zjfvip,FLOOR(SUM(a.ks)) kyksvip\n" 
					+ "FROM (SELECT co.classhour,co.totalsum,co.realsum,co.studentid,IF(co.avgprice=0,co.classhour,cp.totalfee/co.avgprice) ks \n" 
					+ "FROM crm_courseorder co \n" 
					+ "LEFT JOIN (SELECT p.orderid,SUM(amount) totalfee FROM crm_payment p GROUP BY p.orderid) cp ON co.id=cp.orderid "
					+ " WHERE co.delflag=0 AND co.teachtype=1) a GROUP BY a.studentid) b \n" 
					+ "on s.id=b.studentid \n" 
					+ "left join \n" 
					+ "(SELECT a.studentid,SUM(a.classhour) zksxb,SUM(a.totalsum) zjexb,SUM(a.realsum) zjfxb,FLOOR(SUM(a.ks)) kyksxb \n" 
					+ "FROM (SELECT co.classhour,co.totalsum,co.realsum,co.studentid,IF(co.avgprice=0,co.classhour,cp.totalfee/co.avgprice) ks \n" 
					+ "FROM crm_courseorder co \n" 
					+ "LEFT JOIN (SELECT p.orderid,SUM(amount) totalfee FROM crm_payment p GROUP BY p.orderid) cp ON co.id=cp.orderid "
					+ " WHERE  co.delflag=0 AND co.teachtype=2) a GROUP BY a.studentid) c \n" 
					+ "on s.id=c.studentid ");
			 	if(Role.isAdmins(sysuser.getStr("roleids"))){//管理员
					formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0 ");
				}else{
					String fzxqids = Campus.dao.IsCampusFzr(sysuserId);
					if(fzxqids != null){
						formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(fzxqids).append(") ");
					}else{
						if(Role.isTeacher(sysuser.getStr("roleids"))){//老师
							formSql.append("\n LEFT JOIN (SELECT lspk.TEACHER_ID,lspk.STUDENT_ID FROM courseplan lspk "
									+ " GROUP BY lspk.TEACHER_ID,lspk.STUDENT_ID) t "
									+ " ON s.Id=t.STUDENT_ID where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  and s.state = 0 AND t.TEACHER_ID=?");
							paramValue.add(sysuserId);
						} else if(Role.isDudao(sysuser.getStr("roleids"))){//督导
							formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid=?");
							paramValue.add(sysuser.getInt("campusid"));
						}else if(Role.isJiaowu(sysuser.getStr("roleids"))){//教务
							String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
							if(campusids != null){
								formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(campusids);
								if(sysuser.getInt("campusid")!=null){
									formSql.append(",").append(sysuser.getInt("campusid"));
								}
								formSql.append(") ");
							}else{
								if(sysuser.getInt("campusid")!=null){
									formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(sysuser.getInt("campusid")).append(") ");
								}
							}
						}else if(Role.isKcgw(sysuser.getStr("roleids"))){//课程顾问
							String campusids = AccountCampus.dao.getCampusIdsByAccountId(sysuserId);
							if(campusids != null){
								formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(campusids).append(") ");
							}else{
								formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.kcuserid=?");
								paramValue.add(sysuserId);
							}
						}else if(Role.isShichang(sysuser.getStr("roleids"))){//市场
							String campusids = Campus.dao.IsCampusScFzr(sysuserId);
							if(campusids != null){
								formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.campusid in(").append(campusids).append(") ");
							}else{
								formSql.append("\n where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0  AND s.scuserid=?");
								paramValue.add(sysuserId);
							}
						}
					}
				}
			
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			String state = queryParam.get("state");
			if(StringUtils.isEmpty(state))
				queryParam.put("state", "0");
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "state":
					formSql.append(" and s.state = ?");
					paramValue.add(Integer.parseInt(value));
					break;
				case "studentname":
					formSql.append(" and s.real_name like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "begindate":
					formSql.append(" and s.create_time >= ?");
					paramValue.add(value);
					break;
				case "enddate":
					formSql.append(" and s.create_time <= ?");
					paramValue.add(value);
					break;
				case "scuserid":
					formSql.append(" and s.id in (select DISTINCT sk.student_id from student_kcgw sk where sk.kcgw_id=?) ");
					paramValue.add(Integer.parseInt(value));
					break;
				case "oppid":
					formSql.append(" and s.opportunityid = ? ");
					paramValue.add(Integer.parseInt(value));
					break;	
				case "kcuserid":
					formSql.append(" and CONCAT(',',cc.kcgwids,',') like ? ");
					paramValue.add("%,"+value+",%");
					break;
				case "supervisor_id":
					formSql.append(" and s.supervisor_id = ? ");
					paramValue.add(Integer.parseInt(value));
					break;
			
				default:
					break;
				}
			}
			formSql.append(" ORDER BY s.id DESC");
			List<Record> list =  Db.find(select.toString()+formSql.toString(),paramValue.toArray());
//			Page<Record> page = Db.paginate(splitPage.getPageNumber(), 1000000, select.toString(), formSql.toString(), paramValue.toArray());
//			List<Record> list = page.getList();
			for (Record r : list) {
				r.set("ypksvip", CoursePlan.coursePlan.findFirst("SELECT IFNULL(SUM(t.class_hour),0) ypksvip FROM courseplan cp LEFT JOIN time_rank t ON cp.TIMERANK_ID=t.Id  "
						+ " WHERE cp.STATE=0 AND cp.PLAN_TYPE=0 AND cp.class_id=0 AND cp.STUDENT_ID= " + r.getInt("Id")).get("ypksvip"));
				r.set("ypksxb", CoursePlan.coursePlan.findFirst("SELECT IFNULL(SUM(t.class_hour),0) ypksxb FROM courseplan cp LEFT JOIN time_rank t ON cp.TIMERANK_ID=t.Id "
					+ " WHERE cp.STATE=0 AND cp.PLAN_TYPE=0 AND cp.class_id!=0 AND cp.STUDENT_ID= " + r.getInt("Id")).get("ypksxb"));
				String kyksvip = r.getDouble("kyksvip")==null?"0":r.getDouble("kyksvip").toString();
				String ypksvip = r.getBigDecimal("ypksvip")==null?"0":r.getBigDecimal("ypksvip").toString();
				String kyksxb = r.getDouble("kyksxb")==null?"0":r.getDouble("kyksxb").toString();
				String ypksxb = r.getBigDecimal("ypksxb")==null?"0":r.getBigDecimal("ypksxb").toString();
				r.set("kyksvip", ToolString.subZeroAndDot(kyksvip));
				r.set("ypksvip", ToolString.subZeroAndDot(ypksvip));
				r.set("kyksxb", ToolString.subZeroAndDot(kyksxb));
				r.set("ypksxb", ToolString.subZeroAndDot(ypksxb));
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 导出数据
	 * @param response
	 * @param request
	 * @param member
	 */
	public void export(HttpServletResponse response, HttpServletRequest request, List<Record> members,String filename) {
		
		List<Pair> titles = new ArrayList<Pair>();
		titles.add(new Pair("REAL_NAME","姓名"));
		titles.add(new Pair("contacter","联系人"));
		titles.add(new Pair("STATE","状态"));
		titles.add(new Pair("create_time","创建时间"));
		titles.add(new Pair("QQ","QQ"));
		titles.add(new Pair("TEL","电话"));
		titles.add(new Pair("PARENTS_TEL","家长电话"));
		titles.add(new Pair("PARENTS_EMAIL","家长邮箱"));
		titles.add(new Pair("dudao","督导"));
		titles.add(new Pair("scusername","市场专员"));
		titles.add(new Pair("kcusername","课程顾问"));
		titles.add(new Pair("mediatorname","顾问"));
		titles.add(new Pair("zksvip","一对一预购课时"));
		titles.add(new Pair("kyksvip","一对一已购课时"));
		titles.add(new Pair("ypksvip","一对一已排课时"));
		titles.add(new Pair("zksxb","小班预购课时"));
		titles.add(new Pair("kyksxb","小班已购课时"));
		titles.add(new Pair("ypksxb","小班已排课时"));

		
		// 特殊处理
//		for (Member member : Member) {
//			
//		}
//		
		ExcelExportUtil.exportByRecord(response, request, filename, titles , members);
	}

	
	/**
	 * 某学生的所用订单
	 * @param studentid
	 * @return
	 */
	public List<CourseOrder> getStudentOrderlists(Integer userId) {
		String sql = "SELECT o.delflag,o.id,o.subjectids,o.createtime,o.`status`,o.teachtype,o.totalsum,o.realsum,o.rebate,o.classhour, "
				+ " (SELECT COUNT(1) FROM crm_payment p WHERE p.orderid=o.id) paycount, "
				+ " s.REAL_NAME studentname, "
				+ " (SELECT IFNULL(SUM(amount),0) FROM crm_payment p WHERE p.orderid=o.id AND p.ispay=1) paidamount, bc.classNum  "
				+ " FROM crm_courseorder o LEFT JOIN account s ON o.studentid=s.Id "
				+ " left join crm_opportunity opp on opp.id=s.opportunityid "
				+ " LEFT JOIN class_order bc ON o.classorderid=bc.id "
				+ " WHERE 1=1 ";
		SysUser sysuser = SysUser.dao.findById(userId);
		if(!Role.isAdmins(sysuser.getStr("roleids"))){
			sql +=" AND o.operatorid= "+userId;
		}
		List<CourseOrder> list = CourseOrder.dao.find(sql+" ORDER BY o.id desc ");
		for(CourseOrder o : list){
			o.put("subjectname", Subject.dao.getSubjectNameByIds(o.getStr("subjectids")));
		}
		return list;
	}

	/**
	 * 学生课程 统计
	 * @param studentid
	 * @return
	 */
	public Student showCourseUsedCount(String studentid) {
		Student student = Student.dao.findById(Integer.parseInt(studentid));
		try{
			List<CourseOrder> colist = CourseOrder.dao.findByStudentId(studentid);
			
			double zksvip = 0;
			double zksxb = 0;
			double ypvip = 0;
			double ypxb = 0;
			StringBuffer vorderids = new StringBuffer();
			StringBuffer xborderid = new StringBuffer();
			String classorderids = "";
			for(CourseOrder co : colist){
				if(co.getInt("teachtype")==1){
					zksvip+=co.getDouble("classhour");
					vorderids.append(co.getPrimaryKeyValue()).append(",");
				}else if(co.getInt("teachtype")==2){
					zksxb += co.getDouble("classhour");
					classorderids += ","+co.getInt("classorderid").toString();
					xborderid.append(co.getPrimaryKeyValue()).append(",");
				}
			}
			classorderids = classorderids.replaceFirst(",", "");
			if(classorderids.length()>0){
			StringBuffer classordersql = new StringBuffer();
			classordersql.append(" select classNum from class_order where id in (").append(classorderids).append(")");
			List<ClassOrder> orderlist = ClassOrder.dao.find(classordersql.toString());
			student.put("classordername", orderlist);//小班编号
			
			}
			if(vorderids!=null&&vorderids.length()>0){
				vorderids.replace(0, vorderids.length(), vorderids.substring(0, vorderids.length()-1));
			}
			if(xborderid!=null&&xborderid.length()>0){
				xborderid.replace(0, xborderid.length(), xborderid.substring(0, xborderid.length()-1));
			}
			
			List<CoursePlan> sumplan = CoursePlan.coursePlan.find("select count(1) count,sum(tr.class_hour) ypks ,cp.class_id from courseplan cp "
					+ " left join time_rank tr on tr.Id=cp.TIMERANK_ID where STUDENT_ID = ? group by class_id ",studentid);
			if(sumplan!=null && sumplan.size()>0){
				for(CoursePlan sum : sumplan){
					if(sum.getInt("class_id")==0){
						ypvip += sum.getBigDecimal("ypks").intValue();
					}else if(sum.getInt("class_id")!=0 && sum.getInt("class_id")!=null){
						ypxb += sum.getBigDecimal("ypks").intValue();
					}
				}
			}
			student.put("zksvip", zksvip);
			student.put("ypvip", ypvip);
			student.put("zksxb", zksxb);
			student.put("ypxb", ypxb);
			
			int vlength = 0;
			if(vorderids!=null&&vorderids.length()>0){
				StringBuffer vssql = new StringBuffer();
				vssql.append("SELECT a.ks chour,b.yp ypcourse,b.SUBJECT_ID subjectid,b.SUBJECT_NAME FROM\n" +
						"(SELECT o.studentid,SUM(o.classhour) ks FROM crm_courseorder o WHERE o.`status`!=0 AND o.studentid=").append(studentid).append(") a\n" +
						"LEFT JOIN\n" +
						"(SELECT cp.student_id,SUM(tr.class_hour) yp,cp.SUBJECT_ID,s.SUBJECT_NAME FROM courseplan cp LEFT JOIN time_rank tr ON cp.TIMERANK_ID=tr.Id\n" +
						"LEFT JOIN `subject` s ON cp.SUBJECT_ID=s.Id WHERE cp.class_id=0 AND cp.PLAN_TYPE=0 AND cp.STUDENT_ID=").append(studentid).append(" GROUP BY s.id) b\n" +
						"ON a.studentid=b.student_id") ;
				List<Subject> vsublist = Subject.dao.find(vssql.toString());	
				for(Subject subject:vsublist){
					StringBuffer vcsql = new StringBuffer();
					vcsql.append("SELECT a.ks chour,b.yp ypcourse,b.COURSE_ID courseid,b.COURSE_NAME FROM\n" +
							"(SELECT o.studentid,SUM(o.classhour) ks FROM crm_courseorder o WHERE o.`status`!=0 AND o.studentid=?) a\n" +
							"LEFT JOIN\n" +
							"(SELECT cp.student_id,SUM(tr.class_hour) yp,cp.COURSE_ID,s.COURSE_NAME FROM courseplan cp LEFT JOIN time_rank tr ON cp.TIMERANK_ID=tr.Id\n" +
							"LEFT JOIN `course` s ON cp.course_id=s.Id WHERE cp.class_id=0 AND cp.PLAN_TYPE=0 AND cp.STUDENT_ID=? AND cp.SUBJECT_ID = ? GROUP BY s.id) b\n" +
							"ON a.studentid=b.student_id") ;
					List<Course> vcublist = Course.dao.find(vcsql.toString(), studentid,studentid,subject.getInt("SUBJECTID"));
					subject.put("subjectlength", vcublist.size()+1);
					vlength += vcublist.size()+1;
					subject.put("vcourselist", vcublist);
				}
				student.put("vsubject", vsublist);
			}
			student.put("vlength", vlength+1);
			
			int xblength = 0;
			if(classorderids.length()>0){
				StringBuffer xbssql = new StringBuffer();
				xbssql.append("select (SELECT SUM(tr.class_hour) from courseplan cp LEFT JOIN time_rank tr ON tr.Id=cp.TIMERANK_ID "
						+ " WHERE cp.SUBJECT_ID = bc.subject_id AND cp.STUDENT_ID = acc.account_id and cp.class_id <> 0) ypcourse, "
						+ " sub.SUBJECT_NAME,bc.subject_id subid from banci_course bc "
						+ " left join subject sub on bc.subject_id = sub.id left join account_banci acc on acc.banci_id = bc.banci_id "
						+ "  where bc.banci_id in (").append(classorderids).append(") and acc.account_id= ? group by bc.subject_id ") ;
				List<Subject> xbsublist = Subject.dao.find(xbssql.toString(), studentid);	
				for(Subject sub: xbsublist){
					StringBuffer vcsql = new StringBuffer();
					vcsql.append("select (SELECT SUM(tr.class_hour) from courseplan cp LEFT JOIN time_rank tr on tr.Id=cp.TIMERANK_ID "
							+ " WHERE cp.COURSE_ID = bc.course_id AND cp.STUDENT_ID = acc.account_id and cp.class_id <> 0 ) ypcourse, "
							+ " bc.course_id, sub.COURSE_NAME from banci_course bc  LEFT JOIN account_banci acc ON acc.banci_id = bc.banci_id "
							+ " left join course sub on bc.course_id = sub.id"
							+ " where bc.banci_id in (").append(classorderids).append(") and acc.account_id = ? and bc.subject_id= ? group by bc.course_id ") ;
					List<Course> xbcublist = Course.dao.find(vcsql.toString(), studentid,sub.getInt("subid"));
					sub.put("xbcourselength", xbcublist.size()+1);
					xblength += xbcublist.size()+1;
					sub.put("xbcourselist", xbcublist);
				}
				student.put("xbsubject", xbsublist);
			}
			student.put("xblength", xblength+1);
			
		}catch(Exception ex){
			ex.printStackTrace();
			
		}
		
		return student;
	}
	
	/**
	 * 根据学生id获取学生所对应的所有科目名称 返回字符串
	 * @param string
	 * @return
	 */
	public String getStudentSubjectNames(String stuid) {
		String sql = "select GROUP_CONCAT(subjectids) subids from crm_courseorder where studentid = ? ";
		CourseOrder courseOrder = CourseOrder.dao.findFirst(sql, stuid);
		String subjectids = null;
		if(courseOrder!=null&&StrKit.notBlank(courseOrder.getStr("subids"))){
			subjectids = courseOrder.getStr("subids").startsWith("|")?courseOrder.getStr("subids").substring(1):courseOrder.getStr("subids");
			subjectids = subjectids.replace("|", ",").replaceAll(",,", ",");
		}
		String subjectNames = "";
		if(StrKit.notBlank(subjectids)){
			String subjectNameSql = "select group_concat(subject_name) names from subject where id in ("+subjectids+")";
			Subject subject = Subject.dao.findFirst(subjectNameSql);
			subjectNames = StringUtils.isEmpty(subject.getStr("names"))?"无":subject.getStr("names").replace(",", "、");
		}
		return subjectNames;
	}
	
	/**
	 * 
	 * 学生选择该时段是否够课时
	 * @param studentId
	 * @param rankid
	 * @param classtype
	 * @return
	 */
	public boolean checkHaveEnoughHours(String studentId, String rankid, String classtype,String courseId) {
		if(classtype.equals("1"))
			return true;
		else{
			Student student = Student.dao.findById(studentId);
			if(student.getInt("state")==2){//虚拟用户，查询小班课时是否够用
				ClassOrder classorder= ClassOrder.dao.findByXuniId(Integer.parseInt(studentId));
				TimeRank timeRank = TimeRank.dao.findById(rankid);
				int zks = classorder.getInt("lessonNum");
				float ypks =  CoursePlan.coursePlan.getClassYpkcClasshour(classorder.getPrimaryKeyValue());
				double syks = ToolArith.sub(zks, ypks);//剩余课时
				int chargeType = classorder.getInt("chargeType");
				if(chargeType == 1){
					if(syks>0){
						if (timeRank.getBigDecimal("class_hour").doubleValue()>syks) {
							return false;
						}
					}else{
						return false;
					}			
				}
			}else{
				TimeRank timeRank = TimeRank.dao.findById(rankid);
				Course course = Course.dao.findById(Integer.parseInt(courseId));
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
				if (syks > 0) {
					if (ypks + timeRank.getBigDecimal("class_hour").doubleValue() > yjfks +keqian) {
						return false;
					}else{
						if(timeRank.getBigDecimal("class_hour").doubleValue() > syks) {
							return false;
						}
					}
				} else {
					return false;
				}
			}		
		}
		return true;
	}
	/**
	 * 学生生日列表分页
	 * @param splitPage
	 */
	public void getAllStudentBirthDay(SplitPage splitPage) {
		 List<Object> paramValue = new ArrayList<Object>();
			StringBuffer select = new StringBuffer(" select * ," );
			select.append("DATE_FORMAT(birthday,'%m-%d') shengri,");
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy");
			String time=format.format(date); 
			select.append(time).append("-DATE_FORMAT(birthday,'%Y') nianling");
			StringBuffer formSql = new StringBuffer("from account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', roleids) ) > 0 and state <> 2 and BIRTHDAY IS NOT NULL ");
			
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "like":
					formSql.append(" and DATE_FORMAT(birthday,'%m-%d') = DATE_FORMAT((select current_date),'%m-%d') ");
					break;
				case "begin":
					formSql.append(" and birthday >= '").append(value).append("'");
					break;
				case "end":
					formSql.append(" and birthday <= '").append(value).append("'");
					break;
				case "studentname":
					formSql.append(" and real_name like '%").append(value).append("%'");
					break;
				default:
					break;
				}
			}
			if(queryParam.get("begin")==null&&queryParam.get("end")==null){
				
				format=new SimpleDateFormat("MM-dd");
				time=format.format(date); 
				formSql.append(" and DATE_FORMAT(birthday,'%m-%d') >= '").append(time).append("'");
			}
			formSql.append(" order by birthday desc ");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			splitPage.setPage(page);
		
	}

	/**
	 * 购课记录 分页
	 * @return 
	 */
	public List<Record> showCourseOrdersDetail(SplitPage splitPage, Integer sysuserId) {
		List<Object> paramValue = new ArrayList<Object>();
		StringBuffer select = new StringBuffer(
				  " SELECT o.delflag,o.id,o.subjectids,o.createtime,o.`status`,o.teachtype,o.totalsum,o.realsum,o.rebate,o.classhour, "
				+ " (SELECT COUNT(1) FROM crm_payment p WHERE p.orderid=o.id) paycount, "
				+ " s.REAL_NAME studentname, "
				+ " (SELECT IFNULL(SUM(amount),0) FROM crm_payment p WHERE p.orderid=o.id AND p.ispay=1) paidamount, bc.classNum  ");
		
		StringBuffer formSql = new StringBuffer(
				  " FROM crm_courseorder o "
			    + " LEFT JOIN account s ON o.studentid=s.Id "
				+ " LEFT JOIN crm_opportunity opp on opp.id=s.opportunityid "
				+ " LEFT JOIN class_order bc ON o.classorderid=bc.id "
				+ " WHERE 1=1 ");
		SysUser sysUser = SysUser.dao.findById(sysuserId);
		if(!Role.isAdmins(sysUser.getStr("roleids"))){
			formSql.append(" AND o.operatorid = ? ");
			paramValue.add(sysuserId);
		}
		Map<String,String> queryParam = splitPage.getQueryParam();
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "studentname":
				formSql.append(" and s.REAL_NAME like ? ");//预留查询
				paramValue.add("%" + value + "%");
				break;
			default:
				break;
			}
		}
		formSql.append(" ORDER BY o.id desc ");
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
		List<Record> recordList  =page.getList();
		for(Record record : recordList){
			record.set("subjectname", Subject.dao.getSubjectNameByIds(record.getStr("subjectids")));
		}
		splitPage.setPage(page);
		return recordList;
	}



}
