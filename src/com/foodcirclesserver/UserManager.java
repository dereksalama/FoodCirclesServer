/* User utility
 * 
 * createUser(userID, accessToken) - creates new user or updates token if changed
 * getUser(userID) - fetch user based on ID
 * updateStatus(userID, status) - set new status field
 * updateTrain(userID, train) - set new train field
 * 
 * -Derek Salama
 * 
 */
package com.foodcirclesserver;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserManager {
	
	public static final String USER = "user";
	public static final String USER_ID = "user_id";
	public static final String NAME = "name";
	public static final String ACCESS_TOKEN = "access";
	public static final String STATUS = "status";
	public static final String CURRENT_TRAIN = "train";
	
	/* create new user or update existing accessToken */
	public static void createUser(Integer userID, String name, String accessToken) {
		
		if (userID == null || userID <= 0 || accessToken == null || accessToken.length() <= 0) {
			System.out.println("createUser error: null  or 0 value");
			return;
		}
			
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//check if user is already in db
		Key userKey = KeyFactory.createKey(USER, userID);
		
		Entity user;

		try {
			user = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			//user not in ds
			user = new Entity(USER, userID);
			user.setProperty(USER_ID, userID);
			user.setProperty(NAME, name);
			user.setProperty(ACCESS_TOKEN, accessToken);
			
		}
		
		//update accesstoken if different from previous value
		//redundant if user is new...
		if(!((String)user.getProperty(ACCESS_TOKEN)).equalsIgnoreCase(accessToken))
			user.setUnindexedProperty(ACCESS_TOKEN, accessToken);
		
		
	}
	
	public static User getUser(String userID) {
		if (userID == null)
			return null;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Key userKey = KeyFactory.createKey(USER, userID);
		
		Entity user;

		try {
			user = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			//user not in ds
			System.out.println("getUser error: user " + userID + " does not exist");
			return null;
			
		}
		
		String name = (String) user.getProperty(NAME);
		String accessToken = (String) user.getProperty(ACCESS_TOKEN);
		String status = (String) user.getProperty(STATUS);
		String currentTrain = (String) user.getProperty(CURRENT_TRAIN);
		
		User userResult = new User(userID, name, accessToken, status, currentTrain);
		
		return userResult;
	}
	
	public static void updateStatus(Integer userID, String status) {
		
		if (userID == null || userID <= 0 || status == null || status.length() <= 0) {
			System.out.println("updateStatus error: null  or 0 value");
			return;
		}
			
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//check if user is already in db
		Key userKey = KeyFactory.createKey(USER, userID);
		
		Entity user;

		try {
			user = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			//user not in ds
			System.out.println("updateStatus error: user " + userID + " does not exist.");
			return;
		}
		
		user.setProperty(STATUS, status);
	}
	
	public static void updateTrain(Integer userID, String train) {
		
		if (userID == null || userID <= 0 || train == null || train.length() <= 0) {
			System.out.println("updatetrain error: null  or 0 value");
			return;
		}
			
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//check if user is already in db
		Key userKey = KeyFactory.createKey(USER, userID);
		
		Entity user;

		try {
			user = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			//user not in ds
			System.out.println("updatetrain error: user " + userID + " does not exist.");
			return;
		}
		
		user.setProperty(CURRENT_TRAIN, train);
	}
	

}
