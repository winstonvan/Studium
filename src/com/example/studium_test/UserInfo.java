package com.example.studium_test;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
	public String userID;
	public String password;
	public int headImage;
	public Boolean currentOnline;
	public int sumPosting;
	public int sumReply;

	public UserInfo() {
		userID = null;
		password = null;
		headImage = 0;
		currentOnline = false;
		sumPosting = 0;
		sumReply = 0;
	}

	public UserInfo(String userID, String password, int headImage, Boolean currentOnline, int sumPosting,
			int sumReply) {
		this.userID = userID;
		this.password = password;
		this.headImage = headImage;
		this.currentOnline = currentOnline;
		this.sumPosting = sumPosting;
		this.sumReply = sumReply;
	}
}
