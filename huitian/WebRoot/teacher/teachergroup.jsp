<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get("teaching_and_research_section") }</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet"/>
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet"/>
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet"/>
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet"/>
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet"/>
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet"/>
<link href="/css/css/animate.css" rel="stylesheet"/>
<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<style type="text/css">
 .chosen-container{
   margin-top:-3px;
 }
 .xubox_tabmove{
	background:#EEE;
}
.xubox_tabnow{
    color:#31708f;
}
</style>
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
       <form action="/teacher/tgroup" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					    <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
					   &gt;<a href='/teacher/index?_query.state=0'>${_res.get("teacher_management") }</a> &gt; ${_res.get("teaching_and_research_section") }
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
					<label>${_res.get('group.name')}：</label>
						<input type="text" id ="groupname" name="_query.groupname" value="${paramMap['_query.groupname']}">
					<label>${_res.get('person.in.charge')}：</label>
						<input type="text" id="leadername" name="_query.leadername" value="${paramMap['_query.leadername']}">
					<input type="button" onclick="search()" value="${_res.get('admin.common.select') }"  class="btn btn-outline btn-primary">
					<c:if test="${operator_session.qx_teacherfindOfadd }">
						<input type="button" onclick="window.location.href='/teacher/findOfadd'" value="${_res.get('teacher.group.add') }" class="button btn btn-outline btn-warning ">
					</c:if>
			    </div>
			</div>
			</div>

			<div class="col-lg-12" style="min-width:680px">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get('teaching.group.list')}</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered" width="100%">
							<thead>
								<tr>
									<th>${_res.get("index") }</th>
									<th>${_res.get('group.name')}</th>
									<th>${_res.get('person.in.charge')}</th>
									<th>${_res.get('group.member')}</th>
									<th>${_res.get("createtime") }</th>
									<c:if test="${operator_session.qx_teachereditgroup || operator_session.qx_teachershowgroup || operator_session.qx_teacherdeletegroup }">
									<th>${_res.get("operation") }</th></c:if>
								</tr>
							</thead>
							<c:forEach items="${teacherList.list}" var="tgroup" varStatus="index">
								<tr class="odd" align="center">
									<td >${index.count }</td>
									<td>${tgroup.groupname}</td>
									<td>${tgroup.REAL_NAME }</td>
									<td> ${tgroup.name}</td>  
									 <td><fmt:formatDate value="${tgroup.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td> 
									<c:if test="${operator_session.qx_teachereditgroup || operator_session.qx_teachershowgroup || operator_session.qx_teacherdeletegroup }">
									<td>
									<c:if test="${operator_session.qx_teachershowgroup }">
									   <a onclick="showgroup(${tgroup.id})" title="${_res.get('admin.common.see')} " >${_res.get('admin.common.see')} </a> &nbsp;|&nbsp;
									</c:if>
									<c:if test="${operator_session.qx_teachereditgroup }">
										<a href="/teacher/editgroup/${tgroup.id }" title="${_res.get('Modify')} ">${_res.get('Modify')} </a> &nbsp;|&nbsp;
									</c:if>
									<c:if test="${operator_session.qx_teacherdeletegroup }">
										<a href="/teacher/deletegroup/${tgroup.id }" title="${_res.get('admin.common.delete')} " onclick="{if(confirm('确定要删除吗?')){return true;}return false;}">${_res.get('admin.common.delete')} </a> &nbsp;
									</c:if>
									</td>
									</c:if>
								</tr>
								</c:forEach>
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script type="text/javascript">
function showgroup(groupid){
	$.layer({
		type:2,
		shadeClose:'true',
		title:"${_res.get('The.research.group.information')}",
		closeBtn:[0,true],
		shade:[0.5,"#000"],
		border:[0],
		offset:['20px',''],
		area:['600px','480'],
		iframe:{src:'${ctx}/teacher/showgroup/'+groupid}
	});
	
}

/* function showgroup(groupid){
	$.ajax({
		url:"/teacher/showgroup",
		type:"post",
		data:{"groupid":groupid},
		dataType:"json",
		success:function(result){
			var msg = "";
        	for(var i=0;i<result.teacher.length;i++){
        		msg += '<tr><td class="table-bg2"  rowspan="2" style="text-align:center;vertical-align:middle">'
        					+result.teacher[i].REAL_NAME+'</td>';
        		msg += '<td class="table-bg1" valign="middle" style="width: 80px;height: 15px">教学风格</td><td class="table-bg2" colspan="3" >'
        				+(result.teacher[i].STYLE!=null?result.teacher[i].STYLE:"暂无介绍")+'</td></tr>';
        		msg += '<tr><td class="table-bg1" style="width: 80px;height: 15px">教学能力</td><td class="table-bg2" colspan="3">'
        				+(result.teacher[i].ABILITY!=null?result.teacher[i].ABILITY:"暂无介绍")+'</td></tr>';
        	}
			layer.tab({
			    data:[
			        {
			        	title: "${_res.get('admin.common.see')}", 
			        	content:'<table class="table table-bordered">'
			        	+'<tr>'
			        	+'<td class="table-bg1" style="width: 80px;height: 30px">组名称</td><td class="table-bg2" >'
			        			+result.teachgroup.GROUPNAME+'</td>'
			        	+'<td class="table-bg1" style="width: 80px;height: 30px">负责人</td><td class="table-bg2" style="vertical-align:middle">'
			        			+result.teachgroup.LEADERNAME+'</td>'
			        	+'</tr>'
			        	+'<tr>'
			        	+'<td class="table-bg1" colspan="5" style="text-align:center;vertical-align:middle"> 组成员介绍</td>'
			        	+msg
			        	+'</tr>'
			        	+'</table>'
			        }
			    ],
				offset:['150px', ''],
			    area: ['600px', 'auto'] //宽度，高度
			});
			//-----------
			
		}
	});		
} */



</script>

<!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script>
       $('li[ID=nav-nav2]').removeAttr('').attr('class','active');
       
   		//删除按钮，表单提交
	function deleteGroup(form){
		$("#groupForm").attr("action", "/teacher/deletegroup");
		$("#groupForm").submit();
	}
   	
   	
    </script>
</body>
</html>