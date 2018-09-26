Ext.Msg = top.Ext.Msg;
Ext.BLANK_IMAGE_URL = "data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
ScriptMgr = new ScriptLoaderMgr();

// 图片panel
var imagePanel = new Ext.Panel({
	id : 'imagePanel',
	height : 80,
	layout : 'border',
	items:[{
		region: 'center',
		html : '<img src="../../resource/images/otherImages/head.png" height=100% width=100%/>',
	},{
		region: 'east',
		width: 60,
		split: true,
		collapseMode: 'mini',
		collapsed: true,
		layout:'fit',
		items: [getStyleSheetListView()]
	}]
});

// 工具栏panel
var toolbarPanel = new Ext.Toolbar({
	id : "toolbarPanel",
	defaults:{
		scale:'medium'
	},
	bodyStyle : 'border-style:solid;border-color:#4F94CD'
});

// northPanel
var northPanel = new Ext.Panel({
	region : 'north',
	id : 'northPanel',
//	height : 108,
	autoHeight: true,
	// margins: '0 3 0 0',
	// width:500,
//	items : [imagePanel, toolbarPanel],
	items : [imagePanel,toolbarPanel],
	split : false
});

// centerPanel
var centerPanel = new Ext.TabPanel({
	id : 'centerPanel',
	region : "center",
	// contentEl:'center',
	// frame:false,
	// collapsible: false,
	// border:false,
	// margins: '0 0 0 0',
	enableTabScroll : true,
	activeTab : 0,
	deferredRender : false,
	defaults : {
		autoScroll : true
	},
	plugins : [new Ext.ux.TabCloseMenu(),new Ext.ux.TabScrollerMenu()],
	bodyStyle : 'border-style:solid;border-color:#4F94CD',
	items : []
});

// eastPanel 暂不使用
var eastPanel = new Ext.Panel({
	id : 'eastPanel',
	region : "east",
	width : 230,
	collapsible : true,
	collapseMode : 'mini',
	contentEl : 'east',
	frame : false,
	animCollapse : true,
	collapsed : true,
	// margins: '0 0 0 5',
	split : false,
	bodyStyle : 'border-style:solid;border-color:#4F94CD'
});

// southPanel
var southPanel = new Ext.Panel({
	id : 'southPanel',
	region : "south",
	frame : true,
	split : false,
	// border:false,
	autoHeight: true,
	margins : '0 0 0 0',
	bodyStyle : 'background-color:#eee;border-style:solid',
	html : '<p><center>版权所有：人人股份有限公司</center></p>',
	collapsible : false
});

// westPanel 暂不使用
var westPanel = new Ext.Panel({
	region : 'west',
	title:"导航",
//	contentEl : 'west',
	collapsible : true,
	collapseMode : 'mini',
	width : 200,
	frame : false,
	animCollapse : true,
	// collapsed:true,
	// margins: '0 0 0 5',
	split : false,
	bodyStyle : 'border-style:solid;border-color:#4F94CD',
//	html : '<iframe id= \'' + iframeId + '\' src=\'' + url
//		+ '\' frameborder="0" width="100%" height="100%"/>'
		
	html : '<iframe src= "westPanel.jsp" frameborder="0" width="100%" height="100%"/>'
});

Ext.onReady(function() {
	Ext.Ajax.timeout=120000;
	Ext.QuickTips.init();
	
	//初始化菜单
	initMenu();
});

// 添加tab页
window.addTabPage = function(url, title,authSequence,disableClose) {
	//判断是否含有disableClose这个参数，如果有不关闭，没有关闭
	var closable = true;
	if(disableClose){
		closable = false;
	}
	
	var panel={// 动态添加tab页
		id : title,
		visible : true,
		closable : closable,
		title : title
	};
	if(Ext.isString(url)){
		function endsWidth(str,surfix){
			if(str.length>=surfix.length){
				return str.substring(str.length-surfix.length,str.length)==surfix;
			}
			return false;
		}
		if(endsWidth(url,".menu")){
			Ext.Ajax.request({
		        url: url,
		        success: function(response){
		        	var menu=Ext.decode(response.responseText);
		        	ScriptMgr.load({ 
		        		scripts: menu.scripts, 
		        		callback: function(){
		        			addTabPage(menu.construct(),title,authSequence,disableClose);
		        		}
		        	});
		        },
//		        failure: function(){},
		        scope: this,
//		        timeout: (this.timeout*1000),
//		        disableCaching: this.disableCaching,
		        argument: {
//		          'url': url,
//		          'scope': callerScope || window,
//		          'callback': callback,
//		          'options': cfg
		        }
		    });
		}else{
			Ext.apply(panel,{
				html : '<iframe id= \'' + iframeId + '\' src=\'' + url
				+ '\' frameborder="0" width="100%" height="100%"/>'
			});
		}
	}else{
		Ext.apply(panel,{
			layout: 'fit',
			items : [url]
		});
	}
	var iframeId = "f_" + title;
	centerPanel.remove(title);
	
	var tabPage = centerPanel.add(panel);
	centerPanel.setActiveTab(tabPage);// 设置当前tab页
//	centerPanel.doLayout();
	(function(){
		centerPanel.doLayout();
	}).defer(2000);
	return tabPage;
};
// 激活tab页
window.setActiveTab = function(tabId) {
	centerPanel.setActiveTab(tabId);
};
// 关闭tab页
window.closeTab = function(tabId) {
	centerPanel.remove(tabId);
};

// 取得当前活动页的tabId
window.getCurrentTabId = function() {
	return centerPanel.getActiveTab().id;
};

// 取得指定tab页
window.getTab = function(tabId) {
	return centerPanel.getItem(tabId);
};

function home() {
	addTabPage("../main/centerPanel.jsp", "首页");
};

function notSupport() {
	Ext.Msg.alert('提示', "此功能暂不支持！");
};

//function logout() {
//	Ext.Msg.confirm("确认", "确认要注销吗？", function(r) {
//				if (r == "yes") {
//					Ext.Ajax.request({
//								url : 'login!logout.action',
//								method : 'POST',
//								success : function(response) {
//									window
//											.open('../login/login.jsp',
//													"_parent");
//								},
//								failure : function(response) {
//									window
//											.open('../login/login.jsp',
//													"_parent");
//								}
//							});
//				};
//			});
//};

var hideBtn = new Ext.Button({
	text : '隐藏',
    enableToggle:true,
	icon : '../../resource/images/btnImages/up.png',
	handler : function() {
		if (!imagePanel.hidden) {
			imagePanel.hide();
			northPanel.setHeight(28);
            southPanel.hide();
			win.doLayout();
            hideBtn.setText("显示");
            hideBtn.setIcon('../../resource/images/btnImages/down.png');
		} else {
			imagePanel.show();
            southPanel.show();
			northPanel.setHeight(108);
			win.doLayout();
            hideBtn.setText("隐藏");
            hideBtn.setIcon('../../resource/images/btnImages/up.png');
		}
	}
});

//加载第一级目录
function initMenu() {
	getSubMenu(0,function(){
		showMainWin();
		// 添加登录，退出信息
		toolbarPanel.add('->', 
			{
                id:"uidField",
				xtype : 'displayfield',
				value : "当前登录：" + displayName
			}, {
				xtype : 'tbspacer',
				width : 10
			}, 
			{
				xtype : 'tbspacer',
				width : 10
			}, {
				xtype : 'displayfield',
				html : "<a href='javascript:logout()'>" + "退出"
						+ "</a>"
			}, {
				xtype : 'tbspacer',
				width : 5
			},
			hideBtn);

		// 强制重新布局toolbar
		toolbarPanel.doLayout();
	});
}


function addMenuList(items,callback){
	var completed=0;
	function toReturn(){
		if(completed==items.length){
			if(typeof callback == 'function')
				callback.call();
		}
	}
	toReturn();
	Ext.each(items,
		function(item,index,allItems){
			addMenu(item,function(){
				completed++;
				toReturn();
			});
		}
	);
}
function addMenu(item,callback){
	// 找到需要添加节点的父节点添加菜单组件
	var pmenu=toolbarPanel;
	if(item.MENU_PARENT_ID&&item.MENU_PARENT_ID!=0){
		if(Ext.isEmpty(Ext.getCmp(item.MENU_PARENT_ID)))
			return;
		pmenu=Ext.getCmp(item.MENU_PARENT_ID).menu;
	}
	// 如果是叶子节点，不用添加menu属性
    var isSeparator=item.MENU_DISPLAY_NAME=="-";
    var isLeaf=item.IS_LEAF == 0?false:true;
    if(isSeparator){
    	pmenu.add("-");
    }else{
    	var menuItem={
			id : item.SYS_MENU_ID,
			text : '<font style="font-weight:bold">'+item.MENU_DISPLAY_NAME+'</font>',
			MENU_DISPLAY_NAME: item.MENU_DISPLAY_NAME,
			href : item.MENU_HREF,
			iconCls : item.ICON_CLASS,
			disabled : isLeaf?item.DISABLED:false,
			menu : isLeaf?undefined:{
				items : []
			},
			listeners : {
				'click' : function(item, e) {
					e.stopEvent();
					//if (item.getEl()) {
					var href = item.initialConfig["href"];
					if(href){
						if(href.indexOf("f:")>=0){
							var fn = href.substring(2);
							invoker(fn);
						}/*else if(href.indexOf("..")>=0){
							addTabPage(
								href,
								item.text,
								authSequence);
						}*/else{
							var tabTitle=null;
							var menuItem=item;
							while(menuItem){
								if(tabTitle){
									tabTitle=menuItem.MENU_DISPLAY_NAME+"/"+tabTitle;
								}else{
									tabTitle=menuItem.MENU_DISPLAY_NAME;
								}
								if(menuItem.parentMenu)
									menuItem=menuItem.parentMenu.ownerCt;
								else
									menuItem=null;
							}
							addTabPage(
								href,
								tabTitle);
						}
					}
					//}
				}
			}
		};
		pmenu.addItem(menuItem);
	}
    if(!isSeparator&&!isLeaf){
		getSubMenu(item.SYS_MENU_ID,callback);
	}else{
		if (typeof callback == 'function') {
	        callback.call();
        }
	}
}
// 加载子菜单方法
function getSubMenu(parentMenuId,callback) {
	var path = 'menu!getSubMenuList.action?parentMenuId='
			+ parentMenuId;
	Ext.Ajax.request({
		url : path,
		method : 'POST',
		success : function(response) {
			var responseArray = Ext.util.JSON.decode(response.responseText);
			// 移除父节点组件
			var pmenu=toolbarPanel;
			if(parentMenuId!=0){
				if(Ext.isEmpty(Ext.getCmp(parentMenuId)))
					return;
				pmenu=Ext.getCmp(parentMenuId).menu;
			}
			pmenu.removeAll();
			addMenuList(responseArray,callback);
		}
	});
}
function showMainWin(){
	win = new Ext.Viewport({
		id : "viewport",
		loadMask : true,
		autoScroll: true,
		minWidth:800,
		minHeight:600,
		items : [{layout : 'border',items:[northPanel, centerPanel]}]
	});
	//处理最小宽高限制
	win.on('resize',function(obj,adjWidth,adjHeight,rawWidth,rawHeight){
		var resizeHeight=adjHeight>=obj.minHeight?adjHeight:obj.minHeight;
		obj.items.itemAt(0).setHeight(resizeHeight);
		var resizeWidth=adjWidth>=obj.minWidth?adjWidth:obj.minWidth;
		obj.items.itemAt(0).setWidth(resizeWidth);
	});
	win.fireEvent('resize',win,win.getWidth(),win.getHeight(),win.getWidth(),win.getHeight());
	addTabPage("../main/home.jsp", "首页","",false);
}

function invoker(fnName){
	eval("(" + fnName + "())");
}

function logout() {
	Ext.Msg.confirm("确认", "确认要注销吗？", function(r) {
				if (r == "yes") {
					Ext.Ajax.request({
								url : 'login!logout.action',
								method : 'POST',
								success : function(response) {
									window
											.open('../login/login.jsp',
													"_parent");
								},
								failure : function(response) {
									window
											.open('../login/login.jsp',
													"_parent");
								}
							});
				};
			});
};
