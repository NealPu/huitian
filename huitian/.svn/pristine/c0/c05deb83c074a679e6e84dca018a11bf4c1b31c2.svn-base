<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<style type="text/css">
 .chosen-container{
   margin-top:-3px;
 }
</style>
</head>
<body>   
	<div class="margin-nav1" style="padding:10px">
		<div class="col-lg-12">
				<div class="ibox float-e-margins">
				<div class="ibox-title">
						<h5>${_res.get('Report.an.appointment.time.point')}</h5>
					</div>
					<div class="ibox-content">
						<form action="" method="post" id="pointForm">
							<fieldset style="width: 600px">
								<table class="table table-hover table-bordered" width="100%">
									<thead>
										<tr>
											<td>${_res.get("index")}</td>
											<td>${_res.get('appointment.time')}</td>
											<td>${_res.get("report.consultant.who.submits.the.report")}</td>
											<td>${_res.get('admin.dict.property.status')}</td>
											<td>${_res.get('submission.time')}</td>
											<td>${_res.get("operation")}</td>
										</tr>
									</thead>
									<c:forEach items="${studentlist }" var="student" varStatus="index" >
										<tr>
											<td>${index.count }</td>
											<td>${student.appointment }</td>
											<td>${student.teacherName }</td>
											<td><c:if test="${student.state eq '0' }" >${_res.get('Uncommitted')}</c:if>
												<c:if test="${student.state eq '1' }" >${_res.get('Submitted')}</c:if></td>
											<td>${student.submissiontime }</td>
											<td> <c:if test="${student.state eq '0' }" >
													<a href="#" onclick="deleteSetPoint(${student.Id})">${_res.get('admin.common.delete')}</a></c:if>
												<c:if test="${student.state eq '1' }" ><a onclick="showReportDetail(${student.Id})" >${_res.get("report.report")}</a></c:if>
											</td>
										</tr>
									</c:forEach>
								</table>
							</fieldset>
						</form>
					</div>

				</div>
				<div style="clear: both;"></div>
				</div>
			</div>
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
    <script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
		
    <script>
    $(".chosen-select").chosen({disable_search_threshold: 20});
        var config = {
            '.chosen-select': {},
            '.chosen-select-deselect': {
                allow_single_deselect: true
            },
            '.chosen-select-no-single': {
                disable_search_threshold: 10
            },
            '.chosen-select-no-results': {
                no_results_text: 'Oops, nothing found!'
            },
            '.chosen-select-width': {
                width: "95%"
            }
        }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }   
    </script>	
    <script src="/js/js/plugins/layer/laydate/laydate.js"></script>
    <script>
    var index = parent.layer.getFrameIndex(window.name);
    /* parent.layer.iframeAuto(index); */
    
    function deleteSetPoint(id){
    	if(confirm("确定要删除？")){
    		$.ajax({
    			url:"/report/deleteSetPoint",
    			data:{"pointid":id},
    			type:"post",
    			dataType:"json",
    			success:function(result){
    				if(result.code=="1"){
    					layer.msg("删除成功.",2,1);
    					setTimeout("window.location.reload()",2100);
    				}else{
    					layer.msg("未删除成功.",2,2);
    				}
    				
    			}
    		});
    	}
    }
    function showReportDetail(id){
    	$.ajax({
    		url:"/report/getPointTeachergradeId",
    		data:{"pointid":id},
    		dataType:"json",
    		type:"post",
    		success:function(result){
    			if(result.code=="1"){
    				parent.showReportDetail(result.tg.ID);
					parent.layer.close(index);
    			}
    		}
    	});
    }
    
  //弹出后子页面大小会自动适应
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.iframeAuto(index);
	</script>
</body>
</html>