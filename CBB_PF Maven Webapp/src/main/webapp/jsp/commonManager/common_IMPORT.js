var DATE_FORMAT="yyyyMMdd";

function GenerateCombo(param,data){
	return Ext.apply({
    	xtype: 'combo',
        editable : false,
        forceSelection: true,
        triggerAction : 'all',
        mode : 'local',
        valueField: 'value',
        displayField: 'text',
        store: new Ext.data.JsonStore({
            data : data,
            fields: ["text","value"]
        })
    },param);
}
function findIndexByProperty(array,property,value){
	var idx=-1;
	Ext.each(array,function(item,index,allItems){
		if((Ext.isEmpty(property)?item:item[property])==value){
			idx=index;
			return false;
		}
	});
	return idx;
};
function findByProperty(array,property,value){
	var idx=findIndexByProperty(array,property,value);
	if(idx>=0){
		return array[idx];
	}
	return undefined;
};

var TextValue={
	LOG_STATUS:[
        {'text':"签收",'value':"S"}]
};

var Renderer={
	APP_TYPE: function(val){
		//申报类型:1-新增 2-变更 3-删除,默认为1
    	switch(val){
    	case 1:
    		return "新增";
    	case 2:
    		return "变更";
    	case 3:
    		return "删除";
    	default:
    		return "未知："+val;
    	}
	},
	APP_STATUS:function(val,metadata,record,row,col,store){
    	//业务状态:1-暂存,2-申报,默认为1
    	switch(val){
    	case 1:
    		return "暂存";
    	case 2:
    		if(record.get("PRE_NO")!=null && record.get("PRE_NO")!=""){
    			return "审批中";
    		}else{
    		return "申报中";
    		}
    	case 4:
    		return "无需申报";
    	case 99:
    		return "申报完成";
    	default:
    		return "未知："+val;
    	}
    },
	GIFT_FLAG:function(val){
		if(val=="1"||val==1||val==true||val=="true")
			return "是";
		return "否";
	},
	RETURN_STATUS:function(val){
    	if(Ext.isEmpty(val)){
    		return;
    	}
    	//操作结果（2审批通过/3审批不通过
    	switch(val){
    	case 2:
    		return "电子口岸申报中";
    	case 3:
    		return "发送海关成功";
    	case 4:
    		return "发送海关失败";
    	case 100:
    		return "海关退单";
    	case 120:
    		return "海关入库";
    	default:
    		if(val<0) return "处理异常";
    		return "未知："+val;
    	}
    },
    CATEGORY:function(val){
    	//xml文件配置路径
    	//1.商品备案数据 2.电子订单 3.支付凭证 4.物流运单 5.物流运单状态 6.出境清单
    	switch(val){
    	case 1:
    		return "商品备案数据";
    	case 2:
    		return "电子订单";
    	case 3:
    		return "支付凭证";
    	case 4:
    		return "物流运单";
    	case 5:
    		return "物流运单状态";
    	case 6:
    		return "出境清单";
    	}
    },
    BIZ_TYPE:function(val){
    	//1.一般进口 2.一般出口 3.保税进口 4.保税出口
    	switch(parseInt(val)){
    	case 1:
    		return "一般进口";
    	case 2:
    		return "一般出口";
    	case 3:
    		return "保税进口";
    	case 4:
    		return "保税出口";
    	default:
    		return "未知："+val;
    	}
    },
    LOG_STATUS:function(val){
    	if(Ext.isEmpty(val)) return val;
    	
    	var record=findByProperty(TextValue.LOG_STATUS,'value',val);
    	if(record)
    		return record['text'];
    	return "未知："+val;
    },
    DATE:function (val){
    	if(Ext.isEmpty(val)) return val;

  		return new Date(val.time).format("Ymd");
    }
};