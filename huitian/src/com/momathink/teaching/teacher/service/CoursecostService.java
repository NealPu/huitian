package com.momathink.teaching.teacher.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.teaching.teacher.model.Coursecost;

public class CoursecostService extends BaseService {

	private static Logger log = Logger.getLogger(CoursecostService.class);

	
	
	/**
	 * 分页
	 * 
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		log.debug("教师管理：分页处理");
		String select = " select c.*,a.REAL_NAME ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from coursecost c "+"left join account a on a.id = c.creatid"+" where 1=1 ");
		if (null == queryParam) {
			return;
		}
		Set<String> paramKeySet = queryParam.keySet();
		for (String paramKey : paramKeySet) {
			String value = queryParam.get(paramKey);
			switch (paramKey) {
			case "teacherid":// 教师id
				formSqlSb.append(" and c.teacherid = ? ");
				paramValue.add(Integer.parseInt(value));
				break;
			default:
				break;
			}
		}
		formSqlSb.append(" ORDER BY c.startdate DESC");
	}
	
	/**
	 * 保存老师课时费
	 * @param tid
	 * @param coursecost
	 * @param startdate
	 * @return
	 */
	public JSONObject saveTeacherCourseCost(int tid, Coursecost coursecost, Date startdate) {
		JSONObject json = new JSONObject();
		try{
			Coursecost maxStart = Coursecost.dao.findFirst("select max(startdate) maxstart,coursecost.* from coursecost where teacherid = ? and startdate < ? " ,tid,startdate);
			if(maxStart.getDate("maxstart")!=null){
				maxStart = Coursecost.dao.findFirst(" select max(startdate) maxstart,coursecost.* from coursecost where teacherid = ? and startdate = ? ",tid,maxStart.getDate("maxstart"));
			}
			Coursecost minEnd = Coursecost.dao.findFirst("select min(enddate) minend,coursecost.* from coursecost where teacherid = ? and enddate > ? " ,tid,startdate);
			Coursecost minStartEnd = Coursecost.dao.findFirst("select min(startdate) minstart,coursecost.* from coursecost where teacherid = ? and startdate > ? " ,tid,startdate);
			if(minEnd.getInt("Id")==null&&minStartEnd.getInt("Id")!=null){
				minEnd = minStartEnd;
				minEnd.put("minend", minStartEnd.getDate("minstart"));
			}
			if(minEnd.getInt("Id")==null&&maxStart.getInt("Id")==null){
				coursecost.save();
			}
			if(minEnd.getInt("Id")==null&&maxStart.getInt("Id")!=null){
				maxStart.set("enddate", ToolDateTime.getSpecifiedDayBefore(ToolDateTime.format(startdate,"yyyy-MM-dd")));
				coursecost.save();
				maxStart.update();
			}
			if(maxStart.getInt("Id")==null&&minEnd.getDate("startdate")!=null){
				coursecost.set("enddate", ToolDateTime.getSpecifiedDayBefore(ToolDateTime.format(minEnd.getDate("startdate"),"yyyy-MM-dd")));
				coursecost.save();
			}
			if(maxStart.getInt("Id")!=null&&minEnd.getInt("Id")!=null){
				if(minEnd.getDate("minend").toString().equals(maxStart.getDate("enddate").toString())){
					coursecost.set("enddate", minEnd.getDate("minend"));
					/*coursecost.save();*/
					maxStart.set("enddate", ToolDateTime.getSpecifiedDayBefore(ToolDateTime.format(startdate,"yyyy-MM-dd")));
					maxStart.update();
				}else{
					coursecost.set("enddate", ToolDateTime.getSpecifiedDayBefore(ToolDateTime.format(minEnd.getDate("startdate"),"yyyy-MM-dd")));
				}
			if(coursecost.getFloat("yicost")==null){
				coursecost.set("yichost", 0);
			}
			if(coursecost.getFloat("xiaobancost")==null){
				coursecost.set("xiaobancost", 0);
			}
				coursecost.save();
				
			}
			json.put("code", 1);
			json.put("msg", "保存成功");
		}catch(Exception ex){
			ex.printStackTrace();
			json.put("code", 0);
			json.put("msg", "保存失败");
		}
		return json;
	}
	
	public String getEnddate(Date date, int tid){
		Coursecost coursecost = Coursecost.dao.findFirst("select min(enddate) from coursecost where teacherid = ? and enddate > ?",tid,date);
		if(coursecost.getDate("MIN(enddate)")==null){
			return null;
		}
		Date date1 = coursecost.getDate("MIN(enddate)");
		String str=ToolDateTime.format(date1,"yyyy-MM-dd");  
		return str;		
	}

	public Coursecost getCourecostByenddate(String enddate,int tid) {
		Coursecost coursecost = Coursecost.dao.findFirst("select * from coursecost where teacherid = ? and enddate = ?",tid,enddate);
		return coursecost;
	}

	public Coursecost getCourecostBylast(int tid) {
		Coursecost coursecost = Coursecost.dao.findFirst("select * from coursecost where teacherid = ? and enddate is null",tid);
		return coursecost;
	}

	public List<Coursecost> queryCostlistByTeacherId(String tid) {
		List<Coursecost> coursecosts = Coursecost.dao.find("select c.*,a.REAL_NAME from coursecost c left join account a on a.id = c.creatid where teacherid = ? ORDER BY c.startdate DESC",tid);
		return coursecosts;
	}
}
