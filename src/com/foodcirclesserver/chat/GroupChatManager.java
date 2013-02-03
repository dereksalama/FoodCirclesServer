/*
 * GroupChatManager
 * -stores each message as row, should be fetched by circle id
 * 
 * -Derek
 */

package com.foodcirclesserver.chat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.foodcirclesserver.user.User;
import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public class GroupChatManager {
	
	public static final String TYPE = "group_message";
	public static final String TEXT = "text";
	public static final String CIRCLE_ID = "circle_id";
	public static final String TIME = "time";
	public static final String USER_ID = "user_id";
//	public static final String USER_NAME = "user_name";
	
	
	public static void saveMessage(String text, Date time, String userID, 
			 Long circle, DatastoreService ds) {
		
		Entity e = new Entity(TYPE);
		e.setProperty(TEXT, text);
		e.setProperty(TIME, time);
		e.setProperty(USER_ID, userID);
		e.setProperty(CIRCLE_ID, circle);
		ds.put(e);
	}
	
	public static List<GroupChatMessage> getChatForCircle(Long circle, int numOfMessages, DatastoreService ds) {
		
		Query q = new Query(TYPE).addFilter(CIRCLE_ID, Query.FilterOperator.EQUAL, circle);
		q.addSort(TIME, Query.SortDirection.DESCENDING); //sort by time, latest first
		
		//only get # specified
		List<Entity> messages = ds.prepare(q).asList(FetchOptions.Builder.withLimit(numOfMessages));
		
		List<GroupChatMessage> result = new LinkedList<GroupChatMessage>();
		for (Entity e : messages) {
			String userID = (String) e.getProperty(USER_ID);
			Long circleID = (Long) e.getProperty(CIRCLE_ID);
			Date time = (Date) e.getProperty(TIME);
			String text = (String) e.getProperty(TEXT);
			
			//if we feel it is slow, this would be the reason
			//can just store userid & name, then don't need to hit up circle database
			//lose status 'cause need this to get "special" ones for the given circle
			User sender = UserManager.getUserForCircle(userID, circleID, ds);
			
			GroupChatMessage m = new GroupChatMessage(text, time, sender, circleID);
			result.add(m);
		}
		
		return result;
		
	}

}
