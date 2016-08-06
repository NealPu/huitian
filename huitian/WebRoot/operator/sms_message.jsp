<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>短信记录</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
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
.laydate_body .laydate_bottom{
    height:30px !important
}
.laydate_body .laydate_top{
    padding:0 !important
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
		<form action="" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
			    <div class="ibox-title">
					<h5>
					     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
					      &gt;<a href='/smsmessage/index'>短信管理</a> &gt;短信记录
				   </h5>
				   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
				<div class="ibox-content">
				<label>接收人号码：</label>
					<input type="text" id="recipient_tel" name="_query.recipient_tel" value="${paramMap['_query.recipient_tel']}">
				<label>${_res.get('Transmission.time')}：</label>
				<input type="text" class="layer-date" readonly="readonly" id="date1" name="_query.send_time" value="${paramMap['_query.send_time']}" style="margin-top: -8px; width:120px" />
				<label>发送状态：</label>
				<select id="send_state" class="chosen-select" style="width: 150px;" tabindex="2" name="_query.send_state"   >
					<option value="">${_res.get('system.alloptions')}</option>
					<option value="0" <c:if test="${'0' == paramMap['_query.send_state'] }">selected="selected"</c:if>>失败 </option>
					<option value="1" <c:if test="${1 == paramMap['_query.send_state'] }">selected="selected"</c:if>>成功</option>
				</select>
				<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
				
			</div>
			</div>
			</div>
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>短信记录</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th width="5%">${_res.get("index")}</th>
									<th width="10%">接收人号码</th>
									<th width="5%">接收人</th>
									<th width="10%">${_res.get('Transmission.time')}</th>
									<th width="5%">发送状态</th>
									<th width="30%">发送内容</th>
									<th width="20%">结果描述</th>
									<th width="10%">操作</th>
								</tr>
							</thead>
							<c:forEach items="${showPages.list}" var="operator" varStatus="index">
								<tr>
									<td>${index.count }</td>
									<td>${operator.recipient_tel }</td>
									<td>${operator.recipient_type==1?'本人':operator.recipient_type==2?_res.get('Patriarch'):operator.recipient_type==3?_res.get('teacher'):operator.recipient_type==4?'员工':operator.recipient_type==5?'留学顾问':''}</td>
									<td><fmt:formatDate value="${operator.send_time}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
									<td>${operator.send_state ==1?'成功':'失败'}</td>
									<td>${operator.send_msg }</td>
									<td>${operator.errordescription }</td>
									<td>
										<c:if test="${operator.send_state ==0}">
											<a href="javascript:void(0)"  onclick="aginSend(${operator.id})">重新发送</a>
										</c:if>
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
    
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/campus.js"></script>
    <script>
       $('li[ID=nav-nav18]').removeAttr('').attr('class','active');
    </script>
         <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
    <script type="text/javascript">
	    var date1 = {
				elem : '#date1',
				format : 'YYYY-MM-DD',
				max : '2099-06-16', //最大日期
				istime : false,
				istoday : false,
			};
		laydate(date1);
    </script>
     <script type="text/javascript">
/*重新发送失败的短信*/     	
     	function aginSend(id){
     		$.ajax({
     			url:'/smsmessage/aginSend',
     			type:'post',
     			data:{"id":id},
     			dataType:'json',
     			success:function(data){
     				if(data.code==1){
     					layer.msg("发送成功",1,1);
     					parent.window.location.reload();
     				}else{
     					layer.msg("发送失败",1,2);
     				}
     			}
     		});
     	}
     </script>
</body>
</html>