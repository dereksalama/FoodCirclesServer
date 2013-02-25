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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gson.Gson;


public class GetCirclesServlet extends HttpServlet {

	private static final long serialVersionUID = 3911038485119324197L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//real code
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}


		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String tokenHash = (req.getParameter(UserManager.TOKEN_HASH));
		try {
			if (UserManager.validateUser(tokenHash, userID, ds)) {
				List<Circle > circleNames = CircleManager.getCircleNames(userID, ds);

				//add "All Friends" circle
				Circle allFriends = new Circle(CircleManager.ALL_FRIENDS_ID, "All Friends");
				circleNames.add(allFriends);

				Gson gson = new Gson();
				String jString = gson.toJson(circleNames);

				resp.setContentType("text/json");
				resp.setStatus(HttpServletResponse.SC_ACCEPTED);

				resp.getWriter().println(jString);
			}
		} catch (EntityNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}

	}


}
