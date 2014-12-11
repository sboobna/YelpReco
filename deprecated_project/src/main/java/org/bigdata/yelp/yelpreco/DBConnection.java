package org.bigdata.yelp.yelpreco;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import org.bigdata.yelp.util.*;


public class DBConnection {
	
	private String DB_END_POINT;
	private final String DB_USER_NAME;
	private final String DB_PWD;
	private final String DB_NAME;
	private final String DB_PORT;
	
	public DBConnection() throws ClassNotFoundException, IOException {
		Properties prop = Property.getPropInstance();
		DB_END_POINT = prop.getProperty("DB_END_POINT");
		DB_USER_NAME = prop.getProperty("DB_USER_NAME");
		DB_PWD = prop.getProperty("DB_PWD");
		DB_NAME = prop.getProperty("DB_NAME");
		DB_PORT = prop.getProperty("DB_PORT");
	}
	
	// this method creates a connection with RDS
	public Connection createConnection() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Setup the connection with the DB
			return DriverManager.getConnection("jdbc:mysql://"
					+ DB_END_POINT + ":" + DB_PORT + "/" + DB_NAME,
					DB_USER_NAME, DB_PWD);
			
			// Statements allow to issue SQL queries to the database
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
