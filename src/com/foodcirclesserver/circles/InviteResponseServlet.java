package com.foodcirclesserver.circles;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InviteResponseServlet extends HttpServlet {

	private static final long serialVersionUID = 2101971148915359984L;

	//post?
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String keyIDString = req.getParameter("key");
		Long keyID = Long.parseLong(keyIDString);
		
		String acceptedString = req.getParameter("accepted");
		Boolean accepted = Boolean.parseBoolean(acceptedString);
		
		if (keyID == null || accepted == null)
			return;
		
		Key key = KeyFactory.createKey(InviteManager.TYPE, keyID);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity invite;
		try {
			invite = ds.get(key);
		} catch (EntityNotFoundException e) {
			return;
		}
		
		//mark received so user does not see again
		InviteManager.markReceived(keyID, ds);
		
		if (accepted) {
			String receiverID = (String) invite.getProperty(InviteManager.RECEIVER_ID);
			Long circleID = (Long) invite.getProperty(InviteManager.CIRCLE_ID);
			CircleManager.addUserToCircle(receiverID, circleID, ds);
			
			//TODO: notification for sender that friend accepted?
		}		
	}

}
