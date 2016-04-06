package com.onlinemarketing.activity;

import java.io.InputStream;

import com.example.onlinemarketing.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.lib.Debug;
import com.lib.SharedPreferencesUtils;
import com.lib.facebook.LoginFacebook;
import com.onlinemarketing.asystask.LoginRegisterAsystask;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.Output;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity
		implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

	EditText txtusername, txtpass;
	Button btnlogin, btnRegister, btnFace, btn_skip;
	boolean loginStatus;
	Dialog objdealog;
	CheckBox chkRemember;
	AlertDialog.Builder mProgressDialog;
	LoginRegisterAsystask account;
	// google
//	SignInButton btngoogle;
	// private PlusClient mPlusClient;
	private int REQUEST_CODE_RESOLVE_ERR = 301;
	private CallbackManager callback = null;
	TextView txtFogotPass;
	Dialog dialog;
	EditText editErrorReport;
	Button btnOk, btnCancle, btngoogle;
	// cuongntlogin google
	private boolean mSignInClicked;
	private boolean mIntentInProgress;
	private GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;
	private static final int PROFILE_PIC_SIZE = 400;
	private static final int RC_SIGN_IN = 0;
	private ImageView imgProfilePic;
	public static int isChecksignOut;

	static Output out;

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
		btngoogle = (Button) findViewById(R.id.googlebtn);
		btnFace = (Button) findViewById(R.id.btnFace);
		btn_skip = (Button) findViewById(R.id.btnSkip);
		chkRemember = (CheckBox) findViewById(R.id.chkremember);
		txtFogotPass = (TextView) findViewById(R.id.txtFogotPass);
		btnlogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btngoogle.setOnClickListener(this);
		btngoogle.setEnabled(true);
		
		btnFace.setOnClickListener(this);
		btn_skip.setOnClickListener(this);
		txtFogotPass.setOnClickListener(this);
		account = new LoginRegisterAsystask(txtusername.getText().toString().trim(),
				txtpass.getText().toString().trim(), SystemConfig.device_id, "", "", false, this);
		Debug.e(SystemConfig.device_id);
			mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).addApi(Plus.API).addScope(new Scope(Scopes.PROFILE)).build();

	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Lỗi login
	 */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	public void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			isChecksignOut = 2;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnlogin:
			if (isConnect()) {

				boolean checked = chkRemember.isChecked();

				new LoginRegisterAsystask(txtusername.getText().toString().trim(), txtpass.getText().toString().trim(),
						SystemConfig.device_id, "", "", checked, this).execute(SystemConfig.statusLogin);
			}
			break;
		case R.id.btnRegister:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.btnSkip:
			if (isConnect()) {
				SharedPreferencesUtils.putString(this, SystemConfig.USER_ID, "");
				SharedPreferencesUtils.putString(this, SystemConfig.SESSION_ID, "");
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
			break;

		case R.id.googlebtn:
			signInWithGplus();
			break;

		case R.id.btnFace:
			LoginFacebook.onActionLoginFacebook(this, callback, new FacebookCallback<LoginResult>() {

				@Override
				public void onSuccess(LoginResult result) {
					Profile profile = Profile.getCurrentProfile();
					// intent.putExtra(Account.ID, profile.getId());
					String facebook_id = profile.getId().toString();
					String name = profile.getName().toString();
					Debug.e("name: " + name);
					if (isConnect()) {

						loginFacebook_google(facebook_id, "", name, SystemConfig.statusfacebook);

					}

				}

				@Override
				public void onError(FacebookException error) {
					Debug.e("Đăng nhập thất bại" + error.toString());
					 if (error instanceof FacebookAuthorizationException) {
				            if (AccessToken.getCurrentAccessToken() != null) {
				                LoginManager.getInstance().logOut();
				            }
				        }
				}

				@Override
				public void onCancel() {
					Debug.e("Há»§y Ä‘Äƒng nháº­p");
				}

			});
			break;
		case R.id.txtFogotPass:
			dialogForgotPass();
			break;
		}
	}

	public void dialogForgotPass() {
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_forgot_pass);
		editErrorReport = (EditText) dialog.findViewById(R.id.editErrorReport);
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_ErrorReport);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_ErrorReport);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!editErrorReport.getText().toString().isEmpty()) {
					new ForgotPassAsynTask().execute();
					dialog.dismiss();
				} else {
					Debug.showAlert(LoginActivity.this, "Không được để null");
				}
			}
		});
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public class ForgotPassAsynTask extends AsyncTask<Integer, String, Output> {

		JsonProduct jsonProduct;

		@Override
		protected void onPreExecute() {
			jsonProduct = new JsonProduct();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Integer... params) {
			out = jsonProduct.paserForgotPass(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					editErrorReport.getText().toString());
			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				Debug.showAlert(LoginActivity.this, result.getMessage());
			}
			super.onPostExecute(result);
		}
	}

	public void loginFacebook_google(String facebook_id, String google_id, String user_name, int status) {
		if (status == SystemConfig.statusfacebook) {
			new LoginRegisterAsystask(SystemConfig.device_id, "", "", facebook_id, "", user_name, LoginActivity.this)
					.execute(SystemConfig.statusfacebook);
		} else if (status == SystemConfig.statusgoogle) {
			new LoginRegisterAsystask(SystemConfig.device_id, "", "", "", google_id, user_name, LoginActivity.this)
					.execute(SystemConfig.statusgoogle);
		}
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
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	// google
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
			return;
		}

		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Get user's information
		if (isChecksignOut == 2) {
			signOutFromGplus();
		} else if(isChecksignOut == 1){
			getProfileInformation();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}

	/**
	 * lấy thông tin info user's information name, email, profile pic
	 */
	@SuppressWarnings("deprecation")
	private void getProfileInformation() {

		try {

			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				String emailAddr = Plus.AccountApi.getAccountName(mGoogleApiClient);
				Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				String google_id = signedInUser.getId();
				Debug.e("emailAddr: " + emailAddr + "\n signedInUser: " + google_id);
				loginFacebook_google("", google_id, emailAddr, SystemConfig.statusgoogle);

			} else {
				Toast.makeText(getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sign-in into google
	 */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			isChecksignOut = 1;
			mSignInClicked = true;
			resolveSignInError();
		}   
	}

	/**
	 * Sign-out from google
	 */
	public void onActionLogoutGoogle() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Background Async task to load user profile picture from url
	 */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			SystemConfig.logginType = SystemConfig.statusgoogle;
			SystemConfig.avatar = result;
		}
	}
	

}
