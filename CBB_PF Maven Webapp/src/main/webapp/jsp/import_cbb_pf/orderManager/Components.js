Ext.namespace("Ext.ux");
Ext.ux.OrderGridPanel = Ext.extend(Ext.grid.GridPanel, {
	stripeRows : true, // 交替行效果
	loadMask : true,
	pageSize : myPageSize,
	readOnly : false,
	storeCfg: {},
	showColumns : [
//					"ORDERS_ID",
	                 "GUID",
	                 "CUSTOM_CODE",
				     "RECEIVER_ID",
				     "ORDER_NO",
				     "ORDER_TYPE",
//				     "APP_TYPE",
				     "APP_TIME",
				     "APP_STATUS",
//				     "APP_UID",
				     "EBP_CODE",
				     "EBC_CODE",     
//				     "AGENT_CODE",
//				     "GOODS_VALUE",
//				     "FREIGHT",
//				     "CONSIGNEE",
//				     "CONSIGNEE_ADDRESS",
//				     "CONSIGNEE_TELEPHONE",
//				     "CONSIGNEE_COUNTRY",
				     "LOGISTICS_NO",
//				     "NOTE",
				     "RETURN_STATUS",
				     "RETURN_TIME",
				     "RETURN_INFO"/*,
				     "CREAT_TIME",
				     "UPDATE_TIME"*/
	],
	checkSelect: function(single,preventMask){
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
	edit: function (title,param){
		title="订单信息";//+title;
		if(this.readOnly||this.mode=='local'){
			Ext.apply(param,{readOnly:true});
		}
		var infoPanel=new Ext.ux.OrderPanel(param);
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
				GUID:record.get("GUID"),
				ORDERS_ID:record.get("ORDERS_ID"),
				ORDER_NO:record.get("ORDER_NO"),
				APP_STATUS:record.get("APP_STATUS")
			};
			this.getEl().mask("执行中...");
			Ext.Ajax.request({
				scope: this,
				url : 'import-common!delOrder.action',
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
	batchSubmit : function (record){
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
				url : 'n-jcommon!batchSubmit_order.action',
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
	
	
	initComponent : function () {
		this.sm= new Ext.grid.RowSelectionModel({singleSelect:false});
		var store = new Ext.data.Store(Ext.apply({
			url : 'import-common!getAllOrders.action',
			reader : new Ext.data.JsonReader({
				totalProperty : 'total',
				root : "rows"
			}, [ "ORDERS_ID",
			     "GUID",
			     "CUSTOM_CODE",
			     "RECEIVER_ID",
			     "ORDER_NO",
			     "ORDER_TYPE",
			     "APP_TYPE",
			     "APP_TIME",
			     "APP_STATUS",
			     "APP_UID",
			     "EBP_CODE",
			     "EBC_CODE",
			     "AGENT_CODE",
			     "GOODS_VALUE",
			     "TAX_FEE",
			     "FREIGHT",
			     "CURRENCY",
			     "CONSIGNEE_ID",
//			     "CONSIGNEE",
//			     "CONSIGNEE_CODE",
//			     "CONSIGNEE_ADDRESS",
//			     "CONSIGNEE_TELEPHONE",
//			     "CONSIGNEE_COUNTRY",
			     "UNDER_THE_SINGER_ID",
//			     "UNDER_THE_SINGER_NAME",
//			     "UNDER_THE_SINGER_CODE",
//			     "UNDER_THE_SINGER_ADDRESS",
//			     "UNDER_THE_SINGER_TELEPHONE",
//			     "UNDER_THE_SINGER_COUNTRY",
			     "LOGISTICS_CODE",
			     "LOGISTICS_NO",
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
			    id : "ORDER_ID",
			    header : "",
			    dataIndex : "ORDER_ID",
			    hidden : true,
			    hideable : false
			},{
			    id : "GUID",
			    header : "系统唯一序号",
			    dataIndex : "GUID",
			    width : 320
			},
//			new Ext.ux.grid.CodeNameColumn({
//				category: relationCategory_custom,
//			    id : "CUSTOM_CODE",
//			    header : "申报海关代码",
//			    dataIndex : "CUSTOM_CODE"
//			}),
//			new Ext.ux.grid.CodeNameColumn({
//				category: relationCategory_custom,
//			    id : "RECEIVER_ID",
//			    header : "接收海关代码",
//			    dataIndex : "RECEIVER_ID"
//			}),
			{
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
			},{
			    id : "ORDER_NO",
			    header : "订单编号",
			    dataIndex : "ORDER_NO"
			},{
			    id : "ORDER_TYPE",
			    header : "订单类型",
			    dataIndex : "ORDER_TYPE",
			    renderer: Renderer.BIZ_TYPE
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_appUid,
			    id : "APP_UID",
			    header : "用户",
			    dataIndex : "APP_UID"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebp,
			    id : "EBP_CODE",
			    header : "电商平台",
			    dataIndex : "EBP_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebc,
			    id : "EBC_CODE",
			    header : "电商企业",
			    dataIndex : "EBC_CODE"
			}),
//			new Ext.ux.grid.CodeNameColumn({
//				category: relationCategory_agent,
//			    id : "AGENT_CODE",
//			    header : "申报企业",
//			    dataIndex : "AGENT_CODE"
//			}),
			new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_currency,
			    id : "CURRENCY",
			    header : "币制",
			    dataIndex : "CURRENCY"
			}),{
			    id : "GOODS_VALUE",
			    header : "订单商品货款",
			    dataIndex : "GOODS_VALUE"
			},{
			    id : "FREIGHT",
			    header : "订单商品运费",
			    dataIndex : "FREIGHT"
			},{
			    id : "TAX_FEE",
			    header : "综合税费",
			    dataIndex : "TAX_FEE"
			}
			,{
			    id : "CONSIGNEE_ID",
			    header : "收货人名称",
			    dataIndex : "CONSIGNEE_ID"
			},
//			{
//			    id : "CONSIGNEE_ADDRESS",
//			    header : "收货人地址",
//			    dataIndex : "CONSIGNEE_ADDRESS"
//			},{
//			    id : "CONSIGNEE_TELEPHONE",
//			    header : "收货人电话",
//			    dataIndex : "CONSIGNEE_TELEPHONE"
//			},new Ext.ux.grid.CodeNameColumn({
//				category: relationCategory_country,
//				id : "CONSIGNEE_COUNTRY",
//			    header : "收货人所在国",
//			    dataIndex : "CONSIGNEE_COUNTRY"
//			}),
			new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_logistics,
			    id : "LOGISTICS_CODE",
			    header : "物流企业",
			    dataIndex : "LOGISTICS_CODE"
			}),{
			    id : "LOGISTICS_NO",
			    header : "物流运单编号",
			    dataIndex : "LOGISTICS_NO"
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
        	var tbar = new Ext.Toolbar({items:[/*{
			        text: '查询',
			        icon:'../../resource/images/btnImages/search.png',
			        handler: function(){
		
					}.createDelegate(this)
				},'-',*/
        	    {
					text:'新增',
					scale: 'medium',
					icon:'../../resource/images/btnImages/add.png',
					handler: function(b,e){
						var editType='add';
						this.edit('录入',{editType:editType});
					}.createDelegate(this)
				},
				{
			        text: '编辑',
			        scale: 'medium',
			        icon:'../../resource/images/btnImages/modify.png',
			        handler: function(b,e){
			        	var editType='mod';
			        	var data=this.checkSelect(true);
			        	if(data)
			        		this.edit('查看',{editType:editType,record:data});
					}.createDelegate(this)
				},
//				{
//			        text: '删除',
//			        scale: 'medium',
//			        name: 'delete',
//			        disabled: true,
//			        icon:'../../resource/images/btnImages/delete.png',
//			        handler: function(){
//			        	var data=this.checkSelect(true);
//			        	if(data)
//			        		this.delConfirm(data);
//			        }.createDelegate(this)
//				},{
//			        text: '批量提交',
//			        scale: 'medium',
//			        name: 'batchSubmit',
//			        disabled: true,
//			        icon:'../../resource/images/btnImages/accept.png',
//			        handler: function(){
//			        	var data=this.checkSelect(false);
//			        	if(data){
//			        		this.batchSubmit(data);
//			        	}
//			        }.createDelegate(this)
//				},
				{
			        text: '查询',
			        scale: 'medium',
			        name: 'search',
			        icon:'../../resource/images/btnImages/search.png',
			        handler: function(){
			        	order_search(this.store);
					}.createDelegate(this)
				}]
			});
        	if(this.readOnly){
				tbar.remove(3);
				tbar.remove(2);
				tbar.get(0).setText("订单信息");
				tbar.remove(0);
			}else if(this.mode=="local"){
				tbar.remove(3);
				tbar.get(0).setText("订单信息");
				tbar.remove(0);
			}
            this.tbar = tbar;
        }
        this.on('render', function() {
			//添加第二列查询控件
			//搜索字段包括：进出口业务类型，电商企业，商品货号，业务状态，回执状态，备注
			var onebar_order = new Ext.Toolbar({
				id : 'onebar_order',
//				enableOverflow:true,
				items : [{
			        xtype: 'tbtext',
			        text: '订单类型:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"ORDER_TYPE_ORDER_SEARCH",
			        store: new Ext.data.ArrayStore({
			            fields: ['text','value'],
			            data: ComboBoxValue_order.ORDER_TYPE
			        }),
			        displayField:'text',
			        valueField: 'value',
			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:100,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '电商企业:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"EBC_CODE_ORDER_SEARCH",
			        store: new Ext.data.Store({
			        	url : 'common!getCodeCategory.action',
			        	baseParams : {
			        		"relationCategory" : "EBC_CODE"
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
			        width:120,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '订单编号:',
			        width:95
			    },{
			        xtype: 'textfield',
			        fieldLabel: '',
			        id:"ORDER_NO_ORDER_SEARCH",
			        emptyText:"",
			        width:100,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '物流运单编号:',
			        width:95
			    },{
			        xtype: 'textfield',
			        fieldLabel: '',
			        id:"LOGISTICS_NO_ORDER_SEARCH",
			        emptyText:"",
			        width:100,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '业务状态:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"APP_STATUS_ORDER_SEARCH",
			        store: new Ext.data.ArrayStore({
			            fields: ['text','value'],
			            data: ComboBoxValue_order.APP_STATUS
			        }),
			        displayField:'text',
			        valueField: 'value',
			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:100,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '回执状态:',
			        width:80
			    },{
			        xtype: 'combo',
			        fieldLabel: '',
			        id:"RETURN_STATUS_ORDER_SEARCH",
			        store: new Ext.data.ArrayStore({
			            fields: ['text','value'],
			            data: ComboBoxValue_order.RETURN_STATUS
			        }),
			        displayField:'text',
			        valueField: 'value',
			        mode: 'local',
//			        forceSelection: true,
			        triggerAction: 'all',
//			        selectOnFocus:true,
			        width:100,
			        anchor:'50%'
			    },{
			        xtype: 'tbtext',
			        text: '备注:',
			        width:60
			    },{
			        xtype: 'textfield',
			        fieldLabel: '',
			        id:"NOTE_ORDER_SEARCH",
			        emptyText:"",
			        width:100,
			        anchor:'50%'
			    }]
			});
			if(this.readOnly){
				
			}else if(this.mode=="local"){
				
			}else{
				onebar_order.render(this.tbar); // add one tbar
			}
	    },this);
		this.on('destroy', function() {
			if(Ext.getCmp('onebar_order')){
				Ext.destroy(Ext.getCmp('onebar_order'));// 这一句不加可能会有麻烦滴
			}
	    },this);
		this.on('rowclick', function(grid, rowIndex, e) {
			var delButton=this.topToolbar.find("name",'delete')[0];
			if(delButton){
		        var selections = grid.getSelectionModel().getSelections();
		        if (selections.length == 0) return;
		 
		        for ( var i = 0; i < selections.length; i++) {
		            var record = selections[i];
		            delButton.setDisabled(isOrderReadOnly(record));
		        }
			}
			
			button=this.topToolbar.find("name",'batchSubmit')[0];
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
			}
			
	    },this);
		
		Ext.ux.OrderGridPanel.superclass.initComponent.call(this);
	}
});

Ext.ux.OrderFormPanel = Ext.extend(Ext.form.FormPanel, {
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
//			var goodsGrid=findByProperty(this.items.getRange(),"name","itemGrid");
//			var goodsList=this.record.get("GOODSList");
//			var store=goodsGrid.getStore();
//			store.loadData.defer(1000,store,[{total:goodsList.length,rows:goodsList}]);
		}
	},
	isReadOnly: function(){
		if(this.editType=="add") return false;
		if(this.record){
			return isOrderReadOnly(this.record);
		}
		return false;
	},
	submitForm : function(opType){
		var fp=this;
		var form = fp.getForm();
		//表单验证，必须是保存，并且是编辑
		if (form.isValid()|| 
				(opType == '1' 
					&& this.editType=="mod")) {
//			var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
			var itemRecords=[];
//			itemGrid.getStore().each(function(record){
//				itemRecords.push(Ext.encode(record.data));
//			});
			var param={
		        editType: this.editType,
		        APP_STATUS: opType,
		        //GOODSList: itemRecords
		    };
			//添加displayfield参数或未显示参数
			if(this.editType=="mod"){
				Ext.apply(param,{
			        GUID: this.record.get('GUID'),
	//		        APP_TIME: this.record.get('APP_TIME'),
			        ORDERS_ID: this.record.get('ORDERS_ID')
				});
			}
			form.submit({
				scope:fp,
			    clientValidation: false,
			    waitTitle: '正在执行',
			    waitMsg: '请稍后……',
			    url: 'import-common!setOrder.action',
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
	bindHandler : function(){
		var valid = true;
//		var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
//		if(itemGrid){
//			valid=itemGrid.getStore().getCount()>0;
//		}
		if(valid){
			Ext.ux.OrderFormPanel.superclass.bindHandler.call(this);
		}else{
			if(this.fbar){
				var fitems = this.fbar.items.items;
				for(var i = 0, len = fitems.length; i < len; i++){
					var btn = fitems[i];
					if(btn.formBind === true && btn.disabled === valid){
						btn.setDisabled(!valid);
					}
				}
			}
			this.fireEvent('clientvalidation', this, valid);
		}
		
	},
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.submited=!(Ext.isEmpty(this.record)||this.record.get("APP_STATUS")==1);
		this.items=[
//		{xtype:'hidden',name:"ORDERS_ID"},
		{
			xtype:'displayfield',
			name:"GUID",
			fieldLabel:'系统唯一序号',
			hidden: this.editType=="add"
		}
//		,GenerateCodeNameComboGrid({
//			allowBlank : false,
//			fieldLabel:'申报海关代码',
//	        name: 'CUSTOM_CODE'/*,
//	        textField: 'CODE',
//	        columns : [ {
//	            header : '编号',
//	            dataIndex : 'CODE'
//	        }, {
//	            header : '名称',
//	            dataIndex : 'NAME',
//	            hidden: true
//	        } ]*/
//	        },relationCategory_custom
//	    )
//	    ,GenerateCodeNameComboGrid({
//			allowBlank : false,
//			fieldLabel:'接收海关代码',
//	        name: 'RECEIVER_ID'/*,
//	        textField: 'CODE',
//	        columns : [ {
//	            header : '编号',
//	            dataIndex : 'CODE'
//	        }, {
//	            header : '名称',
//	            dataIndex : 'NAME',
//	            hidden: true
//	        } ]*/
//	        },relationCategory_custom
//	    )
	    ,{
	        fieldLabel:'订单编号',
	        name: 'ORDER_NO',
//	        readOnly: this.editType!=="add",
	        maxLength: 33
		},GenerateCodeNameComboGrid({
				id:"ORDER_TYPE",
	    		allowBlank : false,
		    	fieldLabel:'订单类型',
		    	name: 'ORDER_TYPE',
		    	autoLoad: false,
		    	listeners : {
		    		select : function(combo, record, index) {
		    			setUnderTheSinger(this.getValue(),true);
		    			if(Ext.getCmp("addSku")){
		    			Ext.getCmp("addSku").enable();
		    			}
		    		},
		    		afterrender : function(combo) {
		    			setUnderTheSinger(this.getValue(),false);
		    			if(this.getValue()){
		    				if(Ext.getCmp("addSku")){
		    				Ext.getCmp("addSku").enable();
		    			}
		    		}
		    	}
		    	}
	        },relationCategory_bizType,CodeNameData.bizType
	    ),{
			xtype:'displayfield',
	        fieldLabel:'申报时间',
	        name: 'APP_TIME',
	        hidden: !this.submited
	    },
//	    GenerateCodeNameComboGrid({
//	    		allowBlank : false,
//		    	fieldLabel:'用户名称',
//		    	name: 'APP_UID'
//	        },relationCategory_appUid
//	    ),
	    GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'电商平台名称',
		    	name: 'EBP_CODE'
	        },relationCategory_ebp
	    ),GenerateCodeNameComboGrid({
	    		id:"EBC_CODE",
	    		allowBlank : false,
		    	fieldLabel:'电商企业名称',
		    	name: 'EBC_CODE'
	        },relationCategory_ebc
	    ),
//	    GenerateCodeNameComboGrid({
//	    		allowBlank : false,
//		    	fieldLabel:'申报企业名称',
//		    	name: 'AGENT_CODE'
//	        },relationCategory_agent
//	    ),
	    {
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'订单商品货款',
	        maxLength: 20,
	        decimalPrecision:5,
	        allowDecimals:true,
	        name: 'GOODS_VALUE'
	    },{
	    	id:'TAX_FEE',
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'综合税费',
//	        maxLength: 4,
	        decimalPrecision:2,
	        allowDecimals:true,
	        name: 'TAX_FEE',
	        allowBlank : true
	    },{
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'订单商品运费',
	        maxLength: 20,
	        decimalPrecision:5,
	        allowDecimals:true,
	        name: 'FREIGHT'
	    },GenerateCodeNameComboGrid({
	    	fieldLabel:'币制',
	    	name: 'CURRENCY',
	        allowBlank : false
        	},relationCategory_currency
	    ),
	    GenerateContactComboGrid({
	    	id:'UNDER_THE_SINGER_ID',
	    	fieldLabel:'下单人信息',
	    	name: 'UNDER_THE_SINGER_ID',
	        allowBlank : true
        }),
        GenerateContactComboGrid({
	    	fieldLabel:'收货人信息',
	    	name: 'CONSIGNEE_ID',
	        allowBlank : false
        }),
        GenerateCodeNameComboGrid({
    		allowBlank : false,
	    	fieldLabel:'物流企业名称',
	    	name: 'LOGISTICS_CODE'
        	},relationCategory_logistics
	    ),{
	        fieldLabel:'物流运单编号',
	        name: 'LOGISTICS_NO',
	//        readOnly: this.editType!=="add",
	        maxLength: 50
		}
//        ,{
//	    	id:'CONSIGNEE_ADDRESS',
//	        fieldLabel:'收货人地址',
//	        name: 'CONSIGNEE_ADDRESS',
//	        maxLength: 200,
//	        disabled:true
//	    },{
//	    	id:'CONSIGNEE_TELEPHONE',
//	        fieldLabel:'收货人电话',
//	        name: 'CONSIGNEE_TELEPHONE',
//	        maxLength: 50,
//	        disabled:true
//	    },GenerateCodeNameComboGrid({
//	    	id:'CONSIGNEE_COUNTRY',
//	    	fieldLabel:'收货人所在国',
//	    	name: 'CONSIGNEE_COUNTRY',
//	        allowBlank : false,
//	        disabled:true
//        	},relationCategory_country
//	    )
        ,{
	        xtype: 'textarea',
	        maxLength: 1000,
	        fieldLabel:'备注',
	        name: 'NOTE',
	        allowBlank : true,
	    }];
		
//		var itemGrid=new Ext.ux.SkuGridPanel({
//			name:"itemGrid",
//			mode:'local',
//			readOnly:this.readOnly,
//			height: 200,
//			showColumns : [
//			     "ITEM_NO",
//			     "BIZ_TYPE",
//			     "G_NO",
//			     "G_CODE",
//			     "G_NAME",
//			     "G_MODEL",
//			     "BAR_CODE",
//			     "BRAND",
//			     "UNIT",
//			     /*"PRICE",*///备案价格
//			     "CURRENCY",
//			     "ONE_PRICE",//单价
//			     "QTY",
//			     "TOTAL",
////			     "GIFT_FLAG",
//			     "NOTE_OD"],
//			storeCfg:{
//				url: 'import-common!getAllSkus.action',
//				reader : new Ext.data.JsonReader({
//					totalProperty : 'total',
//					root : "rows"
//				}, ["SKU_ID",
//				     "GUID",
//				     "CUSTOM_CODE",
//				     "APP_TYPE",
//				     "APP_TIME",
//				     "APP_STATUS",
//				     "APP_UID",
//				     "PRE_NO",
//				     "EBP_CODE",
//				     "EBC_CODE",
//				     "AGENT_CODE",
//				     "CLASSIFY_CODE",
//				     "ITEM_NO",
//				     "BIZ_TYPE",
//				     "G_NO",
//				     "G_CODE",
//				     "G_NAME",
//				     "G_MODEL",
//				     "BAR_CODE",
//				     "BRAND",
////				     "TAX_CODE",
//				     "UNIT",
//				     "UNIT1",
//				     "UNIT2",
//				     "PRICE",
//				     "CURRENCY",
//				     "GIFT_FLAG",
//				     "NOTE",
//				     "RETURN_STATUS",
//				     "RETURN_TIME",
//				     "RETURN_INFO",
//				     "CREAT_TIME",
//				     "UPDATE_TIME",
//
//				     //电子订单
//				     "ONE_PRICE",
//				     "QTY",
//				     "TOTAL",
//				     "NOTE_OD"])
//			},
//			columns:[ new Ext.grid.RowNumberer({
//					width : 26
//				}), {
//				    id : "ITEM_NO",
//				    header : "电商企业商品货号",
//				    dataIndex : "ITEM_NO"
//				},{
//				    id : "BIZ_TYPE",
//				    header : "进出口业务类型",
//				    dataIndex : "BIZ_TYPE",
//				    renderer: Renderer.BIZ_TYPE
//				},{
//				    id : "G_NO",
//				    header : "海关商品备案编号",
//				    dataIndex : "G_NO"
//				},{
//				    id : "G_CODE",
//				    header : "海关商品编码HSCode",
//				    dataIndex : "G_CODE"
//				},{
//				    id : "G_NAME",
//				    header : "商品名称",
//				    dataIndex : "G_NAME"
//				},{
//				    id : "G_MODEL",
//				    header : "规格型号",
//				    dataIndex : "G_MODEL"
//				},{
//				    id : "BAR_CODE",
//				    header : "条形码",
//				    dataIndex : "BAR_CODE"
//				},{
//				    id : "BRAND",
//				    header : "品牌",
//				    dataIndex : "BRAND"
//				},/*new Ext.ux.grid.CodeNameColumn({
//					category: relationCategory_taxCode,
//				    id : "TAX_CODE",
//				    header : "海关物品税号",
//				    dataIndex : "TAX_CODE"
//				}),*/new Ext.ux.grid.CodeNameColumn({
//					category: relationCategory_unit,
//				    id : "UNIT",
//				    header : "计量单位",
//				    dataIndex : "UNIT"
//				}),{
//				    id : "PRICE",
//				    header : "备案价格",
//				    dataIndex : "PRICE"
//				},new Ext.ux.grid.CodeNameColumn({
//					category: relationCategory_currency,
//				    id : "CURRENCY",
//				    header : "币制",
//				    dataIndex : "CURRENCY"
//				}),
//				//电子订单
//				{
//				    header : "单价",
//				    dataIndex : "ONE_PRICE",
//				    editor : new Ext.form.NumberField({
//						allowBlank:false,
//						allowNegative:false
//				    })
//				},{
//				    header : "数量",
//				    dataIndex : "QTY",
//				    editor : new Ext.ux.form.SpinnerField({
//				    	allowDecimals:false,
//						allowBlank:false,
//						allowNegative:false,
//						value:1,
//						minValue:1,
//						accelerate: true,
//						alternateIncrementValue: 10
//				    })
//				},{
//				    header : "总价",
//				    dataIndex : "TOTAL",
//				    editor : new Ext.form.NumberField({
//						allowBlank:false,
//						allowNegative:false
//				    })
//				},
//				//电子订单
//				{
//				    id : "GIFT_FLAG",
//				    header : "是否赠品",
//				    dataIndex : "GIFT_FLAG",
//				    renderer: Renderer.GIFT_FLAG
//				},{
//				    id : "NOTE_OD",
//				    header : "备注",
//				    dataIndex : "NOTE_OD"
//				}]
//			,
//			listeners:{
//				afteredit : function(e){
//					if(e.value!=e.originalValue){
//						if(e.field=="ONE_PRICE"||e.field=="QTY"){
//							e.record.set("TOTAL",e.record.get("ONE_PRICE")*e.record.get("QTY"));
//						}else if(e.field=="TOTAL"){
//							var ONE_PRICE=parseFloat(e.record.get("TOTAL")*1.0/e.record.get("QTY")).toFixed(2);
//							e.record.set("ONE_PRICE",ONE_PRICE);
//						}
//					}
//				}
//			}
//		});
		/*function countTotal(){
			var itemGrid=
		};
		itemGrid.getStore().on("add",this.countTotal,this);
		itemGrid.getStore().on("update",this.countTotal,this);*/
		var columns=[];
		var hiddens=[];
		Ext.apply(this.defaults,{readOnly:this.readOnly});
		Ext.each(this.items,function(item,index,allItems){
			if(!item.hidden){
				if(columns.length==0||columns[columns.length-1].items==undefined||columns[columns.length-1].items.length==2){
					columns.push({
						layout:'column',
						border:false,
						defaults:{columnWidth: .5},
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
//		this.items.push(itemGrid);
		
		var validateButton=function (){
			if(this.buttons==undefined)
				this.buttons=[];
			if(!this.readOnly){
				this.buttons.push(new Ext.Button({
					disabled: false,
					formBind: false,
			    	text : '保存', 
			    	scale: 'large',
			    	icon : '../../resource/images/btnImages/save.png',
					handler : function(b,e){
						Ext.apply(this.baseParams,{opType:'save'});
						this.submitForm(1);
			    	}.createDelegate(this)
			    }),new Ext.Button({
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

		Ext.ux.OrderFormPanel.superclass.initComponent.call(this);
		this.renderRecord();
	}
});

Ext.ux.OrderPanel = Ext.extend(Ext.Panel, {
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
			return isOrderReadOnly(this.record);
		}
		return false;
	},
//	addItem: function(){
//		var success=false;
//		var itemGridSelect=findByProperty(this.items.getRange(),"name","itemGrid");
//		if(itemGridSelect){
//			var data=itemGridSelect.checkSelect(true);
////			data=new Ext.data.Record(data.data);
//			if(data){
//				var numberField=findByProperty(itemGridSelect.buttons,"name","numbers");
//				if(numberField){
//					var numbers=numberField.getValue();
//					var infoForm=findByProperty(this.items.getRange(),"name","infoForm");
//					if(infoForm){
//						var itemGrid=findByProperty(infoForm.items.getRange(),"name","itemGrid");
//						var ITEM_NO=data.get("ITEM_NO");
//						var ITEM_RECORD_IDX = itemGrid.getStore().find("ITEM_NO",ITEM_NO);
//						
//						var ITEM_RECORD;
//						if(ITEM_RECORD_IDX>=0){
//							ITEM_RECORD=itemGrid.getStore().getAt(ITEM_RECORD_IDX);
//							var qty=ITEM_RECORD.get("QTY");
//							numbers=qty+numbers;
//						}else{
//							ITEM_RECORD=new Ext.data.Record(data.data);
//							itemGrid.getStore().add(ITEM_RECORD);
//						}
//						ITEM_RECORD.beginEdit();
//						if(Ext.isEmpty(ITEM_RECORD.get("ONE_PRICE")))
//							ITEM_RECORD.set("ONE_PRICE",ITEM_RECORD.get("PRICE"));
//						if(Ext.isEmpty(ITEM_RECORD.get("NOTE_OD")))
//							ITEM_RECORD.set("NOTE_OD",ITEM_RECORD.get("NOTE"));
//						ITEM_RECORD.set("QTY",numbers);//数量
//						ITEM_RECORD.set("TOTAL",ITEM_RECORD.get("ONE_PRICE")*ITEM_RECORD.get("QTY"));
//						ITEM_RECORD.endEdit();
//						success=true;
//					}
//				}
//			}
//		}
//		if(success){
//			this.addShoping.createDelegate(this,arguments)();
//		}
//	},
	// private
	initItem: function(){
		
		if(true){
			this.items=[new Ext.ux.OrderFormPanel({
				name:"infoForm",
				readOnly: false,
				editType: this.editType,
				record: this.record
			})];
		}else{
//			this.items=[new Ext.ux.OrderFormPanel({
//				name:"infoForm",
//				record: this.record,
//				editType: this.editType,
//				buttons:[new Ext.Button({
//					id:"addSku",
//		            text: '添加商品',
//		            icon: '../../resource/images/btnImages/package_add.png',
//		            scale: 'medium',
//		            disabled:true,
//		            handler: this.rollPage.createDelegate(this, [1]),
//			    	listeners : {
//			    		click : function(button,e) {
//			    			//更新商品信息
//			    			Ext.apply(SkuCompleteParam,{BIZ_TYPE:Ext.getCmp("ORDER_TYPE").getValue()});
//			    			Ext.apply(SkuCompleteParam,{EBC_CODE:Ext.getCmp("EBC_CODE").getValue()});
//			    			var pageTool = Ext.getCmp("itemGrid").pageTool;
//							if (pageTool) {
//								pageTool.doLoad(pageTool.cursor);
//							}
//			    		}
//			    	}
//		        })]
//			}),new Ext.ux.SkuGridPanel({
//				id:"itemGrid",
//				readOnly:true,
//				name:"itemGrid",
//				showColumns : [
//				     "ITEM_NO",
//				     "BIZ_TYPE",
//				     "G_NO",
//				     "G_CODE",
//				     "G_NAME",
//				     "G_MODEL",
//				     "BAR_CODE",
//				     "BRAND",
//				     "UNIT",
//				     "PRICE",
//				     "CURRENCY",
//				     "GIFT_FLAG",
//				     "NOTE"],
//				storeCfg:{
//					baseParams:SkuCompleteParam
//				},
//				buttons:["数量",new Ext.ux.form.SpinnerField({
//					width: 80,
//					scale: 'large',
//					name:"numbers",
//					allowDecimals:false,
//					allowBlank:false,
//					allowNegative:false,
//					value:1,
//					minValue:1,
//					accelerate: true,
//					alternateIncrementValue: 10
//				}),new Ext.Button({
//					name: "addInto",
//		            text: '加入订单',
//		            icon: '../../resource/images/btnImages/cart_put.png',
//		            scale: 'large',
//		            handler: this.addItem.createDelegate(this)
//		        }),new Ext.Button({
//		        	name: "next",
//		            text: '订单信息',
//		            icon: '../../resource/images/btnImages/cart_go.png',
//		            scale: 'large',
//		            iconAlign: 'right',
//		            handler: this.rollPage.createDelegate(this, [-1])
//		        })]
//			})];
		}
	},
	// private
	initEvents : function(){
		Ext.ux.OrderPanel.superclass.initEvents.call(this);
		if(this.monitorValid){
			this.startMonitoring();
		}
	},
	bindHandler : function(){
//		var addIntoValid=false;
//		var nextValid=false;
//		var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
//		if(itemGrid){
//			var itemData=itemGrid.checkSelect(true,true);
//			var numberField=findByProperty(itemGrid.buttons,"name","numbers");
//			
//			var itemValid=itemData!==false;
//			var numberValid=false;
//			if(numberField){
//				numberValid=numberField.isValid();
//			}
//			addIntoValid=itemValid&&numberValid;
//			
//			var addInto=findByProperty(itemGrid.buttons,"name","addInto");
//			if(addInto){
//				addInto.setDisabled(!addIntoValid);
//			}
//		}
//		this.fireEvent('clientvalidation', this, addIntoValid, nextValid);
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
		Ext.ux.OrderPanel.superclass.beforeDestroy.call(this);
	},
	// private
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.initItem();
		if(!this.readOnly){
			this.monitorValid=true;
//			if(this.editType!="add"){
//				this.activeItem=1;
//			}
//			this.addEvents('clientvalidation');
		}
		Ext.ux.OrderPanel.superclass.initComponent.call(this);
	},
	addShoping:function(btn,e){
//		e.stopPropagation();
//		var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
//
//		var rowIndex = itemGrid.getStore().indexOf(
//				itemGrid.getSelectionModel().getSelections()[0]);
//		
//		var next=findByProperty(itemGrid.buttons,"name","next");
//		
//		var target=Ext.get(itemGrid.view.getRow(rowIndex)),
//			shop=next.getEl(),
//			id=target.id+"-floatOrder";
////			x = target.getLeft() + target.getWidth()/2,
////			y = target.getTop() + target.getHeight()/2;
//
//		if (Ext.isEmpty(Ext.get(id))) {
//			Ext.getBody().createChild({
//				id: id,
//				tag: 'div'
//			});
//		};
//		var obj=Ext.get(id);
//		obj.setSize(target.getWidth(),target.getHeight());
//		obj.alignTo(target,"c-c");
//		obj.dom.innerHTML='<img src="../../resource/images/btnImages/package.png" width="100%" height="100%" />';
////		obj.highlight({duration: .35});
//		var tWidth=32;
//		var tHeight=32;
//		obj.shift({
//			width:tWidth,
//			height:tHeight,
//			x: obj.getAlignToXY(shop, "b-t", [0, -10])[0]+(obj.getWidth()-tWidth)/2,
//			y: obj.getAlignToXY(shop, "b-t", [0, -10])[1]+(obj.getHeight()-tHeight),
//			duration: .2
//		});
//		obj.shift({
//			x: obj.getAlignToXY(shop, "b-b", [0, 0])[0]+(obj.getWidth()-tWidth)/2,
//			y: obj.getAlignToXY(shop, "b-b", [0, 0])[1]+(obj.getHeight()-tHeight),
//			duration: .3,
//			remove: true
//		});
//		me.getAlignToXY(element, position, offsets),
		
//		obj.fadeOut({remove: true});
		/*obj.alignTo(shop,"b-t", [0, -10],{duration:0.5,callback:function(){
			obj.alignTo(shop,"b-b");
			obj.fadeOut({remove: true});
		}});*/
	}
});

//设置下单人信息
function setUnderTheSinger(bizType,needClearValue){
	//进口 下单人必填 必填 行邮税率 必填
	if(bizType == 1 || bizType == 3){
//		Ext.getCmp("UNDER_THE_SINGER_ID").setDisabled(false);
		Ext.getCmp("UNDER_THE_SINGER_ID").allowBlank = false;
		
//		Ext.getCmp("TAX_FEE").setDisabled(false);
		Ext.getCmp("TAX_FEE").allowBlank = false;
	}
	//出口 下单人设置为空 行邮税率 不必填
	else if(bizType == 2 || bizType == 4){
		if(needClearValue){
		Ext.getCmp("UNDER_THE_SINGER_ID").setValue("");
		}
//		Ext.getCmp("UNDER_THE_SINGER_ID").setDisabled(true);
		Ext.getCmp("UNDER_THE_SINGER_ID").allowBlank = true;
		Ext.getCmp("UNDER_THE_SINGER_ID").clearInvalid();
		
		if(needClearValue){
			Ext.getCmp("TAX_FEE").setValue("");
		}
//		Ext.getCmp("TAX_FEE").setDisabled(true);
		Ext.getCmp("TAX_FEE").allowBlank = true;
		Ext.getCmp("TAX_FEE").clearInvalid();
	}
}

//查询数据
function order_search(store){

	var param = {"limit":myPageSize,"start":0,"fuzzy":true};
	
	if(Ext.getCmp('ORDER_TYPE_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"ORDER_TYPE":Ext.getCmp('ORDER_TYPE_ORDER_SEARCH').getValue()});
	}
	if(Ext.getCmp('EBC_CODE_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"EBC_CODE":Ext.getCmp('EBC_CODE_ORDER_SEARCH').getValue()});
	}
	if(Ext.getCmp('ORDER_NO_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"ORDER_NO":Ext.getCmp('ORDER_NO_ORDER_SEARCH').getValue()});
	}
	if(Ext.getCmp('LOGISTICS_NO_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"LOGISTICS_NO":Ext.getCmp('LOGISTICS_NO_ORDER_SEARCH').getValue()});
	}
	if(Ext.getCmp('APP_STATUS_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"APP_STATUS":Ext.getCmp('APP_STATUS_ORDER_SEARCH').getValue()});
	}
	if(Ext.getCmp('RETURN_STATUS_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"RETURN_STATUS":Ext.getCmp('RETURN_STATUS_ORDER_SEARCH').getValue()});
	}
	if(Ext.getCmp('NOTE_ORDER_SEARCH').getValue()){
		Ext.apply(param,{"NOTE":Ext.getCmp('NOTE_ORDER_SEARCH').getValue()});
	}

	store.baseParams = param;

	store.load();
}