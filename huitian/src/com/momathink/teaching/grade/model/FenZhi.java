package com.momathink.teaching.grade.model;

import java.util.List;

import com.momathink.common.annotation.model.Table;
import com.momathink.common.base.BaseModel;
@Table(tableName="fenzhi1")//关联到数据库的表
public class FenZhi extends BaseModel<FenZhi> {
	private static final long  serialVersionUID = 7996762001003047294L;
	//创建数据库表对象
	public static FenZhi dao=new FenZhi();
	/**
	 * 获取表所有的数据
	 */
	public List<FenZhi> findAllFenZhiTableOne(){
		return dao.find("select * from fenzhi1 where type = 0");
	}
	public List<FenZhi> findAllFenZhiTable(){
		return dao.find("select * from fenzhi1 where type = 1");
	}
	//根据类型和课程原值查找数据对象
		public FenZhi levelAndCourseValueFindToScorce(double courseYuanzhi,String level){
			return dao.findFirst("select * from fenzhi1 where yuanzhi = ? and type=?",courseYuanzhi,level );
		}
}
