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
<link href="/css/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet"> 

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>短信模版设置</title>
<style>
label{
  width:100px;
}
p{
  width:350px
}
input[type="text"]{
  width:240px
}
</style>
</head>
<body>
     <div class="ibox float-e-margins" style="margin-bottom:0">
        <div class="ibox-content">
            <form action="/smstemplate/save" method="post" id="templateForm">
					<input type="hidden" name="smsTemplate.id" id="id" value="${sms.id}" />
					<fieldset>
						<p>
							<label> 
								接收用户：
							</label>
							<select name="smsTemplate.sms_type" id="sms_type" class="chosen-select" style="width:240px;">
							     <option value="1" ${sms.sms_type=='1'?"selected='selected'":""}>${_res.get('student')}</option>
								 <option value="2" ${sms.sms_type=='2'?"selected='selected'":""}>${_res.get('Patriarch')}</option>
								 <option value="3" ${sms.sms_type=='3'?"selected='selected'":""}>${_res.get('teacher')}</option>
							</select>
						</p>
						<p>
							<label>模版名称：</label>
							<input type="text" name="smsTemplate.sms_name"  id ="sms_name"  value="${sms.sms_name}" />
						</p>
						<p>
							<label>模板编码： </label> 
							<input type="text" name="smsTemplate.numbers"  id ="numbers"  value="${sms.numbers}" ${type eq 'update'?'disabled="disabled"':''}/>
						</p>
						<!-- <p>
							<label style="margin-top:5px">提交日期： </label> 
							<input type="text" readonly="readonly" id="date1" name="smsTemplate.sms_date" value="" style="margin-top: -8px; width:240px;" />
						</p> -->
						<p style="width:500px">
							<label>中文模版：</label>
							<textarea rows="4" cols="85" id="ch_style" name="smsTemplate.sms_ch_style"  style="width:390px;overflow-x: hidden; overflow-y: scroll;">${fn:trim(sms.sms_ch_style)}</textarea>
						</p>
						<p id="difference" style="width:500px">
							<label>英文模版：</label>
							<textarea rows="4" cols="85" id="en_style" name="smsTemplate.sms_en_style"  style="width:390px;overflow-x: hidden; overflow-y: scroll;">${fn:trim(sms.sms_en_style)}</textarea>
						</p>
						<p style="width:500px">注意：模板编码和内容为{xxxx}的禁止修改，如修改将影响短信下发。</p>
						<c:if test="${type eq 'add'}">
							<input type="button" value="保存" onclick="save('1')" class="btn btn-outline btn-primary">
						</c:if>
						<c:if test="${type eq 'update'}">
							<input type="button" value="更新"  onclick="save('2')"  class="btn btn-outline btn-primary">
						</c:if>
					</fieldset>
					
				</form>
        </div>
     </div>
	<!-- Mainly scripts -->
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<script src="/js/utils.js"></script>
 	<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
<!-- Chosen -->
<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
 <script>     
        $(".chosen-select").chosen({disable_search_threshold: 15});
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
 	<script>
 /*保存模板*/
 		function save(num){
	 		var str = "";
		 	 if(num=='1'){
		 		str+='您确定保存模板吗？';
		 	}else{
		 		str+='您确定修改模板吗？';
		 	}
		 	 var name = $("#sms_name").val();
		 	 var numbers = $("#numbers").val();
		 	 var type = $("#sms_type").val();
		 	 if(name==''){
		 		layer.msg("请填写模板名称",1,2);
		 	 }else if(numbers==''){
		 		layer.msg("请填写模板编号",1,2);
		 	 }else if(type==''){
		 		layer.msg("请选择接收用户",1,2);
		 	 }else{
			 		$.layer({
		 	    		    shade : [0], 
		 	    		    area : ['auto','auto'],
		 	    		    dialog : {
		 	    		        msg:str,
		 	    		        btns : 2, 
		 	    		        type : 4,
		 	    		        btn : ['确定','取消'],
		 	    		        yes : function(){
		 	  		            	$.ajax({
		 				        		url:'/smstemplate/save',
		 				        		type:'post',
		 				        		data:$('#templateForm').serialize(),
		 				        		dataType:'json',
		 				        		success:function(date){
		 				        			if(date.code==1){
		 				        				layer.msg("成功",1,1);
		 				        				setTimeout("parent.window.location.reload()",1000);
		 				        			}else{
		 				        				layer.msg("异常丶请联系管理员",2,2);
		 				        				setTimeout("parent.window.location.reload()",1000);
		 				        			}
		 				        		}
		 				        	});
		 	    		        },
		 	    		        no : function(){
		 	    		            
		 	    		        }
		 	    		    }
		 	    		}); 
		 	 }
 		}
 /*选择时显示模板*/
 		function showMessage(){
 			var sms_name = $("#sms_name").val();
 			var type= $("#sms_type").val();
 			if(type==1 && sms_name==0){
 				$("#ch_style").val("上课通知：{student_name}同学您好：在{course_date}{rank_name},给您在{campus_name}的{room_name},安排了{teacher_name}老师的{course_name}课程，请注意。");
 				$("#en_style").val("");
 			}else if(type==1 && sms_name==1){
 				$("#ch_style").val("课程取消通知：{student_name}同学您好！刚刚给您取消了定于{course_date}{rank_name},在{campus_name}{room_name}的{course_name}课程，请注意");
 				$("#en_style").val("");
 			}else if(type==2 && sms_name==0){ 
 				$("#ch_style").val("上课通知：{student_name}同学家长您好：我们在{course_date}{rank_name},给您的孩子在{campus_name}的{room_name},安排了{teacher_name}老师的{course_name}课程，请注意。");
 				$("#en_style").val("");
 			}else if(type==2 && sms_name==1){ 
 				$("#ch_style").val("课程取消通知：{student_name}同学家长您好：刚刚给您的孩子取消了定于{course_date}{rank_name},在{campus_name}{room_name}的{course_name}课程，如又疑问请联系我们的教务人员。");
 				$("#en_style").val("");
 			}else if(type==3 && sms_name==0){ 
 				$("#ch_style").val("上课通知：{teacher_name}老师您好：在{course_date}{rank_name},给您在{campus_name}的{room_name},安排了{teacher_name}老师的{course_name}课程，请注意。");
 				$("#en_style").val("");
 			}else if(type==3 && sms_name==1){ 
 				$("#ch_style").val("课程取消通知：{teacher_name}老师您好：刚刚给您取消了定于{course_date}{rank_name},在{campus_name}{room_name}，{student_name}同学的{course_name}课程");
 				$("#en_style").val("");
 			}
 			
 		}
 		//$(document).ready(showMessage());
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
		};
		laydate(date1);
	</script>
</body>
</html>
