<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get("faculty_list")}</title>
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
       <form action="/teacher/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;
					   <a href="javascript:window.parent.location='/account'">${_res.get("admin.common.mainPage") }</a> 
					   &gt;<a href='/teacher/index?_query.state=0'>${_res.get("teacher_management")}</a> &gt; ${_res.get("faculty_list")}
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>${_res.get('teacher.name')}：</label>
					<input type="text" id="teachername" name="_query.teachername" value="${paramMap['_query.teachername']}">
				<label>Email：</label>
					<input type="text" id="email" name="_query.email" value="${paramMap['_query.email']}">
				<label>${_res.get("admin.user.property.telephone")}：</label>
					<input type="text" id="phonenumber" name="_query.phonenumber" value="${paramMap['_query.phonenumber']}">
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				
				<c:if test="${operator_session.qx_teacheradd }">
					<input type="button" value="${_res.get('teacher.group.add')}" onclick="addTeacher()" class="btn btn-outline btn-success">
				</c:if>
			</div>
			</div>
			</div>
			
			<div class="col-lg-12" style="min-width:680px">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>${_res.get('faculty_list')}</h5>
					</div>
					<div class="ibox-content">
						<table class="table table-hover table-bordered" width="100%">
							<thead>
								<tr>
									<th rowspan="2">${_res.get("index") }</th>
									<th rowspan="2">${_res.get("sysname") }</th>
									<th rowspan="2">${_res.get("gender") }</th>
									<th rowspan="2">${_res.get('job.category')}</th>
									<th rowspan="2">Email</th>
									<th rowspan="2">${_res.get("telphone") }</th>
									<th rowspan="2">recive_sms</th>
									<th rowspan="2">${_res.get('District')}</th>
									<%-- <th rowspan="2">${_res.get("createtime") }</th> --%>
									<!--<th rowspan="2">教学能力</th> -->
									<th rowspan="2">${_res.get("operation") }</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="teacher" varStatus="status">
								<tr class="odd" align="center">
									<td>${status.count}</td>
									<c:if test="${teacher.isforeignteacher==0}">
										<td>${teacher.real_name}</td>
									</c:if>
									<c:if test="${teacher.isforeignteacher==1}">
										<td>${teacher.real_name}<br>(${_res.get('Foreign.language.teacher')})</td>
									</c:if>
									<td>
										${teacher.sex eq 1 ?_res.get('student.boy'):_res.get('student.girl')}
									</td>
									<td>
									<c:choose>
										<c:when test="${teacher.tworktype != null}">
										${teacher.tworktype eq 1 ?_res.get('Full-time'):_res.get('Part-time.job')}
										</c:when>
										<c:otherwise>
										${_res.get('Temporary.unregistered')}
										</c:otherwise>
									</c:choose>
									<td>${teacher.email}</td>
									<td>${teacher.tel}</td>
									<td>${teacher.receive_sms_teacher eq 1 ?_res.get('admin.common.yes'):_res.get('admin.common.no')}</td>
									<td>${teacher.cname}</td>
									<%-- <td>${fn:substring(teacher.create_time,0,10)}</td> --%>
									<%-- <td>${teacher.style}</td>
									<td>${teacher.ability}</td> --%>
									<td align="center">&nbsp;
										<c:choose>
											<c:when test="${teacher.state==1}">
												<c:if test="${operator_session.qx_teacherfreeze }">
													<a style="color: #515151" href="javascript:void(0)" onclick="freezeAccount(${teacher.id},0)">${_res.get('Recover')}</a>
												</c:if>
											</c:when>
											<c:otherwise>
												<a href="javascript:void(0)" onclick="styleAndAbility(${teacher.id})" >${_res.get('Introduction')}</a>&nbsp;|&nbsp; 
												<c:if test="${operator_session.qx_teacheredit }">
													<a href="#" onclick = "updateTeacher(${teacher.id})" >${ _res.get('admin.common.edit')}</a>&nbsp;|&nbsp; 
												</c:if>
												<c:if test="${operator_session.qx_teacherpassword }">
													<a href="javascript:void(0)" onclick="changePassword(${teacher.id})" >${_res.get("passWord")}</a>&nbsp;|&nbsp;  
												</c:if>
												<c:if test="${operator_session.qx_teachertoManageCoursePage }">
													<a href="#" onclick="updateCourse(${teacher.id})" >${_res.get('course.course')}</a>&nbsp;|&nbsp;
												</c:if>
												<c:if test="${operator_session.qx_teachertheacherRestDay }">
													<a href="javascript:void(0)" onclick="theacherRestDay(${teacher.id})"  >${_res.get('rest.day')}</a>&nbsp;|&nbsp;
												</c:if>
												<c:if test="${operator_session.qx_teachercoursecosttoCoursecostPage }">
													<a href="javascript:void(0)" onclick="toCoursecostPage(${teacher.id})"  >${_res.get('tuition.fee')}</a>&nbsp;|&nbsp;
												</c:if>
												<c:if test="${operator_session.qx_teacherfreeze }">
													<a style="color: #a4a4a4" href="javascript:void(0)" onclick="freezeAccount(${teacher.id},1)">${_res.get('Suspending')}</a>
												</c:if>
											</c:otherwise>
										</c:choose>
										<%-- <c:if test="${operator_session.qx_teachereditAccount }">
											&nbsp;|&nbsp;<a href="/teacher/editAccount/${teacher.id}" title="${_res.get('admin.common.see')} ">${_res.get('admin.common.see')} </a> &nbsp;
										</c:if>	 --%>
									</td>
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
    function updateCourse(id){
    		$.layer({
        	    type: 2,
        	    shadeClose: true,
        	    title: "${_res.get('Modify.the.teachers.course')}",
        	    closeBtn: [0, true],
        	    shade: [0.5, '#000'],
        	    border: [0],
        	    area: ['700px', '320px'],
        	    iframe: {src: '${cxt}/teacher/toManageCoursePage/'+id}
        	});
    }
    function updateTeacher(id){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "${_res.get('Modify')}",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
		    area: ['700px', '650px'],
    	    iframe: {src: '${cxt}/teacher/edit/'+id}
    	});
    }
    function styleAndAbility(id){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "${_res.get('Introduction')}",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    offset:['160px', ''],
    	    area: ['700px', '240px'],
    	    iframe: {src: '${cxt}/teacher/checkTeacherStyle/'+id}
    	});
    }
    function addTeacher(){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "${_res.get('Add.the.teacher')}",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
		    area: ['700px', '650px'],
    	    iframe: {src: '${cxt}/teacher/add'}
    	});
    }
    /* function styleAndAbility(id){
    	$.ajax({
    		url:"/teacher/checkTeacherStyle",
    		type:"post",
    		data:{"id":id},
    		dataType:"json",
    		success:function(result){
    			layer.tab({
    			    data:[
    			        {
    			        	title: '教学能力和风格', 
    			        	content:'<div style="padding:5px 10px;margin:15px 0 0 20px"><table class="table table-bordered">'
    			        	+'<tr class="table-bg1">教学风格:</tr><tr class="table-bg2">'+result.style+'</tr>'
    			        	+'</br></br>'
    			        	+'<tr class="table-bg1">教学能力:</tr><tr class="table-bg2">'+result.ability+'</tr>'
    			        	+'</table></div>'
    			        }
    			    ],
    				offset:['150px', ''],
    			    area: ['600px', 'auto'] //宽度，高度
    			});
    		}
    	});
    } */
    function changePassword(id){
    	layer.prompt({title: '${_res.get("Change.password")}',type: 1,length: 200}, function(val,index){
    		$.ajax({
    			url:"${cxt}/sysuser/changePassword",
    			type:"post",
    			data:{"id":id,"password":val},
    			dataType:"json",
    			success:function(data){
    				if(data.result){
    					 layer.msg('密码修改成功！', 1, 1);
    					 layer.close(index)
    				}else{
    					alert("密码修改失败！");
    				}
    			}
    		});
		});
    }
function freezeAccount(teacherId,state){
	if(confirm("${_res.get('ModifyState')}")){
		$.ajax({
			url:"/teacher/freeze",
			type:"post",
			data:{"teacherId":teacherId,"state":state},
			dataType:"json",
			success:function(result)
			{
				if(result.result=="true")
				{
					alert("操作成功");
					window.location.reload();
				}
				else
				{
					alert(result.result);
				}
			}
		});
	}
}

function theacherRestDay(id){
	$.layer({
	    type: 2,
	    shadeClose: true,
	    title:"${_res.get('Modify.the.day.off')}",
	    closeBtn: [0, true],
	    shade: [0.5, '#000'],
	    border: [0],
	    offset:['30px', ''],
	    area: ['600px','600px'],
	    iframe: {src: '${cxt}/teacher/theacherRestDay/'+id}
	});
}
function toCoursecostPage(id){
	$.layer({
	    type: 2,
	    shadeClose: true,
	    title: "${_res.get('Set.the.class.fees')}",
	    closeBtn: [0, true],
	    shade: [0.5, '#000'],
	    border: [0],
	    offset:['20px', ''],
	    area: ['700px', '460px'],
	    iframe: {src: '${cxt}/teacher/coursecost/toCoursecostPage/'+id}
	});
}
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
    </script>
</body>
</html>