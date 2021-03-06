/*
 * SendMessageServlet
 * NOTE: should change to doPost?
 *
 * req: /sendmessage?text=...&circle_id=...&time=...&user_id=...
 * -time is just using default format for now
 *
 * resp: none
 */

package com.foodcirclesserver.chat;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class SendMessageServlet extends HttpServlet {

	private static final long serialVersionUID = -1346960197002723585L;

	//TODO: notify other members in circle!!
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String text = req.getParameter(GroupChatManager.TEXT);
		String timeString = req.getParameter(GroupChatManager.TIME);

		Date time = new Date();
		try {
			long timeMillis = Long.parseLong(timeString);
			time.setTime(timeMillis);
		} catch (NumberFormatException e) {
			System.out.println("SendMessageServlet: time not parsed");
		}

		String userID = req.getParameter(GroupChatManager.USER_ID);
		String circleString = req.getParameter(GroupChatManager.CIRCLE_ID);
		Long circleID = Long.parseLong(circleString);
		String tokenHash = req.getParameter(UserManager.TOKEN_HASH);

		if (text == null || userID == null || circleID == null)
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		try {
			if (!UserManager.validateUser(tokenHash, userID, ds)) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} catch (EntityNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		resp.setStatus(HttpServletResponse.SC_ACCEPTED);

		GroupChatManager.saveMessage(text, time, userID, circleID, ds);
	}
}
