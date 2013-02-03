package com.foodcirclesserver.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.foodcirclesserver.circles.Circle;
import com.foodcirclesserver.circles.CircleInvite;
import com.foodcirclesserver.circles.CircleManager;
import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
//		String accessToken = "AAACEdEose0cBAEjBSpYWQExfXEg5IKaZAlYF5dqPjlwCkQOaveZArQsjlWETihQd5AH4xTPNb8hAJHRjZAnbXSnMYQbnD83P9bkgiRBiQZDZD";
		
//		List<FacebookFriend> friends = GetFriendsServlet.getFbFriends(userID, accessToken);
		
		String jakeID = "1062900298";
		String lukeID = "1357354248";
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//testing
//		for (FacebookFriend f : friends ) {
//			System.out.println(f.getName() + ", " + f.getUserID());
//		}
		
		
		
//		UserManager.createUser(userID, "Derek Salama", ds);
//		UserManager.createUser(jakeID, "Jake Leichtling", ds);
//		UserManager.createUser(lukeID, "Luke Zirngibl", ds);
		
		
//		User derk = UserManager.getUser(userID);
		
//		List<User> foodFriends = GetFriendsServlet.getFoodFriends(friends, ds);
		
//		for (User e : foodFriends) {
//			System.out.println(e.name + ", " + e.userID);
//		}
/*		
		Gson gson = new Gson();
		String jString = gson.toJson(foodFriends);
		System.out.println("FoodFriends:");
		System.out.println(jString); */
		
		//servlet testing
		//create users
		/*
		String reqUrl = "http://localhost:8888/user?user_id=" + userID + "&action=create&name=Derek_Salama";
		JsonHelper.getJSONfromUrl(reqUrl);
		
		//too lazy to make all that way...
		UserManager.createUser(jakeID, "Jake Leichtling", ds);
		UserManager.createUser(lukeID, "Luke Zirngibl", ds);
		
		reqUrl = "http://localhost:8888/circle?action=create&user_id=" + userID + "&circle_name=test";
		String newCircle = JsonHelper.getJSONfromUrl(reqUrl);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement circle = parser.parse(newCircle);
		Circle test = gson.fromJson(circle, Circle.class);
		
		CircleManager.addUserToCircle(jakeID, test.id, ds);
		CircleManager.addUserToCircle( lukeID,test.id, ds);
		
		
		Circle test2 = CircleManager.createCircle("test2", userID, ds);
		CircleManager.addUserToCircle( jakeID,test2.id,  ds);
		
		//set jake for foco @ 6:30
		reqUrl = "http://localhost:8888/user?user_id=" + jakeID + "&action=loc_time&location=foco&time=6:30";
		JsonHelper.getJSONfromUrl(reqUrl);
		
		//set luke as available
		reqUrl = "http://localhost:8888/user?user_id=" + lukeID + "&action=update&status=0";
		JsonHelper.getJSONfromUrl(reqUrl);
		
		//set Jake as green for test, red for test2
		reqUrl = "http://localhost:8888/user?user_id=" + jakeID + "&action=circle_statuses" +
					"&circles=" + test.id + ",0;" + test2.id + ",0";
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
		System.out.println("Test2 Circle: " + test2Members); */
		
/*
		reqUrl = "http://localhost:8888/getcirclemembers?user_id=" + userID + "&circle_name=All&circle_id=" + -1 +"&access_token=" + accessToken;
		String allFriends = JsonHelper.getJSONfromUrl(reqUrl);
		System.out.println("All Circle: " + allFriends); */
		
		
		List<Circle> circles = CircleManager.getCircleNames(userID, ds);

//		
		Circle test = new Circle((long) 0,"error");
		Circle test2 = new Circle((long) 0, "error");
		
		for (Circle c : circles) {
			if (c.name.equalsIgnoreCase("test")) {
				test = c;
			} else if (c.name.equalsIgnoreCase("test2")) {
				test2 = c;
			}
		}
		
		if (test.id == 0) {
			System.out.println("Test not found");
			return;
		}
		
		if (test2.id == 0) {
			System.out.println("Test2 not found");
			return;
		}
		
		String text = "Froyo?";
		//sending a message to test circle
		String url = "http://localhost:8888/sendmessage?text="+text+"&time="+ (new Date()).getTime() +
				"&user_id="+userID+"&circle_id="+test.id;
		JsonHelper.getJSONfromUrl(url);
		
		//check messages for test circle
		url = "http://localhost:8888/getchat?circle_id="+test.id;
		String result = JsonHelper.getJSONfromUrl(url);
		System.out.println(result);
		
		//send invite to luke for test2 from derek
		url = "http://localhost:8888/sendinvite?circle_id="+test2.id+"&sender_id="+userID+
				"&receiver_id=" + lukeID;
		JsonHelper.getJSONfromUrl(url);
		
		//get invites for luke
		url = "http://localhost:8888/getinvites?receiver_id="+lukeID;
		String jString = JsonHelper.getJSONfromUrl(url);
		
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		
		JsonArray jarray = parser.parse(jString).getAsJsonArray();
		
		List<CircleInvite> invites = new LinkedList<CircleInvite>();
		for (JsonElement e : jarray) {
			CircleInvite inv = gson.fromJson(e, CircleInvite.class);
			invites.add(inv);
			System.out.println("Invite for " + inv.getCircleName() + " from " + inv.getSenderName());	
		}
		
		//deny invite
		url = "http://localhost:8888/invite?key="+invites.get(0).getKeyID()+"&accepted="+false;
		JsonHelper.getJSONfromUrl(url);
		
		//resend
		url = "http://localhost:8888/sendinvite?circle_id"+test2.id+"&sender_id="+userID+
				"&receiver_id" + lukeID;
		JsonHelper.getJSONfromUrl(url);
		
		//accept
		//id should not have changed
		url = "http://localhost:8888/invite?key="+invites.get(0).getKeyID()+"&accepted="+true;
		JsonHelper.getJSONfromUrl(url);
		
		//check members of test2 as seen by derek
		test2 = CircleManager.getCircleWithUsers(test2.id, userID, ds);
		System.out.println(test2.toString());

	}

}
