<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get('intelligent_scheduling')}</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
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

	<script type="text/javascript">
		$(function(){
			$('#studentName').keyup(function(){
				var studentName = $("#studentName").val().trim();
				if (studentName != "") {
					var studentName=$("#studentName").val();
						$.ajax({
							url :  "${cxt}/smartplan/getAccountByNameLike",
							data :"studentName="+studentName,
							type : "post",
							dataType : "json",
							success : function(result) {
								if(result.accounts!=null){
									if(result.accounts.length==0){
										$("#studentName").focus();
										$("#studentInfo").text("学生不存在！");
										return false;
									}else{
										var str="";
										for(var i=0;i<result.accounts.length;i++){
											var studentId = result.accounts[i].ID;
											var realName = result.accounts[i].REAL_NAME;
											if(studentName==realName){
												$("#studentId").val(studentId);
												$("#studentName").val(studentName);
												$("#mohulist").hide();
												return;
											}else{
												str += "<li onclick='dianstu(\"" + studentId+"\",\""+realName+"\")'>" + realName + "</li>";
											}
										}
										$("#stuList").html(str);
										$("#mohulist").show();
									}
								}else{
									$("#stuList").html("");
									$("#mohulist").hide();
									$("#studentName").focus();
									$("#studentInfo").text("学生不存在！");
								}
								
							}
						});
			}else{
				$("#studentInfo").text("");
				$("#studentId").val("");
				$("#studentName").val("");
				$("#stuList").html("");
				$("#mohulist").hide();
			}
			});	
		});
		
		function dianstu(studentId,name){
			$("#studentId").val(studentId);
			$("#studentName").val(name);
			$("#mohulist").hide();
		}
		
	</script>
	


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
.jizhun-mar{
  margin:10px 0
}
</style>
<style type="text/css">
.stu_name {
	position: relative;
	margin-bottom: 15px;
}

.stu_name label {
	display: inline-block;
	font-size: 12px;
	vertical-align: middle;
	width: 100px;
}

.student_list_wrap {
	position: absolute;
	top: 96px;
	left: 8em;
	width: 100px;
	overflow: hidden;
	z-index: 2012;
	background: #09f;
	border: 1px solide;
	border-color: #e2e2e2 #ccc #ccc #e2e2e2;
	padding: 6px;
}
.tds{
	text-align : left;
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;min-width:1100px;">
	  <%@ include file="/common/left-nav.jsp"%>
       <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			   <%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

	    <div class="margin-nav">	
		<form action="/smartplan/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
					    &gt;<a href='/course/queryAllcoursePlans?loginId=${account_session.id}&returnType=1'>${_res.get('curriculum_management')}</a> &gt;${_res.get('intelligent_scheduling')}
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<input type="hidden" id="studentId" name="_query.studentid" value="${paramMap['_query.studentid'] }"/>
				<label> ${_res.get("student")}/${_res.get("group.class")}： </label>
				<input type="text" id="studentName" name="_query.studentname" value="${paramMap['_query.studentname'] }" />
				<div id="mohulist" class="student_list_wrap" style="display: none">
					<ul style="margin-bottom: 10px;" id="stuList"></ul>
				</div> 
				<label>${_res.get('system.campus')}：</label>
				<select id="campusid" class="chosen-select" style="width: 150px;" tabindex="2" name="_query.campusid"  >
					<option value="">--${_res.get('system.alloptions')}--</option>
					<c:forEach items="${campuslist }" var="campus"  >
						<option value="${campus.id }" <c:if test="${campus.id == paramMap['_query.campusid'] }">selected="selected"</c:if> >${campus.CAMPUS_NAME }</option>
					</c:forEach>
				</select>
				&nbsp;
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">&nbsp;
				<c:if test="${operator_session.qx_smartplansetNewRule }">
					<input type="button" value="${_res.get('new.regulation')}" class="btn btn-outline btn-info" onclick="guize()">
				</c:if>
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get('intelligent_scheduling.list')}</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>${_res.get('student')}</th>
									<th>${_res.get('Week')}</th>
									<th>${_res.get('system.campus')}</th>
									<th>${_res.get('time.session')}</th>
									<th>${_res.get('course.course')}</th>
									<th>${_res.get('teacher')}</th>
									<th>${_res.get('student.buildtime')}</th>
									<th>${_res.get('person.who.add')}</th>
									<th>${_res.get('times.of.arrangement')}</th>
									<th>${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="list" varStatus="index">
								<tr align="center">
									<td>${index.count }</td>
									<td>${list.stuname }</td>
									<td>${list.weekday }</td>
									<td>${list.CAMPUS_NAME }</td>
									<td>${list.RANK_NAME }</td>
									<td>${list.COURSE_NAME }</td>
									<td>${list.teachername }</td>
									<td><fmt:formatDate value="${list.addtime }" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>${list.addusername }</td>
									<td onclick="showLastCourse(${list.ID})" >${list.usedtimes }</td>
									<td>
										<c:if test="${operator_session.qx_smartplansetRowRuleCourse }">
											<a href="#" style="color: #515151" onclick="samePeople(${list.ID})">${_res.get('course.arranging')}</a>|
										</c:if>
										<%-- <a href="#" style="color: #515151" onclick="updateRule(${list.ID})">${_res.get('Modify')}</a>| --%>
										<c:if test="${operator_session.qx_smartplandelPlanRule }">
											<a href="#" style="color: #515151" onclick="delRule(${list.ID})">${_res.get('admin.common.delete')}</a>
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
		function guize(){
			var stuid = $("#studentId").val();
			var campusid = $("#campusid").val();
			if(stuid==null||stuid==""){
				layer.msg("请先填写学生.",2,5);
			}else{
				$.layer({
			        type: 2,
			        title: "${_res.get('new.regulation')}",
			        maxmin: false,
			        shadeClose: true, //开启点击遮罩关闭层
			        area : ['600px' , '510px'],
			        offset : ['100px', ''],
			        iframe: {src: "/smartplan/setNewRule?stuid="+stuid+"&campusid="+campusid}
			    });
			}
			
		}
		
		function samePeople(value){
			$.layer({
		        type: 2,
		        title: "${_res.get('new.regulation')}",
		        maxmin: false,
		        shadeClose: true, //开启点击遮罩关闭层
		        area : ['720px' , '510px'],
		        offset : ['80px', ''],
		        iframe: {src: "/smartplan/setRowRuleCourse/"+value}
		    });
		}
	</script>	
	
	<script type="text/javascript">
		function delRule(id){
			$.ajax({
				url:"/smartplan/delPlanRule",
				data:{"id":id},
				type:"post",
				dataType:"json",
				success:function(result){
					layer.msg(result.msg,2,0);
					if(result.code=="1"){
		    			setTimeout("window.location.href='/smartplan/index'",2000);
					}
				}
			});
		}
		
		function showLastCourse(val){
			$.ajax({
				url:"${cxt}/smartplan/showStudentLastCourse",
				type:"post",
				data:{"id":val},
				dataType:"json",
				success:function(result){
					var content = result.msg;
					var pageii = $.layer({
					    type: 1,
					    title: false,
					    area: ['auto', 'auto'],
					    border: [0], //去掉默认边框
					    shade: [0], //去掉遮罩
					    closeBtn: [0, false], //去掉默认关闭按钮
					    shift: 'left', //从左动画弹出
					    page: {
					        html: '<div style="width:480px; padding:10px; border:1px solid #ccc; background-color:#FFF;"><p>'+content+'</p><button id="pagebtn" class="btn btn-outline btn-warning" onclick="">关闭</button></div>'
					    }
					});
					//自设关闭
					$('#pagebtn').on('click', function(){
					    layer.close(pageii);
					});
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
     <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
	<script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
    function data(){
         //开始日期范围限制
        var birthday = {
            elem: '#data',
            format: 'YYYY-MM-DD',
            max : '2099-06-16', //最大日期
            istime: false,
            istoday: false
        };
        laydate(birthday);
    }    
    </script>
    <script>
   	$('li[ID=nav-nav3]').removeAttr('').attr('class','active');
    </script>
</body>
</html>