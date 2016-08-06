<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>欠费管理</title>
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
 .xubox_tabmove{
	background:#EEE;
}
.xubox_tabnow{
    color:#31708f;
}
.btn-pad{
   padding:9px 12px !important;
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

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
				    <div class="ibox-title" style="margin-bottom:20px">
						<h5><img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
						&gt;<a href='/finance/index'>财务管理</a>&gt; 欠费管理</h5>
						<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
					</div>
					<div class="ibox-title">
						<h5>欠费列表</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("student.name")}</th>
									<th>订单号</th>
									<th>${_res.get('course.subject')}/${_res.get('classNum')}</th>
									<th>总金额</th>
									<th>已交金额</th>
									<th>欠费金额</th>
									<th colspan="2">${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${list}" var="orders" varStatus="index">
								<tr align="center">
									<td rowspan="${orders.row}">${orders.REAL_NAME}</td>
									<td rowspan="1">${orders.payone.paymessage.ORDERNUM}</td>
									<td  rowspan="1">${orders.payone.paymessage.TEACHTYPE==1?orders.PAYONE.paymessage.SUBJECTNAME:orders.PAYONE.paymessage.CLASSNUM}</td>
									<td  rowspan="1">${orders.payone.paymessage.REALSUM}</td>
									<td  rowspan="1">${orders.payone.paymessage.PAIDAMOUNT}</td>
									<td  rowspan="1">${orders.payone.paymessage.REALSUM-orders.payone.paymessage.PAIDAMOUNT}</td>
									<td  rowspan="1">
										<c:if test="${operator_session.qx_paymenttoConfirmPage }">
											<a href="#"  onclick="showPayment(${orders.payone.paymessage.id})">确认收款</a>
										</c:if>
									</td>
									<td rowspan="${orders.row}">
										 <a href="#"  onclick="sendCourseOrderMesage(${orders.id})">催款邮件</a> 
									</td>
									<c:forEach items="${orders.allpay}" var="pay">
										<tr align="center">
											<td  rowspan="1">${pay.paymessage.ORDERNUM}</td>
											<td  rowspan="1">${pay.paymessage.TEACHTYPE==1?pay.paymessage.SUBJECTNAME:pay.paymessage.CLASSNUM}</td>
											<td  rowspan="1">${pay.paymessage.REALSUM}</td>
											<td  rowspan="1">${pay.paymessage.PAIDAMOUNT}</td>
											<td  rowspan="1">${pay.paymessage.REALSUM-pay.paymessage.PAIDAMOUNT}</td>
											<td  rowspan="1">
												<c:if test="${operator_session.qx_paymenttoConfirmPage }">
													<a href="#"  onclick="showPayment(${pay.paymessage.id})">确认收款</a>
												</c:if>
											</td>
										</tr>
									</c:forEach>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
	  </div>
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
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/money.js"></script>
    <script type="text/javascript">
    var index = parent.layer.getFrameIndex(window.name);
	parent.layer.iframeAuto(index);
    function showPayment(orderId){
    	$.layer({
    		type: 2,
     	    shadeClose: true,
     	    title: "交费",
     	    closeBtn: [0, true],
     	    shade: [0.5, '#000'],
     	    offset:['100px', ''],
     	    area:['800px','500px'],
    	    iframe: {src: '${cxt}/payment/paymentList/'+orderId}
    	});
    }
	/*给家长发送催费邮件*/
	function sendCourseOrderMesage(studentid){
		$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "催费邮件",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    area: ['800px', '600'],
    	    iframe: {src: '${cxt}/orders/sendCourseOrderMesage/'+studentid}
    	});
	}	
    </script>
</body>
</html>