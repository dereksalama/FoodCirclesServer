/*
 * CircleServlet - add user to circle or create new
 * mandatory params: action, user_id
 *
 * add:
 * 	additional param: circle_id (long)
 * 	resp: none
 *
 * create:
 * 	additional param: circle_name
 * 	resp: new circle
 *
 *
 *
 * NOTE: creating a circle really creates one! make sure that is what the user intends to do or you
 * will start getting duplicates
 */
package com.foodcirclesserver.circles;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gson.Gson;

public class CircleServlet extends HttpServlet {

	private static final long serialVersionUID = -3495539797716469126L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userID = req.getParameter(UserManager.USER_ID);
		String circleName = req.getParameter(CircleManager.CIRCLE_NAME);

		if (userID == null || userID.length() <= 0 || circleName == null || circleName.length() <= 0) {
			resp.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String token = req.getParameter(UserManager.TOKEN_HASH);

		try {
			if (!UserManager.validateUser(token, userID, ds)) {
				resp.sendError(HttpStatus.SC_FORBIDDEN);
				return;
			}
		} catch (EntityNotFoundException e1) {
			e1.printStackTrace();
			resp.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}

		CircleManager.createCircle(circleName, userID, ds);

		Gson gson = new Gson();
		resp.setContentType("text/json");

		List<Circle > circleNames = CircleManager.getCircleNames(userID, ds);
		Collections.sort(circleNames);

		//add "All Friends" circle
		Circle allFriends = new Circle(CircleManager.ALL_FRIENDS_ID, "All Friends");
		circleNames.add(allFriends);

		String jString = gson.toJson(allFriends);
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);
		try {
			resp.getWriter().println(jString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
