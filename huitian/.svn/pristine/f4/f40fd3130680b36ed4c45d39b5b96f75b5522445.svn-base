<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" /> 
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
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

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${_res.get("courses_for_today") }</title>
</head>
<body style="overflow-x:auto;">
<div id="wrapper" style="background: #2f4050;min-width:1100px;">
   <%@ include file="/common/left-nav.jsp"%>
   <div class="gray-bg dashbard-1" id="page-wrapper" style="">
    <div class="row border-bottom">
     <nav class="navbar navbar-static-top fixtop" role="navigation">
        <%@ include file="/common/top-index.jsp"%>
     </nav>
  </div>
  
  <div class="ibox float-e-margins margin-nav" style="margin-left:0">
     <div class="ibox-title">
		<h5>
			<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
			<a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a>
			 &gt;<a href='/course/courseSortListForMonth?loginId=${account_session.id}'>${_res.get("curriculum") }</a> &gt; ${_res.get("courses_for_today") }
		</h5>
		<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
        <div style="clear:both"></div>
	 </div>
     <div class="ibox-content">
          <form	action="/course/queryAllcoursePlans?loginId=${account_session.id}&returnType=7" id="myform" method="post">
					<input type="hidden" name="returnType" id="returnType" value="7">
					<fieldset id="searchTable">
						   <div class="form-group" style="margin-top:15px;">
								<label>${_res.get('student')}：</label> <input type="text" name="studentName" id="studentName" value="${studentName}" />
								<label>${_res.get('teacher')}：</label> <input type="text" name="teacherName" id="teacherName" value="${teacherName}" />
								<label>${_res.get('classNum')}：</label> <input type="text" name="banci" id="banci" value="${banci}" /><br>
						   </div>
						   <div class="form-group" style="margin-top:15px;">
							<label style="float: left;margin-top:8px">${_res.get("course.class.date") }：</label>
							<div style="float: left"> 
								<input type="text" class="form-control layer-date" readonly="readonly" id="date1" name="date1" value="${date1}" style="background-color:#fff;" />
							</div>
							<div style="width:30px;height:30px;line-height:30px;text-align:center;background:#E5E6E7;float: left;margin-top:1px">${_res.get('to')}</div>
							<div style="float: left"> 
								<input type="text" readonly="readonly" class="form-control layer-date" id="date2" name="date2" value="${date2}" style="background-color:#fff;" />
							</div>
							<span style="clear: both;"></span>
							<label style="margin-left:20px;">${_res.get("system.campus") }：</label> 
							<select name="campusId" id="campusId" class="chosen-select" style="width: 150px;height:34px;border-color: #bababa;">
								<option value="">${_res.get("system.alloptions") }</option>
								<c:forEach items="${campus}" var="campus_entity">
									<option value="${campus_entity.id}"	<c:if test="${campus_entity.id eq campusId}">selected="selected"</c:if>>${campus_entity.campus_name}</option>
								</c:forEach>
							</select>
						   </div>
						   <div class="form-group" style="margin-top:15px;">	
							<input type="submit" value="${_res.get('admin.common.select') }" class="btn btn-outline btn-info" />
					       </div>
					</fieldset>
				</form>
          </div>
    </div>
    
    <div>
    <div class="kebiao_today kebiao_paike" style="margin-left:0;margin-top:20px;float: left;">
	 	   <c:forEach items="${timeMap }" var="timeRankMap" >
	 	   		
			       <h4>${timeRankMap.key}
						<script type="text/javascript">
							var now=new Date(Date.parse("${timeRankMap.key}".replace(/-/g,"/"))); 
							var day=now.getDay();  
							var week;
							var arr_week=new Array("${_res.get('system.Sunday')}","${_res.get('system.Monday')}","${_res.get('system.Tuesday')}","${_res.get('system.Wednesday')}","${_res.get('system.Thursday')}","${_res.get('system.Friday')}","${_res.get('system.Saturday')}");
							document.write("("+arr_week[day]+")");
						</script>
					</h4>
					
					<c:forEach items="${timeRankMap.value}" var="timeRank">
				
					<c:if test="${timeRank.value.showcount>0}">	
					<table style="margin: 0 0 0px;width:${fn:length(timeRank.value.rooms)*100+80}px;">
					    <tr style="color:#fff;height:35px;">
					        <td style="background-color: #18a689; border-bottom: 1px solid #AAA; font-weight: bold; border: solid 2px #fff; width: 80px;"
								align="center">${_res.get("system.campus") }</td>
							<td colspan="${fn:length(timeRank.value.rooms)}" style="background-color: #18a689; border-bottom: 1px solid #AAA; font-weight: bold;font-size:16px; border: solid 2px #fff;"
								align="center">${timeRank.key }</td>	
					    </tr>
					    <tr style="height:25px;">
							<td style="background-color: #dff0d8; border-bottom: 1px solid #AAA; font-weight: bold; border: solid 2px #fff; width: 80px;"
								align="center">${_res.get("system.time")}</td>
							<c:forEach items="${timeRank.value.rooms}" var="room" varStatus="r">
								<td style="background-color: #dff0d8; border-bottom: 1px solid #AAA; font-weight: bold; border: solid 2px #fff; width: 100px;"
									align="center">${room.NAME }</td>
							</c:forEach>
						</tr>
						<c:choose>
							<c:when test="${fn:length(timeRank.value.campuslist)==timeRank.value.campusnull }">
								<tr><td colspan="${fn:length(timeRank.value.rooms)+1}" align="center" >${_res.get("course.campus.hasnt.course") }</td></tr>
							</c:when>
							<c:otherwise>  
						
						<c:forEach items="${timeRank.value.campuslist }" var="campus">
					
						<c:choose>
							<c:when test="${fn:length(campus.value.valuelist)>campus.value.isnull }">
						<tr>
							<td style="height: 70px; width: 80px; border: solid 1px #fff;background-color: #dff0d8;text-align: center;">${campus.key }</td>
							
							<c:forEach items="${campus.value.valuelist}" var="course">
								<td style="height: 70px; width: 100px; background-color: #d9edf7; border: solid 1px #fff;" align="left">
									<c:choose>
										<c:when test="${course == null }"></c:when>
											<c:otherwise>
												<c:choose>
												 	<c:when test="${course.PLAN_TYPE==2 }">
												 		${_res.get('type')}：${_res.get('Rest')}<c:if test="${course.rechargecourse }">(${_res.get('Complement.row')})</c:if><br>
												 		${_res.get('teacher')}：${course.T_NAME }<br>
												 		<c:choose>
													 		<c:when test="${course.startrest eq '00:00' && course.endrest eq '23:59' }">
													 			${_res.get('course.class.date')}：${_res.get('All.day.long')}<br>
													 		</c:when>
													 		<c:otherwise>
														 		${_res.get('course.class.date')}：${course.startrest }-${course.endrest }<br>
													 		</c:otherwise>
												 		</c:choose>
												 	</c:when>
												 	<c:otherwise>
													<div title="S:${course.S_NAME}">
														${course.teach_type}<c:if test="${course.rechargecourse }">(${_res.get('Complement.row')})</c:if>
														<c:if test="${course.iscancel==1}">(已取消)</c:if><br>
														S:${course.S_NAME}<br>
														<c:if test="${!empty course.T_NAME}">
															T:${course.T_NAME}<br>
														</c:if>
														C:${course.COURSE_NAME}<br>
														${_res.get("course.remarks")}：${course.REMARK=="暂无"?_res.get('Tsl_no'):course.REMARK}</div>
												 	</c:otherwise>
												</c:choose>
													
											</c:otherwise>
									</c:choose>
								 </td>
							</c:forEach>
							
						</tr>
							
							</c:when>
						</c:choose>
						
						</c:forEach>
						
						</c:otherwise>
					</c:choose>
					</table>
					<br>
					</c:if>
					</c:forEach>
					
					<hr>
					
			</c:forEach>		
	 </div>
	 <div style="clear: both;"></div> 	
	</div>
   </div>
</div> 
<div id="tbox">
	<a id="gotop" href="javascript:void(0)"></a>
</div>
<!-- layerDate plugin javascript -->
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
    $(".chosen-select").chosen({disable_search_threshold: 30});
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
</body>
</html>