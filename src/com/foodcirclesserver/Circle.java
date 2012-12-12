package com.foodcirclesserver;

import java.util.LinkedList;
import java.util.List;


public class Circle {
	
	public String name;
	public Long id;
	
	private List<User> users;
	
	//give users this list of loc/times
	//currently in circle so user can join their friends
	private List<String> currentEvents;
	
	public Circle() {};
	
	public Circle(Long id, String name) {
		this.name = name;
		this.id = id;
	}
	
	public void addUser(User u) {
		if (u != null) {
			if( users == null)
				users = new LinkedList<User>();
			users.add(u);
		}
	}
	
	public void addUserList(List<User> users) {
		this.users = users;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public List<String> generateCurrentEvents() {
		if (users != null) {
			currentEvents = new LinkedList<String>();
			for (User u : users) {
				String eventString = u.getEventString();
				if (eventString != null)
					currentEvents.add(eventString);
			}
			
			return currentEvents;
		} else {
			return null;
		}
	}
	
	public List<String> getCurrentEvents() {
		return currentEvents;
	}
	
	

}
