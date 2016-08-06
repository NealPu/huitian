<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">


<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>添加服务商及配置信息</title>

<style type="text/css">
label{
  height:34px;
  width:80px;
}
.subject_name{
  width:520px;
  margin:-50px 0 0 82px;
}
.class_type{
  margin:-50px 0 40px 82px;
}
#classtype div{
  float:left;
  margin-right:15px  
} 
.student_list_wrap {
	position: absolute;
	top: 100px;
	left: 9.5em;
	width: 100px;
	overflow: hidden;
	z-index: 2012;
	background: #09f;
	border: 1px solide;
	border-color: #e2e2e2 #ccc #ccc #e2e2e2;
	padding: 6px;
}
p{
   width:300px
}
</style>
</head>
<body style="background:#fff">
        <div class="ibox-content">
            <form action="${cxt }/smssettings/save" method="post" id="addprovider" >
					<input type="hidden" name="smsSettings.id" id="id" value="${sms.id}"> 
					<fieldset>
						<div class="stu_name">
							<p>
								<label>服务商:</label> 
								<input type="text" id="service_provider" name="smsSettings.service_provider" value="${sms.service_provider}" />
							</p>
							<p>
								<label>账号:</label> 
								<input type="text" id="user" name="smsSettings.sms_user" value="${sms.sms_user}" />
							</p>	
							<p>
								<label>密码:</label> 
								<input type="text" id="password" name="smsSettings.sms_password" value="${sms.sms_password}" />
							</p>
							<%-- <p>	
								<label>状态:</label> 
								<input type="radio" id="state" name="smsSettings.sms_state"  value="1"  <c:if test="${sms.sms_state=='1'}">checked="checked"</c:if> />使用中
								<input type="radio" id="state" name="smsSettings.sms_state" value="0"  <c:if test="${sms.sms_state=='0'}">checked="checked"</c:if>/>未使用
							</p> --%>
							<p>	
								<label>接口:</label> 
								<input type="text" id="connector" name="smsSettings.sms_connector" value="${sms.sms_connector}" />
							</p>
								<p>	
								<label>servicesHost:</label> 
								<input type="text" id="connector" name="smsSettings.sms_servicesHost" value="${sms.sms_servicesHost}" />
							</p>
								<p>	
								<label>RequestAddRess:</label> 
								<input type="text" id="connector" name="smsSettings.sms_servicesRequestAddRess" value="${sms.sms_servicesRequestAddRess}" />
							</p>
							<p>	
								<label>短信条数:</label> 
								<input type="text" id="sum" name="smsSettings.sms_sum" value="${sms.sms_sum}" />
							</p>
							<c:if test="${operator_session.qx_smssettingssave }">
								<c:if test="${type eq 'add'}">
									<input type="button" value="${_res.get('save')}" onclick="return save();" class="btn btn-outline btn-success" />
								</c:if>
							</c:if>
							<c:if test="${operator_session.qx_smssettingsupdate }">
								<c:if test="${type eq 'update'}">
									<input type="button" value="${_res.get('update')}" onclick="return save();" class="btn btn-outline btn-success" />
								</c:if>
							</c:if>
						</div>
					</fieldset>
				</form>
        </div>

<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script>
       $('li[ID=nav-nav6]').removeAttr('').attr('class','active');
    </script>
    <script type="text/javascript">
			function save(){
				 $.ajax({
		            	type:"post",
						url:"${cxt}/smssettings/save",
						data:$('#addprovider').serialize(),
						dataType:"json",
						success : function(date) {
							 if(date.code==1){
								parent.layer.msg("成功",1,1);
								setTimeout("parent.layer.close(index)", 1000);
								parent.window.location.reload();
							}else{
								parent.layer.msg("异常丶请联系管理员",2,8);
							} 
						}
		            });
			}
			
			//弹出后子页面大小会自动适应
		       var index = parent.layer.getFrameIndex(window.name);
		       parent.layer.iframeAuto(index);
	</script>
</body>
</html>