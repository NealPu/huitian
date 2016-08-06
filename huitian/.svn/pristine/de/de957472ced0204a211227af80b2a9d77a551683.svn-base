<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>交费记录列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
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
		<form action="/payment/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					      &gt;财务管理&gt; 
					      <a href='/finance/index'>账务管理</a>&gt; 缴费记录
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
				</div>
				<div class="ibox-content" style="height:auto;">
				<label>${_res.get("student.name")}：</label>
					<input type="text" id="studentname" name="_query.studentname" value="${paramMap['_query.studentname']}">
				<label>${_res.get('Opp.Entry.Date')}：</label>
					 <input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.startDate" value="${paramMap['_query.startDate']}" style="margin-top: -8px; width:120px" /> 
					至<input type="text" class="layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px" />
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>账务列表</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>${_res.get("student.name")}</th>
									<th>录入日期</th>
									<th>${_res.get('admin.dict.property.status')}</th>
									<th>支付日期</th>
									<th>交费金额</th>
									<th>订单编号</th>
									<th>提交人</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="payment" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${payment.studentname}</td>
									<td><fmt:formatDate value="${payment.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
									<td>${payment.ispay?'已支付':'未支付'}</td>
									<td>${payment.paydate}</td>
									<td>${payment.amount}</td>
									<td>${payment.ordernum}</td>
									<td>${payment.operatename}</td>
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
    
    function showDetail(accountid){
    	window.location.href = "${cxt}/accountbook/index?_query.accountid="+accountid;
    	
    }
    
    function applyRefund(stuid){
    	$.ajax({
    		url:"/finance/checkCanRefund",
    		data:{"id":stuid},
    		datatype:"json",
    		type:"post",
    		success:function(result){
    			if(result.code==1){
			    	window.location.href = "${cxt}/finance/applyRefund?id="+stuid;
    			}else{
    				layer.msg(result.msg,3,2);
    			}
    		}
    	});
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
         <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		//日期范围限制
		var date1 = {
			elem : '#date1',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : false,
			choose : function(datas) {
				date2.min = datas; //开始日选好后，重置结束日的最小日期
				date2.start = datas //将结束日的初始值设定为开始日
			}
		};
		var date2 = {
			elem : '#date2',
			format : 'YYYY-MM-DD',
			max : '2099-06-16',
			istime : false,
			istoday : false,
			choose : function(datas) {
				date1.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(date1);
		laydate(date2);
	</script>
    <script>
       $('li[ID=nav-nav10]').removeAttr('').attr('class','active');
    </script>
</body>
</html>