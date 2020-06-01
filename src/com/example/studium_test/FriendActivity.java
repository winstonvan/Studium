package com.example.studium_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "BroadcastTest";
	private Intent intent;
	
	Button addFriend;
	ListView list_friend;
	EditText addByUsername;
	
	SimpleAdapter simpleAdapter;
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	
	@Override
	  public void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState); 
	    setContentView(R.layout.activity_friend);
	    
	    intent = new Intent(this, BroadcastService.class);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
		
		list_friend = (ListView)findViewById(R.id.list_friend);
		list_friend.setOnItemClickListener(this);
				/*new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,String> map=(HashMap<String,String>)list_friend.getItemAtPosition(arg2);
				globalVar.friendNameOnClick = map.get("friendName");
				
				ComponentName componentname = new ComponentName(FriendActivity.this, MessageActivity.class);
				Intent intent = new Intent();
				intent.setComponent(componentname);
				startActivity(intent);
			}
		}*/
		
		addByUsername = (EditText)findViewById(R.id.addByUsername);
		
		
		addFriend = (Button)findViewById(R.id.addFriend);
		
		addFriend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addFriend();
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

	public void createList() {
		new Thread() {
			public void run() {
				DatabaseAPI.getFriendList(globalVar.currentUser);
			}
		}.start();

	}

	public void updateUI(Intent intent) {
		Log.v("eee", globalVar.friendList.size()+ "");
		
		listItems.clear();
		for (int i = 0; i < globalVar.friendList.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			String status = "";
			//listItem.clear();
			listItem.put("friendName", globalVar.friendList.get(i).userID);
			if(globalVar.friendList.get(i).currentOnline){
				status = "Online";
			}else{
				status = "Offline";
			}
			listItem.put("currentStatus", status);
			
			if(globalVar.friendList.get(i).headImage == 1){
				listItem.put("img", R.drawable.pic1);
			}else if(globalVar.friendList.get(i).headImage == 2){
				listItem.put("img", R.drawable.pic2);
			}else if(globalVar.friendList.get(i).headImage == 3){
				listItem.put("img", R.drawable.pic3);
			}else if(globalVar.friendList.get(i).headImage == 4){
				listItem.put("img", R.drawable.pic4);
			}else{
				listItem.put("img", null);
			}
			
			listItems.add(listItem);
		}
		
		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.friendlist,
				new String[] { "friendName", "currentStatus", "img" }, new int[] { R.id.friendName, R.id.currentStatus, R.id.friendPic });
		list_friend.setAdapter(simpleAdapter);
		
		//simpleAdapter.notifyDataSetChanged();
		
		createList();
		
	}


	
	private void addFriend(){
		final String addFriendByUsername = addByUsername.getText().toString();
		if(addFriendByUsername.length()>0){
			new Thread(){
				public void run(){
					
					//addByUsername.setText(null);
					
					DatabaseAPI.addFriend(globalVar.currentUser, addFriendByUsername);
					DatabaseAPI.addFriend(addFriendByUsername, globalVar.currentUser);
					
				}
			}.start();
		}
		
		addByUsername.setText("");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		HashMap<String,String> map=(HashMap<String,String>)list_friend.getItemAtPosition(arg2);
		globalVar.friendNameOnClick = map.get("friendName");
		
		ComponentName componentname = new ComponentName(FriendActivity.this, MessageActivity.class);
		Intent intent = new Intent();
		intent.setComponent(componentname);
		startActivity(intent);
	}

}
