package com.sjsu.sticky.mail.agent;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sjsu.edu.cmpe273.mail.agent.GoogleMailAgent;
import com.sjsu.edu.cmpe273.mail.agent.MailAgent;

public class GoogleMailAgentTest {

	@Test
	public void testSendMail() {
		MailAgent mailAgent = new GoogleMailAgent();
		assertTrue(mailAgent.sendMail("Sushanta", "sush.47@gmail.com", "You have created a new note", "Here is the direct link to your new note."));
		
	}

}
