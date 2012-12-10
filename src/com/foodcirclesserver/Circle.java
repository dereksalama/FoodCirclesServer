package com.foodcirclesserver;

import java.util.LinkedList;
import java.util.List;


public class Circle {
	
	public String name;
	public Long id;
	
	private List<User> users;
	
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
	
	

}
