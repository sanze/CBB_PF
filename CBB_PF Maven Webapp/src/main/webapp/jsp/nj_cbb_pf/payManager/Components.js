Ext.namespace("Ext.ux");
Ext.ux.PayGridPanel = Ext.extend(Ext.grid.GridPanel, {
	stripeRows : true, // 交替行效果
	loadMask : true,
	pageSize : myPageSize,
	readOnly : false,

	showColumns : [ 
//        "PAY_ID",
	     "GUID",
	     "ORDER_NO",
//	     "CUSTOM_CODE",
	     "RECEIVER_ID",
//	     "APP_TYPE",
	     "APP_TIME",
	     "APP_STATUS",
//	     "APP_UID",
	     "EBP_CODE",
	     "PAY_CODE",
	     "PAY_TYPE",
	     "PAY_NO",
	     "PAY_STATUS",
	     "CHARGE",
	     "GOODS_VALUE",
	     "TAX_FEE",
	     "FREIGHT",
	     "CURRENCY",
//	     "DRAWEE",
//	     "DRAWEE_INDENTITY",
	     "NOTE",
	     "RETURN_STATUS",
	     "RETURN_TIME",
	     "RETURN_INFO"/*,
	     "CREAT_TIME",
	     "UPDATE_TIME"*/],
	checkSelect : function(single,preventMask){
		var records=this.getSelectionModel().getSelections();
		var data=false;
		if(Ext.isEmpty(records)||records.length<1){
			if(preventMask!=true)Ext.Msg.alert("提示", '请选择一条记录');
		}else if(single){
			if(records.length>1){
				if(preventMask!=true)Ext.Msg.alert("提示", '只能选择一条记录');
			}else{
				data=records[0];
			}
		}else{
			data=records;
		}
		return data;
	},
	edit : function (title,param){
		title="支付信息";//+title;
		var infoPanel=new Ext.ux.PayPanel(param);
		var tabPage=top.addTabPage(infoPanel,title,"",false);
		infoPanel.on("close",function(){
			top.closeTab(tabPage);
			if(!this.isDestroyed){
				var pageTool = this.pageTool;
				if (pageTool) {
					pageTool.doLoad(pageTool.cursor);
				}
			}
		},this);
	},
	reload: function(){
		var pageTool = this.pageTool;
		if (pageTool) {
			pageTool.doLoad(pageTool.cursor);
		}
	},
	delConfirm : function (record){
		Ext.MessageBox.confirm('提示', '确定删除？', function(button, text) {
			if (button == 'yes') {
				this.del(record);
			}
		}.createDelegate(this));
	},
	del : function (record){
//		if(isOrderReadOnly(record)){
			var param={
				PAY_ID:record.get("PAY_ID"),
				PAY_NO:record.get("PAY_NO"),
				APP_STATUS:record.get("APP_STATUS")
			};
			this.getEl().mask("执行中...");
			Ext.Ajax.request({
				scope: this,
				url : 'n-jcommon!delPay.action',
				method : "POST",
				params : param,
				success : function(response) {
					this.getEl().unmask();
					var obj = Ext.decode(response.responseText);
					if (obj.returnResult == 0) {
						Ext.Msg.alert("信息", obj.returnMessage, this.reload, this);
					}else{
						this.reload();
					}
				},
				error : function(response) {
					this.getEl().unmask();
					Ext.Msg.alert("异常", response.responseText);
				},
				failure : function(response) {
					this.getEl().unmask();
					Ext.Msg.alert("异常", response.responseText);
				}
			});
//		}
	},
/*	batchSubmit : function (record){
//		if(isOrderReadOnly(record)){
			var jsonDataList = new Array();
			for(var i = 0; i< record.length;i++){
				jsonDataList.push(record[i].get('GUID'));
		    }
			var param={
				guidList:jsonDataList
			};
			this.getEl().mask("执行中...");
			Ext.Ajax.request({
				scope: this,
				url : 'n-jcommon!batchSubmit.action',
				method : "POST",
				params : param,
				success : function(response) {
					this.getEl().unmask();
					var obj = Ext.decode(response.responseText);
					if (obj.returnResult == 0) {
						Ext.Msg.alert("信息", obj.returnMessage, this.reload, this);
					}else{
						this.reload();
					}
				},
				error : function(response) {
					this.getEl().unmask();
					Ext.Msg.alert("异常", response.responseText);
				},
				failure : function(response) {
					this.getEl().unmask();
					Ext.Msg.alert("异常", response.responseText);
				}
			});
//		}
	},*/

	initComponent : function () {
		this.sm= new Ext.grid.RowSelectionModel({singleSelect:false});
		var store = new Ext.data.Store(Ext.apply({
			url : 'n-jcommon!getAllPayes.action',
			reader : new Ext.data.JsonReader({
				totalProperty : 'total',
				root : "rows"
			}, [ 
				"PAY_ID",
				"GUID",
				"ORDER_NO",
//				"CUSTOM_CODE",
				"RECEIVER_ID",
				//"APP_TYPE",
				"APP_TIME",
				"APP_STATUS",
				"APP_UID",
				"EBP_CODE",
				"PAY_CODE",
				"PAY_TYPE",
				"PAY_NO",
				"PAY_STATUS",
				"CHARGE",
				"GOODS_VALUE",
				"TAX_FEE",
				"FREIGHT",
				"CURRENCY",
				"DRAWEE",
				"DRAWEE_INDENTITY",
				"NOTE",
				"RETURN_STATUS",
				"RETURN_TIME",
				"RETURN_INFO",
			    "CREAT_TIME",
			    "UPDATE_TIME",
			    "GOODSList"]),
			listeners:{
			  	"exception": function(proxy,type,action,options,response,arg){
			  		Ext.Msg.alert("提示","加载出错"+
						"<BR>Status:"+response.statusText||"unknow");
			  	}
			}
		},this.storeCfg));
		if(Ext.isEmpty(this.columns)){
			this.columns= [ new Ext.grid.RowNumberer({
				width : 26
			}), {
			    id : "PAY_ID",
			    header : "",
			    dataIndex : "PAY_ID",
			    hidden : true,
			    hideable : false
			},{
			    id : "GUID",
			    header : "系统唯一序号",
			    dataIndex : "GUID",
			    hidden : true,
			    width : 320
			},{
			    id : "ORDER_NO",
			    header : "订单编号",
			    dataIndex : "ORDER_NO"
			},
			new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_pay,
			    id : "PAY_CODE",
			    header : "支付企业名称",
			    dataIndex : "PAY_CODE"
			}),
			new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_custom,
			    id : "RECEIVER_ID",
			    header : "接收海关代码",
			    dataIndex : "RECEIVER_ID"
			}),{
			    id : "APP_TYPE",
			    header : "申报类型",
			    dataIndex : "APP_TYPE",
			    renderer: Renderer.APP_TYPE
			},{
			    id : "APP_TIME",
			    header : "申报时间",
			    dataIndex : "APP_TIME",
			    width : 140
			},{
			    id : "APP_STATUS",
			    header : "业务状态",
			    dataIndex : "APP_STATUS",
			    renderer: Renderer.APP_STATUS
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebp,
			    id : "EBP_CODE",
			    header : "电商平台",
			    dataIndex : "EBP_CODE"
			}),
//			new Ext.ux.grid.CodeNameColumn({
//				category: relationCategory_pay,
//			    id : "PAY_CODE",
//			    header : "支付企业名称",
//			    dataIndex : "PAY_CODE"
//			}),
			{
			    id : "PAY_TYPE",
			    header : "支付交易类型",
			    dataIndex : "PAY_TYPE",
			    value:"M"
			},{
			    id : "PAY_NO",
			    header : "支付交易编号",
			    dataIndex : "PAY_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_payStatus,
			    id : "PAY_STATUS",
			    header : "支付状态",
			    dataIndex : "PAY_STATUS"
			}),{
			    id : "CHARGE",
			    header : "支付金额",
			    dataIndex : "CHARGE"
			},{
			    id : "GOODS_VALUE",
			    header : "商品货款",
			    dataIndex : "GOODS_VALUE"
			},{
			    id : "TAX_FEE",
			    header : "行邮税费",
			    dataIndex : "TAX_FEE"
			},{
			    id : "FREIGHT",
			    header : "运输费用",
			    dataIndex : "FREIGHT"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_currency,
			    id : "CURRENCY",
			    header : "币制",
			    dataIndex : "CURRENCY"
			}),{
			    id : "DRAWEE",
			    header : "支付人姓名",
			    dataIndex : "DRAWEE"
			},{
			    id : "DRAWEE_INDENTITY",
			    header : "支付人代码",
			    dataIndex : "DRAWEE_INDENTITY"
			},{
			    id : "NOTE",
			    header : "备注",
			    dataIndex : "NOTE"
			},{
			    id : "RETURN_STATUS",
			    header : "回执状态",
			    dataIndex : "RETURN_STATUS",
			    renderer: Renderer.RETURN_STATUS
			},{
			    id : "RETURN_TIME",
			    header : "回执时间",
			    dataIndex : "RETURN_TIME",
			    width : 140
			},{
			    id : "RETURN_INFO",
			    header : "回执信息",
			    dataIndex : "RETURN_INFO"
			},{
			    id : "CREAT_TIME",
			    header : "",
			    dataIndex : "CREAT_TIME",
			    hidden : true,
			    hideable : false
			},{
			    id : "UPDATE_TIME",
			    header : "",
			    dataIndex : "UPDATE_TIME",
			    hidden : true,
			    hideable : false
			}];
		}
		Ext.each(this.columns,function(item,index,allItems){
			if(item['dataIndex']){
				var idx=findIndexByProperty(this.showColumns,null,item['dataIndex']);
				item['hidden']=idx<0;
				item['hideable']=!item['hidden'];
			}
			if(Ext.isEmpty(item['width'])&&!Ext.isEmpty(item['header'])){
				item['width']=item['header'].length*20;
			}
			function extendRenderer(val,metadata,record,row,col,store,renderer){
				var data=val;
				if(!Ext.isEmpty(renderer)&&Ext.isFunction(renderer)){
					data=renderer(val,metadata,record,row,col,store);
				}
				if(!Ext.isEmpty(data))
					metadata.attr = 'ext:qtip="' +data+'"';   //关键  
				return data;
			}
			item['renderer']=extendRenderer.createDelegate(undefined,[item['renderer']],6);
		},this);
		var cm = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true
			},
			columns : this.columns
		});
		
        if(Ext.isEmpty(this.store)&&Ext.isEmpty(this.ds)){
            this.store = store;
        }
        if(Ext.isEmpty(this.cm)&&Ext.isEmpty(this.colModel)){
            this.colModel = cm;
        }
        /*if(Ext.isEmpty(this.selModel)&&Ext.isEmpty(this.sm)){
            this.selModel = selModel;
        }*/
        
        if(Ext.isEmpty(this.bbar)&&this.mode!=="local"){
			var bbar = new Ext.PagingToolbar({
				pageSize : this.pageSize,// 每页显示的记录值
				store : this.store,
				displayInfo : true,
				displayMsg : '当前 {0} - {1} ，总数 {2}',
				emptyMsg : "没有记录"
			});
			bbar.doRefresh();
			this.pageTool=bbar;
			this.bbar = bbar;
        }
        
        if(Ext.isEmpty(this.tbar)&&this.tbar!=false){
			var tbar = new Ext.Toolbar({items:[{
					text:'新增',
					scale: 'medium',
					icon:'../../resource/images/btnImages/add.png',
					handler: function(b,e){
						var editType='add';
						this.edit('录入',{editType:editType});
					}.createDelegate(this)
				},{
			        text: '编辑',
			        scale: 'medium',
			        icon:'../../resource/images/btnImages/modify.png',
			        handler: function(b,e){
			        	var editType='mod';
			        	var data=this.checkSelect(true);
			        	if(data)
			        		this.edit('编辑',{editType:editType,record:data});
					}.createDelegate(this)
				},{
			        text: '删除',
			        scale: 'medium',
			        name: 'delete',
			        disabled: true,
			        icon:'../../resource/images/btnImages/delete.png',
			        handler: function(){
			        	var data=this.checkSelect(true);
			        	if(data)
			        		this.delConfirm(data);
			        }.createDelegate(this)
				},{
			        text: '查询',
			        scale: 'medium',
			        name: 'search',
			        icon:'../../resource/images/btnImages/search.png',
			        handler: function(){
			        	pay_search(this.store);
					}.createDelegate(this)
				}
/*				{
			        text: '批量提交',
			        scale: 'medium',
			        name: 'batchSubmit',
			        disabled: true,
			        icon:'../../resource/images/btnImages/accept.png',
			        handler: function(){
			        	var data=this.checkSelect(false);
			        	if(data){
			        		this.batchSubmit(data);
			        	}
			        }.createDelegate(this)
				},*/]
			});
			if(this.readOnly){
				tbar.remove(3);
				tbar.remove(2);
				tbar.get(1).setText("支付信息");
				tbar.remove(0);
			}else if(this.mode=="local"){
				tbar.remove(3);
				tbar.get(1).setText("支付信息");
				tbar.remove(0);
			}
			this.tbar = tbar;
        }
        this.on('render', function() {
			//添加第二列查询控件
			//搜索字段包括：进出口业务类型，电商企业，商品货号，业务状态，回执状态，备注
			new Ext.Toolbar({
				id : 'onebar_pay',
//				enableOverflow:true,
				items : [{
			        xtype: 'tbtext',
			        text: '订单编号:',
			        width:80
			    },{
			        xtype: 'textfield',
			        fieldLabel: '',
			        id:"ORDER_NO_PAY_SEARCH",
			        emptyText:"",
			        width:80,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '支付交易编号:',
			        width:95
			    },{
			        xtype: 'textfield',
			        fieldLabel: '',
			        id:"PAY_NO_PAY_SEARCH",
			        emptyText:"",
			        width:80,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '支付企业:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"PAY_CODE_PAY_SEARCH",
			        store: new Ext.data.Store({
			        	url : 'common!getCodeCategory.action',
			        	baseParams : {
			        		"relationCategory" : "PAY"
			        	},
			        	reader : new Ext.data.JsonReader({
			        		totalProperty : 'total',
			        		root : "rows"
			        	}, [ "NAME", "CODE" ])
			        }),
			        displayField:'NAME',
			        valueField: 'CODE',
//			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:95,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '电商平台:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"EBP_CODE_PAY_SEARCH",
			        store: new Ext.data.Store({
			        	url : 'common!getCodeCategory.action',
			        	baseParams : {
			        		"relationCategory" : "EBP_CODE"
			        	},
			        	reader : new Ext.data.JsonReader({
			        		totalProperty : 'total',
			        		root : "rows"
			        	}, [ "NAME", "CODE" ])
			        }),
			        displayField:'NAME',
			        valueField: 'CODE',
//			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:95,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '支付状态:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"PAY_STATUS_PAY_SEARCH",
			        store: new Ext.data.ArrayStore({
			            fields: ['text','value'],
			            data: ComboBoxValue_pay.PAY_STATUS
			        }),
			        displayField:'text',
			        valueField: 'value',
			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:95,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '业务状态:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"APP_STATUS_PAY_SEARCH",
			        store: new Ext.data.ArrayStore({
			            fields: ['text','value'],
			            data: ComboBoxValue_pay.APP_STATUS
			        }),
			        displayField:'text',
			        valueField: 'value',
			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:95,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '回执状态:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"RETURN_STATUS_PAY_SEARCH",
			        store: new Ext.data.ArrayStore({
			            fields: ['text','value'],
			            data: ComboBoxValue_pay.RETURN_STATUS
			        }),
			        displayField:'text',
			        valueField: 'value',
			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,   
			        width:95,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '备注:',
			        width:60
			    },{
			        xtype: 'textfield',
			        fieldLabel: '',
			        id:"NOTE_PAY_SEARCH",
			        emptyText:"",
			        width:80,
			        anchor:'50%'
			    }]
			}).render(this.tbar); // add one tbar
	    },this);
		this.on('destroy', function() {
			if(Ext.getCmp('onebar_pay')){
				Ext.destroy(Ext.getCmp('onebar_pay'));// 这一句不加可能会有麻烦滴
			}
	    },this);
		this.on('rowclick', function(grid, rowIndex, e) {
	        var selections = grid.getSelectionModel().getSelections();
	        if (selections.length == 0) return;
			var button=this.topToolbar.find("name",'delete')[0];
			if(button){
				if(selections.length>1){
					 button.setDisabled(true);
				}else{
		        for ( var i = 0; i < selections.length; i++) {
		            var record = selections[i];
		            button.setDisabled(isPayReadOnly(record));
		        }
			}
			}
/*			button=this.topToolbar.find("name",'batchSubmit')[0];
			if(button){
				var enableFlag = true;
				for ( var i = 0; i < selections.length; i++) {
		            var record = selections[i];
		            if(record.get("APP_STATUS") != 1){
		            	enableFlag = false;
		            	break;
		            }
		        }
				button.setDisabled(!enableFlag);
			}*/
			
	    },this);
		
		Ext.ux.PayGridPanel.superclass.initComponent.call(this);
	}
});

Ext.ux.PayFormPanel = Ext.extend(Ext.form.FormPanel, {
	record : undefined,
	editType : 'add',
	readOnly: undefined,
	
	autoScroll : true,
	defaults : {
		xtype: 'textfield',
        allowBlank : false,
        anchor : "100%",
        maxLength: 250
	},
	bodyStyle:'padding:10px 10px 10px 10px',
	labelWidth: 130,
	monitorValid: true,

	setRecord : function(record){
		this.record = record;
		this.renderRecord();
	},
	renderRecord : function(){
		if(this.record){
			this.getForm().loadRecord(this.record);
		}
	},
	isReadOnly: function(){
		if(this.editType=="add") return false;
		if(this.record){
			return isPayReadOnly(this.record);
		}
		return false;
	},
	submitForm : function(opType){
		var fp=this;
		var form = fp.getForm();
		//表单验证，必须是保存，并且是编辑
		if (form.isValid() || 
			(opType == '1' 
				&& this.editType=="mod" 
				&& Ext.getCmp('PAY_NO').isValid())) {
			var param={
		        editType: this.editType,
		        APP_STATUS: opType
		    };
			//添加displayfield参数或未显示参数
			if(this.editType=="mod"){
				Ext.apply(param,{
			        GUID: this.record.get('GUID'),
	//		        APP_TIME: this.record.get('APP_TIME'),
			        PAY_ID: this.record.get('PAY_ID')
				});
			}
			form.submit({
				scope:fp,
			    clientValidation: false,
			    waitTitle: '正在执行',
			    waitMsg: '请稍后……',
			    url: 'n-jcommon!setPay.action',
			    params: param,
			    success: function(form, action) {
			    	if(!Ext.isEmpty(action.result.msg)){
			    		Ext.Msg.alert('成功', action.result.msg, 
			    		function(){
			    			this.fireEvent('close',this);
			    		}, this);
			    	}else{
			    		this.fireEvent('close',this);
			    	}
			    },
			    failure: function(form, action) {
			        switch (action.failureType) {
			            case Ext.form.Action.CLIENT_INVALID:
			                Ext.Msg.alert('失败', '表单项存在非法值');
			                break;
			            case Ext.form.Action.CONNECT_FAILURE:
			                Ext.Msg.alert('失败', '与服务器通信异常');
			                break;
			            case Ext.form.Action.SERVER_INVALID:
			               Ext.Msg.alert('失败', action.result.msg);
			       }
			    }
			});
		}
	},
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.submited=!(Ext.isEmpty(this.record)||this.record.get("APP_STATUS")==1);
		this.items=[
//		{xtype:'hidden',name:"PAY_ID"},
		{
			xtype:'displayfield',
			name:"GUID",
			fieldLabel:'系统唯一序号',
			hidden: this.editType=="add"
		},{
			id:"ORDER_NO",
	        fieldLabel:'订单编号',
	        name: 'ORDER_NO',
	        readOnly: true,
	        maxLength: 30
		},GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'支付企业名称',
		    	name: 'PAY_CODE'
	        },relationCategory_pay
	    ),GenerateCodeNameComboGrid({
			allowBlank : false,
			fieldLabel:'接收海关代码',
	        name: 'RECEIVER_ID'
	        },relationCategory_custom
	    ),{
			xtype:'displayfield',
	        fieldLabel:'申报时间',
	        name: 'APP_TIME',
	        hidden: !this.submited
	    },GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'用户名称',
		    	name: 'APP_UID'
	        },relationCategory_appUid
	    ),GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'电商平台名称',
		    	name: 'EBP_CODE',
		    	readOnly: false
	        },relationCategory_ebp
	    ),
//	    GenerateCodeNameComboGrid({
//	    		allowBlank : false,
//		    	fieldLabel:'支付企业名称',
//		    	name: 'PAY_CODE',
//		    	readOnly: false
//	        },relationCategory_pay
//	    ),
	    {
	        fieldLabel:'支付交易编号',
	        name: 'PAY_NO',
	        maxLength: 50
		},GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'支付状态',
		    	name: 'PAY_STATUS',
		    	readOnly: false,
		    	autoLoad: false
	        },relationCategory_payStatus,CodeNameData.payStatus
		),
//		{
//	        fieldLabel:'支付金额',
//	        name: 'CHARGE',
//	        maxLength: 50
//		},
		{
	        fieldLabel:'商品货款',
	        name: 'GOODS_VALUE',
	        readOnly: true
		},{
	        fieldLabel:'行邮税费',
	        name: 'TAX_FEE',
	        readOnly: true
		},{
	        fieldLabel:'运输费用',
	        name: 'FREIGHT',
	        readOnly: true
		},{
	        fieldLabel:'币制',
	        name: 'CURRENCY',
	        readOnly: true
		},
//		{
//	        fieldLabel:'支付人姓名',
//	        name: 'DRAWEE',
//	        readOnly: true
//		},{
//	        fieldLabel:'支付人代码',
//	        name: 'DRAWEE_INDENTITY',
//	        readOnly: true
//		},
		{
	        xtype: 'textarea',
	        maxLength: 1000,
	        fieldLabel:'备注',
	        name: 'NOTE'
	    }];

		var columns=[];
		var hiddens=[];
		Ext.apply(this.defaults,{readOnly:this.readOnly});
		Ext.each(this.items,function(item,index,allItems){
			if(!item.hidden){
				if(columns.length==0||columns[columns.length-1].items==undefined||columns[columns.length-1].items.length==2){
					columns.push({
						layout:'column',
						border:false,
						defaults:{columnWidth: .4},
						items:[]
					});
				}
				var bodyStyle;
				if(columns[columns.length-1].items.length==0)
					bodyStyle='padding:0px 10px 0px 0px';
				else
					bodyStyle='padding:0px 0px 0px 10px';
				columns[columns.length-1].items.push({
					layout:'form',
					border:false,
					bodyStyle:bodyStyle,
					defaults: this.defaults,
					items:[item]
				});
			}
		},this);
		Ext.each(this.items,function(item,index,allItems){
			if(item.hidden){
				columns.push(item);
			}
		},this);
		this.defaults=undefined;
		this.items=columns;
		
		var validateButton=function (){
			if(this.buttons==undefined)
				this.buttons=[];
			
			if(!this.readOnly){
				this.buttons.push(
				new Ext.Button({
					disabled: false,
					formBind: false,
			    	text : '保存', 
			    	scale: 'large',
			    	icon : '../../resource/images/btnImages/save.png',
					handler : function(b,e){
						Ext.apply(this.baseParams,{opType:'save'});
						this.submitForm(1);
			    	}.createDelegate(this)
			    }),
			    new Ext.Button({
			    	disabled: true,
			    	formBind: true,
			    	text : '提交', 
			    	scale: 'large',
			    	icon : '../../resource/images/btnImages/accept.png',
					handler : function(b,e){
						Ext.apply(this.baseParams,{opType:'submit'});
						this.submitForm(2);
			    	}.createDelegate(this)
			    }));
			}
			this.buttons.push(new Ext.Button({
		    	text : '取消', 
		    	scale: 'medium',
		    	icon : '../../resource/images/btnImages/cancel.png',
				handler : function(b,e){
					this.fireEvent('close',this);
		    	}.createDelegate(this)
		    }));
		}.createDelegate(this);

		validateButton();
		
		this.addEvents('close');
		this.enableBubble('close');

		Ext.ux.PayFormPanel.superclass.initComponent.call(this);
		this.renderRecord();
	}
});

Ext.ux.PayPanel = Ext.extend(Ext.Panel, {
	editType : 'add',
	record: undefined,
	readOnly: undefined,
	
	layout:'card',
    activeItem: 0, //确保在容器的配置项中设置了当前活动项！
    monitorValid : false,
    monitorPoll : 500,

//    bodyStyle:'padding:10px',
    
    rollPage: function(direction){
    	var layout=this.getLayout();
    	layout.setActiveItem(this.activeItem+direction);
    	this.activeItem=this.items.indexOf(layout.activeItem);
    },
    isReadOnly: function(){
		if(this.editType=="add") return false;
		if(this.record){
			return isPayReadOnly(this.record);
		}
		return false;
	},
	addItem: function(){
		var success=false;
		var itemGridSelect=findByProperty(this.items.getRange(),"name","itemGrid");
		if(itemGridSelect){
			var data=itemGridSelect.checkSelect(true);
			if(data){
				var infoForm=findByProperty(this.items.getRange(),"name","infoForm");
				if(infoForm){
					var copyFields=[
		                "ORDER_NO","APP_UID","EBP_CODE","FREIGHT","CURRENCY","CONSIGNEE_ID","TAX_FEE","GOODS_VALUE"
	                ];
					Ext.each(copyFields,function(item,index,allItems){
						var value=data.get(item);
						var field=infoForm.find("name",item)[0];
						if(field){
							if(item == 'CONSIGNEE_ID'){
								field.setValue(value.toString());
							}else{
								field.setValue(value);
							}
						}
					});
					success=true;
				}
			}
		}
		if(success){
			this.rollPage.createDelegate(this, [-1])();
		}
	},
	// private
	initItem: function(){
		if(this.readOnly){
			this.items=[new Ext.ux.PayFormPanel({
				name:"infoForm",
				readOnly: true,
				editType: this.editType,
				record: this.record
			})];
		}else{
			this.items=[new Ext.ux.PayFormPanel({
				name:"infoForm",
				record: this.record,
				editType: this.editType,
				buttons:[new Ext.Button({
		            text: '所属订单',
//		            icon: '../../resource/images/btnImages/package_add.png',
		            scale: 'medium',
		            handler: this.rollPage.createDelegate(this, [1])
		        })]
			}),new Ext.ux.OrderGridPanel({
				readOnly:true,
				name:"itemGrid",
				showColumns : [
				     "GUID",
				     "ORDER_NO",
				     "APP_UID",
				     "EBP_CODE",
				     "EBC_CODE",
				     "AGENT_CODE",
//				     "GOODS_VALUE",
//				     "FREIGHT",
//				     "CURRENCY",
				     "CONSIGNEE",
//				     "CONSIGNEE_ADDRESS",
//				     "CONSIGNEE_TELEPHONE",
				     "CONSIGNEE_COUNTRY",
				     "NOTE"],
				storeCfg:{
					baseParams:Ext.applyIf({'IN_USE_PAY':false},OrderCompleteParam)
				},
				buttons:[new Ext.Button({
					name: "addInto",
		            text: '确认选择',
//		            icon: '../../resource/images/btnImages/cart_put.png',
		            scale: 'large',
		            handler: this.addItem.createDelegate(this)
		        }),new Ext.Button({
					name: "next",
		            text: '支付信息',
//		            icon: '../../resource/images/btnImages/cart_put.png',
		            scale: 'large',
		            handler: this.rollPage.createDelegate(this, [-1])
		        })]
			})];
		}
	},
	// private
	initEvents : function(){
		Ext.ux.PayPanel.superclass.initEvents.call(this);
		if(this.monitorValid){
			this.startMonitoring();
		}
	},
	bindHandler : function(){
		var addIntoValid=false;
		var nextValid=false;
		var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
		if(itemGrid){
			var itemData=itemGrid.checkSelect(true,true);
			
			var itemValid=itemData!==false;

			addIntoValid=itemValid;
			
			var addInto=findByProperty(itemGrid.buttons,"name","addInto");
			if(addInto){
				addInto.setDisabled(!addIntoValid);
			}
		}
	},
	startMonitoring : function(){
		if(!this.validTask){
			this.validTask = new Ext.util.TaskRunner();
			this.validTask.start({ 
				run : this.bindHandler,
				interval : this.monitorPoll || 200,
				scope: this
			});
		}
	},
	stopMonitoring : function(){
	    if(this.validTask){
			this.validTask.stopAll();
			this.validTask = null;
		}
	},
	// private
	beforeDestroy : function(){
		this.stopMonitoring();
		Ext.ux.PayPanel.superclass.beforeDestroy.call(this);
	},
	// private
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.initItem();
		if(!this.readOnly){
			this.monitorValid=true;
		}
		Ext.ux.PayPanel.superclass.initComponent.call(this);
	}
});
//查询数据
function pay_search(store){

	var param = {"limit":myPageSize,"start":0,"fuzzy":true};
	
	if(Ext.getCmp('ORDER_NO_PAY_SEARCH').getValue()){
		Ext.apply(param,{"ORDER_NO":Ext.getCmp('ORDER_NO_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('PAY_NO_PAY_SEARCH').getValue()){
		Ext.apply(param,{"PAY_NO":Ext.getCmp('PAY_NO_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('PAY_CODE_PAY_SEARCH').getValue()){
		Ext.apply(param,{"PAY_CODE":Ext.getCmp('PAY_CODE_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('EBP_CODE_PAY_SEARCH').getValue()){
		Ext.apply(param,{"EBP_CODE":Ext.getCmp('EBP_CODE_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('PAY_STATUS_PAY_SEARCH').getValue()){
		Ext.apply(param,{"PAY_STATUS":Ext.getCmp('PAY_STATUS_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('APP_STATUS_PAY_SEARCH').getValue()){
		Ext.apply(param,{"APP_STATUS":Ext.getCmp('APP_STATUS_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('RETURN_STATUS_PAY_SEARCH').getValue()){
		Ext.apply(param,{"RETURN_STATUS":Ext.getCmp('RETURN_STATUS_PAY_SEARCH').getValue()});
	}
	if(Ext.getCmp('NOTE_PAY_SEARCH').getValue()){
		Ext.apply(param,{"NOTE":Ext.getCmp('NOTE_PAY_SEARCH').getValue()});
	}

	store.baseParams = param;

	store.load();
}
