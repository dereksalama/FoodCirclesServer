package com.foodcirclesserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




public class FBOAuth extends HttpServlet {
	
	private static final long serialVersionUID = 8334594117407174449L;
	private static final String REDIRECT = "http://localhost:8888/loginresult";

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
	                String userID = null;
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
	                        if (kv[0].equals("userID")){
	                        	userID = kv[1];
	                        }
	                    }
	                }
	                if (accessToken != null && expires != null) {
	                    //UserService us = UserService.get();
	                    //us.authFacebookLogin(accessToken, expires);
	                    resp.sendRedirect(REDIRECT);
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
