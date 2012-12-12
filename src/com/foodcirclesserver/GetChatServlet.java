/*
 * GetChatServlet
 * req: /getchat?circle_id=...
 * 
 * resp: List<GroupChatMessage>
 * 
 * -Derek
 */

package com.foodcirclesserver;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

public class GetChatServlet extends HttpServlet {

	private static final long serialVersionUID = 771109640004122711L;
	
	public static int DEFAULT_NUM_MESSAGES = 20;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {

		String circleString = req.getParameter(GroupChatManager.CIRCLE_ID);
		Long circleID = Long.parseLong(circleString);
		
		if (circleID == null || circleID == CircleManager.ALL_FRIENDS_ID)
			return;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<GroupChatMessage> ms = GroupChatManager.getChatForCircle(circleID, DEFAULT_NUM_MESSAGES, ds);
		
		Gson gson = new Gson();
		String jString = gson.toJson(ms);
		
		resp.setContentType("text/json");
		
		try {
			resp.getWriter().println(jString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
