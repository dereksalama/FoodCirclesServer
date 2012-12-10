package com.foodcirclesserver;


import com.google.gson.annotations.SerializedName;

public class User {
	
	@SerializedName("user_id")
	public String userID;
	
	public String name;
	
	
	public Integer status;
	
	//should we be sending this around?
	@SerializedName("current_train")
	public Long currentTrainID;
	
	public User (){};
	
	public User(String userID, String name, Integer status, Long currentTrainID) {
		this.userID = userID;
		this.name = name;
		this.status = status;
		this.currentTrainID = currentTrainID;
	}
	

}
