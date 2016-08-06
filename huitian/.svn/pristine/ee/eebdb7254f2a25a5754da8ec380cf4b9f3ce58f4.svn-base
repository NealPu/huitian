package com.momathink.crm.mediator.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.upload.UploadFile;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolDirFile;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.mediator.service.OrganizationService;
@Controller(controllerKey = "/organization")
public class OrganizationController extends BaseController {
	private OrganizationService organizationService = new OrganizationService();
	public void index(){
		List<Organization> list = Organization.dao.getAllOrganizationMessage();
		for(Organization o:list){
			List<String> nametels = new ArrayList<String>();
			if(o.get("sms_names")!=null&&o.get("sms_names")!=""){
				String[] smsname=o.getStr("sms_names").split(",");
				String[] smstel=o.getStr("sms_tels").split(",");
				for(int i=0;i<smsname.length;i++){
					nametels.add(smsname[i]+','+smstel[i]);
				}
			}
			Date expirationDate = o.getDate("expirationDate");
			o.put("closeCountdown", ToolDateTime.getDateDaySpace(expirationDate!=null?expirationDate:new Date(), ToolDateTime.getDate()));
			o.put("nametels",nametels);
		}
		setAttr("org",list);
		renderJsp("/mediator/organizationMessage.jsp");
	}
	/**
	 * 添加机构*
	 */
	public void addOrganization(){
		setAttr("code",0);
		renderJsp("/mediator/layer_organizationFrom.jsp");
	}
	/**
	 * 保存机构信息*
	 */
	public void saveOrgMessage(){
		try{
			Organization organization = getModel(Organization.class);
			organization.save();
		}catch(Exception e){
			e.printStackTrace();
		}
		renderJson("1");
	}
	/**
	 * 修改机构信息
	 * 
	 */
	public void updateOrganization(){
		String[]  num = getPara().split(",");
		Organization org = Organization.dao.findById(num[0]);
		List<String> nametels = new ArrayList<String>();
		if(org.get("sms_names")!=null&&org.get("sms_names")!=""){
			String[] smsname=org.getStr("sms_names").split(",");
			String[] smstel=org.getStr("sms_tels").split(",");
			for(int i=0;i<smsname.length;i++){
				nametels.add(smsname[i]+','+smstel[i]);
			}
			
		}
		org.put("nametels", nametels);
		setAttr("code",num[1]);
		setAttr("org",org);
		//renderJsp("/mediator/layer_organizationFrom.jsp");
		renderJsp("/mediator/organizationMessageUpdate.jsp");
	}
	/**
	 * 保存修改信息
	 */
	public void saveUpdateOrgMessage(){
		try{
			Organization organization = getModel(Organization.class);
			organizationService.update(organization);
		}catch(Exception e){
			e.printStackTrace();
		}
		renderJson("1");
	}
	
	/** /organization/saveUpdateImage
	 * 保存log
	 */
	public void saveUpdateImage(){
		UploadFile file = getFile("fileField");
		String type = getPara("type");
		String newPath = null;
		if ("login".equals(type)) {
			newPath = "/images/logo/logo_login.png";
		} else if ("menu".equals(type)) {
			newPath = "/images/logo/logo_menu.png";
		}
		String msg = ToolDirFile.replaceFile(file, "png", newPath);
		setAttr("msg", msg);
		if ("login".equals(type)) {
			renderJsp("/mediator/import_login_image.jsp");
		} else if ("menu".equals(type)) {
			renderJsp("/mediator/import_menu_image.jsp");
		}
	}
	
}
