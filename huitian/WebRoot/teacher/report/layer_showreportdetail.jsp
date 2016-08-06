<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
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
 h5,h4{
  font-weight: 600
 }
 .basicme{
  background-color:#F3F3F4;
  padding:3px 5px 1px 5px
 }
 .basicme-cotent{
  padding:16px
 }
 .bascotent{
  margin-bottom:5px
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
 label{
  width:90px
 }
 .marginlt{
  margin-left: -40px
 }
</style>
</head>
<body id="body">   
	<div class="margin-nav1">
		<div class="col-lg-12" style="min-width:680px">
		 <div class="ibox float-e-margins">
			<div class="col-lg-12" style="min-width:680px">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
					    <div>
					    	<h4>学生进度报告</h4>
						</div>
						<div class="basicme">
							<h5>基本信息</h5>
						</div>
						<div class="basicme-cotent">
						   <div class="bascotent">
								<label>${_res.get("student.name")}:</label>
									<input type="text" readonly="readonly" name="studentname" value="${record.studentname }" >&nbsp;&nbsp;
								<label>${_res.get('teacher')}:</label>
									<input type="text" readonly="readonly" name="teachername" value="${record.teachername }" >&nbsp;&nbsp;
								<label>就读学校:</label> 
									<input type="text" readonly="readonly" name="school" value="${record.SCHOOL }" >&nbsp;&nbsp;
						   </div>
						   <div class="bascotent">
								<label>年级:</label>
									<input type="text" readonly="readonly"  value="${record.GRADE_NAME }" >&nbsp;&nbsp;
								<label>${_res.get('submission.time')}:</label>
									<input type="text" readonly="readonly" value="${record.submissiontime }" >&nbsp;&nbsp;
								<label>${_res.get('course.course')}:</label>
									<input type="text" readonly="readonly" value="${record.course_name }" >&nbsp;&nbsp;
						    </div>
						</div>
						
					<c:if test="${record.courseplan_id eq '0' }">
						<div>
							<div class="basicme">
								<h5>教学计划</h5>
							</div>
							<ul class="basicme-cotent">
								<li>
								  <div class="bascotent">
								             已授课程内容:
								     <input id="begincoursetime" value="${record.lastcoursebegindate }" readonly="readonly" name="teachergrade.lastcoursebegindate" > ${_res.get('to')} <input id="finishcoursetime" readonly="readonly" value="${record.lastcourseenddate }"  name="teachergrade.lastcourseenddate">
								  </div>
								</li>
								<div style="clear: both;"></div>   
								<li style="float: left;margin-left:78px">
								  <div>
								    <textarea id="bencikecheng1" name="teachergrade.lastcoursedetail" style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 340px;" rows="5">${record.lastcoursedetail }</textarea>
								  </div>
								</li>
								<li style="float: left;margin-left:10px">
								    <span>待授课程内容:</span> 
									<textarea id="bencizuoye1" name="teachergrade.COURSE_DETAILS" style="background: #f2f2f2; border-radius: 6px; border: 1px solid #e2e2e2; padding: 3px; width: 340px;" rows="5">${record.COURSE_DETAILS }</textarea>
								</li>
								<div style="clear: both;"></div>
							</ul>
						</div>
					</c:if>
					
					<div>
					    <div class="basicme">
						   <h5>${_res.get('Academic.Performance')}</h5>
						</div>
						<div>
							<ul>
								<li>
									<div class="col-lg-3" style="margin: 10px 0; float:left;">
										<span class="kechengjilu">${_res.get("attention")}:</span> <select name="teachergrade.ATTENTION" id="zhuyili1" class="chosen-select" style="width:70px">
											<option  selected="selected" >${record.ATTENTION }</option>
										</select>
									</div>
									<div class="col-lg-3" style="margin: 10px 0; float:left;">
										<span class="kechengjilu">${_res.get("understanding")}:</span> <select name="teachergrade.UNDERSTAND" id="lijieli1" class="chosen-select" style="width:70px">
											<option  selected="selected" >${record.UNDERSTAND }</option>
										</select>
									</div>
									<div class="col-lg-3" style="margin: 10px 0; float:left;">
										<span class="kechengjilu">${_res.get("study.attitude")}:</span> <select name="teachergrade.STUDYMANNER" id="xuxitaidu1" class="chosen-select" style="width:70px">
											<option  selected="selected" >${record.STUDYMANNER }</option>
										</select>
									</div>
									<div class="col-lg-3" style="margin: 10px 0; float:left;">
										<span class="kechengjilu">${_res.get("last.homework")}:</span> <select name="teachergrade.STUDYTASK" id="shangcizuoye1" class="chosen-select" style="width:100px">
											<option  selected="selected" >${record.STUDYTASK }</option>
										</select>
									</div>
									<div style="clear: both;"></div>
								</li>
							</ul>
					   </div>
					</div>
					
					<c:if test="${record.courseplan_id eq '0' }">
					 <div>
						<div class="basicme">
							<h5>整体评价（Teacher Feedback）</h5>
						</div>
						<div class="basictop">
							<ul class="marginlt">
								<li> 
									<textarea id="tutormsg" name="teachergrade.teacherfeedback" rows="5">${record.teacherfeedback }</textarea>
								</li>
							</ul>
						</div>
					 </div>	
					</c:if>
					
					<c:if test="${record.courseplan_id != '0' }">	
					 <div>
						<div class="basicme">
							<h5>${_res.get('The.course')}</h5>
						</div>
						<div class="basictop">
							<ul class="marginlt">
								<li>
									<textarea id="question" name="teachergrade.question" rows="5">${record.COURSE_DETAILS }</textarea>
								</li>
							</ul>
						</div>
					  </div>
					  <div>	
						<div class="basicme">
							<h5>${_res.get('This.job')} </h5>
						</div>
						<div class="basictop">
							<ul class="marginlt">
								<li>
									<textarea id="method" name="teachergrade.method" rows="5">${record.HOMEWORK }</textarea>
								</li>
									
							</ul>
						</div>
					 </div>	
				   </c:if>
					
					<div>
						<div class="basicme">
							<h5>${_res.get("qs.in.homework")}（Homework Assigned）</h5>
						</div>
						<div class="basictop">
							<ul class="marginlt">
								<li>
									<textarea id="question" name="teachergrade.question" rows="5">${record.question }</textarea>
								</li>
							</ul>
						</div>
					</div>
					
					<div>
						<div class="basicme">
							<h5>${_res.get('Suggestions.program')}（What to  Prepare for Next Session）</h5>
						</div>
						<div class="basictop">
							<ul class="marginlt">
								<li>
									<textarea id="method" name="teachergrade.method" rows="5">${record.method }</textarea>
								</li>
							</ul>
						</div>
					</div>	
						
					</div>
				</div>
			</div>
		</div>
			<div style="clear:both;"></div>
		</div>
	</div>
  
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
  
</body>
</html>