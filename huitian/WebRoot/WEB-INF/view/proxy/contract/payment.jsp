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
			#contractPayback , #contractBaseInfo {
				background-color: #fbfbfb;
			    border: 1px dashed #ccc;
			    margin-left: 40px;
			    max-width: 810px;
			    padding-left: 20px;
			}
			.mxfa {
				font-size: 20px !important;
				cursor: pointer;
			}
			.nonePaiedList {
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
			#state_2 , #state_1 {
				color : #ed5565;
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
									&gt;&nbsp;&nbsp;渠道管理&nbsp;&gt;&nbsp;代理列表&nbsp;&gt;&nbsp;代理合同 - ${contractDetail.personname }
									&nbsp;&gt;&nbsp;回款记录
							    </h5>
							    <a href="/proxy/contract/proxyContract/${contractDetail.proxyid }" class="btn btn-xs btn-danger frt m-r-lg " >返回</a>
			          			<div style="clear:both"></div>
							</div>
							<div class="ibox-content" >
								<div id="contractBaseInfo" class="m-t-sm" >
									<div class="m-t-sm" >
										<label class="input-s-sm" >合同名称</label><span>${contractDetail.contractname }</span>
									</div>
									<div class="m-t-sm" >
										<label class="input-s-sm" >合同编号</label><span>${contractDetail.contractcode }</span>
									</div>
									<div class="m-t-sm" >
										<label class="input-s-sm" >合同总额</label><span>${contractDetail.amount }</span>
									</div>
									<div class="m-t-sm" >
										<label class="input-s-sm" >有效期</label><span>${contractDetail.effectivedate }</span>
									</div>
									<div class="m-t-sm" >
										<label class="input-s-sm" >合同状态</label>
										<span id="state_${contractDetail.state }" >
											${contractDetail.state eq '2' ? '暂停' : contractDetail.state eq '1' ? '异常' : '正常' }</span>
									</div>
								</div>
								<div id="contractPayback" class="m-t-md">
									<c:choose>
										<c:when test="${!empty paiedList }">
											<div ><label style="font-size: 15px; font-weight: 700; margin-top: 20px;" >回款记录：</label></div>
											<div style="padding-left: 40px;" >
												<table class="table table-hover table-striped " >
													<thead>
														<tr>
															<th>序号</th>
															<th>回款日期</th>
															<th>回款金额</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${paiedList }" var="list" varStatus="index" >
															<tr>
																<td>${index.count }</td>
																<td>${list.backdate }</td>
																<td>${list.amount }</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</c:when>
										<c:otherwise>
											<div class="nonePaiedList" > 当前没有回款记录 </div>
										</c:otherwise>
									</c:choose>
								</div>
								<c:if test="${!empty pendingPay }">
									<div id="contractPayback" class="m-t-md"  >
										<div ><label style="font-size: 15px; font-weight: 700; margin-top: 20px;" >最近待回款：</label></div>
										<div style="padding-left: 40px;" >
											<form action="/proxy/contract/contractPaymentBack" method="post" id="paymentBackForm" >
												<input type="hidden" value="${pendingPay.id }" name="proxyContractPayment.id" />
												<div class="m-t-sm" >
													<label class="input-s-sm" >回款金额</label>
													<input type="text" name="proxyContractPayment.amount" value="${pendingPay.amount }" 
														 readonly="readonly" class="input-s-lg znzhong"  />
												</div>
												<div class="m-t-md" >
													<label class="input-s-sm" >回款日期</label>
													<input type="text" name="proxyContractPayment.backdate" id="backdate" 
														 readonly="readonly" class="input-s-lg znzhong"  />
												</div>
												<div style="clear:both;" ></div>
												<div class="m-t-md" >
													<input type="button" value="提交" id="submitButton" onclick="submitForm()"
														class="btn btn-sm btn-success" />
												</div>
											</form>
										</div>
									</div>
								</c:if>
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
						elem : "#backdate",
						format : 'YYYY-MM-DD',
						istime : false,
						istoday : false
					} ;
				laydate( effectiveDate );
				
				
				$("#paymentBackForm").validate( {
					rules: {
						'proxyContractPayment.amount' : "required",
						'proxyContractPayment.backdate' : "required"
				    },
			        submitHandler:function( form ) {
			        	var msg = " 确定要提交吗？ ";
			        	var contractState = '${contractDetail.state }';
			        	if( "0" != contractState ) { 
			        		msg = " 该合同异常，继续提交操作？ ";
			        	}
			        	if( confirm( msg ) ) {
				        	var loadIndex = layer.load(0);
					        $.ajax( { 
					        	url : "/proxy/contract/saveContractPayment",
					        	type : "post",
					        	data : $( "#paymentBackForm" ).serialize(),
					        	dataType : "json",
					        	async :false,
					        	success : function( result ) {
					        		if( result.flag ) {
					        			window.location.href = "/proxy/contract/contractPayment/${contractDetail.id}";
					        		} else {
					        			layer.msg( result.msg , 2 , 2 );  
					        		}
					        		layer.close( loadIndex );
					        	}
					        } );
			        	}
					}    
				});
				
			} );
			
			
			/* 提交合同内容 */
			function submitForm() {
				$("#paymentBackForm").submit();
			}
			
		</script>
		
	</body>
</html>