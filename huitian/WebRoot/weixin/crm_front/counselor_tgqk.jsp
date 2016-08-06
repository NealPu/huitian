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
<script src="${cxt}/static/js/Crow5.sjs.js"></script>
</head>
<body>
	<div class="wrapper" id="chengdan">
		<header class="header"> 推广情况 </header>
		<div class="main">
			<div class="tab_width">
				<div class="tab">
				<c:choose>
					<c:when test="${empty counselor.spread }">
						您还没有推广用户，赶快推广吧！
					</c:when>
					<c:otherwise>
						<c:forEach items="${counselor.spread }" var="spread">
							<c:choose>
								<c:when test="${spread.key eq 'LV1' }">
									<table>
									<caption align="top">LV1账户情况</caption>
									<tr>
										<td>${_res.get("sysname")}</td>
										<td>注册日期</td>
										<td>推广人数</td>
										<td>贡献佣金</td>
									</tr>
									<c:forEach items="${spread.value.spreaders }" var="spreader">
										<tr class="bg1" height="25px">
											<td>${spreader.realname}</td>
											<td><fmt:formatDate value="${spreader.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
											<td>${spreader.tuiguang }</td>
											<td>${spreader.yongjin }</td>
										</tr>
									</c:forEach>
									</table>
								</c:when>
								<c:otherwise>
									<table>
										<caption align="top">${spread.key}账户情况</caption>
										<tr>
										<td>推广人数</td>
										<td>推荐人数</td>
										<td>成单人数</td>
										<td>贡献佣金</td>
										</tr>
										<tr class="bg1" height="25px">
											<td>${spread.value.total}</td>
											<td>${spread.value.ztjs}</td>
											<td>${spread.value.zcds}</td>
											<td>${spread.value.zyj}</td>
										</tr>
									</table>		
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</div>
	</div>
	<footer style="margin-top: 15px; margin-bottom: 5px">
		<p align="center">&copy;
			${company}
		</p>
	</footer>
</body>
</html>