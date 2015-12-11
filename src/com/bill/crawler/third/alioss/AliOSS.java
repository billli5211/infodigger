package com.bill.crawler.third.alioss;

import java.io.OutputStream;

/**
 * 阿里云OSS api 相关方法可直接添加
 * 
 * @ClassName: AliOSS
 *
 */
public class AliOSS {

	/**
	 * 上传图片输出流到阿里云 1、默认上传到mumu-storage上的images目录下
	 * 2、fileName为上传到阿里云上的命名，要写上路径，如20150320/1.jpg 3、out 为上传文件的输出流
	 * 
	 * @function: uploadImg
	 */
	public static void uploadImg(String fileName, OutputStream out) throws Exception {
		AliOSSUtil.uploadFile(AliOSSConfig.BASE_BUCKET, AliOSSConfig.BASE_IMG_FOLDER + fileName, out, 0);
	}

	/**
	 * 上传本地图片到阿里云 1、默认上传到mumu-storage上的images目录下
	 * 2、key为上传到阿里云上的命名，要写上路径，如20150320/1.jpg
	 * 3、fileName为原始文件路径，如d:/20150320/1.jpg
	 * 
	 * @function: uploadImg
	 */
	public static void uploadImg(String key, String fileName) throws Exception {
		AliOSSUtil.uploadFile(AliOSSConfig.BASE_BUCKET, AliOSSConfig.BASE_IMG_FOLDER + key, fileName, 0);
	}

	// 删除文件夹
	public static void deleteFolder(String folder) throws Exception {
		AliOSSUtil.deleteFolder(AliOSSConfig.BASE_BUCKET, folder);
	}

	// 删除文件
	public static void deleteFile(String fileName) throws Exception {
		if (fileName != null && fileName != "") {
			if (fileName.charAt(0) == '/') {
				fileName = fileName.substring(1);
			}
			AliOSSUtil.deleteFile(AliOSSConfig.BASE_BUCKET, fileName);
		}
	}
}
