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
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>${company }</title>
<link rel="stylesheet" type="text/css" href="${cxt!}/static/css/common.css">
<script type="text/javascript">
	var cxt = "${cxt!}";
	var showid = "${showinfo!}";
</script>
<script src="${cxt!}/static/js/Crow5.sjs.js"></script>
</head>
<body>
	<div class="wrapper" id="chengdan">
		<header class="header"> 推荐佣金 </header>
		<div class="main">
			<div class="tab_width">
				<div class="tab">
					<table>
						<tr>
							<th rowspan="2">${_res.get('student')}</th>
							<th colspan="2">累计消耗</th>
							<th colspan="2">上周消耗</th>
						</tr>
						<tr>
							<th>${_res.get('session')}/元</th>
							<th>佣金(元)</th>
							<th>${_res.get('session')}/元</th>
							<th>佣金(元)</th>
						</tr>
						<c:choose>
					<c:when test="${empty studentList }">
						<tr class="bg1" height="25px">
							<td colspan="5">您还没有成单</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${studentList}" var="student">
						<tr class="bg1" height="25px">
							<td>${student.studentname!}</td>
							<td>${student.ljks!}/${student.ljxh!}</td>
							<td>${student.ljyj!}</td>
							<td>${student.szks!}/${student.szxh!}</td>
							<td>${student.szyj!}</td>
							</tr>
						</c:forEach>
					</c:otherwise>
					</c:choose>
					</table>
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