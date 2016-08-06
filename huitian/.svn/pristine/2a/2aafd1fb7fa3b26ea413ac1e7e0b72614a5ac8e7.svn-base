<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>交费</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<style type="text/css">
body {
	background-color: #eff2f4;
}
.student_list_wrap {
	position: absolute;
	top: 28px;
	left: 14.8em;
	width: 130px;
	overflow: hidden;
	z-index: 2012;
	background: #09f;
	border: 1px solide;
	border-color: #e2e2e2 #ccc #ccc #e2e2e2;
	padding: 6px;
}
.student_list_wrap li {
	display:block;
	line-height: 20px;
	padding: 4px 0;
	border-bottom: 1px dashed #E2E2E2;
	color: #FFF;
	cursor: pointer;
	margin-right:38px;
}
</style>
</head>
<body>
	<div id="wrapper">
		<div class="ibox-content">
			<input type="hidden" id="realsum" name="payment.id" value="${orders.realsum }"/>
			<input type="hidden" id="paidamount" name="payment.id" value="${orders.paidamount==null?0:orders.paidamount }"/>
			<table width="80%" class="table table-hover table-bordered">
				<thead>
					<tr>
						<th>${_res.get("student.name")}</th>
						<th>${_res.get('type.of.class')}</th>
						<th>${_res.get('course.subject')}/${_res.get('classNum')}</th>
						<%-- <th>${_res.get('total.amount')}</th> --%>
						<th>${_res.get('actual.amount')}</th>
						<%-- <th>${_res.get('discount.amount')}</th> --%>
						<th>${_res.get('total.sessions')}</th>
						<th>交费次数</th>
						<th>已交金额</th>
					</tr>
				</thead>
				<tr align="center">
					<td>${orders.studentname}</td>
					<td>${orders.teachtype==1?_res.get("IEP"):_res.get('course.classes')}</td>
					<td>${orders.teachtype==1?orders.subjectname:orders.classnum}</td>
					<%-- <td>${orders.totalsum}</td> --%>
					<td>${orders.realsum}</td>
					<%-- <td>${orders.rebate}</td> --%>
					<td>${orders.classhour}</td>
					<td>${orders.paycount}</td>
					<td>${orders.paidamount}</td>
				</tr>
			</table>
				<form id="paymentForm" action="" method="post">
					<fieldset style="width: 100%;">
						<input type="hidden" id="orderId" name="payment.orderid" value="${orders.id }"/>
						<input type="hidden" id="paymentId" name="payment.id" value="${payment.id }"/>
						<c:choose>
							<c:when test="${orders.teachtype==1}">
								<p>
									<label>交费金额：</label>
									<input type="text" id="amount" name="payment.amount" value="${payment.amount}"     size="20" maxlength="15"/>
									<span id="amountInfo"></span>
								</p>
							</c:when>
							<c:otherwise>
								<p>
									<label>交费金额：</label>
									<input type="text" id="amount" name="payment.amount" value="${payment.amount}"   size="20" maxlength="15"/>
									<%-- <input type="text" id="amount" name="payment.amount" readonly="readonly" value="${payment.amount}"  <c:if test="${isconfirm eq true }"> readonly="readonly" </c:if> size="20" maxlength="15"/> --%>
								</p>
							</c:otherwise>
						</c:choose>
						<p>
							<label>是否收款：</label>
								<input type="radio" id="isPay" name="payment.ispay" value="1" checked="checked">已收
								<c:if test="${!isconfirm}">
									<input type="radio" id="isPay" name="payment.ispay" value="0">未收
								</c:if>
						</p>
						<p id="jffs">
							<label>交费方式：</label>
								<input type="radio" id="payType" name="payment.paytype" value="1" checked='checked'>现金
								<input type="radio" id="payType" name="payment.paytype" value="2">银行转账
								<input type="radio" id="payType" name="payment.paytype" value="3">POS机
								<input type="radio" id="payType" name="payment.paytype" value="4">${_res.get('Else')}
						</p>
						<p id="jfrq">
							<label>交费日期：</label> <input id="paydate" type="text" name="payment.paydate" readonly="readonly" value="${payment.paydate}" size="20"  size="15"/>
							<span id="paydateInfo" style="color: red;"></span>
						</p>
						<p>
							<label>${_res.get('course.remarks')}：</label>
							<textarea id="gross" name="payment.remark" cols="50" rows="7"></textarea><span id="grossInfo" style="color: red;"></span>
						</p>
						<c:if test="${operator_session.qx_paymentconfirm || operator_session.qx_paymentsavePayment }">
						<p>
							<input id="saveButton" type="button" value="${_res.get('save')}" class="btn btn-outline btn-primary" />
						</p>
						</c:if>
					</fieldset>
				</form>
	</div>
	</div>
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
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
		//日期范围限制
		var paydate = {
			elem : '#paydate',
			format : 'YYYY-MM-DD',
			isclear: false, 
			max : laydate.now(), //最大日期
			istime : false,
			istoday : true
		};
		laydate(paydate);
		
	var index = parent.layer.getFrameIndex(window.name);
	
	if('${payment}'==''){
		var realsum = $("#realsum").val();
		var paidamount = $("#paidamount").val();
		var ye=parseFloat(realsum)-parseFloat(paidamount);
		$("#classhour").val('${orders.classhour}');	
		$("#amount").val(ye);	
		$("#paydate").val(laydate.now());	
	}else{
		if('${isconfirm}'){
			$("#paydate").val(laydate.now());
		}
	}
	
	$(function() {
		/* $('#amount').keyup(function() {
			var amount = $(this).val();
			var realsum = $("#realsum").val();
			var paidamount = $("#paidamount").val();
			if(amount.match(/^([1-9]\d*|[0]{1,1})$/)==null){
				$("#amountInfo").text("请输入正确的收费金额");
			}else{
				var ye=parseInt(realsum)-parseInt(paidamount);
				if(parseInt(amount)>parseInt(ye)){
					$("#amountInfo").text("交费金额不能大于应交费用。");
				}else{
					$("#amountInfo").text("");
				}
			}
		}); */
	});
	
	parent.layer.iframeAuto(index);
	$('#saveButton').click(function(){
		var amount = $("#amount").val();
		var path = "";
		if('${isconfirm}'){
			path="${cxt}/payment/confirm";
		}else{
			path="${cxt}/payment/savePayment";
		}
		if(amount.match(/^[0-9]+(.[0-9]{1,2})?$/)==null){
			$("#amountInfo").text("请输入正确的收费金额，不能含有小数");
		}else{
			if(amount<0){
				$("#amountInfo").text("收费金额不能为0.");
			}else{
				if(confirm("确定提交？")){
				    $.ajax({
			            cache: true,
			            type: "POST",
			            url:path,
			            data:$('#paymentForm').serialize(),// 你的formid
			            async: false,
			            error: function(request) {
			            	parent.layer.msg("网络异常，请稍后重试。", 1,1);
			            },
			            success: function(data) {
				    		parent.layer.msg(data.msg, 6,0);
				    		if(data.code=='1'){//成功
				    			parent.window.location.reload();
				    			setTimeout("parent.layer.close(index)", 1000 );
				    		}
			            }
			        });
				}
			}
		}
	});
	
	$(function() {
		$("input[id='isPay']").click(function() {
			var ispay = $(this).val();
			if(ispay=='0'){
				$("#paydate").val("");
				$("#jffs").hide();
				$("#jfrq").hide();
			}else{
				$("#paydate").val(laydate.now());
				$("#jffs").show();
				$("#jfrq").show();
			}
		});
	});
	</script>
</body>
</html>