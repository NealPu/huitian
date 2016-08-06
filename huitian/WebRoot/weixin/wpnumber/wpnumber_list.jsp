<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>微信公众号管理</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet"/>
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<script type="text/javascript">
	function delSubject(subjectId){
		if(confirm('确认删除该科目吗')){
			$.ajax({
				url:"/subject/delSubject1",
				type:"post",
				data:{"subjectId":subjectId},
				dataType:"json",
				success:function(result)
				{
					if(result.result=="true"){
						alert("删除成功");
						window.location.reload();
					}else{
						alert(result.result);
					}
				}
			});
		}
	}
	function modifyOrder(subjectId){
		var sortorder=$("#sortorder_"+subjectId).val();
		$.ajax({
			url:"/subject/modifySubjectOrder",
			type:"post",
			data:{"subjectId":subjectId,"sortOrder":sortorder},
			dataType:"json",
			success:function(result){
				if(result.result=="true"){
					$("#successMsg").html("操作成功");
					$("span").fadeOut(2000,function(){
						window.location.reload();
					});
				}else{
					alert(result.result);
				}
			}
		});
	}
</script>
<style type="text/css">
.btn1{
    margin:-32px 0 0 940px;
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
  
 <div class="margin-nav">	
  <div class="col-lg-12">
     <div class="ibox float-e-margins">
       <div class="ibox-title" style="margin-bottom:20px">
         <h5>
            <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
             &gt;<a href='/weixin/wpnumber'>微信管理</a> &gt; 公众号管理
         </h5>
         <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
       </div>
       <div class="ibox-title">
         <h5>微信公众号列表</h5>
         <c:if test="${operator_session.addwpnumber }">
         	<input type="button" id="tianjia" style="float: right;margin-top:-12px;" class="btn btn-outline btn-primary" value="${_res.get('teacher.group.add')}" onclick="add()"/>
         </c:if>
       </div>
     <div class="ibox-content">
        <table width="80%" class="table table-hover table-bordered">
           <thead>
				<tr>
					<th>${_res.get("index")}</th>
					<th>${_res.get('admin.dict.property.name')}</th>
					<th>微信号</th>
					<th>类型</th>
					<th>appId</th>
					<th>appSecret</th>
					<th>token</th>
					<th>${_res.get('admin.dict.property.status')}</th>
					<th>${_res.get("operation")}</th>
				</tr>
		   </thead>	
				<c:forEach items="${wpnumberlist}" var="wp" varStatus="s">
				<tr align="center">
					<td>${s.count }</td>
					<td>${wp.accountname }</td>
					<td>${wp.accountnumber }</td>
					<td>${wp.accounttype==1?'订阅号':(wp.accounttype==2?'服务号':'企业号') }</td>
					<td>${wp.appid }</td>
					<td>${wp.appsecret }</td>
					<td>${wp.token }</td>
					<td>${wp.status==0?_res.get('admin.dict.property.status.start'):_res.get('Suspending') }</td>
					<td>
						<a href="#" style="color: #515151" onclick="edit(${wp.id})">${ _res.get('admin.common.edit')}</a>|
					<c:choose>
						<c:when test="${wp.status==1}">
							<a style="color:#515151" href="javascript:void(0)" onclick="changeStatus(${wp.id},0)">${_res.get('admin.dict.property.status.start')}</a>
						</c:when>
						<c:otherwise>
							<a style="color:#515151" href="javascript:void(0)" onclick="changeStatus(${wp.id},1)">${_res.get('Suspending')}</a>
						</c:otherwise>
					</c:choose>	
					</td>	
				</tr>
				</c:forEach>
		</table>
     </div>
    </div>
  </div>
  <div style="clear: both;"></div>
  </div>
  </div>
  
  <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/campus.js"></script>
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
    	    title: "添加公众号",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['550px', '550px'],
    	    iframe: {src: '${cxt}/weixin/wpnumber/add/'}
    	});
    }  
    
    function edit(id){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "编辑公众号",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['550px', '550px'],
    	    iframe: {src: '${cxt}/weixin/wpnumber/edit/'+id}
    	});
    }
    
	function changeStatus(id,status){
		if(confirm('确认修改该公众号状态吗？')){
			$.ajax({
				url:"/weixin/wpnumber/changeStatus",
				type:"post",
				data:{"id":id,"status":status},
				dataType:"json",
				success:function(data){
					parent.layer.msg(data.msg, 2,1);
		    		if(data.code=='1'){//成功
		    			setTimeout("parent.layer.close(index)", 1000 );
						parent.window.location.reload();
		    		} 
				}
			});
		}
	}
       $('li[ID=nav-nav13]').removeAttr('').attr('class','active');
    </script>
</div>  
</body>
</html>