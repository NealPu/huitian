<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>物品列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet"/>
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<style type="text/css">
 .chosen-container{
   margin-top:-3px;
 }
</style>
<script type="text/javascript">

function addItems(){
	$.layer({
		type:2,
		shadeClose:true,
		title:"添加物品",
		closeBtn:[0,true],
		shade:[0.5,'#000'],
		border:[0],
		area:['450px','485px'],
		iframe:{src:'${ctx}/itemsManager/add'}
	});
}

function editDetail(id){
	$.layer({
		type:2,
		shadeClose:true,
		title:"${_res.get('Modify')}",
		closeBtn:[0,true],
		shade:[0.5,'#000'],
		border:[0],
		area:['450px','485px'],
		iframe:{src:'${ctx}/itemsManager/edit/'+id}
	});
}

function toItemsIn(id){
	$.layer({
		type:2,
		shadeClose:true,
		title:"物品入库",
		closeBtn:[0,true],
		shade:[0.5,'#000'],
		border:[0],
		area:['450px','295px'],
		iframe:{src:'${ctx}/itemsManager/toItemsIn/'+id}
	});
}

function toItemsOut(id){
	$.layer({
		type:2,
		shadeClose:true,
		title:"物品出库",
		closeBtn:[0,true],
		shade:[0.5,'#000'],
		border:[0],
		area:['450px','495px'],
		iframe:{src:'${ctx}/itemsManager/toItemsOut/'+id}
	});
}

/* $(document).ready(function(){
				$(".fancybox").fancybox({openEffect:"none",closeEffect:"none"})}
); */

</script>
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
		<form action="/itemsManager/list" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					      &gt;<a href='/itemsManager/list'>教材物品</a> &gt;  物品管理
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>物品名称：</label>
					<input type="text" id="realname" name="_query.itemsname" value="${paramMap['_query.itemsname']}">
				<label>${_res.get('Opp.Entry.Date')}：</label>
					 <input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.startDate" value="${paramMap['_query.startDate']}" style="margin-top: -8px; width:120px" /> 
					至<input type="text" class="layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px" />
					<input type="hidden" name="_query.sysuserid" value="${account_session.id }"  />
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				<input type="button" value="${_res.get('Reset')}" onclick="resetSearch()" class="button btn btn-outline btn-warning ">
				<c:if test="${operator_session.qx_itemsManageradd }">
					<input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick='addItems()' class="btn btn-outline btn-info">
				</c:if>
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get('Channel.list')}</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>物品名称</th>
									<th>款式型号</th>
									<th>单价(/元))</th>
									<th>库存</th>
									<th>更新日期</th>
									<th>操作人</th>
									<th>负责人</th>
									<th>场所</th>
									<th width="10%">${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="item" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${item.itemsname}</td>
									<td>${item.kuanshi}</td>
									<td>${item.price}</td>
									<td>${item.sumnum}</td>
									<td>${item.updatedate}</td>
									<td>${item.username}</td>
									<td>${item.teachername}</td>
									<td>${item.place}</td>
									<td>
									<c:if test="${operator_session.qx_itemsManageredit }">
										<a href="#" onclick="editDetail(${item.id})" style="color: #515151">编辑</a>&nbsp;|
									</c:if>
									<c:if test="${operator_session.qx_itemsManagertoItemsIn }">
										<a href="#" onclick="toItemsIn(${item.id})" style="color: #515151">入库</a>&nbsp;|
									</c:if>
									<c:if test="${operator_session.qx_mediatoredit }">
										<a href="#" onclick="toItemsOut(${item.id})" style="color: #515151">出库</a>&nbsp;|
									</c:if>
									<%-- <c:if test="${operator_session.qx_mediatortransferToCommission }">
										<a href="#" onclick="commission(${item.id})" style="color: #515151">上传照片</a>&nbsp;|
									</c:if>
									<c:if test="${operator_session.qx_mediatorview }">
										<a href="#" onclick="showDetail(${item.id})" style="color: #515151">查看</a>
									</c:if> --%>
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

	<script src="/js/js/demo/layer-demo.js"></script>
	
	 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/market.js"></script>
      <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		//日期范围限制
		var date1 = {
			elem : '#date1',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : false,
			choose : function(datas) {
				date2.min = datas; //开始日选好后，重置结束日的最小日期
				date2.start = datas //将结束日的初始值设定为开始日
			}
		};
		var date2 = {
			elem : '#date2',
			format : 'YYYY-MM-DD',
			max : '2099-06-16',
			istime : false,
			istoday : false,
			choose : function(datas) {
				date1.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(date1);
		laydate(date2);
	</script>
    <script>
       $('li[ID=nav-nav8]').removeAttr('').attr('class','active');
    </script>
</body>
</html>