<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
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
 label{
   width:100px
 }
</style>
</head>
<body style="background-color:#EFF2F4">   
	<div class="margin-nav1" style="padding:10px">
				<div class="float-e-margins">
					<div class="ibox-content">
						<form action="/report/submitPoints" method="post" id="pointForm">
							<fieldset>
								<p>
									<label>  ${_res.get("student") }： </label>
									<input type="hidden" id="tab" name="setPoint.id"  value=""> 
									<input type="hidden" id="studentid" name="studentid"  value="${student.id }"> 
									<input type="text" name="setPoint.studentname" id="real_name" readonly="readonly"  value="${student.real_name}" maxlength="20" class="required" /> 
								</p>
								<p>
									<label> ${_res.get("teacher")}： </label> 
									<select id="teacherids" name="teacherids" data-placeholder="${_res.get('Please.select')}（${_res.get('Multiple.choice')}）" class="chosen-select" multiple
										style="width: 340px;" tabindex="4">
										<c:forEach items="${teacherList}" var="teacher">
											<option value="${teacher.id}" class="options" id="tid_${teacher.id }">${teacher.real_name}</option>
										</c:forEach>
									</select> 
									<input id="tids" name="setPoint.teacherids" value="" type="hidden">
								</p>
								<p>
									<label> ${_res.get('Appointment.date')}： </label> 
									<span id="datelists">
										<span><input type="text" readonly="readonly" class="aid_0" name="datelist" id="date_option" style="width:90px;" ></span>
									</span>
								</p>
								<p>
									<label>${_res.get('Advance')}： </label> 
									<input type="text" id="days" name="setPoint.days" size="5" />&nbsp;${_res.get('day')}
								</p>
								<p>
									<label> ${_res.get("course.remarks")}： </label> 
									<textarea rows="4" cols="65" id="remarks" name="setPoint.remark"  ></textarea>
								</p>
								<p>
										<div id="savegroups">
											<input type="button" value="${_res.get('save')}" onclick="savePoint()" class="btn btn-outline btn-primary" />
										</div>
								</p>
							</fieldset>
						</form>
					</div>

				</div>
				<div style="clear: both;"></div>
			</div>
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
    <script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
		
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
    <script src="/js/js/plugins/layer/laydate/laydate.js"></script>
    <script>
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.iframeAuto(index);
    
        var date_option = {
            elem: '#date_option',
            format: 'YYYY-MM-DD',
            max: '2099-06-16', 
            istime: false,
            istoday: false,
            choose: function (datas) {
            	$("#date_option").val("");
            	appendValue(datas);
            }
        };
        laydate(date_option);
        
        function appendValue(dates){
        	var maxId = -1;
        	$("input[name='datelistval']").each(function(){
        		var currentid = $(this).attr("id").split("_")[1];
        		maxId = maxId>currentid?maxId:currentid;
        	});
        	maxId = parseInt(maxId)+1;
        	var spanId = "span_"+maxId;
        	var inputId = "input_"+maxId;
        	var str = "<span onclick='removeSpan(this)' id='"+spanId+"'>"+dates+"、</span>";
        	$("#datelists").append(str);
        	var hideInput = "<input name='datelistval' id='"+inputId+"' type='hidden' value='"+dates+"' />";
        	$("#datelists").append(hideInput);
        }
        function removeSpan(obj){
        	var idnum = $(obj).attr("id").split("_")[1];
        	var inputId = "input_"+idnum;
        	$("#"+inputId).remove();
        	$(obj).remove();
        }
        
        
        function savePoint(){
        	var tchids = $("#teacherids").val();
        	if(tchids==null||tchids.length==0){
        		layer.msg("请选择老师",2,5);
        		return false;
        	}
        	var datelists = "";
        	$("input[name='datelistval']").each(function(){
        		datelists += $(this).val();
        	});
        	if(datelists.trim().length==0){
        		layer.msg("请填写时间",2,5);
        		return false;
        	}
        	$.ajax({
        		url:"/report/submitPoints",
        		type:"post",
        		dataType:"json",
        		data:$("#pointForm").serialize(),
        		success:function(result){
        			if(result.code=='1'){
	       				layer.msg(result.msg,2,1);
	       				setTimeout("parent.layer.close(index)", 2100);
        			}else{
	       				layer.msg(result.msg,2,2);
        			}
        		}
        	});
        }
        
        
        
		$('li[ID=nav-nav17]').removeAttr('').attr('class', 'active');
		
		//弹出后子页面大小会自动适应
	    var index = parent.layer.getFrameIndex(window.name);
	    parent.layer.iframeAuto(index);
	</script>
</body>
</html>