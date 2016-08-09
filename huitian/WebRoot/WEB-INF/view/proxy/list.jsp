<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>代理列表</title>
		<jsp:include page="../basecss.jsp" />
		<style type="text/css" >
			td { text-align: center; }
			th { cursor: pointer; }
			.leftalign { text-align: left; }
			#uploadFilePage {
				display:none;
			}
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
					<form action="/proxy/list" method="post" id="searchForm" >
						<div  class="col-lg-12">
							<div class="ibox float-e-margins">
				    			<div class="ibox-title" >
									<h5>
									   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
									   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
									    &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理列表
								    </h5>
				          			<div style="clear:both"></div>
								</div>
								<div class="ibox-content" style="padding-top: 0px;" >
									<div class="flt m-t-sm m-l">
										<label>姓名</label>
										<input type="text" value="${paramMap['_query.personname'] }" name="_query.personname" class="input-s-sm" />
									</div>
									<div class="flt m-t-sm m-l" >
										<label>所在地</label>
										<input type="text" value="${paramMap['_query.location'] }" name="_query.location" class="input-s-sm" />
									</div>
									
									<div class="flt m-t-sm m-l" >
										<label>创建日期</label>
										<input type="text" readonly="readonly"  value="${paramMap['_query.startdate'] }" id="startdate" 
											 name="_query.startdate" class="input-s-sm znzhong " /> - 
										<input type="text" readonly="readonly"  value="${paramMap['_query.enddate'] }" id="enddate" 
											 name="_query.enddate" class="input-s-sm znzhong " />
									</div>
										 
									<div class="flt m-t-sm m-l" >
										<label>创建人</label>
										<input type="text" value="${paramMap['_query.createname'] }" name="_query.createname" class="input-s-sm" />
									</div>
									
									<div class="flt m-t-sm m-l" >
										<label>服务专员</label>
										<input type="text" value="${paramMap['_query.commissioner'] }" name="_query.commissioner" class="input-s-sm" />
									</div>
									
									<div class="flt m-t-sm m-l" >
										<label>代理类型</label>
										<select class="chosen-select input-s-sm" name="_query.type" >
											<option value="" >全部</option>
											<option value="0" ${paramMap['_query.type'] eq '0' ? 'selected="selected"' : '' } >个人</option>
											<option value="1" ${paramMap['_query.type'] eq '1' ? 'selected="selected"' : '' } >机构</option>
										</select>
									</div>
									<input type="hidden" name="_query.searchType" value="${paramMap['_query.searchType'] }" />
									
									<div class="flt m-t-sm m-l" >
										<input type="button" onclick="research()" value="${_res.get('admin.common.select')}" 
											class="btn btn-outline btn-primary">
										<c:if test="${operator_session.qx_proxynewProxy }">
											<a href="/proxy/newProxy" class="btn btn-outline btn-success">${_res.get('teacher.group.add')}</a>
											<input type="button" id="batchImport" class="btn btn-outline btn-warning" 
					         					value='导入' onclick="batchimport()" />
										</c:if>
										<a onclick="exportProxy()" class="btn btn-outline btn-info">导出</a>
									</div>
									<div style="clear:both;" ></div> 
								</div>
							</div>
						</div>
				
						<div class="col-lg-12" style="min-width:680px">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>代理列表</h5>
								</div>
								<div class="ibox-content">
									<table class="table table-hover table-bordered" width="100%">
										<thead>
											<tr>
												<th onclick="orderbyFun( 'searchForm' , 'pro.createdate' )" >序号</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.type' )" >类型</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.personname' )" >名称</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.companyname' )" >机构名称</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.tel' )" >联系电话</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.location' )" >所在地</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.address' )"  >通讯地址</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.createdate' )" >创建日期</th>
												<th onclick="orderbyFun( 'searchForm' , 'sysuser.real_name' )"  >创建人</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.commissioner' )"  >服务专员</th>
												<th onclick="orderbyFun( 'searchForm' , 'pro.state' )" >状态</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${splitPage.page.list }" var="proxy" varStatus="index" >
												<tr>
													<td>${index.count }</td>
													<td>${proxy.type eq '0' ? '个人' : '机构' } </td>
													<td>${proxy.personname } </td>
													<td>${proxy.companyname } </td>
													<td>${proxy.type eq '0' ? proxy.tel : proxy.companytel } </td>
													<td class="leftalign" >${proxy.location } </td>
													<td class="leftalign" >${proxy.address } </td>
													<td><fmt:formatDate value="${proxy.createdate }" pattern="yyyy-MM-dd" /> </td>
													<td>${proxy.createname } </td>
													<td>${proxy.commissioner }</td>
													<td>${proxy.state eq '0' ? '正常' : '取消' } </td>
													<td>
														<c:if test="${ empty proxy.sysaccountid && proxy.state eq '0' }" >
															<a class="btn btn-xs btn-warning" onclick="openAccount( ${proxy.id } )" >开通账号</a>
														</c:if>
														<c:if test="${!empty proxy.sysaccountid }">
															<a class="btn btn-xs btn-warning" onclick="viewAccount( ${proxy.id } )" >账号</a>
														</c:if>
														<a class="btn btn-xs btn-info" href="/proxy/viewProxy/${proxy.id }" >查看</a>
														<a class="btn btn-xs btn-success" href="/proxy/editProxy/${proxy.id }" >编辑</a>
														<a class="btn btn-xs btn-primary" href="/proxy/cooperationProject/${proxy.id }" >项目</a>
														<a class="btn btn-xs btn-danger" href="/proxy/contract/proxyContract/${proxy.id }" >合同</a>
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
		<jsp:include page="batchimport.jsp" />
		
		<jsp:include page="../basejs.jsp" />
		<script type="text/javascript" src="/js/webuploader/ajaxfileupload.js" ></script>
		<script type="text/javascript">
			
			$( document ).ready( function() {
				var startDate = {
					elem : "#startdate",
					format : 'YYYY-MM-DD',
					istime : false,
					istoday : true,
					choose : function( date ) {
						endDate.min = date;
					}
				}
				var endDate = {
					elem : "#enddate",
					format : 'YYYY-MM-DD',
					istime : false,
					istoday : true,
					choose : function( date ) {
						startDate.max = date;
					}
				}
				laydate( startDate );
				laydate( endDate );
				
			} );
			
			/*   */
			function openAccount( proxyId ) {
				$.layer( {
		    	    type: 2,
		    	    shadeClose: false,
		    	    title: "开通账号",
		    	    closeBtn: [0, true],
		    	    shade: [0.5, '#000'],
		    	    offset:['50px', ''],
				    area: ['700px', '300px'],
		    	    iframe: {src: '${cxt}/proxy/openProxyAccount/' + proxyId }
		    	} );
			}
			
			function viewAccount( proxyId ) {
				$.layer( {
		    	    type: 2,
		    	    shadeClose: false,
		    	    title: "账号信息",
		    	    closeBtn: [0, true],
		    	    shade: [0.5, '#000'],
		    	    offset:['50px', ''],
				    area: ['700px', '300px'],
		    	    iframe: {src: '${cxt}/proxy/viewProxyAccount/' + proxyId }
		    	} );
			}
			
			/* 查询 */
			function research() {
				$( "#searchForm input[name='_query.searchType']" ).val( "" );
				search();
			}
			function exportProxy() {
				$( "#searchForm input[name='_query.searchType']" ).val( "1" );
				search();
			}
			
			
			var fileUploadLayer ;
			function batchimport() {
				fileUploadLayer =  $.layer( {
				    type : 1,
				    area : ['600px' , '230px'],
				    title : "代理信息导入" ,
				    page : { dom : '#uploadFilePage'  },
				    close : function() {
			    		$( "#progressBar" ).hide();
				    	$( "#propressBarView" ).attr( "style" , "width: 45%;" );
				    	$( "#searchForm" ).submit();
				    }
				} );
		    }
			    
		    function submitBatchImport() {
	    		$( "#progressBar" ).show();
	    		$( ".xubox_close" ).hide();
		    	$.ajaxFileUpload( {
					contentType : "multipart/form-data",
					url : "/proxy/importProxy",
					fileElementId : "fileField",
					type : "post",
					dataType : "json",
					success : function( result ) {
						var obj = $.parseJSON( result );
			    		$( ".xubox_close" ).show();
				    	$( "#propressBarView" ).attr( "style" , "width: 100%;" );
			    		$( "#fileField" ).val("");
						if ( obj.flag ) {
							layer.msg( obj.msg , 3 , 1 );
						} else {
							layer.msg( obj.msg , 3 , 2 );
						}
					},
				} );
			}
		    
		    function resetBarView() {
	    		$( "#progressBar" ).hide();
		    	$( "#propressBarView" ).attr( "style" , "width: 45%;" );
		    }
		    
		    function downloadTemplate() {
		    	$( "#templateDownloadForm" ).submit();
		    }
	
		</script>
	
	</body>
</html>