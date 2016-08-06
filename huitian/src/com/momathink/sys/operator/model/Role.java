package com.momathink.sys.operator.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolOperatorSession;

/** 角色 管理  
 * */
@Table(tableName="pt_role")
public class Role extends BaseModel<Role> {
	private static final long serialVersionUID = 2014788334586877154L;
	public static final Role dao = new Role();
	
	/**
	 * 重写save方法
	 */
	public boolean save() {
		if(super.save()){
			ToolOperatorSession.refreshRoleMap(this);
			return true;
		}
		return false;
	}
	
	/**
	 * 重写update方法
	 */
	public boolean update() {
		this.set("version", this.getLong("version")+1);
		if(super.update()){
			ToolOperatorSession.refreshRoleMap(this);
			return true;
		}
		return false;
	}
	
	/**
	 * 根据roleids判断是否是管理员
	 * @param roleids
	 * @return
	 */
	public static boolean isAdmins(String roleids){
		return ToolOperatorSession.judgeRole("admins", roleids);
	}
	
	/**
	 * 根据roleids判断是否是学生
	 * @param roleids
	 * @return
	 */
	public static boolean isStudent(String roleids){
		return ToolOperatorSession.judgeRole("student", roleids);
	}
	
	/**
	 * 根据roleids判断是否是教师
	 * @param roleids
	 * @return
	 */
	public static boolean isTeacher(String roleids){
		return ToolOperatorSession.judgeRole("teachers", roleids);
	}
	
	/**
	 * 根据roleids判断是否是教务
	 * @param roleids
	 * @return
	 */
	public static boolean isJiaowu(String roleids){
		return ToolOperatorSession.judgeRole("jiaowu", roleids);
	}
	
	/**
	 * 根据roleids判断是否是督导
	 * @param roleids
	 * @return
	 */
	public static boolean isDudao(String roleids){
		return ToolOperatorSession.judgeRole("dudao", roleids);
	}
	
	/**
	 * 根据roleids判断是否是市场
	 * @param roleids
	 * @return
	 */
	public static boolean isShichang(String roleids){
		return ToolOperatorSession.judgeRole("shichang", roleids);
	}
	
	/**
	 * 根据roleids判断是否是课程顾问负责人
	 * @param roleids
	 * @return
	 */
	public static boolean isKcgwfzr(String roleids){
		return ToolOperatorSession.judgeRole("kechengguwen", roleids);
	}
	
	/**
	 * 根据roleids判断是否是财务
	 * @param roleids
	 * @return
	 */
	public static boolean isCaiwu(String roleids){
		return ToolOperatorSession.judgeRole("caiwu", roleids);
	}
	
	/**
	 * 根据roleids判断是否是课程顾问
	 * @param roleids
	 * @return
	 */
	public static boolean isKcgw(String roleids){
		return ToolOperatorSession.judgeRole("kcgw", roleids);
	}
	
	/**
	 * 根据roleids判断是否是教学总监
	 * @param roleids
	 * @return
	 */
	public static boolean isJxzj(String roleids){
		return ToolOperatorSession.judgeRole("jxzj", roleids);
	}
	
	/**
	 * 根据roleids判断是否是课程内容总监
	 * @param roleids
	 * @return
	 */
	public static boolean isKcnrzj(String roleids){
		return ToolOperatorSession.judgeRole("kcnrzj", roleids);
	}
	
	/**
	 * 获取所有的角色*
	 * @return
	 */
	public List<Role> getAllRole() {
		String  sql="select id,names,numbers from pt_role ";
		return dao.find(sql);
	}
	
	/**
	 * 获取老师和学生以外所有的角色*
	 * @return
	 */
	public Object getAllRoleNost() {
		String sql ="select id,names from pt_role where numbers <> 'student' and numbers <> 'teacher'";
		return dao.find(sql);
	}
	
	/**
	 * 保存学生保存role表numbers为student的id*
	 * @param string
	 * @return
	 */
	public Role getRoleByNumbers(String numbers) {
		String sql = "select id from pt_role where numbers = '"+numbers+"'";
		return dao.findFirst(sql);
	}
	
	/**
	 * 找到用户已分配的角色*
	 * @param ids
	 * @return
	 */
	public List<Role> getRoleNamesByIds(String ids) {
		String  sql = "select id,names,operatorids from pt_role where id in ("+ids+")";
		return dao.find(sql);
	}
	
	/***
	 *  根据 id 查询 所有 的 角色
	 * @param ids 允许 ,  逗号切割的 形式
	 * @return
	 */
	public List<Role> queryById(String ids){
		return dao.find("SELECT * FROM pt_role WHERE id IN (?)", ids);
	}
}
