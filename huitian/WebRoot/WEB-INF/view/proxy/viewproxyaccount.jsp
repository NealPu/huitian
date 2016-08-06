<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="../basecss.jsp" />
		
		<script src="/js/js/jquery-2.1.1.min.js"></script>
	</head>
	<body style="background-color: #fff" >
		<div class="ibox-content" >
			<div style="margin-left: 100px; max-width: 400px; " >
				<form action="" id="viewOpenAccountForm" method="post" >
					<div class="m-t-sm" >
						<label class="input-s-xs" >代理</label>
						<input type="text" class="input-s-lg" readonly="readonly" 
							 value="${proxy.type eq '1' ? proxyAccount.companyname : proxyAccount.personname }" />
					</div>
					<div class="m-t-md" >
						<label class="input-s-xs" >登陆邮箱</label>
						<input type="text" class="input-s-lg" readonly="readonly" value="${proxyAccount.loginaccount }" />
					</div>
					<div class="m-t-md" >
						<input type="button" value="修改密码" onclick="resetPassword( ${proxyAccount.sysaccountid} )" class="btn btn-sm btn-warning" />
						<c:choose>
							<c:when test="${proxyAccount.accountState == 1 }">
								<input type="button" value="续用账号" onclick="freezeAccount( ${proxyAccount.sysaccountid} , 0 )" class="btn btn-sm btn-danger" />
							</c:when>
							<c:otherwise>
								<input type="button" value="停用账号" onclick="freezeAccount( ${proxyAccount.sysaccountid} , 1 )" class="btn btn-sm btn-danger" />
							</c:otherwise>
						</c:choose>
					</div>
				</form> 
			</div>
		</div>
		
		<jsp:include page="../basejs.jsp" />
		<script type="text/javascript">
			
			function resetPassword( sysAccountId ) {
				layer.prompt( { title: '修改密码' , type: 1 , length: 200 }, function( val , index ) {
		    		$.ajax( {
		    			url: "${cxt}/sysuser/changePassword",
		    			type: "post",
		    			data: { "id" : sysAccountId , "password" : val },
		    			dataType: "json" ,
		    			success: function( data ) {
		    				if( data.result ) {
		    					 layer.msg('密码修改成功！', 1, 1);
		    					 layer.close(index)
		    				}else{
		    					alert("密码修改失败！");
		    				}
		    			}
		    		});
				});
			}
			
			var index = parent.layer.getFrameIndex(window.name);
			function freezeAccount( sysuserId , state ) {
				layer.load( 0 );
				if( confirm('确认要暂停/恢复该用户账号吗？') ) {
					$.ajax( {
						url: "/sysuser/freeze",
						type: "post",
						data: { "sysuserId" : sysuserId , "state" : state },
						dataType: "json",
						success: function( result ) {
							if(result.result=="true" ) {
								parent.viewAccount( '${proxyAccount.id}' );
								parent.layer.close( index );
							} else {
								alert( result.result );
							}
						}
					} );
				}
			}
		
		</script>
		
	</body>
</html>