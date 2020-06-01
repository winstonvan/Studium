package com.example.studium_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PostingDetailsActivity extends Activity {

	private static final String TAG = "BroadcastTest";
	private Intent intent;

	TextView PostingTitle_textview, PostedBy, authorName, postingContent_content;
	ImageView authorPic;
	ListView listView;
	EditText ReplyContent_edittext;
	Button AddReply;

	int authorImage = 0;

	private String replyContent = "";

	SimpleAdapter simpleAdapter;

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posting_details);

		intent = new Intent(this, BroadcastService.class);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));

		PostingTitle_textview = (TextView) findViewById(R.id.PostingTitle_textview);
		PostedBy = (TextView) findViewById(R.id.PostedBy);
		authorName = (TextView) findViewById(R.id.authorName);
		postingContent_content = (TextView) findViewById(R.id.postingContent_content);

		authorPic = (ImageView) findViewById(R.id.authorPic);

		listView = (ListView) findViewById(R.id.listview);

		ReplyContent_edittext = (EditText) findViewById(R.id.ReplyContent_edittext);
		ReplyContent_edittext.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 解决scrollView中嵌套EditText导致不能上下滑动的问题
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_UP:
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}
				return false;
			}
		});

		AddReply = (Button) findViewById(R.id.AddReply);
		AddReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread() {
					public void run() {
						replyContent = ReplyContent_edittext.getText().toString();
						DatabaseAPI.uploadPostings(globalVar.currentUser, globalVar.UIDOnClick, replyContent, 0, null);
					}
				}.start();
				ReplyContent_edittext.setText("");
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

	public void createList2() {
		new Thread() {
			public void run() {
				DatabaseAPI.displayPostingDetail(globalVar.UIDOnClick);
				authorImage = DatabaseAPI.getUserInfo(globalVar.authorOnClick).headImage;
			}
		}.start();

	}

	public void updateUI(Intent intent) {

		listItems.clear();
		// Log.v("fff", globalVar.postingDetail.size()+"");
		if (globalVar.postingDetail.size() > 0) {
			String title = globalVar.postingDetail.get(0).Title;
			String author = globalVar.postingDetail.get(0).SentBy;
			String content = globalVar.postingDetail.get(0).Content;

			// getAuthorInfo();
			if (authorImage == 1) {
				authorPic.setImageResource(R.drawable.pic1);
			} else if (authorImage == 2) {
				authorPic.setImageResource(R.drawable.pic2);
			} else if (authorImage == 3) {
				authorPic.setImageResource(R.drawable.pic3);
			} else if (authorImage == 4) {
				authorPic.setImageResource(R.drawable.pic4);
			} else {

			}

			PostingTitle_textview.setText(title);
			postingContent_content.setText(content);
			PostedBy.setText("Posted by: ");
			authorName.setText(author);
		}

		for (int i = 1; i < globalVar.postingDetail.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("Replier", globalVar.postingDetail.get(i).SentBy);
			listItem.put("contentInfo", globalVar.postingDetail.get(i).Content);
			listItems.add(listItem);
		}

		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.replylist,
				new String[] { "Replier", "contentInfo" }, new int[] { R.id.Replier, R.id.contentInfo });
		listView.setAdapter(simpleAdapter);

		createList2();
	}
}
