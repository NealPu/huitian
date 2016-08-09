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
					<form action="/proxy/uncooperationProject/${proxy.id }" method="post" id="searchForm" >
						<div  class="col-lg-12">
							<div class="ibox float-e-margins">
				    			<div class="ibox-title" >
									<h5>
									   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
									   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
									    &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理项目
								    </h5>
								    <a href="/proxy/cooperationProject/${proxy.id }" class="btn btn-xs btn-danger frt m-r-md" >返回</a>
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
									<h5>未合作项目 </h5>
									<a href="/proxy/cooperationProject/${proxy.id }" class="btn btn-xs btn-primary frt m-r-lg"  >合作项目</a>
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered" width="100%">
										<thead>
											<tr>
												<th >序号</th>
												<th >项目名称</th>
												<th >操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${splitPage.page.list }" var="project" varStatus="index" >
												<tr>
													<td>
														<input type="hidden" id="projectId" value="${project.id }" /> ${index.count }
													</td>
													<td name="projectName" >${project.projectname }</td>
													<td>
														<a onclick="sureCooperate( '${project.id}' , '${project.proId }' )" class="btn btn-xs btn-primary" > 
															合作 </a>
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
		<%-- <jsp:include page="layer_surecoperate.jsp"></jsp:include> --%>
		
		<jsp:include page="../basejs.jsp" />
		<script type="text/javascript">
			
			var proxyId = '${proxy.id}';
			
			/* var sureLayerPage;
			function sureCooperate( domObj ) {
				var projectName = $( domObj ).closest( "tr" ).find( "td[name='projectName']" ).text().trim();
				$( "#sureCooperationPage #sureProjectId" ).val( $( domObj ).closest( "tr" ).find( "#projectId" ).val() );
				$( "#sureCooperationPage #sureProjectName" ).html( projectName );
				
				sureLayerPage =  $.layer( {
				    type : 1,
				    offset : [ '50px' , '' ],
				    area : ['600px' , '250px'],
				    title : " 确定项目合作 " ,
				    page : { dom : '#sureCooperationPage'  },
				    close : function() {
				    	$( "#searchForm" ).submit();
				    }
				} );
			} */
			
			function sureCooperate( projectId , proId ) {
				$.layer({
		      		type:2,
		      		title: "确定项目合作",
		      		shadeClose: false,
		      		closeBtn:[0,true],
		      		shade:[0.5,'#000'],
				    offset : [ '50px' , '' ],
		      		area:[ '630px' ,'400px'],
		      		iframe: {src: '${cxt}/proxy/toAddProxyProject/'+ proxyId + "-" + projectId + "-" + proId }
		      	});
			}
			
			function research() {
				search();
			}
			
			
			
			
		</script>
	
	</body>
</html>