<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>活动管理</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
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
	<div id="wrapper" style="background: #2f4050;">
	  <%@ include file="/common/left-nav.jsp"%>
       <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;">
			<div class="navbar-header">
			  <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" id="btn-primary" href="#" style="margin-top:10px;"><i class="fa fa-bars"></i> </a>
				<div style="margin:20px 0 0 70px;"><h5>
					<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
						&gt;<a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a> &gt; 活动管理
				</h5>
				<a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
				</div>
			</div>
			<div class="top-index"><%@ include file="/common/top-index.jsp"%></div>
			</nav>
		</div>

	    <div class="margin-nav" style="min-width:1050px;width:100%;">	
		<form action="/opportunity/index" method="post" id="searchForm">
			<div  class="col-lg-12">
			  <div class="ibox float-e-margins">
				<div class="ibox-title" style="height:auto;">
				<label>活动名称：</label>
				<select id="sourceid" class="chosen-select" style="width: 150px;" tabindex="2" name="_query.source"  onchange="chooseSource(this.value)" >
					<option value="">--${_res.get('Please.select')}--</option>
					<option value="">英语疯狂对对讲</option>
					<option value="">单词串串大比拼</option>
					<option value="">英语嗨歌奔火星</option>
					<option value="">老师学生互动口语</option>
					<option value="">疯癫老外汉语讲座</option>
				</select>
				<label> ${_res.get("admin.sysLog.property.startdate")}： </label> <input type="text" name="start" readonly="readonly" value="" id="data"/>
				&nbsp;
				<input type="submit" value="${_res.get('admin.common.select')}" class="btn btn-outline btn-primary">&nbsp;
				<input type="button" id="tianjia" value="${_res.get('teacher.group.add')}" onclick="window.location.href='/opportunity/add_activity.jsp'" class="btn btn-outline btn-info">
			</div>
			</div>
			</div>

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>活动列表</h5>
					</div>
					<div class="ibox-content">
						<table width="80%" class="table table-hover table-bordered">
							<thead>
								<tr>
									<th>${_res.get("index")}</th>
									<th>活动名称</th>
									<th>${_res.get("admin.sysLog.property.startdate")}</th>
									<th>结束日期</th>
									<th>${_res.get('person.in.charge')}</th>
									<th>地点</th>
									<th>${_res.get('admin.dict.property.status')}</th>
									<th>${_res.get("operation")}</th>
								</tr>
							</thead>
								<tr align="center">
									<td>1</td>
									<td>英语疯狂对对讲</td>
									<td>2015-05-05</td>
									<td>2015-05-06</td>
									<td>张老师</td>
									<td>海淀区中关村</td>
									<td><span class="label label-warning">${_res.get('Cancelled')}</span></td>
									<td>
										<a href="#" style="color: #515151" onclick="">${_res.get('Modify')}</a>|
										<a href="#" style="color: #515151" onclick="">取消</a>|
										<a href="#" style="color: #515151" onclick="samePeople()">相关人员</a>
									</td>
								</tr>
								<tr align="center">
									<td>2</td>
									<td>单词串串大比拼</td>
									<td>2015-05-07</td>
									<td>2015-05-07</td>
									<td>徐老师</td>
									<td>国贸</td>
									<td><span class="label label-primary">已结束</span></td>
									<td>
										<a href="#" style="color: #515151" onclick="">${_res.get('Modify')}</a>|
										<a href="#" style="color: #515151" onclick="">取消</a>|
										<a href="#" style="color: #515151" onclick="samePeople()">相关人员</a>
									</td>
								</tr>
								<tr align="center">
									<td>3</td>
									<td>英语嗨歌奔火星</td>
									<td>2015-06-07</td>
									<td>2015-06-07</td>
									<td>王老师</td>
									<td>大望路</td>
									<td><span class="label label-success">进行中</span></td>
									<td>
										<a href="#" style="color: #515151" onclick="">${_res.get('Modify')}</a>|
										<a href="#" style="color: #515151" onclick="">取消</a>|
										<a href="#" style="color: #515151" onclick="samePeople()">相关人员</a>
									</td>
								</tr>
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
		
		function samePeople(){
			layer.tab({
			    data:[
			        {
			        	title: '相关人员', 
			        	content:'<table class="table table-bordered">'
			        	+'<tr>'
			        	+'<td class="table-bg1">负责人</td><td class="table-bg2">王老师</td>'
			        	+'<td class="table-bg1">老师</td><td class="table-bg2">李老师</td>'
			        	+'<td class="table-bg1">督导</td><td class="table-bg2">徐督导</td>'
			        	+'</tr>'
			        	+'<tr>'
			        	+'<td class="table-bg1">讲师</td><td class="table-bg2">赵大师</td>'
			        	+'<td class="table-bg1">专员</td><td class="table-bg2">一号专员</td>'
			        	+'<td class="table-bg1">专员</td><td class="table-bg2">二号专员</td>'
			        	+'</tr>'
			        	+'</table>'
			        }
			        
			    ],
				offset:['150px', ''],
			    area: ['600px', 'auto'] //宽度，高度
			});
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
    function feedback(opportunityId){
	    	$.layer({
	    	    type: 2,
	    	    shadeClose: true,
	    	    title: "反馈详情",
	    	    closeBtn: [0, true],
	    	    shade: [0.5, '#000'],
	    	    border: [0],
	    	    area: ['700px', '480px'],
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
    function isorder(opportunityId){
    	layer.confirm('确认该销售机会已预约吗？', function(){
    		$.ajax({
    			url:"${cxt}/opportunity/isOrder",
    			type:"post",
    			data:{"opportunityId":opportunityId},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
						$('#isconver_'+opportunityId).text('已预约');
						$('#isconver_status_'+opportunityId).val(true);
					}else{
    					layer.msg(data.msg, 2, 5);
					}
    			}
    		});
    	});
    }
    function isvisit(opportunityId){
    	layer.confirm('确认该销售机会已上门吗？', function(){
    		$.ajax({
    			url:"${cxt}/opportunity/isVisit",
    			type:"post",
    			data:{"opportunityId":opportunityId},
    			dataType:"json",
    			success:function(data){
					if(data.code=='1'){
    					layer.msg(data.msg, 2, 1);
						$('#isconver_'+opportunityId).text('已上门');
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
    	$.ajax({
			url:"${cxt}/opportunity/getDetailForJson",
			type:"post",
			data:{"opportunityId":opportunityId},
			dataType:"json",
			success:function(result){
				/*var feedbacks = result.FBLIST;
				var feedbackInfo = "<div class='chat-message1'>";
				for(var x in feedbacks){
					var feedback = feedbacks[x];
					var sysusername = feedback.SYSUSERNAME;
					var createtime = feedback.CREATETIME;
					var mediatorname = feedback.MEDIATORNAME;
					var content = feedback.CONTENT;
					feedbackInfo+="<div class='chat-message2'>";
					if(sysusername!=null){
						feedbackInfo+="<table style='width:100%'><tr><td style='width:8%'><a class='message-author1' href='#'>"+sysusername+"</a></td><td><div class='chat-message3'><div style='float:right;'>"+createtime+"</div><br><span class='message-content'>"+content+"</span></div></td></tr></table></div>"
					}else{
						feedbackInfo+="<table style='width:100%'><tr><td style='width:92%'><div class='chat-message4'>"+createtime+"<div style='float:right;'></div>"+"<br><span class='message-content'>"+content+"</span></div></td><td style='text-align:right;'><a class='message-author1' href='#'>"+mediatorname+"</a></td></tr></table></div>"
					}
				}
				if(feedbacks==""){
					feedbackInfo+="<div style='width:20%;margin:10px auto;'>----暂无反馈信息----</div>";
				}
				feedbackInfo+="</div>"*/
				layer.tab({
				    data:[
						//{title: '反馈详情', content:feedbackInfo},
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
				        	+'<td class="table-bg1">成单状态</td><td class="table-bg2">'+(result.o.ISCONVER?"${_res.get('Is.a.single')}":"${_res.get('Not.a.single')}")+'</td>'
				        	+'<td class="table-bg1">成单日期</td><td class="table-bg2">'+(result.o.CONVERTIME==null?'':result.o.CONVERTIME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">${_res.get("Opp.Sales.Source")}</td><td class="table-bg2">'+(result.o.CRMSCNAME)+'</td>'
				        	+'<td class="table-bg1">跟进状态</td><td class="table-bg2">'+(result.o.STATUS?"${_res.get('Opp.Followed.up')}":'结束跟进')+'</td>'
				        	+'<td class="table-bg1">结束时间</td><td class="table-bg2">'+(result.o.OVERTIME==null?'':result.o.OVERTIME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">所属顾问</td><td class="table-bg2">'+(result.o.MEDIATORNAME==null?'':result.o.MEDIATORNAME)+'</td>'
				        	+'<td class="table-bg1">所属市场</td><td class="table-bg2">'+(result.o.SCUSERNAME==null?'':result.o.SCUSERNAME)+'</td>'
				        	+'<td class="table-bg1">所属CC</td><td class="table-bg2">'+(result.o.KCUSERNAME==null?'':result.o.KCUSERNAME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">录入时间</td><td class="table-bg2">'+result.o.CREATETIME+'</td>'
				        	+'<td class="table-bg1">更新时间</td><td class="table-bg2">'+(result.o.UPDATETIME==null?'':result.o.UPDATETIME)+'</td>'
				        	+'<td class="table-bg1">确认用户</td><td class="table-bg2">'+(result.o.CONFIRMUSERNAME==null?'':result.o.CONFIRMUSERNAME)+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">学校</td><td class="table-bg2">'+(result.o.SCHOOLID?result.school.SCHOOLNAME:'')+'</td>'
				        	+'<td class="table-bg1">是否预约</td><td class="table-bg2">'+(result.o.ISORDER?'是':'否')+'</td>'
				        	+'<td class="table-bg1">是否上门</td><td class="table-bg2">'+(result.o.ISVISIT?'是':'否')+'</td>'
				        	+'</tr>'
				        	+'<tr>'
				        	+'<td class="table-bg1">高低年级</td><td class="table-bg2">'+(result.o.JUSTCLASS?'低年级':'高年级')+'</td>'
				        	+'<td class="table-bg1">备注</td><td colspan="5" class="table-bg2">'+(result.NOTE==null?'无':result.NOTE)+'</td>'
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
    		if(${1 == paramMap['_query.source']}){
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
     <!-- layerDate plugin javascript -->
	<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
    <script>
         //开始日期范围限制
        var birthday = {
            elem: '#data',
            format: 'YYYY-MM-DD',
            max: laydate.now(), //最大日期
            istime: false,
            istoday: false
        };
        laydate(birthday);
    
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>