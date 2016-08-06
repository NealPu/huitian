<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">

<link href="/css/css/animate.css" rel="stylesheet">
<script type="text/javascript" src="/js/js/jquery-2.1.1.min.js"></script>

<script type="text/javascript">

</script>

<style type="text/css">
.stu_name {
	position: relative;
	margin-bottom: 15px;
}
.stu_name label {
	display: inline-block;
	font-size: 12px;
	vertical-align: middle;
	width: 100px;
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
	margin-right:38px;;
}

label {
	width: 100px;
}
</style>

</head>
<body>
				<div class="ibox-content" style="width:100%;padding-top:20px;overflow: hidden;height:420px">
					<form action="" method="post" id="feedBackForm">
					<input type="hidden" id="oppid" name="feedback.opportunityid" value="${opportunity.id }">
					<input type="hidden" id="oppid" name="opportunity.id" value="${opportunity.id }">
					<input type="hidden" id= "num" name="num" value="${num}">
						<fieldset style="width: 100%; overflow: hidden;">
							<p>
								<label>${_res.get("Contacts")}：</label>
								<input type="text" id="contacter" name="opportunity.contacter" readonly="readonly" style="width: 150px" value="${opportunity.contacter }" size="20" />
							</p>
							<input type="hidden" id="s"  value="${code}"/>
							<br>
							<p>
								<label>回访日期：</label> 
								<input type="text" class="form-control layer-date"  readonly id="time" name="time" value="${time}" style="margin-top: -11px;width: 150px" />
							</p>
							<p>
							
								<label>回访结果：</label> 
								<select name="feedback.callresult" id="callresult" class="chosen-select" style="width: 150px" tabindex="2">
									<c:if test="${opportunity.isconver eq '1'}">
										<option value="1" selected="selected"  >${_res.get('Is.a.single')}</option>
									</c:if>
									<c:if test="${opportunity.isconver  ne '1'}">
										<!-- <option value="0" selected="selected" >${_res.get("Opp.No.follow-up")}</option> -->
										<option value="2" selected="selected" >${_res.get('Opp.Followed.up')}</option>
										<option value="3" >考虑中</option>
										<option value="4" >无意向</option>
										<option value="6" >有意向</option>
									</c:if>
								</select>
							</p>
							<p>
								<label>客户等级：</label>
								<select id="customer_rating" class="chosen-select" style="width: 150px;" tabindex="2" name="opportunity.customer_rating"   >
									<option value="0" <c:if test="${opportunity.customer_rating eq '0'}">selected="selected"</c:if>>未知客户</option>
									<option value="1" <c:if test="${opportunity.customer_rating eq '1'}">selected="selected"</c:if>>潜在客户</option>
									<option value="2" <c:if test="${opportunity.customer_rating eq '2'}">selected="selected"</c:if>>目标客户</option>
									<option value="3" <c:if test="${opportunity.customer_rating eq '3'}">selected="selected"</c:if>>发展中客户</option>
									<option value="4" <c:if test="${opportunity.customer_rating eq '4'}">selected="selected"</c:if>>交易客户</option>
									<option value="5" <c:if test="${opportunity.customer_rating eq '5'}">selected="selected"</c:if>>后续介绍客户</option>
									<option value="6" <c:if test="${opportunity.customer_rating eq '6'}">selected="selected"</c:if>>非客户</option>
								</select>
							</p>
							<br>
							<p>
								<label>下次回访日期：</label> 
								<input type="text" class="form-control layer-date" readonly="readonly" id="nexttime" name="nexttime" value="${nexttime}" style="margin-top: -11px;width: 150px; background-color: #fff;" />
							</p>
							<br>
							<p>
								<label>${_res.get('course.remarks')}：</label> 
								<br>
								<textarea rows="3" cols="85" name="feedback.content"  style="width:300px;overflow-x: hidden; overflow-y: scroll;">${fn:trim(feedback.content)}</textarea>
							</p>
							<p>
								<c:if test="${operator_session.qx_opportunitytoSaveFeedBack }">
									<input type="button" class="btn btn-outline btn-primary" value="${_res.get('admin.common.determine')}" onclick="sureTime();" /> 
								</c:if>
							</p>
						</fieldset>
					</form>
				</div>

	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		layer.use('extend/layer.ext.js'); //载入layer拓展模块
	</script>
 	<script type="text/javascript">
	 	var time = {
				elem : '#time',
				format : 'YYYY-MM-DD',
				max: laydate.now(),
				istime: false,
				istoday:true,
			};
	 	var nexttime = {
				elem : '#nexttime',
				format : 'YYYY-MM-DD',
				max: '2099-06-16',
				min: laydate.now(),
				istime : false,
				istoday : false,
			};
	 	laydate(time);
		laydate(nexttime);
		
	</script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
	$(".chosen-select").chosen({disable_search_threshold:30});
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
	<script type="text/javascript">
		
		function sureTime(){
			var callresult = $("#callresult").val();
			var customer_rating = $("#customer_rating").val();
			var stime = $("#nexttime").val();
			if(stime==''&&(callresult==2||callresult==3||callresult==6)){
					alert("请选择下次回访日期");
					return;
			} else{
				var index = parent.layer.getFrameIndex(window.name);
				var code = parseInt($("#s").val());
				var oppid = $("#oppid").val();
				var num = $("#num").val();
				var nexttime = $("#nexttime").val();
				var str = "s_"+oppid;
				var s = "z_"+oppid;
				var ss = "n_"+oppid;
				var cr = "r_"+oppid;
				//alert(str);
				$.ajax({
					url:"/opportunity/toSaveFeedBack",
					data:$('#feedBackForm').serialize(),
					type:"post",
					dataType:"json",
					success:function(data){
						//window.returnValue = 'OK'
						//window.parent.document.getElementById("s").value=1;
						window.parent.document.getElementById(str).value=code+1;
						if(num==1){
							window.parent.document.getElementById(ss).innerHTML=nexttime;
							window.parent.document.getElementById(cr).innerHTML=callresult==0?"未知客户":callresult==1?"潜在客户":callresult==2?"目标客户":callresult==3?"发展中客户":callresult==4?"交易客户":callresult==5?"后续交易客户":"非客户";
						}
						window.parent.document.getElementById(s).innerHTML=callresult==1?"${_res.get('Is.a.single')}":callresult==2?"${_res.get('Opp.Followed.up')}":callresult==3?'考虑中':callresult==4?'无意向':'有意向';
						window.parent.document.getElementById(s).className=callresult==1?'btn btn-success btn-xs':callresult==2?'btn btn-primary btn-xs':callresult==3?'btn btn-danger btn-xs':callresult==4?'btn btn-default btn-xs':'btn btn-warning btn-xs';
						if(data.code==1){
							//parent.window.location.reload();
							parent.layer.msg("确认成功",1,1);
							parent.layer.close(index);
						}else{
							parent.layer.msg("出现故障，请联系管理员.",2,7);
						}
						 
					}
				});
			}
		}
		
	</script>
 	
</body>
</html>