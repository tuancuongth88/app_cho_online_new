package com.onlinemarketing.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.lib.SharedPreferencesUtils;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonCategory;
import com.onlinemarketing.json.JsonProfile;
import com.onlinemarketing.object.OutputProduct;
import com.smile.android.gsm.utils.AndroidUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	TimerTask myTimerTask;
	Handler handler = new Handler();
	Timer myTimer = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Debug.e(AndroidUtils.getHashKey(this));
		Debug.e(AndroidUtils.getSHA1(this));
		runCategoryAsystask();
	}
	public void runCategoryAsystask(){
		if (isConnect()) {
			new CategoryAsystask().execute();
		}
	}

	public class CategoryAsystask extends
			AsyncTask<String, String, OutputProduct> {

		String Device_id;
		JsonCategory product;
		JsonProfile profile;
		OutputProduct oOput ;

		@Override
		protected void onPreExecute() {
			product = new JsonCategory();
			profile = new JsonProfile();
			
			

			if (SharedPreferencesUtils.getBoolean(
					SplashActivity.this, SystemConfig.CHECKLOGIN)) {
				SystemConfig.user_id = String
						.valueOf(SharedPreferencesUtils.getString(
								SplashActivity.this,
								SystemConfig.USER_ID));
				SystemConfig.session_id = SharedPreferencesUtils
						.getString(SplashActivity.this,
								SystemConfig.SESSION_ID);
				oOput = profile.paserProfile(SystemConfig.user_id,
						SystemConfig.session_id,
						SystemConfig.device_id,
						SystemConfig.statusProfile);
				SystemConfig.oOputproduct.setProfileDetailVO(oOput.getProfileVO());
				SystemConfig.oOputproduct.setProfileVO(oOput.getProfileVO());
				
		}
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(String... params) {
			try {
				if (isConnect()) {
					oOput = product.paserCategory(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id);
					SystemConfig.oOputproduct.setCategoryVO(oOput.getCategoryVO());
				}
			} catch (Exception e) {
				Debug.e(e.toString());
			}  
			// SystemConfig.oOputproduct.setCategoryVO(list);
			return null;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			if (!SystemConfig.oOputproduct.getCategoryVO().isEmpty()) {
				if (SharedPreferencesUtils.getBoolean(SplashActivity.this,
						SystemConfig.CHECKLOGIN)) {
					SharedPreferencesUtils.getString(SplashActivity.this,
							SystemConfig.USER_ID);
					SharedPreferencesUtils.getString(SplashActivity.this, 
							SystemConfig.SESSION_ID);
					SystemConfig.user_id = String
							.valueOf( SharedPreferencesUtils.getString(
									SplashActivity.this, SystemConfig.USER_ID));
					SystemConfig.session_id = SharedPreferencesUtils.getString( 
							SplashActivity.this, SystemConfig.SESSION_ID);
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					LoginActivity.isChecksignOut = 2;
					Intent intent = new Intent(SplashActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}
	}
}
