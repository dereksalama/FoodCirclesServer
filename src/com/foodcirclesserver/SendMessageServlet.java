/*
 * SendMessageServlet
 * NOTE: should change to doPost?
 * 
 * req: /sendmessage?text=...&circle_id=...&time=...&user_id=...
 * -time is just using default format for now
 * 
 * resp: none
 */

package com.foodcirclesserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class SendMessageServlet extends HttpServlet {

	private static final long serialVersionUID = -1346960197002723585L;

	//TODO: notify other members in circle!!
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String text = req.getParameter(GroupChatManager.TEXT);
		String timeString = req.getParameter(GroupChatManager.TIME);
		
		Date time;
		try {
			time = (new SimpleDateFormat()).parse(timeString); //date formatting?
		} catch (ParseException e) {
			time = new Date(); //default
		} 
		
		String userID = req.getParameter(GroupChatManager.USER_ID);
		String circleString = req.getParameter(GroupChatManager.CIRCLE_ID);
		Long circleID = Long.parseLong(circleString);
		
		if (text == null || userID == null || circleID == null)
			return;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		GroupChatManager.saveMessage(text, time, userID, circleID, ds);
	}
}
