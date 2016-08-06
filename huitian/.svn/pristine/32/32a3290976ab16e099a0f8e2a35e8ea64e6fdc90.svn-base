package com.momathink.teaching.classtype.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName="banci_course")
public class BanciCourse extends BaseModel<BanciCourse> {
	
	private static final long serialVersionUID = 1L;
	public static final BanciCourse dao = new BanciCourse();
	
	public List<BanciCourse> getBanciCourse(Integer classId) {
		String sql = "select bc.*,c.course_name from banci_course bc left join course c on c.id=bc.course_id where banci_id= ? group by bc.course_id ";
		List<BanciCourse> list = BanciCourse.dao.find(sql, classId);
		return list;
	}
	
	public List<BanciCourse> getBanciCourseCanUse(Integer id) {
		return BanciCourse.dao.find("select bc.*,c.course_name,c.Id courseid from banci_course bc left join course c on c.id=bc.course_id where banci_id= ? and c.state = 0 ",id);
	}

}
