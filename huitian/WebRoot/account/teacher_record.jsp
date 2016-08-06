<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet"> 
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<!-- 表格样式 -->
<link rel="stylesheet" href="/css/css/component.css" />
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<!-- 时间控件 -->
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${res.get("course.teacher.record") }</title>
<style type="text/css">
.chosen-container-active.chosen-with-drop .chosen-single div b{
   background-position: -16px 2px !important
}
.chosen-container-single .chosen-single{
   min-height:34px !important;
   padding:6px 12px !important
}
</style>
</head>
<script language="javascript" type="text/javascript">
	function query() {
		document.Form1.action = "/course/queryTeacherCoursePlanDetail";
		document.Form1.submit();
	}
	function clearValue() {
		$("#studentName").attr("value", "");
		$("#teacherName").attr("value", "");
		$("#starttime").attr("value", "");
		$("#endtime").attr("value", "");
	}
		
</script>
<body>
<div id="wrapper" style="background: #2f4050;min-width:1100px;">
   <%@ include file="/common/left-nav.jsp"%>
    <div class="gray-bg dashbard-1" id="page-wrapper">
    <div class="row border-bottom">
     <nav class="navbar navbar-static-top fixtop" role="navigation">
        <%@ include file="/common/top-index.jsp"%>
     </nav>
  </div> 
  
  <div class="margin-nav" style="min-width:650px;">
   <div class="col-lg-12">
   <div class="ibox float-e-margins">
   <div class="ibox-title">
	   <h5>
		   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
		   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a>
		    &gt; ${_res.get("curriculum") } &gt;${_res.get("courses_record") }
	   </h5>
	   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
       <div style="clear:both"></div>
   </div>
   <div class="ibox-content">
					<div>
						<form action="/course/queryTeacherCoursePlanDetail" method="post" id="searchForm">
							<input type="hidden" id="wherepage" name="page" value="${page}"/>
							<fieldset>
							    <div>
									<label>${_res.get("teacher.name") }：</label><input type="text" id="teacherName" name="teacherName" value="${teacherName}">
								    <label>${_res.get("student.name") }：</label><input type="text" id="studentName" name="studentName" value="${studentName}">
								<label style="margin-left:20px;">${_res.get("system.campus") }：</label> <select name="campusId" id="campusId" class="chosen-select" style="width: 150px;border-color: #bababa;">
								<option value="">${_res.get("system.alloptions") }</option>
								<c:forEach items="${campus}" var="campus_entity">
									<option value="${campus_entity.id}"	${campus_entity.id eq campusId?"selected='selected'":""}>${campus_entity.campus_name}</option>
								</c:forEach>
								</select>
								</div>
								<div style="margin-top:10px;">
								    <label>${_res.get("course.record.firsttime") }：</label><input type="text" id="starttime" name="starttime" readonly="readonly" value="${starttime }" />
								    <label>${_res.get("course.record.lasttime") }：</label><input type="text" id="endtime" name="endtime" readonly="readonly" value="${endtime }" />
								&nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-outline btn-primary" id="teacher" value="${_res.get('admin.common.select') }" onclick="search()" />
								<!-- <input type="button" class="btn btn-outline btn-success" onclick="clearValue()" value="清空"> -->
							   </div>   
							</fieldset>
						</form>
					</div>
		</div>
		</div>		
     </div>
     
   <div class="col-lg-12">
	  <div class="ibox float-e-margins">
		 <div class="ibox-content">
		 <c:if test="${coursePlans.list==null }">
					</br>
					<h5>${errormsg }</h5>
				</c:if> 
				<c:if test="${coursePlans.list!=null }">
						<!-- 分页开始 -->
						<div style="margin-bottom: 10px;">
									<ul style="position: absolute;top:15px;left:430px;">
										<li id="liup" class=""><a href="#" id="upxia" onclick="goPage(${page-1})">${_res.get("course.lastPage") }</a>&nbsp;</li>
										<li id="lidown" class="">&nbsp;<a href="#" id="downxia" onclick="goPage(${page+1})">${_res.get("course.nextPage") }</a></li>
									</ul>
								<div>${_res.format("course.record.splitmsg",count,page,pages) }</div>
						</div>
						<!-- 分页结束 -->
			<div class="component">		 
		     <table  border="0" class="jwgl-tab">
		                 <thead>
							<tr align="center">
								<th rowspan="2" width="3%"><label><strong>${_res.get("index") }</strong></label></th>
								<th rowspan="2" width="7%"><label><strong>${_res.get('Study.subjects')}</strong></label></th>
								<th rowspan="2" width="5%"><label><strong>${_res.get("course.class.date") }</strong></label></th>
								<th rowspan="2" width="5%"><label><strong>${_res.get("class.time.session")}</strong></label></th>
								<th rowspan="2" width="5%"><label><strong>${_res.get("class.location")}</strong></label></th>
									<th rowspan="2" width="4%"><label><strong>${_res.get("class.consultant.in.charge")}</strong></label></th>
									<th rowspan="2" width="4%"><label><strong>${_res.get("classNum")}</strong></label></th>
									<th rowspan="2" width="4%"><label><strong>${_res.get("student.name")}</strong></label></th>
								<th rowspan="2" width="8%"><label><strong>${_res.get("learning.content")}</strong></label></th>
								<th colspan="4" width="12%"><label><strong>${_res.get("consultant's.comments")}</strong></label></th>
									<th colspan="5" width="12%"><label><strong>${_res.get("student's.comments")}</strong></label></th>
								<th rowspan="2" width="8%"><label><strong>${_res.get("homework")}</strong></label></th>
								<th rowspan="2" width="8%"><label><strong>${_res.get("qs.in.homework")}</strong></label></th>
								<th rowspan="2" width="8%"><label><strong>${_res.get("solutions")}</strong></label></th>
								<th rowspan="2" width="8%"><label><strong>${_res.get("reminder")}/${_res.get("course.remarks")}</strong></label></th>
							</tr>
							<tr>
								<th><label><strong>${_res.get("understanding")}</strong></label></th>
								<th><label><strong>${_res.get("attention")}</strong></label></th>
								<th><label><strong>${_res.get("last.homework")}</strong></label></th>
								<th><label><strong>${_res.get("study.attitude")}</strong></label></th>
									<th><label><strong>${_res.get("logic")}</strong></label></th>
									<th><label><strong>${_res.get("knowledge")}</strong></label></th>
									<th><label><strong>${_res.get("responsibility")}</strong></label></th>
									<th><label><strong>${_res.get("amiableness")}</strong></label></th>
									<th><label><strong>${_res.get("course.remarks")}</strong></label></th>
							</tr>
						</thead>
							<c:forEach items="${coursePlans.list}" var="course" varStatus="st">
								<tr>
									<td align="center">${st.index+1}</td>
									<td align="center">
									<a href="/knowledge/educationalManage?courseplanId=${course.cpid}" style="text-decoration: none;"> ${course.COURSE_NAME}</a>
									</td>
									<td align="center">${course.COURSE_TIME}</td>
									<td align="center">${course.RANK_NAME}</td>
									<td align="center">${course.CAMPUS_NAME}</td>
									<td align="center">${course.TNAME}</td>
									<td align="center">${course.CLASSNUM}</td>
									<td align="center">${course.SNAME}</td>
									<td align="left">${course.COURSE_DETAILS}</td>
									<td align="center">${course.UNDERSTAND}</td>
									<td align="center">${course.ATTENTION}</td>
									<td align="center">${course.STUDYTASK}</td>
									<td align="center">${course.STUDYMANNER}</td>
									<td align="center">${course.LJX}</td>
									<td align="center">${course.ZSD}</td>
									<td align="center">${course.ZRX}</td>
									<td align="center">${course.QHL}</td>
									<td align="center">${course.SBZ}</td>
									<td align="left">${course.HOMEWORK}</td>
									<td align="left">${course.question}</td>
									<td align="left">${course.method}</td>
									<td align="left">${course.tutormsg}</td>
								</tr>
							</c:forEach>

						</table>
						</div>
				</c:if>
				<div style="margin-top: 20px;" >
					<c:import url="/common/showPage.jsp" />
				</div> 
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
            elem: '#starttime',
            format: 'YYYY-MM-DD',
            max: '2099-06-16', //最大日期
            istime: false,
            istoday: false,
            choose: function (datas) {
            	endtime.min = datas; //开始日选好后，重置结束日的最小日期
            	endtime.start = datas //将结束日的初始值设定为开始日
            }
        };
        var endtime = {
            elem: '#endtime',
            format: 'YYYY-MM-DD',
            max: '2099-06-16',
            istime: false,
            istoday: false,
            choose: function (datas) {
            	starttime.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(starttime);
        laydate(endtime);
 </script>
  <!-- Chosen -->
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>

    <script>
    $(".chosen-select").chosen({disable_search_threshold: 25});
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
<!-- 表格 -->
<script type="text/javascript" src="/js/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/js/js/jquery.ba-throttle-debounce.min.js"></script>
<script type="text/javascript" src="/js/js/jquery.stickyheader.js"></script>  


<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
       $('li[ID=nav-nav4]').removeAttr('').attr('class','active');
    </script>

</body>
</html>