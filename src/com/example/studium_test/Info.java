package com.example.studium_test;

public class Info {
	public String userID;
	public int headImage;
	public Boolean currentOnline;

	public Info() {
		userID = null;
		headImage = 0;
		currentOnline = false;
	}

	public Info(String userID, int headImage, Boolean currentOnline) {
		this.userID = userID;
		this.headImage = headImage;
		this.currentOnline = currentOnline;
	}
}

