package com.example.studium_test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NewPostingActivity extends Activity {

	private RadioGroup radioGroup;
	private EditText postingContent,Title_edittext;
	private Button Back,Post;
	
	private String title = "";
	private String content = "";
	
	private int typeId = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newposting);
		
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup);   
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()  
        {  
                public void onCheckedChanged(RadioGroup group, int checkedId) {  
                // checkedId is the RadioButton selected  
                RadioButton rb=(RadioButton)findViewById(checkedId);  
                //rb.getText()
                if(checkedId == R.id.radioButton1){
                	typeId = 1;
                }else if(checkedId == R.id.radioButton2){
                	typeId = 2;
                }else if(checkedId == R.id.radioButton3){
                	typeId = 3;
                }else if(checkedId == R.id.radioButton4){
                	typeId = 4;
                }else if(checkedId == R.id.radioButton5){
                	typeId = 5;
                }
            }  
        });  
        
        postingContent=(EditText)findViewById(R.id.Content_edittext);
        postingContent.setOnTouchListener(new View.OnTouchListener() {
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
        
        
        
        Title_edittext=(EditText)findViewById(R.id.Title_edittext);
        
        
        
        Back=(Button)findViewById(R.id.Back);
        Back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
        Post=(Button)findViewById(R.id.Post);
        Post.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){
					public void run() {
						title = Title_edittext.getText().toString();
						content = postingContent.getText().toString();
						if(DatabaseAPI.uploadPostings(globalVar.currentUser, null, content, typeId, title)){
							toast(getText(R.string.Success));
						}else{
							toast(getText(R.string.Failed));
						}
					}
				}.start();
				
			}
        	
        });
	}
	
	private void toast(CharSequence hint){
		Looper.prepare(); 
	    Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
	    Looper.loop();
	}
	
}
