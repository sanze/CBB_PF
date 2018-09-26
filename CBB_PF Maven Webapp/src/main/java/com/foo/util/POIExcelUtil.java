package com.foo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.foo.abstractService.AbstractService;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;

public class POIExcelUtil {
	private File file;
	private SXSSFWorkbook book;
	private Sheet sheet;
	private FileOutputStream out = null;
	private List<Map> dat = null;
	private int success;
	private int col;
	private final int PAGE_LIMIT = 1000000;
	//以下用于一半的分组情况（主分组+子分组）
	private int keyColumn, autoGroup[], subGroup[];
	//----------以下用于统计报表导出-------------
	private Map<String, Integer> colMap = null, rowMap = null;
	
	public static List<Map<String,Object>> readExcel(File file,String[] columnNum) throws CommonException{

		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		try {
			Workbook wb = WorkbookFactory.create(file);
			//获取sheet
			Sheet sheet = wb.getSheetAt(0);
			//获取首行数据
			Row firstRow = sheet.getRow(sheet.getFirstRowNum());
			//
			int rowCount = 0;
			for (Row row : sheet) {
				if(rowCount != 0){
					Map<String,Object> rowData = new LinkedHashMap<String,Object>();
					for (int i=0;i<columnNum.length;i++) {
						Cell titleCell = firstRow.getCell(Integer.valueOf(columnNum[i]));
						Cell cell = row.getCell(Integer.valueOf(columnNum[i]));
						
						if(cell!=null){
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), cell.getNumericCellValue());
								break;
							case Cell.CELL_TYPE_BLANK:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), null);
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), cell.getBooleanCellValue());
								break;
							case Cell.CELL_TYPE_ERROR:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), cell.getErrorCellValue());
								break;
							case Cell.CELL_TYPE_FORMULA:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), cell.getCellFormula());
								break;
							case Cell.CELL_TYPE_STRING:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), cell.getStringCellValue());
								break;
							default:
								rowData.put(titleCell.getStringCellValue().toUpperCase(), cell.getStringCellValue());
							}
						}
					}
					data.add(rowData);
				}
				rowCount++;
			}		
		} catch (InvalidFormatException e) {
			throw new CommonException(e, MessageCodeDefine.COM_EXCPT_UNKNOW);
		} catch (IOException e) {
			throw new CommonException(e, MessageCodeDefine.COM_EXCPT_UNKNOW);
		} catch (Exception e) {
			throw new CommonException(e, MessageCodeDefine.COM_EXCPT_UNKNOW);
		}
		for(Map obj:data){
			System.out.println(obj);
		}
		return data;
	}

}