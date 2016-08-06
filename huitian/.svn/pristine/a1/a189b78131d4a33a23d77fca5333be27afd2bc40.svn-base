<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get('teacher.group.add')}、编辑菜单</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<title>${_res.get('teacher.group.add')}、编辑菜单</title>

<style type="text/css">
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
		<div class="ibox-content">
				<form id="menuForm" action="" method="post">
					<input type="hidden" id="menuId" name="menu.id" value="${menu.id }"/>
					<input type="hidden" id="wpnumberId" name="menu.wpnumberid" value="${menu.wpnumberid }"/>
					<fieldset style="width: 100%; padding-top:15px;">
					<p>
							<label>菜单名称：</label>
							<input type="text" id="menuName" name="menu.menuname" value="${menu.menuname }" size="20" maxlength="15"/>
							<span id="menuNameInfo" style="color: red;"></span>
						</p>
						<p>
							<label>菜单类型：</label>
								<input type="radio" id="menutype" name="menu.menutype" value="0" ${menu.menutype == 0 ? "checked='checked'":"" } onchange="parentMenuChose(this.value)">一级菜单
								<input type="radio" id="menutype" name="menu.menutype" value="1" ${menu.menutype == 1 ? "checked='checked'":"" } onchange="parentMenuChose(this.value)">二级菜单
						</p>
						<p id="parentMenu" ${menu.menutype == 1 ? "":"hidden='true'" }>
							<label>所属菜单：</label> 
							<select name="menu.parentid" id="parentid" style="width: 150px" tabindex="2">
							<c:if test="${empty  menu.parentid }"><option value="" >无所属</option></c:if>
								<c:forEach items="${menuList }" var="parent">
									<option value="${parent.id }" ${parent.id==menu.parentid?"selected='selected'":"" }>${parent.menuname }</option>
								</c:forEach>
							</select>
							<span id="subjectInfo" style="color: red;"></span>
						</p>
						<p id="oauth">
							<label>OAuth授权：</label>
								<input type="radio" id="isoauth" name="menu.isoauth" value="0" ${menu.isoauth == 0||operatorType eq 'add' ? "checked='checked'":"" } onclick="hideIsoauth()">${_res.get('admin.common.no')}
								<input type="radio" id="isoauth" name="menu.isoauth" value="1" ${menu.isoauth == 1 ? "checked='checked'":"" } onclick="showIsoauth()">${_res.get('admin.common.yes')}
						</p>
						<p id="redirect_uri" ${menu.isoauth == "0" || menu.isoauth==null ? "hidden='true'":"" }>
							<label>redirect_uri：</label>
							<input id="menuredirect_uri" type="text" name="menu.redirect_uri" value="${menu.redirect_uri }" size="50"/>
						</p>
						<p id="state" ${menu.isoauth == "0" || menu.isoauth==null ? "hidden='true'":"" }>
							<label>state：</label>
							<input id="menustate" type="text" name="menu.state" value="${menu.state }"/>
						</p>
						<p id="type" ${menu.isoauth == "1" ? "hidden='true'":"" }>
							<label>响应类型：</label>
							<input type="radio" name="menu.type" id="menu.type" value="click" ${menu.type == "click" || menu.type == null ? "checked='checked'":"" } onclick="hideUrl()">click</label>
							<input type="radio" name="menu.type" id="menu.type" value="view" ${menu.type == "view" ? "checked='checked'":"" } onclick="hideKey()">view</label>
						</p>
						<p id="key" ${menu.type == "view"||menu.isoauth == "1" ? "hidden='true'":""}>
							<label>key：</label>
							<input id="menuKey" type="text" name="menu.key" value="${menu.key }"/>
						</p>
						<p id="url" ${menu.type == "view" ? "":"hidden='true'"}>
							<label>url：</label>
							<input id="menuUrl" type="text" name="menu.url" value="${menu.url }" size="50"/>
						</p>
						<p>
							<label>菜单顺序：</label>
							<input id="menuSortorder" type="text" name="menu.sortorder" value="${menu.sortorder }"/>
						</p>
						<p>
						<c:if test="${operatorType eq 'add'}">
							<input type="button" value="${_res.get('save')}" onclick="return save();" class="btn btn-outline btn-primary" />
						</c:if>
						<c:if test="${operatorType eq 'update'}">
							<input type="button" value="${_res.get('update')}" onclick="return save();" class="btn btn-outline btn-primary" />
						</c:if>
						</p>
					</fieldset>
				</form>
			</div>
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
	<script>
	 $(".chosen-select").chosen({disable_search_threshold: 30});
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
		var index = parent.layer.getFrameIndex(window.name);
		function parentMenuChose(menuType){
			if(menuType == 0 ){
				$("#parentId").attr("disabled",true);
				$("#parentName").attr("disabled",true);
				$("#parentMenu").hide();
			}else{
				$("#parentMenu").show();
				$("#parentId").attr("disabled",false);
				$("#parentName").attr("disabled",false);
			}
		}

		function showIsoauth(){
			  $("#type").hide();
			  $("#url").hide();
			  $("#key").hide();
			  $("#redirect_uri").show();
			  $("#menuredirect_uri").show();
			  $("#state").show();
			  $("#menustate").show();
			}

		function hideIsoauth(){
			$("#redirect_uri").hide();
			$("#menuredirect_uri").hide();
		    $("#state").hide();
			$("#menustate").hide();
			$("#key").show();
			$("#type").show();
		}

		function hideKey(){
			$("#menuKey").attr("disabled",true);
			$("#key").hide();
			$("#url").show();
			$("#menuUrl").attr("disabled",false);
		}
		
		function checkkong(checkField) {
			var checkValue = $("#"+checkField).val();
		    if (checkValue != "") {
		    	var flag = false; 
		    	return flag;
		    } else {
		    	flag = true;
		    	return flag;
		    }
		}

		function hideUrl(){
			$("#key").show();
			$("#menuKey").attr("disabled",false);
			$("#menuUrl").attr("disabled",true);
			$("#url").hide();
		}
		parent.layer.iframeAuto(index);
		function save() {
				var menuId = $("#menuId").val();
				if(menuId==""){
					path="${cxt}/weixin/menu/save";
				}else{
					path="${cxt}/weixin/menu/update";
				}
				if(checkkong('parentid')){
					$("#subjectInfo").text("所属菜单不能为空");
					$("#parentid").focus();
				}else if(confirm("确定要提交保存/更新菜单吗？")){
					 $.ajax({
				            cache: true,
				            type: "POST",
				            url:path,
				            data:$('#menuForm').serialize(),// 你的formid
				            async: false,
				            error: function(request) {
				            	parent.layer.msg("网络异常，请稍后重试。", 1,1);
				            },
				            success: function(data) {
					    		parent.layer.msg(data.msg, 2,1);
					    		if(data.code=='1'){//成功
					    			setTimeout("parent.layer.close(index)", 1000 );
					    			parent.window.location.reload();
					    		}
				            }
				        });
				}
			}
	</script>
</body>
</html>