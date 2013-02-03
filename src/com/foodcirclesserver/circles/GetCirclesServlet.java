/*
 * GetCirclesServlet - fetch list of user's circles
 * 
 * request:
 *"/getcircles?user_id=..."
 *
 * response:
 * (light) List<Circle> in json (as in no users, just names and ids)
 *
 * -Derek
 */

package com.foodcirclesserver.circles;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;


public class GetCirclesServlet extends HttpServlet {

	private static final long serialVersionUID = 3911038485119324197L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		//real code
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter

		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Circle > circleNames = CircleManager.getCircleNames(userID, ds);
		
		//add "All Friends" circle
		Circle allFriends = new Circle(CircleManager.ALL_FRIENDS_ID, "All Friends"); 
		circleNames.add(allFriends);
		
		Gson gson = new Gson();
		String jString = gson.toJson(circleNames);
//		System.out.println(jString);
		
		resp.setContentType("text/json");
		
		try {
			resp.getWriter().println(jString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
