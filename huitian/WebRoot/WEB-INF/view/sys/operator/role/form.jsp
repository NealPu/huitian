<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">


<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<!-- Mainly scripts -->
<script src="/js/js/jquery-2.1.1.min.js"></script>
<script src="/js/common.js"></script>
<title>${_res.get('teacher.group.add')}&修改角色</title>
<style>
label{
   width:170px;
}
</style>
</head>
<body style="background: white;">
     <div class="ibox float-e-margins">
       <div class="ibox-content">
         <form action="" method="post" id="operatorForm">
					<input type="hidden" id="id" name="role.id" value="${role.id}"/>
					<c:if test="${!empty sysuser.id }">
						<input type="hidden" name="role.version" value="${role.version + 1}">
					</c:if>
					<fieldset>
						<p>
							<label >编号：</label>
								<input type="text" name="role.numbers" value="${role.numbers }" 
								<c:if test="${operatorType eq 'update'}"> disabled="disabled"</c:if>
									class="input-xlarge" maxlength="50" vMin="1" vType="letterNumber" onblur="onblurVali(this);">
								<span class="help-inline">1-50位字母数字</span>
						</p>
						
						<p>
							<label >${_res.get('admin.dict.property.name')}</label>
								<input type="text" name="role.names" value="${role.names }" 
									class="input-xlarge" maxlength="25" vMin="2" vType="length" onblur="onblurVali(this);">
								<span class="help-inline">2-10位任意字符</span>
						</p>
						<p>
							<label>角色简介： </label>
							<textarea rows="5" cols="85" name="role.intro" style="width:480px; overflow-x: hidden; overflow-y: scroll;">${fn:trim(role.intro)}</textarea>
						</p>
						<p>
							<c:if test="${operatorType eq 'add'}">
								<input type="button" value="${_res.get('save')}" onclick="return save();" class="btn btn-outline btn-success" />
							</c:if>
							<c:if test="${operatorType eq 'update'}">
								<input type="button" value="${_res.get('update')}" onclick="return save();" class="btn btn-outline btn-success" />
							</c:if>
						</p>
					</fieldset>
				</form>
         </div>
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

<script src="/js/utils.js"></script>

 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/campus.js"></script>
    <script>
       $('li[ID=nav-nav12]').removeAttr('').attr('class','active');
    </script>
    
    <script type="text/javascript">
    	function changemodule(obj){
    		var ids = "module_"+obj;
    		$("modulenames").val($("#"+ids).val());
    		/* $("#scuserid").trigger("chosen:updated"); */
    	}
    	
    	
    	function save(){
    		var ids = $("#id").val();
    		if(ids==""){
    			if(confirm("确定保存该信息吗？")){
					$("#operatorForm").attr("action", "/operator/roleSave");
                    $("#operatorForm").submit();	
                    setTimeout("parent.layer.close(index)",3000);
					parent.window.location.reload();
				}
    		}else{
    			if(confirm("确认修改该信息吗？")){
    				$("#operatorForm").attr("action","/operator/roleUpdate");
    				$("#operatorForm").submit();
    				setTimeout("parent.layer.close(index)",3000);
					parent.window.location.reload();
    			}
    		}
    		
    	}
    
    </script>
    
</body>
</html>
