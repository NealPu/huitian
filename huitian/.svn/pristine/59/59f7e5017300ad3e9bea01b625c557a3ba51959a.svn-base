<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${_res.get('courselib.name')}</title>
<style>
  label{
    width:100px
  }
</style>
</head>
<body style="background:#fff">
        <div class="ibox-content" style="padding:10px">
            <form action="" method="post" id="callNameMessage" name="form">
					<fieldset>
						<input type="hidden" id="courseplanid"  value="${cp.id}" /> 
						<input type="hidden" id="studentids"  value="${studentids}" /> 
						<input type="hidden" id="refresh"  value="${refresh}" /> 
						<c:forEach items="${stu}" var ="stu">
						  <div>
							<label>${stu.REAL_NAME}</label>
							<input type="radio"  onclick="showRemark(${stu.studentid})" ${stu.sign==1?"checked='checked'":stu.sign==null?"checked='checked'":""} name="singn_${stu.studentid}" value="1">正常
							<input type="radio"  onclick="showRemark(${stu.studentid})" ${stu.sign==2?"checked='checked'":""} name="singn_${stu.studentid}" value="2">迟到
							<input type="radio"  onclick="showRemark(${stu.studentid})" ${stu.sign==3?"checked='checked'":""} name="singn_${stu.studentid}" value="3">旷课
							<input type="radio"  onclick="showRemark(${stu.studentid})" ${stu.sign==4?"checked='checked'":""} name="singn_${stu.studentid}" value="4">请假
							<input type="hidden" id="remark_${stu.studentid}"  value="${stu.remark}" /> 
							<div id="${stu.studentid}">
								
							</div>
						  </div>	
						</c:forEach>
					</fieldset>
					<input type="button" value="${_res.get('admin.common.submit')}"  onclick="saveCallNameMessage();" class="btn btn-outline btn-primary" />
			</form>
        </div>

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
    <script>
	  //弹出后子页面大小会自动适应
	    var index = parent.layer.getFrameIndex(window.name);
		/* parent.layer.iframeAuto(index); */
    </script>
    <script type="text/javascript">
    
	function showRemark(stuid){
		var sign = $("input:radio[name='singn_"+stuid+"']:checked").val();
		var remark = $("#remark_"+stuid).val()=="暂无备注"?"":$("#remark_"+stuid).val();
		$("#"+stuid).html("");
		if(sign!=1){
			var str = '<label> ${_res.get("course.remarks")}： </label><textarea rows="2" cols="28" id="n_'+stuid+'"  >'+remark+'</textarea>';
			$("#"+stuid).append(str);	
		}
	}
	
	function saveCallNameMessage(){
		var studentids = $("#studentids").val();
		var refresh = $("#refresh").val();
		var ids = studentids.substr(0,studentids.length-1).split(",");
		var singn = "";
		var remark="";
		for(var i=0;i<ids.length;i++){
			 var ck = $("input:radio[name='singn_"+ids[i]+"']:checked").val();
			 var cr = $("#n_"+ids[i]).val();
			 remark+=(cr==null?"暂无备注":cr==""?"暂无备注":cr)+"|";
			 singn+=ck+",";
		}
		$.ajax({
        	type:"post",
			url:"${cxt}/course/saveCallNameMessage",
			data:{"singn":singn,"remark":remark,"studentids":studentids,"cpid":$("#courseplanid").val()},
			datatype:"json",
			success : function(data) {
				 if(data.code=='0'){
					layer.msg("保存信息异常",1,2);
				}else{
					if(refresh=='true'){
						parent.window.document.getElementById('searchForm').submit();
					}
					setTimeout("parent.layer.close(index)", 1000);
				} 
			}
        });
	}
	var studentids = $("#studentids").val();
	var ids = studentids.substr(0,studentids.length-1).split(",");
	for(var i=0;i<ids.length;i++){
		showRemark(ids[i]);
	}
</script>
</body>
</html>