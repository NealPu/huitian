<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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