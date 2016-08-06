package com.momathink.sys.system.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="time_rank")
public class TimeRank extends BaseModel<TimeRank>{
	private static final long serialVersionUID = -7516175055292633955L;
	public static final TimeRank dao=new TimeRank();
	/**
	 * 查询所有时间段
	 */
	public List<TimeRank> getTimeRank() {
		String sql = "SELECT t.* FROM time_rank t where t.state=0 order by t.rank_name";
		List<TimeRank> list = TimeRank.dao.find(sql);
		return list;
	}
	
	
	public List<TimeRank> getAddPlanTimeRank(){
		String sql = "select tr.*, 0 as code from time_rank tr where tr.state=0 order by tr.rank_name";
		return dao.find(sql);
	}
	
	/**
	 * 添加时间段
	 */
	public void addTimeRank(String rankName,String rankType,String class_hour) {
		new TimeRank().set("RANK_NAME", rankName).set("RANK_TYPE", rankType).set("class_hour", class_hour).save();
	}
	/**
	 * 更新时间段
	 */
	public void updateTimeRank(int id,String rankName,String rankType,String class_hour) {
		TimeRank.dao.findById(id).set("RANK_NAME", rankName).set("RANK_TYPE", rankType).set("class_hour", class_hour).update();
	}
	
	/**
	 * 根据学生ID查询已经使用时段
	 * @param studentId
	 * @param courseTime
	 * @return
	 */
	public List<Record> queryUsrTimeRank(String studentId, String courseTime) {
		return Db.find("SELECT cp.Id planId,cp.TIMERANK_ID rankId,tr.RANK_NAME rankName,cp.PLAN_TYPE planType FROM courseplan cp LEFT JOIN time_rank tr ON cp.TIMERANK_ID=tr.Id WHERE cp.STUDENT_ID="+studentId+" AND cp.COURSE_TIME ='"+courseTime+"'");
	}
	/**
	 * 根据小班ID查询已经使用的时段
	 * @param classId
	 * @param courseTime
	 * @return
	 */
	public List<Record> queryUsrTimeRankByClassId(String classId, String courseTime) {
		return Db.find("SELECT cp.Id planId,cp.TIMERANK_ID rankId,tr.RANK_NAME rankName,cp.PLAN_TYPE planType FROM courseplan cp LEFT JOIN time_rank tr ON cp.TIMERANK_ID=tr.Id WHERE cp.STATE=1 AND cp.class_id="+classId+" AND cp.COURSE_TIME ='"+courseTime+"'");
	}
	public List<TimeRank> getAllTimeRank() {
		String sql = "SELECT t.* FROM time_rank t order by t.RANK_NAME";
		List<TimeRank> list = TimeRank.dao.find(sql);
		return list;
	}
	
	/**
	 * 获取最大排序数
	 * @return
	 */
	public int getNewMaxIndex() {
		Record record = Db.findFirst("SELECT MAX(RANK_TYPE) max_index FROM time_rank");
		return record.getInt("max_index") == null?1:record.getInt("max_index")+1;
	}
	
	/**
	 * 获取所有时段
	 * @return
	 */
	public List<TimeRank> getTimeRanks(){
		String sql = "select * from time_rank order by RANK_NAME ";
		List<TimeRank> tr = TimeRank.dao.find(sql);
		return  tr;
	}
	/**
	 * 获取时段的课时数
	 * @param rankId
	 * @return
	 */
	public float queryClassHour(String rankId) {
		if(StringUtils.isEmpty(rankId))
			return 0;
		else
			return dao.findById(Integer.parseInt(rankId)).getBigDecimal("class_hour").floatValue();
	}
	
	
	public String getTimeRankNameById(String timeId) {
		if(StringUtils.isEmpty(timeId)){
			return null;
		}else{
			TimeRank t = dao.findById(Integer.parseInt(timeId));
			return t.getStr("rank_name");
		}
	}
	
	public StringBuffer getTimeIdsByTime(String starthour, String startmin, String endhour, String endmin) {
		StringBuffer timeids = new StringBuffer();
		int beginTime = Integer.parseInt(starthour+startmin);
		int endTime = Integer.parseInt(endhour+endmin);
		List<TimeRank> list = dao.getAllTimeRank();
		for(TimeRank tr : list){
			String name = tr.get("RANK_NAME");
			int shour = Integer.parseInt((name.split("-"))[0].replace(":",""));
			int ehour = Integer.parseInt((name.split("-"))[1].replace(":",""));
			if((shour>=beginTime&&shour<=endTime)||(ehour>=beginTime&&ehour<=endTime)){
				timeids.append(",").append(tr.getInt("Id"));	
			}
		}
		timeids.replace(0, 1, "");
		return timeids;
	}
	

	/**
	 * 不在ids里面的时段
	 * @param ids
	 * @return
	 */
	public List<TimeRank> getTimeRanksIdNotIn(String ids) {
		StringBuffer sb = new StringBuffer();
		sb.append("select Id from time_rank where 1=1 ");
		if(!StringUtils.isEmpty(ids)){
			sb.append(" and Id not in (").append(ids).append(")");
		}
		return dao.find(sb.toString());
	}
	
	/**
	 * 取出大于rankname的时段
	 * @param rankname 08:00
	 * @return
	 */
	public List<TimeRank> getTimeRanksDesc(String rankname){
		String sql = "SELECT * FROM time_rank where rank_name>= ? ORDER BY time_rank.RANK_NAME ASC";
		return dao.find(sql,rankname);
	}

	/**
	 * 根据时段名称查询
	 * @param rankname
	 * @return
	 */
	public List<TimeRank> findByRankname(String rankname) {
		String sql = "SELECT * FROM time_rank where rank_name = ? ";
		return dao.find(sql,rankname);
	}

	/**
	 * 获取时段课时
	 * @param timeRankId
	 * @return
	 */
	public double getHourById(Integer timeRankId) {
		TimeRank timeRank = dao.findById(timeRankId);
		return timeRank.getBigDecimal("class_hour").doubleValue();
	}
}
