package com.sjsu.edu.cmpe273.util;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DbUtilities {

	// TODO - take this from configuration file
	private static String mongoURIString = "ds045097.mongolab.com";
	private static int port = 45097;
	private static String MongoDB = "cmpe273project";

	private DbUtilities() {
		// super();
	}

	/**
	 * This method will provide DB connection to mongoDB.
	 * 
	 * @return DB object
	 * @throws Exception
	 */
	// TODO : discuss exception handling
	public static DB getDBConnection() throws Exception {
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient(mongoURIString, port);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		final DB userDB = mongoClient.getDB(MongoDB);
		userDB.authenticate("admin", "admin".toCharArray());
		return userDB;
	}

}
