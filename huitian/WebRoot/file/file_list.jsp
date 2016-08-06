<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>${_res.get('Documents.List')}</title>
<meta name="save" content="history">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
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
 h5{
   font-weight: 100 !important
 }
 .flolet{float: left;margin:0 10px 0 0}
</style>
</head>
<body id="body" style="min-width:1100px;">   
	<div id="wrapper" style="background: #2f4050;height:100%;">
	 <%@ include file="/common/left-nav.jsp"%>
	 <div class="gray-bg dashbard-1" id="page-wrapper" style="height:100%">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			   <%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

        <div class="margin-nav2">
		<form action="" method="post" id="searchForm" >
			<div  class="col-lg-12 m-t-xzl" style="padding-left:0;">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
						<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
						<a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a>
						&gt;${_res.get('Documents.List')}
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				   <div>
						<label style="float: left;margin:6px 0 0 0">${_res.get('Uploaded')}：</label> 
						<input type="text" class="form-control layer-date" readonly="readonly" id="date1" name="_query.begindate" value="${begindate }" style="width:150px;background:#fff" />
						--&nbsp;${_res.get('to')}&nbsp;--
						<input type="text" class="form-control layer-date" readonly="readonly" id="date2" name="_query.enddate" value="${enddate }" style="width:150px;background:#fff" />&nbsp; 
					   
					    <label>${_res.get('student')}：</label>
						<input type="text" id="" name="_query.studentname" style="width:150px;" value="${studentname }">
						<label>${_res.get('File.name')}：</label>
						<input type="text" id="" name="_query.documentname" style="width:150px;" value="${documentname }">
						<br><br>
					    <input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-info">
					    <c:if test="${operator_session.qx_documenttoUploaddocument }">
						    <input type="button" value="${_res.get('teacher.group.add')}" class="btn btn-outline btn-success" onclick="addfile()">
					    </c:if>
					</div>
					<div style="clear: both;"></div>
			   </div>
			 </div>
		   </div>

			<div class="col-lg-12" style="padding-left:0;">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get('Documents.List')}</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
							<tr align="center">
								<th>${_res.get("index")}</th>
								<th>${_res.get('student')}</th>
								<th>${_res.get('File.name')}</th>
								<th>${_res.get('Document.Type')}</th>
								<th>${_res.get('File')}</th>
								<th>${_res.get('Uploaded')}</th>
								<th>${_res.get('Upload.person')}</th>
								<th>${_res.get('operation')}</th>
							</tr>
							</thead>
							<c:forEach items="${showPages.list }" var="list" varStatus="index" >
								<tr align="center" >
									<td>${index.count }</td>
									<td>${list.stuname }</td>
									<td>${list.documentname }</td>
									<td>${list.contenttype }</td>
									<td>${list.filename }</td>
									<td><fmt:formatDate value="${list.uploadtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/> </td>
									<td>${list.uldname }</td>
									<td>
									<%-- <a  href="/document/previewFile/${list.id }">${_res.get('Preview')}</a> --%>
									<a  href="/document/downloadFile/${list.id }">${_res.get('Download')}</a></td>
								</tr>
							</c:forEach>
						</table>
						<div id="splitPageDiv">
							<jsp:include page="/common/splitPage.jsp" />
						</div>
					</div>
				</div>
			</div>
			<div style="clear:both "></div>
		</form>
		</div>
	   </div>
	</div>
<!-- Chosen -->
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>

    <script>
    $(".chosen-select").chosen({disable_search_threshold: 20});
        var config = {
            '.chosen-select': {},
            '.chosen-select-deselect': {
                allow_single_deselect: true
            },
            '.chosen-select-no-single': {
                disable_search_threshold: 10
            },
            '.chosen-select-no-results': {
                no_results_text: 'Oops, nothing found!'
            },
            '.chosen-select-width': {
                width: "95%"
            }
        }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }   
    </script>	
	
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script src="/js/js/demo/layer-demo.js"></script>
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
	
  	<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
    $('li[ID=nav-nav20]').removeAttr('').attr('class','active');
    </script>
    <script type="text/javascript">
    
    function addfile(){
	    $.layer({
		    type: 2,
		    shadeClose: true,
		    title: "${_res.get('Adding.document')}",
		    closeBtn: [0, true],
		    shade: [0.5, '#000'],
		    border: [0],
		    offset:['', ''],
		    area: ['450px', '400px'],
		    /* iframe: {src: "${cxt}/file/file_form.jsp"} */
		     iframe: {src: "${cxt}/document/toUploaddocument"} 
		});
    }
    </script>
</body>
</html>