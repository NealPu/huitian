package com.momathink.sys.operator.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/**
 菜单 模块 管理
 */
@Table(tableName="pt_module")
public class Module extends BaseModel<Module> {

	private static final long serialVersionUID = -1515933061522779523L;
	public static final Module dao = new Module();
	
	/** 查询所有的 模块 */
	public List<Module> queryAll(){
		return dao.find("SELECT * FROM pt_module");
	}

	/** 查询 现有 排序 最大的 数字  */
	public Long queryByOrderidsMax() {
		String sql = "SELECT MAX(orderids) FROM pt_module";
		return Db.queryNumber(sql).longValue();
	}

	/*** 根据父级模块 查询子模块 */
	public List<Module> queryByParentmoduleids(String parentmoduleids) {
		String sql = "SELECT * FROM pt_module WHERE parentmoduleids = ? ";
		return dao.find(sql, parentmoduleids);
	}

	/**
	 * 根据模块标题  获取应有的模块功能权限*
	 * @param systemsid
	 * @return
	 */
	public static List<Module> getFeatures(String systemsid) {
		String  sql ="SELECT m.*,i.name iconname FROM pt_module m "
				+ " LEFT JOIN pt_icon i on m.iconid = i.id "
				+ "  WHERE ISNULL(m.parentmoduleids) AND  m.systemsids = "+systemsid;
		return dao.find(sql);
	}
	
	/**
	 * 获取模块下的小功能*
	 * @param moduleid
	 * @return
	 */
	public List<Module> queryBYParentmoduleids(String parentmoduleids) {
		String sql = "SELECT * FROM pt_module WHERE parentmoduleids IN("+parentmoduleids+") ORDER BY parentmoduleids ASC";
		return dao.find(sql);
	}

	public List<Module> findByAllOperator(Integer sysuserId) {
		String sql = "SELECT count(a.systemsids) maxsyscount ,a.systemsids FROM ( "
				+ " SELECT mo.systemsids,mo.`names`,  mo.parentmoduleids ,mo.id "
				+ " FROM pt_module mo  LEFT JOIN pt_module pmo ON pmo.id = mo.parentmoduleids "
				+ " WHERE FIND_IN_SET(mo.id ,(  SELECT GROUP_CONCAT(DISTINCT moduleids) FROM pt_operator "
				+ " WHERE FIND_IN_SET(id,( SELECT GROUP_CONCAT( DISTINCT REPLACE( operatorids,'operator_','') ) FROM pt_role "
				+ " WHERE FIND_IN_SET( id , (SELECT GROUP_CONCAT(DISTINCT roleids) FROM account WHERE id = ? )) )) ) ) "
				+ " AND pmo.parentmoduleids IS NULL  GROUP BY parentmoduleids ) a  GROUP BY a.systemsids ORDER BY maxsyscount DESC";
		return dao.find(sql,sysuserId);
	}
	

}
