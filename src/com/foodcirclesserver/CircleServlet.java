/*
 * CircleServlet - add user to circle -> also creates new circles
 * 
 * req:
 *"/circle?user_id=...&circle_name=..."
 *
 *resp:
 *none
 */
package com.foodcirclesserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class CircleServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3495539797716469126L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter
		
		String circleName = req.getParameter(CircleManager.CIRCLE_NAME);
		if (circleName == null || circleName.length() <= 0)
			return;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		CircleManager.addUserToCircle(userID, circleName, ds);
		
	}

}
