<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>机会来源</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link rel="shortcut icon" href="/images/ico/favicon.ico" /> 

<style type="text/css">
body {
	background-color: #eff2f4;
}
textarea {
	width: 50%;
}
label {
	width: 100px;
}
</style>
</head>
<body>
	<div id="wrapper" style="background: #2f4050;min-width:1100px">
	  <%@ include file="/common/left-nav.jsp"%>
       <div class="gray-bg dashbard-1" id="page-wrapper">
		<div class="row border-bottom">
			<div>
				<nav class="navbar navbar-static-top fixtop" role="navigation">
					<div class="navbar-header">
					   <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" id="btn-primary" href="#" style="margin-top:10px;"><i class="fa fa-bars"></i> </a>
				         <div style="margin:20px 0 0 70px;"><h5>
							<img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a>
							 &gt;<a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a>&gt; 机会来源
						 </h5>
						 <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
						 </div>
					</div>
					<div class="top-index"><%@ include file="/common/top-index.jsp"%></div>
				</nav>
			</div>
		</div>
		
		<div class="margin-nav" style="width:1000px;margin-left:0;">	
		<div class="row wrapper border-bottom white-bg page-heading margin_zuo" style="width: 95%;">
			<div class="col-lg-12" style="margin-top: 20px;">
				<form id="opportunityForm" name="opportunityForm" action="" method="post">
					<fieldset style="width: 100%; padding-top:15px;">
					   <p>
							<label>${_res.get("admin.user.property.telephone")}：</label>
							<input type="text" id="phonenumber" name="opportunity.phonenumber" value="" size="20" maxlength="15" vMin="11" vType="phone" onblur="onblurVali(this);" onchange="checkExist('phonenumber')"/>
							<span style="color: red;">*</span>
							<span id="phonenumberMes"></span>
						</p>
					    <p>
							<label>信息来源：</label> 
							<select name="source" id="source" class="chosen-select" style="width: 186px" tabindex="2">
							    <option value="">--请选择--</option>
								<option value="1">户外地推</option>
								<option value="2">转介绍</option>
								<option value="3">网络媒体</option>
								<option value="4">机构合作</option>
								<option value="5">主题活动</option>
								<option value="6">定点咨询</option>
							</select>
						</p>
						
						
						<div id="source1">
						<p>
							<label>${_res.get("marketing.specialist")}：</label>
							<select name="markpe" id="markpe" class="chosen-select" onChange="selectoption()" style="width: 150px" tabindex="2">
									<option value="" >--请选择--</option>
									<option value="1" >王专员</option>
									<option value="2" >李专员</option>
									<option value="3" >林专员</option>
							</select>
						</p>
						<p>
							<label>市场兼职：</label>
							<select name="markpa" id="markpa" class="chosen-select" style="width: 150px" tabindex="2">
									<option value="" >--请选择--</option>
							</select>
						</p>
						</div>
						
						<div id="source2">
						<p>
							<label>顾问/督导</label>
							<select name="student" id="student" class="chosen-select" style="width: 150px" tabindex="2">
									<option value="">--请选择--</option>
									<option value="">王XX</option>
									<option value="">刘XX</option>
									<option value="">李XX</option>
									<option value="">赵XX</option>
							</select>
						</p>
						<p>
							<label>${_res.get('student')}：</label>
							<select name="student" id="student" class="chosen-select" style="width: 150px" tabindex="2">
									<option value="">--请选择--</option>
									<option value="">王同学</option>
									<option value="">刘同学</option>
									<option value="">李同学</option>
									<option value="">赵同学</option>
							</select>
						</p>
						</div>
						
						<div id="source3">
						<p>
							<label>网络来源：</label>
							<input type="radio" id="wangzhan" name="radio" value="1" size="20" checked="checked"/>百度
							<input type="radio" id="wangzhan" name="radio" value="2" size="20"/>微信
							<input type="radio" id="wangzhan" name="radio" value="3" size="20"/>360
						</p>
						</div>
						
						<div id="source4">
						<p >
							<label>${_res.get('Affiliation')}：</label> 
							<select name="jigou" id="jigou" class="chosen-select" style="width: 150px" tabindex="2" >
								<option value="">${_res.get('Please.select')}</option>
								<option value="1">aaa机构</option>
								<option value="1">bbb机构</option>
								<option value="1">ccc机构</option>
								<option value="1">ddd机构</option>
							</select>
						</p>
						</div>
						
						<div id="source5">
						<p>
							<label>所属活动：</label> 
							<select name="jigou" id="jigou" class="chosen-select" style="width: 150px" tabindex="2" >
								<option value="">请选择所属活动</option>
								<option value="1">活动一</option>
								<option value="1">活动二</option>
								<option value="1">活动三</option>
								<option value="1">活动四</option>
							</select>
						</p>
						</div>
						
						<div id="source6">
						<p>
							<label>咨询地点：</label> 
							<select name="" id="" class="chosen-select" style="width: 150px" tabindex="2" >
								<option value="">请选择咨询地点</option>
								<option value="1" >海淀中学</option>
								<option value="2" >海淀小学</option>
								<option value="3" >朝阳中学</option>
								<option value="4" >丰台中学</option>
								<option value="5" >中关村小学</option>
							</select>
						</p>
						</div>
						
						<p>
							<label>孩子姓名：</label> 
							<select name="" id="" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="">--请选择--</option>
								<option value="0">张XX</option>
								<option value="1">王XX</option>
								<option value="2">李XX</option>
								<option value="3">杨XX</option>
							</select>
						</p>
						<p>
							<label>${_res.get('gender')}：</label>
							<input type="radio" id="sex" name="opportunity.sex" value="1" checked="checked">${_res.get('student.boy')}
							<input type="radio" id="sex" name="opportunity.sex" value="0" >${_res.get('student.girl')}
						</p>
						<p>
							<label>咨询课程：</label> 
								<input type="checkbox" value="${subject.id }" id="subjectid" name="subjectid" />表音密码
								<input type="checkbox" value="${subject.id }" id="subjectid" name="subjectid" />词义实际
								<input type="checkbox" value="${subject.id }" id="subjectid" name="subjectid" />九九句法
							<span style="color: red;">*</span>
							<span id="subjectMes"></span>
						</p>
						<p id="qd">
							<label>就读学校：</label> 
							<select name="schoolread" id="schoolread" class="chosen-select" style="width: 150px" tabindex="2" onchange="school()">
								<option value="">--请选择学校--</option>
								<option value="0">实验小学</option>
								<option value="1">实验中学</option>
							</select>
						</p>
						<p id="qd">
							<label>年级：</label> 
							<select name="school_grade" id="school_grade" class="chosen-select" style="width: 150px" tabindex="2" >
								<option value="">--请选择年级--</option>
							</select>
						</p>
						<p>
							<label>${_res.get('Parent.Name')}：</label> 
							<select name="" id="" class="chosen-select" style="width: 150px" tabindex="2" >
								<option value="">--请选择--</option>
								<option value="">王XX</option>
								<option value="">张XX</option>
								<option value="">李XX</option>
								<option value="">郑XX</option>
							</select>
						</p>
						<p>
							<label>${_res.get('student.parentphone')}：</label>
							<input type="text" id="phonenumber" name="opportunity.phonenumber" value="" size="20" maxlength="15" vMin="11" vType="phone" onblur="onblurVali(this);" onchange="checkExist('phonenumber')"/>
							<span style="color: red;">*</span>
							<span id="phonenumberMes"></span>
						</p>
						<p>
							<label>家长分类：</label> 
							<select name="" id="" class="chosen-select" style="width: 150px" tabindex="2" >
								<option value="">--请选择--</option>
								<option value="">父亲</option>
								<option value="">母亲</option>
								<option value="">奶奶</option>
								<option value="">爷爷</option>
								<option value="">姑姑</option>
								<option value="">舅舅</option>
							</select>
						</p>
						<p>
							<label>咨询师：</label> 
							<select name="opportunity.kcuserid" id="kcuserid" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="">--请选择--</option>
								<option value="">小张</option>
								<option value="">小王</option>
								<option value="">小李</option>
								<option value="">小陈</option>
								<option value="">小林</option>
							</select>
						</p>
						<p>
							<label>邀约老师：</label> 
							<select name="opportunity.needcalled" id="needcalled" class="chosen-select" style="width: 150px" tabindex="2">
								<option value="">--请选择--</option>
								<option value="">王XX</option>
								<option value="">李XX</option>
								<option value="">刘XX</option>
								<option value="">高XX</option>
								<option value="">杜XX</option>
							</select>
						</p>
						<p>
							<label>${_res.get('course.remarks')}：</label> 
							<textarea rows="5" cols="85" name="opportunity.note"  style="width:440px;overflow-x: hidden; overflow-y: scroll;">${fn:trim(opportunity.note)}</textarea>
						</p>
						<p>
							<input type="button" value="${_res.get('save')}" onclick="save()" class="btn btn-outline btn-primary" />
						</p>
					</fieldset>
				</form>
			</div>
			<div style="clear: both;"></div>
		</div>
	  </div>
	  </div>	
	</div>
	<script src="/js/js/jquery-2.1.1.min.js"></script>
	<!-- Chosen -->
	<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
	<script>
		$(".chosen-select").chosen({disable_search_threshold: 15});
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
		//表单效果控制
		$("#source1").hide();
		$("#source2").hide();
	    $("#source3").hide();
	    $("#source4").hide();
	    $("#source5").hide();
	    $("#source6").hide();
	    
		$("#source").change(function(){
             var a = $("#source").val();
             if(a==1){
            	 $("#source1").show();
             }else{
            	 $("#source1").hide();
             }
             if(a==2){
            	 $("#source2").show();
             }else{
            	 $("#source2").hide();
             }
             if(a==3){
            	 $("#source3").show();
             }else{
            	 $("#source3").hide();
             }
             if(a==4){
            	 $("#source4").show();
             }else{
            	 $("#source4").hide();
             }
             if(a==5){
            	 $("#source5").show();
             }else{
            	 $("#source5").hide();
             }
             if(a==6){
            	 $("#source6").show();
             }else{
            	 $("#source6").hide();
             }
		});
	
		//菜单级联效果
		function selectoption()
	    {
			var d = $("#markpe").val();
			var str = "";
			if(d==1){
			    str="<option>--请选择--</option><option>王专员1</option><option>王专员2</option><option>王专员3</option><option>王专员4</option>"
				$("#markpa").html(str);
				$("#markpa").trigger("chosen:updated");
			}
			if(d==2){
				str="<option>--请选择--</option><option>李专员1</option><option>李专员2</option><option>李专员3</option>"
				$("#markpa").html(str);
				$("#markpa").trigger("chosen:updated");
			}
			if(d==3){
				str="<option>--请选择--</option><option>林专员1</option><option>林专员2</option><option>林专员3</option><option>林专员4</option>"
				$("#markpa").html(str);
				$("#markpa").trigger("chosen:updated");
			}
	        
	    }
		
		//学校级联
		function school(){
			var s = $("#schoolread").val();
			var str = "";
			if(s==0){
				str="<option>一年级</option><option>二年级</option><option>三年级</option><option>四年级</option><option>五年级</option><option>六年级</option>";
			    $("#school_grade").html(str);
			    $("#school_grade").trigger("chosen:updated");
			}
			if(s==1){
				str="<option>初一年级</option><option>初二年级</option><option>初三年级</option>";
				$("#school_grade").html(str);
				$("#school_grade").trigger("chosen:updated");
			}
		}
		
		
		function checkExist(checkField) {
			var checkValue = $("#"+checkField).val();
		    if (checkValue != "") {
		    	var flag = true;
		        $.ajax({
		            url: '${cxt}/opportunity/checkExist',
		            type: 'post',
		            data: {
		                'checkField': checkField,
		                'checkValue': checkValue,
		                'opportunityId': $("#opportunityId").val()
		            },
		            async: false,
		            dataType: 'json',
		            success: function(data) {
		                if (data.result >= 1) {
		                	$("#"+checkField).focus();
	                    	$("#"+checkField+"Mes").text("您填写的数据已存在。");
		                }else{
		                	$("#"+checkField+"Mes").text("");
		                	flag = false;
		                } 
		            }
		        });
		        return flag;
		    } else {
		    	if(checkField!="email"){
			        $("#"+checkField).focus();
			    	$("#"+checkField+"Mes").text("该字段不能为空。");
			        return true;
		    	}else{
		    		$("#"+checkField+"Mes").text("");
		    	}
		    }
		}
		
		function save() {
			if(checkExist('contacter'))
				return false;
			if(checkExist('phonenumber'))
				return false;
			if(checkExist('email'))
				return false;
			var contacter = $("#contacter").val().trim;
			if ($("#contacter").val() == "" || $("#contacter").val() == null) {
				$("#contacter").focus();
				$("#contacterMes").text("联系人不能为空！");
				return false;
			}else{
				$("#contacterMes").text("");
				var flagChoose=false;
				$("input[id='subjectid']").each(function() {
					if($(this).prop("checked")){
						flagChoose=true;
					}
				});	
				if(flagChoose){
					$("#subjectMsg").text("");
					var opportunityId = $("#opportunityId").val();
					if($("#source").val()==1){
						$("#scuserid").attr("disabled",false); 
						if($("#mediatorid").val()==""){
							$("#mediatoryMes").text("请选择所属渠道");
							return false;
						}else{
							$("#mediatoryMes").text("");
						}
					}
					if($("#schoolid").val()==""){
						$("#schoolMes").text("请选择所属校区");
						return false;
					}else{
						$("#campusMes").text("");
					}
					if($("#campusid").val()==""){
						$("#campusMes").text("请选择所属校区");
						return false;
					}else{
						$("#campusMes").text("");
					}
					if($("#kcuserid").val()==""){
						$("#kcgwMes").text("请选择所课程顾问");
						return false;
					}else{
						$("#kcgwMes").text("");
					}
					if(opportunityId==""){
						if(confirm("确定要添加该销售机会吗？")){
							$("#opportunityForm").submit();
						}
					}else{
						if(confirm("确定要修改该销售机会吗？")){
							$("#opportunityForm").attr("action", "/opportunity/update");
							$("#opportunityForm").submit();
						}
					}
				}else{
					$("#subjectMes").text("请选择咨询科目");
				}
			}
		}
		function chooseSource(){
			var id=$("#source").val();
			if(id==1){
				$("#qd").show();
				$("#scuserid").attr("disabled",true); 
				$("#mediatorid").attr("disabled",false); 
			}else{
				$("#mediatorid").attr("disabled",false);
				$("#mediatorid").val('');
				$("#scuserid").attr("disabled",false); 
				$("#qd").hide();
			}
			$("#scuserid").trigger("chosen:updated");
			$("#mediatorid").trigger("chosen:updated");
		}
		function chooseMarketer(id) {
			var scuserid = $("#m_sc_" + id).val();
			$("#scuserid").val(scuserid);
			$("#scuserid").trigger("chosen:updated");
		}
		function chooseSchool(id){
			alert(id);
			var schooid = $("#m_school_"+id).val();
			$("#schoolid").val(schoolid);
			$("#schoolid").trigger("chosen:updated");
		}
		function chooseKcgw(id) {
			var kcuserid = $("#c_kc_" + id).val();
			$("#kcuserid").val(kcuserid);
			$("#kcuserid").trigger("chosen:updated");
		}
	</script>
	<script src="/js/utils.js"></script>
	
	 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script>
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
    </script>
</body>
</html>