<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>出入明细</title>
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

function editOutIn(id,type){
	if(type=="instorage"){
		$.layer({
			type:2,
			shadeClose:true,
			title:"编辑入库",
			closeBtn:[0,true],
			shade:[0.5,'#000'],
			border:[0],
			area:['450px','485px'],
			iframe:{src:'${ctx}/itemsManager/editItemsIn/'+id}
		});
	}else{
		$.layer({
			type:2,
			shadeClose:true,
			title:"编辑入库",
			closeBtn:[0,true],
			shade:[0.5,'#000'],
			border:[0],
			area:['450px','485px'],
			iframe:{src:'${ctx}/itemsManager/editItemsOut/'+id}
		});
	}
	
}

function toItemsIn(id){
	$.layer({
		type:2,
		shadeClose:true,
		title:"物品入库",
		closeBtn:[0,true],
		shade:[0.5,'#000'],
		border:[0],
		area:['450px','255px'],
		iframe:{src:'${ctx}/itemsManager/toItemsIn/'+id}
	});
}

function updateState(ids,state){
	var d = dialog({
	    title: '操作提示',
	    content: "确定要这样操作吗？",
	    okValue: '确定',
	    ok: function () {
	        $.ajax({
	        	url:"/caiwu/itemsManager/deleteOutInItems",
				data:{"ids":ids,"state":state},
				type:"post",
				dataType:"json",
				async:false,
				success:function(result){
					if(result){
						ajaxForm("content","splitPage");
					}else{
						simpleDialog('提示', '操作异常.');
					}
				}
	        });
	        return true;
	    },
	    cancelValue: '取消',
	    cancel: function () {
	    	return true ; //false
	    }
	});
	d.show();
}
function updateState(ids){
	if(confirm('确认删除该记录吗？')){
		$.ajax({
			type:"post",
			url:"${cxt}/itemsManager/deleteOutInItems",
			data:{"ids":ids},
			dataType : "json",
			success:function(data){
				if(data.code==0){
					layer.msg(data.msg,1,5);
	   				setTimeout("window.location.reload()",2000);
				}else{
					layer.msg(data.msg,2,5);
				}
   				
   			}
		 });
	}
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
		<form action="/itemsManager/outInDetail" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					      &gt;<a href='/itemsManager/list'>教材物品</a> &gt;  出入明细
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>物品名称：</label>
					<input type="text" id="realname" name="_query.itemsname" value="${paramMap['_query.itemsname']}">
				<label>操作日期：</label>
					 <input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.startDate" value="${paramMap['_query.startDate']}" style="margin-top: -8px; width:120px" /> 
					至<input type="text" class="layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px" />
					<input type="hidden" name="_query.sysuserid" value="${account_session.id }"  />
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				<input type="button" value="${_res.get('Reset')}" onclick="resetSearch()" class="button btn btn-outline btn-warning ">
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
									<th>数量</th>
									<th>类型</th>
									<th>更新日期</th>
									<th>操作人</th>
									<th>备注</th>
									<th width="10%">${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="item" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${item.itemsname}</td>
									<td>${item.handlenum}</td>
									<td>${item.handletypeval}</td>
									<td>${item.handletime}</td>
									<td>${item.real_name}</td>
									<td>${item.remark}</td>
									<td>
									<c:if test="${operator_session.qx_itemsManagereditItemsIn }">
										<a href="#" onclick="editOutIn(${item.id},'${item.handletype}')" style="color: #515151">编辑</a>&nbsp;|
									</c:if>
									<c:if test="${operator_session.qx_itemsManagerdeleteOutInItems }">
										<a href="#" onclick="updateState(${item.id})" style="color: #515151">删除</a>&nbsp;
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