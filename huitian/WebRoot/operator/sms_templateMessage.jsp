<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>短信模版</title>
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
.laydate_body .laydate_bottom{
    height:30px !important
}
.laydate_body .laydate_top{
    padding:0 !important
}
tr td{
  text-align: center;
}
.textleft{
  text-align: left;
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
		<form action="/smstemplate/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
					      &gt;<a href='/smsmessage/index'>短信管理</a> &gt;短信模版
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>模版名称：</label>
					<select name="sms_name" id="sms_name" class="chosen-select" style="width:150px;" 
								tabindex="2" >
								 <option value="">全部</option>
							     <option value="0" ${name eq '0'?"selected='selected'":""}>下发课表</option>
								 <option value="1" ${name eq '1'?"selected='selected'":""}>取消排课</option>
							</select>
				<label>接收用户：</label>
				<select id="sms_type" class="chosen-select" style="width: 120px;" tabindex="2" name="sms_type"   >
					<option value="0" >全部</option>
					<option value="1" ${type eq '1'?"selected='selected'":""}>${_res.get('student')}</option>
					<option value="2" ${type eq '2'?"selected='selected'":""}>${_res.get('teacher')} </option>
					<option value="3" ${type eq '3'?"selected='selected'":""}>${_res.get('Patriarch')}</option>
				</select>
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				<input type="button" value="添加" class="btn btn-outline btn-success" onclick="add()">
			</div>
			</div>
			</div>
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>短信记录</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
								<tr>
									<th width="3%">${_res.get("index")}</th>
									<th width="8%">模版名称</th>
									<th width="10%">模版编号</th>
									<th width="7%">接收用户</th>
									<th width="30%">中文模版</th>
									<th width="30%">英文模版</th>
									<th width="12%">操作</th>
								</tr>
							</thead>
							<c:forEach items="${list}" var="sms"  varStatus="index">
								<tr>
									<td>${index.count}</td>
									<td>${sms.sms_name}</td>
									<td>${sms.numbers}</td>
									<td>${sms.sms_type==1?_res.get('student'):sms.sms_type==2?_res.get('Patriarch'):_res.get('teacher')}</td>
									<td class="textleft">${sms.sms_ch_style}</td>
									<td class="textleft">${sms.sms_en_style}</td>
									<td>
										  <%-- <a href="javascript:void(0)" onclick="deleteTemplate(${sms.id})">删除</a>| --%>
										<a href="javascript:void(0)" onclick="update(${sms.id})">修改</a>|
										<c:if test="${sms.sms_state == 0}">
											<a href="javascript:void(0)" onclick="updateState(${sms.id}+',1')">使用</a>
										</c:if>
										<c:if test="${sms.sms_state ==1}">
											<a href="javascript:void(0)" onclick="updateState(${sms.id}+',2')">停用</a>
										</c:if> 
									</td>
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
    <script>
     function add(){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "短信模版设置",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['550px', '800px'],
    	    offset : ['120px', ''],
    	    iframe: {src: '${cxt}/smstemplate/add'}
    	});
    }
     
     function update(id){
     	$.layer({
     	    type: 2,
     	    shadeClose: true,
     	    title: "短信模版设置",
     	    closeBtn: [0, true],
     	    shade: [0.5, '#000'],
     	    border: [0],
     	    area: ['550px', '800px'],
     	    offset : ['120px', ''],
     	    iframe: {src: '${cxt}/smstemplate/update/'+id}
     	});
     }
    
     function updateState(id){
    	 var  code = id.substr(id.length-1,id.length); 
    	 var str ="";
    	 if(code==1){
    		 str+="您确定使用改模板吗";
    	 }else{
    		 str+="您确定停用改模板吗";
    	 }
    	 $.layer({
    		     shade : [0], //不显示遮罩
    		     area : ['auto','auto'],
    		     dialog : {
    		         msg:str,
    		         btns : 2, 
    		         type : 4,
    		         btn : ['确定','取消'],
    		         yes : function(){
    					$.ajax({
    						url:'/smstemplate/updateState',
    						type:'post',
    						data:{"id":id},
    						dataType:'json',
    						success:function(data){
    							if(data.code==1){
    								if(code==1){
    									layer.msg("成功使用该模板",1,1);
    						    	 }else{
    						    		 layer.msg("成功停用该模板",1,1);
    						    	 }
    								setTimeout("parent.window.location.reload()",1000);
    							}else{
    								if(code==1){
    									layer.msg("使用异常丶请联系管理员",1,2);
    						    	 }else{
    						    		 layer.msg("停用异常丶请联系管理员",1,2);
    						    	 }
    							}
    						}
    					});	 
    		         },
    		         no : function(){
    		     
    		     }
    		     }
    		 });
     }
     
     function deleteTemplate(id){
    	 $.layer({
    		     shade : [0], //不显示遮罩
    		     area : ['auto','auto'],
    		     dialog : {
    		         msg:"您确定删除该短信模板吗？",
    		         btns : 2, 
    		         type : 4,
    		         btn : ['确定','取消'],
    		         yes : function(){
    					$.ajax({
    						url:'/smstemplate/deleteTemplate',
    						type:'post',
    						data:{"id":id},
    						dataType:'json',
    						success:function(data){
    							if(data.code==1){
   									layer.msg("成功删除该模板",1,1);
   									setTimeout("parent.window.location.reload()",1000);
    							}else if(data.code==0){
    								layer.msg("删除异常丶请联系管理员",1,2);
    							}else{
    								layer.msg("该模板正在使用中丶不能删除",1,2);
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
</body>
</html>