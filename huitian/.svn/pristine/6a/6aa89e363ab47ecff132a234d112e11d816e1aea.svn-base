<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>交费管理</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

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
.chosen-container {
	margin-top: -3px;
}

.xubox_tabmove {
	background: #EEE;
}

.xubox_tabnow {
	color: #31708f;
}

.btn-pad {
	padding: 9px 12px !important;
}

.minimalize-styl-2 {
	padding: 7px 12px !important
}

.count-info .label {
	top: 8px
}

#nav-topwidth>li {
	top: 4px
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050; min-width: 1100px">
		<%@ include file="/common/left-nav.jsp"%>
		<div class="gray-bg dashbard-1" id="page-wrapper">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top fixtop" role="navigation"> <%@ include file="/common/top-index.jsp"%> </nav>
			</div>
			<div class="margin-nav" style="min-width: 1000px; width: 100%;">
				<form action="/orders/index" method="post" id="searchForm">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>
									<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top: -1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> &gt;<a href='/finance/index'>财务管理</a>&gt; 交费管理
								</h5>
								<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
							</div>
							<div class="ibox-content" style="height: auto;">
								<label>${_res.get("student.name")}：</label> 
								<input type="text" id="studentname" style="width: 120px;" name="_query.studentname" value="${paramMap['_query.studentname']}"> 
								<label>${_res.get("admin.user.property.telephone")}：</label> 
								<input type="text" id="phonenumber" style="width: 150px;" name="_query.phonenumber" value="${paramMap['_query.phonenumber']}"> 
								<label>${_res.get("monthly.statistics")}：</label> 
								<input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.beginDate" value="${paramMap['_query.beginDate']}" style="margin-top: -8px; width:120px" /> 
								至<input type="text" class="layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px" /> 
								<br/>
								<br/>
								<label>授课类型</label> <select
									name="_query.teachtype" id="teachtype" class="chosen-select" style="width: 100px;" tabindex="2">
									<option value="">全部</option>
									<option ${paramMap['_query.teachtype']==1?"selected='selected'":""} value="1">一对一</option>
									<option ${paramMap['_query.teachtype']==2?"selected='selected'":""} value="2">小班</option>
								</select> <label>状态</label> <select name="_query.status" id="status" class="chosen-select" style="width: 80px;" tabindex="2">
									<option value="">全部</option>
									<option ${paramMap['_query.status']=='0'?"selected='selected'":""} value="0">待收</option>
									<option ${paramMap['_query.status']=='1'?"selected='selected'":""} value="1">已收</option>
									<option ${paramMap['_query.status']=='2'?"selected='selected'":""} value="2">欠费</option>
								</select> 
								<input type="hidden" id="type" name="_query.type" value="${paramMap['_query.type']}"> 
								<input type="button" onclick="searchMessage(0)" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary"> 
								<input type="button" onclick="toExcel()" value="导出" class="btn btn-outline btn-primary"> 
							</div>
						</div>
					</div>

					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>订单列表</h5>
								<%-- <div style="float: right;">
									<span>统计月份：</span>${paramMap['_query.date']==null?record.date:paramMap['_query.date']}&nbsp;&nbsp;&nbsp;&nbsp; 
									<span>总消耗：${record.yzxh}</span> 
									<input type="button" id="xh_1" onclick="searchMessage(1)" value="未消耗完" class="btn btn-outline btn-primary btn-xs"> 
									<input type="button" id="xh_2" onclick="searchMessage(2)" value="已消耗完" class="btn btn-outline btn-primary btn-xs">
								</div> --%>
							</div>
							<div class="ibox-content">
								<table width="100%" class="table table-hover table-bordered">
									<thead>
										<tr>
											<th width="10%">姓名</th>
											<th width="10%">订单号</th>
											<th width="10%">订单日期</th>
											<th width="5%">${_res.get('admin.dict.property.status')}</th>
											<th width="8%">提交人</th>
											<th width="10%">${_res.get('course.subject')}/${_res.get('classNum')}</th>
											<th width="10%">${_res.get('actual.amount')}/已收额/欠费额</th>
											<th width="10%">${_res.get('total.sessions')}/单价</th>
											<th width="10%">消耗(课时/金额)</th>
											<th width="10%">剩余(课时/金额)</th>
											<th width="10%">${_res.get("operation")}</th>
										</tr>
									</thead>
									<c:forEach items="${showPages.list}" var="orders" varStatus="index">
											<tr align="center">
												<td>${orders.studentname}</td>
												<td>${orders.ordernum}</td>
												<td><fmt:formatDate value="${orders.createtime}" type="time" timeStyle="full" pattern="yyyy/MM/dd" /></td>
												<td>${orders.status==0?'待收':orders.status==1?'已收':'欠费'}</td>
												<td>${orders.operatorname}</td>
												<td>${orders.teachtype==1?orders.subjectname:orders.classnum}</td>
												<td>${orders.realsum}/${orders.paidamount}/${orders.realsum-orders.paidamount}</td>
												<td>${orders.classhour}/${orders.avgprice}</td>
												<td>${orders.consumptionhour}/${orders.consumptioncost}</td>
												<td>${orders.syks}/${orders.ddye}</td>
												<td><c:choose>
														<c:when test="${orders.delflag eq true }">
															<c:if test="${operator_session.qx_ordersshowOrderReviews }">
																<a href="#" style="color: #515151" onclick="showDelReason(${orders.id})">取消原因</a>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${orders.needcheck eq true}">
																	<c:if test="${orders.checkstatus==0 }">
																		<c:if test="${operator_session.qx_ordersorderFirstReviews }">
																			<a href="#" onclick="orderReview(${orders.id},0)">待审核</a>
																		</c:if>
																	</c:if>
																	<c:if test="${orders.checkstatus==1 }">
																		<c:if test="${operator_session.qx_paymentpaymentList }">
																			<a href="#" onclick="showPayment(${orders.id})">${_res.get("admin.common.see")}</a>
																		</c:if>
																		<c:if test="${operator_session.qx_paymenttoPayment }">
																			<a href="#" onclick="paying(${orders.id},${orders.realsum },${orders.paidamount })">交费</a><br/>
																		</c:if>
																		<c:if test="${operator_session.qx_orderstiaoke }">
																			<a href="#" onclick="modify(${orders.id})">${_res.get('Modify')}</a>
																		</c:if>
																	</c:if>
																	<c:if test="${orders.checkstatus==2 }">
																		<c:if test="${operator_session.qx_ordersorderFirstReviews }">
																			<a href="#" onclick="orderReview(${orders.id},2)">未通过</a>
																		</c:if>
																	</c:if>
																</c:when>
																<c:otherwise>
																	<c:if test="${operator_session.qx_paymentpaymentList }">
																		<a href="#" onclick="showPayment(${orders.id})">${_res.get("admin.common.see")}</a>
																	</c:if>
																	<c:if test="${operator_session.qx_paymenttoPayment }">
																		<a href="#" onclick="paying(${orders.id},${orders.realsum },${orders.paidamount })">交费</a><br/>
																	</c:if>
																	<c:if test="${operator_session.qx_orderstiaoke }">
																		<a href="#" onclick="modify(${orders.id})">${_res.get('Modify')}</a>
																	</c:if>
																</c:otherwise>
															</c:choose>
															<c:if test="${operator_session.qx_ordersdelOrder }">
																<a href="#" onclick="delOrder(${orders.id})">取消</a>
															</c:if>
														</c:otherwise>
													</c:choose></td>
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
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
	 $(".chosen-select").chosen({disable_search_threshold: 30});
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
	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
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
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
	<script type="text/javascript">
	/*快捷查询*/
	function searchMessage(code){
		if(code==1){
			$("#type").val(1);
		}else if(code==2){
			$("#type").val(2);
		}else if(code==0){
			$("#type").val("");
		}
		$("#searchForm").attr("action","/orders/index");
		$("#searchForm").submit();
	}
	function toExcel(){
		if(confirm("确定要导出订单记录吗？")){
			$("#searchForm").attr("action","/orders/toExcel");
			$("#searchForm").submit();
			$("#searchForm").attr("action","/orders/index");
		}
	}
    function showOrderReview(orderId){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "订单信息",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area:['800px','500px'],
    	    iframe: {src: '${cxt}/orders/showOrderReviews/'+orderId}
    	});
    }
    
    function orderReview(orderId,type){
    	if(type==1){
    		layer.msg("审核已通过。",1,10);
    	}else{
    		//直接进入审核页:
    		$.layer({
	    	    type: 2,
	    	    shadeClose: true,
	    	    title: "订单审核",
	    	    closeBtn: [0, true],
	    	    shade: [0.5, '#000'],
	    	    border: [0],
	    	    offset:['200px', ''],
	    	    area:['800px','500px'],
	    	    iframe: {src: '${cxt}/orders/orderFirstReviews/'+orderId}
	    	});
    	}
    }
    
    function delOrder(id){
    	layer.prompt({title: '取消订单',type: 3,length: 200}, function(val,index){
    		$.ajax({
    			url:"${cxt}/orders/delOrder",
    			type:"post",
    			data:{"orderId":id,"reason":val},
    			dataType:"json",
    			success:function(data){
    				if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
    					layer.close(index)
    					window.location.reload();				
    				}else{
    					layer.msg(data.msg, 2, 5);
    				}
    			}
    		});
		});
    }

    function paying(orderId,zks,yjks){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "交费",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    offset:['100px', ''],
    	    area: ['700px', '600px'],
    	    iframe: {src: '${cxt}/payment/toPayment/'+orderId}
    	});
    }
    /*购课*/ 
    function tiaoke(orderId){
    	$.layer({
    		type:2,
    		title:"${_res.get('Purchase.of.course')}",
    		closeBtn:[0,true],
    		shade:[0.5,'#000'],
    		shadeClose:true,
    		area:['700px','460px'],
    		iframe:{src:'${cxt}/orders/tiaoke/'+orderId}
    	});
    }
    function modify(orderId){
    	window.location.href="${cxt}/orders/edit/"+orderId;
    }
    function showPayment(orderId){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "订单信息",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    offset:['100px', ''],
    	    area:['800px','500px'],
    	    iframe: {src: '${cxt}/payment/paymentList/'+orderId}
    	});
    }
    
    function showDelReason(orderId){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "订单取消原因",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    offset:['160px', ''],
    	    area: ['700px', ''],
    	    iframe: {src: '${cxt}/orders/showOrderReviews/'+orderId}
    	});
    }
    var state = "${paramMap['_query.type']}";
    if(state==1||state==""){
   	 $("#xh_1").removeClass("btn-outline");
    }
    if(state==2){
   	 $("#xh_2").removeClass("btn-outline");
    }
    </script>
</body>
</html>