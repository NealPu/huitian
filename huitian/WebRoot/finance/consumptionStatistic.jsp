<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>交费记录列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
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

		<div class="margin-nav" style="min-width:1000px;width:100%;">
		<form action="/finance/consumptionStatistic" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					      &gt;财务管理&gt; 消耗统计
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
				</div>
				<div class="ibox-content" style="height:auto;">
				<label>${_res.get("student.name")}：</label>
					<input type="text" id="studentname" name="_query.studentname" value="${paramMap['_query.studentname']}">
				<label>科目：</label>
				<select id="subjectId" name="_query.subjectId" class="chosen-select" style="width: 150px;" onchange="chooseCourse()">
					<option value="">全部</option>
					<c:forEach items="${subjectList }" var="subject">
						<option value="${subject.id }" ${subject.id eq paramMap['_query.subjectId']?'selected="selected"':'' } >${subject.subject_name }</option>
					</c:forEach>
				</select>
				<label>课程：</label>
				<select id="courseId" name="_query.courseId" class="chosen-select" style="width: 150px;">
					<option value="">全部</option>
					<c:forEach items="${courseList }" var="course">
						<option value="${course.id }" ${course.id eq paramMap['_query.courseId']?'selected="selected"':'' } >${course.course_name }</option>
					</c:forEach>
				</select>
				<label>校区：</label>
				<select id="campusId" name="_query.campusId" class="chosen-select" style="width: 150px;">
					<option value="">全部</option>
					<c:forEach items="${campusList }" var="campus">
						<option value="${campus.id }" ${campus.id eq paramMap['_query.campusId']?'selected="selected"':'' } >${campus.campus_name }</option>
					</c:forEach>
				</select>
				<br><br>
				<label>授课类型：</label>
				<select id="teachType" name="_query.teachType" class="chosen-select" style="width: 150px;">
					<option value="">全部</option>
					<option value="1" ${1 eq paramMap['_query.teachType']?'selected="selected"':'' } >1对1</option>
					<option value="2" ${2 eq paramMap['_query.teachType']?'selected="selected"':'' } >小班</option>
				</select>
				<label>课程日期：</label>
					 <input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.beginDate" value="${paramMap['_query.beginDate']}" style="margin-top: -8px; width:120px" /> 
					至<input type="text" class="layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px" />
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>消耗列表</h5>
						<span style="float: right;">总课时：${totalHour }/总消耗额：${totalAmount }</span>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>课程日期</th>
									<th>时段</th>
									<th>科目</th>
									<th>课程</th>
									<th>学生/班次</th>
									<th>授课教师</th>
									<th>校区/教室</th>
									<th>课时</th>
									<th>金额</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="coursePlan" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${coursePlan.courseTime}</td>
									<td>${coursePlan.rank_name}</td>
									<td>${coursePlan.subject_name}</td>
									<td>${coursePlan.course_name}</td>
									<td>${coursePlan.studentname}</td>
									<td>${coursePlan.teachername}</td>
									<td>${coursePlan.campus_name}/${coursePlan.roomname}</td>
									<td>${coursePlan.class_hour}</td>
									<td>${coursePlan.totalfee}</td>
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
    <script type="text/javascript">
    
    function showDetail(accountid){
    	window.location.href = "${cxt}/accountbook/index?_query.accountid="+accountid;
    	
    }
    
    function applyRefund(stuid){
    	$.ajax({
    		url:"/finance/checkCanRefund",
    		data:{"id":stuid},
    		datatype:"json",
    		type:"post",
    		success:function(result){
    			if(result.code==1){
			    	window.location.href = "${cxt}/finance/applyRefund?id="+stuid;
    			}else{
    				layer.msg(result.msg,3,2);
    			}
    		}
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
    <script src="/js/js/top-nav/money.js"></script>
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
		
		function chooseCourse() {
			var subjectId = $("#subjectId").val();
			$("#courseId").html("");
			var str = "<option value=''>全部</option>";
			if(subjectId != ""){
				$.ajax({
					url : '/course/getCourseBySubjectIds',
					type : 'post',
					data : {
						'ids' : subjectId
					},
					dataType : 'json',
					success : function(data) {
						if (data.length > 0) {
							for (var i = 0; i < data.length; i++) {
								str += '<option value="'+data[i].COURSEID+'">' + data[i].COURSENAME + '</option>'
							}
						}
						$("#courseId").append(str);
						$("#courseId").trigger("chosen:updated");
					}
				});
			}else{
				$("#courseId").append(str);
				$("#courseId").trigger("chosen:updated");
			}
		}
	</script>
    <script>
       $('li[ID=nav-nav10]').removeAttr('').attr('class','active');
    </script>
</body>
</html>