<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<link href="/css/css/laydate.css" rel="stylesheet" />

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css"
	rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript"
	src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>${_res.get('tuition.fee')}</title>
<style>
label {
	width: 170px;
}
</style>
</head>
<body>
	<div class="ibox float-e-margins" style="margin-bottom:0">
		<div class="ibox-content">
			<form action="" method="post" id="salaryForm" >
			<fieldset style="width: 850px">
			<div class="col-lg-12" style="min-width: 680px">
			<div class="ibox float-e-margins">
			<div class="ibox-title">
			<c:if test="${!empty msg }">
					${msg}
			</c:if>
			<c:if test="${empty msg }">
				<h5>${teacher.REAL_NAME}的课时费</h5>
			</c:if>	
			</div>
			<div class="ibox-content">		
				<table class="table table-hover table-bordered" width="100%">
							<thead>
								<tr>
									<th rowspan="2">${_res.get("index")}</th>
									<th rowspan="2">1v1课时费</th>
									<th rowspan="2">小班课时费</th>
									<th rowspan="2">生效日期</th>
									<th rowspan="2">${_rea.get('Date.due')}</th>
								</tr>
							</thead>
							<c:forEach items="${costList}" var="coursecost" varStatus="status">
								<tr class="odd" align="center">
									<td>${status.count}</td>
									<td>${coursecost.yicost}</td>
									<td>${coursecost.xiaobancost}</td>
									<td>${coursecost.startdate}</td>
									<td>${coursecost.enddate}</td>
								</tr>
							</c:forEach>
				</table>
			</div>
			</div>
			</div>	
			</fieldset>			
				<fieldset style="width: 850px">
					<div class="col-lg-12" style="min-width: 680px">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>考勤列表</h5>
							</div>
							<div class="ibox-content">
								<table class="table table-hover table-bordered" width="100%">
									<thead>
										<tr>
											<th rowspan="2">${_res.get("index") }</th>
											<th rowspan="2">${_res.get("course.class.date") }</th>
											<th rowspan="2">${_res.get("class.time.session")}</th>
											<th rowspan="2">${_res.get('type.of.class')}</th>
											<th rowspan="2">${_res.get('course.course')}</th>
											<th rowspan="2">${_res.get('student')}</th>
											<th rowspan="2">${_res.get("class.attendance")}</th>
											<th rowspan="2">${_res.get('session')}</th>
											<th rowspan="2">${_res.get('tuition.fee')}</th>
											<th rowspan="2">课酬</th>
										</tr>
									</thead>
									<c:forEach items="${list}" var="teacherchecke" varStatus="status">
										<tr class="odd" align="center">
											<td>${status.count}</td>
											<td>${teacherchecke.yuefen}</td>
											<td>${teacherchecke.RANK_NAME}</td>
											<td>${teacherchecke.sklx}</td>
											<td>${teacherchecke.SUBJECT_NAME}</td>
											<td>${teacherchecke.REAL_NAME}</td>
											<td>${teacherchecke.iscancel==0?teacherchecke.kaoqin:_res.get('Cancelled')}</td>
											<td>${teacherchecke.iscancel==0?teacherchecke.class_hour:teacherchecke.teacherhour}</td>
											<td>${teacherchecke.cost}</td>
											<td>${teacherchecke.kechou}</td>
										</tr>
									</c:forEach>
									<tr>
										<td>合计</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td>${sumhoures}</td>
										<td></td>
										<td>${sumcost}</td>
								</table>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<input id="teacherid" type="hidden" name="salary.teacher_id" value="${tid}"/>
					<input type="hidden" id="yuefen" name="salary.stat_time" value = "${yuefen}"/>
					<input type="hidden" id="zks" name="salary.sum_hours" value = "${zks}"/>
					<input type="hidden" id="zc" name="salary.normal_hours" value = "${zc}"/>
					<input type="hidden" id="cd" name="salary.late_hours" value = "${cd}"/>
					<input type="hidden" id="bq" name="salary.buqian_hours" value = "${bq}"/>
					<input type="hidden" id="wei" name="salary.wei_hours" value = "${wei}"/>
					<input type="hidden" id="sumcost" name="salary.normal_salary" value = "${sumcost}"/>
							<p>
							<label>应发：${sumcost}</label>
							</p>
						    <p><label>实发：</label>
							<input type="text" id="shifa" name="salary.real_salary" />
							<input type="button" value="${_res.get('admin.common.submit')}" onclick="return saveAccount();" class="btn btn-outline btn-primary" />
							</p>
				</fieldset>
			</form>
		</div>
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
	<!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script type="text/javascript">
    function checkempty(checkField) {
		var checkValue = $("#"+checkField).val();
	    if (checkValue == "") {
	    	return false;
	}
	}
    function saveAccount() {
		if (checkempty('shifa')) {
			return false;
		}else{
			$.ajax({
				type : "post",
				url : "${cxt}/salary/save",
				data : $('#salaryForm').serialize(),
				datatype : "json",
				success : function(data) {
					if (data.code == '0') {
						layer.msg(data.msg, 2, 5);
					} else {
						setTimeout("parent.layer.close(index)", 3000);
						parent.window.location.reload();
					}
				}
			});
		}
	}

    </script>
	<script>
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
	<script type="text/javascript">
		
	</script>
	<script>
		$('li[ID=nav-nav2]').removeAttr('').attr('class', 'active');
	</script>
	
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
        laydate(date1);
 </script>
</body>
</html>
