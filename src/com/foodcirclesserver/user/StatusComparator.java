package com.foodcirclesserver.user;

import java.util.Comparator;

public class StatusComparator implements Comparator<User> {

	public int compare(User left, User right) {
		if (left.status != right.status) {
			return left.status - right.status;
		} else {
			return left.compareTo(right);
		}
	}

}
