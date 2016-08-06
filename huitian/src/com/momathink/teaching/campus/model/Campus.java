package com.momathink.teaching.campus.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName="campus")
public class Campus extends BaseModel<Campus> {
	private static final long serialVersionUID = 1L;
	public static final Campus dao = new Campus();
	/**
	 * 查询所有的校区
	 */
	public List<Campus> getCampus() {
		List<Campus> list = Campus.dao.find("SELECT CONCAT('|',c.id,'|') ids ,c.* from campus c  where state=0");
		return list;
	}
	
	public List<Campus> getCampusMassage(){
		String sql = "select campus.*,sysuser.REAL_NAME,sysuser.tel fzrtel,sysuser.email fzremail from campus left join account as sysuser on campus.presidentid=sysuser.id ";
		List<Campus> list = Campus.dao.find(sql);
		return list;
	}

	public Campus showCampusMassage(String campusId) {
		String sql = "select campus.*,sysuser.REAL_NAME FROM campus left join account as sysuser on campus.presidentid=sysuser.id where campus.id= ? ";
		Campus campus = Campus.dao.findFirst(sql, campusId);
		return campus;
	}
	
	public String getCampusNameById(String campusId){
		Campus campus = dao.findById(Integer.parseInt(campusId));
		return campus.getStr("campus_name");
	}
	
	public List<Campus> getCanUseCampusInfo(){
		String sql = "select campus.id,campus.campus_name,sysuser.REAL_NAME fzrname,sysuser.tel fzrtel,sysuser.email fzremail from campus left join account as sysuser on campus.kcuserid=sysuser.id where campus.state=0";
		List<Campus> list = Campus.dao.find(sql);
		return list;
	}
	public Campus getCampusInfo(String campusId){
		String sql = "select c.*,"
				+ "f.id fzrid,f.REAL_NAME fzrname,f.tel fzrtel,f.email fzremail, "
				+ "j.id jwfzrid,j.REAL_NAME jwfzrname,j.tel jwfzrtel,j.email jwfzremail, "
				+ "k.id kcfzrid,k.REAL_NAME kcfzrname,k.tel kcfzrtel,k.email kcfzremail, "
				+ "s.id scfzrid,s.REAL_NAME scfzrname,s.tel scfzrtel,s.email scfzremail "
				+ "from campus c "
				+ "left join account as f on c.presidentid=f.id "
				+ "left join account as j on c.jwuserid=j.id "
				+ "left join account as k on c.kcuserid=k.id "
				+ "left join account as s on c.scuserid=s.id "
				+ "where c.state=0 and c.id=?";
		Campus campus = Campus.dao.findFirst(sql,Integer.parseInt(campusId));
		return campus;
	}
	public Campus getCampusInfoStateNotZero(String campusId){
		String sql = "select c.*,"
				+ "f.id fzrid,f.REAL_NAME fzrname,f.tel fzrtel,f.email fzremail, "
				+ "j.id jwfzrid,j.REAL_NAME jwfzrname,j.tel jwfzrtel,j.email jwfzremail, "
				+ "k.id kcfzrid,k.REAL_NAME kcfzrname,k.tel kcfzrtel,k.email kcfzremail, "
				+ "s.id scfzrid,s.REAL_NAME scfzrname,s.tel scfzrtel,s.email scfzremail "
				+ "from campus c "
				+ "left join account as f on c.presidentid=f.id "
				+ "left join account as j on c.jwuserid=j.id "
				+ "left join account as k on c.kcuserid=k.id "
				+ "left join account as s on c.scuserid=s.id "
				+ "where c.id=?";
		Campus campus = Campus.dao.findFirst(sql,Integer.parseInt(campusId));
		return campus;
	}

	/**
	 * 查询用户是否是课程顾问负责人
	 * @param sysuserId
	 * @return
	 */
	public String IsCampusKcFzr(Integer sysuserId) {
		List<Campus> campusList = dao.find("select * from campus where kcuserid=?",sysuserId);
		if(campusList!=null && campusList.size()>0){
			StringBuffer campusids = new StringBuffer();
			for(Campus c : campusList){
				campusids.append(c.getPrimaryKeyValue()).append(",");
			}
			return campusids.substring(0, campusids.length()-1);
		}else{
			return null;
		}
	}
	/**
	 * 是否是市场负责人
	 * @param sysuserId
	 * @return
	 */
	public String IsCampusScFzr(Integer sysuserId) {
		List<Campus> campusList = dao.find("select * from campus where scuserid=?",sysuserId);
		if(campusList!=null && campusList.size()>0){
			StringBuffer campusids = new StringBuffer();
			for(Campus c : campusList){
				campusids.append(c.getPrimaryKeyValue()).append(",");
			}
			return campusids.substring(0, campusids.length()-1);
		}else{
			return null;
		}
	}
	/**
	 * 是否是教务负责人
	 * @param sysuserId
	 * @return
	 */
	public String IsCampusJwFzr(Integer sysuserId) {
		List<Campus> campusList = dao.find("select * from campus where jwuserid=?",sysuserId);
		if(campusList!=null && campusList.size()>0){
			StringBuffer campusids = new StringBuffer();
			for(Campus c : campusList){
				campusids.append(c.getPrimaryKeyValue()).append(",");
			}
			return campusids.substring(0, campusids.length()-1);
		}else{
			return null;
		}
	}
	/**
	 * 是否是校区负责人
	 * @param sysuserId
	 * @return
	 */
	public String IsCampusFzr(Integer sysuserId) {
		List<Campus> campusList = dao.find("select * from campus where presidentid=?",sysuserId);
		if(campusList!=null && campusList.size()>0){
			StringBuffer campusids = new StringBuffer();
			for(Campus c : campusList){
				campusids.append(c.getPrimaryKeyValue()).append(",");
			}
			return campusids.substring(0, campusids.length()-1);
		}else{
			return null;
		}
	}

	public List<Campus> getCampusByName(String campusname) {
		try{
			List<Campus> campus = dao.find("select * from campus where CAMPUS_NAME like '%"+campusname+"%'");
			return campus;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public List<Campus> getCampusIds(String campuskcids) {
		List<Campus> list = dao.find("select * from campus where Id in ( ? )",campuskcids);
		return list;
	}

	/**
	 * 随机获取老师所属校区中的一个
	 * @param teacherId
	 * @return
	 */
	public Campus getRoundCampusId(String teacherId) {
		String sql="SELECT b.campus_id FROM ( SELECT * FROM account_campus WHERE account_campus.account_id= ? ) b "
				+ " WHERE b.campus_id >= (SELECT floor(RAND() * (SELECT MAX(campus_id) FROM account_campus WHERE account_id= ? ))) "
				+ " ORDER BY campus_id LIMIT 1 ";
		if(!StringUtils.isEmpty(teacherId)){
			Campus campus = dao.findFirst(sql, teacherId,teacherId);
			return campus;
		}else{
			return null;
		}
	}

	public List<Campus> getCampusIdsIn(String campusIdsByAccountId) {
		List<Campus> list = dao.find("select * from campus where Id in ("+campusIdsByAccountId+")");
		return list;
	}

	/**
	 * 获取当前登录用户所属校区 
	 * @author David
	 * @param loginUserId
	 * @return
	 */
	public List<Campus> getCampusByLoginUser(Integer loginUserId) {
		List<Campus> campusList = Campus.dao.find("select xq.* from campus xq left join account_campus ac on xq.id = ac.campus_id where ac.account_id=?",loginUserId);
		return campusList;
	}

}
