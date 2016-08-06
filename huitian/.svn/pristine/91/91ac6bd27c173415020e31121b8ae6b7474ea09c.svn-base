package com.momathink.teaching.course.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.account.model.AccountBanci;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.student.model.Student;

public class CourseService extends BaseService {
	
	public static final CoursePlan dao = new CoursePlan();

	public void list(SplitPage splitPage) {
		String select = "SELECT c.*,s.SUBJECT_NAME ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM course c LEFT JOIN `subject` s ON c.SUBJECT_ID=s.Id WHERE c.SUBJECT_ID != 0 ");
		if (null == queryParam) {
			return;
		}

		String coursename = queryParam.get("coursename");
		String subjectid = queryParam.get("subjectid");
		if (null != coursename && !coursename.equals("")) {
			formSqlSb.append(" AND c.course_name like ? ");
			paramValue.add("%" + coursename + "%");
		}
		if (null != subjectid && !subjectid.equals("")) {
			formSqlSb.append(" AND c.subject_id =? ");
			paramValue.add(Integer.parseInt(subjectid));
		}
		formSqlSb.append(" ORDER BY c.id,c.subject_id");
	}
	
	public List<Record> getCoursePlanDay(String courseTime, Integer campusId) {
		return dao.getCoursePlanDay(courseTime,campusId);
	}
	@Before(Tx.class)
	public void save(Course course) {
		try {
			course.set("create_time", ToolDateTime.getDate());
			course.save();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("添加公司异常");
		}
	}
	
	public List<TimeRank> getTimeRank(){
		return TimeRank.dao.getTimeRanks();
	}
	
	@Before(Tx.class)
	public void update(Course course) {
		try {
			course.set("update_time", new Date());
			course.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<Course> getVIPStuCourse(String courseIds) {
		String[] arrCourses = courseIds.replace("|", ",").split(",");
		List<Course> list = Course.dao.getVIPStuCourse(arrCourses);
		return list;
	}

	@SuppressWarnings("unused")
	public List<CoursePlan> getCoursePlansByStuId(String stuId) {
		List<CoursePlan> lists = CoursePlan.coursePlan.getCoursePlansByStuId(stuId);
		for(int i=0;i<lists.size();i++){
			if(lists==null)
				lists.remove(i);
		}
		return lists;
	}

	public static List<CoursePlan> circleCoursePlan(List<CoursePlan> list){
		for(int i=0;i<list.size();i++){
			if(list.get(i).getInt("CLASS_ID")!=0){
				if(i<list.size()-1){
					if(list.get(i).getStr("COURSE_TIME").equals(list.get(i+1).getStr("COURSE_TIME")) 
							&& list.get(i).getInt("CLASS_ID").equals(list.get(i+1).getInt("CLASS_ID")) 
							&& list.get(i).getInt("TIMERANK_ID").equals(list.get(i+1).getInt("TIMERANK_ID"))){
						list.remove(i+1);
						circleCoursePlan(list);
					}
				}
				
			}
		}
		return list;
	}
	
	public List<CoursePlan> getCoursePlansBetweenDates(String stuId, String tId, String startDate, String endDate,String courseType) {
		List<CoursePlan> list = new ArrayList<CoursePlan>();
		List<CoursePlan> cplist = CoursePlan.coursePlan.getStuCoursePlansBetweenDates(stuId,startDate,endDate);
		List<CoursePlan> tlist = CoursePlan.coursePlan.getCoursePlansByTeacherId(tId,startDate,endDate);
		if(courseType.equals("0")){//课程
			Iterator<CoursePlan> iter = cplist.iterator();
			while(iter.hasNext()){
				CoursePlan plan = iter.next();
				if(plan.getInt("CLASS_ID")!=0){
					iter.remove();
				}
			}
		}else{//模考
			return cplist;
		}
		/*Iterator<CoursePlan> it = tlist.iterator();
		while(it.hasNext()){
			CoursePlan cplan = it.next();
			if(cplan.getInt("CLASS_ID")!=0 && cplan.getInt("STATE")==0){
				it.remove();
			}
		}*/
		for(int i=0;i<cplist.size();i++){
			list.add(cplist.get(i));
		}
		Iterator<CoursePlan> iter = tlist.iterator();
		while(iter.hasNext()){
			CoursePlan cp = iter.next();
			for(int i=0;i<list.size();i++){
				if(list.get(i).getInt("ID").equals(cp.getInt("ID"))){
					iter.remove();
				}
			}
		}
		for(int i=0;i<tlist.size();i++){
			list.add(tlist.get(i));
		}
		return list;
	}

	public List<CoursePlan> getCoursePlansByDay(String courseTime, String studentId, String teacherId) {
		List<CoursePlan> studentCoursePlanList = CoursePlan.coursePlan.getCoursePlansByDay(courseTime,studentId);//获取学生的排课信息
		Student student = Student.dao.findById(studentId);
		if(student.getInt("STATE").toString().equals("2")){//等于2是小班
			ClassOrder classorder = ClassOrder.dao.findByXuniId(student.getInt("Id"));
			List<CourseOrder> ablist = CourseOrder.dao.findOrderByClassId(classorder.getInt("Id"));
			if(ablist.size()>0){
				for(CourseOrder ab:ablist){
					if(!ab.getInt("studentid").toString().equals(studentId)){
						List<CoursePlan> stulist = CoursePlan.coursePlan.getCoursePlansByDay(courseTime,ab.getInt("studentid").toString());
						if(stulist.size()>0){
							studentCoursePlanList.addAll( stulist );
						}
					}
				}
			}
		}else{//1对1排课
			List<AccountBanci> ablist = AccountBanci.dao.findAllByAccountId(studentId);
			if(ablist.size()>0){
				for(AccountBanci ab:ablist){
					List<CoursePlan> stulist = CoursePlan.coursePlan.getCoursePlansByDay(courseTime,student.getPrimaryKeyValue(),ab.getInt("banci_id"));
					if(stulist.size()>0){
						studentCoursePlanList.addAll( stulist );
					}
				}
			}
			
		}
		List<CoursePlan> tcpListDay = CoursePlan.coursePlan.getTeacherCoursePlansByDay(courseTime, teacherId);
		//老师和学生的排课无重复并集 （老师已排课程列表中移除与学生排课重复的(同一次课程)的课程 ，再做并集）
		tcpListDay.removeAll( studentCoursePlanList );
		studentCoursePlanList.addAll( tcpListDay );
		return studentCoursePlanList;
	}

	public List<CoursePlan> getClassCoursePlansBetweenDates(String banciId, String tId, String startDate, String endDate) {
		List<CoursePlan> list = new ArrayList<CoursePlan>();
		List<CoursePlan> cplist = CoursePlan.coursePlan.getClassCoursePlansBetweenDates(banciId,startDate,endDate);
		List<CoursePlan> tlist = CoursePlan.coursePlan.getCoursePlansByTeacherId(tId,startDate,endDate);
		Iterator<CoursePlan> it = tlist.iterator();
		while(it.hasNext()){
			CoursePlan cplan = it.next();
			if(cplan.getInt("CLASS_ID")!=0 && cplan.getInt("STATE")==0){
				it.remove();
			}
		}
		
		for(int i=0;i<cplist.size();i++){
			list.add(cplist.get(i));
		}
		Iterator<CoursePlan> iter = tlist.iterator();
		while(iter.hasNext()){
			CoursePlan cp = iter.next();
			for(int i=0;i<list.size();i++){
				if(list.get(i).getInt("ID").equals(cp.getInt("ID"))){
					iter.remove();
				}
			}
		}
		for(int i=0;i<tlist.size();i++){
			list.add(tlist.get(i));
		}
		return list;
	}


	public List<Map<String, Object>> getTeacherWeekDayRestRankId(String tId,Date parse, String dateInWeek) {
		List<CoursePlan> plan = CoursePlan.coursePlan.queryDayRestCount(ToolDateTime.format(parse, "yyyy-MM-dd"), tId);
		if(plan.size()>0){
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<TimeRank> trlist = TimeRank.dao.getTimeRank();
			for(CoursePlan cp : plan){
				Integer st = Integer.parseInt(cp.getStr("startrest").replace(":", ""));
				Integer et = Integer.parseInt(cp.getStr("endrest").replace(":", ""));
				for(int j=0;j<trlist.size();j++){
					int ft = Integer.parseInt(trlist.get(j).getStr("RANK_NAME").split("-")[0].replaceAll(":", ""));
					int lt = Integer.parseInt(trlist.get(j).getStr("RANK_NAME").split("-")[1].replaceAll(":", ""));
					if((ft<=st&&lt>st)||(ft>=st&&lt<=et)||(ft<et&&lt>=et)){
						Map<String,Object> map = new LinkedHashMap<String, Object>();
						map.put("timeId", trlist.get(j).getInt("ID"));
						map.put("rankname", trlist.get(j).getStr("RANK_NAME"));
						map.put("day", parse);
						list.add(map);
					}
				}
				
			}
			return list;
		}else{
			return null;
		}
	}

	/**
	 * 课程列表
	 * @param stuid
	 * @param classid
	 * @param type 1一对一;2小班
	 * @param courseType 0课程；1模考
	 * @return
	 */
	public List<Record> getStudentOrClassCourse(String stuid,String classid,String type,String courseType){
		StringBuffer sb = new StringBuffer();
		if(type.equals("1")){
			if(courseType.equals("0"))
				sb.append("SELECT distinct course.course_name,course.Id courseid FROM course WHERE FIND_IN_SET(course.id ,(SELECT (GROUP_CONCAT(DISTINCT course_id)) subids FROM user_course WHERE account_id=").append(stuid).append(")) AND course.STATE = 0");
			if(courseType.equals("1"))
				sb.append("SELECT subject.Id,subject.SUBJECT_NAME FROM subject WHERE FIND_IN_SET(subject.Id, (SELECT REPLACE (GROUP_CONCAT(DISTINCT subjectids),'|',',') subids FROM crm_courseorder WHERE crm_courseorder.studentid=").append(stuid).append(")) AND subject.STATE=0");
		}
		if(type.equals("2")){
			if(courseType.equals("0"))
				sb.append(" select distinct c.course_name,c.Id courseid from banci_course bc left join course c on c.id=bc.course_id where banci_id=").append(classid).append(" and c.state = 0  ");
			if(courseType.equals("1"))
				sb.append(" SELECT subject.Id,subject.SUBJECT_NAME FROM subject WHERE FIND_IN_SET(subject.Id ,(SELECT GROUP_CONCAT(DISTINCT bc.subject_id) FROM banci_course bc WHERE  bc.banci_id=").append(classid).append(")) and subject.STATE=0 ");
		}
		return Db.find(sb.toString());
	}
	
}
