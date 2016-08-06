<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>学校管理</title>
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
	<div id="wrapper" style="background: #2f4050;">
	  <%@ include file="/common/left-nav.jsp"%>
       <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;">
			<div class="navbar-header">
			  <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" id="btn-primary" href="#" style="margin-top:10px;"><i class="fa fa-bars"></i> </a>
				<div style="margin:20px 0 0 70px;"><h5>
					<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
					&gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt; 学校管理
				</h5>
				</div>
			</div>
			<div class="top-index"><%@ include file="/common/top-index.jsp"%></div>
			</nav>
		</div>

	    <div class="margin-nav" style="min-width:1050px;width:100%;">	
		<form action="/opportunity/schoolList" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
				<div class="ibox-title" style="height:auto">
				<label>学校名称：</label>
					<input type="text" id="schoolname" name="_query.schoolname" value="${paramMap['_query.schoolname']}">
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				<!--  -->
				<input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick="window.location.href='/opportunity/oppAddSchool'" class="btn btn-outline btn-info">
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>学校列表</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>学校名称</th>
									<th>学校电话</th>
									<th>学校邮箱</th>
									<th>校长姓名</th>
									<th>校长电话</th>
									<th>学校地址</th>
									<th>${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="school" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${school.schoolname }</td>
									<td>${school.schoolphone}</td>
									<td>${school.schoolemail}</td>
									<td>${school.principalname}</td>
									<td>${school.principalphone}</td>
									<td>${school.schooladdress}</td>
									<td>
									<c:if test="${operator_session.qx_opportunityupdateSchool }">
										<a href="${cxt}/opportunity/updateSchool/${school.id}" style="color: #515151">${_res.get('Modify')}</a>|
									</c:if>
									<c:if test="${operator_session.qx_opportunitydelOppSchool }">
										<a href="#" style="color: #515151" onclick="isconver(${school.id})">${_res.get('admin.common.delete')}</a>
									</c:if>
									</td>
									<input id="isconver_status_${school.id}" type="hidden" value="${school.isconver }">
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
    function isconver(schoolId){
    	layer.confirm('确认删除该学校吗？', function(){ 
    		$.ajax({
    			url:"${cxt}/opportunity/delOppSchool",
    			type:"post",
    			data:{"schoolId":schoolId},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
    					window.location.reload();
					}else{
    					layer.msg(data.msg, 2, 5);
					}
    			}
    		});
    	});
    }
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