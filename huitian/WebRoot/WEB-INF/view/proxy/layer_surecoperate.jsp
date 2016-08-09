<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../basecss.jsp" />
<style type="text/css" >
	.leftalign { text-align: left; }
</style>
<script src="/js/js/jquery-2.1.1.min.js"></script>
<div id="sureCooperationPage" >
   	<div style="width:100%;">	
		<div class="ibox-content">
			<div style="height: 90%;width: 100%;">
				<form action="" method="post" id="sureProjectForm"  > 
					<fieldset>
						<input type="hidden" name="proxyProject.proxyid" value="${proxy.id }" />
						<input type="hidden" name="proxyProject.projectid" id="sureProjectId" value="${project.id }" />
						<div class=""> 
							<div class="m-t-sm flt" >
								<label class="input-s-sm  " >合作项目：</label>
								<span class="" id="sureProjectName" > ${project.projectname } </span>
							</div>
							<div style="clear:both;" ></div>
							<div class="m-t-md flt" id="cooperateDates" >
								<label class="input-s-sm " >合作日期：</label>
								<input type="text" class="znzhong" readonly="readonly" id="projectStart" name="proxyProject.startdate"  /> - 
								<input type="text" class="znzhong" readonly="readonly" id="projectEnd" name="proxyProject.enddate" /> 
							</div>
							<div style="clear:both;" ></div>
							<input type="button"  onclick="submitProject()" class="btn btn-sm btn-info m-t-md"  value="确定" /> 
							<div style="clear:both;" ></div>
						</div>  
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>	

<jsp:include page="../basejs.jsp" />
<script type="text/javascript" >
	$( document ).ready( function() {
		var startDate = {
			elem : "#projectStart",
			format : 'YYYY-MM-DD',
			min : '${cooperation.startdate}',
			max : '${cooperation.enddate}',
			istime : false,
			istoday : true,
			choose : function( date ) {
				endDate.min = date;
			}
		}
		var endDate = {
			elem : "#projectEnd",
			format : 'YYYY-MM-DD',
			min : '${cooperation.startdate}',
			max : '${cooperation.enddate}',
			istime : false,
			istoday : true,
			choose : function( date ) {
				startDate.max = date;
			}
		}
		laydate( startDate );
		laydate( endDate );
		
	} );
	
	function submitProject() {
		if( confirm( "确定合作该项目？" ) ) {
			$.ajax( {
				url : "/proxy/sureCooperate",
				data : $( "#sureProjectForm" ).serialize(),
				dataType : "json",
				type : "post",
				success : function( result ) {
					if( result.flag ) {
						parent.research();
					} else {
						layer.msg( result.msg , 2 ,2 );
					}
				}
			} );
		}
	}
	
</script>


