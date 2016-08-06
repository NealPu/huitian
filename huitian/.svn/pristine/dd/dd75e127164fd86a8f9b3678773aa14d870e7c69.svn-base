<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>来源明细</title>
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
  <div class="paddall">   
	<table class="table table-hover table-bordered">
	 <thead>
		<tr>
			<th>${_res.get("index")}</th>
			<th>${_res.get('sysname')}</th>
			<th>XXX</th>
			<th>XXX</th>
			<th>XXX</th>
			<th>XXX</th>
			<th>XXX</th>
		</tr>
	  </thead>
	  <tbody>
		<tr>
			<td>1</td>
			<td>XXX</td>
			<td>XXX</td>
			<td>XXX</td>
			<td>XXX</td>
			<td>XXX</td>
			<td>XXX</td>
		</tr>
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