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
		Util.doPage();
		Util.showPage('searchId');
		$('#additional').on('click', function() {
			Util.showPage('followContent');
			var a = $('#contactername').val().trim(), b = $('#contacterids').val().trim(), c = $('#subjectname').val().trim();
			$('#A_contactername').val(a || '');
			$('#A_contacterids').val(b || '');
			$('#A_subjectname').val(c || '');
		});
		//隐藏 提示框
		$('#msgMask').on('click', function(e) {
			$('#msgMask').addClass('disno');
		});
		var bankUrl = cxt + '/weixin/mediator/additionalInfo';
		$('#additionalInfo').on('click', function() {
			var a = $('#A_contacterids').val().trim(), b = $('#A_content').val().trim();
			if (b.length<5||b.length>400) {
				Util.showMsg('反馈内容在5到400字以内！');
				return;
			}
			var params = {
				contacterIds : a,
				content : b,
			};
			$('#wancheng').addClass('disno');
			Util.showMsg('正在提交补充内容！');
			$.post(bankUrl, params, function(data) {
				if (data.code == 0) {
					$('#wancheng').addClass('disno');
					Util.showMsg('补充信息反馈成功！');
					b = $('#A_content').val('')
					resetShow();
				} else {
					Util.showMsg(data.msg);
					$('#wancheng').removeClass('disno');
				}
			}, 'json');
		});

		window.onhashchange = Util.doPage;

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
	function buchong(ids, name, subject) {
		$('#followContent').removeClass('disno');
		$('#wancheng').removeClass('disno');
		$('#searchId').addClass('disno');
		$('#A_contacterids').val(ids || '');
		$('#A_contactername').val(name || '');
		$('#A_subjectname').val(subject || '');
	}
	function showFollow(id) {
		Util.showMsg('正在努力加载中！');
		var url = cxt + '/weixin/mediator/showFollows';
		$.post(
						url,
						{
							id : id
						},
						function(json) {
							if (json && json.code == 0) {
								$('#follow').removeClass('disno');
								$('#con').addClass('disno');
								var tr = "<div>";
								$('#title').html("<span>&lt;&lt;返回&nbsp;" + json.title + "</span>");
								$('#bcnr').html(
										"<input class='cxys' type='button' value='补充反馈内容' onclick=\"buchong('" + json.id + "','" + json.name + "','"
												+ json.subjectname + "')\"/>");
								for (i in json.data) {
									if (json.data[i].MEDIATORID != null) {
										tr += "<div style='margin-right:3%;margin-top:-2%;width:280px;float:right;'><div style='margin-left:270px;width:15px;hight:5px;'>我</div><div class='rsanjiao' style='width: 10px;height: 30px;'></div><div class='talk_righttext'>"
									} else {
										tr += "<div style='margin-left:1%;margin-top:-2%;width:280px;float:left;'><div style='margin-right:20px;width:22px;hight:5px;'>CC</div><div class='lsanjiao' style='width: 10px;height: 30px;'></div><div class='talk_lefttext'>"
									}
									tr += json.data[i].CONTENT + "<br>";
									tr += "<div style='font-size:10px;text-align: center;margin-bottom:0;'>" + json.data[i].CREATETIME
											+ "</span><p style='width:99%;word-wrap: break-word;'>" + "</div>";
									tr += "</p></div></div><div>";
								}
								$('#detail').html(tr + "</div>");
								$('#msgMask').addClass('disno');
							} else {
								alert("没有数据");
							}
						}, 'json');
	}
	function resetShow() {
		$('#con').removeClass('disno');
		$('#searchId').removeClass('disno');
		$('#follow').addClass('disno');
		$('#followContent').addClass('disno');
	}
</script>
</head>
<body>
	<div id="msgMask" class="msgMask disno" style="height: 500px;">
		<div id="msgbox" class="msgbox" style="margin-top: 206px;">
			<img src="${cxt}/static/images/error.png" height='20' /><em style="font-style: initial; font-size: 15px;"></em>
		</div>
	</div>
	<div class="wrapper" id=searchId>
		<header class="header"> 我的推荐跟进 </header>
		<div id="con" class="main">
			<c:choose>
				<c:when test="${empty contacterList }">
					<span>您还没有推荐，赶快去推荐吧！</span>
				</c:when>
				<c:otherwise>
					<c:forEach items="${contacterList }" var="contacter">
						<div class="yhxx" style="margin-top: 5px;">
							<span onclick="showFollow('${contacter.id}')" style="font-size: 15px"> 
							<strong>
								<c:choose>
									<c:when test="${contacter.status eq true }">跟进中</c:when>
									<c:otherwise>
										<c:if test="${contacter.isconver eq true }">${_res.get('Is.a.single')}</c:if>
										<c:if test="${contacter.isconver eq false }">${_res.get('Not.a.single')}</c:if>
									</c:otherwise>
								</c:choose>
							</strong>
							<fmt:formatDate value="${contacter.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/>
							${contacter.contacter}咨询${contacter.subject_name}
							</span> &nbsp;&gt;&gt;
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="follow" class="main disno" style="margin-top: 5px;">
			<div class="yhxx" onclick="resetShow()">
				<span id="title"></span>
			</div>
			<div class="Bankinfo" style="background-color: white; overflow: auto;">
				<div id="detail"></div>
			</div>
			<div id="bcnr" class="cx" style="margin-top: 10px;"></div>
		</div>
	</div>

	<div class="wrapper disno" id="followContent">
		<header class="header"> 补充信息 </header>
		<div class=bank>
			<div>
				<div class="bright">
					<input id="A_contacterids" type="hidden" disabled="disabled" />
				</div>
			</div>
			<div style="width: 100%; height: 30px;">
				<div class="bbleft" style="float: left; font-size: 15px;">联&nbsp系&nbsp人：</div>
				<div class="bbright" style="float: left; line-height: 20px;">
					<label><input id="A_contactername" type="text" disabled="disabled"
						style="margin: auto; font-size: 15px; color: black; height: 20px; width: 100%;" />
				</div>
				</label>
			</div>
			<div style="width: 100%; height: 30px;">
				<div class="bbleft" style="float: left; font-size: 15px;">咨询科目：</div>
				<div class="bbright" style="float: left; line-height: 20px;">
					<label><input id="A_subjectname" type="text" disabled="disabled"
						style="margin: auto; font-size: 15px; color: black; height: 20px; width: 100%;" />
				</div>
				</label>
			</div>
			<div style="margin-top: 25px;">
				<div class="bleft">补充内容：</div>
				<div class="bright">
					<textarea id="A_content" style="background-color: #C9DCE9;"></textarea>
				</div>
			</div>
		</div>
		<div id="wancheng" style="overflow: hidden;">
			<div class="cx" id='additionalInfo'>
				<input class="cxys" type="button" value="完成" />
			</div>
		</div>
	</div>
	<footer style="margin-top: 15px; margin-bottom: 5px">
		<p align="center">&copy; ${company }</p>
	</footer>
</body>
</html>