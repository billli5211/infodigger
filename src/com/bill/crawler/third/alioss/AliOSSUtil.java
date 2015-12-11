package com.bill.crawler.third.alioss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;

/**
 * 阿里云OSS 实现
 * 
 * @ClassName: AliOSSUtil
 *
 */
public class AliOSSUtil {
	private static OSSClient client = new OSSClient(AliOSSConfig.OSS_ENDPOINT, AliOSSConfig.ACCESS_ID,
			AliOSSConfig.ACCESS_KEY);

	// 创建Bucket.
	public static void createBucket(String bucketName) throws OSSException, ClientException {
		try {
			// 创建bucket
			client.createBucket(bucketName);
		} catch (ServiceException e) {
			if (!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())) {
				// 如果Bucket已经存在，则忽略
				// log.error("创建bucket=" + bucketName + "失败," + e.getMessage());
				throw e;
			}
		}
	}

	// 删除一个Bucket和其中的Objects
	public static void deleteBucket(String bucketName) throws OSSException, ClientException {

		ObjectListing ObjectListing = client.listObjects(bucketName);
		List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
		for (int i = 0; i < listDeletes.size(); i++) {
			String objectName = listDeletes.get(i).getKey();
			// 如果不为空，先删除bucket下的文件
			client.deleteObject(bucketName, objectName);
		}
		client.deleteBucket(bucketName);
	}

	// 把Bucket设置为所有人可读
	public static void setBucketPublicReadable(String bucketName) throws OSSException, ClientException {
		// 设置bucket的访问权限，public-read-write权限
		client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
	}

	// 创建文件夹
	public static void createFolder(String bucketName, String folderName) throws Exception {
		// 要创建的文件夹名称,在满足Object命名规则的情况下以"/"结尾
		String objectName = folderName + "/";

		ObjectMetadata objectMeta = new ObjectMetadata();
		/*
		 * 这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,dataStream仍然可以有数据
		 * 照样可以上传下载,只是控制台会对以"/"结尾的Object以文件夹的方式展示,用户可以利用这种方式来实现文件夹模拟功能,创建形式上的文件夹
		 */
		byte[] buffer = new byte[0];
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		objectMeta.setContentLength(0);
		try {
			client.putObject(bucketName, objectName, in, objectMeta);
		} catch (Exception e) {
			// log.error("创建文件夹失败," + e.getMessage());
			throw new Exception(e);
		} finally {
			in.close();
		}
	}

	// 删除文件夹
	public static void deleteFolder(String bucketName, String folderName) throws OSSException,
			ClientException {
		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

		// 递归列出fun目录下的所有文件
		listObjectsRequest.setPrefix(folderName + "/");

		ObjectListing listing = client.listObjects(listObjectsRequest);

		// 遍历所有Object,删除
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			client.deleteObject(bucketName, objectSummary.getKey());
		}

		// 遍历所有CommonPrefix
		for (String commonPrefix : listing.getCommonPrefixes()) {
			client.deleteObject(bucketName, commonPrefix);
		}
	}

	/**
	 * 上传本地文件到阿里云
	 * 
	 * @function: uploadFile
	 * @param bucketName
	 *            阿里云存储实例
	 * @param key
	 *            阿里云上的命名，路径+名字
	 * @param filename
	 *            文件本地路径
	 * @param type
	 *            类型，0-图片，1-音频，2其他
	 * @throws OSSException
	 * @throws ClientException
	 * @throws FileNotFoundException
	 * @return void
	 */
	public static void uploadFile(String bucketName, String key, String filename, int type)
			throws OSSException, ClientException, FileNotFoundException {
		File file = new File(filename);

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		switch (type) {
		case 0: {// 图片
			objectMeta.setContentType("image/jpeg");
			break;
		}
		case 1: {// 音频
			objectMeta.setContentType("Audio/Video");
			break;
		}
		case 2: {
			// objectMeta.setContentType("image/jpeg");
			break;
		}
		default:
			break;
		}

		InputStream input = new FileInputStream(file);
		client.putObject(bucketName, key, input, objectMeta);
	}

	/**
	 * 上传文件输出流到阿里云
	 * 
	 * @function: uploadFile
	 * @param bucketName
	 *            阿里云存储实例
	 * @param key
	 *            阿里云上的命名，路径+名字
	 * @param out
	 *            文件输出流
	 * @param type
	 *            类型，0-图片，1-音频，2其他
	 * @throws OSSException
	 * @throws ClientException
	 * @throws FileNotFoundException
	 * @return void
	 */
	public static void uploadFile(String bucketName, String key, OutputStream out, int type)
			throws OSSException, ClientException, FileNotFoundException {

		ByteArrayInputStream input = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());

		ObjectMetadata objectMeta = new ObjectMetadata();

		objectMeta.setContentLength(input.available());// 设置大小，必须

		switch (type) {
		case 0: {// 图片
			objectMeta.setContentType("image/jpeg");
			break;
		}
		case 1: {// 音频
			objectMeta.setContentType("Audio/Video");
			break;
		}
		case 2: {
			// objectMeta.setContentType("image/jpeg");
			break;
		}
		default:
			break;
		}

		client.putObject(bucketName, key, input, objectMeta);
	}

	// 下载文件
	public static void downloadFile(String bucketName, String key, String filename) throws OSSException,
			ClientException {
		client.getObject(new GetObjectRequest(bucketName, key), new File(filename));
	}

	// 删除文件
	public static void deleteFile(String bucketName, String key) throws OSSException, ClientException {
		client.deleteObject(bucketName, key);
	}
}
