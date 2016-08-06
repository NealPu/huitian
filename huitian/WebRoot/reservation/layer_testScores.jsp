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
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.8.2.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
</head>
<body style="background: #fff">
	<div class="ibox-content" style="padding: 30px; margin-top: 28px">
		<div>
			<table class="table table-bordered">
				<tr align="center">
					<td class="table-bg1" width="180px;">${_res.get('student.name')}:</td>
					<td class="table-bg2" width="280px;">${reservation.studentname}</td>
					<td colspan="2" rowspan="4">
						<iframe id="ifstu" name="ifstu" style="width:248px;height:200px;"  frameborder=0 scrolling=no src="/student/head.jsp"> </iframe>
					</td>
				</tr>
				<tr align="center">
					<td class="table-bg1" width="20px;">${_res.get('Age')}:</td>
					<td class="table-bg2" width="20px;">${reservation.age}</td>
				</tr>
				<tr align="center">
					<td class="table-bg1">${_res.get('date')}:</td>
					<td class="table-bg2">${reservation.reservationtime}</td>
				</tr>
				<tr align="center">
					<td class="table-bg1">${_res.get('Assessor')}:</td>
					<td class="table-bg2">${reservation.username}</td>
				</tr>
			</table>
		</div>
		<form action="" method="post" id="addTestScorse">
			<input type="hidden" name="testScorse.id" value="${test.id}">
			<input type="hidden" name="testScorse.reservationid" value="${reservation.id}">
			<fieldset>
				<table class="table table-bordered">
					<tr align="center">
						<td class="table-bg1" width="20px;">Subject Area</td>
						<td class="table-bg1" width="20px;">Student Score</td>
						<td class="table-bg1" width="20px;">Total</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Speaking & Listening</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.scorse1" id="scorse1" value="${test.scorse1==null?0:test.scorse1}" onblur="check(1)">
						</td>
						<td class="table-bg1">60</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Self-Awareness</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.scorse2" id="scorse2" value="${test.scorse2==null?0:test.scorse2}" onblur="check(2)">
						</td>
						<td class="table-bg1">15</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Cultural Awareness</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.scorse3" id="scorse3" value="${test.scorse3==null?0:test.scorse3}" onblur="check(3)">
						</td>
						<td class="table-bg1">25</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Reading & Writing</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.scorse4" id="scorse4" value="${test.scorse4==null?0:test.scorse4}" onblur="check(4)">
						</td>
						<td class="table-bg1" width="20px;">25</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Critical Thinking</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.scorse5" id="scorse5" value="${test.scorse5==null?0:test.scorse5}" onblur="check(5)">
						</td>
						<td class="table-bg1">20</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Creative Thinking</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.scorse6" id="scorse6" value="${test.scorse6==null?0:test.scorse6}" onblur="check(6)">
						</td>
						<td class="table-bg1" width="20px;">16</td>
					</tr>
					
					<tr align="center">
						<td class="table-bg1" colspan="3">Qualitative Evaluation</td>
					</tr>
					<tr >
						<td  class="table-bg1" colspan="3" rowspan="3">
						<textarea rows="5" cols="85"  name="testScorse.evaluation"  style="width: 630px;color:#FF0000; overflow-x: hidden; overflow-y: scroll;">${fn:trim(test.evaluation)}</textarea>
						</td>
					</tr>
					<tr></tr>
					<tr></tr>
					<tr align="center">
						<td class="table-bg1" colspan="3">Recommendations</td>
					</tr>
					<tr align="center">
						<td class="table-bg1" >Type</td>
						<td class="table-bg1" >Course</td>
						<td class="table-bg1" >Goal</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Speaking & Listening</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.course1" id="course1" value="${test.course1}">
						</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.goal1" id="goal1" value="${test.goal1}">
						</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Reading & Writing</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.course2" id="course2" value="${test.course2}">
						</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.goal2" id="goal2" value="${test.goal2}">
						</td>
					</tr>
					<tr align="center">
						<td class="table-bg1">Elective</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.course3" id="course3" value="${test.course3}">
						</td>
						<td class="table-bg1">
							<input type="text" style="color:#FF0000;" name="testScorse.goal3" id="goal3" value="${test.goal3}">
						</td>
					</tr>
				</table>
				<input type="hidden"  id=studentid name="studentid" value="${reservation.studentid}">
				<input type="hidden"  id="picid" name="picid" value="">
				<input type="button"  class="btn btn-outline btn-primary" onclick="save()" value="${code==1?_res.get('save'):_res.get('update')}">
			</fieldset>
		</form>
	</div>

	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>

	<script type="text/javascript">
	
	$(document).ready(function(){
		setTimeout("sss()",1000);
	});
	function sss(){
		var name = '${reservation.url}';
		if(name!=""&&name!=null){
			$("#picid").val('${reservation.headpictureid}');
			$("#ifstu").contents().find("#pic").html('<img id="pic" src="/images/headPortrait/'+name+'" style="width: 144px; height: 144px;">');
		}else{
			$("#ifstu").contents().find("#pic").html('<img id="pic" src="/images/headPortrait/touxiang.png" style="width: 144px; height: 144px;">');
		}
	}
	/*校验成绩*/
		function check(code){
			var num = $("#scorse"+code).val();
			var reg=/^[0-9]+(.[0-9]{1,3})?$/;
			if(!reg.test(num)){
				layer.msg("分数输入不规范，请重新填写",1,2);
				$("#scorse"+code).focus();
			}
		}
	/*保存*/
	function save(){
		if(confirm("确认保存吗?")){
			if($("#ifstu").contents().find("#url").val()!=""){
				$("#picid").val($("#ifstu").contents().find("#url").val());
			}
			$.ajax({
				url:'/reservation/saveTestScorse',
				type:'post',
				data:$("#addTestScorse").serialize(),
				dataType:'json',
				success:function(data){
					if(data==0){
						layer.msg("成绩保存成功",1,1);	
					}else{
						layer.msg("成绩保存失败",1,2);
					}
					window.location.reload();
				}
			})
		}
	}
	</script>

	<script>
		//日期范围限制
		var courseTime = {
			elem : '#endTime',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : true,
			choose : function(dates) { //选择好日期的回调
				dateChange(dates)
			}
		};
		laydate(courseTime);
	</script>

	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
		layer.use('extend/layer.ext.js'); //载入layer拓展模块/classtype/editClassType
		/* //弹出后子页面大小会自动适应
		  var index = parent.layer.getFrameIndex(window.name);
		  parent.layer.iframeAuto(index); */
	</script>
	<script>
		$('li[ID=nav-nav21]').removeAttr('').attr('class', 'active');
	</script>
</body>
</html>