package com.example.studium_test;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PostingsActivity extends Activity {

	private static final String TAG = "BroadcastTest";
	private Intent intent;
	static Button addPosting;
	SimpleAdapter simpleAdapter;

	ListView listView;
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postings);

		intent = new Intent(this, BroadcastService.class);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
		
		listView = (ListView) findViewById(R.id.list1);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(arg2);   
                globalVar.titleOnClick=map.get("Title");   
                globalVar.authorOnClick=map.get("Author");  
                globalVar.UIDOnClick = globalVar.postingList.get(arg2).UID;
                
                ComponentName componentname = new ComponentName(PostingsActivity.this, PostingDetailsActivity.class);
				Intent intent = new Intent();
				intent.setComponent(componentname);
				startActivity(intent);
                
			}
			
		});
		
		addPosting = (Button)findViewById(R.id.addPosting);
		addPosting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ComponentName componentname = new ComponentName(PostingsActivity.this, NewPostingActivity.class);
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

	public void createList() {
		new Thread() {
			public void run() {
				DatabaseAPI.searchOrdisplayPostingsList(null, 0);
			}
		}.start();

	}

	public void updateUI(Intent intent) {

		Log.v("hhh", globalVar.postingList.size()+ "");
		
		listItems.clear();
		for (int i = 0; i < globalVar.postingList.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			//listItem.clear();
			listItem.put("Title", globalVar.postingList.get(i).Title);
			listItem.put("Author", globalVar.postingList.get(i).SentBy);
			listItems.add(listItem);
		}
		
		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item,
				new String[] { "Title", "Author" }, new int[] { R.id.tvF, R.id.tvS });
		listView.setAdapter(simpleAdapter);
		
		//simpleAdapter.notifyDataSetChanged();
		
		createList();
		
	}
}
