<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>查看代理</title>
		<jsp:include page="../basecss.jsp" />
		<style type="text/css">
			#proxyBaseMsg {
				background-color: #fbfbfb;
			    border: 1px dashed #ccc;
			    margin-left: 40px;
			    max-width: 820px;
			    padding: 0 0 20px 20px;
			}
			input[ type="text" ] ,textarea {
				padding-left: 4px;
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
					<div  class="col-lg-12">
						<div class="ibox float-e-margins">
			    			<div class="ibox-title">
								<h5>
								   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
								   <a href="javascript:window.parent.location='/account'">首页</a>&nbsp;&nbsp;
								   &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理列表&nbsp;&gt;&nbsp;查看代理
							    </h5>
							    <a href="/proxy/list" class="btn btn-xs btn-danger frt m-l-xs " >返回</a>
							    <a href="/proxy/contract/proxyContract/${proxy.id }" class="btn btn-xs btn-success frt m-l-xs" >合同</a>
							    <a href="/proxy/cooperationProject/${proxy.id }" class="btn btn-xs btn-primary frt m-l-xs" >合作项目</a>
							    <a href="/proxy/editProxy/${proxy.id }" class="btn btn-xs btn-info frt m-l-xs" >编辑</a>
			          			<div style="clear:both"></div>
							</div>
							<div class="ibox-content" >
								<form action="/proxy/viewProxy/${proxy.id }" method="post" id="viewProxyForm" >
									<div id="proxyBaseMsg" >
										<div class="m-t-sm flt " >
											<label class="input-s-xs" >创建人</label>
											<input type="text" name="proxy.createdate" class="input-s" value="${proxy.createName }" />
										</div>
										<div class="m-t-sm flt m-l-xl"  >
											<label class="input-s-xs" >创建日期 </label>
											<input type="text" name="proxy.createdate" class="input-s" 
												value='<fmt:formatDate value="${proxy.createdate }" pattern="yyyy-MM-dd" />' />
										</div>
										<div class="clear:both;" ></div>
										<div class="m-t-md flt " >
											<label class="input-s-xs" >类型</label>
											<input type="radio" name="proxy.type" value="0" ${proxy.type ne '1' ? 'checked="checked"' : '' } /> 个人
											<input type="radio" name="proxy.type" value="1" ${proxy.type eq '1' ? 'checked="checked"' : '' } /> 机构
										</div>
										<div class="m-t-md flt " style="margin-left: 156px;" >
											<label class="input-s-xs" >
												<span class="person" >姓名</span>
												<span class="company" >负责人</span>
											</label>
											<input type="text" name="proxy.personname" class="input-s" value="${proxy.personname }" />
										</div>
										<div class="clear:both;" ></div>
										<div class="m-t-md flt " >
											<label class="input-s-xs" >
												<span class="person" >联系电话</span>
												<span class="company" >负责人电话</span>
											</label>
											<input type="text" name="proxy.tel" class="input-s" value="${proxy.tel }" maxlength="15" />
										</div>
										<div class="m-t-md flt m-l-xl" >
											<label class="input-s-xs" >身份证号</label>
											<input type="text" name="proxy.IDcard" class="input-s" value="${proxy.IDcard }" maxlength="18" />
										</div>
										<div class="clear:both;" ></div>
										<div class="m-t-md flt" >
											<label class="input-s-xs" >可用余额</label>
											<input type="text" class="input-s" value="${proxy.usableBalance }" />
										</div><br>
										<div class="clear:both;" ></div>
										<div class="m-t-md flt company " >
											<label class="input-s-xs" >所属公司</label>
											<input type="text" name="proxy.companyname" class="input-s" value="${proxy.companyname }" />
										</div>
										<div class="m-t-md flt m-l-xl company " >
											<label class="input-s-xs" >公司电话</label>
											<input type="text" name="proxy.companytel" class="input-s" maxlength="15" value="${proxy.companytel }" />
										</div>
										<div class="clear:both;" ></div><p>
										<div class="m-t-md flt commission " >
											<label class="input-s-xs" >服务专员</label>
											<input type="text" name="proxy.commissioner" class="input-s" value="${proxy.commissioner }" />
										</div>
										<div class="m-t-md flt location " >
											<label class="input-s-xs" >所在地</label>
											<input type="text" name="proxy.location" class="input-s" value="${proxy.location }" />
										</div>
										<div class="clear:both;" ></div>
										<div class="m-t-md flt" >
											<label class="input-s-xs" >通讯地址</label>
											<input type="text" name="proxy.address" style="width: 450px;" value="${proxy.address }" />
										</div>
										<div style="clear:both;" ></div>
										<div class="m-t-md flt" >
											<label class="input-s-xs flt " >备注</label>
											<textarea rows="3" cols="83" name="proxy.remark" >${proxy.remark }</textarea>
										</div>
										<div style="clear:both;" ></div>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>	  
		</div>  
		<jsp:include page="../basejs.jsp" />
		<script type="text/javascript">
			
			$( document ).ready( function() {
				typeValChange();
				$( "input[name='proxy.type']" ).change( function() {
					typeValChange();
				} );
				
				disabledForm( "viewProxyForm" );
			} );
			
			function typeValChange() {
				var typeVal = $( "input[name='proxy.type']:checked" ).val();
				if( typeVal == "0" ) {
					$( ".person" ).show();
					$( ".company" ).hide();
					$( ".commission" ).addClass( "m-l-xl" );
				}
				if( typeVal == "1" ) {
					$( ".person" ).hide();
					$( ".company" ).show();
					$( ".location" ).addClass( "m-l-xl" );
				}
			}
			
		</script>
	
	</body>
</html>