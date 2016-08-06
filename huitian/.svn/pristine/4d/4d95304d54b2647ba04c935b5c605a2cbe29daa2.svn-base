<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>校区统计明细</title>
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
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
 tr td{
   background:#fff;
   text-align: center;
 }
 .paddall{
   padding:5px
 }
</style>
</head>
<body style="background:#fff">
  <div style="max-height: 500px" class="paddall">   
	<table class="table table-hover table-bordered">
	 <thead>
		<tr>
			<th>${_res.get("index")}</th>
			<th>${_res.get("Contacts")}</th>
			<th>性别</th>
			<th>${_res.get('Opp.Entry.Date')}</th>
			<th>电话</th>
			<th>科目</th>
			<th>就近校区</th>
			<th>${_res.get("Opp.Channel")}</th>
			<th>市场专员</th>
			<th>课程顾问</th>
			<th>${_res.get("Opp.Lead.Status")}</th>
		</tr>
	  </thead>
	  <tbody>
		<c:forEach items="${mingxi}" var="opportunity" varStatus="status">
			<tr align="center">
				<td>${status.count}</td>
				<td>${opportunity.contacter}</td>
				<td>${opportunity.sex==true?_res.get('student.boy'):_res.get('student.girl')}</td>
				<td><fmt:formatDate value="${opportunity.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
				<td>${opportunity.phonenumber}</td>
				<td>${opportunity.subjectnames}</td>
				<td>${opportunity.campusid==null?'':opportunity.campus_name}</td>
				<td>${opportunity.crmscid==1?opportunity.mediatorname:opportunity.crmscname}</td>
				<td>${opportunity.scusername==null?'':opportunity.scusername}</td>
				<td>${opportunity.kcusername==null?'':opportunity.kcusername}</td>
				<td id="isconver_${opportunity.id}">
					 <c:choose>
				        <c:when test="${opportunity.isconver eq 0 }"><label id ="z_${opportunity.id}" class="btn btn-info btn-xs">${_res.get('Opp.No.follow-up')}</label></c:when>
				        <c:when test="${opportunity.isconver eq 1 }"><label id ="z_${opportunity.id}" class="btn btn-success btn-xs">${_res.get('Is.a.single')}</label></c:when>
					    <c:when test="${opportunity.isconver eq 2 }"><label id ="z_${opportunity.id}" class="btn btn-primary btn-xs">${_res.get('Opp.Followed.up')}</label></c:when>
					    <c:when test="${opportunity.isconver eq 3 }"><label id ="z_${opportunity.id}" class="btn btn-danger btn-xs">考虑中</label></c:when>
						<c:when test="${opportunity.isconver eq 4 }"><label id ="z_${opportunity.id}" class="btn btn-default btn-xs">无意向</label></c:when>
						<c:when test="${opportunity.isconver eq 5 }"><label id ="z_${opportunity.id}" class="btn btn-default btn-xs">已放弃</label></c:when>
						<c:when test="${opportunity.isconver eq 6 }"><label id ="z_${opportunity.id}" class="btn btn-warning btn-xs">有意向</label></c:when>
					  </c:choose>
				</td>
			</tr>
		</c:forEach>
	  </tbody>	
	</table>
  </div>
    <!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script src="/js/js/demo/layer-demo.js"></script>
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
        
      //弹出后子页面大小会自动适应
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.iframeAuto(index);
    </script>	
</body>
</html>