package com.momathink.teaching.teacher.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;

@Table(tableName = "account")
public class Teacher extends BaseModel<Teacher> {

	private static final long serialVersionUID = 8091569877737327778L;
	public static final Teacher dao = new Teacher();
	/**
	 * 检查是否存在相应字段的数据
	 * @param field
	 * @param value
	 * @param mediatorId
	 * @return
	 */
	public Long queryCount(String field, String value, String id) {
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			StringBuffer sql = new StringBuffer("select count(1) from account s where 1=1 and ");
			sql.append(field).append("='").append(value).append("'");
			if (!ToolString.isNull(id)) {
				sql.append(" and id != ").append(id);
			}
			return Db.queryLong(sql.toString());
		} else {
			return null;
		}
	}
	public Teacher getTeacherByName(String tname) {
		String sql = "select * from account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0 AND REAL_NAME = ? ";
		Teacher teacher = Teacher.dao.findFirst(sql, tname);
		return teacher;
	}
	public String getTeacherNameById(String tid) {
		if(StringUtils.isEmpty(tid)){
			return null;
		}else{
			Teacher teacher = dao.findById(Integer.parseInt(tid));
			return teacher.getStr("REAL_NAME");
		}
	}
	
	/**
	 * 获取老师
	 * @return
	 */
	public List<Teacher> getTeachers() {
		String sql = "select * from account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0 ORDER BY state, CONVERT(REAL_NAME USING gbk)";
		List<Teacher> list = dao.find(sql);
		return list;
	}
	
	
	/**
	 * 根绝课程id获取能上该课程的老师
	 * @param courseid
	 * @return
	 */
	public List<Teacher> getTeachersByCourseids(String courseid){
		if(StrKit.isBlank(courseid))
			return null;
		else
			courseid = "|"+courseid+"|";
		String sql = " SELECT tch.* FROM account tch LEFT JOIN (SELECT Id,CONCAT('|',CLASS_TYPE,'|') classtypeids FROM `account` WHERE "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0) a ON a.Id=tch.Id WHERE a.classtypeids LIKE '%"+courseid+"%' ";
		return dao.find(sql);
	}
	
	public List<Teacher> getTeachersByCourseidsState(String courseid){
		if(StrKit.isBlank(courseid))
			return null;
		else
			courseid = "|"+courseid+"|";
		String sql = " SELECT tch.* FROM account tch LEFT JOIN (SELECT Id,CONCAT('|',CLASS_TYPE,'|') classtypeids FROM `account` WHERE "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0) a ON a.Id=tch.Id WHERE tch.state = 0 and a.classtypeids LIKE '%"+courseid+"%' ";
		return dao.find(sql);
	}
	/**
	 * 获取某个校区下的所有教师
	 * @param campusid
	 * @return
	 */
	public List<Teacher> getTeachersByCampusid(String campusid) {
		String sql ="SELECT t.Id,t.REAL_NAME FROM account t "
				+ " LEFT JOIN account_campus ac ON account_id = t.id "
				+ " LEFT JOIN campus c on ac.campus_id = c.Id WHERE "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', t.roleids) ) > 0 and c.Id =?";
		return dao.find(sql,campusid);
	}
	/**
	 * 根据ids查询教师
	 * @param ids
	 * @return
	 */
	public List<Teacher> findByIds(String ids) {
		StringBuffer sf = new StringBuffer();
		sf.append("select id,real_name from account where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', roleids) ) > 0 and state=0  ") ;
		if(!ids.equals("")){
			sf.append(" and id in (").append(ids).append(")");
		}
		return dao.find(sf.toString());
	}
	
}
