package com.momathink.teaching.teacher.model;
import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "announcement")
public class Announcement extends BaseModel<Announcement> {

	private static final long serialVersionUID = 8091569877737327778L;
	public static final Announcement dao = new Announcement();
	/**
	 * 获取该教师发送的消息 以及消息阅读情况*
	 * @param teacherI
	 * @return
	 */
	public List<Announcement> findSendMessage(Integer teacherId) {
		String  sql = "SELECT *  FROM announcement   where  teacherid = ? ";
		return dao.find(sql,teacherId);
	}
	/**
	 * 根据id获取消息的详情*
	 * @param id
	 * @return
	 */
	public Announcement findByIdForDetail(Integer id) {
		String sql  = "select a.*,s.real_name from announcement a "
				+ " left join account s on a.teacherid = s.id  where a.id = ?";
		return dao.findFirst(sql,id);
	}
	/**
	 * 获取登陆人的未读消息
	 * @param int1
	 * @return
	 */
	public Long getCountsUnreadMessage(Integer userid) {
		String sql = "SELECT  COUNT(1) num from announcementreceiver where state<>1 and recipientid= ?";
		Announcement ac = Announcement.dao.findFirst(sql,userid);
		return ac.getLong("num");
	}
	
}
