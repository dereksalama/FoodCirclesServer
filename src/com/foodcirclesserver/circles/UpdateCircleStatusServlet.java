package com.foodcirclesserver.circles;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UpdateCircleStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 6813697667363684992L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userID = req.getParameter(UserManager.USER_ID);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		UserManager.updateStatus(userID, UserManager.OTHER, ds);
		String circleList = req.getParameter("circles");
		String[] circles = circleList.split(";");
		for(String circle : circles) {
			String[] pair = circle.split(",");
			Long circleID = Long.parseLong(pair[0]);
			Integer circleStatus = Integer.parseInt(pair[1]);
			CircleManager.setStatusForCircle(userID, circleStatus, circleID, ds);
		}
	}

}
