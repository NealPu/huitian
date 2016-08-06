<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>微信菜单列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
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

<script src="/js/js/jquery-2.1.1.min.js"></script>
<style type="text/css">
.chosen-container {
	margin-top: -3px;
}

.xubox_tabmove {
	background: #EEE;
}

.xubox_tabnow {
	color: #31708f;
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;min-width:1100px">
		<div class="left-nav"><%@ include file="/common/left-nav.jsp"%></div>
		<div class="gray-bg dashbard-1" id="page-wrapper">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top fixtop" role="navigation"s>
				    <%@ include file="/common/top-index.jsp"%>
				</nav>
			</div>

			<div style="margin-left: -14px; margin-top: 40px;">
				<form action="/weixin/menu/index" method="post" id="searchForm">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
						    <div class="ibox-title">
								<h5>
								      <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/weixin'">${_res.get('admin.common.mainPage')}</a> 
								      &gt;<a href='/weixin/wpnumber'>微信管理</a> &gt; 菜单列表
							    </h5>
							    <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
							</div>
							<div class="ibox-content">
								<label>所属公众号：</label>
									<select name="wpnumberid" id="wpnumberid" class="chosen-select" style="width: 150px" tabindex="2">
									<c:forEach items="${wpnumberList }" var="wpnumber">
										<option value="${wpnumber.id }" ${wpnumber.id==wpnumberid ?"selected='selected'":"" }>${wpnumber.accountname }</option>
									</c:forEach>
									</select>
								<input type="submit" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
								<input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick="add('')" class="btn btn-outline btn-info">
							</div>
						</div>
					</div>
					<c:if test="${!empty menuList }">
						<div class="col-lg-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>菜单列表</h5>
									<div style="margin:-10px 0 0 -50px;float:right"><input type="button" id="tongbu" class="btn btn1 btn-outline btn-primary" value="同步菜单" onclick="tongbuMenu(${wpnumberid})"/></div>
								</div>
								<div class="ibox-content">
									<table width="80%" class="table table-hover table-bordered">
										<thead>
											<tr>
												<th>${_res.get("index")}</th>
												<th>菜单类型</th>
												<th>菜单名称</th>
												<th>所属菜单</th>
												<th>响应类型</th>
												<th>key/URL</th>
												<th>菜单顺序</th>
												<th>${_res.get("operation")}</th>
											</tr>
										</thead>
										<c:forEach items="${menuList}" var="menu" varStatus="index">
											<tr align="center">
												<td>${index.count }</td>
												<td>${menu.menutype==0?'一级菜单':'二级菜单'}</td>
												<td>${menu.menuname}</td>
												<td></td>
												<td>${menu.type}</td>
												<td>${menu.type == 'click'?menu.key:menu.url}</td>
												<td>${menu.sortorder}</td>
												<td>
												<a href="#" style="color: #515151" onclick="view(${menu.id})">${_res.get('admin.common.see')}</a>|
												<c:if test="${menu.subcount<5 }">
													<a href="#" style="color: #515151" onclick="add(${menu.id})">${_res.get('teacher.group.add')}</a>|
												</c:if>
												<a href="#" style="color: #515151" onclick="edit(${menu.id})">${ _res.get('admin.common.edit')}</a>
												</td>
											</tr>
											<c:forEach items="${menu.submenus }" var="sub" varStatus="step">
												<tr align="right">
													<td>${step.count }</td>
													<td>${sub.menutype==0?'一级菜单':'二级菜单'}</td>
													<td>${sub.menuname}</td>
													<td>${menu.menuname}</td>
													<td>${sub.type}</td>
													<td>${sub.type == 'click'?sub.key:sub.url}</td>
													<td>${sub.sortorder}</td>
													<td>
													<a href="#" style="color: #515151" onclick="view(${sub.id})">${_res.get('admin.common.see')}</a>|
													<c:if test="${sub.status==0 }">
														<a href="#" style="color: #515151" onclick="changeStatus(${sub.id},1)">${_res.get('Suspending')}</a>|
													</c:if>
													<c:if test="${sub.status==1 }">
														<a href="#" style="color: #515151" onclick="changeStatus(${sub.id},0)">${_res.get('admin.dict.property.status.start')}</a>|
													</c:if>
													<a href="#" style="color: #515151" onclick="edit(${sub.id})">${ _res.get('admin.common.edit')}</a>
													</td>
												</tr>
											</c:forEach>
										</c:forEach>
									</table>
								</div>
							</div>
						</div>
					</c:if>
					<div style="clear: both;"></div>
				</form>
			</div>
		</div>
	</div>
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
	<script type="text/javascript">
    function add(id){
    	var wpnumberid = $("#wpnumberid").val();
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "添加菜单",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['600px', '550px'],
    	    iframe: {src: '${cxt}/weixin/menu/add?firstMenuId='+id+'&wpnumberId='+wpnumberid}
    	});
    }  
	function view(id){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "菜单信息",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['600px', '550px'],
    	    iframe: {src: '${cxt}/weixin/menu/view/'+id}
    	});
    }
   
    function edit(id){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "更新菜单",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['600px', '550px'],
    	    iframe: {src: '${cxt}/weixin/menu/edit/'+id}
    	});
    }    
    function tongbuMenu(wpnumberid){
    	layer.confirm('确定要同步菜单到微信服务器吗？', function(){
    		$.ajax({
    			url:"${cxt}/weixin/menu/synchronizeMenu",
    			type:"post",
    			data:{"type":"1","wpnumberid":wpnumberid},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
					}else{
    					layer.msg(data.msg, 2, 5);
					}
    			}
    		});
    	});
    }
    function changeStatus(menuId,status){
    	layer.confirm('确定要暂停该菜单吗？', function(){
    		$.ajax({
    			url:"${cxt}/weixin/menu/changeMenuStatus",
    			type:"post",
    			data:{"menuId":menuId,"status":status},
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
	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
	<script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/campus.js"></script>
	<script>
       $('li[ID=nav-nav13]').removeAttr('').attr('class','active');
    </script>
</body>
</html>