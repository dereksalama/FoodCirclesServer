package com.foodcirclesserver.trains;
/*
 * GetTrainServlet - pretty self explanatory
 * req:
 *"/gettrain?train_id=...
 *
 * resp:
 * train in json
 * 
 * -Derek
 */

/*package com.foodcirclesserver;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

public class GetTrainServlet extends HttpServlet {

	private static final long serialVersionUID = -881132405857046474L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		Long trainID = Long.parseLong(req.getParameter(TrainManager.TRAIN_ID));
		if (trainID == null || trainID == 0)
			return;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Train result = TrainManager.getTrain(trainID, ds);
		
		Gson gson = new Gson();
		resp.setContentType("text/json");
		String jString = gson.toJson(result);
		

		try {
			resp.getWriter().println(jString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}*/
