package com.onlinemarketing.activity;

import com.example.onlinemarketing.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.SignInButton;
import com.lib.Debug;
import com.lib.SharedPreferencesUtils;
import com.lib.facebook.LoginFacebook;
import com.onlinemarketing.asystask.LoginRegisterAsystask;
import com.onlinemarketing.config.SystemConfig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends BaseActivity implements OnClickListener {

	EditText txtusername, txtpass;
	Button btnlogin, btnRegister, btnFace, btn_skip;
	boolean loginStatus;
	Dialog objdealog;
	CheckBox chkRemember;
	AlertDialog.Builder mProgressDialog;
	LoginRegisterAsystask account;
	// google
//	SignInButton btngoogle;
//	private PlusClient mPlusClient;
	private int REQUEST_CODE_RESOLVE_ERR = 301;
	private CallbackManager callback = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FacebookSdk.sdkInitialize(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		callback = CallbackManager.Factory.create();
		txtusername = (EditText) findViewById(R.id.txtusername);
		txtpass = (EditText) findViewById(R.id.txtpassword);
		btnlogin = (Button) findViewById(R.id.btnlogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
//		btngoogle = (SignInButton) findViewById(R.id.googlebtn);
		btnFace = (Button) findViewById(R.id.btnFace);
		btn_skip = (Button) findViewById(R.id.btnSkip);
		chkRemember = (CheckBox) findViewById(R.id.chkremember);
		btnlogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);  
//		btngoogle.setOnClickListener(this);
		btnFace.setOnClickListener(this);
		btn_skip.setOnClickListener(this);
		
		account = new LoginRegisterAsystask(txtusername.getText().toString()
				.trim(), txtpass.getText().toString().trim(),
				SystemConfig.device_id, "", "",false,this);
		Debug.e(SystemConfig.device_id);
		/*mPlusClient = new PlusClient.Builder(getApplicationContext(),
				new ConnectionCallbacks() {

					@Override
					public void onDisconnected() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onConnected(Bundle arg0) {
						// TODO Auto-generated method stub
						Person person = mPlusClient.getCurrentPerson();
						Debug.showAlert(LoginActivity.this,
								String.valueOf(person.getDisplayName()));
						Debug.showAlert(LoginActivity.this,
								String.valueOf(person.getId()));
						Debug.showAlert(LoginActivity.this,
								String.valueOf(person.getGender()));
					}
				}, new OnConnectionFailedListener() {

					@Override
					public void onConnectionFailed(ConnectionResult result) {
						// TODO Auto-generated method stub
						if (result.hasResolution()) {

							try {
								result.startResolutionForResult(
										LoginActivity.this,
										REQUEST_CODE_RESOLVE_ERR);
							} catch (SendIntentException e) {
								mPlusClient.disconnect();
								mPlusClient.connect();
							}
						}
					}
				}).build();*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnlogin:
			if (isConnect()) {
				
				boolean checked = chkRemember.isChecked();
				
				new LoginRegisterAsystask(txtusername.getText().toString()
						.trim(), txtpass.getText().toString().trim(),
						SystemConfig.device_id, "", "",checked,this)
						.execute(SystemConfig.statusLogin);
			}
			break;
		case R.id.btnRegister:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.btnSkip:
			if (isConnect()) {
				SharedPreferencesUtils.putString(this, SystemConfig.USER_ID, "");
		        SharedPreferencesUtils.putString(this, SystemConfig.SESSION_ID, "");
				startActivity(new Intent(LoginActivity.this,
						MainActivity.class));
			}
			break;

		/*case R.id.googlebtn:
			if (!mPlusClient.isConnected()) {

				mPlusClient.connect();
				startActivity(new Intent(LoginActivity.this,
						HomePageActivity.class));

			} else if (mPlusClient.isConnected()) {
				{
					mPlusClient.clearDefaultAccount();
					mPlusClient.disconnect();
				}
			}
			break;*/

		case R.id.btnFace:
			LoginFacebook.onActionLoginFacebook(this, callback,
					new FacebookCallback<LoginResult>() {

						@Override
						public void onSuccess(LoginResult result) {
							 Profile profile = Profile.getCurrentProfile();
//							 intent.putExtra(Account.ID, profile.getId());
							 String facebook_id = profile.getId().toString();
							 String name =  profile.getName().toString();
							 Debug.e("name: "+ name);
							 if (isConnect()) {
									
								 loginFacebook_google(facebook_id, "", name);
									
								}
							
						}

						@Override
						public void onError(FacebookException error) {
							Debug.e("Ä�Äƒng nháº­p tháº¥t báº¡i "
									+ error.toString());
						}

						@Override
						public void onCancel() {
							Debug.e("Há»§y Ä‘Äƒng nháº­p");
						}

					});
			break;
		}
	}
	public  void loginFacebook_google(String facebook_id, String google_id, String user_name) {
		new LoginRegisterAsystask(
				SystemConfig.device_id, "", "",facebook_id,"",user_name, LoginActivity.this)
				.execute(SystemConfig.statusfacebook);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callback.onActivityResult(requestCode, resultCode, data);
	}
}
