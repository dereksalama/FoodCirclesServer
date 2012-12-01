package com.foodcirclesserver;

public class AccessToken {
	public String userID;
	public String token;
	public String expires;
	
	public AccessToken(String user, String token, String exp) {
		userID = user;
		this.token = token;
		expires = exp;
	}

}
