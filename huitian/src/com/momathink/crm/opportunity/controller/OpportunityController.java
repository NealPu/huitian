package com.momathink.crm.opportunity.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.MesContantsFinal;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.common.tools.ToolMD5;
import com.momathink.common.tools.ToolString;
import com.momathink.crm.mediator.model.Mediator;
import com.momathink.crm.mediator.model.Organization;
import com.momathink.crm.opportunity.model.CrmSource;
import com.momathink.crm.opportunity.model.Feedback;
import com.momathink.crm.opportunity.model.Opportunity;
import com.momathink.crm.opportunity.service.OpportunityService;
import com.momathink.finance.model.CourseOrder;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.sms.service.MessageService;
import com.momathink.sys.system.model.SysUser;
import com.momathink.teaching.campus.model.Campus;
import com.momathink.teaching.course.model.Course;
import com.momathink.teaching.student.model.Student;
import com.momathink.teaching.student.model.StudentKcgw;
import com.momathink.teaching.student.service.StudentService;
import com.momathink.teaching.subject.model.Subject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * 销售机会
 * @author David
 *
 */
@Controller(controllerKey = "/opportunity")
public class OpportunityController extends BaseController {

	private static Logger log = Logger.getLogger(OpportunityController.class);

	private OpportunityService opportunityService = new OpportunityService();
	private StudentService studentService = new StudentService();
	/**
	 * 销售机会列表
	 */
	@SuppressWarnings("unchecked")
	public void index() {
		log.debug("销售机会：分页");
		List<SysUser> sysUserList = SysUser.dao.getSysUser();
		List<Mediator> list;
		SysUser sysuser = SysUser.dao.findById(getSysuserId());
		if(Role.isShichang(sysuser.getStr("roleids"))){
			list = Mediator.dao.findMediatorIdAndName(sysuser.getInt("Id").toString());
		}else{
			list = Mediator.dao.findMediatorIdAndName(null);
		}
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("userid", getSysuserId().toString());
		opportunityService.list(splitPage);
		setAttr("mediatorList", list);
		setAttr("sourceList",CrmSource.dao.getAllcrmSource());
		setAttr("sysUserList", sysUserList);
		setAttr("showPages", splitPage.getPage());
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> olist = page.getList();
		for (Record r : olist) {
			Opportunity co = Opportunity.dao.findById(r.getInt("id"));
			co.set("isread", 1);
			co.update();
		}
		setAttr("roles",Role.dao.getAllRole());
		render("/opportunity/opportunity_list.jsp");
	}
	
	public void toImportPage() {
		setAttr("campusList", Campus.dao.getCampus());
		renderJsp("/opportunity/import_tch.jsp");
	}
	
	public void downLoad() {

		String path = PathKit.getWebRootPath() + File.separator + "opportunity" + File.separator + "import_temp.xls";
		renderFile(new File(path));
	}
	
	public void importTch() throws MySQLIntegrityConstraintViolationException, IOException{
		// 获取文件
		String msg = "导入成功！";
		try {
			
			UploadFile file = getFile("fileField");
			String path = file.getSaveDirectory() + file.getFileName();
			String campusid = getPara("campus");
			// 处理导入数据
			Map<String,Object> flagList = dealDataByPath(path);
			boolean flag = (boolean) flagList.get("flag");
			if(!flag){
				msg = "上传失败,请使用提供模版上传.";
			}else{
				@SuppressWarnings("unchecked")
				List<Map<Integer, String>> list = (List<Map<Integer, String>>) flagList.get("list"); // 分析EXCEL数据

			for (Map<Integer, String> map : list) { // 遍历取出的数据，并保存
				Opportunity opportunity = new Opportunity();
				opportunity.put("contacter", map.get(0));
				opportunity.put("sex", map.get(1).equals("男") ? "1" : "0");
				opportunity.put("phonenumber", map.get(2));
				opportunity.put("campusid", campusid);
				opportunity.save();
			}
			}
		} catch (ActiveRecordException e) {
			msg = "文件中个别老师与系统中老师重复,导入失败";
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg = "导入失败";
		}
		// 返回结果
		setAttr("msg", msg);
		render("/opportunity/import_info.jsp");
		
	}
	
	private Map<String,Object> dealDataByPath(String path) {
		Map<String,Object> flagList = new HashMap<String,Object>();
		
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		// 工作簿
		HSSFWorkbook hwb = null;
		try {
			hwb = new HSSFWorkbook(new FileInputStream(new File(path)));

			HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
			
			boolean flag = isProvidedModel(sheet.getRow(0));
			if(!flag){
				flagList.put("flag", false);
				flagList.put("list", list);
				return flagList;
			}else{
				flagList.put("flag", true);
			}

			for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {// 第二行开始取值，第一行为标题行

				HSSFRow row = sheet.getRow(i); // 获取到第i列的行数据(表格行)

				Map<Integer, String> map = new HashMap<Integer, String>();

				for (int j = 0; j < row.getLastCellNum(); j++) {

					HSSFCell cell = row.getCell(j); // 获取到第j行的数据(单元格)

					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					map.put(j, cell.getStringCellValue());
				}

				list.add(map);
			}
			flagList.put("list", list);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return flagList;
	}
	
	public boolean isProvidedModel(HSSFRow titleRow){
		//姓名
		HSSFCell cellName = titleRow.getCell(0);
		cellName.setCellType(HSSFCell.CELL_TYPE_STRING);
		String realname = cellName.getStringCellValue();
		if(!realname.equals("姓名")){
			return false;
		}
		//性别
		HSSFCell cellSex = titleRow.getCell(1);
		cellSex.setCellType(HSSFCell.CELL_TYPE_STRING);
		String sex = cellSex.getStringCellValue();
		if(!sex.equals("性别")){
			return false;
		}
		//联系电话
		HSSFCell cellTel = titleRow.getCell(2);
		cellTel.setCellType(HSSFCell.CELL_TYPE_STRING);
		String tel = cellTel.getStringCellValue();
		if(!tel.equals("联系电话")){
			return false;
		}
		
		return true;
	}
	
	/**
	 *  销售分配*
	 */
	public void allot(){
		String date1 = getPara("date1");
		String date2 = getPara("date2");
		String date3 = getPara("date3");
		setAttr("date1", date1);
		setAttr("date2", date2);
		setAttr("date3", date3);
		setAttr("roles",Role.dao.getAllRole());
		List<Mediator> list;
		SysUser sysuser = SysUser.dao.findById(getSysuserId());
		if(Role.isShichang(sysuser.getStr("roleids"))){
			list = Mediator.dao.findMediatorIdAndName(sysuser.getInt("Id").toString());
		}else{
			list = Mediator.dao.findMediatorIdAndName(null);
		}
		Map<String,String> queryParam = splitPage.getQueryParam();
		if(!StringUtils.isEmpty(date1)) queryParam.put("startday", date1);
		if(!StringUtils.isEmpty(date2)) queryParam.put("endday", date2);
		if(!StringUtils.isEmpty(date3)) queryParam.put("dayday", date3);
		opportunityService.allotList(splitPage,getSysuserId().toString());
		setAttr("showPages", splitPage.getPage());
		setAttr("mediatorList", list);
		setAttr("sourceList",CrmSource.dao.getAllcrmSource());
		setAttr("subjectlist",Subject.dao.getSubject());
//		String campuskcids = Campus.dao.IsCampusKcFzr(getSysuserId());
//		setAttr("campuslist",Campus.dao.getCampusIds(campuskcids));
		setAttr("campuslist",Campus.dao.getCampus());
		render("/opportunity/opportunity_allot.jsp");
	}
	/**
	 * 我的机会
	 */
	public void myAllot(){
		try{
			String date1 = getPara("date1");
			String date2 = getPara("date2");
			String date3 = getPara("date3");
			setAttr("date1", date1);
			setAttr("date2", date2);
			setAttr("date3", date3);
			setAttr("roles",Role.dao.getAllRole());
			List<Mediator> list;
			SysUser sysuser = SysUser.dao.findById(getSysuserId());
			if(Role.isShichang(sysuser.getStr("roleids"))){
				list = Mediator.dao.findMediatorIdAndName(sysuser.getInt("Id").toString());
			}else{
				list = Mediator.dao.findMediatorIdAndName(null);
			}
			Map<String,String> queryParam = splitPage.getQueryParam();
			if(!StringUtils.isEmpty(date1)) queryParam.put("startday", date1);
			if(!StringUtils.isEmpty(date2)) queryParam.put("endday", date2);
			if(!StringUtils.isEmpty(date3)) queryParam.put("dayday", date3);
			queryParam.put("myvallot", getSysuserId().toString());
			opportunityService.allotList(splitPage,getSysuserId().toString());
			setAttr("showPages", splitPage.getPage());
			setAttr("mediatorList", list);
			setAttr("sourceList",CrmSource.dao.getAllcrmSource());
			setAttr("subjectlist",Subject.dao.getSubject());
			setAttr("campuslist",Campus.dao.getCampus());
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				switch (paramKey) {
				case "myvallot":
					@SuppressWarnings("unchecked")
					Page<Record> page = (Page<Record>)splitPage.getPage();
					List<Record> olist = page.getList();
					for (Record r : olist) {
						Integer oppid = r.getInt("Id");
						long count = Feedback.dao.queryBackTimes(oppid,getSysuserId());
						r.set("backtimes",count);
					}
					break;
				default:
					break;
				}
			}
			render("/opportunity/opportunity_myvallot.jsp");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void toAllot(){
		String[] oppids = getParaValues("oppIdValue");
		StringBuffer sb = new StringBuffer("");
		if(oppids.length>0){
			for(String str:oppids){
				sb.append(str+"|");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		setAttr("oppids",sb.toString());
		System.out.println(sb.toString().replace(",", "|"));
		setAttr("opp",sb.toString().replace(",", "|"));
		List<Record> wuxiaolist = opportunityService.getwuxiaojihui();
		String campuskcids = Campus.dao.IsCampusKcFzr(getSysuserId());
		List<SysUser> kcgws = SysUser.dao.getKechengguwen(campuskcids);
		for(SysUser user:kcgws){
			for(Record wuxiao : wuxiaolist){
				Long shu = wuxiao.getLong("shu");
				if(shu!=null && wuxiao.getInt("kcuserid")!=null){
					int kcuserid = wuxiao.getInt("kcuserid");
				    int id =  user.getInt("Id");
					if(kcuserid==id){
					user.put("shu", shu);
					}
				}
			}
		}
		setAttr("kcgws",kcgws);
		renderJsp("/opportunity/layer_toallot.jsp");
//		renderJsp("/opportunity/opportunity_fenpei.jsp");
	}
	/**
	 * 分配
	 */
	public void allotSave(){
		String oppids = getPara("oppids");//机会id
		String[] kcgwsids = getPara("kcgwsids").split(",");
		String[] opp = oppids.split(",");
		boolean flag = false;
		if(opp.length>0){
			String[] temp = kcgwsids;
			for(int j=0;j<opp.length;j++){
				if(temp.length==0){
					temp = kcgwsids;
				}
				if(temp.length>0){
					int index = (int)(Math.random() * temp.length);
					flag =  opportunityService.setKcgwToOpp(temp[index],opp[j]);
					if(flag){
						String[] temps = new String[temp.length-1];
						circle:for(int i=0;i<temp.length;i++){
							if(i==index){
								for(int k=index;k<temp.length-1;k++){
									temps[k] = temp[k+1];
								}
								break circle;
							}else{
								temps[i] = temps[i];
							}
						}
						temp = temps;
					}else{
						break ;
					}
				}
			}
		}
		JSONObject json = new JSONObject();
		if(flag){
			json.put("code", "1");
			json.put("msg", "分配成功");
		}else{
			json.put("code", "0");
			json.put("msg", "分配失败");
		}
		renderJson(json);
	}
	
	/**
	 * 转销售池
	 */
	public void delVallot(){
		JSONObject json = new JSONObject();
		boolean flag = false;
		String[] oppids = getPara("oppids").split(",");
		if(oppids.length>0){
			for(int i=0;i<oppids.length;i++){
				flag =  opportunityService.delOppVallot(oppids[i]);
				if(!flag){
					break;
				}
			}
		}
		if(flag){
			json.put("code", "1");
			json.put("msg", "转出成功");
		}else{
			json.put("code", "0");
			json.put("msg", "取消失败");
		}
		renderJson(json);
	}
	
	public void setRebackTime(){
		renderJsp("/opportunity/layer_setrebacktime.jsp");
	}
	
	public void sureRebackTime(){
		JSONObject json = new JSONObject();
		String[] oppids = getPara("oppids").split(",");
		String time = getPara("time");
		boolean flag = false;
		if(oppids.length>0){
			for(int i=0;i<oppids.length;i++){
				flag =  opportunityService.sureRebackTime(oppids[i],time);
				if(!flag){
					break;
				}
			}
		}
		if(flag){
			json.put("code", "1");
			json.put("msg", "取消成功");
		}else{
			json.put("code", "0");
			json.put("msg", "取消失败");
		}
		renderJson(json);
	}
	
	public void writeFeedBack(){
		String[] id = getPara().split(",");
		Opportunity opp = Opportunity.dao.findById(id[0]);
		setAttr("time",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		long count = Feedback.dao.queryBackTimes(Integer.parseInt(id[0]),getSysuserId());
		setAttr("opportunity",opp);
		setAttr("code",count);
		setAttr("num",id[1]);
		renderJsp("/opportunity/layer_writenewfeedback.jsp");
	}
	
	public void toSaveFeedBack(){
		Opportunity opp = getModel(Opportunity.class);
		Feedback fb = getModel(Feedback.class);
		String time = getPara("time");
		fb.set("createtime", time+" 00:00:00");
		fb.set("userid", getSysuserId());
		String content = getPara("feedback.content");
		if(StringUtils.isEmpty(content)){
			fb.set("content", " ");
		}
		fb.save();
		String nexttime = getPara("nexttime");
		Integer result = fb.getInt("callresult");
		Integer oppiscover = result==0?0:result==1?1:result==2?2:result==3?3:result==4?4:result==5?5:6;
		if(!StringUtils.isEmpty(time)){
			opp.set("lastvisittime", time);
		}
		if(!StringUtils.isEmpty(nexttime)){
			opp.set("nextvisit", nexttime);
		}
		opp.set("isconver",oppiscover);
		if(oppiscover==1){
			opp.set("isconver", 1);
			opp.set("status", 0);
			opp.set("overtime", ToolDateTime.getDate());
			opp.set("convertime", ToolDateTime.getDate());
			opp.set("confirmuserid", getSysuserId());
		}
		opp.update();
		JSONObject json = new JSONObject();
		json.put("code", 1);
		renderJson(json);
	}
	
	
	public void feedBackRecord(){
		String oppid = getPara();
		List<Feedback> fblist = Feedback.dao.queryBackRecord(oppid, getSysuserId());
		if(fblist.isEmpty()){
			setAttr("code","1");
		}else{
			setAttr("code","2");
			setAttr("fblist",fblist);
		}
		renderJsp("/opportunity/layer_feedBackRecord.jsp");
	}
	

	/**
	 * 保存销售机会
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	
	public void save() {
		JSONObject json = new JSONObject();
		String code =getPara("opportunitycode");
		Opportunity opportunity = getModel(Opportunity.class);
		Integer isconver = opportunity.getInt("isconver");
		String mediatorname = getPara("mediatorname");
		if(!mediatorname.equals("")){
			Mediator m = Mediator.dao.findByName(mediatorname.trim());
			if(m!=null){
				opportunity.set("mediatorid", m.getInt("id"));
			}else{
				Mediator newm = new Mediator();
				newm.set("realname",mediatorname).set("sysuserid",opportunity.get("scuserid")).set("createtime", new Date()).save();
				opportunity.set("mediatorid",newm.getPrimaryKeyValue());
			}
		}
		if(code.equals("1")){
			opportunity.set("kcuserid",getSysuserId());
			opportunity.set("isconver", 0);
		}
				//	}
		if(opportunity.getInt("kcuserid")!=null){
			if(!StringUtils.isEmpty(opportunity.getInt("kcuserid").toString())){
				opportunity.set("vallottime", new Date());
			}
		}
		opportunityService.save(opportunity);
		//获取学生信息并保存
		Integer oppid = opportunity.getPrimaryKeyValue();
		Integer relation = Integer.parseInt(getPara("opportunity.relation"));
		String pname = getPara("opportunity.contacter");
		String ptel = getPara("opportunity.phonenumber");
		String pemail = getPara("opportunity.email");
		String name = getPara("studentname")==""?null:getPara("studentname");
		String tel=getPara("tels")==""?null:getPara("tels");
		String email = getPara("emails")==""?null:getPara("emails");
		String sex = getPara("studentsex");
		String birth = getPara("birth")==""?null:getPara("birth");
		String idtype =getPara("idtype")==""?null:getPara("idtype");
		String idnumber = getPara("idnumber")==""?null:getPara("idnumber");
		String qq = getPara("qq")==""?null:getPara("qq");
		String skype=getPara("skype")==""?null:getPara("skype");
		String wechat=getPara("wechat")==""?null:getPara("wechat");
		String nationality=getPara("nationality")==""?null:getPara("nationality");
		String naticeplace=getPara("naticeplace")==""?null:getPara("naticeplace");
		String address=getPara("address")==""?null:getPara("address");
		String school = getPara("school")==""?null:getPara("school");
		String grade = getPara("grade")==""?null:getPara("grade");
		String teacher = getPara("teacher");
		String teachertel =getPara("teachertel");
		String englishteacher = getPara("englishteacher");
		String englishteachertel=getPara("englishteachertel");
		String isshowMessage = getPara("isshowMessage");
		if(isshowMessage.equals("1")){
			Student student = new Student();
			/*判断是否的本人   或者 家人   并保存其信息*/
			if(relation==2 || relation==3){
				student.set("prname",pname).set("PARENTS_TEL",ptel).set("PARENTS_EMAIL",pemail);
			}
			Role role = Role.dao.getRoleByNumbers("student");
			student.set("roleids", role.getInt("id")+",");
			student.set("user_pwd", ToolMD5.getMD5("111111"));
			student.set("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			student.set("real_name",name).set("tel",tel).set("email",email);
			student.set("sex",sex).set("BIRTHDAY",birth).set("classteacher",teacher).set("classteachertel",teachertel).set("englishteacher",englishteacher).set("englishteachertel",englishteachertel);
			student.set("zjtype",idtype).set("zjnumber", idnumber).set("qq",qq).set("skype", skype).set("wechat", wechat);
			student.set("nationality", nationality).set("address",naticeplace).set("stuaddress",address).set("school",school);
			student.set("grade_name",grade).set("opportunityid",oppid).set("USER_TYPE",1);
			if(isconver==1){
				student.set("state",0);
				student.set("campusid",opportunity.getInt("campusid"));
			}else{
				student.set("state",3);
			}
			student.save();
			if(opportunity.get("kcuserid")!=null&&opportunity.get("kcuserid")!=""){
				StudentKcgw sk = new StudentKcgw();
				sk.set("student_id",student.getPrimaryKeyValue()).set("kcgw_id",opportunity.getInt("kcuserid")).save();
			}
		}
		// 发短信给课程顾问
		MessageService.sendMessageToAdvisor(MesContantsFinal.kc_sms_tuijian,MesContantsFinal.kc_email_tuijian, opportunity.getPrimaryKeyValue().toString());
		// 发短信给留学顾问
		Mediator mediator = Mediator.dao.findById(opportunity.getInt("mediatorid"));
		if(opportunity.getInt("source")==1||opportunity.getInt("source")==6){
			if(mediator!=null&&opportunity.getInt("kcuserid")==null)
			MessageService.sendMessageToMediator(MesContantsFinal.cs_sms_tuijian, MesContantsFinal.cs_email_tuijian,opportunity.getPrimaryKeyValue().toString(),null,null,null);
		}
		json.put("code", 1);
		json.put("msg", "添加成功");
		/*}else{
		json.put("code", 0);
		json.put("msg", "咨询的科目不能为空");
	}*/
	renderJson(json);
	}

	/**
	 * 添加销售机会
	 */
	
	
	public void addOpportunity() {
//		String code = getPara();
//		if(code.equals("1")){
//			Record record = getSessionAttr("account_session");
//			if(!Role.isShichang(record.getStr("roleids"))){//&&不是市场负责人
//				setAttr("code",code);
//			}else{
//				setAttr("code","3");
//			}
//		}
		Opportunity opportunity = new Opportunity();
		Record record = getSessionAttr("account_session");
		if(Role.isShichang(record.getStr("roleids"))){//&&不是市场负责人
//			opportunity.put("campusid", record.getStr("campusid"));
			opportunity.put("scuserid", record.getInt("ID"));
		}
		if(Role.isKcgwfzr(record.getStr("roleids"))){
//			opportunity.put("campusid", record.getStr("campusid"));
			opportunity.put("kcuserid", record.getInt("ID"));
		}
		setAttr("opportunity",opportunity);
		
		List<SysUser> sysUserList = SysUser.dao.getScuserid();
		List<SysUser> kcgwList = SysUser.dao.getKechengguwen(null);
		List<Subject> subjectList = Subject.dao.getSubject();
		List<Campus> campusList = Campus.dao.getCampus();
		setAttr("sourceList",CrmSource.dao.getAllcrmSource());
		setAttr("kcgwList", kcgwList);
		setAttr("campusList", campusList);
		setAttr("sysUserList", sysUserList);
		setAttr("subjectList", subjectList);
		setAttr("operatorType", "add");
		setAttr("code","3");
		render("/opportunity/opportunity_form.jsp");
	}

	/**
	 * 准备更新
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void edit() {
		String str = getPara();
		Opportunity opportunity = Opportunity.dao.findById(str);
		if(opportunity.get("mediatorid")!=null&&opportunity.getInt("mediatorid")!=0){
			Mediator m = Mediator.dao.findById(opportunity.getInt("mediatorid"));
			setAttr("mediatornames",m.getStr("realname"));
		}else{
			setAttr("mediatornames","");
		}
		List<SysUser> sysUserList = SysUser.dao.getScuserid();
		List<Subject> subjectList = Subject.dao.getSubject();
		/*注释掉的是页面checkbox形式下数据的回填信息*/
		/*String subjectIds = opportunity.getStr("subjectids");
		if(!StringUtils.isEmpty(subjectIds)){
			String subIds[] = subjectIds.split("\\|");
			for(Subject subject : subjectList){
				String _sid = subject.getPrimaryKeyValue().toString();
				for(String sid : subIds){
					if(_sid.equals(sid)){
						subject.put("selected", "selected");
					}
				}
			}
		}*/
		Student student = Student.dao.findByOppid(str);
		if(student != null){
			setAttr("student",student);
			setAttr("nums",1);
		}else{
			Student s = Student.dao.getFistStudentByOppid(str);
			if(s!=null){
				setAttr("student",s);
				setAttr("nums",2);
			}else{
				setAttr("student",null);
				setAttr("nums",3);
			}
		}
		List<Campus> campusList = Campus.dao.getCampus();
		setAttr("sourceList",CrmSource.dao.getAllcrmSource());
		setAttr("campusList", campusList);
		setAttr("subjectList", subjectList);
		List<SysUser> s = SysUser.dao.getKechengguwen(null);
		setAttr("kcgwList",s);
		setAttr("sysUserList", sysUserList);
		setAttr("opportunity", opportunity);
		setAttr("operatorType", "update");
		setAttr("num","1");
		render("/opportunity/opportunity_form.jsp");
	}

	/**
	 * 更新
	 * 
	 * @author David
	 * @since JDK 1.7
	 */
	public void update() {
		JSONObject json = new JSONObject();
		try{
			Opportunity opportunity = getModel(Opportunity.class);
			Integer isconver = opportunity.getInt("isconver");
			String mediatorname = getPara("mediatorname");
			if(!mediatorname.equals("")){
				Mediator m = Mediator.dao.findByName(mediatorname.trim());
				if(m!=null){
					opportunity.set("mediatorid", m.getInt("id"));
				}else{
					Mediator newm = new Mediator();
					newm.set("realname",mediatorname).set("createtime", new Date()).save();
					opportunity.set("mediatorid",newm.getPrimaryKeyValue());
				}
			}
			opportunityService.update(opportunity);
			Integer relation = Integer.parseInt(getPara("opportunity.relation"));
			String pname = getPara("opportunity.contacter");
			String ptel = getPara("opportunity.phonenumber");
			String pemail = getPara("opportunity.email");
			String name = getPara("studentname")==""?null:getPara("studentname");
			String tel=getPara("tels")==""?null:getPara("tels");
			String email = getPara("emails")==""?null:getPara("emails");
			String sex = getPara("studentsex");
			String birth = getPara("birth")==""?null:getPara("birth");
			String idtype =getPara("idtype")==""?null:getPara("idtype");
			String idnumber = getPara("idnumber")==""?null:getPara("idnumber");
			String qq = getPara("qq")==""?null:getPara("qq");
			String skype=getPara("skype")==""?null:getPara("skype");
			String wechat=getPara("wechat")==""?null:getPara("wechat");
			String nationality=getPara("nationality")==""?null:getPara("nationality");
			String naticeplace=getPara("naticeplace")==""?null:getPara("naticeplace");
			String address=getPara("address")==""?null:getPara("address");
			String school = getPara("school")==""?null:getPara("school");
			String grade = getPara("grade")==""?null:getPara("grade");
			String teacher = getPara("teacher")==""?null:getPara("teacher");
			String teachertel =getPara("teachertel")==""?null:getPara("teachertel");
			String englishteacher = getPara("englishteacher")==""?null:getPara("englishteacher");
			String englishteachertel=getPara("englishteachertel")==""?null:getPara("englishteachertel");
			String isshowMessage = getPara("isshowMessage");
			
			String id = getPara("studentid");
			Student student = null;
			if(id!=""){
				student= Student.dao.findById(id);
				/*判断是否的本人   或者 家人   并保存其信息*/
				if(relation==2 || relation==3){
					student.set("prname",pname).set("PARENTS_TEL",ptel).set("PARENTS_EMAIL",pemail);
				}
				student.set("real_name",name).set("tel",tel).set("email",email);
				student.set("sex",sex).set("BIRTHDAY",birth).set("classteacher",teacher).set("classteachertel",teachertel).set("englishteacher",englishteacher).set("englishteachertel",englishteachertel);
				student.set("zjtype",idtype).set("zjnumber", idnumber).set("qq",qq).set("skype", skype).set("wechat", wechat);
				student.set("nationality", nationality).set("address",naticeplace).set("stuaddress",address).set("school",school);
				student.set("grade_name",grade);
				if(isconver==1){
					student.set("state",0);
					student.set("campusid",opportunity.getInt("campusid"));
				}else{
					student.set("state",3);
				}
				studentService.update(student);
				if(opportunity.get("kcuserid")!=null&&opportunity.get("kcuserid")!=""){
					List<StudentKcgw> list = StudentKcgw.dao.getAllKcgwidsByStudentid(student.getPrimaryKeyValue());
					for(StudentKcgw skl:list){
						skl.delete();
					}
					StudentKcgw sk = new StudentKcgw();
					sk.set("student_id",student.getPrimaryKeyValue()).set("kcgw_id",opportunity.getInt("kcuserid")).save();
				}
			}else{
				if(isshowMessage.equals("1")){
					student=new Student();
					if(isconver==1){
						student.set("state",0);
						student.set("campusid",opportunity.getInt("campusid"));
					}else{
						student.set("state",3);
					}
					student.set("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					/*判断是否的本人   或者 家人   并保存其信息*/
					if(relation==2 || relation==3){
						student.set("prname",pname).set("PARENTS_TEL",ptel).set("PARENTS_EMAIL",pemail);
					}
					student.set("real_name",name).set("tel",tel).set("email",email).set("opportunityid",getPara("opportunity.id"));
					student.set("sex",sex).set("BIRTHDAY",birth).set("classteacher",teacher).set("classteachertel",teachertel).set("englishteacher",englishteacher).set("englishteachertel",englishteachertel);
					student.set("zjtype",idtype).set("zjnumber", idnumber).set("qq",qq).set("skype", skype).set("wechat", wechat);
					student.set("nationality", nationality).set("address",naticeplace).set("stuaddress",address).set("school",school);
					student.set("grade_name",grade).set("USER_TYPE","1");
					studentService.save(student);
					if(opportunity.get("kcuserid")!=null&&opportunity.get("kcuserid")!=""){
						StudentKcgw sk = new StudentKcgw();
						sk.set("student_id",student.getPrimaryKeyValue()).set("kcgw_id",opportunity.getInt("kcuserid")).save();
					}
				}
			}
			json.put("code", 1);
			json.put("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.put("code", 0);
			json.put("msg", "更新数据异常");
		}finally{
			renderJson(json);
		}
	}

	/**
	 * 检查是否存在
	 */
	public void checkExist() {
		String field = getPara("checkField");
		String value = getPara("checkValue");
		String opportunityId = getPara("opportunityId");
		if(!ToolString.isNull(field)&&!ToolString.isNull(value)){
			Long count = Opportunity.dao.queryOpportunityCount(field,value,opportunityId);
			renderJson("result",count);
		}else{
			renderJson("result",null);
		}
	}
	
	/**
	 * 添加销售机会
	 */
	/*public void addFeedback() {
		List<SysUser> sysUserList = SysUser.dao.getSysUser();
		List<Mediator> list = Mediator.dao.findMediatorIdAndName();
		Opportunity opportunity = Opportunity.dao.findById(getPara());
		setAttr("opportunity", opportunity);
		setAttr("mediatorList", list);
		setAttr("sysUserList", sysUserList);
		setAttr("operatorType", "add");
		render("/opportunity/opportunity_form.jsp");
	}*/
	
	/**
	 * 获取销售机会详情
	 */
	public void getDetailForJson(){
		//JSONObject json = new JSONObject();
		String opportunityId = getPara();
		Opportunity o = opportunityService.findDetailById(opportunityId);
		setAttr("o", o);
		
		List<Student> students = Student.dao.findStudentsByOppid(opportunityId);
		for(Student s : students){
			String studentid = s.getInt("id").toString();
			List<CourseOrder> co = CourseOrder.dao.getAllFeeMessage(studentid);
			s.put("courseorders", co);
			for(CourseOrder c:co){
				if(!ToolString.isNull(c.getStr("subjectids"))&&!c.getStr("subjectids").equals("0")){
					String subjectids = c.getStr("subjectids");
					if(subjectids.substring(0,1).equals("|")){
						subjectids = subjectids.substring(1);
					}
					subjectids = subjectids.replace("|", ",");
					List<Subject> sub = Subject.dao.findByIds(subjectids);
					StringBuffer sf = new StringBuffer("");
					for(Subject sb:sub){
						sf.append(sb.getStr("subject_name")).append(",");
					}
					c.put("subName", sf.deleteCharAt(sf.length()-1).toString());
				}
			}
		}
		setAttr("student",students);
		renderJsp("/opportunity/layer_showDetailOpportunity.jsp");
	}
	
	/**
	 * 保存反馈信息
	 */
	public void saveFeedback(){
		Record sysuser = getSessionAttr("account_session");
		String opportunityId = getPara("opportunityId");
		String content=getPara("content");
		Feedback feedback = new Feedback();
		feedback.set("userid", sysuser.getInt("id"));
		feedback.set("opportunityid", opportunityId);
		feedback.set("content", content);
		String mediatorid = getPara("mediatorid");
		if(!StringUtils.isEmpty(mediatorid)){
			feedback.set("mediatorid", mediatorid);
		}
		feedback.set("createtime", new Date());
		boolean result = feedback.save();
		JSONObject json = new JSONObject();
		json.put("result", result);
		renderJson(json);
	}
	
	public void feedList(){
		String opportunityId = getPara();
		Opportunity o = opportunityService.findDetailById(opportunityId);
		setAttr("opportunity", o);
		renderJsp("/opportunity/feedback_text.jsp");
	}
	
	public void tosaveFeedback(){
		Record sysuser = getSessionAttr("account_session");
		String opportunityId = getPara("opportunityId");
		String content=getPara("content");
		Feedback feedback = new Feedback();
		feedback.set("userid", sysuser.getInt("id"));
		feedback.set("opportunityid", opportunityId);
		feedback.set("content", content);
		String mediatorid = getPara("mediatorid");
		if(!StringUtils.isEmpty(mediatorid)){
			feedback.set("mediatorid", mediatorid);
		}
		feedback.set("createtime", new Date());
		boolean result = feedback.save();
		JSONObject json = new JSONObject();
		String code="1";
		String msg="反馈成功！";
		Opportunity opp = Opportunity.dao.findById(opportunityId);
		SysUser kcuser = SysUser.dao.findById(opp.getInt("kcuserid"));
		if(kcuser!=null&&result){
			MessageService.sendMessageToMediator(MesContantsFinal.cs_sms_fankui, MesContantsFinal.cs_email_fankui,opportunityId,null,null,content);
		}
		json.put("result", result);
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	/**
	 * 确认销售机会成单
	 */
	public void isConver(){
		String opportunityId = getPara("opportunityId");
		JSONObject json = new JSONObject();
		String result = "销售机会不存在！";
		String code="0";
		if(!StringUtils.isEmpty(opportunityId)){
			Opportunity opportunity = Opportunity.dao.findById(Integer.parseInt(opportunityId));
			if(opportunity!=null){
				String isconver = opportunity.getInt("isconver").toString();
				if(isconver.equals("1")){
					result="销售机会已确认成单！";
				}else{
					opportunity.set("isconver", 1);
					opportunity.set("status", 0);
					opportunity.set("overtime", ToolDateTime.getDate());
					opportunity.set("convertime", ToolDateTime.getDate());
					opportunity.set("confirmuserid", getSysuserId());
					try {
						opportunityService.update(opportunity);
						result = "销售机会确认成单成功！";
						code="1";
					} catch (Exception e) {
						e.printStackTrace();
						result = "销售机会确认成单失败！";
					}
				}
			}
		}
		json.put("msg", result);
		json.put("code", code);
		renderJson(json);
	}
	
	public void toAddStudent(){
		try{
		String opportunityId = getPara();
		List<Opportunity> opportunityList = new ArrayList<Opportunity>();
		List<Subject> subjectList = Subject.dao.getSubject();
		if(StringUtils.isEmpty(opportunityId)){
			opportunityList = Opportunity.dao.findIsConver();
		}else{
			Student account;
			Student accounts = Student.dao.findByOppid(opportunityId);
			if(accounts==null){
				account = new Student();
			}else{
				setAttr("difference","0");
				account=accounts;
				Opportunity opportunity = Opportunity.dao.findById(opportunityId);
				String ids = opportunity.get("subjectids").toString().replace("|", ",");
				List<Subject> subject = Subject.dao.findByIds(ids);
				StringBuffer str = new StringBuffer();
				for(Subject s:subject){
					str.append(s.get("SUBJECT_NAME")).append(",");
				}
				setAttr("subjectlist",str.toString().substring(0,str.toString().length()-1));
				List<Course> courselist = Course.dao.getCourseBySubjectIds(ids);
				setAttr("courselist",courselist);
			}
			Opportunity opportunity = Opportunity.dao.findById(Integer.parseInt(opportunityId));
			opportunityList.add(opportunity);
			String subids = opportunity.getStr("subjectids");
			if(!StringUtils.isEmpty(subids)){
				String subjectids[] = subids.split("\\|");
				for(Subject subject : subjectList){
					String id = subject.getPrimaryKeyValue().toString();
					for(String sid : subjectids){
						if(id.equals(sid)){
							subject.put("checked", "checked");
						}
					}
				}
			}
			if(opportunity.getInt("isconver")!=1){
				int relation = opportunity.getInt("relation");//与学生关系：1本人2母亲3父亲4其他
				if(relation == 1){
					account.set("REAL_NAME", opportunity.getStr("contacter"));
					account.set("USER_NAME", opportunity.getStr("contacter"));
					account.set("TEL", opportunity.getStr("phonenumber"));
					account.set("EMAIL", opportunity.getStr("email"));
					account.set("SEX", opportunity.getBoolean("sex"));
				}else{
					account.set("prname", opportunity.getStr("contacter"));
					account.set("PARENTS_TEL", opportunity.getStr("phonenumber"));
					account.set("PARENTS_EMAIL", opportunity.getStr("email"));
				}
			}
			if(opportunity.getInt("campusid")!=null)
				account.set("campusid", opportunity.getInt("campusid"));
			account.set("opportunityid", opportunity.getPrimaryKeyValue());
			setAttr("student",account);
		}
		List<Campus> clist = Campus.dao.getCampus();
		setAttr("campusList",clist);
		setAttr("olist",opportunityList);
		setAttr("subjects", subjectList);
		setAttr("supervisors", SysUser.dao.getDudao());
		int studentId = 0;
		setAttr("studentId", studentId);
		setAttr("roles",Role.dao.getAllRole());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		renderJsp("/student/layer_student_form.jsp");
	}
	
	/**
	 * 近期销售
	 */
	public void queryOpportunityJson(){
		try{
			Integer count = getParaToInt("count");
			List<Opportunity> list= Opportunity.dao.findByLimit(count);
			JSONObject json = new JSONObject();
			if(list!=null){
				String code="1";//0失败1成功
				json.put("code", code);
			}else{
				String code="0";//0失败1成功
				String msg = "无最近的销售机会记录！";
				json.put("msg", msg);
				json.put("code", code);
			}
			json.put("list", list);
			renderJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 来源管理列表
	 */
	public void crmcourse(){
		setAttr("sourceList",CrmSource.dao.getAllcrmSource());
		renderJsp("/opportunity/sourcelist.jsp");
	}
	
	/**
	 * 暂停或恢复来源
	 */
	public void sourceFreeze(){
		JSONObject json = new JSONObject();
		try{
			CrmSource cs = CrmSource.dao.findById(getPara("sourceId"));
			boolean flag = getPara("state").equals("1")?true:false;
			cs.set("state", flag).set("updatetime", new Date()).set("version", cs.getInt("version")+1).update();
			json.put("result", "1");
		}catch(Exception ex){
			ex.printStackTrace();
			String msg = getPara("state").equals("1")?"暂停失败":"恢复失败";
			json.put("result", msg);
		}
		renderJson(json);
	}
	
	/**
	 * 删除来源
	 */
	public void delSource(){
		JSONObject json = new JSONObject();
		try{
			Integer id = Integer.parseInt(getPara("sourceId"));
			Long count = Opportunity.dao.getOppCountBySourceId(id.toString());
			if(count==0){
				//删除
				CrmSource.dao.deleteById(id);
				json.put("code", 1);
				json.put("msg", "删除成功.");
			}else{
				//使用过了，不能删除 
				json.put("code", 2);
				json.put("msg", "有销售来自该来源，不能删除.");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			json.put("code", 0);
			json.put("msg", "删除出现问题，请联系管理员.");
		}
		renderJson(json);
		
	}
	
	
	/**
	 * 添加来源
	 */
	public void toAddCrmSource(){
		setAttr("addupdate","add");
		renderJsp("/opportunity/sourceadd.jsp");
	}
	
	/**
	 * 修改来源
	 */
	public void toUpdateCrmSource(){
		CrmSource cs = CrmSource.dao.findById(getPara());
		setAttr("source",cs);
		setAttr("addupdate","update");
		renderJsp("/opportunity/sourceadd.jsp");
	}
	
	/**
	 * 保存来源添加/修改
	 */
	public void toSaveCrmSource(){
		try{
			CrmSource source = getModel(CrmSource.class);
			if(getPara("addupdate").equals("add")){
				source.set("createtime", new Date()).save();
			}else if(getPara("addupdate").equals("update")&&source.getInt("id")!=null) {
				source.set("updatetime", new Date()).update();
			}
			redirect("/opportunity/crmcourse");	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 检查来源是否存在
	 */
	public void checkExistSource(){
		JSONObject json = new JSONObject();
		int code = 0;
		try{
			long count = CrmSource.dao.countNumByName(getPara("name"),getPara("id"));
			code = count==0?0:1;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		json.put("result", code);
		renderJson(json);
	}
	/**
	 * 常量池
	 */
	public void pool(){
		List<Record> subjectlist=Db.find("select Id,SUBJECT_NAME from subject where STATE=0");//科目
		List<Record> campulist=Db.find("select Id,campus_name from campus");//校区
		Map<String,String> queryParam = splitPage.getQueryParam();
		queryParam.put("userid", getSysuserId().toString());
		queryParam.put("querytype", "pool");
		opportunityService.list(splitPage);
		setAttr("campuslist", campulist);
		setAttr("showPages", splitPage.getPage());
		setAttr("subjectlist", subjectlist);
		@SuppressWarnings("unchecked")
		Page<Record> page = (Page<Record>) splitPage.getPage();
		List<Record> olist = page.getList();
		for (Record r : olist) {
			r.set("feedbacktimes", Feedback.dao.queryBackRecord(r.getInt("id").toString(),getSysuserId()).size());
		}
		renderJsp("/opportunity/opportunity_pool.jsp");
	}
	/**
	 * 领取
	 */
	public void lingqu(){
		JSONObject json=new JSONObject();
		String[] ids=getPara("oppids").split(",");
		String msg="领取失败！";
		String code="0";
		boolean flag = false;
		boolean flag1 = false;
		int sysid = getSysuserId();
		Organization  org = Organization.dao.findById(1);
		double weichengdan = Double.parseDouble(org.get("basic_maxsingular").toString());
		List<Record> wuxiaolist = opportunityService.getwuxiaojihuiByid(sysid);
		if(!wuxiaolist.isEmpty()){
			for(Record shu : wuxiaolist){
				if((shu.getLong("shu")+ids.length) > weichengdan){
					int kexuan = (int) (weichengdan - shu.getLong("shu"));
					if(kexuan >= 0){
						msg = "您还能领取"+kexuan+"个机会";
					}else{
						msg = "您的未成单过多，不能领取机会";
					}
				}else{
					flag1 = true;
				}
			}
		}else{
			flag1 = true;
		}
		try{
			if(flag1){
				for(int i=0;i<ids.length;i++){
					Opportunity opportunity=Opportunity.dao.findById(ids[i]);
					opportunity.set("kcuserid",getSysuserId());
					opportunity.set("isconver", 0);
					flag = opportunity.update();
					if(!flag){
						break;
					}
				}
			}
			if(flag){
				code = "1";
				msg = "领取成功";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		json.put("msg", msg);
		json.put("code",code);
		renderJson(json);
	}
	/**
	 * 查看今天需要回访的销售机会
	 */
	public void checkTodayReturnMessage(){
		try{
			Record r = getSessionAttr("account_session");
			List<Opportunity> list = Opportunity.dao.findAllTodayReturnOppMessage(r);
			for(Opportunity c:list){
				//获取回访的次数
				Integer oppid = c.getInt("Id");
				long count = Feedback.dao.queryBackTimes(oppid,getSysuserId());
				c.put("sum",count);
				//获取咨询的科目
				String ids = c.get("subjectids");
				c.put("names", Subject.dao.getSubjectNameByIds(ids));
				/*//判断当前登录的用户
				String code = (c.get("kcgwid").equals(getSysuserId()))?"0":"1";
				c.put("code", code);*/
			}
			renderJson(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
