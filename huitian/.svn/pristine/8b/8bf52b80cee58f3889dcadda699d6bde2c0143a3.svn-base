<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>学校计划</title>
<style type="text/css">
.header {
	font-size: 12px;
   background:#fff !important;
}
table > tbody > tr > td{
   line-height:8px !important;
}
.panel-default>.panel-heading{
   color: #676a6c !important;
}
.ibox-con{
   background:#f5f5f5 !important;
}
.panel-heading{
   border:0 !important;
}
</style>
</head>
<body>
<div id="wrapper" style="background: #2f4050;">
  <%@ include file="/common/left-nav.jsp"%>
   <div class="gray-bg dashbard-1" id="page-wrapper">
  <div class="row border-bottom" >
     <nav class="navbar navbar-static-top" role="navigation" style="padding:0 0 5px;margin-left:-15px;position:fixed;width:100%;background-color:#fff;">
        <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" id="btn-primary" href="#" style="margin-top:10px;"><i class="fa fa-bars"></i> </a>
            <div style="margin:20px 0 0 70px;"><h5><img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
            &gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt;<a href='javascript:history.go(-1);'>市场计划</a>   &gt; 学校计划</h5></div>
        </div>
        <div class="top-index"><%@ include file="/common/top-index.jsp"%></div>
     </nav>
  </div>
  
  <div class="margin-nav" style="margin-left:0;min-width:540px">
     <div class="col-lg-12" style="margin:20px 0;padding:0">
			<div class="ibox float-e-margins">
				<div class="ibox-title" style="height:auto;">
					<h3>学校计划</h3>
					<p>${fn:substring(month, 0, 4)}年${fn:substring(month, 5, 7)}月市场学校计划</p>
				</div>	
				<div class="ibox-content">
					<c:forEach items="${weekmap }" var="weekmaplist"  >
					<c:if test="${fn:length(weekmaplist.value)!=0 }">
					 <div class="panel panel-default">
					   <div class="panel-heading">
					    <div class="ibox-con">
					      <a data-toggle="collapse" data-parent="#accordion" onclick="toggle(${weekmaplist.key})" href="tabs_panels.html#collapse${weekmaplist.key}">
						     <h4>第${weekmaplist.key eq '1'?'一':weekmaplist.key eq '2'?'二':weekmaplist.key eq '3'?'三':weekmaplist.key eq '4'?'四':weekmaplist.key eq '5'?'五':'六' }周
						     <div class="toggle_fa">
							     <span style="font-size:10px" class="fa fa-chevron-down" id="fa_sp${weekmaplist.key}"></span>
	                             <span class="fa fa-chevron-up" id="fa_spa${weekmaplist.key}" style="display:none;font-size:10px"></span>
						     </div>
						     </h4>
						  </a>
						</div>
						<div id="collapse${weekmaplist.key }" class="panel-collapse collapse">
						<c:forEach items="${weekmaplist.value }" var="listvalue" >
						<P> ${listvalue.weekday eq '1'?'周一':listvalue.weekday eq '2'?'周二':listvalue.weekday eq '3'?'周三':listvalue.weekday eq '4'?'周四':'周五' }&nbsp;&nbsp;&nbsp;
					 	</P>
						    <table class="table table-hover table-bordered" width="100%" id="${listvalue.day }table">
							<thead>
							<%-- <c:if test="${fn:length(listvalue.mplistday) > 0 } "> --%>
								<tr align="center">
									<th class="header" >学校</th>
									<th class="header" >年级</th>
									<th class="header" >计划收单</th>
									<th class="header" >实际收单</th>
									<th class="header" >计划YES</th>
									<th class="header" >实际YES</th>
									<th class="header" >计划上门</th>
									<th class="header" >实际上门</th>
									<th class="header" >计划招生</th>
									<th class="header" >实际招生</th>
									<!-- <th class="header" >计划费用</th>
									<th class="header" >实际费用</th> -->
								</tr>
							<%-- </c:if> --%>
							</thead>
							<tbody>
							<c:forEach items="${listvalue.mplistday }" var="scplan" >
								<tr class="odd" align="center">
										<td rowspan="2" style="line-height:40px !important;">${scplan.schoolname }</td>
										<td>低年级</td>
										<td>${scplan.lowplanorder }</td>
										<td>${scplan.lowrealorder }</td>
										<td>${scplan.lowplanyes }</td>
										<td>${scplan.lowrealyes }</td>
										<td>${scplan.lowplanvisit }</td>
										<td>${scplan.lowrealvisit }</td>
										<td>${scplan.lowplanrecruit }</td>
										<td>${scplan.lowrealrecruit }</td>
										<%-- <td>${scplan.lowplancost }</td>
										<td>${scplan.lowrealcost }</td> --%>
								</tr>
								<tr class="odd" align="center">
										<td>高年级</td>
										<td>${scplan.upplanorder }</td>
										<td>${scplan.uprealorder }</td>
										<td>${scplan.upplanyes }</td>
										<td>${scplan.uprealyes }</td>
										<td>${scplan.upplanvisit }</td>
										<td>${scplan.uprealvisit }</td>
										<td>${scplan.upplanrecruit }</td>
										<td>${scplan.uprealrecruit }</td>
										<%-- <td>${scplan.upplancost }</td>
										<td>${scplan.uprealcost }</td> --%>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						</c:forEach>
						</div>
						</div>
						</div>
						</c:if>
					</c:forEach>
			</div>
			</div>
	 </div>
	 <div style="clear: both;"></div>			
   </div>
   </div>
</div>  
<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/plugins/layer/layer.min.js"></script>
    <script>
    function toggle(id){
	        if($("#fa_spa"+id).is(":hidden")){
	        	$("#fa_sp"+id).hide();
	        	$("#fa_spa"+id).show();
	        }else{
	        	$("#fa_spa"+id).hide();
	        	$("#fa_sp"+id).show();
	        }
    }
    
    $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>