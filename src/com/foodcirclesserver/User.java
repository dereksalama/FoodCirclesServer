package com.foodcirclesserver;

public class User {
	
	
	public String userID;
	public String name;
	public String accessToken;
	public String status;
	public String currentTrainID;
	
	public User(String userID, String name, String accessToken, String status, String currentTrainID) {
		this.userID = userID;
		this.name = name;
		this.accessToken = accessToken;
		this.status = status;
		this.currentTrainID = currentTrainID;
	}
	

}
