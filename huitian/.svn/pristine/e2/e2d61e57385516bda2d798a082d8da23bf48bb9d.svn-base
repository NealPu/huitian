package com.momathink.teaching.course.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.account.service.AccountService;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.course.model.CourseBack;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.course.service.CourseCancelService;

@Controller(controllerKey="/coursecancel")
public class CourseCancelController extends BaseController {

	private CourseCancelService coursecancelService = new CourseCancelService();
	private AccountService accountService = new AccountService();
	/**
	 * 查看已取消课程信息*
	 * */
	public void index() {
		try{
			List<Campus> campusList = Campus.dao.getCampus();
			coursecancelService.list(splitPage);
			setAttr("showPages", splitPage.getPage());
			setAttr("campuslist", campusList);
			renderJsp("/course/findDelCoursePlan.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 恢复课程信息*
	 */
	public void restoreCoursePlan(){
		JSONObject json = new JSONObject();
		try{
			boolean teacherflag = false;
			String id = getPara("id");
			CourseBack cb = CourseBack.dao.findByIdToMessage(id);
			String[] ctime = cb.getStr("RANK_NAME").split("-");
			List<CoursePlan> cplist =CoursePlan.coursePlan.getTeacherCoursePlansByDay(cb.getStr("course_time"),cb.getInt("teacher_id").toString());
			if(cplist.isEmpty()){
				teacherflag = true;
			}else{
				for(CoursePlan cp :cplist){
					String[] ytime = cp.getStr("RANK_NAME").split("-");
					if(ctime[1].compareTo(ytime[0])<=0||ctime[0].compareTo(ytime[1])>0){
						teacherflag = true;
						break;
					}
				}
				if(!teacherflag){
					json.put("code",1);
				}
			}
			boolean studentflag=false;
			List<CoursePlan> slist = CoursePlan.coursePlan.getCoursePlansByDay(cb.getStr("course_time"),cb.getInt("student_id").toString());
			if(slist.isEmpty()){
				studentflag = true;
			}else{
				for(CoursePlan cp :slist){
					String[] ytime = cp.getStr("RANK_NAME").split("-");
					if(ctime[1].compareTo(ytime[0])<=0||ctime[0].compareTo(ytime[1])>0){
						studentflag = true;
						break;
					}
				}
				if(!studentflag){
					json.put("code",2);
				}
			}
			boolean roomflag = false;
			List<CoursePlan> rlist = CoursePlan.coursePlan.getCoursePlansByRoomId(cb.getInt("room_id"),cb.getStr("course_time"));
			if(rlist.isEmpty()){
				roomflag = true;
			}else{
				for(CoursePlan cp :rlist){
					String[] ytime = cp.getStr("RANK_NAME").split("-");
					if(ctime[1].compareTo(ytime[0])<=0||ctime[0].compareTo(ytime[1])>0){
						roomflag = true;
						break;
					}
				}
				if(!roomflag){
					json.put("code",3);
				}
			}
			if(teacherflag&&studentflag&&roomflag){
				boolean result = accountService.consumeCourse(Integer.parseInt(id), cb.getInt("student_id"), getSysuserId(),0);
				if(result){
					String sql1 = "insert into courseplan SELECT * from  courseplan_back where ID=? ";
					Db.update(sql1,id);
					CourseBack c= CourseBack.dao.findById(id);
					c.delete();
					json.put("code", 0);
				}else{
					json.put("code", 4);
				}
			}
			renderJson(json);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
