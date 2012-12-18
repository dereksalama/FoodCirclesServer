package com.foodcirclesserver;

public class CircleInvite {
	
	public String circleName;
	public Long keyID;
	
	public String senderName;
	
	public CircleInvite(String circleName, Long keyID, String senderName) {
		this.circleName = circleName;
		this.keyID = keyID;
		this.senderName = senderName;
	}

}
