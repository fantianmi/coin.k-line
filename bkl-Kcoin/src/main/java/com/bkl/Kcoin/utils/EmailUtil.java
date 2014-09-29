package com.bkl.Kcoin.utils;

import java.sql.Timestamp;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.mail.EmailException;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.email.SystemEmailPool;
import com.bkl.Kcoin.entity.User;
import com.km.common.config.Config;

public class EmailUtil {

	private final static String CONTEXT_PATH = Config.getString("config.context.path");

	public final static String ACTIVE_SUBJECT = Config.getString("mail.active.subject");
	public final static String FORGET_PASSWORD_SUBJECT = Config.getString("mail.forget.password.subject");
	public final static String EMAIL = Config.getString("system.email");
	public final static String EMAIL_USER = Config.getString("system.email.user");
	public final static String EMAIL_PASSWORD = Config.getString("system.email.password");
	public final static String EMAIL_HOST = Config.getString("system.email.host");
	public final static int EMAIL_PORT = Config.getInt("system.email.port");
	public final static boolean EMAIL_SSL_ENABLED = Config.getBoolean("system.email.ssl.enabled");

	public static Session MAIL_SESSION = null;
	
	static {
		Properties props = new Properties();
		props.put("mail.smtp.auth","true");  
		props.put("mail.smtp.ssl.enable", EMAIL_SSL_ENABLED + "");
		props.put("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		MAIL_SESSION = Session.getDefaultInstance(props);
		MAIL_SESSION.setDebug(true);
	}
	
	public static Message getActiveMailMsg(User user) {
		Message mailMessage = new MimeMessage(MAIL_SESSION);

		String url = CONTEXT_PATH + "/open/activeUser?email=" + user.getEmail();
		String content = "<p>请点击如下链接进行激活:<p><a href='" + url + "' target='_blank'>" + url + "</a>";
		try {
			mailMessage.setFrom(new InternetAddress(EMAIL));
			mailMessage.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
			mailMessage.setSubject(ACTIVE_SUBJECT);
			mailMessage.setContent(content, "text/html; charset=UTF-8");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return mailMessage;
	}
	
	public static Message getResetPasswordMailMsg(User user) {
		Message mailMessage = new MimeMessage(MAIL_SESSION);
		
		// 15分钟后过期
		Timestamp overTime = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000);
		String key = user.getEmail() + "$$" + (overTime.getTime() / 1000);
		String url = CONTEXT_PATH + "/doResetPassword.jsp?uid=" + user.getId() + "&token=" + DigestUtils.sha512Hex(key);
		String content = "<p>请点击如下链接进行修改密码:<p><a href='" + url + "' target='_blank'>" + url + "</a>";
		try {
			mailMessage.setFrom(new InternetAddress(EMAIL));
			mailMessage.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
			mailMessage.setSubject(FORGET_PASSWORD_SUBJECT);
			mailMessage.setContent(content, "text/html; charset=UTF-8");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return mailMessage;
	}
	
	public static void sendActiveMail(User user) throws EmailException {
		if (!CoinConfig.enableEmailActive()) {
			return;
		}
		SystemEmailPool.add(getActiveMailMsg(user));
	}

	public static void sendResetPasswordMail(User user) throws EmailException {
		if (!CoinConfig.enableEmailActive()) {
			return;
		}
		SystemEmailPool.add(getResetPasswordMailMsg(user));
	}

	public static void main(String[] args) throws EmailException {
		User u = new User();
		u.setEmail("214368971@qq.com");

		final User tu = u;

		for (int i = 0; i < 1; i++) {
			EmailUtil.sendResetPasswordMail(tu);
		}
	}
}
