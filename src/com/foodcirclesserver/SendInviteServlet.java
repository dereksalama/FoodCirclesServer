package com.foodcirclesserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;


public class SendInviteServlet extends HttpServlet {

	private static final long serialVersionUID = -5354835351160362455L;
	
	//Should this be POST?
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String circleIDString = req.getParameter(InviteManager.CIRCLE_ID);
		Long circleID = Long.parseLong(circleIDString);
		
		String senderID = req.getParameter(InviteManager.SENDER_ID);
		String receiverID = req.getParameter(InviteManager.RECEIVER_ID);
		
		if (circleID == null || senderID == null || receiverID == null) 
			return;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Key k = InviteManager.saveInvite(circleID, senderID, receiverID, ds);
		
		//TODO: send notification!
		//display user name and circle name on invite
		//use key to accept invite
		
	}

}
