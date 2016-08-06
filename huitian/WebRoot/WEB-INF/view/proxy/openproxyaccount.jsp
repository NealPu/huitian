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
				<form action="" id="openAccountForm" method="post" >
					<input type="hidden" name="proxyId" value="${proxyId }"  />
					<div class="m-t-sm" >
						<label class="input-s-xs" >代理</label><span>${proxyInfo.type eq '0' ? proxyInfo.personname : proxyInfo.companyname }</span>
					</div>
					<div class="m-t-md" >
						<label class="input-s-xs" >邮箱</label>
						<input type="text" class="input-s-lg" name="sysUser.email" id="accountMail" onblur="checkAccountMailExist( this.value )" />
					</div>
					<div class="m-t-md" >
						<label class="input-s-xs" >账号名</label>
						<input type="text" class="input-s-lg" name="sysUser.REAL_NAME" id="accountName" />
					</div>
					<div class="m-t-md" >
						<input type="button" value="提交" onclick="submitForm()" class="btn btn-sm btn-primary" />
					</div>
				</form>
			</div>
		</div>
		
		<jsp:include page="../basejs.jsp" />
		<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
		<script type="text/javascript">
			$( document ).ready( function() {
				
				$("#openAccountForm").validate( {
					rules: {
						'sysUser.email' : {
							required : true,
						 	email : true
						} ,
						'sysUser.REAL_NAME' : "required"
				    },
			        submitHandler:function( form ) {
			        	if( checkAccountMailExist( $( "#accountMail" ).val() ) ) {
				        	if( confirm( "确定要开通账号吗？" ) ) {
					        	var loadIndex = layer.load(0);
						        $.ajax( { 
						        	url : "/proxy/saveProxySysAccount",
						        	type : "post",
						        	data : $( "#openAccountForm" ).serialize(),
						        	dataType : "json",
						        	async : false,
						        	success : function( result ) {
						        		if( result.flag ) {
						        			parent.window.location.href = "/proxy/list";
						        		} else {
						        			layer.msg( result.msg , 2 , 2 );  
						        		}
						        		layer.close( loadIndex );
						        	}
						        } );
				        	}
			        	}
					}    
				});
			} );
			
			function submitForm() {
				$( "#openAccountForm" ).submit();
			}
			
			
			function checkAccountMailExist( accountMail ) {
				console.log( "check" );
			    if ( accountMail != "") {
			    	var flag = true;
			        $.ajax( {
			            url: '${cxt}/sysuser/checkExist',
			            type: 'post',
			            data: {
			                'checkField': "email",
			                'checkValue': accountMail,
			                'id': ""
			            },
			            async: false,
			            dataType: 'json',
			            success: function( data ) {
			                if ( data.result >= 1 ) {
			                	$( "#accountName" ).focus();
			                	layer.msg( "您填写的登陆邮箱已存在。" , 2 , 2 );
			                	flag = false;
			                } else {
			                	flag = true;
			                }
			            }
			        } );
			        return flag;
			    } else {
			        return false;
			    }
			}
		
		</script>
		
	</body>
</html>