<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>网校学生账号</title>
		<jsp:include page="../basecss.jsp" />
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
					<form action="/netstudentaccount/netStudentList" method="post" id="searchForm" >
						<div  class="col-lg-12">
							<div class="ibox float-e-margins">
				    			<div class="ibox-title" >
									<h5>
									   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
									   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
									    &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;网校学生
								    </h5>
				          			<div style="clear:both"></div>
								</div>
								<div class="ibox-content" style="padding-top: 0px;" >
									<div class="flt m-t-sm m-l">
										<label>姓名</label>
										<input type="text" value="${paramMap['_query.studentaccount'] }" name="_query.studentaccount" class="input-s" />
									</div>
									
									<div class="flt m-t-sm m-l" >
										<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" 
											class="btn btn-outline btn-primary" />
										<c:if test="${operator_session.qx_netstudentaccountaddNetStudent }">
											<a href="/netstudentaccount/addNetStudent" class="btn btn-outline btn-success">
												${_res.get('teacher.group.add')}</a>
										</c:if>
									</div>
									<div style="clear:both;" ></div> 
								</div>
							</div>
						</div>
				
						<div class="col-lg-12" style="min-width:680px">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>网校学生账号</h5>
									<input type="button" onclick="manualPushStudentAccount()" value="同步学生账号" 
										class="btn btn-xs btn-primary frt m-r-md " />
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered" width="100%">
										<thead>
											<tr>
												<th > 序号 </th>
												<th > 学生账号 </th>
												<th > 添加日期 </th>
												<th > 发放状态 </th>
												<th > 操作 </th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${splitPage.page.list }" var="account" varStatus="index" >
												<tr>
													<td>${index.count }</td>
													<td>${account.netstudentname }</td>
													<td><fmt:formatDate value="${account.createdate }" pattern="yyyy-MM-dd" /></td>
													<td>${account.grantstate eq '0' ? "未发放" : "已发放" }</td>
													<td>
														<c:if test="${account.grantstate eq '0' && account.netstudentid != null }">
															<!-- 发放的时候选择项目课程会根据发放者受到限制 -->
															<a class="btn btn-xs btn-danger" href="/netstudentaccount/grantAccount/${account.id }" >发放</a>
														</c:if>
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
			
			function manualPushStudentAccount() {
				var pushIndex = layer.load( 0 );
				$.ajax( {
					url : "/netstudentaccount/pushNetStudentAccount",
					dataType : "json",
					success:function( pushDatas ) {
						console.log( pushDatas );
						$.ajax( {
							url : "http:/..../....",
							data : pushDatas,
							dataType : "json",
							type : "post",
							async: false,
							success : function( pushResult ) {
								$.ajax( {
									url : "/netstudentaccount/netStudentPushResult",
									data : pushResult,
									dataType : "json",
									type : "post",
									async: false,
									success : function( result ) {
										layer.close( pushIndex );
										$( "#searchForm" ).submit();
									}
								} )
							}
						} );
					}
				} );
			}
		
		
		
		</script>
	
	</body>
</html>