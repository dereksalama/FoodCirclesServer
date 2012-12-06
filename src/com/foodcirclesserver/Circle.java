package com.foodcirclesserver;

import java.util.LinkedList;
import java.util.List;


public class Circle {
	
	public String name;
	
	public List<User> users;
	
	public Circle(String name) {
		this.name = name;
		users = new LinkedList<User>(); //not sure if optimal data structure...
	}

}
