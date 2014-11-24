package com.sjsu.edu.cmpe273.mail.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class GoogleMailAgent implements MailAgent {

	private static final String SENDER_MAIL_ID = "cmpe273.17@gmail.com";
	private static final String SENDER_PASSWORD = "sithuaung";
	VelocityEngine velocityEngine;

	@Override
	public boolean sendMail(String name, String mailID, String subject,
			String msg) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "*");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(SENDER_MAIL_ID,
								SENDER_PASSWORD);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(SENDER_MAIL_ID));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mailID));
			message.setSubject(subject);

			Map<String, Object> model = new HashMap<>();
			model.put("name", name);
			model.put("email", SENDER_MAIL_ID);
			model.put("message", msg);
			velocityEngine = new VelocityEngine();
			velocityEngine.setProperty("resource.loader", "class");
			velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "templates/NewNoteNotificationMessage.vm",
					"UTF-8", model);

			message.setText(text);
			Transport.send(message);

			return true;

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

}
