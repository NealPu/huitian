<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>${_res.get('Classroom.curriculum')}</title>
<meta name="save" content="history">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- 回到顶部 -->
<link type="text/css" href="/css/lrtk.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/js.js"></script>
<!-- table -->
<link type="text/css" href="/css/component.css" rel="stylesheet" />
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<style type="text/css">
 h5{font-weight: 100 !important}
 .classkb{background:#e7eaec;padding:5px;margin-bottom:5px}
 .school{background:#D9EDF7;color:#31708F;padding:5px;margin-bottom:10px;font-weight: 600}
 .zhi{height:30px;width:30px;line-height:30px;text-align:center;background:#EEE;position: absolute;left:406px;top:17px}
 .bgTips span em {display: inline-block;height: 12px;width: 12px;vertical-align: middle;border: 1px solid #fff;margin-right: 5px}
 .bgTips span em.BT-1 {background-color: #71b62d}
 .bgTips span em.BT-2 {background-color: #54a4ff}
 .bgTips span em.BT-3 {background-color: #c288f2}
 .bgTips span em.BT-4 {background-color: #e68a4e}
 .bgTips span em.BT-5 {background-color: #33c0aa}
 .T-1 {background-color: #71b62d;padding:10px;color:#fff;margin:10px 0;width:130px;word-wrap:break-word}
 .T-2 {background-color: #54a4ff;padding:10px;color:#fff;margin:10px 0;width:130px;word-wrap:break-word}
 .T-3 {background-color: #c288f2;padding:10px;color:#fff;margin:10px 0;width:130px;word-wrap:break-word}
 .T-4 {background-color: #e68a4e;padding:10px;color:#fff;margin:10px 0;width:130px;word-wrap:break-word}
 .T-5 {background-color: #33c0aa;padding:10px;color:#fff;margin:10px 0;width:130px;word-wrap:break-word}
 .tabtdbg td{border: 1px solid #E7E7E7 !important}
  tbody tr:nth-child(2n-1){background-color:#fff}
  .sticky-wrap{margin:10px 0 15px}
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
		<form action="/course/queryAllcoursePlansByroom" method="post" id="searchForm" >
			<div  class="col-lg-12 m-t-xzl" style="padding-left:0;width:1075px ">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
						<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
						${_res.get('curriculum')} &gt; ${_res.get('Classroom.curriculum')} 
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          			<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				    <div style="float: left;">
						<label>${_res.get('system.campus')}： </label>
						<select name="campusId" id="campusId" class="chosen-select" style="width: 130px; border-color: #bababa;">
							<option value="">${_res.get("system.alloptions") }</option>
							<c:forEach items="${campus}" var="campus_entity">
								<option value="${campus_entity.id}"	<c:if test="${campus_entity.id eq campusId}">selected="selected"</c:if>>${campus_entity.campus_name}</option>
							</c:forEach>
						</select>&nbsp;&nbsp;
					</div>	
					<div style="float: left;">
						<label style="float: left;margin-top:6px">${_res.get('course.class.date')}：</label> 
						<div style="float: left">
						    <input type="text" class="form-control layer-date" readonly="readonly" id="date1" name="date1" value="${date1}" style="width:170px; background-color: #fff;" />
						</div>
						<div style="width:30px;height:30px;line-height:30px;text-align:center;background:#E5E6E7;float: left;margin-top:1px">${_res.get('to')}</div>
						<div style="float: left">    
						    <input type="text" class="form-control layer-date" readonly="readonly" id="date2" name="date2" value="${date2}" style="width:170px; background-color: #fff;" />&nbsp;&nbsp; 
				        </div>
				    </div>
				    <input type="submit" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-info">
			   </div>
			 </div>
		   </div>

			<div style="padding-left:0;width:1060px">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get('Classroom.curriculum')}</h5>
						<div class="bgTips" style="margin-left:100px;">
							<span> <em class="BT-1"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.rest") }
								</span>
							<span> <em class="BT-2"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.netcourse") }
								</span>
							<span> <em class="BT-4"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.course") }
								</span>
							<span> <em class="BT-3"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.classes") }
								</span>
							<span> <em class="BT-5"> &nbsp; </em> ${_res.get("course.indicates") }：${_res.get("course.outcourse") }
								</span>
						</div>	
					</div>
					<div class="ibox-content">
					    <c:forEach items="${timeMap}" var="timeRank">
					<div class="school">${timeRank.key }</div>
					<div style="width:1020px;overflow-x:auto ">
					<table class="overflow-y" style="margin: 0 0 0px;width:${fn:length(timeRank.value.rooms)*140+80}px;max-width:${fn:length(timeRank.value.rooms)*140+80}px;">
					   <thead>
					    <tr style="height:25px;">
							<th style=" width: 80px;background:#DFF0D8;border-color:#bbde9d;color:#3C763D" align="center"></th>
							<c:forEach items="${timeRank.value.rooms}" var="room" varStatus="r">
								<th style="width: 140px;background:#DFF0D8;border-color:#bbde9d;color:#3C763D" align="center">${room.NAME }</th>
							</c:forEach>
						</tr>
					</thead>
						<c:choose>
							<c:when test="${fn:length(timeRank.value.campuslist)==timeRank.value.campusnull }">
								<tr><td colspan="${fn:length(timeRank.value.rooms)+1}" align="center" >${_res.get("course.campus.hasnt.course") }</td></tr>
							</c:when>
							<c:otherwise>  
						<c:forEach items="${timeRank.value.campuslist }" var="campus">
						<c:choose>
							<c:when test="${fn:length(campus.value.valuelist)>campus.value.isnull }">
						<tr class="tabtdbg">
							<td style="height: 70px;text-align: center;background:#FCF8E3;border:1px solid #f0dcb4;color:#8A6D3B">${campus.key }
							<script type="text/javascript">
								var now=new Date(Date.parse("${campus.key}".replace(/-/g,"/"))); 
								var day=now.getDay();  
								var week;
								var arr_week=new Array("${_res.get('system.Sunday')}","${_res.get('system.Monday')}",
										"${_res.get('system.Tuesday')}",
										"${_res.get('system.Wednesday')}","${_res.get('system.Thursday')}",
										"${_res.get('system.Friday')}","${_res.get('system.Saturday')}");
								document.write("("+arr_week[day]+")");
							</script>
							</td>
							<c:forEach items="${campus.value.valuelist }" var="courseList">
							
								<td style="height: 70px;vertical-align: top;" align="left">
									<c:choose>
										<c:when test="${courseList == null }"></c:when>
											<c:otherwise>
											<c:forEach items="${courseList}" var="course">
												<c:choose>
												 	<c:when test="${course.PLAN_TYPE==2 }">
												 	<div class="T-1" title="S:${course.S_NAME}">
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
												 	</div>
												 	</c:when>
												 	<c:otherwise>
												 	<c:choose>
												 		<c:when test="${course.campustype==0 }">
												 		<div class="T-5" title="S:${course.S_NAME}">
												 		</c:when>
												 		<c:when test="${course.class_id!=0 }">
												 		<div class="T-3" title="S:${course.S_NAME}">
												 		</c:when>
												 		<c:when test="${course.netCourse==1 }">
												 		<div class="T-2" title="S:${course.S_NAME}">
												 		</c:when>
												 		<c:otherwise>
												 		<div class="T-4" title="S:${course.S_NAME}">
												 		</c:otherwise>
												 	</c:choose>
												 		${course.iscancel==1?"(课程已取消)<br>":""}
														${_res.get('system.time')}：${course.RANK_NAME}<br>
														${course.teach_type}<c:if test="${course.rechargecourse }">(${_res.get('Complement.row')})</c:if><br>
														<c:if test="${course.S_NAME!=''}">
															${_res.get('student')}:${course.S_NAME}<br>
														</c:if>
														<c:if test="${!empty course.T_NAME}">
														${_res.get('teacher')}:${course.T_NAME}<br>
														</c:if>
														${_res.get('course.course')}:${course.COURSE_NAME}<br>
														${_res.get("course.remarks")}：${course.REMARK=="暂无"?_res.get('Tsl_no'):course.REMARK}</div>
												 	</c:otherwise>
												</c:choose>
											</c:forEach>	
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
					</div>
					<br>
					</c:forEach>
					</div>
				</div>
			</div>
			<div style="clear:both "></div>
		</form>
		</div>
	   </div>
	</div>
	
<div id="tbox">
	<a id="gotop" href="javascript:void(0)"></a>
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
    <script src="/js/js/top-nav/teach-1.js"></script>
    <!-- /table-top -->
	<script type="text/javascript" src="/js/table/jquery.ba-throttle-debounce.min.js"></script>
	<script type="text/javascript" src="/js/table/jquery.stickyheader.js"></script>
    <script>
         $('li[ID=nav-nav4]').removeAttr('').attr('class','active');
    </script>
</body>
</html>