package com.onlinemarketing.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import com.lib.Debug;

public class Constan{
	public static Context context;
	
	public static int getIntProperty(String key) {

		return Integer.parseInt(getProperty(key));
	}

	public static String getProperty(String key) {

		Properties properties = new Properties();
		try {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open("constan.properties");
			InputStreamReader inputread = new InputStreamReader(inputStream,
					"UTF-8");
			properties.load(inputread);
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return properties.getProperty(key);
	}
	



}
