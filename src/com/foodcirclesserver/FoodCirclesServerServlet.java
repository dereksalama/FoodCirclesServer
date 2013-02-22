package com.foodcirclesserver;

import java.io.IOException;
import javax.servlet.http.*;

import com.foodcirclesserver.util.TestBench;

@SuppressWarnings("serial")
public class FoodCirclesServerServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		TestBench.main();
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
