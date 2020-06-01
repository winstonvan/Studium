package com.example.studium_test;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ProfileActivity extends Activity{

	TextView userName, currentStatus, sumPosting, sumReply;
	ImageView profilePic;
	Button changeProfile, signOut;
	
	private static final String TAG = "BroadcastTest";
	private Intent intent;
	
	UserInfo userInfo;
	
	@Override
	  public void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState); 
	    setContentView(R.layout.activity_profile);
	    
	    intent = new Intent(this, BroadcastService.class);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
	    
	    userName = (TextView)findViewById(R.id.userName);
	    userName.setText("Username: " + globalVar.currentUser);
	    currentStatus = (TextView)findViewById(R.id.currentStatus);
	    sumPosting = (TextView)findViewById(R.id.sumPosting);
	    sumReply = (TextView)findViewById(R.id.sumReply);
	    
	    profilePic = (ImageView)findViewById(R.id.profilePic);
	    
	    signOut = (Button)findViewById(R.id.signOut);
	    signOut.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(){
					public void run(){
						DatabaseAPI.logout(globalVar.currentUser);
					}
				}.start();
				
				ComponentName componentname = new ComponentName(ProfileActivity.this, LogInActivity.class);
				Intent intent1 = new Intent();
				intent1.setComponent(componentname);
				startActivity(intent1);
			}
	    	
	    });
	    changeProfile = (Button)findViewById(R.id.changeProfile);
	    changeProfile.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ComponentName componentname = new ComponentName(ProfileActivity.this, ChangeProfileActivity.class);
				Intent intent1 = new Intent();
				intent1.setComponent(componentname);
				startActivity(intent1);
			}
	    	
	    });
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateUI(intent);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		stopService(intent);
	}

	public void getInfo() {
		new Thread() {
			public void run() {
				userInfo = DatabaseAPI.displayUserInfo(globalVar.currentUser);
			}
		}.start();

	}

	public void updateUI(Intent intent) {
		
		
		
		if(userInfo != null){
			
			int profileImage = userInfo.headImage;
			if (profileImage == 1) {
				profilePic.setImageResource(R.drawable.pic1);
			} else if (profileImage == 2) {
				profilePic.setImageResource(R.drawable.pic2);
			} else if (profileImage == 3) {
				profilePic.setImageResource(R.drawable.pic3);
			} else if (profileImage == 4) {
				profilePic.setImageResource(R.drawable.pic4);
			} else {

			}
			
			String status = "";
			if (userInfo.currentOnline) {
                status = "Current Status: <font color = 'green'>Online</font>";
            } else {
                status = "Current Status: <font color = 'red'>Offline</font>";
            }
            currentStatus.setText(Html.fromHtml(status));
			
			int sumPosting_num = userInfo.sumPosting;
			sumPosting.setText("Sum Postings: " + sumPosting_num);
			
			int sumReply_num = userInfo.sumReply;
			sumReply.setText("Sum Replies: " + sumReply_num);
			
		}
		
		getInfo();
		
	}

}
