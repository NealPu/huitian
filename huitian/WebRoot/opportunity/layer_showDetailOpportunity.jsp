<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"><meta name="renderer" content="webkit">
<title>销售机会信息</title>
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />
<link href="/css/css/layer/need/laydate.css" rel="stylesheet" />
<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script src="/js/js/jquery-2.1.1.min.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<style type="text/css">
 .chosen-container{
   margin-top:-3px;
 }
</style>
</head>
<body>
  <div style="padding:10px;max-height:450px">   
	<table class="table table-bordered">
		<tr>
        	<td class="table-bg1 ">${_res.get("Contacts")}</td>
        	<td class="table-bg2">${o.CONTACTER}</td>
        	<td class="table-bg1 ">${_res.get('gender')}</td>
        	<td class="table-bg2">${o.SEX?_res.get('student.boy'):_res.get('student.girl')}</td>
        	<td class="table-bg1 ">咨询科目</td>
        	<td class="table-bg2">${o.SUBJECTNAMES}</td>
        	<td class="table-bg1">${_res.get("admin.user.property.telephone")}</td>
        	<td class="table-bg2">${o.PHONENUMBER}</td>
        </tr>
        <tr>
        	<td class="table-bg1">学生关系</td>
        	<td class="table-bg2">${o.RELATION eq '1'?'本人':(o.RELATION eq '2'?'母亲':o.RELATION eq '3'?'父亲':'其他')}</td>
        	<td class="table-bg1">主动联系</td>
        	<td class="table-bg2">${o.NEEDCALLED?_res.get('admin.common.yes'):_res.get('admin.common.no')}</td>
        	<td class="table-bg1">反馈次数</td>
        	<td class="table-bg2">${o.FEEDBACKTIMES}(${_res.get("frequency")})</td>
        	<td class="table-bg1">成单状态</td>
        	<td class="table-bg2">
  			${o.ISCONVER eq '0'?_res.get("Opp.No.follow-up"):o.ISCONVER eq '1'?_res.get('Is.a.single'): o.ISCONVER eq '2'?_res.get('Opp.Followed.up'):o.ISCONVER eq '3'?'考虑中':o.ISCONVER eq '4'?'无意向': o.ISCONVER eq '5'?'已放弃':'有意向'}
  			</td>
        </tr>
        <tr>
        	<td class="table-bg1">成单日期</td>
        	<td class="table-bg2">${o.CONVERTIME==null?'':fn:substring(o.CONVERTIME ,"0","10")}</td>
        	<td class="table-bg1">${_res.get("Opp.Sales.Source")}</td>
        	<td class="table-bg2">${o.CRMSCNAME}</td>
        	<td class="table-bg1">咨询课程</td>
        	<td class="table-bg2">${o.CLASSTYPE eq '1'?_res.get("IEP"):_res.get('course.classes')}</td>
        	<td class="table-bg1">${_res.get("admin.sysLog.property.enddate")}</td>
        	<td class="table-bg2">${o.OVERTIME==null?'':fn:substring(o.OVERTIME,"0","10")}</td>
        </tr>
        <tr>
        	<td class="table-bg1">所属留学顾问</td>
        	<td class="table-bg2">${o.MEDIATORNAME==null?'':o.MEDIATORNAME}</td>
        	<td class="table-bg1">${_res.get("marketing.specialist")}</td>
        	<td class="table-bg2">${o.SCUSERNAME==null?'':o.SCUSERNAME}</td>
        	<td class="table-bg1">${_res.get("course.consultant")}</td>
        	<td class="table-bg2">${o.KCUSERNAME==null?'':o.KCUSERNAME}</td>
        	<td class="table-bg1">录入时间</td>
        	<td class="table-bg2">${fn:substring(o.CREATETIME,"0","10")}</td>
        </tr>
        <tr>
        	<td class="table-bg1">更新时间</td>
        	<td class="table-bg2">${o.UPDATETIME==null?'':fn:substring(o.UPDATETIME,"0","10")}</td>
        	<td class="table-bg1">确认用户</td>
        	<td class="table-bg2">${o.CONFIRMUSERNAME==null?'':o.CONFIRMUSERNAME}</td>
        	<td class="table-bg1">客户等级</td>
        	<td class="table-bg2">${o.customer_rating=='0'?'未知客户':o.customer_rating=='1'?'潜在客户':o.customer_rating=='2'?'目标客户':o.customer_rating=='3'?'发展中客户':o.customer_rating=='4'?'交易客户':o.customer_rating=='5'?'后续交易客户':'非客户'}</td>
        	<td class="table-bg1">下次回访日期</td>
        	<td class="table-bg2" colspan="2">${o.NEXTVISIT==null?"":fn:substring(o.NEXTVISIT,"0","10")}</td>
        </tr>
        <tr>
        	<td class="table-bg1">${_res.get("course.remarks")}</td>
        	<td colspan="7" class="table-bg2">${o.NOTE==null?'无':o.NOTE}</td>
        </tr>
        	<c:if test="${!empty student }">
        		<!-- <tr>
        			<td colspan="8"  align="center">该销售机会成单的学生购课交费信息 </td>
        		</tr> -->
        		<c:forEach items="${student}" var = "s">
	        			<tr>
				        	<td colspan="8" align="center">${_res.get('courselib.studentMsg')} </td>
			   		    </tr>
			   		    <tr>
			   		    	<td class="table-bg1">${_res.get('sysname')}</td>
			   		    	<td class="table-bg1">性别</td>
			   		    	<td class="table-bg1" colspan="2" >电话</td>
			   		    	<td class="table-bg1" colspan="2" >${_res.get('Date.of.birth')}</td>
			   		    	<td class="table-bg1" colspan="2" >邮箱</td>
			   		    </tr>
			   		    <tr>
			   		    	<td class="table-bg2">${s.REAL_NAME}</td>
			   		    	<td class="table-bg2">${s.sex=='0'?"女":"男"}</td>
			   		    	<td class="table-bg2" colspan="2" >${s.TEL==null?"暂无联系方式":s.TEL}</td>
			   		    	<td class="table-bg2" colspan="2" >${s.BIRTHDAY}</td>
			   		    	<td class="table-bg2" colspan="2" >${s.EMAIL}</td>
			   		    </tr>
	        		    <tr>
				        	<td colspan="8" align="center">交费信息</td>
	        		    </tr> 
		        		<tr>
		        			<td class="table-bg1">创建日期</td>
		        			<td class="table-bg1">交费日期</td>
		        			<td class="table-bg1">交费状态</td>
				       	 	<td class="table-bg1">${_res.get('course.subject')}</td>
				       	 	<td class="table-bg1">${_res.get('total.sessions')}</td>
				       	 	<td class="table-bg1">使用定金</td>
				       	 	<td class="table-bg1">${_res.get('total.amount')}</td>
				       	 	<td class="table-bg1">实收</td>
				       	 	<!-- <td class="table-bg1">平均课时单价</td> -->
				       	 	
		        		</tr>
		        		<c:forEach items="${s.courseorders}" var="c">
			        		<tr>
			        			<td class="table-bg2">${fn:substring(c.CREATETIME,"0","10")}</td>
			        			<td class="table-bg2">${fn:substring(c.PAIEDTIME,"0","10")}</td>
			        			<td class="table-bg2">${c.STATUS=='0'?'未支付':c.STATUS=='1'?'已支付':'欠费'}</td>
			        			<td class="table-bg2">${c.SUBNAME}</td>
					       	 	<td class="table-bg2">${c.CLASSHOUR}</td>
					       	 	<td class="table-bg2">${c.DEPOSIT}</td>
					       	 	<td class="table-bg2">${c.TOTALSUM}</td>
					       	 	<td class="table-bg2">${c.REALSUM}</td>
					       	 	<%-- <td class="table-bg2">${c.AVGPRICE}</td> --%>
			        		</tr>
		        		</c:forEach> 
		        		<%-- <c:if test="${ !empty s.PAIEDTIME}">
			        		<tr>
					        	<td class="table-bg1" colspan="8" align="center">完成交费日期：${fn:substring(s.PAIEDTIME,"0","16")}</td>
		        		    </tr>
	        		    </c:if>  --%>
	        	</c:forEach>
        	</c:if>
	</table>
 </div>	
    <script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
		
    <script>
    $(".chosen-select").chosen({disable_search_threshold: 20});
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
        
      //弹出后子页面大小会自动适应
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.iframeAuto(index);
    </script>	
</body>
</html>