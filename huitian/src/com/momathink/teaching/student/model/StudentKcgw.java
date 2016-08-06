package com.momathink.teaching.student.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.sys.system.model.SysUser;
@Table(tableName = "student_kcgw")
public class StudentKcgw extends BaseModel<StudentKcgw>{
	private static final long serialVersionUID = 24099496026157413L;
	public static final StudentKcgw dao = new StudentKcgw();
	
	
	
	public List<StudentKcgw> getAllKcgwidsByStudentid(int studentId) {
		return dao.find("select * from student_kcgw where student_id = ? ", studentId);
	}
	public StudentKcgw getKcgwNames(String para) {
		return dao.findFirst("SELECT GROUP_CONCAT(k.REAL_NAME) real_name,CONCAT(',',GROUP_CONCAT(ak.kcgw_id),',') kcgwids,"
							+" ak.student_id  id"
							+" FROM"
							+" student_kcgw ak"
							+" LEFT JOIN account a ON ak.student_id = a.Id"
							+" LEFT JOIN (select * from account where "
							+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0"
							+ " ) k ON k.Id = ak.kcgw_id"
							+" WHERE a.id = ? "+" GROUP BY a.Id ", para);
	}
	
	public List<SysUser> getKcgwByStudentId(Integer studentId) {
		String sql = "SELECT s.Id,s.REAL_NAME FROM student_kcgw sk LEFT JOIN account s ON sk.kcgw_id=s.Id WHERE sk.student_id=?";
		return SysUser.dao.find(sql, studentId);
	}
}
