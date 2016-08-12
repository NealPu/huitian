<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.fa {
	    display: inline-block;
	    font-family: FontAwesome;
	    font-style: normal;
	    font-weight: normal;
	    line-height: 1;
	    font-size-adjust: none;
	    font-stretch: normal;
	    font-feature-settings: normal;
	    font-language-override: normal;
	    font-kerning: auto;
	    font-synthesis: weight style;
	    font-variant: normal;
	    font-size: inherit;
	    text-rendering: auto;
	}
</style>

<div class="navbar-header">
   <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" href="#">
     <i class="fa fa-bars"></i>
   </a>
	<ul class="topright-nav" id ="topMessage">
 
	</ul>
</div>

<div class="top-index" >
	<ul class="nav navbar-top-links navbar-right nav-topwidth" id="nav-topwidth">
		<%-- <li class="guojihua">
				<span class="m-r-sm text-muted">
					${_res.get("admin.common.sl") }：
					<input type="button" value="中文" onclick="chengeI18n('zh_CN')" class="btn btn-success btn-outline btn-xs">&nbsp;&nbsp;
					<input type="button" value="En" onclick="chengeI18n('en_US')" class="btn btn-primary btn-outline btn-xs">
				</span>
		</li> --%>
		<li > <a onclick="entryNetSchool()" > 进入网校 </a> </li>
		<li class="dropdown">
			<a class="dropdown-toggle count-info" data-toggle="dropdown" href="index.html#"> 
				<i class="fa fa-bell"></i> 
				<span class="label label-primary" id="total" style="background:#1ab394;"></span>
			</a>
			<ul class="dropdown-menu dropdown-alerts">
			 <c:if test="${operator_session.qx_orders}">
				<li><a  href="/orders/index?_query.isread=0&&_query.scuserid=${account_session.id}">
						<div>
							<i class="fa fa-envelope fa-fw"></i> 最新订单
							<!-- 管理员  财务 -->
							<span class="pull-right text-muted small" id="orderCount"></span>
						</div>
				</a></li>
				<li class="divider"></li>
				</c:if>
				
				<c:if test="${operator_session.qx_opportunity}">
				<li><a href="/opportunity/index?_query.isread=0&&_query.scuserid=${account_session.id}">
						<div>
							<i class="fa fa-qq fa-fw" ></i> 新增销售机会
							<!-- 所有 -->
							<span class="pull-right text-muted small" id="oppocounts"></span>
						</div>
				</a></li>
				<li class="divider"></li>
				</c:if>
				
				<c:if test="${operator_session.qx_studentbirthday}">
				<li><a href="#" onclick="toStudentBirthday()" id="tobirthday">
						<div>
							<i class="fa fa-birthday-cake"></i>&nbsp;生日提醒
							<!-- 所有 -->
							<span class="pull-right text-muted small" id="birthcounts"></span>
						</div>
				</a></li>
				<li class="divider"></li>
				</c:if>
				
				<c:if test="${operator_session.qx_reportteacherReports}">
					<li><a href="" onclick="toTeacherReport()" id="reportid" >
							<div>
								<i class="fa fa-file-text"></i>&nbsp;报告提醒
								<span class="pull-right text-muted small" id="reportcounts"></span>
							</div>
					</a></li>
						<li class="divider"></li>
				</c:if>
				<c:if test="${operator_session.qx_reportteacherReports}">
					<li><a href="/teacher/queryAllReceiver"  >
							<div>
								<i class="fa fa-comment"></i>&nbsp;消息提醒
								<span class="pull-right text-muted small" id="recvertions"></span>
							</div>
					</a></li>
				</c:if>
				<!--  <li>
                                <div class="text-center link-block">
                                    <a href="notifications.html">
                                        <strong>查看所有 </strong>
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </div>
                            </li>  -->
			</ul>
		</li>
        <li><a href="/account/exit"><i class="fa fa-sign-out"></i>退出</a></li>
		
	</ul>
</div>	
<script type="text/javascript">
function chengeI18n(type){
	$.ajax({
		url:"/i18n/setI18nCookies",
		type:"post",
		data:{"_locale":type},
		dataType:"json",
		success:function(result){
			window.location.reload();
		}
	});
}

function getMessage(){
	$.ajax({
		url : "${cxt}/main/getMessage",
		type : "post",
		dataType : "json",
		success : function(data) {
			var str1=data.total;
			var str2=data.orderCount;
			var str3=data.oppocounts;
			$("#total").text(str1);
			$("#orderCount").text(str2);
			$("#oppocounts").text(str3);
			$("#birthcounts").text(data.count);
			$("#reportcounts").text(data.reportcounts);
			$("#recvertions").text(data.noann);
		}
	});
}


 function toTeacherReport(){
	var daytime = getCurrentDayTime();
	$("#reportid").attr("href","/report/teacherReports?_query.state=0&&_query.startappointment="+daytime+"&&_query.endappointment="+daytime);
	
} 
function toStudentBirthday(){
	var d = new Date();
	var str = (d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()).toString();
	$("#tobirthday").attr("href","/student/birthday?_query.like="+str);
}

function getCurrentDayTime(){
	var mydate = new Date();
	var year =  mydate.getFullYear();
	var month = mydate.getMonth()+1; 
	var day = mydate.getDate(); 
	return year+"-"+month+"-"+day;
}
function getHeadMessage(){
   	$("#topMessage").html('');
   	$.ajax({
   		url:'/operator/getHeadMessage',
   		type:'post',
   		data:{},
   		dataType:'json',
   		success:function(data){
   			var str="";
   			for(i in data){
   				if(data[i].FLAG){
   					if('${head}'==data[i].ID){
   						str +=' <a href="/operator/getModulCompetence?systemsid='+data[i].ID+'">';
   						if('${en}'==1){
   							str +='<li sty class="top-background"><div>'+data[i].NAMES+'</div></li></a> ';
   						}else if('${en}'==2){
   							str +='<li sty class="top-background"><div>'+data[i].NUMBERS+'</div></li></a> ';
   						}else{
   							str +='<li sty class="top-background"><div>'+data[i].NAMES+'</div></li></a> ';
   						}
   	   	   				
   					}else{
   						str +=' <a href="/operator/getModulCompetence?systemsid='+data[i].ID+'">';
   						if('${en}'==1){
   	   	   					str +='<li ><div>'+data[i].NAMES+'</div></li></a> ';
   						}else if('${en}'==2){
   							str +='<li ><div>'+data[i].NUMBERS+'</div></li></a> ';
   						}else{
   							str +='<li ><div>'+data[i].NAMES+'</div></li></a> ';
   						}
   					}
   					
   				}
   			}
   			$("#topMessage").append(str);
   		}
   	});
   }
$(document).ready(function(){
	getHeadMessage();
	/* setTimeout(function(){
		getMessage();
	}, 3000);
	setInterval(function(){
		getMessage();
	}, 180000); */
});

	function entryNetSchool() {
		$.ajax( {
			url : "",
			async : false,
			success : function( result ) {
				
			}
			
		} );
	}
	

</script>
