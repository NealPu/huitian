<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>反馈消息</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
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
</style>
</head>
<body>
  <div class="feedback-box" >
    <div class="chat-message1">
        <div class="chat-message1">
          <c:forEach items="${opportunity.fblist}"  var="feedback">
	          <div class="chat-message2">
	            <c:if test="${feedback.sysusername!=null}">
	             <table style="width:100%">
	                 <tr>
	                    <td style="width:8%"><a class="message-author1" href="#">${feedback.sysusername}</a></td>
	                    <td>
	                        <div class="chat-message3">
	                           <div style="float:right;">${feedback.createtime}</div><br>
	                               <span class="message-content">${feedback.content}</span>
	                        </div>
	                    </td>
	                 </tr>
	             </table>
	            </c:if>
	            <c:if test="${feedback.mediatorname!=null}">
	             <table style='width:100%'>
	                 <tr>
	                   <td style='width:92%'>
	                      <div class='chat-message4'>${feedback.createtime}<div style='float:right;'></div><br>
	                         <span class='message-content'>${feedback.content}</span>
	                      </div>
	                   </td>
	                   <td style='text-align:right;'>
	                      <a class='message-author1' href='#'>${feedback.mediatorname}</a>
	                   </td>
	                 </tr>
	             </table> 
	            </c:if>
	          </div>
          </c:forEach>
        </div>
       </div>
       <div style="width:450px;margin:0 auto 10px;">
         <form id="paymentForm" action="" method="post">
             <input id="opportunityId" name="opportunityId" type="hidden" value="${opportunity.id}"/>
             <label>销售机会反馈：</label>
             <textarea id="content" name="content" cols="100" rows="3" style="width:100%"></textarea>
             <c:if test="${operator_session.qx_opportunitytosaveFeedback }">
			 	<input onclick="saveAccount()" type="button" value="${_res.get('save')}" class="btn btn-outline btn-primary" />
             </c:if>
	     </form>
	  </div>
   </div>
    <!-- Mainly scripts -->
    <script src="/js/js/bootstrap.min.js?v=1.7"></script>
    <script src="/js/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/js/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="/js/js/hplus.js?v=1.7"></script>
    <script type="text/javascript">
     var index = parent.layer.getFrameIndex(window.name);
		  parent.layer.iframeAuto(index);  
		  function saveAccount(){
		    	  var result = $("#content").val();
		    	  if(result == ""  ){
		    		  parent.layer.msg("不能为空",2,8);
		    	  }else{
		    		   $.ajax({
		       			url:"${cxt}/opportunity/tosaveFeedback",
		       			type:"post",
		       			data:$('#paymentForm').serialize(),
		       			async: false,
		       			success:function(data){
		       					parent.layer.msg(data.msg,3,1);
		       				if(data.code=="1"){
		       					parent.window.location.reload();
				    			setTimeout("parent.layer.close(index)",4000);
		       				}
		       			}
		    		   });	
		    	   }
		    }
    </script>
    
</body>
</html>