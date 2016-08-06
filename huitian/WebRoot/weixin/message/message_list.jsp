<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>消息列表</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet" />
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet" />
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet" />
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet" />
<link href="/css/css/animate.css" rel="stylesheet" />

<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<script type="text/javascript">
//改变公众号后查询对应事件
function getEventkeyListByWpnumberid(wpnumber){
		$.ajax({
			url:'/weixin/message/getEventkeyByWpnumberid',
			data :{
				"wpnumber":wpnumber
			},
			type:'post',
			dataType:'json',
			success:function(data){
					var str = "";
					for(var i=0;i<data.menuname.length;i++){
						var menuname = data.menuname[i].MENUNAME;
						var menukey = data.menuname[i].KEY;
						str += "<option value='"+menukey+"'>"+menuname+"</option>";
					}
					$("#eventkey").append(str);
					$("#eventkey").trigger("chosen:updated");	
			}
		});
}
</script>
<style type="text/css">
.chosen-container {
	margin-top: -3px;
}

.xubox_tabmove {
	background: #EEE;
}

.xubox_tabnow {
	color: #31708f;
}

input {
	border: 1px solid #e1e1e1
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

			<div class="margin-nav" style="width: 100%;">
				<form action="/weixin/message/list" method="post" id="searchForm">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
						    <div class="ibox-title">
								<h5>
								     <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
								     &gt;<a href='/weixin/wpnumber'>微信管理</a> &gt;  消息列表
							   </h5>
							   <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
							</div>
							<div class="ibox-content">
								<label>关注日期：</label>
								<input type="text" readonly="readonly" id="date1" name="_query.startDate" value="${paramMap['_query.startDate']}"
									style="background-color: #fff; width: 130px; cursor: not-allowed" />
								${_res.get('to')}<input type="text" readonly="readonly" id="date2" name="_query.endDate" value="${paramMap['_query.endDate']}" 
									style="background-color: #fff; width: 130px; cursor: not-allowed" /> 
									<br>
									<br>
								<label>所属公众号：</label> 
								<select name="_query.wpnumberid" id="wpnumber" class="chosen-select" style="width: 130px; height: 35px; border-color: #bababa;" onchange="getEventkeyListByWpnumberid(this.value);">
									<option value="">${_res.get('system.alloptions')}</option>
									<c:forEach items="${wpnumberList }" var="wpnumber">
										<option  value="${wpnumber.id }" <c:if test="${wpnumber.id == paramMap['_query.wpnumberid'] }">selected="selected"</c:if>>${wpnumber.accountname }</option>
									</c:forEach>
								</select> 
								<label>事件：</label> 
								<select name="_query.eventkey" id="eventkey" class="chosen-select" style="width: 150px; height: 35px; border-color: #bababa;">
									<option value="">${_res.get('system.alloptions')}</option>
									<c:forEach items="${menuList }" var="menu">
										<option  value="${menu.key }" <c:if test="${menu.key == paramMap['_query.eventkey'] }">selected="selected"</c:if>>${menu.menuname }</option>
									</c:forEach>
								</select> 
								<input type="button" onclick="search()" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">
								
							</div>
						</div>
					</div>

					<div class="col-lg-12" style="min-width: 680px">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>${_res.get('admin.user.list.table')}</h5>
							</div>
							<div class="ibox-content">
								<table class="table table-hover table-bordered" width="100%">
									<thead>
										<tr>
											<th rowspan="2">${_res.get("index")}</th>
											<th rowspan="2">所属公众号</th>
											<th rowspan="2">时间</th>
											<th rowspan="2">事件类型</th>
											<th rowspan="2">事件</th>
											<th rowspan="2">发送方帐号</th>
											<!-- 
											<th rowspan="2">${_res.get("operation")}</th>
											 -->
										</tr>
									</thead>
									<c:forEach items="${showPages.list}" var="message" varStatus="status">
										<tr class="odd" align="center">
											<td>${status.count}</td>
											<td>${message.accountname}</td>
											<td><fmt:formatDate value="${message.createdate}" type="time" timeStyle="full" pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td>${message.Event}</td>
											<td>${message.menuname}</td>
											<td>${message.nickname}</td>
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
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
	<script type="text/javascript">

</script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>

	<script>
    $(".chosen-select").chosen({disable_search_threshold: 30});
        var config = {
            '.chosen-select': {},
            '.chosen-select-deselect': {
                allow_single_deselect: true
            },
            '.chosen-select-no-single': {
                disable_search_threshold: 10
            },
            '.chosen-select-no-results': {
                no_results_text: 'Oops, nothing found!'
            },
            '.chosen-select-width': {
                width: "95%"
            }
        }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }   
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
       $('li[ID=nav-nav13]').removeAttr('').attr('class','active');
    </script>
	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
	<script>
         //日期范围限制
        var date1 = {
            elem: '#date1',
            format: 'YYYY-MM-DD',
            max: '2099-06-16', //最大日期
            istime: false,
            istoday: false,
            choose: function (datas) {
                date2.min = datas; //开始日选好后，重置结束日的最小日期
                date2.start = datas //将结束日的初始值设定为开始日
            }
        };
        var date2 = {
            elem: '#date2',
            format: 'YYYY-MM-DD',
            max: '2099-06-16',
            istime: false,
            istoday: false,
            choose: function (datas) {
                date1.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(date1);
        laydate(date2);
 </script>
	<script type="text/javascript">
    function setSysUserGroup(ids){
	    $.layer({
		    type: 2,
		    shadeClose: true,
		    title: "分组选择",
		    closeBtn: [0, true],
		    shade: [0.5, '#000'],
		    border: [0],
		    offset:['20px', ''],
		    area: ['700px', ($(window).height() - 50) +'px'],
		    iframe: {src: "${cxt}/operator/setSysUserGroup/"+ids}
		});
    }
    </script>
</body>
</html>