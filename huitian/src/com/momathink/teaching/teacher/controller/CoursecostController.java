package com.momathink.teaching.teacher.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.teaching.teacher.model.Coursecost;
import com.momathink.teaching.teacher.model.Teacher;
import com.momathink.teaching.teacher.service.CoursecostService;


@Controller(controllerKey = "/teacher/coursecost")
public class CoursecostController extends BaseController {
	private static final Logger logger = Logger.getLogger(CoursecostController.class);
	private CoursecostService coursecostService = new CoursecostService();
	public void toCoursecostPage(){
		try{
			String tid = getPara();
			if(StringUtils.isEmpty(tid)){
				setAttr("msg", "没有该教师信息");
			}else{
				Teacher teacher = Teacher.dao.findById(Integer.parseInt(tid));
				setAttr("teacher", teacher);
				List <Coursecost> costList = coursecostService.queryCostlistByTeacherId(tid);
				setAttr("costList", costList);
			}
			renderJsp("/teacher/layer_coursecostPage.jsp");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 保存课时费
	 */
	public void save() {
		JSONObject json = new JSONObject();
		try {
			Coursecost coursecost = getModel(Coursecost.class);
			coursecost.set("creattime", new Date());
			coursecost.set("creatid",getSysuserId());
			int tid = coursecost.getInt("teacherid");
			Date date1 = coursecost.getDate("startdate");//开始时间
			json = coursecostService.saveTeacherCourseCost(tid,coursecost,date1);
			/*String str2 = ToolDateTime.getSpecifiedDayBefore(ToolDateTime.format(date1,"yyyy-MM-dd"));//开始时间前一天
			String enddate = coursecostService.getEnddate(date1,tid);
			coursecost.set("enddate",enddate);
			Coursecost coursecost1 = new Coursecost();
			if(enddate!=null){
			coursecost1 = coursecostService.getCourecostByenddate(enddate,tid);
			}else{
			coursecost1 = coursecostService.getCourecostBylast(tid);
			}
			if(coursecost1!=null){
			coursecost1.set("enddate",str2).update();
			}
			coursecost.save();*/
			/*json.put("code", 1);
			json.put("msg", "添加成功");*/
		} catch (Exception ex) {
			logger.error(ex.toString());
			json.put("code", 0);
			json.put("msg", "添加信息异常，请联系管理员");
		}
		renderJson(json);
	}
	
	public void checkExist(){
		String date = getPara("date");
		String  tid = getPara("tid");
		long count = 0L;
			count = Coursecost.dao.queryCount(date,tid);
		if(count==0){
			renderJson("result",null);
		}else{
			renderJson("result",count);
		}
	}
	
	public void delete(){
		JSONObject json = new JSONObject();
		String msg="删除成功";
		try{
		String id = getPara("id");
			Coursecost.dao.deleteById(id);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		json.put("msg", msg);
		renderJson(json);
	}
}