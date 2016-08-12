<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>添加网校学生账号</title>
		<jsp:include page="../basecss.jsp" />
		<style type="text/css">
			
			.noneContract {
			    border-radius: 10px;
			    clear: both;
			    display: block;
			    font-size: 16px;
			    font-weight: normal;
			    max-width: 350px;
			    overflow: hidden;
			    padding: 9px 20px 9px 10px;
			}
			.mxfa {
				font-size: 20px !important;
				cursor: pointer;
			}
			.p-f-5 {
				padding-left : 5px;
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
								   &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;网校学员&nbsp;&gt;&nbsp;新建网校账号
							    </h5>
							    <a href="/netstudentaccount/netStudentList" class="btn btn-xs btn-danger frt m-l-lg " >返回</a>
			          			<div style="clear:both"></div>
							</div>
							<div class="ibox-content" >
								<form action="/netstudentaccount/saveBatchNetStudent" method="post" id="batchNetStudentAddForm" >
									<div style="margin-left: 40px;" >
										<div class="noneContract" > 填写账号名称  </div>
										<div id="netstudentlist" style="padding-left: 45px;" class="m-t-md m-b-lg" >
											<div class="" >
												<label class="m-r" > 1 、</label>
												<input type="text" class="p-f-5 input-s accounts " onblur="checkAccountExist( this.value )"
													name="accountStudent_1"  />
												<input type="hidden" value="1" name="order"  >
											</div>
											<span id="plusFa" class="fa fa-plus mxfa m-t-sm" onclick="plusOrder()" ></span>
										</div> 
										<input class="btn btn-sm btn-primary" value="保存" type="button" onclick="submitBatchAdd()" />
									</div>
								</form>
								<div style="clear:both;"></div>
								
							</div>
						</div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>	  
		</div>  
		<jsp:include page="../basejs.jsp" />
		
		<script type="text/javascript">
			
			function checkAccountExist( accountName ) {
				$.ajax( {
					url : "/netstudentaccount/checkAccountNameExist",
					data : { "accountName" : accountName },
					dataType : "json",
					type : "post",
					async : false,
					success : function( result ) {
						if( result ) {
							layer.msg( "当前账号名称不可用" , 2 , 2 );
						}
					}
				} );
			}
		
			function plusOrder() {
				var lastOrder = $( "#netstudentlist" ).find( "input[name='order']" ).last().val();
				var currentOrder = Number( lastOrder ) + 1 ;
				var payStr = "<div class='m-t-md'>";
				payStr += "<label class='m-r'> <span id='labelOrder'>" + currentOrder + " </span> 、</label>";
				payStr += "<input type='text' class='p-f-5 input-s accounts ' name='accountStudent_" + currentOrder + "' />";
				payStr += "<input type='hidden' value='" + currentOrder + "' name='order' />";
				payStr += "<span class='fa fa-minus-circle m-l mxfa' onclick='removeAccountOrder( this )' ></span></div>";
				
				$( "#plusFa" ).before( payStr );
			}
			
			
			function removeAccountOrder( domObj ) {
				var lastOrder = $( "#netstudentlist" ).find( "input[name='order']" ).last().val();
				var currentOrder = $( domObj ).closest( "div" ).find( "input:last" ).val();
				$( domObj ).closest( "div" ).remove();
				if( lastOrder > currentOrder ) {
					$( "#netstudentlist input[name='order']" ).each( function() {
						var checkOrder = $( this ).val();
						if( checkOrder > currentOrder ) {
							var newOrder = Number(checkOrder) - 1 ;
							$( this ).val( newOrder );
							$( this ).prev().attr( "name" , "accountStudent_" + newOrder );
							$( this ).closest( "div" ).find( "#labelOrder" ).text( newOrder );
						}
					} );
				}
			}
			
			
			function submitBatchAdd() {
				
				var accounts = "";
				$( ".accounts" ).each( function() {
					accounts += $( this ).val().trim();
				} );
				
				if( accounts == "" || accounts.length == 0  ) {
					layer.msg( "没有填写账号" , 2 , 2 );
					return false;
				}
				
				var submitLayer = layer.load( 0 );
				$.ajax( {
					url : "/netstudentaccount/saveBatchNetStudent",
					data : $( "#batchNetStudentAddForm" ).serialize(),
					dataType : "json",
					ype : "post",
					async: false,
					success : function( result ) {
						if( result.flag ) {
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
													layer.close( submitLayer );
													window.location.href="/netstudentaccount/netStudentList";
												}
											} )
										}
									} );
								}
							} );
						} else {
							layer.close( submitLayer );
						}
					}
				} );
			}
			
		
		</script>
		
	
	</body>
</html>