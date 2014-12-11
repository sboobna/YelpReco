package org.bigdata.yelp.yelpreco;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

public class DBUtil {

	Connection connection;
	Statement statement;
	ResultSet result;
	
	public DBUtil() throws ClassNotFoundException, IOException {
		DBConnection conn = new DBConnection();
        connection = conn.createConnection();
        if(connection == null){
        	System.out.println("Could not connect to the database!");
        	System.exit(1);
        }
	}
	
	public ArrayList<BusinessRating> getBusinessRatingsOfUser(String userId) throws SQLException {
		
		ArrayList<BusinessRating> businessRatings = new ArrayList<BusinessRating>();
		
		String query = "SELECT businessId, rating from UserBusinessMapping where userId='" + userId + "';";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		while(result.next()) {
			String businessId = result.getString("businessId");
			int rating = result.getInt("rating");
			BusinessRating businessRating = new BusinessRating(businessId, rating);
			businessRatings.add(businessRating);
		}
		close();
		return businessRatings;
	}

	public ArrayList<String> getUsers() throws SQLException {
		ArrayList<String> users = new ArrayList<String>();
		
		String query = "SELECT userId from User;";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		while(result.next()) {
			String userId = result.getString("userId");
			users.add(userId);
		}
		close();
		return users;
	}
	
	public ArrayList<String> getBusinesses() throws SQLException {
		ArrayList<String> businesses = new ArrayList<String>();
		
		String query = "SELECT businessId from Business;";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		while(result.next()) {
			String businessId = result.getString("businessId");
			businesses.add(businessId);
		}
		close();
		return businesses;
	}

	public int getRating(String userId, String businessId) throws SQLException {
		String query = "SELECT rating from UserBusinessMapping where userId='" + userId + "' and businessId='" + businessId + "';";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		int rating = 0;
		if(result.next()) {
			rating = result.getInt("rating");
		}
		close();
		return rating;
	}

	public int getTotalBusinessRating(String businessId) throws SQLException {
		String query = "SELECT SUM(rating) as totalRating from UserBusinessMapping where businessId='" + businessId + "';";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		int totalRating = 0;
		if(result.next()) {
			totalRating = result.getInt("totalRating");
		}
		close();
		return totalRating;
	}

	public double getAvgUserRating(String userId) throws SQLException {
		String query = "SELECT AVG(rating) as avgRating from UserBusinessMapping where userId='" + userId + "';";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		double avgRating = 0.0;
		if(result.next()) {
			avgRating = result.getDouble("avgRating");
		}
		close();
		return avgRating;
	}

	public HashSet<String> getBusinessesRatedByUser(String userId) throws SQLException {
		String query = "SELECT businessId from UserBusinessMapping where userId='" + userId + "';";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		HashSet<String> businessesRatedByUser = new HashSet<String>();
		while(result.next()) {
			businessesRatedByUser.add(result.getString("businessId"));
		}
		return businessesRatedByUser;
	}

	public ArrayList<UserRating> getUserRatingsOfBusiness(String businessId) throws SQLException {
		
		ArrayList<UserRating> userRatings = new ArrayList<UserRating>();
		
		String query = "SELECT userId, rating from UserBusinessMapping where businessId='" + businessId + "';";
		statement = connection.createStatement();
		result = statement.executeQuery(query);
		while(result.next()) {
			String userId = result.getString("userId");
			int rating = result.getInt("rating");
			UserRating userRating = new UserRating(userId, rating);
			userRatings.add(userRating);
		}
		close();
		return userRatings;
	}
	
	private void close() {
		try {
			result.close();
			statement.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize() {
		try {
			connection.close();
		}
		catch(SQLException e) {
			System.out.println("Could not close connection!");
		}
	}
}


class BusinessRating {
	String businessId;
	int rating;
	
	public BusinessRating(String businessId, int rating) {
		this.businessId = businessId;
		this.rating = rating;
	}
	
	public String getbusinessId() {
		return businessId;
	}
	
	public int getRating() {
		return rating;
	}
}

class UserRating {
	String userId;
	int rating;
	
	public UserRating(String userId, int rating) {
		this.userId = userId;
		this.rating = rating;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public int getRating() {
		return rating;
	}
}
