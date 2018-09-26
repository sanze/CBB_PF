function isOrderReadOnly(record){
	return !(record.get("APP_STATUS")==1||	//暂存
			record.get("RETURN_STATUS")==4||	//发送海关失败
			record.get("RETURN_STATUS")==100);	//海关退单
}
var OrderCompleteParam={
	RETURN_STATUS:120	//海关入库
};