package com.momathink.crm.mediator.model;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

@Table(tableName = "crm_company")
public class Company extends BaseModel<Company> {

	private static final long serialVersionUID = 9173917692482770076L;
	public static final Company dao = new Company();
	
	public Long queryCompanyCount(String field, String value, String companyId,String userid) {
		if (!ToolString.isNull(field) && !ToolString.isNull(value)) {
			StringBuffer sql = new StringBuffer("select count(1) from crm_company where ");
			sql.append(field).append("='").append(value).append("'");
			if (!ToolString.isNull(companyId)) {
				sql.append(" and id != ").append(companyId);
			}
			if(!StringUtils.isEmpty(userid)){
				SysUser user = SysUser.dao.findById(userid);
				if(Role.isShichang(user.getStr("roleids"))){
					sql.append(" and createuserid = ").append(userid);
				}
			}
			return Db.queryLong(sql.toString());
		} else {
			return null;
		}
	}
	/**
	 * 模糊查询*
	 * @param companyname
	 * @return
	 */
	public List<Company> findByName(String companyname) {
		String sql = "select * from crm_company where companyname like \"%" + companyname + "%\" ";
		return dao.find(sql);
	}
	/**
	 * 根据机构名字查询机构*
	 * @param companyname
	 * @return
	 */
	public Company findByNames(String companyname) {
		String sql = "select * from crm_company where companyname = '"+companyname+"'";
		return dao.findFirst(sql);
	}
	public Long getUseCompanyMediators(Integer companyId) {
		if(companyId==null){
			return 0l;
		}else{
			Long count = Db.queryLong("SELECT COUNT(1) FROM crm_mediator WHERE companyid=?",companyId);
			return count;
		}
	}
	
	/**
	 * 获取机构
	 * @param userid
	 * @return
	 */
	public List<Company> getAllCompanys(String userid) {
		StringBuffer sql = new StringBuffer("select * from crm_company where 1=1 ");
		if(!StringUtils.isEmpty(userid)){
			sql.append(" and createuserid = ").append(userid);
		}
		return dao.find(sql.toString());
	}

}
