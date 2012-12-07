package com.foodcirclesserver;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class FoodCirclesServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		TestBench.main();
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
