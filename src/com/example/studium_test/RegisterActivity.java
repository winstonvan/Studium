package com.example.studium_test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.studium_test.CustomNestRadioGroup;
import com.example.studium_test.CustomNestRadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity implements OnCheckedChangeListener {

	static EditText UserName_edittext, Password_edittext;
	static Button Register, Back;
	static com.example.studium_test.CustomNestRadioGroup selectProfilePicture;

	private int headImage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		UserName_edittext = (EditText) findViewById(R.id.UserName_edittext);
		Password_edittext = (EditText) findViewById(R.id.Password_edittext);
		// Password_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
		// UserName_edittext.getText().toString();

		selectProfilePicture = (com.example.studium_test.CustomNestRadioGroup) findViewById(R.id.selectProfilePicture);
		selectProfilePicture.setOnCheckedChangeListener(this);

		Register = (Button) findViewById(R.id.Register);
		Register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String userName = UserName_edittext.getText().toString();
				final String password = Password_edittext.getText().toString();
				// Toast.makeText(getApplicationContext(), "start",
				// Toast.LENGTH_SHORT).show();
				new Thread() {
					public void run() {
						try {
							DatabaseAPI.createUser(userName, password, headImage);
							ComponentName componentname = new ComponentName(RegisterActivity.this, LogInActivity.class);
							Intent intent = new Intent();
							intent.setComponent(componentname);
							startActivity(intent);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();

			}
		});

		Back = (Button) findViewById(R.id.Back);
		Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ComponentName componentname = new ComponentName(RegisterActivity.this, LogInActivity.class);
				Intent intent = new Intent();
				intent.setComponent(componentname);
				startActivity(intent);
			}

		});

	}

	@Override
	public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		group.check(checkedId);
		if (group.getId() == R.id.selectProfilePicture) {
			if (checkedId == R.id.pic1) {
				Toast.makeText(getApplicationContext(), "01", Toast.LENGTH_SHORT).show();
				headImage = 01;
			} else if (checkedId == R.id.pic2) {
				Toast.makeText(getApplicationContext(), "02", Toast.LENGTH_SHORT).show();
				headImage = 02;
			} else if (checkedId == R.id.pic3) {
				Toast.makeText(getApplicationContext(), "03", Toast.LENGTH_SHORT).show();
				headImage = 03;
			} else if (checkedId == R.id.pic4) {
				Toast.makeText(getApplicationContext(), "04", Toast.LENGTH_SHORT).show();
				headImage = 04;
			}
		}
	}

	private void toast(CharSequence hint) {
		Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
	}
}
