<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head><meta http-equiv="X-UA-Compatible" content="IE=8">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">   
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">   
	<META HTTP-EQUIV="Expires" CONTENT="0">

  <title>跨境电商平台</title>
    <link rel="stylesheet" type="text/css" href="../../resource/ext/resources/css/ext-all-notheme.css" />
    <link rel="stylesheet" type="text/css" href="../../resource/expandExt/css/ux-all.css" />
    <link rel="stylesheet" type="text/css" href="../../resource/css/common.css"/>
    
    <script type="text/javascript" src="../../resource/ext/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../../resource/ext/ext-all.js"></script>
    <script type="text/javascript" src="../../resource/expandExt/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="../../resource/expandExt/ux-all.js"></script>
    <script type="text/javascript" src="../../resource/expandExt/ux-lang-zh_CN.js"></script>
    
    <script type="text/javascript" src="../../resource/expandExt/js/timeComponent/WdatePicker.js"></script>
    
    <!-- Theme includes -->
    <link rel="stylesheet" type="text/css" title="default"	text="天蓝"	href="../../resource/ext/resources/css/xtheme-default.css" />
    <link rel="stylesheet" type="text/css" title="gray"		text="灰白"	href="../../resource/ext/resources/css/xtheme-gray.css" />
    <link rel="stylesheet" type="text/css" title="access"	text="黑灰"	href="../../resource/ext/resources/css/xtheme-access.css" />

    <link rel="stylesheet" type="text/css" title="black"	text="灰黑"	href="../../resource/ext/resources/css/ext-all-xtheme-black.css" />
    <link rel="stylesheet" type="text/css" title="brown"	text="香槟"	href="../../resource/ext/resources/css/ext-all-xtheme-brown.css" />
    <link rel="stylesheet" type="text/css" title="brown02"	text="米黄"	href="../../resource/ext/resources/css/ext-all-xtheme-brown02.css" />
    <link rel="stylesheet" type="text/css" title="green"	text="草绿"	href="../../resource/ext/resources/css/ext-all-xtheme-green.css" />
    <link rel="stylesheet" type="text/css" title="pink"		text="粉红"	href="../../resource/ext/resources/css/ext-all-xtheme-pink.css" />
    <link rel="stylesheet" type="text/css" title="purple"	text="紫藤"	href="../../resource/ext/resources/css/ext-all-xtheme-purple.css" />
    <link rel="stylesheet" type="text/css" title="red03"	text="玫红"	href="../../resource/ext/resources/css/ext-all-xtheme-red03.css" />

    <script type="text/javascript" src="ScriptLoader.js"></script>
    <script type="text/javascript" src="styleswitcher.js"></script>
    <script type="text/javascript" src="main.js"></script>
    <script type="text/javascript">
    
    	userId="<%=session.getAttribute("SYS_USER_ID")%>";
		displayName="<%=session.getAttribute("USER_NAME")%>";
		userName="<%=session.getAttribute("LOGIN_NAME")%>";
    	if(userId == null || userId == "null"){
			window.open('../login/login.jsp', "_parent");
		}
  		//禁用backspace
		window.onload=function(){
			document.getElementsByTagName("body")[0].onkeydown =function(){
			
			//获取事件对象
			var event = window.event || arguments[0];
			
			if(event.keyCode==8){//判断按键为backSpace键
			
					//获取按键按下时光标做指向的element
					var elem = event.srcElement || event.currentTarget; 
					
					//判断是否需要阻止按下键盘的事件默认传递
					var name = elem.nodeName;
					
					if(name!='INPUT' && name!='TEXTAREA'){
						return _stopIt(event);
					}
					var type_e = elem.type.toUpperCase();
					if(name=='INPUT' && (type_e!='TEXT' && type_e!='TEXTAREA' && type_e!='PASSWORD' && type_e!='FILE')){
							return _stopIt(event);
					}
					if(name=='INPUT' && (elem.readOnly==true || elem.disabled ==true)){
							return _stopIt(event);
					}
				}
			}
		}
		function _stopIt(e){
				if(e.returnValue){
					e.returnValue = false ;
				}
				if(e.preventDefault ){
					e.preventDefault();
				}				
	
				return false;
		}

	</script>
</head>
<body id="main" >
<%--
    <div id="west" class="x-hide-display">
        <IFRAME name="west"  frameBorder=0 scrolling=no width="100%" height="100%">
		</IFRAME>
    </div>
    <div id="east" class="x-hide-display">
    	<IFRAME name="east" frameBorder=0 scrolling=no width="100%" height="100%">
		</IFRAME>
    </div>
    <div id="center" class="x-hide-display">
    	<IFRAME name="center" frameBorder=0 scrolling=no width="100%" height="100%">
		</IFRAME>
    </div>
    <div id="south" class="x-hide-display">
        <IFRAME name="south"  frameBorder=0 scrolling=no width="100%" height="100%">
		</IFRAME>
    </div>
    <div id="north" class="x-hide-display">
    	<IFRAME name="north" frameBorder=0 scrolling=no width="100%" height="100%">
		</IFRAME>
    </div>
--%>
</body>
</html>