package com.momathink.crm.brokerage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.tools.ToolArith;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.crm.brokerage.model.Brokerage;
import com.momathink.crm.brokerage.model.BrokerageDetail;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.sys.account.model.AccountBook;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.classtype.model.ClassOrder;
import com.momathink.teaching.course.model.Course;

public class BrokerageService extends BaseService {

	private static Logger log = Logger.getLogger(BrokerageService.class);
	private BrokerageDetailService brokerageDetailService = new BrokerageDetailService();
	


	@SuppressWarnings("unchecked")
	public void list(SplitPage splitPage) {
		log.debug("佣金结算：分页处理");
		String select = " select * ";
		splitPageBase(splitPage, select);
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> list = page.getList();
		for (Record r : list) {
			r.set("ratios", ToolArith.mul(r.getBigDecimal("ratio").doubleValue(), 100));
		}
	}

	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from crm_brokerage b where 1=1");
		if (null == queryParam) {
			return;
		}

		String mediatorId = queryParam.get("mediatorId");
		String beginDate = queryParam.get("beginMonth");
		String endDate = queryParam.get("endMonth");
		String isPay = queryParam.get("isPay");

		if (!StringUtils.isEmpty(mediatorId)) {
			formSqlSb.append(" AND b.mediatorid= ? ");
			paramValue.add(Integer.parseInt(mediatorId));
		}
		if (null != isPay && !isPay.equals("")) {
			formSqlSb.append(" AND b.ispay= ? ");
			paramValue.add(isPay);
		}
		if (null != beginDate && !beginDate.equals("")) {
			formSqlSb.append(" AND b.clearingmonth>= ? ");
			paramValue.add(beginDate);
		}
		if (null != endDate && !endDate.equals("")) {
			formSqlSb.append(" AND b.clearingmonth<= ? ");
			paramValue.add(endDate);
		}
		formSqlSb.append(" order by b.createdate desc");
	}
	
	/**
	 * 推荐佣金结算
	 * @param opportunityid
	 * @return
	 */
	public Mediator tuijianSettlement(String opportunityid) {
		if (StringUtils.isEmpty(opportunityid)) {
			return null;
		} else {
			Mediator mediator = Mediator.dao.findById(opportunityid);
			if (mediator == null) {
				return null;
			} else {
				List<Opportunity> chengdanList = Opportunity.dao.findIsConverById(opportunityid, "1");
				mediator.put("chengdanList", chengdanList);
				return mediator;
			}
		}
	}

	public List<Mediator> tuijianBrokerage(String counselorName, String beginDate, String endDate, String brokerageType) {
		Map<String, Mediator> counselorMap = Mediator.dao.findAllMediator();
		List<Mediator> clist = new ArrayList<Mediator>();
		List<Record> list = this.brokerageStat(null, counselorName, beginDate, endDate, brokerageType, null);
		for (Record r : list) {
			boolean nohas = true;
			String cids = r.getStr("mediatorid");
			String ispay = r.getStr("ispay");
			Mediator c = counselorMap.get(cids);
			for (Mediator mediator : clist) {
				String mediatorid = mediator.getPrimaryKeyValue().toString();
				if (cids.equals(mediatorid)) {
					if ("1".equals(ispay)) {
						mediator.put("wjkc", r.getLong("wjkc"));// 可结算课程
						mediator.put("yj", r.getBigDecimal("yj"));
					} else {
						mediator.put("bkjskc", r.getLong("wjkc"));// 不可结算课程
						mediator.put("bkjsyj", r.getBigDecimal("yj"));
					}
					Date ksr = mediator.getDate("kaishiri");
					Date jzr = mediator.getDate("jiezhiri");
					Date _ksr = r.getDate("kaishiri");
					Date _jzr = r.getDate("jiezhiri");
					int ksrdays = ToolDateTime.getDateDaySpace(ksr, _ksr);
					int jzrdays = ToolDateTime.getDateDaySpace(jzr, _jzr);
					if (ksrdays > 0) {
						mediator.put("kaishiri", _ksr);
					}
					if (jzrdays < 0) {
						mediator.put("jiezhiri", _jzr);
					}
					nohas = false;
					break;
				} else {
					continue;
				}
			}
			if (nohas) {
				if ("1".equals(ispay)) {
					c.put("wjkc", r.getLong("wjkc"));// 可结算课程
					c.put("yj", r.getBigDecimal("yj"));
					c.put("bkjskc", "0");// 不可结算课程
					c.put("bkjsyj", "0");
				} else {
					c.put("wjkc", "0");// 可结算课程
					c.put("yj", "0");
					c.put("bkjskc", r.getLong("wjkc"));// 不可结算课程
					c.put("bkjsyj", r.getBigDecimal("yj"));
				}
				c.put("brokerageType", brokerageType);
				c.put("kaishiri", r.getDate("kaishiri"));
				c.put("jiezhiri", r.getDate("jiezhiri"));
				clist.add(c);
			}
		}
		return clist;
	}

	private List<Record> brokerageStat(String mediatorid, String counselorName, String beginDate, String endDate, String brokerageType, String ispay) {
		StringBuffer sql = new StringBuffer(
				"SELECT mkbd.mediatorid,mkbd.ispay,COUNT(1) wjkc,SUM(mkbd.commission) yj,MIN(mkbd.coursetime) kaishiri,MAX(mkbd.coursetime) jiezhiri FROM mk_brokerage_detail mkbd WHERE mkbd.brokerageid IS NULL ");
		if (!StringUtils.isEmpty(mediatorid))
			sql.append(" AND mkbd.mediatorid = '" + mediatorid + "'");
		if (!StringUtils.isEmpty(counselorName))
			sql.append(" and mkbd.counselorname like '%" + counselorName + "%'");
		if (!StringUtils.isEmpty(beginDate))
			sql.append(" and mkbd.coursetime >= '" + beginDate + "'");
		if (!StringUtils.isEmpty(endDate))
			sql.append(" and mkbd.coursetime <= '" + endDate + "'");
		if (!StringUtils.isEmpty(brokerageType))
			sql.append(" AND mkbd.brokeragetype = '" + brokerageType + "'");
		if (!StringUtils.isEmpty(ispay))
			sql.append(" AND mkbd.ispay = '" + ispay + "'");
		sql.append(" GROUP BY mkbd.mediatorid,mkbd.ispay");
		List<Record> list = Db.find(sql.toString());
		return list;
	}

	public Mediator tuijianBrokerageDetail(String mediatorid, String beginDate, String endDate, String brokerageType) {
		if (StringUtils.isEmpty(mediatorid)) {
			return null;
		} else {
			Mediator c = Mediator.dao.findById(mediatorid);
			List<BrokerageDetail> bdlist = brokerageDetailService.queryDetailWeijie(mediatorid, beginDate, endDate, brokerageType);
			List<Record> list = this.brokerageStat(c.getPrimaryKeyValue().toString(), null, beginDate, endDate, brokerageType, null);
			for (Record r : list) {
				c.put("wjkc", r.getLong("wjkc"));
				c.put("kaishiri", r.getDate("kaishiri"));
				c.put("jiezhiri", r.getDate("jiezhiri"));
				c.put("yj", r.getBigDecimal("yj"));
				c.put("brokerageType", brokerageType);
			}
			c.put("stat", list);
			c.put("brokerageDetail", bdlist);
			return c;
		}
	}

	public Mediator saveBrokerage(String mediatorid, String beginDate, String endDate, String brokerageType, SysUser user) {
		if (StringUtils.isEmpty(mediatorid)) {
			return null;
		} else {
			Mediator c = Mediator.dao.findById(mediatorid);
			Brokerage brokerage = new Brokerage();
			List<Record> list = this.brokerageStat(c.getPrimaryKeyValue().toString(), null, beginDate, endDate, brokerageType, "1");
			for (Record r : list) {
				brokerage.set("coursetotal", r.getLong("wjkc"));
				brokerage.set("begindate", r.getDate("kaishiri"));
				brokerage.set("enddate", r.getDate("jiezhiri"));
				brokerage.set("moneysum", r.getBigDecimal("yj"));
				brokerage.set("mediatorid", mediatorid);
				brokerage.set("mediatorname", c.getStr("realname"));
				brokerage.set("createdate", new Date());
				brokerage.set("brokerageType", brokerageType);
				brokerage.set("userid", user.getPrimaryKeyValue());
				brokerage.set("username", user.getStr("real_name"));
				brokerage.set("ispay", "0");
				brokerage.save();
				String brokerageids = brokerage.getPrimaryKeyValue().toString();
				brokerageDetailService.updateBrokerageDetail(mediatorid, beginDate, endDate, brokerageType, brokerageids);
			}
			List<Brokerage> blist = Brokerage.dao.findByMediatorId(mediatorid, "");
			c.put("brokerageList", blist);
			return c;
		}
	}

	public void confirmPayByIds(String id, SysUser user) {
		Brokerage b = Brokerage.dao.findById(Integer.parseInt(id));
		if (b != null) {
			Mediator mediator = Mediator.dao.findById(b.getStr("mediatorid"));
			b.set("ispay", "1");
			b.set("payuserid", user.getInt("id"));
			b.set("payusername", user.getStr("real_name"));
			b.set("paytime", new Date());
			b.update();
			if (!StringUtils.isEmpty(mediator.getStr("phonenumber"))) {
				MessageService.sendMessageToMediator(MesContantsFinal.cs_sms_jiesuan, MesContantsFinal.cs_email_jiesuan, null, null,null,null);
			}
		}
	}

	public Mediator queryBrokerageDetail(String mediatorid, String brokerageids) {
		if (StringUtils.isEmpty(mediatorid)) {
			return null;
		} else {
			Mediator c = Mediator.dao.findById(mediatorid);
			List<BrokerageDetail> bdlsit = BrokerageDetail.dao.findByBrokerageIds(brokerageids);
			Brokerage brokerage = Brokerage.dao.findById(brokerageids);
			c.put("brokerage", brokerage);
			c.put("brokerageDetail", bdlsit);
			return c;
		}
	}

	public List<Brokerage> queryBrokerage(Mediator mediator, String brokerageType) {
		if (mediator == null) {
			return null;
		} else {
			List<Brokerage> list = Brokerage.dao.findByMediatorId(mediator.getPrimaryKeyValue().toString(), brokerageType);
			return list;
		}
	}

	public List<Mediator> tuijianStat(String mediatorId) {
		List<Mediator> vipList = Mediator.dao.statVip(mediatorId);
		List<Mediator> banList = Mediator.dao.statBan(mediatorId);
		List<Mediator> list = new ArrayList<Mediator>();
		Organization org = Organization.dao.findById(1);
		for(Mediator vm: vipList){
			
			Double ratio = vm.getBigDecimal("ratio")==null?Double.parseDouble(org.get("basic_defaultpromo").toString()):vm.getBigDecimal("ratio").doubleValue();
			vm.set("ratio", ToolArith.mul(ratio, 100));
			boolean flag = true;
			String vyf = vm.getStr("statmonth");//月份
			String vmid = vm.getInt("mediatorid")+"";
			for(Mediator bm:banList){
				String byf = bm.getStr("statmonth");
				String bmid = vm.getInt("mediatorid")+"";
				if(vyf.equals(byf)&&vmid.equals(bmid)){
					double banxh = bm.getBigDecimal("bansxh")==null?0:bm.getBigDecimal("bansxh").doubleValue();
					vm.put("bansxh", bm.getBigDecimal("bansxh"));
					vm.put("banxxh", bm.getBigDecimal("banxxh"));
					vm.put("banyj", ToolArith.mul(banxh, ratio));
					vm.put("banks", bm.getBigDecimal("banks"));
					flag = false;
					break;
				}else{
					continue;
				}
			}
			if(flag){
				vm.put("bansxh", 0);
				vm.put("banxxh", 0);
				vm.put("banyj", 0);
				vm.put("banks", 0);
			}
			double vipxh = vm.getBigDecimal("vipsxh")==null?0:vm.getBigDecimal("vipsxh").doubleValue();
			vm.put("vipyj", ToolArith.mul(vipxh, ratio));
			list.add(vm);//只有一对一
		}
		for(Mediator m: banList){
			boolean flag = true;
			String month = m.getStr("statmonth");
			String mid = m.getInt("mediatorid")+"";
			for(Mediator vm: vipList){
				String vmonth = vm.getStr("statmonth");
				String vmid = vm.getInt("mediatorid")+"";
				if(month.equals(vmonth)&&mid.equals(vmid)){
					flag = false;
					break;
				}else{
					continue;
				}
			}
			if(flag){//在上没的循环中一对一中没有小班的统计
				Organization orgs= Organization.dao.findById(1);
				Double ratio = m.getBigDecimal("ratio")==null?Double.parseDouble(orgs.get("basic_defaultpromo").toString()):m.getBigDecimal("ratio").doubleValue();
				m.put("vipsxh", 0);
				m.put("vipxxh", 0);
				m.put("vipyj", 0);
				m.put("vipks", 0);
				double banxh = m.getBigDecimal("bansxh")==null?0:m.getBigDecimal("bansxh").doubleValue();
				m.put("banyj", ToolArith.mul(banxh, ratio));
				m.set("ratio", ToolArith.mul(ratio, 100));
				list.add(m);
			}
		}
		return list;
	}

	public Mediator tuijianStatDetail(String mediatorId,String statMonth) {
		Mediator mediator = Mediator.dao.findById(Integer.parseInt(mediatorId));
		List<AccountBook> vipList = AccountBook.dao.queryVipDetail(mediatorId,statMonth);
		List<AccountBook> banList = AccountBook.dao.queryBanDetail(mediatorId,statMonth);
		List<AccountBook> list = new ArrayList<AccountBook>();
		double sxhsum = 0;
		double xxhsum = 0;
		double kssum = 0;
		for(AccountBook vip: vipList){
			double sxh = vip.getBigDecimal("sxh")==null?0:vip.getBigDecimal("sxh").doubleValue();//实消耗
			double xxh = vip.getBigDecimal("xxh")==null?0:vip.getBigDecimal("xxh").doubleValue();//虚消耗
			double ks = vip.getBigDecimal("ks")==null?0:vip.getBigDecimal("ks").doubleValue();//虚消耗
			sxhsum = ToolArith.add(sxhsum, sxh);
			xxhsum = ToolArith.add(xxhsum, xxh);
			kssum = ToolArith.add(kssum, ks);
			list.add(vip);//只有一对一
		}
		for(AccountBook ban: banList){
			double sxh = ban.getBigDecimal("sxh")==null?0:ban.getBigDecimal("sxh").doubleValue();//实消耗
			double xxh = ban.getBigDecimal("xxh")==null?0:ban.getBigDecimal("xxh").doubleValue();//虚消耗
			double ks = ban.getInt("ks")==null?0:ban.getInt("ks");
			sxhsum = ToolArith.add(sxhsum, sxh);
			xxhsum = ToolArith.add(xxhsum, xxh);
			kssum = ToolArith.add(kssum, ks);
			list.add(ban);
		}
		Organization org = Organization.dao.findById(1);
		Double ratio = mediator.getBigDecimal("ratio")==null?Double.parseDouble(org.get("basic_defaultpromo").toString()):mediator.getBigDecimal("ratio").doubleValue();
		mediator.put("sxhsum", sxhsum);
		mediator.put("xxhsum", xxhsum);
		mediator.put("kssum", kssum);
		mediator.put("statMonth", statMonth);
		mediator.put("sum", ToolArith.mul(sxhsum, ratio));
		mediator.set("ratio", ToolArith.mul(ratio, 100));
		mediator.put("accountbookList", list);
		return mediator;
	}

	public synchronized JSONObject settlementTuijian(String mediatorId, String statMonth,Integer sysuserId) {
		SysUser sysuser = SysUser.dao.findById(sysuserId);
		JSONObject json = new JSONObject();
		String code ="0";
		String msg ="结算成功";
		if(sysuser == null){
			msg="请先登录系统再进行结算提交";
		}else{
			Mediator mediator = this.tuijianStatDetail(mediatorId, statMonth);
			if(mediator == null){
				msg="没有要结算的渠道";
			}else{
				List<AccountBook> list = mediator.get("accountbookList");
				if(list == null){
					msg="没有要结算的数据";
				}else{
					try {
						boolean isclearing = Brokerage.dao.findByClearingMonth(statMonth);
						if(isclearing){//已有结算记录
							msg="渠道"+mediator.getStr("realname")+"已经结算过了";
						}else{
							Brokerage brokerage = new Brokerage();
							Date nowdate = ToolDateTime.getDate();
							brokerage.set("createdate", nowdate);
							brokerage.set("clearingmonth", statMonth);
							brokerage.set("mediatorid", mediator.getPrimaryKeyValue());
							brokerage.set("mediatorname", mediator.getStr("realname"));
							brokerage.set("moneysum", mediator.getDouble("sum"));
							brokerage.set("realsum", mediator.getDouble("sxhsum"));
							brokerage.set("coursetotal", mediator.getDouble("kssum"));
							brokerage.set("ratio", ToolArith.div(mediator.getDouble("ratio"),100));
							brokerage.set("type", 1);
							brokerage.set("ispay", 0);
							brokerage.set("createuserid", sysuser.getPrimaryKeyValue());
							brokerage.set("createusername", sysuser.getStr("real_name"));
							boolean isok = brokerage.save();
							if(isok){
								for(AccountBook b:list){
									log.info("结算---->"+b);
									BrokerageDetail detail = new BrokerageDetail();
									detail.set("studentid", b.get("accountid"));
									detail.set("studentname", b.get("studentname"));
									detail.set("mediatorid", mediator.getPrimaryKeyValue());
									detail.set("mediatorname", mediator.getStr("realname"));
									detail.set("courseplanid", b.getInt("courseplanid"));
									if(b.getInt("courseid")!=null){
										detail.set("courseid", b.getInt("courseid"));
										detail.set("coursename", Course.dao.findById(b.getInt("courseid")).getStr("course_name"));
									}
									if(b.getInt("classorderid")!=null){
										detail.set("classorderid", b.getInt("classorderid"));
										detail.set("classnum", ClassOrder.dao.findById(b.getInt("classorderid")).getStr("classnum"));
									}
									detail.set("coursetime",b.get("statmonth"));
									detail.set("createtime", new Date());
									detail.set("accountid", b.getInt("accountid"));
									detail.set("brokerageid", brokerage.getPrimaryKeyValue());
									detail.set("amount", b.get("realamount"));
									detail.set("accountbookid", b.getPrimaryKeyValue());
									detail.set("ispay",0);
									detail.set("createuserid", sysuser.getPrimaryKeyValue());
									detail.set("createusername", sysuser.getStr("real_name"));
									boolean saveok = detail.save();
									if(saveok){
										b.set("isclearing", 1);
										b.set("version",b.getInt("version")+1);
										b.set("updatetime", new Date());
										b.update();
										continue;
									}else{
										detail.deleteByBrokerageId(brokerage.getPrimaryKeyValue());
										msg="结算数据有问题,请联系系统管理员";
									}
								}
							}else{
								msg="提交结算没有成功，请联系管理员";
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						msg="系统异常，请联系管理员";
					}
				}
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}

	/**
	 * 支付佣金确认
	 * @param brokerageid
	 */
	public boolean sureIspayBrokerage(String brokerageid,String paytime) {
		Brokerage  bd = Brokerage.dao.findById(brokerageid);
		bd.set("ispay", 1);//已支付
		bd.set("paytime", paytime);
		bd.set("version", bd.getInt("version")+1);
		return bd.update();
	}


}
