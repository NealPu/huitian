<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>账务列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
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
 .xubox_tabmove{
	background:#EEE;
}
.xubox_tabnow{
    color:#31708f;
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;min-width:1100px">
	  <%@ include file="/common/left-nav.jsp"%>
       <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			  <%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

		<div class="margin-nav" style="min-width:1000px;width:100%;">
		<form action="/finance/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					      &gt;<a href='/finance/index'>财务管理</a>&gt; 账务列表
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
				</div>
				<div class="ibox-content" style="height:auto;">
				<label>${_res.get("student.name")}：</label>
					<input type="text" id="studentname" name="_query.studentname" value="${paramMap['_query.studentname']}">
				<label>${_res.get("admin.user.property.telephone")}：</label>
					<input type="text" id="phonenumber" name="_query.phonenumber" value="${paramMap['_query.phonenumber']}">
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>账务列表</h5>
							<div style="float: right;">
								<input type="button" id="s_2" onclick="initData()" value="初始化全部计费" class="btn btn-outline btn-primary btn-xs">
							</div>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>${_res.get("student.name")}</th>
									<th>添加日期</th>
									<th>${_res.get('admin.dict.property.status')}</th>
									<!-- <th>预存</th> -->
									<!-- th>预存副</th -->
									<th>实收额</th>
									<!-- th>主账户</th -->
									<!-- th>副账户</th -->
									<!-- th>主消耗</th -->
									<!-- th>副消耗</th -->
									<!-- th>${_res.get('discount.amount')}</th -->
									<th>${_res.get('total.sessions')}</th>
									<th>${_res.get("scheduled.classes")}(${_res.get("session")})</th>
									<c:if test="${operator_session.qx_accountbook }">
										<th>${_res.get("operation")}</th>
									</c:if>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="account" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${account.studentname}</td>
									<td><fmt:formatDate value="${account.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
									<td>${account.zt}</td>
									<%-- <td>${account.temprealbalance}</td> --%>
									<!-- td>${account.temprewardbalance}</td -->
									<td>${account.ss}</td>
									<!-- td>${account.realbalance}</td -->
									<!--td>${account.rewardbalance}</td -->
									<!--td>${account.sxh}</td -->
									<!--td>${account.xxh}</td -->
									<!--td>${account.yh}</td -->
									<td>${account.zks}</td>
									<td>${account.xhks}</td>
									<c:if test="${operator_session.qx_accountbook }">
										<td>
											<a href="#" onclick="showDetail(${account.id})">交费记录</a>&nbsp;&nbsp;
											<a href="#" onclick="showConsumptionDetail('${account.studentname}')">消耗</a>&nbsp;&nbsp;
											<c:if test="${operator_session.qx_financeapplyRefund }">
											   	<a href="#" onclick="applyRefund(${account.id})" >退费</a>
				                            </c:if>
				                            <a href="#" onclick="initData(${account.id})">初始计费</a>&nbsp;&nbsp;
										</td>
									</c:if>
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script type="text/javascript">
    
    function initData(accountid){
    	if(confirm("确定要重新对该学生初始计费？")){
    		$.ajax({
    			url:"${cxt}/finance/initdata",
    			type:"post",
    			data:{"key":"init","studentid":accountid},
    			dataType:"json",
    			success:function(data){
    				layer.msg(data.msg, 2, 1);
    			}
    		});
    	}
    }
    function showDetail(accountid){
    	window.location.href = "${cxt}/payment/index?_query.studentId="+accountid;
    	
    }
    function showConsumptionDetail(studentName){
    	window.location.href = "${cxt}/finance/consumptionStatistic?_query.studentname="+studentName;
    	
    }
    
    function applyRefund(stuid){
    	window.location.href = "${cxt}/finance/applyRefund?id="+stuid;
    }
    
    </script>
    
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/money.js"></script>
    <script>
       $('li[ID=nav-nav10]').removeAttr('').attr('class','active');
    </script>
</body>
</html>