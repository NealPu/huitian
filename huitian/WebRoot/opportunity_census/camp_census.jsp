<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<!-- 回到顶部 -->
<link type="text/css" href="/css/lrtk.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/js.js"></script>

<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>校区统计</title>
<style>
  .datepicker-orient-top{
    top:180px !important
  }
</style>

</head>
<body>
	<input type="hidden" value="${campsusStr}" id="campsusStr"/>
	<input type="hidden" value="${seriesStr}" id="seriesStr"/>
	<div id="wrapper" style="background: #2f4050; height: 100%;">
		<div class="left-nav"><%@ include file="/common/left-nav.jsp"%></div>
		<div class="gray-bg dashbard-1" id="page-wrapper">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top" role="navigation" style="margin-left: -15px; position: fixed; width: 100%; background-color: #fff;border:0">
					<%@ include file="/common/top-index.jsp"%>
				</nav>
			</div>

			<div class="margin-nav1">
				<div class="ibox float-e-margins">
				    <div class="ibox-title">
						<h5>
						   <img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">首页</a> 
						  &gt;<a href='/teacher/index?_query.state=0'>销售统计</a> &gt;校区统计
						</h5>
						<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
					</div>
					<form action="/opportunitycensus/opportunityCensusByCampus" method="post" id="campuscensusForm">
					<div class="ibox-content">
					  <div style="float: left;margin:0 10px 0 0">
						<label> ${_res.get("system.campus")}： </label> 
						<select id="teacherids" name="teacherids" data-placeholder="${_res.get('Please.select')}（${_res.get('Multiple.choice')}）" class="chosen-select" multiple style="width: 300px;" tabindex="4">
							<c:forEach items="${campusList}" var="teacher">
								<option value="${teacher.id}" class="options" id="tid_${teacher.id }">${teacher.campus_name}</option>
							</c:forEach>
						</select> <input id="tids" name="teachergroup.teacherids" value="" type="hidden">
					   </div>
						<div class="form-group" id="data_4" style="float: left;">
                            <label style="float: left;margin-top:6px">选择月份：</label>
                            <div style="float: left;">
	                            <div class="input-group date" style="width:180px;float: left;">
	                                <input id="yuefen2" type="text" name="yuefen2" class="form-control" value="${yuefen2}">
	                                <span class="input-group-addon" style="display: none;"></span>
	                            </div><div style="width:30px;height:30px;line-height:30px;text-align:center;background:#E5E6E7;float: left;">到</div>
	                            <div class="input-group date" style="width:180px;float: left;">
	                                <input id="yuefen1" type="text" name="yuefen1" class="form-control" value="${yuefen1}" style="float: left;">
	                                <span class="input-group-addon" style="display: none;"></span>
	                            </div>
	                        </div>    
                       </div>
                       <input type="button" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-info" id="" onclick="campuscensusForm()" style="margin-left:10px">
					</div>
					
					<div style="background:#fff;margin-top:30px;padding:15px">
					   <div id="main" style="height:400px;"></div>
					</div>
					
					<div class="ibox-content" style="margin-top:30px;width:600px">
						
							<fieldset>
							   <table class="table table-hover table-bordered">
									<thead>
										<tr align="center">
										   <th>月份</th>
										   <th>${_res.get("system.campus")}</th>
										   <th>咨询量</th>
										   <th>成单量</th>
										   <th>操作</th>
										</tr>
									 </thead>
									 <c:forEach items="${monthmap}" var="month" varStatus="status">
										<tr align="center">
											<td  rowspan="${fn:length(month.value)+1}">${month.key}</td>
											<c:forEach items="${month.value}" var="campus">
											<tr>
												<td>${campus.campus_name}</td>
												<td>${campus.ZXL}</td>
												<td>${campus.CDL}</td>
												<td>
												   <a href="javascript:void(0)" onclick="tongji('${month.key}',${campus.message})">统计明细</a>
												</td>
											</tr>
											</c:forEach>
										</tr>
									</c:forEach>
								</table>
							</fieldset>
					</div>
				</form>
				</div>
				<div style="clear: both;"></div>
			</div>
		</div>
	</div>
	<div id="tbox">
		<a id="gotop" href="javascript:void(0)"></a>
	</div>
	<!-- Mainly scripts -->
	<script src="/js/js/jquery-2.1.1.min.js"></script>

	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script src="/js/utils.js"></script>
	<!-- Mainly scripts -->
	<script src="/js/js/bootstrap.min.js?v=1.7"></script>
	<script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/js/js/hplus.js?v=1.7"></script>
	<!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script src="/js/js/demo/layer-demo.js"></script>
	<script type="text/javascript">
	    function tongji(yuefen,id){
	    	var cord = 0;
		    $.layer({
			    type: 2,
			    shadeClose: true,
			    title: "来源统计明细",
			    closeBtn: [0, true],
			    shade: [0.5, '#000'],
			    border: [0],
			    offset:['20px', ''],
			    area: ['700px', '200px'],
			    iframe: {src: "${cxt}/opportunitycensus/layerCensus?code="+cord+"&yuefen="+yuefen+"&message="+id}
			});
	    }
    </script>
	<!-- map -->
    <script src="/js/js/top-nav/sale-1.js"></script>
	<script src="/js/echarts.js"></script>
    <script type="text/javascript">
    //校区统计图
        require.config({
            paths: {
                echarts: '/js/dist'
            }
        });
     // 使用
        require(
            [
                'echarts',
                'echarts/chart/line',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
            	
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main'),'macarons'); 
                var option = {
						title : {
							text: '分校区统计',
							subtext: '情况如下：'
						},
						tooltip : {
							trigger: 'axis'
						},
						legend: {
							
							data:[
'${campsusStr}'
								//'A校区咨询量','B校区咨询量','C校区咨询量','',
								//'A校区成单量','B校区成单量','C校区成单量'
							]
						},
						toolbox: {
							show : true,
							feature : {
								mark : {show: true},
								dataView : {show: true, readOnly: false},
								magicType : {show: true, type: ['line', 'bar']},
								restore : {show: true},
								saveAsImage : {show: true}
							}
						},
						calculable : true,
						grid: {y: 70, y2:30, x2:20},
						xAxis : [
							{
								type : 'category',
								data : [${yuefenStr}]
							},
							{
								type : 'category',
								axisLine: {show:false},
								axisTick: {show:false},
								axisLabel: {show:false},
								splitArea: {show:false},
								splitLine: {show:false},
								data : [${yuefenStr}]
							}
						],
						yAxis : [
							{
								type : 'value',
								axisLabel:{formatter:'{value} 个'}
							}
						],
						series : [${seriesStr}]
						
					};
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );
    </script>
	<script type="text/javascript">
	function campuscensusForm() {
		getIds();
		$("#campuscensusForm").attr("action", "/opportunitycensus/opportunityCensusByCampus");
		$("#campuscensusForm").submit();
	}
	//获取下拉菜单的值
	function getIds() {
		var teacherids = "";
		var list = document.getElementsByClassName("search-choice");
		for (var i = 0; i < list.length; i++) {
			var name = list[i].children[0].innerHTML;
			var olist = document.getElementsByClassName("options");
			for (var j = 0; j < olist.length; j++) {
				var oname = olist[j].innerHTML;
				if (oname == name) {
					teacherids += "|" + olist[j].getAttribute('value');
					break;
				}
			}
		}
		$("#tids").val(teacherids);
	}
</script>
<script type="text/javascript">
var ids = '${teacheredit}';
//多选select 数据初始化
function chose_mult_set_ini(select, values){
    var arr = values.split('|');
    var length = arr.length;
    var value = '';
    for(i=0;i<length;i++){
        value = arr[i];
        $(select+" [value='"+value+"']").attr('selected','selected');
    }
    $(select).trigger("chosen:updated");
}

 $(document).ready(function () {
     chose_mult_set_ini('#teacherids',ids);
     $(".chosen-select").chosen();
 });
</script>
	<script>
		
		//-----------------下拉菜单插件
		$(".chosen-select").chosen({
			disable_search_threshold : 20
		});
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
		$('li[ID=nav-nav19]').removeAttr('').attr('class', 'active');
	</script>
	<!-- Data picker -->
    <script src="/js/js/plugins/datapicker/bootstrap-datepicker.js"></script>
	<script>
	$(document).ready(function () {
        $('#data_4 .input-group.date').datepicker({
        	format:'yyyy-mm',
            minViewMode: 1,
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true,
            todayHighlight: true,
        });
    });
    
	$("#yuefen2").change(function(){
		var d = new Date();
		var mon = d.getFullYear()+'-'+(d.getMonth()+1)+'-01 00:00:00';
		var mon2 = $("#yuefen2").val()+'-01 00:00:00';
		mon =  mon.replace(/-/g,"/");
		mon2 =  mon2.replace(/-/g,"/");
		var oDate1 = new Date(mon);
		var oDate2 = new Date(mon2);
		if(oDate1 < oDate2){
			alert("所选月份不能大于当前月份");
			$("#yuefen2").val('');
		}else{
			//判断月份一不能大于月份二
			var mon3 = $("#yuefen1").val()+'-01 00:00:00';
			mon3 =  mon3.replace(/-/g,"/");
			var oDate3 = new Date(mon3);
			if(oDate2 > oDate3){
				alert("所选月份范围不能大于后一个月份");
				$("#yuefen2").val('');
			};
		};
	});
	$("#yuefen1").change(function(){
		var d = new Date();
		var mon = d.getFullYear()+'-'+(d.getMonth()+1)+'-01 00:00:00';
		var mon2 = $("#yuefen1").val()+'-01 00:00:00';
		mon =  mon.replace(/-/g,"/");
		mon2 =  mon2.replace(/-/g,"/");
		var oDate1 = new Date(mon);
		var oDate2 = new Date(mon2);
		if(oDate1 < oDate2){
			alert("所选月份不能大于当前月份");
			$("#yuefen1").val('');
		}else{
			//判断月份一不能小于月份二
			var mon3 = $("#yuefen2").val()+'-01 00:00:00';
			mon3 =  mon3.replace(/-/g,"/");
			var oDate3 = new Date(mon3);
			if(oDate2 < oDate3){
				alert("所选月份范围不能小于前一个月份");
				$("#yuefen1").val('');
			};
		};	
	});
	</script>
</body>
</html>
