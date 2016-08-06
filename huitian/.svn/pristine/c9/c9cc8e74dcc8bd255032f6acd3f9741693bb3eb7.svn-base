package com.momathink.crm.opportunity.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.opportunity.model.Feedback;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.crm.opportunity.service.OpportunityCensusService;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.subject.model.Subject;

/**
 * 销售机会
 * @author David
 *
 */
@Controller(controllerKey = "/opportunitycensus")
public class OpportunityCensusController extends BaseController {

	private OpportunityCensusService opportunityCensusService = new OpportunityCensusService();

	/**
	 * 销售统计列表
	 * @throws ParseException 
	 */
	public void opportunityCensusByCampus() throws ParseException{
		String yuefen1 = getPara("yuefen1");
		String yuefen2 = getPara("yuefen2");
		String campusids = getPara("teachergroup.teacherids");
		int campusCount = 0;
		StringBuffer campusIdStrB = new StringBuffer();
		StringBuffer campsusTitleStrB = new StringBuffer();
		List<Campus> campusList = new ArrayList<Campus>();
		List<Record> campsusNameList = new ArrayList<Record>();
		if(!ToolString.isNull(campusids)){
			campusids = campusids.replaceFirst("\\|", "");
			setAttr("teacheredit", campusids);
			String[] str = campusids.replace("|", ",").split(",");
			for(int i=0;i<str.length;i++){
				campusIdStrB.append(","+str[i]);
				campusCount++;
			}
			campusids = campusIdStrB.toString().replaceFirst(",", "");
			campusList = Campus.dao.find("SELECT * FROM campus WHERE Id IN ("+campusids+") ORDER BY Id");
			campsusNameList = Db.find("SELECT CAMPUS_NAME FROM campus WHERE Id IN ("+campusids+") ORDER BY Id");
		}else{
			List<Record> campusCountList = Db.find("SELECT COUNT(*) count FROM campus ");
			campusCount =Integer.parseInt(String.valueOf(campusCountList.get(0).getLong("count")));
			campusList = Campus.dao.getCampus();
			campsusNameList = Db.find("SELECT CAMPUS_NAME FROM campus ORDER BY Id");
		}	
		
		for(Record campsus:campsusNameList){
			String campsusname = campsus.getStr("CAMPUS_NAME");
			campsusTitleStrB.append("'"+campsusname+"-咨询量',");
		}
		campsusTitleStrB.append("'',");
			for(Record campsus:campsusNameList){
				String campsusname = campsus.getStr("CAMPUS_NAME");
				campsusTitleStrB.append("'"+campsusname+"-成单量',");
			}
		String campsusStr = campsusTitleStrB.toString();
		campsusStr = StringUtils.reverse(campsusStr);
		campsusStr = campsusStr.replaceFirst(",'","");
		campsusStr = StringUtils.reverse(campsusStr);
		campsusStr = campsusStr.replaceFirst("'","");
		setAttr("campsusStr",campsusStr);
		
		if(ToolString.isNull(yuefen1)){
			yuefen1 = ToolDateTime.format(new Date(), "yyyy-MM");
		}
		if(ToolString.isNull(yuefen2)){
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
			Date beginDate = dft.parse(yuefen1);
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 5);
			date.set(Calendar.DAY_OF_MONTH, 15);
			Date startDate = dft.parse(dft.format(date.getTime()));
			yuefen2 = ToolDateTime.format(startDate, "yyyy-MM");
		}
		List<Opportunity> list = opportunityCensusService.getOpportunityCensusByCampus(yuefen1,yuefen2,campusids);
		setAttr("list", list);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		Date endMonth = dft.parse(yuefen1);
		Date startMonth = dft.parse(yuefen2);
		List<String> monthList = ToolDateTime.printMonths(startMonth,endMonth);
		setAttr("campusList", Campus.dao.getCampus());
		Map<String, List<Campus>> monthmap = new LinkedHashMap<String, List<Campus>>();
		List<StringBuffer> zxldataStrB = new ArrayList<StringBuffer>();
		List<StringBuffer> cdldataStrB = new ArrayList<StringBuffer>();
		List<String> zxlcolor = new ArrayList<String>();
		zxlcolor.add("26,179,148,0.5");
		zxlcolor.add("247,165,74,0.5");
		zxlcolor.add("26,123,185,0.5");
		List<String> cdlcolor = new ArrayList<String>();
		cdlcolor.add("26,179,148,1");
		cdlcolor.add("247,165,74,1");
		cdlcolor.add("26,123,185,1");
		List<String> textStyle = new ArrayList<String>();
		textStyle.add("");
		textStyle.add(",textStyle:{color:'#27727B'}");
		textStyle.add(",textStyle:{color:'#E87C25'}");
		List<String> formatter = new ArrayList<String>();
		formatter.add(",formatter:function(p){return p.value > 0 ? (p.value +'\\n'):'';}");
		formatter.add(",formatter:function(p){return p.value > 0 ? (p.value +'+'):'';}");
		for(int j=0;j<campusCount;j++){
			zxldataStrB.add(new StringBuffer());
			cdldataStrB.add(new StringBuffer());
		}
		StringBuffer yuefenStrB = new StringBuffer();
		for(String month:monthList){//月份
			List<Campus> _campusList = new ArrayList<Campus>();
			int i = 0;
			for(Campus campus : campusList){
				Campus _campus = new Campus();
				_campus.put("zxl",0L);
				_campus.put("cdl",0L);
				_campus.put("campus_name",campus.getStr("campus_name"));
				_campus.put("message",campus.getInt("Id"));
				for(Opportunity opportunity:list){
					if(month.equalsIgnoreCase(opportunity.getStr("yuefen"))){
						if(campus.getPrimaryKeyValue().equals(opportunity.getInt("id"))){
							_campus.put("zxl",opportunity.getLong("zxl"));
							_campus.put("cdl",opportunity.getLong("cdl"));
							break;
						}
					}else{
						continue;
					}
				}
				zxldataStrB.get(i).append(","+_campus.getLong("zxl"));
				cdldataStrB.get(i).append(","+_campus.getLong("cdl"));
				_campusList.add(_campus);
				i++;
			}
			monthmap.put(month, _campusList);
			yuefenStrB.append(",'"+month+"'");
		}
		String yuefenStr = yuefenStrB.toString();
		yuefenStr = yuefenStr.replaceFirst(",","");
		setAttr("yuefenStr", yuefenStr);
		List<String> zxldata = new ArrayList<String>();
		List<String> cdldata = new ArrayList<String>();
		for(int j=0;j<campusCount;j++){
			zxldata.add(zxldataStrB.get(j).toString().replaceFirst(",",""));
			cdldata.add(cdldataStrB.get(j).toString().replaceFirst(",",""));
		}
		StringBuffer zxlStrB = new StringBuffer();
		StringBuffer cdlStrB = new StringBuffer();
		int i = 0;
		for(Campus campus:campusList){
			int j = i%3; 
			cdlStrB.append("{name:'"+campus.getStr("CAMPUS_NAME")+"-成单量',type:'bar',itemStyle: {normal:");
			cdlStrB.append(" {color:'rgba("+cdlcolor.get(j)+")', label:{show:true"+textStyle.get(j)+"}}},data:["+cdldata.get(i)+"]},");
			zxlStrB.append("{name:'"+campus.getStr("CAMPUS_NAME")+"-咨询量',type:'bar',xAxisIndex:1,itemStyle: {normal:");
			zxlStrB.append(" {color:'rgba("+zxlcolor.get(j)+")',label:{show:true"+formatter.get(0)+"}}},data:["+zxldata.get(i)+"]},");
			i++;
		}
		String zxlStr = zxlStrB.toString();
		zxlStr = StringUtils.reverse(zxlStr);
		zxlStr = zxlStr.replaceFirst(",","");
		zxlStr = StringUtils.reverse(zxlStr);
		cdlStrB.append(zxlStr);
		setAttr("seriesStr", cdlStrB);
		setAttr("monthmap", monthmap);
		setAttr("yuefen1", yuefen1);
		setAttr("yuefen2", yuefen2);
		render("/opportunity_census/camp_census.jsp");
	}
	
	public void opportunityCensusBySource() throws ParseException  {
		String yuefen1 = getPara("yuefen1");
		String yuefen2 = getPara("yuefen2");
		String campus = getPara("campus");
		setAttr("campusList", Campus.dao.getCampus());
		setAttr("campusid", campus);
		setAttr("yuefen1", yuefen1);
		setAttr("yuefen2", yuefen2);
		
		boolean flag = false;
		if(ToolString.isNull(yuefen1)){
			yuefen1 = ToolDateTime.format(new Date(), "yyyy-MM");
		}
		if(ToolString.isNull(yuefen2)){
			flag = true;
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
			Date beginDate = dft.parse(yuefen1);
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 11);
			date.set(Calendar.DAY_OF_MONTH, 15);
			Date startDate = dft.parse(dft.format(date.getTime()));
			yuefen2 = ToolDateTime.format(startDate, "yyyy-MM");
		}
		List<Opportunity> list = opportunityCensusService.getOpportunityCensusBySource(yuefen1,yuefen2,campus);
		setAttr("list", list);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		Date endMonth = dft.parse(yuefen1);
		Date startMonth = dft.parse(yuefen2);
		List<String> monthList = ToolDateTime.printMonths(startMonth,endMonth);
		Map<String, List<Record>> monthmap = new LinkedHashMap<String, List<Record>>();
		List<StringBuffer> zxldataStrB = new ArrayList<StringBuffer>();
		List<StringBuffer> cdldataStrB = new ArrayList<StringBuffer>();
		for(int j=0;j<monthList.size();j++){
			zxldataStrB.add(new StringBuffer());
			cdldataStrB.add(new StringBuffer());
		}
		List<Record> sourceList = Db.find("SELECT * FROM crm_source ");
		StringBuffer sourcename = new StringBuffer();
		for(Record source:sourceList){
				sourcename.append("'"+source.getStr("name")+"',");
		}
		List <String> yuefenList = new ArrayList<String>();
		StringBuffer yuefenStrB = new StringBuffer();
		Long Maxzxl = 0L;
		Long Maxcdl = 0L;
		int i = 0;
		for(String month:monthList){//月份
			List<Record> _sourceList = new ArrayList<Record>();
			for(Record source : sourceList){
				Record _source = new Record();
				_source.set("zxl",0L);
				_source.set("cdl",0L);
				_source.set("name",source.getStr("name"));
				_source.set("message",source.getInt("id"));
				for(Opportunity opportunity:list){
					if(month.equalsIgnoreCase(opportunity.getStr("yuefen"))){
						if(source.getInt("id").equals(opportunity.getInt("source"))){
							_source.set("zxl",opportunity.getLong("zxl"));
							_source.set("cdl",opportunity.getLong("cdl"));
							if(Maxzxl<opportunity.getLong("zxl")){
								Maxzxl = opportunity.getLong("zxl");
							}
							if(Maxcdl<opportunity.getLong("cdl")){
								Maxcdl = opportunity.getLong("cdl");
							}
							break;
						}
					}else{
						continue;
					}
				}
				zxldataStrB.get(i).append(","+_source.getLong("zxl"));
				cdldataStrB.get(i).append(","+_source.getLong("cdl"));
				_sourceList.add(_source);
			}
			i++;
			monthmap.put(month, _sourceList);
			yuefenStrB.append(",'"+month+"-01'");
			yuefenList.add(month);
		}
		List<String> zxldata = new ArrayList<String>();
		List<String> cdldata = new ArrayList<String>();
		for(int j=0;j<monthList.size();j++){
			zxldata.add(zxldataStrB.get(j).toString().replaceFirst(",",""));
			cdldata.add(cdldataStrB.get(j).toString().replaceFirst(",",""));
		}
		String yuefenStr = yuefenStrB.toString();
		yuefenStr = yuefenStr.replaceFirst(",","");
		StringBuffer optionStrB = new StringBuffer();
		optionStrB.append("{timeline:{data:["+yuefenStr+"],");
		if(flag){
			optionStrB.append("currentIndex:"+(yuefenList.size()-1)+",");
		}
		optionStrB.append("label : {formatter : function(s) { return s.slice(5, 7);}},autoPlay : false,playInterval : 1000 },options:[{title : {'text':'"+yuefenList.get(0)+"',/*'subtext':'XXXX年来源统计'*/},tooltip : {'trigger':'axis'},legend : {x:'right','data':['咨询量','成单量'],'selected':{'咨询量':true,'成单量':true}},toolbox : {'show':true,orient : 'vertical',x: 'right',y: 'center','feature':{'mark':{'show':true},'dataView':{'show':true,'readOnly':false}, 'magicType':{'show':true,'type':['line','bar','stack','tiled']},'restore':{'show':true},'saveAsImage':{'show':true}}},calculable : true,grid : {'y':80,'y2':100},xAxis : [{'type':'category','axisLabel':{'interval':0},'data':["+sourcename+"]}],yAxis : [{'type':'value','name':'咨询量','max':"+Maxzxl+"},{'type':'value','name':'成单量','max':"+Maxcdl+"}],series : [{'name':'咨询量','type':'bar','markLine':{ symbol : ['arrow','none'], symbolSize : [4, 2],itemStyle : {normal: {lineStyle: {color:'orange'},barBorderColor:'orange',label:{ position:'left', formatter:function(params){return Math.round(params.value);},textStyle:{color:'orange'}}}},'data':[{'type':'average','name':'平均值'}]},'data': ["+zxldata.get(0)+"]},{'name':'成单量','yAxisIndex':1,'type':'bar','data': ["+cdldata.get(0)+"]},]},");
		for(int x = 1; x<yuefenList.size();x++){
			optionStrB.append("{title : {'text':'"+yuefenList.get(x)+"'},series : [{'data': ["+zxldata.get(x)+"]},{'data': ["+cdldata.get(x)+"]}]},");
		}
		optionStrB.append("]}");
		setAttr("option", optionStrB);
		setAttr("monthmap", monthmap);
		render("/opportunity_census/source_census.jsp");
	}
	
	public void opportunityCensusBymediator() throws ParseException  {
		String yuefen1 = getPara("yuefen1");
		String yuefen2 = getPara("yuefen2");
		String campus = getPara("campus");
		setAttr("campusList", Campus.dao.getCampus());
		setAttr("campusid", campus);
		setAttr("yuefen1", yuefen1);
		setAttr("yuefen2", yuefen2);
		
		boolean flag = false;
		if(ToolString.isNull(yuefen1)){
			yuefen1 = ToolDateTime.format(new Date(), "yyyy-MM");
		}
		if(ToolString.isNull(yuefen2)){
			flag = true;
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
			Date beginDate = dft.parse(yuefen1);
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 11);
			date.set(Calendar.DAY_OF_MONTH, 15);
			Date startDate = dft.parse(dft.format(date.getTime()));
			yuefen2 = ToolDateTime.format(startDate, "yyyy-MM");
		}
		List<Opportunity> list = opportunityCensusService.getOpportunityCensusBymediator(yuefen1,yuefen2,campus);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		Date endMonth = dft.parse(yuefen1);
		Date startMonth = dft.parse(yuefen2);
		List<String> monthList = ToolDateTime.printMonths(startMonth,endMonth);
		Map<String, List<Record>> monthmap = new LinkedHashMap<String, List<Record>>();
		List<StringBuffer> zxldataStrB = new ArrayList<StringBuffer>();
		List<StringBuffer> cdldataStrB = new ArrayList<StringBuffer>();
		for(int j=0;j<monthList.size();j++){
			zxldataStrB.add(new StringBuffer());
			cdldataStrB.add(new StringBuffer());
		}
		List<StringBuffer> mediatorNameList = new ArrayList<StringBuffer>();
		StringBuffer yuefenStrB = new StringBuffer();
		int i = 0;
		Long Maxzxl = 0L;
		Long Maxcdl = 0L;
		for(String month:monthList){//月份
			List<Record> _mediatorList = new ArrayList<Record>();
			StringBuffer mediatorName = new StringBuffer();
			for(Opportunity opportunity:list){
				Record _mediator = new Record();
				if(month.equalsIgnoreCase(opportunity.getStr("yuefen"))&&(!ToolString.isNull(opportunity.getStr("realname")))){
						_mediator.set("zxl",opportunity.getLong("zxl"));
						_mediator.set("cdl",opportunity.getLong("cdl"));
						_mediator.set("realname",opportunity.getStr("realname"));
						_mediator.set("message",opportunity.getInt("mediatorid"));
						zxldataStrB.get(i).append(","+_mediator.getLong("zxl"));
						cdldataStrB.get(i).append(","+_mediator.getLong("cdl"));
						_mediatorList.add(_mediator);
						if(Maxzxl<opportunity.getLong("zxl")){
							Maxzxl = opportunity.getLong("zxl");
						}
						if(Maxcdl<opportunity.getLong("cdl")){
							Maxcdl = opportunity.getLong("cdl");
						}
						mediatorName.append("'"+opportunity.getStr("realname")+"',");
					}
			}
			i++;
			monthmap.put(month, _mediatorList);
			yuefenStrB.append(",'"+month+"-01'");
			mediatorNameList.add(mediatorName);
		}
		List<String> zxldata = new ArrayList<String>();
		List<String> cdldata = new ArrayList<String>();
		for(int j=0;j<monthList.size();j++){
			zxldata.add(zxldataStrB.get(j).toString().replaceFirst(",",""));
			cdldata.add(cdldataStrB.get(j).toString().replaceFirst(",",""));
		}
		
		
		String yuefenStr = yuefenStrB.toString();
		yuefenStr = yuefenStr.replaceFirst(",","");
		StringBuffer optionStrB = new StringBuffer();
		
		optionStrB.append("{timeline:{data:["+yuefenStr+"],");
		if(flag){
			optionStrB.append("currentIndex:"+(monthList.size()-1)+",");
		}
		optionStrB.append("label : {formatter : function(s) { return s.slice(5, 7);}},autoPlay : false,playInterval : 1000 },options:[{title : {'text':'"+monthList.get(0)+"',/*'subtext':'XXXX年来源统计'*/},tooltip : {'trigger':'axis'},legend : {x:'right','data':['咨询量','成单量'],'selected':{'咨询量':true,'成单量':true}},toolbox : {'show':true,orient : 'vertical',x: 'right',y: 'center','feature':{'mark':{'show':true},'dataView':{'show':true,'readOnly':false}, 'magicType':{'show':true,'type':['line','bar','stack','tiled']},'restore':{'show':true},'saveAsImage':{'show':true}}},calculable : true,grid : {'y':80,'y2':100},xAxis : [{'type':'category','axisLabel':{'interval':0},'data':["+mediatorNameList.get(0)+"]}],yAxis : [{'type':'value','name':'咨询量','max':"+Maxzxl+"},{'type':'value','name':'成单量','max':"+Maxcdl+"}],series : [{'name':'咨询量','type':'bar','markLine':{ symbol : ['arrow','none'], symbolSize : [4, 2],itemStyle : {normal: {lineStyle: {color:'orange'},barBorderColor:'orange',label:{ position:'left', formatter:function(params){return Math.round(params.value);},textStyle:{color:'orange'}}}},'data':[{'type':'average','name':'平均值'}]},'data': ["+zxldata.get(0)+"]},{'name':'成单量','yAxisIndex':1,'type':'bar','data': ["+cdldata.get(0)+"]},]},");
		
		for(int x = 1; x<monthList.size();x++){
			optionStrB.append("{title : {'text':'"+monthList.get(x)+"'},series : [{'data': ["+zxldata.get(x)+"]},{'data': ["+cdldata.get(x)+"]},],xAxis : [{'type':'category','axisLabel':{'interval':0},'data':["+mediatorNameList.get(x)+"]}],},");
		}
		optionStrB.append("]}");
		setAttr("option", optionStrB);
		setAttr("yuefenStr", yuefenStr);
		setAttr("monthmap", monthmap);
		render("/opportunity_census/channel_census.jsp");
	}
	 
	public void opportunityCensusBykcuser() throws ParseException  {
		String yuefen1 = getPara("yuefen1");
		String yuefen2 = getPara("yuefen2");
		String campus = getPara("campus");
		setAttr("campusList", Campus.dao.getCampus());
		setAttr("campusid", campus);
		setAttr("yuefen1", yuefen1);
		setAttr("yuefen2", yuefen2);
		
		boolean flag = false;
		if(ToolString.isNull(yuefen1)){
			yuefen1 = ToolDateTime.format(new Date(), "yyyy-MM");
		}
		if(ToolString.isNull(yuefen2)){
			flag = true;
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
			Date beginDate = dft.parse(yuefen1);
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 11);
			date.set(Calendar.DAY_OF_MONTH, 15);
			Date startDate = dft.parse(dft.format(date.getTime()));
			yuefen2 = ToolDateTime.format(startDate, "yyyy-MM");
		}
		List<Opportunity> list = opportunityCensusService.getOpportunityCensusBykcuser(yuefen1,yuefen2,campus);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		Date endMonth = dft.parse(yuefen1);
		Date startMonth = dft.parse(yuefen2);
		List<String> monthList = ToolDateTime.printMonths(startMonth,endMonth);
		Map<String, List<Record>> monthmap = new LinkedHashMap<String, List<Record>>();
		List<StringBuffer> zxldataStrB = new ArrayList<StringBuffer>();
		List<StringBuffer> cdldataStrB = new ArrayList<StringBuffer>();
		for(int j=0;j<monthList.size();j++){
			zxldataStrB.add(new StringBuffer());
			cdldataStrB.add(new StringBuffer());
		}
		List<StringBuffer> mediatorNameList = new ArrayList<StringBuffer>();
		StringBuffer yuefenStrB = new StringBuffer();
		int i = 0;
		Long Maxzxl = 0L;
		Long Maxcdl = 0L;
		for(String month:monthList){//月份
			List<Record> _mediatorList = new ArrayList<Record>();
			StringBuffer mediatorName = new StringBuffer();
			for(Opportunity opportunity:list){
				Record _mediator = new Record();
				if(month.equalsIgnoreCase(opportunity.getStr("yuefen"))&&(!ToolString.isNull(opportunity.getStr("REAL_NAME")))){
						_mediator.set("zxl",opportunity.getLong("zxl"));
						_mediator.set("cdl",opportunity.getLong("cdl"));
						_mediator.set("realname",opportunity.getStr("REAL_NAME"));
						_mediator.set("message",opportunity.getInt("kcuserid"));
						zxldataStrB.get(i).append(","+_mediator.getLong("zxl"));
						cdldataStrB.get(i).append(","+_mediator.getLong("cdl"));
						_mediatorList.add(_mediator);
						if(Maxzxl<opportunity.getLong("zxl")){
							Maxzxl = opportunity.getLong("zxl");
						}
						if(Maxcdl<opportunity.getLong("cdl")){
							Maxcdl = opportunity.getLong("cdl");
						}
						mediatorName.append("'"+opportunity.getStr("REAL_NAME")+"',");
					}
			}
			i++;
			monthmap.put(month, _mediatorList);
			yuefenStrB.append(",'"+month+"-01'");
			mediatorNameList.add(mediatorName);
		}
		List<String> zxldata = new ArrayList<String>();
		List<String> cdldata = new ArrayList<String>();
		for(int j=0;j<monthList.size();j++){
			zxldata.add(zxldataStrB.get(j).toString().replaceFirst(",",""));
			cdldata.add(cdldataStrB.get(j).toString().replaceFirst(",",""));
		}
		
		
		String yuefenStr = yuefenStrB.toString();
		yuefenStr = yuefenStr.replaceFirst(",","");
		StringBuffer optionStrB = new StringBuffer();
		
		optionStrB.append("{timeline:{data:["+yuefenStr+"],");
		if(flag){
			optionStrB.append("currentIndex:"+(monthList.size()-1)+",");
		}
		optionStrB.append("label : {formatter : function(s) { return s.slice(5, 7);}},autoPlay : false,playInterval : 1000 },options:[{title : {'text':'"+monthList.get(0)+"',/*'subtext':'XXXX年来源统计'*/},tooltip : {'trigger':'axis'},legend : {x:'right','data':['咨询量','成单量'],'selected':{'咨询量':true,'成单量':true}},toolbox : {'show':true,orient : 'vertical',x: 'right',y: 'center','feature':{'mark':{'show':true},'dataView':{'show':true,'readOnly':false}, 'magicType':{'show':true,'type':['line','bar','stack','tiled']},'restore':{'show':true},'saveAsImage':{'show':true}}},calculable : true,grid : {'y':80,'y2':100},xAxis : [{'type':'category','axisLabel':{'interval':0},'data':["+mediatorNameList.get(0)+"]}],yAxis : [{'type':'value','name':'咨询量','max':"+Maxzxl+"},{'type':'value','name':'成单量','max':"+Maxcdl+"}],series : [{'name':'咨询量','type':'bar','markLine':{ symbol : ['arrow','none'], symbolSize : [4, 2],itemStyle : {normal: {lineStyle: {color:'orange'},barBorderColor:'orange',label:{ position:'left', formatter:function(params){return Math.round(params.value);},textStyle:{color:'orange'}}}},'data':[{'type':'average','name':'平均值'}]},'data': ["+zxldata.get(0)+"]},{'name':'成单量','yAxisIndex':1,'type':'bar','data': ["+cdldata.get(0)+"]},]},");
		
		for(int x = 1; x<monthList.size();x++){
			optionStrB.append("{title : {'text':'"+monthList.get(x)+"'},series : [{'data': ["+zxldata.get(x)+"]},{'data': ["+cdldata.get(x)+"]},],xAxis : [{'type':'category','axisLabel':{'interval':0},'data':["+mediatorNameList.get(x)+"]}],},");
		}
		optionStrB.append("]}");
		setAttr("option", optionStrB);
		setAttr("yuefenStr", yuefenStr);
		setAttr("monthmap", monthmap);
		render("/opportunity_census/kcuser_census.jsp");
	}
	
	public void opportunityCensusByCustomerRating() throws ParseException  {
		String yuefen1 = getPara("yuefen1");
		String yuefen2 = getPara("yuefen2");
		String campus = getPara("campus");
		setAttr("campusList", Campus.dao.getCampus());
		setAttr("campusid", campus);
		setAttr("yuefen1", yuefen1);
		setAttr("yuefen2", yuefen2);
		
		boolean flag = false;
		if(ToolString.isNull(yuefen1)){
			yuefen1 = ToolDateTime.format(new Date(), "yyyy-MM");
		}
		if(ToolString.isNull(yuefen2)){
			flag = true;
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
			Date beginDate = dft.parse(yuefen1);
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 11);
			date.set(Calendar.DAY_OF_MONTH, 15);
			Date startDate = dft.parse(dft.format(date.getTime()));
			yuefen2 = ToolDateTime.format(startDate, "yyyy-MM");
		}
		List<Opportunity> list = opportunityCensusService.getOpportunityCensusByCustomerRating(yuefen1,yuefen2,campus);
		setAttr("list", list);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
		Date endMonth = dft.parse(yuefen1);
		Date startMonth = dft.parse(yuefen2);
		List<String> monthList = ToolDateTime.printMonths(startMonth,endMonth);
		Map<String, List<Record>> monthmap = new LinkedHashMap<String, List<Record>>();
		List<StringBuffer> zxldataStrB = new ArrayList<StringBuffer>();
		List<StringBuffer> cdldataStrB = new ArrayList<StringBuffer>();
		for(int j=0;j<monthList.size();j++){
			zxldataStrB.add(new StringBuffer());
			cdldataStrB.add(new StringBuffer());
		}
		List<Record> customerRatingList = new ArrayList<Record>();
		Record customerRating = new Record();
		customerRating.set("name", "未知客户");
		customerRating.set("id", 0);
		customerRatingList.add(customerRating);
		customerRating = new Record();
		customerRating.set("name", "潜在客户");
		customerRating.set("id", 1);
		customerRatingList.add(customerRating);
		customerRating = new Record();
		customerRating.set("name", "目标客户");
		customerRating.set("id", 2);
		customerRatingList.add(customerRating);
		customerRating = new Record();
		customerRating.set("name", "发展中客户");
		customerRating.set("id", 3);
		customerRatingList.add(customerRating);
		customerRating = new Record();
		customerRating.set("name", "交易客户");
		customerRating.set("id", 4);
		customerRatingList.add(customerRating);
		customerRating = new Record();
		customerRating.set("name", "后续介绍客户");
		customerRating.set("id", 5);
		customerRatingList.add(customerRating);
		customerRating = new Record();
		customerRating.set("name", "非客户");
		customerRating.set("id", 6);
		customerRatingList.add(customerRating);
		
		StringBuffer customerRatingname = new StringBuffer();
		for(Record source:customerRatingList){
			customerRatingname.append("'"+source.getStr("name")+"',");
		}
		List <String> yuefenList = new ArrayList<String>();
		StringBuffer yuefenStrB = new StringBuffer();
		Long Maxzxl = 0L;
		Long Maxcdl = 0L;
		int i = 0;
		for(String month:monthList){//月份
			List<Record> _sourceList = new ArrayList<Record>();
			for(Record source : customerRatingList){
				Record _source = new Record();
				_source.set("zxl",0L);
				_source.set("cdl",0L);
				_source.set("name",source.getStr("name"));
				_source.set("message",source.getInt("id"));
				for(Opportunity opportunity:list){
					if(month.equalsIgnoreCase(opportunity.getStr("yuefen"))){
						if(source.getInt("id").equals(opportunity.getInt("customer_rating"))){
							_source.set("zxl",opportunity.getLong("zxl"));
							_source.set("cdl",opportunity.getLong("cdl"));
							if(Maxzxl<opportunity.getLong("zxl")){
								Maxzxl = opportunity.getLong("zxl");
							}
							if(Maxcdl<opportunity.getLong("cdl")){
								Maxcdl = opportunity.getLong("cdl");
							}
							break;
						}
					}else{
						continue;
					}
				}
				zxldataStrB.get(i).append(","+_source.getLong("zxl"));
				cdldataStrB.get(i).append(","+_source.getLong("cdl"));
				_sourceList.add(_source);
			}
			i++;
			monthmap.put(month, _sourceList);
			yuefenStrB.append(",'"+month+"-01'");
			yuefenList.add(month);
		}
		List<String> zxldata = new ArrayList<String>();
		List<String> cdldata = new ArrayList<String>();
		for(int j=0;j<monthList.size();j++){
			zxldata.add(zxldataStrB.get(j).toString().replaceFirst(",",""));
			cdldata.add(cdldataStrB.get(j).toString().replaceFirst(",",""));
		}
		String yuefenStr = yuefenStrB.toString();
		yuefenStr = yuefenStr.replaceFirst(",","");
		StringBuffer optionStrB = new StringBuffer();
		
		optionStrB.append("{timeline:{data:["+yuefenStr+"],");
		if(flag){
			optionStrB.append("currentIndex:"+(monthList.size()-1)+",");
		}
		optionStrB.append("label : {formatter : function(s) { return s.slice(5, 7);}},autoPlay : false,playInterval : 1000 },options:[{title : {'text':'"+yuefenList.get(0)+"',/*'subtext':'XXXX年来源统计'*/},tooltip : {'trigger':'axis'},legend : {x:'right','data':['咨询量','成单量'],'selected':{'咨询量':true,'成单量':true}},toolbox : {'show':true,orient : 'vertical',x: 'right',y: 'center','feature':{'mark':{'show':true},'dataView':{'show':true,'readOnly':false}, 'magicType':{'show':true,'type':['line','bar','stack','tiled']},'restore':{'show':true},'saveAsImage':{'show':true}}},calculable : true,grid : {'y':80,'y2':100},xAxis : [{'type':'category','axisLabel':{'interval':0},'data':["+customerRatingname+"]}],yAxis : [{'type':'value','name':'咨询量','max':"+Maxzxl+"},{'type':'value','name':'成单量','max':"+Maxcdl+"}],series : [{'name':'咨询量','type':'bar','markLine':{ symbol : ['arrow','none'], symbolSize : [4, 2],itemStyle : {normal: {lineStyle: {color:'orange'},barBorderColor:'orange',label:{ position:'left', formatter:function(params){return Math.round(params.value);},textStyle:{color:'orange'}}}},'data':[{'type':'average','name':'平均值'}]},'data': ["+zxldata.get(0)+"]},{'name':'成单量','yAxisIndex':1,'type':'bar','data': ["+cdldata.get(0)+"]},]},");
		
		for(int x = 1; x<yuefenList.size();x++){
			optionStrB.append("{title : {'text':'"+yuefenList.get(x)+"'},series : [{'data': ["+zxldata.get(x)+"]},{'data': ["+cdldata.get(x)+"]}]},");
		}
		optionStrB.append("]}");
		setAttr("option", optionStrB);
		setAttr("monthmap", monthmap);
		render("/opportunity_census/customerrating_census.jsp");
	}
	
	public void  layerCensus(){
		String code = getPara("code");
		String yuefen = getPara("yuefen");
		String message = getPara("message");
		List <Record> mingxi = opportunityCensusService.getmingxi(code,yuefen,message);
		for (Record r : mingxi) {
			String sids = r.getStr("subjectids");
			r.set("subjectnames", Subject.dao.getSubjectNameByIds(sids));
			r.set("feedbacktimes", Feedback.dao.getFeedbackTimes(r.getInt("id")));
		}
		setAttr("mingxi", mingxi);
		render("/opportunity_census/tongji_mingxi.jsp");
	}
	
}
