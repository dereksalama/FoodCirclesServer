package com.foodcirclesserver.auth;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.UserManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class UpdateTokenServlet extends HttpServlet {

	private static final long serialVersionUID = -8149178488056507189L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		String userID = req.getParameter(UserManager.USER_ID);
		String hashString = req.getParameter(UserManager.TOKEN_HASH);
		Integer hash = Integer.parseInt(hashString);

		String newHashString = req.getParameter("new_hash");
		Integer newHash = Integer.parseInt(newHashString);

		try {
			UserManager.updateHash(newHash, hash, userID, ds);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User not created");
		}
	}


}
