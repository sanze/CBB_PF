package com.foo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.foo.common.CommonDefine;
import com.foo.dao.mysql.CommonManagerMapper;

public class CommonUtil {
      
	/**
	 * 获取上周一的日期
	 * 
	 * @param
	 * 
	 * @return 上周一的日期
	 */
	public static Date getFirstDayOfLastWeek(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.setFirstDayOfWeek(Calendar.MONDAY);
		if (cld.get(Calendar.DAY_OF_YEAR) < 7) {
			cld.set(cld.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
		}
		cld.roll(Calendar.WEEK_OF_YEAR, false);
		cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cld.getTime();
	}

	/**
	 * 获取上周日的日期
	 * 
	 * @param
	 * 
	 * @return 上周日的日期
	 */
	public static Date getLastDayOfLastWeek(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.setFirstDayOfWeek(Calendar.MONDAY);
		if (cld.get(Calendar.DAY_OF_YEAR) < 7) {
			cld.set(cld.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
		}
		cld.roll(Calendar.WEEK_OF_YEAR, false);
		cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cld.getTime();
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @param
	 * 
	 * @return 昨天的日期
	 */
	public static Date Yesterday(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		if (cld.get(Calendar.DAY_OF_YEAR) == 1) {
			cld.set(cld.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
		} else {
			cld.roll(Calendar.DAY_OF_YEAR, false);
		}
		return cld.getTime();
	}

	/**
	 * 获取上月1号的日期
	 * 
	 * @param
	 * 
	 * @return 上月1号的日期
	 */
	public static Date getFirstDayOfLastMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		if (cld.get(Calendar.MONTH) == Calendar.JANUARY) {
			cld.set(cld.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 1);
		} else {
			cld.set(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH) - 1, 1);
		}
		return cld.getTime();
	}

	/**
	 * 获取上月最后一天的日期
	 * 
	 * @param
	 * 
	 * @return 上月最后一天的日期
	 */
	public static Date getLastDayOfLastMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		if (cld.get(Calendar.MONTH) == Calendar.JANUARY) {
			cld.set(cld.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
		} else {
			cld.set(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH) - 1, 1);
			cld.set(Calendar.DAY_OF_MONTH,
					cld.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return cld.getTime();
	}
	
	/**
	 * 获得与指定日期相差指定时间差的日期
	 * @param specifiedDay
	 * @param dayGap
	 * @param minuteGap
	 * @return
	 */
	public static Date getSpecifiedDay(Date specifiedDay,int dayGap,int minuteGap) {
		Calendar c = Calendar.getInstance();
		c.setTime(specifiedDay);
		int day = c.get(Calendar.DATE);
		int minute = c.get(Calendar.MINUTE);
		c.set(Calendar.DATE, day + dayGap);
		c.set(Calendar.MINUTE, minute + minuteGap);
		Date target = c.getTime();
		return target;
	}
	
	
	
	/**
	 * 在系统控制台输出两个时间直接的差值
	 * @param title
	 * @param first
	 * @param second
	 */
	public static void timeDif(String title, Date first, Date second){
		long l=second.getTime()-first.getTime();
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		long min=((l/(60*1000))-day*24*60-hour*60);
		long s=(l/1000-day*24*60*60-hour*60*60-min*60);
		long ms = l- day*24*60*60*1000 - hour*60*60*1000 - min*60*1000 - s*1000;
//		System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
		System.out.println(title + ":花费了" + (day==0?"":day+"天") + (hour==0?"":hour+"小时") + 
				(min==0?"":min+"分") + (s==0?"0.":s+".") + (ms>=100?ms:ms>=10?"0"+ms:"00"+ms) + "秒");
	}
	
	
	/**
	 * 获取日期格式对象
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getDateFormatter(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter;
	}
	
	
	
	/**
	 * 获取系统配置属性
	 * @param propertyName 属性名
	 * @return
	 */
	public static String getSystemConfigProperty(String propertyName) {
		ResourceBundle bundle = ResourceBundle
				.getBundle("resourceConfig/systemConfig/"
						+ CommonDefine.SYSTEM_CONFIG_FILE);
		String propertyValue = null;
		try {
			propertyValue = new String(bundle.getString(propertyName).getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {

		}
		// 未配置ftpIp时使用本机计算机名
		if ((propertyValue == null || propertyValue.isEmpty())
				&& CommonDefine.FTP_IP.equals(propertyName)) {
			propertyValue = getLocalHostName();
		}
		return propertyValue;
	}
	
	/**
	 * 获取消息字段映射文件
	 * @param messageType 消息类型
	 * @return
	 */
	public static ResourceBundle getMessageMappingResource(String messageType) {
		ResourceBundle bundle = ResourceBundle
				.getBundle("resourceConfig/messageMapping/"
						+ messageType);
		return bundle;
	}
	
	/**
	 * 获取消息字段映射文件
	 * @param messageType 消息类型
	 * @return
	 */
	public static ResourceBundle getResource(String fielName) {
		ResourceBundle bundle = ResourceBundle
				.getBundle("resourceConfig/"
						+ fielName);
		return bundle;
	}
	
	/**
	 * 获取消息配置属性
	 * @param propertyName 属性名
	 * @return
	 */
	public static String getMessageConfigProperty(String propertyName){
		ResourceBundle bundle = ResourceBundle.getBundle("resourceConfig/i18n/"+CommonDefine.MESSAGE_CONFIG_FILE);
		return bundle.getString(propertyName);
	}
	
	/** 获取本机计算机名*/
	public static String getLocalHostName(){
		try {
			InetAddress address=InetAddress.getLocalHost();
			return address.getHostName().toString();
		} catch (Exception e) {
			
		}
		return null;
		
	}
	
	/** 检查到指定ip的网络状态*/
	public static boolean isReachable(String targetIp){
		boolean isReachable = true;
		//测试能否ping通
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(targetIp);
			isReachable = inetAddress.isReachable(5000);
		} catch (UnknownHostException e1) {
			isReachable = false;
		} catch (IOException e) {
			isReachable = false;
		} catch (Exception e) {
			isReachable = false;
		}
		//如果ping不通，使用cmd模式再次ping，南昌发现服务器cmd ping可以ping通
		//但是使用InetAddress.isReachable返回false,原因是服务器tcp 7端口未开通
		if(!isReachable){
			isReachable = isReachableUseCmd(targetIp);
		}
		return isReachable;
	}
	
	//使用命令行检测是否可以ping通
	public static boolean isReachableUseCmd(String targetIp) {
		
		boolean isReachable = false;
		Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
		Process process = null; // 声明处理类对象
		String line = null; // 返回行信息
		InputStream is = null; // 输入流
		InputStreamReader isr = null; // 字节流
		BufferedReader br = null;
		try {
			process = runtime.exec("ping " + targetIp); // PING
			is = process.getInputStream(); // 实例化输入流
			isr = new InputStreamReader(is);// 把输入流转换成字节流
			br = new BufferedReader(isr);// 从字节中读取文本
			while ((line = br.readLine()) != null) {
				if (line.contains("TTL")) {
					isReachable = true;
					break;
				}
			}
			is.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			runtime.exit(1);
		}
		return isReachable;
	}

	/**
	 * 自动生成guid，从指定表获取指定字段的最大id值，用来生成guid流水号
	 * @param prifix 前缀
	 * @param number 补位位数
	 * @param tableName 表名
	 * @param column 字段名
	 * @return
	 */
	public static synchronized String generalGuid(String prifix,int number,String tableName) {
		
		String dbName = SpringContextUtil.getDataBaseName();
		
		CommonManagerMapper commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
		
		SimpleDateFormat sf = new SimpleDateFormat(
				CommonDefine.RETRIEVAL_TIME_FORMAT);

		String currentTime = sf.format(new Date());

		Integer maxId = commonManagerMapper.selectMaxIdFromTable(dbName, tableName);

		maxId = maxId == null ? 1 : maxId.intValue();

		String formatNumber = String.format("%0"+number+"d", maxId);

		String target = prifix + "-" + currentTime + "-" + formatNumber;

		return target;
	}
	
	/**
	 * 自动生成guid，从指定表获取指定字段的最大id值，用来生成guid流水号
	 * @param prifix 前缀
	 * @param number 补位位数
	 * @param tableName 表名
	 * @param column 字段名
	 * @return
	 */
	public static synchronized String generalGuid4NJ(int type,String ebcCode,String customsCode) {
		
		SimpleDateFormat df = new SimpleDateFormat(CommonDefine.RETRIEVAL_TIME_FORMAT_NJ);

//		String staticString = getSystemConfigProperty(CommonDefine.ENTERPRISE_CODE);
//		
//		if(staticString == null){
//			staticString = "unConfig";
//		}
		
		String currentTime = df.format(new Date())+"00";
		
		String prifix = "";
		
		switch(type){
		case CommonDefine.CEB201:
			prifix = "CEB_201";
			break;
		case CommonDefine.CEB301:
			prifix = "CEB_301";
			break;
		case CommonDefine.CEB401:
			prifix = "CEB_401";
			break;
		case CommonDefine.CEB501:
			prifix = "CEB_501";
			break;
		case CommonDefine.CEB503:
			prifix = "CEB_503";
			break;
		case CommonDefine.CEB601:
			prifix = "CEB_601";
			break;
			
		case CommonDefine.CEB511:
			prifix = "CEB_511";
			currentTime = currentTime+"000000000";
			break;
		case CommonDefine.CEB311:
			prifix = "CEB_311";
			break;
			default:
				prifix = "CEB";
		}
		if(!ebcCode.isEmpty()){
			ebcCode = ebcCode + "_";
		}
		if(!customsCode.isEmpty()){
			customsCode = customsCode + "_";
		}
		String target = prifix + "_" + ebcCode + customsCode + currentTime;

		return target;
	}
	
	
	/**
	 * 自动生成guid，从指定表获取指定字段的最大id值，用来生成guid流水号
	 * @param prifix 前缀
	 * @param number 补位位数
	 * @param tableName 表名
	 * @param column 字段名
	 * @return
	 */
	public static synchronized String generalLogisticsNo(int number,String columnName,String tableName) {
		
		CommonManagerMapper commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");

		Integer maxId = commonManagerMapper.selectMaxNoFromTable(columnName, tableName);

		maxId = maxId == null ? 0 : maxId.intValue();

		String formatNumber = String.format("%0"+number+"d", maxId+1);

		String target = formatNumber;

		return target;
	}
	
    // 加密  
    public static String encryptionBase64(String str) {  
        byte[] b = null;  
        String s = null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if (b != null) {  
            s = new BASE64Encoder().encode(b);  
        }  
        return s;  
    }  
  
    // 解密  
    public static String decryptBase64(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
        	BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
    
    /*** 
     * MD5加码 生成32位md5码 
     */  
    public static String encryptionMD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }
    
    
    public static String makeSign(String content){  
    	String signKey = CommonUtil.getSystemConfigProperty("signKey");
    	String rawStr = signKey + content + signKey; 
    	MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String data_digest = null;
		try {
			data_digest = new String(Base64.encodeBase64(md5.digest(rawStr.getBytes("utf-8"))),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return data_digest;  
    }
    
    
    
	
	public static void main(String args[]){
		System.out.println(makeSign("helloworld"));
	}
	
}
