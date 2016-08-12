package com.momathink.sys.system.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolString;
/***
 * 账户 管理
 */
@Table(tableName = "account")
public class SysUser extends BaseModel<SysUser> {

	private static final long serialVersionUID = 4762813779629969917L;
	public static final SysUser dao = new SysUser();

	/** 获取 所有账户  使用的 角色 组合 */
	public List<SysUser> queryAllRoleids(){
		return dao.find("SELECT roleids FROM account WHERE roleids != ''  GROUP BY roleids");
	}
	
	public List<SysUser> getSysUser(){
		return dao.find("select * from account a where state=0 and "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', a.roleids) ) = 0 "
				+ " and LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', a.roleids) ) = 0");
	}
	/**
	 * 获取督导
	 * @return
	 */
	public List<SysUser> getDudao(){
		return dao.find("select * from  account where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'dudao'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 获取教务
	 * @return
	 */
	public List<SysUser> getJiaowu(){
		return dao.find("select * from  account where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'jiaowu'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 获取市场
	 * @return
	 */
	public List<SysUser> getScuserid(){
		return dao.find("select * from  account where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'shichang'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 获取课程顾问
	 * @return
	 */
	public List<SysUser> getKcuserid(){
		return dao.find("select * from  account where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 检查是否存在相应字段的数据
	 * @param field
	 * @param value
	 * @param mediatorId
	 * @return
	 */
	public Long queryCount(String field, String value, String id) {
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			StringBuffer sql = new StringBuffer("select count(1) from account  "
					+ " where 1=1  and ");
			sql.append(field).append("='").append(value).append("'");
			if (!ToolString.isNull(id)) {
				sql.append(" and id != ").append(id);
			}
			return Db.queryLong(sql.toString());
		} else {
			return null;
		}
	}
	/**
	 * 获取所有的用户 不包含老师和学生
	 * @return
	 */
	public List<SysUser> getAllSysUsers() {
		String sql = "select s.* from account s where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', s.roleids) ) = 0 "
					+ " and LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) = 0 ";
		List<SysUser> list = dao.find(sql);
		return list;
	}

	/**
	 * 获取课程顾问
	 * @return
	 */
	public List<SysUser> getKechengguwen(String campusId) {
		return dao.find("select account.Id,account.REAL_NAME,account.SEX,campus.CAMPUS_NAME from  account left join campus on account.campusid = campus.id where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 某校区课程顾问
	 * @param campusid
	 * @return
	 */
	public List<SysUser> getKechengguwenCampus(String campusid){
		return dao.find("select CONCAT('|',a.id,'|')  kcgwids , a.* from  account a left join account_campus ac on a.id=ac.account_id where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', a.roleids) ) > 0 and ac.campus_id = ? ",campusid);
	}
	
	/**
	 * 获取教务
	 * @param campusId
	 * @return
	 */
	public List<SysUser> getJiaowu(String campusId) {
		return dao.find("select * from  account where "
				+ "LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'jiaowu'), CONCAT(',', roleids) ) > 0");
	}
	/**
	 * 获取财务
	 * @param campusId
	 * @return
	 */
	public List<SysUser> getCaiwu(String campusId) {
		return dao.find("select * from  account where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'caiwu'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 获取某校区的所有督导
	 */
	public List<SysUser> getTutorsByCampusid(String campusid) {
		return dao.find("select a.* from account a left join account_campus ac on a.id=ac.account_id where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'dudao'), CONCAT(',', a.roleids) ) > 0 and ac.campus_id = ? ", campusid);
	}
	/**
	 * 获取某校区的所有课程顾问*
	 */
	public List<SysUser> getCampusKcgws(String campusid){
		return dao.find("select a.*,CONCAT('|',a.id,'|') KCGWIDS from account a "
				+ " left join account_campus ac on a.id=ac.account_id where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', a.roleids) ) > 0 and ac.campus_id in( "+campusid+" )");
	}
	public List<SysUser> getAllKcgw(){
		return dao.find("select * from account  where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'kcgw'), CONCAT(',', roleids) ) > 0");
	}
	
	/**
	 * 获取某校区的所有教务
	 */
	public List<SysUser> getJiaowuByCampusid(String campusid) {
		return dao.find("select a.* from account a left join account_campus ac on a.id=ac.account_id where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'jiaowu'), CONCAT(',', a.roleids) ) > 0  and ac.campus_id = ? ", campusid);
	}
	
	/**
	 * 获取某校区的所有市场
	 */
	public List<SysUser> getShichangByCampusid(String campusid) {
		return dao.find("select a.* from account a left join account_campus ac on a.id=ac.account_id where "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'shichang'), CONCAT(',', a.roleids) ) > 0 and ac.campus_id = ? ", campusid);
	}
	/**
	 * 获取所有的发送报告权限的用户*
	 * @return
	 */
	public List<SysUser> findAllSysUser() {
		String sql = " select id ,REAL_NAME,roleids from account where state <> 2 ";
		return dao.find(sql);
	}
	
	
	public Integer insertNetUserAccount( String email , String accountName , String netSchoolId  ) {
		String insertSql = " insert into account( email , real_name , user_pwd , create_time , user_type , netschoolid ) "
				+ " values ( ? , ? , ? , now() , 0 , ? ) ";
		Db.update( insertSql , email , accountName , ToolMD5.getMD5( "111111" ) , netSchoolId );
		SysUser netAccount = SysUser.dao.queryByNetSchoolId( netSchoolId );
		if( null != netAccount ) {
			return netAccount.getInt( "id" );
		}
		return null;
	}

	public SysUser queryByNetSchoolId( String netSchoolId ) {
		String sql = " select id , email ,real_name , user_type , roleids , netschoolid from account where netschoolid = ? ";
		return dao.findFirst( sql , netSchoolId );
	}
	
	
	
}
