/*
 * CreateUserServlet - creating and updating users
 *  -Derek Salama
 */
package com.foodcirclesserver.user;


import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

public class CreateUserServlet extends HttpServlet {

	private static final long serialVersionUID = -5751629299347261001L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userID = req.getParameter(UserManager.USER_ID);
		if (userID == null || userID.length() <= 0)
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);


		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String name = req.getParameter(UserManager.NAME);
		String parsedName = name.replace('_', ' '); //can't do request with spaces, so use underscores and replace

		String hashString = req.getParameter(UserManager.TOKEN_HASH);
		try {
			User u = UserManager.createUser(userID, parsedName, hashString, ds);
			Gson gson = new Gson();
			String jString = gson.toJson(u);

			resp.setContentType("text/json");
			try {

				resp.getWriter().println(jString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
		} catch (IllegalStateException e) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}

}

