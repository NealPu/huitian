<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>${_res.get("courses.roadmap") }</title>
<link href='/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<!-- 回到顶部 -->
<link type="text/css" href="/css/lrtk.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/js.js"></script>
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<style type="text/css">
	.header{font-size:12px}
	body{font-size: 12px;font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif}
	#calendar {width: 100%;margin: 0 auto}
	.student_list_wrap {position: absolute;top: 170px;left: 8em;width: 140px;overflow: hidden;z-index: 2012;background: #09f;border: 1px solide;border-color: #e2e2e2 #ccc #ccc #e2e2e2;padding: 6px}
	ul,li { list-style-type: none;margin: 0}
	.pk_info {width: 128px;float: left;height: 68px;padding: 0 4px}
	.fc-event{padding:1px 0;margin-left:2.5px}
	.fc-event-inner span{display:block;margin-left:2px}
	.bgTips span {display: inline-block}
	.bgTips span em {display: inline-block;height: 12px;width: 12px;vertical-align: middle;border: 1px solid #fff;margin-right: 5px}
	.bgTips span em.BT-1 {background-color: #71b62d}
	.bgTips span em.BT-2 {background-color: #54a4ff}
	.bgTips span em.BT-3 {background-color: #c288f2}
	.bgTips span em.BT-4 {background-color: #e68a4e}
	.bgTips span em.BT-5 {background-color: #33c0aa}
	.chosen-container-single .chosen-single{border-radius:0 !important;border:1px solid #E5E6E7 !important}
</style>
  
<script src='/js/js/jquery-2.1.1.min.js'></script>
<script src='/jquery/jquery-ui-1.10.2.custom.min.js'></script>
<script src='/fullcalendar/fullcalendar.min.js'></script>
<script src="/js/js/plugins/layer/layer.min.js"></script>
<script>
//var str="${records}";
//str=str.replace(new RegExp("<br>", 'g'),"\\n\\r");
	$(document).ready(function(){
		var tchname = '${teacherName}';
		$.ajax({
			url :  "${cxt}/teacher/getAllTeachers",
			type : "post",
			dataType : "json",
			success : function(result) {
				var str2 = "";
				for (var i = 0; i < result.teacherlist.length; i++) {
					var teacherId = result.teacherlist[i].ID;
					var teacherName = result.teacherlist[i].REAL_NAME;
					if(tchname == teacherName){
						str2 += "<option selected='selected' value='"+teacherName+"'>"+teacherName+"</option>";
					}else{
						str2 += "<option value='"+teacherName+"'>"+teacherName+"</option>";
					}
				}
				$("#teacherName").append(str2);
				$("#teacherName").trigger("chosen:updated");
			}
		});
	});

	$(document).ready(function() {
		var teacherName = $("#teacherName").val();
		if(teacherName == null || teacherName.trim() == ""){
			teacherName = '${teacherName}';
		}
		var studentName = $("#studentName").val();
		var banciName = $("#banciName").val();
		var date1 = $("#date1").val();
		var date2 = $("#date2").val();
		var oDate1 = new Date();
		if(date1 != '' && date1 != null){
			var str=date1.toString();
			str = str.replace(/-/g,"/");
			oDate1 = new Date(str);
		}
		var year = oDate1.getFullYear();
		var month = oDate1.getMonth();
		$('#calendar').fullCalendar({
			editable: true,
			year:year,
			month:month,
			events : function(start, end, callback) {
				var startTime = $.fullCalendar.formatDate(start, "yyyy-MM-dd");
				var endTime = $.fullCalendar.formatDate(end, "yyyy-MM-dd");
				if(date1 == '' || date1 == null){
					startTime = $.fullCalendar.formatDate(start, "yyyy-MM-dd");
				}else{
					startTime = date1 ;
				}
				if(date2 == '' || date2 == null){
					endTime = $.fullCalendar.formatDate(end, "yyyy-MM-dd");
				}else{
					endTime = date2;
				}
				$.ajax({
					type : "post",
					url : "/course/courseSortListForMonthGetJson",
					success : function(result) {
						str = result.replace(new RegExp("<br>", 'g'), "\\n\\r");
						callback(eval(str)); //填写信息
					},
					data : {
						"flag" : "0",
						"studentName" : studentName,
						"banciName" : banciName,
						"teacherName" : teacherName,
						"startTime" : startTime,
						"endTime" : endTime,
						"myCourse" : false,
						/* "starthour" : $("#starthour").val(),
						"endhour" : $("#endhour").val(),
						"startmin" : $("#startmin").val(),
						"endmin" : $("#endmin").val(), */
						"campusid" : $("#campusid").val(),
						"timerankid" : $("#timerankid").val()
					},
					cache : false,
					error : function(XMLHttpRequest, textStatus, errorThrown) {
					// alert(XMLHttpRequest);
					}
				});
			},
			eventMouseover : function(event){
				var massage = event.msg;
				var classId = event.classId;
				var stu = event.student;
				var net = event.netCourse;
				if(classId == 0){
					var str = "";
					if(massage != "暂无"){
						str += "备注:"+event.msg+"<br>";
					}
					if(net == 1){
						str += " ${_res.get('course.netcourse')} ";
					}
					if(massage != "暂无" || net == 1 ){
						layer.tips(str, this, {
							style: ['background-color:#78BA32; color:#fff', '#78BA32'],
							maxWidth:185,
							guide: 1,
							time: 6,
							closeBtn:[0, true]
						});
					}
				}else{
					var str = "";
					if(stu != "无"){
						str += "${_res.get('student')}:"+event.student+"<br>";
					}
					if(massage != "暂无"){
						str += "备注:"+event.msg+"<br>";
					}
					if(net == 1){
						str += " ${_res.get('course.netcourse')} ";
					}
					if(stu != "无" || massage != "暂无" || net == "1"){
						layer.tips(str, this, {
							style: ['background-color:#78BA32; color:#fff', '#78BA32'],
							maxWidth:185,
							guide: 1,
							time: 6,
							closeBtn:[0, true]
						});
					}
				}
			},
			eventClick : function(event, jsEvent, view) {
				if(event.plantype!=2){
					if('${operator_session.qx_knowledgegetCourseplanMsg }'){
						/*window.location.href = "/knowledge/getCourseplanMsg?courseplanId=" + event.courseplanId;*/
					 	window.location.href = "/knowledge/educationalManage?courseplanId=" + event.courseplanId;
					}
				}
			},
			eventAfterRender : function(event, element, view) {
				if(event.plantype == 2){
					if(event.campustype == 1){
						if(event.netCourse == 1) {
							element.css("background-color", "#54a4ff");
							element.css("border-color", "#54a4ff");
						}else if(event.classId > 0){
							element.css("background-color", "#c288f2");
							element.css("border-color", "#c288f2");
						}else{
							element.css("background-color", "#e68a4e");
							element.css("border-color", "#e68a4e");
						}
					}else{
						element.css("background-color", "#33c0aa");
						element.css("border-color", "#33c0aa");
					}
					element.css("background-color", "#71b62d");
					element.css("border-color", "#71b62d");
				}else{
					if(event.campustype == 1){
						if(event.netCourse == 1) {
							element.css("background-color", "#54a4ff");
							element.css("border-color", "#54a4ff");
						}else if(event.classId > 0){
							element.css("background-color", "#c288f2");
							element.css("border-color", "#c288f2");
						}else{
							element.css("background-color", "#e68a4e");
							element.css("border-color", "#e68a4e");
						}
					}else{
						element.css("background-color", "#33c0aa");
						element.css("border-color", "#33c0aa");
					}
				}
			},
			monthNames: ["${_res.get('system.monthNames.January')}", "${_res.get('system.monthNames.February')}", "${_res.get('system.monthNames.March')}", 
			             "${_res.get('system.monthNames.April')}", "${_res.get('system.monthNames.May')}", "${_res.get('system.monthNames.June')}", 
			             "${_res.get('system.monthNames.July')}", "${_res.get('system.monthNames.August')}", "${_res.get('system.monthNames.September')}", 
			             "${_res.get('system.monthNames.October')}", "${_res.get('system.monthNames.November')}", "${_res.get('system.monthNames.December')}"],  
			monthNamesShort: ["${_res.get('system.monthNamesShort.Jan')}", "${_res.get('system.monthNamesShort.Feb')}", "${_res.get('system.monthNamesShort.Mar')}", 
			                  "${_res.get('system.monthNamesShort.Apr')}", "${_res.get('system.monthNamesShort.May')}", "${_res.get('system.monthNamesShort.Jun')}", 
			                  "${_res.get('system.monthNamesShort.Jul')}", "${_res.get('system.monthNamesShort.Aug')}", "${_res.get('system.monthNamesShort.Sept')}", 
			                  "${_res.get('system.monthNamesShort.Oct')}", "${_res.get('system.monthNamesShort.Nov')}", "${_res.get('system.monthNamesShort.Dec')}"],  
			dayNames: ["${_res.get('system.Sunday')}", "${_res.get('system.Monday')}", "${_res.get('system.Tuesday')}", "${_res.get('system.Wednesday')}",
			           "${_res.get('system.Thursday')}", "${_res.get('system.Friday')}", "${_res.get('system.Saturday')}"],  
			dayNamesShort: ["${_res.get('system.dayNamesShort.Sun')}", "${_res.get('system.dayNamesShort.Mon')}", "${_res.get('system.dayNamesShort.Tues')}", "${_res.get('system.dayNamesShort.Wed')}",
			                "${_res.get('system.dayNamesShort.Thur')}", "${_res.get('system.dayNamesShort.Fri')}", "${_res.get('system.dayNamesShort.Sat')}"],  
			today: ["今天"],  
			firstDay: 1,  
			buttonText: {  
				today: '${_res.get("course.currentMonth")}',  
				month: '月',  
				week: '周',  
				day: '日',  
				prev: '${_res.get("course.lastMonth")}',  
				next: '${_res.get("course.nextMonth")}'  
			}
		});
	});

	$(document).ready(function(){
		$('#studentName').keyup(function(){
			var studentName = $("#studentName").val().trim();
			if (studentName != "") {
			var studentName=$("#studentName").val();
			$.ajax({
				url :  "${cxt}/account/getAccountByNameLike",
				data :"studentName="+studentName,
				type : "post",
				dataType : "json",
				success : function(result) {
					if(result.accounts != null){
						if(result.accounts.length == 0){
							$("#studentName").focus();
							return false;
						}else{
							var str="";
							for(var i = 0 ; i < result.accounts.length ; i++){
								var studentId = result.accounts[i].ID;
								var realName = result.accounts[i].REAL_NAME;
								if(studentName == realName){
									$("#studentId").val(studentId);
									$("#mohulist").hide();
									dianstu(studentId);
									return;
								}else{
									str += "<li onclick='dianstu(" + studentId + ")'>" + realName + "</li>";
								}
							}
							$("#stuList").html(str);
							$("#mohulist").show();
						}
					}else{
						$("#stuList").html("");
						$("#mohulist").hide();
						$("#studentName").focus();
					}
				}
			});
			}
		});
	});

	function dianstu(stuId){
		$("#mohulist").hide();
		$("#mohulist").html();
		$.ajax({
			url :  "/student/queryCourseInfo",
			data :"studentId="+stuId,
			type : "post",
			dataType : "json",
			success : function(result) {
				if(result.account!="noResult"){
					$("#studentName").val(result.account.stuName);
				}else{
					alert("此学员不存在");
				}
			},
			error:function(result){
				alert("ERROR! 请重新登录或刷新页面重试！");
			}
		});
	}
</script>
</head>
<body>
<div id="wrapper" style="background: #2f4050;min-width:1250px;">
	<%@ include file="/common/left-nav.jsp"%>
	<div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom" >
			<nav class="navbar navbar-static-top fixtop" role="navigation">
				<%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>
		<div class="ibox float-e-margins margin-nav" style="margin-left:0;">
			<div class="ibox-title">
				<h5>
					<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					<a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> &gt; ${_res.get("courses.roadmap") }
				</h5>
				<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
			</div> 
			<div class="ibox-content">
				<form action="/course/courseSortListForMonth" method="post">
					<fieldset id="searchTable">
						<span class="m-r-sm text-muted welcome-message">
							<label>${_res.get("student") }/${_res.get("classNum") }：</label>
							<input type="text" name="studentName" id="studentName" value="${studentName}"/>
						</span>
						<div id="mohulist" class="student_list_wrap" style="display: none">
							<ul style="margin-bottom: 10px;" id="stuList"></ul>
						</div>&nbsp;&nbsp;&nbsp;
						<span class="m-r-sm text-muted welcome-message">
							<label>${_res.get("teacher") }：</label>
							<select name="teacherName" id="teacherName" class="chosen-select" style="width:150px;">
								<option value="" >${_res.get("system.alloptions") }</option>
							</select>
						</span>
						<span class="m-r-sm text-muted welcome-message">
							<label>${_res.get("system.campus") }：</label>
							<select id="campusid" name="campusid" class="chosen-select" style="width:150px;">
								<option value="">${_res.get("system.alloptions")}</option>
								<c:forEach items="${campuslist}" var="campuslist">
									<option value="${campuslist.Id }" <c:if test="${campuslist.Id == campusid }" >selected="selected"</c:if> >${campuslist.CAMPUS_NAME }</option>
								</c:forEach>
							</select>
						</span>
						<div class="form-group" style="margin-top:15px;">
							<div style="float: left;margin:6px 5px 0 0"> 
								<label style="float: left;margin-top:0px">${_res.get("course.class.date") }：</label>
								<div style="float: left"> 
									<input type="text" class="form-control layer-date date_double" readonly="readonly" id="date1" name="date1" value="${date1}" style="background-color:#fff; width:150px;" />
								</div>
								<div style="width:30px;height:30px;line-height:30px;text-align:center;background:#E5E6E7;float: left;margin-top:-7px">${_res.get('to')}</div>
								<div style="float: left"> 
									<input type="text" readonly="readonly" class="form-control layer-date date_double" id="date2" name="date2" value="${date2}" style="background-color:#fff; width:150px;" />
								</div>
							</div>
							<%-- <div class="m-r-sm text-muted welcome-message">
								<label style="float: left;margin-top:6px">${_res.get("system.time") }：</label>
								<div style="float: left">
									<select id="starthour" name="starthour" class="chosen-select" style="width:70px;">
										<option value="">00</option>
										<c:forEach items="${hour}" var="starthourlist">
											<option value="${starthourlist.key }" <c:if test="${starthourlist.key == starthour }" >selected="selected"</c:if> >${starthourlist.value }</option>
										</c:forEach>
									</select>&nbsp;:&nbsp;
									<select id="startmin" name="startmin" class="chosen-select" style="width:70px;">
										<option value="">00</option>
										<c:forEach items="${min}" var="startminlist">
											<option value="${startminlist.key }" <c:if test="${startminlist.key == startmin }" >selected="selected"</c:if> >${startminlist.value }</option>
										</c:forEach>
									</select>
								</div>
								<div style="width:30px;height:30px;line-height:30px;text-align:center;background:#E5E6E7;float: left;margin-top:0px">${_res.get('to')}</div>
								<div style="float: left">
									<select id="endhour" name="endhour" class="chosen-select" style="width:70px;">
										<option value="">23</option>
										<c:forEach items="${hour}" var="endhourlist">
											<option value="${endhourlist.key }" <c:if test="${endhourlist.key == endhour }" >selected="selected"</c:if> >${endhourlist.value }</option>
										</c:forEach>
									</select>&nbsp;:&nbsp;
									<select id="endmin" name="endmin" class="chosen-select" style="width:70px;">
										<option value="">59</option>
										<c:forEach items="${min}" var="endminlist">
											<option value="${endminlist.key }" <c:if test="${endminlist.key == endmin }" >selected="selected"</c:if> >${endminlist.value }</option>
										</c:forEach>
									</select>
								</div> 
							</div> --%>
							<span style="clear: both;"></span>
						</div>
						<span class="m-r-sm welcome-message">
						&nbsp;&nbsp;<input type="submit" value="${_res.get('admin.common.select') }" class="btn btn-outline btn-primary" style="margin-top:-2px"/>&nbsp;&nbsp;
						</span>
					</fieldset>
				</form>
			</div>
			<div class="col-lg-12" style="margin:20px 0 0;padding:0">
				<div class="ibox float-e-margins" style="min-width:1000px;">
					<div class="ibox-title">
						<h5>${_res.get("course.list") }</h5>
						<div class="bgTips" style="margin-left:100px;">
							<span>
								<em class="BT-1"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.rest") }
							</span>
							<span>
								<em class="BT-2"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.netcourse") }
							</span>
							<span>
								<em class="BT-4"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.course") }
							</span>
							<span>
								<em class="BT-3"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.classes") }
							</span>
							<span>
								<em class="BT-5"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.outcourse") }
							</span>
						</div>
					</div>
					<div class="ibox-content">
						<div id='calendar' class="fc fc-ltr"></div>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
		</div>
	</div>
</div>
<div id="tbox" style="z-index: 9999;">
	<a id="gotop" href="javascript:void(0)"></a>
</div>
<!-- Mainly scripts -->
<script src="/js/js/bootstrap.min.js?v=1.7"></script>
<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="/js/js/hplus.js?v=1.7"></script>
<script src="/js/js/top-nav/teach-1.js"></script>
<script>
	$('li[ID=nav-nav4]').removeAttr('').attr('class','active');
</script>
<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
<script>
	//日期范围限制
	var date1 = {
		elem: '#date1',
		format: 'YYYY-MM-DD',
		max: '2099-06-16', //最大日期
		istime: false,
		istoday: false,
		choose: function (datas) {
			date2.min = datas; //开始日选好后，重置结束日的最小日期
			date2.start = datas //将结束日的初始值设定为开始日
		}
	};
	var date2 = {
		elem: '#date2',
		format: 'YYYY-MM-DD',
		max: '2099-06-16',
		istime: false,
		istoday: false,
		choose: function (datas) {
			date1.max = datas; //结束日选好后，重置开始日的最大日期
		}
	};
	laydate(date1);
	laydate(date2);
</script> 
<!-- Chosen -->
<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
<script>     
	$(".chosen-select").chosen({disable_search_threshold: 10});
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
</body>
</html>