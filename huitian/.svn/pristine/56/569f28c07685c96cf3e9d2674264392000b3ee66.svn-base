package com.momathink.teaching.reservation.model;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table(tableName = "testscorse")
public class TestScorse extends BaseModel<TestScorse> {

	private static final long serialVersionUID = 24099496026157413L;
	public static final TestScorse dao = new TestScorse();
	/**
	 * 根据预约id查找成绩
	 * @param para
	 * @return
	 */
	public TestScorse findByReservationId(String reservationid) {
		String  sql="select * from testscorse where reservationid = ? ";
		return dao.findFirst(sql,reservationid);
	}
	
}
