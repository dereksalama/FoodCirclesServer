package com.foodcirclesserver.trains;
/*
 * TrainServlet - create and update actions
 * 
 * req format:
 * "/train?action=...
 * 
 * -create - new train with name, time, location (all optional)
 * 			- automatically adds user
 * 		-fields = user_id, train_name, time, location
 * -update - updates name, time, location fields (all optional
 * 		-fields = train_id, train_name, time, location
 * 
 * resp format:
 * train in json
 * 
 * -Derek
 * 
 * NOT USING FOR NOW
 */

/*package com.foodcirclesserver;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

public class TrainServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7891651075392263257L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(UserManager.USER_ID);
	
		String action = req.getParameter("action");
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Gson gson = new Gson();
		resp.setContentType("text/json");
		String jString;
		Train result = null;
		
		String trainName = req.getParameter(TrainManager.TRAIN_NAME);
		String timeString = req.getParameter(TrainManager.TIME);
		String location = req.getParameter(TrainManager.LOCATION);
		Date time;
		try {
			time = new SimpleDateFormat().parse(timeString);//use default?
		} catch (ParseException e) {
			//TODO: default time? timezones might be tricky..
			time = null;
		} 

		switch (action) {
		
		case "create":
			result = TrainManager.createTrain(trainName, time, location, ds);
			User u = UserManager.getUser(userID, ds);
			if (u != null) {
				UserManager.updateTrain(u.userID, result.trainID, ds); //auto add user on create
				result.users.add(u);
			}

			jString = gson.toJson(result);
			

			try {
				resp.getWriter().println(jString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case "update":
			Long trainID = Long.parseLong(req.getParameter(TrainManager.TRAIN_ID));
			boolean success = TrainManager.updateTrain(trainID, trainName, time, location, ds);
			if (success)
				result = TrainManager.getTrain(trainID, ds);
			
			jString = gson.toJson(result);
			
			resp.setContentType("text/json");
			try {
				resp.getWriter().println(jString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		}
	}

}
*/