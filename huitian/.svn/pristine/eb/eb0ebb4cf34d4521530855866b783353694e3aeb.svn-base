<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet" />
<link href="/css/css/animate.css" rel="stylesheet" />

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${_res.get("faculty_statistics") }</title>
<!-- 时间控件 -->
<style type="text/css">
.header {
	font-size: 12px
}

#teacherfenyeup {
	margin: 0 0 0 -40px
}

#teacherfenyedown {
	margin: -10px 0 0 -40px
}
</style>
</head>
<script type="text/javascript">
	var flag = false;
	function teacherdown() {
		$("#queryType").val(2);
		teachertongji();
	}
	function teacherup() {
		$("#queryType").val(1);
		teachertongji();
	}
	function nowMonth() {
		$("#queryType").val(0);
		teachertongji();
	}
	function queryState() {
		$("#queryType").val(4);
		teachertongji();
	}
	function teachertongji() {
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		var queryType = $("#queryType").val();
		var teacherId = $("#teacherId").val();
		$.ajax({
			type : "post",
			url : "/account/teachertongjilog",
			dataType : "json",
			data : {
				"startTime" : starttime,
				"endTime" : endtime,
				"queryType" : queryType,
				"teacherId" : teacherId
			},
			success : function(result) {
				$("#starttime").val(result.startTime);
				$("#endtime").val(result.endTime);
				$("#sum").text('${_res.get("Total.monthly")}：' + result.sumkecheng + '${_res.get("classes")} -- ' + result.sumkeshi + '${_res.get("session")}');
				var str = "";
				for (var i = 0; i < result.list.length; i++) {
					str += '<tr class="odd" align="center">';
					str += '<td>' + (i + 1) + '</td>';
					str += '<td>' + result.list[i].REALNAME + '</td>';
					str += '<td>' + result.list[i].KECHENG + '</td>';
					str += '<td>' + result.list[i].KESHI + '</td>';
					str += '<td>' + result.list[i].OTKESHI + '</td>';
					str += '<td>' + result.list[i].NOOTKESHI + '</td>';
					/* str += '<td>' + result.list[i].DELLKESHI + '</td>'; */
					str += '<td>' + result.list[i].TPINGLUN + '</td>';
					str += '<td>' + result.list[i].SIGNIN + '</td>';
					str += '<td><a href="javascript:void(0)" onclick="teacherParticlar(' + result.list[i].TID + ')">${_res.get("teacher.tongji.detail")}</a></td></tr>';
				}
				$("#liebiao").html(str);
			}
		});
	}

	function teacherParticlar(teacherId) {
		var startTime = $("#starttime").val();
		var endTime = $("#endtime").val();
		window.location.href = '/courseplan/getTeacherMessage?_query.tid=' + teacherId + '&_query.startTime=' + startTime + '&_query.endTime=' + endTime;
	}
</script>

<body onload="teachertongji()">
	<div id="wrapper" style="background: #2f4050; min-width: 1100px;">
		<%@ include file="/common/left-nav.jsp"%>
		<div class="gray-bg dashbard-1" id="page-wrapper">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top fixtop" role="navigation">
					<%@ include file="/common/top-index.jsp"%>
				</nav>
			</div>

			<div class="margin-nav" style="min-width: 600px;">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>
								<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top: -1px">&nbsp;&nbsp; <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> &gt;<a href='/course/getCourseCount'>${_res.get("courses_statistics") }</a> &gt;${_res.get("faculty_statistics") }
							</h5>
							<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float: right">${_res.get("system.reback")}</a>
							<div style="clear: both"></div>
						</div>
						<div class="ibox-content">
							<input id="queryType" type="hidden" name="queryType" value="">
							<fieldset>
								<div style="float: left; margin-right: 5px">
									<label style="float: left; margin: 6px 0 0">${_res.get("course.class.date") }：</label>
									<div style="float: left">
										<input type="text" class="form-control layer-date" id="starttime" readonly="readonly" value="${startTime }" style="width: 170px; background-color: #fff;" />
									</div>
									<div style="width: 30px; height: 30px; line-height: 30px; text-align: center; background: #E5E6E7; float: left; margin-top: 1px">${_res.get('to')}</div>
									<div style="float: left">
										<input type="text" class="form-control layer-date" id="endtime" readonly="readonly" value="${endTime }" style="width: 170px; background-color: #fff;" />
									</div>
								</div>
								<div style="float: left; margin-right: 5px">
									<label>${_res.get("teacher.name") }：</label> 
									<select name="teacherId" id="teacherId" class="chosen-select" style="width: 150px;">
										<option value="">${_res.get("system.alloptions") }</option>
										<c:forEach items="${teacherList }" var="teacher">
											<option value="${teacher.id }">${teacher.real_name}</option>
										</c:forEach>
									</select>
								</div>
								<div style="float: left; margin-top: -2px">
									<input type="button" class="btn btn-outline btn-primary" id="teacher" value="${_res.get('admin.common.select') }" onclick="queryState()" />
								</div>
								<div style="clear: both;"></div>
							</fieldset>
						</div>
					</div>
				</div>

				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>${_res.get("faculty_list") }</h5>
						</div>
						<div class="ibox-content">
							<!-- 老师 分月开始 -->
							<div class="clearfix" id="teacherfenyeup">
								<div>
									<ul>
										<li id="teacherliup"><a href="#" id="teacherup" onclick="teacherup()">${_res.get("course.lastMonth") }</a>&nbsp;&nbsp;&nbsp;</li>
										<li id="teacherlidown"><a href="javascript:void(0)" onclick="teacherdown()" id="teacherdown"> ${_res.get("course.nextMonth") }</a>&nbsp;&nbsp;&nbsp;</li>
										<li id="teacherliup"><a href="#" id="nowMonth" onclick="nowMonth()">${_res.get("course.currentMonth") }</a></li>
									</ul>
								</div>
							</div>
							<!-- 老师 分月结束 -->
							<table class="table table-hover table-bordered">
								<thead>
									<tr>
										<th colspan="9" id="sum"></th>
									</tr>
									<tr align="center" id="liebiaotou">
										<th class="header" width="80px">${_res.get("index") }</th>
										<th class="header" width="80px">${_res.get("teacher") }</th>
										<th class="header" width="80px">${_res.get("course.course") }(${_res.get('classes')})</th>
										<th class="header" width="80px">${_res.get('session')}</th>
										<th class="header" width="80px">OT(YES)</th>
										<th class="header" width="80px">OT(NO)</th>
										<!-- <th class="header" width="80px">取消课时</th> -->
										<th class="header" width="100px">${_res.get("courses.evaluation")}(${_res.get("frequency")})</th>
										<th class="header" width="100px">${_res.get("class.attendance")}(${_res.get("frequency")})</th>
										<th class="header" width="100px">${_res.get("operation") }</th>
									</tr>
								</thead>
								<tbody id="liebiao">
								</tbody>
							</table>
							<!--老师 分月开始 -->
							<div class="clearfix" id="teacherfenyedown">
								<ul>
									<li id="teacherliup"><a href="#" id="teacherup" onclick="teacherup()">${_res.get("course.lastMonth") }</a>&nbsp;&nbsp;&nbsp;</li>
									<li id="teacherlidown"><a href="javascript:void(0)" onclick="teacherdown()" class="disabled" id="teacherdown"> ${_res.get("course.nextMonth") }</a>&nbsp;&nbsp;&nbsp;</li>
									<li id="teacherliup"><a href="#" id="nowMonth" onclick="nowMonth()">${_res.get("course.currentMonth") }</a></li>
								</ul>
							</div>
							<!--老师 分月结束 -->
						</div>
					</div>
				</div>
				<div style="clear: both;"></div>
			</div>
		</div>
	</div>
	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
	<script>
		//日期范围限制
		var starttime = {
			elem : '#starttime',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : false,
			choose : function(datas) {
				endtime.min = datas; //开始日选好后，重置结束日的最小日期
				endtime.start = datas //将结束日的初始值设定为开始日
			}
		};
		var endtime = {
			elem : '#endtime',
			format : 'YYYY-MM-DD',
			max : '2099-06-16',
			istime : false,
			istoday : false,
			choose : function(datas) {
				starttime.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(starttime);
		laydate(endtime);
	</script>

	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
	<script src="/js/js/top-nav/top-nav.js"></script>
	<script src="/js/js/top-nav/teach.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
		$(".chosen-select").chosen({
			disable_search_threshold : 25
		});
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
</body>
</html>