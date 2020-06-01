package com.example.studium_test;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Driver;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseAPI {

	private static final String host = "den1.mysql3.gear.host";
	private static final String Database_Name = "studiumdatabase";
	private static final String Database_User = "studiumdatabase";
	private static final String Database_Password = "Nh0N?tQ?c2sO";
	private static final String Post_Num = "3306";
	private static final String ConnectLink = "jdbc:mysql://den1.mysql3.gear.host/studiumdatabase?user=studiumdatabase&password=Nh0N?tQ?c2sO&useUnicode=true&characterEncoding=UTF8&autoReconnect=true&failOverReadOnly=false";

	public static Boolean createUser(String userName, String password, int headImage) throws SQLException {
		Boolean result = false;
		// sql statement
		String String1 = "SELECT * FROM " + Database_Name + ".User WHERE userID='" + userName + "';";
		String String2 = "INSERT INTO " + Database_Name
				+ ".User (userID, headImage, currentOnline, u_password) VALUES ('" + userName + "', " + headImage
				+ ", 0, '" + password + "');";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// to check username
			ResultSet rs = stmt.executeQuery(String1);
			result = true;
			if (rs.next())
				result = false;
			rs.close();
			// add new data
			if (result)
				result = stmt.execute(String2);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Boolean login(String userName, String password) {
		Boolean result = false;
		// sql statement
		String String1 = "SELECT * FROM " + Database_Name + ".User WHERE userID='" + userName + "';";
		String String2 = "UPDATE " + Database_Name + ".User SET currentOnline=1 WHERE userID='" + userName + "';";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// to get user info
			ResultSet rs = stmt.executeQuery(String1);
			// to check username and password
			if (rs.next())
				result = rs.getString("u_password").equals(password);
			rs.close();
			// change current Online
			if (result)
				stmt.execute(String2);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void logout(String userName) {
		// sql statement
		String String1 = "UPDATE " + Database_Name + ".User SET currentOnline=0 WHERE userID='" + userName + "';";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// change current Online
			stmt.execute(String1);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void changeUserInfo(String userName, String password, int headImage) {
		// sql statement
		String String1 = "UPDATE " + Database_Name + ".User SET u_password='" + password + "', headImage=" + headImage
				+ " WHERE userID='" + userName + "';";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// change info
			stmt.execute(String1);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static UserInfo displayUserInfo(String userName) { // need change //
																// afterward
		UserInfo result = null;
		// sql statement
		String String1 = "SELECT * FROM studiumdatabase.User WHERE userID='" + userName + "';";
		String String2 = "SELECT sentBy, COUNT(UID) AS 'sumPosting' FROM studiumdatabase.Postings WHERE sentBy='"
				+ userName + "' AND replyTo IS NULL;";
		String String3 = "SELECT sentBy, COUNT(UID) AS 'sumReply' FROM studiumdatabase.Postings WHERE sentBy='"
				+ userName + "' AND replyTo IS NOT NULL;";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// read user base info
			ResultSet rs = stmt.executeQuery(String1);
			rs.first();
			String userID = rs.getString("userID");
			String password = rs.getString("u_password");
			int headImage = rs.getInt("headImage");
			Boolean currentOnline = rs.getBoolean("currentOnline");
			rs.close();
			// read sum Posting
			rs = stmt.executeQuery(String2);
			rs.first();
			int sumPosting = rs.getInt("sumPosting");
			rs.close();
			// sum Reply
			rs = stmt.executeQuery(String3);
			rs.first();
			int sumReply = rs.getInt("sumReply");
			rs.close();
			// set result
			result = new UserInfo(userID, password, headImage, currentOnline, sumPosting, sumReply);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Info getUserInfo(String userName) { // need change //
		// afterward
		Info result = null;
		// sql statement
		String String = "SELECT * FROM studiumdatabase.User WHERE userID='" + userName + "';";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// read user base info
			ResultSet rs = stmt.executeQuery(String);
			rs.first();
			String userID = rs.getString("userID");
			int headImage = rs.getInt("headImage");
			Boolean currentOnline = rs.getBoolean("currentOnline");
			rs.close();
			result = new Info(userID, headImage, currentOnline);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	@TargetApi(24)
	public static void sendMessage(String sentBy, String sentTo, String content) {
		// sql statement
		Log.v("send", "1");
		String UID = UUID.randomUUID().toString();
		Log.v("send", "2");
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Log.v("send", "3");
		// String sentTime = sdf.format(new Date());
		String sentTime = getDate();
		Log.v("send", "4");
		String String1 = "INSERT INTO studiumdatabase.Message (UID,sentBy,sentTo,content,sentTime) VALUES ('" + UID
				+ "', '" + sentBy + "', '" + sentTo + "', '" + content + "', '" + sentTime + "');";
		Log.v("send", "5");

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// add message
			stmt.execute(String1);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static List<ChatMsgEntity> displayMessage(String user1, String user2) {
		List<ChatMsgEntity> msgList = new ArrayList<ChatMsgEntity>();
		// sql statement
		String String1 = "SELECT * FROM studiumdatabase.Message WHERE (sentBy='" + user1 + "' AND sentTo='" + user2
				+ "') OR (sentBy='" + user2 + "' AND sentTo='" + user1 + "') ORDER BY sentTime ASC;";
		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// get message
			ResultSet result = stmt.executeQuery(String1);
			// set massage
			while (result.next()) {
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(result.getString("sentTime"));
				String name = result.getString("sentBy");
				entity.setName(name);
				entity.setMsgType(name.equals(user2));
				entity.setMessage(result.getString("content"));
				msgList.add(entity);
				Log.v("ccc", "ooo");
			}
			// close
			result.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Log.v("ccc", msgList.size() + "");
		return msgList;
	}

	@TargetApi(24)
	public static Boolean uploadPostings(String sentBy, String replyTo, String content, int contentType, String title) {
		Boolean r = false;
		// sql statement
		String UID = UUID.randomUUID().toString();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// String sentTime = sdf.format(new Date());
		String sentTime = getDate();
		String to = replyTo == null ? "null" : "'" + replyTo + "'";
		String String1 = "INSERT INTO studiumdatabase.Postings (UID, sentBy, replyTo, content, sentTime, contentType, title) VALUES ('"
				+ UID + "', '" + sentBy + "', " + to + ", '" + content + "', '" + sentTime + "', " + contentType + ", '"
				+ title + "');";
		// Log.v("test",String1);
		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// add posting
			r = stmt.execute(String1);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static void displayPostingDetail(String UID) {
		// sql statement
		String String1 = "SELECT * FROM studiumdatabase.Postings WHERE UID='" + UID + "' OR replyTo='" + UID
				+ "' ORDER BY sentTime ASC;";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// get posting detail
			ResultSet result = stmt.executeQuery(String1);
			// save detail to list
			globalVar.postingDetail.clear();
			while (result.next()) {
				String SentBy = result.getString("sentBy");
				String ReplyTo = result.getString("replyTo");
				String Content = result.getString("content");
				Date sentTime = result.getDate("sentTime");
				int contentType1 = result.getInt("contentType");
				String Title = result.getString("title");

				globalVar.postingDetail
						.add(new PostingInfo(UID, SentBy, ReplyTo, Content, sentTime, contentType1, Title));
			}
			// close
			result.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void deletePostings(String UID) {
		// sql statement
		String String1 = "DELETE * FROM studiumdatabase.Postings WHERE UID='" + UID + "' OR replyTo='" + UID + "';";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// delete posting
			stmt.execute(String1);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void searchOrdisplayPostingsList(String sentBy, int contentType) {
		ResultSet result = null;
		// sql statement
		String String1 = "SELECT * FROM studiumdatabase.Postings WHERE replyTo IS NULL ORDER BY sentTime ASC;";
		String String2 = "SELECT * FROM studiumdatabase.Postings WHERE sentBy='" + sentBy
				+ "' AND replyTo IS NULL ORDER BY sentTime ASC;";
		String String3 = "SELECT * FROM studiumdatabase.Postings WHERE contentType=" + contentType
				+ " AND replyTo IS NULL ORDER BY sentTime ASC;";
		String String4 = "SELECT * FROM studiumdatabase.Postings WHERE sentBy='" + sentBy + "' AND contentType="
				+ contentType + " AND replyTo IS NULL ORDER BY sentTime ASC;";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// search
			if (sentBy == null && contentType == 0) {
				result = stmt.executeQuery(String1);
			} else if (contentType == 0) {
				result = stmt.executeQuery(String2);
			} else if (sentBy == null) {
				result = stmt.executeQuery(String3);
			} else {
				result = stmt.executeQuery(String4);
			}

			globalVar.postingList.clear();
			while (result.next()) {
				String UID = result.getString("UID");
				String SentBy = result.getString("sentBy");
				String ReplyTo = result.getString("replyTo");
				String Content = result.getString("content");
				Date sentTime = result.getDate("sentTime");
				int contentType1 = result.getInt("contentType");
				String Title = result.getString("title");

				globalVar.postingList
						.add(new PostingInfo(UID, SentBy, ReplyTo, Content, sentTime, contentType1, Title));
			}

			// close
			result.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void getFriendList(String user) {
		// sql statement
		String String1 = "SELECT * FROM studiumdatabase.user WHERE userID='" + user + "';";

		List<String> friendNames = new ArrayList<String>();
		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// get friend list
			ResultSet result = stmt.executeQuery(String1);
			result.first();
			friendNames = String2List(result.getString("friendList"));
			// close
			result.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException) 2" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		List<Info> huanCun = new ArrayList<Info>();
		for(int i=0;i<friendNames.size();i++){
			Info info = getUserInfo(friendNames.get(i));
			Log.v("rrr", friendNames.get(i));
			huanCun.add(info);
		}
		globalVar.friendList.clear();
		globalVar.friendList = huanCun;
		
	}

	public static void addFriend(String user, String friend) {
		// sql statement
		Info info = getUserInfo(friend);
		globalVar.friendList.add(info);
		String list = List2String(globalVar.friendList);
		String String1 = "UPDATE studiumdatabase.user SET friendList=CONCAT_WS(',',friendList,'" + friend + "') WHERE `userID`='" + user + "';";

		try {
			// connect to sql server
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(ConnectLink);
			Statement stmt = conn.createStatement();
			Log.v("yzy", "success to connect!");
			// add friend
			stmt.execute(String1);
			// close
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			Log.v("yzy", "fail to connect! (ClassNotFoundException)" + "  " + e.getMessage());
		} catch (SQLException e) {
			Log.v("yzy", "fail to connect! (SQLException)" + "  " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@TargetApi(24)
	private static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}

	private static String List2String(List<Info> nameList) {
		String ListString = nameList.get(0).userID;
		for (int i = 1; i < nameList.size(); i++) {
			ListString = ListString + "," + nameList.get(i).userID;
		}
		return ListString;
	}

	private static List<String> String2List(String nameList) {
		
		if(nameList == null){
			nameList = "";
			Log.v("www", "haha");
		}
		String[] arrary = nameList.split(",");
		List<String> listString = new ArrayList<String>();
		for (int i = 0; i < arrary.length; i++) {
			listString.add(arrary[i]);
		}
		return listString;
	}
}
