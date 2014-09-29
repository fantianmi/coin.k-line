package com.bkl.Kcoin.email;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;

import com.bkl.Kcoin.utils.EmailUtil;

public enum SystemEmailPool {
	INSTANCE;

	private static BlockingQueue<Message> email_pool = new LinkedBlockingDeque<Message>();

	static {
		Thread thread = new Thread(new EmailSender());
		thread.start();
	}

	public static void add(Message emailMsg) {
		email_pool.add(emailMsg);
	}

	public static Message take() {
		try {
			return email_pool.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	static class EmailSender implements Runnable {

		@Override
		public void run() {
			Transport trans = null;
			try {
				trans = EmailUtil.MAIL_SESSION.getTransport("smtp");
			} catch (NoSuchProviderException e1) {
				e1.printStackTrace();
			}
			while (true) {
				final Message emailMsg = SystemEmailPool.take();
				try {
					if(!trans.isConnected()) {
						trans.connect(EmailUtil.EMAIL_HOST, EmailUtil.EMAIL_PORT, EmailUtil.EMAIL_USER, EmailUtil.EMAIL_PASSWORD);
					}
					trans.sendMessage(emailMsg, emailMsg.getAllRecipients());					
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
