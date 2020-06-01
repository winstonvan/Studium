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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchPostingsActivity extends Activity {

	Spinner spinner;
	EditText inputAuthorName;
	Button search;

	private ArrayAdapter<String> adapter = null;
	private static final String[] contentType = { "", "Computing", "Java Programming", "C# Programming",
			"C++Programming", "Android Programming" };

	private int typeId;

	private static final String TAG = "BroadcastTest";
	private Intent intent;

	SimpleAdapter simpleAdapter;

	ListView listView;
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_postings);

		intent = new Intent(this, BroadcastService.class);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));

		globalVar.postingList.clear();

		inputAuthorName = (EditText) findViewById(R.id.inputAuthorName);

		listView = (ListView) findViewById(R.id.list2);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(arg2);   
                globalVar.titleOnClick=map.get("Title");   
                globalVar.authorOnClick=map.get("Author");  
                globalVar.UIDOnClick = globalVar.postingList.get(arg2).UID;
                
                ComponentName componentname = new ComponentName(SearchPostingsActivity.this, PostingDetailsActivity.class);
				Intent intent = new Intent();
				intent.setComponent(componentname);
				startActivity(intent);
                
			}
			
		});

		spinner = (Spinner) findViewById(R.id.spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, contentType);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				String spinner_text = ((TextView) arg1).getText().toString();
				if (spinner_text == "") {
					typeId = 0;
				} else if (spinner_text == "Computing") {
					typeId = 1;
				} else if (spinner_text == "Java Programming") {
					typeId = 2;
				} else if (spinner_text == "C# Programming") {
					typeId = 3;
				} else if (spinner_text == "C++Programming") {
					typeId = 4;
				} else if (spinner_text == "Android Programming") {
					typeId = 5;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				globalVar.postingList.clear();
				final String authorName = inputAuthorName.getText().toString();
				if(authorName.length()>0){
					new Thread() {
						public void run() {
							DatabaseAPI.searchOrdisplayPostingsList(authorName, typeId);
						}
					}.start();
				}else{
					new Thread() {
						public void run() {
							DatabaseAPI.searchOrdisplayPostingsList(null, typeId);
						}
					}.start();
				}
				
				inputAuthorName.setText("");
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

	public void updateUI(Intent intent) {
		listItems.clear();
		for (int i = 0; i < globalVar.postingList.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			// listItem.clear();
			listItem.put("Title", globalVar.postingList.get(i).Title);
			listItem.put("Author", globalVar.postingList.get(i).SentBy);
			listItems.add(listItem);
		}
		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item_search, new String[] { "Title", "Author" },
				new int[] { R.id.tvF_search, R.id.tvS_search });
		listView.setAdapter(simpleAdapter);

		// simpleAdapter.notifyDataSetChanged();
	}

}
