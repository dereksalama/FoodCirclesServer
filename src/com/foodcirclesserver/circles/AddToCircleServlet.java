package com.foodcirclesserver.circles;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class AddToCircleServlet extends HttpServlet {

	private static final long serialVersionUID = -6837562522339503309L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userID = req.getParameter(UserManager.USER_ID);
		String circleIDString = req.getParameter(CircleManager.CIRCLE_ID);

		if (userID == null || userID.length() <= 0 || circleIDString == null || circleIDString.length() <= 0) {
			resp.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}

		Long circleID = Long.parseLong(circleIDString);

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

		CircleManager.addUserToCircle(userID, circleID, ds);

	}

}
