<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>回访信息</title>
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
</style>
</head>
<body>
  <div style="padding:10px;max-height:500px;overflow-y:scroll">
    <c:if test="${code eq '1'}">
       	暂无回访记录
    </c:if>
    <c:if test="${code eq '2'}">   
	<table class="table table-bordered" style="text-align:center" >
		<tr>
        	<td class="table-bg2" width="10%">${_res.get("index")}</td>
        	<td class="table-bg2" width="14%">课程顾问</td>
        	<td class="table-bg2" width="15%">回访时间</td>
        	<!--  <td class="table-bg2" width="13%">通话状态</td>-->
        	<td class="table-bg2" width="15%">回访结果</td>
        	<td class="table-bg2" width="46%">回访内容</td>
       	</tr>
       	<c:forEach items="${fblist}" var = "result" varStatus="vs">
       		<tr>
       			<td class="table-bg2"> ${vs.count}</td>
       			<td class="table-bg2">${result.REAL_NAME}</td>
				<td class="table-bg2">${result.CREATETIME}</td>
				<!-- <td class="table-bg2">${result.CALLSTATUS eq '1'?"已接通":result.CALLSTATUS eq '2'?"未接通":result.CALLSTATUS eq '3'?"再联系":result.CALLSTATUS eq '4'?"通话中":result.CALLSTATUS eq '5'?"空号":""}</td> -->
				<td class="table-bg2">
				  ${result.CALLRESULT eq '0'?_res.get("Opp.No.follow-up"):result.CALLRESULT eq '1'?_res.get('Is.a.single'):result.CALLRESULT eq '2'?_res.get('Opp.Followed.up'):result.CALLRESULT eq '3'?'考虑中':result.CALLRESULT eq '4'?'无意向':result.CALLRESULT eq '5'?'已放弃':'有意向'}</td>
				<td class="table-bg2" style="text-align:left">${result.CONTENT}</td>
			</tr>
       	</c:forEach>
       	</c:if>
       	
	</table>
  </div>	
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