<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script src="/js/js/jquery-2.1.1.min.js"></script>
<!-- Mainly scripts -->
<script src="/js/js/bootstrap.min.js?v=1.7"></script>
<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="/js/js/hplus.js?v=1.7"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>教案库</title>
<!-- Mainly scripts -->
<style type="text/css">
ul, ol, li {
	list-style: none;
}

.header {
	font-size: 12px;
}

.kechengjilu {
	display: inline;
	width: 60px;
	height: 34px;
	line-height: 34px;
	margin-top: 10px;
}

select {
	margin: 0px 0 0 10px;
}

.dengemail:hover {
	text-decoration: none;
}

#sign {
	width: 150px;
}
</style>
</head>
<script type="text/javascript">
	$(document).ready(
			function() {
				if ($('#returnMsg').html().length > 5) {
					$('#returnMsg').show();
				} else {
					$('#returnMsg').hide();
				}
				var coursename = $("#coursename").val();
				$("#mingcheng").html("" + coursename);
				var course_id = $("#course_id").val();
				var courseplan_id = $("#courseplan_id").val();
				//回显打分的信息
				$.ajax({
					type : "post",
					url : "/teachergrade/getTeachergrade",
					dataType : "json",
					data : {
						"courseplan_id" : courseplan_id
					},
					success : function(value) {
						
					}
				});
				var course_id = $("#course_id").val();
				$.ajax({
					type : "post",
					url : "/course/getSignMessage?studentId=" + $("#studentId").val() + "&&courseplan_id=" + $("#courseplan_id").val(),
					dataType : "json",
					success : function(value) {
						$("#course").html(value.record4);
						$("#coursePlan").html(value.record1.COURSENUM);
						$("#signCourse").html(value.record2.SIGNNUM);
						$("#noSignCourse").html(value.record3);
						$("#happen").html(value.record6.HAPPEN);
						$("#late").html(value.record5);

					},
					error : function(error) {
						// alert(error);
					}
				});
				$.ajax({
					type : "post",
					url : "/account/getStudentMessage?studentId=" + $("#studentId").val(),
					dataType : "json",
					success : function(value) {
						if (value.errorMessage != null) {
							alert(value.errorMessage);
							window.location.href = "/course/courseSort_month.jsp";
						}
						if (value.remark == null) {
							$("#remark").append("<li>${_res.get('course.remarks')}：${_res.get('courselib.nomessage')}</li>");
						} else {
							$("#remark").append("<li class='clearfix'><span class='fl'>${_res.get('course.remarks')}：</span><span style='display: block;width: 230px;' class='fl SM-Num'>" + value.remark + "</span></li>");
						}
						/* if (value.pqList == null) {
							$("#grade1").append("<li>暂无分数信息</li>");
						} else {
							for (i in value.pqList) {
								$("#grade1").append("<div><li>" + value.pqList[i].SUBJECTNAME + "培前分数：");
								for (j in value.pqList[i].DETAIL) {
									$("#grade1").append("<br><span><label class='duiqi'>" + value.pqList[i].DETAIL[j].COURSENAME + ":</label>" + value.pqList[i].DETAIL[j].SCORE + "</span></div></li><br>");
								}
							}
						} */
						/* if (value.phList == null) {
							$("#grade2").append("<li>暂无分数信息</li>");
						} else {
							for (i in value.phList) {
								$("#grade2").append("<div><li>" + value.phList[i].SUBJECTNAME + "培后分数：");
								for (j in value.phList[i].DETAIL) {
									$("#grade2").append( "<br><span><label class='duiqi'>" + value.phList[i].DETAIL[j].COURSENAME + ":</label>" + value.phList[i].DETAIL[j].SCORE + "</span></li></div><br>");
								}
							}
						} */
					},
					error : function(error) {
						// alert(error);
					}
				});

			});
</script>
<body>
	<div id="wrapper" style="background: #2f4050;">
		<%@ include file="/common/left-nav.jsp"%>
		<div class="gray-bg dashbard-1" id="page-wrapper">
			<div class="row border-bottom yincang" style="margin: 0 0 60px;">
				<nav class="navbar navbar-static-top" role="navigation" style="margin-left: -15px; position: fixed; width: 100%; background-color: #fff;border:0">
					<div class="navbar-header">
						<input type="hidden" id="course_id" value="${course_id}" /> 
						<input type="hidden" id="courseplan_id" value="${courseplan_id}" /> 
						<input type="hidden" id="coursename" value="${coursename}" /> 
						<input type="hidden" id="returnUrl" value="${returnUrl}" /> 
						<input type="hidden" id="studentId" value="${studentId}" /> 
						<input type="hidden" id="campusId" value="${campusId}" /> 
						<input type="hidden" id="courseDate" value="${courseDate}" /> 
						<input type="hidden" id="rankTime" value="${rankTime}" />
						<input type="hidden" id="understandings" value="${_res.get('understanding')}" />
						<input type="hidden" id="attentions" value="${_res.get('attention')}" />
					</div>
					<%@ include file="/common/top-index.jsp"%>
				</nav>
			</div>
			<div class="margin-nav">
				<div class="wrapper wrapper-content animated fadeInRight" style="padding:0 10px 40px">
					<div class="row">
					   <div class="ibox-title" style="margin-bottom:20px">
							<h5>
								<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
								<a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> &gt;
								${_res.get("curriculum") } &gt; ${_res.get("curriculum_arrangement") } &gt; <span id="mingcheng"></span>
							</h5>
							<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float: right">${_res.get("system.reback") }</a>
			                <div style="clear: both;"></div>
						</div>
								<div class="col-lg-4 ui-sortable" style="padding-left:0">
									<div class="ibox float-e-margins">
										<div class="ibox-title">
											<h5>${_res.get("courselib.monthCount") }</h5>
										</div>
										<div class="ibox-content">
											<div id="message">
												<li><c:if test="${((record.tworktype eq '1' && record.fullsign==true) || (record.tworktype eq '0'&& record.partsign==true))}">
														<a href="javascript: void(0)" id="sign" onclick="signin()" class="btn btn-outline btn-primary"></a>
													</c:if> 
													<c:if test="${((record.tworktype eq '1' && record.fullsign==true)||(record.tworktype eq '0' && record.partsign==true))}">
														<p>
															<label class="duiqi">${_res.get("courselib.normalCourse") }：</label><span id="course"></span>
														</p>
													</c:if>
													<p>
														<label class="duiqi">${_res.get("courselib.planedCourse") }：</label><span id="coursePlan"></span>节&nbsp;|&nbsp;${_res.get("courselib.finishedCourse") }：<span id="happen"></span>节
													</p> 
													<c:if test="${((record.tworktype eq '1' && record.fullsign==true)||(record.tworktype eq '0' && record.partsign==true))}">
														<p>
															<label class="duiqi">${_res.get("courselib.Signin") }：</label>
															<span id="signCourse" style="color: green;"></span>节&nbsp;|&nbsp;
															${_res.get("courselib.notSignin") }：<span id="noSignCourse" style="color: red;"></span>节 &nbsp;|&nbsp;
															${_res.get("courselib.late") }：<span id="late"></span>节
														</p>
														<c:if test="${operator_session.qx_courseplangetTeacherMessage }">
															<a href="/courseplan/getTeacherMessage?_query.tid=${record.teatcherId}&_query.startTime=${record.courseTime}" class="dengemail">${_res.get("courselib.historycount") }</a>
														</c:if>
													</c:if></li>
											</div>
										</div>
									</div>
								</div>
							<div class="col-lg-4 ui-sortable">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>${_res.get("courselib.courseMsg") }</h5>
										
									</div>
									<div class="ibox-content">
										<ul>
											<li>
											<p>
													<span>${_res.get("teacher") }：</span> <span>${record.teachername}</span>
												</p>
												<p>
													<span>${_res.get("course.course") }：</span> <span>${record.coursename}</span>
												</p>
												<p>
													<span>${_res.get("course.class.date") }：</span> <span><fmt:formatDate value="${record.course_time}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></span>
												</p>
												<p>
													<span>${_res.get("system.time") }：</span> <span>${record.rankname}</span>
												</p>
												<p>
													<span>${_res.get("courselib.location") }：</span> <span>${record.campusname}-${record.roomname}</span>
												</p> 
												<input type="hidden" id="courseTime" value="${record.coursetime}"> 
												<input type="hidden" id="teaId" value="${record.teatcherId}">
												<input type="hidden" id="TIMENAME" value="${record.TIMENAME}"> 
												<input type="hidden" id="stuId" value="${record.studentId}">
												<input type="hidden" id="signstate" value="${record.SIGNIN}">
											</li>
										</ul>
									</div>
								</div>
							</div>

							<div class="col-lg-4 ui-sortable" style="padding-right:0">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>${_res.get("courselib.studentMsg") }</h5>
									</div>
									<div class="ibox-content">
										<div><a href="javascript: void(0)" style="width:150px;" onclick="callName()" class="btn btn-outline btn-primary">${_res.get("courselib.name") }</a></div>
										<c:if test="${!empty class_num}">
											<li>${_res.get("course.classes") }：${class_num }</li>
										</c:if>
											<c:forEach items="${stu }" var="stu" varStatus="s">
													<div style="display: block; margin-bottom: 10px; float: left;">
														<a href="javascript: void(0)" onclick="detail(${stu.Id })"> 
														<c:choose>
															<c:when test="${record.STUDENTNAME eq stu.REAL_NAME}">
																<span style="color: blue;">${stu.REAL_NAME }</span>
															</c:when>
														<c:otherwise>
															${stu.REAL_NAME }
														</c:otherwise>
														</c:choose>
														</a>|
													</div>
												</c:forEach>
											<!-- <li id="grade1"></li>
											<li id="grade2"></li>
											<li id="remark"></li> -->
										<div style="text-align: right; margin-top: 48px;">
										<%-- <c:if test="${operator_session.qx_accountqueryStudentInfo }">
											<a style="margin-down: 10px;" href="javascript: void(0)" class="dengemail" onclick="detail()">${_res.get("courselib.queryDetail") }</a>
										</c:if>
										<c:if test="${operator_session.qx_accountqueryStudentCourseInfo }">
											<a style="margin-down: 10px;" href="javascript: void(0)" class="dengemail" onclick="studentCoursePlanDetail()">${_res.get("courselib.studentrecord") }</a>
										</c:if> --%>
										点击姓名查看学生信息和课程记录
										</div>
									</div>
								</div>
							</div>


						<div class="col-lg-12 ui-sortable" style="padding-left:0;padding-right:0">
							<div class="zsd-cont-r">
								<!-- <fieldset id="searchTable" style="width: auto;"> -->
								<div class="ibox-content" id="jiaowu_pingjia" style="margin-bottom: 20px">
									<form action="" method="post">
										<input type="hidden" id="stuNum" value="${stuNum}" />
										<input type="hidden" id="courseplanid" value="${courseplan_id}" name="courseplanid"/>
											<span id="returnMsg" style="color: red; font-size: 20px;">消息提示：${message }</span>
											<h5>
												<font>${_res.get("courselib.teacher.evaluation") }(${_res.get("courselib.comment.remark") })</font>
											</h5>
											<hr>
											<div style="text-align:center;" >
												<h5>请选择评价的学生：</h5>
												<c:forEach items="${stu }" var="stu" varStatus="stuCount">
														<input type="button" id="ca_${stu.studentid}" onclick="replaceMessage(${stu.studentid})" class="btn btn-outline btn-primary"  value="${stu.REAL_NAME }">
												</c:forEach>
												<input type="hidden" value="${stu[0].studentid}" id="replaceid" >
											</div>
											<hr>
											<ol class="teacher-comment-sele" style="padding: 0">
													<li>
														<div class="col-lg-3" style="margin: 10px 0;">
															<span class="kechengjilu">${_res.get("attention")}:</span> 
															<select  id="zhuyili">
																<option ${tg.ATTENTION=='优'?"selected='selected'":""}  value="优">--${_res.get("excellent")}--</option>
																<option ${tg.ATTENTION=='良'?"selected='selected'":""} value="良">--${_res.get("good")}--</option>
																<option ${tg.ATTENTION=='中'?"selected='selected'":""} value="中">--${_res.get("average")}--</option>
																<option ${tg.ATTENTION=='差'?"selected='selected'":""} value="差">--${_res.get("poor")}--</option>
															</select>
														</div>
														<div class="col-lg-3" style="margin: 10px 0;">
															<span class="kechengjilu">${_res.get("understanding")}:</span> 
															<select name="lijieli1" id="lijieli">
																<option ${tg.UNDERSTAND=='优'?"selected='selected'":""} value="优">--${_res.get("excellent")}--</option>
																<option ${tg.UNDERSTAND=='良'?"selected='selected'":""} value="良">--${_res.get("good")}--</option>
																<option ${tg.UNDERSTAND=='中'?"selected='selected'":""} value="中">--${_res.get("average")}--</option>
																<option ${tg.UNDERSTAND=='差'?"selected='selected'":""} value="差">--${_res.get("poor")}--</option>
															</select>
														</div>
														<div class="col-lg-3" style="margin: 10px 0;">
															<span class="kechengjilu">${_res.get("study.attitude")}:</span> 
															<select name="xuxitaidu1" id="xuxitaidu">
																<option ${tg.STUDYMANNER=='优'?"selected='selected'":""} value="优">--${_res.get("excellent")}--</option>
																<option ${tg.STUDYMANNER=='良'?"selected='selected'":""} value="良">--${_res.get("good")}--</option>
																<option ${tg.STUDYMANNER=='中'?"selected='selected'":""} value="中">--${_res.get("average")}--</option>
																<option ${tg.STUDYMANNER=='差'?"selected='selected'":""} value="差">--${_res.get("poor")}--</option>
															</select>
														</div>
														<div class="col-lg-3" style="margin: 10px 0;">
															<span class="kechengjilu">${_res.get("last.homework")}:</span> 
															<select name="shangcizuoye1" id="shangcizuoye">
																<option ${tg.STUDYTASK=='未布置'?"selected='selected'":""} value="未布置">--${_res.get("Not_furnished")}--</option>
																<option ${tg.STUDYTASK=='完成较好'?"selected='selected'":""} value="完成较好">--${_res.get("Well_completion")}--</option>
																<option ${tg.STUDYTASK=='完成较差'?"selected='selected'":""} value="完成较差">--${_res.get("Completion_poor")}--</option>
																<option ${tg.STUDYTASK=='未完成'?"selected='selected'":""} value="未完成">--${_res.get("incomplete")}--</option>
															</select>
														</div>
														<div style="clear: both;"></div>
													</li>
											</ol>
											<hr>
												<li><span>${_res.get('The.course')}:</span> 
												<textarea id="bencikecheng" name="teachergrade.course_details" style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 100%;" rows="5">${tg.COURSE_DETAILS}</textarea></li>
												<li><span>${_res.get('This.job')}:</span> 
												<textarea id="bencizuoye" name="teachergrade.homework"
														style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 100%;" rows="5">${tg.HOMEWORK}</textarea></li>
												<li><span>${_res.get("qs.in.homework")}:</span> 
												<textarea id="question" name="teachergrade.question"
														style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 100%;" rows="5">${tg.question}</textarea></li>
												<li><span>${_res.get("solutions")}:</span> 
												<textarea id="method" name="teachergrade.method"
														style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 100%;" rows="5">${tg.method}</textarea></li>
												<li><span>${_res.get("course.remarks")}/${_res.get("reminder")}:</span> 
												<textarea id="tutormsg" name="teachergrade.tutormsg"
														style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 100%;" rows="5">${tg.tutormsg}</textarea></li>
									</form>
									<c:if test="${operator_session.qx_teachergradesaveTeachergrade }">
										<div align="center" class="form-group">
											<input class="btn btn-outline btn-primary" type="button" onclick="saveTeachergrade()" value="${_res.get('admin.common.submit') }">&nbsp;&nbsp; 
										</div>
										<div style="clear: both;"></div>
									</c:if>
								</div>
								<!-- </fieldset> -->
							</div>
						</div>
						<div style="clear: both;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
<script language="javascript" type="text/javascript">
	//选择学生
	function replaceMessage(studentid){
		var replaceid = $("#replaceid").val();
		$("#ca_"+replaceid).addClass("btn-outline");
		$("#ca_"+studentid).removeClass("btn-outline");
		$("#replaceid").val(studentid);
		var courseplanid = $("#courseplanid").val();
	
		$.ajax({
			url:'/knowledge/getStudentMessage',
			type:'post',
			data:{
				'studentid':studentid,'courseplanid':courseplanid
			},
			dataType:'json',
			success:function(data){
				if(data.code==1){
					$("#zhuyili").val(data.tg.ATTENTION==""?"优":data.tg.ATTENTION);
					$("#zhuyili").trigger("chosen:updated");
					$("#lijieli").val(data.tg.UNDERSTAND==""?"优":data.tg.UNDERSTAND);
					$("#lijieli").trigger("chosen:updated");
					$("#xuxitaidu").val(data.tg.STUDYMANNER==""?"优":data.tg.STUDYMANNER);
					$("#xuxitaidu").trigger("chosen:updated");
					$("#shangcizuoye").val(data.tg.STUDYTASK==""?"未布置":data.tg.STUDYTASK);
					$("#shangcizuoye").trigger("chosen:updated");
					$("#bencikecheng").val(data.tg.COURSE_DETAILS);
					$("#bencizuoye").val(data.tg.HOMEWORK);
					$("#question").val(data.tg.QUESTION);
					$("#method").val(data.tg.METHOD);
					$("#tutormsg").val(data.tg.TUTORMSG);
				}else{
					$("#zhuyili").val('优');
					$("#zhuyili").trigger("chosen:updated");
					$("#lijieli").val('优');
					$("#lijieli").trigger("chosen:updated");
					$("#xuxitaidu").val('优');
					$("#xuxitaidu").trigger("chosen:updated");
					$("#shangcizuoye").val('未布置');
					$("#shangcizuoye").trigger("chosen:updated");
				
					$("#question").val("");
					$("#method").val("");
					$("#tutormsg").val("");
				}
			}
		})
	}
	replaceMessage('${stu[0].studentid}');
	//签到方法
	function signin() {
		var TIMENAME = $("#TIMENAME").val();
		var courseplan_id = $("#courseplan_id").val();
		var courseTime = $("#courseTime").val();
		$.ajax({
			url : "/course/siGninCourese?courseplan_id=" + courseplan_id + "&&TIMENAME=" + TIMENAME + "&&courseTime=" + courseTime,
			dataType : "json",
			type : "post",
			success : function(result) {
				if (result.message == "no") {
					alert('${_res.get("educational_manage_1") }');
				} else if (result.message == "notearly") {
					alert('${_res.get("educational_manage_2") }');
				} else if (result.message == "NotDay") {
					alert('${_res.get("educational_manage_3") }');
				} else if (result.message == "short") {
					alert('${_res.get("educational_manage_4") }');
				} else if (result.message == "campusfullno") {
					alert('${_res.get("educational_manage_5") }');
				} else if (result.message == "campuspartno") {
					alert('${_res.get("educational_manage_6") }');
				} else if (result.message == "ok") {
					//在这加签到效果
					$("#sign").html("签到成功");
					$.ajax({
						type : "post",
						url : "/course/getSignMessage?studentId=" + $("#studentId").val() + "&&courseplan_id=" + $("#courseplan_id").val(),
						dataType : "json",
						success : function(value) {
							$("#course").html(value.record4);
							$("#coursePlan").html(value.record1.COURSENUM);
							$("#signCourse").html(value.record5);
							$("#noSignCourse").html(value.record3);
							$("#happen").html(value.record6.HAPPEN);
							$("#late").html(value.record5);

						},
						error : function(error) {
							alert(error);
						}
					});
				} else if (result.message == "not") {

				} else if (result.message.late != "") {
					$("#sign").html("${_res.get('courselib.late')}" + result.message.late + "分钟");
					$.ajax({
						type : "post",
						url : "/course/getSignMessage?studentId=" + $("#studentId").val() + "&&courseplan_id=" + $("#courseplan_id").val(),
						dataType : "json",
						success : function(value) {
							$("#course").html(value.record4);
							$("#coursePlan").html(value.record1.COURSENUM);
							$("#signCourse").html(value.record5);
							$("#noSignCourse").html(value.record3);
							$("#happen").html(value.record6.HAPPEN);
							$("#late").html(value.record5);
						},
						error : function(error) {
							alert(error);
						}
					});
				}
			},
			error : function(error) {
				alert("fault");
			}
		})
	}
	//点名方法
	 function callName(){
		var TIMENAME = $("#TIMENAME").val();
		var courseplan_id = $("#courseplan_id").val();
		var courseTime = $("#courseTime").val();
		var courseid = $("#course_id").val();
		var campusid = $("#campusId").val();
		var courseDate =$("#courseDate").val(); 
		var rankTime =$("#rankTime").val(); 
		var teaId =$("#teaId").val(); 
		$.ajax({
			url : "/course/siGninCourese?courseplan_id=" + courseplan_id + "&&TIMENAME=" + TIMENAME + "&&courseTime=" + courseTime,
			dataType : "json",
			type : "post",
			success : function(result) {
				if (result.message == "no") {
					alert("非校区内不支持此功能");
					return;
				} /* else if (result.message == "NotDay") {
					alert("只能记录当天的点名情况");
					return ;
				} */ else{
					$.layer({
				   	    type: 2,
				   	    shadeClose: true,
				   	    title: "${_res.get('student_attendence') }",
				   	    closeBtn: [0, true],
				   	    shade: [0.5, '#000'],
				   	    border: [0],
				   	    area: ['600px', '200px'],
				   	    iframe: {src: "/course/callNameMessage?coursePlanId="+courseplan_id+"&refresh=false"}
				   	}); 
				}
			}
		});
		
	}
	//提交老师对学生的评价
	function saveTeachergrade() {
		var studentid = $("#replaceid").val();
		var courseplanid = $("#courseplanid").val();
		var zhuyili =  $("#zhuyili").val();
		var lijieli =  $("#lijieli").val();
		var xuxitaidu =  $("#xuxitaidu").val();
		var shangcizuoye =  $("#shangcizuoye").val();
		var bencikecheng =  $("#bencikecheng").val();
		var bencizuoye =  $("#bencizuoye").val();
		var question =  $("#question").val();
		var method =  $("#method").val();
		var tutormsg =  $("#tutormsg").val();
			$.ajax({
				url:'/teachergrade/saveTeachergrade',
				type:'post',
				data:{
					'studentid':studentid,'courseplanid':courseplanid,
					'zhuyili':zhuyili,'lijieli':lijieli,
					'xuxitaidu':xuxitaidu,'shangcizuoye':shangcizuoye,
					'bencikecheng':bencikecheng,'bencizuoye':bencizuoye,
					'question':question,'method':method,'tutormsg':tutormsg
				},
				dataType:'json',
				success:function(data){
					layer.msg(data.message,1,2);
					setTimeout("window.reload.location();",1000);
				}
			})
	}
	$(function() {
		var signin = $("#signstate").val();
 		if (signin == 1) {
			$("#sign").html('${_res.get("courselib.Signin")}');
		} else if (signin == 0) {
			$("#sign").html('${_res.get("courselib.sign")}');
		} else if (signin == 2) {
			$("#sign").html('${_res.get("courselib.late")}');
		}else{
			$("#sign").html('${_res.get("Has.retroactive")}${_res.get("admin.sysLog.property.status.success")}');
		} 
	});


	function detail(studentId) {
	 	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "详情",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    offset:['100px', ''],
    	    area: ['850px', '550px'],
    	    iframe: {src: '${cxt}/account/queryStudentInfo/'+studentId}
    	});

	}
	
	function studentCoursePlanDetail(){
		var studentid = $("#stuId").val();
   		$.layer({
   			type: 2,
		    shadeClose: true,
		    title: "${_res.get('courses_record')}",
		    closeBtn: [0, true],
		    shade: [0.5, '#000'],
		    border: [0],
		    offset:['50px', ''],
		    area: ['800px', '500px'],
       	    iframe: {src: "/account/queryStudentCourseInfo/" + studentid}
       	});
    }
</script>

<script src="/js/js/top-nav/top-nav.js"></script>
<script src="/js/js/top-nav/teach.js"></script>
<script>
	$('li[ID=nav-nav4]').removeAttr('').attr('class', 'active');
</script>
</html>