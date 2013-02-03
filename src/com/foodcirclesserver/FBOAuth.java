package com.foodcirclesserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foodcirclesserver.user.Facebook;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




public class FBOAuth extends HttpServlet {
	
	private static final long serialVersionUID = 8334594117407174449L;
	private static final String REDIRECT = "/logintest.jsp";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws MalformedURLException {
		//TestBench.main();
        String code = req.getParameter("code");
        if (code != null && code.length() > 0) {
            String authURL = Facebook.getAuthURL(code);
            URL url;
				url = new URL(authURL);
	            try {
	                String result = readURL(url);
	                String accessToken = null;
	                Integer expires = null;
	                String[] pairs = result.split("&");
	                for (String pair : pairs) {
	                    String[] kv = pair.split("=");
	                    if (kv.length != 2) {
	                        throw new RuntimeException("Unexpected auth response");
	                    } else {
	                        if (kv[0].equals("access_token")) {
	                            accessToken = kv[1];
	                        }
	                        if (kv[0].equals("expires")) {
	                            expires = Integer.valueOf(kv[1]);
	                        }
	                    }
	                }
	                resp.getWriter().println(accessToken);
	                /*
	                if (accessToken != null && expires != null ) {
	                    //UserService us = UserService.get();
	                    //us.authFacebookLogin(accessToken, expires);
	                	req.setAttribute("access_token", accessToken);
	                	req.setAttribute("exp", expires);
//	                    RequestDispatcher rd = getServletContext().getRequestDispatcher(REDIRECT);
//	                    rd.forward(req, resp);
	                	
	                	//now that we have access token, get userid
	                	//messy...
	                	Integer userID = null;
	                	
	                	JsonParser parser = new JsonParser();
	                	String jString = JsonHelper.getJSONfromUrl(Facebook.getUserURL(accessToken));
	                	
	                	JsonObject jObj = parser.parse(jString).getAsJsonObject();
	                	userID = jObj.get("id").getAsInt();
		                
		                
		                if (userID != null) {
		                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/getfriends?");
		                    
		                    req.setAttribute(UserManager.USER_ID, userID);
		                    rd.forward(req, resp);
		                    
		                    String reqURL = "http://localhost:8888/getfriends?" + 
		                    			UserManager.USER_ID + "=" + userID + "&" + 
		                    			"access_token" + "=" + accessToken;

		                    String response = JsonHelper.getJSONfromUrl(reqURL);
		                    System.out.println(response);
		                    //shits not working for some reason
//		            		resp.setContentType("text/json");
//		                    resp.getOutputStream().print(response);
//		                	System.out.println(userID);
//		                	System.out.println(accessToken);
		                }
		                
		                
	                } else {
	                    throw new RuntimeException("Access token and expires not found");
	                }
	                */
	            } catch (IOException e) {
	                throw new RuntimeException(e);
			}

        }       
	}
	
    private String readURL(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }
    
    

}
