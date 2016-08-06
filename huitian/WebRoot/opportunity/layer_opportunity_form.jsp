<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>添加销售机会</title>
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
p{
   width:280px;
   float:left;
}
.spanred{
   color:red
}
input[type="text"]{
   width:150px
}
#idtype_chosen{
   width:150px !important
}
.student_list_wrap {
    position: absolute;
    top: 35px;
    width: 149px;
    overflow: hidden;
    z-index: 2012;
    background: rgba(0, 223, 255, 0.84);
    border: 1px solide;
    border-color: #e2e2e2 #ccc #ccc #e2e2e2;
    padding: 6px;
    margin-top: 194px;
    margin-left: 384px;
}
.student_list_wrap li {
    display: block;
    line-height: 20px;
    padding: 4px 0;
    border-bottom: 1px dashed #676a6c;
    cursor: pointer;
    text-align: center;
    width: 55%;
}
.display{display:none}
</style>
</head>
<body>
		<div>
			<div class="col-lg-12" style="margin-top: 20px;">
				<form id="opportunityForm" action="${cxt }/opportunity/save" method="post">
					<fieldset style="width: 100%; padding-top:15px;">
						<input type="hidden" id="opportunityId" name="opportunity.id" value="${opportunity.id }"/>
						<input type="hidden"  id="oppkcuserid" value="${opportunity.kcuserid }" >
						<input type="hidden" id="code" name="opportunitycode" value="${code}"/>
						<input type="hidden" id="num" name="opportunitynum" value="${num}"/>
						<input type="hidden" id="nums" name="nums" value="${nums}"/>
						<c:if test="${!empty opportunity.id }">
							<input type="hidden" name="opportunity.version" value="${opportunity.version + 1}">
						</c:if>
						
						<div class="ibox float-e-margins">
						   <div class="ibox-title">
						     <h5>销售机会信息</h5>
						     <div class="ibox-tools">
						       <a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
						     </div>
						   </div>
						   <div class="ibox-content">
						      <div>
						         <p>
									<label>${_res.get("Contacts")}：</label>
									<input type="text" id="contacter" name="opportunity.contacter"  <c:if test="${num eq '0'}">disabled</c:if>
									value="${opportunity.contacter }" size="20" maxlength="15" vMin="2" vType="checkTestName" onblur="onblurVali(this);" onchange="checkExist('contacter')"/>
									<span class="spanred">*</span>
									<!-- <br><span id="contacterMes" class="spanred"></span> -->
								</p>
								<p>
									<label>${_res.get("admin.user.property.telephone")}：</label>
									<input type="text" id="phonenumber" name="opportunity.phonenumber"  <c:if test="${num eq '0'}">disabled</c:if>
									value="${opportunity.phonenumber }" size="20" maxlength="15" vMin="11" vType="tellphone" onblur="onblurVali(this);" onchange="checkExist('phonenumber')"/>
									<span class="spanred">*</span>
									<!-- <br><span id="phonenumberMes" class="spanred"></span> -->
								</p>
								<p>
									<label>电子邮箱：</label>
									<input type="text" id="email" name="opportunity.email"  <c:if test="${num eq '0'}">disabled</c:if>
									value="${opportunity.email }" size="20" maxlength="100" vMin="0" vType="email" onblur="onblurVali(this);" onchange="checkExist('email')"/>
									<!-- <span id="emailMes" class="spanred"></span> -->
								</p>
								<div style="clear: both;">
									<p>
										<label>${_res.get('gender')}：</label>
										<input type="radio" id="sex" name="opportunity.sex" value="1" <c:if test="${num eq '0'}">disabled</c:if>
										<c:if test="${opportunity.sex||opportunity.sex==null}">checked="checked"</c:if>>${_res.get('student.boy')}
										<input type="radio" id="sex" name="opportunity.sex" value="0"  <c:if test="${num eq '0'}">disabled</c:if>
										<c:if test="${opportunity.sex==false}">checked="checked"</c:if>>${_res.get('student.girl')}
									</p>
									<p>
										<label>咨询课程：</label>
										<input type="radio" id="calsstype" name="opportunity.classtype" value="1" 
										<c:if test="${num eq '0'}">disabled</c:if>
										<c:if test="${opportunity.classtype=='1'||opportunity.classtype==null}">checked="checked"</c:if>>${_res.get("IEP")}
										<input type="radio" id="classtype" name="opportunity.classtype" value="0" 
										<c:if test="${num eq '0'}">disabled</c:if>
										<c:if test="${opportunity.classtype=='0'}">checked="checked"</c:if>>${_res.get('course.classes')}
									</p>
								</div>
								<p style="width:100%">
									<label>咨询科目：</label>
									<select id="subjectid" name="subjectid" data-placeholder="${_res.get('Please.select')}（${_res.get('Multiple.choice')}）" class="chosen-select" multiple
											style="width: 80%;" tabindex="4">
										<c:forEach items="${subjectList}" var="subject">
											<option value="${subject.id}" class="options" id="tid_${subject.id}">${subject.SUBJECT_NAME}</option>
										</c:forEach>
									</select> 
									<input id="subjectids" name="opportunity.subjectids" value="" type="hidden">
									
									<%--注释部分是selected单选框  可选取一个科目--%>
									<%-- <select name="opportunity.subjectids" id="subjectid" class="chosen-select" style="width: 150px" tabindex="2" >
										<option value=""> 请选择科目 </option>
										<c:forEach items="${subjectList }" var="subject">
											<option value="${subject.id }"  >${subject.SUBJECT_NAME }</option>
										</c:forEach>
									</select> --%>
									 <%--注释部分是checkbox复选框  可选取多个科目--%>
									<%-- <div style="width:560px;float:left">
										<c:forEach items="${subjectList }" var="subject">
											 <div style="width:auto;padding:0 2px;float:left">
											 <input type="checkbox" value="${subject.id }" id="subjectid" name="subjectid" 
											 <c:if test="${num eq '0'}">disabled</c:if>
											 <c:if test="${subject.checked eq 'checked'}">checked='checked'</c:if>/>${subject.SUBJECT_NAME }</div>
										</c:forEach>
									    <span class="spanred">*</span>
									</div> --%>
									<!-- <span id="subjectMes" class="spanred"></span> -->
								</p>
								<p>
									<label>${_res.get("Opp.Sales.Source")}：</label>
									<select name="opportunity.source" id="source" class="chosen-select" style="width: 150px" tabindex="2" onchange="chooseSource()">
										<c:forEach  var="source" items="${sourceList }">
											<option value="${source.id }" <c:if test="${num eq '0'}">disabled</c:if>
											<c:if test='${opportunity.source eq source.id }'>selected='selected'</c:if> >${source.name }</option>
										</c:forEach>
									</select>
								</p>
								<p id="qd">
									<label>${_res.get("Opp.Channel")}：</label> 
									<input id="mediatorid" name="mediatorname" onblur="onblurmediator()" 
									oninput="getLikeMediator(this.value)"  onpropertychange="getLikeMediator(this.value)" value="${mediatornames}" type="text">
									<div id="showSelectMessage" class="student_list_wrap" style="display: none">
										<ul style="margin-bottom: 10px;" id="mediaorlist"></ul>
									</div>
								</p>
								<p id="sop" style="position: relative;">
									<label>介绍人</label>
									<%-- <input type="text" id="recommendusername" name="opportunity.recommendusername" value="${opportunity.recommendusername }" size="14" maxlength="15" /> --%>
									<input type="text" id="recommendusername" name="opportunity.recommendusername" onkeyup="findAccountByName()" value="${opportunity.recommendusername }" />
										<div id="mohulist" class="student_list_wrap" style="display: none;position: absolute;width:150px;left:418px;top:230px;z-index:999;height:330px">
											<ul style="margin-bottom: 10px;" id="stuList"></ul>
										</div>
								</p>
								<div style="clear: both;"></div>
								<p>
									<label>${_res.get("marketing.specialist")}：</label> 
									<select name="opportunity.scuserid" id="scuserid" class="chosen-select" style="width: 150px" tabindex="2" ${opportunity.source==1||opportunity.source==null?"disabled='disabled'":"" }>
										<option <c:if test="${num eq '0'}">disabled</c:if> value="">请选择</option>
										<c:forEach items="${sysUserList }" var="sysUser">
											<option value="${sysUser.id }" <c:if test="${num eq '0'}">disabled</c:if>
											<c:if test="${sysUser.id==opportunity.scuserid }">selected='selected'</c:if> >${sysUser.real_name }</option>
										</c:forEach>
									</select>
								</p>
								<p>
									<label>就近校区：</label> 
									<select name="opportunity.campusid" id="campusid" class="chosen-select" style="width: 150px" tabindex="2" onchange="chooseKcge()">
										<option  value="">请选择上课地点</option>
										<c:forEach items="${campusList }" var="campus">
											<option value="${campus.id }"  
											<c:if test="${campus.id==opportunity.campusid }">selected='selected'</c:if> >${campus.campus_name }</option>
										</c:forEach>
									</select>
									<c:forEach items="${campusList }" var="campus">
										<input id="c_kc_${campus.id }" <c:if test="${num eq '0'}">disabled</c:if> type="hidden" value="${campus.kcuserid }" >
									</c:forEach>
									<span class="spanred">*</span>
									<span id="campusMes" class="spanred"></span>
								</p>
								<c:if test="${code ne '1'}">
								<p>
									<label>${_res.get("course.consultant")}：</label>
									<select name="opportunity.kcuserid" id="kcuserid" class="chosen-select" style="width: 150px" tabindex="2">
									</select>
									<span id="kcgwMes" class="spanred"></span>
								</p> 
								</c:if>
								<p>
									<label>与学生关系：</label> 
									<select name="opportunity.relation" id="relation" class="chosen-select" onchange="ChangeState(this.value)" style="width: 150px" tabindex="2">
										<option value="1" <c:if test="${num eq '0'}">disabled</c:if> <c:if test="${opportunity.relation eq 1 }">selected='selected'</c:if>>本人</option>
										<option value="2" <c:if test="${num eq '0'}">disabled</c:if> <c:if test="${opportunity.relation eq 2 }">selected='selected'</c:if>>母亲</option>
										<option value="3" <c:if test="${num eq '0'}">disabled</c:if> <c:if test="${opportunity.relation eq 3 }">selected='selected'</c:if>>父亲</option>
										<option value="4" <c:if test="${num eq '0'}">disabled</c:if> <c:if test="${opportunity.relation eq 4 }">selected='selected'</c:if>>其他</option>
									</select>
								</p>
								<p>
									<label>主动联系：</label> 
									<select name="opportunity.needcalled" id="needcalled" class="chosen-select" style="width: 150px" tabindex="2">
										<option value="0" <c:if test="${num eq '0'}">disabled</c:if>
										<c:if test="${opportunity.needcalled}">selected='selected'</c:if>>${_res.get('admin.common.no')}</option>
										<option value="1" <c:if test="${num eq '0'}">disabled</c:if>
										<c:if test="${opportunity.needcalled}">selected='selected'</c:if>>${_res.get('admin.common.yes')}</option>
									</select>
								</p>
								<p>
									<label>${_res.get("Opp.Lead.Status")}：</label> 
									<select name="opportunity.isconver" id="isconver" class="chosen-select" style="width: 150px" tabindex="2">
										<option value="0"  <c:if test="${opportunity.isconver eq '0' }">selected='selected'</c:if>>${_res.get("Opp.No.follow-up")}</option>
										<option value="1"  <c:if test="${opportunity.isconver eq '1' }">selected='selected'</c:if>>${_res.get('Is.a.single')}</option>
										<option value="2"  <c:if test="${opportunity.isconver eq '2' }">selected='selected'</c:if>>${_res.get('Opp.Followed.up')}</option>
										<option value="3"  <c:if test="${opportunity.isconver eq '3' }">selected='selected'</c:if>>考虑中</option>
										<option value="4"  <c:if test="${opportunity.isconver eq '4' }">selected='selected'</c:if>>无意向</option>
										<option value="5"  <c:if test="${opportunity.isconver eq '5' }">selected='selected'</c:if>>已放弃</option>
										<option value="6"  <c:if test="${opportunity.isconver eq '6' }">selected='selected'</c:if>>有意向</option>
									</select>
								</p>
								<p>
									<label>下次回访时间：</label> 
									<input type="text" name="opportunity.nextvisit" readonly="readonly"  <c:if test="${num eq '0'}">disabled</c:if>
									value="${opportunity.nextvisit}" id="nextvisit" style="width: 150px" tabindex="2" />
									<%-- <input id="nextvisit" type="text" name="opportunity.nextvisit" readonly="readonly" value="${opportunity.nextvisit}" size="15" /> --%>
								</p>
								<p>
									<label>客户等级：：</label> 
									<select name="opportunity.customer_rating" id="customer_rating" class="chosen-select" style="width: 150px" tabindex="2">
										<option value="0"  <c:if test="${opportunity.customer_rating eq '0' }">selected='selected'</c:if>>未知客户</option>
										<option value="1"  <c:if test="${opportunity.customer_rating eq '1' }">selected='selected'</c:if>>潜在客户</option>
										<option value="2"  <c:if test="${opportunity.customer_rating eq '2' }">selected='selected'</c:if>>目标客户</option>
										<option value="3"  <c:if test="${opportunity.customer_rating eq '3' }">selected='selected'</c:if>>发展中客户</option>
										<option value="4"  <c:if test="${opportunity.customer_rating eq '4' }">selected='selected'</c:if>>交易客户</option>
										<option value="5"  <c:if test="${opportunity.customer_rating eq '5' }">selected='selected'</c:if>>后续介绍客户</option>
										<option value="6"  <c:if test="${opportunity.customer_rating eq '6' }">selected='selected'</c:if>>非客户</option>
									</select>
								</p>
								<p style="width:100%">
									<label>${_res.get('course.remarks')}：</label> 
									<textarea rows="5" cols="85" name="opportunity.note" <c:if test="${num eq '0'}">disabled</c:if> style="width:100%;overflow-x: hidden; overflow-y: scroll;">${fn:trim(opportunity.note)}</textarea>
								</p>
								<div style="clear:both"></div>
						      </div>
						   </div>
						</div>
						
						<div class="ibox float-e-margins border-bottom">
						   <div class="ibox-title">
						     <h5><a onclick="showStudentMessage('1')" href="#" >${_res.get('courselib.studentMsg')}</a></h5>
						     <div class="ibox-tools">
						       <a class="collapse-link"><i id="fa-chevron-down"  class="fa fa-chevron-down" onclick ="showStudentMessage('2')" ></i></a>
						     </div>
						   </div>
						   <div class="ibox-content" id="show">
						      <div>
								<input type="hidden" id ="studentid"  name="studentid" value="${student.id}">
								<p>
									<label>${_res.get('sysname')}</label>
									<input type="text" id="studentname" name="studentname" value="${student.REAL_NAME}" onchange="checkName('real_name')"><br>
									<span id="studentnames" class="spanred"></span> 
								</p>
								<p>
									<label>电话</label>
									<input type="text" id="tels"  name="tels" onchange="checkSomeMessage('tel')" value="${student.tel}"> 
									<span id="telsMes" class="spanred"></span>
								</p>
								<p>
									<label>性别</label>
									<input type="radio" id="studentsexs" <c:if test="${student.sex eq '1'}">checked="checked"</c:if> name="studentsex"  value="1"> 男
									<input type="radio" id="studentsex" <c:if test="${student.sex eq '0'}">checked="checked"</c:if> name="studentsex" value="0"> 女
								</p>
								<div style="clear: both;"></div>
								<p>
									<label>${_res.get('Date.of.birth')}</label>
									<input type="text" name="birth" readonly="readonly"  id="birth" style="width: 150px" tabindex="2"  value="${student.BIRTHDAY}"/>
								</p>
								<p>
									<label>证件类型</label>
									<select id="idtype"  name ="idtype" class="chosen-select" style="width: 150px" tabindex="2" > 
										<option value="1" <c:if test="${'1'==student.zjtype}">selected="selected"</c:if>>身份证</option>
										<option value="2" <c:if test="${2==student.zjtype}">selected="selected"</c:if>>驾驶证</option>
										<option value="3" <c:if test="${3==student.zjtype}">selected="selected"</c:if>>护照</option>
										<option value="4" <c:if test="${4==student.zjtype}">selected="selected"</c:if>>其他</option>
									</select>
								</p>
								<p>
									<label>证件号码</label>
									<input type="text" id="idnumber"  name="idnumber" value="${student.zjnumber}"> 
								</p>
								<p>
									<label>QQ</label>
									<input type="text" id="qq"  name="qq" value="${student.qq}"> 
								</p>
								<p>
									<label>Skype:</label>
									<input type="text" id="skype"  name="skype" value="${student.skype}"> 
								</p>
								<p>
									<label>邮箱</label>
									<input type="text" id="emails" name="emails" onchange="checkSomeMessage('email')" value="${student.email}"> 
									<span id="emailsMes" class="spanred"></span>
								</p>
								<p>
									<label>微信</label>
									<input type="text" id="wechat" name="wechat" value="${student.wechat}"> 
								</p>
								<p>
									<label>国籍</label>
									<input type="text" id="nationality" name="nationality"  value="${student.nationality}"> 
								</p>
								<p>
									<label>籍贯</label>
									<input type="text" id="naticeplace"  name="naticeplace" value="${student.address}"> 
								</p>
								<p>
									<label>家庭住址</label>
									<input type="text" id="address" name="address" value="${student.stuaddress}"> 
								</p>
								<p>
									<label>学校</label>
									<input type="text" id="school" name="school"  value="${student.school}"> 
								</p><p>
									<label>年级</label>
									<input type="text" id="grade"  name="grade" value="${student.grade_name}"> 
								</p>
								<p>
									<label>班主任</label>
									<input type="text" id="teacher" name="teacher" value="${student.classteacher}"> 
								</p>
								<p>
									<label>班主任电话</label>
									<input type="text" id="teachertel"  name="teachertel" value="${student.classteachertel}"> 
								</p>
								<p>
									<label>英语老师</label>
									<input type="text" id="englishteacher" name="englishteacher" value="${student.englishteacher}"> 
								</p>
								<p>
									<label>英语老师电话</label>
									<input type="text" id="englishteachertel"  name="englishteachertel" value="${student.englishteachertel}"> 
								</p>
								<input type="hidden" id="isshowMessage" name="isshowMessage" value=""> 
								<div style="clear: both;"></div>
							</div>
						   </div>
						</div>
						
						<p>
						<c:if test="${operator_session.qx_opportunitysave }">
						<c:if test="${operatorType eq 'add'}">
							<input id ="save" type="button" value="${_res.get('save')}"  class="btn btn-outline btn-primary" />
						</c:if>
						</c:if>
						<c:if test="${num eq '1'}">
						<c:if test="${operator_session.qx_opportunityupdate }">
						<c:if test="${operatorType eq 'update'}">
							<input id="save" type="button" value="${_res.get('update')}" class="btn btn-outline btn-primary" />
						</c:if>
						</c:if>
						</c:if>
						</p>
					</fieldset>
				</form>
			</div>
			<div style="clear: both;"></div>
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
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); 
    </script>
	<script>
	$(function(){
		$('#save').click(function(){
			getIds();
			var contacter = $("#contacter").val();
			var phonenumber = $("#phonenumber").val();
			var opportunityId = $("#opportunityId").val();
			var	 email= $("#email").val();
			if($("#contacter").val().trim()=="" || $("#contacter").val()==null){
				layer.msg("联系人不能为空！",1,2);
				$("#contacter").focus();
			}else if(!checkExist('contacter')){
				layer.msg("联系人已存在丶请更换",1,2)
				$("#contacter").focus();
			}else if(phonenumber==""){
				layer.msg("请填写联系电话",1,2);
				$("#phonenumber").focus();
			}else if(!checkExist('phonenumber')){
				layer.msg("电话已存在丶请更换",1,2)
				$("#phonenumber").focus();
			}/* else if(email.trim()==""){
				layer.msg("请填写邮箱",1,2);
				$("#email").focus();
			} */else if(!checkExist('email')){
				layer.msg("邮箱已存在丶请更换",1,2)
				$("#email").focus();
			}else if($("#source").val()==1 && $("#mediatorid").val()==""){
				layer.msg("请填写所属渠道",1,2);
				$("#mediatorid").focus();
			}else if($("#source").val()==6 && $("#recommendusername").val()==""){
				layer.msg("请填写介绍人",1,2);
				$("#recommendusername").focus();
			}else if($("#campusid").val()==""){
				layer.msg("请选择就近校区",1,2);
				$("#campusid").focus();
			}else if(!checkSomeMessage('tel')){
				$("#telMes").text("电话号码不能重复");
				$("#tel").focus();
			}else if(!checkSomeMessage('email')){
				$("#emailMes").text("电话号码不能重复");
				$("#emails").focus();
			}else{
				var flagChoose=false;
				var subjectids = $("#subjectids").val();
				if(subjectids!=""){
					flagChoose=true;
				}
					/*注释掉的部分是获取checkbox的value ，并对其进行判断*/
				/* $("input[id='subjectid']").each(function() {
					if($(this).prop("checked")){
						flagChoose=true;
					}
				}); */	
				if(flagChoose){
					var opportunityId = $("#opportunityId").val();
					if($("#source").val()==1){
						$("#scuserid").attr("disabled",false); 
						if($("#mediatorid").val()==""){
							alert("请填写所属渠道");
							return false;
						}
					}
					if(!$('#fa-chevron-down').is('.fa-chevron-down')){
						$("#isshowMessage").val(1);
						if(checkName('real_name')){
							return false;
						}
					}else{
						$("#isshowMessage").val(2);
					} 
					if(opportunityId==""){
						if(confirm("确定要添加该销售机会吗？")){
							$.ajax({
								type:"post",
								url:"${cxt}/opportunity/save",
								data:$('#opportunityForm').serialize(),// 你的formid
								datatype:"json",
								success : function(data) {
									 if(data.code=='0'){
										layer.msg(data.msg,2,5);
									}else{
										setTimeout("parent.layer.close(index)", 3000);
										parent.window.location.reload();
									} 
								}	
							});
						}
					}else{
						if(confirm("确定要修改该销售机会吗？")){
							$.ajax({
								type:"post",
								url:"${cxt}/opportunity/update",
								data:$('#opportunityForm').serialize(),// 你的formid
								datatype:"json",
								success : function(data) {
									 if(data.code=='0'){
										layer.msg(data.msg,2,5);
									}else{
										setTimeout("parent.layer.close(index)", 3000);
										parent.window.location.reload();
									} 
								}	
							});
						}
					}
				}else{
					alert("请选择咨询科目");
				}
			}
		});
	});
		function checkExist(checkField) {
			var checkValue = $("#"+checkField).val();
		    if (checkValue != "") {
		    	var flag = false;
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
		                	flag = true;
		                } 
		            }
		        });
		        return flag;
		    } else {
		    	return true;
		    }
		}
		function chooseSource(){
			var id=$("#source").val();
			
			if(id==1){
				$("#sop").hide();
				$("#qd").show();
				$("#scuserid").attr("disabled",true); 
				$("#mediatorid").attr("disabled",false); 
			}else if(id==6){
				$("#sop").show();
				$("#mediatorid").attr("disabled",false);
				$("#mediatorid").val('');
				$("#scuserid").attr("disabled",false); 
				$("#qd").hide();
			}else{
				$("#mediatorid").attr("disabled",false);
				$("#mediatorid").val('');
				$("#scuserid").attr("disabled",false); 
				$("#qd").hide();
				$("#sop").hide();
			}
			$("#scuserid").trigger("chosen:updated");
			$("#mediatorid").trigger("chosen:updated");
		}
		function chooseMarketer(id) {
			var scuserid = $("#m_sc_" + id).val();
			$("#scuserid").val(scuserid);
			$("#scuserid").trigger("chosen:updated");
		}
		/*获取多可科目选项的值*/
		function getIds() {
			var subjectids = "";
			var list = document.getElementsByClassName("search-choice");
			for (var i = 0; i < list.length; i++) {
				var name = list[i].children[0].innerHTML;
				var olist = document.getElementsByClassName("options");
				for (var j = 0; j < olist.length; j++) {
					var oname = olist[j].innerHTML;
					if (oname == name) {
						subjectids += olist[j].getAttribute('value') +"|" ;
						break;
					}
				}
			}
			
			var s = subjectids.substr(0,subjectids.length-1);
			$("#subjectids").val(s);
		}
		
		/*显示学生信息 根据关系  判断是否回填信息*/
		function showStudentMessage(num){
			if(num==1){
				document.getElementById( "show" ).style.display = "";
				$("#fa-chevron-down").removeClass("fa-chevron-down");
				$("#fa-chevron-down").addClass("fa-chevron-up");
			}else{
				if(!$('#fa-chevron-down').is('.fa-chevron-down')){
					$.layer({
						    shade : [0], 
						    area : ['auto','auto'],
						    dialog : {
						        msg:'您确定不保存学信息吗？',
						        btns : 2, 
						        type : 4,
						        btn : ['确定','取消'],
						        yes : function(){
										layer.msg("学生信息将不会保存",1,2);
										document.getElementById( "show" ).style.display = "none";
						        },
						        no : function(){
										$("#fa-chevron-down").removeClass("fa-chevron-down");
										$("#fa-chevron-down").addClass("fa-chevron-up");
										document.getElementById( "show" ).style.display ="" ;
						        }
						    }
						});
				}
			}
			var relation= $("#relation").val();
			var studentname = $("#contacter").val();
			var tel = $("#phonenumber").val();
			var email = $("#email").val();
			var sex = $("input:radio[id='sex']:checked").val();
			if(relation==1){
				 $("#studentname").val(studentname);
				 $("#tels").val(tel);
				 $("#emails").val(email);
				 if(sex==0){
					 $("#studentsex").attr("checked",true);	
					 $("#kcuserid").trigger("chosen:updated");
				 }else{
					 $("#studentsexs").attr("checked",true);	
					 $("#kcuserid").trigger("chosen:updated");
				}
			}
		}
		/*检验电话和邮箱是否已经被使用 ,根据ajax实现异步更新*/
		function checkSomeMessage(checkMessage){
			var checkValue = $("#"+checkMessage+"s").val();
			if(checkValue.trim()!=""){
				var flag = false;
				$.ajax({
					url:'/student/checkExist',
					type:'post',
					data:{"checkField":checkMessage,
						  "checkValue":checkValue,
						  "id":$("#studentid").val()},
					async:false,
					dataType:'json',
					success:function(data){
						 if (data.result >= 1) {
			                	$("#"+checkMessage).focus();
		                    	$("#"+checkMessage+"sMes").text("您填写的数据已存在。");
			                }else{
			                	$("#"+checkMessage+"sMes").text("");
			                	flag = true;
			                }
					}
				});
				return flag;
			}else{
				return true;
			}
		}
		/*与学生关系的状态改变是判断是否清空学生的姓名，电话，和邮箱*/
		function ChangeState(num){
			if(num!=1){
				 $("#studentname").val('');
				 $("#tels").val('');
				 $("#emails").val('');
				 $("#studentsexs").attr("checked",false);
				 $("#studentsex").attr("checked",false);	
			}else{
				var studentname = $("#contacter").val();
				var tel = $("#phonenumber").val();
				var email = $("#email").val();
				var sex = $("input:radio[id='sex']:checked").val();
					 $("#studentname").val(studentname);
					 $("#tels").val(tel);
					 $("#emails").val(email);
					 if(sex==0){
						 $("#studentsex").prop("checked",true);	
						 $("#studentsex").trigger("chosen:updated");
					 }else{
						 $("#studentsexs").prop("checked",true);	
						 $("#studentsexs").trigger("chosen:updated");
					}
			}
		}
		/*查询介绍人 ,数据是account中所有人的名字*/
		function findAccountByName(){
			var recommendusername  = $("#recommendusername").val();
				$.ajax({
					url:'/student/getNameByLike',
					type:'post',
					data:{"recommendusername":recommendusername},
					dataType:'json',
					success:function(result){
							if(result.accounts==1){
								$("#stuList").html("");
								$("#mohulist").hide();
							}
							if(result.accounts.length==0){
								$("#stuList").html("");
								$("#mohulist").hide();
							}
							if(result.accounts.length>=1){
								var str="";
								for(var i=0;i<result.accounts.length;i++){
									str+="<li onclick='dianstu(\""+result.accounts[i].REAL_NAME+"\")'>"+result.accounts[i].REAL_NAME+"</li>";
								}
								$("#stuList").html(str);
								$("#mohulist").show();
							}
					}
				});
		}
		/*回填模糊查询介绍人*/
		function  dianstu(name){
			$("#mohulist").hide();
			$("#mohulist").html();
			$("#recommendusername").val(name);
		}
		/*检查该学生名是否已经存在*/
		function checkName(checkField) {
		var checkValue = $("#studentname").val();
	    if (checkValue != "") {
	    	var flag = true;
	        $.ajax({
	            url: '${cxt}/student/checkExist',
	            type: 'post',
	            data: {
	                'checkField': checkField,
	                'checkValue': checkValue,
	                'id': null
	            },
	            async: false,
	            dataType: 'json',
	            success: function(data) {
	                if (data.result >= 1) {
	                	$("#studentname").focus();
                    	$("#studentnames").text("您填写的数据已存在。");
	                }else{
	                	$("#studentnames").text("");
	                	flag = false;
	                } 
	            }
	        });
	        return flag;
	    } else {
	        $("#studentname").focus();
	    	$("#studentnames").text("该字段不能为空。");
	        return true;
	    }
	}
		function chooseKcge() {
			var id = $("#campusid").val();
			var oppkcuserid = $("#oppkcuserid").val();
			var flag = $("#num").val()=='0';
			$.ajax({
				url:'/sysuser/getKcge',
				type:'post',
				data:{'id':id,'oppkcuserid':oppkcuserid},
				dataType:'json',
				success:function(data){
					 var str="";
					 if(data.oppkcuserid==""){
						 str+="<option <c:if test='"+flag+"'>disabled</c:if> value=''>请选择课程顾问</option>";
					 }
					for(var i = 0;i<data.accountList.length;i++){
						var flag = data.accountList[i].ID==data.oppkcuserid;
						var id = "kcuserid"+data.accountList[i].ID;
						 str+='<option id="'+id+'" <c:if test="'+flag+'">disabled</c:if> value="'+data.accountList[i].ID+'">'+data.accountList[i].REAL_NAME+'</option>';
					 }
					$("#kcuserid").html(str);
					$("#kcuserid"+data.oppkcuserid).attr("selected",true);
					$("#kcuserid").trigger("chosen:updated");
				}
			});
			chooseSource();
		}
		/*模糊查询渠道*/
		function getLikeMediator(name){
			if(name.trim()!=""){
				$.ajax({
					url:'/mediator/getLikeMediatorName',
					type:'post',
					data:{'name':name},
					dataType:'json',
					success:function(data){
						var str="";
						if(data.code==1){
							if(data.namelist.length>0){
								for(var i=0;i<data.namelist.length;i++){
									str+='<li onclick="showSelectName(\''+data.namelist[i].REALNAME+'\')">'+data.namelist[i].REALNAME+'</li>'
								}
							}
						}
						if(str!=""){
							$("#mediaorlist").html(str);
							$("#showSelectMessage").show();
						}
					}
					
				})
			}
		}
		/*回填选中的数据*/
		function showSelectName(name){
			$("#mediatorid").val(name);
			$("#showSelectMessage").hide();
		}
		/*隐藏*/
		function onblurmediator(){
			var flag = true;
			$("#showSelectMessage li").click(function(){
				flag = false;
			});
			setTimeout(function(){
				if(flag){
					$("#showSelectMessage").hide();
				}
			},100);
			
		}
		function showUpdateMessage(){
			
			var nums = $("#nums").val();
			if(nums==1 || nums==2){
				document.getElementById( "show" ).style.display = "";
				//$("#show").show();
			}else{
				document.getElementById( "show" ).style.display = "none";
			}
		}
		$(document).ready(chooseKcge());
		$(document).ready(showUpdateMessage());
	</script>
	<script type="text/javascript">
			var ids = '${opportunity.subjectids}';
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
		         chose_mult_set_ini('#subjectid',ids);
		         $(".chosen-select").chosen();
		     });
	</script>
	<script src="/js/utils.js"></script>
	
	 <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script src="/js/js/plugins/layer/laydate/laydate.dev.js"></script>
    <script>
       $('li[ID=nav-nav9]').removeAttr('').attr('class','active');
       
     //弹出后子页面大小会自动适应
       /* var index = parent.layer.getFrameIndex(window.name);
       parent.layer.iframeAuto(index); */
	//日期范围限制
	var nextvisit = {
		elem : '#nextvisit',
		format : 'YYYY-MM-DD',
		min: laydate.now(),
		max : '2099-06-16', //最大日期--gai zhe li
		istime : false,
		istoday : true
	};
	laydate(nextvisit);
	//出生日期
	var birth = {
		elem : '#birth',
		format : 'YYYY-MM-DD',
		//min: laydate.now(),
		max : laydate.now(), //最大日期--gai zhe li
		istime : false,
		istoday : true
	};
	laydate(birth);
    </script>
</body>
</html>