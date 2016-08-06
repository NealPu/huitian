<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<title>${company }</title>
<link rel="stylesheet" type="text/css" href="${cxt}/static/css/common.css">
<script src="${cxt}/js/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
	var cxt = "${cxt}";
</script>
<script src="${cxt}/ui/charisma/js/jquery-1.7.2.min.js"></script>
<script>
	$(function() {
		//顾问注册手机号查询接口
		var referralsUrl = cxt + '/weixin/mediator/referrals';
		var contacter = {
			openid : null,
			contactername : null,
			phonenumber : null,
			subjectid : null,
			relation : null,
			needcalled : null,
			campus : null,
			note : null
		};
		$('#referrals').on(
				'click',
				function() {
					var r = $('#contactername').val().trim(), m = $('#phonenumber').val().trim(), n = $('#note').val().trim(), s = $('#subjectid')
							.val(), o = $('#openid').val(), l = $('#relation').val();
					if (Util.validateName($('#contactername'))) {
						Util.showMsg('联系人姓名请输入2-20位汉字或字母组合');
						return;
					}
					if (Util.validateTel(m) != '') {
						Util.showMsg('请输入正确的联系方式');
						return;
					}
					contacter.contactername = r;
					contacter.phonenumber = m;
					contacter.subjectid = s;
					contacter.relation = l;
					contacter.note = n;
					contacter.openid = o;
					contacter.needcalled = $('#needcalled').val();
					contacter.campus = $("input[name='campus']:checked").val();
					referralsInfo();
				});
		function jumur1() {
			window.location.href = cxt + "/weixin/message/oauth2?openId=" + $('#openid').val() + "&state=gdqk";
		}
		function referralsInfo() {
			$.post(referralsUrl, contacter, function(json) {
				if (json && json.code == 0) {
					Util.showMsg("提交成功！");
					setTimeout(jumur1, 2000);
				} else {
					Util.showMsg(json.msg);
				}
			}, 'json');
		}
		//隐藏 提示框
		$('#msgMask').on('click', function(e) {
			$('#msgMask').addClass('disno');
		});
	});

	Util = {
		//校验手机号
		validatePhoneNum : function(phoneNum) {
			var partten = /^1[34578]\d{9}$/;
			if (phoneNum == '') {
				return "请输入正确的手机号码（中国境内）"
			} else if (partten.test(phoneNum) == false) {
				return "请输入正确的手机号码（中国境内）";
			} else {
				return "";
			}
		},
		//校验电话
		validateTel : function(tel) {
			var phReg = /^0\d{2,4}-?\d{7,8}$/;
			return !phReg.test(tel) && Util.validatePhoneNum(tel) != ''
		},
		//校验名称
		validateName : function(username) {
			var par = /^[A-Za-z\u4e00-\u9fa5]+$/;
			return username.val().length < 2 || username.val().length > 20 || !par.test(username.val());
		},
		//显示错误提示
		showMsg : function(msg, obj, isfocus) {
			if (arguments.length == 1) {
				$('#msgbox').css('margin-top', '206px');
				$('#msgMask').removeClass('disno');
				$('#msgbox em').html(msg);
				return;
			}
			var box = $('body').getBox();
			var h = 0;
			if (!obj) {
				var wh = window.innerHeight || document.documentElement.clientHeight;
				$('#msgMask').css('height', (box.height < 210 ? wh : box.height) + 'px');
				h = Math.abs(box.top) + wh / 2 - 15;
			} else {
				h = Math.abs(box.top) + $(obj).getBox().top + 52;
				if (!(isfocus && isfocus == 1)) {
					obj.focus();
				}
			}
			$('#msgbox').css('margin-top', h + 'px');
			$('#msgMask').removeClass('disno');
			$('#msgbox em').html(msg);
		}
	}
</script>
</head>
<body>
	<header class="header">我要推荐 </header>
	<div id="reg" style='margin: 10px; font-size: 15px;'>
		<input type="hidden" name="counselor.openid" id="openid" value="${openid}">
		<div>
			<label for="realname"> 学生联系人：<br>
			<input id="contactername" type="text" value="" placeholder="建议真实姓名" /><span id="sp"></span>
			</label>
		</div>
		<div>
			<label for="mobile"> ${_res.get("admin.user.property.telephone")}： <br>
			<input id="phonenumber" type="text" value="" placeholder="建议手机号" /><span id="sp1"></span>
			</label>
		</div>
		<div class="ldq" style="display: inline;">
			<label for="subject"> 咨询科目：
		</div>
		<div class="rdq">
			<select name="subjectid" id="subjectid" class="xlc" style="border: 0; weidth: 28px; height: 27px; display: inline; font-size: 15px;"${cxt}/static/images/jiantou.png");">
				<c:forEach items="${subjectList}" var="subject">
					<option value="${subject.id}">${subject.subject_name}</option>
				</c:forEach>
			</select> <span id="sp2"></span> </label>
		</div>
		<div class="ldq">
			<label for="guanxi"> 与学生关系：
		</div>
		<div class="rdq">
			<select name="relation_select" id="relation" class="xlc" style="border: 0; weidth: 28px; height: 27px; font-size: 15px; background-image: url("${cxt}/static/images/jiantou.png");">
				<option value="1">本&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人</option>
				<option value="2">母&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;亲</option>
				<option value="3">父&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;亲</option>
				<option value="4">其&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;他</option>
			</select> <span id="sp3"></span> </label>
		</div>
		<div class="ldq">
			<label for="lianxi"> 是否联系：
		</div>
		<div class="rdq">
			<select id="needcalled" class="xlc" style="border: 0; weidth: 28px; height: 27px; font-size: 15px; background-image: url("${cxt}/static/images/jiantou.png");">
				<option value="1">现在联系</option>
				<option value="2">今天上午联系</option>
				<option value="3">今天下午联系</option>
				<option value="4">今天晚上联系</option>
				<option value="5">等待呼入(报备)</option>
			</select> <span id="sp4"></span> </label>
		</div>
		<div>
			${_res.get("class.location")}：<br>
			<c:forEach items="${campusList }" var="campus">
				<input type="radio" id="zgc" value="${campus.id }" name="campus" checked="checked"><label for="zgc">${campus.campus_name }（${campus.fzrname }）</label> 
			</c:forEach>
			</form>
			<span id="sp5"></span>
		</div>
		<div>
			${_res.get("course.remarks")}：<br>
			<div class="beizhu">
				<textarea id="note" maxlength="400" rows="5" style="width: 100%; height: 100%; font-size: 15px" placeholder="如需备注请填写，400字以内。"></textarea>
			</div>
		</div>
		<div style="overflow: hidden;">
			<div class="cx" id='referrals'>
				<input class="cxys" type="button" value="提交" />
			</div>
		</div>
	</div>
	<div id="msgMask" class="msgMask disno" style="height: 500px;">
		<div id="msgbox" class="msgbox" style="margin-top: 206px;">
			<img src="${cxt}/static/images/error.png" height='20' /><em style="font-style: initial; font-size: 15px;"></em>
		</div>
	</div>

	<footer style="margin-top: 15px; margin-bottom: 5px">
		<p align="center">&copy; ${company }</p>
	</footer>
</body>
</html>