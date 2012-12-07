/*
 * GetFriendsServlet - fetch list of fb friends that are also FC users
 * 
 * request format:
 * "/getfriends?user_id=...&access_token=...
 * NOTE: will not work if access token is out of date!
 * 
 * response example:
 * [{"user_id":"1062900298","name":"Jake Leichtling"},{"user_id":"1357354248","name":"Luke Zirngibl"}]
 * NOTE: null fields are not included (like train in this example), see User.java for all field names 
 * 
 * -Derek
 */

package com.foodcirclesserver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class GetFriendsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2707498412415996993L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		//real code
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter
		
		String accessToken = req.getParameter("access_token");
		if (accessToken == null || accessToken.length() <= 0)
			return; //invalid token parameter
		
		List<FacebookFriend> fbFriends = getFbFriends(userID, accessToken);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<User> foodFriends = getFoodFriends(fbFriends, ds);


		Gson gson = new Gson();
		String jString = gson.toJson(foodFriends);
//		System.out.println(jString);
		
		resp.setContentType("text/json");
		
		try {

			resp.getWriter().println(jString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//testing code
		//TestBench.main();
	}
	
	public static List<FacebookFriend> getFbFriends(String userID, String accessToken) {
		
		Gson gson = new Gson();
		
		JsonParser parser = new JsonParser();
		String jString = JsonHelper.getJSONfromUrl(Facebook.friendsURL(userID, accessToken));
		
		if (jString == null)
			return null;
		
		JsonObject response = parser.parse(jString).getAsJsonObject();
		JsonArray data = response.getAsJsonArray("data");
		
		if (data == null)
			return null; //often token expired error
		
		List<FacebookFriend> friends = new LinkedList<FacebookFriend>();
		for (JsonElement obj : data) {
			FacebookFriend f = gson.fromJson(obj, FacebookFriend.class);
			friends.add(f);
		}
		
		return friends;
	}
	
	public static List<User> getFoodFriends(List<FacebookFriend> fbFriends,  DatastoreService ds) {
		if (fbFriends == null)
			return null;
		
		List<User> foodFriends = new LinkedList<User>();
		
		for (FacebookFriend fbF : fbFriends) {
			User foodF = UserManager.getUser(fbF.getUserID(), ds);
			if (foodF != null)
				foodFriends.add(foodF);
		}
		
		
		return foodFriends;
	}
	
}
