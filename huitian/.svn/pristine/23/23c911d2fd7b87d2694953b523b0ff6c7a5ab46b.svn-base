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
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 

<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<style>
label{
   width:170px;
}

</style>
</head>
<body>
<div id="wrapper" style="background: #2f4050;">
   <%@ include file="/common/left-nav.jsp"%>
   <div class="gray-bg dashbard-1" id="page-wrapper">
    <div class="row border-bottom">
     <nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;border:0">
        <%@ include file="/common/top-index.jsp"%>
     </nav>
  </div>
  
  <div class="margin-nav" style="margin-left:0;">	
     <div class="ibox float-e-margins">
       <div class="ibox-title">
         <h5>
           	<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
           &gt;<a href='/reservation/index'>${_res.get('Appointment_Test')}</a> &gt;${_res.get('Add_test')}
         </h5>
         <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get('system.reback')}</a>
         <div style="clear:both"></div>
       </div>
     <div class="ibox-content" style="width:100%;padding:15px;margin-top:20px;background: white;">
     	<div style="float: left;" >
				<p>
					<iframe id="ifstu" name="ifstu" width=100% height=200px;  frameborder=0 scrolling=no src="/student/head.jsp"> </iframe>
				</p>
			</div>
     	<div >
          <form action="" method="post" id="addReservation" name="form1">
					<fieldset>
						<p>
							<label>${_res.get('student.name')}:</label> 
							<select id="studentid" class="chosen-select" onchange=getHeadPic() name="reservation.studentid" style="width: 150px;">
								<c:forEach items="${listStudent}" var="s">
									<option  value="${s.id}" <c:if test="${s.id==reservation.studentid}">selected="selected"</c:if> >${s.real_name}</option>
								</c:forEach>
							</select>
						</p>
						<p>
							<label>${_res.get('course.class.date')}:</label>
							 <input type="text" class="layer-date "  readonly="readonly" id="date1" 
							 name="reservation.reservationtime" value="${reservation.reservationtime}" style="margin-top: -8px; width:150px" /> 
						</p>
						<p>
							<label>${_res.get('time.session')}:</label>
							<select id="timerankid" class="chosen-select" name="reservation.timerankid" style="width: 150px;">
								<c:forEach items="${listTimeRank}" var="tr">
									<option  value="${tr.id}" <c:if test="${tr.id==reservation.timerankid}">selected="selected"</c:if> >${tr.rank_name }</option>
								</c:forEach>
							</select>
						</p>
						<p>
							<label>${_res.get('system.campus')}:</label>
							<select id="campusid" class="chosen-select" name="reservation.campusid" style="width: 150px;" onchange="getClassRoom(this.value)">
								<option value="">${_res.get('Please.select')}</option>
								<c:forEach items="${listCampus}" var="c">
									<option value="${c.id }" <c:if test="${c.id==reservation.campusid}">selected="selected"</c:if>>${c.campus_name }</option>
								</c:forEach>
							</select>
						</p>
						<p>
							<label>${_res.get('class.classroom')}:</label>
							<select id="roomid" class="chosen-select" name="reservation.roomid" style="width: 150px;">
								<option value="">${_res.get('Please.select')}</option>
							</select>
						</p>
						<p>
							<label>${_res.get('teacher.name')}:</label>
							<select id="teacherid" class="chosen-select" name="reservation.teacherid" style="width: 150px;">
								<option value="">${_res.get('Please.select')}</option>
							</select>
						</p>
						<p>
							<label>${_res.get('type')}： </label>
								<input type="radio" name="reservation.type" value="0" checked="checked" />${_res.get('Off-line')}
								<input type="radio" name="reservation.type" value="1" />${_res.get('On-line')}
						</p>
						<input type="hidden" id="picurl" name="headpictureid" value="">
					</fieldset>
					<input type="button" class="btn btn-outline btn-primary" onclick="saveReservationMessage()" value="${_res.get('save')}">
				</form>
     </div>
     </div>
     <div style="clear: both;"></div>
     </div>
  </div>   
</div>
<!-- Mainly scripts -->
	<script src="/js/js/jquery-2.1.1.min.js"></script>
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
   
     <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/top-nav/top-nav.js"></script>
    <script src="/js/js/top-nav/teach.js"></script>
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
    <script>
    $(".chosen-select").chosen({disable_search_threshold: 1});
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
    <!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script>
       $('li[ID=nav-nav21]').removeAttr('').attr('class','active');
       /* 
     //弹出后子页面大小会自动适应
       var index = parent.layer.getFrameIndex(window.name);
       parent.layer.iframeAuto(index); */
    </script>
    <script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
	<script>
		//日期范围限制
		var date1 = {
			elem : '#date1',
			format : 'YYYY-MM-DD',
			max : '2099-06-16', //最大日期
			min : laydate.now(),
			istime : false,
			istoday : false,
			choose : function(datas) {
			}
		};
		laydate(date1);
	</script>
	<script>
	function getHeadPic(){
		$.ajax({
			url:'/reservation/getHeadPic',
			type:'post',
			data:{'id':$("#studentid").val()},
			dataType:'json',
			success:function(data){
				if(data!=1){
					sss(data.URL);
					$("#ifstu").contents().find("#url").val(data.ID);
				}else{
					sss("touxiang1.png");
				}
			}
		})
	}
	function sss(name){
		if(name!=""&&name!=null){
			$("#ifstu").contents().find("#pic").html('<img id="pic" src="/images/headPortrait/'+name+'" style="width: 144px; height: 144px;">');
		}
	}
/*获取教室和教师*/	
		function getClassRoom(campusid){
			var date= $("#date1").val();
			var timerankid= $("#timerankid").val();
			var roomid='${reservation.roomid}';
			var teacherid='${reservation.teacherid}';
			$("#roomid").html('<option value="">${_res.get("Please.select")}</option>');
			$("#teacherid").html('<option value="">${_res.get("Please.select")}</option>');
			if(campusid!=''&&date1!=''){
				$.ajax({
					url:'/reservation/getClassRoom',
					type:'post',
					data:{'campusid':campusid,'reservationTime':date,'timerankid':timerankid},
					dataType:'json',
					success:function(data){
						if(data.code==0){
							var room="";
							var teacher="";
							if(data.isroom==0){
								for(i in data.rooms){
									if(data.rooms[i].CODE==1){
										room+='<option disabled="disabled" value="'+data.rooms[i].ID+'">'+data.rooms[i].NAME+'(教室在该时间已被占用)</option>';
									}else{
										if(roomid==+data.rooms[i].ID){
											room+='<option  selected="selected" value="'+data.rooms[i].ID+'">'+data.rooms[i].NAME+'</option>';
										}else{
											room+='<option  value="'+data.rooms[i].ID+'">'+data.rooms[i].NAME+'</option>';
										}
									
									}
									
								}
							}else{
								room+='<option value="">校区下暂未添加教室</option>';
							}
							$("#roomid").append(room);
							$("#roomid").trigger("chosen:updated");
							if(data.isteacher==0){
								for(i in data.teachers){
									if(data.teachers[i].CODE==1){
										teacher+='<option disabled="disabled" value="'+data.teachers[i].ID+'">'+data.teachers[i].REAL_NAME+'(教师在该时间已有安排)</option>';
									}else{
										if(teacherid==data.teachers[i].ID){
											teacher+='<option selected="selected" value="'+data.teachers[i].ID+'">'+data.teachers[i].REAL_NAME+'</option>';
										}else{
											teacher+='<option value="'+data.teachers[i].ID+'">'+data.teachers[i].REAL_NAME+'</option>';
										}
									
									}
								}
							}else{
								teacher+='<option value="">校区下暂未添加教师</option>';
							}
							$("#teacherid").append(teacher);
							$("#teacherid").trigger("chosen:updated");
						}
					}
				});
			}
		}
/*保存*/
	function saveReservationMessage(){
		if($("#ifstu").contents().find("#url").val()!=""){
			$("#picurl").val($("#ifstu").contents().find("#url").val());
		}
		var date = $("#date1").val();
		if(date==""){
			layer.msg("预约日期不能为空",2,1);
			return false;
		}
		if($("#campusid").val()==""){
			layer.msg("请选择校区",2,1);
			return false;
		}
		if($("#roomid").val()==""){
			layer.msg("请选择教室",2,1);
			return false;
		}
		if($("#teacherid").val()==""){
			layer.msg("请选择教师",2,1);
			return false;
		}
		if(confirm("确认添加这次预约测试吗?")){
			$.ajax({
				url:'/reservation/saveReservationMessage',
				type:'post',
				data:$("#addReservation").serialize(),
				dataType:'json',
				success:function(data){
					 if(data==0){
							layer.msg("SUCCESS",1,1);
						}else{
							layer.msg("ERROR",2,1);
						} 
					 setTimeout("window.location.reload();", 1000);
				}
			})
		}
	}
	 $(document).ready(function() {
		 getHeadPic();
	}); 
	</script>
</body>
</html>