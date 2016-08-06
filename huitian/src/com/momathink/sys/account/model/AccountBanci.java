package com.momathink.sys.account.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName="account_banci")
public class AccountBanci extends BaseModel<AccountBanci> {
	private static final long serialVersionUID = 1L;
	public static final AccountBanci dao = new AccountBanci();
	
	/**
	 * 根据课程id查询可用班次详细信息
	 * @param course_id
	 * @return
	 */
	public static List<AccountBanci> findUseCourseOrder(Integer course_id){
		String sql = "SELECT\n" +
				"	account_banci.Id,\n" +
				"	banci_course.lesson_num,\n" +
				"	banci_course.course_id,\n" +
				"	account_banci.banci_id,\n" +
				"	account_banci.account_id,\n" +
				"	class_order.classNum,\n" +
				"	class_type.`name`,\n" +
				"	class_order.teachTime,\n" +
				"	class_order.lessonNum\n" +
				"FROM\n" +
				"	account_banci\n" +
				"LEFT JOIN banci_course ON account_banci.banci_id = banci_course.banci_id\n" +
				"LEFT JOIN class_order ON account_banci.banci_id = class_order.id\n" +
				"LEFT JOIN class_type ON class_order.classtype_id = class_type.id\n" +
				"WHERE\n" +
				"	banci_course.course_id = ?\n" +
				"AND class_order.teachTime >= curdate()\n" +
				"GROUP BY\n" +
				"	class_order.classNum";
		List<AccountBanci> banci = AccountBanci.dao.find(sql, course_id);
		return banci;
	}
	
	/**
	 * 根据课程id查询可用班次
	 * @param course_id
	 * @return
	 */
	public static List<AccountBanci> findCourseOrder(Integer course_id){
		String sql = "SELECT\n" +
				"	class_order.classNum,\n" +
				"	class_type.`name`,\n" +
				"	class_order.teachTime,\n" +
				"	class_order.lessonNum\n" +
				"FROM\n" +
				"	account_banci\n" +
				"LEFT JOIN banci_course ON account_banci.banci_id = banci_course.banci_id\n" +
				"LEFT JOIN class_order ON account_banci.banci_id = class_order.id\n" +
				"LEFT JOIN class_type ON class_order.classtype_id = class_type.id\n" +
				"WHERE\n" +
				"	banci_course.course_id = ?\n" +
				"AND class_order.teachTime >= curdate()\n" +
				"GROUP BY\n" +
				"	class_order.classNum";
		List<AccountBanci> banci = AccountBanci.dao.find(sql, course_id);
		return banci;
	}

	public List<AccountBanci> findByAccountId(String accountId) {
		return dao.find("select a.*,c.classNum,c.lessonnum,c.totalfee from account_banci a LEFT JOIN class_order c ON a.banci_id=c.id where a.state=0 and a.account_id = ?",Integer.parseInt(accountId));
	}
	
	/**
	 * 查询学生的所有班课，包括退班的
	 * @param accountId
	 * @return
	 */
	public List<AccountBanci> findAllByAccountId(String accountId) {
		return dao.find("select a.*,c.classNum,c.lessonnum,c.totalfee from account_banci a LEFT JOIN class_order c ON a.banci_id=c.id where a.account_id = ?",Integer.parseInt(accountId));
	}
	
	public AccountBanci findABbyStuClassId(String stuId,Integer classId){
		String sql = "select * from account_banci where account_id= ? and banci_id= ?";
		return dao.findFirst(sql, stuId,classId);
	}

	public List<AccountBanci> findABbyClassId(Integer classId) {
		// TODO Auto-generated method stub
		String sql = "select * from account_banci where banci_id= ? and state = 0";
		return dao.find(sql, classId);
	}

	/**
	 * 根据班ID查询该班下的所有学生姓名 
	 * @author David
	 * @param banciId 班次ID
	 * @return
	 */
	public String getStudentNameByBanId(int banciId) {
		String names = Db.queryStr("SELECT GROUP_CONCAT(stu.real_name) from account_banci ab left join account stu on ab.account_id=stu.id where stu.state=0 and ab.banci_id=?",banciId);
		return names;
	}

	/**
	 * 根据班次ID查询该班下的学生人数
	 * @author David
	 * @param classOrderId
	 */
	public Long getStudentCountByClassOrderId(Integer classOrderId,Integer accountId) {
		return Db.queryLong("SELECT COUNT(1) stuCount from account_banci ab WHERE ab.banci_id=? AND ab.account_id !=? AND ab.state=0", classOrderId,accountId);
	}

	/**
	 * 根据班次ID获取班级中的所有学生
	 * @param classOrderId
	 * @return
	 */
	public List<AccountBanci> getStudentsByClassOrderId(Integer classOrderId) {
		return dao.find("select ab.*,s.REAL_NAME,s.TEL,s.EMAIL from account_banci ab left join account s on ab.account_id=s.Id where ab.banci_id=? and s.STATE=0", classOrderId);
	}

	/**
	 * 获取班次下的学生姓名
	 * @return
	 */
	public Map<Integer, String> getStudents(Integer classOrderId) {
		List<Object> paramValue = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("SELECT banci_id,GROUP_CONCAT(stu.real_name) studentName from account_banci ab left join account stu on ab.account_id=stu.id where stu.state=0 ");
		if(classOrderId != null){
			sql.append(" and banci_id = ?");
			paramValue.add(classOrderId);
		}
		List<Record> list = Db.find(sql.append("GROUP BY ab.banci_id").toString(),paramValue);
		Map<Integer,String> maps = new HashMap<Integer,String>();
		for(Record record : list){
			maps.put(record.getInt("banci_id"), record.getStr("studentName"));
		}
		return maps;
	}

	/**
	 * 获取学生的班次ID
	 * @param studentId
	 * @return
	 */
	public String getBanciIds(Integer studentId) {
		String sql ="SELECT GROUP_CONCAT(ab.banci_id) from account_banci ab where ab.account_id=?";
		return Db.queryStr(sql,studentId);
	}

}
