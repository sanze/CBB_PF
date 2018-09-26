package com.foo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.foo.common.CommonException;
import com.foo.util.FtpUtils;

/**
 * Maven
 * @author xuxiaojun
 *
 */

public class TestFtpUtil {

//	@Test
//	public void testUploadFile() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		String sourceFilePath = "D://DKY-项目结构说明.doc";
//		String destFilePath = "测试文件/测试";
//		String destFileName = "DKY-项目结构说明.doc";
//		
//		assertTrue(util.uploadFile(sourceFilePath, destFilePath, destFileName));
//	}
//	
//	@Test
//	public void testDownloadFile() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		String sourceFilePath = "测试文件/测试/DKY-项目结构说明.doc";
//		String destFilePath = "D:/FTP文件目录/测试文件/";
//		String destFileName = "DownloadFile.doc";
//		
//		assertTrue(util.downloadFile(sourceFilePath, destFilePath, destFileName));
//	}
//	
//	@Test
//	public void testGetFile() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		String fileName = "测试文件/DKY-项目结构说明.doc";
//		
//		System.out.println(util.getFile(fileName).getName());
//	}
//	
//	@Test
//	public void testMoveFile() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		String sourceFilePath = "测试文件/测试";
//		String sourcefileName = "DKY-项目结构说明.doc";
//		String destFilePath = "测试文件";
//		String destFileName = "MoveFile.doc";
//		
//		Assert.assertTrue(util.moveFile(sourceFilePath, sourcefileName, destFilePath, destFileName));
//	}
	
//	@Test
//	public void testCreateFile() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		assertTrue(util.createFile("D:/测试文件DownloadFile.doc"));
//	}
//	
//	@Test
//	public void testCreateDirectory() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		assertTrue(util.createDirectory("文件夹/文件夹/文件夹"));
//	}
//	
//	@Test
//	public void testDeleteFile() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		Assert.assertTrue(util.deleteFile("测试文件/MoveFile.doc"));
//	}
//	
//	@Test
//	public void testGetFileList() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		List<String> fileList = util.getFileList("电子订单/输入");
//		
//		for(String file:fileList){
//			System.out.println(file);
//		}
//	}
	
//	@Test
//	public void testGetFileSize() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		long size = util.getFileSize("电子订单/输入", "133.37.155.70_2-贝森.csv");
//		
//		System.out.println(size);
//	}
	
//	@Test
//	public void testCheckFileExist() throws CommonException{
//		
//		FtpUtils util = new FtpUtils("127.0.0.1",21,"admin","admin");
//		
//		Assert.assertTrue(util.checkFileExist("电子订单/输入","133.37.155.70_2-贝森.csv"));
//	}
	
}
