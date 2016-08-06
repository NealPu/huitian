package com.momathink.sys.system.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.course.model.CoursePlan;

@Controller(controllerKey="/time")
public class TimeController extends BaseController {
	private static final Logger logger = Logger.getLogger(TimeController.class);

	/**
	 * 时间段管理
	 */
	public void findTimeManager() {
		try {
			List<TimeRank> timeRanks = TimeRank.dao.getAllTimeRank();
			setAttr("timeRanks", timeRanks); // 所有时间段
			renderJsp("/time/findTimeManager.jsp");
		} catch (Exception e) {
			logger.error("TimeController.findTimeManager", e);
			renderJsp("/time/findTimeManager.jsp");
		}
	}

	/**
	 * 修改时间段跳转
	 */
	public void editTimeManager() {
		try {
			String timeRankId = getPara("timeRankId");
			setAttr("code","0");
			if (timeRankId != null && !timeRankId.equals("")) {
				TimeRank timeRank = TimeRank.dao.findById(timeRankId);
				long useCount = CoursePlan.coursePlan.queryCountByTimeRankId(timeRankId);
				if(useCount>0)
					setAttr("code","1");
				String[] times = timeRank.get("RANK_NAME").toString().split("-");
				String startTime = times[0];// 开始时间
				String endTime = times[1];// 结束时间
				String startHour = startTime.split(":")[0];// 开始时间 时
				String startMin = startTime.split(":")[1];// 开始时间 分
				String endHour = endTime.split(":")[0];// 结束时间 时
				String endMin = endTime.split(":")[1];// 结束时间 分
				setAttr("startHour", startHour);
				setAttr("startMin", startMin);
				setAttr("endHour", endHour);
				setAttr("endMin", endMin);
				setAttr("timeRankId", timeRankId);
				setAttr("rankType", timeRank.get("RANK_TYPE"));
				setAttr("class_hour", timeRank.getBigDecimal("class_hour"));
			}
			renderJsp("/time/addTimeManager.jsp");
		} catch (Exception e) {
			logger.error("TimeController.editTimeManager", e);
			renderJsp("/time/addTimeManager.jsp");
		}
	}

	/**
	 * 添加或修改时间段
	 */
	public void doAddTimeManager() {
		try {
			String timeRankId = getPara("timeRankId");
			String classHour = getPara("class_hour");
			//String rankType = getPara("rankType");
			String startHour = getPara("startHour");
			String endHour = getPara("endHour");
			String startMin = getPara("startMin");
			String endMin = getPara("endMin");
			String startTime = startHour + ":" + startMin;// 开始时间
			String endTime = endHour + ":" + endMin;// 结束时间
			String time = startTime + "-" + endTime;
			if (timeRankId != null && !timeRankId.equals("")) {
				long useCount = CoursePlan.coursePlan.queryCountByTimeRankId(timeRankId);
				if (useCount > 0) {
					redirect("/time/editTimeManager?timeRankId=" + timeRankId + "&code=1");
				} else {
					TimeRank.dao.updateTimeRank(Integer.parseInt(timeRankId), time, "1", classHour);// 更新时间段
					redirect("/time/findTimeManager");
				}
			} else {
				TimeRank.dao.addTimeRank(time, "1", classHour);// 排序字段默认为1  排序未使用
				redirect("/time/findTimeManager");
			}
		} catch (Exception e) {
			logger.error("TimeControl1ler.doAddTimeManager", e);
			redirect("/time/findTimeManager");
		}
	}

	public void toAddTimeManager() {
		setAttr("code", 0);
		setAttr("rankType", TimeRank.dao.getNewMaxIndex());
		renderJsp("/time/addTimeManager.jsp");
	}

	/**
	 * 删除时间段
	 */
	public void delTimeManager() {
		try {
			if (getParaToInt("timeRankId") != null) {
				int id = getParaToInt("timeRankId");
				int state = getParaToInt("state");
				// 查询该时段是否有未开始或未结束的课，有则不让删除。sql语义：查找大于当前日期的该时段的排课和等与当前日期该时段未开始或未结束的排课
				String sql = "select * from courseplan  c"
						+ " LEFT JOIN time_rank t ON c.TIMERANK_ID=t.Id"
						+ " where  (c.COURSE_TIME>NOW() OR (DATE_FORMAT(c.COURSE_TIME,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') and SUBSTRING(t.RANK_NAME,-5)>DATE_FORMAT(NOW(),'%H:%i')))"
						+ " and c.state=0 and t.id=?" + " ORDER BY COURSE_TIME ";
				if (Db.find(sql, id).size() > 0 && state == 1)// 如果有排课未开始或未结束
				{
					renderJson("result", "该时段有未开始或未结束的排课，不能停用");
				} else {
					Db.update("update time_rank set state=? where id=?", state, id);// 改变状态，删除成功，该时段只会在按月浏览的历史排课中显示
					renderJson("result", "true");
				}
			}
		} catch (Exception e) {
			logger.error("TimeController.delTimeManager", e);
		}
	}

	/**
	 * 查询学生的时间段
	 */
	public void getTimeRankByStudentId() {
		try {
			String studentId = getPara("studentId");
			String courseTime = getPara("courseTime");
			String sql = "select time_rank.Id as rankId,time_rank.rank_name as rankName,courseplan.id as planId,courseplan.plan_type as planType  from time_rank left join courseplan on "
					+ "(time_rank.id=courseplan.TIMERANK_ID and courseplan.COURSE_TIME='"
					+ courseTime
					+ "' and courseplan.STUDENT_ID="
					+ studentId
					+ ") where time_rank.state=0 group by rankName order by rankId ";
			renderJson("timeRanks", Db.find(sql));
		} catch (Exception e) {
			logger.error("TimeController.getTimeRankByStudentId", e);
		}
	}

	/**
	 * 查询班次的时间段
	 */
	public void getTimeRankByClassId() {
		try {
			String classId = getPara("classId");
			String courseTime = getPara("courseTime");
			String sql = "select time_rank.Id as rankId,time_rank.rank_name as rankName,courseplan.id as planId,courseplan.plan_type as planType  "
					+ "from time_rank left join courseplan on(time_rank.id=courseplan.TIMERANK_ID and courseplan.COURSE_TIME=? "
					+ "AND courseplan.class_id = ?) where time_rank.state=0 group by rankName order by rankId ";
			renderJson("timeRanks", Db.find(sql, courseTime, classId));
		} catch (Exception e) {
			logger.error("TimeController.getTimeRankByClassId", e);
		}
	}

	public void modifyTimeRankOrder() {
		try {
			if (getParaToInt("timeRankId") != null) {
				int id = getParaToInt("timeRankId");
				int sortOrder = ToolString.isNull(getPara("sortOrder")) ? 1 : getParaToInt("sortOrder");
				String sql = "SELECT * from time_rank where id=?";
				if (Db.find(sql, id).size() > 0) {
					Db.update("update time_rank set rank_type=" + sortOrder + " where id=?", id);
					renderJson("result", "true");
				} else {
					renderJson("result", "该时段不存在！");
				}
			}
		} catch (Exception e) {
			logger.error("TimeController.modifyTimeRankOrder", e);
		}
	}
	
	/**
	 * 检查时段是否已存在
	 */
	public void checkRankname() {
		try {
			
			String rankname = getPara("rankname");
			List<TimeRank> timerankList = TimeRank.dao.findByRankname(rankname);
			if (getParaToInt("id") != null) {
				TimeRank timerank = TimeRank.dao.findById(getParaToInt("id"));
				if(rankname.equals(timerank.getStr("RANK_NAME"))){
					if(timerankList.size()==1){
						renderJson("result", true);
					}else{
						renderJson("result", false);
					}
				}else{
					if(timerankList.size()==0){
						renderJson("result", true);
					}else{
						renderJson("result", false);
					}
				}
			}else{
				if(timerankList.size()==0){
					renderJson("result", true);
				}else{
					renderJson("result", false);
				}
			}
		} catch (Exception e) {
			logger.error("TimeController.modifyTimeRankOrder", e);
		}
	}
}
