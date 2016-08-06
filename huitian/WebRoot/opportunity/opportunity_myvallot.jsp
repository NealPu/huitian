<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>我的机会</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 
<script src="/js/js/jquery-2.1.1.min.js"></script>
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
		<form action="/opportunity/myAllot" method="post" id="searchForm">
		<input type="hidden" value="${paramMap['_query.myvallot'] }" name="_query.myvallot" >
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
						<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
						  &gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt; 我的机会
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>${_res.get("Contacts")}：</label>
					<input type="text" id="contacter" name="_query.contacter" style="width:120px;" value="${paramMap['_query.contacter']}">
				<label>${_res.get("admin.user.property.telephone")}：</label>
					<input type="text" id="phonenumber" name="_query.phonenumber" style="width:120px;" value="${paramMap['_query.phonenumber']}">
				<label>回访日期：</label>
					<input type="text" class="form-control layer-date" readonly="readonly" id="date1" name="_query.startNextvisitDate" value="${paramMap['_query.startNextvisitDate']}" style="margin-top: -8px; width:120px; background-color: #fff;" /> 
					至<input type="text" class="form-control layer-date" readonly="readonly" id="date2" name="_query.endNextvisitDate" value="${paramMap['_query.endNextvisitDate']}" style="margin-top: -8px; width:120px; background-color: #fff;" />  
				<label>就近校区：</label>
				<select id="campusid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.campusid"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${campuslist}" var="campus">
						<option value="${campus.id }" <c:if test="${campus.id == paramMap['_query.campusid'] }">selected="selected"</c:if>>${campus.CAMPUS_NAME }</option>
					</c:forEach>
				</select>
				<br/>
				<br/>
				<label>咨询科目：</label>
				<select id="subjectids" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.subjectids"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${subjectlist}" var="subject">
						<option value="${subject.id }" <c:if test="${subject.id == paramMap['_query.subjectids'] }">selected="selected"</c:if>>${subject.SUBJECT_NAME }</option>
					</c:forEach>
				</select>
				<label>${_res.get('type.of.class')}：</label>
				<select id="classtype" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.classtype"  >
					<option value="" selected="selected" >${_res.get('system.alloptions')}</option>
					<option value="1" <c:if test="${paramMap['_query.classtype'] == '1' }">selected="selected"</c:if>>${_res.get("IEP")}</option>
					<option value="0" <c:if test="${paramMap['_query.classtype'] == '0' }">selected="selected"</c:if>>${_res.get('course.classes')}</option>
				</select>
				<label>${_res.get("Opp.Lead.Status")}：</label>
				<select id="isconver" class="chosen-select" style="width: 150px;" tabindex="2" name="_query.isconver"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="0" <c:if test="${'0' == paramMap['_query.isconver'] }">selected="selected"</c:if>>${_res.get("Opp.No.follow-up")}</option>
					<option value="1" <c:if test="${1 == paramMap['_query.isconver'] }">selected="selected"</c:if>>${_res.get('Is.a.single')}</option>
					<option value="2" <c:if test="${2 == paramMap['_query.isconver'] }">selected="selected"</c:if>>${_res.get('Opp.Followed.up')}</option>
					<option value="3" <c:if test="${3 == paramMap['_query.isconver'] }">selected="selected"</c:if>>考虑中</option>
					<option value="4" <c:if test="${4 == paramMap['_query.isconver'] }">selected="selected"</c:if>>无意向</option>
					<%-- <option value="5" <c:if test="${5 == paramMap['_query.isconver'] }">selected="selected"</c:if>>已放弃</option> --%>
					<option value="6" <c:if test="${6 == paramMap['_query.isconver'] }">selected="selected"</c:if>>有意向</option>
				</select>
				<label>客户等级：</label>
				<select id="customer_rating" class="chosen-select" style="width: 140px;" tabindex="2" name="_query.customer_rating"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="0" <c:if test="${'0' == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>未知客户</option>
					<option value="1" <c:if test="${1 == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>潜在客户</option>
					<option value="2" <c:if test="${2 == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>目标客户</option>
					<option value="3" <c:if test="${3 == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>发展中客户</option>
					<option value="4" <c:if test="${4 == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>交易客户</option>
					<option value="5" <c:if test="${5 == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>后续介绍客户</option>
					<option value="6" <c:if test="${6 == paramMap['_query.customer_rating'] }">selected="selected"</c:if>>非客户</option>
				</select>
				<br>
				<br>
				<label>${_res.get("Opp.Sales.Source")}：</label>
				<select id="sourceid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.sourceid"  onchange="chooseSource(this.value)" >
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${sourceList}" var="source">
						<option value="${source.id }" <c:if test="${source.id == paramMap['_query.sourceid'] }">selected="selected"</c:if>>${source.name }</option>
					</c:forEach>
				</select>
				<div style="display:none" >
					<select id="sourcenames" >
					<c:forEach items="${sourceList}" var="source">
						<option value="sourcename${source.id }">${source.name }</option>
					</c:forEach>
				</select>
				</div>
				<span id="mediatorSearch" >
				<label>${_res.get("Opp.Channel")}：</label>
				<select id="mediatorid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.mediatorid">
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${mediatorList}" var="mediator">
						<option value="${mediator.id }" <c:if test="${mediator.id == paramMap['_query.mediatorid'] }">selected="selected"</c:if>>${mediator.realname }</option>
					</c:forEach>
				</select></span>
				<input type="button" onclick="sendMessage(9)" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				<c:if test="${operator_session.qx_opportunityaddOpportunity }">
					<%-- <input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick="addMyOpp()" class="btn btn-outline btn-info"> --%>
				</c:if>
				<input type="hidden" name="_query.isnoconver" id="isnoconver" value="">
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins" >
					<div class="ibox-title" style="float:left; width:100%;height:auto" >
						<input type="hidden" value="${paramMap['_query.valloted'] }" id="queryvalloted" name="_query.valloted" >
						<div >
							<c:if test="${operator_session.qx_opportunitysetRebackTime }">
								<input type="button" onclick="setrebacktime()" id="hasvalloted"  value="设置回访时间"  class="btn btn-info btn-sm">	
							</c:if>
							<c:if test="${operator_session.qx_opportunitydelmyVallot }">
								<input type="button" onclick="turntopool()" value="转销售池" id="novalloted" class="btn btn-warning btn-sm">
							</c:if>
							<div style="float: right;">
								<input type="button" id="s_0" onclick="sendMessage(0)" value="未成单"  class="btn btn-outline btn-primary btn-xs">
								<input type="button" id="s_1" onclick="sendMessage(1)" value="已成单"  class="btn btn-outline btn-primary btn-xs">
								<input type="button" id="s_4" onclick="sendMessage(4)" value="无意向"  class="btn btn-outline btn-primary btn-xs">
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th width="3%"><input type="checkbox" id="oppids" name="oppids" onchange="checkAllOpps()"></th>
									<th>${_res.get("Contacts")}</th>
									<th>${_res.get('gender')}</th>
									<th>${_res.get("admin.user.property.telephone")}</th>
									<!-- <th>邮箱</th> -->
									<!-- <th>QQ号码</th> -->
									<!-- <th>${_res.get('Opp.Entry.Date')}</th> -->
									<th width="5%">${_res.get('course.subject')}</th>
									<th>${_res.get('type.of.class')}</th>
									<th>就近校区</th>
									<th>下次回访日期</th>
									<th>关系</th>
									<th>来源</th>
									<th>客户等级</th>
									<th>${_res.get('admin.dict.property.status')}</th>
									<th>回访</th>
									<th width="16%">${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="opportunity" varStatus="index">
								<tr align="center">
									<td><c:if test="${opportunity.isconver!='1' }"><input type="checkbox" id="oppids${opportunity.id}" class="" name="opportunityids" value="${opportunity.id}" ></c:if></td>
									<td>
										<a href="#" style="color: #515151" onclick="showMessage(${opportunity.id})">${opportunity.contacter}</a>
									</td>
									<td>${opportunity.sex==true?_res.get('student.boy'):_res.get('student.girl')}</td>
									<td>${opportunity.phonenumber}</td>
									<%-- <td>${opportunity.email}</td> --%>
									<%-- <td>${opportunity.qq}</td> --%>
									<%-- <td><fmt:formatDate value="${opportunity.createtime}" type="time" timeStyle="full" pattern="yy-MM-dd"/></td> --%>
									<td>${opportunity.subjectnames}</td>
									<td>${opportunity.classtype==1?_res.get("IEP"):_res.get('course.classes')}</td>
									<td>${opportunity.campusname}</td>
									<td>
										<span id ="n_${opportunity.id}">
											<fmt:formatDate value="${opportunity.nextvisit}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/>
										</span>
									</td>
									<td>
										<c:choose>
											<c:when test="${opportunity.relation eq 1 }">本人</c:when>
											<c:when test="${opportunity.relation eq 2 }">母亲</c:when>
											<c:when test="${opportunity.relation eq 3 }">父亲</c:when>
											<c:otherwise>${_res.get('Else')}</c:otherwise>
										</c:choose>
									</td>
									<td>${opportunity.crmscid==1?opportunity.mediatorname:opportunity.crmscname}</td>
									<td>
										 <c:choose>
									        <c:when test="${opportunity.customer_rating eq '0' }"><span id ="r_${opportunity.id}" >未知客户</span></c:when>
									        <c:when test="${opportunity.customer_rating eq '1' }"><span id ="r_${opportunity.id}" >潜在客户</span></c:when>
									        <c:when test="${opportunity.customer_rating eq '2' }"><span id ="r_${opportunity.id}" >目标客户</span></c:when>
									        <c:when test="${opportunity.customer_rating eq '3' }"><span id ="r_${opportunity.id}" >发展中客户</span></c:when>
									        <c:when test="${opportunity.customer_rating eq '4' }"><span id ="r_${opportunity.id}" >交易客户</span></c:when>
									        <c:when test="${opportunity.customer_rating eq '5' }"><span id ="r_${opportunity.id}" >后续交易客户</span></c:when>
									        <c:when test="${opportunity.customer_rating eq '6' }"><span id ="r_${opportunity.id}" >非客户</span></c:when>
									    </c:choose>
									</td>
									<td>
									    <c:choose>
									        <c:when test="${opportunity.isconver eq 0 }"><label id ="z_${opportunity.id}" class="btn btn-info btn-xs">${_res.get("Opp.No.follow-up")}</label></c:when>
									        <c:when test="${opportunity.isconver eq 1 }"><label id ="z_${opportunity.id}" class="btn btn-success btn-xs">${_res.get('Is.a.single')}</label></c:when>
									        <c:when test="${opportunity.isconver eq 2 }"><label id ="z_${opportunity.id}" class="btn btn-primary btn-xs">跟进中</label></c:when>
									        <c:when test="${opportunity.isconver eq 3 }"><label id ="z_${opportunity.id}" class="btn btn-danger btn-xs">考虑中</label></c:when>
									        <c:when test="${opportunity.isconver eq 4 }"><label id ="z_${opportunity.id}" class="btn btn-default btn-xs">无意向</label></c:when>
									        <c:when test="${opportunity.isconver eq 5 }"><label id ="z_${opportunity.id}" class="btn btn-default btn-xs">已放弃</label></c:when>
									        <c:when test="${opportunity.isconver eq 6 }"><label id ="z_${opportunity.id}" class="btn btn-warning btn-xs">有意向</label></c:when>
									    </c:choose>
									</td>
									<td >
										<a onclick="feedBackRecord(${opportunity.id})" >
											<input type="button" readonly  style = "text-align: center" id ="s_${opportunity.id}" value="${opportunity.backtimes}" class="btn btn-success btn-outline btn-xs"/>
										</a>
									</td>
									<td>
										
										<c:if test="${operator_session.qx_opportunitygetDetailForJson }">
											<a href="#"  onclick="showDetail(${opportunity.id})">详情</a> |
										</c:if>
										<c:if test="${operator_session.qx_opportunityedit }">
											<a href="#"  onclick="window.location.href='/opportunity/edit/${opportunity.id}'">编辑</a> |
										</c:if>
										<c:if test="${operator_session.qx_opportunitywriteFeedBack }">
											<%-- <c:if test="${opportunity.isconver!='1' }"> --%>
											<a onclick="writeFeedBack(${opportunity.id}+',1')" >回访</a> | <%-- </c:if> --%>
										</c:if>
										<c:if test="${operator_session.qx_opportunitytoAddStudent }">
											<c:if test="${opportunity.isconver=='1' }"><a onclick="addstu(${opportunity.id})" >报名</a>  </c:if>
										</c:if>
										<c:if test="${operator_session.qx_opportunityisConver }">
											<c:if test="${opportunity.isconver ne '1' }"><a onclick="addstu(${opportunity.id})"> 成单</a></c:if>
										</c:if>
									</td>
								</tr>
								<input id="isconver_status_${opportunity.id}" type="hidden" value="${opportunity.isconver }">
							</c:forEach>
							
						</table>
						<div id="splitPageDiv">
							<jsp:include page="/common/splitPage.jsp" />
						</div>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
		</form>
	 </div>
	 </div>	
	</div>

	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
	$(".chosen-select").chosen({disable_search_threshold:30});
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    
    <script type="text/javascript">
    function sendMessage(code){
    	if(code==9){
    		$("#isnoconver").val('');
    	}else if(code!=9){
    		$("#isconver").val('');
    		if(code==0){
    			$("#isnoconver").val(0);
    		}else if(code==1){
    			$("#isnoconver").val(1);
    		}else{
    			$("#isnoconver").val(4);
    		}
    	}
    	$("#searchForm").attr("action", "/opportunity/myAllot");
        $("#searchForm").submit();	
    }
    /*添加我的销售机会*/
    function addMyOpp(){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "添加销售机会",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['1000px', '635px'],
    	    iframe: {src: '${cxt}/opportunity/addOpportunity/'+1}
    	});
    }
    function checkAllOpps(){
    	if($("#oppids").is(":checked")){
	    	$("[name='opportunityids']").each(function(){
	    		$(this).prop("checked","checked");
			});
    	}else{
	    	$("[name='opportunityids']").each(function(){
	    		$(this).prop("checked",false);
			});
    	}
    }
    /*我的销售机会中修改销售机会*/
    function modifyOpp(opportunityId){
    	var oppid = opportunityId.substr(opportunityId.length-1,opportunityId.length);
    	if(oppid=='0'){
    		str = "查看销售机会";
    	}else{
    		str="修改销售机会"
    	}
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title:str,
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['1000px', '635px'],
    	    iframe: {src: '${cxt}/opportunity/edit/'+opportunityId}
    	});
    }
    function setrebacktime(){
    	var oppIdValue = [];
		$('input[name="opportunityids"]:checked').each(function() {
			oppIdValue.push($(this).val());
		});
		if(oppIdValue.length==0){
			layer.msg("没有选择机会。");
			return false;
		}else{
	    	$.layer({
		        type: 2,
		        title: '下次回访日期',
		        maxmin: false,
		        shadeClose: true, //开启点击遮罩关闭层
		        area : ['360px' , '445px'],
		        offset : ['80px', ''],
		        iframe: {src: "/opportunity/setRebackTime"}
		    });
		}
    	
    }
    
    function turntopool(){
    	var oppIdValue = [];
		$('input[name="opportunityids"]:checked').each(function() {
			oppIdValue.push($(this).val());
		});
		
		if(oppIdValue.length==0){
			layer.msg("没有机会选择。");
			return false;
		}else{
			if(confirm('确定将机会转销售池？')){
	    		$.ajax({
	    			url:"${cxt}/opportunity/delVallot",
	    			type:"post",
	    			data:{
	    				"oppids" : oppIdValue.join(",")
	    				},
	    			dataType:"json",
	    			success:function(data){
	    				parent.layer.msg(data.msg,2,1);
	    				if(data.code == "1"){
	    					parent.window.location.reload();
	    				}
	    			}
	    		});
	    	}
			
		}
    }
    
    function writeFeedBack(id){
    	$.layer({
	        type: 2,
	        title: '新增回访记录',
	        maxmin: false,
	        shadeClose: true, //开启点击遮罩关闭层
	        area : ['460px' , '455px'],
	        offset : ['80px', ''],
	        iframe: {src: "/opportunity/writeFeedBack/"+id}
	    });
    }
    
    function addstu(opportunityId){
    	var isconver = $('#isconver_status_'+opportunityId).val();
    	 if(isconver=='1'){
    		if(confirm("该销售机会已添加过学生，是否继续添加学生？")){
    			$.layer({
       	    		type:2,
       	    		shadeClose:true,
       	    		title:'添加学生',
       	    		closeBtn:[0,true],
       	    		shade:[0.5,'#000'],
       	    		border:[0],
       	    		area:['900px','620px'],
       	    		iframe:{src:'${cxt}/opportunity/toAddStudent/'+opportunityId}
       	    	});
    		}
    	}else{ 
   	    	$.layer({
   	    		type:2,
   	    		shadeClose:true,
   	    		title:'添加学生',
   	    		closeBtn:[0,true],
   	    		shade:[0.5,'#000'],
   	    		border:[0],
   	    		area:['900px','620px'],
   	    		iframe:{src:'${cxt}/opportunity/toAddStudent/'+opportunityId}
   	    	});
    	}
    }
    function feedBackRecord(id){
    	$.layer({
	        type: 2,
	        title: '回访记录',
	        maxmin: false,
	        shadeClose: true, //开启点击遮罩关闭层
	        area : ['750px' , '400px'],
	        offset : ['80px', ''],
	        iframe: {src: "/opportunity/feedBackRecord/"+id}
	    });
    }
    function showDetail(opportunityId){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "销售机会信息",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['800px', '800px'],
    	    offset : ['50px', ''],
    	    iframe: {src: '${cxt}/opportunity/getDetailForJson/'+opportunityId}
    	});
    }
  /*   function feedBackRecord(id){
    	$.ajax({
			url:"${cxt}/opportunity/feedBackRecord",
			type:"post",
			data:{"opportunityId":id},
			dataType:"json",
			success:function(result){
				var record = "";
				if(result.length>0){
					for(var i=0;i<result.length;i++){
						var time = result[i].CREATETIME;
						var callstatus = result[i].CALLSTATUS=="1"?"已接通":result[i].CALLSTATUS=="2"?"未接通":result[i].CALLSTATUS=="3"?"再联系":result[i].CALLSTATUS=="4"?"通话中":result[i].CALLSTATUS=="5"?"空号":"";
						var callresult = result[i].CALLRESULT=="1"?"沟通中":result[i].CALLRESULT=="2"?"有意向":result[i].CALLRESULT=="3"?"无意向":result[i].CALLRESULT=="4"?"考虑中":result[i].CALLRESULT=="5"?"已报名":result[i].CALLRESULT=="6"?"已放弃":"";
						record += '<tr><td class="table-bg2">'+(i+1)+'</td><td class="table-bg2">'+result[i].REAL_NAME+'</td>';
						record += '<td class="table-bg2">'+time+'</td>';
						record += '<td class="table-bg2">'+callstatus+'</td>';
						record += '<td class="table-bg2">'+callresult+'</td>';
						record += '<td class="table-bg2" style="text-align:left">'+result[i].CONTENT+'</td></tr>';
					}
				}
				layer.tab({
				    data:[
				        {
				        	title: '回访记录', 
				        	content:'<table class="table table-bordered" style="text-align:center" >'
				        	+'<tr>'
				        	+'<td class="table-bg2">${_res.get("index")}</td><td class="table-bg2">课程顾问</td>'
				        	+'<td class="table-bg2">回访时间</td><td class="table-bg2">通话状态</td>'
				        	+'<td class="table-bg2">回访结果</td><td class="table-bg2">回访内容</td>'
				        	+'</tr>' +record+ '</table>'
				        }
				        
				    ],
					offset:['150px', ''],
				    area: ['600px', 'auto'] //宽度，高度
				});
			}
		});
    } */
    function showMessage(opportunityId){
    	$.ajax({
			url:"${cxt}/opportunity/getDetailForJson",
			type:"post",
			data:{"opportunityId":opportunityId},
			dataType:"json",
			success:function(result){
				layer.tab({
				    data:[
				        {
				        	title: '销售机会信息', 
				        	content:'<table class="table table-bordered">'
				        	+'<tr>'
				        	+'<td class="table-bg1">${_res.get("Contacts")}</td><td class="table-bg2">'+result.o.CONTACTER+'</td>'
				        	+'<td class="table-bg1">性别</td><td class="table-bg2">'+(result.o.SEX?'男':'女')+'</td>'
				        	+'<td class="table-bg1">咨询科目</td><td class="table-bg2">'+result.o.SUBJECTNAMES+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">电话号码</td><td class="table-bg2">'+result.o.PHONENUMBER+'</td>'
				        	+'<td class="table-bg1">学生关系</td><td class="table-bg2">'+(result.o.RELATION==1?'本人':(result.o.RELATION==2?'母亲':result.o.RELATION==3?'父亲':'其他'))+'</td>'
				        	+'<td class="table-bg1">主动联系</td><td class="table-bg2">'+(result.o.NEEDCALLED?'是':'否')+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">反馈次数</td><td class="table-bg2">'+result.o.FEEDBACKTIMES+'次</td>'
				        	+'<td class="table-bg1">成单状态</td><td class="table-bg2">'+(result.o.ISCONVER==0?"${_res.get('Opp.No.follow-up')}":result.o.ISCONVER==1?"${_res.get('Is.a.single')}":result.o.ISCONVER==2?"${_res.get('Opp.Followed.up')}":result.o.ISCONVER==3?"考虑中":result.o.ISCONVER==4?"无意向":"已放弃")+'</td>'
				        	+'<td class="table-bg1">成单日期</td><td class="table-bg2">'+(result.o.CONVERTIME==null?'':result.o.CONVERTIME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">${_res.get("Opp.Sales.Source")}</td><td class="table-bg2">'+(result.o.CRMSCNAME)+'</td>'
				        	+'<td class="table-bg1">咨询课程</td><td class="table-bg2">'+(result.o.CLASSTYPE?'一对一':'小班')+'</td>'
				        	+'<td class="table-bg1">结束时间</td><td class="table-bg2">'+(result.o.OVERTIME==null?'':result.o.OVERTIME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">所属留学顾问</td><td class="table-bg2">'+(result.o.MEDIATORNAME==null?'':result.o.MEDIATORNAME)+'</td>'
				        	+'<td class="table-bg1">所属市场</td><td class="table-bg2">'+(result.o.SCUSERNAME==null?'':result.o.SCUSERNAME)+'</td>'
				        	+'<td class="table-bg1">所属课程顾问</td><td class="table-bg2">'+(result.o.KCUSERNAME==null?'':result.o.KCUSERNAME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">录入时间</td><td class="table-bg2">'+result.o.CREATETIME+'</td>'
				        	+'<td class="table-bg1">更新时间</td><td class="table-bg2">'+(result.o.UPDATETIME==null?'':result.o.UPDATETIME)+'</td>'
				        	+'<td class="table-bg1">确认用户</td><td class="table-bg2">'+(result.o.CONFIRMUSERNAME==null?'':result.o.CONFIRMUSERNAME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">下次回访日期</td><td class="table-bg2">'+(result.o.NEXTVISIT==null?"":result.o.NEXTVISIT)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">备注</td><td colspan="5" class="table-bg2">'+(result.o.NOTE==null?'无':result.o.NOTE)+'</td>'
				        	+'</tr>'
				        	+'</table>'
				        }
				        
				    ],
					offset:['150px', ''],
				    area: ['600px', 'auto'] //宽度，高度
				});
			}
		});
    }
    function chengdan(opportunityId){
    	layer.confirm('确认该销售机会已成单吗？', function(){
    		$.ajax({
    			url:"${cxt}/opportunity/isConver",
    			type:"post",
    			data:{"opportunityId":opportunityId},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
						parent.window.location.reload();
						$('#isconver_'+opportunityId).text("${_res.get('Is.a.single')}");
						$('#isconver_status_'+opportunityId).val(true);
					}else{
    					layer.msg(data.msg, 2, 5);
						parent.window.location.reload();
					}
    			}
    		});
    	});
    }
    function updateSomeMessage(){
    	var str = writeFeedBack();
    	//var str = document.getElementById("s").value;
    	$("#ss").value(str);
    	$("#ss").trigger("chosen:updated");
    }
    var state = "${paramMap['_query.isnoconver']}";
    if(state==""){
    	state=2;
    }
  	 $("#s_"+state).removeClass("btn-outline");
    </script>
    <script type="text/javascript">
    	$(function(){
    		chooseSource(${paramMap['_query.sourceid'] });
    		if(${'1' == paramMap['_query.valloted'] }){
    			$("#againvallot").show();
    			$("#delvallot").show();
    			$("#vallot").hide();
    		}else{
    			if(${'0' == paramMap['_query.valloted'] }){
	    			$("#againvallot").hide();
	    			$("#delvallot").hide();
	    			$("#vallot").show();
    			}
    		}
    	});
    	
    	function chooseSource(id){
    		/* var nameid = "sourcename"+id
    		var name = $("#"+nameid).text();
    		alert(name); */
			if(1==id){
				$("#mediatorSearch").show();
			}else{
				$("#mediatorSearch").hide();
			}
		}
    	
    </script>
    <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		//日期范围限制
		var date1 = {
			elem : '#date1',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			istime : false,
			istoday : false,
			choose : function(datas) {
				date2.min = datas; //开始日选好后，重置结束日的最小日期
				date2.start = datas //将结束日的初始值设定为开始日
			}
		};
		var date2 = {
			elem : '#date2',
			format : 'YYYY-MM-DD',
			max : '2099-06-16',
			istime : false,
			istoday : false,
			choose : function(datas) {
				date1.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(date1);
		laydate(date2);
	</script>
    
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/sale.js"></script>
    <script>
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>