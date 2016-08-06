package com.momathink.crm.brokerage.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.crm.brokerage.model.Brokerage;
import com.momathink.crm.brokerage.model.BrokerageDetail;
import com.momathink.crm.brokerage.service.BrokerageService;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.sys.system.model.SysUser;

/**
 * 报表统计 ClassName: ReportController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年8月29日 下午4:25:56 <br/>
 *
 * @author David
 * @version
 * @since JDK 1.7
 */
@Controller(controllerKey = "/brokerage")
public class BrokerageController extends BaseController {

	private static Logger log = Logger.getLogger(BrokerageController.class);

	private BrokerageService brokerageService = new BrokerageService();

	/**
	 * 佣金支付记录
	 */
	public void index() {
		log.debug("结算记录：支付记录分页");
		List<Mediator> mediators = Mediator.dao.findAll();
		setAttr("mediators", mediators);
		brokerageService.list(splitPage);
		setAttr("showPages", splitPage.getPage());
		render("/WEB-INF/view/brokerage/brokerage_list.jsp");
	}
	
	/**
	 * 结算明细
	 */
	public void showBrokerageDetail(){
		String brokerageid = getPara("brokerageId");
		setAttr("brokerage",Brokerage.dao.findById(brokerageid));
		List<BrokerageDetail> list = BrokerageDetail.dao.findByBrokerageIds(brokerageid);
		setAttr("detail",list);
		setAttr("brokerageid",brokerageid);
		render("/WEB-INF/view/brokerage/showBrokerageDetail.jsp");
	}
	
	/**
	 * 确认支付佣金
	 */
	public void sureIspayBrokerage(){
		JSONObject json = new JSONObject();
		String brokerageid = getPara("brokerageid");
		String paytime = getPara("paytime");
		boolean sure = brokerageService.sureIspayBrokerage(brokerageid,paytime);
		if(sure){
			json.put("code", 1);
		}else{
			json.put("code", 0);
		}
		renderJson(json);
	}
	
	public void turntosurepae(){
		render("/WEB-INF/view/brokerage/sureIspayBrokerage.jsp");
	}
	

	/**
	 * 推荐佣金
	 */
	public void tuijian() {
		log.debug("佣金管理：推荐佣金");
		String mediatorId = getPara("mediatorId");
		List<Mediator> mediators = Mediator.dao.findAll();
		setAttr("mediators", mediators);
		List<Mediator> mediatorList = brokerageService.tuijianStat(mediatorId);
		setAttr("mediatorList", mediatorList);
		render("/WEB-INF/view/brokerage/tuijian_stat_list.jsp");
	}

	public void tuijianStatDetail(){
		log.debug("佣金管理：推荐佣金");
		String mediatorId = getPara("mediatorId");
		String statMonth = getPara("statMonth");
		Mediator mediator = brokerageService.tuijianStatDetail(mediatorId,statMonth);
		setAttr("mediator", mediator);
		render("/WEB-INF/view/brokerage/tuijian_detail_list.jsp");
	}
	
	/**
	 * 提交推荐结算
	 */
	public void tuijianSubmit() {
		log.debug("佣金管理：结算提交");
		String mediatorId = getPara("mediatorId");
		String statMonth = getPara("statMonth");
		JSONObject json = new JSONObject();
		json = brokerageService.settlementTuijian(mediatorId,statMonth,getSysuserId());
		renderJson(json);
	}
	/**
	 * 推广佣金
	 */
	public void tuiguang() {
		log.debug("佣金管理：推广佣金");
		String counselorName = getPara("counselorName");
		String beginDate = getPara("beginDate");
		String endDate = getPara("endDate");
		List<Mediator> counselorList = brokerageService.tuijianBrokerage(counselorName, beginDate, endDate, "2");
		setAttr("counselorList", counselorList);
		render("/brokerage/tuiguang/list.jsp");
	}

	/**
	 * 佣金结算
	 */
	public void settlement() {
		log.debug("佣金管理：结算");
		String counselorids = getPara("counselorids");
		String beginDate = getPara("beginDate");
		String endDate = getPara("endDate");
		String brokerageType = getPara("brokerageType");
		Mediator counselor = brokerageService.tuijianBrokerageDetail(counselorids, beginDate, endDate, brokerageType);
		setAttr("counselor", counselor);
		if ("1".equals(brokerageType))
			render("/brokerage/tuijian/settlement.jsp");
		else
			render("/brokerage/tuiguang/settlement.jsp");

	}

	/**
	 * 提交结算
	 */
	public void settlementSubmit() {
		log.debug("佣金管理：结算提交");
		SysUser user = SysUser.dao.findById(getParaToInt("accountId"));
		String counselorids = getPara("counselorids");
		String beginDate = getPara("beginDate");
		String endDate = getPara("endDate");
		String brokerageType = getPara("brokerageType");
		Mediator counselor = brokerageService.saveBrokerage(counselorids, beginDate, endDate, brokerageType, user);
		setAttr("counselor", counselor);
		redirect("/brokerage");
	}

	/**
	 * 确认支付
	 */
	public void confirmPay() {
		log.debug("佣金管理：确认支付");
		SysUser user = SysUser.dao.findById(getParaToInt("accountId"));
		brokerageService.confirmPayByIds(getPara(), user);
		redirect("/brokerage");
	}

	/**
	 * 查看佣金详情
	 */
	public void queryBrokerageDetail() {
		log.debug("佣金管理：查看结算明细");
		String counselorids = getPara("counselorids");
		String brokerageids = getPara("brokerageids");
		Mediator counselor = brokerageService.queryBrokerageDetail(counselorids, brokerageids);
		setAttr("counselor", counselor);
		render("/brokerage/brokerageDetail.jsp");
	}
}
