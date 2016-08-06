<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>${_res.get('Opp.The.opportunity.to.list')}</title>
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
.laydate_bottom{
   height:30px !important
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

	    <div class="margin-nav" style="min-width:1050px;width:100%;">	
		<form action="/opportunity/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					     &gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt; ${_res.get('Opp.The.opportunity.to.list')}
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>${_res.get("Contacts")}：</label>
					<input type="text" id="contacter" name="_query.contacter" style="width: 100px" value="${paramMap['_query.contacter']}">
				<label>${_res.get("admin.user.property.telephone")}：</label>
					<input type="text" id="phonenumber" name="_query.phonenumber" style="width: 120px" value="${paramMap['_query.phonenumber']}">
				<label>${_res.get('Opp.Entry.Date')}：</label>
					 <input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.startDate" value="${paramMap['_query.startDate']}" style="margin-top: -8px; width:120px" /> 
					至<input type="text" class="layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px" />
				<br/>
				<br/>
				
				<label>${_res.get("Opp.Sales.Source")}：</label>
				<select id="sourceid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.source"  onchange="chooseSource(this.value)" >
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${sourceList}" var="source">
						<option value="${source.id }" <c:if test="${source.id == paramMap['_query.source'] }">selected="selected"</c:if>>${source.name }</option>
					</c:forEach>
				</select>
				<span id="mediatorSearch" >
				<label>${_res.get("Opp.Channel")}：</label>
				<select id="mediatorid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.mediatorid">
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${mediatorList}" var="mediator">
						<option value="${mediator.id }" <c:if test="${mediator.id == paramMap['_query.mediatorid'] }">selected="selected"</c:if>>${mediator.realname }</option>
					</c:forEach>
				</select></span>
							<input type="hidden" name="_query.sysuserid" value="${account_session.id }"  />
							<label>${_res.get("marketing.specialist")}：</label>
							<select id="scuserid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.scuserid">
								<option value="">${_res.get('system.alloptions')}</option>
								<c:forEach items="${sysUserList}" var="sysUser">
									<option value="${sysUser.id }" <c:if test="${sysUser.id == paramMap['_query.scuserid'] }">selected="selected"</c:if>>${sysUser.real_name }</option>
								</c:forEach>
							</select>
							<label>${_res.get("course.consultant")}：</label>
							<select id="kcuserid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.kcuserid">
								<option value="">${_res.get('system.alloptions')}</option>
								<c:forEach items="${sysUserList}" var="sysUser">
									<option value="${sysUser.id }" <c:if test="${sysUser.id == paramMap['_query.kcuserid'] }">selected="selected"</c:if>>${sysUser.real_name }</option>
								</c:forEach>
							</select>
				<label>${_res.get("Opp.Lead.Status")}：</label>
				<select id="isconver" class="chosen-select" style="width: 100px;" tabindex="2" name="_query.isconver"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="0" <c:if test="${'0' == paramMap['_query.isconver'] }">selected="selected"</c:if>>${_res.get("Opp.No.follow-up")}</option>
					<option value="1" <c:if test="${1 == paramMap['_query.isconver'] }">selected="selected"</c:if>>${_res.get('Is.a.single')}</option>
					<option value="2" <c:if test="${2 == paramMap['_query.isconver'] }">selected="selected"</c:if>>${_res.get('Opp.Followed.up')}</option>
					<option value="3" <c:if test="${3 == paramMap['_query.isconver'] }">selected="selected"</c:if>>考虑中</option>
					<option value="4" <c:if test="${4 == paramMap['_query.isconver'] }">selected="selected"</c:if>>无意向</option>
					<option value="5" <c:if test="${5 == paramMap['_query.isconver'] }">selected="selected"</c:if>>已放弃</option>
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
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				<!--  -->
				<c:if test="${operator_session.qx_opportunityaddOpportunity }">
				<%-- <input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick="addOpp()" class="btn btn-outline btn-info"> --%>
				<input type="button" value="导入" onclick="importTch()" class="btn btn-outline btn-primary" >
				</c:if>
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>销售列表</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("Contacts")}</th>
									<th>${_res.get('gender')}</th>
									<th width="10%"> ${_res.get('Opp.Entry.Date')}</th>
									<th>${_res.get("admin.user.property.telephone")}</th>
									<th width="5%">关系</th>
									<th>${_res.get('course.subject')}</th>
									<th width="6%">主动联系</th>
									<th width="8%">就近校区</th>
									<th width="11%">${_res.get("Opp.Channel")}</th>
									<th width="6%">${_res.get("marketing.specialist")}</th>
									<th width="6%">${_res.get("course.consultant")}</th>
									<th width="8%">客户等级</th>
									<th>反馈</th>
									<th>${_res.get("Opp.Lead.Status")}</th>
									<th width="10%">${_res.get("operation")}</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="opportunity" varStatus="index">
								<tr align="center">
									<td>${opportunity.contacter}</td>
									<td>${opportunity.sex==true?_res.get('student.boy'):_res.get('student.girl')}</td>
									<td><fmt:formatDate value="${opportunity.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>${opportunity.phonenumber}</td>
									<td>
										<c:choose>
											<c:when test="${opportunity.relation eq 1 }">本人</c:when>
											<c:when test="${opportunity.relation eq 2 }">母亲</c:when>
											<c:when test="${opportunity.relation eq 3 }">父亲</c:when>
											<c:otherwise>${_res.get('Else')}</c:otherwise>
										</c:choose>
									</td>
									<td style="text-align: left;">${opportunity.subjectnames}</td>
									<td>${opportunity.needcalled==true?_res.get('admin.common.yes'):_res.get('admin.common.no')}</td>
									<td>${opportunity.campusid==null?'':opportunity.campus_name}</td>
									<td>${opportunity.crmscid==1?opportunity.mediatorname:opportunity.crmscname}</td>
									<td>${opportunity.scusername==null?'':opportunity.scusername}</td>
									<td>${opportunity.kcusername==null?'':opportunity.kcusername}</td>
									<td>${opportunity.customer_rating=='0'?'未知客户':opportunity.customer_rating=='1'?'潜在客户':opportunity.customer_rating=='2'?'目标客户':opportunity.customer_rating=='3'?'发展中客户':opportunity.customer_rating=='4'?'交易客户':opportunity.customer_rating=='5'?'后续交易客户':'非客户'}</td>
									<td><a class="btn btn-success btn-outline btn-xs"  onclick="feedBackRecord(${opportunity.id})" >${opportunity.feedbacktimes}</a></td>
									<td id="isconver_${opportunity.id}">
									  <c:choose>
									        <c:when test="${opportunity.isconver eq '0' }"><label id ="z_${opportunity.id}" class="btn btn-info btn-xs">${_res.get("Opp.No.follow-up")}</label></c:when>
									        <c:when test="${opportunity.isconver eq '1' }"><label id ="z_${opportunity.id}" class="btn btn-success btn-xs">${_res.get('Is.a.single')}</label></c:when>
									        <c:when test="${opportunity.isconver eq '2' }"><label id ="z_${opportunity.id}" class="btn btn-primary btn-xs">${_res.get('Opp.Followed.up')}</label></c:when>
									        <c:when test="${opportunity.isconver eq '3' }"><label id ="z_${opportunity.id}" class="btn btn-danger btn-xs">考虑中</label></c:when>
									        <c:when test="${opportunity.isconver eq '4' }"><label id ="z_${opportunity.id}" class="btn btn-default btn-xs">无意向</label></c:when>
									        <c:when test="${opportunity.isconver eq '5'}"><label id ="z_${opportunity.id}" class="btn btn-default btn-xs">已放弃</label></c:when>
									        <c:when test="${opportunity.isconver eq '6' }"><label id ="z_${opportunity.id}" class="btn btn-warning btn-xs">有意向</label></c:when>
									    </c:choose>
									</td>
									<td>
										<%-- <c:if test="${operator_session.qx_opportunityfeedList }">
											<c:if test="${opportunity.oppid ne null}">
												<a href="/student/index?_query.oppid=${opportunity.id}" style="color: #515151" >${_res.get('student')}</a>|
											</c:if>
										</c:if> --%>
										<c:if test="${operator_session.qx_opportunitygetDetailForJson }">
										<a href="javascript:void(0)" style="color: #515151" onclick="showDetail(${opportunity.id})">详情</a> |
										</c:if>
										<c:if test="${operator_session.qx_opportunityedit }">
										<a href="javascript:void(0)" onclick="window.location.href='/opportunity/edit/${opportunity.id}'" style="color: #515151">${_res.get('Modify')}</a>
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
	
	<script type="text/javascript">
	function importTch(){
	$.layer({
	    type: 2,
	    shadeClose: true,
	    title: "导入机会",
	    closeBtn: [0, true],
	    shade: [0.5, '#000'],
	    border: [0],
	    offset:['20px', ''],
	    area: ['500px', '200px'],
	    iframe: {src: "/opportunity/toImportPage"}
	});
}
	</script>
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
 /*   function feedback(opportunityId){
    	layer.prompt({title: '销售机会反馈',type: 3,length: 200}, function(val,index){
    		$.ajax({
    			url:"${cxt}/opportunity/saveFeedback",
    			type:"post",
    			data:{"opportunityId":opportunityId,"content":val},
    			dataType:"json",
    			success:function(data){
    				if(data.result){
    					 layer.msg('反馈信息保存成功！', 1, 1);
    					 layer.close(index)
    				}else{
    					alert("反馈信息保存失败！");
    				}
    			}
    		});
		});
    } **/
    function feedBackRecord(id){
    	$.layer({
	        type: 2,
	        title: '回访记录',
	        maxmin: false,
	        shadeClose: true, //开启点击遮罩关闭层
	        area : ['800px' , '400px'],
	        offset : ['80px', ''],
	        iframe: {src: "/opportunity/feedBackRecord/"+id}
	    });
    }
    function checkOppAllStudent(id){
    	$.layer({
	        type: 2,
	        title: '回访记录',
	        maxmin: false,
	        shadeClose: true, //开启点击遮罩关闭层
	        area : ['800px' , '400px'],
	        offset : ['80px', ''],
	        iframe: {src: "/student/checkOppAllStudent/"+id}
	    });
    }
    function addOpp(){
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "添加销售机会",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['1000px', '635px'],
    	    iframe: {src: '${cxt}/opportunity/addOpportunity/'+2}
    	});
    }
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
    
    function feedback(opportunityId){
	    	$.layer({
	    	    type: 2,
	    	    shadeClose: true,
	    	    title: "反馈详情",
	    	    closeBtn: [0, true],
	    	    shade: [0.5, '#000'],
	    	    border: [0],
	    	    area: ['600px', '480px'],
	    	    iframe: {src: '${cxt}/opportunity/feedList/'+opportunityId}
	    	});
    }
    
    function isconver(opportunityId){
    	layer.confirm('确认该销售机会已成单吗？', function(){
    		$.ajax({
    			url:"${cxt}/opportunity/isConver",
    			type:"post",
    			data:{"opportunityId":opportunityId},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
						$('#isconver_'+opportunityId).text("${_res.get('Is.a.single')}");
						$('#isconver_status_'+opportunityId).val(true);
					}else{
    					layer.msg(data.msg, 2, 5);
					}
    			}
    		});
    	});
    }
    function addstu(opportunityId){
    	var isconver = $('#isconver_status_'+opportunityId).val();
    	if(isconver=='false'){
    		layer.msg("未成单不能添加学生！", 2, 5);
    	}else{
    		window.location.href='${cxt}/opportunity/toAddStudent/'+opportunityId;
    	}
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
    	    offset : ['40px', ''],
    	    iframe: {src: '${cxt}/opportunity/getDetailForJson/'+opportunityId}
    	});
    }
    
    </script>
    <script type="text/javascript">
    	$(function(){
    		if(${1 == paramMap['_query.source'] }){
    			$("#mediatorSearch").show();
    		}else{
    			$("#mediatorSearch").hide();
    		}
    	});
    	
    	function chooseSource(id){
			if(id==1){
				$("#mediatorSearch").show();
			}else{
				$("#mediatorSearch").hide();
			}
		}
    	
    </script>
    
    
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/sale.js"></script>
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
    <script>
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>