<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<title>${company }</title>
<link rel="stylesheet" type="text/css" href="${cxt }/static/css/common.css">
<script src="${cxt }/js/js/jquery-2.1.1.min.js"></script>
<script>
		//校验电话
		//validateTel : function(tel) {
		//	var phReg = /^0\d{2,4}-?\d{7,8}$/;
		//	return !phReg.test(tel) && Util.validatePhoneNum(tel) != ''
		//}
</script>
</head>
<body>
	<header class="header">推荐同学 </header>
	<form id="paymentForm" action="" method="post">
	<div id="reg" style='margin: 10px; font-size: 15px;'>
		<input type="hidden" name="counselor.openid" id="openid" value="${openid}">
		<div class="tjtx-head" style="font-size: 14px;color: red;">
			我们不搞虚的！你没看错！<br>
			马上推荐你身边同学来！<br>
			礼品咔咔咔！<br>
			Apple Watch、iPhone6、MacBook、MacBook Pro<br>
		<a href="http://weixin.luode.org/manage/counselor_dhjf.html">【礼品列表】</a>
		</div>
		<div>
			<label for="realname"> 我的姓名：<br>
			<input id="myname" name="myname" type="text" value="" placeholder="建议真实姓名" class="tjtx-input"/><span id="sp"></span>
			</label>
		</div>
		<div>
			<label for="mobile"> 我的手机： <br>
			<input id="myphone" name="myphone" type="text" value="" placeholder="建议真实手机号" class="tjtx-input"/><span id="sp1"></span>
			</label>
		</div>
		<div>
			<label for="realname" class="tjtx-label"> 同学姓名：
			<input id="stuname" name="stuname" type="text" value="" placeholder="建议真实姓名" class="tjtx-put"/><span id="sp3"></span>
			</label>
		</div>
		<div>
			<label for="mobile" class="tjtx-label"> 同学手机：
			<input id="stuphone" name="stuphone" type="text" value="" placeholder="建议真实手机号" class="tjtx-put"/><span id="sp4"></span>
			</label>
		</div>
		<div class="ldq" style="display: inline;">
			<label for="subject"> 学习科目：
		</div>
		<div class="rdq">
			<select name="subjectid" id="subjectid" class="xlc" style="border: 0; weidth: 28px; height: 27px; display: inline; font-size: 15px;"${cxt }/static/images/jiantou.png");">
				<c:forEach items="${subjectList}" var="subject">
					<option value="${subject.id}">${subject.subject_name}</option>
				</c:forEach>
			</select> <span id="sp2"></span> </label>
		</div>
		<div>
			${_res.get("course.remarks")}：<br>
			<div class="beizhu">
				<textarea id="remark" name="remark" maxlength="400" rows="5" style="width: 100%; height: 100%; font-size: 15px" placeholder="如需备注请填写，400字以内。"></textarea>
			</div>
		</div>
		<div style="overflow: hidden;">
			<div class="cx" id='referrals'>
				<input class="cxys" onclick="checkform()" type="button" value="提交" />
			</div>
		</div>
	</div>
	</form>
	<div id="msgMask" class="msgMask disno" style="height: 500px;">
		<div id="msgbox" class="msgbox" style="margin-top: 206px;">
			<img src="${cxt }/static/images/error.png" height='20' /><em style="font-style: initial; font-size: 15px;"></em>
		</div>
	</div>

	<footer style="margin-top: 15px; margin-bottom: 5px">
		<p align="center">&copy; ${company }</p>
	</footer>
	
	<script>	
	   function checkform(){
		   var mn = $('#myname').val().length;
		   var sn = $('#stuname').val().length;
					if ($('#myname').val()=='' || mn < 3){
						alert('我的姓名请输入2-20位汉字或字母组合');
						return false;
					}else{
						if ($('#myphone').val()==''){
							alert('我的号码不能为空');
							return false;
						}else{
							if ($('#stuname').val()=='' || sn < 3){
								alert('同学姓名请输入2-20位汉字或字母组合');
								return false;
							}else{
								if ($('#stuphone').val()==''){
									alert('请输入推荐同学正确的联系方式');
									return false;
								}else{
									$.ajax({
							   			url:"${cxt }/weixin/mediator/studentRecommend",
							   			type:"post",
							   			async: false,
							   			data:{
							   				"myname":$('#myname').val(),
							   				  "myphone":$('#myphone').val(),
							   				  "stuname":$('#stuname').val(),
							   				  "stuphone":$('#stuphone').val(),
							   				  "subjectid":$('#subjectid').val(),
							   				  "remark":$('#remark').val()
							   				  },
							   			dataType:"json",
							   			success:function(data){
												if(data.code=='0'){
													alert("提交成功！");
													$('#myname').val("");
									   				$('#myphone').val("");
									   				$('#stuname').val("");
									   				$('#stuphone').val("");
									   				$('#subjectid').val("");
									   				$('#remark').val("");
												}else{
													alert(data.msg);
												}
							   			}
							   		 });									
								}
							}
						}
					}
	   }
	</script>
</body>
</html>