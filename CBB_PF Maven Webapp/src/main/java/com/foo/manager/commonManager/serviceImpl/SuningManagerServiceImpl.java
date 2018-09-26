package com.foo.manager.commonManager.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foo.IService.ISuningManagerService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.manager.commonManager.service.SuningManagerService;
import com.foo.util.CommonUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class SuningManagerServiceImpl extends SuningManagerService implements ISuningManagerService{
	
	@Override
	public Map<String, Object> getAllLogisticses(Map<String, Object> params)
			throws CommonException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		int total = 0;

		// 开始
		Integer start = params.get("start") == null ? null : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());

		params.remove("start");
		params.remove("limit");
		try {
			String tableName = "t_logistics_sn";
			
			List<String> keys=new ArrayList<String>(params.keySet());
			List<Object> values=new ArrayList<Object>(params.values());
			rows = commonManagerMapper.selectTableListByNVList(tableName, 
					keys,values,start, limit);
			total = commonManagerMapper.selectTableListCountByNVList(tableName,
					keys,values);
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void delLogistics(Map<String, Object> params) throws CommonException {
		try {
			commonManagerMapper.delTableById("t_logistics_sn", "LOGISTICS_ID",
					params.get("LOGISTICS_ID"));
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void setLogistics(Map<String, Object> logistics, boolean statusOnly)
			throws CommonException {
		try {
			String primaryCol="LOGISTICS_ID";
			String tableName="t_logistics_sn";
			if(statusOnly){
				List<String> keys=Arrays.asList(new String[]{
						"LOGISTICS_STATUS","RETURN_STATUS","RETURN_TIME","RETURN_INFO"});
				List<Object> values=Arrays.asList(new Object[]{
						logistics.get("LOGISTICS_STATUS"),
						null,null,null});
				commonManagerMapper.updateTableByNVList(tableName, primaryCol,
						logistics.get(primaryCol), keys, values);
				Object primaryId=logistics.get(primaryCol);
				
				//生成xml报文
				//修改APP_STATUS为upload
				logistics.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
				submitXml_LOGISTICS(logistics,
						Integer.valueOf(primaryId.toString()),
						CommonDefine.CEB503, CommonDefine.LOGISTICS_TYPE_SUNING);
			}else{
				String uniqueCol="LOGISTICS_NO";
				// 唯一性校验
				uniqueCheck(tableName,uniqueCol,logistics.get(uniqueCol),primaryCol,logistics.get(primaryCol),false);
				uniqueCol="ORDER_NO";
				uniqueCheck(tableName,uniqueCol,logistics.get(uniqueCol),primaryCol,logistics.get(primaryCol),false);
				
				logistics.remove("editType");
				
				commonManagerMapper.updateTableByNVList(tableName, primaryCol,
						logistics.get(primaryCol), new ArrayList<String>(logistics.keySet()),
						new ArrayList<Object>(logistics.values()));
	
				Object primaryId=logistics.get(primaryCol);
	
				// 生成xml报文
				submitXml_LOGISTICS(logistics,
						Integer.valueOf(primaryId.toString()),
						CommonDefine.CEB501, CommonDefine.LOGISTICS_TYPE_SUNING);
			}
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void addLogistics(Map<String, Object> logistics) throws CommonException {
		try {
			String tableName="t_logistics_sn";
			String uniqueCol="LOGISTICS_NO";
			String primaryCol="LOGISTICS_ID";
			// 唯一性校验
			uniqueCheck(tableName,uniqueCol,logistics.get(uniqueCol),null,null,false);
			uniqueCol="ORDER_NO";
			uniqueCheck(tableName,uniqueCol,logistics.get(uniqueCol),null,null,false);
			
			logistics.remove("editType");
			// 设置空id
			logistics.put(primaryCol, null);
			
			// 设置guid
			logistics.put("GUID", CommonUtil.generalGuid(CommonDefine.GUID_FOR_LOGISTICS_SN,4,tableName));
			// 设置创建时间
			logistics.put("CREAT_TIME", new Date());
			
			Map<String,Object> primary=new HashMap<String,Object>();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList(tableName,
					new ArrayList<String>(logistics.keySet()), 
					new ArrayList<Object>(logistics.values()),
					primary);
			
			Object primaryId=primary.get("primaryId");

			// 生成xml报文
			submitXml_LOGISTICS(logistics,
					Integer.valueOf(primaryId.toString()), CommonDefine.CEB501,
					CommonDefine.LOGISTICS_TYPE_SUNING);
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
}
