package com.momathink.sys.operator.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

/** 功能 Url 管理  
 *  */
@Table(tableName="pt_operator")
public class Operator extends BaseModel<Operator> {

	private static final long serialVersionUID = -5723362110185587763L;
	public static final Operator dao =new Operator();
	
	/**
	 * 重写save方法
	 */
	public boolean save() {
		String formaturl = "qx_" + (this.getStr("url").replace("/", ""));
		this.set("formaturl", formaturl);
		return super.save();
	}
	
	/**
	 * 重写update方法
	 */
	public boolean update() {
		this.set("version", this.getLong("version")+1);
		return super.update();
	}
	
	/** 获取所有的 功能 */
	public List<Operator> queryAll() {
		return dao.find("SELECT * FROM pt_operator");
	}

	/** 根据 uri 查询  */
	public Operator queryByUrl(String url) {
		return dao.findFirst("SELECT * FROM pt_operator WHERE url = ?", url);
	}

	/** 根据  所属模块(菜单)id 获取下面所有 功能  */
	public List<Operator> queryByModuleids(String moduleids) {
		return dao.find("SELECT * FROM pt_operator WHERE moduleids = ?", moduleids);
	}

	/**
	 * 获取模块下的所有功能*
	 * @param systemsid 模块的id 
	 * @return
	 */
	public List<Operator> queryBySystemsid(Integer systemsid) {
		String sql=
				  " SELECT o.id "
				+ " FROM pt_operator o "
				+ " LEFT JOIN pt_module m  ON o.moduleids = m.id "
				+ " LEFT JOIN pt_module pm ON m.parentmoduleids = pm.id "
				+ " LEFT JOIN pt_systems s  ON pm.systemsids = s.id "
				+ " WHERE o.privilege = 1  "
				+ " AND s.id = ? ";
		return dao.find(sql, systemsid);
	}
	
	/**
	 * 根据路径找到模块*
	 * @param url
	 * @return
	 */
	public Operator queryByUrlEnhanced(String url) {
		char c = url.charAt(url.length()-1);
		if(url.indexOf("index")>0){
			url=url.substring(0,url.indexOf("index")-1);
		}else if(url.indexOf('?')>0){
			url=url.substring(0,url.indexOf('?'));
		}else if(c=='/'){
			url=url.substring(0,url.length()-1);
		}else if(url.substring(url.lastIndexOf('/')+1).matches("^[0-9]*$")){
			url = url.substring(0,url.lastIndexOf('/'));
		}
		return dao.findFirst("SELECT moduleids,privilege FROM pt_operator WHERE url= ?", url);
	}

}
