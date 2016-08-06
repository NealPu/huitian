<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>分配</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" href="/css/css/style.css" rel="stylesheet" />
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/js/js/plugins/layer/skin/layer.css" rel="stylesheet"/>
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet" />

<!-- Morris -->
<script src="/js/js/ztree/jquery-1.4.4.min.js"></script>
<script src="/js/js/ztree/jquery.ztree.core-3.5.js"></script>
<script src="/js/js/ztree/jquery.ztree.excheck-3.5.js"></script>
<script src="/js/js/ztree/jquery.ztree.exhide-3.5.js"></script>
<script src="/js/js/jquery-2.1.1.min.js"></script>

<script type="text/javascript">
		
</script>

<style type="text/css">
</style>
</head>
<body>
			<div class="ibox-content" >
				<div class="boxes" >
					<div class="box_span">
						<p>
						</p>
						<div class="box-content">
							<table width="80%" class="table table-hover table-bordered">
									<thead>
										<tr>
											<th><input type="checkbox" id="oppids" name="oppids" onclick="checkAllOpps()"></th>
											<th>咨询顾问</th>
											<th>${_res.get('gender')}</th>
											<th>用户类型</th>
											<th>未成单数量</th>
											<th>${_res.get('District')}</th>
										</tr>
									</thead>
									<c:forEach items="${kcgws}" var="kcgws" varStatus="index">
										<tr align="center">
											<td><input type="checkbox" id="oppids${kcgws.id}"  name="opportunityids" value="${kcgws.id}" ></td>
											<td>${kcgws.REAL_NAME }</td>
											<td>${kcgws.SEX==1?_res.get('student.boy'):_res.get('student.girl') }</td>
											<td>${kcgws.TYPENAME }</td>
											<td>${kcgws.shu}</td>
											<td>${kcgws.CAMPUS_NAME }</td>
										</tr>
									</c:forEach>
								</table>
						</div>
					</div>
				</div>
			
		
	<input type="hidden" value="${oppids }" id="oppidvalue" >
	<input type="hidden" value="${opp }" id="opp" >
		<div style="clear: both;"></div>
		<div style="width:28px;margin:10px 0 0 280px;">
			<c:if test="${operator_session.qx_opportunityallotSave }">
		    	<input type="button" value="${_res.get('save')}" onclick="setSaveValue();" class="btn btn-outline btn-primary" />
			</c:if>
		</div>
	 </div>	
    <script type="text/javascript">
	    function checkAllOpps(){
	    	if($("#oppids").is(":checked")){
		    	$("[name='opportunityids']").each(function(){
		    		$(this).prop("checked","checked");
				});
	    	}else{
		    	$("[name='opportunityids']").each(function(){
		    		$(this).prop("checked",false);
				});
	    	}
	    }
    </script>
    
    <!-- layer javascript -->
	<script src="/js/js/plugins/layer/layer.min.js"></script>
	<script>
        layer.use('extend/layer.ext.js'); //载入layer拓展模块
    </script>
    <script type="text/javascript">
		var index = parent.layer.getFrameIndex(window.name);
		
	    function setSaveValue(){
	    	var opp = $("#opp").val();
	    	var oppsize = opp.split("|").length;
			var kcgwIdValue = [];
			$('input[name="opportunityids"]:checked').each(function() {
				kcgwIdValue.push($(this).val());
			});
			if(kcgwIdValue.length==0){
				layer.msg("请选择要分配的人员。",3,8);
				return false;
			}else{
				if(kcgwIdValue.length>oppsize){
					layer.msg("总过只有"+oppsize+"个销售机会要分配",3,8);
					return false;
				}else{
					if(confirm('确认此次分配？')){
			    		$.ajax({
			    			url:"${cxt}/opportunity/allotSave",
			    			type:"post",
			    			data:{
			    				"oppids" : $("#oppidvalue").val(),
			    				"kcgwsids" : kcgwIdValue.join(",")
			    				},
			    			dataType:"json",
			    			success:function(data){
			    				parent.layer.msg(data.msg,2,1);
			    				if(data.code == "1"){
			    					/* parent.window.location.href="/opportunity/allot"; */
			    					parent.window.location.reload();
			    					parent.layer.close(index);
			    				}
			    			}
			    		});
			    	}
				}
			}
	    }
	   
	  //弹出后子页面大小会自动适应
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.iframeAuto(index);
	</script>
    
</body>
</html>