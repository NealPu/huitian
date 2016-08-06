<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>来源管理</title>
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

	    <div class="margin-nav" style="width:100%;">	
		<form action="" method="post" id="searchForm">

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
				    <div class="ibox-title" style="margin-bottom:20px">
						<h5>
						    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
						      &gt;<a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a> &gt; 来源管理
						</h5>
						<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
					</div>
					<div class="ibox-title">
						<h5>来源列表</h5>
						<c:if test="${operator_session.qx_opportunitytoAddCrmSource }">
						<input type="button" id="tianjia" class="navbar-right btn btn-outline btn-primary input_right" style="margin:-10px 10px 0 0;float: right;" value="${_res.get('teacher.group.add')}" onclick="addSource()">
						</c:if>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>来源</th>
									<th>${_res.get("student.buildtime")}</th>
									<th>管理</th>
								</tr>
							</thead>
							<c:forEach items="${sourceList}" var="source" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${source.name}</td>
									<td><fmt:formatDate value="${source.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>
										<c:choose>
												<c:when test="${source.state eq true}">
												<c:if test="${operator_session.qx_opportunitysourceFreeze }">
													<a style="color: #515151" href="javascript:void(0)" onclick="freezeAccount(${source.id},0)">${_res.get('Recover')}</a>|
													</c:if>
												</c:when>
												<c:otherwise>
												<c:if test="${operator_session.qx_opportunitytoUpdateCrmSource }">
												<a href="javascript:void(0)" onclick="updataSource(${source.id})" >${ _res.get('admin.common.edit')}</a>|
												</c:if>
												<c:if test="${operator_session.qx_opportunitysourceFreeze }">
												<a style="color: #a4a4a4" href="javascript:void(0)" onclick="freezeAccount(${source.id},1)">${_res.get('Suspending')}</a> 
												</c:if>
												</c:otherwise>
											</c:choose>
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
    
    <script type="text/javascript">
    function freezeAccount(sourceId,state){
    	var suremsg ="";
    	if(state==0){
    		suremsg="确认要恢复该来源吗?";
    	}else if(state==1){
    		suremsg="确认要暂停该来源吗?";
    	}
    	if(confirm(suremsg)){
    		$.ajax({
    			url:"/opportunity/sourceFreeze",
    			type:"post",
    			data:{"sourceId":sourceId,"state":state},
    			dataType:"json",
    			success:function(result)
    			{
    				if(result.result==1)
    				{
    					layer.msg("操作成功",2,6);
    					window.location.reload();
    				}
    				else
    				{
    					layer.msg(result.result,2,2);
    				}
    			}
    		});
    	}
    }
    
    function delSource(sourceId){
    	if(confirm("确定要删除吗?")){
    		$.ajax({
    			url:"/opportunity/delSource",
    			type:"post",
    			data:{"sourceId":sourceId},
    			dataType:"json",
    			success:function(result)
    			{
    				if(result.code==1)
    				{
    					layer.msg(result.msg,2,6);
    					window.location.reload();
    				}
    				else
    				{
    					layer.msg(result.msg,2,3);
    				}
    			}
    		});
    	}
    }
    /*添加来源*/
    function addSource(){
    	  $.layer({
  		    type: 2,
  		    shadeClose: true,
  		    title: "添加来源",
  		    closeBtn: [0, true],
  		    shade: [0.5, '#000'],
  		    border: [0],
  		    offset:['20px', ''],
  		    area: ['480px', '200px'],
  		    iframe: {src: "${cxt}/opportunity/toAddCrmSource"}
  		});
    }
    /*修改来源*/
    function updataSource(id){
  	  $.layer({
  		    type: 2,
  		    shadeClose: true,
  		    title: "修改来源",
  		    closeBtn: [0, true],
  		    shade: [0.5, '#000'],
  		    border: [0],
  		    offset:['20px', ''],
  		    area: ['480px', '200px'],
  		    iframe: {src: "${cxt}/opportunity/toUpdateCrmSource/"+id}
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
    <script src="/js/js/top-nav/sale.js"></script>
    <script>
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>