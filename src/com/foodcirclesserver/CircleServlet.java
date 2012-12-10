/*
 * CircleServlet - add user to circle or create new
 * mandatory params: action, circle_name, user_id
 * 
 * add:
 * 	additional param: circle_id (long)
 * 	resp: none
 * 
 * create:
 * 	resp: new circle 
 * 
 * 
 *
 * NOTE: creating a circle really creates one! make sure that is what the user intends to do or you
 * will start getting duplicates
 */
package com.foodcirclesserver;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

public class CircleServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3495539797716469126L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter
		
		String circleName = req.getParameter(CircleManager.CIRCLE_NAME);
		if (circleName == null || circleName.length() <= 0)
			return;

		
		String action = req.getParameter("action");
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Gson gson = new Gson();
		resp.setContentType("text/json");
		
		switch(action) {
		case "create" :
			Circle newCircle = CircleManager.createCircle(circleName, userID, ds);
			String jString = gson.toJson(newCircle);
			try {
				resp.getWriter().println(jString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "add" :
			Long circleID = Long.parseLong(req.getParameter(CircleManager.CIRCLE_ID));
			if (circleID == null)
				return;
			CircleManager.addUserToCircle(userID, circleID, circleName, ds);
			break;
		}
	}

}