package com.momathink.crm.mediator.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName = "crm_organization")
public class Organization extends BaseModel<Organization> {

	private static final long serialVersionUID = 9173917692482770076L;
	public static final Organization dao = new Organization();
	public List<Organization> getAllOrganizationMessage() {
		String sql = "select * from crm_organization ";
		return dao.find(sql);
	}
	public Organization getOrganizationMessage() {
		String sql = "select * from crm_organization ";
		return dao.findFirst(sql);
	}
	
}
