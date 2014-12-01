package org.bigdata.yelp.yelpreco;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;


public class App 
{
	static DBUtil dbutil;
	
    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        if(args.length < 1) {
        	System.err.println("Please provide the user id");
        	System.exit(1);
        }
        String userId = args[0];
        dbutil = new DBUtil();
    	HashMap<String, Double> recommendationPower = makeRecoPower(userId);
    	HashMap<String, Double> predictions = predictRating(userId, recommendationPower);
    	for(Entry e: predictions.entrySet()) {
    		System.out.println("User " + userId + " predicted rating for business " + e.getKey() + " is " + e.getValue());
    	}
    }
    
    public static HashMap<String, Double> predictRating(String userId, HashMap<String, Double> recommendationPower) throws SQLException {
    	HashMap<String, Double> predictions = new HashMap<String, Double>();
    	ArrayList<String> businesses = dbutil.getBusinesses();
		double avgUserRating = dbutil.getAvgUserRating(userId);
		ArrayList<String> users = dbutil.getUsers();
		HashMap<String, Double> avgUserRatings = new HashMap<String, Double>();
		for(String eachUser: users) {
			avgUserRatings.put(eachUser, dbutil.getAvgUserRating(eachUser));
		}
		HashSet<String> businessesRatedByUser = dbutil.getBusinessesRatedByUser(userId);
		
		for(String eachBusiness: businesses) {
			if(businessesRatedByUser.contains(eachBusiness)) {
				continue;
			}
			double prediction = avgUserRating;
			ArrayList<UserRating> userRatings = dbutil.getUserRatingsOfBusiness(eachBusiness);
			for(UserRating eachUser: userRatings) {
				double recoPower = recommendationPower.get(eachUser.getUserId());
				if(recoPower == 0.0) {
					continue;
				}
				
				prediction += recoPower*((double)eachUser.getRating() - avgUserRatings.get(eachUser.getUserId()));
			}
			
			predictions.put(eachBusiness, prediction);
		}
		return predictions;
	}

	public static HashMap<String, Double> makeRecoPower(String userId) throws ClassNotFoundException, IOException, SQLException {
    	ArrayList<BusinessRating> businessRatings = dbutil.getBusinessRatingsOfUser(userId);
    	ArrayList<String> users = dbutil.getUsers();
    	int totalRating = 0;
    	HashMap<String, Integer> totalBusinessRatings = new HashMap<String, Integer>();
    	HashMap<String, Double> recommendationPower = new HashMap<String, Double>();
    	
    	for(BusinessRating eachRating: businessRatings) {
    		int rating = eachRating.getRating();
    		totalRating += rating;
    		int totalBusinessRating = dbutil.getTotalBusinessRating(eachRating.getbusinessId());
    		totalBusinessRatings.put(eachRating.getbusinessId(), totalBusinessRating);
    	}
    	
    	for(String eachUser: users) {
    		if(eachUser.equals(userId)) {
    			continue;
    		}
    		double recoPower = 0.0;
    		
    		for(BusinessRating eachRating: businessRatings) {
    			String businessId = eachRating.getbusinessId();
    			int usersRating = dbutil.getRating(eachUser, businessId);
    			
    			recoPower += ((double)(eachRating.getRating()*usersRating)/(double)totalBusinessRatings.get(businessId));
    		}
    		recoPower /= (double)totalRating;
    		recommendationPower.put(eachUser, recoPower);
    		System.out.println("Recommendation Power from user " + userId + " to user " + eachUser + " is " + recoPower);
    	}
    	return recommendationPower;
    }
}
