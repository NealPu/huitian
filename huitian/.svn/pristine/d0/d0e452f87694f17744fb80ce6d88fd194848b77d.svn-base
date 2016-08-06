<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<style type="text/css">
	.querydiv{float:left; height:35px;  line-height:35px;margin-bottom: 20px;margin-right: 20px;}
</style>
<!-- jquery-2.1.1.min -->
<script type="text/javascript" src="/js/js/jquery-2.1.1.min.js"></script>
<!-- splitPageHtml -->
<script src="/js/common.js"></script>
<!-- Chosen -->
<script type="text/javascript" src="/js/js/plugins/chosen/chosen.jquery.js"></script>
<!-- layer javascript -->
<script type="text/javascript" src="/js/js/plugins/layer/layer.min.js"></script>
<!-- Mainly scripts -->
<script type="text/javascript" src="/js/js/bootstrap.min.js?v=1.7"></script>
<script type="text/javascript" src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Custom and plugin javascript -->
<script type="text/javascript" src="/js/js/hplus.js?v=1.7"></script>
<!-- 日期控件 -->
<script type="text/javascript" src="/js/js/plugins/layer/laydate/laydate.js"></script>
<!-- 常用工具包  -->
<script src="/js/utils.js"></script>
<!-- 级联 操作  -->
<script type="text/javascript" src="/js/cascade.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	<!--  //载入layer拓展模块 -->
	layer.use('extend/layer.ext.js');
	
	$(".chosen-select").chosen({
		disable_search_threshold : 20
	});
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
	
});
</script>