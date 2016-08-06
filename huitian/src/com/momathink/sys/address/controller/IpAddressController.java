package com.momathink.sys.address.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.sys.address.model.IpAddress;
import com.momathink.sys.address.service.IpAddressService;
import com.momathink.teaching.campus.model.Campus;
@Controller(controllerKey="/address")
public class IpAddressController extends BaseController {
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(IpAddress.class);
	private IpAddressService ipAddressService = new IpAddressService();

	/**
	 * 查询所有的ip地址
	 * */
	public void getAllIpAdress() {
		try {
			Map<String,String> queryParam = splitPage.getQueryParam();
			if(!StringUtils.isEmpty(getPara("ipaddress"))){
				queryParam.put("name", getPara("ipaddress").toString());
			}
			splitPage.setOrderColunm("Id");
			splitPage.setOrderMode("desc");
			ipAddressService.list(splitPage);
			setAttr("showPages", splitPage.getPage());
			renderJsp("/ipaddress/ipaddress_list.jsp");
		} catch (Exception e) {
			logger.error("在IpAddressController的getAllIpAdress出现" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 添加ip地址
	 * */
	public void createIpAddress() {
		JSONObject json = new JSONObject();
		String code="0";
		String msg="添加失败！";
		try {
			String ipAddress = getPara("ip");
			int campuid=getParaToInt("campusid");
			IpAddress saveIpaddress = getModel(IpAddress.class);
			saveIpaddress.set("campus_id", campuid);
			saveIpaddress.set("name", ipAddress);
			saveIpaddress.set("create_time", new Date());
			if(saveIpaddress.save()){
				code="1";
				msg="添加成功！";
			}
		} catch (Exception e) {
			logger.equals("IpAddressController下的createIpAddress出现" + e);
			e.printStackTrace();
		}
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}

	/**
	 * 删除id地址
	 * */
	public void delIpAddress() {
		try {
			int ipaddressId = getParaToInt("ipaddressId");
			ipAddressService.deleteByRecordId(ipaddressId);
			getAllIpAdress();
		} catch (Exception e) {
			logger.equals("IpAddressController下的delIpAddress出现" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 修改IpAddress地址
	 * */
	public void updateIpAddress() {
		JSONObject json = new JSONObject();
		String code="0";
		String msg="修改失败！";
		try {
			int ipaddressId = getParaToInt("ipaddressId");
			String name = getPara("name");
			int campusid=getParaToInt("campusid");
			String.format("yyyy-mm-dd", new Date());
			IpAddress updateIpaddress = getModel(IpAddress.class);
			updateIpaddress.set("name", name);
			updateIpaddress.set("create_time", new Date());
			updateIpaddress.set("id", ipaddressId);
			updateIpaddress.set("campus_id", campusid);
			if(updateIpaddress.update()){
				code="1";
				msg="修改成功！";
			}
		} catch (Exception e) {
			logger.equals("IpAddressController下的updateAddress出现" + e);
			e.printStackTrace();
		}
		json.put("code", code);
		json.put("msg",msg);
		renderJson(json);
	}

	/**
	 * 通过id获取IdAddress地址
	 * 
	 * */
	public void getIpaddressById() {
		try {
			List<Campus> campus = Campus.dao.getCampus();
	        setAttr("campus", campus);
	        int ipaddressId=Integer.parseInt(getPara());
			String sql = "select * from ipaddress where Id =" + ipaddressId;
			Record ipaddress = Db.findFirst(sql);
			setAttr("ipaddress", ipaddress);
			renderJsp("/ipaddress/ipaddress_edit.jsp");
		} catch (Exception e) {
			logger.equals("IpAddressController下的getIpAddressById出现" + e);
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有校区
	 */
    public void getAllCampus(){
    	try {
        List<Campus> campus = Campus.dao.getCampus();
        setAttr("campus", campus);
    	renderJsp("/ipaddress/apaddress_add.jsp");
    	} catch (Exception e) {
			logger.equals("IpAddressController下的getAllCampus出现" + e);
			e.printStackTrace();
		}
    }
}
