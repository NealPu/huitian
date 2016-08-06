<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<body style="background:#EFF2F4">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0;position:fixed;width:100%;background-color:#fff;">
			<div class="navbar-header" style="padding: 15px 0 0 30px;">
				<p><label>选择回访日期:</label></p>
			</div>
			</nav>
		</div>
		<div class="col-lg-9" style="margin-top: 80px;width:350px;">
			<div class="ibox float-e-margins">
				<div class="ibox-content" >
					<form action="" method="post" id="coursePlan">
						<fieldset style="width: 100%; overflow: hidden;">
							<p>
								<label>回访日期：</label> 
								<input type="text" class="form-control layer-date" readonly="readonly" id="time" name="time" value="${time}" style="margin-top: 8px;" />
							</p>
							<p>
								<input type="button" class="btn btn-outline btn-primary" value="${_res.get('admin.common.determine')}" onclick="sureTime();" /> 
							</p>
						</fieldset>
					</form>
				</div>
			</div>
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
				min: laydate.now(),
				max: '2099-06-16',
				istime : false,
				istoday : false,
			};
		laydate(time);
		
	</script>
	
	<script type="text/javascript">
		
		function sureTime(){
			var index = parent.layer.getFrameIndex(window.name);
			var id = parent.$("#brokerageid").val();
			var oppIdValue = [];
			parent.$('input[name="opportunityids"]:checked').each(function() {
				oppIdValue.push($(this).val());
			});
			$.ajax({
				url:"/opportunity/sureRebackTime",
				data:{
					"oppids":oppIdValue.join(","),
					"time":$("#time").val()
				},
				type:"post",
				dataType:"json",
				success:function(data){
					parent.window.location.reload();
					if(data.code==1){
						parent.layer.msg("确认成功",2,6);
						parent.layer.close(index);
					}else{
						parent.layer.msg("出现故障，请联系管理员.",2,7);
					}
					
				}
				
			});
			
		}
		
	</script>
 	
</body>
</html>