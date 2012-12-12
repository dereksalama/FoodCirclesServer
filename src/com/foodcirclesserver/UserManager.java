/* User utility
 * 
 * -methods are still evolving so I'm too lazy to go through them right now
 * 
 * -Derek Salama
 * 
 */
package com.foodcirclesserver;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class UserManager {
	
	public static final String USER = "user";
	public static final String USER_ID = "user_id";
	public static final String NAME = "name";
	public static final String STATUS = "status";
//	public static final String CURRENT_TRAIN = "train";
	public static final String DESIRED_TIME = "time";
	public static final String DESIRED_LOCATION = "location";
	
	//user statuses
	public static final Integer GREEN = 0;
	public static final Integer YELLOW = 1;
	public static final Integer RED = 2;
	public static final Integer OTHER = 3;
	
	public static void updateLocationAndTime(String userID, String time, String location, DatastoreService ds) {
		
		if (userID == null || userID.length() <= 0) {
			System.out.println("updateStatus error: null  or 0 value");
			return;
		}
		
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
		
		user.setProperty(DESIRED_TIME, time);
		user.setProperty(DESIRED_LOCATION, location);
		ds.put(user);
	}
	
	public static void createUser(String userID, String name, DatastoreService ds) {
		
		if (userID == null || userID.length() <= 0) {
			System.out.println("createUser error: null  or 0 value");
			return;
		}
			
		
		//check if user is already in db
		Key userKey = KeyFactory.createKey(USER, userID);
		
		Entity user;

		try {
			user = ds.get(userKey);
			//user in ds, do nothing
		} catch (EntityNotFoundException e) {
			//user not in ds
			user = new Entity(USER, userID);
			user.setProperty(USER_ID, userID);
			user.setProperty(NAME, name);
			user.setProperty(STATUS, RED);
			ds.put(user);
			
		}
		
	}
	
	//status set for given circle
	public static User getUserForCircle(String userID, Long circleID, DatastoreService ds) {
		User u = getUser(userID, ds);
		if ( u == null ) {
			return null;
		} else if ( u.status != UserManager.OTHER) {
			return u;
		} else {
			Query q = new Query(CircleManager.TYPE).addFilter(CircleManager.CIRCLE_ID, 
					Query.FilterOperator.EQUAL, circleID);
			q.addFilter(CircleManager.USER_ID, Query.FilterOperator.EQUAL, userID);
			Entity e = ds.prepare(q).asSingleEntity();
			Long status = (Long) e.getProperty(CircleManager.USER_STATUS);
			u.status = status.intValue();
			if (u.status == UserManager.RED) {
				u.desiredLocation = null;
				u.desiredTime = null;
			}
			
			return u;
		}
	}
	
	public static User getUser(String userID,  DatastoreService ds) {
		if (userID == null)
			return null;
		
		Entity user = getUserAsEntity(userID, ds);
		if (user == null)
			return null;
		
		String name = (String) user.getProperty(NAME);
		Long status = (Long) user.getProperty(STATUS);
		String time = (String) user.getProperty(DESIRED_TIME);
		String loc = (String) user.getProperty(DESIRED_LOCATION);
		
		User userResult = new User(userID, name, status.intValue(), time, loc);
		
		return userResult;
	}
	
	public static Entity getUserAsEntity(String userID,  DatastoreService ds) {
		
		Key userKey = KeyFactory.createKey(USER, userID);
		
		Entity user;

		try {
			user = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			//user not in ds
//			System.out.println("getUser error: user " + userID + " does not exist");
			return null;
			
		}
		
		return user;
		
	}
	
	public static void updateStatus(String userID, Integer status,  DatastoreService ds) {
		
		if (userID == null || userID.length() <= 0 || status == null) {
			System.out.println("updateStatus error: null  or 0 value");
			return;
		}
		
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
		if (status == UserManager.RED) { //erase time and loc if going red
			user.setProperty(DESIRED_LOCATION, null);
			user.setProperty(DESIRED_TIME, null);
		}
		ds.put(user);
	}
	
/*	public static void updateTrain(String userID, Long train,  DatastoreService ds) {
		
		if (userID == null || userID.length() <= 0 || train == null ) {
			System.out.println("updatetrain error: null  or 0 value");
			return;
		}
		
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
		ds.put(user);
	}*/
	

}
