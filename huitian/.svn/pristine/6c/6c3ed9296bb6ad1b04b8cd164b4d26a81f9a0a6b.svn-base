<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get("student.performance.page") }</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">


<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">

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
<script type="text/javascript">
	function resetSearch() {
		$("#studentName").val('');
		$('#subjectId').val('');
	}
	function delRecord(recordId){
		if(confirm('确认删除该成绩记录吗')){
			$.ajax({
				url:"/grade/delGradeRecord",
				type:"post",
				data:{"recordId":recordId},
				dataType:"json",
				success:function(result)
				{
					if(result.result=="true"){
						alert("删除成功");
						window.location.reload();
					}else{
						alert(result.result);
					}
				}
			});
		}
	}
	//成绩列表修改
	function modRecord(recordId){
		 $.layer({
	    	    type: 2,
	    	    shadeClose: true,
	    	    title: "${_res.get('Modify.the.total.score')}",
	    	    closeBtn: [0, true],
	    	    shade: [0.5, '#000'],
	    	    border: [0],
	    	    offset:['100px', ''],
	    	    area: ['800px', '800px'],
	    	    iframe: {src: '${cxt}/grade/toModifyScoredId/'+recordId}
	    	   });
		
	}
	function queryGradeDetail(recordId){
		$.ajax({
			url:"${cxt}/grade/queryGradeDetail",
			type:"post",
			data:{"recordId":recordId},
			dataType:"json",
			success:function(result){
				var content = "";
				if(result.code=="1"){
					content = "<tr><td class='table-bg'>${_res.get('sysname')}</td><td>"+result.record.STUDENTNAME+"</td><td class='table-bg'>${_res.get('course.subject')}</td><td>"+result.record.SUBJECTNAME+"</td><td class='table-bg'>${_res.get('total.score')}</td><td>"+result.record.GROSSSCORE+"${_res.get('Minute')}</td><td class='table-bg'>${_res.get('grade.exam.date')}</td><td>"+result.record.EXAMDATE+"</td></tr></br><tr>";
					for(i in result.detail){
						var courseName = result.detail[i].COURSENAME;
						var score = result.detail[i].SCORE;
						content +="<td class='table-bg'>"+courseName+"</td><td>"+score+"${_res.get('Minute')}</td>";
					}
					
					content += "</tr><tr><td class='table-bg'>${_res.get('course.remarks')}</td><td>"+result.record.REMARK+"</td></tr>";
				}else{
					content=result.msg;
				}
				var pageii = $.layer({
				    type: 1,
				    title: false,
				    area: ['auto', 'auto'],
				    border: [0], //去掉默认边框
				    shade: [0], //去掉遮罩
				    closeBtn: [0, false], //去掉默认关闭按钮
				    shift: 'left', //从左动画弹出
				    page: {
				        html: '<div style="width:480px; padding:10px; border:1px solid #ccc; background-color:#FFF;"><p><table class="table-chakan table-bordered">'+content+'</table></p><button id="pagebtn" class="btn btn-outline btn-warning" onclick="">${_res.get("admin.common.close")}</button></div>'
				    }
				});
				//自设关闭
				$('#pagebtn').on('click', function(){
				    layer.close(pageii);
				});
			}
		});
	}
</script>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;min-width:1000px">
	  <%@ include file="/common/left-nav.jsp"%>
	  <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			<%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

 		<div class="margin-nav" style="margin-left:-14px;">
		<form action="/grade/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
						<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
						<a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a>
						&gt;<a href='/student/index'>${_res.get('student_management') }</a> &gt; ${_res.get('performance_management') }
				    </h5>
				    <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content" style="height:auto;">
				<label>${_res.get("student.name")}：</label>
					<input type="text" id="studentName" name="_query.studentName" value="${paramMap['_query.studentName']}">
				<label>${_res.get("course.subject") }：</label>
				<select id="subjectId" class="chosen-select" style="width: 150px;" tabindex="2" name="_query.subjectId">
					<option value="">${_res.get("system.alloptions")}</option>
					<c:forEach items="${subjectList}" var="subject">
						<option value="${subject.id }" <c:if test="${subject.id == paramMap['_query.subjectId'] }">selected="selected"</c:if>>${subject.subject_name }</option>
					</c:forEach>
				</select>
				<input type="button" onclick="search()" value="${_res.get('admin.common.select') }" class="btn btn-outline btn-primary">
				<!--  -->
				<c:if test="${operator_session.qx_gradeaddGradeRecord }">
				<input type="button" id="tianjia" value="${_res.get('teacher.group.add') }" onclick="window.location.href='/grade/addGradeRecord/${paramMap['_query.studentId']}'"  class="btn btn-outline btn-info">
				</c:if>
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get("course's.complete.info")}</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index") }</th>
									<th>${_res.get("student.name") }</th>
									<th>${_res.get('name.of.subject')}</th>
									<th>${_res.get('type.of.score')}</th>
									<th>${_res.get('type.of.grade')}</th>
									<th>${_res.get('grade.exam.date')}</th>
									<th>${_res.get('total.score')}</th>
									<th>${_res.get('detailed.info')}</th>
									<th>${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="grade" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${grade.studentname}</td>
									<td>${grade.subjectname}</td>
									<td>${grade.scoretype==true?_res.get('After.training'):_res.get('Training.ago')}</td>
									<td>${grade.gradetype==1?_res.get('Test.scores'):grade.gradetype==0?_res.get('Mold.test.results'):_res.get('Else')}</td>
									<td>${grade.examdate}</td>
									<td>${grade.grossscore}</td>
									<td>${grade.hasdetail==true?_res.get('admin.common.yes'):_res.get('admin.common.no')}</td>
									<td>
									<c:if test="${operator_session.qx_gradequeryGradeDetail }">
									<a href="javascript:void(0)" style="color: #515151" onclick="queryGradeDetail(${grade.id})">${_res.get("admin.common.see")}</a>&nbsp;|&nbsp;
									</c:if>
									<c:if test="${operator_session.qx_gradetoModifyScoredId }">
									<a href="javascript:void(0)" style="color: #515151" onclick="modRecord(${grade.id})">${_res.get('Modify')}</a>&nbsp;|&nbsp;
									</c:if>
									<c:if test="${operator_session.qx_gradedelGradeRecord }">
									<a href="javascript:void(0)" style="color: #515151" onclick="delRecord(${grade.id})">${_res.get('admin.common.delete')}</a>
									</c:if>
									</td>
								</tr>
							</c:forEach>
						</table>
						<div id="splitPageDiv">
							<jsp:include page="/common/splitPage.jsp" />
						</div>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
		</form>
		</div>
		</div>
	</div>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
	$(".chosen-select").chosen({disable_search_threshold:30});
		var config = {
			'.chosen-select' : {},
			'.chosen-select-deselect' : {
				allow_single_deselect : true
			},
			'.chosen-select-no-single' : {
				disable_search_threshold : 10
			},
			'.chosen-select-no-results' : {
				no_results_text : 'Oops, nothing found!'
			},
			'.chosen-select-width' : {
				width : "95%"
			}
		}
		for ( var selector in config) {
			$(selector).chosen(config[selector]);
		}
		
	</script>
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>

	<script src="/js/js/demo/layer-demo.js"></script>
	
	<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
       $('li[ID=nav-nav1]').removeAttr('').attr('class','active');
    </script>
</body>
</html>