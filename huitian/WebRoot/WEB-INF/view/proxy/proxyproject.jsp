<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>项目列表</title>
		<jsp:include page="../basecss.jsp" />
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
					<form action="/proxy/cooperationProject/${proxy.id }" method="post" id="searchForm" >
						<div  class="col-lg-12">
							<div class="ibox float-e-margins">
				    			<div class="ibox-title" >
									<h5>
									   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
									   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
									    &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理项目
								    </h5>
								    <a href="/proxy/list" class="btn btn-xs btn-danger frt m-r-md" >返回</a>
				          			<div style="clear:both"></div>
								</div>
								<div class="ibox-content" style="padding-top: 0px;" >
									<div class="flt m-t-sm m-l">
										<label>项目名称</label>
										<input type="text" value="${paramMap['_query.projectName'] }" name="_query.projectName" class="input-s" />
									</div>
									
									<div class="flt m-t-sm m-l" >
										<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" 
											class="btn btn-outline btn-primary">
									</div>
									
									<div style="clear:both;" ></div>
								</div>
							</div>
						</div>
				
						<div class="col-lg-12" style="min-width:680px">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>合作记录</h5>
									<a href="/proxy/uncooperationProject/${proxy.id }" class="btn btn-xs btn-primary frt m-r-lg"  >未合作项目</a>
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered" width="100%">
										<thead>
											<tr>
												<th >序号</th>
												<th >合作项目</th>
												<th >添加日期</th>
												<th >合作日期</th>
												<th >状态</th>
												<th >操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${splitPage.page.list }" var="project" varStatus="index" >
												<tr>
													<td>${index.count }</td>
													<td>${project.projectname }</td>
													<td><fmt:formatDate value="${project.createdate }" pattern="yyyy-MM-dd" /> </td>
													<td>
														<c:choose>
															<c:when test="${project.startdate eq '1900-01-01' && project.endate eq '9999-12-31' }"> 永久 </c:when>
															<c:otherwise>${project.startdate } - ${project.enddate }</c:otherwise>
														</c:choose>
													</td>
													<td>${project.state eq '0' ? '正常' : '结束' }</td>
													<td>
														<c:if test="${project.state eq '0' }">
															<a onclick="endingCooperation(${project.id})" class="btn btn-xs btn-primary" > 停用 </a>
														</c:if>
														<a href="javascript:void(0)" class="btn btn-xs btn-info" > 查看 </a>
													</td>
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
		
		<jsp:include page="../basejs.jsp" />
		<script type="text/javascript">
			
			function endingCooperation(  cooperationId ) {
				if( confirm( "确定要结束合作吗？" ) ){
					$.ajax( {
						url : "/proxy/endingCooperation",
						data : { "cooperationId" : cooperationId },
						dataType : "json",
						type : "post",
						success : function( result ) {
							if( result.flag ) {
								$( "#searchForm" ).submit();
							} else {
								layer.msg( result.msg , 2 , 2 );
							}
						}
					} );
				}
				
			}
	
		</script>
	
	</body>
</html>