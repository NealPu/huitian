<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<style type="text/css">
.error {
	color: red;
	width: 150px;
}

label {
	width: 65px;
}
.chosen-results{
    max-height:90px !important
}
.bangding{
   position: absolute;
   top:28px;
   left:240px
}
</style>
<title>绑定</title>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;height:195px;overflow-y: hidden;">
		<div class="ibox-content">
		              <div style="margin: 15px 0 25px 0">
						<form action="" method="post" >
						    <label>绑定渠道</label>
						    <input type="hidden" value="${id}" name="id" id="id">
						    <select name="realname" id="realname" class="chosen-select" style="width:130px">
						        <option value="">请选择</option>
						        <c:forEach items="${crmlist}" var="cm">
						           <c:if test="${cm!=null }">
						             <option value="${cm.realname}">${cm.realname }</option>
						           </c:if>
						        </c:forEach>
						    </select>
						    &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;
						    <div class="bangding"><input type="submit" value="绑定" id="bandbut" class="btn btn-outline btn-primary" /></div>
						</form>
					</div>
		
						<table class="table table-hover table-bordered" width="100%">
							<thead>
								<tr>
									<th rowspan="2">昵称</th>
									<th rowspan="2">${_res.get('admin.dict.property.status')}</th>
									<th rowspan="2">${_res.get('gender')}</th>
									<th rowspan="2">关注日期</th>
								</tr>
							</thead>
							<c:forEach items="${userlist}" var="user" varStatus="status">
								<tr class="odd" align="center">
									<td>${user.nickname}</td>
									<td>${user.isbound==0?'未绑定':'已绑定'}</td>
									<td>${user.xingbie}</td>
									<td><fmt:formatDate value="${user.guanzhuTime}" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
							</c:forEach>
						</table>   
						 
					</div>
					
					</div>
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
		layer.use('extend/layer.ext.js'); //载入layer拓展模块
	</script>
	<script>
		$(".chosen-select").chosen({
			disable_search_threshold : 10
		});
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
	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
	<script type="text/javascript">
	$("#bandbut").click(function() {
		var id = $("#id").val();
		var realname = $("#realname").val();
		if(""!=realname){
			$.ajax({
				cache : true,
				type : "POST",
				url : "/weixin/user/bandok",
				data : {
					'id' : id,
					'realname' : realname
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
		}else{
			alert("请选择渠道");
		}
	});
	</script>
	<script>
		$('li[ID=nav-nav13]').removeAttr('').attr('class', 'active');
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.iframeAuto(index);
	</script>
</body>
</html>