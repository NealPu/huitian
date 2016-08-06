package com.momathink.teaching.document.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;
import com.momathink.common.annotation.controller.Controller;
import com.momathink.common.base.BaseController;
import com.momathink.common.constants.DictKeys;
import com.momathink.common.plugin.PropertiesPlugin;
import com.momathink.common.tools.ToolDateTime;
import com.momathink.teaching.document.model.UploadDocument;
import com.momathink.teaching.document.service.DocumentService;
import com.momathink.teaching.student.model.Student;

/**
 *  @author prq
 *  @time 2015年8月19日 14时19分34秒
 *  @document 文件管理
 */

@Controller(controllerKey="/document")
public class DocumentController extends BaseController {

	private static final Logger logger = Logger.getLogger(DocumentController.class);
	private DocumentService documentService = new DocumentService();
	
	/**
	 *  文档列表
	 *   /document/documentList
	 *   2015-08-19 14:20:09
	 */
	public void documentList(){
		logger.info("文档列表--/document/documentList--");
		/*Map<String,String> queryParam = splitPage.getQueryParam();
		setqueryParamMap(queryParam);*/
		documentService.documentList(splitPage);
		setAttr("showPages",splitPage.getPage());
		renderJsp("/file/file_list.jsp");
	}
	
	/** 
	 *  /document/toUploaddocument
	 *  
	 */
	public void toUploaddocument(){
		logger.info("弹窗添加文档");
		List<Student> studentlist = Student.dao.find("select * from account where  "
				+ " LOCATE( (SELECT CONCAT(',', id, ',') ids FROM pt_role  WHERE numbers = 'student'), CONCAT(',', roleids) ) > 0 and state <> 3 order by id desc ");
		setAttr("stulist",studentlist);
		renderJsp("/file/layer_fileform.jsp");
	}
	
	/** 
	 *  /document/uploadThisDocument
	 *  文档上传入库
	 */
	public void uploadThisDocument(){
		JSONObject json = new JSONObject();
		String msg = "导入成功！";
		String code = "1";
		logger.info("保存上传文档信息");
		try {
			
			String todayStr = ToolDateTime.format(ToolDateTime.getDate(), ToolDateTime.pattern_ymd).trim();
			
			File dir = new File(todayStr+File.separator);
			System.out.println(dir.length());
			UploadFile file = getFile("uploadDataField", todayStr+File.separator,(Integer)PropertiesPlugin.getParamMapValue(DictKeys.config_maxPostSize_key));
			String filename = file.getFileName();
			String path = file.getSaveDirectory() + file.getFileName();
			UploadDocument udc = getModel(UploadDocument.class);
			udc.set("documentname", getPara("documentname"));
			udc.set("studentid", StrKit.isBlank(getPara("studentid"))?null:getPara("studentid"));
			udc.set("contenttype", getPara("contenttype"));
			udc.set("filename", filename);
			udc.set("path", path);
			udc.set("uploaduser", getSysuserId());
			udc.set("uploadtime", new Date());
			udc.save();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg = "导入失败";
			code = "0";
		}
			json.put("msg", msg);
			json.put("code", code);
			renderJson(json);
	}
	
	/**
	 *  /document/downloadFile
	 *  文件下载
	 */
	public void downloadFile(){
		logger.info("文档下载");
		String filedid = getPara();
		UploadDocument document = UploadDocument.dao.findById(filedid);
		renderFile(new File(document.getStr("path")));
	}
	
	/**  
	 *  /document/previewFile
	 *  文件预览
	 *  @version 1.0 
	 *  2015-08-20 还不支持
	 */
	public void previewFile(){
		logger.info("文件预览");
//		String fileid = getPara();
//		UploadDocument document = UploadDocument.dao.findById(fileid);
//		File file = new File(document.getStr("path"));
		
		
	}
	
	
}
