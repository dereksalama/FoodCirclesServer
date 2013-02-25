/*
 * GetCircleMembersServlet - get the users (excluding specified user) from given servlet
 *
 * request:
 *"/getcirclemembers?user_id=...&circle_id=..."
 * -> if trying to get "All Friends" circle, must ALSO include access token
 * 			(&access_token=...)
 *
 * response:
 * Circle.java in json
 *
 * -Derek
 */

package com.foodcirclesserver.circles;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.FacebookFriend;
import com.foodcirclesserver.user.GetFriendsServlet;
import com.foodcirclesserver.user.User;
import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gson.Gson;

public class GetCircleMembersServlet extends HttpServlet {

	private static final long serialVersionUID = -1986666610659062911L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userID = req.getParameter(UserManager.USER_ID);
		String token = req.getParameter(UserManager.TOKEN_HASH);
		String circleIDString = req.getParameter(CircleManager.CIRCLE_ID);
		if (userID == null || userID.length() <= 0 || token == null || token.length() == 0 ||
				circleIDString == null || circleIDString.length() == 0)
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

		Long circleID = Long.parseLong(circleIDString);

		Circle result = null;
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		try {
			if (!UserManager.validateUser(token, userID, ds)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} catch (EntityNotFoundException e1) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

		//if fetching "all friends" need access token for fb stuff
		//TODO: security
		if (circleID == CircleManager.ALL_FRIENDS_ID) {
			String accessToken = req.getParameter("access_token");
			result = constructAllFriendsCircle(userID, accessToken, ds);
		} else {
			result = CircleManager.getCircleWithUsers(circleID, userID, ds);
			result.generateCurrentEvents();
		}

		if (result == null)
			resp.sendError(500, "No friends found");

		Collections.sort(result.getUsers());

		Gson gson = new Gson();
		String jString = gson.toJson(result);
//		System.out.println(jString);

		resp.setContentType("text/json");

		resp.getWriter().println(jString);

	}

	public static Circle constructAllFriendsCircle(String userID, String accessToken, DatastoreService ds) {
		Circle result = new Circle( CircleManager.ALL_FRIENDS_ID, CircleManager.ALL_FRIENDS_CIRCLE);

		List<FacebookFriend> fbFriends = GetFriendsServlet.getFbFriends(userID, accessToken);
		if (fbFriends == null)
			return null;
		List<User> friends = GetFriendsServlet.getFoodFriends(fbFriends, ds);

		result.addUserList(friends);

		return result;
	}

}
