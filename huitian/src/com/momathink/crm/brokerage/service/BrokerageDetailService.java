package com.momathink.crm.brokerage.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.brokerage.model.BrokerageDetail;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.finance.service.PaymentService;

public class BrokerageDetailService extends BaseService {

	private static Logger log = Logger.getLogger(BrokerageDetailService.class);
	private PaymentService paymentService = new PaymentService();
	public void list(SplitPage splitPage) {
		log.info("学生消耗返佣明细：分页处理");
		String select = " select * ";
		splitPageBase(splitPage, select);
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from mk_brokerage_detail where brokerageids is null ");

		if (null == queryParam) {
			return;
		}

		String mediatorname = queryParam.get("mediatorname");// 用户真实姓名

		if (null != mediatorname && !mediatorname.equals("")) {
			formSqlSb.append(" and mediatorname like ? ");
			paramValue.add("%" + mediatorname.trim() + "%");
		}
	}
	@Before(Tx.class)
	public void processCoursePlan(){
		log.info("定时任务：分页处理");
		String date = ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd);
		String sql = "SELECT c.*,mks.mediatorid,mks.ids studentid,sb.SUBJECT_NAME subjectname FROM\n" +
				"(SELECT cp.Id courseplanid,s.ids accountids,cp.class_id classtype,s.REAL_NAME studentname,co.COURSE_NAME coursename,co.SUBJECT_ID,DATE_FORMAT(cp.COURSE_TIME,'%Y-%m-%d') coursetime\n" +
				"FROM courseplan cp LEFT JOIN account s ON cp.STUDENT_ID=s.Id\n" +
				"LEFT JOIN course co ON cp.COURSE_ID=co.Id\n" +
				"LEFT JOIN subject sb ON cp.SUBJECT_ID=sb.Id\n" +
				"WHERE s.ids IS NOT NULL AND cp.STATE!=2 AND cp.task_run=0 AND DATE_FORMAT(cp.COURSE_TIME,'%Y-%m-%d')<='"+ToolDateTime.getSpecifiedDayBefore(date)+"' "+ 
				"ORDER BY cp.STUDENT_ID,cp.COURSE_TIME) c\n" +
				"LEFT JOIN mk_student mks ON c.accountids=mks.accountids\n"+
				"LEFT JOIN `subject` sb ON c.SUBJECT_ID=sb.Id";
		List<Record> list = Db.find(sql);
		log.info("需要返佣课程数："+list.size());
		if(list!=null && list.size()>0){
			int i=1;
			Map<String ,Mediator> counselorMap = Mediator.dao.findAllMediator();
			for(Record r : list){
				BrokerageDetail bd = new BrokerageDetail();
				String mediatorid = r.getStr("mediatorid");
				Mediator counselor = counselorMap.get(mediatorid);
				String parentidsLV1 = counselor.getInt("parentid").toString();
				Map<String,String> paymentMap = paymentService.queryPrice(r.getInt("studentid").toString(),r.getInt("classtype").toString());
				if(paymentMap == null || paymentMap.size()==0){
					continue;
				}
				long price = Long.parseLong(paymentMap.get("price"));
				String ispay = paymentMap.get("ispay");
				bd.set("studentid", r.getInt("studentid"));
				bd.set("studentname", r.getStr("studentname"));
				bd.set("coursename", r.getStr("coursename"));
				bd.set("mediatorid", mediatorid);
				bd.set("accountids", r.getStr("accountids"));
				bd.set("courseplanid", r.getInt("courseplanid"));
				bd.set("classtype", r.getInt("classtype")==0?0:1);
				bd.set("coursetime", r.getStr("coursetime"));
				bd.set("mediatorname", counselor.getStr("realname"));
				bd.set("subjectname", r.getStr("subjectname"));
				bd.set("brokeragetype", "1");
				bd.set("commission", price*0.25);
				bd.set("price", price);
				bd.set("ratio", 0.25);
				bd.set("createtime", new Date());
				bd.set("ispay", ispay);
				bd.set("paymentid", paymentMap.get("paymentid"));
				this.save(bd);
				log.info("第"+i+"个学生："+r.getStr("studentname")+",所属顾问："+counselor.getStr("realname")+",科目："+r.getStr("subjectname")+"，课程单价："+price+",返佣类型：推荐，返佣比率："+0.25);
				if(!StringUtils.isEmpty(parentidsLV1)){//LV1
					bd.set("brokeragetype", "2");
					Mediator counselorLV1 = counselorMap.get(parentidsLV1);
					String parentidsLV2 = counselorLV1.getInt("parentid").toString();
					bd.set("mediatorid", parentidsLV1);
					bd.set("mediatorname", counselorLV1.getStr("realname"));
					bd.set("lvtype", "1");
					bd.set("commission", price*0.05);
					bd.set("ratio", 0.05);
					this.save(bd);
					log.info("第"+i+"个学生："+r.getStr("studentname")+",所属顾问："+counselor.getStr("realname")+",科目："+r.getStr("subjectname")+"，课程单价："+price+",返佣类型：推广L1，返佣比率："+0.05);
					if(!StringUtils.isEmpty(parentidsLV2)){//LV2
						Mediator counselorLV2 = counselorMap.get(parentidsLV2);
						String parentidsLV3 = counselorLV2.getInt("parentid").toString();
						bd.set("mediatorid", parentidsLV2);
						bd.set("mediatorname", counselorLV2.getStr("realname"));
						bd.set("lvtype", "2");
						bd.set("commission", price*0.02);
						bd.set("ratio", 0.02);
						this.save(bd);
						log.info("第"+i+"个学生："+r.getStr("studentname")+",所属顾问："+counselor.getStr("realname")+",科目："+r.getStr("subjectname")+"，课程单价："+price+",返佣类型：推广L2，返佣比率："+0.02);
						if(!StringUtils.isEmpty(parentidsLV3)){//LV3
							Mediator counselorLV3 = counselorMap.get(parentidsLV3);
							String parentidsLV4 = counselorLV3.getInt("parentid").toString();
							bd.set("mediatorid", parentidsLV3);
							bd.set("mediatorname", counselorLV3.getStr("realname"));
							bd.set("lvtype", "3");
							bd.set("commission", price*0.01);
							bd.set("ratio", 0.01);
							this.save(bd);
							log.info("第"+i+"个学生："+r.getStr("studentname")+",所属顾问："+counselor.getStr("realname")+",科目："+r.getStr("subjectname")+"，课程单价："+price+",返佣类型：推广L3，返佣比率："+0.01);
							if(!StringUtils.isEmpty(parentidsLV4)){//LV4
								Mediator counselorLV4 = counselorMap.get(parentidsLV4);
								String parentidsLV5 = counselorLV4.getInt("parentid").toString();
								bd.set("mediatorid", parentidsLV4);
								bd.set("mediatorname", counselorLV4.getStr("realname"));
								bd.set("lvtype", "4");
								bd.set("commission", price*0.005);
								bd.set("ratio", 0.005);
								this.save(bd);
								log.info("第"+i+"个学生："+r.getStr("studentname")+",所属顾问："+counselor.getStr("realname")+",科目："+r.getStr("subjectname")+"，课程单价："+price+",返佣类型：推广L4，返佣比率："+0.005);
								if(!StringUtils.isEmpty(parentidsLV5)){//LV5
									Mediator counselorLV5 = counselorMap.get(parentidsLV5);
									bd.set("mediatorid", parentidsLV5);
									bd.set("mediatorname", counselorLV5.getStr("realname"));
									bd.set("lvtype", "5");
									bd.set("commission", price*0.0025);
									bd.set("ratio", 0.0025);
									this.save(bd);
									log.info("第"+i+"个学生："+r.getStr("studentname")+",所属顾问："+counselor.getStr("realname")+",科目："+r.getStr("subjectname")+"，课程单价："+price+",返佣类型：推广L5，返佣比率："+0.025);
								}
							}
						}	
					}	
				}
				String updateSql = "update courseplan cp set cp.task_run=1 where cp.id="+r.getInt("courseplanid");
				Db.update(updateSql);
			}
			i++;
		}
	}

	private void save(BrokerageDetail brokerageDetail) {
		BrokerageDetail bd= brokerageDetail;
		bd.save();
	}

	public List<BrokerageDetail> queryDetailWeijie(String mediatorid, String beginDate, String endDate, String brokerageType) {
		StringBuffer sql = new StringBuffer("SELECT * FROM mk_brokerage_detail mkbd WHERE mkbd.brokerageids IS NULL ");
		if(!StringUtils.isEmpty(mediatorid))
			sql.append(" and mkbd.mediatorid = '"+mediatorid+"'");
		if(!StringUtils.isEmpty(beginDate))
			sql.append(" and mkbd.coursetime >= '"+beginDate+"'");
		if(!StringUtils.isEmpty(endDate))
			sql.append(" and mkbd.coursetime <= '"+endDate+"'");
		if(!StringUtils.isEmpty(brokerageType))
			sql.append(" AND mkbd.brokeragetype = '"+brokerageType+"'");
		sql.append(" ORDER BY mkbd.ispay ,mkbd.studentname, mkbd.coursetime");
		List<BrokerageDetail> list = BrokerageDetail.dao.find(sql.toString());
		return list;
	}

	public void updateBrokerageDetail(String mediatorid, String beginDate, String endDate, String brokerageType, String brokerageids) {
		StringBuffer sql = new StringBuffer("UPDATE mk_brokerage_detail mkbd SET mkbd.brokerageids = ? WHERE mkbd.brokerageids IS NULL and mkbd.ispay='1' ");
		if(!StringUtils.isEmpty(mediatorid))
			sql.append(" and mkbd.mediatorid = '"+mediatorid+"'");
		if(!StringUtils.isEmpty(beginDate))
			sql.append(" and mkbd.coursetime >= '"+beginDate+"'");
		if(!StringUtils.isEmpty(endDate))
			sql.append(" and mkbd.coursetime <= '"+endDate+"'");
		if(!StringUtils.isEmpty(brokerageType))
			sql.append(" AND mkbd.brokeragetype = '"+brokerageType+"'");
		Db.update(sql.toString(), brokerageids);
	}

	public void updateBrokerageIspay(String paymentIds) {
		StringBuffer sql = new StringBuffer("UPDATE mk_brokerage_detail mkbd SET mkbd.ispay = 1 WHERE mkbd.paymentids = ? ");
		Db.update(sql.toString(), paymentIds);
	}
}
