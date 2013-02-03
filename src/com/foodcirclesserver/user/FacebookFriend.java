package com.foodcirclesserver.user;

import com.google.gson.annotations.SerializedName;

public class FacebookFriend {
	
	@SerializedName("id")
	private String userID;
	
	@SerializedName("name")
	private String name;
	
	public FacebookFriend(){};
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
