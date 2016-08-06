<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>新建代理</title>
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
					<div  class="col-lg-12">
						<div class="ibox float-e-margins">
			    			<div class="ibox-title">
								<h5>
								   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
								   <a href="javascript:window.parent.location='/account'">首页</a>&nbsp;&nbsp;
								   &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理列表&nbsp;&gt;&nbsp;新建代理
							    </h5>
							    <a href="/proxy/list" class="btn btn-xs btn-danger frt m-l-lg " >返回</a>
			          			<div style="clear:both"></div>
							</div>
							<div class="ibox-content" >
								<form action="/proxy/save" method="post" id="newProxyForm" >
									<input type="hidden" value="${proxy.id }" name="proxy.id" />
									<div style="margin-left: 40px;" >
										<div class="m-t-sm" >
											<label class="input-s-sm" >类型</label>
											<input type="radio" name="proxy.type" value="0" ${proxy.type ne '1' ? 'checked="checked"' : '' } /> 个人
											<input type="radio" name="proxy.type" value="1" ${proxy.type eq '1' ? 'checked="checked"' : '' } /> 机构
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm" >
												<span class="person" >姓名</span>
												<span class="company" >负责人</span>
											</label>
											<input type="text" name="proxy.personname" class="input-s" value="${proxy.personname }" />
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm" >
												<span class="person" >联系电话</span>
												<span class="company" >负责人电话</span>
											</label>
											<input type="text" name="proxy.tel" class="input-s" value="${proxy.tel }" maxlength="15" />
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm" >身份证号</label>
											<input type="text" name="proxy.IDcard" class="input-s" value="${proxy.IDcard }" maxlength="18" />
										</div>
										<div class="m-t-md company " >
											<label class="input-s-sm" >所属公司</label>
											<input type="text" name="proxy.companyname" class="input-s" value="${proxy.companyname }" />
										</div>
										<div class="m-t-md company " >
											<label class="input-s-sm" >公司电话</label>
											<input type="text" name="proxy.companytel" class="input-s" maxlength="15" value="${proxy.companytel }" />
										</div>
										<div class="m-t-md " >
											<label class="input-s-sm" >通讯地址</label>
											<input type="text" name="proxy.address" class="input-s" value="${proxy.address }" />
										</div>
										<div class="m-t-md " >
											<label class="input-s-sm" >所在地</label>
											<input type="text" name="proxy.location" class="input-s" value="${proxy.location }" />
										</div>
										<div class="m-t-md " >
											<label class="input-s-sm" >服务专员</label>
											<input type="text" name="proxy.commissioner" class="input-s" value="${proxy.commissioner }" />
										</div>
										<div style="clear:both;" ></div>
										<div class="m-t-md " >
											<label class="input-s-sm flt " >备注</label>
											<textarea rows="5" cols="83" name="proxy.remark" >${proxy.remark }</textarea>
										</div>
										<div style="clear:both;" ></div>
										<div class="m-t-md">
											<input type="button" onclick="submitProxy()" value="提交" class="btn btn-primary btn-sm " />
										</div>
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
			} );
			
			function typeValChange() {
				var typeVal = $( "input[name='proxy.type']:checked" ).val();
				if( typeVal == "0" ) {
					$( ".person" ).show();
					$( ".company" ).hide();
				}
				if( typeVal == "1" ) {
					$( ".person" ).hide();
					$( ".company" ).show();
				}
			}
			
			function submitProxy(){
				if( confirm( "确定要提交代理信息吗？" ) ){
					var loadIndex = layer.load(0);
					$.ajax( {
						url : "/proxy/saveProxy",
						type : "post",
						data : $( "#newProxyForm" ).serialize(),
						dataType : "json",
						success : function( result ) {
							if( result.flag ) {
								window.location.href="/proxy/list";
							} else {
								layer.msg( result.msg , 2 , 2 );
							}
							layer.close( loadIndex );
						}
					} );
				}
			}
			
		
		</script>
	
	</body>
</html>