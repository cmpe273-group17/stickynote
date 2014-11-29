package com.sjsu.edu.cmpe273.util;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * 
 * @author Avadhut Thakar 
 * This file contains static method to get MongoDB connection
 * 
 */
public class DbUtilities {
	// TODO - take this from configuration file
	private static String mongoURIString = "localhost";
	private static int port = 27017;
	private static String MongoDB = "project";

	private DbUtilities() {
		// super();
	}

	/**
	 * This method will provide DB connection to mongoDB.
	 * 
	 * @return DB object
	 * @throws Exception
	 */
	//TODO : discuss exception handling
	public static DB getDBConnection() throws Exception {
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(mongoURIString, port);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		final DB blogDatabase = mongoClient.getDB(MongoDB);
		return blogDatabase;
	}

}
