package com.foodcirclesserver;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;



public class TestBench {

	/**
	 * @param args
	 */
	public static void main() {
		
		//Derek
		String userID = "576485009"; 
		
		//make sure you update this!!
		String accessToken = "AAACEdEose0cBAEjBSpYWQExfXEg5IKaZAlYF5dqPjlwCkQOaveZArQsjlWETihQd5AH4xTPNb8hAJHRjZAnbXSnMYQbnD83P9bkgiRBiQZDZD";
		
//		List<FacebookFriend> friends = GetFriendsServlet.getFbFriends(userID, accessToken);
		
		String jakeID = "1062900298";
		String lukeID = "1357354248";
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//testing
//		for (FacebookFriend f : friends ) {
//			System.out.println(f.getName() + ", " + f.getUserID());
//		}
/*		
		
		
		UserManager.createUser(userID, "Derek Salama", ds);
		UserManager.createUser(jakeID, "Jake Leichtling", ds);
		UserManager.createUser(lukeID, "Luke Zirngibl", ds);
		
		
//		User derk = UserManager.getUser(userID);
		
		List<User> foodFriends = GetFriendsServlet.getFoodFriends(friends, ds);
		
//		for (User e : foodFriends) {
//			System.out.println(e.name + ", " + e.userID);
//		}
		
		Gson gson = new Gson();
		String jString = gson.toJson(foodFriends);
		System.out.println("FoodFriends:");
		System.out.println(jString);*/
		
		//servlet testing
		//create users
		String reqUrl = "http://localhost:8888/user?user_id=" + userID + "&action=create&name=Derek_Salama";
		JsonHelper.getJSONfromUrl(reqUrl);
		
		//too lazy to make all that way...
		UserManager.createUser(jakeID, "Jake Leichtling", ds);
		UserManager.createUser(lukeID, "Luke Zirngibl", ds);
		
		reqUrl = "http://localhost:8888/circle?action=create&user_id=" + userID + "&circle_name=test";
		String newCircle = JsonHelper.getJSONfromUrl(reqUrl);
//		System.out.println("new circle: " + newCircle);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement circle = parser.parse(newCircle);
		Circle test = gson.fromJson(circle, Circle.class);
		
		CircleManager.addUserToCircle(jakeID, test.id, "test", ds);
		CircleManager.addUserToCircle( lukeID,test.id, "test", ds);
		
		
		Circle test2 = CircleManager.createCircle("test2", userID, ds);
		CircleManager.addUserToCircle( jakeID,test2.id, test2.name, ds);
		
		
		//set luke as available
		reqUrl = "http://localhost:8888/user?user_id=" + lukeID + "&action=update&status=0";
		JsonHelper.getJSONfromUrl(reqUrl);
		
		//set Jake as green for test, yellow for test2
		reqUrl = "http://localhost:8888/user?user_id=" + jakeID + "&action=circle_statuses" +
					"&circles=" + test.id + ",0;" + test2.id + ",1";
		JsonHelper.getJSONfromUrl(reqUrl);
		
		//get circles
		reqUrl = "http://localhost:8888/getcircles?user_id=" + userID;
		String circleNames = JsonHelper.getJSONfromUrl(reqUrl);
		System.out.println("Circles:  " + circleNames);
		
		//get members in circles
		reqUrl = "http://localhost:8888/getcirclemembers?user_id=" + userID + "&circle_name=test&circle_id=" + test.id;
		String testMembers = JsonHelper.getJSONfromUrl(reqUrl);
		System.out.println("Test Circle: " + testMembers);
		
		reqUrl = "http://localhost:8888/getcirclemembers?user_id=" + userID + "&circle_name=test2&circle_id=" + test2.id;
		String test2Members = JsonHelper.getJSONfromUrl(reqUrl);
		System.out.println("Test2 Circle: " + test2Members);
		
		reqUrl = "http://localhost:8888/getcirclemembers?user_id=" + userID + "&circle_name=All&circle_id=" + -1 +"&access_token=" + accessToken;
		String allFriends = JsonHelper.getJSONfromUrl(reqUrl);
		System.out.println("All Circle: " + allFriends);
		
		

	}

}
