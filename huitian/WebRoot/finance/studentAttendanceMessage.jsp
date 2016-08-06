<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>学生考勤信息</title>
<meta name="save" content="history">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
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
 h5{
   font-weight: 100 !important
 }
</style>
</head>
<body id="body">   
	<div id="wrapper" style="background: #2f4050;height:100%;min-width:1100px">
	 <%@ include file="/common/left-nav.jsp"%>
	 <div class="gray-bg dashbard-1" id="page-wrapper" style="height:100%">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			   <%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

        <div class="margin-nav2" style="min-width:1000px;">
		<form action="" method="post" id="searchForm" >
			<div  class="col-lg-12 m-t-xzl" style="padding-left:0;">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
						<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
						&gt;<a href='/attendance/studentIndex'>考勤管理</a> &gt;<a href='javascript:history.go(-1);'>学生考勤</a> &gt;学生考勤信息
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
            	   <div style="clear:both"></div>
				</div>
			 </div>
		   </div>
			<div class="col-lg-12" style="padding-left:0;">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>学生考勤信息</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
							<tr align="center">
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('student')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('teacher')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('class.time.session')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('time.session')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('type')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('course.course')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('class.classroom')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">${_res.get('system.campus')}</th>
								<th rowspan="2" style="height:15px;line-height: 34px;">点名状态</th>
							</tr>
							</thead>
							<c:forEach items="${courseplan}" var="cp" varStatus="index">
								<tr align="center">
									<td>${cp.studentname}</td>
									<td>${cp.teachername}</td>
									<td><fmt:formatDate value="${cp.course_time}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>${cp.rankname}</td>
									<c:if test="${cp.class_id==0}">
										<td>${_res.get("IEP")}</td>
									</c:if>
									<c:if test="${cp.class_id  ne 0}">
										<td>${_res.get('course.classes')}(${cp.classNum})</td>
									</c:if>
									<td>${cp.course_name}</td>
									<td>${cp.roomname}</td>
									<td>${cp.campus_name}</td>
									<td>${cp.singn==1?_res.get("normal"):cp.singn==2?_res.get('courselib.late'):cp.singn==3?_res.get('Ask.for.leave'):cp.singn==4?_res.get("operation"):_res.get('Unnamed')}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
			<div style="clear:both "></div>
		</form>
		</div>
	   </div>
	</div>
<!-- Chosen -->
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>

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
	
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script src="/js/js/demo/layer-demo.js"></script>
  	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
  	<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/money.js"></script>
    <script>
    $('li[ID=nav-nav15]').removeAttr('').attr('class','active');
    //$(".left-nav").css("height", window.screenLeft);
    //alert($("#wrapper").outerHeight(true));
    </script>
</body>
</html>