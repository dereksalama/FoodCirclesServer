package com.foodcirclesserver.circles;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gson.Gson;

public class GetInvitesServlet extends HttpServlet {

	private static final long serialVersionUID = -6972004172908534922L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String userID = req.getParameter(InviteManager.RECEIVER_ID);
		if (userID == null)
			return;

		String tokenHash = (req.getParameter(UserManager.TOKEN_HASH));

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		try {
			//TODO: hitting the datastore twice here for the user...
			if (UserManager.validateUser(tokenHash, userID, ds)) {
				List<CircleInvite> invites = InviteManager.getInvitesForUser(userID, ds);

				Gson gson = new Gson();
				String jString = gson.toJson(invites);

				resp.setContentType("text/json");

				try {
					resp.getWriter().println(jString);
					resp.setStatus(HttpServletResponse.SC_ACCEPTED);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
