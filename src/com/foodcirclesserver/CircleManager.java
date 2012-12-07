package com.foodcirclesserver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


public class CircleManager {
	
	public static final String TYPE = "cirle";
	public static final String CIRCLE_NAME = "circle_name";
	public static final String USER_ID = "user_id";
	public static final String USER_STATUS = "status"; //specific status for circle
	
	public static final String ALL_FRIENDS_CIRCLE = "All";
	
	public static void addUserToCircle(String userID, String circleName, DatastoreService ds) {
		if (userID == null || userID.length() <= 0 || circleName == null || circleName.length() <= 0)
			return;
		
		//check to see if already exists
		//add user filter
		Query q = new Query(TYPE).addFilter(USER_ID, Query.FilterOperator.EQUAL, userID);
		
		//add circle filter
		q.addFilter(CIRCLE_NAME, Query.FilterOperator.EQUAL, circleName);
		
		PreparedQuery pq = ds.prepare(q);
		
		Entity result;
		
		try {
			result = pq.asSingleEntity();
		} catch (PreparedQuery.TooManyResultsException e) {
			//error - more than one in server!
			return;
		}
		
		if (result == null) {
			//not found, put in datastore
			Entity e = new Entity(TYPE);
			e.setProperty(CIRCLE_NAME, circleName);
			e.setProperty(USER_ID, userID);
			e.setProperty(USER_STATUS, UserManager.RED); //set default to red?
			ds.put(e);
			
		}
	}
	
	public static void setStatusForCircle(String userID, Integer status, String circleName, DatastoreService ds) {
		//add user filter
		Query q = new Query(TYPE).addFilter(USER_ID, Query.FilterOperator.EQUAL, userID);
		
		//add circle filter
		q.addFilter(CIRCLE_NAME, Query.FilterOperator.EQUAL, circleName);
		
		PreparedQuery pq = ds.prepare(q);
		
		Entity result;
		
		try {
			result = pq.asSingleEntity();
		} catch (PreparedQuery.TooManyResultsException e) {
			//error - more than one in server!
			return;
		}
		
		result.setProperty(USER_STATUS, status);
		ds.put(result);
	}
	
	public static List<Circle> getCirclesByUser(String userID,  DatastoreService ds) {
		List<String> circleNames = getCircleNames(userID, ds);
		List<Circle> result = new LinkedList<Circle>();
		
		for(String circleName : circleNames) {
			Circle c = getCircle(circleName, userID, ds);
			result.add(c);
		}
		
		return result;
	}
	
	//NOTE: DOES NOT INCLUDE SPECIFIED USER IN CIRCLES
	public static Circle getCircle(String circleName, String userID,  DatastoreService ds) {
		if (circleName == null || circleName.length() <= 0)
			return null;
		
		Circle result = new Circle(circleName);
		
		Query q = new Query(TYPE).addFilter(CIRCLE_NAME, Query.FilterOperator.EQUAL, circleName);
		
		Iterator<Entity> pq = ds.prepare(q).asIterator();

		while(pq.hasNext()) {
			Entity e = pq.next();
			String user = (String) e.getProperty(USER_ID);
			if(!user.equalsIgnoreCase(userID)) { //don't add specified current user
				User u = UserManager.getUser(user, ds);
				if (u.status == UserManager.OTHER) { //if status is set to "other" get specific status
					long specStatus = (Long) e.getProperty(USER_STATUS);
					u.status = (int) specStatus;
				}
				result.users.add(u);
			}
		}
		
		return result;
	}
		
	
	public static List<String> getCircleNames(String userID,  DatastoreService ds) {
		if (userID == null || userID.length() <= 0)
			return null;
		
		List<String> result = new LinkedList<String>();
		
		Query q = new Query(TYPE).addFilter(USER_ID, Query.FilterOperator.EQUAL, userID);
		
		Iterator<Entity> pq = ds.prepare(q).asIterator();

		while(pq.hasNext()) {
			Entity e = pq.next();
			String circleName = (String) e.getProperty(CIRCLE_NAME);
			result.add(circleName);
		}
		
		return result;
		
	}

}
