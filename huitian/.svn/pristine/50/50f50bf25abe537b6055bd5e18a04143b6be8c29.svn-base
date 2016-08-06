<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>添加反馈</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 

<style type="text/css">
body {
	background-color: #eff2f4;
}
textarea {
	width: 50%;
}
label {
	width: 80px;
}
</style>
</head>
<body>
	<div id="wrapper">
		<div class="row border-bottom">
			<div>
				<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0; position: fixed; width: 100%; background-color: #fff;">
					<div class="navbar-header" style="margin: 10px 0 0 30px;">
						<h5>
							<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
							 &gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt;<a href='javascript:history.go(-1);'>销售列表</a>&gt;跟进反馈
						</h5>
					</div>
				</nav>
			</div>
		</div>

		<div class="row wrapper border-bottom white-bg page-heading margin_zuo" style="width: 95%; margin-top: 100px;">
			<div class="col-lg-12" style="margin-top: 20px;">
				<form id="opportunityForm" action="${cxt }/opportunity/save" method="post">
					<fieldset style="width: 100%; padding-top:15px;">
						<input type="hidden" id="opportunityId" name="opportunity.id" value="${opportunity.id }"/>
						<c:if test="${!empty opportunity.id }">
							<input type="hidden" name="opportunity.version" value="${opportunity.version + 1}">
						</c:if>
						<p>
							<label>${_res.get("Contacts")}：</label>
							<input type="text" id="contacter" name="opportunity.contacter" value="${opportunity.contacter }" size="20" maxlength="15" vMin="2" vType="chinaLetterNumber" onblur="onblurVali(this);" onchange="checkExist('contacter')"/>
							<span id="contacterMes"></span>
						</p>
						<p>
							<label>${_res.get("admin.user.property.telephone")}：</label>
							<input type="text" id="phonenumber" name="opportunity.phonenumber" value="${opportunity.phonenumber }" size="20" maxlength="15" vMin="11" vType="phone" onblur="onblurVali(this);" onchange="checkExist('phonenumber')"/>
							<span id="phonenumberMes"></span>
						</p>
						<p>
							<label>电子邮箱：</label>
							<input type="text" id="email" name="opportunity.email" value="${opportunity.email }" size="20" maxlength="100" vMin="6" vType="email" onblur="onblurVali(this);" onchange="checkExist('email')"/>
							<span id="emailMes"></span>
						</p>
						<p>
							<label>${_res.get('gender')}：</label>
							<input type="radio" id="sex" name="opportunity.sex" value="1" checked="checked">${_res.get('student.boy')}
							<input type="radio" id="sex" name="opportunity.sex" value="0" >${_res.get('student.girl')}
						</p>
						<p>
							<label>所属市场：</label> 
							<select name="opportunity.scuserid" id="scuserid" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="">--${_res.get('Please.select')}--</option>
								<c:forEach items="${sysUserList }" var="sysUser">
									<option value="${sysUser.id }" <c:if test="${sysUser.id==opportunity.scuserid }">selected='selected'</c:if> >${sysUser.real_name }</option>
								</c:forEach>
							</select>
						</p>
						<p>
							<label>所属CC：</label> 
							<select name="opportunity.kcuserid" id="kcuserid" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="">--${_res.get('Please.select')}--</option>
								<c:forEach items="${sysUserList }" var="sysUser">
									<option value="${sysUser.id }" <c:if test="${sysUser.id==opportunity.kcuserid }">selected='selected'</c:if> >${sysUser.real_name }</option>
								</c:forEach>
							</select>
						</p>
						<p>
							<label>所属顾问：</label> 
							<select name="opportunity.mediatorid" id="mediatorid" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="">--${_res.get('Please.select')}--</option>
								<c:forEach items="${mediatorList }" var="mediator">
									<option value="${mediator.id }" <c:if test="${mediator.id==opportunity.mediatorid }">selected='selected'</c:if> >${mediator.realname }</option>
								</c:forEach>
							</select>
						</p>
						<p>
							<label>与学生关系：</label> 
							<select name="opportunity.relation" id="relation" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="1" <c:if test="${opportunity.relation eq 1 }">selected='selected'</c:if>>本人</option>
								<option value="2" <c:if test="${opportunity.relation eq 2 }">selected='selected'</c:if>>母亲</option>
								<option value="3" <c:if test="${opportunity.relation eq 3 }">selected='selected'</c:if>>父亲</option>
								<option value="4" <c:if test="${opportunity.relation eq 4 }">selected='selected'</c:if>>其他</option>
							</select>
						</p>
						<p>
							<label>主动联系：</label> 
							<select name="opportunity.needcalled" id="needcalled" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="0" <c:if test="${opportunity.needcalled}">selected='selected'</c:if>>${_res.get('admin.common.no')}</option>
								<option value="1" <c:if test="${opportunity.needcalled}">selected='selected'</c:if>>${_res.get('admin.common.yes')}</option>
							</select>
						</p>
						<p>
						<c:if test="${operator_session.qx_opportunitysave }">
						<c:if test="${operatorType eq 'add'}">
							<input type="button" value="${_res.get('save')}" onclick="return save();" class="btn btn-outline btn-primary" />
						</c:if>
						</c:if>
						<c:if test="${operator_session.qx_opportunityupdate }">
						<c:if test="${operatorType eq 'update'}">
							<input type="button" value="${_res.get('update')}" onclick="return save();" class="btn btn-outline btn-primary" />
						</c:if>
							</c:if>
							<input type="button" onclick="window.history.go(-1)" value="${_res.get('system.reback')}" class="btn btn-outline btn-success">
						</p>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
		var config = {
			'.chosen-select' : {},
			'.chosen-select-deselect' : {
				allow_single_deselect : true
			},
			'.chosen-select-no-single' : {
				disable_search_threshold : 10
			},
			'.chosen-select-no-results' : {
				no_results_text : 'Oops, nothing found!'
			},
			'.chosen-select-width' : {
				width : "95%"
			}
		}
		for ( var selector in config) {
			$(selector).chosen(config[selector]);
		}
	</script>
	<script>
		function checkExist(checkField) {
			var checkValue = $("#"+checkField).val();
		    if (checkValue != "") {
		    	var flag = true;
		        $.ajax({
		            url: '${cxt}/opportunity/checkExist',
		            type: 'post',
		            data: {
		                'checkField': checkField,
		                'checkValue': checkValue,
		                'opportunityId': $("#opportunityId").val()
		            },
		            async: false,
		            dataType: 'json',
		            success: function(data) {
		                if (data.result >= 1) {
		                	$("#"+checkField).focus();
	                    	$("#"+checkField+"Mes").text("您填写的数据已存在。");
		                }else{
		                	$("#"+checkField+"Mes").text("");
		                	flag = false;
		                } 
		            }
		        });
		        return flag;
		    } else {
		        $("#"+checkField).focus();
		    	$("#"+checkField+"Mes").text("该字段不能为空。");
		        return true;
		    }
		}
		
		function save() {
			if(checkExist('contacter'))
				return false;
			if(checkExist('phonenumber'))
				return false;
			if(checkExist('email'))
				return false;
			var contacter = $("#contacter").val().trim;
			if ($("#contacter").val() == "" || $("#contacter").val() == null) {
				$("#contacter").focus();
				$("#contacterMes").text("联系人不能为空！");
				return false;
			}else{
				$("#contacterMes").text("");
				var opportunityId = $("#opportunityId").val();
				if(opportunityId==""){
					$("#opportunityForm").submit();
				}else{
					if(confirm("确定要修改该销售机会吗？")){
						$("#opportunityForm").attr("action", "/opportunity/update");
						$("#opportunityForm").submit();
					}
				}
			}
		}
	</script>
	<script src="/js/utils.js"></script>
</body>
</html>