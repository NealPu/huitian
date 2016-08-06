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
<link rel="stylesheet" type="text/css" href="/simditor/styles/simditor.css" />
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
 h5,h4{
  font-weight: 600
 }
 .basicme{
  background-color:#F3F3F4;
  padding:2px 5px 1px 10px
 }
 .basicme-cotent{
  padding:16px
 }
 .basicme-textarea{
  padding:16px 0
 }
 .bascotent{
  margin-bottom:10px
 }
 label{
  width:100px;
  font-weight: 100
 }
 .basictop{
  margin-top: 10px
 }
 textarea{
  border-radius: 6px;
  border: 1px solid #e2e2e2;
  padding: 3px;
  width: 100%
 }
 .marginlt{
  margin-left: -40px
 }
 .laydate_body .laydate_bottom{
  height:30px !important
 }
 .laydate_body .laydate_top{
  padding:0 !important
 }
 input[type="text"]{
  height:30px
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
       <form action="" method="post" id="gradeUpdateForm">
			<div class="col-lg-12" style="min-width:680px">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
					    <div>
					    	<h4 style="float: left;">${_res.get("progress_weekly_report")}</h4>
					    	<a class="btn btn-outline btn-primary btn-xs" style="float: right" href="javascript:void(0)" onclick="window.history.go(-1)">返回</a>
						    <div style="clear: both;"></div>
						</div>
					
						<div class="basicme">
							<h5>基本信息</h5>
						</div>
						<div class="basicme-cotent">
							<input type="hidden" id="teachergradeid" name="gradeUpdate.id" value="${pointgrade.id }" >
							<input type="hidden" id="studentid" name="studentid" value="${baseMsg.studentid }" >
							<input type="hidden" id="teacherid" name="teacherid" value="${baseMsg.teacherid  }" >
							<input type="hidden" id="pointid" name="gradeUpdate.pointid" value="${baseMsg.id }" >
							<input type="hidden" id="type" name="type" value="${type}" >
						
							<div class="bascotent">	
								<label>${_res.get("student.name")}:</label>
								<input type="text" readonly="readonly" name="studentname" value="${baseMsg.studentname }" >&nbsp;&nbsp;&nbsp;
								<label>${_res.get('teacher')}:</label>
								<input type="text" readonly="readonly" name="teachername" value="${baseMsg.teachername }" >&nbsp;&nbsp;&nbsp;
								<label>${_res.get('current_school')}:</label> 
								<input type="text" readonly="readonly" name="school" value="${baseMsg.SCHOOL }" >
							</div>
							<div class="bascotent">	
								<label>${_res.get('Grade')}:</label>
								<input type="text" readonly="readonly"  value="${baseMsg.GRADE_NAME }" >&nbsp;&nbsp;&nbsp;
								<label>${_res.get('appointment.time')}:</label>
								<input type="text" readonly="readonly" value="${baseMsg.appointment }" >&nbsp;&nbsp;&nbsp;
								<label>${_res.get('course.course')} :</label>
								<select id="courseid" name="gradeUpdate.courseid"  class="chosen-select" style="width:185px">
									<c:forEach items="${courseList }" var="course" >
										<c:if test="${pointgrade.courseid == course.id }">
											<option id="course_${course.id }" value="${course.id}" selected="selected">${course.COURSE_NAME }</option>
										</c:if>
											<option id="course_${course.id }" value="${course.id}">${course.COURSE_NAME }</option>
										</c:forEach>
								</select>
							</div>	
						</div>
						<h3>已完成的内容:</h3>
						<div>
							<c:if test="${type==1||type==2}">
								<div class="basicme">
									<h5>概要综述（Structure）《${type==1?"翻译版":"原版"}》</h5>
								</div>
								<div class="basicme-textarea" style="background: #D2D2D2;">
									<ul class="marginlt">
										<li> 
											${type==1?pointgrade.teacherfeedback:another.teacherfeedback}
										</li>
									</ul>
								</div>
							</c:if>
						   <div class="basicme">
								<h5>概要综述（Structure）</h5>
							</div>
							<div class="basicme-textarea">
								<ul class="marginlt">
									<li> 
										<textarea id="tutormsg" name="gradeUpdate.teacherfeedback" >
											${type==2?pointgrade.teacherfeedback:another.teacherfeedback}
										</textarea>
									</li>
								</ul>
							</div>
						</div>
						
						<div>
							<c:if test="${type==1||type==2}">
								<div class="basicme">
									<h5>学术方面（Academic Content）《${type==1?"翻译版":"原版"}》</h5>
								</div>
								<div class="basicme-textarea" style="background: #D2D2D2;">
									<ul class="marginlt">
										<li> 
											${type==1?pointgrade.question:another.question}
										</li>
									</ul>
								</div>
							</c:if>
						   <div class="basicme">
								<h5>学术方面（Academic Content）</h5>
							</div>
							<div class="basicme-textarea">
								<ul class="marginlt">
									<li>
										<textarea id="question" name="gradeUpdate.question" rows="5">
											${type==2?pointgrade.question:another.question}
										</textarea>
									</li>
								</ul>
							</div>
						</div>
						
						<div>
							<c:if test="${type==1||type==2}">
								<div class="basicme">
									<h5>学习习惯（Study Habits）《${type==1?"翻译版":"原版"}》</h5>
								</div>
								<div class="basicme-textarea" style="background: #D2D2D2;">
									<ul class="marginlt">
										<li> 
											${type==1?pointgrade.method:another.method}
										</li>
									</ul>
								</div>
							</c:if>
						  <div class="basicme">
							  <h5>学习习惯（Study Habits）</h5>
						  </div>
						  <div class="basicme-textarea">
							<ul class="marginlt">
								<li>
									<textarea id="method" name="gradeUpdate.method" rows="5">
										${type==2?pointgrade.method:another.method}
									</textarea>
								</li>
									
							</ul>
						 </div>
						</div>
						<div>
							<c:if test="${type==1||type==2}">
								<div class="basicme">
									<h5>行为表现（Behavio）《${type==1?"翻译版":"原版"}》</h5>
								</div>
								<div class="basicme-textarea" style="background: #D2D2D2;">
									<ul class="marginlt">
										<li> 
											${type==1?pointgrade.homework:another.homework}
										</li>
									</ul>
								</div>
							</c:if>
							  <div class="basicme">
								  <h5>行为表现（Behavio）</h5>
							  </div>
							  <div class="basicme-textarea">
								<ul class="marginlt">
									<li>
										<textarea id="homework" name="gradeUpdate.homework" rows="5">
											${type==2?pointgrade.homework:another.homework}
										</textarea>
									</li>
										
								</ul>
							 </div>
						</div>
						<c:if test="${code==0}">
							<div id="addnext" >
								<input type="button"  onclick="addnextMessage()" class="btn btn-outline btn-primary " value="接下来计划">
							</div>
							<div id="deletenext" style="display: none">
								<input type="button"  onclick="delnextMessage()" class="btn btn-outline btn-primary " value="取消接下来计划">
							</div>
						</c:if>	
					<div id="showmessage" style="display: none">
						<h3>接下来的计划:</h3>
						<input type="hidden"  name="nextid" value="${nexttg.id }" >
						<label>预约时间：</label>
						<input type="text" id="begincoursetime" readonly="readonly" value="${nexttg.appointment}" name="nextpoint" >
						<br><br>
						<div>
						   <div class="basicme">
								<h5>概要综述（Structure）</h5>
							</div>
							<div class="basicme-textarea">
								<ul class="marginlt">
									<li> 
										<textarea id="nexttutormsg" name="teacherfeedback" rows="5">${nexttg.teacherfeedback }</textarea>
									</li>
								</ul>
							</div>
						</div>
						
						<div>
						   <div class="basicme">
								<h5>学术方面（Academic Content）</h5>
							</div>
							<div class="basicme-textarea">
								<ul class="marginlt">
									<li>
										<textarea id="nextquestion" name="question" rows="5">${nexttg.question }</textarea>
									</li>
								</ul>
							</div>
						</div>
						
						<div>
						  <div class="basicme">
							  <h5>学习习惯（Study Habits）</h5>
						  </div>
						  <div class="basicme-textarea">
							<ul class="marginlt">
								<li>
									<textarea id="nextmethod" name="method" rows="5">${nexttg.method }</textarea>
								</li>
									
							</ul>
						 </div>
						</div>
						<div>
						  <div class="basicme">
							  <h5>行为表现（Behavio）</h5>
						  </div>
						  <div class="basicme-textarea">
							<ul class="marginlt">
								<li>
									<textarea id="nexthomework" name="homework" rows="5">${nexttg.homework }</textarea>
								</li>
									
							</ul>
						 </div>
						</div>
				</div>		
						<input type="hidden" id="nextmessage" name="nextmessage" value=""> 
						<div align="center" class="form-group">
							<c:choose>
								<c:when test="${pointgrade.id>=0 }">
									<input class="btn btn-outline btn-primary" type="button" onclick="udpateReport()"  value="${_res.get('update')}">&nbsp;&nbsp; 
								</c:when>
								<c:otherwise>
									<input class="btn btn-outline btn-primary " type="button" onclick="saveReport()"  value="${_res.get('admin.common.submit') }">&nbsp;&nbsp; 
								</c:otherwise>
							</c:choose>
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
    <script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		layer.use('extend/layer.ext.js'); //载入layer拓展模块
	</script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script type="text/javascript" src="/simditor/scripts/module.js"></script>
	<script type="text/javascript" src="/simditor/scripts/hotkeys.js"></script>
	<script type="text/javascript" src="/simditor/scripts/uploader.js"></script>
	<script type="text/javascript" src="/simditor/scripts/simditor.js"></script>
	<script>
        $(document).ready(function(){
        			var editor=new Simditor({textarea:$("#tutormsg")});
        			var editor=new Simditor({textarea:$("#question")});
        			var editor=new Simditor({textarea:$("#method")});
        			var editor=new Simditor({textarea:$("#homework")});
        			var editor=new Simditor({textarea:$("#nexttutormsg")});
        			var editor=new Simditor({textarea:$("#nextquestion")});
        			var editor=new Simditor({textarea:$("#nextmethod")});
        			var editor=new Simditor({textarea:$("#nexthomework")});
        		});
        
	</script>
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
    <script>
   	var begincoursetime = {
			elem : '#begincoursetime',
			format : 'YYYY-MM-DD',
			istime : false,
			istoday : false,
			choose : function(datas) {
				finishcoursetime.min = datas; 
			}
		};
   	var finishcoursetime = {
			elem : '#finishcoursetime',
			format : 'YYYY-MM-DD',
			istime : false,
			istoday : false,
			choose : function(datas) {
				begincoursetime.max = datas; 
			}
		};
	laydate(begincoursetime);
	laydate(finishcoursetime);
	function saveReport(){
		var nextmessage = $("#nextmessage").val();
		if(nextmessage==1){
			var nextpoint = $("#begincoursetime").val();
			if(nextpoint==""){
				layer.msg("请选择下次周报时间",1,2);
				return false;
			}
		}
		$("#gradeUpdateForm").attr("action","/report/submitReportDetail");
		$("#gradeUpdateForm").submit(); 
	}
	function udpateReport(){
		$("#gradeUpdateForm").attr("action","/report/updateReportDetail");
		$("#gradeUpdateForm").submit();
	}
	function addnextMessage(){
		$("#nextmessage").val(1);
		document.getElementById("showmessage").style.display="";
		document.getElementById("addnext").style.display="none";
		document.getElementById("deletenext").style.display="";
	}
	function delnextMessage(){
		$("#nextmessage").val("");
		document.getElementById("showmessage").style.display="none";
		document.getElementById("addnext").style.display="";
		document.getElementById("deletenext").style.display="none";
	}
    </script>
</body>
</html>