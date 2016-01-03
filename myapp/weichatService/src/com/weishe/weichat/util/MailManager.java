package com.weishe.weichat.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;

public class MailManager {

	private MailSender mailSender;

	private String textTemplate;// 要发送的文本信息

	private static final String EMAIL_FROM = "1528568708@qq.com";

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTextTemplate(String textTemplate) {
		this.textTemplate = textTemplate;
	}

	/**
	 * 发�?纯文本的用户修改通知邮件.
	 */
	public void sendNotificationMail(String subject, String content,
			String toEmail) {
		SimpleMailMessage msg = new SimpleMailMessage();// SimpleMailMessage只能用来发�?text文本
		msg.setFrom(EMAIL_FROM);// 此处用QQ测试必须和xml中发送端保持的一致否则报�?但是网上说发送�?,这里还可以另起Email别名，不用和xml里的username�?��？？)
		msg.setTo(toEmail);// 接受�?
		msg.setSubject(subject);// 主题
		msg.setText(content);// 正文内容
		try {
			mailSender.send(msg);// 发�?邮件
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
