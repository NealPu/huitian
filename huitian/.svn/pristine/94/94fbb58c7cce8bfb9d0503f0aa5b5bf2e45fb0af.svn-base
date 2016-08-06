<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get('teacher.group.add')}/编辑公众号</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<style type="text/css">
<style type="text/css">
body {
	background-color: #eff2f4;
}
textarea {
	width: 50%;
}
label {
	width: 80px;
}
</style>
</head>
<body style="overflow: hidden;">
		<div class="ibox-content">
				<form id="form" action="" method="post">
					<input type="hidden" id="id" name="wpnumber.id" value="${wpnumber.id }"/>
						<p>
							<label>公众号名称：</label>
							<input type="text" id="accountName" name="wpnumber.accountname" value="${wpnumber.accountname }" size="20" maxlength="100"/>
							<span id="accountNameInfo" style="color: red;">*</span>
						</p>
						<p>
							<label>微信号：</label>
							<input type="text" id="accountNumber" name="wpnumber.accountnumber" value="${wpnumber.accountnumber }" size="20" maxlength="100"/>
							<span id="accountNumberInfo" style="color: red;">*</span>
						</p>
						<p>
							<label>AppId：</label>
							<input type="text" id="appId" name="wpnumber.appid" value="${wpnumber.appid }" size="50" maxlength="100"/>
							<span id="appIdInfo" style="color: red;">*</span>
						</p>
						<p>
							<label>AppSecret：</label>
							<input type="text" id="appSecret" name="wpnumber.appsecret" value="${wpnumber.appsecret }" size="50" maxlength="100"/>
							<span id="appSecretInfo" style="color: red;">*</span>
						</p>
						<p>
							<label>Token：</label>
							<input type="text" id="token" name="wpnumber.token" value="${wpnumber.token }" size="50" maxlength="100"/>
							<span id="tokenInfo" style="color: red;">*</span>
						</p>
						<p>
							<label>类型：</label>
							<input type="radio" id="accounttype" name="wpnumber.accounttype" value="1" ${wpnumber.accounttype==null?"checked='checked'":(wpnumber.accounttype == 1 ? "checked='checked'":"") } >订阅号
							<input type="radio" id="accounttype" name="wpnumber.accounttype" value="2" ${wpnumber.accounttype == 2 ? "checked='checked'":"" } >服务号
							<input type="radio" id="accounttype" name="wpnumber.accounttype" value="3" ${wpnumber.accounttype == 3 ? "checked='checked'":"" } >企业号
						</p>
						<p>
						<c:if test="${operatorType eq 'add'}">
							<input type="button" value="${_res.get('save')}" onclick="return save();" class="btn btn-outline btn-success" />
						</c:if>
						<c:if test="${operatorType eq 'update'}">
							<input type="button" value="${_res.get('update')}" onclick="return save();" class="btn btn-outline btn-primary" />
						</c:if>
						</p>
				</form>
			</div>
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
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
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.iframeAuto(index);
		function save() {
				var id = $("#id").val();
				var accountName = $("#accountName").val();
				var accountNumber = $("#accountNumber").val();
				var appId = $("#appId").val();
				var appSecret = $("#appSecret").val();
				var token = $("#token").val();
				if(accountName==''||accountNumber==''||appId==''||appSecret==''||token==''){
					parent.layer.msg("请正确填写", 1,1);
					return false;
				}
				if(id==""){
					path="${cxt}/weixin/wpnumber/save";
				}else{
					path="${cxt}/weixin/wpnumber/update";
				}
				if(confirm("确定要提交保存/更新公众号吗？")){
					 $.ajax({
				            cache: true,
				            type: "POST",
				            url:path,
				            data:$('#form').serialize(),// 你的formid
				            async: false,
				            error: function(request) {
				            	parent.layer.msg("网络异常，请稍后重试。", 1,1);
				            },
				            success: function(data) {
					    		parent.layer.msg(data.msg, 2,1);
					    		if(data.code=='1'){//成功
					    			setTimeout("parent.layer.close(index)", 1000 );
									parent.window.location.reload();
					    		} 
				            }
				        });
				}
			}
	</script>
</body>
</html>