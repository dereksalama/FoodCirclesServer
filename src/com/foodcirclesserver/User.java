package com.foodcirclesserver;


import com.google.gson.annotations.SerializedName;

public class User {
	
	@SerializedName("user_id")
	public String userID;
	
	public String name;
	
	
	public Integer status;
	
	@SerializedName("current_train")
	public String currentTrainID;
	
	public User(String userID, String name, Integer status, String currentTrainID) {
		this.userID = userID;
		this.name = name;
		this.status = status;
		this.currentTrainID = currentTrainID;
	}
	

}
