<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>销售分配</title>
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

	    <div class="margin-nav" style="min-width:1050px;width:100%;">	
		<form action="/opportunity/allot" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox-title">
				<h5>
					<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
					 &gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt; 销售分配
				</h5>
				<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
			 </div>
			  <div class="ibox float-e-margins">
				<div class="ibox-content">
				<label>${_res.get("Contacts")}：</label>
					<input type="text" id="contacter" name="_query.contacter" style="width:120px;" value="${paramMap['_query.contacter']}">
				<label>${_res.get("admin.user.property.telephone")}：</label>
					<input type="text" id="phonenumber" name="_query.phonenumber" style="width:120px;" value="${paramMap['_query.phonenumber']}">
				<label>${_res.get('Opp.Entry.Date')}：</label>
					<input type="text" class="form-control layer-date" readonly="readonly" id="date3" name="date3" value="${date3}" style="margin-top: -8px; width:120px; background-color: #fff;" />
				<label>分配日期：</label> 
					<input type="text" class="form-control layer-date" readonly="readonly" id="date1" name="date1" value="${date1}" style="margin-top: -8px; width:120px; background-color: #fff;" />
					<%-- <input type="text" class="form-control layer-date" readonly="readonly" id="date2" name="date2" value="${date2}" style="margin-top: -8px; width:120px; background-color: #fff;" />  --%>
				<label>就近校区：</label>
				<select id="campusid" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.campusid"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${campuslist}" var="campus">
						<option value="${campus.id }" <c:if test="${campus.id == paramMap['_query.campusid'] }">selected="selected"</c:if>>${campus.CAMPUS_NAME }</option>
					</c:forEach>
				</select>
				<br/>
				<br/>
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
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins" >
					<div class="ibox-title" style="float:left; width:100%;height:auto" >
						<input type="hidden" value="${paramMap['_query.valloted'] }" id="queryvalloted" name="_query.valloted" >
						<div style="float:left;" >
							<input type="button" onclick="showValloted()" id="hasvalloted"  value="已分配" class="btn btn-info btn-sm">	
							<input type="button" onclick="shownoValloted()" value="未分配" id="novalloted" class="btn btn-warning btn-sm">
						</div>
						<div style="float:right;" >
						<c:if test="${operator_session.qx_opportunitytoAllot }">
							<input type="button" onclick="allotFunction()" value="再分配" id="againvallot" class="btn btn-outline btn-primary btn-sm">
							<input type="button" onclick="allotFunction()" id="vallot" value="分配" class="btn btn-outline btn-info btn-sm">
						</c:if>
						<c:if test="${operator_session.qx_opportunitydelVallot }">
							<input type="button" onclick="delFunction()" value="取消分配" id="delvallot" class="btn btn-outline btn-warning btn-sm">
						</c:if>
						</div>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th><input type="checkbox" id="oppids" name="oppids" onchange="checkAllOpps()"></th>
									<th>${_res.get("index")}</th>
									<th>${_res.get("Contacts")}</th>
									<th>${_res.get('gender')}</th>
									<th>${_res.get("admin.user.property.telephone")}</th>
									<!-- <th>邮箱</th> -->
									<!-- <th>QQ号码</th> -->
									<th>${_res.get('Opp.Entry.Date')}</th>
									<th>${_res.get('course.subject')}</th>
									<th>${_res.get('type.of.class')}</th>
									<th>就近校区</th>
									<th>分配日期</th>
									<th>关系</th>
									<th>来源</th>
									<th>客户等级</th>
									<th>${_res.get('admin.dict.property.status')}</th>
									<th>所属课程顾问}</th>
									<!-- <th>${_res.get("operation")}</th> -->
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="opportunity" varStatus="index">
								<tr align="center">
									<td><input type="checkbox" id="oppids${opportunity.id}" class="" name="opportunityids" value="${opportunity.id}" ></td>
									<td>${index.count }</td>
									<td>
										<a href="#" style="color: #515151" onclick="showMessage(${opportunity.id})">${opportunity.contacter}</a>
									</td>
									<td>${opportunity.sex==true?_res.get('student.boy'):_res.get('student.girl')}</td>
									<td>${opportunity.phonenumber}</td>
									<%-- <td>${opportunity.email}</td> --%>
									<%-- <td>${opportunity.qq}</td> --%>
									<td><fmt:formatDate value="${opportunity.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>${opportunity.subjectnames}</td>
									<td>${opportunity.classtype==1?_res.get("IEP"):_res.get('course.classes')}</td>
									<td>${opportunity.campusname}</td>
									<td><fmt:formatDate value="${opportunity.vallottime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>
										<c:choose>
											<c:when test="${opportunity.relation eq 1 }">本人</c:when>
											<c:when test="${opportunity.relation eq 2 }">母亲</c:when>
											<c:when test="${opportunity.relation eq 3 }">父亲</c:when>
											<c:otherwise>${_res.get('Else')}</c:otherwise>
										</c:choose>
									</td>
									<td>${opportunity.crmscid==1?opportunity.mediatorname:opportunity.crmscname}</td>
									<td>${opportunity.customer_rating=='0'?'未知客户':opportunity.customer_rating=='1'?'潜在客户':opportunity.customer_rating=='2'?'目标客户':opportunity.customer_rating=='3'?'发展中客户':opportunity.customer_rating=='4'?'交易客户':opportunity.customer_rating=='5'?'后续交易客户':'非客户'}</td>
									<td>${opportunity.kcuserid eq null?'未分配':'已分配'}</td>
									<td>${opportunity.kcusername==null?'':opportunity.kcusername}</td>
									<%-- <td><a href="#" onclick ="modifyOpp(${opportunity.id})" >${_res.get('Modify')}</a></td> --%>
								</tr>
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
    
    
   function allotFunction(){
	   var oppIdValue = [];
		$('input[name="opportunityids"]:checked').each(function() {
			oppIdValue.push($(this).val());
		});
		
		if(oppIdValue.length==0){
			layer.msg("没有机会选择。");
			return false;
		}else{
			$.layer({
			    type: 2,
			    shadeClose: true,
			    title: "分配",
			    closeBtn: [0, true],
			    shade: [0.8, '#000'],
			    border: [0],
			    offset: ['60px',''],
			    area: ['700px','255px'],
			    iframe: {src: '${cxt}/opportunity/toAllot?oppIdValue='+oppIdValue}
			}); 
		}
   }
   
   function delFunction(){
	   var oppIdValue = [];
		$('input[name="opportunityids"]:checked').each(function() {
			oppIdValue.push($(this).val());
		});
		
		if(oppIdValue.length==0){
			layer.msg("没有机会选择。");
			return false;
		}else{
			if(confirm('确认取消选中机会的分配？')){
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
   
   function showValloted(){
	   $("#queryvalloted").val(1);
	   $("#searchForm").submit();
   }
   function shownoValloted(){
	   $("#queryvalloted").val(0);
	   $("#searchForm").submit();
   }
    
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
    	
    	 function modifyOpp(opportunityId){
    	    	$.layer({
    	    	    type: 2,
    	    	    shadeClose: true,
    	    	    title: "修改销售机会",
    	    	    closeBtn: [0, true],
    	    	    shade: [0.5, '#000'],
    	    	    border: [0],
    	    	    area: ['800px', '600px'],
    	    	    iframe: {src: '${cxt}/opportunity/edit/'+opportunityId}
    	    	});
    	    }
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
    </script>
    <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		//日期范围限制
		var date1 = {
			elem : '#date1',
			format : 'YYYY-MM-DD',
			min : '1970-01-01',
			//max : laydate.now(), //最大日期
			istime : false,
			istoday : false,
			choose : function(datas) {
				dianBegintime(datas);
			}
		};
		var date3 = {
				elem : '#date3',
				format : 'YYYY-MM-DD',
				min : '1970-01-01',
				//max : laydate.now(), //最大日期
				istime : false,
				istoday : false,
				choose : function(datas) {
					dianBegintime(datas);
				}
			};
			laydate(date1);
			laydate(date3);
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