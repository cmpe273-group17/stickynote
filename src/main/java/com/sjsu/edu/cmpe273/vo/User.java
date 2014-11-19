package com.sjsu.edu.cmpe273.vo;

import java.util.List;

/**
 * 
 * @author Avadhut Thakar
 * 
 * This class contains information related to User.
 */
public class User {
	/**
	 * Constructor
	 */
	public User() {
		super();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Document> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<Document> documentList) {
		this.documentList = documentList;
	}

	public boolean isAlerts() {
		return alerts;
	}

	public void setAlerts(boolean alerts) {
		this.alerts = alerts;
	}

	private String user;
	private String name;
	private String password;
	private String email;
	private List<Document> documentList;
	private boolean alerts;

}
