package com.sjsu.edu.cmpe273.mail.agent;

public interface MailAgent {

	public boolean sendMail(String name, String mailID, String subject,
			String msg);

}
