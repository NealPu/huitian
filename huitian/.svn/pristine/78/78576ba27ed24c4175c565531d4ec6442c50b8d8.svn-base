package com.momathink.finance.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolString;
import com.momathink.finance.model.CourseOrder;
import com.momathink.finance.model.Refund;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.subject.model.Subject;
import com.momathink.teaching.teacher.model.Teachergrade;

/**
 * 财务管理
 * 
 * @author David
 *
 */
public class FinanceService extends BaseService {

	public void list(SplitPage splitPage) {
		String select = "SELECT s.Id,s.REAL_NAME studentname,s.CREATE_TIME createtime,IF(s.STATE=0,'正常','注销') zt,IFNULL(a.realsum,0) ss,IFNULL(a.classhour,0) zks,IFNULL(b.xhks,0) xhks\n";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("FROM account s LEFT JOIN\n" +
				"(SELECT o.studentid,SUM(o.realsum) realsum,SUM(o.classhour) classhour,SUM(o.rebate) rebate FROM crm_courseorder o  WHERE o.delflag=0 and o.`status`!=0 GROUP BY o.studentid) a\n" +
				"ON s.id=a.studentid\n" +
				"LEFT JOIN\n" +
				"(SELECT c.STUDENT_ID studentid,SUM(tr.class_hour) xhks FROM courseplan c left JOIN time_rank tr on tr.Id=c.TIMERANK_ID WHERE c.STATE=0 GROUP BY c.STUDENT_ID) b\n" +
				"ON s.id=b.studentid\n"+ 
				"WHERE s.STATE=0 AND LOCATE((SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0");
		if (null == queryParam) {
			return;
		}
		String studentname = queryParam.get("studentname");
		String phonenumber = queryParam.get("phonenumber");
		String campusSql = queryParam.get("campusSql");

		if (null != studentname && !studentname.equals("")) {
			formSqlSb.append(" and s.real_name like ?");
			paramValue.add("%" + studentname + "%");
		}
		if (null != phonenumber && !phonenumber.equals("")) {
			formSqlSb.append(" and s.tel like ?");
			paramValue.add("%"+phonenumber+"%");
		}
		if(!StringUtils.isEmpty(campusSql)){
			formSqlSb.append(campusSql);
		}

		formSqlSb.append(" order by id desc");
	}
	
	/**
	 * 取出学生该订单中对应科目已排未上的课程
	 * @param subjectids
	 * @param studentid
	 * @param teachtype
	 * @return
	 */
	public List<CoursePlan> getStudentOrderSubjectPlaned(String subjectids, String studentId, String teachType,Integer classOrderId) {
		List<CoursePlan> list = new ArrayList<CoursePlan>();
		if(subjectids.startsWith("|")){
			subjectids = subjectids.substring(1);
		}
		String[] sub_ids_arr = subjectids.split("\\|");
		if(sub_ids_arr.length>0){
			for(String sub_id:sub_ids_arr){
				List<CoursePlan> subjectStudentCourseplans = CoursePlan.coursePlan.querySubStuCps(sub_id,studentId,teachType,classOrderId);
				if(subjectStudentCourseplans!=null&&subjectStudentCourseplans.size()>0){
					for(CoursePlan subStucp:subjectStudentCourseplans){
						list.add(subStucp);
					}
				}
			}
		}
		return list;
	}
	

	public void updateStudentSubjectCoursePlaned(Integer studentId, Integer courseOrderId,Integer sysUserId) {
		List<CoursePlan> cplist = CoursePlan.coursePlan.getUnusedCoursePlan(studentId,courseOrderId);
		if(cplist!=null&&cplist.size()>0){
			for(CoursePlan cp:cplist){//清理未上排课要注意小班和1对1区别
				Integer coursePlanId = cp.getInt("courseplanid");
				if(cp.getInt("class_id")==0){//1对1直接进行删除排课操作
					CoursePlan.coursePlan.deleteStuCoursePlan(coursePlanId,sysUserId, "退费删除.");
				}else{//小班，courseplan中没有保存学生的课程记录，需要去删除teacherGrade中和accountBook中数据
					Teachergrade.teachergrade.deleteByCoursePlanId(coursePlanId,studentId,sysUserId);
				}
			}
		}
	}


	/**
	 * 保存退费
	 * @param refund
	 */
	public JSONObject saveRefund(Refund refund,Integer sysuserid) {
		JSONObject json = new JSONObject();
		refund.set("addtime", new Date());
		BigDecimal before = refund.getBigDecimal("betotlebalance");//退费前该订单的实收额
		BigDecimal rebalance = refund.getBigDecimal("balance");//退费金额
		BigDecimal bt = before.subtract(rebalance);
		refund.set("aftotlebalance",bt);
		CourseOrder co = CourseOrder.dao.findById(refund.getInt("courseorderid"));
		double beforclasshour = co.getDouble("classhour");
		refund.set("beforeclasshours", beforclasshour);
		try{
			co.set("classhour", ToolArith.sub(beforclasshour, refund.getDouble("classhours")));
			co.set("realsum", bt);
			co.set("totalsum", bt);
			co.set("updatetime", new Date());
			co.set("version", co.getInt("version")+1);
			//没有减课时
			co.update();
			Student student = Student.dao.findById(co.getInt("studentid"));
			refund.save();
			Integer studentId = student.getPrimaryKeyValue();
			updateStudentSubjectCoursePlaned(studentId, co.getPrimaryKeyValue(),sysuserid);
			json.put("code", 1);
			json.put("msg", "退费成功.");
		}catch(Exception ex){
			ex.printStackTrace();
			json.put("code", 0);
			json.put("msg", "操作失败.");
		}
		return json;
		
	}
	public void queryRefundPageMessage(SplitPage splitPage) {
		List<Object> paramValue = new ArrayList<Object>();
		StringBuffer select =new StringBuffer("select cr.*,tt.real_name as transactname,a.real_name as studentname,cc.createtime,cc.status,cc.subjectids,co.classNum,cc.ordernum,cc.teachtype,cc.classhour");
		StringBuffer formSqlSb= new StringBuffer(" from crm_refunds cr"
				+ " left join crm_courseorder cc on cr.courseorderid = cc.id"
				+ " left join account a on cc.studentid = a.id"
				+ " left join account tt on cr.sysuser = tt.id "
				+ " left join class_order co on co.id = cc.classorderid  where 1=1");
		Map<String,String> queryParam = splitPage.getQueryParam();
		if (null == queryParam) {
			return;
		}
		String studentname = queryParam.get("studentname");
		String transactname = queryParam.get("transactname");
		String refunddate = queryParam.get("refunddate");
		String campusSql = queryParam.get("campusSql");

		if (!StringUtils.isEmpty(studentname)) {
			formSqlSb.append(" AND a.real_name like ? ");
			paramValue.add("%"+studentname+"%");
		}
		if (!StringUtils.isEmpty(transactname)) {
			formSqlSb.append(" AND tt.real_name like ? ");
			paramValue.add("%"+transactname+"%");
		}
		if (!StringUtils.isEmpty(refunddate)) {
			formSqlSb.append(" AND DATE_FORMAT(cr.refundtime,'%Y-%m-%d') = ? ");
			paramValue.add(refunddate);
		}
		if(!StringUtils.isEmpty(campusSql)){
			formSqlSb.append(campusSql);
		}

		formSqlSb.append(" ORDER BY cr.refundtime");
		
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSqlSb.toString(), paramValue.toArray());
		List<Record> list = page.getList();
		String subname = "";
		for (Record r : list) {
			String subids = r.getStr("subjectids").replace("|", ",");
			if(!ToolString.isNull(subids)){
				List<Subject> sublist = Subject.dao.find("select distinct SUBJECT_NAME from subject where id in ( "+subids+" )");
				if(sublist!=null&&sublist.size()>0){
					for(Subject sub : sublist){
						subname = subname+"、"+sub.getStr("subject_name");
					}
				}
				subname = subname.replaceFirst("、", "");
			}
			r.set("subjectname", subname);
		}
		splitPage.setPage(page);
	}

	/**
	 * 获取课程消耗列表
	 * @param splitPage
	 */
	public Record consumptionList(SplitPage splitPage) {
		String select = "SELECT cp.Id,DATE_FORMAT(cp.COURSE_TIME,'%Y-%m-%d') courseTime,k.SUBJECT_NAME,c.COURSE_NAME,cp.class_id,s.REAL_NAME studentname,t.REAL_NAME teachername,x.CAMPUS_NAME,r.ADDRESS roomname,tr.RANK_NAME,tr.class_hour,b.totalfee,b.carriedover ";
		StringBuilder selectTotal = new StringBuilder("SELECT SUM(tr.class_hour) totalHour,SUM(b.totalfee) totalAmount ");
		StringBuilder formSqlSb = new StringBuilder("FROM courseplan cp\n");
		formSqlSb.append("LEFT JOIN `subject` k ON cp.SUBJECT_ID=k.Id\n");
		formSqlSb.append("LEFT JOIN course c ON cp.COURSE_ID=c.Id\n");
		formSqlSb.append("LEFT JOIN account s ON cp.STUDENT_ID=s.Id\n");
		formSqlSb.append("LEFT JOIN account t ON cp.TEACHER_ID=t.Id\n");
		formSqlSb.append("LEFT JOIN campus x ON cp.CAMPUS_ID=x.Id\n");
		formSqlSb.append("LEFT JOIN classroom r ON cp.ROOM_ID=r.Id\n");
		formSqlSb.append("LEFT JOIN time_rank tr ON cp.TIMERANK_ID=tr.Id\n");
		formSqlSb.append("LEFT JOIN (SELECT ab.courseplanid,SUM(ab.realamount) totalfee,ab.carriedover FROM account_book ab WHERE ab.operatetype=4 GROUP BY ab.courseplanid) b ON cp.Id=b.courseplanid\n");
		formSqlSb.append("WHERE cp.PLAN_TYPE=0 AND cp.STATE=0 ");
		
		Map<String, String> queryParam = splitPage.getQueryParam();
		List<Object> paramValue = new LinkedList<Object>();
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "studentname":// 学生姓名
				formSqlSb.append(" AND s.real_name like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "teacherName"://  教师姓名
				formSqlSb.append(" AND t.real_name like ? ");
				paramValue.add("%" + value + "%");
				break;
			case "subjectId":// 科目
				formSqlSb.append(" AND cp.subject_id = ? ");
				paramValue.add(value);
				break;
			case "courseId":// 课程
				formSqlSb.append(" AND cp.course_id = ? ");
				paramValue.add(value);
				break;
			case "campusId":// 校区
				formSqlSb.append(" AND cp.CAMPUS_ID = ? ");
				paramValue.add(value);
				break;
			case "beginDate"://查询开始日期
				formSqlSb.append(" AND cp.COURSE_TIME>=? ");
				paramValue.add(value + " 00:00:00");
				break;
			case "endDate":// 结束日期
				formSqlSb.append(" AND cp.COURSE_TIME<=? ");
				paramValue.add(value + " 23:59:59");
				break;
			case "teachType":// 授课类型
				if("1".equals(value))
					formSqlSb.append(" AND cp.class_id = ? ");
				else
					formSqlSb.append(" AND cp.class_id != ? ");
				paramValue.add(0);
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" ORDER BY cp.COURSE_TIME desc,tr.RANK_NAME,r.ADDRESS");
		Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select, formSqlSb.toString(), paramValue.toArray());
		splitPage.setPage(page);
		Record record= Db.findFirst(selectTotal.append(formSqlSb).toString(), paramValue.toArray());
		return record;
	}

}
