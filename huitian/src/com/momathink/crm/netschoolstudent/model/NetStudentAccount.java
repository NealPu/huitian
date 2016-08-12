package com.momathink.crm.netschoolstudent.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;

@Table( tableName="netstudentaccount" )
public class NetStudentAccount extends BaseModel<NetStudentAccount> {

	private static final long serialVersionUID = 6554421021854787745L;
	
	public static final NetStudentAccount dao = new NetStudentAccount();

	public List<NetStudentAccount> unpushedStudentInfo() {
		String selectSql = " select id jwStudentId , netstudentname studentName from netstudentaccount where accountstate = 0 and netstudentid is null ";
		return dao.find( selectSql );
	}

	public void updateNetAccountId( String jwStudentId , String netAccountId ) {
		String updateSql = " update netstudentaccount set netstudentid = ? where id = ?  ";
		Db.update( updateSql , netAccountId , jwStudentId );
		
	}

	public boolean queryAccountNameExist( String accountName ) {
		String countSql = " select count( 1 ) nameCounts from netstudentaccount where netstudentname = ? ";
		NetStudentAccount account = dao.findFirst( countSql , accountName );
		if( null != account && account.getLong( "nameCounts" ) >  0  ) {
			return true;
		}
		return false;
	}
	

}

