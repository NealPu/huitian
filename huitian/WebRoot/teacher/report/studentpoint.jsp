<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get("report.second.menu.settask")}</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet"/>
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet"/>
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet"/>
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet"/>
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet"/>
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet"/>
<link href="/css/css/animate.css" rel="stylesheet"/>
<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<style type="text/css">
 .chosen-container{
   margin-top:-3px;
 }
 .xubox_tabmove{
	background:#EEE;
}
.xubox_tabnow{
    color:#31708f;
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;">
	   <%@ include file="/common/left-nav.jsp"%>
	   <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;border:0">
			  <%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>
		
	  <div class="margin-nav" style="width:100%;">	
       <form action="/report/setPoint" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					    <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
					   &gt;<a href="/report/setPoint">${_res.get("report.first.menuname")}</a> &gt; ${_res.get("report.second.menu.settask")}
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
					<label>${_res.get("student.name") }：</label>
						<input type="text" id ="student" name="studentname" value="${studentname}">
					<input type="button" onclick="search()" value="${_res.get('admin.common.select') }"  class="btn btn-outline btn-primary">
			    </div>
			</div>
			</div>

			<div class="col-lg-12" style="min-width:680px">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get("report.list.of.pitch.points")}</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered" width="100%">
							<thead>
								<tr>
									<th>${_res.get("index") }</th>
									<th>${_res.get("student") }</th>
									<th>${_res.get('course.subject')}</th>
									<th>${_res.get("operation") }</th>
								</tr>
							</thead>
								<c:forEach items="${showPages}" var="student" varStatus="index">
									<tr class="odd" align="center">
										<td >${index.count }</td>
										<td>${student.real_name}</td>
										<td>${student.subjectname }</td>
										<td>
											<c:if test="${operator_session.qx_reportgetStudentReportPoints }">
											   	<a onclick="showStudentPoints(${student.id})" title="${_res.get('admin.common.see')}" >${_res.get('admin.common.see')} </a> &nbsp;
											</c:if>
											<c:if test="${operator_session.qx_reporttoSetPoint }">
											   |&nbsp;<a href="#" onclick="toSetPoint(${student.id})" title="${_res.get('set')}">${_res.get('set')}</a> &nbsp;
											</c:if>
										   |&nbsp;<a href="/report/reportList?studentname=${student.real_name }"  title="${_res.get('report.report')} " onclick="">${_res.get("report.report")} </a> &nbsp;
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
			<div style="clear:both;"></div>
		</form>
	</div>
	</div>	  
</div>  
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script type="text/javascript">

function toSetPoint(id){
	$.layer({
		type:2,
		shadeClose:'true',
		title:'设置',
		closeBtn:[0,true],
		shade:[0.5,"#000"],
		border:[0],
		offset:['20px',''],
		area:['560px',''],
		iframe:{src:'${ctx}/report/toSetPoint/'+id}
	});
}

function showStudentPoints(id){
	$.layer({
		type:2,
		shadeClose:'true',
		title:"${_res.get('admin.common.see')}",
		closeBtn:[0,true],
		shade:[0.5,"#000"],
		border:[0],
		offset:['20px',''],
		area:['700px','600px'],
		iframe:{src:'${ctx}/report/getStudentReportPoints/'+id}
	});
}

function showReportDetail(id){
	$.layer({
			type: 2,
	    shadeClose: true,
	    title: "${_res.get('admin.common.see')}",
	    closeBtn: [0, true],
	    shade: [0.5, '#000'],
	    border: [0],
	    offset:['50px', ''],
	    area: ['1050px', '500px'],
   	    iframe: {src: "/report/showReportDetail/" + id}
   	});
}

</script>

<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
       $('li[ID=nav-nav17]').removeAttr('').attr('class','active');
    </script>
</body>
</html>