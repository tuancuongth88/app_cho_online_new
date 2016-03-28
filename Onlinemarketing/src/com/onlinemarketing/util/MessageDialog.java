package com.onlinemarketing.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;


public class MessageDialog extends Message {

	public MessageDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void showDialog(int id, Drawable icon, String title, String message,
			String strButton1, String strButton2, String strButton3,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle(title);
		alertDialog.setIcon(icon);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		alertDialog.create();
		alertDialog.setNegativeButton(strButton1, listener);
		alertDialog.setNeutralButton(strButton2, listener);
		alertDialog.setPositiveButton(strButton3, listener);
		Dialog dialog = alertDialog.show();
		AlertDialog alertDialog2 = (AlertDialog) dialog;
		alertDialog2.getButton(AlertDialog.BUTTON_POSITIVE).setId(id);
		alertDialog2.getButton(AlertDialog.BUTTON_NEUTRAL).setId(id);
		alertDialog2.getButton(AlertDialog.BUTTON_NEGATIVE).setId(id);
	}
}
