function isInventoryReadOnly(record){
	return !(record.get("APP_STATUS")==1||	//暂存
			record.get("RETURN_STATUS")==4||	//发送海关失败
			record.get("RETURN_STATUS")==100);	//海关退单
}
var InventoryCompleteParam={
	RETURN_STATUS:899 //结关
};
function isInventoryComplete(record){
	if(Ext.isEmpty(record)) return false;
	return (record.get("RETURN_STATUS")==899);	//结关
}