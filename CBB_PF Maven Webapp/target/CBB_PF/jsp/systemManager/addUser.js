
function isExistAuthAtStore(dId, userList) {
    for (var i = 0; i < userList.length; i++) {
        if (userList[i].SYS_ROLE_ID == dId) {
            return true;
        }
    }
    return false;
}

var roleStore = new Ext.data.Store({
        url : 'user-management!getRoleList.action',
        reader : new Ext.data.JsonReader({
            totalProperty : 'total',
            root : "rows"
        }, [
                "SYS_ROLE_ID", "NAME"
            ])
    });

roleStore.load({
    callback : function (r, options, success) {
        if (success) {
            if (saveType == 1) {
            	
            	var jsonData = {
            			"sysUserId" : sysUserId
        		};
            	var jsonString = Ext.encode(jsonData);
            	
                Ext.Ajax.request({
                    url : 'user-management!getUserDetailInfo.action',
                    method : 'POST',
                    //async: false,
                    params : {"jsonString":jsonString},
                    success : function (response) {
                        var obj = Ext.decode(response.responseText);
                        var roleDetail = obj.roleInfo;
                        var roleRowCount = roleStore.getCount();
                        if (saveType == 1) {
                            if (roleDetail != null) {
                                for (var j = 0; j < roleRowCount; j++) {
                                    for (var i = 0; i < roleDetail.length; i++) {
                                        if (roleDetail[i].SYS_ROLE_ID == roleStore.getAt(j).get("SYS_ROLE_ID")) {
                                            rolePanel.getSelectionModel().selectRow(j, true);
                                        }
                                    }
                                }
                            }
                        }

                    },
                    error : function (response) {
                        Ext.Msg.alert("错误", response.responseText);
                    },
                    failure : function (response) {
                        Ext.Msg.alert("错误", response.responseText);
                    }
                });
            }

        } else {
            Ext.Msg.alert('错误', '查询失败！请重新查询');
        }
    }
});


var userName = new Ext.form.TextField({
        id : 'userName',
        name : 'userName',
        fieldLabel : '用户名',
        sideText : '<font color=red>*</font>',
//        width : 120,
//        height : 20,
        emptyText : '请输入用户名',
        allowBlank : false,
        maxLength : 10,
        maxLengthText : '姓名最大长度不能超过10个字符!'
    });

var loginName = new Ext.form.TextField({
        id : 'loginName',
        name : 'loginName',
//        bodyStyle : 'padding:10px 50px 100px',
        fieldLabel : '登录名',
        sideText : '<font color=red>*</font>',
        emptyText : '请输入登录名',
//        width : 120,
//        height : 20,
        allowBlank : false,
        maxLength : 10,
        maxLengthText : '登录名最大长度不能超过10个字符!'
    });

//var jobNumber = new Ext.form.TextField({
//        id : 'jobNumber',
//        name : 'jobNumber',
//        fieldLabel : '工号',
//        sideText : '<font color=red>*</font>',
//        emptyText : '请输入工号',
//        allowBlank : false,
////        width : 120,
////        height : 20,
//        maxLength : 20,
//        maxLengthText : '工号最大长度不能超过20个字符!'
//    });

var telephone = new Ext.form.TextField({
        id : 'telephone',
        name : 'telephone',
        fieldLabel : '电话号码',
        sideText : '<font color=red>*</font>',
        emptyText : '请输入电话号码 ',
        allowBlank : false,
        minLength : 4,
        maxLength : 24,
//        width : 120,
//        height : 20,
        regex : /^[\d]{4,24}$/,
        regexText : '请输入正确的手机号码!'
    });

var email = new Ext.form.TextField({
        id : 'email',
        name : 'email',
        fieldLabel : '邮箱',
        //emptyText:'请输入邮箱',
        allowBlank : true,
//        width : 120,
//        height : 20,
        maxLength : 50,
        vtype : "email", //email格式验证
        vtypeText : "不是有效的邮箱地址"//错误提示信息,默认值我就不说了
    });

var note = new Ext.form.TextField({
        id : 'note',
        name : 'note',
//        width:855,
        anchor:'95%',
//        height : 20,
        fieldLabel : '备注',
        emptyText : '请输入备注',
        allowBlank : true,
        maxLength : 200
    });

var checkboxSelectionModel = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        handleMouseDown : Ext.emptyFn
    });

var checkboxSelectionModel1 = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        handleMouseDown : Ext.emptyFn
    });

var roleCM = new Ext.grid.ColumnModel({
        // specify any defaults for each column
        defaults : {
            sortable : true,
            forceFit : false
        },
        columns : [new Ext.grid.RowNumberer({
    		width : 26
    	}), checkboxSelectionModel, {
                id : 'SYS_ROLE_ID',
                header : 'id',
                dataIndex : 'SYS_ROLE_ID',
                hidden : true
            }, {
                id : 'NAME',
                header : '角色名称',
                width : 140,
                dataIndex : 'NAME',
                editor : new Ext.form.TextField({
                    allowBlank : false,
                    maxValue : 40,
                    minValue : 1
                })
            }
        ]
    });

var rolePanel = new Ext.grid.GridPanel({
        id : "rolePanel",
        // region : "center",
        height :  (Ext.getBody().getHeight()-160)*0.8, 
        width : 400,
//        height:250,
        border : true,
        autoScroll : true,
        stripeRows:true,
        frame : false,
        cm : roleCM,
        store : roleStore,
        loadMask : true,
        clicksToEdit : 2, // 设置点击几次才可编辑
        selModel : checkboxSelectionModel, // 必须加不然不能选checkbox
        viewConfig : {
            forceFit : true
        }
    });



var height = window.screen.height*0.6;
var baseDetail = new Ext.FormPanel({
        id : "base",
        labelAlign: 'left',
        frame : false,
//        autoScroll : true,
        border : false,
        width:600,
        height:420,
        bodyStyle : 'padding:20px 20px 0 20px;',
        items :[{
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : .5,
                                layout : "form",
                                labelWidth : 60,
                                border : false,
                                items : [userName,telephone]
                            }, {
                                columnWidth : .5,
                                layout : "form",
                                labelWidth : 60,
                                border : false,
                                items : [loginName,email]
                            }
                        ]
                    }, {
                        layout : "column",
                        bodyStyle : 'padding:3px 0px 0 0px;',
                        border : false,
                        items : [{
                            layout : "form",
                            id:'noteCon',
                            labelWidth : 60,
                            width : 400 ,  
                            border : false,
                            items : note
                        }, {
                            layout : "form",
                            id:'modifyPassCon',
                            width : 200,
                            bodyStyle : 'padding-left:55px;',
                            border : false,
                            items : [{
                                id : 'modifyPass',
                                name : 'modifyPass',  
                                text:'<span style="color:red;">修改密码</span>',
                                xtype:'button',
                                width : 145,
                                height : 20,
                                border : false,
                                handler:modifyPassword,
								// html : '<a href="javascript:;" onclick="modifyPassword();"><span
								// style="color:red;font-size:12px">修改密码</span></a>',
                                hidden : true
                            }]
                        }
                        ]
                    },{
                        layout : {
                            type : 'column',
                            columns : 2
                        },
                        border : false,
                        bodyStyle : 'padding:20px 0 0 65px',
                        items : [{
                                baseCls : 'x-plain',
                                border : true,
                                items : rolePanel
                            }
                        ]
                    }
                ]
    });


var panel = new Ext.Panel({
    region:"center",
    frame : false,
    layout:'form',
    autoScroll : true,
    border : false,
    items:baseDetail,
    buttons : [{
        id : 'ok',
        text : '确定',
        handler : function () {
            saveConfig();
        }
    }, {
        id : 'reset',
        text : '重置',
        handler : function () {
            retConfig();
        }
    }, {
        text : '取消',
        handler : function () {
            // 关闭修改任务信息窗口
            var win = parent.Ext.getCmp('createUserWindow');
            if (win) {
                win.close();
            }
        }
    }
]
});
//修改密码
function modifyPassword() {
    var url = 'modifyPass.jsp';
    var passWindow = new Ext.Window({
            id : 'passWindow',
            title : '修改密码',
            width : 400,
            height : 220,
            isTopContainer : true,
            modal : true,
            autoScroll : true,
            html : '<iframe  id="modifyUserPass_panel" name = "modifyUserPass_panel"  src = ' + url
             + ' height="100%" width="100%" frameBorder=0 border=0/>'
        });
    passWindow.show();

}

function close() {
    var win = parent.Ext.getCmp('createUserWindow');
    if (win) {
        win.close();
    }
}

//重置
function retConfig() {
    if (saveType == 0) {
        Ext.getCmp('base').form.reset();
        rolePanel.getSelectionModel().clearSelections();
    } else if (saveType == 1) {
        if (qjsonData != null && qjsonData != undefined) {
            var userInfo = qjsonData.userInfo;
            Ext.getCmp('userName').setValue(userInfo[0].USER_NAME);
            Ext.getCmp('loginName').setValue(userInfo[0].LOGIN_NAME);
            Ext.getCmp('telephone').setValue(userInfo[0].TELEPHONE);
            Ext.getCmp('email').setValue(userInfo[0].EMAIL);
            Ext.getCmp('note').setValue(userInfo[0].NOTE);

            rolePanel.getSelectionModel().clearSelections();

            //重置权限域
            var roleDetail = qjsonData.roleInfo;
            var roleRowCount = roleStore.getCount();
            for (var j = 0; j < roleRowCount; j++) {
                for (var i = 0; i < roleDetail.length; i++) {
                    if (roleDetail[i].SYS_ROLE_ID == roleStore.getAt(j).get("SYS_ROLE_ID")) {
                        rolePanel.getSelectionModel().selectRow(j, true);
                    }
                }
            }

        }
    }

}

function saveConfig() {
    if (!baseDetail.getForm().isValid()) {
        return;
    }

    var userName = Ext.getCmp("userName").getValue();
    var loginName = Ext.getCmp("loginName").getValue();
    var telephone = Ext.getCmp("telephone").getValue();
    var email = Ext.getCmp("email").getValue();
    var note = Ext.getCmp("note").getValue();
    var roleSelectRecord = rolePanel.getSelectionModel().getSelections();
    var roleArray = new Array();
    for (var i = 0; i < roleSelectRecord.length; i++) {
    	roleArray.push(roleSelectRecord[i].get("SYS_ROLE_ID"));
    }
    
    var jsonData;
    
    if (saveType == 0) {
    		jsonData = {
    			'USER_NAME' : userName,
    	        'LOGIN_NAME' : loginName,
    	        'TELEPHONE' : telephone,
    	        'EMAIL' : email,
    	        'NOTE' : note,
    	        "roleArray" : roleArray
    	    };
    }else if(saveType == 1){
    		jsonData = {
    			"SYS_USER_ID":sysUserId,
    			'USER_NAME' : userName,
    	        'LOGIN_NAME' : loginName,
    	        'TELEPHONE' : telephone,
    	        'EMAIL' : email,
    	        'NOTE' : note,
    	        "roleArray" : roleArray
    	    };
    }
    var jsonString = Ext.encode(jsonData);
    
    Ext.getBody().mask('正在执行，请稍候...');
    Ext.Ajax.request({
        url : 'user-management!modifyUser.action',
        type : 'post',
        params : { // 请求参数
            'jsonString' : jsonString
        },
        success : function (response) {
            Ext.getBody().unmask();
            var obj = Ext.decode(response.responseText);
            if (obj.returnResult == 1) { //新增成功
                parent.store.reload();
                close();
            } else {
                Ext.Msg.alert("错误", obj.returnMessage);
            }
        },
        error : function (response) {
            Ext.getBody().unmask();
            Ext.Msg.alert("错误", "添加失败,请联系管理员!");
        },
        failure : function (response) {
            top.Ext.getBody().unmask();
            Ext.Msg.alert("错误", "添加失败,请联系管理员!");
        }
    });
}

var qjsonData;

function initData() {
    if (saveType == 1) {
        Ext.getCmp('modifyPass').show();
        Ext.getCmp('noteCon').setWidth(400);
    }
    var jsonData = {
        "sysUserId" : sysUserId
    };
	var jsonString = Ext.encode(jsonData);
    var roleArray = new Array();
    var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"加载中..."});
	   myMask.show();
    Ext.Ajax.request({
        url : 'user-management!getUserDetailInfo.action',
        method : 'POST',
        //async: false,
        params : {"jsonString":jsonString},
        success : function (response) {
            var obj = Ext.decode(response.responseText);
            var baseDetail = obj.userInfo;
            qjsonData = obj;
            Ext.getCmp('userName').setValue(baseDetail[0].USER_NAME);
            Ext.getCmp('loginName').setValue(baseDetail[0].LOGIN_NAME);
            Ext.getCmp('telephone').setValue(baseDetail[0].TELEPHONE);
            Ext.getCmp('email').setValue(baseDetail[0].EMAIL);
            Ext.getCmp('note').setValue(baseDetail[0].NOTE);
            if (saveType == 2) {
                Ext.getCmp('ok').hide();
                Ext.getCmp('userName').disable(true);
                Ext.getCmp('loginName').disable(true);
                Ext.getCmp('telephone').disable(true);
                Ext.getCmp('email').disable(true);
                Ext.getCmp('note').disable(true);
            } else {
                Ext.getCmp('loginName').disable(true);
            }
            myMask.hide();
        },
        error : function (response) {
            Ext.Msg.alert("错误", response.responseText);
            myMask.hide();
        },
        failure : function (response) {
            Ext.Msg.alert("错误", response.responseText);
            myMask.hide();
        }
    });
}

Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = "../../resource/ext/resources/images/default/s.gif";
    Ext.Msg = top.Ext.Msg;
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'title';
    if (saveType == 1 || saveType == 2) {
        initData();
    }

    var win = new Ext.Viewport({
            id : 'win',
            loadMask : true,
            layout: 'border',
            items : [panel],
            renderTo : Ext.getBody()
        });
});
