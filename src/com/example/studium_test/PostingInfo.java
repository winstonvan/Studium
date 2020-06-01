package com.example.studium_test;

import java.util.Date;

public class PostingInfo {

	public String UID;
	public String SentBy;
	public String ReplyTo;
	public String Content;
	public Date sentTime;
	public int contentType;
	public String Title;

	public PostingInfo() {
		String UID = null;
		String SentBy = null;
		String ReplyTo = null;
		String Content = null;
		Date sentTime = null;
		int contentType = 0;
		String Title = null;
	}

	public PostingInfo(String UID, String SentBy, String ReplyTo, String Content, Date sentTime2, int contentType,
			String Title) {

		this.UID = UID;
		this.SentBy = SentBy;
		this.ReplyTo = ReplyTo;
		this.Content = Content;
		this.sentTime = sentTime2;
		this.contentType = contentType;
		this.Title = Title;
	}

}
