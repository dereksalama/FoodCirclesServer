package com.foodcirclesserver.user;
/*
 * updatestatus
 */

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UpdateStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 2105825059683254992L;


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String action = req.getParameter("action");
		String userID = req.getParameter(UserManager.USER_ID);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		if (action.equals("update_status")) {
			Integer status = Integer.parseInt(req.getParameter(UserManager.STATUS));
			UserManager.updateStatus(userID, status, ds);
		} else if (action.equals("status_loc_time")) {
			Integer status = Integer.parseInt(req.getParameter(UserManager.STATUS));
			UserManager.updateStatus(userID, status, ds);
			//don't break - fall through to do location and time
			String timeString = req.getParameter(UserManager.DESIRED_TIME);
			String location = req.getParameter(UserManager.DESIRED_LOCATION);

			UserManager.updateLocationAndTime(userID, timeString, location, ds);
		} else if (action.equals("loc_time")) {
			String timeString = req.getParameter(UserManager.DESIRED_TIME);
			String location = req.getParameter(UserManager.DESIRED_LOCATION);
			UserManager.updateLocationAndTime(userID, timeString, location, ds);
		}
	}

}
