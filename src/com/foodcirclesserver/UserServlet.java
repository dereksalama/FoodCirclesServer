/*
 * UserServlet - creating and updating users
 * req: /user?userID=...&action=...
 *  	-Action specifies what you want to do
 *  	-current operations:
 *  		-create: &name=... -> initializes user 
 *  		-update: &status=...&train=... -> set (global) status and current train
 *  		-circle_statuses: &cricles=circle1,#;circle2,# (comma between circle name and status, semicolon
 *  														between different circles)
 *  							--> set statuses for specific trains
 *  
 *  -Derek Salama
 */
package com.foodcirclesserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = -5751629299347261001L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			return; //invalid user parameter
		
		String action = req.getParameter("action");
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		switch (action) {
		
		case "create":
			String name = req.getParameter(UserManager.NAME);
			UserManager.createUser(userID, name, ds);
			break;
		case "update":
			Integer status = Integer.parseInt(req.getParameter(UserManager.STATUS));
			String currentTrain = req.getParameter(UserManager.CURRENT_TRAIN);
			UserManager.updateUser(userID, status, currentTrain, ds);
			break;
		case "circle_statuses": //set status for indiv circles
			String circleList = req.getParameter("circles");
			String[] circles = circleList.split(";");
			for(String circle : circles) {
				String[] pair = circle.split(",");
				String circleName = pair[0];
				Integer circleStatus = Integer.parseInt(pair[1]);
				CircleManager.setStatusForCircle(userID, circleStatus, circleName, ds);
			}
		}
	}

}
	
