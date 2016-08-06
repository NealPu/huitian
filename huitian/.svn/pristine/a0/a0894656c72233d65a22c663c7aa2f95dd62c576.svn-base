package com.momathink.teaching.subject.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolString;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.subject.model.Subject;

@Controller(controllerKey="/subject")
public class SubjectController extends BaseController {
	private static final Logger logger = Logger.getLogger(SubjectController.class);
	
	/**
	 * 科目管理
	 */
	public void findSubjectManager() {
		try{
			List<Subject> subject = Subject.dao.getSubject();
			setAttr("subject", subject);
			renderJsp("/subject/findSubjectManager.jsp");
		}catch(Exception e)
		{
			logger.error("SubjectController.findSubjectManager",e);
			renderJsp("/subject/findSubjectManager.jsp");
		}
	}
	/**
	 * 验证科目名是否已存在
	 */
	public void checkSubjectName()
	{
		try{
			String subjectName=getPara("subjectName");
			Subject subject=Subject.dao.findFirst("select * from subject where state=0 and Subject_Name=?",subjectName);
			boolean flag=false;
			if(subject!=null)
			{
				flag=true;
			}
			renderJson(flag);
		}catch(Exception e)
		{
			logger.error("SubjectController.checkSubjectName",e);
			renderJson(false);
		}
	}
	/**
	 * 添加或更新科目信息
	 */
	public void doAddSubjectManager() {
		String id = getPara("id"); // 科目id
		String subjectName = getPara("subjectName"); // 科目名称
		try {
			if (!ToolString.isNull(id)) {// id不为空更新，为空添加
				Subject.dao.findById(id).set("subject_Name", subjectName).update();
				Integer courseSubjectId = -Integer.parseInt(id);
				Course course = Course.dao.findById(courseSubjectId);
				course.set("COURSE_NAME", subjectName).set("UPDATE_TIME", new Date()).set("version", course.getInt("version")+1).update();
			} else {
				Subject sub = new Subject();
				sub.set("subject_Name", subjectName).save();
				new Course().set("Id",-sub.getInt("Id")).set("COURSE_NAME", sub.getStr("SUBJECT_NAME"))
					.set("CREATE_TIME", new Date()).set("UPDATE_TIME", new Date()).set("SUBJECT_ID", 0).save();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SubjectController.doAddSubjectManager", e);
		}
		renderJson("1");
	}
	/**
	 * 根据科目id查找科目的信息回显到页面
	 */
	public void editSubjectManager(){
		try{
			String subject_id = getPara();//科目id
			Record record = Db.findById("subject",subject_id);
			if(!ToolString.isNull(subject_id)){
				setAttr("id", subject_id);
				setAttr("subjectName", record.get("SUBJECT_NAME"));
			}
			renderJsp("/subject/addSubjectManager.jsp");
		}catch(Exception e)
		{
			logger.error("SubjectController.editSubjectManager",e);
			renderJsp("/subject/addSubjectManager.jsp");
		}
	}
	/**
	 * 删除科目:检查该科目下是否有课程
	 */
	public void delSubject1() {
		try {
			if (getParaToInt("subjectId") != null) {
				int id = getParaToInt("subjectId");
				// 查询该科目下是否有课程
				String sql = "SELECT * from course c where c.SUBJECT_ID=?";
				if (Db.find(sql, id).size() > 0)// 如果有排课未开始或未结束
				{
					renderJson("result", "该科目已有课程，禁止删除！");
				} else {
					Db.update("update subject set state=1 where id=?", id);// 改变状态，删除成功
					renderJson("result", "true");
				}
			}
		} catch (Exception e) {
			logger.error("SubjectController.delSubject", e);
		}
	}
	/**
	 * 变更科目顺序
	 */
	public void modifySubjectOrder() {
		try {
			if (getParaToInt("subjectId") != null) {
				int id = getParaToInt("subjectId");
				int sortOrder = getParaToInt("sortOrder");
				String sql = "SELECT * from subject s where s.id=?";
				if (Db.find(sql, id).size() > 0){
					Db.update("update subject set sortorder="+sortOrder+" where id=?", id);
					renderJson("result", "true");
				} else {
					renderJson("result", "该科目不存在！");
				}
			}
		} catch (Exception e) {
			logger.error("SubjectController.delSubject", e);
		}
	}
}
