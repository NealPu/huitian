<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">


<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/js/jquery-2.1.1.min.js"></script>
<script src="/js/js/plugins/layer/layer.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${_res.get("performance_measurement") }</title>
<script type="text/javascript">
//根据名字模糊查询数据库
$(function(){	
	$('#studentName').keyup(function(){		
			var studentName = $("#studentName").val().trim();
			if (studentName != "") {
			var studentName=$("#studentName").val();
				$.ajax({
					url :  "${cxt}/grade/getAccountByNameLike",
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
										$("#studentid").val(studentId);
										$("#studentName").val(realName);
										$("#mohulist").hide();
										$("#studentInfo").text("");
										return;
									}else{
										str += "<li onclick='dianstu(" + studentId+",\""+realName + "\")'>" + realName + "</li>";
									}
								}
								$("#stuList").html(str);
								$("#mohulist").show();
								$("#studentInfo").text("");
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
	
	$('#saveGrade').click(function(){
		var level = $("#level").val();
		var studentName = $("#studentName").val();
		var studentid=$("#studentid").val();
		var examdate=$("#examTime").val();
		var subjectname = $("#subjectname").val();
		
		var r=/^[0-9]*$/;
		var rs =/^[0-9]+(.[0-9]{2})?$/;
		if(examdate==""){
			layer.msg("请选择考试时间",2,5);
			return false;
		}else if(studentName.trim()==""){
			layer.msg("请填写完整的姓名",2,5);
			return false;
		}else if(level==2){
			layer.msg("请选择考试类型",2,5);
			return false;
		}else if($("input").val()==""){
			layer.msg("请填写完整数据",2,5);
			return false;
		}else{	
			 $.ajax({
				url:"/grade/getCource",
				type:"post",
				data:{"subjectname":subjectname},
				datatype:"json",
				success : function(data){
					for(i in data.listCourse){
						if($("#c_"+data.listCourse[i].ID).val().trim()==""||$("#m_"+data.listCourse[i].ID).val().trim()==""){
							layer.msg("请填写完整的数据",2,5);
							return false;
						}
					}
					if(confirm("确定提交？")){
						$("#subjectname").attr('disabled',false);
					$.ajax({
						type:"post",
						url:"/grade/saveScore",
						data:$('#courseForm').serialize(),// 你的formid
						datatype:"json",
						success : function(data) {
							if(data.code=='0'){
								layer.msg(data.msg,2,5);
							}else{
							layer.msg(data.msg,2,5);
							setTimeout(parent.window.location.reload(), 3000);
							}
						}	
					});
					}
				}
			}); 
		}
	});
	
});
//模糊查询后选择结果并显示 
function dianstu(stuId,realname){
	$("#studentid").val(stuId);
	$("#studentName").val(realname);
	$("#mohulist").hide();
}

//用户体验好对数据进行判断
function checkNumber(obj,str){
	//获取input元素的id
	var ids = $(obj).attr("id");
	//拆分获取的元素id
	var id = ids.split("_")[1];
	//获取id否value
	var  correct  = $(obj).val();
	var  wrong =$("#"+str+"_"+id).val();
	
	var r=/^[0-9]*$/;
	 if(!r.test(correct)||!r.test(wrong)){
		layer.msg("请填写正整数",2,5);
		return false;
	}
	
}
function setLowerCase(txt_Str) {
    txt_Str.toLowerCase()();
}
</script>
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
	top: 35px;
	left: 8.5em;
	width: 100px;
	overflow: hidden;
	z-index: 2012;
	background: #09f;
	border: 1px solide;
	border-color: #e2e2e2 #ccc #ccc #e2e2e2;
	padding: 6px;
}
label {
	width: 100px;
}

.tds{
	text-align : left;
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;height:100%;min-width:1000px;">
	    <%@ include file="/common/left-nav.jsp"%>
	    <div class="gray-bg dashbard-1" id="page-wrapper" style="height:100%">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top fixtop" role="navigation">
			<%@ include file="/common/top-index.jsp"%>
			</nav>
		</div>

		 <div class="margin-nav" style="min-width:720px;width:100%;margin-left:0;">
		<div class="col-lg-12" style="margin-left:-15px;">
			<div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
					  &gt;<a href='/student/index'>${_res.get('student_management') }</a> &gt;  ${_res.get("performance_measurement") }
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
					<form action="" method="post" id="courseForm">
						<input type="hidden" id="studentid" name="gradeRecord.studentid"  >
						<fieldset style="width: 100%;">
							<div>
								<label>${_res.get("grade.exam.date") }：</label>
								<input id="examTime" type="text" name="gradeRecord.examdate" readonly="readonly" value="${examTime}" size="15" />
							</div></br>
							<div class="stu_name" id="stu_name">
								<label>${_res.get("student.name")}：</label>
								<input type="text" id="studentName" name="gradeRecord.studentname"   />
								<div id="mohulist" class="student_list_wrap" style="display: none">
									<ul style="margin-bottom: 10px;" id="stuList"></ul>
								</div>
								<div style="margin:10px 0 0 100px;"><span id="studentInfo"></span></div>							
							</div>	
							<div>
							<label>&nbsp;&nbsp;${_res.get("course.subject") }：</label>
								<input type="text" id="subjectname" name="gradeRecord.subjectname" value="${subject.SUBJECT_NAME}"  disabled></br></br> 
							<label>L&nbsp;E&nbsp;V&nbsp;E&nbsp;L：</label>
								<select id="level" size="1" class="chosen-select" style="width: 150px;" tabindex="2" name="gradeRecord.leveltype">
									<option value="2" selected>${_res.get("system.alloptions")}</option>
									<option value="0">Upper Level</option>
									<option value="1">Middle Level</option>
								</select>
							</div></br></br> 			
					<div>
					<c:forEach items="${list}"  var="i" >
						<label>${i.COURSE_NAME}：</label> 
						${_res.get("grade.correct.questions") }：<input type="text" id="c_${i.ID}" name="c_${i.ID}" onchange="checkNumber(this,'m')"  style="width: 120px;">
						${_res.get("grade.error.questions") }：<input type="text" id="m_${i.ID}" name="m_${i.ID}" onchange="checkNumber(this,'c')"  style="width: 120px;"></br></br> 
					</c:forEach>
					<%-- <label>&nbsp;&nbsp;${_res.get("grade.writing") }：</label>
						${_res.get("grade.actual.score") }：<input type="text" id="xiezuo" name="xiezuo"   style="width: 120px;"> --%>
					</div>
					<div>
						<c:if test="${operator_session.qx_gradesaveScore }">
							<input id="saveGrade" type="button" value="${_res.get('teacher.group.add') }" class="btn btn-outline btn-primary" />
						</c:if>
					</div>		
						</fieldset>
					</form>
				</div>
			</div>
		</div>
		</div>
 		</div>
	</div>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
	$(".chosen-select").chosen({disable_search_threshold:30});
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

	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		layer.use('extend/layer.ext.js'); //载入layer拓展模块
	</script>
	
	<script type="text/javascript">
		
	</script>
 
 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
       $('li[ID=nav-nav1]').removeAttr('').attr('class','active');
    </script> 
    <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
	<script>
		//日期范围限制
		var examTime = {
			elem : '#examTime',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : true
		};
		laydate(examTime);
	</script>
 
</body>
</html>