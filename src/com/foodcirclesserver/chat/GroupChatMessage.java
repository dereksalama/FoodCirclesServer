package com.foodcirclesserver.chat;

import java.util.Date;

import com.foodcirclesserver.user.User;


public class GroupChatMessage implements Comparable<GroupChatMessage> {

	public String text;

	public Date time;

	public User sender;

	public Long circle;

	public GroupChatMessage() {}

	public GroupChatMessage(String text, Date time, User sender, Long circle) {
		this.text = text;
		this.time = time;
		this.sender = sender;
		this.circle = circle;
	}

	@Override
	public String toString() {
		return text;
	}

	public int compareTo(GroupChatMessage other) {
		return  (this.time.compareTo(other.time));
	}

}
