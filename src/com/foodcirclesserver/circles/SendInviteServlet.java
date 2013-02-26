package com.foodcirclesserver.circles;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;


public class SendInviteServlet extends HttpServlet {

	private static final long serialVersionUID = -5354835351160362455L;

	//Should this be POST?
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String circleIDString = req.getParameter(InviteManager.CIRCLE_ID);
		Long circleID = Long.parseLong(circleIDString);

		String senderID = req.getParameter(InviteManager.SENDER_ID);
		String[] receiverIDs = req.getParameterValues(InviteManager.RECEIVER_ID);

		if (circleID == null || senderID == null || receiverIDs == null)
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

		String tokenHash = (req.getParameter(UserManager.TOKEN_HASH));
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		try {
			if (UserManager.validateUser(tokenHash, senderID, ds)) {
				for(String receiver : receiverIDs) {
					//TODO: send notification!
					Key k = InviteManager.saveInvite(circleID, senderID, receiver, ds);
				}
				resp.setStatus(HttpServletResponse.SC_ACCEPTED);
				//display user name and circle name on invite
				//use key to accept invite
			}
		} catch (EntityNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}





	}

}
