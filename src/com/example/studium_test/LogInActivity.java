package com.example.studium_test;

import java.sql.SQLException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends Activity {

	static TextView RegisterHere;
	static EditText UserName_edittext, Password_edittext;
	static Button login;
	CheckBox checkBox1;

	int trigger = 0;
	Boolean running = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);

		UserName_edittext = (EditText) findViewById(R.id.UserName_edittext);
		Password_edittext = (EditText) findViewById(R.id.Password_edittext);
		
		Password_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());

		RegisterHere = (TextView) findViewById(R.id.Register);
		RegisterHere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ComponentName componentname = new ComponentName(LogInActivity.this, RegisterActivity.class);
				Intent intent = new Intent();
				intent.setComponent(componentname);
				startActivity(intent);
			}

		});

		login = (Button) findViewById(R.id.Login);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String userName = UserName_edittext.getText().toString();
				final String password = Password_edittext.getText().toString();
				new Thread() {
					public void run() {

							if (DatabaseAPI.login(userName, password)) {
								globalVar.currentUser = userName;
								globalVar.currentPassword = password;
								ComponentName componentname = new ComponentName(LogInActivity.this, MenuActivity.class);
								Intent intent = new Intent();
								intent.setComponent(componentname);
								startActivity(intent);
							}else{
								toast(getText(R.string.Failed));
							}

					};
				}.start();

			}

		});
		
		checkBox1=(CheckBox) findViewById(R.id.checkBox1);  
		  
        checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
              
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
                // TODO Auto-generated method stub  
                if(!isChecked){  
                    //show password       
                	Password_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  
                }else{  
                    //hide password  
                	Password_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());  
                }  
                  
            }  
        });  

	}

	private void toast(CharSequence hint){
		Looper.prepare(); 
	    Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
	    Looper.loop();
	}

}
