package com.sjsu.edu.cmpe273.dao;

// Include the Dropbox SDK.
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.dropbox.core.json.JsonReadException;
import com.sjsu.edu.cmpe273.util.PDFFileConversion;
import com.sjsu.edu.cmpe273.vo.Document;
import com.sjsu.edu.cmpe273.vo.User;

/**
 * 
 * This class provides method to store and remove the files from Dropbox. It
 * will require the authentication key as input and it will convert message
 * typed by user to DropBox file
 * 
 * @author Avadhut Thakar
 * 
 */
public class DropBoxDAO {

	DbxClient client = null;

	/**
	 * This method will convert message into dropbox file
	 * 
	 * @param path
	 *            - Path of input file.
	 * @param message
	 *            - Message on the note
	 * @return True/False
	 */
	public boolean createNote(String accessToken, User user, Document document) {
		// File inputFile = new File("F:/Temp.txt");
		BufferedInputStream inputStream = null;
		client = getClient(accessToken);
		System.out.println("========="+client);
		if (client == null) {
			return false;
		}

		try {
			InputStream is = new ByteArrayInputStream(document.getMessage()
					.getBytes());
			inputStream = new BufferedInputStream(is);
			DbxEntry.File uploadedFile = client.uploadFile("/stickynotesapp/"
					+ document.getName(), DbxWriteMode.add(), document
					.getMessage().length(), inputStream);
			//DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return true;
	}

	/**
	 * This method will authenticate the user for dropbox against the
	 * Accesstoken
	 * 
	 * @param path
	 * @return DbxClient object
	 */
	private DbxClient getClient(String accessToken) {
		// Read auth info file.
		DbxAuthInfo authInfo;
		try {
			
			authInfo = DbxAuthInfo.Reader.readFully(accessToken);
		} catch (JsonReadException ex) {
			System.err.println("Error in reading token: " + ex.getMessage());
			return null;
		}
		String userLocale = Locale.getDefault().toString();
		DbxRequestConfig requestConfig = new DbxRequestConfig(
				"examples-account-info", userLocale);
		DbxClient client = new DbxClient(requestConfig, authInfo.accessToken,
				authInfo.host);
		return client;
	}

	/**
	 * This method will convert the file into PDF file.
	 * 
	 * @param user
	 */
	public void createPDF(String user, Document document) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
		// outputStream);
		File file = new File(user);
		if (!file.isDirectory()) {
			file.mkdir();
		}
		try {
			client.getFile("/stickynoteapp/"+document.getName(), null, outputStream);
			byte[] byteArray = outputStream.toByteArray();
			String output = new String(byteArray);
			PDFFileConversion pdfa = new PDFFileConversion();
			pdfa.createPDFFile(user + "/stickynoteapp/"+user+"/"+document.getName(), output);
			outputStream.close();

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	/**
	 * This method will remove the file for user whose access token is passed.
	 * 
	 * @param fileName
	 * @param user
	 * @param accessToken
	 */
	public boolean removeNote(String accessToken, User user, Document document) {

		client = getClient(accessToken);
		if (client == null) {
			return false;
		}
		try {
			client.delete("/stickynotesapp/" + document.getName());
			// TODO call the code for updating mongoDB
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param accessToken
	 * @param user
	 * @param document
	 * @return
	 */
	public boolean removeAllNote(String accessToken, User user) {

		int fileSize = 0;
		for (; fileSize < user.getDocumentList().size(); fileSize++) {
			removeNote(accessToken, user, user.getDocumentList().get(fileSize));
		}

		return true;
	}

	/**
	 * This method will read all the files from Dropbox and set the message into
	 * each document
	 * 
	 * @param accessToken
	 * @param user
	 */
	public void getAllFiles(String accessToken, User user) {
		// Check the size
		if (user.getDocumentList().size() != 0) {
			client = getClient(accessToken);
			int fileSize = 0;
			for (; fileSize < user.getDocumentList().size(); fileSize++) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				try {
					client.getFile("/stickynotesapp/"
							+ user.getDocumentList().get(fileSize).getName(),
							null, outputStream);
					byte[] byteArray = outputStream.toByteArray();
					String output = new String(byteArray);
					user.getDocumentList().get(fileSize).setMessage(output);
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * This method will be update the file in the dropbox.
	 * 
	 * @param accessToken
	 * @param user
	 */
	public void updateFile(String accessToken, User user, Document document) {
		// Check the size
		removeNote(accessToken, user, document);
		createNote(accessToken, user, document);
	}

}
