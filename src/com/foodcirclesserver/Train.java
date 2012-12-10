package com.foodcirclesserver;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Train {
	
	public Long trainID;
	public String trainName;
	public Date time;
	public List<User> users;
	public String location;
	
	public Train(Long trainID, String trainName, Date time, String location) {
		this.trainID = trainID;
		this.time = time;
		this.location = location;
		this.trainName = trainName;
		
		users = new LinkedList<User>();
	}

}
