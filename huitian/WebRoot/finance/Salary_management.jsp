<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>薪资管理</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet" />
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet" />
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet" />
<link href="/css/css/animate.css" rel="stylesheet" />

<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<style type="text/css">
.chosen-container {
	margin-top: -3px
}

.xubox_tabmove {
	background: #EEE
}

.xubox_tabnow {
	color: #31708f
}

input {
	border: 1px solid #e1e1e1
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

			<div class="margin-nav" style="width: 100%;">
				<form action="/salary" method="post" id="searchForm">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
						    <div class="ibox-title">
								<h5>
								     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
								     &gt;<a href='/salary/index'>薪资管理</a> &gt;薪资列表
							   </h5>
							</div>
							<div class="ibox-content">
							<label>${_res.get('teacher.name')}：</label>
								<input type="text" id="teachername" name="_query.teachername" value="${paramMap['_query.teachername']}"
									style="background-color: #fff; width: 130px; " />&nbsp;&nbsp;&nbsp;&nbsp;
								<label>薪资月份：</label>
								<input type="text" readonly="readonly" id="date1" name="_query.date" value="${paramMap['_query.date']}"
									style="background-color: #fff; width: 130px; cursor: not-allowed" />&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
								
							</div>
						</div>
					</div>

					<div class="col-lg-12" style="min-width: 680px">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>薪资列表</h5>
							</div>
							<div class="ibox-content">
								<table class="table table-hover table-bordered" width="100%">
									<thead>
										<tr>
											<th rowspan="2">${_res.get("index")}</th>
											<th rowspan="2">${_res.get("teacher.name")}</th>
											<th rowspan="2">${_res.get("monthly.statistics")}</th>
											<th rowspan="2">${_res.get('total.sessions')}</th>
											<th rowspan="2">${_res.get("normal")}</th>
											<th rowspan="2">${_res.get('courselib.late')}</th>
											<th rowspan="2">应发</th>
											<th rowspan="2">实发</th>
											<th rowspan="2">提交人</th>
											<th rowspan="2">${_res.get("report.date.of.submission")}</th>
										</tr>
									</thead>
									<c:forEach items="${showPages.list}" var="salary" varStatus="status">
										<tr class="odd" align="center">
											<td>${status.count}</td>
											<td>${salary.real_name}</td>
											<td>${salary.stat_time}</td>
											<td>${salary.sum_hours}</td>
											<td>${salary.normal_hours}</td>
											<td>${salary.late_hours}</td>
											<td>${salary.normal_salary}</td>
											<td>${salary.real_salary}</td>
											<td>${salary.creatname}</td>
											<td><fmt:formatDate value="${salary.creat_time}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
											<!-- <td><a href="javascript:void(0)" onclick="toCoursecostPage(${teacherchecke.teacher_id})" title="计算" >计算</a></td> -->
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
	<script type="text/javascript">
	function toCoursecostPage(id){
		$.layer({
		    type: 2,
		    shadeClose: true,
		    title: "计算薪资",
		    closeBtn: [0, true],
		    shade: [0.5, '#000'],
		    border: [0],
		    offset:['20px', ''],
		    area: ['700px', '500px'],
		    iframe: {src: '${cxt}/salary/jisuanlist/'+id}
		});
	}
</script>


	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/money.js"></script>
	<script>
       $('li[ID=nav-nav16]').removeAttr('').attr('class','active');
    </script>
	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
	<script>
         //日期范围限制
        var date1 = {
            elem: '#date1',
            format: 'YYYY-MM',
            max: '2099-06-16', //最大日期
            istime: false,
            istoday: false,
            choose: function (datas) {
                date2.min = datas; //开始日选好后，重置结束日的最小日期
                date2.start = datas //将结束日的初始值设定为开始日
            }
        };
        laydate(date1);
 </script>
	<script type="text/javascript">
    function setSysUserGroup(ids){
	    $.layer({
		    type: 2,
		    shadeClose: true,
		    title: "分组选择",
		    closeBtn: [0, true],
		    shade: [0.5, '#000'],
		    border: [0],
		    offset:['20px', ''],
		    area: ['700px', ($(window).height() - 50) +'px'],
		    iframe: {src: "${cxt}/operator/setSysUserGroup/"+ids}
		});
    }
    </script>
</body>
</html>