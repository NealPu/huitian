package com.momathink.teaching.reservation.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;


@Table(tableName = "reservation")
public class Reservation extends BaseModel<Reservation> {

	private static final long serialVersionUID = 8091569877737327778L;
	public static final Reservation dao = new Reservation();
	/**
	 * 获取预约详情
	 * @param id
	 * @return
	 */
	public Reservation findByIdMessage(String id) {
		String sql="SELECT r.id, s.REAL_NAME studentname,r.studentid,s.age, u.real_name username,"
								+ " s.state,r.state isstate,r.reservationtime ,hp.url,s.headpictureid "
								+ " FROM reservation r "
								+ " LEFT JOIN account s on r.studentid = s.Id "
								+ " left join headpicture hp on s.headpictureid = hp.id "
								+ " left join account u on r.teacherid =  u.id "
								+ " where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0 and r.id=?";
		return dao.findFirst(sql,id);
	}
	/**
	 * 获取教师的预约信息未答复的
	 * @param teacherid
	 * @return
	 */
	
	public List<Reservation> getMessageTeacherId(Integer teacherid) {
		String sql = "SELECT r.id,r.reservationtime,r.state,s.REAL_NAME studentname, u.real_name username,"
				+ " c.CAMPUS_NAME campusname,cr.NAME roomname ,tr.RANK_NAME rankname "
				+ " FROM reservation r "
				+ " LEFT JOIN account s ON r.studentid = s.Id "
				+ " LEFT JOIN campus  c  on r.campusid = c.Id "
				+ " LEFT JOIN classroom cr ON r.roomid = cr.Id "
				+ " LEFT JOIN time_rank tr on r.timerankid = tr.Id"
				+ " left join account u on r.sysuerid = u.id  "
				+ " where date_format(r.reservationtime,'%Y-%m-%d') >= (select current_date) and r.teacherid = ? ";
		return dao.find(sql,teacherid);
	}
	
}
