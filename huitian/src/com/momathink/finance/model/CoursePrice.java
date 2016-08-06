package com.momathink.finance.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.teaching.course.model.CoursePlan;

@Table(tableName = "crm_courseprice")
public class CoursePrice extends BaseModel<CoursePrice> {

	private static final long serialVersionUID = -620110110316617792L;
	public static final CoursePrice dao = new CoursePrice();

	public List<CoursePrice> findCoursePriceByStudentId(String studentId) {
		String sql = "SELECT p.*,c.COURSE_NAME FROM\n" + "(SELECT cc.id,cc.subjectid,cc.courseid,SUM(cc.classhour) ks \n"
				+ "FROM crm_courseprice cc left join crm_courseorder coo on coo.id = cc.orderid and coo.delflag=0  WHERE cc.studentid=? GROUP BY cc.subjectid,cc.courseid) p\n" + "LEFT JOIN course c ON p.courseid=c.Id";
		return StringUtils.isEmpty(studentId) ? null : dao.find(sql, Integer.parseInt(studentId));
	}

	public List<CoursePrice> findByOrderId(String orderId) {
		String sql = "SELECT cp.*,c.COURSE_NAME FROM crm_courseprice cp "
				+ " LEFT JOIN course c ON cp.courseid=c.Id WHERE cp.orderid=?";
		return StringUtils.isEmpty(orderId) ? null : dao.find(sql, Integer.parseInt(orderId));
	}

	public float getRemainHour(String studentId, String courseId) {
		// 查询总节数和已经排课的节数
		Record info = dao.getCoursePirce(studentId, courseId);
		Float _ygks = info.getBigDecimal("ks").floatValue();
		float ygks = _ygks == null ? 0 : _ygks.floatValue();
		float ypks = CoursePlan.coursePlan.getUseClasshour(studentId,courseId);
		return ygks - ypks;
	}

	public Record getCoursePirce(String studentId, String courseId) {
		if (StringUtils.isEmpty(studentId) || StringUtils.isEmpty(courseId)) {
			return null;
		} else {
			String sql = "SELECT sum(cp.classhour) ks FROM crm_courseprice cp WHERE cp.studentid=? AND cp.courseid=?";
			Record record = Db.findFirst(sql, Integer.parseInt(studentId), Integer.parseInt(courseId));
			return record;
		}
	}

	/**
	 * 获取订单购买的课程
	 * @param orderId
	 * @param courseId
	 * @return
	 */
	public CoursePrice findByOrderIdAndCourseId(Integer orderId, Integer courseId) {
		String sql = "SELECT cp.*,c.COURSE_NAME FROM crm_courseprice cp LEFT JOIN course c ON cp.courseid=c.Id WHERE cp.orderid=? and cp.courseid=?";
		return dao.findFirst(sql, orderId,courseId);
	}

	public void deleteByOrderId(Integer orderId) {
		String sql = "DELETE FROM crm_courseprice WHERE crm_courseprice.orderid=?";
		Db.update(sql, orderId);
	}

	/**
	 * 获取订单的课程信息
	 * @param courseOrderId
	 * @return
	 */
	public String getCourseids(Integer courseOrderId) {
		String sql="select GROUP_CONCAT( DISTINCT courseid SEPARATOR '|') courseids from crm_courseprice cp left join course c on cp.courseid = c.id WHERE cp.orderid=?";
		CoursePrice ucourse = dao.findFirst(sql, courseOrderId);
		return ucourse==null?"":ucourse.getStr("courseids");
	}
}
