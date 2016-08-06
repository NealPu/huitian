<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>短信设置</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<script src="/js/js/jquery-2.1.1.min.js"></script>
<script src="/js/common.js"></script>
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

	    <div class="margin-nav" style="min-width:1050px;width:100%;">	
		<form action="" method="post" id="searchForm">

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
				    <div class="ibox-title" style="margin-bottom:20px">
						<h5><img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
						<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
						 &gt;<a href='/smsmessage/index'>短信管理</a> &gt; 短信设置</h5>
						 <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
					</div>
				<div class="ibox-title">
					<h5>服务商列表</h5>
					<c:if test="${operator_session.qx_operatoraddRoles }">
						<input type="button"  class="navbar-right btn btn-outline btn-primary input_right" style="margin:-10px 10px 0 0;float: right;" value="${_res.get('teacher.group.add')}" onclick="addProvider()">
					</c:if>
				</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered" style="text-align:center;">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>服务商</th>
									<th>账号</th>
									<th>密码</th>
									<th>状态</th>
									<th>短信剩余</th>
									<th>Host</th>
									<th>AddRess</th>
									<th>操作</th>
								</tr>
							</thead>
							<c:forEach items="${list}" var="s" varStatus="index">
								<tr>
									<td>${index.count }</td>
									<td>${s.service_provider}</td>
									<td>${s.sms_user}</td>
									<td>${s.sms_password}</td>
									<td>${s.sms_state==0?"未使用":"使用中"}</td>
									<td>${s.sms_surplus}</td>
									<td>${s.sms_servicesHost}</td>
									<td>${s.sms_servicesRequestAddRess}</td>
									<td>
										 <c:if test="${operator_session.qx_smssettingsupdate }">
											<a href="#" style="color: #515151" onclick="update(${s.id})">编辑</a> |
										</c:if>
										<c:if test="${s.sms_state==0}">
											<c:if test="${operator_session.qx_smssettingsupdateState }">
												<a href="#" style="color: #515151" onclick="updateState(${s.id})">使用</a> 
											</c:if>
										</c:if>
										<c:if test="${s.sms_state==1}">
											<c:if test="${operator_session.qx_smssettingsupdateState }">
												<a href="#" style="color: #515151" onclick="updateState(${s.id})">停用</a> 
											</c:if>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
		</form>
	 </div>
	 </div>	
	</div>

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
    
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/campus.js"></script>
    <script>
       $('li[ID=nav-nav18]').removeAttr('').attr('class','active');
    </script>
    
    <script type="text/javascript">
/*新添加*/
    function addProvider(){
	    $.layer({
		    type: 2,
		    shadeClose: true,
		    title: "添加服务商及其配置信息",
		    closeBtn: [0, true],
		    shade: [0.5, '#000'],
		    border: [0],
		    offset:['20px', ''],
		    area: ['350px', '500px'],
		    iframe: {src: "${cxt}/smssettings/add"}
		});
    }
    
/*修改*/
    function update(id){
    	  $.layer({
  		    type: 2,
  		    shadeClose: true,
  		    title: "修改服务商配置信息",
  		    closeBtn: [0, true],
  		    shade: [0.5, '#000'],
  		    border: [0],
  		    offset:['20px', ''],
  		    area: ['500px', '500px'],
  		    iframe: {src: "${cxt}/smssettings/update/"+id}
  		});
    }
    
/*修改状态*/
    var index = parent.layer.getFrameIndex(window.name);
	parent.layer.iframeAuto(index);
    function updateState(id){
    	$.layer({
    		    shade : [0], 
    		    area : ['auto','auto'],
    		    dialog : {
    		        msg:'您确定修改当前状态吗？',
    		        btns : 2, 
    		        type : 4,
    		        btn : ['确定','取消'],
    		        yes : function(){
  		            	$.ajax({
			        		url:'/smssettings/updateState',
			        		type:'post',
			        		data:{"id":id},
			        		dataType:'json',
			        		success:function(date){
			        			if(date.code==1){
			        				parent.layer.msg("状态更改成功",3,1);
			        				parent.layer.close(index);
			        				setTimeout("parent.window.location.reload()",1000);
			        			}else{
			        				parent.layer.msg("状态更改异常")
			        			}
			        		}
			        	});
    		        },
    		        no : function(){
    		            
    		        }
    		    }
    		});
    }
    
    
    </script>
</body>
</html>