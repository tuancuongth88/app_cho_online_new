package com.onlinemarketing.activity;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.asystask.LoginRegisterAsystask;
import com.onlinemarketing.config.SystemConfig;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	EditText txtusername, txtpass;
	Button register;
	LoginRegisterAsystask account;
	ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		txtusername = (EditText) findViewById(R.id.txtusername);
		txtpass = (EditText) findViewById(R.id.txtpassword);
		register = (Button) findViewById(R.id.btnRegister);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgBack.setOnClickListener(this);
		register.setOnClickListener(this);
		account = new LoginRegisterAsystask(txtusername.getText().toString(), txtpass.getText().toString(),
				SystemConfig.device_id, "", "", false, this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBackTitle:
			this.finish();
			break;
		case R.id.btnRegister:
			if (isConnect()) {
				Debug.e("SystemConfig.device_id: " + SystemConfig.device_id);
				new LoginRegisterAsystask(txtusername.getText().toString(), txtpass.getText().toString(),
						SystemConfig.device_id, "", "", false, this).execute(SystemConfig.statusRegister);
			}
			break;
		}
	}

}
