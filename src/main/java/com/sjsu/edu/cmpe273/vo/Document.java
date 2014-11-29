package com.sjsu.edu.cmpe273.vo;

import java.util.Date;

/**
 * 
 * @author Avadhut Thakar
 * 
 *         This class contains information related to documents.
 */
public class Document {
	/**
	 * Constructor
	 */
	public Document() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String name;
	private Date createdAt;
	private Date updatedAt;
	private String message;
	
	/**
	 * 
	 * Author : Prakruthi Nagaraj
	 * 
	 *        To convert toString
	 */
	@Override
		 public String toString() {
			 return new StringBuffer(" name : ").append(this.name.toString())
					 .append(" message : ").append(this.message).toString();
		  }
}
