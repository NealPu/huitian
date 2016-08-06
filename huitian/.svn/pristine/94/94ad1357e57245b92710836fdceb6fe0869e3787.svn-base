<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<link type="text/css" href="/css/css/style.css" rel="stylesheet" /> 
<link type="text/css" href="/css/css/bootstrap.min.css" rel="stylesheet" />
<link href="/css/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="/css/css/plugins/chosen/chosen_style.css" rel="stylesheet">
<link href="/font-awesome/css/font-awesome.css?v=1.7" rel="stylesheet">
<link href="/css/css/laydate.css" rel="stylesheet">
<link href="/css/css/layer/need/laydate.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">
<link href="/css/css/summernote.css" rel="stylesheet">
<link href="/css/css/summernote-bs3.css" rel="stylesheet"> 

<!-- Morris -->
<link href="/css/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
<!-- Gritter -->
<link href="/js/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="/css/css/animate.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-validation-1.8.0/lib/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-validation-1.8.0/jquery.validate.js"></script>
<link rel="shortcut icon" href="/images/ico/favicon.ico" />
<title>添加定点咨询</title>
<style>
label{
 width:170px;
}
.no-padding{
   border:1px solid #EEE;
}
</style>
</head>
<body>
<div id="wrapper" style="background: #2f4050;height:100%;">
  <%@ include file="/common/left-nav.jsp"%>
  <div class="gray-bg dashbard-1" id="page-wrapper" style="height:100%;">
    <div class="row border-bottom">
     <nav class="navbar navbar-static-top" role="navigation" style="margin-left:-15px;position:fixed;width:100%;background-color:#fff;">
        <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " id="btn-primary" href="#" style="margin-top:10px"><i class="fa fa-bars"></i> </a>
            <div style="margin:20px 0 0 70px;"><h5><img alt="" src="/images/img/currtposition.png" width="16" style="margin-top:-1px">&nbsp;&nbsp;<a href="javascript:window.parent.location='/account'">${_res.get('admin.common.mainPage')}</a> 
            &gt;<a href='/opportunity/index'>${_res.get('Opp.Sales.Opportunities')}</a> &gt;<a href='javascript:history.go(-1);'>定点咨询</a>&gt;添加定点咨询</h5>
            <a onclick="window.history.go(-1)" href="javascript:void(0)" class="btn btn-outline btn-primary btn-xs" style="float:right">${_res.get("system.reback")}</a>
          		<div style="clear:both"></div>
            </div>
        </div>
        <div class="top-index"><%@ include file="/common/top-index.jsp"%></div>
     </nav>
   </div>
   
   <div class="margin-nav" style="margin-left:0;">
     <div class="ibox float-e-margins">
        <div class="ibox-title">
           <h4>${_res.get('teacher.group.add')} &amp; ${_res.get('Modify')}</h4>
        </div>
        <div class="ibox-content">
            <form action="" method="post" id="studentForm">
					<fieldset>
					    <p>
							<label> 
								地点：
							</label> 
							<input type="text" name="name" id="name" value="" maxlength="20" class="required" maxlength="15" vMin="2" vType="chinaLetterNumber" onblur="onblurVali(this);" />
							<font color="red"> * <span id="real_nameMes"> </span></font>
						</p>
						<p>
							<label> 
								${_res.get('person.in.charge')}:
							</label>
							<select name="people" id="people" class="chosen-select" style="width:186px;" tabindex="2">
								<option value="">--${_res.get('Please.select')}--</option>
								<option value="1">贾宝玉</option>
								<option value="2">林黛玉</option>
								<option value="3">王熙凤</option>
								<option value="4">薛宝钗</option>
							</select>
						</p>
						<p>
							<label>${_res.get('course.class.date')}： </label> <input type="text" name="start" readonly="readonly" value="" id="data1"/>
						</p>
						<p>
							<label> 信息收集员： </label>
							<select data-placeholder="--${_res.get('Please.select')}--（${_res.get('Multiple.choice')}）" class="chosen-select" multiple style="width:340px;" tabindex="4">
							     <option value="">--${_res.get('Please.select')}--</option>
							     <option value="1">紫薇</option>
							     <option value="2">容嬷嬷</option>
							     <option value="3">尔康</option>
							     <option value="4">尔泰</option>
							     <option value="5">小燕子</option>
							     <option value="6">晴儿</option>
							     <option value="7">老佛爷</option>
							     <option value="8">含香</option>
							     <option value="9">肖剑</option>
							     <option value="10">小凳子</option>
							</select>
						</p>
						<input type="button" value="${_res.get('save')}" class="btn btn-outline btn-primary" />
					</fieldset>
				</form>
        </div>
     </div>
     <div style="clear:both;"></div>
   </div>
  </div>
</div>   
<!-- Mainly scripts -->
<script src="/js/js/jquery-2.1.1.min.js"></script>
<script src="/js/js/summernote.js"></script>
<script src="/js/js/summernote-zh-CN.js"></script>

<!-- Chosen -->
<script src="/js/js/plugins/chosen/chosen.jquery.js"></script>
 <script>     
        $(".chosen-select").chosen({disable_search_threshold: 15});
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

 
  <!-- layerDate plugin javascript -->
<script src="/js/js/plugins/layer/laydate/laydate.js"></script>
    <script>
         //开始日期范围限制
        var birthday = {
            elem: '#data1',
            format: 'YYYY-MM-DD',
            max: laydate.now(), //最大日期
            istime: false,
            istoday: false
        };
        laydate(birthday);
        
        //结束日期范围限制
        var birthday = {
            elem: '#data2',
            format: 'YYYY-MM-DD',
            max: laydate.now(), //最大日期
            istime: false,
            istoday: false
        };
        laydate(birthday);
        
        $(document).ready(function () {

            $('.summernote').summernote({
                lang: 'zh-CN'
            });

        });        
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
