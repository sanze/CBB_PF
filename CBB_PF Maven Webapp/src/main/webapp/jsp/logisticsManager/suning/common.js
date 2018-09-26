function isTaskAvailable(record){
	var fields=[ "ORDER_NO",
	     "WEIGHT",
	     "NET_WEIGHT",
	     "PACK_NO",
	     "GOODS_INFO",
	     "CONSIGNEE",
	     "CONSIGNEE_ADDRESS",
	     "CONSIGNEE_TELEPHONE",
	     "CONSIGNEE_COUNTRY",
	     "SHIPPER",
	     "SHIPPER_ADDRESS",
	     "SHIPPER_TELEPHONE",
	     "SHIPPER_COUNTRY",
	     "NOTE"];
	for(var i=0;i<fields.length;i++){
		if(Ext.isEmpty(record.get(fields[i])))
			return false;
	}
	return true;
}