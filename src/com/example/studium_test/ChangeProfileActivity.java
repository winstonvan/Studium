package com.example.studium_test;

import com.example.studium_test.CustomNestRadioGroup.OnCheckedChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeProfileActivity extends Activity implements OnCheckedChangeListener {

	static com.example.studium_test.CustomNestRadioGroup selectNewProfilePicture;

	EditText newPassword_editText;
	Button Back, Apply;

	private int headImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_profile);

		selectNewProfilePicture = (com.example.studium_test.CustomNestRadioGroup) findViewById(
				R.id.selectNewProfilePicture);
		selectNewProfilePicture.setOnCheckedChangeListener(this);

		newPassword_editText = (EditText) findViewById(R.id.newPassword_editText);

		Back = (Button) findViewById(R.id.Back);
		Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		Apply = (Button) findViewById(R.id.Apply);
		Apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String newPassword = newPassword_editText.getText().toString();
				if (newPassword.length() > 0) {
					new Thread() {
						public void run() {
							DatabaseAPI.changeUserInfo(globalVar.currentUser, newPassword, headImage);
						}
					}.start();
				}else{
					new Thread() {
						public void run() {
							DatabaseAPI.changeUserInfo(globalVar.currentUser, globalVar.currentPassword, headImage);
						}
					}.start();
				}

				finish();
			}

		});
	}

	@Override
	public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		group.check(checkedId);
		if (group.getId() == R.id.selectNewProfilePicture) {
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
}
