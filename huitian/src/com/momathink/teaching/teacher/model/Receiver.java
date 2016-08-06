package com.momathink.teaching.teacher.model;
import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "announcementreceiver")
public class Receiver extends BaseModel<Receiver> {

	private static final long serialVersionUID = 8091569877737327778L;
	public static final Receiver dao = new Receiver();
	/**
	 * 获取公告信息
	 * @param teacherId
	 * @return
	 */
	public List<Receiver> findSendMessage(String teacherId) {
		String sql ="select a.*,t.real_name,ar.state,ar.id arid "
				+ " from announcementreceiver ar "
				+ " left join announcement a on ar.senderid = a.id "
				+ " left join account t on a.teacherid = t.id "
				+ " where  ar.recipientid = ? order by ar.state " ;
		return dao.find(sql,teacherId);
	}
	/**
	 * 获取接收人信息*
	 * @param id
	 * @return
	 */
	public List<Receiver> getUserState(Integer id) {
		String sql ="select t.real_name,ar.state,ar.reply "
				+ " from announcementreceiver ar "
				+ " left join account t on ar.recipientid = t.id "
				+ " where ar.senderid =?   " ;
		return dao.find(sql,id);
	}
	/**
	 * 获取接受人所有公告的信息
	 * @param sysuserId
	 * @return
	 */
	public List<Receiver> findByUserid(Integer sysuserId) {
		String  sql ="select * from announcementreceiver where recipientid=?";
		return dao.find(sql,sysuserId);
	}
	/**
	 * 获取公告的接收人ID
	 * @param id
	 * @return
	 */
	public List<Receiver> fingSendMessage(String id) {
		String sql ="select * from announcementreceiver where senderid = ?";
		return dao.find(sql,id);
	}
	
}
