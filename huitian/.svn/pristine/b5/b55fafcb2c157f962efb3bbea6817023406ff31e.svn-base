package com.momathink.teaching.teacher.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.sys.account.model.AccountBook;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.course.model.CourseBack;

@Table(tableName = "teachergrade")
public class Teachergrade extends BaseModel<Teachergrade> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3730979503009601434L;
	public static final Teachergrade teachergrade = new Teachergrade();
	
	/**
	 * 根据设置节点id获取报告内容
	 * @param pointid
	 * @return
	 */
	public Teachergrade getPointGradeByPointId(String pointid) {
		String sql = "select * from teachergrade where pointid = ? ";
		return teachergrade.findFirst(sql, pointid);
	}
	/**
	 *
	 * @param cpid
	 * @return
	 */
	public Teachergrade getGradeByCoursePlanId(String cpid){
		if(StrKit.isBlank(cpid))
			return null;
		return teachergrade.findFirst("select * from teachergrade where COURSEPLAN_ID = ? ",cpid);
	}
	/**
	 * 找到下次预约的时间信息*
	 * @param int1
	 * @return
	 */
	public Teachergrade getNextTeachergrade(Integer tgid) {
		String  sql = "select tg.*,sp.appointment from teachergrade tg "
				+ " left join jw_setpoint sp on tg.pointid = sp.id  where tutorid = ? ";
		return teachergrade.findFirst(sql,tgid);
	}
	/**
	 * 根据课程id和学生id查看*
	 * @param courseplanid
	 * @param studentid
	 * @return
	 */
	public Teachergrade findByCoursePlanIdAndStudentid(String courseplanid, String studentid) {
		String sql = "select * from teachergrade where courseplan_id = "+courseplanid+" and studentid = "+ studentid ;
		return teachergrade.findFirst(sql);
	}
	public List<Teachergrade> getGradeByCoursePlanIds(String cpid) {
		return teachergrade.find("select * from teachergrade where COURSEPLAN_ID = ? ",cpid);
	}
	public List<Teachergrade> getListByStudentIdAndClassOrderId(Integer studentId, Integer classOrderId) {
		String sql = "select tg.*,cp.timerank_id from teachergrade tg left join courseplan cp on tg.COURSEPLAN_ID=cp.Id WHERE tg.studentid=? AND cp.class_id=?";
		return teachergrade.find(sql, studentId,classOrderId);
	}
	public double getHasHour(Integer studentId, Integer classOrderId) {
		List<Teachergrade> list = teachergrade.getListByStudentIdAndClassOrderId(studentId, classOrderId);
		double sumHour = 0;
		for(Teachergrade tg : list){
			double classHour =TimeRank.dao.getHourById(tg.getInt("timerank_id"));
			sumHour = ToolArith.add(sumHour, classHour);
		}
		return sumHour;
	}
	
	/**
	 * 根据排课ID获取小班学生
	 * @param coursePlanId
	 * @return
	 */
	public String getStudentNameByCoursePlanId(Integer coursePlanId) {
		String sql = "SELECT GROUP_CONCAT(s.REAL_NAME) studentNames FROM teachergrade t LEFT JOIN account s ON t.studentid=s.Id WHERE t.COURSEPLAN_ID=?";
		return Db.queryStr(sql, coursePlanId);
	}
	
	/**
	 * 根据课程ID删除小班某个学生的排课记录
	 * @param coursePlanId
	 * @param studentId
	 */
	public void deleteByCoursePlanId(Integer coursePlanId,Integer studentId,Integer operateUserId) {
		Teachergrade teacherGrade = teachergrade.findByCoursePlanIdAndStudentid(coursePlanId+"", studentId+"");
		if(teacherGrade != null){
			String sql1 = "insert into courseplan_back SELECT * from  courseplan where courseplan.id = ? ";
			Db.update(sql1, coursePlanId);
			CourseBack cp = CourseBack.dao.findById(coursePlanId);
			cp.set("del_msg", "退费").set("update_time", ToolDateTime.getDate()).set("deluserid", operateUserId).update();
			AccountBook.dao.deleteByAccountIdAndCoursePlanId(studentId, coursePlanId);//删除某个学生的
			teacherGrade.delete();
		}
	}
	
	/**
	 * 根据课程ID删除所有学生小班关联记录和1对1的上课评价记录
	 * @param coursePlanId
	 */
	public void deleteByCoursePlanId(Integer coursePlanId) {
		String sql = "delete from teachergrade where COURSEPLAN_ID=?";
		Db.update(sql, coursePlanId);
		AccountBook.dao.deleteByCoursePlanId(coursePlanId);//删除所有学生的排课记录
	}
	
	/**
	 * 查询某个日期参加小班的学生名单
	 * @param queryParams
	 * @return
	 */
	public Map<Integer, String> getStudentNames(Map<String, String> queryParams) {
		String sql = "SELECT tg.COURSEPLAN_ID,GROUP_CONCAT(s.REAL_NAME) studentNames FROM teachergrade tg \n" +
				"left join account s on tg.studentid=s.Id\n" +
				"left join courseplan cp on tg.COURSEPLAN_ID=cp.Id\n" +
				"WHERE s.STATE=0 AND cp.COURSE_TIME>=? AND cp.COURSE_TIME<=? AND cp.class_id!=0\n" +
				"GROUP BY tg.COURSEPLAN_ID";
		List<Teachergrade> list = teachergrade.find(sql, queryParams.get("startDate"),queryParams.get("endDate"));
		Map<Integer,String> map = new HashMap<Integer, String>();
		for(Teachergrade t : list){
			map.put(t.getInt("COURSEPLAN_ID"), t.getStr("studentNames"));
		}
		return map;
	}
	
}
