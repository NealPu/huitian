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
	var showid = "${showinfo}";
</script>
<script src="${cxt}/ui/charisma/js/jquery-1.7.2.min.js"></script>
<script>
	$(function() {
		//隐藏 提示框
		$('#msgMask').on('click', function(e) {
			$('#msgMask').addClass('disno');
		});

		$('#changeBankinfo').on('click', function() {
			$('#searchReslutId').addClass('disno');
			$('#bankinfo').removeClass('disno');
		});

		var bankUrl = cxt + '/weixin/mediator/changeBank';
		$('#changeBank').on('click', function() {
			var a = $('#A_cardholder').val(), b = $('#A_bankcard').val(), c = $('#A_bankname').val();
			if (Util.validateName($('#A_cardholder'))) {
				Util.showMsg('请输入正确的持卡人姓名');
				return;
			}
			if (Util.validateBankcard(b) != '') {
				Util.showMsg('请输入正确的银行卡号');
				return;
			}
			if (Util.validateName($('#A_bankname'))) {
				Util.showMsg('银行名称由4-30位汉字或字母组合');
				return;
			}
			var params = {
				cardholder : a,
				bankcard : b,
				bankname : c,
				id : $('#idsV').val()
			};
			$.post(bankUrl, params, function(data) {
				if (data.code == 0) {
					Util.showMsg(data.msg);
					setTimeout(function jumurl() {
						window.location.href = cxt + "/weixin/message/oauth2?openId=" + $('#openid').val() + "&state=register";
					}, 2000);
				} else {
					Util.showMsg(data.msg, null);
				}
			}, 'json');
		});

	});

	Util = {
		//显示页面
		showPage : function(pageId) {
			location.hash = pageId;
		},
		doPage : function(e) {
			var hash = location.hash;
			var selectedArr = document.querySelectorAll("div[selected='true']");
			for (var i = 0; i < selectedArr.length; i++) {
				$(selectedArr[i]).attr("selected", "false");
			}
			$(hash).attr('selected', 'true');
		},
		//校验手机号
		validatePhoneNum : function(phoneNum) {
			var partten = /^((\(\d{3}\))|(\d{3}\-))?1[358]\d{9}$/;
			if (phoneNum == '') {
				return "请输入正确的手机号码（中国境内）"
			} else if (partten.test(phoneNum) == false) {
				return "请输入正确的手机号码（中国境内）";
			} else {
				return "";
			}
		},
		//校验名称
		validateName : function(username) {
			alert(username.val());
			var par = /^[A-Za-z\u4e00-\u9fa5]+$/;
			return username.val().length < 2 || username.val().length > 20 || !par.test(username.val());
		},
		//校验银行卡
		validateBankcard : function(bankcard) {
			var partten = /^[1-9]{1}[0-9]{15,19}$/;
			if (bankcard == '') {
				return '银行卡号不能为空。';
			} else if (partten.test(bankcard) == false) {
				return '银行卡号不正确，请确认！';
			} else {
				return '';
			}
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
	<div id="msgMask" class="msgMask disno" style="height: 500px;">
		<div id="msgbox" class="msgbox" style="margin-top: 206px;">
			<img src="${cxt}/static/images/error.png" height='20' /><em style="font-style: initial;"></em>
		</div>
	</div>
	<div class="wrapper" id="searchReslutId">
		<header class="header"> 您的基本信息 </header>
		<div class="main">
			<input type="hidden" id="idsV" value="${counselor.id }">
			<input type="hidden" id="openid" value="${counselor.openid }">
			<div class="yhxx" style="border-bottom: none; border-radius: 5px 5px 0 0; -webkit-border-radius: 5px 5px 0 0;">
				<span>${_res.get("sysname")}</span>
				<p id="realnameV">${counselor.realname }</p>
			</div>
			<div class="yhxx" style="border-bottom: none;">
				<span>手机号</span>
				<p id="mobileV">${counselor.phonenumber }</p>
			</div>
			<div class="yhxx" style="border-bottom: none;">
				<span>QQ号码</span>
				<p id="qqV">${counselor.qq }</p>
			</div>
			<div class="yhxx">
				<span>注册日期</span>
				<p id="regdateV"><fmt:formatDate value="${counselor.createtime }" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:dd"/></p>
			</div>
			<div style="height: 10px;"></div>
			<div class="yhxx" style="border-bottom: none;">
				<span>银行卡信息</span>
				<div id="changeBankinfo" class="cgmima">绑定/变更</div>
			</div>
			<div class="Bankinfo">
				<p id="banknameV">开户行：${counselor.bankname }</p>
				<p id="cardholderV">持卡人：${counselor.cardholder }</p>
				<p id="bankcardV">银行卡：${counselor.bankcard }</p>
			</div>
			<div class="xcips">
				<strong>温馨提示：</strong>注册信息暂不提供在线修改。
			</div>
		</div>
	</div>

	<div class="wrapper disno" id="bankinfo" selected="true">
		<header class="header"> 银行卡信息 </header>
		<div class=bank>
			<div>
				<div class="bleft">持卡人姓名：</div>
				<div class="bright">
					<input id="A_cardholder" type="text" value="" />
				</div>
			</div>
			<div>
				<div class="bleft">银行卡号：</div>
				<div class="bright">
					<input id="A_bankcard" type="text" value="" />
				</div>
			</div>
			<div>
				<div class="bleft">开户行名称：</div>
				<div class="bright">
					<textarea id="A_bankname"></textarea>
				</div>
			</div>
		</div>
		<div style="overflow: hidden;">
			<div class="cx" id='changeBank'>
				<input class="cxys" type="button" value="完成" />
			</div>
		</div>
	</div>

	<footer style="margin-top: 15px; margin-bottom: 5px">
		<p align="center">&copy; ${company }</p>
	</footer>
</body>
</html>