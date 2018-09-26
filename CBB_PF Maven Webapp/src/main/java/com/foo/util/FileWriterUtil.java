package com.foo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Map;

import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;

/**
 * @author 533
 * 
 */
public class FileWriterUtil {

	private static String lineSeparator = "\n";//System.getProperty("line.separator");

	/**
	 * @param filePath
	 * @param model
	 * @param append
	 * @return
	 * @throws CommonException
	 */
	public static File writeToTxt(String filePath,
			Map data) throws CommonException {

		File file = new File(filePath);
		OutputStreamWriter fileWriter = null;
		try {
			// 创建父文件
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			fileWriter = new OutputStreamWriter(new FileOutputStream(file.getPath()),"GBK");
			
			for(Object key:data.keySet()){
				StringBuffer result = new StringBuffer();
				result.append(new Date()+"  "+key+" ：\n"+data.get(key));
				fileWriter.write(result.toString());
				fileWriter.write(lineSeparator);
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new CommonException(e, MessageCodeDefine.COM_EXCPT_UNKNOW);
		} finally {

		}
		return file;
	}
}
