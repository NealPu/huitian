<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<!-- Mainly scripts -->

	<script src="/js/js/bootstrap.min.js?v=3.3.0"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Flot -->
	<script src="/js/js/plugins/flot/jquery.flot.js"></script>
	<script src="/js/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
	<script src="/js/js/plugins/flot/jquery.flot.spline.js"></script>
	<script src="/js/js/plugins/flot/jquery.flot.resize.js"></script>
	<script src="/js/js/plugins/flot/jquery.flot.pie.js"></script>
	<script src="/js/js/plugins/flot/jquery.flot.symbol.js"></script>
	
    <!-- Peity -->
	<script src="/js/js/plugins/peity/jquery.peity.min.js"></script>
	<script src="/js/js/demo/peity-demo.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=2.0.0"></script>
	<script src="/js/js/plugins/pace/pace.min.js"></script>

	<!-- jQuery UI -->
	<script src="/js/js/plugins/jquery-ui/jquery-ui.min.js"></script>

	<!-- Jvectormap -->
	<script src="/js/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
	<script
		src="/js/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>

	<!-- EayPIE -->
	<script src="/js/js/plugins/easypiechart/jquery.easypiechart.js"></script>

	<!-- Sparkline -->
	<script src="/js/js/plugins/sparkline/jquery.sparkline.min.js"></script>

	<!-- Sparkline demo data  -->
	<script src="/js/js/demo/sparkline-demo.js"></script>
	
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/plugins/layer/layer.min.js"></script>
    <script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <!-------- 自定义部分 -------------->
    	

<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
<!-- layer javascript -->
<script src="/js/js/plugins/layer/layer.min.js"></script>
<script type="text/javascript" > 
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
	
	layer.use('extend/layer.ext.js'); //载入layer拓展模块
	
	
	function disabledForm(formid){
		$("form[id='"+formid+"'] :input").attr("disabled","disabled");  
	    $("form[id='"+formid+"'] :text").attr("disabled","disabled");  
	    $("form[id='"+formid+"'] textarea").attr("disabled","disabled");  
	    $("form[id='"+formid+"'] select").attr("disabled","disabled");  
	    $("form[id='"+formid+"'] :radio").attr("disabled","disabled");  
	    $("form[id='"+formid+"'] :checkbox").attr("disabled","disabled"); 
	}
	
</script>
<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
<!-- Mainly scripts -->
<script src="/js/js/bootstrap.min.js?v=1.7"></script>
<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="/js/js/hplus.js?v=1.7"></script>
<script src="/js/js/top-nav/top-nav.js"></script>