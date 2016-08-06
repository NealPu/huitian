package com.momathink.finance.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.finance.model.Salary;
import com.momathink.finance.service.SalaryService;
import com.momathink.teaching.teacher.model.Coursecost;
import com.momathink.teaching.teacher.model.Teacher;
import com.momathink.teaching.teacher.service.CoursecostService;

/**
 * 薪资管理
 * 
 * @author David
 */
@Controller(controllerKey = "/salary")
public class SalaryController extends BaseController {
	private static final Logger logger = Logger.getLogger(SalaryController.class);
	private SalaryService salaryService = new SalaryService();
	private CoursecostService coursecostService = new CoursecostService();
	/**
	 * 教师考勤
	 * 
	 * @author David
	 */
	public void index(){
		salaryService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/finance/Salary_management.jsp");
	}
	
	public void jisuanlist() throws ParseException{
		String tid = getPara("id");
		String yuefen = getPara("yuefen");
		String zks = getPara("zks");
		String zc = getPara("zc");
		String cd = getPara("cd");
		String bq = getPara("bq");
		String wei = getPara("wei");
		if(StringUtils.isEmpty(tid)){
			setAttr("msg", "没有该教师信息");
		}else{
			Teacher teacher = Teacher.dao.findById(Integer.parseInt(tid));
			setAttr("teacher", teacher);
			List <Coursecost> costList = coursecostService.queryCostlistByTeacherId(tid);
			setAttr("costList", costList);
		}
		List<Record> list1 = salaryService.getsalarylist(tid, yuefen);
		List<Coursecost> list2 = Coursecost.dao.find("select * from coursecost where teacherid = ?",tid);
		float sumhoures = 0;
		float sumcost = 0;
		for(Record teacherchecke : list1){
			String date0 = teacherchecke.getStr("yuefen");
			String sklx = teacherchecke.getStr("sklx");
			double classhour = Integer.parseInt(teacherchecke.get("iscancel").toString())==0?teacherchecke.getBigDecimal("class_hour").floatValue():Double.parseDouble(teacherchecke.get("teacherhour").toString());
			for(Coursecost coursecost : list2){
			Date date1 = coursecost.getDate("startdate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");                
		    Date date = sdf.parse(date0);  
			Date date2 = coursecost.getDate("enddate");
			if(date2 == null){
				date2 = sdf.parse("2099-12-31");				
			}
			if(date.getTime() >= date1.getTime() && date.getTime() <= date2.getTime()){
				float cost = 0;
				if(sklx.equals("一对一")){
					cost = coursecost.getFloat("yicost");
				}else{
					cost = coursecost.getFloat("xiaobancost");
				}
					teacherchecke.set("cost", cost);
					double kechou = cost*classhour;
					teacherchecke.set("kechou", kechou);
					sumcost += kechou;
					sumhoures += classhour;	
					break;
				}
			}
		}
		setAttr("list", list1);
		setAttr("sumhoures", sumhoures);
		setAttr("sumcost", sumcost);
		setAttr("tid", tid);
		setAttr("zks", zks);
		setAttr("zc", zc);
		setAttr("cd", cd);
		setAttr("bq", bq);
		setAttr("wei", wei);
		setAttr("yuefen",yuefen);
		renderJsp("/finance/layer_salaryPage.jsp");
	}
	
	public void save(){
		JSONObject json = new JSONObject();
		try {
		Salary salary = getModel(Salary.class);
		salary.set("creat_time", new Date());
		salary.set("creat_id",getSysuserId());
		salary.save();
		redirect("/teacherchecke/index");
		json.put("code", 1);
		json.put("msg", "添加成功");
		} catch (Exception ex) {
			logger.error(ex.toString());
			json.put("code", 0);
			json.put("msg", "添加信息异常，请联系管理员");
		}
		renderJson(json);
	}
}
