package com.momathink.teaching.reservation.service;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.momathink.common.base.BaseService;
import com.momathink.common.base.SplitPage;
import com.momathink.sys.operator.model.Role;
import com.momathink.sys.system.model.SysUser;

public class ReservationService extends BaseService{
	//private static Logger log = Logger.getLogger(ReservationService.class);		
	/**
	 * 学生列表分页
	 */
	public void queryMyStudents(SplitPage splitPage,Integer sysuserId) {
		 List<Object> paramValue = new ArrayList<Object>();
		if(sysuserId!=null){
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			StringBuffer select = new StringBuffer("SELECT r.id, s.REAL_NAME studentname,t.REAL_NAME teachername,"
					+ " c.CAMPUS_NAME ,tr.RANK_NAME,cr.NAME classroomname,s.state,r.state isstate,r.reservationtime,r.type,u.real_name username " );
			StringBuffer formSql = 
					new StringBuffer(" FROM "
								+ " reservation r "
								+ " LEFT JOIN account s on r.studentid = s.Id "
								+ " LEFT JOIN account t on r.teacherid = t.id "
								+ " LEFT JOIN campus  c on r.campusid = c.Id  "
								+ " LEFT JOIN classroom cr on r.roomid = cr.Id "
								+ " left join account u on  r.sysuerid = u.id "
								+ " LEFT JOIN time_rank tr on r.timerankid = tr.Id "
								+ " where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0 "
								+ " and LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', t.roleids) ) > 0 ");
			if(Role.isTeacher(sysuser.getStr("roleids"))){
				formSql.append(" and t.id= ").append(sysuserId);
			}else if(Role.isStudent(sysuser.getStr("roleids"))){
				formSql.append(" and s.id= ").append(sysuserId);
			}
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "state":
					formSql.append(" and s.state = ?");
					paramValue.add(Integer.parseInt(value));
					break;
				case "studentname":
					formSql.append(" and s.real_name like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "kcuserid":
					formSql.append(" and CONCAT(',',cc.kcgwids,',') like ? ");
					paramValue.add("%,"+value+",%");
					break;
				default:
					break;
				}
			}
			formSql.append(" ORDER BY s.id DESC");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			splitPage.setPage(page);
		}
	}
	public List<Record> queryMyStudentsToExcel(SplitPage splitPage,Integer sysuserId) {
		 List<Object> paramValue = new ArrayList<Object>();
		if(sysuserId!=null){
			SysUser sysuser = SysUser.dao.findById(sysuserId);
			StringBuffer select = new StringBuffer("SELECT s.REAL_NAME studentname,s.sex,t.REAL_NAME teachername,"
					+ " r.reservationtime,ts.* " );
			StringBuffer formSql = 
					new StringBuffer(" FROM "
								+ " reservation r "
								+ " left join testscorse ts on r.id = ts.reservationid"
								+ " LEFT JOIN account s on r.studentid = s.Id "
								+ " LEFT JOIN account t on r.teacherid = t.id "
								+ " LEFT JOIN campus  c on r.campusid = c.Id  "
								+ " LEFT JOIN classroom cr on r.roomid = cr.Id "
								+ " left join account u on  r.sysuerid = u.id "
								+ " LEFT JOIN time_rank tr on r.timerankid = tr.Id "
								+ " where LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', s.roleids) ) > 0 "
								+ " and LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'teachers'), CONCAT(',', t.roleids) ) > 0 ");
			if(Role.isTeacher(sysuser.getStr("roleids"))){
				formSql.append(" and t.id= ").append(sysuserId);
			}else if(Role.isStudent(sysuser.getStr("roleids"))){
				formSql.append(" and s.id= ").append(sysuserId);
			}
			Map<String,String> queryParam = splitPage.getQueryParam();
			Set<String> paramKeySet = queryParam.keySet();
			for (String paramKey : paramKeySet) {
				String value = queryParam.get(paramKey);
				switch (paramKey) {
				case "state":
					formSql.append(" and s.state = ?");
					paramValue.add(Integer.parseInt(value));
					break;
				case "studentname":
					formSql.append(" and s.real_name like ? ");
					paramValue.add("%" + value + "%");
					break;
				case "kcuserid":
					formSql.append(" and CONCAT(',',cc.kcgwids,',') like ? ");
					paramValue.add("%,"+value+",%");
					break;
				default:
					break;
				}
			}
			formSql.append(" ORDER BY s.id DESC");
			Page<Record> page = Db.paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select.toString(), formSql.toString(), paramValue.toArray());
			List<Record> list = page.getList();
			splitPage.setPage(page);
			return list;
		}
		return null;
	}

	/**
	 * 导出格式
	 * @param response
	 * @param request
	 * @param list
	 * @param filename
	 */
	public void export(HttpServletResponse response, HttpServletRequest request, List<Record> list, String filenames) {
		//Excel文件
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook();  
				//单元格换行
				HSSFCellStyle cellStyle=hssfWorkbook.createCellStyle();
				cellStyle.setWrapText(true);
			
				evaluateScoreDetail(hssfWorkbook,list);
				
		        try {
					String filename = filenames+".xls";
					filename = URLEncoder.encode(filename, "UTF-8");

					response.reset();
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-Disposition", "attachment; filename="+filename);
					response.setContentType("application/octet-stream;charset=UTF-8");
		        	
					OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
					hssfWorkbook.write(outputStream);
					outputStream.flush();
					outputStream.close();
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
	}
public void evaluateScoreDetail(HSSFWorkbook hssfWorkbook,List<Record> list){
		//样式
	  HSSFFont font = hssfWorkbook.createFont();   
	     font.setFontName("微软雅黑");   
	     font.setFontHeightInPoints((short) 10);   
	     // 普通单元格样式   
	     HSSFCellStyle style = hssfWorkbook.createCellStyle();   
	     style.setFont(font);   
	     style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中   
	     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
	     style.setWrapText(true);   
	     style.setLeftBorderColor(HSSFColor.BLACK.index);   
	     style.setBorderLeft((short) 1);   
	     style.setRightBorderColor(HSSFColor.BLACK.index);   
	     style.setBorderRight((short) 1);   
	     style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体   
	     style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．   
	     style.setFillForegroundColor(HSSFColor.WHITE.index);
	     //表单
		HSSFSheet hssfSheet = hssfWorkbook.createSheet("数据信息");  
		HSSFRow row;
		HSSFCell cell ;
		for(int i=0;i<list.size()+3;i++){
			row = hssfSheet.createRow(i) ;
			for(int j=0;j<18;j++){
				cell = row.createCell(j);
				 cell.setCellStyle(style);
			}
		}
		hssfSheet.addMergedRegion(new CellRangeAddress(0,2,0,0));
		cell = hssfSheet.getRow(0).getCell(0);
		cell.setCellValue("NO"); 
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,2,1,1));
		cell = hssfSheet.getRow(0).getCell(1);
		cell.setCellValue("Name"); 
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,2,2,2));
		cell = hssfSheet.getRow(0).getCell(2);
		cell.setCellValue("Age"); 
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,2,3,3));
		cell = hssfSheet.getRow(0).getCell(3);
		cell.setCellValue("Gender"); 
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,2,4,4));
		cell = hssfSheet.getRow(0).getCell(4);
		cell.setCellValue("Assessment Date"); 
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,2,5,5));
		cell = hssfSheet.getRow(0).getCell(5);
		cell.setCellValue("Assessed By"); 
		
		
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,0,6,11));
		cell = hssfSheet.getRow(0).getCell(6);
		cell.setCellValue("Subject Area"); 
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,6,6));
		cell = hssfSheet.getRow(1).getCell(6);
		cell.setCellValue("S&L"); 
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,6,6));
		cell = hssfSheet.getRow(2).getCell(6);
		cell.setCellValue("Total: 60"); 
		
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,7,7));
		cell = hssfSheet.getRow(1).getCell(7);
		cell.setCellValue("R&W");
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,7,7));
		cell = hssfSheet.getRow(2).getCell(7);
		cell.setCellValue("Total: 25");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,8,8));
		cell = hssfSheet.getRow(1).getCell(8);
		cell.setCellValue("Self-Awareness"); 
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,8,8));
		cell = hssfSheet.getRow(2).getCell(8);
		cell.setCellValue("Total: 15");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,9,9));
		cell = hssfSheet.getRow(1).getCell(9);
		cell.setCellValue("Culture Awareness"); 
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,9,9));
		cell = hssfSheet.getRow(2).getCell(9);
		cell.setCellValue("Total: 25");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,10,10));
		cell = hssfSheet.getRow(1).getCell(10);
		cell.setCellValue("Critical Thinking"); 
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,10,10));
		cell = hssfSheet.getRow(2).getCell(10);
		cell.setCellValue("Total: 20");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,11,11));
		cell = hssfSheet.getRow(1).getCell(11);
		cell.setCellValue("Creative Thinking"); 
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,11,11));
		cell = hssfSheet.getRow(2).getCell(11);
		cell.setCellValue("Total: 16");
		
		
		
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0,0,12,17));
		cell = hssfSheet.getRow(0).getCell(12);
		cell.setCellValue("Recommendations");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,12,13));
		cell = hssfSheet.getRow(1).getCell(12);
		cell.setCellValue("Speaking & Listening");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,12,12));
		cell = hssfSheet.getRow(2).getCell(12);
		cell.setCellValue("Course");
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,13,13));
		cell = hssfSheet.getRow(2).getCell(13);
		cell.setCellValue("Goal");
		
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,14,15));
		cell = hssfSheet.getRow(1).getCell(14);
		cell.setCellValue("Reading & Writing");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,14,14));
		cell = hssfSheet.getRow(2).getCell(14);
		cell.setCellValue("Course");
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,15,15));
		cell = hssfSheet.getRow(2).getCell(15);
		cell.setCellValue("Goal");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(1,1,16,17));
		cell = hssfSheet.getRow(1).getCell(16);
		cell.setCellValue("Elective");
		
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,16,16));
		cell = hssfSheet.getRow(2).getCell(16);
		cell.setCellValue("Course");
		hssfSheet.addMergedRegion(new CellRangeAddress(2,2,17,17));
		cell = hssfSheet.getRow(2).getCell(17);
		cell.setCellValue("Goal");
		
		
		
		
		if(list.size()>0){
			int colcount = 3 ;
			for(int i=0;i<list.size();i++){
				cell = hssfSheet.getRow(colcount).getCell(0);
				cell.setCellValue(i+1);
				
				cell = hssfSheet.getRow(colcount).getCell(1);
				cell.setCellValue(list.get(i).get("studentname")!=null?list.get(i).getStr("studentname"):"");
				
				cell = hssfSheet.getRow(colcount).getCell(2);
				cell.setCellValue(list.get(i).get("age")!=null?list.get(i).getInt("age").toString():"");
				
				cell = hssfSheet.getRow(colcount).getCell(3);
				cell.setCellValue(list.get(i).getInt("sex")==0?"女":"男");
				
				cell = hssfSheet.getRow(colcount).getCell(4);
				cell.setCellValue(list.get(i).get("reservationtime")!=null?list.get(i).getDate("reservationtime").toString():"");
				
				
				cell = hssfSheet.getRow(colcount).getCell(5);
				cell.setCellValue(list.get(i).get("teachername")!=null?list.get(i).getStr("teachername"):"");
				
				/*----------*/
				cell = hssfSheet.getRow(colcount).getCell(6);
				cell.setCellValue(list.get(i).get("scorse1")!=null?list.get(i).getDouble("scorse1").toString():"0");
				
				cell = hssfSheet.getRow(colcount).getCell(7);
				cell.setCellValue(list.get(i).get("scorse2")!=null?list.get(i).getDouble("scorse2").toString():"0");
				
				cell = hssfSheet.getRow(colcount).getCell(8);
				cell.setCellValue(list.get(i).get("scorse3")!=null?list.get(i).getDouble("scorse3").toString():"0");
				
				cell = hssfSheet.getRow(colcount).getCell(9);
				cell.setCellValue(list.get(i).get("scorse4")!=null?list.get(i).getDouble("scorse4").toString():"0");
				
				cell = hssfSheet.getRow(colcount).getCell(10);
				cell.setCellValue(list.get(i).get("scorse5")!=null?list.get(i).getDouble("scorse5").toString():"0");
				
				cell = hssfSheet.getRow(colcount).getCell(11);
				cell.setCellValue(list.get(i).get("scorse6")!=null?list.get(i).getDouble("scorse6").toString():"0");
				
				
				cell = hssfSheet.getRow(colcount).getCell(12);
				cell.setCellValue(list.get(i).get("course1")!=null?list.get(i).getStr("course1"):"");
				
				cell = hssfSheet.getRow(colcount).getCell(13);
				cell.setCellValue(list.get(i).get("goal1")!=null?list.get(i).getStr("goal1"):"");
				
				cell = hssfSheet.getRow(colcount).getCell(14);
				cell.setCellValue(list.get(i).get("course2")!=null?list.get(i).getStr("course2"):"");
				
				cell = hssfSheet.getRow(colcount).getCell(15);
				cell.setCellValue(list.get(i).get("goal2")!=null?list.get(i).getStr("goal2"):"");
				
				cell = hssfSheet.getRow(colcount).getCell(16);
				cell.setCellValue(list.get(i).get("course3")!=null?list.get(i).getStr("course2"):"");
				
				cell = hssfSheet.getRow(colcount).getCell(17);
				cell.setCellValue(list.get(i).get("goal3")!=null?list.get(i).getStr("goal3"):"");
				
				colcount++;
			}
		}
		
	}
}
