package com.sjsu.edu.cmpe273.mail.agent;

public interface MailAgent {

	public boolean sendRegistrationMail(String name, String mailID, String subject,
			String msg);
	public boolean sendNotesMail(String name, String mailID, String subject,
			String msg);

}
