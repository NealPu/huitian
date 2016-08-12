<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>待回款记录</title>
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
					<form action="/proxy/finance/nearlestToPay" method="post" id="searchForm" >
						<div  class="col-lg-12">
							<div class="ibox float-e-margins">
				    			<div class="ibox-title" >
									<h5>
									   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
									   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
									    &gt;&nbsp;&nbsp;财务管理&nbsp;&gt;&nbsp;代理回款&nbsp;&gt;&nbsp;最近待回款
								    </h5>
				          			<div style="clear:both"></div>
								</div>
								<div class="ibox-content" style="padding-top: 0px;" >
									<div class="flt m-t-sm m-l">
										<label>代理</label>
										<input type="text" value="${paramMap['_query.proxyname'] }" name="_query.proxyname" class="input-s" />
									</div>
									<div class="flt m-t-sm m-l">
										<label>合同名称</label>
										<input type="text" value="${paramMap['_query.contractname'] }" name="_query.contractname" class="input-s" />
									</div>
									<div class="flt m-t-sm m-l">
										<label>合同编号</label>
										<input type="text" value="${paramMap['_query.contractcode'] }" name="_query.contractcode" class="input-s" />
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
									<h5>最近待回款</h5>
									<a href="/proxy/finance/repayment" class="btn btn-xs btn-info frt m-r-md " >代理回款记录</a>
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered" width="100%">
										<thead>
											<tr>
												<th >序号</th>
												<th >代理</th>
												<th >回款合同</th>
												<th >合同编号</th>
												<th >回款额</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${splitPage.page.list }" var="payment" varStatus="index" >
												<tr>
													<td>${index.count }</td>
													<td>${payment.type eq '0' ? payment.personname : payment.companyname }</td>
													<td>${payment.contractname }</td>
													<td>${payment.contractcode }</td>
													<td>${payment.amount }</td>
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