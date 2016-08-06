package com.momathink.teaching.subject.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "subject")
public class Subject extends BaseModel<Subject> {

	private static final long serialVersionUID = 853601691942071700L;
	public static final Subject dao = new Subject();

	/**
	 * 查询所有的科目
	 */
	public List<Subject> getSubject() {
		List<Subject> list = dao.find("SELECT * from subject WHERE STATE=0 order by subject_name");
		return list;
	}
	
	/**
	 * 查询所有的科目
	 */
	public List<Subject> getSubject(int schoolId) {
		List<Subject> list = dao.find("SELECT * from subject WHERE school_id=? and STATE=0 ",schoolId);
		return list;
	}

	/**
	 * 根据科目状态查询科目
	 * @param state
	 * @return
	 */
	public List<Subject> findSubjectByState(int state) {
		return dao.find("SELECT * from subject WHERE STATE=? ",state);
	}

	/**
	 * 根据传入的IDS进行查询
	 * @param subjectIds：格式为‘,’分割
	 * @return
	 */
	public List<Subject> findByIds(String subjectIds) {
		return StringUtils.isEmpty(subjectIds)?null:dao.find("SELECT * from subject WHERE STATE=0 AND id IN("+subjectIds+")");
	}

	/**
	 * 根据科目Id获取科目名称
	 * @param subjectId
	 * @return
	 */
	public String getSubjectNameById(String subjectId) {
		return StringUtils.isEmpty(subjectId)?null:dao.findById(Integer.parseInt(subjectId)).getStr("subject_name");
	}

	/**
	 * 根据科目IDS查询科目名称
	 * @param subjectids：格式为|分割
	 * @return
	 */
	public String getSubjectNameByIds(String subjectids) {

		StringBuffer subjectNames = new StringBuffer();
		if (StringUtils.isEmpty(subjectids)) {
			subjectNames.append("无");
		} else {
			if(subjectids.substring(0, 1).equals("|")){
				subjectids = subjectids.substring(1);
			}
			String _sids = subjectids.replace("|", ",");
			List<Subject> slist = Subject.dao.findByIds(_sids);
			if (slist == null || slist.size() == 0) {
				subjectNames.append("无");
			} else {
				for (Subject s : slist) {
					subjectNames.append(s.getStr("SUBJECT_NAME")).append("、");
				}
				subjectNames.deleteCharAt(subjectNames.length() - 1);
			}
		}
		return subjectNames.toString();
	
	}
	/**
	 * 根据课程名称查找
	 * @param subjectName
	 * @return
	 */
	public Subject findSubjectByName(String subjectName) {
		String sql = "select * from subject where SUBJECT_NAME = ? ";
		Subject sub = dao.findFirst(sql, subjectName);
		return sub;
	}

	/**
	 *  学生订单下的科目
	 * @param stuid
	 * @return
	 */
	public List<Subject> getStudentCourseOrderSubject(String stuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT subject.Id,subject.SUBJECT_NAME FROM subject WHERE FIND_IN_SET(subject.Id, (SELECT REPLACE (GROUP_CONCAT(DISTINCT subjectids),'|',',') subids FROM crm_courseorder WHERE crm_courseorder.teachtype=1 and crm_courseorder.delflag=0 and crm_courseorder.status!=0 and crm_courseorder.studentid=").append(stuid).append(")) AND subject.STATE=0");
		return dao.find(sb.toString());
	}

	
}
