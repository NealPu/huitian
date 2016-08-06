<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">


<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/js/jquery-2.1.1.min.js"></script>
<script src="/js/js/plugins/layer/layer.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${_res.get("score_template") }</title>

</head>
<body>
	<div id="wrapper" style="background: #2f4050;height:100%;min-width:1000px">
	    <%@ include file="/common/left-nav.jsp"%>
	    <div class="gray-bg dashbard-1" id="page-wrapper" style="height:100%">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			<%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

		 <div class="margin-nav" style="min-width:720px;width:100%;margin-left:0;">
		 <div class="ibox float-e-margins">
		   <div class="ibox-title">
				<h5>
					<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					<a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a>
					&gt;<a href='/student/index'>${_res.get('student_management') }</a> &gt;  ${_res.get("score_template") }
				</h5>
				<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
			</div>
		</div>
		<div class="col-lg-12" style="width:50%; float:left;">
		Upper Level
			<div class="ibox float-e-margins">
				<table class="table table-striped table-bordered table-hover dataTables-example">
							<thead>
								<tr>
									<th >${_res.get("grade.original.value") }</th>
									<th >${_res.get("grade.Reading") }</th>
									<th >${_res.get("grade.Vocabulary") }</th>
									<th >${_res.get("grade.Math") }</th>
								</tr>
							</thead>
							<c:forEach items="${fenzhiListOne}" var="orders" >
								<tr align="center">
									<td >${orders.yuanzhi}</td>
									<td >${orders.yuedu}</td>
									<td >${orders.cihui}</td>
									<td >${orders.shuxue}</td>
							</c:forEach>
				</table>
			</div>
		</div>
		<div  class="col-lg-12" style="width:50%;float:right;">
		Middle Level
			<div class="ibox float-e-margins">
				<table class="table table-striped table-bordered table-hover dataTables-example">
							<thead>
								<tr>
									<th >${_res.get("grade.original.value") }</th>
									<th >${_res.get("grade.Reading") }</th>
									<th >${_res.get("grade.Vocabulary") }</th>
									<th >${_res.get("grade.Math") }</th>
								</tr>
							</thead>
							<c:forEach items="${fenzhiList}" var="order" >
								<tr align="center">
									<td >${order.yuanzhi}</td>
									<td >${order.yuedu}</td>
									<td >${order.cihui}</td>
									<td >${order.shuxue}</td>
							</c:forEach>
				</table>
			</div>
		</div>
		</div>
		<div style="clear:both"></div>
 		</div>
	</div>
	
	
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
	$(".chosen-select").chosen({disable_search_threshold:30});
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

	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		layer.use('extend/layer.ext.js'); //载入layer拓展模块
	</script>
 
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