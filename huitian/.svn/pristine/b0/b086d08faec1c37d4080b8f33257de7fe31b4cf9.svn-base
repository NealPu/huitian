<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>销售池</title>
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
		<form action="/opportunity/pool" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					    <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
					      &gt; <a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt; 销售池
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>${_res.get("Contacts")}：</label>
					<input type="text" id="contacter" name="_query.contacter" value="${paramMap['_query.contacter']}" style="width:120px">
				<label>${_res.get("admin.user.property.telephone")}：</label>
					<input type="text" id="phonenumber" name="_query.phonenumber" value="${paramMap['_query.phonenumber']}" style="width:120px">
				<label>${_res.get('Opp.Entry.Date')}：</label> 
					<input type="text" class="form-control layer-date" readonly="readonly" id="date1" name="_query.startDate" value="${paramMap['_query.startDate']}" style="margin-top: -8px; width:120px; background-color: #fff;" /> 
					至<input type="text" class="form-control layer-date" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" style="margin-top: -8px; width:120px; background-color: #fff;" />
				<label>就近校区：</label>
				<select id="campus" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.campusid" >
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${campuslist}" var="campus">
						<option value="${campus.Id }" <c:if test="${campus.Id == paramMap['_query.campusid'] }">selected="selected"</c:if>>${campus.campus_name }</option>
					</c:forEach>
				</select>
				<br/>
				<br/>
				<label>${_res.get('course.subject')}：</label>
				<select id="subject" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.subjectids">
					<option value="">${_res.get('system.alloptions')}</option>
					<c:forEach items="${subjectlist }" var="subject" >
					      <option value="${subject.Id }" <c:if test="${subject.Id == paramMap['_query.subjectids'] }">selected="selected"</c:if>>${subject.subject_name }</option> 
					</c:forEach>
				</select>
				<label>${_res.get('type.of.class')}：</label>
				<select id="classtype" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.classtype">
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="1" <c:if test="${'1' == paramMap['_query.classtype'] }">selected="selected"</c:if>>${_res.get("IEP")}</option>
					<option value="0" <c:if test="${'0' == paramMap['_query.classtype'] }">selected="selected"</c:if>>${_res.get('course.classes')}</option>
				</select>
				<label>关系：</label>
				<select id="relation" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.relation">
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="1" <c:if test="${1 == paramMap['_query.relation'] }">selected="selected"</c:if>>本人</option>
					<option value="2" <c:if test="${2 == paramMap['_query.relation'] }">selected="selected"</c:if>>母亲</option>
					<option value="3" <c:if test="${3 == paramMap['_query.relation'] }">selected="selected"</c:if>>父亲</option>
					<option value="4" <c:if test="${4 == paramMap['_query.relation'] }">selected="selected"</c:if>>其他</option>
				</select>
				<label>线索来源：</label>
				<select id="source" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.source">
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="1" <c:if test="${1 == paramMap['_query.source'] }">selected="selected"</c:if>>${_res.get("Opp.Channel")}</option>
					<option value="2" <c:if test="${2 == paramMap['_query.source'] }">selected="selected"</c:if>>网络</option>
					<option value="3" <c:if test="${3 == paramMap['_query.source'] }">selected="selected"</c:if>>400${_res.get("admin.user.property.telephone")}</option>
					<option value="4" <c:if test="${4 == paramMap['_query.source'] }">selected="selected"</c:if>>到访</option>
					<option value="5" <c:if test="${5 == paramMap['_query.source'] }">selected="selected"</c:if>>第三方</option>
					<option value="6" <c:if test="${6 == paramMap['_query.source'] }">selected="selected"</c:if>>转介绍</option>
					<option value="7" <c:if test="${7 == paramMap['_query.source'] }">selected="selected"</c:if>>其他</option>
				</select>
				<br><br>
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
				<%-- <label>${_res.get("Opp.Lead.Status")}：</label>
				<select id="isconver" class="chosen-select" style="width: 120px;" tabindex="2" name="_query.isconver" >
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="4" <c:if test="${4 == paramMap['_query.isconver'] }">selected="selected"</c:if>>无意向</option>
					<option value="5" <c:if test="${5 == paramMap['_query.isconver'] }">selected="selected"</c:if>>已放弃</option>
				</select><br><br> --%>
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary" >&nbsp;&nbsp;
				
			</div>
			</div>
			</div>
			</form>
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title" style="height:auto">
						<c:if test="${operator_session.qx_opportunityfenpei }">
							<input type="button" value="分配" class="btn btn-info btn-sm"  onclick="fenpei()">&nbsp;&nbsp;
						</c:if>
						<c:if test="${operator_session.qx_opportunitylingqu }">
							<input type="button" value="领取"  onclick="lingqu()" class="btn btn-warning btn-sm">
						</c:if>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
								    <th><input type="checkbox" id="check" onchange="xuanze()" ></th>
									<th>${_res.get("index")}</th>
									<th>${_res.get("Contacts")}</th>
									<th>${_res.get('gender')}</th>
									<th>${_res.get('Opp.Entry.Date')}</th>
									<th>${_res.get("admin.user.property.telephone")}</th>
									<!-- <th>电子邮箱</th> -->
									<!-- <th>QQ号码</th> -->
									<th>${_res.get('course.subject')}</th>
									<th>${_res.get('type.of.class')}</th>
									<th>就近校区</th>
									<th>关系</th>
									<th>${_res.get("Opp.Lead.Status")}</th>
									<th>来源</th>
									<th>客户等级</th>
									<th>回访次数</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="opportunity" varStatus="index">
								<tr align="center">
								    <td><input type="checkbox" id="box" value="${opportunity.id }"></td>
									<td>${index.count}</td>
									<td>
										<a href="#" style="color: #515151" onclick="showMessage(${opportunity.id})">${opportunity.contacter}</a>
									</td>
									<td>${opportunity.sex==true?_res.get('student.boy'):_res.get('student.girl')}</td>
									<td><fmt:formatDate value="${opportunity.createtime}" type="time" timeStyle="full" pattern="yyyy-MM-dd"/></td>
									<td>${opportunity.phonenumber}</td>
									<%-- <td>${opportunity.email}</td> --%>
									<%-- <td>${opportunity.qq}</td> --%>
									<td>${opportunity.subjectnames}</td>
									<td>${opportunity.classtype==1?_res.get("IEP"):_res.get('course.classes')}</td>
									<td>${opportunity.campus_name}</td>
									<td>
										<c:choose>
											<c:when test="${opportunity.relation eq 1 }">本人</c:when>
											<c:when test="${opportunity.relation eq 2 }">母亲</c:when>
											<c:when test="${opportunity.relation eq 3 }">父亲</c:when>
											<c:otherwise>${_res.get('Else')}</c:otherwise>
										</c:choose>
									</td>
									<td>${opportunity.isconver==4?"无意向":"已放弃"}</td>
									<td>${opportunity.crmscid==1?opportunity.mediatorname:opportunity.crmscname}</td>
									<td>${opportunity.customer_rating=='0'?'未知客户':opportunity.customer_rating=='1'?'潜在客户':opportunity.customer_rating=='2'?'目标客户':opportunity.customer_rating=='3'?'发展中客户':opportunity.customer_rating=='4'?'交易客户':opportunity.customer_rating=='5'?'后续交易客户':'非客户'}</td>
									<td><a onclick="feedBackRecord(${opportunity.id})"  class="btn btn-success btn-outline btn-xs">${opportunity.feedbacktimes}</a></td>
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
	        area : ['750px' , '455px'],
	        offset : ['80px', ''],
	        iframe: {src: "/opportunity/feedBackRecord/"+id}
	    });
    }
    function xuanze(){
    	if($("#check").is(":checked")){
    		$("[id='box']").each(function(){
    			$(this).prop("checked","checked");
    		})
    	}else{
    		$("[id='box']").each(function(){
    			$(this).prop("checked",false);
    		})
    	}
    }
    function lingqu(){
    	var opp=[];
    	$("input[id='box']:checked").each(function(){
    		opp.push($(this).val());
    	})
    	if(opp.length==0){
    		layer.msg("没有机会选择");
    		return false;
    	}
    	layer.confirm('确认领取机会吗？', function(){
    		$.ajax({
    			url:"${cxt}/opportunity/lingqu?oppids="+opp,
    			type:"post",
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
    					parent.window.location.reload();
					}else{
    					layer.msg(data.msg, 2, 5);
					}
    			}
    		});
    	});
    	
    }
    
    function fenpei(){
    	var oppIdValue=[];
    	$("input[id='box']:checked").each(function(){
    		oppIdValue.push($(this).val());
    	})
    	if(oppIdValue.length==0){
    		layer.msg("没有机会选择");
    		return false;
    	}
    	$.layer({
    	    type: 2,
    	    shadeClose: true,
    	    title: "分配",
    	    closeBtn: [0, true],
    	    shade: [0.5, '#000'],
    	    border: [0],
    	    area: ['600px', '480px'],
    	    iframe: {src: '${cxt}/opportunity/toAllot?oppIdValue='+oppIdValue}
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