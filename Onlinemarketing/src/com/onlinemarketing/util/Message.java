package com.onlinemarketing.util;

import com.onlinemarketing.config.Constan;

import android.content.Context;
import android.widget.Toast;

public class Message {
	 static Context context = Constan.context;

	public Message(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public static void showMessage(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static void showMessage(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
