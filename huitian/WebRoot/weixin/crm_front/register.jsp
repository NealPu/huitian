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
<script>
	var cxt = "${cxt}";
	$(function() {
		//顾问注册手机号查询接口
		var regUrl = cxt + '/weixin/mediator/register';
		var mediator = {
			openid : null,
			realname : null,
			mobile : null,
			qq : null,
			invitecode : null
		};
		$('#search').on('click', function() {
			var username = $('#realname').val();
			var mobile = $('#mobile').val();
			var qq = $('#qq').val();
			var invitecode = $('#invitecode').val();
			if (username == '') {
				Util.showMsg('姓名不能为空');
				return;
			} else {
				if (Util.validateName(username)) {
					Util.showMsg('请输入正确的姓名');
					return;
				}
			}
			if (mobile == '') {
				Util.showMsg('手机号码不能为空');
				return;
			} else {
				if (Util.validatePhoneNum(mobile) != '') {
					Util.showMsg('请输入正确的联系方式');
					return;
				}
			}
			if (qq == '') {
				Util.showMsg('QQ号不能为空');
				return;
			} else {
				if (Util.validateQQ(qq) != '') {
					Util.showMsg('请输入正确的QQ号码');
					return;
				}
			}
			if (invitecode == '') {
				Util.showMsg('邀请码不能为空');
				return;
			}
			mediator.realname = $('#realname').val();
			mediator.mobile = $('#mobile').val();
			/* mediator.email = $('#email').val(); */
			mediator.invitecode = $('#invitecode').val();
			mediator.openid = $('#openid').val();
			mediator.qq = qq;
			indexPage();
		});
		//页面跳转
		function jumurl() {
			window.location.href = cxt + "/weixin/message/oauth2?openId=" + $('#openid').val() + "&state=tuijian";
		}
		function indexPage() {
			$.post(regUrl, mediator, function(json) {
				if (json && json.code == 0) {
					Util.showMsg("注册绑定成功！");
					setTimeout(jumurl, 2000);
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
		//显示页面
		showPage : function(pageId) {
			location.hash = pageId;
		},
		doPage : function(e) {
			var hash = location.hash;
			var selectedArr = document.querySelectorAll("section[selected='true']");
			for (var i = 0; i < selectedArr.length; i++) {
				$(selectedArr[i]).attr("selected", "false");
			}
			$(hash).attr('selected', 'true');
		},
		//校验手机号
		validatePhoneNum : function(phoneNum) {
			var partten = /^((\(\d{3}\))|(\d{3}\-))?1[3578]\d{9}$/;
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
			return username.length<2||username.length>20 || !par.test(username);
		},
		/* //验证邮箱
		validateEmail:function(email){
		    var partten = /^[a-zA-Z0-9][a-zA-Z0-9._-]*\@[a-zA-Z0-9]+\.[a-zA-Z0-9\.]+$/;
		    if(email==''){
		        return '请输入正确的邮箱名';
		    }else if(partten.test(email)==false){
		        return '请输入正确的邮箱名';
		    }else{
		        return '';
		    }
		}, */
		//校验QQ
		validateQQ : function(qq) {
			var partten = /^[1-9]{1}[0-9]{4,13}$/;
			if (qq == '') {
				return '';
			} else if (partten.test(qq) == false) {
				return 'QQ不正确，请确认';
			} else {
				return '';
			}
		},
		//校验银行卡
		validateBankcard : function(bankcard) {
			var partten = /^[1-9]{1}[0-9]{15,19}$/;
			if (bankcard == '') {
				return '';
			} else if (partten.test(bankcard) == false) {
				return '银行卡号不正确，请确认';
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
	<header class="header">注册</header>
	<div id="reg" style='margin: 10px; font-size: 15px;'>
		<input type="hidden" name="mediator.openid" id="openid" value="${openid}">
		<div>
			<label> ${_res.get("sysname")}<br>
			<input type="text" name="mediator.realname" id="realname" placeholder="建议真实姓名" value="" /><span id="sp"></span>
			</label>
		</div>
		<div>
			<label> 手机号<br>
			<input type="text" name="mediator.mobile" id="mobile" value="" /><span id="sp1"></span>
			</label>
		</div>
		<div>
			<label> QQ<br>
			<input type="text" name="mediator.qq" id="qq" value="" /><span id="sp3"></span>
			</label>
		</div>
		<div>
			<label> 邀请码<br>
			<input type="text" name="invitecode" id="invitecode" value="" /><span id="sp4"></span>
			</label>
		</div>
		<div class="cx" id='search' style="margin-top: 10px;">
			<input class="cxys" type="button" value="注册" id='inputSearch' />
		</div>
	</div>

	<div id="msgMask" class="msgMask disno" style="height: 500px;">
		<div id="msgbox" class="msgbox" style="margin-top: 206px;">
			<img src="${cxt}/static/images/error.png" height='20' /><em style="font-style: initial;"></em>
		</div>
	</div>

	<footer style="margin-top: 15px; margin-bottom: 5px">
		<p align="center">&copy; ${company }</p>
	</footer>
</body>
</html>