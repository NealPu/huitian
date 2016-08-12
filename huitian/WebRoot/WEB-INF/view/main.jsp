<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<title>云教务排课系统</title>

<jsp:include page="mainpagecss.jsp" />
</head>
<body class="pace-done" >
	<div id="wrapper">
		<%@ include file="/common/left-nav.jsp"%>
		<div id="page-wrapper" class="gray-bg dashbard-1">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
				    <%@ include file="/common/top-index.jsp"%>
				</nav>
			</div>
			<div class="wrapper wrapper-content">
<!--综合统计信息《包含年、季度、月、周》 -->
				<c:if test="${operator_session.qx_maingetMonth}">
					<div class="row"> 
						<div style="margin-left:14px">
							<!-- 查找收入，销售机会，入学人数 -->
							<input type="button" class="btn btn-primary btn-outline btn-sm" id="week" onclick ="zhou()" value="近一周">
							<input type="button" class="btn btn-danger btn-outline btn-sm" id="month" onclick ="yigeyue()" value="近一个月">
							<input type="button" class="btn btn-warning btn-outline btn-sm" id="thMonth" onclick ="sangeyue()" value="近三个月">
							<input type="button" class="btn btn-success btn-outline btn-sm" id="year" onclick ="yinian()" value="近一年">
							<div style="height:10px"></div>
						</div>	
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
							
								<div class="ibox-title" >
									<span class="label label-success pull-right" id="shouru">月</span>
									<h5>收入</h5>
								</div>
								<div class="ibox-content">
									  
									<a href=""><h1 class="no-margins" id="income1"></h1></a>
									<h1 class="no-margins" id="income1"></h1>
									<div class="stat-percent font-bold text-success" id="income2"></div>
									<!-- <small>本月收入</small> -->
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<span class="label label-info pull-right" id="jihui">月</span>
									<h5>${_res.get('Opp.Sales.Opportunities')}</h5>
								</div>
								<div class="ibox-content">
									<a href="/opportunity/index"><h1 class="no-margins" id="saleyear1"></h1></a>
									<h1 class="no-margins" id="saleyear1"></h1>
									<div class="stat-percent font-bold text-info" id="saleyear2"></div>
									<!-- <small>本月销售机会</small> -->
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<span class="label label-success pull-right" id="xuesheng" >月</span>
									<h5>${_res.get("class.number.of.students")}</h5>
								</div>
								<div class="ibox-content">
									<a  href="/student/index?_query.state=0" ><h1 class="no-margins" id="stunum1"></h1></a>
									<h1 class="no-margins" id="stunum1"></h1>
									<div class="stat-percent font-bold text-success" id="stunum2"></div>
									<!-- <small>本月学生人数</small> -->
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<span class="label label-primary pull-right" id="keshi" >月</span>
									<h5>${_res.get('session')}</h5>
								</div>
								<div class="ibox-content">
									<a  href="/course/queryAllcoursePlans?loginId=${account_session.id}&returnType=7&campusId="  >
									<h1 class="no-margins" id="coursenum1"></h1></a>
									<div class="stat-percent font-bold text-navy" id="coursenum2"></div>
									<!-- <small>本月课时数</small> -->
								</div>
							</div>
						</div>
						<div class="col-lg-12">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>订单</h5>
									<div class="pull-right">
										<div class="btn-group">
											<button type="button" class="btn btn-xs btn-white active">天</button>
											<!--  <button type="button" class="btn btn-xs btn-white">月</button>
	                                        <button type="button" class="btn btn-xs btn-white">年</button>  -->
										</div>
									</div>
								</div>
								<div class="ibox-content">
									<div class="row">
										<div class="col-lg-9">
											<div class="flot-chart">
												<div class="flot-chart-content" id="flot-dashboard-chart"></div>
											</div>
										</div>
										<div class="col-lg-3">
											<ul class="stat-list">
												<li>
													<h2 class="no-margins" id="userOrders" ></h2> <small id="dingdan">订单总数(周、月、年/单)</small>
													<div class="stat-percent">
													</div>
													<div class="progress progress-mini">
														<div style="width: 0;" class="progress-bar"></div>
													</div>
												</li>
												<li>
													<h2 class="no-margins" id="totalOrders" ></h2> <small id="fukuan">付款总数(周、月、年/单)</small>
													<div class="stat-percent">
													</div>
													<div class="progress progress-mini">
														<div style="width: 0;" class="progress-bar"></div>
													</div>
												</li>
												<li>
													<h2 class="no-margins" id="totalNum"></h2> <small id="jine">付款金额（周.月.年/RMB）</small>
													<div class="stat-percent">
													</div>
													<div class="progress progress-mini">
														<div style="width: 0;" class="progress-bar"></div>
													</div>
												</li>
											</ul>
										</div>
									</div>
								</div>
	
							</div>
						</div>
					</div>
				</c:if>
		
<!--教师相关数据信息-->
				<c:if test="${operator_session.qx_mainteachermessage}">
					<div class="row">
						<div style="margin-left:14px">
							<!-- 查找收入，销售机会，入学人数 -->
							<input type="button" class="btn btn-primary btn-outline btn-sm"  onclick ="queryCoTM(1)" value="${_res.get('This_week')}">
							<input type="button" class="btn btn-danger btn-outline btn-sm" onclick ="queryCoTM(2)" value="${_res.get('This_month')}">
							<input type="button" class="btn btn-warning btn-outline btn-sm"  onclick ="queryCoTM(3)" value="${_res.get('This_quarter')}">
							<input type="button" class="btn btn-success btn-outline btn-sm"  onclick ="queryCoTM(4)" value="${_res.get('This_Year')}">
							<div style="height:10px"></div>
						</div>	
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
							
								<div class="ibox-title" >
									<span class="label label-success pull-right" id="kehsi">月</span>
									<h5>${_res.get('class.number.of.class.sessions')}</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins" id="income3"></h1>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<span class="label label-success pull-right" id="yiqiandao" >月</span>
									<h5>${_res.get('courselib.sign')}（${_res.get('frequency')}）</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins" id="stunum3"></h1>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<span class="label label-primary pull-right" id="weiqiandao" >月</span>
									<h5>${_res.get('courselib.notSignin')}（${_res.get('frequency')}）</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins" id="coursenum3"></h1>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<span class="label label-info pull-right" >日</span>
									<h5>${_res.get("Today_lesson")}</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins" id="saleyear3"></h1>
								</div>
							</div>
						</div>
					</div>
					<div> 
						<div class="ibox float-e-margins" >
							<div class="ibox-title" style="height:52px; ">
								<h5><span id="teacher">${_res.get('Your_course_information_today')}</span></h5>
								<div id="showcourseplanmessage" style="float: right;"></div>
							</div>
							<div class="ibox-content">
							<table id="coursemessage" class="table table-hover table-bordered" width="100%">
								
							</table>
							</div>
							<div id="hid"></div>
						</div>
					</div>
					<div> 
						<div class="ibox float-e-margins" >
							<div class="ibox-title">
								<h5><span id="teacher">${_res.get("Your_Reservation_information")}</span></h5>
							</div>
							<div class="ibox-content">
							<table id="reservation" class="table table-hover table-bordered" width="100%">
								
							</table>
							</div>
							<div id="hid"></div>
						</div>
					</div>
				</c:if>
			 <c:if test="${isstudent}">
				<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div> 
						<div class="ibox-title">
							<h5><span id="student">${_res.get('Your_course_information_today')}</span></h5>
						</div>
						<div class="ibox-content">
						<table id="studentcoursemessage" class="table table-hover table-bordered" width="100%">
							
						</table>
						</div>
						<!-- <div id="hid"></div> -->
					</div>
				</div>
			</div>
			</c:if>				
<!--公告信息发送-->				
				<c:if test="${operator_session.qx_teachergetTeacherSendMessage}">
					<div class="row">
						<div class="col-lg-4" style="width:50%">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>${_res.get('It_has_issued_notice')}</h5>
									<div class="ibox-tools">
										<c:if test="${operator_session.qx_teachersendMessage}">
											<input type="button" onclick="sendNews()" class="btn btn-xs btn-info" 
												 value="${_res.get('Send_announcement')}" />
										</c:if>
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i> </a> 
										<a class="close-link"> <i class="fa fa-times"></i> </a>
									</div>
								</div>
								<div class="ibox-content ibox-heading">
									<small><i class="fa fa-tim" id="sends"></i> </small>
								</div>
								<div class="ibox-content">
									<div class="feed-activity-list" id="sendmessage">
									</div>
								</div>
							</div>
						</div>
						<div class="col-lg-4" style="width:50%">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>${_res.get('Announcement_Information')}</h5>
									<div class="ibox-tools">
										<c:if test="${operator_session.qx_teacherupdateStates}">
											<input type="button" onclick="updateStates()" class="btn btn-xs btn-info" value="${_res.get('Mark_all_as_read')}">
										</c:if>
										<a class="collapse-link"> 
											<i class="fa fa-chevron-up"></i>
										</a> 
										<a class="close-link"> 
											<i class="fa fa-times"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content ibox-heading">
									<small><i class="fa fa-tim" id="feedbacklengths"></i> </small>
								</div>
								<div class="ibox-content">
									<div class="feed-activity-list" id="feedbackmsgs">
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:if>
		

				
				<div class="row">
<!--反馈消息 ——销售的相关数据-->
					<c:if test="${operator_session.qx_feedbackqueryFeedbackJson}">
						<div class="col-lg-4">
							<div class="ibox float-e-margins">
								<div class="ibox-title">
									<h5>反馈消息</h5>
									<div class="ibox-tools">
										<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
										</a> <a class="close-link"> <i class="fa fa-times"></i>
										</a>
									</div>
								</div>
								<div class="ibox-content ibox-heading">
									<h3>
										<i class="fa fa-envelope-o"></i> 新消息
									</h3>
									<small><i class="fa fa-tim" id="feedbacklength"></i> </small>
								</div>
								<div class="ibox-content">
									<div class="feed-activity-list" id="feedbackmsg">
									</div>
								</div>
							</div>
						</div>
					</c:if>
					
					<div class="col-lg-8">
					<div class="row">
<!--今日待回访客户—销售相关的数据 -->
						<c:if test="${operator_session.qx_opportunitycheckTodayReturnMessage}">
							<div class="col-lg-12">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>今日待回访客户</h5>
										<div class="ibox-tools">
											<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
											</a> <a class="close-link"> <i class="fa fa-times"></i>
											</a>
										</div>
									</div>
									<div class="ibox-content">
										<table class="table table-hover no-margins">
											<thead>
												<tr>
													<th style="text-align: center">${_res.get('admin.dict.property.status')}</th>
													<th style="text-align: center">课程顾问</th>
													<th style="text-align: center">就近校区</th>
													<th style="text-align: center">咨询科目</th>
													<th style="text-align: center">${_res.get("Contacts")}</th>
													<th style="text-align: center">联系人电话</th>
													<th style="text-align: center">回访次数</th>
													<th style="text-align: center">${_res.get("operation")}</th>
												</tr>
											</thead>
											<tbody id="returnvisit">

											</tbody>
										</table>
									</div>
								</div>
							</div>
						</c:if>	
						</div>
					</div>
				</div>
				
				<div class="row" >
					<div class="col-lg-5" >
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>网校课程销售</h5>
								<div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"> </i> </a> 
									<a class="close-link"> <i class="fa fa-times"> </i> </a>
								</div>
							</div>
							<div class="ibox-content">
								<table class="table table-hover no-margins">
									<thead>
										<tr>
											<th>序号</th>
											<th>课程</th>
											<th>销售数量</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="courseSaleRecords" >
										
									</tbody>
								</table>
							</div>
						</div>
					</div>	
					<div class="col-lg-7">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>课程评论</h5>
								<div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"> </i> </a> 
									<a class="close-link"> <i class="fa fa-times"> </i> </a>
								</div>
							</div>
							<div class="ibox-content">
								<table class="table table-hover no-margins">
									<thead>
										<tr>
											<th>序号</th>
											<th>课程</th>
											<th>评论</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="courseCommentRecords" >
										
									</tbody>
								</table>
							</div>
						</div>
					</div>	
				</div>
				<div class="row" >
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>课程答疑</h5>
								<div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"> </i> </a> 
									<a class="close-link"> <i class="fa fa-times"> </i> </a>
								</div>
							</div>
							<div class="ibox-content">
								<table class="table table-hover no-margins">
									<thead>
										<tr>
											<th>序号</th>
											<th>课程</th>
											<th>问题</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody id="courseQARecords" >
										
									</tbody>
								</table>
							</div>
						</div>
					</div>	
				</div>
				
			</div>
			<div class="footer">
				<div class="pull-right">
					<a href="http://www.yunjiaowu.cn" target="_blank">BY：YunJiaoWu.CN</a>
				</div>
				<div>
					<strong>Copyright</strong> 云教务 &copy; 2015-${copyrighYear }
				</div>
			</div>
		</div>
	</div>
	
	
	<jsp:include page="mainpagejs.jsp" />
	
</body>
</html>