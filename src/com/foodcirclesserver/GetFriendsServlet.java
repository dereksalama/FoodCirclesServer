package com.foodcirclesserver;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class GetFriendsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2707498412415996993L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter
		
		String accessToken = req.getParameter(UserManager.ACCESS_TOKEN);
		if (accessToken == null || accessToken.length() <= 0)
			return; //invalid token parameter
		
		List<FacebookFriend> friends = getFriends(userID, accessToken);
		
		//testing
		for (FacebookFriend f : friends ) {
			System.out.println(f.getName() + ", " + f.getUserID());
		}
	}
	
	public List<FacebookFriend> getFriends(String userID, String accessToken) {
		
		Gson gson = new Gson();
		
		JsonParser parser = new JsonParser();
		String jString = JsonHelper.getJSONfromUrl(Facebook.friendsURL(userID, accessToken));
		
		if (jString == null)
			return null;
		
		JsonObject response = parser.parse(jString).getAsJsonObject();
		JsonArray data = response.getAsJsonArray("data");
		
		List<FacebookFriend> friends = new LinkedList<FacebookFriend>();
		for (JsonElement obj : data) {
			FacebookFriend f = gson.fromJson(obj, FacebookFriend.class);
			friends.add(f);
		}
		
		return friends;
	}

}
