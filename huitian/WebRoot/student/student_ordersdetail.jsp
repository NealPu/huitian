<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get('purchase_records')}</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<!-- Data Tables -->
<link href="/css/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
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
 .xubox_tabmove{
	background:#EEE;
}
.xubox_tabnow{
    color:#31708f;
}
h5{
    font-weight: 100 !important
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;">
	  <%@ include file="/common/left-nav.jsp"%>
       <div class="gray-bg dashbard-1" id="page-wrapper" style="min-width:1020px">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;border:0">
			<%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>
		<form action="/student/showCourseOrdersDetail" method="post" id="searchForm" >
		<div class="margin-nav" style="min-width:1000px;width:100%;">
			<div class="col-lg-12" style="margin-top:-5px">
				<div class="ibox float-e-margins">
				    <div class="ibox-title">
						<h5 ><img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
						&gt;<a href='/student/index'>${_res.get('student_management')}</a> &gt;  ${_res.get('purchase_records')}</h5>
						<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
					</div>
					<div class="ibox-content">
					<!-- 查询的内容 -->
						<label>${_res.get("student.name") }：</label>
						<input type="text" id="studentname" name="_query.studentname" style="width:150px;" value="${paramMap['_query.studentname']}">
						
						<input type="button" onclick="search()" value="${_res.get('admin.common.select') }" class="btn btn-outline btn-info">
					<!-- 查询结束 -->
						 <div style="clear: both;"></div>
					</div>

						<div class="col-lg-12" style="padding-left: 0;">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>${_res.get("student_list") }</h5>
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered">
										<thead>
											<tr>
												<th >${_res.get("index")}</th>
												<th >${_res.get("student.name")}</th>
												<th >${_res.get('date.of.purchase')}</th>
												<th >${_res.get('admin.dict.property.status')}</th>
												<th >${_res.get('type.of.class')}</th>
												<th >${_res.get('course.subject')}/${_res.get('classNum')}</th>
												<th >${_res.get('total.amount')}</th>
												<th >${_res.get('actual.amount')}</th>
												<th >${_res.get('discount.amount')}</th>
												<th >${_res.get('total.sessions')}</th>
											</tr>
										</thead>
										<c:forEach items="${buylist}" var="orders" varStatus="index">
											<tr align="center">
												<td >${index.count }</td>
												<td >${orders.studentname}</td>
												<td ><fmt:formatDate value="${orders.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd "/></td>
												<td >
													<c:choose>
														<c:when test="${orders.delflag eq true }">
															${_res.get('Cancelled')}
														</c:when>
														<c:otherwise>
															${orders.status==0?'未支付':orders.status==1?'已支付':'欠费'}
														</c:otherwise>
													</c:choose>
												</td>
												<td >${orders.teachtype==1?_res.get("IEP"):_res.get('course.classes')}</td>
												<td >${orders.teachtype==1?orders.subjectname:orders.classnum}</td>
												<td >${orders.totalsum}</td>
												<td >${orders.realsum}</td>
												<td >${orders.rebate}</td>
												<td >${orders.classhour}</td>
										</c:forEach>
									</table>
									<div id="splitPageDiv">
										<jsp:include page="/common/splitPage.jsp" />
									</div>
								</div>
							</div>
						</div>
					</div>
			</div>
			<div style="clear: both;"></div>
	  </div>
	  </form>
	  </div>
	</div>
    
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Data Tables -->
    <script src="/js/js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="/js/js/plugins/dataTables/dataTables.bootstrap.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/plugins/pace/pace.min.js"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
   
    <script>
    $('li[ID=nav-nav1]').removeAttr('').attr('class','active');
    </script>
</body>
</html>