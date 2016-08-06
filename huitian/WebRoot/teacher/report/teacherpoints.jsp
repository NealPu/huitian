<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get("report.my.report")}</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet"/>
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet"/>
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet"/>
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet"/>
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet"/>
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet"/>
<link href="/css/css/animate.css" rel="stylesheet"/>
<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
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
.laydate_body .laydate_bottom{
    height:30px !important
}
.laydate_body .laydate_top{
    padding:0 !important
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;">
	   <%@ include file="/common/left-nav.jsp"%>
	   <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;border:0">
			  <%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>
		
	  <div class="margin-nav" style="width:100%;">	
       <form action="/report/teacherReports" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					    <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
					   &gt;${_res.get("report.first.menuname")} &gt; ${_res.get("report.my.report")}
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
					<label>${_res.get('appointment.time')}：</label>
						<input type="text" id ="startappointment" readonly="readonly" name="_query.startappointment" value="${paramMap['_query.startappointment']}">${_res.get('to')}
						<input type="text" id ="endappointment" readonly="readonly" name="_query.endappointment" value="${paramMap['_query.endappointment']}">
					<label>${_res.get("student.name") }：</label>
						<input type="text" id ="student" name="_query.studentname" value="${paramMap['_query.studentname']}">
					<label>${_res.get('admin.dict.property.status')}:</label>
						<select id="state" name="_query.state" class="chosen-select" style="width:100px">
							<option value="" >${_res.get('system.alloptions')}</option>
							<c:choose>
								<c:when test="${paramMap['_query.state'] eq '1' }">
									<option value="1" selected="selected" >${_res.get('Submitted')}</option>
									<option value="0" >${_res.get('Uncommitted')}</option>
								</c:when>
								<c:when test="${paramMap['_query.state'] eq '0' }">
									<option value="1"  >${_res.get('Submitted')}</option>
									<option value="0" selected="selected" >${_res.get('Uncommitted')}</option>
								</c:when>
								<c:otherwise>
									<option value="1" >${_res.get('Submitted')}</option>
									<option value="0" >${_res.get('Uncommitted')}</option>
								</c:otherwise>
							</c:choose>
						</select><br><br>
						<label>${_res.get('submission.time')}：</label>
						<input type="text" id ="startsubmission" readonly="readonly" name="_query.startsubmission" value="${paramMap['_query.startsubmission']}">${_res.get('to')}
						<input type="text" id ="endsubmission" readonly="readonly" name="_query.endsubmission" value="${paramMap['_query.endsubmission']}">
					
						
					<input type="button" onclick="search()" value="${_res.get('admin.common.select') }"  class="btn btn-outline btn-primary">
			    </div>
			</div>
			</div>

			<div class="col-lg-12" style="min-width:680px">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get("report.my.report")}</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered" width="100%">
							<thead>
								<tr>
									<th>${_res.get("index") }</th>
									<th>${_res.get('appointment.time')}</th>
									<th>${_res.get("student") }</th>
									<th>${_res.get('admin.dict.property.status')}</th>
									<th>${_res.get("report.date.of.submission")}</th>
									<th>${_res.get("operation") }</th>
								</tr>
							</thead>
								<c:forEach items="${showPages}" var="list" varStatus="index">
									<tr class="odd" align="center">
										<td >${index.count }</td>
										<td>${list.appointment}</td>
										<td>${list.studentName}</td>
										<td>${list.state=='0'?_res.get('Uncommitted'):_res.get('Submitted')}</td>
										<td>${list.submissiontime }</td>
										<td>
											<c:if test="${operator_session.qx_accountqueryStudentCourseInfo }">
											   	<a onclick="detail(${list.studentid})" title="${_res.get('admin.common.see')} " >${_res.get('courses_record')} </a> &nbsp;|&nbsp;
											</c:if>
										   	<c:if test="${operator_session.qx_reporttoFillReport }">
											   	<c:if test="${list.state=='0' }">
												   	<a href="/report/toFillReport/${list.id }"  title="${_res.get('Reporting')}">${_res.get('Reporting')} </a> &nbsp;
											   	</c:if>
										   	</c:if>
										   	<c:if test="${operator_session.qx_reporttoFixReport }">
											   	<c:if test="${list.state=='1' }">
												   	<a href="/report/toFixReport?pointid=${list.id}&type=1"  title="${_res.get('Edit.report')} ">${_res.get('Edit.report')} </a> &nbsp;
											   	</c:if>
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
			<div style="clear:both;"></div>
		</form>
	</div>
	</div>	  
</div>  
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    

<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
     <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		//日期范围限制
		var date1 = {
			elem : '#startappointment',
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
			elem : '#endappointment',
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
		var startsubmission = {
			elem : '#startsubmission',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : false,
			choose : function(datas) {
				endsubmission.min = datas; //开始日选好后，重置结束日的最小日期
				endsubmission.start = datas //将结束日的初始值设定为开始日
			}
		};
		var endsubmission = {
			elem : '#endsubmission',
			format : 'YYYY-MM-DD',
			max : '2099-06-16',
			istime : false,
			istoday : false,
			choose : function(datas) {
				startsubmission.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(startsubmission);
		laydate(endsubmission);
	</script>
    <script>
       $('li[ID=nav-nav17]').removeAttr('').attr('class','active');
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
    <script type="text/javascript">
	    function detail(id){
	   		$.layer({
	   			type: 2,
			    shadeClose: true,
			    title: "${_res.get('courses_record')}",
			    closeBtn: [0, true],
			    shade: [0.5, '#000'],
			    border: [0],
			    offset:['50px', ''],
			    area: ['700px', '500px'],
	       	    iframe: {src: "/account/queryStudentCourseInfo/" + id}
	       	});
	    }
	</script>
</body>
</html>