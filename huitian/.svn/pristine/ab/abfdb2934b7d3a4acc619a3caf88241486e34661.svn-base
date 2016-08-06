package com.momathink.teaching.reservation.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.system.model.SysUser;
import com.momathink.sys.system.model.TimeRank;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.campus.model.Classroom;
import com.momathink.teaching.course.model.CoursePlan;
import com.momathink.teaching.reservation.model.Reservation;
import com.momathink.teaching.reservation.model.TestScorse;
import com.momathink.teaching.reservation.service.ReservationService;
import com.momathink.teaching.student.model.HeadPicture;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.teacher.model.Teacher;

/**
 * 预约测试s
 * 
 * @author Administrator
 *
 */
@Controller(controllerKey = "/reservation")
public class ReservationController extends BaseController {
	private ReservationService reservationService = new ReservationService();

	/**
	 * 预约测试学生列表
	 */
	public void index() {
		reservationService.queryMyStudents(splitPage, getSysuserId());
		setAttr("kcgwList", SysUser.dao.getKechengguwen(null));
		setAttr("showPages", splitPage.getPage());
		render("/reservation/student_list.jsp");
	}

	/**
	 * 预约
	 */
	public void reservationTest() {
		try {
			setAttr("reservation",Reservation.dao.findById(getPara()));
			setAttr("listStudent", Student.dao.getAllStudent());
			setAttr("listCampus", Campus.dao.getCampus());
			setAttr("listTimeRank", TimeRank.dao.getTimeRank());
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJsp("/reservation/reservationtest.jsp");
	}

	/**
	 * 获取可用教室
	 */
	public void getClassRoom() {
		JSONObject json = new JSONObject();
		try {
			String campusid = getPara("campusid");
			String timerankid = getPara("timerankid");
			TimeRank tr = TimeRank.dao.findById(timerankid);
			String[] rankname = tr.getStr("rank_name").split("-");
			String reservationTime = getPara("reservationTime")+" 00:00:00";
			List<Classroom> list = Classroom.dao.getClassRoomByCamp(Integer.parseInt(campusid));
			if (!list.isEmpty()) {
				for (Classroom room : list) {
					List<CoursePlan> listcp = CoursePlan.coursePlan.getCoursetimeRoomPlans(room.getInt("id").toString(), reservationTime);
					if (!listcp.isEmpty()) {
						for (CoursePlan cp : listcp) {
							String[] ranknames = cp.getStr("rank_name").split("-");
							if ((rankname[0].compareTo(ranknames[0]) < 0 && rankname[1].compareTo(ranknames[0]) > 0)
									|| (rankname[0].compareTo(ranknames[1]) < 0 && rankname[1].compareTo(ranknames[1]) > 0)
									|| (rankname[0].compareTo(ranknames[0]) >= 0 && rankname[1].compareTo(ranknames[1]) <= 0)
									|| (rankname[0].compareTo(ranknames[0]) <= 0 && rankname[1].compareTo(ranknames[1]) >= 0)) {
								room.put("code", 1);
							}
						}
					}
				}
				json.put("rooms",list);
				json.put("isroom",0);
			}else{
				json.put("isroom",1);
			}
			List<Teacher> listTeacher = Teacher.dao.getTeachersByCampusid(campusid);
			if(!listTeacher.isEmpty()){
				for(Teacher t:listTeacher){
					List<CoursePlan> listcp=CoursePlan.coursePlan.getCourseDateAndTeacherId(reservationTime,t.getInt("id"));
					if(!listcp.isEmpty()){
						for(CoursePlan cp:listcp){
							String[] ranknames = cp.getStr("rank_name").split("-");
							if ((rankname[0].compareTo(ranknames[0]) < 0 && rankname[1].compareTo(ranknames[0]) > 0)
									|| (rankname[0].compareTo(ranknames[1]) < 0 && rankname[1].compareTo(ranknames[1]) > 0)
									|| (rankname[0].compareTo(ranknames[0]) >= 0 && rankname[1].compareTo(ranknames[1]) <= 0)
									|| (rankname[0].compareTo(ranknames[0]) <= 0 && rankname[1].compareTo(ranknames[1]) >= 0)) {
								t.put("code", 1);
							}
						}
					}
				}
				json.put("teachers", listTeacher);
				json.put("isteacher", 0);
			}else{
				json.put("isteacher", 1);
			}
			json.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 1);
		}
		renderJson(json);
	}
	/**
	 * 保存预约测试信息
	 */
	public void saveReservationMessage(){
		try{
			Reservation r = getModel(Reservation.class);
			Student s = Student.dao.findById(r.getInt("studentid"));
			if(s.get("headpictureid")!=null){
				HeadPicture h = HeadPicture.dao.findById(s.getInt("headpictureid"));
				h.set("state", 0).set("sysuserid",getSysuserId()).update();
			}
			String hpid = getPara("headpictureid");
			if(!hpid.equals("")){
				HeadPicture h = HeadPicture.dao.findById(hpid);
				h.set("state", 1).set("sysuserid",getSysuserId()).update();
				s.set("headpictureid",hpid).update();
			}
			List<HeadPicture> list = HeadPicture.dao.getScrapPicture(getSysuserId());
			if(!list.isEmpty()){
				for(HeadPicture hp:list){
					File file = new File(hp.getStr("name")+hp.getStr("url"));
					file.delete();
					hp.delete();
				}
			}
			r.set("state", 1);
			r.set("sysuerid", getSysuserId());
			r.set("createtime",new Date());
			r.save();
			renderJson(0);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(1);
		}
	}
	/**
	 * 接受预约
	 */
	public void isReservation(){
		try{
			Reservation  s = Reservation.dao.findById(getPara("id"));
			CoursePlan cp = new CoursePlan();
			cp.set("ROOM_ID",s.getInt("roomid")).set("TIMERANK_ID",s.getInt("timerankid"))
			.set("CAMPUS_ID",s.getInt("campusid")).set("STUDENT_ID",s.getInt("studentid"))
			.set("TEACHER_ID",s.getInt("teacherid") ).set("COURSE_TIME",s.get("reservationtime")+" 00:00:00")
			.set("STATE",0).set("CREATE_TIME",s.get("createtime")+" 00:00:00").set("KNOWLEDGE_NAMES","")
			.set("TEACHER_PINGLUN","").set("STUDENT_PINGLUN","").save();
			s.set("state",2).set("courseplanid", cp.getPrimaryKeyValue()).update();
			renderJson(0);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(1);
		}
	}
	/**
	 * 取消预约测试
	 */
	public void deleteReservation(){
		try{
			Reservation  s = Reservation.dao.findById(getPara("id"));
			if(s.get("courseplanid")!=null||s.get("courseplanid")!=""){
				CoursePlan cp = CoursePlan.coursePlan.findById(s.getInt("courseplanid"));
				cp.delete();
			}
			s.delete();
			renderJson(0);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(1);
		}
	}
	/**
	 * 拒绝预约
	 */
	public void refuseReservation(){
		try{
			Reservation  s = Reservation.dao.findById(getPara("id"));
			s.set("state", 3).update();
			renderJson(0);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(1);
		}
	}
	/**
	 * 添加成绩
	 */
	public void addTestScores(){
		try{
			Reservation r = Reservation.dao.findByIdMessage(getPara()); 
			Student s = Student.dao.findById(r.getInt("studentid"));
			if(s.get("headpictureid")!=null){
				setAttr("hp",HeadPicture.dao.findById(s.getInt("headpictureid")));
			}else{
				setAttr("hp",null);
			}
			TestScorse ts = TestScorse.dao.findByReservationId(getPara());
			if(ts!=null){
				setAttr("code",0);
			}else{
				setAttr("code",1);
			}
			setAttr("test",ts);
			setAttr("reservation",r);
		}catch(Exception e){
			e.printStackTrace();
		}
		renderJsp("/reservation/layer_testScores.jsp");
	}
	/**
	 * 保存添加成绩
	 */
	public void saveTestScorse(){
		try{
			TestScorse ts =getModel(TestScorse.class);
				String hpid = getPara("picid");
				if(!hpid.equals("")){
					String studentid = getPara("studentid");
					Student s = Student.dao.findById(studentid);
					if(s.get("headpictureid")!=null){
						HeadPicture h = HeadPicture.dao.findById(s.getInt("headpictureid"));
						h.set("state", 0).set("sysuserid",getSysuserId()).update();
					}
					HeadPicture h = HeadPicture.dao.findById(hpid);
					h.set("state", 1).set("sysuserid",getSysuserId()).update();
					s.set("headpictureid",hpid).update();
				}
				List<HeadPicture> list = HeadPicture.dao.getScrapPicture(getSysuserId());
				if(!list.isEmpty()){
					for(HeadPicture hp:list){
						File file = new File(hp.getStr("name")+hp.getStr("url"));
						file.delete();
						hp.delete();
					}
				}
			
			if(ts.get("id")!=null){
				ts.update();
			}else{
				ts.set("sysuserid",getSysuserId());
				ts.save();
			}
		
			renderJson(0);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(1);
		}
	}
	/**
	 * 关联头像
	 */
	public void getHeadPic(){
		String id = getPara("id");
		Student s = Student.dao.findById(id);
		if(s.get("headpictureid")!=null){
			HeadPicture pic = HeadPicture.dao.findById(s.getInt("headpictureid"));
			renderJson(pic);
		}else{
			renderJson(1);
		}
		
	}
	/**
	 * 导出
	 */
	public void toExcel(){
		try{
			List<Record> list = reservationService.queryMyStudentsToExcel(splitPage, getSysuserId());
			reservationService.export(getResponse(), getRequest(), list,"预约测试表，导出日期"+new Date());
			renderNull();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
