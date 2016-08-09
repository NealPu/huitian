<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>项目列表</title>
		<jsp:include page="../../basecss.jsp" />
		<style type="text/css" >
			.leftalign { text-align: left; }
		</style>
		<script src="/js/js/jquery-2.1.1.min.js"></script>
	</head>
	<body>
		<div id="wrapper" style="background: #2f4050;min-width:1100px">
			<%@ include file="/common/left-nav.jsp"%>
			<div class="gray-bg dashbard-1" id="page-wrapper">
				<div class="row border-bottom">
					<nav class="navbar navbar-static-top fixtop" role="navigation">
					  <%@ include file="/common/top-index.jsp"%>
					</nav>
				</div>
			
				<div class="margin-nav" style="width:100%;">	
					<form action="/proxy/proxyproject/projectList" method="post" id="searchForm" >
						<div  class="col-lg-12">
							<div class="ibox float-e-margins">
				    			<div class="ibox-title" >
									<h5>
									   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
									   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
									    &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;合作项目
								    </h5>
				          			<div style="clear:both"></div>
								</div>
								<div class="ibox-content" style="padding-top: 0px;" >
									<div class="flt m-t-sm m-l">
										<label>项目名称</label>
										<input type="text" value="${paramMap['_query.projectname'] }" name="_query.projectname" class="input-s" />
									</div>
									
									<div class="flt m-t-sm m-l" >
										<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" 
											class="btn btn-outline btn-primary" />
									</div>
									<div style="clear:both;" ></div> 
								</div>
							</div>
						</div>
				
						<div class="col-lg-12" style="min-width:680px">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>项目列表</h5>
									<c:if test="${secondAgents }" >
										<a href="/proxy/proxyproject/notOpenedProject" class="btn btn-xs btn-info frt m-r-md " >未开放</a></c:if>
									<c:if test="${secondAgents || firstAgents }" >
										<a href="/proxy/proxyproject/notCooperationList" class="btn btn-xs btn-primary frt m-r-md" >未合作</a></c:if>
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered" width="100%">
										<thead>
											<tr>
												<th >序号</th>
												<th >名称</th>
												<th >状态</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${splitPage.page.list }" var="project" varStatus="index" >
												<tr>
													<td>${index.count }</td>
													<td>${project.projectname} </td>
													<td>${project.state eq '0' ? '正常' : '取消' } </td>
												</tr>
											</c:forEach>
										</tbody>
									</table>    
									<div id="splitPageDiv">
										<jsp:include page="/common/splitPage.jsp" />
									</div>
								</div>
							</div>
						</div>
						<div style="clear:both;"></div>
					</form>
				</div>
			</div>	  
		</div>  
		
		<jsp:include page="../../basejs.jsp" />
		<script type="text/javascript">
			
		</script>
	
	</body>
</html>