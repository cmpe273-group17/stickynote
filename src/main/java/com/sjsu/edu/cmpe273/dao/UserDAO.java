package com.sjsu.edu.cmpe273.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sjsu.edu.cmpe273.util.DbUtilities;
import com.sjsu.edu.cmpe273.vo.Document;
import com.sjsu.edu.cmpe273.vo.User;

/**
 * 
 * @author Avadhut Thakar
 * 
 *         This class provides method for - 1. Store user related data into
 *         database 2. Validate the user login against this data
 */

public class UserDAO {
	private final DBCollection usersCollection;
	private static UserDAO user = null;

	private UserDAO(final DB userDatabase) {
		usersCollection = userDatabase.getCollection("users");
	}

	/**
	 * This method adds user into the DB. The user object will be passed as
	 * input to this. And depending on the values set in the user object it will
	 * set the values in DB
	 * 
	 * @param user
	 * @return
	 */
	public boolean addUser(User user) {
		BasicDBObject doc = null;
		doc = new BasicDBObject("_id", user.getUser()).append("password",
				encryptedPassword(user.getPassword())).append("name",
				user.getName());
		if (user.getAuthCode() != null && user.getAuthCode() != "") {
			doc.append("authKey", base64Encode(user.getAuthCode()));
		}

		String email = user.getEmail();
		if (email != null && !email.equals("")) {
			doc.append("email", email);
		}
		doc.append("alerts", user.isAlerts() ? "Y" : "N");
		List<Document> lst = user.getDocumentList();
		BasicDBList lst1 = new BasicDBList();
		doc.append("documents", lst1);
		try {
			usersCollection.insert(doc);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method will update the authkey for user.
	 * 
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user, String authKey) {
		// Integer.toString(random.nextInt()));
		BasicDBObject query = null;
		BasicDBObject value = null;
		String encryptedAuthKey = base64Encode(authKey);
		query = new BasicDBObject().append("_id", user.getUser());
		value = new BasicDBObject().append("$set",
				new BasicDBObject().append("authKey", encryptedAuthKey));
		try {
			usersCollection.update(query, value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method validates the user id and password.
	 * 
	 * @param user
	 * @return
	 */
	public boolean validateLogin(User user) {
		DBObject userDetail = null;
		String username = user.getUser();
		String password = encryptedPassword(user.getPassword());
		BasicDBObject doc = null;
		doc = new BasicDBObject("_id", username);
		try {
			userDetail = usersCollection.findOne(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (userDetail != null) {
			String userPassword = (userDetail.get("password").toString());
			if (!userPassword.equals(password)) {
				return false;
			}
			user.setEmail(userDetail.get("email") != null ? userDetail.get(
					"password").toString() : null);
			List<Document> lst = new ArrayList<Document>();
			BasicDBList documentList = (BasicDBList) userDetail
					.get("documents");
			for (Iterator<Object> it = documentList.iterator(); it.hasNext();) {
				Document document = new Document();
				BasicDBObject dbo = (BasicDBObject) it.next();
				document.setName(dbo.get("name").toString());
				document.setCreatedAt((Date) dbo.get("createdAt"));
				document.setCreatedAt((Date) dbo.get("updatedAt"));
				lst.add(document);
			}
			user.setDocumentList(lst);
			return true;
		}
		return false;
	}

	/**
	 * Private method for encryption.
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	private String encryptedPassword(String password) {
		String passwordToHash = password;
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(passwordToHash.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * Singletone class
	 * 
	 * @return
	 * @throws Exception
	 */

	public static UserDAO getUserDAO() {
		if (user == null) {
			try {
				user = new UserDAO(DbUtilities.getDBConnection());
			} catch (Exception e) {
				System.out.println("Database is not available now."
						+ " Can not create the uesr");
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		return user;
	}

	/**
	 * The method will return the details of the user when user logs into the
	 * application.
	 * 
	 * @param user
	 */
	public User getUserDetails(String userId) {

		DBObject userDetail = null;
		User user = new User();
		BasicDBObject doc = null;
		doc = new BasicDBObject("_id", userId);
		try {
			userDetail = usersCollection.findOne(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userDetail.get("name") != null && userDetail.get("name") != "")
			user.setName(userDetail.get("name").toString());
		user.setUser(userDetail.get("_id").toString());

		user.setPassword(userDetail.get("password").toString());
		user.setEmail(userDetail.get("email") != null ? userDetail.get(
				"email").toString() : null);
		if (userDetail.get("authKey") != null) {
			user.setAuthCode(base64Decode(userDetail.get("authKey").toString())
					.toString());
			System.out.println("user.getauth key === " + user.getAuthCode());
		}
		if(userDetail.get("alerts")!=null && userDetail.get("alerts").equals("Y")){
			user.setAlerts(true);
		}else{
			user.setAlerts(false);
		}
		List<Document> lst = new ArrayList<Document>();
		BasicDBList documentList = (BasicDBList) userDetail.get("documents");
		for (Iterator<Object> it = documentList.iterator(); it.hasNext();) {
			Document document = new Document();
			BasicDBObject dbo = (BasicDBObject) it.next();
			document.setName(dbo.get("name").toString());
			document.setCreatedAt((Date) dbo.get("createdAt"));
			document.setCreatedAt((Date) dbo.get("updatedAt"));
			lst.add(document);
		}
		user.setDocumentList(lst);
		return user;
	}

	/**
	 * This method will remove the document name from the document list in the
	 * DB
	 * 
	 * @return true/false
	 */
	public boolean removeDocument(String user_id, String docName) {

		WriteResult userDetail = null;
		BasicDBObject doc = null;
		doc = new BasicDBObject("_id", user_id);
		try {
			userDetail = usersCollection.update(doc, new BasicDBObject("$pull",
					new BasicDBObject("documents", new BasicDBObject("name",
							docName))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userDetail.getN() == 1)
			return true;
		else
			return false;
	}

	/**
	 * This method will add the document name to the user document.
	 * 
	 * @return true/false
	 */
	public boolean addDocument(String user_id, String docName) {
		WriteResult userDetail = null;
		BasicDBObject doc = null;
		doc = new BasicDBObject("_id", user_id);
		try {
			userDetail = usersCollection.update(
					doc,
					new BasicDBObject("$push", new BasicDBObject("documents",
							new BasicDBObject("name", docName).append(
									"createdAt", new Date()).append(
									"updatedAt", new Date()))));
		} catch (Exception e) {

			e.printStackTrace();
		}
		if (userDetail.getN() == 1)
			return true;
		else
			return false;
	}

	private String base64Encode(String stringToEncode) {
		byte[] stringToEncodeBytes = stringToEncode.getBytes();
		return Base64.encodeBase64String(stringToEncodeBytes);
	}

	private String base64Decode(String stringToDecode) {
		byte[] decodedBytes = Base64.decodeBase64(stringToDecode);
		return new String(decodedBytes);
	}

}
