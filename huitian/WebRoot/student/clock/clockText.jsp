<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta name="GENERATOR" content="Microsoft FrontPage 6.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>USB ID 卡读卡器控件调用例程 V1.0</title>
<script id=clientEventHandlersVBS language=javascript>
<!--
var isrun;
var strls = "";



function readcard()
{

		strls =  IDCardReader.getcardid();
		if(strls != "")
		{
			CardIDShower.value = CardIDShower.value + strls + "\r\n";
		}

}

function readcardonce()
{

		strls =  IDCardReader.getcardidonce();
		if(strls != "")
		{
			CardIDShower.value = CardIDShower.value + strls + "\r\n";
		}

}



function readdevicenumber()
{

	strls=IDCardReader.pcdgetdevicenumber();
	if(strls != "")
        {
		CardIDShower.value = CardIDShower.value + "设备硬件号为:" + strls + "\r\n";
	}
}


function beep()
{
	
	IDCardReader.pcdbeep (100);//100表示响100毫秒
	 
}



function clears1()
{
	CardIDShower.value = "";
}

-->
</script>
</head>
<body>
<OBJECT CLASSID="clsid:E64532A5-6F77-4967-9774-3D2141854991" id="IDCardReader" VIEWASTEXT width="0" height="0">
</OBJECT>



</body>
<body>
<table border="0" style="border-collapse: collapse" width="549" height="307">
	<tr>
		<td width="372" rowspan="5">
<TEXTAREA rows=21 cols=48 ID=CardIDShower name="S1">

</TEXTAREA></td>
	</tr>

	<tr>
		<td width="173">
<INPUT type="button" value="读卡" onclick="javascript:readcard()"></td>
	</tr>

	<tr>
		<td width="173">
<INPUT type="button" value="读一次卡，想再读必须拿开卡再放上去" onclick="javascript:readcardonce()"></td>
	</tr>


	<tr>
		<td width="173">
<INPUT type="button" value="读出设备硬件号(全球唯一编号)" onclick="javascript:readdevicenumber()"></td>
	</tr>

	<tr>
		<td width="173">
<INPUT type="button" value="读卡器“嘀”一声" onclick="javascript:beep()"></td>
	</tr>


<tr>
<td width="173">　
<INPUT type="button" value="清空记录" onclick="javascript:clears1()"></td>
	</tr>

	<tr>
		<td width="173">　</td>
	</tr>
</table>

<p><font style="font-size: 9pt">提示：</font></p>
<p><font style="font-size: 9pt">&nbsp;&nbsp;&nbsp; 1、运行本例程前，需先注册 IDCardReader 
控件，注册方法为：“开始”-&gt;“运行”-&gt;输入“regsvr32 ***\IDCardReader.dll”-&gt;“确定”。其中“***”为控件所在路径。</font></p>
<p>
        <font style="font-size: 9pt">&nbsp;&nbsp;&nbsp; 2、在IE的Internet 属性设定，让浏览器允许运行 ActiveX 控件。</font></p>
    <p>
        <font style="font-size: 9pt">&nbsp;&nbsp;&nbsp; 3、如果是在服务器端运行本网页，还需在IE的Internet 属性中设定，将服务器网址设为可信站点，否则网页无权运行本地控件。</font></p>

</body>

</html>

