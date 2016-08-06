<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
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
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<script src="/js/js/jquery-2.1.1.min.js"></script>
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
	<div id="wrapper" style="background: #2f4050;">
	  <%@ include file="/common/left-nav.jsp"%>
			<div class="ibox float-e-margins">
					<div class="ibox-content" style="height:100%">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
								    <th><input type="checkbox" id="check" onclick="xuanze()" ></th>
									<th>${_res.get("index")}</th>
									<th>咨询顾问</th>
									<th>${_res.get('gender')}</th>
									<th>用户类型</th>
									<th>未成单数量</th>
									<th>${_res.get('District')}</th>
								</tr>
							</thead>
							<c:forEach items="${kcgws}" var="kcgw" varStatus="index">
								<tr align="center">
								    <td><input type="checkbox" id="box" value="${kcgw.id }"></td>
									<td>${index.count}</td>
									<td>${kcgw.real_name}</td>
									<td>${kcgw.xingbie}</td>
									<td></td>
									<td>${kcgw.shu}</td>
									<td>${kcgw.campus_name}</td>
								</tr>
							</c:forEach>
						</table>
						<input type="hidden" name="ids" value="${oppids}" id="ids"/>
						<input type="button" value="${_res.get('save')}"  id="save" style="position: absolute;right:15px;"/>
						<div id="splitPageDiv">
							<jsp:include page="/common/splitPage.jsp" />
						</div>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script type="text/javascript">
    $("#save").click(function() {
    	var opp=[];
    	$("input[id='box']:checked").each(function(){
    		opp.push($(this).val());
    	})
    	
    	if(opp.length==0){
    		layer.msg("请选择");
    		return false;
    	}
    	var ids=$("#ids").val();
			$.ajax({
				cache : true,
				type : "POST",
				url : "/opportunity/allotSave?kcgwsids="+opp,
				data : {
					'oppids' : ids
				},// 你的formid
				async : false,
				error : function(request) {
					parent.layer.msg("网络异常，请稍后重试。", 1, 1);
				},
				success : function(data) {
					parent.layer.msg(data.msg, 6, 0);
					if (data.code == '1') {//成功
						parent.window.location.reload();
						setTimeout("parent.layer.close(index)", 1000);
					}
				}
			});
	});
    function xuanze(){
    	if($("#check").is(":checked")){
    		$("[id='box']").each(function(){
    			$(this).prop("checked","checked");
    		})
    	}else{
    		$("[id='box']").each(function(){
    			$(this).prop("checked",false);
    		})
    	}
    }
    function feedback(opportunityId){
	    	$.layer({
	    	    type: 2,
	    	    shadeClose: true,
	    	    title: "反馈详情",
	    	    closeBtn: [0, true],
	    	    shade: [0.5, '#000'],
	    	    border: [0],
	    	    area: ['600px', '480px'],
	    	    iframe: {src: '${cxt}/opportunity/feedList/'+opportunityId}
	    	});
    }
    
    function isconver(opportunityId){
    	layer.confirm('确认该销售机会已成单吗？', function(){
    		$.ajax({
    			url:"${cxt}/opportunity/isConver",
    			type:"post",
    			data:{"opportunityId":opportunityId},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
						$('#isconver_'+opportunityId).text("${_res.get('Is.a.single')}");
						$('#isconver_status_'+opportunityId).val(true);
					}else{
    					layer.msg(data.msg, 2, 5);
					}
    			}
    		});
    	});
    }
    function addstu(opportunityId){
    	var isconver = $('#isconver_status_'+opportunityId).val();
    	if(isconver=='false'){
    		layer.msg("未成单不能添加学生！", 2, 5);
    	}else{
    		window.location.href='${cxt}/opportunity/toAddStudent/'+opportunityId;
    	}
    }    
    </script>
    <script type="text/javascript">
    	$(function(){
    		if(${1 == paramMap['_query.source'] }){
    			$("#mediatorSearch").show();
    		}else{
    			$("#mediatorSearch").hide();
    		}
    	});
    	
    	function chooseSource(id){
			if(id==1){
				$("#mediatorSearch").show();
			}else{
				$("#mediatorSearch").hide();
			}
		}
    	
    </script>
     
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
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script>
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>