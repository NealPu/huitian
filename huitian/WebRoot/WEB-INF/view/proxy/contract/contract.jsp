<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<title>代理合同</title>
		<jsp:include page="../../basecss.jsp" />
		<style type="text/css">
			.flt { float: left; }
			.frt { float: right; }
			#newContractDiv {
				background-color: #fbfbfb;
			    border: 1px dashed #ccc;
			    margin-left: 40px;
			    max-width: 810px;
			    padding-left: 20px;
			    padding-right: 20px;
			}
			.mxfa {
				font-size: 20px !important;
				cursor: pointer;
			}
			.noneContract {
				background-color: #e9f5e6;
			    border-radius: 17px;
			    clear: both;
			    display: block;
			    font-size: 12px;
			    font-weight: normal;
			    line-height: 140%;
			    overflow: hidden;
			    padding: 9px 20px 9px 10px;
			    max-width: 910px;
			    text-align: center;
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
								   &gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理列表&nbsp;&gt;&nbsp;代理合同 - ${proxy.personname }
							    </h5>
							    <a href="/proxy/list" class="btn btn-xs btn-danger frt m-r-lg " >返回</a>
							    <a onclick="newContract()" class="btn btn-xs btn-primary frt m-r-lg " >新建合同</a>
			          			<div style="clear:both"></div>
							</div>
							<div class="ibox-content" >
								<div id="proxyContactList" >
									<c:choose>
										<c:when test="${!empty contractList }">
											<div style="border-bottom: 1px solid #e7eaec;" >
												<h5>合同列表</h5>
											</div>
											<table class="table table-hover table-striped " >
												<thead>
													<tr>
														<th>序号</th>
														<th>合同名称</th>
														<th>合同编号</th>
														<th>合同总额</th>
														<th>创建日期</th>
														<th>有效期</th>
														<th>回款状态</th>
														<th>最近回款</th>
														<th>最近回款日期</th>
														<th>待回款额</th>
														<th>状态</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${contractList }" var="list" varStatus="index" >
														<tr>
															<td>${index.count }</td>
															<td>${list.contractname }</td>
															<td>${list.contractcode }</td>
															<td>${list.amount }</td>
															<td><fmt:formatDate value="${list.createdate }" pattern="yyyy-MM-dd"/> </td>
															<td><fmt:formatDate value="${list.effectivedate }" pattern="yyyy-MM-dd"/> </td>
															<td>${list.paystate eq '1' ? '回款完成' : '回款中' }</td>
															<td>${empty list.lastPay.amount ? "-" : list.lastPay.amount }</td>
															<td>${empty list.lastPay.backdate ? "-" : list.lastPay.backdate }</td>
															<td>${empty list.pendingPay.amount ? "-" : list.pendingPay.amount }</td>
															<td>${list.state eq '0' ? "正常" : list.state eq '2' ? "停用" : "异常" }</td>
															<td>
																<a class="btn btn-xs btn-info" onclick="editContract('${list.id}' )" > 编辑 </a>
																<a class="btn btn-xs btn-danger" onclick="changeState('${list.id}' , '${list.state }')" >
																	${list.state eq '2' ? '继续' : '停用' } 
																</a>
																<a class="btn btn-xs btn-success" href="/proxy/contract/contractPayment/${list.id }" >回款</a>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</c:when>
										<c:otherwise>
											<div class="noneContract" > 当前没有合同，请点击上面<i>“新建合同”</i>按钮进行添加 </div>
										</c:otherwise>
									</c:choose>
								</div>
								<div id="newContractDiv" class="m-t-md"  style="display:none;" >
									<div style="border-bottom:1px dashed #ccc;" >
										<label style="font-size: 15px; font-weight: 700; margin-top: 20px;" >新建合同：</label>
									</div>
									<form action="/proxy/contract/saveContract" method="post" id="newProxyContractForm" >
										<input type="hidden" id="contractId" name="proxyContract.id" />
										<input type="hidden" value="${proxy.id }" name="proxyContract.proxyid" />
										<div class="m-t-sm" >
											<label class="input-s-sm" >合同名称</label>
											<input type="text" name="proxyContract.contractname" id="contractname" class="input-s-lg"  />
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm" >合同编号</label>
											<input type="text" name="proxyContract.contractcode" class="input-s-lg" id="contractcode"  />
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm" >合同金额</label>
											<input type="text" name="proxyContract.amount" id="amount" class="input-s znzhong" />
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm" >有效日期</label>
											<input type="text" name="proxyContract.effectivedate" class="znzhong" readonly="readonly" 
												  id="effective"  />
										</div>
										<div style="clear:both;" ></div>
										<div class="m-t-md" >
											<label class="input-s-sm flt " >回款顺序</label>
											<div id="paymentlist" style="padding-left: 120px;" >
												<div class="" >
													<label class="m-r" >第 1 次回款额</label>
													<input type="text" class="znzhong input-s-sm pay" name="payment_1"  />
													<input type="hidden" value="1" name="order"  >
												</div>
												<span id="plusFa" class="fa fa-plus mxfa m-t-sm" onclick="plusOrder()" ></span>
											</div> 
										</div>
										<div class="m-t-md" >
											<label class="input-s-sm flt " >备注</label>
											<textarea rows="5" cols="83" id="contractRemark" name="proxyContract.remark" ></textarea>
										</div>
										<div class="m-t-md" >
											<input type="button" value="提交" id="submitButton" onclick="submitForm()"
												class="btn btn-sm btn-success" />
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>	  
		</div>  
		<jsp:include page="../../basejs.jsp" />
		<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
		<script type="text/javascript">
			
			$( document ).ready( function() {
				
				var effectiveDate = {
						elem : "#effective",
						format : 'YYYY-MM-DD',
						istime : false,
						istoday : false
					} ;
				laydate( effectiveDate );
				
				
				$("#newProxyContractForm").validate( {
					rules: {
						'proxyContract.contractname' : "required",
						'proxyContract.contractcode' : "required",
						'proxyContract.amount' : {
							required : true,
						 	number : true
						} ,
						'proxyContract.effectivedate' : "required",
						'payment_1' : {
							required : true,
						 	number : true
						}
				    },
			        submitHandler:function( form ) {
			        	
			        	if( checkErrorPaycounts() != 0 ) {
			        		layer.msg( "回款数额填写有误,请检查" , 2 , 2 );
			        		return false;
			        	}
			        	var loadIndex = layer.load(0);
				        $.ajax( { 
				        	url : "/proxy/contract/saveProxyContract",
				        	type : "post",
				        	data : $( "#newProxyContractForm" ).serialize(),
				        	dataType : "json",
				        	async :false,
				        	success : function( result ) {
				        		if( result.flag ) {
				        			window.location.href = "/proxy/contract/proxyContract/${proxy.id}";
				        		} else {
				        			layer.msg( result.msg , 2 , 2 );  
				        		}
				        		layer.close( loadIndex );
				        	}
				        } );
					}    
				});
				
			} );
			
			/* 校验填写回款数额 */
			function checkErrorPaycounts() {
				var errorCounts = 0;
				var payAmounts = 0 ;
	        	$( ".pay" ).each( function() {
	        		var payNumber = $( this ).val();
	        		var result = payNumber.match('^[0-9]+(.[0-9]{1,2})?$');
	        		if( result == null ) {
	        			errorCounts += 1;
	        		} else {
	        			payAmounts += Number( payNumber );
	        		}
	        	} );
	        	
	        	if( errorCounts == 0 ) {
	        		var contractAmount = $( "#amount" ).val();
	        		if( contractAmount != payAmounts ) {
	        			return 1 ;
	        		}
	        	}
	        	return errorCounts ;
			}
			
			/* 新建合同 */
			function newContract() {
				$( "#newContractDiv" ).show();
			}
			
			/* 新增回款填写单 */
			function plusOrder() {
				var lastOrder = $( "#paymentlist" ).find( "input[name='order']" ).last().val();
				var currentOrder = Number( lastOrder ) + 1 ;
				var payStr = "<div class='m-t-md'>";
				payStr += "<label class='m-r'>第 <span id='labelOrder'>" + currentOrder + "</span> 次回款额</label>";
				payStr += "<input type='text' class='znzhong input-s-sm pay' name='payment_" + currentOrder + "' />";
				payStr += "<input type='hidden' value='" + currentOrder + "' name='order' />";
				payStr += "<span class='fa fa-minus-circle m-l mxfa' onclick='removePayOrder( this )' ></span></div>";
				
				$( "#plusFa" ).before( payStr );
			}
			
			/* 移除回款填写单 */
			function removePayOrder( domObj ) {
				var lastOrder = $( "#paymentlist" ).find( "input[name='order']" ).last().val();
				var currentOrder = $( domObj ).closest( "div" ).find( "input:last" ).val();
				$( domObj ).closest( "div" ).remove();
				if( lastOrder > currentOrder ) {
					$( "#paymentlist input[name='order']" ).each( function() {
						var checkOrder = $( this ).val();
						if( checkOrder > currentOrder ) {
							var newOrder = Number(checkOrder) - 1 ;
							$( this ).val( newOrder );
							$( this ).prev().attr( "name" , "payment_" + newOrder );
							$( this ).closest( "div" ).find( "#labelOrder" ).text( newOrder );
						}
						
					} );
				}
			}
			
			/* 提交合同内容 */
			function submitForm() {
				$("#newProxyContractForm").submit();
			}
			
			/* 停用/继续 */
			function changeState( contractId , state ) {
				state = state == "2" ? '0' : '2';
				if( confirm( "确定要这样操作吗?" ) ) {
					var loadIndex = layer.load( 0 );
					$.ajax( {
						url : "/proxy/contract/changeContractState",
						data : { "contractId" : contractId , "state" : state },
						dataType : "json",
						type : "post",
						async : false,
						success : function( result ) {
							if( result.flag ) {
			        			window.location.href = "/proxy/contract/proxyContract/${proxy.id}";
			        		} else {
			        			layer.msg( result.msg , 2 , 2 );  
			        		}
							layer.close( loadIndex );
						}
					} );
				}
			}
			
			function editContract( contractId ) {
				var loadIndex = layer.load( 0 );
				$.ajax( {
					url : "/proxy/contract/editProxyContractDetail",
					data : { "contractId" : contractId },
					dataType : "json",
					type : "post",
					async : false,
					success : function( result ) {
						$( "#contractId" ).val( result.contract.ID );
						$( "#contractRemark" ).text( result.contract.REMARK );
						$( "#contractname" ).val( result.contract.CONTRACTNAME );
						$( "#contractcode" ).val( result.contract.CONTRACTCODE );
						$( "#amount" ).val( result.contract.AMOUNT );
						$( "#effective" ).val( result.contract.EFFECTIVEDATE );
						
						$( "#paymentlist div" ).remove();
						
						var firstAmount = 0 ;
						var payList = "";
						for( var i = 0  ; i < result.contract.RULES.length ; i++ ) {
							var payOrder = result.contract.RULES[i].PAYORDER;
							var payState = result.contract.RULES[i].STATE;
							if( payOrder == "1" ) {
								firstAmount = result.contract.RULES[i].AMOUNT ;
							} else {
								payList += " <div class='m-t-md back' ><label class='m-r'>第 <span id='labelOrder'>" + payOrder + "</span> 次回款额 </label> "; 
								payList += " <input type='text' name='payment_" + payOrder + "' value='" + result.contract.RULES[i].AMOUNT + "'  "; 
								if( payState == 2 ) {
									payList += " readonly='readonly' ";
								}
								payList += " class='znzhong input-s-sm pay' /><input type='hidden' name='order' value='" + payOrder + "' /> ";
								if( payState != 2 ) {
									//已回款就不能删除
									payList += " <span onclick='removePayOrder( this )' class='fa fa-minus-circle m-l mxfa'></span></div> "; 
								}
							}
						}
						
						var firstStr = "<div> <label class='m-r'>第 1 次回款额</label> <input type='text' name='payment_1' value='" + firstAmount ;
						firstStr +=  "'  class='znzhong input-s-sm pay'> <input type='hidden' name='order' value='1'> </div>  ";
						
						$( "#plusFa" ).before( firstStr );
						$( "#plusFa" ).before( payList );
						
						$( "#newContractDiv" ).show();
						
						layer.close( loadIndex );
					}
				} );
			}
			
		</script>
		
	</body>
</html>