/*
 * circle manager
 * -database columns:  circle_id, circle_name, user_id, status
 * -unique id for each circle
 * -each row is user/circle pair, w/ user status for specific circle
 *
 * -Derek
 */
package com.foodcirclesserver.circles;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.foodcirclesserver.user.User;
import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


public class CircleManager {

	public static final String TYPE = "circle";
	public static final String CIRCLE_ID = "circle_id";
	public static final String CIRCLE_NAME = "circle_name";
	public static final String USER_ID = "user_id";
	public static final String USER_STATUS = "status"; //specific status for circle

	public static final String ALL_FRIENDS_CIRCLE = "All";
	public static final long ALL_FRIENDS_ID = -1;


	//create unique key for circle and return
	//auto adds creating user
	public static Circle createCircle(String circleName, String userID, DatastoreService ds) {
		if (userID == null || userID.length() <= 0 || circleName == null || circleName.length() <= 0)
			return null;

		//TODO: do we allow a user to be in 2 circles of the same name? probably have to...

		//slightly obscure, but we are generating random numbers until we get an empty query response
		//on the circle_id member. can't just use a datastore key because each member/column row needs
		//to be unique but the circle_id will be repeated
		Random r = new Random();
		int count;
		long id;
		do {
			id = r.nextLong();
			Query q = new Query(TYPE).addFilter(CIRCLE_ID, Query.FilterOperator.EQUAL, id);
			count = ds.prepare(q).countEntities(FetchOptions.Builder.withLimit(1));
		} while ( count > 0 || id == -1);


		Entity circle;

		circle = new Entity(TYPE); //random datastore key
		circle.setProperty(CIRCLE_NAME, circleName);
		circle.setProperty(CIRCLE_ID, id);
		circle.setProperty(USER_ID, userID);
		circle.setProperty(USER_STATUS, UserManager.RED); //default
		ds.put(circle);

		Circle result = new Circle(id, circleName);
		User u = UserManager.getUser(userID, ds);
		result.addUser(u);

		return result;

	}


	//NOTE: circle must exist first!!!
	public static void addUserToCircle(String userID, Long circleID, DatastoreService ds) {
		if (userID == null || userID.length() <= 0 || circleID == null)
			return;

		//check to see if already exists
		//add user filter
		Query q = new Query(TYPE).addFilter(USER_ID, Query.FilterOperator.EQUAL, userID);

		//add circle filter
		q.addFilter(CIRCLE_ID, Query.FilterOperator.EQUAL, circleID);

		int count = ds.prepare(q).countEntities(FetchOptions.Builder.withLimit(1));

		//if count > 0, then user is already in this circle
		if (count < 1) {
			Circle c = getCircleByID(circleID, ds);

			if (c != null) {
				Entity e = new Entity(TYPE);
				e.setProperty(CIRCLE_ID, circleID);
				e.setProperty(CIRCLE_NAME, c.name);
				e.setProperty(USER_ID, userID);
				e.setProperty(USER_STATUS, UserManager.RED); //set default to red?
				ds.put(e);
			}

		}
	}

	public static void setStatusForCircle(String userID, Integer status, Long circleID, DatastoreService ds) {
		//add user filter
		Query q = new Query(TYPE).addFilter(USER_ID, Query.FilterOperator.EQUAL, userID);

		//add circle filter
		q.addFilter(CIRCLE_ID, Query.FilterOperator.EQUAL, circleID);

		PreparedQuery pq = ds.prepare(q);

		Entity result;

		try {
			result = pq.asSingleEntity();
		} catch (PreparedQuery.TooManyResultsException e) {
			//error - more than one in server!
			return;
		}

		if (result != null) {
			result.setProperty(USER_STATUS, status);
			ds.put(result);
		}
	}


	//NOTE: DOES NOT INCLUDE SPECIFIED USER IN CIRCLES
	public static Circle getCircleWithUsers(long circleID, String userID,  DatastoreService ds) {


		Query q = new Query(TYPE).addFilter(CIRCLE_ID, Query.FilterOperator.EQUAL, circleID);

		Iterator<Entity> pq = ds.prepare(q).asIterator();

		Circle result = getCircleByID(circleID, ds);

		while(pq.hasNext()) {
			Entity e = pq.next();
			String user = (String) e.getProperty(USER_ID);
			if(!user.equalsIgnoreCase(userID)) { //don't add specified current user
				User u = UserManager.getUser(user, ds);
				if (u != null) {
					if (u.status == UserManager.OTHER) { //if status is set to "other" get specific status
						long specStatus = (Long) e.getProperty(USER_STATUS);
						u.status = (int) specStatus;
						if (specStatus == UserManager.RED) { //if unavailable for this circle
							u.desiredLocation = null;
							u.desiredTime = null;
						}
					}
					result.addUser(u);
				}
			}
		}

		return result;
	}


	//TODO: replace if we create database for circle info
	public static Circle getCircleByID(Long circleID, DatastoreService ds) {

		Query q = new Query(TYPE).addFilter(CIRCLE_ID, Query.FilterOperator.EQUAL, circleID);
		List<Entity> circles = ds.prepare(q).asList(FetchOptions.Builder.withLimit(1));
		if (circles != null && circles.size() > 0) {
			Entity e = circles.get(0);
			String name = (String) e.getProperty(CIRCLE_NAME);
			return new Circle(circleID, name);
		} else {
			return null;
		}

	}

	//returns circle object with uninitialized user list (lightweight)
	public static List<Circle> getCircleNames(String userID,  DatastoreService ds) {
		if (userID == null || userID.length() <= 0)
			return null;

		List<Circle> result = new LinkedList<Circle>();

		Query q = new Query(TYPE).addFilter(USER_ID, Query.FilterOperator.EQUAL, userID);

		Iterator<Entity> pq = ds.prepare(q).asIterator();

		while(pq.hasNext()) {
			Entity e = pq.next();
			String circleName = (String) e.getProperty(CIRCLE_NAME);
			Long circleID = (Long) e.getProperty(CIRCLE_ID);
			result.add(new Circle(circleID, circleName));
		}

		return result;

	}

}
