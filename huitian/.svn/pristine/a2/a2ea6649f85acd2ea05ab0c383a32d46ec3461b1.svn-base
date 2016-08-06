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
</head>
<body>
	<div class="wrapper" id="chengdan">
		<header class="header"> 成单情况 </header>
		<div class="main">
			<div class="tab_width">
				<div class="tab">
					<table>
						<tr>
							<th rowspan="2">${_res.get('student')}</th>
							<th rowspan="2">${_res.get('total.sessions')}</th>
							<th colspan="3">课时消耗</th>
						</tr>
						<tr>
							<th>上周</th>
							<th>本周</th>
							<th>累计</th>
						</tr>
						<c:choose>
							<c:when test="${empty studentList }">
								<tr class="bg1" height="25px">
									<td colspan="5">您还没有成单</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${studentList }" var="student">
									<tr class="bg1" height="25px">
										<td>${student.real_name}</td>
										<td>${student.jieshutotal}</td>
										<td>${student.szks}</td>
										<td>${student.bzks}</td>
										<td>${student.ljks}</td>
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
		<p align="center">&copy; ${company }</p>
	</footer>
</body>
</html>