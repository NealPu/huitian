package com.momathink.teaching.campus.model;

import java.util.Date;
import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
import com.momathink.common.tools.ToolString;

@Table(tableName="classroom")
public class Classroom extends BaseModel<Classroom> {
	private static final long serialVersionUID = 1L;
	public static final Classroom dao = new Classroom();
	/**
	 * 查询该校区所有的教室分页
	 */
	public List<Classroom> getClassroom(int Campus_ID,String classAddr,String page) {
		// 要查询第几页
		int pagecount = (Integer.parseInt(page)-1) * 20;
		//分页记录
		String sql = "SELECT class.* FROM classroom class where class.campus_id=?";
		if (!ToolString.isNull(classAddr)) {
			classAddr = classAddr.trim();
			sql += " and (class.Name like '%" + classAddr + "%')";
		}
		sql += " order by class.Id asc LIMIT " + pagecount + ",20;";
		List<Classroom> list = Classroom.dao.find(sql,Campus_ID);
		return list;
	}
	/**
	 * 查询该校区所有的教室总记录数
	 */
	public String getClassroomNum(int Campus_ID,String classAddr) {
		//一共有多少条记录
		String sqlcount = "SELECT count(1) as counts FROM classroom class where state=0 and class.campus_id=?";
		if (!ToolString.isNull(classAddr)) {
			classAddr = classAddr.trim();
			sqlcount += " and (class.Name like '%" + classAddr+ "%')";
		}
		String classNum = Classroom.dao.find(sqlcount,Campus_ID).get(0).get("counts").toString();
		return classNum;
	}
	/**
	 * 添加教室
	 */
	public void addClassroom(String classAddress,int Campus_ID,int maxpeople) {
		new Classroom().set("maxpeople", maxpeople).set("address", classAddress).set("name", classAddress).set("Campus_ID", Campus_ID).set("create_time", new Date()).save();
	}
	/**
	 * 更新教室
	 */
	public void updateClassroom(int id,String classAddress) {
		Classroom.dao.findById(id).set("address", classAddress).set("name", classAddress).update();
	}
	public  List<Classroom> getClassRoomByCamp(Integer campusId) {
		// TODO Auto-generated method stub
		String sql = "select * from classroom where campus_id = ?";
		List<Classroom> cr = Classroom.dao.find(sql, campusId);
		return cr;
	}

	public List<Classroom> getAllRooms() {
		return dao.find("select * from classroom where state=0");
	}
	
	public String getRoomNameById(String roomId){
		return dao.findById(Integer.parseInt(roomId)).getStr("name");
	}
	
	/**
	 * 取出校区的所有正常的教室
	 * @param campusid
	 * @return
	 */
	public List<Classroom> getClassRoombyCampusid(String campusid){
		String sql = " select * from classroom where campus_id = ? and state=0 ";
		return dao.find(sql, campusid);
	}
}
