<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.m-b-file {
		margin-bottom: 0px !important;
	    margin-right: 0;
	    width: 416px;
	}
	.progress {
	    background-color: #f5f5f5;
	    border-radius: 4px;
	    height: 13px;
	    margin-bottom: 10px;
	    overflow: hidden;
	    display : none;
	}
	.loadingMsg {
		float: left;
		font-size: 10px;
		margin-left: 5px;
	}
</style>
<div id="uploadFilePage" >
   	<div style="width:100%;">	
		<div class="ibox-content">
			<span style="color:red;" >上传文件大小不能超过50MB.</span>
			<div style="height: 40%;width: 100%;">
				<form action="/proxy/importProxy" method="post" id="batchImportForm" enctype="multipart/form-data"> 
					<fieldset>
						<div class=""> 
							<div class="m-t-sm flt" >
								<label class="flt" >请选择文件：</label>
								<span class="flt" >
									<input type="file" name="fileField" class="file m-b-file" id="fileField" size="10" onchange="resetBarView()" />
									<!-- <input id="progressBar" ><span id="progressBarVal" ></span> -->
									<div class="progress progress-striped active" id="progressBar" >
			                            <div class="progress-bar" role="progressbar" aria-valuenow="75" id="propressBarView"
			                            	 aria-valuemin="0" aria-valuemax="100" style="width: 45%">
			                            </div>
			                            <span class="loadingMsg" >正在上传...</span>
			                        </div>
								</span>
							</div>
							<div style="clear:both;" ></div>
							<input type="button"  onclick="submitBatchImport()" class=" btn btn-xs btn-info m-t-sm"  value="上传" /> 
							<input type="button"  onclick="downloadTemplate()" class=" btn btn-xs btn-primary m-t-sm"  value="下载模板" /> 
						</div>  
					</fieldset>
				</form>
				<form action="/proxy/downloadTemplate" id="templateDownloadForm" method="post"> </form>
			</div>
		</div>
	</div>
</div>	

