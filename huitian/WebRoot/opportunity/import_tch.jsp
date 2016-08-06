<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<title>导入</title>
<style>
   body{
    color:#555;
    background:#fff
   }
  .font-med{
    font-size: 16px;
    font-weight: 500
  }
  .file-box{ position:relative;width:480px;margin-top:10px} 
  .txt{ height:22px; border:1px solid #cdcdcd; width:180px;} 
  .file{ position:absolute; top:0; right:80px; height:24px; filter:alpha(opacity:0);opacity: 0;width:260px } 
</style>
</head>
<body>
     <div class="ibox-content">
      
         <div class="file-box"> 
			<form action="" method="post" id="searchForm" enctype="multipart/form-data"> 
			<label> ${_res.get("system.campus")}： </label> 
				<select id="campus" name="campus" class="chosen-select" style="width: 130px;" tabindex="4">
					<option value="">--${_res.get('Please.select')}--</option>
				   <c:forEach items="${campusList}" var="campus">
						<option value="${campus.Id}" class="options" >${campus.CAMPUS_NAME}</option>
					</c:forEach>
				</select>
				<div style="margin-top:10px">
					<label>请选择文件：</label>
					<span><input type='text' placeholder="点击选择文件" name='textfield' id='textfield' class='txt' /> 
					<input type="file" name="fileField" class="file" id="fileField" size="10"  style="top:46px;left: 76px; width:180px;height:28px;"
					 onchange="document.getElementById('textfield').value=this.value" /> 
					</span>
					<input type='button' onclick="importTch()" class='btn btn-outline btn-primary' value='导入机会' /> 
					<input type="button" onclick="downLoad()" class="btn btn-outline btn-success" value="下载模版" />
				</div> 
			</form> 
		  </div>  
     </div>
<!-- Mainly scripts -->
<script src="/js/js/jquery-2.1.1.min.js"></script>
<script src="/js/utils.js"></script>

<script>
		function importTch(){
			if($("#fileField").val().trim()==""||$("#fileField").val()==null){
				alert("请选择文件");
				return false;
			}
			if($("#campus").val().trim()==""||$("#campus").val()==null){
				alert("请选择校区");
				return false;
			}
			
			$("#searchForm").attr("action", "${cxt}/opportunity/importTch");
		    $("#searchForm").submit();	
		}
		function downLoad(){
			$("#searchForm").attr("action", "${cxt}/opportunity/downLoad");
		    $("#searchForm").submit();
		}
</script>
<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script src="/js/utils.js"></script>
<script src="/js/dist/timelineOption.js"></script>
	<script>
		
		//-----------------下拉菜单插件
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
	</script>

 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
</body>
</html>
