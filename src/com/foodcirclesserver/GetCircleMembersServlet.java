/*
 * GetCircleMembersServlet - get the users (excluding specified user) from given servlet
 * 
 * request:
 *"/getcirclemembers?user_id=...&circle_name=..."
 * -> if trying to get "All Friends" circle, must ALSO include access token
 * 			(&access_token=...)
 * 
 * response:
 * Circle.java in json
 * 
 * -Derek
 */

package com.foodcirclesserver;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

public class GetCircleMembersServlet extends HttpServlet {
	
	private static final long serialVersionUID = -1986666610659062911L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter
		
		String circleName = req.getParameter(CircleManager.CIRCLE_NAME);
		if (circleName == null || circleName.length() <= 0)
			return;
		
		Circle result;
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//if fetching "all friends" need access token for fb stuff
		if (circleName.equalsIgnoreCase(CircleManager.ALL_FRIENDS_CIRCLE)) {
			String accessToken = req.getParameter("access_token");
			result = constructAllFriendsCircle(userID, accessToken, ds);
		} else {
			result = CircleManager.getCircle(circleName, userID, ds);
		}
		
		Gson gson = new Gson();
		String jString = gson.toJson(result);
		System.out.println(jString);
		
		resp.setContentType("text/json");
		
		try {
			resp.getWriter().println(jString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Circle constructAllFriendsCircle(String userID, String accessToken, DatastoreService ds) {
		Circle result = new Circle(CircleManager.ALL_FRIENDS_CIRCLE);
		
		List<FacebookFriend> fbFriends = GetFriendsServlet.getFbFriends(userID, accessToken);
		List<User> friends = GetFriendsServlet.getFoodFriends(fbFriends, ds);
		
		result.users = friends;
		
		return result;
	}

}
