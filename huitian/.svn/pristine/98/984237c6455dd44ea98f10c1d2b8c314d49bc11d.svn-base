<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>定点列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
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
	width:100% !important;
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
					 &gt;<a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a> &gt; 定点列表
				</h5>
				<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
			</div>
			<div class="top-index"><%@ include file="/common/top-index.jsp"%></div>
			</nav>
		</div>

	    <div class="margin-nav" style="min-width:1050px;width:100%;">	
		<form action="/opportunity/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
				<div class="ibox-title" style="height:auto;">
				<label>地点：</label>
				<select id="sourceid" class="chosen-select" style="width: 150px;" tabindex="2" name="_query.source"  onchange="chooseSource(this.value)" >
					<option value="">--${_res.get('Please.select')}--</option>
					<option value="">昌平</option>
					<option value="">朝阳</option>
					<option value="">海淀</option>
					<option value="">丰台</option>
					<option value="">通州</option>
				</select>
				<label> ${_res.get('course.class.date')}： </label> <input type="text" name="start" readonly="readonly" value="" id="data"/>
				&nbsp;
				<input type="submit" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">&nbsp;
				<input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick="window.location.href='/opportunity/dingdian_form.jsp'" class="btn btn-outline btn-info">
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>活动列表</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>地点</th>
									<th>${_res.get('person.in.charge')}</th>
									<th>${_res.get('course.class.date')}</th>
									<th>信息收集员</th>
									<th>${_res.get("operation")}</th>
								</tr>
							</thead>
								<tr align="center">
									<td>1</td>
									<td>海淀</td>
									<td>张老师</td>
									<td>2015-05-05</td>
									<td>赵XX</td>
									<td>
										<a href="#" style="color: #515151" onclick="">${_res.get('Modify')}</a>|
										<a href="#" style="color: #515151" onclick="">${_res.get('admin.common.delete')}</a>|
										<a href="#" style="color: #515151" onclick="samePeople()">${_res.get("admin.common.see")}</a>
									</td>
								</tr>
								<tr align="center">
									<td>2</td>
									<td>国贸</td>
									<td>赵老师</td>
									<td>2015-05-07</td>
									<td>徐XX</td>
									<td>
										<a href="#" style="color: #515151" onclick="">${_res.get('Modify')}</a>|
										<a href="#" style="color: #515151" onclick="">${_res.get('admin.common.delete')}</a>|
										<a href="#" style="color: #515151" onclick="samePeople()">${_res.get("admin.common.see")}</a>
									</td>
								</tr>
								<tr align="center">
									<td>3</td>
									<td>丰台</td>
									<td>王老师</td>
									<td>2015-06-07</td>
									<td>杨XX</td>
									<td>
										<a href="#" style="color: #515151" onclick="">${_res.get('Modify')}</a>|
										<a href="#" style="color: #515151" onclick="">${_res.get('admin.common.delete')}</a>|
										<a href="#" style="color: #515151" onclick="samePeople()">${_res.get("admin.common.see")}</a>
									</td>
								</tr>
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script src="/js/js/demo/layer-demo.js"></script>
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
		
		function samePeople(){
			layer.tab({
			    data:[
			        {
			        	title: '相关人员', 
			        	content:'<table class="table table-bordered">'
			        	+'<tr>'
			        	+'<td class="table-bg1">地点</td><td class="table-bg2">海淀</td>'
			        	+'<td class="table-bg1">负责人</td><td class="table-bg2">张老师</td>'
			        	+'</tr>'
			        	+'<tr>'
			        	+'<td class="table-bg1">${_res.get("course.class.date")}</td><td class="table-bg2">2015-05-05</td>'
			        	+'<td class="table-bg1">信息收集员</td><td class="table-bg2">赵XX</td>'
			        	+'</tr>'
			        	+'<tr>'
			        	+'<td class="table-bg1">备注</td><td class="table-bg2" colspan="3">此为备注留言</td>'
			        	+'</tr>'
			        	+'</table>'
			        }
			        
			    ],
				offset:['150px', ''],
			    area: ['400px', 'auto'] //宽度，高度
			}); 
		}
	</script>	
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
     <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
    <script>
         //开始日期范围限制
        var birthday = {
            elem: '#data',
            format: 'YYYY-MM-DD',
            max: laydate.now(), //最大日期
            istime: false,
            istoday: false
        };
        laydate(birthday);
    
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>