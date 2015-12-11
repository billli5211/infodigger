package com.bill.crawler.utility;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import com.bill.crawler.global.SystemAccount;
import com.google.common.collect.Maps;

/*
 * use libs: 
 * commons-email.jar
 * javax.mail.jar
 * 
 * 
 * 
 * reference:
 * http://haolloyin.blog.51cto.com/1177454/354320/  
 */
public class EmailUtil {

	public static boolean sendEmail(String email, String userName, String subject, String content) {
		SimpleEmail emailUtil = new SimpleEmail();
		emailUtil.setCharset("utf-8");
		emailUtil.setHostName("smtp.163.com");
		try {
			emailUtil.addTo(email, userName);
			emailUtil.setAuthenticator(new DefaultAuthenticator(SystemAccount.MailAccount163,
					SystemAccount.MailPassword163));// 密码需要邮箱里设置pop等信息。
			emailUtil.setFrom(SystemAccount.MailAccount163, "your new friend");
			emailUtil.setSubject(subject);
			emailUtil.setMsg(content);
			emailUtil.send();
			return true;
		} catch (Exception e) {
			LogUtil.logger.error(e.toString());
			return false;
		}
	}

	public static void main(String args[]) {
		boolean result = sendEmail("shilongdred@163.com", "bill", "hi", "just say hi");
		if (result) {
			LogUtil.logger.info("successfully send out email");
		}

		EmailTest.testSendSimpleEmail();
	}
}

/*
 * bill: pop3用以接收邮件，smtp发送邮件。需要不同的收发服务器。 在登陆qq邮箱后，在设置/Pop3,
 * Smtp设置中，需要开启SMTP、POP3服务，以使得第三方工具可以收发邮件。
 */
class Email {
	// 邮箱服务器的登录用户名
	private static String username = SystemAccount.MailAccountQQ;
	// 邮箱服务器的密码
	private static String password = SystemAccount.MailPasswordQQ;
	// 邮箱服务器smtp host,此处采用自己的QQ邮箱作为邮件服务器
	private static String smtpHost = "smtp.qq.com";
	// 发送方的邮箱（必须为邮箱服务器的登录用户名）
	private static String fromEmail = SystemAccount.MailAccountQQ;
	// 发送方姓名
	private static String fromUsername = "your new friend";
	// 邮件内容编码，防止乱码
	private static String charset = "UTF-8";

	/**
	 * java发送邮件-commons-email
	 * 
	 * @param nameAndAddrMap
	 *            用户名及对应的邮箱组成的Map
	 * @param subject
	 *            邮件主题或标题
	 * @param htmlContent
	 *            邮件内容html格式
	 * @throws EmailException
	 * @throws MessagingException
	 * @author chenleixing
	 */
	public static void sendEmail(Map<String, String> nameAndAddrMap, String subject, String htmlContent)
			throws EmailException, MessagingException {
		// SimpleEmail email = new SimpleEmail();//创建简单邮件,不可附件、HTML文本等
		// MultiPartEmail email = new MultiPartEmail();//创建能加附件的邮件,可多个、网络附件亦可
		/*
		 * ImageHtmlEmail:HTML文本的邮件、通过2代码转转内联图片, 1.3新增的，但我怎么也测试不成功
		 * ImageHtmlEmail网上都是官网上例子而官网上例子比较模糊 ImageHtmlEmail email=new
		 * ImageHtmlEmail();
		 */
		HtmlEmail email = new HtmlEmail();// 创建能加附件内容为HTML文本的邮件、HTML直接内联图片但必须用setHtmlMsg()传邮件内容

		// email.setDebug(true);//是否开启调试默认不开启
		email.setSSLOnConnect(true);// 开启SSL加密
		email.setStartTLSEnabled(true);// 开启TLS加密

		// 设置smtp host,此处采用自己的QQ邮箱作为邮件服务器
		email.setHostName(Email.smtpHost);
		// 登录邮件服务器的用户名和密码（保证邮件服务器POP3/SMTP服务开启）
		email.setAuthentication(Email.username, Email.password);

		email.setFrom(Email.fromEmail, Email.fromUsername);// 发送方
		for (Map.Entry<String, String> map : nameAndAddrMap.entrySet()) {// 遍历用户名及对应的邮箱地址组成的map
			email.addTo(map.getValue(), map.getKey());// 接收方邮箱和用户名
		}
		// email.addCc("chen_leixing@qq.com");//抄送方
		// email.addBcc("leixing_chen@120.com");//秘密抄送方

		email.setCharset(Email.charset);// 设置邮件的字符集为UTF-8防止乱码
		email.setSubject(subject);// 主题
		email.setHtmlMsg(htmlContent);// 邮件内容:<font
										// color='red'>测试简单邮件发送功能！</font>

		/*
		 * HtmlEmail、ImageHtmlEmail有setHtmlMsg()方法，且可以直接内联图片,可网上都搞那么复杂说不行如<img
		 * src='http://www.apache.org/images/asf_logo_wide.gif'
		 * />本人测试新浪、搜狐、QQ邮箱等都能显示
		 */
		/*
		 * //如果使用setMsg()传邮件内容，则HtmlEmail内嵌图片的方法 URL url = new
		 * URL("http://www.jianlimuban.com/图片"); String cid = email.embed(url,
		 * "名字"); email.setHtmlMsg("<img src='cid:"+cid+"' />");
		 */

		// 这是ImageHtmlEmail的内嵌图片方法，我多次测试都不行，官网提供比较模糊，而大家都是用官网举的例子
		// 内嵌图片,此处会抛出MessagingException, MalformedURLException异常
		// URL url=new URL("http://www.apache.org");//定义你基本URL来解决相对资源的位置
		// email.setDataSourceResolver(new
		// DataSourceUrlResolver(url));//这样HTML内容里如果有此路径下的图片会直接内联

		// 创建邮件附件可多个
		/*
		 * EmailAttachment attachment = new EmailAttachment();//创建附件
		 * attachment.setPath("D:\\MongoDB.txt");//本地附件，绝对路径
		 * //attachment.setURL(new
		 * URL("http://www.baidu.com/moumou附件"));//可添加网络上的附件
		 * attachment.setDisposition(EmailAttachment.ATTACHMENT);
		 * attachment.setDescription("MongoDB入门精通");//附件描述
		 * attachment.setName("NoSQL数据库");//附件名称
		 * email.attach(attachment);//添加附件到邮件,可添加多个
		 * email.attach(attachment);//添加附件到邮件,可添加多个
		 */

		/*
		 * email.buildMimeMessage();//构建内容类型 ，
		 * //设置内容的字符集为UTF-8,先buildMimeMessage才能设置内容文本 ,但不能发送HTML格式的文本
		 * email.getMimeMessage
		 * ().setText("<font color='red'>测试简单邮件发送功能！</font>","UTF-8");
		 */

		email.send();// 发送邮件
	}
}

class EmailTest {

	public static void testSendSimpleEmail() {

		// 用户名和对应的邮箱
		Map<String, String> nameAndAddrMap = Maps.newHashMap();// 新建一个map
		nameAndAddrMap.put("dred", "shilongdred@163.com");

		String htmlContent = "<font color='red'>测试简单邮件发送功能！</font>";// 邮件内容
		String subject = "你好！";// 主题或者标题
		try {
			Email.sendEmail(nameAndAddrMap, subject, htmlContent);// 测试发送邮件功能
		} catch (Exception e) {
			LogUtil.logger.info("邮件发送失败！");
			// logger.setMessage(e.getMessage());
		}
	}
}
