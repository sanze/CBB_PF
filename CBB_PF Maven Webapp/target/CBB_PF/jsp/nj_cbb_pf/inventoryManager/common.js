function isInventoryReadOnly(record){
	return !(record.get("APP_STATUS")==1||	//暂存
			record.get("RETURN_STATUS")==3);	//海关退单
}
var InventoryCompleteParam={
	RETURN_STATUS:2 //结关
};
function isInventoryComplete(record){
	if(Ext.isEmpty(record)) return false;
	return (record.get("RETURN_STATUS")==2);	//结关
}

var ComboBoxValue_inventory={
	APP_STATUS:[["暂存","1"],["审批中/申报中","2"],["无需申报","4"],["申报完成","99"]],
	RETURN_STATUS:[["审批通过","2"],["审批不通过","3"]]
};