package com.foodcirclesserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




public class FBOAuth extends HttpServlet {
	
	private static final long serialVersionUID = 8334594117407174449L;
	private static final String REDIRECT = "/logintest.jsp";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String code = req.getParameter("code");
        if (code != null && code.length() > 0) {
            String authURL = Facebook.getAuthURL(code);
            URL url;
			try {
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
	                if (accessToken != null && expires != null ) {
	                    //UserService us = UserService.get();
	                    //us.authFacebookLogin(accessToken, expires);
	                	req.setAttribute(UserManager.ACCESS_TOKEN, accessToken);
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
//		                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/getfriends?");
//		                    
//		                    req.setAttribute(UserManager.USER_ID, userID);
//		                    rd.forward(req, resp);
		                    
		                    URL requrl = new URL("http://localhost:8888/getfriends?" + 
		                    			UserManager.USER_ID + "=" + userID + "&" + 
		                    			UserManager.ACCESS_TOKEN + "=" + accessToken);
		                    
		                    BufferedReader reader = new BufferedReader(new InputStreamReader(requrl.openStream()));
		                    String line;

		                    while ((line = reader.readLine()) != null) {
		                        // ...
		                    }
		                    reader.close();
		                }
		                
	                } else {
	                    throw new RuntimeException("Access token and expires not found");
	                }
	            } catch (IOException e) {
	                throw new RuntimeException(e);
	            } 
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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