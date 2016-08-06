<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<!-- saved from url=(0060)http://wap.koudaitong.com/v2/showcase/feature?alias=a0p3u61u -->
<html class="no-js " lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="format-detection" content="telephone=no">
<meta http-equiv="cleartype" content="on">
<title>${company }</title>

<!-- meta viewport -->
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- CSS -->
<link rel="stylesheet" type="text/css" href="${cxt}/static/css/base.css">
<link rel="stylesheet" type="text/css" href="${cxt}/static/css/showcase.css">
<style>
body {
	background-color: #f9f9f9;
}
</style>
</head>

<body class=" ">
	<!-- container -->
	<div class="container ">
		<div class="content js-mini-height">
			<div class="content-body">
				<!-- 标题 -->
				<div>
					<div class="custom-title  text-left">
						<h2 class="title">推荐规则</h2>
						<p class="sub_title">2014-09-15 ${company }</p>
					</div>
				</div>

				<!-- 富文本内容区域 -->
				<div class="custom-richtext">
					<p>
						<span style="color: rgb(192, 0, 0);">一、推荐学生佣金和返佣时间？</span><br>
					</p>
					<p>1、佣金=学生消耗金额x返佣比例。</p>
					<p>2、返佣时间为每月1日。</p>
					<p>
						<span style="color: rgb(192, 0, 0);">二、如何推荐咨询客户？</span>
					</p>
					<p>打开${gzhname }微信服务号。</p>
					<p>
						第一步，点击“<span style="color: rgb(192, 0, 0);">推荐赚</span>”菜单；
					</p>
					<p>
						第二步，选择其中的“<span style="color: rgb(192, 0, 0);">我要推荐</span>”进入推荐页面；
					</p>
					<p>第三步，填写咨询人的基本信息提交即可。</p>
					<p>提交后我们的系统会收到你的提交请求，并会发短信通知我们的课程顾问进行跟进。</p>
					<p>
						<span style="color: rgb(192, 0, 0);">三、如何查看推荐的进展情况？</span>
					</p>
					<p>打开${gzhname }微信服务号。</p>
					<p>
						第一步，点击“<span style="color: rgb(192, 0, 0);">推荐赚</span>”菜单；
					</p>
					<p>
						第二步，选择其中的“<span style="color: rgb(192, 0, 0);">跟单情况</span>”进入跟单页面查看推荐情况。
					</p>
					<p>课程顾问跟进后，会提交跟进的反馈信息，系统同时会发送短信通知您有新的跟进进展，您打开微信查看即可。</p>
					<p>
						<span style="color: rgb(192, 0, 0);">四、如何查看成单情况？</span><br>
					</p>
					<p>打开${gzhname }微信服务号。</p>
					<p>
						第一步，点击“<span style="color: rgb(192, 0, 0);">推荐赚</span>”菜单；
					</p>
					<p>
						第二步，选择其中的“<span style="color: rgb(192, 0, 0);">成单情况</span>”进入成单页面查看学生消耗情况。
					</p>
					<p>
						<span style="color: rgb(192, 0, 0);">五、如何查看推荐返佣情况？</span>
					</p>
					<p>打开${gzhname }微信服务号。</p>
					<p>
						第一步，点击“<span style="color: rgb(192, 0, 0);">账户</span>”菜单；
					</p>
					<p>
						第二步，选择其中的“<span style="color: rgb(192, 0, 0);">推荐佣金</span>”进入推荐佣金页面查看返佣情况和明细。
					</p>
					<p>
						<span style="color: rgb(192, 0, 0);">六、如何查看我的基本信息和银行卡信息？</span>
					</p>
					<p>打开${gzhname }微信服务号。</p>
					<p>
						第一步，点击“<span style="color: rgb(192, 0, 0);">账户</span>”菜单；
					</p>
					<p>
						第二步，选择其中的“<span style="color: rgb(192, 0, 0);">我的信息</span>”查看和修改银行卡信息。
					</p>
					<p>
						<span style="color: rgb(192, 0, 0);">七、联系我们</span>
					</p>
					<p>如果有疑问，请联系客服：${kfemail }</p>
					<p>最终解释权归${company }所有</p>
					<p style="text-align: right;">${company }</p>
				</div>

			</div>


		</div>
		<div class="js-footer" style="min-height: 1px;">
			<div class="footer">
				<div class="footer">
					<div class="copyright">
						<div class="ft-copyright">
							由&nbsp;<a href="${website }" target="_blank">${company }</a>&nbsp;提供技术支持
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="motify">
		<div class="motify-inner"></div>
	</div>
</body>
</html>