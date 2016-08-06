<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>退费记录</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- Data Tables -->
<link href="/css/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<style>
#chaxun{
   color:#18a689
}
#chaxun:hover{
   color:#fff
}
</style>
</head>
<body>
<div id="wrapper" style="background: #2f4050;min-width:1100px">
  <%@ include file="/common/left-nav.jsp"%>
   <div class="gray-bg dashbard-1" id="page-wrapper" style="height:100%;width:auto">
    <div class="row border-bottom">
     <nav class="navbar navbar-static-top fixtop" role="navigation">
        <%@ include file="/common/top-index.jsp"%>
     </nav>
  </div>
  <div class="margin-nav">
<form action="/finance/queryRefundPage" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					     当前位置：<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> &gt;<a href='/finance/index'>财务管理</a>&gt; 退费纪录
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
				</div>
				<div class="ibox-content" style="height:auto;">
				<label>${_res.get('student')}：</label>
				<input type="text" id="studentname" name="_query.studentname" style="width:150px;" value="${paramMap['_query.studentname']}">
				<label>经办人：</label>
				<input type="text" id="transactname" name="_query.transactname" style="width:150px;" value="${paramMap['_query.transactname']}">
				<label>退费日期：</label> 
				<input type="text" readonly="readonly" id="date1" name="_query.refunddate" value="${paramMap['_query.refunddate']}" style="margin-top: -8px; width:186px" />
				
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary" id="chaxun">
				
			</div>
			</div>
			</div>
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>退费记录</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
						<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>${_res.get('student')}</th>
									<th>订单编号</th>
									<th>订单类型</th>
									<th>${_res.get("course.subject")}/${_res.get("group.class")}</th>
									<th>应收</th>
									<th>${_res.get('total.sessions')}(退费前)</th>
									<th>退费金额</th>
									<th>退费课时</th>
									<th>退费日期</th>
									<th>经办人</th>
									<th>记录日期</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="refund" varStatus="index">
								<tr align="center">
									<td>${index.count}</td>
									<td>${refund.studentname}</td>
									<td>${refund.ordernum}</td>
									<td>${refund.teachtype eq '1'?_res.get("IEP"):_res.get('course.classes')}</td>
									<td>${refund.teachtype eq '1'?refund.subjectname:refund.classNum}</td>
									<td>${refund.betotlebalance}</td>
									<td>${refund.beforeclasshours}</td>
									<td>${refund.balance}</td>
									<td>${refund.classhours}</td>
									<td><fmt:formatDate value="${refund.refundtime }" pattern="yyyy/MM/dd" /></td>
									<td>${refund.transactname}</td>
									<td><fmt:formatDate value="${refund.addtime }" pattern="yyyy/MM/dd HH:mm:ss" /></td>
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
	<script src="/js/js/demo/layer-demo.js"></script>
	 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=3.3.0"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Data Tables -->
    <script src="/js/js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="/js/js/plugins/dataTables/dataTables.bootstrap.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/plugins/pace/pace.min.js"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/money.js"></script>
    <!-- Chosen -->
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
    <script>
    $(".chosen-select").chosen({disable_search_threshold: 25});
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
    <script>
    $('li[ID=nav-nav10]').removeAttr('').attr('class','active');
    </script>
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
    <script type="text/javascript">
    
    </script>
</body>
</html>